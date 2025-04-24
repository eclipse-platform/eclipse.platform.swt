package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Point;

public abstract class ListRenderer extends ControlRenderer {

	public abstract Point computeDefaultSize();

	public abstract int getLineHeight();

	public abstract Point computeTextSize();

	protected final List list;

	public ListRenderer(List list) {
		super(list);
		this.list = list;
	}
}
