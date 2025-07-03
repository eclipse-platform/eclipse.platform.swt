package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public class CheckBoxPainter {

	static final String COLOR_SELECTION = "button.background.selection"; //$NON-NLS-1$
	static final String COLOR_BOX = "button.box"; //$NON-NLS-1$
	static final String COLOR_BOX_DISABLED = "button.box.disabled"; //$NON-NLS-1$
	static final String COLOR_GRAYED = "button.gray"; //$NON-NLS-1$
	static final String COLOR_DISABLED = "disabled"; //$NON-NLS-1$

	public static void paintCheckbox(GC gc, int x, int y, boolean enabled, boolean checked, boolean grayed,
			int boxSize, ColorProvider colorProvider) {
		final boolean selection = checked;

		var fgBef = gc.getForeground();
		var bgBef = gc.getBackground();

		if (selection) {
			gc.setBackground(
					colorProvider.getColor(enabled ? grayed ? COLOR_GRAYED : COLOR_SELECTION : COLOR_DISABLED));
			int partialBoxBorder = 2;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder, boxSize - 2 * partialBoxBorder + 1,
					boxSize - 2 * partialBoxBorder + 1, boxSize / 4 - partialBoxBorder / 2,
					boxSize / 4 - partialBoxBorder / 2);
		}

		gc.setForeground(colorProvider.getColor(enabled ? COLOR_BOX : COLOR_BOX_DISABLED));
		gc.drawRoundRectangle(x, y, boxSize, boxSize, 4, 4);

		gc.setForeground(fgBef);
		gc.setBackground(bgBef);

	}
}


