package businessLogic.util;

import java.util.concurrent.TimeUnit;

/**
 * The <code>Timer</code> class contains methods to start, finish and retrieve the time.
 * <p>It can be instantiated and use multiple timers at once.
 * 
 * @author Ferran Tudela
 *
 */
public class Timer {

	private long startTime;
	private long finishTime;
	private boolean running;
	
	public Timer() {
		startTime = 0;
		finishTime = 0;
		running = false;
	}

	/**
	 * Start the timer
	 */
	public void startTimer() {
		startTime = System.nanoTime();
		running = true;
	}	

	/**
	 * Stop the timer
	 */
	public void stopTimer() {
		finishTime = System.nanoTime() - startTime;
		running = false;
	}

	/**
	 * Get the time string with the format "h m s"
	 * 
	 * @return the time formated
	 */
	public String getFormattedCurrentTime() {

		long currentTime = System.nanoTime() - startTime;

		long hours = TimeUnit.NANOSECONDS.toHours(currentTime);
		long minutes = TimeUnit.NANOSECONDS.toMinutes(currentTime);
		long seconds = TimeUnit.NANOSECONDS.toSeconds(currentTime);
		long milliseconds = TimeUnit.NANOSECONDS.toMillis(currentTime);
		
		return String.format("%d h  %d m  %d s  %d ms",
				hours,
				minutes - TimeUnit.HOURS.toMinutes(hours),
				seconds - TimeUnit.MINUTES.toSeconds(minutes),
				milliseconds - TimeUnit.SECONDS.toMillis(seconds));
	}

	/**
	 * Get the current time in nanosecond
	 * @return time in nanoseconds
	 */
	public long getCurrentNanoTime() {
		return System.nanoTime() - startTime;
	}

	/**
	 * Get the current time in milliseconds
	 * @return time in milliseconds
	 */
	public long getCurrentMillisTime() {
		return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
	}

	/**
	 * Return the running state of the timer
	 * @return <strong>true</strong> if is running, <strong>false</strong> otherwise
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Get the time string with the format <i>"h  m  s"</i>.<br>
	 * If the timer is still running it will stop it.
	 * @return the time formated
	 */
	public String getFormattedFinishTime() {

		stopTimer();

		long hours = TimeUnit.NANOSECONDS.toHours(finishTime);
		long minutes = TimeUnit.NANOSECONDS.toMinutes(finishTime);
		long seconds = TimeUnit.NANOSECONDS.toSeconds(finishTime);
		long milliseconds = TimeUnit.NANOSECONDS.toMillis(finishTime);
		
		return String.format("%d h  %d m  %d s  %d ms",
				hours,
				minutes - TimeUnit.HOURS.toMinutes(hours),
				seconds - TimeUnit.MINUTES.toSeconds(minutes),
				milliseconds - TimeUnit.SECONDS.toMillis(seconds));
	}


	/**
	 * Get the finish time in nanosecond<p>
	 * If the timer is still running it will stop it
	 * @return time in nanoseconds
	 */
	public long getFinishNanoTime() {
		stopTimer();
		return finishTime;
	}

	/**
	 * Get the finish time in milliseconds
	 * If the timer is still running it will stop it
	 * @return time in milliseconds
	 */
	public long getFinishMillisTime() {
		stopTimer();
		return TimeUnit.NANOSECONDS.toMillis(finishTime);
	}


}
