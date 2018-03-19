/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 */
package org.eclipse.swt.tests.manualJUnit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/** Convienience class for easy copy & paste */
@FixMethodOrder(MethodSorters.JVM)
public class MJ_Tree extends MJ_root {


	// Shared elements:
		final Listener ownerDrawnListener = event -> {
			final TreeItem item = (TreeItem)event.item;
			Tree tree = item.getParent();
			AtomicBoolean packpending = (AtomicBoolean) tree.getData();

			if (event.type == SWT.EraseItem) {
			}

			if (event.type == SWT.PaintItem) {
				final String text1 = (String)item.getData() + " (gc)";
				if (event.index == 0) {
					event.gc.drawText(text1, event.x, event.y, true);
				}
			}
			if (event.type == SWT.MeasureItem) {
				event.height = 50;
				event.width =  100;
			}
			if (event.type == SWT.SetData) {
				final int index = tree.indexOf(item);
				final String data = "Virtual item:" + index;
				item.setData(data);
				item.setText("VirtItem " + index);
				if (tree.getColumnCount() > 1) {
					for (int i = 1; i <= tree.getColumnCount(); i++) {
						item.setText(i, " Col: " + i + "   " + data);
					}
				}

				if (packpending.get()) {
					packpending.set(false);
					display.asyncExec(() -> {
						tree.setRedraw(false);
						for (TreeColumn column : tree.getColumns()) {
							column.pack();
						}
						tree.setRedraw(true);
					});
				}
			}
		};

	/**
	 *  <a href="https://bugs.eclipse.org/bugs/attachment.cgi?id=272647">Screenshot </a>
	 */
	@Test
	public void ownerDrawn_cheese_single_col () {
		knownToBeBrokenGtk3("Cheese on gtk3"); // z for warning
		Shell shell = mkShell("Expected: There should be no cheese in the items. Move over shouldn't cheese out. See javadoc for screenshot");
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER |  SWT.H_SCROLL | SWT.V_SCROLL);

		AtomicBoolean packpending = new AtomicBoolean(true);
		tree.setData(packpending);

		tree.addListener(SWT.EraseItem, 	ownerDrawnListener); // Not relevant.
		tree.addListener(SWT.SetData, 		ownerDrawnListener);
		tree.addListener(SWT.MeasureItem,  ownerDrawnListener);
		tree.addListener(SWT.PaintItem, 	ownerDrawnListener);

		final TreeColumn treeColumn = new TreeColumn(tree, SWT.LEFT);
		treeColumn.setText("First Left Column");
		treeColumn.setMoveable(true);

		tree.setItemCount(100);

		shell.setSize(800, 600); // Shell size seems to have a little bit of an initial impact. (may show proper with some shell sizes, incorrectly for others).
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void ownerDrawn_cheese_multiple_col() {
		final Shell shell = mkShell("Expected: No cheese in multiple column, also mouse move over no cheese.");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER |  SWT.H_SCROLL | SWT.V_SCROLL);

		AtomicBoolean packpending = new AtomicBoolean(true);
		tree.setData(packpending);

		tree.addListener(SWT.EraseItem, ownerDrawnListener);
		tree.addListener(SWT.SetData, ownerDrawnListener);
		tree.addListener(SWT.MeasureItem, ownerDrawnListener);
		tree.addListener(SWT.PaintItem, ownerDrawnListener);

		BiConsumer<Integer, String> createColCons = (colStyle, colName) -> {
			final TreeColumn treeColumn = new TreeColumn(tree, colStyle);
			treeColumn.setText(colName);
			treeColumn.setMoveable(true);
		};

		createColCons.accept(SWT.LEFT, "LEFT");
		createColCons.accept(SWT.CENTER, "CENTER");
		createColCons.accept(SWT.RIGHT, "RIGHT");

		int ItemCount = 1000;
		tree.setItemCount(ItemCount);
		tree.setHeaderVisible(true);

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

