package businessLogic.util;

public final class Verbose {

	private static boolean verbose;
	
	private Verbose() {
		verbose = false;
	}

	public static boolean isEnabled() {
		return verbose;
	}

	public static void enable() {
		verbose = true;
	}
	
	public static void disable() {
		verbose = false;
	}

}
