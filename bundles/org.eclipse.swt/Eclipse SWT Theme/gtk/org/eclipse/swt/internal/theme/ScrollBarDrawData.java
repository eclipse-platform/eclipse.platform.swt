package org.eclipse.swt.internal.theme;

import org.eclipse.swt.graphics.*;

public class ScrollBarDrawData extends RangeDrawData {
	public int thumb;
	public int increment;
	public int pageIncrement;
	
public ScrollBarDrawData() {
	state = new int[6];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}
