package com.wsruralhouse.gui.debug;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class DebugBorderEventListener implements AWTEventListener{
	
	private static JComponent source;

	public void eventDispatched(AWTEvent event) {

		Object o = event.getSource();
		System.out.println(o.toString());
		if (o instanceof JComponent) {

			source = (JComponent) o;
			source.getRootPane().add(new Canvas());
			System.out.println(MouseInfo.getPointerInfo().getLocation());			


			Border border = source.getBorder();		
			
			//Compound borders
			Border compound;
			Border redline = BorderFactory.createLineBorder(Color.red);

			//Add a red outline to the frame.
			compound = BorderFactory.createCompoundBorder(redline, border);
			source.setBorder(compound);

			//Add a title to the red-outlined frame.
			compound = BorderFactory.createTitledBorder(
					compound,
					source.getClass().getName(),
					TitledBorder.CENTER,
					TitledBorder.BELOW_BOTTOM);
			source.setBorder(compound);

		}

	}

	class Canvas extends JComponent {

		private static final long serialVersionUID = -3392450644639136003L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawRect (getWidth(), getHeight(), 30, 30);  
		}

	}

	@SuppressWarnings("unused")
	private static class RectDraw extends JComponent {

		private static final long serialVersionUID = 8599029427375702998L;

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);  
			g.drawRect(230,80,10,10);  
			g.setColor(Color.RED);  
			//g.fillRect(230,80,10,10);  

		}

		public Dimension getPreferredSize() {
			return getPreferredSize(); // appropriate constants
		}

	}

	public static Component findComponentUnderGlassPaneAt(Point p, Component top) {

		Component c = null;

		if (top.isShowing()) {
			if (top instanceof RootPaneContainer)
				c = ((RootPaneContainer) top).getLayeredPane().findComponentAt(SwingUtilities.convertPoint(top, p, ((RootPaneContainer) top).getLayeredPane()));
			else
				c = ((Container) top).findComponentAt(p);
		}

		return c;
	}

}

//package com.wsruralhouse.gui.debug;
//import java.awt.AWTEvent;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Image;
//import java.awt.MouseInfo;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.Shape;
//import java.awt.event.AWTEventListener;
//import java.awt.event.MouseEvent;
//import java.awt.image.ImageObserver;
//import java.text.AttributedCharacterIterator;
//
//import javax.swing.JComponent;
//import javax.swing.RootPaneContainer;
//import javax.swing.SwingUtilities;
//import javax.swing.border.Border;
//
//public class DebugBorderEventListener implements AWTEventListener {
//
//	public void eventDispatched(AWTEvent event) {
//
//		Object o = event.getSource();
//
//		JComponent source = (JComponent) o;
//		Border border = source.getBorder();
//
//		Point point = MouseInfo.getPointerInfo().getLocation();
//		System.out.println(point);
//		Component c = findComponentUnderGlassPaneAt(point, source);
//		RectDraw rect = new RectDraw();
//		rect.paint();
//
//
//
//		//		if (border != null && border instanceof DebugBorder) {
//		//			source.setBorder(((DebugBorder) border).getDelegate());
//		//		} else if (border != null) {
//		//			source.setBorder(new DebugBorder(border));
//		//		}
//
//	}
//
//	public static Component findComponentUnderGlassPaneAt(Point p, Component top) {
//
//		Component c = null;
//
//		if (top.isShowing()) {
//			if (top instanceof RootPaneContainer) {
//				c = ((RootPaneContainer) top).getLayeredPane().findComponentAt(
//						SwingUtilities.convertPoint(top, p, ((RootPaneContainer) top).getLayeredPane()));
//			} else {
//				c = ((Container) top).findComponentAt(p);
//			}
//		}
//
//		return c;
//	}
//
//	private static class RectDraw extends JComponent {
//
//		protected void paintComponent(Graphics g) {
//			super.paintComponent(g);  
//			g.drawRect(230,80,10,10);  
//			g.setColor(Color.RED);  
//			//g.fillRect(230,80,10,10);  
//		}
//
//		public Dimension getPreferredSize() {
//			return new Dimension(1, 10); // appropriate constants
//		}
//
//	}
//
//}