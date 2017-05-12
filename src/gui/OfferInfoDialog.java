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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

import gui.ClientMainPanel.CellDetails;
import gui.components.FrameShader;
import gui.components.ImagePanel;
import gui.components.InfoTextPane;

import javax.swing.JTextField;
import java.awt.Cursor;
import java.awt.Font;
import com.toedter.calendar.JDateChooser;

import domain.Client;

import java.awt.Rectangle;
import javax.swing.JLabel;

public class OfferInfoDialog extends JDialog {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -5923341666785935549L;

	public static String TITLE = "";

	private final JPanel contentPanel = new JPanel();
	private JPanel dialogTitleBar, bookOfferTabPanel, buttonPane;
	private ImagePanel imagePanel;
	private JTabbedPane tabbedPane;
	private InfoTextPane infoTextPane;
	private JScrollPane scrollPane, infoTextScrollPane;
	private Component parentFrame;
	private FrameShader frameShader;
	private JTextField dialogTitle, textFieldPrice;
	private JDateChooser firstDateChooser, lastDateChooser;
	private JButton btnX, btnBookOffer, okButton, cancelButton;

	private CellDetails rowContent;
	private Calendar firstDate, lastDate;
	private JLabel lblPrice;

	/*
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
	 */

	/**
	 * A constructor to create the dialog filled with the row information
	 * 
	 * @param parentFrame the parent frame of this dialog
	 * @param rowContent the object of type {@code CellDetails} that contains all the data, that will be displayed
	 */
	public OfferInfoDialog(JFrame parentFrame, CellDetails rowContent) {
		super(parentFrame);
		setUndecorated(true);
		this.parentFrame = parentFrame;
		this.rowContent = rowContent;
		frameShader = new FrameShader(parentFrame);

		System.out.println("[OfferInfoDialog]: Showing " + rowContent.getOffer().toString());

		getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.DARK_GRAY));
		setResizable(true);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 490, 657);
		getContentPane().setLayout(new BorderLayout());

		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{218, 45, 0};
		gbl_contentPanel.rowHeights = new int[]{20, 20, 0, 0, 77, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);

		// --------------------------------------------------------------------
		//  Dialog title bar setup
		// --------------------------------------------------------------------

		getContentPane().add(getDialogTitleBar(), BorderLayout.NORTH);


		// --------------------------------------------------------------------
		//  Dialog components setup
		// --------------------------------------------------------------------

		getContentPane().add(getScrollPane(), BorderLayout.CENTER);


		// --------------------------------------------------------------------
		//  Button pane setup
		// --------------------------------------------------------------------

		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);


		// --------------------------------------------------------------------
		//  Event listeners
		// --------------------------------------------------------------------

		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				if (e.getWindow() != OfferInfoDialog.this) {
					System.out.println(e.getSource().getClass().getName() + " -> Gained focus!");
					frameShader.setEnabled(true);
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e) {
				if(e.getOppositeWindow() != null) {
					if (!SwingUtilities.isDescendingFrom(e.getOppositeWindow(), OfferInfoDialog.this)) {
						System.out.println(e.getSource().getClass().getName() + "-> Closed the dialog!");
						frameShader.setEnabled(false);
						dispose();
					}
				}
			}

		});

		//		parentFrame.addComponentListener(new ComponentListener() {
		//
		//			@Override
		//			public void componentShown(ComponentEvent e) {
		//				System.out.println("shown");
		//			}
		//
		//			@Override
		//			public void componentResized(ComponentEvent e) {
		//				System.out.println(e.getSource().getClass() + " >> " + e.paramString());
		//				//				int width = ((parentFrame.getSize().width - getSize().width/2)/100)*85;
		//				//				int height = ((parentFrame.getSize().height - getSize().height/2)/100)*99;
		//				//				setSize(width, height);
		//				//				validate();
		//			}
		//
		//			@Override
		//			public void componentMoved(ComponentEvent e) {
		//				//				System.out.println(e.getSource().getClass() + " >> " + e.paramString());
		//				//				setLocation(getFrameCenter(parentFrame));
		//			}
		//
		//			@Override
		//			public void componentHidden(ComponentEvent e) {
		//			}
		//
		//		});

		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println(e.getSource().getClass().getName() + " -> Window opened");
				frameShader.setEnabled(true);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println(e.getSource().getClass().getName() + " -> Window closing");
				frameShader.setEnabled(false);
			}
			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println(e.getSource() + " -> Lost focus");

			}
		};
		addWindowListener(exitListener);

	}

	private JScrollPane getScrollPane() {
		if(scrollPane == null) {	

			scrollPane = new JScrollPane(contentPanel);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//imagePanel
			GridBagConstraints gbc_imagePanel = new GridBagConstraints();
			gbc_imagePanel.gridheight = 4;
			gbc_imagePanel.insets = new Insets(0, 5, 5, 5);
			gbc_imagePanel.fill = GridBagConstraints.BOTH;
			gbc_imagePanel.gridx = 0;
			gbc_imagePanel.gridy = 1;		
			contentPanel.add(getImagePanel(), gbc_imagePanel);

			//textPane
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.gridheight = 4;
			gbc_textPane.insets = new Insets(0, 5, 5, 0);
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 1;
			gbc_textPane.gridy = 1;
			contentPanel.add(getInfoTextScrollPane(), gbc_textPane);

			//tabbedPane
			GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
			gbc_tabbedPane.gridwidth = 2;
			gbc_tabbedPane.fill = GridBagConstraints.BOTH;
			gbc_tabbedPane.gridx = 0;
			gbc_tabbedPane.gridy = 6;
			contentPanel.add(getTabbedPane(), gbc_tabbedPane);

		}
		return scrollPane;
	}

	private JPanel getDialogTitleBar() {
		if(dialogTitleBar == null) {

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

			//title
			GridBagConstraints gbc_title = new GridBagConstraints();
			gbc_title.anchor = GridBagConstraints.LINE_START;
			gbc_title.insets = new Insets(5, 5, 5, 5);
			gbc_title.gridx = 0;
			gbc_title.gridy = 0;
			gbc_title.fill = GridBagConstraints.HORIZONTAL;
			dialogTitleBar.add(getDialogTitle(), gbc_title);

			//btnX
			GridBagConstraints gbc_btnX = new GridBagConstraints();
			gbc_btnX.anchor = GridBagConstraints.EAST;
			gbc_btnX.gridx = 1;
			gbc_btnX.gridy = 0;
			gbc_btnX.insets = new Insets(5, 5, 5, 5);
			dialogTitleBar.add(getDialogTitleCloseButton(), gbc_btnX);

		}
		return dialogTitleBar;
	}

	private JTextField getDialogTitle() {
		if (dialogTitle == null) {
			if(rowContent.getOffer().getRuralHouse().getName() != null) {
				TITLE = rowContent.getOffer().getRuralHouse().getName();				
			}
			dialogTitle = new JTextField(TITLE);
			dialogTitle.setOpaque(false);
			dialogTitle.setBorder(null);
			dialogTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
			dialogTitle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			dialogTitle.setEditable(false);
			dialogTitle.setFocusable(false);
			dialogTitle.setColumns(10);
			dialogTitle.setHighlighter(null);//Disable text selection
		}
		return dialogTitle;
	}

	private JButton getDialogTitleCloseButton() {
		if(btnX == null) {
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
		}
		return btnX;
	}

	private ImagePanel getImagePanel() {
		if(imagePanel == null) {
			ImageIcon imageIcon = rowContent.getOffer().getRuralHouse().getImage(0);				
			imagePanel = new ImagePanel(imageIcon);
			imagePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			imagePanel.setMinimumSize(imagePanel.getImageSize());
		}
		return imagePanel;
	}

	private InfoTextPane getInfoTextPane() {
		if(infoTextPane == null) {
			infoTextPane = new InfoTextPane();

			String title = rowContent.getOffer().getRuralHouse().getName();
			if(title != null) {
			} else {		
				title = "";
			}

			StringBuilder sb = new StringBuilder();
			sb.append("Address:");
			sb.append(System.getProperty("line.separator"));
			sb.append(rowContent.getOffer().getRuralHouse().getAddress());
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			sb.append("Description:");
			sb.append(System.getProperty("line.separator"));
			sb.append(rowContent.getOffer().getRuralHouse().getDescription());
			sb.append(System.getProperty("line.separator"));

			infoTextPane.setText(title, sb.toString());
			infoTextPane.setEditable(false);
			infoTextPane.setHighlighter(null); //Disable text selection
			infoTextPane.setOpaque(false);
			//Place in top the caret (also setting the scrollbar top)
			infoTextPane.setCaretPosition(0);
		}
		return infoTextPane;
	}

	private JScrollPane getInfoTextScrollPane() {
		if(infoTextScrollPane == null) {
			infoTextScrollPane = new JScrollPane(getInfoTextPane());
			infoTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			infoTextScrollPane.setPreferredSize(new Dimension(250, 155));
			infoTextScrollPane.setMinimumSize(new Dimension(10, 10));
		}
		return infoTextScrollPane;
	}

	private JTabbedPane getTabbedPane() {
		if(tabbedPane == null) {
			tabbedPane = new JTabbedPane(SwingConstants.TOP);
			tabbedPane.addTab("Book Offer", null, getBookOfferTabPanel(), null);
		}
		return tabbedPane;
	}

	private JPanel getBookOfferTabPanel() {
		if(bookOfferTabPanel == null) {

			bookOfferTabPanel = new JPanel();
			GridBagLayout gbl_tabPanel = new GridBagLayout();
			gbl_tabPanel.columnWidths = new int[]{180, 177, 0};
			gbl_tabPanel.rowHeights = new int[]{40, 0, 0, 0, 0};
			gbl_tabPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_tabPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
			bookOfferTabPanel.setLayout(gbl_tabPanel);

			//firstDateChooser
			GridBagConstraints gbc_firstDateChooser = new GridBagConstraints();
			gbc_firstDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_firstDateChooser.insets = new Insets(5, 20, 5, 20);
			gbc_firstDateChooser.gridx = 0;
			gbc_firstDateChooser.gridy = 0;
			bookOfferTabPanel.add(getFirstDateChooser(), gbc_firstDateChooser);

			//lastDateChooser
			GridBagConstraints gbc_lastDateChooser = new GridBagConstraints();
			gbc_lastDateChooser.fill = GridBagConstraints.HORIZONTAL;
			gbc_lastDateChooser.insets = new Insets(5, 20, 5, 20);
			gbc_lastDateChooser.gridx = 1;
			gbc_lastDateChooser.gridy = 0;
			gbc_lastDateChooser.fill = GridBagConstraints.HORIZONTAL;
			bookOfferTabPanel.add(getLastDateChooser(), gbc_lastDateChooser);

			//lblPrice
			GridBagConstraints gbc_lblPrice = new GridBagConstraints();
			gbc_lblPrice.anchor = GridBagConstraints.EAST;
			gbc_lblPrice.insets = new Insets(0, 0, 5, 5);
			gbc_lblPrice.gridx = 0;
			gbc_lblPrice.gridy = 2;
			bookOfferTabPanel.add(getLblPrice(), gbc_lblPrice);

			//textFieldPrice
			GridBagConstraints gbc_textFieldPrice = new GridBagConstraints();
			gbc_textFieldPrice.anchor = GridBagConstraints.WEST;
			gbc_textFieldPrice.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldPrice.gridx = 1;
			gbc_textFieldPrice.gridy = 2;
			bookOfferTabPanel.add(getTextFieldPrice(), gbc_textFieldPrice);

			//btnBookOffer
			GridBagConstraints gbc_btnBookOffer = new GridBagConstraints();
			gbc_btnBookOffer.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnBookOffer.gridwidth = 2;
			gbc_btnBookOffer.gridx = 0;
			gbc_btnBookOffer.gridy = 3;
			bookOfferTabPanel.add(getBtnBookOffer(), gbc_btnBookOffer);

		}
		return bookOfferTabPanel;
	}

	private JDateChooser getFirstDateChooser() {
		if(firstDateChooser == null) {
			firstDateChooser = new JDateChooser();
			//			firstDateChooser.setMinSelectableDate(Calendar.getInstance().getTime());
			firstDateChooser.setSelectableDateRange(getMinDate(rowContent.getOffer().getStartDate()), rowContent.getOffer().getEndDate());
			//			firstDateChooser.setDate(firstDateChooser.getMinSelectableDate());
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
						updatePrice();			
						System.out.println(e.getPropertyName() + ": " + sdf.format(firstDate.getTime()));
					}
				}
			});
		}
		return firstDateChooser;
	}

	private JDateChooser getLastDateChooser() {
		if(lastDateChooser == null) {
			lastDateChooser = new JDateChooser();
			//			lastDateChooser.setMinSelectableDate(firstDateChooser.getMinSelectableDate());
			lastDateChooser.setSelectableDateRange(firstDateChooser.getMinSelectableDate(), rowContent.getOffer().getEndDate());
			//			lastDateChooser.setDate(lastDateChooser.getMaxSelectableDate());
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
						updatePrice();
						System.out.println(e.getPropertyName() + ": " + sdf.format(lastDate.getTime()));
					}
				}
			});
		}
		return lastDateChooser;
	}

	private JLabel getLblPrice() {
		if(lblPrice == null) {
			NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
			lblPrice = new JLabel("Price (" + formatter.format(rowContent.getOffer().getPrice()) + " per night): ");
		}
		return lblPrice;
	}

	private JTextField getTextFieldPrice() {
		if(textFieldPrice == null) {
			textFieldPrice = new JTextField();
			textFieldPrice.setEditable(false);
			textFieldPrice.setColumns(10);
		}
		return textFieldPrice;
	}

	private JButton getBtnBookOffer() {
		if (btnBookOffer == null) {
			btnBookOffer = new JButton("Book Offer");
			btnBookOffer.setEnabled(false);
			btnBookOffer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnBookOffer.setPreferredSize(new Dimension(150, 40));
			btnBookOffer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {	
					MainWindow.getBusinessLogic().createBooking((Client)MainWindow.user, rowContent.getOffer());
					frameShader.setEnabled(false);
					dispose();
				}
			});
		}
		return btnBookOffer;
	}

	private JPanel getButtonPane() {
		if(buttonPane == null) {
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

			//okButton
			buttonPane.add(getOkButton());

			//cancelButton
			buttonPane.add(getCancelButton());

		}
		return buttonPane;
	}

	private JButton getOkButton() {
		if(okButton == null) {
			okButton = new JButton("OK");
			okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		return okButton;
	}

	private JButton getCancelButton() {
		if (cancelButton == null) {		
			cancelButton = new JButton("Cancel");
			cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}
		return cancelButton;
	}

	private void updatePrice() {
		if(firstDate != null && lastDate != null) {
			NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
			textFieldPrice.setText(formatter.format(getPrice()));	
			btnBookOffer.setEnabled(true);
		}
	}

	public Date getMinDate(Date date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date);		
		return getMinDate(startDate).getTime();
	}

	public Calendar getMinDate(Calendar startDate) {
		Calendar currentDate = Calendar.getInstance();
		return startDate.before(currentDate)? currentDate : startDate;
	}

	public Date getMaxDate(Date date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date);		
		return getMaxDate(startDate).getTime();
	}

	public Calendar getMaxDate(Calendar endDate) {
		Calendar currentDate = Calendar.getInstance();
		return endDate.after(currentDate)? currentDate : endDate;
	}

	/**
	 * Get the difference between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the difference
	 * @return the difference value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillis = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
	}

	private double getPrice() {
		long days = getDateDiff(firstDate.getTime(), lastDate.getTime(), TimeUnit.DAYS) + 1;
		return days * rowContent.getOffer().getPrice();
	}

	public Component getParentComponent() {
		return parentFrame;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentFrame = parentComponent;
	}


}
