package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public class DefaultTableRenderer extends TableRenderer {

	protected DefaultTableRenderer(Table table) {
		super(table);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		var ca = table.getClientArea();
		if (ca.width == 0 || ca.height == 0) return;

		table.updateScrollBarWithTextSize();

		table.getColumnsHandler().paint(gc);
		table.getItemsHandler().paint(gc);
	}
}
