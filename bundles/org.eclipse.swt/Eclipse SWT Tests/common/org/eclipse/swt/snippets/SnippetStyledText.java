package org.eclipse.swt.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleEditableTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextAttributeEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetStyledText {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 357");
		shell.setLayout(new FillLayout());

		final StyledText text = new StyledText(shell, SWT.BORDER | SWT.MULTI);
		text.setText("The quick brown fox jumps over the lazy dog.\nThat's all folks!");
		TextStyle textStyle = new TextStyle(new Font(display, "Courier", 12, SWT.BOLD),
				display.getSystemColor(SWT.COLOR_RED), null);
		textStyle.strikeout = true;
		textStyle.underline = true;
		textStyle.underlineStyle = SWT.UNDERLINE_SINGLE;
		text.setStyleRanges(new int[] { 4, 5 }, new StyleRange[] { new StyleRange(textStyle) });

		text.getAccessible().addAccessibleEditableTextListener(new AccessibleEditableTextAdapter() {
			@Override
			public void setTextAttributes(AccessibleTextAttributeEvent e) {
				TextStyle textStyle = e.textStyle;
				if (textStyle != null) {
					/* Copy all of the TextStyle fields into the new StyleRange. */
					StyleRange style = new StyleRange(textStyle);
					/*
					 * Create new graphics resources because the old ones are only valid during the
					 * event.
					 */
					if (textStyle.font != null) {
						style.font = new Font(display, textStyle.font.getFontData());
					}
					if (textStyle.foreground != null) {
						style.foreground = new Color(textStyle.foreground.getRGB());
					}
					if (textStyle.background != null) {
						style.background = new Color(textStyle.background.getRGB());
					}
					if (textStyle.underlineColor != null) {
						style.underlineColor = new Color(textStyle.underlineColor.getRGB());
					}
					if (textStyle.strikeoutColor != null) {
						style.strikeoutColor = new Color(textStyle.strikeoutColor.getRGB());
					}
					if (textStyle.borderColor != null) {
						style.borderColor = new Color(textStyle.borderColor.getRGB());
					}
					/* Set the StyleRange into the StyledText. */
					style.start = e.start;
					style.length = e.end - e.start;
					text.setStyleRange(style);
					e.result = ACC.OK;
				} else {
					text.setStyleRanges(e.start, e.end - e.start, null, null);
				}
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}