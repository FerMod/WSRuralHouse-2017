package gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class DebugWindow extends JFrame {

	private static final long serialVersionUID = -6589446765161096036L;
	
	public static void main(String[] args) {
        new DebugWindow();
    }

	/**
	 * Create the frame.
	 */
	public DebugWindow() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException ex) {
				} catch (InstantiationException ex) {
				} catch (IllegalAccessException ex) {
				} catch (UnsupportedLookAndFeelException ex) {
				}

				CapturePane capturePane = new CapturePane();
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setLayout(new BorderLayout());
				add(capturePane);
				setSize(650, 350);
				setLocationRelativeTo(null);
				setVisible(true);

				PrintStream ps = System.out;
				System.setOut(new PrintStream(new StreamCapturer("SYTEM.OUT", capturePane, ps)));

				ps = System.err;
				System.setErr(new PrintStream(new StreamCapturer("SYTEM.ERR", capturePane, ps)));

				System.out.println("Hi! Im normal text.");
				System.out.println("Im in this TextArea, but I also appear in the console!");
				System.err.println("Hi! Im error!");
			}            
		});

	}

	public class CapturePane extends JPanel implements Consumer {

		private static final long serialVersionUID = 6513396748219028375L;

		private JTextArea output;

		public CapturePane() {
			setLayout(new BorderLayout());
			output = new JTextArea();
			output.setEditable(false);
			output.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			add(new JScrollPane(output));
		}

		@Override
		public void appendText(final String text) {
			if (EventQueue.isDispatchThread()) {
				output.append(text);
				output.setCaretPosition(output.getText().length());
			} else {

				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						appendText(text);
					}
				});

			}
		}    
	}

	public interface Consumer {        
		public void appendText(String text);        
	}

	public class StreamCapturer extends OutputStream {

		private StringBuilder buffer;
		private String prefix;
		private Consumer consumer;
		private PrintStream old;

		public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
			this.prefix = prefix;
			buffer = new StringBuilder(128);
			buffer.append("[").append(prefix).append("] ");
			this.old = old;
			this.consumer = consumer;
		}

		@Override
		public void write(int b) throws IOException {
			char c = (char) b;
			String value = Character.toString(c);
			buffer.append(value);
			if (value.equals("\n")) {
				consumer.appendText(buffer.toString());
				buffer.delete(0, buffer.length());
				buffer.append("[").append(prefix).append("]: ");
			}
			old.print(c);
		}    

	}  

}

