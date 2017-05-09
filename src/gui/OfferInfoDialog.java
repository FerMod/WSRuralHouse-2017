package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import gui.components.FrameShader;
import gui.components.ImagePanel;
import gui.components.InfoTextPane;

import javax.swing.JTextField;
import java.awt.Cursor;
import java.awt.Font;
import com.toedter.calendar.JDateChooser;
import java.awt.Rectangle;

public class OfferInfoDialog extends JDialog {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -5923341666785935549L;

	private final JPanel contentPanel = new JPanel();
	private JPanel dialogTitleBar;
	private ImagePanel imagePanel;
	private JTabbedPane tabbedPane;
	private InfoTextPane infoTextPane;
	private JScrollPane scrollPane, infoTextScrollPane;
	private Component parentFrame;
	private FrameShader frameShader;
	private JTextField title;
	private JDateChooser firstDateChooser, lastDateChooser;
	private JButton btnX;

	private Calendar firstDate, lastDate;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JFrame parentFrame = new JFrame();
					parentFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					parentFrame.setSize(800, 800);
					parentFrame.setLocationByPlatform(true);
					parentFrame.setVisible(true);
					parentFrame.getContentPane().setLayout(new GridBagLayout());
					JButton launchDialog = new JButton("LaunchDialog");	
					parentFrame.getContentPane().add(launchDialog);

