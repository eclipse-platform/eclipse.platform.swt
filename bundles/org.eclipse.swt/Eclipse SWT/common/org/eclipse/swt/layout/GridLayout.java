/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.layout;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class lay out the control children of a 
 * <code>Composite</code> in a grid. 
 * <p>
 * <code>GridLayout</code> has a number of configuration fields, and the 
 * controls it lays out can have an associated layout data object, called 
 * <code>GridData</code>. The power of <code>GridLayout</code> lies in the 
 * ability to configure <code>GridData</code> for each control in the layout. 
 * </p>
 * <p>
 * The following code creates a shell managed by a <code>GridLayout</code>
 * with 3 columns:
 * <pre>
 * 		Display display = new Display();
 * 		Shell shell = new Shell(display);
 * 		GridLayout gridLayout = new GridLayout();
 * 		gridLayout.numColumns = 3;
 * 		shell.setLayout(gridLayout);
 * </pre>
 * The <code>numColumns</code> field is the most important field in a 
 * <code>GridLayout</code>. Widgets are laid out in columns from left 
 * to right, and a new row is created when <code>numColumns</code> + 1 
 * controls are added to the <code>Composite<code>.
 * </p>
 * 
 * @see GridData
 */
public final class GridLayout extends Layout {
 
 	/**
 	 * numColumns specifies the number of cell columns in the layout.
 	 *
 	 * The default value is 1.
 	 */
	public int numColumns = 1;

	/**
	 * makeColumnsEqualWidth specifies whether all columns in the layout
	 * will be forced to have the same width.
	 *
	 * The default value is false.
	 */
	public boolean makeColumnsEqualWidth = false;
	
	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the layout.
	 *
	 * The default value is 5.
	 */
 	public int marginWidth = 5;
 	
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the layout.
	 *
	 * The default value is 5.
	 */
 	public int marginHeight = 5;

	/**
	 * horizontalSpacing specifies the number of pixels between the right
	 * edge of one cell and the left edge of its neighbouring cell to
	 * the right.
	 *
	 * The default value is 5.
	 */
 	public int horizontalSpacing = 5;

	/**
	 * verticalSpacing specifies the number of pixels between the bottom
	 * edge of one cell and the top edge of its neighbouring cell underneath.
	 *
	 * The default value is 5.
	 */
 	public int verticalSpacing = 5;

/**
 * Constructs a new instance of this class.
 */
public GridLayout () {
}

/**
 * Constructs a new instance of this class given the
 * number of columns, and whether or not the columns
 * should be forced to have the same width.
 *
 * @param numColumns the number of columns in the grid
 * @param makeColumnsEqualWidth whether or not the columns will have equal width
 * 
 * @since 2.0
 */
public GridLayout (int numColumns, boolean makeColumnsEqualWidth) {
	this.numColumns = numColumns;
	this.makeColumnsEqualWidth = makeColumnsEqualWidth;
}

protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
	Point size =  layout (composite, false, 0, 0, 0, 0, flushCache);
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	return size;
}

Point computeSize (Control control, boolean flushCache) {
	GridData data = (GridData) control.getLayoutData ();
	if (data == null) control.setLayoutData (data = new GridData ());
	if (!flushCache & data.cacheWidth != -1 && data.cacheHeight != -1) {
		return new Point (data.cacheWidth, data.cacheHeight);
	}
	return control.computeSize (data.widthHint, data.heightHint, flushCache);
}

GridData getData (Control [][] grid, int row, int column, int rowCount, int columnCount, boolean first) {
	Control control = grid [row] [column];
	if (control != null) {
		GridData data = (GridData) control.getLayoutData ();
		int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
		int vSpan = Math.max (1, data.verticalSpan);
		int i = first ? row + vSpan - 1 : row - vSpan + 1;
		int j = first ? column + hSpan - 1 : column - hSpan + 1;
		if (0 <= i && i < rowCount) {
			if (0 <= j && j < columnCount) {
				if (control == grid [i][j]) return data;
			}
		}
	}
	return null;
}

protected void layout (Composite composite, boolean flushCache) {
	Rectangle rect = composite.getClientArea ();
	layout (composite, true, rect.x, rect.y, rect.width, rect.height, flushCache);
}

