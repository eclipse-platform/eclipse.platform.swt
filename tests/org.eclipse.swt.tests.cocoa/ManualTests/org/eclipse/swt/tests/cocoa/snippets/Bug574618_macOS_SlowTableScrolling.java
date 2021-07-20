/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.cocoa.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class Bug574618_macOS_SlowTableScrolling {
	static final boolean USE_PAINTITEM   = true;    // Do realistic painting in SWT.PaintItem
	static final boolean USE_MEASUREITEM = true;  	// Do realistic measuring in SWT.MeasureItem
	static final boolean USE_ERASEITEM   = true;   	// Use SWT.EraseItem to disable default painting
	static final boolean USE_ITEM_TEXT   = false;  	// Use TableItem.setText() to provide item texts

	public static void main (String[] args) {
		final Display display = new Display ();

		final Shell shellTests = new Shell (display);
		shellTests.setLayout (new GridLayout (1, true));

		Label hint = new Label (shellTests, 0);
		hint.setText (
			"== On macOS 10.14 and 10.15 ==\n" +
			"1) Use JDK11 or lower, so that sdk is < 10.14, check with :\n" +
			"   otool -l /Volumes/macOS_Shared/JDK/jdk-11.0.3.jdk/Contents/Home/bin/java | grep sdk\n" +
			"2) Click button below to open test Table\n" +
			"3) Click some row and use 'arrow down' keyboard key to scroll Table\n" +
			"4) Without patch: scrolling becomes a lot slower at the bottom visible row.\n" +
			"   This happens because entire Table is repainted when scrolling.\n" +
			"5) With patch: only a portion of Table is painted in this case\n" +
			"\n" +
			"== On macOS 11 ==\n" +
			"1) Click button below to open test Table\n" +
			"2) Click some row and use 'arrow down' keyboard key to scroll Table\n" +
			"3) Without patch: snippet will report 512x512px areas painted on every key press\n" +
			"   The painting will be SLOW. Depending on the machine, it could feel painfully slow.\n" +
			"4) With patch: only the affected rows are painted (with areas like 512x42px)\n" +
			"   On my machine, that's some 8x faster\n" +
			"\n" +
			"== General ==\n" +
			"1) See console output for snippet's reports\n" +
			"2) macOS sometimes caches Table ahead in the direction of scrolling\n" +
			"   It will paint more items, but will allow to scroll table with mouse wheel without\n" +
			"   painting as long it's within cached area. This doesn't work with keyboard scrolling\n" +
			"   because it changes Table's contents, causing cache to be invalidated.\n" +
			""
		);

		Button button;

		button = new Button (shellTests, 0);
		button.setText ("Open test Table");
		button.addListener (SWT.Selection, event -> {
			final Shell shell = new Shell (shellTests, SWT.SHELL_TRIM);
			shell.setLayout (new GridLayout (1, true));

			final Table table = new Table (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			TableTester tester = new TableTester (table);

			final int numRows = 1000;
			final int numCols = 15;
			for (int iColumn = 0; iColumn < numCols; iColumn++) {
				new TableColumn (table, 0).setWidth (tester.CELL_W);
			}

			for (int iRow = 0; iRow < numRows; iRow++) {
				TableItem item = new TableItem (table, 0);

				String[] itemTexts;
				if (!USE_ITEM_TEXT) {
					itemTexts = new String[numCols];
					item.setData (itemTexts);
				}

				for (int iCol = 0; iCol < numCols; iCol++) {
					String text = "Test string " + iRow + ":" + iCol;

					if (!USE_ITEM_TEXT)
						itemTexts[iCol] = text;
					else
						item.setText (iCol, text);
				}
			}

			table.setSelection (0);

			// These listeners are used to measure performance
			table.addListener (SWT.Paint, tester);
			table.addListener (SWT.MeasureItem, tester);
			table.addListener (SWT.Selection, tester);

			if (USE_PAINTITEM)
				table.addListener (SWT.PaintItem, tester);

			if (USE_ERASEITEM)
				table.addListener (SWT.EraseItem, tester);

			shell.setSize (1000, 800);
			shell.open ();
		});

		hint = new Label (shellTests, 0);
		hint.setText (
			"In this test, none of Tables shall have focus rings"
		);

		button = new Button (shellTests, 0);
		button.setText ("Test Table focus rings");
		button.addListener (SWT.Selection, event -> {
			final Shell shell = new Shell (shellTests, SWT.SHELL_TRIM);
			shell.setLayout (new GridLayout (3, true));

			for (int iTable = 0; iTable < 3; iTable++) {
				Table table = new Table (shell, SWT.BORDER);
				for (int iRow = 0; iRow < 3; iRow++) {
					TableItem item = new TableItem (table, 0);
					item.setText ("TableItem");
				}
			}

			for (int iTree = 0; iTree < 3; iTree++) {
				Tree tree = new Tree (shell, SWT.BORDER);
				for (int iRow = 0; iRow < 3; iRow++) {
					TreeItem item = new TreeItem (tree, 0);
					item.setText ("TreeItem");
				}
			}

			shell.pack ();
			shell.open ();
		});

		shellTests.pack ();
		shellTests.open ();

		while (!shellTests.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}

		display.dispose ();
	}

	static class TableTester implements Listener {
		public int CELL_W = 110;
		public int CELL_H = 20;

		Table table;
		TablePerf perf = new TablePerf();

		public TableTester (Table table) {
			this.table = table;
		}

		@Override
		public void handleEvent (Event event) {
			if (SWT.Paint == event.type) {
				perf.onPaint (table.getSelectionIndex (), event.x, event.y, event.width, event.height);
			} else if (SWT.Selection == event.type) {
				perf.onSelection (table.getSelectionIndex ());
			} else if (SWT.EraseItem == event.type) {
				// Prevent macOS from doing its own painting
				event.detail &= ~SWT.FOREGROUND;
			} else if (SWT.PaintItem == event.type) {
				event.gc.drawText (getItemText (event), event.x, event.y);
			} else if (SWT.MeasureItem == event.type) {
				perf.onItemPaint (event);

				if (USE_MEASUREITEM) {
					// Even if 'GC.textExtent()' was called, still return hardcoded sizes
					// so that snippet paints the same number of items in both modes and
					// timings are easier to compare.
					event.gc.textExtent (getItemText (event));
				}

				event.width = CELL_W;
				event.height = CELL_H;
			}
		}

		String getItemText (Event event) {
			if (USE_ITEM_TEXT) {
				TableItem item = (TableItem) event.item;
				return item.getText (event.index);
			} else {
				String[] itemTexts = (String[]) event.item.getData ();
				return itemTexts[event.index];
			}
		}
	}

	static class CellRanges {
		private ArrayList<Point> cells = new ArrayList<Point> ();

		private static class Range {
			public int rowBeg;
			public int rowEnd;
			public int colBeg;
			public int colEnd;
		}

		public void add (int col, int row) {
			cells.add (new Point (col, row));
		}

		public void clear () {
			cells.clear ();
		}

		public int size () {
			return cells.size ();
		}

		public String composeList () {
			cells.sort ((lhs, rhs) -> {
				if (lhs.y != rhs.y)
					return (lhs.y - rhs.y);

				return (lhs.x - rhs.x);
			});

			return composeList (cells);
		}

		public static String composeList (ArrayList<Point> a_cells) {
			if (0 == a_cells.size ())
				return "";

			// Move away duplicates
			ArrayList<Point> cells = new ArrayList<Point>();
			String duplicatesList = "";
			{
				ArrayList<Point> duplicateCells = new ArrayList<Point>();
				cells.add (a_cells.get(0));
				for (int i = 1; i < a_cells.size(); i++) {
					Point prevCell = a_cells.get(i-1);
					Point currCell = a_cells.get(i);

					if ((currCell.x == prevCell.x) && (currCell.y == prevCell.y))
						duplicateCells.add (currCell);
					else
						cells.add (currCell);
				}

				if (duplicateCells.size() != 0)
					duplicatesList = composeList (duplicateCells);
			}

			// Compress columns
			ArrayList<Range> ranges1 = new ArrayList<Range>();
			{
				Range curRange = null;
				for (Point curItem : cells) {
					if (curRange != null) {
						if ((curRange.rowBeg == curItem.y) && (curRange.colEnd + 1 == curItem.x)) {
							curRange.colEnd = curItem.x;
							continue;
						} else {
							ranges1.add (curRange);
						}
					}

					curRange = new Range ();
					curRange.colBeg = curItem.x;
					curRange.colEnd = curItem.x;
					curRange.rowBeg = curItem.y;
					curRange.rowEnd = curItem.y;
				}
				ranges1.add (curRange);
			}

			// Compress rows
			ArrayList<Range> ranges2 = new ArrayList<Range>();
			{
				ranges1.sort ((lhs, rhs) -> {
					if (lhs.colBeg != rhs.colBeg)
						return (lhs.colBeg - rhs.colBeg);

					return (lhs.rowBeg - rhs.rowBeg);
				});

				Range curRange = null;
				for (Range curItem : ranges1) {
					if (curRange != null) {
						if ((curRange.rowEnd+1 == curItem.rowBeg) &&
							(curRange.colBeg   == curItem.colBeg) &&
							(curRange.colEnd   == curItem.colEnd)) {
							curRange.rowEnd = curItem.rowEnd;
							continue;
						} else {
							ranges2.add (curRange);
						}
					}

					curRange = new Range ();
					curRange.colBeg = curItem.colBeg;
					curRange.colEnd = curItem.colEnd;
					curRange.rowBeg = curItem.rowBeg;
					curRange.rowEnd = curItem.rowEnd;
				}
				ranges2.add (curRange);
			}

			// Print
			StringBuilder sb = new StringBuilder ();
			{
				ranges2.sort ((lhs, rhs) -> {
					if (lhs.rowBeg != rhs.rowBeg)
						return (lhs.rowBeg - rhs.rowBeg);

					return (lhs.colBeg - rhs.colBeg);
				});

				for (Range curItem : ranges2) {
					sb.append(" r");
					sb.append(curItem.rowBeg);
					if (curItem.rowBeg != curItem.rowEnd) {
						sb.append ("-");
						sb.append (curItem.rowEnd);
					}

					sb.append(":c");
					sb.append(curItem.colBeg);
					if (curItem.colBeg != curItem.colEnd) {
						sb.append ("-");
						sb.append (curItem.colEnd);
					}
				}
			}

			return duplicatesList + sb.toString ();
		}
	}

	static class TablePerf {
		private Timer tmrPaint = new Timer ();

		private Avg avgPaintTime = new Avg ();
		private double curPaintTime = 0;

		private boolean newBatch = true;
		private CellRanges batchItems = new CellRanges ();
		private CellRanges allItems = new CellRanges ();

		public void onItemPaint(Event event) {
			if (newBatch) {
				// I wasn't able to measure table scrolling time directly, because
				// macOS does drawing lazily, and will sometimes skip a turn.
				// The workaround is to assume measure time between first 'SWT.MeasureItem' and final 'SWT.Paint'.
				// Note that unlike 'SWT.MeasureItem', hooking 'SWT.PaintItem' causes SWT to do additional
				// work when painting, which will interfere with performance measurements.
				newBatch = false;
				batchItems.clear ();
				tmrPaint.restart ();
			}

			final TableItem item = (TableItem) event.item;
			final int row = item.getParent ().indexOf (item);
			final int col = event.index;
			batchItems.add (col, row);
			allItems.add (col, row);
		}

		public void onPaint (int a_id, int a_x, int a_y, int a_cx, int a_cy) {
			double batchTime = tmrPaint.getElapsed (true);

			System.out.format (
				"#%03d Paint     Time=%.2f Area=%20s Items=%3d {%s}\n",
				a_id,
				batchTime,
				String.format ("{%d,%d %dx%d}", a_x, a_y, a_cx, a_cy),
				batchItems.size (),
				batchItems.composeList ()
			);

			// The current paint cycle has ended
			newBatch = true;

			// macOS often paints in batches. The workaround is to sum them up.
			curPaintTime += batchTime;
		}

		public void onSelection (int a_id) {
			avgPaintTime.push (curPaintTime);

			System.out.format (
				"#%03d Selection Time=%.2f Avg=%.2f [%3d]            Items=%3d {%s}\n",
				a_id,
				curPaintTime,
				avgPaintTime.getAvg (),
				avgPaintTime.getCount (),
				allItems.size (),
				allItems.composeList ()
			);

			curPaintTime = 0;
			allItems.clear ();
		}
	}

	static class Timer {
		long lastNanoTime;

		public Timer () {
			restart ();
		}

		public double getElapsed (boolean restart) {
			final long currNanoTime = System.nanoTime ();
			final double result = (currNanoTime - lastNanoTime) / 1_000_000_000d;

			if (restart)
				lastNanoTime = currNanoTime;

			return result;
		}

		public void restart () {
			lastNanoTime = System.nanoTime ();
		}
	}

	static class Avg {
		int count;
		double sum;

		static final int WARMUP_VALUES = 5;

		void push (double value) {
			count++;
			if (getCount () <= 0)
				return;

			sum += value;
		}

		void reset () {
			count = 0;
			sum = 0;
		}

		double getAvg () {
			int count = getCount ();
			if (count <= 0)
				return 0;

			return (sum / count);
		}

		int getCount () {
			int result = count - WARMUP_VALUES;
			if (result < 0)
				return 0;

			return result;
		}
	}
}