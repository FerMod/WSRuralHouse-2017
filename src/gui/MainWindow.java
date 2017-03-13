package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.PopupMenu;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import domain.User.Role;
import gui.components.ui.CustomTabbedPaneUI;

import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1810393566512302281L;

	private JPanel contentPane;
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
						frame.setVisible(true);
					} else {
						System.exit(1);
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
		String[] options = new String[] {"GUEST", "CLIENT", "OWNER", "ADMIN", "SUPER_ADMIN"};
		int response = JOptionPane.showOptionDialog(null, "Open the window as: ", "Choose option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		switch (response) {
		case 0:
			return Role.GUEST;
		case 1:
			return Role.CLIENT;
		case 2:
			return Role.OWNER;
		case 3:
			return Role.ADMIN;
		case 4:
			return Role.SUPER_ADMIN;
		default:
			return null;
		}
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(Role role) {
		this.role = role;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 465));
		setSize(700, 565);
		getRolePanel(role);
		//setJMenuBar(getRoleMenuBar());
		//frame.pack();
		pack();

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

		setLocationRelativeTo(null);

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
		case GUEST:
			//return new GuestMainPanel();
			return null;
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
