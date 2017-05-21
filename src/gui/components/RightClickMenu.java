package gui.components;

import java.awt.event.ActionEvent;
import java.io.IOException;

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

	private JTextComponent textComponent;

	public RightClickMenu(JTextComponent textComponent) {
		this.textComponent = textComponent;
		initComponent();
	}

	private void initComponent() {

		if(textComponent.getHighlighter() != null) {

			ImageIcon icon = null;

			// Cut
			Action cut = new DefaultEditorKit.CutAction();
			cut.putValue(Action.NAME, "Cut");
			cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));

			try {
				icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/cut.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			cut.putValue(Action.SMALL_ICON, icon);
			cut.setEnabled(textComponent.isEditable());

			add(cut);

			// Copy
			Action copy = new DefaultEditorKit.CopyAction();
			copy.putValue(Action.NAME, "Copy");
			copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));

			try {
				icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/copy.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			copy.putValue(Action.SMALL_ICON, icon);

			add(copy);


			// Paste
			Action paste = new DefaultEditorKit.PasteAction();
			paste.putValue(Action.NAME, "Paste");
			paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));

			try {
				icon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/icons/paste.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			paste.putValue(Action.SMALL_ICON, icon);			
			paste.setEnabled(textComponent.isEditable());

			add(paste);


			// Separator
			addSeparator();


			// Select All
			Action selectAll = new SelectAllAction();
			selectAll.putValue(Action.NAME, "SelectAll");
			selectAll.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));

			JMenuItem selectAllMenuItem = new JMenuItem(selectAll);
			selectAllMenuItem.setHorizontalTextPosition(SwingConstants.LEFT);
			selectAllMenuItem.setHorizontalAlignment(SwingConstants.LEFT);

			add(selectAllMenuItem);

		}

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
