package gui.debug;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GlitchImagePanel extends JPanel {

	private static final long serialVersionUID = 2179562467773096093L;

	private final List<Image> image;

	public static void main(String[] args) {
		new GlitchImagePanel(Arrays.asList("/img/glitch.gif", "/img/glitched.gif"));
	}

	public GlitchImagePanel(List<String> resource) {
		super();
		JFrame frame = new JFrame();
		
		image = new ArrayList<Image>(resource.size()+1);
		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			image.add(capture);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		for (String img : resource) {
			image.add(frame.getToolkit().createImage(getClass().getResource(img)));
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setContentPane(this);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		//		frame.setBackground(new Color(Color.TRANSLUCENT, true));
		frame.setIconImage(null);
		frame.setVisible(true);

		//		hideToSystemTray(frame);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					synchronized (Thread.currentThread()) {
						frame.dispose();
					}
				} catch (InterruptedException e) {
				}				
			}
		});
		thread.setDaemon(true);
		thread.start();		
	}
	static int total = 0;
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Image img : image) {
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);		
		}
	}

	//	public void hideToSystemTray(JFrame frame) {
	//
	//		if(SystemTray.isSupported()){
	//			System.out.println("system tray supported");
	//			tray = SystemTray.getSystemTray();
	//
	//			Image image = Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
	//			ActionListener exitListener=new ActionListener() {
	//				public void actionPerformed(ActionEvent e) {
	//					System.out.println("Exiting....");
	//					System.exit(0);
	//				}
	//			};
	//			PopupMenu popup=new PopupMenu();
	//			MenuItem defaultItem=new MenuItem("Exit");
	//			defaultItem.addActionListener(exitListener);
	//			popup.add(defaultItem);
	//			defaultItem=new MenuItem("Open");
	//			defaultItem.addActionListener(new ActionListener() {
	//				public void actionPerformed(ActionEvent e) {
	//					frame.setVisible(true);
	//					frame.setExtendedState(JFrame.NORMAL);
	//				}
	//			});
	//			popup.add(defaultItem);
	//			trayIcon=new TrayIcon(image, "SystemTray Demo", popup);
	//			trayIcon.setImageAutoSize(true);
	//		}else{
	//			System.out.println("system tray not supported");
	//		}
	//		frame.addWindowStateListener(new WindowStateListener() {
	//			public void windowStateChanged(WindowEvent e) {
	//				if(e.getNewState() == JFrame.ICONIFIED){
	//					try {
	//						tray.add(trayIcon);
	//						setVisible(false);
	//						System.out.println("added to SystemTray");
	//					} catch (AWTException ex) {
	//						System.out.println("unable to add to tray");
	//					}
	//				}
	//				if(e.getNewState()==7){
	//					try{
	//						tray.add(trayIcon);
	//						setVisible(false);
	//						System.out.println("added to SystemTray");
	//					}catch(AWTException ex){
	//						System.out.println("unable to add to system tray");
	//					}
	//				}
	//				if(e.getNewState() == JFrame.MAXIMIZED_BOTH){
	//					tray.remove(trayIcon);
	//					setVisible(true);
	//					System.out.println("Tray icon removed");
	//				}
	//				if(e.getNewState() == JFrame.NORMAL){
	//					tray.remove(trayIcon);
	//					setVisible(true);
	//					System.out.println("Tray icon removed");
	//				}
	//			}
	//		});
	//
	//	}

}