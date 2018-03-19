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

import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // To make it easier for human to go through the tests. Same order makes tests easier to recognize.
public class MJ_Table extends MJ_root {

	// Shared elements:
		final Listener ownerDrawnListener = event -> {
			final TableItem item = (TableItem)event.item;
			Table table = item.getParent();
			AtomicBoolean packpending = (AtomicBoolean) table.getData();

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
				final int index = table.indexOf(item);
				final String data = "Virtual item:" + index;
				item.setData(data);
				item.setText("VirtItem " + index);
				if (table.getColumnCount() > 1) {
					for (int i = 1; i <= table.getColumnCount(); i++) {
						item.setText(i, " Col: " + i + "   " + data);
					}
				}

				if (packpending.get()) {
					packpending.set(false);
					display.asyncExec(() -> {
						table.setRedraw(false);
						for (TableColumn column : table.getColumns()) {
							column.pack();
						}
						table.setRedraw(true);
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

		final Table table = new Table(shell, SWT.VIRTUAL | SWT.BORDER |  SWT.H_SCROLL | SWT.V_SCROLL);

		AtomicBoolean packpending = new AtomicBoolean(true);
		table.setData(packpending);

		table.addListener(SWT.EraseItem, 	ownerDrawnListener); // Not relevant.
		table.addListener(SWT.SetData, 		ownerDrawnListener);
		table.addListener(SWT.MeasureItem,  ownerDrawnListener);
		table.addListener(SWT.PaintItem, 	ownerDrawnListener);

		final TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("First Left Column");
		tableColumn.setMoveable(true);

		table.setItemCount(100);

		shell.setSize(800, 600); // Shell size seems to have a little bit of an initial impact. (may show proper with some shell sizes, incorrectly for others).
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void ownerDrawn_cheese_multiple_col() {
		final Shell shell = mkShell("Expected: No cheese in multiple column, also mouse move over no cheese.");
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final Table table = new Table(shell, SWT.VIRTUAL | SWT.BORDER |  SWT.H_SCROLL | SWT.V_SCROLL);

		AtomicBoolean packpending = new AtomicBoolean(true);
		table.setData(packpending);

		table.addListener(SWT.EraseItem, ownerDrawnListener);
		table.addListener(SWT.SetData, ownerDrawnListener);
		table.addListener(SWT.MeasureItem, ownerDrawnListener);
		table.addListener(SWT.PaintItem, ownerDrawnListener);

		BiConsumer<Integer, String> createColCons = (colStyle, colName) -> {
			final TableColumn tableColumn = new TableColumn(table, colStyle);
			tableColumn.setText(colName);
			tableColumn.setMoveable(true);
		};

		createColCons.accept(SWT.LEFT, "LEFT");
		createColCons.accept(SWT.CENTER, "CENTER");
		createColCons.accept(SWT.RIGHT, "RIGHT");

		int ItemCount = 1000;
		table.setItemCount(ItemCount);
		table.setHeaderVisible(true);

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	/**
		 * <a href="http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm#_example4"> Screenshot </a>
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
			final Table table = new Table(shell, SWT.NONE);
			table.setBounds(10,10,350,300);
	//			 table.setBackgroundImage(parliamentImage);
			for (int i = 0; i < 12; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(MONTHS[i] + " (" + LOWS[i] + "C..." + HIGHS[i] + "C)");
			}
			final int clientWidth = table.getClientArea().width;

			/*
			 * NOTE: MeasureItem and EraseItem are called repeatedly. Therefore it is
			 * critical for performance that these methods be as efficient as possible.
			 */
			table.addListener(SWT.MeasureItem, event -> {
				int itemIndex = table.indexOf((TableItem)event.item);
				int rightX = (HIGHS[itemIndex] - SCALE_MIN) * clientWidth / SCALE_RANGE;
				event.width = rightX;
			});
			table.addListener(SWT.EraseItem, event -> {
				int itemIndex = table.indexOf((TableItem)event.item);
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

			final Table table = new Table(shell, SWT.BORDER);
			table.setHeaderVisible(true);

			new TableItem(table, SWT.NONE).setText("Item1");
			TableColumn column1 = new TableColumn(table, SWT.NONE);
	//		column1.setWidth(10);	// Setting column width (or packing) makes items visible.

			table.setSize(200, 200);
			System.out.println(column1.handle);

			shell.setSize(SWIDTH, SHEIGHT);
			shell.open();
			mainLoop(shell);
		}

	@Test
	public void ownerDrawn_multiColumn_gc_snippet239 () {
		Shell shell = mkShell("Verify that text is correctly drawn across 2 columns. 4 columns in total.");
		shell.setText("Text spans two columns in a TableItem");
		shell.setLayout (new FillLayout());
		final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		int columnCount = 4;
		for (int i=0; i<columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			System.out.println("Column " + i);
			column.setText("Column " + i);
		}
		int itemCount = 8;
		for (int i = 0; i < itemCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "item "+i+" a");
			item.setText(3, "item "+i+" d");
		}
		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		final String string = "text that spans two columns";
		GC gc = new GC(table);
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
							TableColumn column1 = table.getColumn(1);
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
		table.addListener(SWT.MeasureItem, paintListener);
		table.addListener(SWT.PaintItem, paintListener);
		for (int i = 0; i < columnCount; i++) {
			table.getColumn(i).pack();
		}
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void bug73812_tableColumn_getWidth_0 () {
		Shell shell = mkShell("Verify that all columns are of same width. (100).");
		shell.setSize(SWIDTH, SHEIGHT);
		shell.setLayout(new FillLayout());

	    final Table tt = new Table(shell, SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
	    tt.setLinesVisible(true);
	    tt.setHeaderVisible(true);

	    for (int i = 0; i < 10; i++) {
	      TableColumn tc = new TableColumn(tt, SWT.NONE);
	      tc.setWidth(100);
	      System.out.println(tc.getWidth());
	      tc.setWidth(tc.getWidth());
	      tc.setText("Column " + i);
	    }

	    for (int i = 0; i < 100; i++) {
	      new TableItem(tt, SWT.NONE);
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
	      final Table table = new Table(comp, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
	      table.setHeaderVisible(true);
	      table.setLinesVisible(true);
	      final TableColumn column1 = new TableColumn(table, SWT.NONE);
	      column1.setText("Column 1");
	      column1.setResizable(false);
	      final TableColumn column2 = new TableColumn(table, SWT.NONE);
	      column2.setText("Column 2");
	      column2.setResizable(false);
	      for (int i = 0; i < 60; i++) {
	         TableItem item = new TableItem(table, SWT.NONE);
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
	            Rectangle area = table.getParent().getClientArea();
	            Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	            int width = area.width - 2 * table.getBorderWidth();
	            if (preferredSize.y > area.height)
	            {
	               // Subtract the scrollbar width from the total column width
	               // if a vertical scrollbar will be required
	               Point vBarSize = table.getVerticalBar().getSize();
	               width -= vBarSize.x;
	            }
	            Point oldSize = table.getSize();
	            if (oldSize.x > area.width)
	            {
	               // table is getting smaller so make the columns
	               // smaller first and then resize the table to
	               // match the client area width
	               setColumnWidths.accept(width);
	               table.setSize(area.width, area.height);
	            }
	            else
	            {
	               // table is getting bigger so make the table
	               // bigger first and then make the columns wider
	               // to match the client area width
	               table.setSize(area.width, area.height);
	               setColumnWidths.accept(width);
	            }
	         }
	      });
	      shell.open();
	      mainLoop(shell);
	}

	@Test
	public void basicTable_Snippet35() {
		Shell shell = mkShell("Basic Table with a few items, no column, no headers");
		shell.setLayout(new FillLayout());
		Table table = new Table (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i=0; i<12; i++) {
			TableItem item = new TableItem (table, 0);
			item.setText ("Item " + i);
		}
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}



	@Test
	public void ownerDrawn_icons_on_right_side_of_column_Snippet230() {
		Shell shell = mkShell("Verify icons are visible on all columns on right side. (on gtk3, icons are cut off Bug 531882)");
		knownToBeBrokenGtk3("On gtk3, icons in col 1 and 2 are cut off. Bug 531882");

		final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
		shell.setLayout(new FillLayout ());
		Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		int columnCount = 3;
		for (int i=0; i<columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column " + i);
		}
		int itemCount = 8;
		for(int i = 0; i < itemCount; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] {"item "+i+" a", "item "+i+" b", "item "+i+" c"});
		}

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		Listener paintListener = event -> {
			switch(event.type) {
				case SWT.MeasureItem: {
					Rectangle rect1 = image.getBounds();
					event.width += rect1.width;
					event.height = Math.max(event.height, rect1.height + 2);
					break;
				}
				case SWT.PaintItem: {
					int x = event.x + event.width;
					Rectangle rect2 = image.getBounds();
					int offset = Math.max(0, (event.height - rect2.height) / 2);
					event.gc.drawImage(image, x, event.y + offset);
					break;
				}
			}
		};
		table.addListener(SWT.MeasureItem, paintListener);
		table.addListener(SWT.PaintItem, paintListener);

		for(int i = 0; i < columnCount; i++) {
			table.getColumn(i).pack();
		}
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open();
		shell.addDisposeListener(e -> {
			if(image != null) image.dispose();
		});
		mainLoop(shell);
	}

	@Test
	public void ownerDrawn_barChart_Snippet228() {
		Shell shell = mkShell("Ensure you see bar-charts in 2nd column and they resize with shell.");
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Bug Status");
		column1.setWidth(100);
		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Percent");
		column2.setWidth(200);
		String[] labels = new String[]{"Resolved", "New", "Won't Fix", "Invalid"};
		for (int i=0; i<labels.length; i++) {
			 TableItem item = new TableItem(table, SWT.NONE);
			 item.setText(labels[i]);
		}

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		table.addListener(SWT.PaintItem, new Listener() {
			int[] percents = new int[] {50, 30, 5, 15};
			@Override
			public void handleEvent(Event event) {
				if (event.index == 1) {
					GC gc = event.gc;
					TableItem item = (TableItem)event.item;
					int index = table.indexOf(item);
					int percent = percents[index];
					Color foreground = gc.getForeground();
					Color background = gc.getBackground();
					gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
					gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
					int width = (column2.getWidth() - 1) * percent / 100;
					gc.fillGradientRectangle(event.x, event.y, width, event.height, true);
					Rectangle rect2 = new Rectangle(event.x, event.y, width-1, event.height-1);
					gc.drawRectangle(rect2);
					gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
					String text = percent+"%";
					Point size = event.gc.textExtent(text);
					int offset = Math.max(0, (event.height - size.y) / 2);
					gc.drawText(text, event.x+2, event.y+offset, true);
					gc.setForeground(background);
					gc.setBackground(foreground);
				}
			}
		});
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void ownerDrawn_textAlignment_Snippet231 () {
		Shell shell = mkShell("Ensure there is no cheese, also when hoving over items");
		final int COLUMN_COUNT = 4;
		final int ITEM_COUNT = 8;
		final int TEXT_MARGIN = 3;
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int i = 0; i < COLUMN_COUNT; i++) {
			new TableColumn(table, SWT.NONE);
		}
		for (int i = 0; i < ITEM_COUNT; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < COLUMN_COUNT; j++) {
				String string = "item " + i + " col " + j;
				if ((i + j) % 3 == 1) {
					string +="\nnew line1";
				}
				if ((i + j) % 3 == 2) {
					string +="\nnew line1\nnew line2";
				}
				item.setText(j, string);
			}
		}

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		table.addListener(SWT.MeasureItem, event -> {
			TableItem item = (TableItem)event.item;
			String text = item.getText(event.index);
			Point size = event.gc.textExtent(text);
			event.width = size.x + 2 * TEXT_MARGIN;
			event.height = Math.max(event.height, size.y + TEXT_MARGIN);
		});
		table.addListener(SWT.EraseItem, event -> event.detail &= ~SWT.FOREGROUND);
		table.addListener(SWT.PaintItem, event -> {
			TableItem item = (TableItem)event.item;
			String text = item.getText(event.index);
			/* center column 1 vertically */
			int yOffset = 0;
			if (event.index == 1) {
				Point size = event.gc.textExtent(text);
				yOffset = Math.max(0, (event.height - size.y) / 2);
			}
			event.gc.drawText(text, event.x + TEXT_MARGIN, event.y + yOffset, true);
		});

		for (int i = 0; i < COLUMN_COUNT; i++) {
			table.getColumn(i).pack();
		}
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void ownerDrawn_CustomItemHeight () {
		Shell shell = mkShell("Ensure Item 3 (yellow) is bigger than the other items");
		int bigItem = 3;

		shell.setLayout(new FillLayout());
		shell.setSize (SWIDTH, SHEIGHT);
		final Table table = new Table(shell, SWT.NONE);
		table.setLinesVisible(true);


		for (int i = 0; i < 5; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			if (i == bigItem) {
				item.setText("Item " + bigItem + " has bigger height");
				item.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
			}
			else {
				item.setText("item " + i);
				item.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
			}
		}

		/*
		 * NOTE: MeasureItem is called repeatedly.  Therefore it is critical
		 * for performance that this method be as efficient as possible.
		 */
		table.addListener(SWT.MeasureItem, event -> {
			int clientWidth = table.getClientArea().width;
			event.height = event.gc.getFontMetrics().getHeight() * 2; // * table.indexOf((TableItem) event.item)  to have different height rows.
			event.width = clientWidth * 2;

			if (table.indexOf((TableItem) event.item) == bigItem) {
				event.height *= 2;
			}
		});

		shell.open();
		mainLoop(shell);
	}


	/**
	 * Snippet 144 modified to auto-populate items when shell is activated instead.
	 */
	@Test
	public void virtual_addManyItems_Snippet144 () {
		final Shell shell = mkShell("Shell Should show and items should be populated in lazy way");
		shell.setSize (SWIDTH, SHEIGHT);
		final int COUNT = 100000;
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		final Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
		final Label label1 = new Label(shell, SWT.NONE);
		table.addListener (SWT.SetData, event -> {
			TableItem item = (TableItem) event.item;
			int index = table.indexOf (item);
			item.setText ("Item  " + index);
			label1.setText("Added " + item.getText());
		});
		table.setLayoutData (new RowData (shell.getBounds().width - 100, 400));
		final Label label2 = new Label(shell, SWT.NONE);
		shell.addShellListener( new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				System.out.println("activated");
				long t1 = System.currentTimeMillis ();
				table.setItemCount (COUNT);
				long t2 = System.currentTimeMillis ();
				label2.setText ("Items: " + COUNT + ", Time: " + (t2 - t1) + " (ms)");
				shell.layout ();
			}
		});
		shell.open ();
		mainLoop(shell);
	}

	@Test
	public void column_headers_Snippet38() {
		Shell shell = mkShell("Matrix with rows & columns and columns with description. (BUG gtk2/3 text seems slightly cut off, Bug 531875)");
		knownToBeBrokenGtk("Text in column is cut off at the moment. See Bug 531875");

		shell.setLayout(new GridLayout());
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {" ", "C", "!", "Description", "Resource", "In Folder", "Location"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		int count = 128;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "x");
			item.setText (1, "y");
			item.setText (2, "!");
			item.setText (3, "this stuff behaves the way I expect");
			item.setText (4, "almost everywhere");
			item.setText (5, "some.folder");
			item.setText (6, "line " + i + " in nowhere");
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}


	@Test
	public void column_header_icons_Snippet297() {
		Image images[] = new Image[] {
			display.getSystemImage(SWT.ICON_INFORMATION),
			display.getSystemImage(SWT.ICON_ERROR),
			display.getSystemImage(SWT.ICON_QUESTION),
			display.getSystemImage(SWT.ICON_WARNING),
		};
		String[] titles = {"Information", "Error", "Question", "Warning"};
		String[] questions = {"who?", "what?", "where?", "when?", "why?"};
		Shell shell = new Shell (display);
		shell.setLayout(new GridLayout());
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		GridData data = new GridData (SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData (data);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);

		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
			column.setImage(images [i]);
		}

		int count = 128;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "some info");
			item.setText (1, "error #" + i);
			item.setText (2, questions [i % questions.length]);
			item.setText (3, "look out!");
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}

		table.setHeaderBackground(display.getSystemColor(SWT.COLOR_BLUE));
		table.setHeaderForeground(display.getSystemColor(SWT.COLOR_RED));

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}

