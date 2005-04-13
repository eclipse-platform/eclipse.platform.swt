/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class GraphicsExample {
	
	Composite parent;
	GraphicsTab[] tabs;
	GraphicsTab tab;
	Object[] tabBackground;
	
	boolean animate;
	
	Listener redrawListener;
	
	ToolBar toolBar;
	Tree tabList;
	Canvas canvas;
	Composite controlPanel, tabPanel;
	ToolItem playItem, pauseItem, backItem, dbItem;
	Spinner timerSpinner;
	
	Menu backMenu;
	MenuItem customMI;
	Image customImage;
	Color customColor;
	
	Vector images;
	
	static boolean advanceGraphics, advanceGraphicsInit;
	
	static final int TIMER = 30;
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("examples_graphics"); //$NON-NLS-1$

public GraphicsExample(final Composite parent) {
	this.parent = parent;
	redrawListener = new Listener() {
		public void handleEvent(Event e) {
			redraw();
		}
	};
	GridData data;
	GridLayout layout = new GridLayout(3, false);
	layout.horizontalSpacing = 1;
	parent.setLayout(layout);
	tabs = createTabs();	
	images = new Vector();	
	createToolBar(parent);
	createTabList(parent);
	final Sash sash = new Sash(parent, SWT.VERTICAL);
	createTabPanel(parent);
	data = new GridData(SWT.FILL, SWT.CENTER, true, false);
	data.horizontalSpan = 3;
	toolBar.setLayoutData(data);	
	data = new GridData(SWT.CENTER, SWT.FILL, false, true);
	data.widthHint = tabList.computeSize(SWT.DEFAULT, SWT.DEFAULT).x + 50;
	tabList.setLayoutData(data);
	data = new GridData(SWT.CENTER, SWT.FILL, false, true);
	sash.setLayoutData(data);
	data = new GridData(SWT.FILL, SWT.FILL, true, true);
	tabPanel.setLayoutData(data);
	sash.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			if (event.detail != SWT.DRAG) {
				GridData data = (GridData)tabList.getLayoutData();				
				data.widthHint = event.x - tabList.computeTrim(0, 0, 0, 0).width;
				parent.layout(true);
				animate = pauseItem.getEnabled();
			} else {
				animate = false;
			}
		}
	});	
	setTab(tab);
	startAnimationTimer();
}

boolean checkAdvancedGraphics() {
	if (advanceGraphicsInit) return advanceGraphics;
	advanceGraphicsInit = true;
	Display display = Display.getCurrent();
	try {
		Path path = new Path(display);
		path.dispose();
	} catch (SWTException e) {
		Shell shell = display.getActiveShell(), newShell = null;
		if (shell == null) shell = newShell = new Shell(display);
		MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
		dialog.setText(RESOURCE_BUNDLE.getString("Warning")); //$NON-NLS-1$
		dialog.setMessage(RESOURCE_BUNDLE.getString("LibNotFound")); //$NON-NLS-1$
		dialog.open();
		if (newShell != null) newShell.dispose();
		return false;
	}
	return advanceGraphics = true;
}

void createCanvas(Composite parent) {
	canvas = new Canvas(parent, SWT.NO_BACKGROUND);
	canvas.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			GC gc;
			Rectangle rect = canvas.getClientArea();
			Image buffer = null;
			if (dbItem.getSelection()) {
				buffer = new Image(canvas.getDisplay(), rect);
				gc = new GC(buffer);
			} else {
				gc = event.gc;
			}
			paintBackground(gc, rect);
			GraphicsTab tab = getTab();
			if (tab != null) tab.paint(gc, rect.width, rect.height);
			if (gc != event.gc) gc.dispose();
			if (buffer != null) {
				event.gc.drawImage(buffer, 0, 0);
				buffer.dispose();
			}
		}
	});
}

void createControlPanel(Composite parent) {
	Group group;
	controlPanel = group = new Group(parent, SWT.NONE);
	group.setText(getResourceString("Settings")); //$NON-NLS-1$
	controlPanel.setLayout(new RowLayout());
}

void createTabPanel(Composite parent) {
	tabPanel = new Composite(parent, SWT.NONE);
	GridData data;
	GridLayout layout = new GridLayout(1, false);
	layout.marginHeight = layout.marginWidth = 0;
	tabPanel.setLayout(layout);
	createCanvas(tabPanel);
	createControlPanel(tabPanel);
	data = new GridData(SWT.FILL, SWT.FILL, true, true);
	canvas.setLayoutData(data);	
	data = new GridData(SWT.FILL, SWT.CENTER, true, false);
	controlPanel.setLayoutData(data);
}

