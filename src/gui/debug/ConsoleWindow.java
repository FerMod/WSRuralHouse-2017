package gui.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;

import gui.components.RightClickMenu;

public class ConsoleWindow extends JFrame {

	/**
	 *  Generated serial version ID
	 */
	private static final long serialVersionUID = -2968340110042327344L;

	private CapturePane capturePane;

	public static void main(String[] args) {

		ConsoleWindow consoleWindow = new ConsoleWindow();

		consoleWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		consoleWindow.setVisible(true);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*
					 *	┌─────────────────────┒
					 *	│ Press 'ESC' to exit ┃
					 *	┕━━━━━━━━━━━━━━━━━━━━━┛
					 */
					//					System.out.println("┌─────────────────────┒");
					//					System.out.println("│ Press 'ESC' to exit ┃");	
					//					System.out.println("┕━━━━━━━━━━━━━━━━━━━━━┛");
					System.out.println(consoleWindow.wrapTextInBox("Press 'ESC' to exit"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {					
			@Override
			public void run() {
				System.out.println("Testing ConsoleWindow");
			}
		}, Calendar.getInstance().getTime(), 1000);

		consoleWindow.addKeyListener(new KeyListener() {			
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}				
			}
		});

		consoleWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				timer.cancel();
			}
		});

	}

	public String wrapTextInBox(String text) {

		char topLeftCorner = 0x250C; 		// ┌
		char topHorizontal = 0x2500; 		// ─
		char topRightCorner = 0x2512; 		// ┒
		char leftVertical = 0x2502;			// │
		char rightVertical = 0x2503; 		// ┃
		char bottomLeftCorner = 0x2515;		// ┕
		char bottomHorizontal = 0x2501;		// ━
		char bottomRightCorner = 0x251B;	// ┛

		// create a string made up of n copies of s
		// String.join("", Collections.nCopies(n, s))

		StringBuilder sb = new StringBuilder();
		sb.append(Character.toString((char)topLeftCorner));
		sb.append(String.join("", Collections.nCopies(text.length()+2, Character.toString((char)topHorizontal))));
		sb.append(Character.toString((char)topRightCorner));

		sb.append(System.getProperty("line.separator"));

		sb.append(Character.toString((char)leftVertical));
		sb.append(" " + text + " ");
		sb.append(Character.toString((char)rightVertical));

		sb.append(System.getProperty("line.separator"));

		sb.append(Character.toString((char)bottomLeftCorner));
		sb.append(String.join("", Collections.nCopies(text.length()+2, Character.toString((char)bottomHorizontal))));
		sb.append(Character.toString((char)bottomRightCorner));

		sb.append(System.getProperty("line.separator"));

		return sb.toString();		
	}

	public ConsoleWindow() {
		this("Console Output");
	}

	public ConsoleWindow(String title) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setTitle(title);
					initComponents();
					initOutputStreams();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initComponents() {
		capturePane = new CapturePane();
		setContentPane(capturePane.getContentPanel()); 
		setFocusable(true);
		//frame.setMinimumSize(new Dimension(272, 39));
		setSize(650, 350);
		setLocationRelativeTo(null);

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

	private void initOutputStreams() {
		setOutStream(new StreamCapturer(capturePane, System.out));
		setErrStream(new StreamCapturer(Color.RED, capturePane, System.err));				
	}

	public void setInStream(InputStream inputStream) {
		System.setIn(inputStream);
	}

	public void setOutStream(OutputStream outputStream) {
		System.setOut(new PrintStream(outputStream));
	}

	public void setErrStream(OutputStream outputStream) {
		System.setErr(new PrintStream(outputStream));
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	public void showInBrowse(File file) {
		showInBrowse(file.toURI());
	}

	public void showInBrowse(URL url) {
		try {
			showInBrowse(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void showInBrowse(URI uri) {
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

	public void showInConsole(File file) {
		showInConsole(file.toURI());
	}

	public void showInConsole(URI uri) {
		try {
			showFile(uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void showInConsole(URL url) {
		showFile(url);
	}

	public void showFile(URL url) {
		try {
			capturePane.getTextPane().setPage(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restartApplication(Class<?> applicationClass) {

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
			//output.setComponentPopupMenu(getPopupMenu());
			RightClickMenu rightClickMenu = new RightClickMenu(output);
			output.setComponentPopupMenu(rightClickMenu);
			output.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			DefaultCaret caret = (DefaultCaret) output.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			output.setCaret(caret);
			panel.add(output);
			scrollPane = new JScrollPane(panel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		}

		/*
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
		 */

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
					// Charset.forName(Charset.defaultCharset().name()).encode(text);

					//					byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
					//					doc.insertString(doc.getLength(), new String(bytes, Charset.forName("UTF-8")), aset);
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
			char c = (char)b;
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

