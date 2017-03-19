package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsoleKeyListener implements KeyListener {

//	private T t;
//
//	public ConsoleKeyListener(T t) {
//		this.t = t;
//		
//	}
	

	public ConsoleKeyListener() {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("EVENTO: " + e.getKeyChar());
		switch (e.getKeyCode()) {
		case KeyEvent.VK_F12:
			if(ConsoleWindow.isVisible()) {
				ConsoleWindow.setVisible(false);
			} else {
//				System.out.println("## " + t.getClass().getName()  + " ##");
				ConsoleWindow.setVisible(true);
			}
			break;
		case KeyEvent.VK_ESCAPE:
			ConsoleWindow.dispose();
			break;
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}