package com.wsruralhouse.gui.debug;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class MousePointLabel implements AWTEventListener {

	private boolean visible;
	private JFrame frame;
	private MouseCoordinatesComponent mouseCoordinatesComponent;
	private Cursor oldCursor;

	@SuppressWarnings("unused")
	static private Component glassPane;

	public MousePointLabel(JFrame frame) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//glassPane = new GlassPane(frame);
				//frame.getRootPane().setGlassPane(glassPane);
				visible = false;
				glassPane = frame.getRootPane().getGlassPane();
				mouseCoordinatesComponent = new MouseCoordinatesComponent();
				//glassPane.setVisible(visible = true);
			}            
		});	
	}

//	public GlassPane getGlassPane() {
//		return glassPane;
//	}

	@Override
	public void eventDispatched(AWTEvent event) {

		Object o = event.getSource();

		if(o instanceof JFrame) {
			JFrame source = (JFrame)o;
			System.out.println(source.toString());
			if(frame != source) {
				new MousePointLabel(source);
			}
		}

		//		if (myGlassPane == null) {
		//			//new MousePointLabel();
		//		}
		//	
//		Object o = event.getSource();

		if (o instanceof JComponent) {

			if (event.getID() == MouseEvent.MOUSE_MOVED && visible) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				//mouseCoordinatesComponent.setLocation(MouseInfo.getPointerInfo().getLocation());
				mouseCoordinatesComponent.setX(p.x);
				mouseCoordinatesComponent.setY(p.x);
				mouseCoordinatesComponent.repaint();
			}

		}

	}

//	/**
//	 * We have to provide our own glass pane so that it can paint.
//	 */
//	class GlassPane extends JComponent implements MouseMotionListener {
//
//		private static final long serialVersionUID = 8727511601030079941L;
//
//		private JFrame frame;
//		private Point point;
//		private CBListener listener;
//
//		//		public void itemStateChanged(ItemEvent e) {
//		//			setVisible(e.getStateChange() == ItemEvent.MOUSE_MOTION_EVENT_MASK && visible);
//		//		}
//
//		public GlassPane(JFrame frame) {
//			this.frame = frame;
//			enableMouseListener();
//			//setVisible(true);
//		}
//
//		public void enableMouseListener() {
//			if(listener != null) {
//				listener = new CBListener(this, frame);
//				frame.getRootPane().setGlassPane(this);
//				frame.addMouseListener(listener);
//				frame.addMouseMotionListener(listener);	
//			}
//		}
//
//		public void disableMouseListener() {
//			if(listener != null) {
//				frame.removeMouseListener(listener);
//				frame.removeMouseMotionListener(listener);	
//			}
//		}
//
//
//		protected void paintComponent(Graphics g) {
//			if (point != null) {
//				g.setColor(Color.red);
//				g.fillOval(point.x - 10, point.y - 10, 20, 20);
//
//				super.paintComponent(g);		
//				String s = "("+ point.x + ", " + point.y +")";
//				System.out.println(s);
//				g.setColor(Color.red);
//				g.drawString(s, point.x, point.y);
//			}
//		}
//
//		@Override
//		public void mouseDragged(MouseEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void mouseMoved(MouseEvent e) {
//			setVisible(true);
//
//		}
//
//		public void setPoint(Point p) {
//			point = p;
//		}
//
//		public JFrame getFrame() {
//			return frame;
//		}
//
//
//
//	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if(visible) {
			if(mouseCoordinatesComponent == null) {
				mouseCoordinatesComponent = new MouseCoordinatesComponent();
			}

			// add my component to the DRAG_LAYER of the layered pane (JLayeredPane)
			JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
			layeredPane.add(mouseCoordinatesComponent, JLayeredPane.DRAG_LAYER);
			mouseCoordinatesComponent.setBounds(0, 0, frame.getWidth(), frame.getHeight());

			//			// add a mouse motion listener, and update my custom mouse cursor with the x/y coordinates as the user moves the mouse
			//			frame.addMouseMotionListener(this);

			oldCursor = frame.getCursor();

			// make the cursor a crosshair shape
			frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

		} else {
			//			frame.removeMouseMotionListener(this);
			frame.remove(mouseCoordinatesComponent);
			frame.setCursor(oldCursor);
		}
	}

	public boolean isVisible() {
		return visible;
	}

//	/**
//	 * Listen for all events that our check box is likely to be
//	 * interested in.  Redispatch them to the check box.
//	 */
//	class CBListener extends MouseInputAdapter {
//
//		Toolkit toolkit;
//		Component glassPane;
//		Container contentPane;
//
//		public CBListener(Component glassPane, Container contentPane) {
//			toolkit = Toolkit.getDefaultToolkit();
//			this.glassPane = glassPane;
//			this.contentPane = contentPane;
//		}
//
//		public void mouseMoved(MouseEvent e) {
//			redispatchMouseEvent(e, true);
//		}
//
//		public void mouseDragged(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		public void mouseClicked(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		public void mouseEntered(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		public void mouseExited(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		public void mousePressed(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		public void mouseReleased(MouseEvent e) {
//			redispatchMouseEvent(e, false);
//		}
//
//		//A basic implementation of redispatching events.
//		private void redispatchMouseEvent(MouseEvent e, boolean repaint) {
//			
//			Point glassPanePoint = e.getPoint();
//			Container container = contentPane;
//			Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
//			if (containerPoint.y < 0) { //we're not in the content pane
//				//The mouse event is over non-system window 
//				//decorations, such as the ones provided by
//				//the Java look and feel.
//				//Could handle specially.
//			} else {
//				//The mouse event is probably over the content pane.
//				//Find out exactly which component it's over.  
//				Component component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
//
//				if (component != null) {
//					//Forward events over the check box.
//					Point componentPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
//					component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), componentPoint.x, componentPoint.y, e.getClickCount(), e.isPopupTrigger()));
//				}
//
//			}
//
//			//Update the glass pane if requested.
//			if (repaint) {
//				//glassPane.setPoint(glassPanePoint);
//				glassPane.repaint();
//			}
//
//		}
//
//	}

	/**
	 * This is the class that draws the x/y coordinates
	 * near the mouse cursor/pointer.
	 */
	class MouseCoordinatesComponent extends JComponent {

		private static final long serialVersionUID = -6706894989699395062L;

		private int x;
		private int y;

		public MouseCoordinatesComponent() {
			this.setBackground(Color.blue);
		}

		// use the x/y coordinates to update the mouse cursor text/label
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);		
			String s = "("+ x + ", " + y +")";
			System.out.println(s);
			g.setColor(Color.red);
			g.drawString(s, x, y);
		}

		@Override
		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		@Override
		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

}