		/**
		 * <a href="http://www.eclipse.org/articles/Article-CustomDrawingTreeAndTreeItems/customDraw.htm#_example4"> Screenshot </a>
		 */
		@Test
		public void ownerDrawn_eraseItem_Snippet273() {
			knownToBeBrokenGtk3("Test currently broken on Gtk3. See Comment#1 of Bug 531551");

			final String[] MONTHS = {
					"Jan", "Feb", "Mar", "Apr", "May", "Jun",
					"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
				};
			final int[] HIGHS = {-7, -4, 1, 11, 18, 24, 26, 25, 20, 13, 5, -4};
			final int[] LOWS = {-15, -13, -7, 1, 7, 13, 15, 14, 10, 4, -2, -11};
			final int SCALE_MIN = -30; final int SCALE_MAX = 30;
			final int SCALE_RANGE = Math.abs(SCALE_MIN - SCALE_MAX);

			Shell shell = mkShell(" Gtk3:broken, no erasing (1st March 2018)   Gtk2: Background is used as bar-chart of sort. See screenshot.");
			final Color blue = display.getSystemColor(SWT.COLOR_BLUE);
			final Color white = display.getSystemColor(SWT.COLOR_WHITE);
			final Color red = display.getSystemColor(SWT.COLOR_RED);
	//			 final Image parliamentImage = new Image(display, "./parliament.jpg");
			final Tree tree = new Tree(shell, SWT.NONE);
			tree.setBounds(10,10,350,300);
	//			 tree.setBackgroundImage(parliamentImage);
			for (int i = 0; i < 12; i++) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(MONTHS[i] + " (" + LOWS[i] + "C..." + HIGHS[i] + "C)");
			}
			final int clientWidth = tree.getClientArea().width;

