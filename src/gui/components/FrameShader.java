package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class FrameShader {
	
	/*
	 * TODO: Block events in GlassPane.
	 * http://www.java2s.com/Code/Java/Swing-JFC/Showhowaglasspanecanbeusedtoblockmouseandkeyevents.htm
	 * https://docs.oracle.com/javase/tutorial/uiswing/components/rootpane.html
	 */

	private boolean enabled;
	private Component oldGlassPane;
	private JFrame frame;

	public FrameShader(JFrame frame) {
		this.frame = frame;
		enabled = false;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JFrame getFrame() {
		return frame;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if(enabled) {
			oldGlassPane = frame.getRootPane().getGlassPane();
			frame.getRootPane().setGlassPane(new ShadedGlassPane());		
		} else {
			frame.getRootPane().setGlassPane(oldGlassPane);
		}
		frame.getRootPane().getGlassPane().setVisible(enabled);
	}

	private class ShadedGlassPane extends JComponent {		
		/**
		 * Generated serial version ID
		 */
		private static final long serialVersionUID = 1610697073529560891L;

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, getWidth(), getHeight());
			super.paintComponent(g);
		}
	}

}
