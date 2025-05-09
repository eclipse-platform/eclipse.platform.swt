package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public class DefaultTreeRenderer extends TreeRenderer {

	protected DefaultTreeRenderer(Tree tree) {
		super(tree);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		Rectangle ca = tree.getClientArea();
		if (ca.width == 0 || ca.height == 0) {
			return;
		}

		tree.getColumnsHandler().paint(gc);
		tree.getItemsHandler().paint(gc);
	}
}
