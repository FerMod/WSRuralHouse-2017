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

import javax.swing.JFrame;

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

	public ConsoleKeyEventDispatcher() {
		debugEventListener = null;
		addKeyEventDispatcher(this);
		konami = new Konami(new Runnable() {			
			@Override
			public void run() {
				try {
					openWebpage(new URI("https://youtu.be/zqqq8uqSDnk?t=1s"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		//Konamis.setEnabled(true); // ^.^
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
