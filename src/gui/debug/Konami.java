package gui.debug;

import java.awt.event.KeyEvent;

public class Konami {

	private static int[] CODE = {KeyEvent.VK_UP, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_B, KeyEvent.VK_A};

	private final Runnable runnable;
	private int index;

	public Konami(Runnable runnable) {
		this.runnable = runnable;
	}

	public void checkCode(int code) {
		
		if (code == CODE[index]) {
			index++;
		} else {
			index = 0;
		}
		
		if (index == CODE.length) {
			index = 0;
			runnable.run();
		}
		
	}
	
}
