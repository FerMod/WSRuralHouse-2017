package gui.debug;

import java.awt.AWTEvent;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//public abstract class ConsoleKeyEvent<T> implements KeyEventDispatcher, ConsoleKeyEventDispatcher<T> {
public class ConsoleKeyEventDispatcher implements KeyEventDispatcher {

	//	Class<T> t;
	//
	//	public ConsoleKeyEvent(Class<T> t) {
	//		this.t = t;
	//		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	//	}

	private DebugBorderEventListener debugEventListener;
	private MousePointLabel mousePointLabel;
	private Konami konami;
	private static int count = 0;
	private int millis;

	public ConsoleKeyEventDispatcher() {
		debugEventListener = null;
		addKeyEventDispatcher(this);
		konami = new Konami(new Runnable() { // ^.^		
			@Override
			public void run() {			
				showMessage();
			}
		});
	}

	private void showMessage() {

		String message = null;
		String audioFile = "/img/major_puzzle_solved.wav";
		millis = -1;
		boolean fade = true;

		switch (count) {
		case 0:
			fade = false;
			break;
		case 1:
			message = "You shouldn't be doing this...";
			break;
		case 2:
			message = "I'm warning you...";
			break;
		case 3:
			message = "Ok... For you...\nBut don't do it again...\nIt's a warning.";
			break;
		case 4:
			message = "Now, you should stop... I'm serious...";
			break;
		case 5:
			message = "Last warning... You don't wanna see what can I do...\nDON'T DO THIS AGAIN.";
			break;
		case 6:
			message = "I   d i d   t e l l   y o u,   a n d   y o u   d i d ' t   s t o p . . .";
			audioFile = "/img/major_puzzle_solved_reversed.wav";
			millis = 10000;
			fade = false;
			break;
		}

		Sound sound = new Sound();
		sound.playSound(getClass().getResource(audioFile), millis, fade);	

		if(count == 6) {
			Thread thread = new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						new GlitchImagePanel(Arrays.asList("/img/glitch.gif", "/img/glitched.gif"), millis);
						Thread.sleep(millis);	
						System.exit(666);
					} catch (InterruptedException e) {
					}
				}
			});
			thread.setDaemon(true);
			thread.start();
		}

		if(count != 0) {
			JOptionPane.showOptionDialog(null,
					message,
					null,
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					new String[] {"Accept"},
					"Accept");	
		}

		if(count == 3) {
			try {
				openWebpage(new URI("https://youtu.be/zqqq8uqSDnk?t=1s"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		count++;

	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void addKeyEventDispatcher(KeyEventDispatcher dispatcher) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
	}

	public void removeKeyEventDispatcher(KeyEventDispatcher dispatcher) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
	}

	public void showConsole() {
		ConsoleWindow.setVisible(true);
		System.out.println("[" + this.getClass().getSimpleName() + "]: ConsoleWindow visible");
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() == KeyEvent.KEY_PRESSED) {
			//System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
			konami.checkCode(e.getKeyCode());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_F12:
				if(ConsoleWindow.isVisible()) {
					System.out.println("[" + this.getClass().getSimpleName() + "]: ConsoleWindow hidden");
					ConsoleWindow.setVisible(false);
				} else {
					ConsoleWindow.setVisible(true);
					System.out.println("[" + this.getClass().getSimpleName() + "]: ConsoleWindow visible");
				}
				return true;
			case KeyEvent.VK_F11:
				System.out.println("[" + this.getClass().getSimpleName() + "]: ConsoleWindow disposed");
				ConsoleWindow.dispose();
				return true;
			case KeyEvent.VK_F10:

				Object o = e.getSource();
				if(o instanceof JFrame) {
					if(mousePointLabel == null) {
						JFrame source = (JFrame)o;
						System.out.println(source.toString());
						mousePointLabel = new MousePointLabel(source);	
					}
					//					mousePointLabel.setVisible(true);
					//					Toolkit.getDefaultToolkit().addAWTEventListener(mousePointLabel, AWTEvent.MOUSE_MOTION_EVENT_MASK);

					if(!mousePointLabel.isVisible()) {
						mousePointLabel.setVisible(true);
						Toolkit.getDefaultToolkit().addAWTEventListener(mousePointLabel, AWTEvent.MOUSE_MOTION_EVENT_MASK);			
						System.out.println("[" + this.getClass().getSimpleName() + "]: Mouse coordinates drawing enabled");						
					} else {
						mousePointLabel.setVisible(false);
						Toolkit.getDefaultToolkit().removeAWTEventListener(mousePointLabel);				
						System.out.println("[" + this.getClass().getSimpleName() + "]: Mouse coordinates drawing disabled");						
					}
				}
				return true;
			case KeyEvent.VK_F9:
				if(debugEventListener == null) {
					debugEventListener = new DebugBorderEventListener();					
					Toolkit.getDefaultToolkit().addAWTEventListener(debugEventListener, AWTEvent.MOUSE_MOTION_EVENT_MASK);
					System.out.println("[" + this.getClass().getSimpleName() + "]: Border drawing enabled");
				} else {
					Toolkit.getDefaultToolkit().removeAWTEventListener(debugEventListener);
					debugEventListener = null;
					System.out.println("[" + this.getClass().getSimpleName() + "]: Border drawing disabled");
				}
				return false;
			}
		}
		//The KeyEvent is passed to the next KeyEventDispatcher in the chain
		return false;
	}
}
