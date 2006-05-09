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
package org.eclipse.swt.dnd;

 
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TreeDragAndDropEffect extends DragAndDropEffect {

	private Tree tree;

TreeDragAndDropEffect(Tree tree) {
	this.tree = tree;
}
Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	TreeItem item = tree.getItem(coordinates);
	if (item != null) return item;

	Rectangle area = tree.getClientArea();
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		coordinates = new Point(x1, y);
		coordinates = tree.toControl(coordinates);
		item = tree.getItem(coordinates);
		if (item != null) return item;
	}
	return null;
}
}
