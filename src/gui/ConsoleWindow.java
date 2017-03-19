package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public final class ConsoleWindow {

	private static JFrame frame;
	private static CapturePane capturePane;

	//private final Class<T> type;
	//private Method[] declaredMethods;

	public static void main(String[] args) {
		new ConsoleWindow();
	}

	//TODO remove
	//Constructor header. 
	//	public ConsoleOutput(Class<T> type) {
	//
	//		this.type = type;

	private ConsoleWindow() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(frame == null) {
					createAndShowGui();
//					new ConsoleKeyListener(frame);
				}
			}            
		});
	}

	private static void createAndShowGui() {

		//ConsoleOutput consoleOutput = new ConsoleOutput();

		frame = new JFrame("Console");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		capturePane = new CapturePane();
		frame.setContentPane(capturePane); 
		//frame.pack();
		frame.setLocationByPlatform(true);
		frame.setMinimumSize(new Dimension(228, 39));
		frame.setSize(650, 350);
		//frame.setLocationRelativeTo(null);	

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

	public static boolean isVisible() {
		if(frame != null) {
			return frame.isVisible();
		} else {
			return false;
		}
	}

	public static void setVisible(boolean b) {
		if(b) {
			if(frame != null) {
				frame.setVisible(b);
			} else {
				createAndShowGui();
			}
		}
	}

	public static void dispose() {
		if(frame != null) {
			frame.dispose();
		}
	}

	private static class CapturePane extends JPanel implements Consumer {

		private static final long serialVersionUID = 6513396748219028375L;

		private JTextPane output;

		public CapturePane() {
			super(new BorderLayout());
			output = new JTextPane() {
				private static final long serialVersionUID = -595836354069640799L;
				// Override getScrollableTracksViewportWidth to preserve the full width of the text
				@Override
				public boolean getScrollableTracksViewportWidth() {
					Component parent = getParent();
					ComponentUI ui = getUI();

					return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
				}
			};
			output.setEditable(false);
			output.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			//add(output);
			JScrollPane scrollPane = new JScrollPane(output);
			add(scrollPane);
			//setLayout(new BorderLayout());


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
				aset = sc.addAttribute(aset, StyleConstants.Size, 12);
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

	}

	public interface Consumer {

		public void appendText(String text);

		public void appendText(String text, Color color);  

	}

	public static class StreamCapturer extends OutputStream {

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