	/**
	 * Based on Snippet 106 with some modificaitons.
	 */
	@Test
	public void column_dynamically_added_after_shellOpened_Snippet106() {
		Shell shell = mkShell("Verify dynamic column (2) was added properly.");

		final Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
		table.setHeaderVisible (true);
		for (int i=0; i<4; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText ("Column " + i);
		}
		final TableColumn [] columns = table.getColumns ();
		for (int i=0; i<12; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			for (int j=0; j<columns.length; j++) {
				item.setText (j, "Item " + i);
			}
		}
		for (int i=0; i<columns.length; i++) columns [i].pack ();
		Button button = new Button (shell, SWT.PUSH);
		final int index = 1;
		button.setText ("Insert Column " + index + "a");

		AtomicBoolean columnAdded = new AtomicBoolean(false);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				if (!columnAdded.get()) {
					columnAdded.set(true);
					TableColumn column = new TableColumn (table, SWT.NONE, index);
					column.setImage(display.getSystemImage(SWT.ICON_WARNING)); //added to make it easier to spot in a test.
					column.setText ("Column " + index + " added after shellopen");
					TableItem [] items = table.getItems ();
					for (int i=0; i<items.length; i++) {
						items [i].setText (index, "Item " + i + " added");
					}
					column.pack ();
				}
			}
		});

		shell.setSize (SWIDTH, SHEIGHT);
		Rectangle shellClientArea = shell.getClientArea();
		table.setSize(shellClientArea.width, shellClientArea.height);
		shell.open ();
		mainLoop(shell);
	}

	@Test
	public void column_dynamic_resize() {
		Shell shell = mkShell("Try resizing shell. Columns should resize as you resize shell");
		shell.setLayout(new FillLayout());

		final Composite comp = new Composite(shell, SWT.NONE);
		final Table table = new Table(comp, SWT.BORDER | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column 1");
		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column 2");
		for (int i = 0; i < 10; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] {"item 0" + i, "item 1"+i});
		}
		comp.addControlListener(ControlListener.controlResizedAdapter(e -> {
			Rectangle area = comp.getClientArea();
			Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			ScrollBar vBar = table.getVerticalBar();
			int width = area.width - table.computeTrim(0, 0, 0, 0).width - vBar.getSize().x;
			if (size.y > area.height + table.getHeaderHeight()) {
				// Subtract the scrollbar width from the total column width
				// if a vertical scrollbar will be required
				Point vBarSize = vBar.getSize();
				width -= vBarSize.x;
			}
			Point oldSize = table.getSize();
			if (oldSize.x > area.width) {
				// table is getting smaller so make the columns
				// smaller first and then resize the table to
				// match the client area width
				column1.setWidth(width / 3);
				column2.setWidth(width - column1.getWidth());
				table.setSize(area.width, area.height);
			} else {
				// table is getting bigger so make the table
				// bigger first and then make the columns wider
				// to match the client area width
				table.setSize(area.width, area.height);
				column1.setWidth(width / 3);
				column2.setWidth(width - column1.getWidth());
			}
		}));

		shell.open();
		mainLoop(shell);
	}

	@Test
	public void measureItem_custom_Column_width_onPack_Snippet272() {
		Shell shell = mkShell("Double click on column boundary '|', observe column get's bigger");
		shell.setSize(SWIDTH, SHEIGHT);
		shell.setLayout(new FillLayout());
		Table table = new Table(shell, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		final TableColumn column0 = new TableColumn(table, SWT.NONE);
		column0.setWidth(110);
		column0.setText("2x Click pipe ->");
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(110);
		column0.addListener(SWT.Selection, event -> column0.pack());
		column1.addListener(SWT.Selection, event -> column1.pack());
		for (int i = 0; i < 5; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "item " + i + " col 0");
			item.setText(1, "item " + i + " col 1");
		}

		/*
		 * NOTE: MeasureItem is called repeatedly.  Therefore it is critical
		 * for performance that this method be as efficient as possible.
		 */
		table.addListener(SWT.MeasureItem, event -> event.width *= 4);  // This guy makes the difference.

		shell.open();
		mainLoop(shell);
	}

	@Test
	public void checkBoxes() {
		Shell shell = mkShell("You should see some checkboxes");
		shell.setLayout(new FillLayout());
		Table table = new Table (shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (int i=0; i<12; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Item " + i);
		}
		Rectangle clientArea = shell.getClientArea ();
		table.setBounds (clientArea.x, clientArea.y, 600, 500);

		final Label label = new Label(shell, SWT.NONE);

		table.addListener (SWT.Selection, event -> {
			String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
			label.setText(event.item + " " + string);
		});
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}

	@Test
	public void color_singleColumn() {
		Shell shell = mkShell("Header and content should be colored red and blue. Column header should be of a darker color.");
		shell.setLayout(new FillLayout());

		Table table = new Table (shell, SWT.None);
		table.setHeaderVisible(true);
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Column header has darker colors.");
		column.pack();

		for (int i = 0; i < 200; i++) {
			TableItem item = new TableItem(table, SWT.None);
			item.setText("Item " + i);
		}

		table.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		table.setForeground(display.getSystemColor(SWT.COLOR_RED));

		table.setHeaderBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		table.setHeaderForeground(display.getSystemColor(SWT.COLOR_DARK_RED));

		shell.open();
		mainLoop(shell);
	}

	@Test
	public void color_MultipleColumn() {
		Shell shell = mkShell("Header and content should be colored red and blue. Column header should be of a darker color.");
		shell.setLayout(new FillLayout());

		Table table = new Table (shell, SWT.None);
		table.setHeaderVisible(true);
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Column 1");
		column.pack();

		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setText("Column 2");
		column2.pack();

		for (int i = 0; i < 200; i++) {
			TableItem item = new TableItem(table, SWT.None);
			item.setText(new String[] {"Item " + i,  "Item col2: " + i});
		}

		table.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		table.setForeground(display.getSystemColor(SWT.COLOR_RED));

		table.setHeaderBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		table.setHeaderForeground(display.getSystemColor(SWT.COLOR_DARK_RED));

		shell.open();
		mainLoop(shell);
	}

	@Test
	public void color_differentCells_Snippet129 () {
		Shell shell = mkShell("Table cells should be of different colors");

		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color gray = display.getSystemColor(SWT.COLOR_GRAY);
		shell.setLayout(new FillLayout());
		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBackground(gray);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"entire","row","red foreground"});
		item.setForeground(red);
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"entire","row","red background"});
		item.setBackground(red);
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"entire","row","white fore/red back"});
		item.setForeground(white);
		item.setBackground(red);
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"normal","blue foreground","red foreground"});
		item.setForeground(1, blue);
		item.setForeground(2, red);
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"normal","blue background","red background"});
		item.setBackground(1, blue);
		item.setBackground(2, red);
		item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"white fore/blue back","normal","white fore/red back"});
		item.setForeground(0, white);
		item.setBackground(0, blue);
		item.setForeground(2, white);
		item.setBackground(2, red);

		column1.pack();
		column2.pack();
		column3.pack();

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void programaticScrolling_Snippet52() {
		Shell shell = mkShell("Table should be scrolled down to 100th item");
		Table table = new Table (shell, SWT.BORDER | SWT.MULTI);
		Rectangle clientArea = shell.getClientArea ();
		table.setBounds (clientArea.x, clientArea.y,800, 600);
		for (int i=0; i<128; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Item " + i);
		}
		table.setSelection (100);					// <<< This is what we're testing.
		shell.pack ();
		shell.open ();
		mainLoop(shell);
	}

	@Test
	public void sort_by_column_Snippet2() {
	    Shell shell = mkShell("Click on columns to verify items are sorted properly");
	    shell.setLayout(new FillLayout());

	    final Table table = new Table(shell, SWT.BORDER);
	    table.setHeaderVisible(true);
	    final TableColumn column1 = new TableColumn(table, SWT.NONE);
	    column1.setText("Column 1");
	    final TableColumn column2 = new TableColumn(table, SWT.NONE);
	    column2.setText("Column 2");
	    TableItem item = new TableItem(table, SWT.NONE);
	    item.setText(new String[] {"a", "3"});
	    item = new TableItem(table, SWT.NONE);
	    item.setText(new String[] {"b", "2"});
	    item = new TableItem(table, SWT.NONE);
	    item.setText(new String[] {"c", "1"});
	    column1.setWidth(100);
	    column2.setWidth(100);
	    Listener sortListener = e -> {
		    TableItem[] items = table.getItems();
		    Collator collator = Collator.getInstance(Locale.getDefault());
		    TableColumn column = (TableColumn)e.widget;
		    int index = column == column1 ? 0 : 1;
		    for (int i = 1; i < items.length; i++) {
		        String value1 = items[i].getText(index);
		        for (int j = 0; j < i; j++){
		            String value2 = items[j].getText(index);
		            if (collator.compare(value1, value2) < 0) {
		                String[] values = {items[i].getText(0), items[i].getText(1)};
		                items[i].dispose();
		                TableItem item1 = new TableItem(table, SWT.NONE, j);
		                item1.setText(values);
		                items = table.getItems();
		                break;
		            }
		        }
		    }
		    table.setSortColumn(column);
		};
	    column1.addListener(SWT.Selection, sortListener);
	    column2.addListener(SWT.Selection, sortListener);
	    table.setSortColumn(column1);
	    table.setSortDirection(SWT.UP);
	    shell.setSize(SWIDTH, SHEIGHT);
	    shell.open();
	    mainLoop(shell);
	}

	@Test
	public void sort_by_columnt_virtual_Snippet192 () {
		Shell shell = mkShell("Click on column heading to sort");
		// initialize data with keys and random values
		int size = 100;
		Random random = new Random();
		final int[][] data = new int[size][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new int[] {i, random.nextInt()};
		}
		// create a virtual table to display data
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(size);
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Key");
		column1.setWidth(200);
		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Value");
		column2.setWidth(200);
		table.addListener(SWT.SetData, e -> {
			TableItem item = (TableItem) e.item;
			int index = table.indexOf(item);
			int[] datum = data[index];
			item.setText(new String[] {Integer.toString(datum[0]),
					Integer.toString(datum[1]) });
		});
		// Add sort indicator and sort data when column selected
		Listener sortListener = e -> {
			// determine new sort column and direction
			TableColumn sortColumn = table.getSortColumn();
			TableColumn currentColumn = (TableColumn) e.widget;
			int dir = table.getSortDirection();
			if (sortColumn == currentColumn) {
				dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
			} else {
				table.setSortColumn(currentColumn);
				dir = SWT.UP;
			}
			// sort the data based on column and direction
			final int index = currentColumn == column1 ? 0 : 1;
			final int direction = dir;
			Arrays.sort(data, (a, b) -> {
				if (a[index] == b[index]) return 0;
				if (direction == SWT.UP) {
					return a[index] < b[index] ? -1 : 1;
				}
				return a[index] < b[index] ? 1 : -1;
			});
			// update data displayed in table
			table.setSortDirection(dir);
			table.clearAll();
		};
		column1.addListener(SWT.Selection, sortListener);
		column2.addListener(SWT.Selection, sortListener);
		table.setSortColumn(column1);
		table.setSortDirection(SWT.UP);
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void icons_ofDiffSize_inItems_Snippet349() {
		Shell shell = mkShell("Verify that items have icons of different sizes and icons are not cut off");

		BiFunction<Integer, Integer, Image> createImage = (width, height) -> {
			Image result = new Image(display, width, height);
			GC gc = new GC(result);
			for (int x = -height; x < width; x += 4) {
				gc.drawLine(x, 0, x + height, height);
			}
			gc.dispose();
			return result;
		};

		final int COLUMN_COUNT = 3;
		final int TEXT_MARGIN = 3;
		final String KEY_WIDTHS = "widths";
		final String KEY_IMAGES = "images";

		Image[] images = new Image[4];
		images[0] = createImage.apply(16, 16);
		images[1] = createImage.apply(32, 16);
		images[2] = createImage.apply(48, 16);

		shell.setLayout(new FillLayout());
		Table table = new Table(shell, SWT.NONE);
		for (int i = 0; i < COLUMN_COUNT; i++) {
			new TableColumn(table, SWT.NONE);
		}
		for (int i = 0; i < 8; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			Image[] itemImages = new Image[COLUMN_COUNT];
			item.setData(KEY_IMAGES, itemImages);
			for (int j = 0; j < COLUMN_COUNT; j++) {
				item.setText(j, "item " + i + " col " + j);
				itemImages[j] = images[(i * COLUMN_COUNT + j) % images.length];
			}
		}

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		final int itemHeight = table.getItemHeight();
		GC gc = new GC(table);
		FontMetrics metrics = gc.getFontMetrics();
		final int fontHeight = metrics.getHeight();
		gc.dispose();
		Listener paintListener = event -> {
			switch (event.type) {
				case SWT.MeasureItem: {
					int column1 = event.index;
					TableItem item1 = (TableItem)event.item;
					Image[] images1 = (Image[])item1.getData(KEY_IMAGES);
					Image image1 = images1[column1];
					if (image1 == null) {
						/* don't change the native-calculated event.width */
						break;
					}
					int[] cachedWidths = (int[])item1.getData(KEY_WIDTHS);
					if (cachedWidths == null) {
						cachedWidths = new int[COLUMN_COUNT];
						item1.setData(KEY_WIDTHS, cachedWidths);
					}
					if (cachedWidths[column1] == 0) {
						int width = image1.getBounds().width + 2 * TEXT_MARGIN;
						GC gc1 = new GC(item1.getParent());
						width += gc1.stringExtent(item1.getText()).x;
						gc1.dispose();
						cachedWidths[column1] = width;
					}
					event.width = cachedWidths[column1];
					break;
				}
				case SWT.EraseItem: {
					int column2 = event.index;
					TableItem item2 = (TableItem)event.item;
					Image[] images2 = (Image[])item2.getData(KEY_IMAGES);
					Image image2 = images2[column2];
					if (image2 == null) {
						break;
					}
					/* disable the native drawing of this item */
					event.detail &= ~SWT.FOREGROUND;
					break;
				}
				case SWT.PaintItem: {
					int column3 = event.index;
					TableItem item3 = (TableItem)event.item;
					Image[] images3 = (Image[])item3.getData(KEY_IMAGES);
					Image image3 = images3[column3];
					if (image3 == null) {
						/* this item is drawn natively, don't touch it*/
						break;
					}

					int x = event.x;
					event.gc.drawImage(image3, x, event.y + (itemHeight - image3.getBounds().height) / 2);
					x += image3.getBounds().width + TEXT_MARGIN;
					event.gc.drawString(item3.getText(column3), x, event.y + (itemHeight - fontHeight) / 2);
					break;
				}
			}
		};
		table.addListener(SWT.MeasureItem, paintListener);
		table.addListener(SWT.EraseItem, paintListener);
		table.addListener(SWT.PaintItem, paintListener);

		for (int i = 0; i < COLUMN_COUNT; i++) {
			table.getColumn(i).pack();
		}

		shell.addDisposeListener(e -> {
			for (int i = 0; i < images.length; i++) {
				if (images[i] != null) {
					images[i].dispose();
				}
			}
		});

		shell.setSize(SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}

	@Test
	public void styledItems_Snippet236() {
		Shell shell = mkShell("Verify tableitems with custom styleText control looks proper & multiple items can be selected");
		shell.setLayout(new FillLayout());
		Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		for(int i = 0; i < 10; i++) {
			new TableItem(table, SWT.NONE);
		}
		final TextLayout textLayout = new TextLayout(display);
		textLayout.setText("SWT: Standard Widget Toolkit");
		Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
		Font font2 = new Font(display, "Tahoma", 10, SWT.NORMAL);
		Font font3 = new Font(display, "Tahoma", 14, SWT.ITALIC);
		TextStyle style1 = new TextStyle(font1, display.getSystemColor(SWT.COLOR_BLUE), null);
		TextStyle style2 = new TextStyle(font2, display.getSystemColor(SWT.COLOR_MAGENTA), null);
		TextStyle style3 = new TextStyle(font3, display.getSystemColor(SWT.COLOR_RED), null);
		textLayout.setStyle(style1, 0, 0); textLayout.setStyle(style1, 5, 12);
		textLayout.setStyle(style2, 1, 1); textLayout.setStyle(style2, 14, 19);
		textLayout.setStyle(style3, 2, 2); textLayout.setStyle(style3, 21, 27);

		/*
		 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
		 * Therefore, it is critical for performance that these methods be
		 * as efficient as possible.
		 */
		table.addListener(SWT.PaintItem, event -> textLayout.draw(event.gc, event.x, event.y));
		final Rectangle textLayoutBounds = textLayout.getBounds();
		table.addListener(SWT.MeasureItem, e -> {
			e.width = textLayoutBounds.width + 2;
			e.height = textLayoutBounds.height + 2;
		});
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open();
		shell.addDisposeListener(e -> {
			font1.dispose();
			font2.dispose();
			font3.dispose();
			textLayout.dispose();
		});
		mainLoop(shell);
	}

	@Test
	public void cout_visible_items_Snippet253 ()  {
		final Shell shell = mkShell("Ensure correct item count is displayed in count. Try resizing shell to show 15 items, it should show 15. Gtk3 issues");
		knownToBeBrokenGtk3("On gtk3, the measure is off, see bug 531884");

		FillLayout layout = new FillLayout (SWT.VERTICAL);
		shell.setLayout (layout);
		final Table table = new Table (shell, SWT.NONE);
		for (int i=0; i<32; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Item " + (i+1) + " is quite long");
		}
		final Button button = new Button (shell, SWT.PUSH);

		Runnable fixCount = () -> {
			Rectangle rect = table.getClientArea ();
			int itemHeight = table.getItemHeight ();
			int headerHeight = table.getHeaderHeight ();
			int visibleCount = (rect.height - headerHeight + itemHeight - 1) / itemHeight;
			button.setText ("Visible Items [" + visibleCount + "]");
		};

		button.setText ("Visible Items []");
		button.addListener (SWT.Selection, e -> {
			fixCount.run();
		});

		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				fixCount.run();
			}
		});
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				fixCount.run();
			}
		});
		// setSize(..)_ affects bug/snippet behaviour.
		shell.setSize(1160, 820); //820 => ~15 items or so, depending on your theme.
		shell.open();
		mainLoop(shell);
	}


	/** Snippet 149 was modified.
	 * - No added columns.
	 * - Button instead of progress bar.
	 */
	@Test
	public void tableEditor_multiple_controls_Snippet149 () {
		knownToBeBrokenGtk3("Snippet is broken on Gtk3. See Bug 531885");
		Shell shell = mkShell("Items should be replaced with Buttons (broken on Gtk3, Bug 531885)");
		shell.setLayout (new FillLayout ());
		Table table = new Table (shell, SWT.BORDER);
		for (int i = 0; i < 20; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText ("Task " + i);
			Button button = new Button (table, SWT.NONE);
			button.setText("hello world " + i);
			button.setVisible(true);
			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Button pressed");
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			TableEditor editor = new TableEditor (table);
			editor.grabHorizontal = editor.grabVertical = true;
			editor.setEditor (button, item, 0);
		}
		shell.setSize (SWIDTH, SHEIGHT);
		shell.open();
		mainLoop(shell);
	}


	@Test
	public void tableEditor_dynamically_created_Snippet124()  {
		Shell shell = mkShell("Verify you can edit cells");
		shell.setLayout (new FillLayout ());
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible (true);
		for (int i=0; i<3; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setWidth(100);
		}
		for (int i=0; i<3; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(new String [] {"" + i, "" + i , "" + i});
		}
		final TableEditor editor = new TableEditor (table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener (SWT.MouseDown, event -> {
			Rectangle clientArea = table.getClientArea ();
			Point pt = new Point (event.x, event.y);
			int index = table.getTopIndex ();
			while (index < table.getItemCount ()) {
				boolean visible = false;
				final TableItem item = table.getItem (index);
				for (int i=0; i<table.getColumnCount (); i++) {
					Rectangle rect = item.getBounds (i);
					if (rect.contains (pt)) {
						final int column = i;
						final Text text = new Text (table, SWT.NONE);
						Listener textListener = e -> {
							switch (e.type) {
								case SWT.FocusOut:
									item.setText (column, text.getText ());
									text.dispose ();
									break;
								case SWT.Traverse:
									switch (e.detail) {
										case SWT.TRAVERSE_RETURN:
											item.setText (column, text.getText ());
											//FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											text.dispose ();
											e.doit = false;
									}
									break;
							}
						};
						text.addListener (SWT.FocusOut, textListener);
						text.addListener (SWT.Traverse, textListener);
						editor.setEditor (text, item, i);
						text.setText (item.getText (i));
						text.selectAll ();
						text.setFocus ();
						return;
					}
					if (!visible && rect.intersects (clientArea)) {
						visible = true;
					}
				}
				if (!visible) return;
				index++;
			}
		});
		shell.setSize(SWIDTH, SHEIGHT);
		shell.open ();
		mainLoop(shell);
	}
}

