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
	if (data.flushCache) {
		data.cacheWidth = data.cacheHeight = -1;
		data.flushCache = false;
	}
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
		GridData data = (GridData) child.getLayoutData ();
		if (data == null) child.setLayoutData (data = new GridData ());
		Point size = computeSize (child, flushCache);
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
	
	/* Column widths */
	int availableWidth = width - horizontalSpacing * (columnCount - 1) - marginWidth * 2;
	int expandCount = 0;
	int [] widths = new int [columnCount];
	int [] minWidths = new int [columnCount];
	boolean [] expandColumn = new boolean [columnCount];
	for (int j=0; j<columnCount; j++) {
		for (int i=0; i<rowCount; i++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, true);
			if (data != null) {
				int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
				if (hSpan == 1) {
					int w = data.cacheWidth + data.horizontalIndent;
					widths [j] = Math.max (widths [j], w);
					if (data.grabExcessHorizontalSpace) {
						if (!expandColumn [j]) expandCount++;
						expandColumn [j] = true;
					}
					if (data.widthHint != SWT.DEFAULT || !data.grabExcessHorizontalSpace) {
						minWidths [j] = Math.max (minWidths [j], w);
					}
				}
			}
		}
		for (int i=0; i<rowCount; i++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, false);
			if (data != null) {
				int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
				if (hSpan > 1) {
					int spanWidth = 0, spanMinWidth = 0, spanExpandCount = 0;
					for (int k=0; k<hSpan; k++) {
						spanWidth += widths [j-k];
						spanMinWidth += minWidths [j-k];
						if (expandColumn [j-k]) spanExpandCount++;
					}
					if (data.grabExcessHorizontalSpace && spanExpandCount == 0) {
						expandCount++;
						expandColumn [j] = true;
					}
					int w = data.cacheWidth + data.horizontalIndent - spanWidth - (hSpan - 1) * horizontalSpacing;
					if (w > 0) {
						if (spanExpandCount == 0) {
							widths [j] += w;
						} else {
							int delta = w / spanExpandCount;
							int remainder = w % spanExpandCount, last = -1;
							for (int k = 0; k < hSpan; k++) {
								if (expandColumn [j-k]) {
									widths [last=j-k] += delta;
								}
							}
							if (last > -1) widths [last] += remainder;	
						}
					}
					if (data.widthHint != SWT.DEFAULT || !data.grabExcessHorizontalSpace) {
						w = data.cacheWidth + data.horizontalIndent - spanMinWidth - (hSpan - 1) * horizontalSpacing;
						if (w > 0) {
							if (spanExpandCount == 0) {
								minWidths [j] += w;
							} else {
								int delta = w / spanExpandCount;
								int remainder = w % spanExpandCount, last = -1;
								for (int k = 0; k < hSpan; k++) {
									if (expandColumn [j-k]) {
										minWidths [last=j-k] += delta;
									}
								}
								if (last > -1) minWidths [last] += remainder;	
							}
						}
					}
				}
			}
		}
	}
	if (makeColumnsEqualWidth) {
		int minColumnWidth = 0;
		int columnWidth = 0;
		for (int i=0; i<columnCount; i++) {
			minColumnWidth = Math.max (minColumnWidth, minWidths [i]);
			columnWidth = Math.max (columnWidth, widths [i]);
		}
		columnWidth = move ? Math.max (minColumnWidth, availableWidth / columnCount) : columnWidth;
		for (int i=0; i<columnCount; i++) {
			expandColumn [i] = true;
			widths [i] = columnWidth;
		}
	} else {
		if (move && expandCount > 0) {
			int totalWidth = 0;
			for (int i=0; i<columnCount; i++) {
				totalWidth += widths [i];
			}
			int count = expandCount;
			int delta = (availableWidth - totalWidth) / count;
			int remainder = (availableWidth - totalWidth) % count;
			int last = -1;
			while (totalWidth != availableWidth) {
				for (int j=0; j<columnCount; j++) {
					if (expandColumn [j]) {
						if (widths [j] + delta > minWidths [j]) {
							widths [last = j] = widths [j] + delta;
						} else {
							widths [j] = minWidths [j];
							expandColumn [j] = false;
							count--;
						}
					}
				}
				if (last > -1) widths [last] += remainder;
				
				for (int j=0; j<columnCount; j++) {
					for (int i=0; i<rowCount; i++) {
						GridData data = getData (grid, i, j, rowCount, columnCount, false);
						if (data != null) {		
							int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
							if (hSpan > 1) {
								if (data.widthHint != SWT.DEFAULT || !data.grabExcessHorizontalSpace) {
									int spanWidth = 0, spanExpandCount = 0;
									for (int k=0; k<hSpan; k++) {
										spanWidth += widths [j-k];
										if (expandColumn [j-k]) spanExpandCount++;
									}
									int w = data.cacheWidth + data.horizontalIndent - spanWidth - (hSpan - 1) * horizontalSpacing;
									if (w > 0) {
										if (spanExpandCount == 0) {
											widths [j] += w;
										} else {
											int delta2 = w / spanExpandCount;
											int remainder2 = w % spanExpandCount, last2 = -1;
											for (int k = 0; k < hSpan; k++) {
												if (expandColumn [j-k]) {
													widths [last2=j-k] += delta2;
												}
											}
											if (last2 > -1) widths [last2] += remainder2;	
										}
									}
								}
							}
						}
					}
				}
				if (count == 0) break;
				totalWidth = 0;
				for (int i=0; i<columnCount; i++) {
					totalWidth += widths [i];
				}
				delta = (availableWidth - totalWidth) / count;
				remainder = (availableWidth - totalWidth) % count;
				last = -1;
			}
		}
	}
	
	/* Wrapping */
	if (move) {
		for (int j=0; j<columnCount; j++) {
			for (int i=0; i<rowCount; i++) {
				GridData data = getData (grid, i, j, rowCount, columnCount, false);
				if (data != null) {
					if (data.heightHint == SWT.DEFAULT) {
						Control child = grid [i][j];
						//TEMPORARY CODE
						if ((child.getStyle () & SWT.WRAP) != 0) {
							int hSpan = Math.max (1, Math.min (data.horizontalSpan, columnCount));
							int currentWidth = 0;
							for (int k=0; k<hSpan; k++) {
								currentWidth += widths [j-k];
							}
							currentWidth += (hSpan - 1) * horizontalSpacing - data.horizontalIndent;
							if ((currentWidth != data.cacheWidth && data.horizontalAlignment == SWT.FILL) ||
								(data.cacheWidth > currentWidth)) { 
								int trim = 0;
								if (child instanceof Scrollable) {
									Rectangle rect = ((Scrollable) child).computeTrim (0, 0, 0, 0);
									trim = rect.width;
								} else {
									trim = child.getBorderWidth () * 2;
								}
								currentWidth = Math.max (0, currentWidth - trim);
								Point size = child.computeSize (currentWidth, data.heightHint, true);
								data.cacheWidth = size.x;
								data.cacheHeight = size.y;
								data.flushCache = true;
							}
						}
					}
				}
			}
		}
	}
	
	/* Row heights */
	int availableHeight = height - verticalSpacing * (rowCount - 1) - marginHeight * 2;
	expandCount = 0;
	int [] heights = new int [rowCount];
	int [] minHeights = new int [rowCount];
	boolean [] expandRow = new boolean [rowCount];
	for (int i=0; i<rowCount; i++) {
		for (int j=0; j<columnCount; j++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, true);
			if (data != null) {
				int vSpan = Math.max (1, Math.min (data.verticalSpan, rowCount));
				if (vSpan == 1) {
					int h = data.cacheHeight; // + data.verticalIndent;
					heights [i] = Math.max (heights [i], h);
					if (data.grabExcessVerticalSpace) {
						if (!expandRow [i]) expandCount++;
						expandRow [i] = true;
					}
					if (data.heightHint != SWT.DEFAULT || !data.grabExcessVerticalSpace) {
						minHeights [i] = Math.max (minHeights [i], h);
					}
				}
			}
		}
		for (int j=0; j<columnCount; j++) {
			GridData data = getData (grid, i, j, rowCount, columnCount, false);
			if (data != null) {
				int vSpan = Math.max (1, Math.min (data.verticalSpan, rowCount));
				if (vSpan > 1) {
					int spanHeight = 0, spanMinHeight = 0, spanExpandCount = 0;
					for (int k=0; k<vSpan; k++) {
						spanHeight += heights [i-k];
						spanMinHeight += minHeights [i-k];
						if (expandRow [i-k]) spanExpandCount++;
					}
					if (data.grabExcessVerticalSpace && spanExpandCount == 0) {
						expandCount++;
						expandRow [i] = true;
					}
					int h = data.cacheHeight - spanHeight - (vSpan - 1) * verticalSpacing; // + data.verticalalIndent
					if (h > 0) {
						if (spanExpandCount == 0) {
							heights [i] += h;
						} else {
							int delta = h / spanExpandCount;
							int remainder = h % spanExpandCount, last = -1;
							for (int k = 0; k < vSpan; k++) {
								if (expandRow [i-k]) {
									heights [last=i-k] += delta;
								}
							}
							if (last > -1) heights [last] += remainder;	
						}
					}
					if (data.heightHint != SWT.DEFAULT || !data.grabExcessVerticalSpace) {
						h = data.cacheHeight - spanMinHeight - (vSpan - 1) * verticalSpacing; // + data.verticalIndent
						if (h > 0) {
							if (spanExpandCount == 0) {
								minHeights [i] += h;
							} else {
								int delta = h / spanExpandCount;
								int remainder = h % spanExpandCount, last = -1;
								for (int k = 0; k < vSpan; k++) {
									if (expandRow [i-k]) {
										minHeights [last=i-k] += delta;
									}
								}
								if (last > -1) minHeights [last] += remainder;	
							}
						}
					}
				}
			}
		}
	}
	if (move && expandCount > 0) {
		int totalHeight = 0;
		for (int i=0; i<rowCount; i++) {
			totalHeight += heights [i];
		}
		int count = expandCount;
		int delta = (availableHeight - totalHeight) / count;
		int remainder = (availableHeight - totalHeight) % count;
		int last = -1;
		while (totalHeight != availableHeight) {
			for (int i=0; i<rowCount; i++) {
				if (expandRow [i]) {
					if (heights [i] + delta > minHeights [i]) {
						heights [last = i] = heights [i] + delta;
					} else {
						heights [i] = minHeights [i];
						expandRow [i] = false;
						count--;
					}
				}
			}
			if (last > -1) heights [last] += remainder;
			
			for (int i=0; i<rowCount; i++) {
				for (int j=0; j<columnCount; j++) {
					GridData data = getData (grid, i, j, rowCount, columnCount, false);
					if (data != null) {		
						int vSpan = Math.max (1, Math.min (data.verticalSpan, rowCount));
						if (vSpan > 1) {
							if (data.heightHint != SWT.DEFAULT || !data.grabExcessVerticalSpace) {
								int spanHeight = 0, spanExpandCount = 0;
								for (int k=0; k<vSpan; k++) {
									spanHeight += heights [i-k];
									if (expandRow [i-k]) spanExpandCount++;
								}
								int h = data.cacheHeight - spanHeight - (vSpan - 1) * verticalSpacing; // + data.verticalIndent
								if (h > 0) {
									if (spanExpandCount == 0) {
										heights [i] += h;
									} else {
										int delta2 = h / spanExpandCount;
										int remainder2 = h % spanExpandCount, last2 = -1;
										for (int k = 0; k < vSpan; k++) {
											if (expandRow [i-k]) {
												heights [last2=i-k] += delta2;
											}
										}
										if (last2 > -1) heights [last2] += remainder2;	
									}
								}
							}
						}
					}
				}
			}
			if (count == 0) break;
			totalHeight = 0;
			for (int i=0; i<rowCount; i++) {
				totalHeight += heights [i];
			}
			delta = (availableHeight - totalHeight) / count;
			remainder = (availableHeight - totalHeight) % count;
			last = -1;
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
	
	int totalDefaultWidth = 0;
	int totalDefaultHeight = 0;
	for (int i=0; i<columnCount; i++) {
		totalDefaultWidth += widths [i];
	}
	for (int i=0; i<rowCount; i++) {
		totalDefaultHeight += heights [i];
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