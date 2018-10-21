package gui.debug;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound extends Thread implements LineListener {

	private static Clip clip;
	private FloatControl gainControl;  // for volume
	private float currentDB;
	private float targetDB;
	private float fadeStep;
	private boolean isFading;

	public Sound() {
		currentDB = 0F;
		targetDB = 0F;
		fadeStep = 0.5F;   // .1 works for applets, 1 is okay for apps
		isFading = false;
	}

	public void loopSound(URL url) {
		play(url, -1, Clip.LOOP_CONTINUOUSLY, false);
	}

	public void loopSound(URL url, int count) {
		play(url, count, -1, false);
	}

	public void loopSound(URL url, boolean fade) {
		play(url, -1, Clip.LOOP_CONTINUOUSLY, fade);
	}

	public void loopSound(URL url, int count, boolean fade) {
		play(url, count, -1, fade);
	}

	public void loopSound(URL url, int endTime, int count, boolean fade) {
		play(url, count, endTime, fade);
	}

	public void playSound(URL url) {
		play(url, 0, -1, false);
	}

	public void playSound(URL url, boolean fade) {
		play(url, 0, -1, fade);
	}

	public void playSound(URL url, int endTime) {
		play(url, 0, endTime, false);
	}

	public void playSound(URL url, int endTime, boolean fade) {
		play(url, 0, endTime, fade);
	}

	private void play(URL url, int count, int endTime, boolean fade) {
		try {
			if(clip != null) {
				if(clip.isRunning()) {
					clip.stop();
				}
				if(clip.isOpen()) {
					clip.stop();
				}
			}

			clip = AudioSystem.getClip();
			// getAudioInputStream() also accepts a File or InputStream
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.addLineListener(this);
			clip.open(ais);
			clip.loop(count);

			if(endTime >= 0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(endTime);
							if(clip.isRunning()) {
								clip.stop();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				thread.setDaemon(true);
				thread.start();
			}


			this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			this.currentDB = gainControl.getValue();

			if(fade) {
				shiftVolumeTo(targetDB);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopSound() {
		if(clip != null) {
			if(clip.isRunning()) {
				clip.stop();
			}
			if(clip.isOpen()) {
				clip.stop();
			}
		}
	}

	/**
	 * Set the volume to a value between 0 and 1.
	 * @param value the volume value. Must be btween 0 and 1
	 */
	public void volume(double value) {
		value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
		try {
			float dB = (float)(Math.log(value)/Math.log(10.0)*20.0);
			gainControl.setValue(dB);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Fade the volume to a new value.  To shift volume while sound is playing,
	 * ie. to simulate motion to or from an object, the volume has to change
	 * smoothly in a short period of time.  Unfortunately this makes an annoying
	 * clicking noise, mostly noticeable in the browser.  I reduce the click
	 * by fading the volume in small increments with delays in between.  This
	 * means that you can't change the volume very quickly.  The fade has to
	 * to take a second or two to prevent clicks.
	 */
	private void shiftVolumeTo(double value) {
		// value is between 0 and 1
		value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
		targetDB = (float)(Math.log(value)/Math.log(10.0)*20.0);
		if (!isFading) {
			Thread thread = new Thread(this);  // start a thread to fade volume
			thread.setDaemon(true);
			thread.start();  // calls run() below
		}
	}

	/**
	 * Run by thread, this will step the volume up or down to a target level.
	 * Applets need fadePerStep=.1 to minimize clicks.
	 * Apps can get away with fadePerStep=1.0 for a faster fade with no clicks.
	 */
	public void run() {
		isFading = true;   // prevent running twice on same sound
		if (currentDB > targetDB) {
			while (currentDB > targetDB) {
				currentDB -= fadeStep;
				gainControl.setValue(currentDB);
				try {		
					Thread.sleep(50);
				} catch (Exception e) {
					//Nothing to catch
				}
			}
		} else if (currentDB < targetDB) {
			while (currentDB < targetDB) {
				currentDB += fadeStep;
				gainControl.setValue(currentDB);
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					//Nothing to catch
				}
			}
		}
		isFading = false;
		currentDB = targetDB;  // now sound is at this volume level
	}

	@Override
	public void update(LineEvent event) {
		if(event.getType().equals(Type.OPEN)) {
		} else if(event.getType().equals(Type.CLOSE)) {
		} else if(event.getType().equals(Type.START)) {
		} else if(event.getType().equals(Type.STOP)) {
		}
	}

}
