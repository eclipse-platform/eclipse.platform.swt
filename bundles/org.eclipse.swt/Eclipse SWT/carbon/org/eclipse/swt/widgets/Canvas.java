package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class Canvas extends Composite {
//	Caret caret;

Canvas () {
	/* Do nothing */
}

public Canvas (Composite parent, int style) {
	super (parent, style);
}

//public Caret getCaret () {
//	checkWidget();
//    return caret;
//}

//public void setCaret (Caret caret) {
//	checkWidget();
//}

public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
}

}