package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Point;

public abstract class GroupRenderer extends ControlRenderer {

	protected abstract Point getTextExtent();

	protected abstract void clearCache();

	protected final Group group;

	public GroupRenderer(Group group) {
		super(group);
		this.group = group;
	}
}