Point layout (Composite composite, boolean move, int x, int y, int width, int height, boolean flushCache) {
	if (numColumns < 1) return new Point (marginWidth * 2, marginHeight * 2);
	Control [] children = composite.getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		Point size = computeSize (child, flushCache);
		GridData data = (GridData) child.getLayoutData ();
		data.cacheWidth = size.x;
		data.cacheHeight = size.y;
	}
	
	/* Build the grid */
	int row = 0, column = 0, rowCount = 0, columnCount = numColumns;
	Control [][] grid = new Control [4] [columnCount];
	for (int i=0; i<children.length; i++) {	
		Control child = children [i];
		GridData data = (GridData) child.getLayoutData ();
		int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
		int vSpan = Math.max (1, data.verticalSpan);
		while (true) {
			int lastRow = row + vSpan;
			if (lastRow >= grid.length) {
				Control [] [] newGrid = new Control [lastRow + 4] [columnCount];
				System.arraycopy (grid, 0, newGrid, 0, grid.length);
				grid = newGrid;
			}
			if (grid [row] == null) {
				grid [row] = new Control [columnCount];
			}
			while (column < columnCount && grid [row] [column] != null) {
				column++;
			}
			int endCount = column + hSpan;
			if (endCount <= columnCount) {
				int index = column;
				while (index < endCount && grid [row] [index] == null) {
					index++;
				}
				if (index == endCount) break;
				column = index;
			}
			if (column + hSpan >= columnCount) {
				column = 0;
				row++;
			}
		}
		for (int j=0; j<vSpan; j++) {
			if (grid [row + j] == null) {
				grid [row + j] = new Control [columnCount];
			}
			for (int k=0; k<hSpan; k++) {
				grid [row + j] [column + k] = child;
			}
		}
		rowCount = Math.max (rowCount, row + vSpan);
		column += hSpan;
	}
	
	/* Determine the available, default and minimum column widths */
	int availableWidth = width - horizontalSpacing * (columnCount - 1) - marginWidth * 2;
	int totalMinWidth = 0, totalDefaultWidth = 0, expandColumnCount = 0;
	int [] minWidths = new int [columnCount], defaultWidths = new int [columnCount];
	boolean [] expandColumn = new boolean [columnCount];
	for (int j=0; j<columnCount; j++) {
		for (int i=0; i<rowCount; i++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, false);
			if (data != null) {			
				int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
				
				/* Compute the minimum widths */
				int w = data.cacheWidth + data.horizontalIndent;
				for (int k = 1; k < hSpan; k++) {
					w -= minWidths [j-k];
				}
				w -= (hSpan - 1) * horizontalSpacing;
				if (!data.grabExcessHorizontalSpace || data.widthHint != SWT.DEFAULT) {
					minWidths [j] = Math.max (minWidths [j], w);
				}
				
				/* Compute the default widths */
				w = data.cacheWidth + data.horizontalIndent;
				for (int k = 1; k < hSpan; k++) {
					w -= defaultWidths [j-k];
				}
				w -= (hSpan - 1) * horizontalSpacing;
				defaultWidths [j] = Math.max (defaultWidths [j], w);
				
				/* Mark columns that are expandable */
				if (data.grabExcessHorizontalSpace) {
					if (!expandColumn [j]) expandColumnCount++;
					expandColumn [j] = true;
				}
			}
		}
		totalMinWidth += minWidths [j];
		totalDefaultWidth += defaultWidths [j];
	}
	
	/* Assign widths to columns */
	int [] widths = null;
	if (makeColumnsEqualWidth) {
		int minColumnWidth = 0;
		int defaultColumnWidth = 0;
		for (int i=0; i<columnCount; i++) {
			minColumnWidth = Math.max (minColumnWidth, minWidths [i]);
			defaultColumnWidth = Math.max (defaultColumnWidth, defaultWidths [i]);
		}
		int columnWidth = move ? Math.max (minColumnWidth, availableWidth / columnCount) : defaultColumnWidth;
		totalDefaultWidth = columnWidth * columnCount;
		widths = new int [columnCount];
		for (int i=0; i<columnCount; i++) {
			expandColumn [i] = true;
			widths [i] = columnWidth;
		}
	} else {
		if (!move) {
			widths = defaultWidths;
		} else {
			if (availableWidth <= totalMinWidth || expandColumnCount == 0) {
				widths = minWidths;
			} else {
				int total = 0, extra = (availableWidth - totalDefaultWidth) / expandColumnCount;
				if (extra < 0) {
					int oldExpandColumnCount = 0;
					do {
						oldExpandColumnCount = expandColumnCount;
						expandColumnCount = 0;
						total = 0;
						for (int i = 0; i < columnCount; i++) {
							if (expandColumn [i] && defaultWidths [i] + extra > minWidths [i]) {
								expandColumnCount++;
								total += defaultWidths [i];
							} else {
								total += minWidths [i];
							}
						}
						extra = expandColumnCount == 0 ? 0 : (availableWidth - total) / expandColumnCount;
					} while (oldExpandColumnCount > expandColumnCount);
				}
				total = expandColumnCount = 0;
				widths = new int [columnCount];
				for (int j=0; j<columnCount; j++) {
					int minWidth = 0;
					for (int i=0; i<rowCount; i++) {
						GridData data = getData(grid, i, j, rowCount, columnCount, false);
						if (data != null) {
							int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
							int w = data.cacheWidth + data.horizontalIndent;
							for (int k = 1; k < hSpan; k++) {
								w -= widths [j-k];
							}
							w -= (hSpan - 1) * horizontalSpacing;
							widths [j] = Math.max (widths [j], w);
							if (!data.grabExcessHorizontalSpace || data.widthHint != SWT.DEFAULT) {
								minWidth = Math.max (minWidth, w);
							}
						}
					}
					if (expandColumn [j]) {
						expandColumnCount++;
						widths [j] = Math.max(widths [j] + extra, minWidth);
					}
					total += widths [j];
				}
				if (total < availableWidth && expandColumnCount > 0) {
					int last = -1;
					extra = (availableWidth - total) / expandColumnCount;
					int remainder = (availableWidth - total) % expandColumnCount;
					total = 0;
					for (int i=0; i<columnCount; i++) {
						if (expandColumn [i]) {
							widths [last = i] += extra;
						}
						total += widths [i];
					}
					if (last != -1) widths [last] += remainder;
				}
			}
		}
	}
	
	/* Determine the available, default and minimum row heights */
	int availableHeight = height - verticalSpacing * (rowCount - 1) - marginHeight * 2;
	int totalMinHeight = 0, totalDefaultHeight = 0, expandRowCount = 0;
	int [] minHeights = new int [rowCount], defaultHeights = new int [rowCount];
	boolean [] expandRow = new boolean [rowCount];
	for (int i=0; i<rowCount; i++) {
		for (int j=0; j<columnCount; j++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, false);
			if (data != null) {
				int vSpan = Math.max (1, data.verticalSpan);
				
				/* Compute the minimum heights */
				int h = data.cacheHeight; // + data.verticalIndent;
				for (int k = 1; k < vSpan; k++) {
					h -= minHeights[i-k];
				}
				h -= (vSpan - 1) * verticalSpacing;
				if (!data.grabExcessVerticalSpace || data.heightHint != SWT.DEFAULT) {
					minHeights [i] = Math.max (minHeights [i], h);
				}
				
				/* Compute the default heights */
				h = data.cacheHeight; // + data.verticalIndent;
				for (int m = 1; m < vSpan; m++) {
					h -= defaultHeights[i-m];
				}
				h -= (vSpan - 1) * verticalSpacing;
				defaultHeights [i] = Math.max (defaultHeights [i], h);
				
				/* Mark rows that are expandable */
				if (data.grabExcessVerticalSpace) {
					if (!expandRow [i]) expandRowCount++;
					expandRow [i] = true;
				}
			}
		}
		totalMinHeight += minHeights [i];
		totalDefaultHeight += defaultHeights [i];
	}
	
	/* Assign heights to rows */
	int [] heights = null;
	if (!move) {
		heights = defaultHeights;
	} else {
		if (availableHeight <= totalMinHeight || expandRowCount == 0) {
			heights = minHeights;
		} else {
			int total = 0, extra = (availableHeight - totalDefaultHeight) / expandRowCount;
			if (extra < 0) {
				int oldExpandRowCount = 0;
				do {
					oldExpandRowCount = expandRowCount;
					expandRowCount = 0;
					total = 0;
					for (int i = 0; i < rowCount; i++) {
						if (expandRow [i] && defaultHeights [i] + extra > minHeights [i]) {
							expandRowCount++;
							total += defaultHeights [i];
						} else {
							total += minHeights [i];
						}
					}
					extra = expandRowCount == 0 ? 0 : (availableHeight - total) / expandRowCount;
				} while (oldExpandRowCount > expandRowCount);
			}
			total = expandRowCount = 0;
			heights = new int [rowCount];
			for (int i=0; i<rowCount; i++) {
				int minHeight = 0;
				for (int j=0; j<columnCount; j++) {
					GridData data = getData (grid, i, j, rowCount, columnCount, false);
					if (data != null) {
						int vSpan = Math.max (1, data.verticalSpan);
						int h = data.cacheHeight; // + data.verticalIndent;
						for (int k=1; k<vSpan; k++) {
							h -= heights[i-k];
						}
						h -= (vSpan - 1) * verticalSpacing;
						heights [i] = Math.max (heights [i], h);
						if (!data.grabExcessVerticalSpace || data.heightHint != SWT.DEFAULT) {
							minHeight = Math.max (minHeight, h);
						}
					}
				}
				if (expandRow [i]) {
					expandRowCount++;
					heights [i] = Math.max(heights [i] + extra, minHeight);
				}
				total += heights [i];
			}
			if (total < availableHeight && expandRowCount > 0) {
				int last = -1;
				extra = (availableHeight - total) / expandRowCount;
				int remainder = (availableHeight - total) % expandRowCount;
				total = 0;
				for (int i=0; i<rowCount; i++) {
					if (expandRow [i]) {
						heights [last = i] += extra;
					}
					total += heights [i];
				}
				if (last != -1) heights [last] += remainder;
			}
		}
	}
	
	/* Position the controls */
	if (move) {
		int gridY = y + marginHeight;
		for (int i=0; i<rowCount; i++) {
			int gridX = x + marginWidth;
			for (int j=0; j<columnCount; j++) {
				GridData data = getData (grid, i, j, rowCount, columnCount, true);
				if (data != null) {
					int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
					int vSpan = Math.max (1, data.verticalSpan);
					int cellWidth = 0, cellHeight = 0;
					for (int k=0; k<hSpan; k++) {
						cellWidth += widths [j+k];
					}
					for (int k=0; k<vSpan; k++) {
						cellHeight += heights [i+k];
					}
					cellWidth += horizontalSpacing * (hSpan - 1);
					int childX = gridX + data.horizontalIndent;
					int childWidth = Math.min (data.cacheWidth, cellWidth);
					switch (data.horizontalAlignment) {
						case SWT.CENTER:
						case GridData.CENTER:
							childX = gridX + Math.max (0, (cellWidth - childWidth) / 2);
							break;
						case SWT.RIGHT:
						case SWT.END:
						case GridData.END:
							childX = gridX + Math.max (0, cellWidth - childWidth);
							break;
						case SWT.FILL:
							childWidth = cellWidth - data.horizontalIndent;
							break;
					}
					cellHeight += verticalSpacing * (vSpan - 1);
					int childY = gridY; // + data.verticalIndent;
					int childHeight = Math.min (data.cacheHeight, cellHeight);
					switch (data.verticalAlignment) {
						case SWT.CENTER:
						case GridData.CENTER:
							childY = gridY + Math.max (0, (cellHeight - childHeight) / 2);
							break;
						case SWT.BOTTOM:
						case SWT.END:
						case GridData.END:
							childY = gridY + Math.max (0, cellHeight - childHeight);
							break;
						case SWT.FILL:
							childHeight = cellHeight; // - data.verticalIndent;
							break;
					}
					Control child = grid [i][j];
					if (child != null) {
						child.setBounds (childX, childY, childWidth, childHeight);
					}
				}
				gridX += widths [j] + horizontalSpacing;
			}
			gridY += heights [i] + verticalSpacing;
		}
	}
	
	totalDefaultWidth += horizontalSpacing * (columnCount - 1) + marginWidth * 2;
	totalDefaultHeight += verticalSpacing * (rowCount - 1) + marginHeight * 2;
	return new Point (totalDefaultWidth, totalDefaultHeight);
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

public String toString () {
 	String string = getName ()+" {";
 	if (numColumns != 1) string += "numColumns="+numColumns+" ";
 	if (makeColumnsEqualWidth) string += "makeColumnsEqualWidth="+makeColumnsEqualWidth+" ";
 	if (marginWidth != 0) string += "marginWidth="+marginWidth+" ";
 	if (marginHeight != 0) string += "marginHeight="+marginHeight+" ";
 	if (horizontalSpacing != 0) string += "horizontalSpacing="+horizontalSpacing+" ";
 	if (verticalSpacing != 0) string += "verticalSpacing="+verticalSpacing+" ";
 	string = string.trim();
 	string += "}";
 	return string;
}
}