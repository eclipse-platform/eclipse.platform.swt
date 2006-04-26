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
