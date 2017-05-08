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
import javax.swing.JTextArea;
import java.awt.Font;
import com.toedter.calendar.JCalendar;
import java.awt.Rectangle;

public class OfferInfoDialog extends JDialog {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -5923341666785935549L;

	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private Component parentFrame;
	private FrameShader frameShader;
	private JTextField title;

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
		gbl_contentPanel.rowHeights = new int[]{20, 20, 0, 0, 77, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);

		ImagePanel imagePanel = new ImagePanel();
		imagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 4;
		gbc_panel.insets = new Insets(0, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		contentPanel.add(imagePanel, gbc_panel);
		//imagePanel.setPreferredSize(imagePanel.getImageSize());
		imagePanel.setMinimumSize(imagePanel.getImageSize());

		InfoTextPane textPane = new InfoTextPane("Title goes here", new ImagePanel(), "JTextPane is a subclass of JEditorPane that " +
				"uses a StyledEditorKit and StyledDocument, and provides " +
				"cover methods for interacting with those objects.");
		textPane.setEditable(false);
		textPane.setHighlighter(null); //Disable text selection
		textPane.setOpaque(false);
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridheight = 4;
		gbc_textPane.insets = new Insets(0, 5, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 1;
		contentPanel.add(paneScrollPane, gbc_textPane);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 7;
		contentPanel.add(tabbedPane, gbc_tabbedPane);

		//		OfferIntervalSelectionPanel offerIntervalSelectionPanel = new OfferIntervalSelectionPanel();	
		//		offerIntervalSelectionPanel.setVisible(true);
		JPanel tabPanel = new JPanel();
		tabbedPane.addTab("Book Offer", null, tabPanel, null);

		tabbedPane.addTab("New tab", null, tabPanel, null);
		
		JCalendar calendar = new JCalendar();
		calendar.setBounds(new Rectangle(190, 60, 225, 150));
		tabPanel.add(calendar);


		JButton btnNewButton = new JButton("Book Offer");
		tabPanel.add(btnNewButton);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setPreferredSize(new Dimension(150, 40));


		JPanel dialogTitleBar = new JPanel();
		dialogTitleBar.setBackground(UIManager.getColor("TextField.light"));
		dialogTitleBar.setForeground(UIManager.getColor("Button.foreground"));
		dialogTitleBar.setBorder(null);
		dialogTitleBar.setOpaque(true);
		getContentPane().add(dialogTitleBar, BorderLayout.NORTH);
		GridBagLayout gbl_dialogTitleBar = new GridBagLayout();
		gbl_dialogTitleBar.columnWidths = new int[]{318, 22, 0};
		gbl_dialogTitleBar.rowHeights = new int[]{22, 0};
		gbl_dialogTitleBar.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_dialogTitleBar.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		dialogTitleBar.setLayout(gbl_dialogTitleBar);
		
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

		JButton btnX = new JButton("");
		btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnX.setBorderPainted(false);
		btnX.setSelectedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close.png")));
		btnX.setRolloverSelectedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_pressed.png")));
		btnX.setRolloverIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_hover.png")));
		btnX.setPressedIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close_pressed.png")));
		btnX.setContentAreaFilled(false);
		btnX.setDefaultCapable(false);
		btnX.setFocusPainted(false);
		btnX.setFocusable(false);
		btnX.setFocusTraversalKeysEnabled(false);
		btnX.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});			
		btnX.setBackground(UIManager.getColor("Button.light"));
		btnX.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnX.setPreferredSize(new Dimension(22, 22));
		btnX.setMinimumSize(new Dimension(9, 9));
		btnX.setMaximumSize(new Dimension(9, 9));
		btnX.setIconTextGap(0);
		btnX.setIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close.png")));
		GridBagConstraints gbc_btnX = new GridBagConstraints();
		gbc_btnX.anchor = GridBagConstraints.EAST;
		gbc_btnX.gridx = 1;
		gbc_btnX.gridy = 0;
		gbc_btnX.insets = new Insets(5, 5, 5, 5);
		dialogTitleBar.add(btnX, gbc_btnX);


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

	public Component getParentComponent() {
		return parentFrame;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentFrame = parentComponent;
	}


}
