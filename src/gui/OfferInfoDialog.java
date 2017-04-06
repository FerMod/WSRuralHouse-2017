package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import domain.RuralHouse;
import gui.ClientMainPanel.CellDetails;
import gui.components.FrameShader;
import gui.components.ImagePanel;

public class OfferInfoDialog extends JDialog {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -5923341666785935549L;

	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private Component parentFrame;
	private FrameShader frameShader;

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
					parentFrame.getContentPane().add(launchDialog);

					launchDialog.addActionListener(new ActionListener() {				
						@Override
						public void actionPerformed(ActionEvent e) {
							OfferInfoDialog dialog = new OfferInfoDialog(parentFrame);
							dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							//Set location relative to the parent frame. ALWAYS BEFORE SHOWING THE DIALOG.
							dialog.setLocationRelativeTo(parentFrame);
							dialog.setVisible(true);
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param rowContent 
	 */
	public OfferInfoDialog(JFrame parentFrame) {

		this.parentFrame = parentFrame;
		frameShader = new FrameShader(parentFrame);
		

		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("GAIN");
				if (e.getWindow() != OfferInfoDialog.this) {
					//frameShader.setEnabled(true);
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

		setUndecorated(true);
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
		gbl_contentPanel.columnWidths = new int[]{218, 56, 45, 42, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
	
		contentPanel.setLayout(gbl_contentPanel);
		{
			ImagePanel imagePanel = new ImagePanel();
			imagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridheight = 3;
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(imagePanel, gbc_panel);
			imagePanel.setImage(getClass().getResource("/img/icons/loading.gif"));
			//imagePanel.setPreferredSize(imagePanel.getImageSize());
			imagePanel.setMinimumSize(imagePanel.getImageSize());
		}
		{
			JButton btnNewButton = new JButton("Book Offer");
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.gridx = 2;
			gbc_btnNewButton.gridy = 1;
			contentPanel.add(btnNewButton, gbc_btnNewButton);
			btnNewButton.setPreferredSize(new Dimension(150, 40));
		}
		{
			JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
			GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
			gbc_tabbedPane.gridwidth = 4;
			gbc_tabbedPane.fill = GridBagConstraints.BOTH;
			gbc_tabbedPane.gridx = 0;
			gbc_tabbedPane.gridy = 6;
			contentPanel.add(tabbedPane, gbc_tabbedPane);
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("New tab", null, panel, null);
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("New tab", null, panel, null);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setForeground(UIManager.getColor("Button.foreground"));
			panel.setBorder(null);
			panel.setOpaque(true);
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JButton btnX = new JButton("");
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
				btnX.setAlignmentY(Component.TOP_ALIGNMENT);
				btnX.setAlignmentX(Component.RIGHT_ALIGNMENT);
				btnX.setPreferredSize(new Dimension(22, 22));
				btnX.setMinimumSize(new Dimension(9, 9));
				btnX.setMaximumSize(new Dimension(9, 9));
				btnX.setMargin(new Insets(0, 0, 0, 0));
				btnX.setIconTextGap(0);
				btnX.setIcon(new ImageIcon(OfferInfoDialog.class.getResource("/img/icons/close_button/window-close.png")));
				panel.add(btnX);
			}
		}
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public Component getParentComponent() {
		return parentFrame;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentFrame = parentComponent;
	}


}
