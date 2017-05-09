package gui.components;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class InfoTextPane extends JTextPane {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -73593325903127059L;

	public InfoTextPane(String title, ImagePanel imagePanel, String body) {

		


		//		String[] initString = { "The title goes here\n",            //bold
		//					"image\n",                                   //italic
		////					"content",                                    //bold
		////					"text ",                                      //small
		////					"component, ",                                //large
		////					"which supports embedded components...",//regular
		////					"",                                //button
		////					"...and embedded icons... \n",         //regular
		////					" ",                                          //icon
		//					"JTextPane is a subclass of JEditorPane that " +
		//					"uses a StyledEditorKit and StyledDocument, and provides " +
		//					"cover methods for interacting with those objects."
		//			};

		String[] initString = {	
				title + "\n\n",		//title
				/*" ",*/					//imagePanel
				body				//body
		};

		String[] initStyles = { "title", /*"imagePanel",*/ "body"};

		StyledDocument doc = getStyledDocument();
		addStylesToDocument(doc);

		try {
			for (int i=0; i < initString.length; i++) {
				doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}

	}

	protected void addStylesToDocument(StyledDocument doc) {

		//Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style style = doc.addStyle("italic", regular);
		StyleConstants.setItalic(style, true);

		style = doc.addStyle("bold", regular);
		StyleConstants.setBold(style, true);

		style = doc.addStyle("title", regular);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBold(style, true);

		style = doc.addStyle("body", regular);
		StyleConstants.setFontSize(style, 12);

		/*
		style = doc.addStyle("large", regular);
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		ImageIcon pigIcon = createImageIcon("images/Pig.gif", "a cute pig");
		if (pigIcon != null) {
			StyleConstants.setIcon(style, pigIcon);
		}

		style = doc.addStyle("button", regular);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
		ImageIcon soundIcon = createImageIcon("images/sound.gif", "sound icon");
		JButton button = new JButton();
		if (soundIcon != null) {
			button.setIcon(soundIcon);
		} else {
			button.setText("BEEP");
		}

		ImageIcon soundIcon = createImageIcon("images/sound.gif", "sound icon");
		JButton button = new JButton();
		if (soundIcon != null) {
			button.setIcon(soundIcon);
		} else {
			button.setText("BEEP");
		}

		button.setCursor(Cursor.getDefaultCursor());
		button.setMargin(new Insets(0,0,0,0));
		button.setActionCommand(buttonString);
		button.addActionListener(this);
		*/
		
//		style = doc.addStyle("imagePanel", regular);
//		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
//
//		StyleConstants.setIcon(style, imagePanel.getImage());
	}

}
