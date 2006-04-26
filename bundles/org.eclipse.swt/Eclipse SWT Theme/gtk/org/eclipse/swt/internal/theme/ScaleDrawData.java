package org.eclipse.swt.internal.theme;

import org.eclipse.swt.graphics.*;

public class ScaleDrawData extends RangeDrawData {
	public int increment;
	public int pageIncrement;
	
public ScaleDrawData() {
	state = new int[4];
}

void draw(Theme theme, GC gc, Rectangle bounds) {
	
}

int hit(Theme theme, Point position, Rectangle bounds) {
	return bounds.contains(position) ? DrawData.WIDGET_WHOLE : DrawData.WIDGET_NOWHERE;
}

}
