package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;
import dataAccess.DataAccess;
import domain.AbstractUser;
import domain.Admin;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import domain.AbstractUser.Role;
import domain.Review.ReviewState;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;
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
	public static AbstractUser user;
	private JTabbedPane tabbedPane;
	private int lastPaneIndex = 0;

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

					try {
						UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
						System.err.println(e.toString());
					}

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
					Client client = (Client) getBusinessLogic().createUser("Client@clientmail.com", "Client", "ClientPassword", response);
					Admin admin = (Admin) getBusinessLogic().createUser("adminTemp@admin.com", "adminTemp", "adminTemp", Role.ADMIN);
					Owner owner = (Owner)getBusinessLogic().createUser("own@gmail.com", "own", "own", Role.OWNER);
					SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
					RuralHouse rh = getBusinessLogic().createRuralHouse(owner, "Rural House Name", "Descripcion de la casa bonita", getBusinessLogic().createCity("Donostia"), "La calle larga 4 - 3ºb");
					rh.getReview().setState(admin, ReviewState.APPROVED);
					getBusinessLogic().update(rh);
					Offer offer1 = null, offer2 = null;
					try {
						offer1 = getBusinessLogic().createOffer(rh, date.parse("2017/1/20"), date.parse("2017/3/23"), 13);
						offer2 = getBusinessLogic().createOffer(rh, date.parse("2017/5/7"), date.parse("2017/9/16"), 24);
					} catch (OverlappingOfferException | BadDatesException | ParseException e) {
						e.printStackTrace();
					}
					try {
						getBusinessLogic().createBooking(client, offer1, date.parse("2017/1/4"), date.parse("2019/2/20"));
						getBusinessLogic().createBooking(client, offer2, date.parse("2017/6/13"), date.parse("2019/8/2"));						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return client;
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

		MainWindow.user = user;

		setMinimumSize(new Dimension(780, 600));
		setSize(900, 800);
		validate();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setJMenuBar(getRoleMenuBar());

		contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getTabbedPane());
		setContentPane(contentPane);


		//		addComponentListener(new ComponentListener() {
		//			@Override
		//			public void componentShown(ComponentEvent e) {
		//			}
		//			@Override
		//			public void componentResized(ComponentEvent e) {
		//				System.out.println(MainWindow.class.getName()+"[Width: " + getWidth() + ", Height" + getHeight() + "]");
		//			}
		//			@Override
		//			public void componentMoved(ComponentEvent e) {
		//			}
		//			@Override
		//			public void componentHidden(ComponentEvent e) {
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



	private JTabbedPane getTabbedPane() {
		if(tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setUI(new CustomTabbedPaneUI());
			setupTabs(MainWindow.user.getRole());

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
						} else {
							tabbedPane.setSelectedIndex(lastPaneIndex);
						}
					} else {
						lastPaneIndex = tabbedPane.getSelectedIndex();
					}
				}
			};
			tabbedPane.addChangeListener(changeListener);

		}
		return tabbedPane;
	}

	private void setupTabs(Role role) {
		Map<String, JPanel> panelMap = getRoleTabPanels(role);
		for (Entry<String, JPanel> entry : panelMap.entrySet()) {
			tabbedPane.add(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Obtain a {@code LinkedHashMap<String, JPanel>} with the tab panels that the user will see
	 * 
	 * @param role the role of the user
	 * @return a {@code LinkedHashMap<String, JPanel>} of elements in the order that where added
	 * 
	 * @see Li
	 */
	public LinkedHashMap<String, JPanel> getRoleTabPanels(Role role) {
		LinkedHashMap<String, JPanel> panelMap = new LinkedHashMap<String, JPanel>();
		switch (role) {
		case CLIENT:
			panelMap.put("Rural House Offers", new ClientMainPanel(this));
			panelMap.put("Offer Bookings", new BookingsTablePanel(this));
			break;
		case OWNER:
			//FIXME VERY VERY TEMPORAL!!
			//panelMap.put("Ower Main Menu", (JPanel) new MainGUI(role).getContentPane());
			panelMap.put("Ower Main Menu", (JPanel) new OwnerRuralHousesPanel(this));
			// panelMap.put("Main Menu", new OwnerMainPanel(this));
			break;
		case ADMIN:
			panelMap.put("Admin Main Menu", new AdminMainPanel(this));
			break;
		case SUPER_ADMIN:
			//panelMap.put("Super Admin Main Menu", new SuperAdminMainPanel(this));
			break;
		default:
			//[TODO]: Throw exception when the user role content pane is not defined 
			System.exit(1);
			break;
		}
		panelMap.put("Profile", getProfilePanel(MainWindow.user));
		panelMap.put("Log Out", null);
		return panelMap;
	}

	public JPanel getProfilePanel(AbstractUser user) {
		return new ProfilePane(user);
	}

	@SuppressWarnings("unused")
	@Deprecated
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
			MainWindow.user = null;
			return true;
		case JOptionPane.NO_OPTION:
		default:
			return false;
		}
	}

}
