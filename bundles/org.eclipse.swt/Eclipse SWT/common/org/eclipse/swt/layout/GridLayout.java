/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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

  	// Private variables.  Cached values used to cut down on grid calculations.
	Vector grid = new Vector();
	int [] pixelColumnWidths;
	int [] pixelRowHeights;
	int [] expandableColumns;
	int [] expandableRows;
	
/**
 * Constructs a new instance of this class.
 */
public GridLayout() {
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
public GridLayout(int numColumns, boolean makeColumnsEqualWidth) {
	this.numColumns = numColumns;
	this.makeColumnsEqualWidth = makeColumnsEqualWidth;
}

void adjustGridDimensions(Composite composite, boolean flushCache) {
	// Ensure that controls that span more than one row or column have enough space.
	for (int row = 0; row < grid.size(); row++) {
		for (int column = 0; column < numColumns; column++) {
			GridData spec = ((GridData[]) grid.elementAt(row))[column];
			if (spec.isItemData()) {
				// Widgets spanning columns.
				if (spec.hSpan > 1) {
					Control child = composite.getChildren()[spec.childIndex];
					Point extent = child.computeSize(spec.widthHint, spec.heightHint, flushCache);

					// Calculate the size of the control's spanned columns.
					int lastSpanIndex = column + spec.hSpan;
					int spannedSize = 0;
					for (int c = column; c < lastSpanIndex; c++) {
						spannedSize = spannedSize + pixelColumnWidths[c] + horizontalSpacing;
					}
					spannedSize = spannedSize - horizontalSpacing;

					// If the spanned columns are not large enough to display the control, adjust the column
					// sizes to account for the extra space that is needed.
					if (extent.x + spec.horizontalIndent > spannedSize) {
						int extraSpaceNeeded = extent.x + spec.horizontalIndent - spannedSize;
						int lastColumn = column + spec.hSpan - 1;
						int colWidth;
						if (makeColumnsEqualWidth) {
							// Evenly distribute the extra space amongst all of the columns.
							int columnExtra = extraSpaceNeeded / numColumns;
							int columnRemainder = extraSpaceNeeded % numColumns;
							for (int i = 0; i < pixelColumnWidths.length; i++) {
								colWidth = pixelColumnWidths[i] + columnExtra;
								pixelColumnWidths[i] = colWidth;
							}
							colWidth = pixelColumnWidths[lastColumn] + columnRemainder;
							pixelColumnWidths[lastColumn] = colWidth;
						} else {
							Vector localExpandableColumns = new Vector();
							for (int i = column; i <= lastColumn; i++) {
								for (int j = 0; j < expandableColumns.length; j++) {
									if (expandableColumns[j] == i) {
										localExpandableColumns.addElement(new Integer(i));
									}
								}
							}
							if (localExpandableColumns.size() > 0) {
								// If any of the control's columns grab excess space, allocate the space amongst those columns.
								int columnExtra = extraSpaceNeeded / localExpandableColumns.size();
								int columnRemainder = extraSpaceNeeded % localExpandableColumns.size();
								for (int i = 0; i < localExpandableColumns.size(); i++) {
									int expandableCol = ((Integer) localExpandableColumns.elementAt(i)).intValue();
									colWidth = pixelColumnWidths[expandableCol] + columnExtra;
									pixelColumnWidths[expandableCol] = colWidth;
								}
								colWidth = pixelColumnWidths[lastColumn] + columnRemainder;
								pixelColumnWidths[lastColumn] = colWidth;
							} else {
								// Add the extra space to the control's last column if none of its columns grab excess space.
								colWidth = pixelColumnWidths[lastColumn] + extraSpaceNeeded;
								pixelColumnWidths[lastColumn] = colWidth;
							}
						}
					}
				}

				// Widgets spanning rows.
				if (spec.verticalSpan > 1) {
					Control child = composite.getChildren()[spec.childIndex];
					Point extent = child.computeSize(spec.widthHint, spec.heightHint, flushCache);

					// Calculate the size of the control's spanned rows.
					int lastSpanIndex = row + spec.verticalSpan;
					int spannedSize = 0;
					for (int r = row; r < lastSpanIndex; r++) {
						spannedSize = spannedSize + pixelRowHeights[r] + verticalSpacing;
					}
					spannedSize = spannedSize - verticalSpacing;
					// If the spanned rows are not large enough to display the control, adjust the row
					// sizes to account for the extra space that is needed.
					if (extent.y > spannedSize) {
						int extraSpaceNeeded = extent.y - spannedSize;
						int lastRow = row + spec.verticalSpan - 1;
						int rowHeight;
						Vector localExpandableRows = new Vector();
						for (int i = row; i <= lastRow; i++) {
							for (int j = 0; j < expandableRows.length; j++) {
								if (expandableRows[j] == i) {
									localExpandableRows.addElement(new Integer(i));
								}
							}
						}
						if (localExpandableRows.size() > 0) {
							// If any of the control's rows grab excess space, allocate the space amongst those rows.
							int rowExtra = extraSpaceNeeded / localExpandableRows.size();
							int rowRemainder = extraSpaceNeeded % localExpandableRows.size();
							for (int i = 0; i < localExpandableRows.size(); i++) {
								int expandableRow = ((Integer) localExpandableRows.elementAt(i)).intValue();
								rowHeight = pixelRowHeights[expandableRow] + rowExtra;
								pixelRowHeights[expandableRow] = rowHeight;
							}
							rowHeight = pixelRowHeights[lastRow] + rowRemainder;
							pixelRowHeights[lastRow] = rowHeight;
						} else {
							// Add the extra space to the control's last row if no rows grab excess space.
							rowHeight = pixelRowHeights[lastRow] + extraSpaceNeeded;
							pixelRowHeights[lastRow] = rowHeight;
						}
					}
				}
			}
		}
	}
}
void calculateGridDimensions(Composite composite, boolean flushCache) {
	int maxWidth, childWidth, maxHeight, childHeight;
	
	//
	Control[] children = composite.getChildren();
	Point[] childSizes = new Point[children.length];
	pixelColumnWidths = new int[numColumns];
	pixelRowHeights = new int[grid.size()];
	
	// Loop through the grid by column to get the width that each column needs to be.
	// Each column will be as wide as its widest control.
	for (int column = 0; column < numColumns; column++) {
		maxWidth = 0;
		for (int row = 0; row < grid.size(); row++) {
			GridData spec = ((GridData[]) grid.elementAt(row))[column];
			if (spec.isItemData()) {
				Control child = children[spec.childIndex];
				childSizes[spec.childIndex] = child.computeSize(spec.widthHint, spec.heightHint, flushCache);
				childWidth = childSizes[spec.childIndex].x + spec.horizontalIndent;
				if (spec.hSpan == 1) {
					maxWidth = Math.max(maxWidth, childWidth);
				}
			}
		}
		// Cache the values for later use.
		pixelColumnWidths[column] = maxWidth;
	}

	// 
	if (makeColumnsEqualWidth) {
		maxWidth = 0;
		// Find the largest column size that is necessary and make each column that size.
		for (int i = 0; i < numColumns; i++) {
			maxWidth = Math.max(maxWidth, pixelColumnWidths[i]);
		}
		for (int i = 0; i < numColumns; i++) {
			pixelColumnWidths[i] = maxWidth;
		}
	}

	// Loop through the grid by row to get the height that each row needs to be.
	// Each row will be as high as its tallest control.
	for (int row = 0; row < grid.size(); row++) {
		maxHeight = 0;
		for (int column = 0; column < numColumns; column++) {
			GridData spec = ((GridData[]) grid.elementAt(row))[column];
			if (spec.isItemData()) {
				childHeight = childSizes[spec.childIndex].y;
				if (spec.verticalSpan == 1) {
					maxHeight = Math.max(maxHeight, childHeight);
				}
			}
		}
		// Cache the values for later use.
		pixelRowHeights[row] = maxHeight;
	}
}
void computeExpandableCells() {
	// If a control grabs excess horizontal space, the last column that the control spans
	// will be expandable.  Similarly, if a control grabs excess vertical space, the 
	// last row that the control spans will be expandable.
	Hashtable growColumns = new Hashtable();
	Hashtable growRows = new Hashtable();
	for (int col = 0; col < numColumns; col++) {
		for (int row = 0; row < grid.size(); row++) {
			GridData spec = ((GridData[]) grid.elementAt(row))[col];
			if (spec.grabExcessHorizontalSpace) {
				growColumns.put(new Integer(col + spec.hSpan - 1), new Object());
			}
			if (spec.grabExcessVerticalSpace) {
				growRows.put(new Integer(row + spec.verticalSpan - 1), new Object());
			}
		}
	}

	// Cache the values.  These values are used later during children layout.
	int i = 0;
	Enumeration enum = growColumns.keys();
	expandableColumns = new int[growColumns.size()];
	while (enum.hasMoreElements()) {
		expandableColumns[i] = ((Integer)enum.nextElement()).intValue();
		i = i + 1;
	}
	i = 0;
	enum = growRows.keys();
	expandableRows = new int[growRows.size()];
	while (enum.hasMoreElements()) {
		expandableRows[i] = ((Integer)enum.nextElement()).intValue();
		i = i + 1;
	}
}
Point computeLayoutSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	int totalMarginHeight, totalMarginWidth;
	int totalWidth, totalHeight;
	int cols, rows;

	// Initialize the grid and other cached information that help with the grid layout.
	if (grid.size() == 0) {
		createGrid(composite);
		calculateGridDimensions(composite, flushCache);
		computeExpandableCells();
		adjustGridDimensions(composite, flushCache);
	}

	//
	cols = numColumns;
	rows = grid.size();
	totalMarginHeight = marginHeight;
	totalMarginWidth = marginWidth;

	// The total width is the margin plus border width plus space between each column, 
	// plus the width of each column.
	totalWidth = (totalMarginWidth * 2) + ((cols - 1) * horizontalSpacing);

	//Add up the width of each column. 
	for (int i = 0; i < pixelColumnWidths.length; i++) {
		totalWidth = totalWidth + pixelColumnWidths[i];
	}

	// The total height is the margin plus border height, plus space between each row, 
	// plus the height of the tallest child in each row.
	totalHeight = (totalMarginHeight * 2) + ((rows - 1) * verticalSpacing);

	//Add up the height of each row. 
	for (int i = 0; i < pixelRowHeights.length; i++) {
		totalHeight = totalHeight + pixelRowHeights[i];
	}

	if (wHint != SWT.DEFAULT) totalWidth = wHint;
	if (hHint != SWT.DEFAULT) totalHeight = hHint;
	// The preferred extent is the width and height that will accomodate the grid's controls.
	return new Point(totalWidth, totalHeight);
}
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	Control[] children = composite.getChildren();
	int numChildren = children.length;

	if (numChildren == 0) return new Point(0,0);

	if (flushCache) {
		// Cause the grid and its related information to be calculated
		// again.
		grid.removeAllElements();
	}
	return computeLayoutSize(composite, wHint, hHint, flushCache);
}
Point getFirstEmptyCell(int row, int column) {
	GridData[] rowData = (GridData[]) grid.elementAt(row);
	while (column < numColumns && rowData[column] != null) {
		column++;
	}
	if (column == numColumns) {
		row++;
		column = 0;
		if (row  == grid.size()) {
			grid.addElement(emptyRow());
		}
		return getFirstEmptyCell(row, column);
	}
	return new Point(row, column);
}
Point getLastEmptyCell(int row, int column) {
	GridData[] rowData = (GridData[])grid.elementAt(row);
	while (column < numColumns && rowData[column] == null ) {
		column++;
	}
	return new Point(row, column - 1);
}	
Point getCell(int row, int column, int width, int height) {
	Point start = getFirstEmptyCell(row, column);
	Point end = getLastEmptyCell(start.x, start.y);
	if (end.y + 1 - start.y >= width) return start;
	GridData[] rowData = (GridData[]) grid.elementAt(start.x);
	for (int j = start.y; j < end.y + 1; j++) {
		GridData spacerSpec = new GridData();
		spacerSpec.isItemData = false;
		rowData[j] = spacerSpec;
	}
	return getCell(end.x, end.y, width, height);
}
void createGrid(Composite composite) {
	int row, column, rowFill, columnFill;
	Control[] children;
	GridData spacerSpec;

	// 
	children = composite.getChildren();

	// 
	grid.addElement(emptyRow());
	row = 0;
	column = 0;

	// Loop through the children and place their associated layout specs in the
	// grid.  Placement occurs left to right, top to bottom (i.e., by row).
	for (int i = 0; i < children.length; i++) {
		// Find the first available spot in the grid.
		Control child = children[i];
		GridData spec = (GridData) child.getLayoutData();
		if (spec == null) {
			spec = new GridData();
			child.setLayoutData(spec);
		}
		spec.hSpan = Math.min(spec.horizontalSpan, numColumns);
		Point p = getCell(row, column, spec.hSpan, spec.verticalSpan);
		row = p.x; column = p.y;

		// The vertical span for the item will be at least 1.  If it is > 1,
		// add other rows to the grid.
		for (int j = 2; j <= spec.verticalSpan; j++) {
			if (row + j > grid.size()) {
				grid.addElement(emptyRow());
			}
		}

		// Store the layout spec.  Also cache the childIndex.  NOTE: That we assume the children of a
		// composite are maintained in the order in which they are created and added to the composite.
		((GridData[]) grid.elementAt(row))[column] = spec;
		spec.childIndex = i;

		// Put spacers in the grid to account for the item's vertical and horizontal
		// span.
		rowFill = spec.verticalSpan - 1;
		columnFill = spec.hSpan - 1;
		for (int r = 1; r <= rowFill; r++) {
			for (int c = 0; c < spec.hSpan; c++) {
				spacerSpec = new GridData();
				spacerSpec.isItemData = false;
				((GridData[]) grid.elementAt(row + r))[column + c] = spacerSpec;
			}
		}
		for (int c = 1; c <= columnFill; c++) {
			for (int r = 0; r < spec.verticalSpan; r++) {
				spacerSpec = new GridData();
				spacerSpec.isItemData = false;
				((GridData[]) grid.elementAt(row + r))[column + c] = spacerSpec;
			}
		}
		column = column + spec.hSpan - 1;
	}

	// Fill out empty grid cells with spacers.
	for (int r = row; r < grid.size(); r++) {
		GridData[] rowData = (GridData[]) grid.elementAt(r);
		for (int c = 0; c < numColumns; c++) {
			if (rowData[c] == null) {
				spacerSpec = new GridData();
				spacerSpec.isItemData = false;
				rowData[c] = spacerSpec;
			}
		}
	}
}
GridData[] emptyRow() {
	GridData[] row = new GridData[numColumns];
	for (int i = 0; i < numColumns; i++) {
		row[i] = null;}
	return row;
}
protected void layout(Composite composite, boolean flushCache) {
	int[] columnWidths;
	int[] rowHeights;
	int rowSize, rowY, columnX;
	int compositeWidth, compositeHeight;
	int excessHorizontal, excessVertical;
	Control[] children;
	if (flushCache) {
		// Cause the grid and its related information to be calculated
		// again.
		grid.removeAllElements();
	}
	children = composite.getChildren();
	if (children.length == 0)
		return;

	//
	Point extent = computeSize(composite, SWT.DEFAULT, SWT.DEFAULT, flushCache);
	columnWidths = new int[numColumns];
	for (int i = 0; i < pixelColumnWidths.length; i++) {
		columnWidths[i] = pixelColumnWidths[i];
	}
	rowHeights = new int[grid.size()];
	for (int i = 0; i < pixelRowHeights.length; i++) {
		rowHeights[i] = pixelRowHeights[i];
	}
	int columnWidth = 0;
	rowSize = Math.max(1, grid.size());

	// 
	compositeWidth = extent.x;
	compositeHeight = extent.y;

	// Calculate whether or not there is any extra space or not enough space due to a resize 
	// operation.  Then allocate/deallocate the space to columns and rows that are expandable.  
	// If a control grabs excess space, its last column or row will be expandable.
	excessHorizontal = composite.getClientArea().width - compositeWidth;
	excessVertical = composite.getClientArea().height - compositeHeight;

	// Allocate/deallocate horizontal space.
	if (expandableColumns.length != 0) {
		int excess, remainder, last;
		int colWidth;
		excess = excessHorizontal / expandableColumns.length;
		remainder = excessHorizontal % expandableColumns.length;
		last = 0;
		for (int i = 0; i < expandableColumns.length; i++) {
			int expandableCol = expandableColumns[i];
			colWidth = columnWidths[expandableCol];
			colWidth = colWidth + excess;
			columnWidths[expandableCol] = colWidth;
			last = Math.max(last, expandableCol);
		}
		colWidth = columnWidths[last];
		colWidth = colWidth + remainder;
		columnWidths[last] = colWidth;
	}

	// Go through all specs in each expandable column and get the maximum specified
	// widthHint.  Use this as the minimumWidth for the column.
	for (int i = 0; i < expandableColumns.length; i++) {
		int expandableCol = expandableColumns[i];
		int colWidth = columnWidths[expandableCol];
		int minWidth = 0;
		for (int j = 0; j < grid.size(); j++) {
			GridData[] row = (GridData[]) grid.elementAt(j);
			GridData spec = row[expandableCol];
			if (spec.hSpan == 1) {
				minWidth = Math.max(minWidth, spec.widthHint);
			}
		}
		columnWidths[expandableCol] = Math.max(colWidth, minWidth);
	}
	// Allocate/deallocate vertical space.
	if (expandableRows.length != 0) {
		int excess, remainder, last;
		int rowHeight;
		excess = excessVertical / expandableRows.length;
		remainder = excessVertical % expandableRows.length;
		last = 0;
		for (int i = 0; i < expandableRows.length; i++) {
			int expandableRow = expandableRows[i];
			rowHeight = rowHeights[expandableRow];
			rowHeight = rowHeight + excess;
			rowHeights[expandableRow] = rowHeight;
			last = Math.max(last, expandableRow);
		}
		rowHeight = rowHeights[last];
		rowHeight = rowHeight + remainder;
		rowHeights[last] = rowHeight;
	}
	// Go through all specs in each expandable row and get the maximum specified
	// heightHint.  Use this as the minimumHeight for the row.
	for (int i = 0; i < expandableRows.length; i++) {
		int expandableRow = expandableRows[i];
		int rowHeight = rowHeights[expandableRow];
		int minHeight = 0;
		GridData[] row = (GridData[]) grid.elementAt(expandableRow);
		for (int j = 0; j < numColumns; j++) {
			GridData spec = row[j];
			if (spec.verticalSpan == 1) {
				minHeight = Math.max(minHeight, spec.heightHint);
			}
		}
		rowHeights[expandableRow] = Math.max(rowHeight, minHeight);
	}

	// Get the starting x and y.
	columnX = marginWidth + composite.getClientArea().x;
	rowY = marginHeight + composite.getClientArea().y;

	// Layout the control left to right, top to bottom.
	for (int r = 0; r < rowSize; r++) {
		int rowHeight = rowHeights[r];
		GridData[] row = (GridData[]) grid.elementAt(r);

		// 
		for (int c = 0; c < row.length; c++) {
			int spannedWidth = 0, spannedHeight = 0;
			int hAlign = 0, vAlign = 0;
			int widgetX = 0, widgetY = 0;
			int widgetW = 0, widgetH = 0;

			//
			GridData spec = (GridData) row[c];
			if (makeColumnsEqualWidth) {
				columnWidth = composite.getClientArea().width - 2 * (marginWidth)  - ((numColumns - 1) * horizontalSpacing);
				columnWidth = columnWidth / numColumns;
				for (int i = 0; i < columnWidths.length; i++) {
					columnWidths[i] = columnWidth;
				}
			} else {
				columnWidth = columnWidths[c];
			}

			//
			spannedWidth = columnWidth;
			for (int k = 1; k < spec.hSpan; k++) {
				if ((c + k) <= numColumns) {
					if (!makeColumnsEqualWidth) {
						columnWidth = columnWidths[c + k];
					}
					spannedWidth = spannedWidth + columnWidth + horizontalSpacing;
				}
			}

			//
			spannedHeight = rowHeight;
			for (int k = 1; k < spec.verticalSpan; k++) {
				if ((r + k) <= grid.size()) {
					spannedHeight = spannedHeight + rowHeights[r + k] + verticalSpacing;
				}
			}

			//
			if (spec.isItemData()) {
				Control child = children[spec.childIndex];
				Point childExtent = child.computeSize(spec.widthHint, spec.heightHint, flushCache);
				hAlign = spec.horizontalAlignment;
				widgetX = columnX;

				// Calculate the x and width values for the control.
				if (hAlign == GridData.CENTER || hAlign == SWT.CENTER) {
					widgetX = widgetX + (spannedWidth / 2) - (childExtent.x / 2);
				} else
					if (hAlign == GridData.END || hAlign == SWT.END || hAlign == SWT.RIGHT) {
						widgetX = widgetX + spannedWidth - childExtent.x - spec.horizontalIndent;
					} else {
						widgetX = widgetX + spec.horizontalIndent;
					}
				if (hAlign == GridData.FILL) {
					widgetW = spannedWidth - spec.horizontalIndent;
					widgetX = columnX + spec.horizontalIndent;
				} else {
					widgetW = childExtent.x;
				}

				// Calculate the y and height values for the control.
				vAlign = spec.verticalAlignment;
				widgetY = rowY;
				if (vAlign == GridData.CENTER || vAlign == SWT.CENTER) {
					widgetY = widgetY + (spannedHeight / 2) - (childExtent.y / 2);
				} else
					if (vAlign == GridData.END || vAlign == SWT.END || vAlign == SWT.BOTTOM) {
						widgetY = widgetY + spannedHeight - childExtent.y;
					}
				if (vAlign == GridData.FILL) {
					widgetH = spannedHeight;
					widgetY = rowY;
				} else {
					widgetH = childExtent.y;
				}
				// Place the control.
				child.setBounds(widgetX, widgetY, widgetW, widgetH);
			}
			// Update the starting x value.
			columnX = columnX + columnWidths[c] + horizontalSpacing;
		}
		// Update the starting y value and since we're starting a new row, reset the starting x value.
		rowY = rowY + rowHeights[r] + verticalSpacing;
		columnX = marginWidth + composite.getClientArea().x;
	}
}
}