			/*
			 * NOTE: MeasureItem and EraseItem are called repeatedly. Therefore it is
			 * critical for performance that these methods be as efficient as possible.
			 */
			tree.addListener(SWT.MeasureItem, event -> {
				int itemIndex = tree.indexOf((TreeItem)event.item);
				int rightX = (HIGHS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
				event.width = rightX;
			});
			tree.addListener(SWT.EraseItem, event -> {
				int itemIndex = tree.indexOf((TreeItem)event.item);
				int leftX = (LOWS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
				int rightX = (HIGHS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
				GC gc = event.gc;
				Rectangle clipping = gc.getClipping();
				clipping.x = leftX;
				clipping.width = rightX - leftX;
				gc.setClipping(clipping);
				Color oldForeground = gc.getForeground();
				Color oldBackground = gc.getBackground();
				gc.setForeground(blue);
				gc.setBackground(white);
				gc.fillGradientRectangle(event.x, event.y, event.width / 2, event.height, false);
				gc.setForeground(white);
				gc.setBackground(red);
				gc.fillGradientRectangle(
					event.x + event.width / 2, event.y, event.width / 2, event.height, false);
				gc.setForeground(oldForeground);
				gc.setBackground(oldBackground);
				event.detail &= ~SWT.BACKGROUND;
				event.detail &= ~SWT.HOT;
			});
			shell.setSize(SWIDTH, SHEIGHT);
			shell.open();
			mainLoop(shell);
		}

	/**
		 * On Windows/Mac, if columnWidth is not set via setWidth or pack, then items are not visible.
		 */
		@Test
		public void column_noWidth_bug399522 () {
			Shell shell = mkShell("Expected : You shouldn't see the column/item as column width not set yet.");

			final Tree tree = new Tree(shell, SWT.BORDER);
			tree.setHeaderVisible(true);

			new TreeItem(tree, SWT.NONE).setText("Item1");
			TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
	//		column1.setWidth(10);	// Setting column width (or packing) makes items visible.

			tree.setSize(200, 200);
			System.out.println(column1.handle);

			shell.setSize(SWIDTH, SHEIGHT);
			shell.open();
			mainLoop(shell);
		}

	@Test
	public void ownerDrawn_multiColumn_gc_snippet239 () {
		Shell shell = mkShell("Verify that text is correctly drawn across 2 columns. 4 columns in total.");
		shell.setText("Text spans two columns in a TreeItem");
		shell.setLayout (new FillLayout());
		final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		int columnCount = 4;
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			System.out.println("Column " + i);
			column.setText("Column " + i);
		}
		int itemCount = 8;
		for (int i = 0; i < itemCount; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(0, "item "+i+" a");
			item.setText(3, "item "+i+" d");
		}
		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		final String string = "text that spans two columns";
		GC gc = new GC(tree);
		final Point extent = gc.stringExtent(string);
		gc.dispose();
		final Color red = display.getSystemColor(SWT.COLOR_RED);
		Listener paintListener = event -> {
			switch(event.type) {
				case SWT.MeasureItem: {
					if (event.index == 1 || event.index == 2) {
						event.width = extent.x/2;
						event.height = Math.max(event.height, extent.y + 2);
					}
					break;
				}
				case SWT.PaintItem: {
					if (event.index == 1 || event.index == 2) {
						int offset = 0;
						if (event.index == 2) {
							TreeColumn column1 = tree.getColumn(1);
							offset = column1.getWidth();
						}
						event.gc.setForeground(red);
						int y = event.y + (event.height - extent.y)/2;
						event.gc.drawString(string, event.x - offset, y, true);
					}
					break;
				}
			}
		};
		tree.addListener(SWT.MeasureItem, paintListener);
		tree.addListener(SWT.PaintItem, paintListener);
		for (int i = 0; i < columnCount; i++) {
			tree.getColumn(i).pack();
		}
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void bug73812_treeColumn_getWidth_0 () {
		Shell shell = mkShell("Verify that all columns are of same width. (100).");
		shell.setSize(SWIDTH, SHEIGHT);
		shell.setLayout(new FillLayout());

	    final Tree tt = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
	    tt.setLinesVisible(true);
	    tt.setHeaderVisible(true);

	    for (int i = 0; i < 10; i++) {
	      TreeColumn tc = new TreeColumn(tt, SWT.NONE);
	      tc.setWidth(100);
	      System.out.println(tc.getWidth());
	      tc.setWidth(tc.getWidth());
	      tc.setText("Column " + i);
	    }

	    for (int i = 0; i < 100; i++) {
	      new TreeItem(tt, SWT.NONE);
	    }

	    shell.open();
		mainLoop(shell);
	}

	/**
	 * Last column should be big enough so that text inside it can be read.
	 * Notion that getWidth() when called after setWidth() returns a different size.
	 */
	@Test
	public void bug51079_setWidth_getWidth() {
		Shell shell = mkShell("column SetGet Width : Make shell smaller and bigger. If you don't see COL_SIZE_ERROR in console, all is well.");
		shell.setSize(SWIDTH, SHEIGHT);
		shell.setLayout(new FillLayout());
	      StringBuffer sbBuffer = new StringBuffer();

	      final Composite comp = new Composite(shell, SWT.NONE);
	      final Tree tree = new Tree(comp, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
	      tree.setHeaderVisible(true);
	      tree.setLinesVisible(true);
	      final TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
	      column1.setText("Column 1");
	      column1.setResizable(false);
	      final TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
	      column2.setText("Column 2");
	      column2.setResizable(false);
	      for (int i = 0; i < 60; i++) {
	         TreeItem item = new TreeItem(tree, SWT.NONE);
	         sbBuffer.append("M");
	         item.setText(new String[] { "item 0 " + sbBuffer.toString() + " " + i, "item 1 " + i });
	      }

	      Consumer<Integer> setColumnWidths = (width) -> {
	    	  int c1w = (int)(width * 0.9);
	          column1.setWidth(c1w);
	          int c1wPost = column1.getWidth();
	          if (c1w != c1wPost)
	       	   System.err.println("COL_SIZE_ERROR 1 Expected:" + c1w + " actual:" + c1wPost);

	          int c2w = width - column1.getWidth();
	          column2.setWidth(c2w);
	          int c2wPost = column2.getWidth();
	          if (c2w != c2wPost)
	               System.err.println("COL_SIZE_ERROR 2 Expected:" + c2w + " actual:" + column2.getWidth());
	      };

	      comp.addControlListener(new ControlAdapter()
	      {
	         @Override
			public void controlResized(ControlEvent e)
	         {
	            Rectangle area = tree.getParent().getClientArea();
	            Point preferredSize = tree.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	            int width = area.width - 2 * tree.getBorderWidth();
	            if (preferredSize.y > area.height)
	            {
	               // Subtract the scrollbar width from the total column width
	               // if a vertical scrollbar will be required
	               Point vBarSize = tree.getVerticalBar().getSize();
	               width -= vBarSize.x;
	            }
	            Point oldSize = tree.getSize();
	            if (oldSize.x > area.width)
	            {
	               // tree is getting smaller so make the columns
	               // smaller first and then resize the tree to
	               // match the client area width
	               setColumnWidths.accept(width);
	               tree.setSize(area.width, area.height);
	            }
	            else
	            {
	               // tree is getting bigger so make the tree
	               // bigger first and then make the columns wider
	               // to match the client area width
	               tree.setSize(area.width, area.height);
	               setColumnWidths.accept(width);
	            }
	         }
	      });
	      shell.open();
	      mainLoop(shell);
	}

	@Test
	public void basicTree_Snippet35() {
		// original was table, modified for tree.
		Shell shell = mkShell("TREE: Basic Tree with a few items, no column, no headers");
		shell.setLayout(new FillLayout());
		Tree tree = new Tree (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i=0; i<12; i++) {
			TreeItem item = new TreeItem (tree, 0);
			item.setText ("Item " + i);
		}
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}
}
