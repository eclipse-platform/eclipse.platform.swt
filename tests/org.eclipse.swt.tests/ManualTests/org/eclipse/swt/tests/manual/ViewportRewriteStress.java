/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Manual stress harness for the array-backed SWT viewport rewrite.
 *
 * <p>The scenarios are original SWT tests inspired by the behavioral shapes in
 * long-standing SWT/Java2s examples: virtual SetData tables, lazy trees,
 * owner-paint, and ScrolledComposite. No third-party example source is copied.</p>
 */
public final class ViewportRewriteStress {
	private static final int TABLE_ROWS = 2_000_000;
	private static final int TREE_ROOTS = 10_000_000;
	private static final int TREE_CHILDREN = 10_000;
	private static final int CANVAS_ROWS = 10_000_000;
	private static final int ROW_HEIGHT = 22;
	private static final int OVERSCAN = 8;
	private static final String SCREENSHOT_DIR_PROPERTY = "swt.viewport.screenshots";
	private static final String SCREENSHOT_EXIT_PROPERTY = "swt.viewport.screenshots.exit";
	private static final String SCREENSHOT_NATIVE_PROPERTY = "swt.viewport.screenshots.native";
	private static final String SCREENSHOT_TREE_ROOT_KEY = "viewport.screenshot.root";
	private static final String SCREENSHOT_TREE_CHILD_KEY = "viewport.screenshot.child";
	private static final List<ScreenshotScenario> SCREENSHOT_SCENARIOS = new ArrayList<> ();

	private record ScreenshotScenario (String name, Control target, Runnable prepare) {
	}

	private record ScrollLayoutSnapshot (
			boolean horizontalVisible,
			boolean verticalVisible,
			boolean cornerVisible,
			int bodyWidth,
			int bodyHeight,
			int headerWidth,
			int visibleRows) {
	}

	private ViewportRewriteStress () {
	}

	public static void main (String[] args) {
		Display display = new Display ();
		WidgetSpy.NonDisposedWidgetTracker spyTracker = screenshotTrackingEnabled ()
				? createViewportSpyTracker () : null;
        if (spyTracker != null) {
            spyTracker.startTracking();
        }
		Shell shell = new Shell (display);
		shell.setText ("SWT viewport rewrite stress");
		shell.setLayout (new FillLayout ());
		shell.setSize (1200, 800);

		TabFolder tabs = new TabFolder (shell, SWT.NONE);
		createTableTab (tabs);
		createTreeTab (tabs);
		createScrolledCanvasTab (tabs);
		createLogicalViewportShellTab (tabs);

		shell.open ();
		scheduleScreenshotSuite (display, shell, spyTracker);
		while (!shell.isDisposed ()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
		}
        if (spyTracker != null) {
            spyTracker.stopTracking();
        }
		display.dispose ();
	}

