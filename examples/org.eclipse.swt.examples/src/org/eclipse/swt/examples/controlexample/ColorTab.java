/*******************************************************************************
 * Copyright (c) 2000, 2016 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ian Pun <ipun@redhat.com> - addition of Color tab
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

class ColorTab extends Tab {
	Table colors;
	Group colorsGroup;
	HashMap <Integer, String> hmap = new HashMap <> ();
	static final int namedColorEnd = 8;
	static String [] columnTitles	= {ControlExample.getResourceString("ColorTitle_0"),
			   ControlExample.getResourceString("ColorTitle_1"),
			   ControlExample.getResourceString("ColorTitle_2"),
			   ControlExample.getResourceString("ColorTitle_3")};

	/* Size widgets added to the "Size" group */
	Button packColumnsButton;

	/**
	 * Creates the color tab within a given instance of ControlExample.
	 */
	ColorTab(ControlExample instance) {
		super(instance);
		addTableElements();
	}

	void addTableElements () {
	    hmap.put(SWT.COLOR_WHITE, "COLOR_WHITE");
	    hmap.put(SWT.COLOR_BLACK, "COLOR_BLACK");
	    hmap.put(SWT.COLOR_RED, "COLOR_RED");
	    hmap.put(SWT.COLOR_DARK_RED, "COLOR_DARK_RED");
	    hmap.put(SWT.COLOR_GREEN, "COLOR_GREEN");
	    hmap.put(SWT.COLOR_DARK_GREEN, "COLOR_DARK_GREEN");
	    hmap.put(SWT.COLOR_YELLOW, "COLOR_YELLOW");
	    hmap.put(SWT.COLOR_DARK_YELLOW, "COLOR_DARK_YELLOW");
	    hmap.put(SWT.COLOR_WIDGET_DARK_SHADOW, "COLOR_WIDGET_DARK_SHADOW");
	    hmap.put(SWT.COLOR_WIDGET_NORMAL_SHADOW, "COLOR_WIDGET_NORMAL_SHADOW");
	    hmap.put(SWT.COLOR_WIDGET_LIGHT_SHADOW, "COLOR_WIDGET_LIGHT_SHADOW");
	    hmap.put(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW, "COLOR_WIDGET_HIGHLIGHT_SHADOW");
	    hmap.put(SWT.COLOR_WIDGET_FOREGROUND, "COLOR_WIDGET_FOREGROUND");
	    hmap.put(SWT.COLOR_WIDGET_BACKGROUND, "COLOR_WIDGET_BACKGROUND");
	    hmap.put(SWT.COLOR_WIDGET_BORDER, "COLOR_WIDGET_BORDER");
	    hmap.put(SWT.COLOR_LIST_FOREGROUND, "COLOR_LIST_FOREGROUND");
	    hmap.put(SWT.COLOR_LIST_BACKGROUND, "COLOR_LIST_BACKGROUND");
	    hmap.put(SWT.COLOR_LIST_SELECTION, "COLOR_LIST_SELECTION");
	    hmap.put(SWT.COLOR_LIST_SELECTION_TEXT, "COLOR_LIST_SELECTION_TEXT");
	    hmap.put(SWT.COLOR_INFO_FOREGROUND, "COLOR_INFO_FOREGROUND");
	    hmap.put(SWT.COLOR_INFO_BACKGROUND, "COLOR_INFO_BACKGROUND");
	    hmap.put(SWT.COLOR_TITLE_FOREGROUND, "COLOR_TITLE_FOREGROUND");
	    hmap.put(SWT.COLOR_TITLE_BACKGROUND, "COLOR_TITLE_BACKGROUND");
	    hmap.put(SWT.COLOR_TITLE_BACKGROUND_GRADIENT, "COLOR_TITLE_BACKGROUND_GRADIENT");
	    hmap.put(SWT.COLOR_TITLE_INACTIVE_FOREGROUND, "COLOR_TITLE_INACTIVE_FOREGROUND");
	    hmap.put(SWT.COLOR_TITLE_INACTIVE_BACKGROUND, "COLOR_TITLE_INACTIVE_BACKGROUND");
	    hmap.put(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT, "COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT");
	    hmap.put(SWT.COLOR_LINK_FOREGROUND, "COLOR_LINK_FOREGROUND");
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
	void createExampleGroup () {
		super.createExampleGroup ();
		/* Create a group for the list */
		colorsGroup = new Group (exampleGroup, SWT.NONE);
		colorsGroup.setLayout (new GridLayout ());
		colorsGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		colorsGroup.setText ("Color");
	}

	/**
	 * Creates the "Example" widgets.
	 */
	@Override
	void createExampleWidgets () {

		/* Create the color table widget */
		/* Compute the widget style */
		int style = getDefaultStyle();
		colors = new Table (colorsGroup, style);
		colors.setHeaderVisible(true);
		// fill in the table.
		for (String columnTitle : columnTitles) {
			TableColumn tableColumn = new TableColumn(colors, SWT.NONE);
			tableColumn.setText(columnTitle);
			tableColumn.setToolTipText(ControlExample.getResourceString("Tooltip", columnTitle));
		}
		// fill in the Data. Put an empty line inbetween "Named" and "SWT" colors.
		boolean emptyLineFlag=false;
		for (Entry<Integer, String> entry : hmap.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();
			if (!emptyLineFlag) {
				TableItem item = new TableItem(colors, SWT.NONE);
				item.setText(value);
				item.setText(0, value);
				item.setText(1, "Named");
				item.setText(2, getRGBcolor(key));
				// the spaces will help the color cell be large enough to see
				item.setText(3, "            ");
				item.setBackground(3, display.getSystemColor(key));
				if (key == namedColorEnd) {
					TableItem emptyItem = new TableItem(colors, SWT.NONE);
					emptyItem.setText("");
					emptyLineFlag = true;
				}
			} else {
				TableItem item = new TableItem(colors, SWT.NONE);
				item.setText(value);
				item.setText(0, value + " ");
				item.setText(1, "System ");
				item.setText(2, getRGBcolor(key) + " ");
				// the spaces will help the color cell be large enough to see
				item.setText(3, "            ");
				item.setBackground(3, display.getSystemColor(key));
			}
		}
		for (int i = 0; i < columnTitles.length; i++) {
			colors.getColumn(i).pack();
		}
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {colors};
	}

	/**
	 * Gets the Tab name.
	 */
	@Override
	String getTabText () {
		return "Color";
	}

	/**
	 * Colors only needs Orientation, Size, and Other groups. Everything else will be removed.
	 */
	@Override
	void createSizeGroup () {
		super.createSizeGroup();

		packColumnsButton = new Button (sizeGroup, SWT.PUSH);
		packColumnsButton.setText (ControlExample.getResourceString("Pack_Columns"));
		packColumnsButton.addSelectionListener(widgetSelectedAdapter(event -> {
			packColumns ();
			setExampleWidgetSize ();
		}));
	}

	void packColumns () {
		int columnCount = colors.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TableColumn tableColumn = colors.getColumn(i);
			tableColumn.pack();
		}
	}

	String getRGBcolor(int id){
		Color color = display.getSystemColor(id);
		return String.format("(%d,%d,%d,%d)", color.getRed(), color.getGreen(),
				color.getBlue(), color.getAlpha());
	}

	@Override
	boolean rtlSupport() {
		return false;
	}

	/**
	 * Override the "Control" group.  The "Control" group
	 * is typically the right hand column in the tab.
	 */
	@Override
	void createControlGroup () {
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		controlGroup.setLayout (new GridLayout (2, true));
		controlGroup.setLayoutData (new GridData(SWT.FILL, SWT.FILL, false, false));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
		/* Create individual groups inside the "Control" group */
		createOtherGroup ();
		createSetGetGroup();
		createSizeGroup ();
		createOrientationGroup ();

		SelectionListener selectionListener = widgetSelectedAdapter(event -> {
			if ((event.widget.getStyle () & SWT.RADIO) != 0) {
				if (!((Button) event.widget).getSelection ()) return;
			}
			if (!handleTextDirection (event.widget)) {
				recreateExampleWidgets ();
			}
		});
		// attach listeners to the Orientation buttons
		rtlButton.addSelectionListener (selectionListener);
		ltrButton.addSelectionListener (selectionListener);
		defaultOrietationButton.addSelectionListener (selectionListener);
	}
}
