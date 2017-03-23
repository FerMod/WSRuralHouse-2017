package gui.debug;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

//public abstract class ConsoleKeyEvent<T> implements KeyEventDispatcher, ConsoleKeyEventDispatcher<T> {
public class ConsoleKeyEventDispatcher implements KeyEventDispatcher {

	//	Class<T> t;
	//
	//	public ConsoleKeyEvent(Class<T> t) {
	//		this.t = t;
	//		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	//	}

	public ConsoleKeyEventDispatcher() {
		addKeyEventDispatcher(this);
	}

	public void addKeyEventDispatcher(KeyEventDispatcher dispatcher) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
	}

	public void removeKeyEventDispatcher(KeyEventDispatcher dispatcher) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() == KeyEvent.KEY_PRESSED) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_F12:
				if(ConsoleWindow.isVisible()) {
					System.out.println("[" + this.getClass().getName() + "]: ConsoleWindow hidden");
					ConsoleWindow.setVisible(false);
				} else {
					ConsoleWindow.setVisible(true);
					System.out.println("[" + this.getClass().getName() + "]: ConsoleWindow visible");
				}
				return true;
			case KeyEvent.VK_F11:
				System.out.println("[" + this.getClass().getName() + "]: ConsoleWindow disposed");
				ConsoleWindow.dispose();
				return true;
			}
			return false;
		}
		//The KeyEvent is passed to the next KeyEventDispatcher in the chain
		return false;
	}
}
