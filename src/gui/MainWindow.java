package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;
import dataAccess.DataAccess;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import exceptions.DuplicatedEntityException;
import gui.components.ui.CustomTabbedPaneUI;
import gui.debug.ConsoleKeyEventDispatcher;

/**
 * This is the core of the application, from where is choose which panel will be opened for which user.
 *	
 *
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1810393566512302281L;

	private static ApplicationFacadeInterface appFacadeInterface;

	private JPanel contentPane;
	private AbstractUser user;
	private JTabbedPane tabbedPane;

	/**
	 * Launches the {@code MainWindow} application.</br>
	 * This will prompt a dialog to choose between which type of user wants to be launched the {@code MainWindow}
	 * <p>
	 * This is {@code main} method is only for debugging purposes.</br>
	 * <strong><em>This will be removed in a nearby future,</em></strong> and will be accessed as the designed way.
	 * 
	 */
	@Deprecated
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new ConsoleKeyEventDispatcher();
					
					ApplicationFacadeImpl aplicationFacade = new ApplicationFacadeImpl();
					aplicationFacade.setDataAccess(new DataAccess());
					MainWindow.setBussinessLogic(aplicationFacade);
					
					AbstractUser user = setupDebugAccount();					
					MainWindow frame = new MainWindow(user);
					frame.setFocusable(true);
					frame.setVisible(true); 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Only for debugging purposes it will be removed in a future
	 * @return the chosen user fictional account
	 */
	@Deprecated
	private static AbstractUser setupDebugAccount() {
		Role[] options = new Role[] {Role.CLIENT, Role.OWNER, Role.ADMIN, Role.SUPER_ADMIN};
		Role response = (Role)JOptionPane.showInputDialog(null, "Open the window as: ", "Choose option", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(response != null) {
			try {
				switch (response) {
				case CLIENT:
					return getBusinessLogic().createUser("Client@clientmail.com", "Client", "ClientPassword", response);
				case OWNER:
					return getBusinessLogic().createUser("Owner@ownermail.com", "Owner", "OwnerPassword", response);
				case ADMIN:
					return getBusinessLogic().createUser("Admin@adminmail.com", "Admin", "AdminPassword", response);
				case SUPER_ADMIN:
					//Not implemented
					return getBusinessLogic().createUser("SuperAdmin@superadminmail.com", "SuperAdmin", "SuperAdminPassword", response);
				}
			} catch (DuplicatedEntityException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
		return null;
	}

	public static ApplicationFacadeInterface getBusinessLogic(){
		return appFacadeInterface;
	}

	public static void setBussinessLogic (ApplicationFacadeInterface applicationFacadeInterface){
		appFacadeInterface = applicationFacadeInterface;
	}

	/**
	 * Create the frame.
	 */
	public MainWindow(AbstractUser user) {

		this.user = user;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getRolePanel(user.getRole());
		//setJMenuBar(getRoleMenuBar());


		contentPane = new JPanel();
		//		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setUI(new CustomTabbedPaneUI());
		//contentPane.add(tabbedPane);
		tabbedPane.addTab("Rural Houses", getRootPane().add(getRolePanel(user.getRole())));
		tabbedPane.addTab("Profile", getProfilePanel());
//		tabbedPane.addTab("Maybe another pane?", new TextArea("Yeh awesome... another pane..."));
//		tabbedPane.addTab("Ideas for another pane...",  new JFileChooser());

		//		JButton logOutButton = new JButton("Log Out");
		//		tabbedPane.setTabComponentAt(tabbedPane.getTabCount(), logOutButton);
		tabbedPane.addTab("Log Out", null);

		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();				
				System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
				if(index == sourceTabbedPane.getTabCount()-1) {
					if(logOutQuestion()) {
						SharedFrame sharedFrame = new SharedFrame();
						sharedFrame.setVisible(true);
						dispose();
					}
					tabbedPane.setSelectedIndex(0); //TODO cleanup
				}
			}
		};
		tabbedPane.addChangeListener(changeListener);

		//		tabbedPane.addMouseMotionListener(new MouseMotionListener() {
		//			@Override
		//			public void mouseDragged(MouseEvent e) {}
		//			@Override
		//			public void mouseMoved(MouseEvent e){
		//				adjustCursor(e);
		//			}
		//		});

		contentPane.add(tabbedPane);

		setMinimumSize(new Dimension(780, 600));
		setSize(900, 800);
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

		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitQuestion();
			}
		};
		this.addWindowListener(exitListener);	

	}

	private JPanel getProfilePanel() {
		JPanel profilePanel = new JPanel();
		profilePanel.setLayout(null);
		//TODO: Make a separate class with the user profile /////////////////
		JLabel lblUser = new JLabel("User: ");
		lblUser.setBounds(10, 11, 64, 14);
		profilePanel.add(lblUser);

		JLabel lblNewLabel = new JLabel(user.getUsername());
		lblNewLabel.setBounds(84, 11, 411, 14);
		profilePanel.add(lblNewLabel);

		JLabel lblEmail = new JLabel("Password:");
		lblEmail.setBounds(10, 36, 64, 14);
		profilePanel.add(lblEmail);

		JPasswordField lblNewLabel_1 = new JPasswordField(user.getPassword().replaceAll(".", "*"));
		//lblNewLabel_1.setEchoChar('☺');
		lblNewLabel_1.setBorder(null);
		lblNewLabel_1.setFocusTraversalKeysEnabled(false);
		lblNewLabel_1.setFocusable(false);
		lblNewLabel_1.setEditable(false);
		lblNewLabel_1.setBounds(84, 36, 190, 19);
		profilePanel.add(lblNewLabel_1);

		JLabel lblRole = new JLabel("e-mail: ");
		lblRole.setBounds(10, 61, 64, 14);
		profilePanel.add(lblRole);

		JLabel lblNewLabel_2 = new JLabel(user.getEmail());
		lblNewLabel_2.setBounds(84, 61, 411, 19);
		profilePanel.add(lblNewLabel_2);

		JLabel label = new JLabel("Role: ");
		label.setBounds(10, 86, 64, 14);
		profilePanel.add(label);

		JLabel label_1 = new JLabel(user.getRole().toString());
		label_1.setBounds(84, 84, 411, 19);
		profilePanel.add(label_1);		
		///////////////////////////////////////////////
		return profilePanel;
	}

	//		private void adjustCursor(MouseEvent e) {
	//	
	//			TabbedPaneUI ui = tabbedPane.getUI();
	//	
	//			int index = ui.tabForCoordinate(tabbedPane, e.getX(), e.getY());
	//	
	//			if (index >= 0) {
	//				tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	//			} else {
	//				tabbedPane.setCursor(null);
	//			}
	//	
	//		}

	public JPanel getRolePanel(Role role) {
		switch (role) {
		case CLIENT:
			return new ClientMainPanel(this);
		case OWNER:
			//FIXME VERY VERY TEMPORAL!!
			return (JPanel) new MainGUI(role).getContentPane();
			//return new OwnerMainPanel(this);
		case ADMIN:
			return new AdminMainPanel(this);
		case SUPER_ADMIN:
			//return new SuperAdminMainPanel(this);
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

	public int exitQuestion() {
		int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", null, JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
		return reply;
	}

	public boolean logOutQuestion() {
		int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", null, JOptionPane.YES_NO_OPTION);
		switch (reply) {
		case JOptionPane.YES_OPTION:			
			return true;
		case JOptionPane.NO_OPTION:
		default:
			return false;
		}
	}

}
