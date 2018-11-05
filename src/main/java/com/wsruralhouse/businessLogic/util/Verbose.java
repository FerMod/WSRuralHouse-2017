package com.wsruralhouse.businessLogic.util;

public final class Verbose {

	private static boolean verbose;
	
	private Verbose() {
		verbose = false;
	}

	public static boolean isEnabled() {
		return verbose;
	}

	public static boolean enabled() {
		return verbose = true;
	}
	
	public static void disabled() {
		verbose = false;
	}

}
