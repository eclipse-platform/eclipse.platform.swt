/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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
