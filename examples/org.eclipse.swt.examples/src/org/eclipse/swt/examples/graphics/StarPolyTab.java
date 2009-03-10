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
package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class StarPolyTab extends GraphicsTab {
	int[] radial;
	static final int POINTS  = 11;
	
	Combo fillRuleCb;

public StarPolyTab(GraphicsExample example) {
	super(example);
	radial = new int[POINTS * 2];
}

public void createControlPanel(Composite parent) {
	new Label(parent, SWT.NONE).setText(GraphicsExample.getResourceString("FillRule")); //$NON-NLS-1$
	fillRuleCb = new Combo(parent, SWT.DROP_DOWN);
	fillRuleCb.add("FILL_EVEN_ODD");
	fillRuleCb.add("FILL_WINDING");
	fillRuleCb.select(0);
	fillRuleCb.addListener(SWT.Selection, new Listener() {
		public void handleEvent (Event event) {
			example.redraw();
		}
	});
}

public String getCategory() {
	return GraphicsExample.getResourceString("Polygons"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("StarPolygon"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("StarPolygonDescription"); //$NON-NLS-1$
}

public void paint(GC gc, int width, int height) {
    int centerX = width / 2;
    int centerY = height / 2;
    int pos = 0;
    for (int i = 0; i < POINTS; ++i) {
        double r = Math.PI*2 * pos/POINTS;
        radial[i*2] = (int)((1+Math.cos(r))*centerX);
        radial[i*2+1] = (int)((1+Math.sin(r))*centerY);
        pos = (pos + POINTS/2) % POINTS;
    }
	gc.setFillRule(fillRuleCb.getSelectionIndex() != 0 ? SWT.FILL_WINDING : SWT.FILL_EVEN_ODD);
	gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
    gc.fillPolygon(radial);
    gc.drawPolygon(radial);
}
}
