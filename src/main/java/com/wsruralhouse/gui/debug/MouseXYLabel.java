package com.wsruralhouse.gui.debug;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/** 
 * Creates a custom Java mouse cursor that shows the x/y coordinates of the mouse as the mouse in the component.
 */
public class MouseXYLabel implements AWTEventListener {

	private JFrame frame;
	private boolean visible;
	private MouseCoordinatesComponent mouseCoordinatesComponent;
	private Cursor cursor;

	public MouseXYLabel(JFrame frame) {
		this.frame = frame;
		mouseCoordinatesComponent = new MouseCoordinatesComponent();
		cursor = new Cursor(Cursor.DEFAULT_CURSOR);
	}

	public boolean isVisible() {
		return visible;		
	}

	public void setVisible(boolean visible) {
		if(visible) {
			frame.setCursor(cursor);
			frame.getRootPane().getLayeredPane().add(mouseCoordinatesComponent, JLayeredPane.DRAG_LAYER);
			mouseCoordinatesComponent.setBounds(0, 0, frame.getWidth(), frame.getHeight());		
			Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_MOTION_EVENT_MASK);
		} else if(frame != null) {
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			frame.getRootPane().getLayeredPane().remove(mouseCoordinatesComponent);
			Toolkit.getDefaultToolkit().removeAWTEventListener(this);	
		}
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public Cursor getCursor() {
		return frame.getCursor();		
	}

	@Override
	public void eventDispatched(AWTEvent event) {

		//Object o = event.getSource();

		if(frame.getMousePosition() != null) {
			mouseCoordinatesComponent.setBounds(0, 0, frame.getWidth(), frame.getHeight());	
			mouseCoordinatesComponent.setPoint(frame.getMousePosition().getLocation().x, frame.getMousePosition().getLocation().y);
		} else {
			mouseCoordinatesComponent.clearComponent();
		}
		mouseCoordinatesComponent.repaint();
	}

	/**
	 * This is the class that draws the x/y coordinates
	 * near the mouse cursor/pointer.
	 */
	private class MouseCoordinatesComponent extends JComponent {

		/**
		 * Generated serial version ID
		 */
		private static final long serialVersionUID = 4052810015763044755L;

		private Point point;

		public MouseCoordinatesComponent() {
			point = new Point(0, 0);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.red);
			System.out.println("[x=" + MouseInfo.getPointerInfo().getLocation().x + ",y=" + MouseInfo.getPointerInfo().getLocation().y + "](" + point.x + "," + point.y + ")");
			String s = "(" + point.x + "," + point.y + ")";
			g.drawString(s, point.x, point.y);
		}

		public void clearComponent() {
			super.getGraphics().clearRect(point.x, point.x, point.x, point.x);
		}

		public void setPoint(int x, int y) {
			point = new Point(x, y);
		}


	}

}