void createToolBar(final Composite parent) {
	final Display display = parent.getDisplay();
	
	toolBar = new ToolBar(parent, SWT.FLAT);
	Listener toolBarListener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Selection: {
					if (event.widget == playItem) {
						animate = true;
						playItem.setEnabled(!animate);
						pauseItem.setEnabled(animate);
					} else if (event.widget == pauseItem) {
						animate = false;
						playItem.setEnabled(!animate);
						pauseItem.setEnabled(animate);
					} else if (event.widget == backItem) {
						final ToolItem toolItem = (ToolItem) event.widget;
						final ToolBar  toolBar = toolItem.getParent();			
						Rectangle toolItemBounds = toolItem.getBounds();
						Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
						backMenu.setLocation(point.x, point.y + toolItemBounds.height);
						backMenu.setVisible(true);
					}
				}
				break;
			}
		}
	};
	
	playItem = new ToolItem(toolBar, SWT.PUSH);
	playItem.setText(getResourceString("Play")); //$NON-NLS-1$
	playItem.setImage(loadImage(display, "play.gif")); //$NON-NLS-1$
	playItem.addListener(SWT.Selection, toolBarListener);
	
	pauseItem = new ToolItem(toolBar, SWT.PUSH);
	pauseItem.setText(getResourceString("Pause")); //$NON-NLS-1$
	pauseItem.setImage(loadImage(display, "pause.gif")); //$NON-NLS-1$
	pauseItem.addListener(SWT.Selection, toolBarListener);
	
	backItem = new ToolItem(toolBar, SWT.PUSH);
	backItem.setText(getResourceString("Background")); //$NON-NLS-1$
	backItem.addListener(SWT.Selection, toolBarListener);
	String[] names = new String[]{
		getResourceString("White"), //$NON-NLS-1$
		getResourceString("Black"), //$NON-NLS-1$
		getResourceString("Red"), //$NON-NLS-1$
		getResourceString("Green"), //$NON-NLS-1$
		getResourceString("Blue"), //$NON-NLS-1$
		getResourceString("CustomColor"), //$NON-NLS-1$
	};
	Color[] colors = new Color[]{
		display.getSystemColor(SWT.COLOR_WHITE),	
		display.getSystemColor(SWT.COLOR_BLACK),	
		display.getSystemColor(SWT.COLOR_RED),	
		display.getSystemColor(SWT.COLOR_GREEN),	
		display.getSystemColor(SWT.COLOR_BLUE),
		null,
	};	
	backMenu = new Menu(parent);
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			MenuItem item = (MenuItem)event.widget;
			if (customMI == item) {
				ColorDialog dialog = new ColorDialog(parent.getShell());
				RGB rgb = dialog.open();
				if (rgb == null) return;
				if (customColor != null) customColor.dispose();
				customColor = new Color(display, rgb);
				if (customImage != null) customImage.dispose();
				customImage = createImage(display, customColor);
				item.setData(new Object[]{customColor, customImage});
				item.setImage(customImage);
			}
			tabBackground = (Object[])item.getData();
			backItem.setImage((Image)tabBackground[1]);
			canvas.redraw();
		}
	};
	for (int i = 0; i < names.length; i++) {
		MenuItem item = new MenuItem(backMenu, SWT.NONE);
		item.setText(names[i]);
		item.addListener(SWT.Selection, listener);
		Image image = null;
		if (colors[i] != null) {
			image = createImage(display, colors[i]);
			images.addElement(image);
			item.setImage(image);
		} else {
			// custom menu item
			customMI = item;
		}
		item.setData(new Object[]{colors[i], image});
		if (tabBackground == null) {
			tabBackground = (Object[])item.getData();
			backItem.setImage((Image)tabBackground[1]);
		}
	}
	
	dbItem = new ToolItem(toolBar, SWT.CHECK);
	dbItem.setText(getResourceString("DoubleBuffer")); //$NON-NLS-1$
	dbItem.setImage(loadImage(display, "db.gif")); //$NON-NLS-1$

	ToolItem separator = new ToolItem(toolBar, SWT.SEPARATOR);
	Composite comp = new Composite(toolBar, SWT.NONE);
	GridData data;
	GridLayout layout = new GridLayout(1, false);
	layout.verticalSpacing = 0;
	layout.marginWidth = layout.marginHeight = 3;
	comp.setLayout(layout);
	timerSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	timerSpinner.setLayoutData(data);
	Label label = new Label(comp, SWT.NONE);
	label.setText(getResourceString("Animation")); //$NON-NLS-1$
	data = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	label.setLayoutData(data);
	timerSpinner.setMaximum(1000);
	timerSpinner.setSelection(TIMER);
	timerSpinner.setSelection(TIMER);
	separator.setControl(comp);
	separator.setWidth(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
}

Image createImage(Display display, Color color) {
	Image image = new Image(display, 16, 16);
	GC gc = new GC(image);
	gc.setBackground(color);
	Rectangle rect = image.getBounds();
	gc.fillRectangle(rect);
	if (color.equals(display.getSystemColor(SWT.COLOR_BLACK))) {
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	}
	gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height - 1);
	gc.dispose();
	return image;
}

