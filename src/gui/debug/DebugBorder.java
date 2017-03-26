package gui.debug;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class DebugBorder implements Border {
	
	private Border border;

	public DebugBorder(Border b) {
		this.border = b;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		border.paintBorder(c, g, x, y, width, height);
		Insets insets = border.getBorderInsets(c);
		Color layerColor = new Color(1.0f, 0.0f, 0.0f, 0.85f);
		g.setColor(layerColor);
		// top
		g.fillRect(x, y, width, insets.top);
		// left
		g.fillRect(x, y + insets.top, insets.left, height - insets.bottom - insets.top);
		// bottom
		g.fillRect(x, y + height - insets.bottom, width, insets.bottom);
		// right
		g.fillRect(x + width - insets.right, y + insets.top, insets.right, height - insets.bottom - insets.top);
	}

	public Insets getBorderInsets(Component c) {
		return border.getBorderInsets(c);
	}

	public boolean isBorderOpaque() {
		return border.isBorderOpaque();
	}

	public Border getDelegate() {
		return border;
	}
	
}
