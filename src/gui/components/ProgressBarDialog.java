package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import gui.components.ui.ProgressCircleUI;

public class ProgressBarDialog extends JDialog implements ActionListener, PropertyChangeListener {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -1394815540100018936L;

	private JProgressBar progressBar;
	private JButton cancelButton, startButton;
	//	private JTextArea taskOutput;
	private Task task; 
	private boolean loopProgress = false;
	private ProgressCircleUI progressCircleUI;

	/**
	 * Create the GUI and show it. As with all GUI code, this must run
	 * on the event-dispatching thread.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		JDialog dialog = new ProgressBarDialog();
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setUndecorated(false);
		//		//Create and set up the content pane.
		//		JComponent newContentPane = new ProgressBarDemo();
		//		newContentPane.setOpaque(true); //content panes must be opaque
		//		frame.setContentPane(newContentPane);

		//Display the window.
		dialog.setSize(new Dimension(400, 600));
		dialog.setLocationRelativeTo(null); 
		dialog.setVisible(true);
	}

	public ProgressBarDialog() {
		super();

		//Create the demo's UI.
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setDoubleBuffered(true);
		progressCircleUI = new ProgressCircleUI(Color.GRAY, true);
		progressBar.setUI(progressCircleUI);

		//		taskOutput = new JTextArea(5, 20);
		//		taskOutput.setMargin(new Insets(5,5,5,5));
		//		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		panel.setLayout(new GridLayout(4, 4, 10, 10));
		panel.add(startButton);
		panel.add(cancelButton);
		panel.add(progressBar);


		this.add(BorderLayout.CENTER, progressBar);
		this.add(BorderLayout.NORTH, new JLabel("Progress..."));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


		add(panel, BorderLayout.PAGE_START);
		//		add(new JScrollPane(taskOutput), BorderLayout.CENTER);

		//Instances of javax.swing.SwingWorker are not, so we create new instances as needed.
		setLoopProgress(true);
	}

	public boolean isLoopProgress() {
		return loopProgress;
	}

	public void setLoopProgress(boolean loopProgress) {
		this.loopProgress = loopProgress;
	}

	/**
	 * Invoked when the user presses the button.
	 */
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand() == "start") {
			startButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//Instances of javax.swing.SwingWorker are not reusuable, so we create new instances as needed.
			task = new Task();
			task.addPropertyChangeListener(this);
			task.execute();
		} else if(evt.getActionCommand() == "cancel") {
			dispose();			
		}
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			//			taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
		} 
	}

	class Task extends SwingWorker<Void, Void> {

		private Optional<String> taskName = Optional.empty();
		private long startTime = System.nanoTime();
		private long elapsedTime = System.nanoTime() - startTime;
//		private int progress;

		public Task() {
		}

		public Task(String taskName) {
			this.taskName = Optional.of(taskName);
		}

		public Optional<String> getTaskName() {
			return taskName;
		}

		public long getElapsedTime() {
			return elapsedTime;
		}

		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			//			Random random = new Random();
			//			progress = 0;
			//Initialize progress property.
			setProgress(0);
			while (!limitReached()) {
				//Sleep for up to one second.
				try {
					Thread.sleep(80);
				} catch (InterruptedException ignore) {}
				//Make random progress.
				//				setProgress(Math.min(progress++, 100));

				setProgress(getProgress()+1);
			}
			return null;
		}

		private synchronized boolean limitReached() {
			if(getProgress() >= 100) {
				if(!loopProgress) {
					return true;
				} else {				
					setProgress(0);

				}
			}
			return false;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			//			Toolkit.getDefaultToolkit().beep();
			cancelButton.setEnabled(true);
			setCursor(null); //turn off the wait cursor
			//			taskOutput.append("Done!\n");
		}
	}

}
