package com.wsruralhouse.gui.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public class RightClickMenu extends JPopupMenu {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -1689516973326338576L;

	private static final String ICON_FOLDER_PATH = "img/icons/";

	private JTextComponent textComponent;

	public RightClickMenu(JTextComponent textComponent) {
		this.textComponent = textComponent;
		initComponent();
	}

	private void initComponent() {

		if(textComponent.getHighlighter() != null) {

			// Actions
			add(getCutAction());
			add(getCopyAction());
			add(getPasteAction());

			// Separator
			addSeparator();


			// Select All
			add(getSelectAllAction());

		}

	}

	private Action getSelectAllAction() {
		
		Action selectAll = new SelectAllAction();
		selectAll.putValue(Action.NAME, "SelectAll");
		selectAll.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));

		JMenuItem selectAllMenuItem = new JMenuItem(selectAll);
		selectAllMenuItem.setHorizontalTextPosition(SwingConstants.LEFT);
		selectAllMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		
		return selectAll;
	}

	private Action getCutAction() {

		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));

		Optional<ImageIcon> icon = Optional.empty();
		try {
			icon = getImageIcon(ICON_FOLDER_PATH + "cut.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(icon.isPresent()) {
			cut.putValue(Action.SMALL_ICON, icon.get());				
		}
		cut.setEnabled(textComponent.isEditable());

		return cut;
	}

	private Action getCopyAction() {

		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));

		Optional<ImageIcon> icon = Optional.empty();
		try {
			icon = getImageIcon(ICON_FOLDER_PATH + "copy.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(icon.isPresent()) {
			copy.putValue(Action.SMALL_ICON, icon.get());				
		}

		return copy;
	}

	private Action getPasteAction() {
		
		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		
		Optional<ImageIcon> icon = Optional.empty();
		try {
			icon = getImageIcon(ICON_FOLDER_PATH + "paste.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		if (icon.isPresent()) {
			paste.putValue(Action.SMALL_ICON, icon);							
		}
		paste.setEnabled(textComponent.isEditable());
		
		return paste;
	}

	private Optional<ImageIcon> getImageIcon(String path) throws IOException {

		InputStream inputStream = getClass().getResourceAsStream(path);
		if(inputStream == null) {
			System.err.println("Resource not found: " + path);
			return Optional.empty();			
		}

		return Optional.ofNullable(new ImageIcon(ImageIO.read(inputStream)));
	}

	private static class SelectAllAction extends TextAction {
		/**
		 * Generated serial version ID
		 */
		private static final long serialVersionUID = -2451448648221545271L;

		public SelectAllAction() {
			super("Select All");
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getFocusedComponent();
			if(component != null) {
				component.selectAll();
				component.requestFocusInWindow();
			}
		}

	}

}
