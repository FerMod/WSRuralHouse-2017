package gui.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;

public final class ConsoleWindow {

	private static JFrame frame;
	private static String frameTitle = "Console Output";
	private static CloseOperation defaultCloseOperation = CloseOperation.HIDE_ON_CLOSE;
	private static CapturePane capturePane;

	public enum CloseOperation {
		HIDE_ON_CLOSE(JFrame.HIDE_ON_CLOSE), 
		DISPOSE_ON_CLOSE(JFrame.DISPOSE_ON_CLOSE),
		EXIT_ON_CLOSE(JFrame.EXIT_ON_CLOSE),
		DO_NOTHING_ON_CLOSE(JFrame.DO_NOTHING_ON_CLOSE);

		private final int operation;

		private CloseOperation(int operation) {
			this.operation = operation;
		}

		public int getValue() {
			return this.operation;
		}

	}

	public static void main(String[] args) {
		new ConsoleWindow();
	}

	private ConsoleWindow() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGui();
			}            
		});
	}

	private void createAndShowGui() {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		frame = new JFrame(frameTitle);
		frame.setDefaultCloseOperation(defaultCloseOperation.getValue());
		capturePane = new CapturePane();
		frame.setContentPane(capturePane.getContentPanel()); 
		frame.setLocationByPlatform(true);
		//frame.setMinimumSize(new Dimension(272, 39));
		frame.setSize(650, 350);
		frame.setFocusable(true);

		System.setOut(new PrintStream(new StreamCapturer(capturePane, System.out)));
		System.setErr(new PrintStream(new StreamCapturer(Color.RED, capturePane, System.err)));

		frame.setVisible(true);

		/*
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Test...");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);

		//a submenu
		declaredMethods = type.getClass().getDeclaredMethods();
		menuItem = new JMenuItem("Method");
		menuItem.addActionListener(new ActionListener() {						
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < declaredMethods.length; i++) {
					System.out.println(declaredMethods[i].toString());
				}
				Method method = (Method)JOptionPane.showInputDialog(null, "Choose the method to test: ", "Choose method", JOptionPane.QUESTION_MESSAGE, null, declaredMethods, declaredMethods[0]);
				//						for (int i = 0; i < declaredMethods.length; i++) {
				//							method = declaredMethods[i];
				//						}

				if(method != null) {
					Class<?>[] paramTypes = method.getParameterTypes();
					Object[] params = new Object[method.getParameterCount()];
					for (int i = 0; i < params.length ; i++) {
						String input = JOptionPane.showInputDialog("Enter " + paramTypes[i].getName() + ": ");
						params[i] = input;

					}

					try {
						method.invoke(params);
					} catch (IllegalArgumentException ex) {
					} catch (IllegalAccessException ex) {
					} catch (InvocationTargetException ex) {
					}

				}
			}
		});
		menu.add(menuItem);
		menu.addSeparator();

		menuBar.add(menu);
		setJMenuBar(menuBar);
		 */		
	}

	public static void showInBrowse(File file) {
		showInBrowse(file.toURI());
	}

	public static void showInBrowse(URL url) {
		try {
			showInBrowse(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void showInBrowse(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported()? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			showInConsole(uri);
		}
	}

	public static void showInConsole(File file) {
		showInConsole(file.toURI());
	}

	public static void showInConsole(URI uri) {
		try {
			showFile(uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void showInConsole(URL url) {
		showFile(url);
	}

	public static void showFile(URL url) {
		try {
			capturePane.getTextPane().setPage(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isVisible() {
		if(frame != null) {
			return frame.isVisible();
		} else {
			return false;
		}
	}

	public static void setVisible(boolean b) {
		if(frame != null) {
			//the frame exists, and will show (b = true) or hide (b = false) de window
			frame.setVisible(b);
		} else if(b) {
			//The frame does not exist, and
			new ConsoleWindow();
		}
	}

	public static void dispose() {
		if(frame != null) {
			frame.dispose();
		}
	}

	public static String getTitle() {
		return frameTitle;
	}

	public static void setTitle(String title) {
		frameTitle = title;
	}

	/**
	 * Sets the operation that will happen by default when
	 * the user initiates a "close" on this frame.
	 * You must specify one of the following choices:
	 * <br><br>
	 * <ul>
	 * <li><code>DO_NOTHING_ON_CLOSE</code>
	 * (defined in <code>WindowConstants</code>):
	 * Don't do anything; require the
	 * program to handle the operation in the <code>windowClosing</code>
	 * method of a registered <code>WindowListener</code> object.
	 *
	 * <li><code>HIDE_ON_CLOSE</code>
	 * (defined in <code>WindowConstants</code>):
	 * Automatically hide the frame after
	 * invoking any registered <code>WindowListener</code>
	 * objects.
	 *
	 * <li><code>DISPOSE_ON_CLOSE</code>
	 * (defined in <code>WindowConstants</code>):
	 * Automatically hide and dispose the
	 * frame after invoking any registered <code>WindowListener</code>
	 * objects.
	 *
	 * <li><code>EXIT_ON_CLOSE</code>
	 * (defined in <code>JFrame</code>):
	 * Exit the application using the <code>System</code>
	 * <code>exit</code> method.  Use this only in applications.
	 * </ul>
	 * <p>
	 * The value is set to <code>HIDE_ON_CLOSE</code> by default. Changes
	 * to the value of this property cause the firing of a property
	 * change event, with property name "defaultCloseOperation".
	 * <p>
	 * <b>Note</b>: When the last displayable window within the
	 * Java virtual machine (VM) is disposed of, the VM may
	 * terminate.  See <a href="../../java/awt/doc-files/AWTThreadIssues.html">
	 * AWT Threading Issues</a> for more information.
	 *
	 * @param operation the operation which should be performed when the
	 *        user closes the frame
	 */
	public static void setDefaultCloseOperation(CloseOperation operation) {
		defaultCloseOperation = operation;
	}

	public static CloseOperation getDefaultCloseOperation() {
		return defaultCloseOperation;
	}

	public static void restartApplication(Class<?> applicationClass) {

		try {

			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			final File currentJar = new File(applicationClass.getProtectionDomain().getCodeSource().getLocation().toURI());

			System.out.println(applicationClass + "");
			System.out.println(javaBin);
			System.out.println(currentJar);

			if(currentJar.getName().endsWith(".jar")) {
				final ArrayList<String> command = new ArrayList<String>();
				command.add(javaBin);
				command.add("-jar");
				command.add(currentJar.getPath());

				final ProcessBuilder builder = new ProcessBuilder(command);
				builder.start();
				System.exit(0);
			}

		} catch (URISyntaxException  e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static class CapturePane implements Consumer {

		private JPanel panel;
		private JTextPane output;
		private JScrollPane scrollPane;

		public CapturePane() {

			panel = new JPanel(new BorderLayout());

			output = new OutputTextPane();

			output.setEditable(false);
			//output.setHighlighter(null); //Disable text selection
			output.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

			panel.add(output);
			scrollPane = new JScrollPane(panel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);

			output.setComponentPopupMenu(getPopupMenu());

		}

		private JPopupMenu getPopupMenu() {

			JPopupMenu popupMenu = null;

			if(output.getHighlighter() != null) {

				popupMenu = new JPopupMenu();
				ImageIcon icon = null;

				// Cut
				Action cut = new DefaultEditorKit.CutAction();
				cut.putValue(Action.NAME, "Cut");
				cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));

				try {
					icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/cut.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				cut.putValue(Action.SMALL_ICON, icon);
				cut.setEnabled(output.isEditable());

				popupMenu.add(cut);


				// Copy
				Action copy = new DefaultEditorKit.CopyAction();
				copy.putValue(Action.NAME, "Copy");
				copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
				
				try {
					icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/copy.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}

				copy.putValue(Action.SMALL_ICON, icon);

				popupMenu.add(copy);
				

				// Paste
				Action paste = new DefaultEditorKit.PasteAction();
				paste.putValue(Action.NAME, "Paste");
				paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));

				try {
					icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/paste.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				paste.putValue(Action.SMALL_ICON, icon);
				paste.setEnabled(output.isEditable());
				
				popupMenu.add(paste);


				// Separator
				popupMenu.addSeparator();


				// Select All
				Action selectAll = new SelectAllAction();
				selectAll.putValue(Action.NAME, "SelectAll");
				selectAll.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));

				JMenuItem selectAllMenuItem = new JMenuItem(selectAll);
				selectAllMenuItem.setHorizontalTextPosition(SwingConstants.LEFT);
				selectAllMenuItem.setHorizontalAlignment(SwingConstants.LEFT);

				popupMenu.add(selectAllMenuItem);


			}

			return popupMenu;
		}

		public static class SelectAllAction extends TextAction {
			/**
			 * Generated serial version ID
			 */
			private static final long serialVersionUID = -2451448648221545271L;

			public SelectAllAction() {
				super("Select All");
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextComponent component = getFocusedComponent();
				if(component != null) {
					component.selectAll();
					component.requestFocusInWindow();
				}
			}

		}

		public Container getContentPanel() {
			return scrollPane;
		}

		public JTextPane getTextPane() {
			return output;
		}

		@Override
		public void appendText(final String text) {
			appendText(text, Color.BLACK);
		}

		@Override
		public void appendText(final String text, final Color color) {
			if (EventQueue.isDispatchThread()) {
				StyledDocument doc = output.getStyledDocument();
				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

				aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
				aset = sc.addAttribute(aset, StyleConstants.Size, new JEditorPane().getFont().getSize());
				aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
				aset = sc.addAttribute(aset, StyleConstants.Foreground, color);

				try {
					doc.insertString(doc.getLength(), text, aset);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

			} else {

				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						appendText(text, color);
					}
				});

			}
		}

		private class OutputTextPane extends JTextPane {

			/**
			 * Generated serial version ID
			 */
			private static final long serialVersionUID = 8387122820900506959L;

			// Override getScrollableTracksViewportWidth to preserve the full width of the text
			@Override
			public boolean getScrollableTracksViewportWidth() {
				Component parent = getParent();
				ComponentUI ui = getUI();
				return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
			}

		}
	}

	public interface Consumer {

		public void appendText(String text);

		public void appendText(String text, Color color);  

	}

	private static class StreamCapturer extends OutputStream {

		private StringBuilder buffer;
		private String prefix;
		private Color color;
		private Consumer consumer;
		private PrintStream old;

		public StreamCapturer(Consumer consumer, PrintStream old) {
			this("", Color.BLACK, consumer, old);
		}

		public StreamCapturer(Color color, Consumer consumer, PrintStream old) {
			this("", color, consumer, old);
		}

		@SuppressWarnings("unused")
		public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
			this(prefix, Color.BLACK, consumer, old);
		}

		public StreamCapturer(String prefix, Color color, Consumer consumer, PrintStream old) {
			this.prefix = prefix;
			this.color = color;
			buffer = new StringBuilder(128);
			buffer.append(prefix);			 
			this.old = old;
			this.consumer = consumer;
		}

		@Override
		public void write(int b) throws IOException {
			char c = (char) b;
			String value = Character.toString(c);
			buffer.append(value);
			if (value.equals("\n")) {
				consumer.appendText(buffer.toString(), color);
				buffer.delete(0, buffer.length());
				buffer.append(prefix);
			}
			old.print(c);
		}    

	}

}