	private static void createTableTab (TabFolder tabs) {
		Composite root = tab (tabs, "2M virtual Table");
		root.setLayout (new GridLayout (1, false));

		Label status = new Label (root, SWT.NONE);
		status.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		Table table = new Table (root, SWT.VIRTUAL | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
		table.setHeaderVisible (true);
		table.setLinesVisible (true);
		table.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		for (int i = 0; i < 4; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText ("Column " + i);
			column.setWidth (220);
		}

		AtomicLong setData = new AtomicLong ();
		AtomicLong paint = new AtomicLong ();
		table.addListener (SWT.SetData, event -> {
			setData.incrementAndGet ();
			int index = event.index;
			TableItem item = (TableItem) event.item;
			item.setText (new String[] {
					"row " + index,
					"group " + (index >>> 10),
					"hex " + Integer.toHexString (index),
					"mask " + (index & 63)
			});
            if ((index & 31) == 0) {
                item.setChecked(true);
            }
		});
		table.addListener (SWT.PaintItem, event -> paint.incrementAndGet ());
		table.setItemCount (TABLE_ROWS);

		Composite buttons = new Composite (root, SWT.NONE);
		buttons.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
		buttons.setLayout (new RowLayout ());
		jumpButton (buttons, "top", () -> redrawLocked (table, () -> table.setTopIndex (0)));
		jumpButton (buttons, "middle", () -> redrawLocked (table, () -> table.setTopIndex (TABLE_ROWS / 2)));
		jumpButton (buttons, "end", () -> redrawLocked (table, () -> table.setTopIndex (TABLE_ROWS - 1)));
		jumpButton (buttons, "select distant", () -> redrawLocked (table, () -> {
			table.setSelection (new int[] {0, TABLE_ROWS / 3, TABLE_ROWS / 2, TABLE_ROWS - 1});
			table.showSelection ();
		}));

		refreshStatus (root.getDisplay (), status, () ->
				"logical=" + table.getItemCount ()
				+ "  top=" + table.getTopIndex ()
				+ "  selected=" + table.getSelectionCount ()
				+ "  SetData=" + setData.get ()
				+ "  PaintItem=" + paint.get ());

		screenshotScenario ("table-top", root, () -> {
			selectTab (tabs, root);
			redrawLocked (table, () -> table.setTopIndex (0));
		});
		screenshotScenario ("table-middle", root, () -> {
			selectTab (tabs, root);
			redrawLocked (table, () -> table.setTopIndex (TABLE_ROWS / 2));
		});
		screenshotScenario ("table-end", root, () -> {
			selectTab (tabs, root);
			redrawLocked (table, () -> table.setTopIndex (TABLE_ROWS - 1));
		});
		screenshotScenario ("table-checked-selection", root, () -> {
			selectTab (tabs, root);
			redrawLocked (table, () -> {
				table.setTopIndex (0);
				table.setSelection (new int[] {0, 1, 32, 33, 64});
				table.showSelection ();
			});
		});
	}

	private static void createTreeTab (TabFolder tabs) {
		Composite root = tab (tabs, "10M x lazy Tree");
		root.setLayout (new GridLayout (1, false));

		Label status = new Label (root, SWT.NONE);
		status.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		Tree tree = new Tree (root, SWT.VIRTUAL | SWT.CHECK | SWT.MULTI | SWT.BORDER);
		tree.setHeaderVisible (true);
		tree.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		TreeColumn column = new TreeColumn (tree, SWT.NONE);
		column.setText ("Lazy logical tree");
		column.setWidth (700);

		AtomicLong setData = new AtomicLong ();
		AtomicLong paints = new AtomicLong ();
		tree.addListener (SWT.SetData, event -> {
			setData.incrementAndGet ();
			TreeItem item = (TreeItem) event.item;
			int depth = depth (item);
			item.setText ("depth=" + depth + " index=" + event.index);
            if (depth < 3 && item.getItemCount() == 0) {
                item.setItemCount(TREE_CHILDREN);
            }
            if ((event.index & 63) == 0) {
                item.setChecked(true);
            }
		});
		tree.addListener (SWT.PaintItem, event -> paints.incrementAndGet ());
		tree.setItemCount (TREE_ROOTS);

		tree.addListener (SWT.Collapse, event -> {
			TreeItem item = (TreeItem) event.item;
			item.setData ("viewport.collapsed.childCount", item.getItemCount ());
		});

		Composite buttons = new Composite (root, SWT.NONE);
		buttons.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
		buttons.setLayout (new RowLayout ());
		jumpButton (buttons, "first root", () -> redrawLocked (tree, () -> {
			TreeItem item = tree.getItem (0);
			tree.setTopItem (item);
			item.setExpanded (true);
		}));
		jumpButton (buttons, "middle root", () -> redrawLocked (tree, () -> {
			TreeItem item = tree.getItem (TREE_ROOTS / 2);
			tree.setTopItem (item);
			item.setExpanded (true);
		}));
		jumpButton (buttons, "last root", () -> redrawLocked (tree, () -> {
			TreeItem item = tree.getItem (TREE_ROOTS - 1);
			tree.setTopItem (item);
		}));
		jumpButton (buttons, "locked mutate", () -> redrawLocked (tree, () -> {
			TreeItem rootItem = tree.getItem (TREE_ROOTS / 4);
			rootItem.setExpanded (false);
			rootItem.setItemCount (TREE_CHILDREN);
			rootItem.setChecked (!rootItem.getChecked ());
			rootItem.setExpanded (true);
			tree.setTopItem (rootItem);
		}));

		refreshStatus (root.getDisplay (), status, () ->
				"logical roots=" + tree.getItemCount ()
				+ "  selection=" + tree.getSelectionCount ()
				+ "  SetData=" + setData.get ()
				+ "  PaintItem=" + paints.get ());

		screenshotScenario ("tree-first-expanded", root, () -> {
			selectTab (tabs, root);
			redrawLocked (tree, () -> {
				TreeItem item = tree.getItem (0);
				if (item.getItemCount () != TREE_CHILDREN) {
					throw new AssertionError ("stress branch must demand its logical children");
				}
				item.setExpanded (true);
				if (!item.getExpanded ()) {
					throw new AssertionError ("stress branch did not expand");
				}
				tree.setTopItem (item);
			});
		});
		screenshotScenario ("tree-middle-expanded", root, () -> {
			selectTab (tabs, root);
			redrawLocked (tree, () -> {
				TreeItem item = tree.getItem (TREE_ROOTS / 2);
				if (item.getItemCount () != TREE_CHILDREN) {
					throw new AssertionError ("stress branch must demand its logical children");
				}
				item.setExpanded (true);
				if (!item.getExpanded ()) {
					throw new AssertionError ("stress branch did not expand");
				}
				tree.setTopItem (item);
			});
		});
		screenshotScenario ("tree-middle-collapsed", root, () -> {
			selectTab (tabs, root);
			redrawLocked (tree, () -> {
				TreeItem item = tree.getItem (TREE_ROOTS / 2);
				item.setExpanded (false);
				tree.setTopItem (item);
			});
		});

		AtomicReference<TreeItem> screenshotRoot = new AtomicReference<> ();
		AtomicReference<TreeItem> screenshotChild = new AtomicReference<> ();
		Runnable preparePinnedTree = () -> {
			TreeItem branch = screenshotRoot.get ();
			if (branch == null || branch.isDisposed ()) {
				branch = tree.getItem (17);
				branch.setText ("screenshot root 17");
				branch.setItemCount (2_000);
				TreeItem child = branch.getItem (10);
				child.setText ("pinned child 10");
				child.setChecked (true);
				child.setGrayed (true);
				screenshotRoot.set (branch);
				screenshotChild.set (child);
				tree.setData (SCREENSHOT_TREE_ROOT_KEY, branch);
				tree.setData (SCREENSHOT_TREE_CHILD_KEY, child);
			}
		};
		screenshotScenario ("tree-pinned-expanded", root, () -> {
			selectTab (tabs, root);
			preparePinnedTree.run ();
			TreeItem branch = screenshotRoot.get ();
			TreeItem child = screenshotChild.get ();
			redrawLocked (tree, () -> {
				branch.setExpanded (true);
				tree.setTopItem (branch);
				tree.setSelection (child);
			});
		});
		screenshotScenario ("tree-pinned-collapsed", root, () -> {
			selectTab (tabs, root);
			preparePinnedTree.run ();
			TreeItem branch = screenshotRoot.get ();
			redrawLocked (tree, () -> {
				branch.setExpanded (false);
				tree.setTopItem (branch);
				tree.setSelection (branch);
			});
		});
		screenshotScenario ("tree-pinned-restored", root, () -> {
			selectTab (tabs, root);
			preparePinnedTree.run ();
			TreeItem branch = screenshotRoot.get ();
			TreeItem child = screenshotChild.get ();
			redrawLocked (tree, () -> {
				branch.setExpanded (true);
				tree.setTopItem (branch);
				tree.setSelection (child);
			});
		});
	}

	private static void createScrolledCanvasTab (TabFolder tabs) {
		Composite root = tab (tabs, "ScrolledComposite paint-only");
		root.setLayout (new GridLayout (1, false));

		Label status = new Label (root, SWT.NONE);
		status.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		AtomicLong headerPaints = new AtomicLong ();
		AtomicLong horizontalScrolls = new AtomicLong ();
		AtomicLong verticalScrolls = new AtomicLong ();
		AtomicInteger headerOriginX = new AtomicInteger ();

		Canvas header = new Canvas (root, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		GridData headerData = new GridData (SWT.FILL, SWT.CENTER, true, false);
		headerData.heightHint = 28;
		header.setLayoutData (headerData);
		header.addListener (SWT.Paint, event -> {
			headerPaints.incrementAndGet ();
			int x = -headerOriginX.get ();
			for (int column = 0; column < 8; column++) {
				int width = 200;
				event.gc.drawRectangle (x, 0, width, 27);
				event.gc.drawText ("column " + column, x + 8, 5, true);
				x += width;
			}
		});

		ScrolledComposite scroller = new ScrolledComposite (
				root, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scroller.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		Canvas canvas = new Canvas (scroller, SWT.DOUBLE_BUFFERED);
		int logicalHeight = Math.multiplyExact (CANVAS_ROWS, ROW_HEIGHT);
		canvas.setSize (1, 1);
		scroller.setContent (canvas);
		scroller.setLogicalContentSize (1600, logicalHeight);
		ScrollBar horizontal = scroller.getHorizontalBar ();
		if (horizontal != null) {
			horizontal.addListener (SWT.Selection, event -> {
				horizontalScrolls.incrementAndGet ();
				int next = horizontal.getSelection ();
                if (headerOriginX.getAndSet(next) != next) {
                    header.redraw();
                }
			});
		}
		ScrollBar vertical = scroller.getVerticalBar ();
		if (vertical != null) {
			vertical.addListener (SWT.Selection, event -> verticalScrolls.incrementAndGet ());
		}

		long [] selectionMasks = new long [(CANVAS_ROWS + Long.SIZE - 1) / Long.SIZE];
		AtomicLong paintEvents = new AtomicLong ();
		AtomicLong paintedRows = new AtomicLong ();

		canvas.addListener (SWT.Paint, event -> {
			paintEvents.incrementAndGet ();
			Rectangle clip = event.gc.getClipping ();
			Point origin = scroller.getOrigin ();
			int firstVisible = Math.max (0, (origin.y + clip.y) / ROW_HEIGHT);
			int lastVisible = Math.min (CANVAS_ROWS,
					(origin.y + clip.y + clip.height + ROW_HEIGHT - 1) / ROW_HEIGHT);
			int firstPaint = Math.max (0, firstVisible - OVERSCAN);
			int lastPaint = Math.min (CANVAS_ROWS, lastVisible + OVERSCAN);
			paintedRows.addAndGet (lastPaint - firstPaint);

			for (int row = firstPaint; row < lastPaint; row++) {
				int y = row * ROW_HEIGHT - origin.y;
				if (selected (selectionMasks, row)) {
					event.gc.fillRectangle (0, y, canvas.getClientArea ().width, ROW_HEIGHT);
				}
				event.gc.drawText ("logical row " + row, 8 - origin.x, y + 3, true);
				event.gc.drawLine (-origin.x, y + ROW_HEIGHT - 1,
						1600 - origin.x, y + ROW_HEIGHT - 1);
			}
		});

		canvas.addListener (SWT.MouseDown, event -> {
			Point origin = scroller.getOrigin ();
			int row = (origin.y + event.y) / ROW_HEIGHT;
			if (0 <= row && row < CANVAS_ROWS) {
				toggle (selectionMasks, row);
				canvas.redraw ();
			}
		});

		Composite buttons = new Composite (root, SWT.NONE);
		buttons.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
		buttons.setLayout (new RowLayout ());
		jumpButton (buttons, "top", () -> jumpVertical (scroller, canvas, 0));
		jumpButton (buttons, "middle", () -> jumpVertical (scroller, canvas, CANVAS_ROWS / 2));
		jumpButton (buttons, "end", () -> jumpVertical (scroller, canvas, CANVAS_ROWS - 1));
		jumpButton (buttons, "x=600", () -> jumpHorizontal (scroller, canvas, header, headerOriginX, 600));
		jumpButton (buttons, "x=0", () -> jumpHorizontal (scroller, canvas, header, headerOriginX, 0));

		refreshStatus (root.getDisplay (), status, () -> {
			Point origin = scroller.getOrigin ();
			int first = Math.max (0, origin.y / ROW_HEIGHT);
			int visible = Math.max (1, scroller.getClientArea ().height / ROW_HEIGHT + 1);
			int paintStart = Math.max (0, first - OVERSCAN);
			int paintEnd = Math.min (CANVAS_ROWS, first + visible + OVERSCAN);
			return "logical=" + CANVAS_ROWS
					+ "  viewport rows=[" + first + "," + Math.min (CANVAS_ROWS, first + visible) + ")"
					+ "  paint+overscan=[" + paintStart + "," + paintEnd + ")"
					+ "  bodyPaints=" + paintEvents.get ()
					+ "  headerPaints=" + headerPaints.get ()
					+ "  hScrolls=" + horizontalScrolls.get ()
					+ "  vScrolls=" + verticalScrolls.get ()
					+ "  rowsAttempted=" + paintedRows.get ()
					+ "  stateBytes~=" + (selectionMasks.length * Long.BYTES);
		});

		screenshotScenario ("scrolled-top", root, () -> {
			selectTab (tabs, root);
			jumpVertical (scroller, canvas, 0);
		});
		screenshotScenario ("scrolled-middle", root, () -> {
			selectTab (tabs, root);
			jumpVertical (scroller, canvas, CANVAS_ROWS / 2);
		});
		screenshotScenario ("scrolled-middle-x600", root, () -> {
			selectTab (tabs, root);
			jumpVertical (scroller, canvas, CANVAS_ROWS / 2);
			jumpHorizontal (scroller, canvas, header, headerOriginX, 600);
		});
	}


	private static void createLogicalViewportShellTab (TabFolder tabs) {
		Composite root = tab (tabs, "Logical viewport + chrome planes");
		root.setLayout (new GridLayout (1, false));

		Label status = new Label (root, SWT.NONE);
		status.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		AtomicInteger horizontalOrigin = new AtomicInteger ();
		AtomicInteger topRow = new AtomicInteger ();
		AtomicInteger logicalRows = new AtomicInteger (CANVAS_ROWS);
		AtomicReference<ScrollLayoutSnapshot> scrollLayout = new AtomicReference<> ();
		AtomicLong bodyPaints = new AtomicLong ();
		AtomicLong headerPaints = new AtomicLong ();
		AtomicLong verticalEvents = new AtomicLong ();
		AtomicLong horizontalEvents = new AtomicLong ();
		AtomicLong retainedCommands = new AtomicLong ();
		final int logicalWidth = 8 * 240;

		ViewportPaintGraph headerGraph = new ViewportPaintGraph ();
		for (int column = 0; column < 8; column++) {
			int x = column * 240;
			headerGraph.drawRectangle (headerGraph.root (), x, 0, 240, 27);
			headerGraph.text (headerGraph.root (), "logical column " + column, x + 8, 5, true);
		}

		ViewportPaintGraph rowStrokeGraph = new ViewportPaintGraph ();
		int rowStroke = rowStrokeGraph.template ();
		for (int column = 1; column < 8; column++) {
			int x = column * 240;
			rowStrokeGraph.line (rowStroke, x, 0, x, ROW_HEIGHT);
		}
		rowStrokeGraph.line (rowStroke, 0, ROW_HEIGHT - 1, logicalWidth, ROW_HEIGHT - 1);

		Canvas header = new Canvas (root, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		GridData headerData = new GridData (SWT.FILL, SWT.CENTER, true, false);
		headerData.heightHint = 28;
		header.setLayoutData (headerData);
		header.addListener (SWT.Paint, event -> {
			headerPaints.incrementAndGet ();
			ViewportPaintGraph.ReplayStats stats = headerGraph.replay (
					event.gc,
					ViewportPaintGraph.Affine.translation (-horizontalOrigin.get (), 0),
					event.gc.getClipping ());
			retainedCommands.addAndGet (stats.drawnCommands ());
		});

		Canvas body = new Canvas (
				root, SWT.DOUBLE_BUFFERED | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		body.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		long [] selectionMasks = new long [(CANVAS_ROWS + Long.SIZE - 1) / Long.SIZE];

		ScrollBar vertical = body.getVerticalBar ();
		ScrollBar horizontal = body.getHorizontalBar ();

		Runnable configureScrollbars = () -> {
			Rectangle client = body.getClientArea ();
			Point horizontalSize = horizontal.getSize ();
			Point verticalSize = vertical.getSize ();
			int outerWidth = client.width + (vertical.getVisible () ? verticalSize.x : 0);
			int outerHeight = client.height + (horizontal.getVisible () ? horizontalSize.y : 0);
			ScrollLayoutSnapshot layout = solveViewportScrollLayout (
					outerWidth, outerHeight, 0,
					logicalRows.get (), ROW_HEIGHT, logicalWidth,
					Math.max (0, horizontalSize.y), Math.max (0, verticalSize.x));
			scrollLayout.set (layout);
			horizontal.setVisible (layout.horizontalVisible ());
			vertical.setVisible (layout.verticalVisible ());

			int rows = Math.max (1, logicalRows.get ());
			int visibleRows = Math.max (1, Math.min (rows, layout.visibleRows ()));
			int top = Math.min (topRow.get (), Math.max (0, rows - visibleRows));
			vertical.setValues (top, 0, rows, visibleRows, 1, visibleRows);

			int visibleWidth = Math.max (1, Math.min (logicalWidth, layout.bodyWidth ()));
			int x = Math.min (horizontalOrigin.get (), Math.max (0, logicalWidth - visibleWidth));
			horizontal.setValues (x, 0, logicalWidth, visibleWidth, 24, visibleWidth);
			topRow.set (vertical.getSelection ());
			horizontalOrigin.set (horizontal.getSelection ());
		};
		body.addListener (SWT.Resize, event -> configureScrollbars.run ());

		vertical.addListener (SWT.Selection, event -> {
			verticalEvents.incrementAndGet ();
			topRow.set (vertical.getSelection ());
			body.redraw ();
			// Deliberately do not redraw the header plane on vertical-only scroll.
		});
		horizontal.addListener (SWT.Selection, event -> {
			horizontalEvents.incrementAndGet ();
			horizontalOrigin.set (horizontal.getSelection ());
			body.redraw ();
			header.redraw ();
		});

		body.addListener (SWT.Paint, event -> {
			bodyPaints.incrementAndGet ();
			Rectangle client = body.getClientArea ();
			int firstVisible = topRow.get ();
			int rowLimit = logicalRows.get ();
			int visibleRows = Math.max (1,
					Math.min (Math.max (0, rowLimit - firstVisible),
							(client.height + ROW_HEIGHT - 1) / ROW_HEIGHT));
			int firstPaint = Math.max (0, firstVisible - OVERSCAN);
			int lastPaint = Math.min (rowLimit, firstVisible + visibleRows + OVERSCAN);
			int xOffset = horizontalOrigin.get ();

			for (int row = firstPaint; row < lastPaint; row++) {
				int screenY = (row - firstVisible) * ROW_HEIGHT;
                if (screenY + ROW_HEIGHT < 0 || screenY > client.height) {
                    continue;
                }
				if (selected (selectionMasks, row)) {
					event.gc.fillRectangle (0, screenY, client.width, ROW_HEIGHT);
				}
				event.gc.drawText ("logical row " + row, 8 - xOffset, screenY + 3, true);
				ViewportPaintGraph.ReplayStats stats = rowStrokeGraph.replayTemplate (
						event.gc, rowStroke,
						ViewportPaintGraph.Affine.translation (-xOffset, screenY),
						event.gc.getClipping ());
				retainedCommands.addAndGet (stats.drawnCommands ());
			}
		});

		body.addListener (SWT.MouseDown, event -> {
			int row = topRow.get () + Math.max (0, event.y / ROW_HEIGHT);
			if (row < logicalRows.get ()) {
				toggle (selectionMasks, row);
				body.redraw (0, (row - topRow.get ()) * ROW_HEIGHT,
						body.getClientArea ().width, ROW_HEIGHT, false);
			}
		});

		Composite buttons = new Composite (root, SWT.NONE);
		buttons.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
		buttons.setLayout (new RowLayout ());
		jumpButton (buttons, "top", () -> {
			vertical.setSelection (0);
			topRow.set (vertical.getSelection ());
			body.redraw ();
		});
		jumpButton (buttons, "middle", () -> {
			vertical.setSelection (logicalRows.get () / 2);
			topRow.set (vertical.getSelection ());
			body.redraw ();
		});
		jumpButton (buttons, "end", () -> {
			vertical.setSelection (logicalRows.get ());
			topRow.set (vertical.getSelection ());
			body.redraw ();
		});
		jumpButton (buttons, "x=720", () -> {
			horizontal.setSelection (720);
			horizontalOrigin.set (horizontal.getSelection ());
			body.redraw ();
			header.redraw ();
		});
		jumpButton (buttons, "x=0", () -> {
			horizontal.setSelection (0);
			horizontalOrigin.set (horizontal.getSelection ());
			body.redraw ();
			header.redraw ();
		});

		root.getDisplay ().asyncExec (configureScrollbars);
		refreshStatus (root.getDisplay (), status, () -> {
			Rectangle client = body.getClientArea ();
			ScrollLayoutSnapshot layout = scrollLayout.get ();
			int visibleRows = layout == null ? 0 : layout.visibleRows ();
			long estimatedPixelHeight = (long) logicalRows.get () * ROW_HEIGHT;
			return "logicalRows=" + logicalRows.get ()
					+ "  logicalWidth=" + logicalWidth
					+ "  sampleRow=" + ROW_HEIGHT + "px"
					+ "  estimatedPixels=" + estimatedPixelHeight
					+ "  nativeBodyHeight=" + client.height
					+ "  topRow=" + topRow.get ()
					+ "  visibleRows=" + visibleRows
					+ "  hVisible=" + horizontal.getVisible ()
					+ "  vVisible=" + vertical.getVisible ()
					+ "  corner=" + (layout != null && layout.cornerVisible ())
					+ "  bodyPaints=" + bodyPaints.get ()
					+ "  headerPaints=" + headerPaints.get ()
					+ "  vEvents=" + verticalEvents.get ()
					+ "  hEvents=" + horizontalEvents.get ()
					+ "  retainedGeometry="
					+ (headerGraph.geometryNodeCount () + rowStrokeGraph.geometryNodeCount ())
					+ "  retainedDraws=" + retainedCommands.get ();
		});

		screenshotScenario ("logical-top", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			configureScrollbars.run ();
			vertical.setSelection (0);
			topRow.set (vertical.getSelection ());
			horizontal.setSelection (0);
			horizontalOrigin.set (horizontal.getSelection ());
			header.redraw ();
			body.redraw ();
		});
		screenshotScenario ("logical-middle", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			configureScrollbars.run ();
			vertical.setSelection (CANVAS_ROWS / 2);
			topRow.set (vertical.getSelection ());
			horizontal.setSelection (0);
			horizontalOrigin.set (horizontal.getSelection ());
			header.redraw ();
			body.redraw ();
		});
		screenshotScenario ("logical-middle-x720", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			configureScrollbars.run ();
			vertical.setSelection (CANVAS_ROWS / 2);
			topRow.set (vertical.getSelection ());
			horizontal.setSelection (720);
			horizontalOrigin.set (horizontal.getSelection ());
			header.redraw ();
			body.redraw ();
		});
		screenshotScenario ("logical-resize-narrow", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			root.getShell ().setSize (640, 420);
			root.getShell ().layout (true, true);
			configureScrollbars.run ();
		});
		screenshotScenario ("logical-resize-wide", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			root.getShell ().setSize (2400, 900);
			root.getShell ().layout (true, true);
			configureScrollbars.run ();
		});
		screenshotScenario ("logical-vertical-only", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (CANVAS_ROWS);
			root.getShell ().setSize (2400, 900);
			root.getShell ().layout (true, true);
			configureScrollbars.run ();
			header.redraw ();
			body.redraw ();
		});
		screenshotScenario ("logical-no-scrollbars", root, () -> {
			selectTab (tabs, root);
			logicalRows.set (12);
			topRow.set (0);
			horizontalOrigin.set (0);
			root.getShell ().setSize (2400, 900);
			root.getShell ().layout (true, true);
			configureScrollbars.run ();
			header.redraw ();
			body.redraw ();
		});
	}

	private static boolean screenshotTrackingEnabled () {
		String directory = System.getProperty (SCREENSHOT_DIR_PROPERTY);
		return directory != null && !directory.isBlank ();
	}

	private static WidgetSpy.NonDisposedWidgetTracker createViewportSpyTracker () {
		WidgetSpy.NonDisposedWidgetTracker tracker = new WidgetSpy.NonDisposedWidgetTracker ();
		tracker.setTrackedTypes (List.of (TreeItem.class, TableItem.class));
		return tracker;
	}

	private static void screenshotScenario (String name, Control target, Runnable prepare) {
		SCREENSHOT_SCENARIOS.add (new ScreenshotScenario (name, target, prepare));
	}

	private static void scheduleScreenshotSuite (
			Display display, Shell shell, WidgetSpy.NonDisposedWidgetTracker tracker) {
		String directory = System.getProperty (SCREENSHOT_DIR_PROPERTY);
        if (directory == null || directory.isBlank() || SCREENSHOT_SCENARIOS.isEmpty()) {
            return;
        }
		Path output = Path.of (directory);
		try {
			Files.createDirectories (output);
		} catch (IOException failure) {
			throw new IllegalStateException ("Cannot create screenshot directory " + output, failure);
		}
		display.asyncExec (() -> captureNext (display, shell, output, tracker, 0));
	}

	private static void captureNext (
			Display display, Shell shell, Path output,
			WidgetSpy.NonDisposedWidgetTracker tracker, int index) {
		if (index >= SCREENSHOT_SCENARIOS.size ()) {
            if (Boolean.getBoolean(SCREENSHOT_EXIT_PROPERTY) && !shell.isDisposed()) {
                shell.dispose();
            }
			return;
		}
		ScreenshotScenario scenario = SCREENSHOT_SCENARIOS.get (index);
		if (scenario.target ().isDisposed ()) {
			captureNext (display, shell, output, tracker, index + 1);
			return;
		}
		scenario.prepare ().run ();
		scenario.target ().getShell ().layout (true, true);
		scenario.target ().redraw ();
		scenario.target ().update ();
		display.timerExec (150, () -> {
			if (!scenario.target ().isDisposed ()) {
				Path png = output.resolve (scenario.name () + ".png");
				capturePng (scenario.target (), png);
				Path nativePng = null;
				String nativeCaptureError = null;
				if (Boolean.getBoolean (SCREENSHOT_NATIVE_PROPERTY)) {
					nativePng = output.resolve (scenario.name () + "-native.png");
					try {
						captureNativeWindowPng (scenario.target (), nativePng);
					} catch (RuntimeException failure) {
						nativeCaptureError = failure.toString ();
						nativePng = null;
					}
				}
				writeSpySnapshot (
						scenario.name (), scenario.target (), tracker, png, nativePng,
						nativeCaptureError, output.resolve (scenario.name () + ".txt"));
			}
			captureNext (display, shell, output, tracker, index + 1);
		});
	}

	private static void capturePng (Control control, Path path) {
		Point size = control.getSize ();
        if (size.x <= 0 || size.y <= 0) {
            return;
        }
		Image image = new Image (control.getDisplay (), size.x, size.y);
		GC gc = new GC (image);
		try {
			if (!control.print (gc)) {
				gc.dispose ();
				gc = new GC (control);
				gc.copyArea (image, 0, 0);
			}
		} finally {
			gc.dispose ();
		}
		try {
			ImageLoader loader = new ImageLoader ();
			loader.data = new ImageData[] {image.getImageData ()};
			loader.save (path.toString (), SWT.IMAGE_PNG);
		} finally {
			image.dispose ();
		}
	}

	private static void captureNativeWindowPng (Control control, Path path) {
		Shell shell = control.getShell ();
		Rectangle bounds = shell.getBounds ();
        if (bounds.width <= 0 || bounds.height <= 0) {
            return;
        }
		Display display = control.getDisplay ();
		Image image = new Image (display, bounds.width, bounds.height);
		GC gc = new GC (display);
		try {
			/*
			 * Java2s/SWT Snippet-style screen capture: the Display GC reads native
			 * pixels, so this complementary image includes OS/native chrome that
			 * Control.print() may not. Wayland may reject or blank desktop capture;
			 * therefore this path is opt-in diagnostic evidence, not a CI oracle.
			 */
			gc.copyArea (image, bounds.x, bounds.y);
		} finally {
			gc.dispose ();
		}
		try {
			ImageLoader loader = new ImageLoader ();
			loader.data = new ImageData[] {image.getImageData ()};
			loader.save (path.toString (), SWT.IMAGE_PNG);
		} finally {
			image.dispose ();
		}
	}

	private static void writeSpySnapshot (
			String scenario, Control target,
			WidgetSpy.NonDisposedWidgetTracker tracker, Path png, Path nativePng,
			String nativeCaptureError, Path path) {
		StringBuilder out = new StringBuilder (4096);
		out.append ("scenario=").append (scenario).append ('\n');
		out.append ("platform=").append (SWT.getPlatform ()).append ('\n');
		out.append ("screenshot=").append (png.getFileName ()).append ('\n');
		out.append ("screenshot.sha256=").append (sha256 (png)).append ('\n');
		if (nativePng != null && Files.exists (nativePng)) {
			out.append ("nativeScreenshot=").append (nativePng.getFileName ()).append ('\n');
			out.append ("nativeScreenshot.sha256=").append (sha256 (nativePng)).append ('\n');
		} else if (nativeCaptureError != null) {
			out.append ("nativeScreenshot.error=").append (nativeCaptureError).append ('\n');
		}
		appendControlSnapshot (out, target, "");
		if (tracker != null) {
			Map<Widget, Error> widgets = tracker.getNonDisposedWidgets ();
			long tables = widgets.keySet ().stream ().filter (TableItem.class::isInstance).count ();
			long trees = widgets.keySet ().stream ().filter (TreeItem.class::isInstance).count ();
			long localTables = widgets.keySet ().stream ()
					.filter (TableItem.class::isInstance)
					.map (TableItem.class::cast)
					.filter (item -> belongsTo (target, item.getParent ()))
					.count ();
			long localTrees = widgets.keySet ().stream ()
					.filter (TreeItem.class::isInstance)
					.map (TreeItem.class::cast)
					.filter (item -> belongsTo (target, item.getParent ()))
					.count ();
			out.append ("spy.liveTableItems=").append (tables).append ('\n');
			out.append ("spy.liveTreeItems=").append (trees).append ('\n');
			out.append ("spy.liveTrackedItems=").append (widgets.size ()).append ('\n');
			out.append ("spy.localTableItems=").append (localTables).append ('\n');
			out.append ("spy.localTreeItems=").append (localTrees).append ('\n');
		}
		try {
			Files.writeString (path, out, StandardCharsets.UTF_8);
		} catch (IOException failure) {
			throw new IllegalStateException ("Cannot write viewport spy snapshot " + path, failure);
		}
	}

	private static boolean belongsTo (Control root, Control control) {
		for (Control current = control; current != null; current = current.getParent ()) {
            if (current == root) {
                return true;
            }
		}
		return false;
	}

	private static String sha256 (Path path) {
		try {
			byte[] digest = MessageDigest.getInstance ("SHA-256").digest (Files.readAllBytes (path));
			return java.util.HexFormat.of ().formatHex (digest);
		} catch (IOException | NoSuchAlgorithmException failure) {
			throw new IllegalStateException ("Cannot hash screenshot " + path, failure);
		}
	}

	private static void appendControlSnapshot (StringBuilder out, Control control, String indent) {
        if (control == null || control.isDisposed()) {
            return;
        }
		Composite parent = control.getParent ();
		Object layoutData = control.getLayoutData ();
		out.append (indent).append ("control=").append (control.getClass ().getName ())
				.append (" style=0x").append (Integer.toHexString (control.getStyle ()))
				.append (" parent=").append (parent == null ? "<none>" : parent.getClass ().getName ())
				.append (" bounds=").append (control.getBounds ())
				.append (" client=").append (control instanceof Scrollable scrollable ? scrollable.getClientArea () : "<not scrollable>")
				.append (" visible=").append (control.getVisible ())
				.append (" enabled=").append (control.getEnabled ())
				.append (" layoutData=").append (layoutData == null ? "<none>" : layoutData.getClass ().getName ())
				.append ('\n');
		if (control instanceof Scrollable scrollable) {
			appendScrollBarSnapshot (out, indent + "  h.", scrollable.getHorizontalBar ());
			appendScrollBarSnapshot (out, indent + "  v.", scrollable.getVerticalBar ());
		}
		if (control instanceof Table table) {
			out.append (indent).append ("  table.itemCount=").append (table.getItemCount ())
					.append (" topIndex=").append (table.getTopIndex ())
					.append (" selectionCount=").append (table.getSelectionCount ())
					.append (" itemHeight=").append (table.getItemHeight ())
					.append (" columns=").append (table.getColumnCount ())
					.append (" headerVisible=").append (table.getHeaderVisible ())
					.append ('\n');
		} else if (control instanceof Tree tree) {
			TreeItem top = tree.getTopItem ();
			out.append (indent).append ("  tree.rootCount=").append (tree.getItemCount ())
					.append (" selectionCount=").append (tree.getSelectionCount ())
					.append (" itemHeight=").append (tree.getItemHeight ())
					.append (" columns=").append (tree.getColumnCount ())
					.append (" headerVisible=").append (tree.getHeaderVisible ())
					.append (" topRootIndex=").append (top == null ? -1 : tree.indexOf (top))
					.append ('\n');
			appendTreeViewportInternals (out, tree, indent + "  ");
		} else if (control instanceof ScrolledComposite scrolled) {
			out.append (indent).append ("  scrolled.origin=").append (scrolled.getOrigin ())
					.append (" minWidth=").append (scrolled.getMinWidth ())
					.append (" minHeight=").append (scrolled.getMinHeight ())
					.append (" expandH=").append (scrolled.getExpandHorizontal ())
					.append (" expandV=").append (scrolled.getExpandVertical ())
					.append ('\n');
		}
		if (control instanceof Composite composite) {
			Layout layout = composite.getLayout ();
			out.append (indent).append ("  layout=")
					.append (layout == null ? "<none>" : layout.getClass ().getName ())
					.append ('\n');
			for (Control child : composite.getChildren ()) {
				appendControlSnapshot (out, child, indent + "  ");
			}
		}
	}

	private static void appendTreeViewportInternals (
			StringBuilder out, Tree tree, String indent) {
		Object rootValue = tree.getData (SCREENSHOT_TREE_ROOT_KEY);
		Object childValue = tree.getData (SCREENSHOT_TREE_CHILD_KEY);
		if (rootValue instanceof TreeItem rootItem && !rootItem.isDisposed ()) {
			out.append (indent).append ("tree.screenshotRoot childCount=")
					.append (rootItem.getItemCount ())
					.append (" expanded=").append (rootItem.getExpanded ())
					.append (" checked=").append (rootItem.getChecked ())
					.append (" grayed=").append (rootItem.getGrayed ())
					.append ('\n');
			appendReflectiveValue (
					out, indent, "tree.screenshotRoot.nativeResidentChildren",
					tree, "virtualResidentChildCount",
					new Class<?>[] {TreeItem.class}, new Object[] {rootItem});
		}
		if (childValue instanceof TreeItem childItem && !childItem.isDisposed ()) {
			out.append (indent).append ("tree.screenshotChild checked=")
					.append (childItem.getChecked ())
					.append (" grayed=").append (childItem.getGrayed ())
					.append (" expanded=").append (childItem.getExpanded ())
					.append (" text=").append (childItem.getText ())
					.append ('\n');
		}
		appendReflectiveValue (
				out, indent, "tree.nativeResidentRoots",
				tree, "virtualResidentChildCount", new Class<?>[] {long.class}, new Object[] {0L});
		appendReflectiveValue (
				out, indent, "tree.virtualVisibleRows",
				tree, "virtualVisibleRowCount", new Class<?>[0], new Object[0]);
		appendReflectiveFieldMethod (
				out, indent, "tree.topology.materializedCount",
				tree, "virtualTopology", "materializedCount");
		appendReflectiveFieldMethod (
				out, indent, "tree.viewport.topRow",
				tree, "virtualViewport", "topRow");
		appendReflectiveFieldMethod (
				out, indent, "tree.viewport.visibleRows",
				tree, "virtualViewport", "visibleRows");
		appendReflectiveFieldMethod (
				out, indent, "tree.viewport.firstPaintRow",
				tree, "virtualViewport", "firstPaintRow");
		appendReflectiveFieldMethod (
				out, indent, "tree.viewport.paintRowCount",
				tree, "virtualViewport", "paintRowCount");
	}

	private static ScrollLayoutSnapshot solveViewportScrollLayout (
			int outerWidth, int outerHeight, int headerHeight,
			long logicalRows, int sampleRowHeight, long logicalContentWidth,
			int horizontalBarHeight, int verticalBarWidth) {
		try {
			Class<?> type = Class.forName ("org.eclipse.swt.widgets.ViewportScrollLayout");
			Field autoField = type.getDeclaredField ("AUTO");
			autoField.setAccessible (true);
			int auto = autoField.getInt (null);
			Method solve = type.getDeclaredMethod (
					"solve",
					int.class, int.class, int.class,
					long.class, int.class, long.class,
					int.class, int.class, int.class, int.class);
			solve.setAccessible (true);
			Object result = solve.invoke (
					null,
					outerWidth, outerHeight, headerHeight,
					logicalRows, sampleRowHeight, logicalContentWidth,
					horizontalBarHeight, verticalBarWidth, auto, auto);
			return new ScrollLayoutSnapshot (
					(boolean) invokeNoArg (result, "horizontalVisible"),
					(boolean) invokeNoArg (result, "verticalVisible"),
					(boolean) invokeNoArg (result, "cornerVisible"),
					(int) invokeNoArg (result, "bodyWidth"),
					(int) invokeNoArg (result, "bodyHeight"),
					(int) invokeNoArg (result, "headerWidth"),
					(int) invokeNoArg (result, "visibleRows"));
		} catch (ReflectiveOperationException failure) {
			throw new IllegalStateException ("Cannot evaluate viewport scrollbar layout", failure);
		}
	}

	private static Object invokeNoArg (Object target, String methodName)
			throws ReflectiveOperationException {
		Method method = target.getClass ().getDeclaredMethod (methodName);
		method.setAccessible (true);
		return method.invoke (target);
	}

	private static void appendReflectiveValue (
			StringBuilder out, String indent, String label, Object target,
			String methodName, Class<?>[] parameterTypes, Object[] args) {
		try {
			Method method = target.getClass ().getDeclaredMethod (methodName, parameterTypes);
			method.setAccessible (true);
			out.append (indent).append (label).append ('=')
					.append (method.invoke (target, args)).append ('\n');
		} catch (ReflectiveOperationException | RuntimeException unavailable) {
			out.append (indent).append (label).append ("=<unavailable>").append ('\n');
		}
	}

	private static void appendReflectiveFieldMethod (
			StringBuilder out, String indent, String label, Object target,
			String fieldName, String methodName) {
		try {
			Field field = target.getClass ().getDeclaredField (fieldName);
			field.setAccessible (true);
			Object owner = field.get (target);
			if (owner == null) {
				out.append (indent).append (label).append ("=<none>").append ('\n');
				return;
			}
			Method method = owner.getClass ().getDeclaredMethod (methodName);
			method.setAccessible (true);
			out.append (indent).append (label).append ('=')
					.append (method.invoke (owner)).append ('\n');
		} catch (ReflectiveOperationException | RuntimeException unavailable) {
			out.append (indent).append (label).append ("=<unavailable>").append ('\n');
		}
	}

	private static void appendScrollBarSnapshot (
			StringBuilder out, String prefix, ScrollBar bar) {
        if (bar == null || bar.isDisposed()) {
            return;
        }
		out.append (prefix).append ("scrollbar selection=").append (bar.getSelection ())
				.append (" min=").append (bar.getMinimum ())
				.append (" max=").append (bar.getMaximum ())
				.append (" thumb=").append (bar.getThumb ())
				.append (" increment=").append (bar.getIncrement ())
				.append (" pageIncrement=").append (bar.getPageIncrement ())
				.append (" visible=").append (bar.getVisible ())
				.append ('\n');
	}

	private static void selectTab (TabFolder folder, Control control) {
		TabItem[] items = folder.getItems ();
		for (int index = 0; index < items.length; index++) {
			if (items [index].getControl () == control) {
				folder.setSelection (index);
				return;
			}
		}
	}

	private static Composite tab (TabFolder folder, String text) {
		TabItem item = new TabItem (folder, SWT.NONE);
		item.setText (text);
		Composite composite = new Composite (folder, SWT.NONE);
		item.setControl (composite);
		return composite;
	}

	private static void jumpButton (Composite parent, String text, Runnable action) {
		Button button = new Button (parent, SWT.PUSH);
		button.setText (text);
		button.addListener (SWT.Selection, event -> action.run ());
	}

	private static void jumpVertical (ScrolledComposite scroller, Canvas canvas, int row) {
		Point origin = scroller.getOrigin ();
		redrawLocked (canvas, () -> scroller.setOrigin (origin.x, row * ROW_HEIGHT));
		canvas.redraw ();
	}

	private static void jumpHorizontal (
			ScrolledComposite scroller, Canvas canvas, Canvas header,
			AtomicInteger headerOriginX, int x) {
		Point origin = scroller.getOrigin ();
		redrawLocked (canvas, () -> scroller.setOrigin (x, origin.y));
		int actual = scroller.getOrigin ().x;
        if (headerOriginX.getAndSet(actual) != actual) {
            header.redraw();
        }
		canvas.redraw ();
	}

	private static void redrawLocked (Control control, Runnable mutation) {
		control.setRedraw (false);
		try {
			mutation.run ();
		} finally {
            if (!control.isDisposed()) {
                control.setRedraw(true);
            }
		}
	}

	private static int depth (TreeItem item) {
		int result = 0;
        while ((item = item.getParentItem()) != null) {
            result++;
        }
		return result;
	}

	private static boolean selected (long[] masks, int index) {
		return (masks [index >>> 6] & (1L << (index & 63))) != 0;
	}

	private static void toggle (long[] masks, int index) {
		masks [index >>> 6] ^= 1L << (index & 63);
	}

	private static void refreshStatus (Display display, Label label, java.util.function.Supplier<String> text) {
		Runnable refresh = new Runnable () {
			@Override
			public void run () {
                if (label.isDisposed()) {
                    return;
                }
				label.setText (text.get ());
				display.timerExec (250, this);
			}
		};
		display.timerExec (250, refresh);
	}
}
