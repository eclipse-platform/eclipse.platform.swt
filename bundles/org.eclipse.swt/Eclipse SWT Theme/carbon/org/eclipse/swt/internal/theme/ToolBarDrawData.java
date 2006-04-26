package org.eclipse.swt.internal.theme;

import org.eclipse.swt.graphics.*;

public class ToolBarDrawData extends DrawData {
	
public ToolBarDrawData() {
	state = new int[1];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
}

int hit(Theme theme, Point position, Rectangle bounds) {
	if (!bounds.contains(position)) return DrawData.WIDGET_NOWHERE;
	return DrawData.WIDGET_WHOLE;
}

}
