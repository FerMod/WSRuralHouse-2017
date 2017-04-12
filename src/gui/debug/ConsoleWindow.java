package gui.debug;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

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
	private static String windowTitle = "Console Output";

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

		frame = new JFrame(windowTitle);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		CapturePane capturePane = new CapturePane();
		frame.setContentPane(capturePane.getContentPanel()); 
		frame.setLocationByPlatform(true);
		frame.setMinimumSize(new Dimension(272, 39));
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

	public static void setTitle(String title) {
		windowTitle = title;
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

	private static class CapturePane implements Consumer {

		private JPanel panel;
		private JTextPane output;
		private JScrollPane scrollPane;

		public CapturePane() {

			panel = new JPanel(new BorderLayout());

			output = new OutputTextPane();

			output.setEditable(false);
			output.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			
			panel.add(output);
			scrollPane = new JScrollPane(panel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		}

		public Container getContentPanel() {
			return scrollPane;
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
				aset = sc.addAttribute(aset, StyleConstants.Size, 16);
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

