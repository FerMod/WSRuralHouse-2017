package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import domain.AbstractUser.Role;
import gui.components.ui.CustomTabbedPaneUI;
import gui.debug.ConsoleKeyEventDispatcher;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1810393566512302281L;

	private JPanel contentPane;
	@SuppressWarnings("unused")
	private Role role;
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Role role = getWindowRole();
					if(role != null) {
						MainWindow frame = new MainWindow(role);
//						frame.addKeyListener(
						//new ConsoleKeyEvent<>(this.getClass());
						new ConsoleKeyEventDispatcher();
						frame.setFocusable(true);			
						frame.setVisible(true); 
					} else {
						System.exit(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Only to debug the windows
	 * @return the chosen role
	 */
	private static Role getWindowRole() {
		Role[] options = new Role[] {Role.CLIENT, Role.OWNER, Role.ADMIN, Role.SUPER_ADMIN};
		Role response = (Role)JOptionPane.showInputDialog(null, "Open the window as: ", "Choose option", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return response;
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(Role role) {

		//ConsoleWindow.setVisible(true);

		this.role = role;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRolePanel(role);
		//setJMenuBar(getRoleMenuBar());


		contentPane = new JPanel();
		//		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setUI(new CustomTabbedPaneUI());
		//contentPane.add(tabbedPane);
		tabbedPane.addTab("Rural Houses", getRolePanel(role));
		tabbedPane.addTab("Maybe another pane?", new TextArea("Yeh awesome... another pane..."));
		tabbedPane.addTab("Profile", new TextArea("Profile goes here"));
		tabbedPane.addTab("", new TextArea("Inifinite posibilities...\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nmaybe(?)"));
		tabbedPane.addTab("Log Out", new TextArea(""));
		//		tabbedPane.addMouseMotionListener(new MouseMotionListener() {
		//			@Override
		//			public void mouseDragged(MouseEvent e) {}
		//			@Override
		//			public void mouseMoved(MouseEvent e){
		//				adjustCursor(e);
		//			}
		//		});

		contentPane.add(tabbedPane);

		//setMinimumSize(new Dimension(600, 365));
		setSize(760, 400);
		//pack();
		validate();
		setLocationRelativeTo(null);

//		addComponentListener(new ComponentListener() {
//			@Override
//			public void componentShown(ComponentEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//			@Override
//			public void componentResized(ComponentEvent e) {
//				System.out.println(MainWindow.class.getName()+"[Width: " + getWidth() + ", Height" + getHeight() + "]");
//			}
//			@Override
//			public void componentMoved(ComponentEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//			@Override
//			public void componentHidden(ComponentEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});

	}

	//	private void adjustCursor(MouseEvent e) {
	//
	//		TabbedPaneUI ui = tabbedPane.getUI();
	//
	//		int index = ui.tabForCoordinate(tabbedPane, e.getX(), e.getY());
	//
	//		if (index >= 0) {
	//			tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	//		} else {
	//			tabbedPane.setCursor(null);
	//		}
	//
	//	}

	public JPanel getRolePanel(Role role) {
		switch (role) {
		case CLIENT:
			return new ClientMainPanel();
		case OWNER:
			return new OwnerMainPanel();
		case ADMIN:
			//return new AdminMainPanel();
			return null;
		case SUPER_ADMIN:
			//return new SuperAdminMainPanel();
			return null;
		default:
			//[TODO]: Throw exception when the user role content pane is not defined 
			System.exit(1);
			return null;
		}
	}

	@SuppressWarnings("unused")
	private JMenuBar getRoleMenuBar() {
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("A Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");

		//a group of JMenuItems
		menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menu.add(menuItem);

		menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menu.add(menuItem);

		//a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		//a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		//a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		submenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		submenu.add(menuItem);
		menu.add(submenu);

		//Build second menu in the menu bar.
		menu = new JMenu("Another Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
		menuBar.add(menu);

		return menuBar;
	}

}
