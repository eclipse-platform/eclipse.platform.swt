package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class Canvas extends Composite {

Canvas () {
	/* Do nothing */
}

public Canvas (Composite parent, int style) {
	super (parent, style);
}


void redrawWidget (int x, int y, int width, int height, boolean all) {
}

void releaseWidget () {
	super.releaseWidget();
}

public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget();
}

public void setLocation (int x, int y) {
	checkWidget();
}

public void setSize (int width, int height) {
	checkWidget();
}
}
