package gui.debug;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.border.Border;

public class DebugBorderEventListener implements AWTEventListener {
	
	public void eventDispatched(AWTEvent event) {
		
		Object o = event.getSource();
		
		if (o instanceof JComponent) {
			JComponent source = (JComponent) o;
			Border border = source.getBorder();
			
			switch (event.getID()) {
			
			case MouseEvent.MOUSE_ENTERED:
				if (border != null) {
					
					source.setBorder(new DebugBorder(border));
				}
				break;
			case MouseEvent.MOUSE_EXITED:
				if (border != null && border instanceof DebugBorder) {
					source.setBorder(((DebugBorder) border).getDelegate());
				}
				break;
			}
			
		}
		
	}
	
	private static class RectDraw extends JComponent {

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
	
}

//package gui.debug;
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