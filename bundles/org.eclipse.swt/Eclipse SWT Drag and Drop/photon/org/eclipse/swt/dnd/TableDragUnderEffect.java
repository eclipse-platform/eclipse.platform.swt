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

class TableDragAndDropEffect extends DragAndDropEffect {
	private Table table;
	
TableDragAndDropEffect(Table table) {
	this.table = table;
}
Widget getItem(int x, int y) {
	if (table == null) return null;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	TableItem item = table.getItem(coordinates);
	if (item != null) return item;
	
	Rectangle area = table.getClientArea();
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		coordinates = new Point(x1, y);
		coordinates = table.toControl(coordinates);
		item = table.getItem(coordinates);
		if (item != null) return item;
	}
	return null;
	
}
}
