package gui.prototypes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import gui.components.TextPrompt;

public final class TextComponentStyler {
	
	private TextComponentStyler() {
	}
	
	public static JTextComponent applyStyle(JTextComponent textComponent) {
		return applyStyle("", textComponent);
	}

	public static JTextComponent applyStyle(String tipText, JTextComponent textComponent) {
		TextPrompt textPrompt = new TextPrompt(textComponent);
		textPrompt.setText(tipText);
		textPrompt.setStyle(Font.BOLD);
		textPrompt.setAlpha(128);
		Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
		Border insideBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
		textComponent.setBorder(border);
		return textComponent;
	}

}