					launchDialog.addActionListener(new ActionListener() {				
						@Override
						public void actionPerformed(ActionEvent e) {
							OfferInfoDialog dialog = new OfferInfoDialog(parentFrame);
							dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							dialog.validate();
							//Set location relative to the parent frame. ALWAYS BEFORE SHOWING THE DIALOG.
							dialog.setLocationRelativeTo(parentFrame);
							//dialog.setLocation(getFrameCenter(parentFrame));
							dialog.setVisible(true);

							//							JOptionPane.showInternalMessageDialog(parentFrame.getContentPane(), dialog.getRootPane());
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearMarks(Window frame) {
		Graphics2D g2 = (Graphics2D)frame.getGraphics();
		g2.clearRect(0, 0, frame.getSize().width, frame.getSize().height);
	}

	public Graphics2D markPoint(Window frame, int x, int y, Color color) {
		Graphics2D g2d = (Graphics2D)frame.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaint(color);
		g2d.setBackground(color);
		g2d.fillOval(x, y, 5, 5);
		return g2d;
	}

	public Point getFrameCenter(Window frame) {
		frame.getLocationOnScreen();
		int x = (int) (frame.getSize().width / 2);
		int y = (int) (frame.getSize().height / 2);

		clearMarks(frame);
		markPoint(frame, x, y, Color.RED);
		markPoint(frame, frame.getLocationOnScreen().x, frame.getLocationOnScreen().y, Color.GREEN);

		return new Point(x, y);
	}

	/**
	 * Create the dialog.
	 * @param rowContent 
	 */
	public OfferInfoDialog(JFrame parentFrame) {
		super(parentFrame);
		setUndecorated(true);
		this.parentFrame = parentFrame;
		frameShader = new FrameShader(parentFrame);

		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("GAIN");
				if (e.getWindow() != OfferInfoDialog.this) {
					frameShader.setEnabled(true);
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e) {
				if(e.getOppositeWindow() != null) {
					if (!SwingUtilities.isDescendingFrom(e.getOppositeWindow(), OfferInfoDialog.this)) {
						System.out.println("Closed the dialog!");
						frameShader.setEnabled(false);
						dispose();
					}
				}
			}

		});

		parentFrame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println(e.getSource().getClass() + " >> " + e.paramString());
				//				int width = ((parentFrame.getSize().width - getSize().width/2)/100)*85;
				//				int height = ((parentFrame.getSize().height - getSize().height/2)/100)*99;
				//				setSize(width, height);
				//				validate();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				//				System.out.println(e.getSource().getClass() + " >> " + e.paramString());
				//				setLocation(getFrameCenter(parentFrame));
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

		});

		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println("Window opened");
				frameShader.setEnabled(true);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Window closing");
				frameShader.setEnabled(false);
			}
		};
		this.addWindowListener(exitListener);
		getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.DARK_GRAY));
		setResizable(true);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 490, 657);
		getContentPane().setLayout(new BorderLayout());

		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{218, 45, 0};
		gbl_contentPanel.rowHeights = new int[]{20, 20, 0, 0, 77, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);

		dialogTitleBar = new JPanel();
		dialogTitleBar.setBackground(UIManager.getColor("TextField.light"));
		dialogTitleBar.setForeground(UIManager.getColor("Button.foreground"));
		dialogTitleBar.setBorder(null);
		dialogTitleBar.setOpaque(true);
		GridBagLayout gbl_dialogTitleBar = new GridBagLayout();
		gbl_dialogTitleBar.columnWidths = new int[]{318, 22, 0};
		gbl_dialogTitleBar.rowHeights = new int[]{22, 0};
		gbl_dialogTitleBar.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_dialogTitleBar.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		dialogTitleBar.setLayout(gbl_dialogTitleBar);
		getContentPane().add(dialogTitleBar, BorderLayout.NORTH);

		title = new JTextField("Dialog Title");
		title.setOpaque(false);
		title.setBorder(null);
		title.setFont(new Font("SansSerif", Font.BOLD, 16));
		title.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		title.setEditable(false);
		title.setColumns(10);
		title.setHighlighter(null);//Disable text selection
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.anchor = GridBagConstraints.LINE_START;
		gbc_title.insets = new Insets(5, 5, 5, 5);
		gbc_title.gridx = 0;
		gbc_title.gridy = 0;
		gbc_title.fill = GridBagConstraints.HORIZONTAL;
		dialogTitleBar.add(title, gbc_title);

		btnX = new JButton();
		btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnX.setBorderPainted(false);
		btnX.setIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close.png")));
		btnX.setSelectedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close.png")));
		btnX.setRolloverSelectedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_pressed.png")));
		btnX.setRolloverIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_hover.png")));
		btnX.setPressedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_pressed.png")));
		btnX.setContentAreaFilled(false);
		btnX.setDefaultCapable(false);
		btnX.setFocusPainted(false);
		btnX.setFocusable(false);
		btnX.setFocusTraversalKeysEnabled(false);
		btnX.setBackground(UIManager.getColor("Button.light"));
		btnX.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnX.setPreferredSize(new Dimension(22, 22));
		btnX.setMinimumSize(new Dimension(9, 9));
		btnX.setMaximumSize(new Dimension(9, 9));
		btnX.setIconTextGap(0);
		btnX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});			
		GridBagConstraints gbc_btnX = new GridBagConstraints();
		gbc_btnX.anchor = GridBagConstraints.EAST;
		gbc_btnX.gridx = 1;
		gbc_btnX.gridy = 0;
		gbc_btnX.insets = new Insets(5, 5, 5, 5);
		dialogTitleBar.add(btnX, gbc_btnX);

		imagePanel = new ImagePanel();
		imagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 4;
		gbc_panel.insets = new Insets(0, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		imagePanel.setMinimumSize(imagePanel.getImageSize());
		contentPanel.add(imagePanel, gbc_panel);

		infoTextPane = new InfoTextPane("Title goes here", new ImagePanel(), "JTextPane is a subclass of JEditorPane that " +
				"uses a StyledEditorKit and StyledDocument, and provides " +
				"cover methods for interacting with those objects.");
		infoTextPane.setEditable(false);
		infoTextPane.setHighlighter(null); //Disable text selection
		infoTextPane.setOpaque(false);
		infoTextScrollPane = new JScrollPane(infoTextPane);
		infoTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		infoTextScrollPane.setPreferredSize(new Dimension(250, 155));
		infoTextScrollPane.setMinimumSize(new Dimension(10, 10));
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridheight = 4;
		gbc_textPane.insets = new Insets(0, 5, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 1;
		contentPanel.add(infoTextScrollPane, gbc_textPane);

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 6;
		contentPanel.add(tabbedPane, gbc_tabbedPane);

		//		OfferIntervalSelectionPanel offerIntervalSelectionPanel = new OfferIntervalSelectionPanel();	
		//		offerIntervalSelectionPanel.setVisible(true);
		JPanel tabPanel = new JPanel();
		tabbedPane.addTab("New tab", null, tabPanel, null);

		GridBagLayout gbl_tabPanel = new GridBagLayout();
		gbl_tabPanel.columnWidths = new int[]{180, 177, 0};
		gbl_tabPanel.rowHeights = new int[]{40, 0, 0, 0, 0};
		gbl_tabPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_tabPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		tabPanel.setLayout(gbl_tabPanel);
		tabbedPane.addTab("Book Offer", null, tabPanel, null);

		firstDateChooser = new JDateChooser();
		firstDateChooser.setMinSelectableDate(Calendar.getInstance().getTime());
		//firstDateChooser.setSelectableDateRange(getMinDate( OFFER_START_DATE ), max); //TODO Get first day offer, and last day offer
		firstDateChooser.getJCalendar().setWeekOfYearVisible(true);
		firstDateChooser.setBounds(new Rectangle(190, 60, 225, 150));
		firstDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("date")) {
					firstDate = new GregorianCalendar();
					firstDate.setTime((Date) e.getNewValue());
					lastDateChooser.setMinSelectableDate(firstDate.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
					System.out.println(e.getPropertyName() + ": " + sdf.format(firstDate.getTime()));
				}
			}
		});
		GridBagConstraints gbc_firstDateChooser = new GridBagConstraints();
		gbc_firstDateChooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstDateChooser.insets = new Insets(5, 20, 5, 20);
		gbc_firstDateChooser.gridx = 0;
		gbc_firstDateChooser.gridy = 0;
		tabPanel.add(firstDateChooser, gbc_firstDateChooser);

		lastDateChooser = new JDateChooser();
		lastDateChooser.setMinSelectableDate(firstDateChooser.getMinSelectableDate());
		//lastDateChooser.setSelectableDateRange(firstDateChooser.getMinSelectableDate(), max); //TODO Get first day offer, and last day offer
		lastDateChooser.getJCalendar().setWeekOfYearVisible(true);
		lastDateChooser.setBounds(new Rectangle(190, 60, 225, 150));
		lastDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("date")) {
					lastDate = new GregorianCalendar();
					lastDate.setTime((Date) e.getNewValue());
					firstDateChooser.setMaxSelectableDate(lastDate.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
					System.out.println(e.getPropertyName() + ": " + sdf.format(lastDate.getTime()));
				}
			}
		});
		lastDateChooser.setMinSelectableDate(firstDateChooser.getMinSelectableDate());
		GridBagConstraints gbc_lastDateChooser = new GridBagConstraints();
		gbc_lastDateChooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastDateChooser.insets = new Insets(5, 20, 5, 20);
		gbc_lastDateChooser.gridx = 1;
		gbc_lastDateChooser.gridy = 0;
		tabPanel.add(lastDateChooser, gbc_lastDateChooser);

		JButton btnNewButton = new JButton("Book Offer");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 3;
		tabPanel.add(btnNewButton, gbc_btnNewButton);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setPreferredSize(new Dimension(150, 40));

		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);


		JButton cancelButton = new JButton("Cancel");
		cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	@SuppressWarnings("unused")
	private Calendar getMinDate(Calendar startDate) {
		Calendar currentDate = Calendar.getInstance();
		return startDate.before(currentDate)? currentDate : startDate;
	}

	@SuppressWarnings("unused")
	private Calendar getMaxDate(Calendar endDate) {
		return null; //TODO Return end date
	}

	public Component getParentComponent() {
		return parentFrame;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentFrame = parentComponent;
	}


}
