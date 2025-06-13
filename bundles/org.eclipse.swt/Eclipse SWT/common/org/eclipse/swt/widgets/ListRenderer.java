package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public abstract class ListRenderer extends ControlRenderer {

	public static final String COLOR_BACKGROUND = "list.background"; //$NON-NLS-1$
	public static final String COLOR_BORDER = "list.border"; //$NON-NLS-1$
	public static final String COLOR_SELECTION_BACKGROUND = "list.selection.background"; //$NON-NLS-1$
	public static final String COLOR_SELECTION_FOREGROUND = "list.selection.foreground"; //$NON-NLS-1$

	public abstract Point computeDefaultSize();

	public abstract int getLineHeight();

	public abstract Point computeTextSize();

	protected final List list;

	public ListRenderer(List list) {
		super(list);
		this.list = list;
	}

	@Override
	public Color getDefaultBackground() {
		return getColor(COLOR_BACKGROUND);
	}
}
