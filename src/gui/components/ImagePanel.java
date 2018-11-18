package gui.components;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -886982507395266888L;

	/**
	 * When no image is provided in the constructor this one will be used
	 */
	public static final ImageIcon NO_IMAGE = new ImageIcon(ImagePanel.class.getResource("/img/icons/loading.gif"));

	private ImageIcon imageIcon = NO_IMAGE;

	public ImagePanel() {
	}

	public ImagePanel(ImageIcon imageIcon) {
		this(imageIcon, null);
	}

	public ImagePanel(ImageIcon imageIcon, Dimension dimension) {
		setImage(imageIcon);
		setPreferredSize(dimension);
	}

	public ImagePanel(String pathname) {
		this(pathname, null);
	}

	public ImagePanel(String pathname, Dimension dimension) {
		setImage(pathname);
		setPreferredSize(dimension);
	}

	public ImagePanel(URL url) {		
		this(url, null);
	}

	public ImagePanel(URL url, Dimension dimension) {		
		setImage(url);		
		setPreferredSize(dimension);
	}

	public ImageIcon getImage() {
		return imageIcon;
	}

	public void setImage(String pathname) {
		if(imageIcon != null) {
			imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource(pathname)));
		} else {
			imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(pathname)));
		}
	}

	public void setImage(URL url) {
		if(imageIcon != null) {
			imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(url));
		} else {
			imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(url));
		}
	}

	public void setImage(ImageIcon imageIcon) {		
		if(imageIcon != null) {
			imageIcon.setImage(imageIcon.getImage());
		} else {
			this.imageIcon = imageIcon;
		}
	}

	public void setImageSize(Dimension dimension) {
		imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_DEFAULT));
	}

	public Dimension getImageSize() {
		return new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		return getImageSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(imageIcon != null) {
			int x = (this.getWidth() - imageIcon.getIconWidth()) / 2;
			int y = (this.getHeight() - imageIcon.getIconHeight()) / 2;
			g.drawImage(imageIcon.getImage(), x, y, this);  // See javadoc for more info about the parameters    
		}
	}

}
