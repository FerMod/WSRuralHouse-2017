package gui.components.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ProgressCircleUI extends BasicProgressBarUI {

	private Color progressColor, backgroundColor;
	private Font font;

	private boolean drawBackground;
	private boolean loopProgress = false;

	public ProgressCircleUI(Color progressColor, boolean drawBackground) {	
		this(progressColor,	drawBackground, false);
	}

	public ProgressCircleUI(Color progressColor, boolean drawBackground, boolean loopProgress) {	
		this(progressColor,
				(Color)LookAndFeel.getDesktopPropertyValue("ProgressBar.foreground", new Color(221, 221, 221)),
				(Font)LookAndFeel.getDesktopPropertyValue("ProgressBar.font", new Font("Dialog.bold", 1, 12)),
				drawBackground,
				loopProgress);
	}

	public ProgressCircleUI() {	
		this((Color)LookAndFeel.getDesktopPropertyValue("ProgressBar.background", new Color(0, 0, 255)));
	}

	public ProgressCircleUI(Color progressColor) {
		this(progressColor, (Color)LookAndFeel.getDesktopPropertyValue("ProgressBar.foreground", new Color(221, 221, 221)));
	}

	public ProgressCircleUI(Color progressColor, Color backgroundColor) {
		this(progressColor, backgroundColor, (Font)LookAndFeel.getDesktopPropertyValue("ProgressBar.font", new Font("Dialog.bold", 1, 12)));
	}

	public ProgressCircleUI(Color progressColor, Color backgroundColor, Font font) {
		this(progressColor, backgroundColor, font, true, false);
	}

	private ProgressCircleUI(Color progressColor, Color backgroundColor, Font font, boolean drawBackground, boolean loopProgress) {
		this.progressColor = progressColor;
		this.backgroundColor = backgroundColor;
		this.font = font;
		this.drawBackground = drawBackground;
		this.loopProgress = loopProgress;
	}

	public Color getProgressColor() {
		return progressColor;
	}

	public void setProgressColor(Color progressColor) {
		this.progressColor = progressColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}


	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Dimension dimension = super.getPreferredSize(component);
		int size = Math.max(dimension.width, dimension.height); //Take the biggest dimension
		dimension.setSize(size, size);  // Make it square
		return dimension;
	}


	@Override
	public synchronized void paint(Graphics g, JComponent c) {

		if(limitReached()) {
			swapColors();
		}

		// area for border
		Insets insets = progressBar.getInsets();

		int width  = progressBar.getWidth()  - insets.right - insets.left;
		int height = progressBar.getHeight() - insets.top - insets.bottom;


		if (width > 0 && height > 0) {
			// draw the cells
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(progressBar.getForeground());

			final double degree = 360 * progressBar.getPercentComplete();
			final double size = Math.min(width, height);
			final double x = insets.left + width  * .5;
			final double y = insets.top  + height * .5;
			final double ellipseSize = size * .5;
			final double arcSize = ellipseSize * .5; //or - 20;

			final Shape inner = new Ellipse2D.Double(x - arcSize, y - arcSize, arcSize * 2, arcSize * 2);
			final Shape sector;
			if(loopProgress) {
				sector = new Arc2D.Double(x - ellipseSize, y - ellipseSize, size, size, 90 - degree, degree*-1, Arc2D.PIE);
			} else {
				sector = new Arc2D.Double(x - ellipseSize, y - ellipseSize, size, size, 90 - degree, degree, Arc2D.PIE);
			}

			final Area foreground = new Area(sector);
			final Area hole = new Area(inner);

			foreground.subtract(hole);

			if(!drawBackground) {

				// No background

				foreground.subtract(hole);

				g2.setPaint(progressColor);
				g2.fill(foreground);
				g2.dispose();

			} else {

				// Background

				final Shape outer  = new Ellipse2D.Double(x - ellipseSize, y - ellipseSize, size, size);

				final Area background = new Area(outer);

				background.subtract(hole);

				// draw background track
				g2.setPaint(backgroundColor);
				g2.fill(background);

				// draw progress track
				g2.setPaint(progressColor);
				g2.fill(foreground);
				g2.dispose();

			}

			// Deal with possible text painting
			if (progressBar.isStringPainted()) {
				progressBar.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), (int)size/6));
				paintString(g, insets.left, insets.top, width, height, 0, insets);
			}
		}

	}

	private boolean limitReached() {
		if(progressBar.getValue() == 100) {
			if(loopProgress) {
				progressBar.setValue(0);
			} else {
				return true;
			}
		}
		return false;
	}

	public synchronized void swapColors() {
		Color temp = progressColor;
		progressColor = backgroundColor;
		backgroundColor = temp;
	}

}
