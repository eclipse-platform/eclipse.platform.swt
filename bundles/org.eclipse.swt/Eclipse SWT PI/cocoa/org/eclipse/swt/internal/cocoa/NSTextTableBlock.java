/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextTableBlock extends NSTextBlock {

public NSTextTableBlock() {
	super();
}

public NSTextTableBlock(int id) {
	super(id);
}

public int columnSpan() {
	return OS.objc_msgSend(this.id, OS.sel_columnSpan);
}

public id initWithTable(NSTextTable table, int row, int rowSpan, int col, int colSpan) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTable_1startingRow_1rowSpan_1startingColumn_1columnSpan_1, table != null ? table.id : 0, row, rowSpan, col, colSpan);
	return result != 0 ? new id(result) : null;
}

public int rowSpan() {
	return OS.objc_msgSend(this.id, OS.sel_rowSpan);
}

public int startingColumn() {
	return OS.objc_msgSend(this.id, OS.sel_startingColumn);
}

public int startingRow() {
	return OS.objc_msgSend(this.id, OS.sel_startingRow);
}

public NSTextTable table() {
	int result = OS.objc_msgSend(this.id, OS.sel_table);
	return result != 0 ? new NSTextTable(result) : null;
}

}