void createTabList(Composite parent) {
	tabList = new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	HashSet set = new HashSet();
	for (int i = 0; i < tabs.length; i++) {
		GraphicsTab tab = tabs[i];
		set.add(tab.getCategory());
	}
	for (Iterator iter = set.iterator(); iter.hasNext();) {
		String text = (String) iter.next();
		TreeItem item = new TreeItem(tabList, SWT.NONE);
		item.setText(text);
	}
	TreeItem[] items = tabList.getItems();
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items[i];
		for (int j = 0; j < tabs.length; j++) {
			GraphicsTab tab = tabs[j];
			if (item.getText().equals(tab.getCategory())) {
				TreeItem item1 = new TreeItem(item, SWT.NONE);
				item1.setText(tab.getText());
				item1.setData(tab);
			}
		}
	}
	tabList.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem)event.item;
			if (item != null) {
				setTab((GraphicsTab)item.getData());
			}
		}
	});
}

GraphicsTab[] createTabs() {
	return new GraphicsTab[] {
		new LineTab(this),
		new StarPolyTab(this),
		tab = new IntroTab(this),
		new BlackHoleTab(this),
	};
}

/**
 * Disposes all resources created by the receiver.
 */
public void dispose() {
	if (tabs != null) {
		for (int i = 0; i < tabs.length; i++) {
			GraphicsTab tab = tabs[i];
			tab.dispose();
		}
	}
	tabs = null;
	if (images != null) {
		for (int i = 0; i < images.size(); i++) {
			((Image)images.elementAt(i)).dispose();
		}
	}
	images = null;
	if (customColor != null) customColor.dispose();
	customColor = null;
	if (customImage != null) customImage.dispose();
	customImage = null;
}

TreeItem findItemByData(TreeItem[] items, Object data) {
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items[i];
		if (item.getData() == data) return item;
		item = findItemByData(item.getItems(), data);
		if (item != null) return item;
	}
	return null;
}

/**
 * Gets the current tab.
 */
public GraphicsTab getTab() {
	return tab;
}

Listener getRedrawListener() {
	return redrawListener;
}

/**
 * Gets a string from the resource bundle.
 * We don't want to crash because of a missing String.
 * Returns the key if not found.
 */
static String getResourceString(String key) {
	try {
		return RESOURCE_BUNDLE.getString(key);
	} catch (MissingResourceException e) {
		return key;
	} catch (NullPointerException e) {
		return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
	}			
}

static Image loadImage (Display display, Class clazz, String string) {
	InputStream stream = clazz.getResourceAsStream (string);
	if (stream == null) return null;
	Image image = null;
	try {
		image = new Image (display, stream);
	} catch (SWTException ex) {
	} finally {
		try {
			stream.close ();
		} catch (IOException ex) {}
	}
	return image;
}

Image loadImage(Display display, String name) {
	Image image = loadImage(display, GraphicsExample.class, name);
	if (image != null) images.addElement(image);
	return image;
}

void paintBackground(GC gc, Rectangle rect) {
	gc.setBackground((Color)tabBackground[0]);
	gc.fillRectangle(rect);
}

/**
 * Redraws the current tab.
 */
public void redraw() {
	canvas.redraw();
}

/**
 * Grabs input focus.
 */
public void setFocus() {
	tabList.setFocus();
}

/**
 * Sets the current tab.
 */
public void setTab(GraphicsTab tab) {
	this.tab = tab;
	Control[] children = controlPanel.getChildren();
	for (int i = 0; i < children.length; i++) {
		Control control = children[i];
		control.dispose();
	}
	if (tab != null) {
		tab.createControlPanel(controlPanel);
		animate = tab.isAnimated();
	}
	playItem.setEnabled(!animate);
	pauseItem.setEnabled(animate);
	GridData data = (GridData)controlPanel.getLayoutData();
	children = controlPanel.getChildren();
	data.exclude = children.length == 0;
	controlPanel.setVisible(!data.exclude);
	if (data.exclude) {
		tabPanel.layout();
	} else {
		tabPanel.layout(children);
	}
	if (tab != null) {
		TreeItem[] selection = tabList.getSelection();
		if (selection.length == 0 || selection[0].getData() != tab) {
			TreeItem item = findItemByData(tabList.getItems(), tab);
			if (item != null) tabList.setSelection(new TreeItem[]{item});
		}
	}
	canvas.redraw();
}

void startAnimationTimer() {
	final Display display = Display.getCurrent();
	display.timerExec(timerSpinner.getSelection(), new Runnable() {
		public void run() {
			if (canvas.isDisposed()) return;
			if (animate) {
				GraphicsTab tab = getTab();
				if (tab != null && tab.isAnimated()) {
					Rectangle rect = canvas.getClientArea();
					tab.next(rect.width, rect.height);
					canvas.redraw();
					canvas.update();
				}
			}
			display.timerExec(timerSpinner.getSelection(), this);
		}
	});
}

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText(getResourceString("SWTGraphics")); //$NON-NLS-1$
	final GraphicsExample example = new GraphicsExample(shell);
	shell.addListener(SWT.Close, new Listener() {
		public void handleEvent(Event event) {
			example.dispose();
		}
	});	
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
}
}
