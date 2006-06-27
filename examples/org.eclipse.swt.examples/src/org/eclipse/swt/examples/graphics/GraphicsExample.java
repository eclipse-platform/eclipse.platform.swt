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

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class is the main class of the graphics application. Various "tabs" are
 * created and made visible by this class.
 */
public class GraphicsExample {
	
	Composite parent;
	GraphicsTab[] tabs;				// tabs to be found in the application
	GraphicsTab tab;				// the current tab
	GraphicsBackground background;	// used to store information about the background
	
	ToolBar toolBar;					// toolbar that contains backItem and dbItem
	Tree tabList;						// tree structure of tabs
	Canvas canvas;
	Composite mainControlPanel, tabControlPanel;  // control panel applicable to all tabs and 
												  // control panel specific to a tab
	ToolItem backItem, dbItem;		// background, double buffer items
	
	Menu backMenu;						// background menu item
	
	ArrayList resources;			// stores resources that will be disposed

	static boolean advanceGraphics, advanceGraphicsInit;
	
	static final int TIMER = 30;
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("examples_graphics"); //$NON-NLS-1$

public GraphicsExample(final Composite parent) {
	this.parent = parent;
	GridData data;
	GridLayout layout = new GridLayout(3, false);
	layout.horizontalSpacing = 1;
	parent.setLayout(layout);
	tabs = createTabs();
	resources = new ArrayList();
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
	mainControlPanel.setLayoutData(data);
	sash.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			if (event.detail != SWT.DRAG) {
				GridData data = (GridData)tabList.getLayoutData();				
				data.widthHint = event.x - tabList.computeTrim(0, 0, 0, 0).width;
				parent.layout(true);
				if (tab instanceof AnimatedGraphicsTab) {
					AnimatedGraphicsTab agt = ((AnimatedGraphicsTab) tab);
					agt.setAnimation(agt.pauseItem.getEnabled()); 
				}
			} else {
				if (tab instanceof AnimatedGraphicsTab) {
					AnimatedGraphicsTab agt = ((AnimatedGraphicsTab) tab);
					agt.setAnimation(false);
				}
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

/**
 * This method is used to paint the background.
 * If both colors have been initialized, a gradient will be created for the
 * background, otherwise; If only the first color is initialized, that color
 * will be applied to the background, otherwise; If the image has been
 * initialized, the image will be applied as a background pattern.
 */
void paintBackground(GC gc, Rectangle rect) {
	Pattern pattern = null;
	if (background.getBgColor1() != null) {
		if (background.getBgColor2() != null) { // gradient
			pattern = new Pattern(Display.getCurrent(), 0, 0, rect.width, 
					rect.height,
					background.getBgColor1(),
					background.getBgColor2());
			gc.setBackgroundPattern(pattern);
		} else {	// solid color
			gc.setBackground(background.getBgColor1());
		}
	} else if (background.getBgImage() != null) {		// image
		pattern = new Pattern(Display.getCurrent(), background.getBgImage());
		gc.setBackgroundPattern(pattern);
	}
	gc.fillRectangle(rect);
	if (pattern != null) pattern.dispose();
}

/**
 * Creates the control panel 
 * @param parent
 */
void createControlPanel(Composite parent) {
	Group group;
	tabControlPanel = group = new Group(parent, SWT.NONE);
	group.setText(getResourceString("Settings")); //$NON-NLS-1$
	tabControlPanel.setLayout(new RowLayout());
}

void createTabPanel(Composite parent) {
	mainControlPanel = new Composite(parent, SWT.NONE);
	GridData data;
	GridLayout layout = new GridLayout(1, false);
	layout.marginHeight = layout.marginWidth = 0;
	mainControlPanel.setLayout(layout);
	createCanvas(mainControlPanel);
	createControlPanel(mainControlPanel);
	data = new GridData(SWT.FILL, SWT.FILL, true, true);
	canvas.setLayoutData(data);	
	data = new GridData(SWT.FILL, SWT.CENTER, true, false);
	tabControlPanel.setLayoutData(data);
}

void createToolBar(final Composite parent) {
	final Display display = parent.getDisplay();
	
	toolBar = new ToolBar(parent, SWT.FLAT);
	
	ColorMenu colorMenu = new ColorMenu();
	
	// setup items to be contained in the background menu
	colorMenu.setColorItems(true);
	colorMenu.setPatternItems(true);
	colorMenu.setGradientItems(true);
	
	// create the background menu
	backMenu = colorMenu.createMenu(parent, new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			background = gb;
			backItem.setImage(gb.getBgImage());
			if (canvas != null) canvas.redraw();
		}
	});
	
	// initialize the background to the first item in the menu
	background =(GraphicsBackground)backMenu.getItem(0).getData();
	
	// background tool item
	backItem = new ToolItem(toolBar, SWT.PUSH);
	backItem.setText(getResourceString("Background")); //$NON-NLS-1$
	backItem.setImage(background.getBgImage());
	backItem.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			if (event.widget == backItem) {
				final ToolItem toolItem = (ToolItem) event.widget;
				final ToolBar  toolBar = toolItem.getParent();
				Rectangle toolItemBounds = toolItem.getBounds();
				Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
				backMenu.setLocation(point.x, point.y + toolItemBounds.height);
				backMenu.setVisible(true);
			}
		}
	});
	
	// double buffer tool item
	dbItem = new ToolItem(toolBar, SWT.CHECK);
	dbItem.setText(getResourceString("DoubleBuffer")); //$NON-NLS-1$
	dbItem.setImage(loadImage(display, "db.gif")); //$NON-NLS-1$
}

/**
 * Creates an image based on a gradient pattern made up of two colors.
 * 
 * @param display - The Display
 * @param color1 - The first color used to create the image
 * @param color2 - The second color used to create the image
 * 
 * */
static Image createImage(Display display, Color color1, Color color2, int width, int height) {
	Image image = new Image(display, width, height);
	GC gc = new GC(image);
	Rectangle rect = image.getBounds();
	Pattern pattern = new Pattern(display, rect.x, rect.y, rect.width - 1,
				rect.height - 1, color1, color2);
	gc.setBackgroundPattern(pattern);
	gc.fillRectangle(rect);
	gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height - 1);
	gc.dispose();
	pattern.dispose();
	return image;
}

/**
 * Creates an image based on the color provided and returns it.
 * 
 * @param display - The Display
 * @param color - The color used to create the image
 * 
 * */
static Image createImage(Display display, Color color) {
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
				GraphicsTab gt = (GraphicsTab)item.getData();
				if (gt == tab) return;
				setTab((GraphicsTab)item.getData());
			}
		}
	});
}

/**
 * Initializes the GraphicsTab instances that will be contained in GraphicsExample.
 * */
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
	if (resources != null) {
		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i) instanceof Resource) {
				((Resource)resources.get(i)).dispose();
			}
		}
	}
	resources = null;
	
	if (backMenu != null) {
		backMenu.dispose();
		backMenu = null;
	}
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
	if (image != null) resources.add(image);
	return image;
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
	Control[] children = tabControlPanel.getChildren();
	for (int i = 0; i < children.length; i++) {
		Control control = children[i];
		control.dispose();
	}
	if (this.tab != null) this.tab.dispose();
	this.tab = tab;
	if (tab != null) {
		tab.createControlPanel(tabControlPanel);
	}

	GridData data = (GridData)tabControlPanel.getLayoutData();
	children = tabControlPanel.getChildren();
	data.exclude = children.length == 0;
	tabControlPanel.setVisible(!data.exclude);
	if (data.exclude) {
		mainControlPanel.layout();
	} else {
		mainControlPanel.layout(children);
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

/**
 * Starts the animation if the animate flag is set.
 */
void startAnimationTimer() {
	final Display display = Display.getCurrent();
	display.timerExec(TIMER, new Runnable() {
		public void run() {
			if (canvas.isDisposed()) return;
			int timeout = TIMER;
			GraphicsTab tab = getTab();
			if (tab instanceof AnimatedGraphicsTab) {
				AnimatedGraphicsTab animTab = (AnimatedGraphicsTab) tab;	
				if (animTab.getAnimation()) {
					Rectangle rect = canvas.getClientArea();
					animTab.next(rect.width, rect.height);
					canvas.redraw();
					canvas.update();
				}
				timeout =  animTab.getSelectedAnimationTime();
			}
			display.timerExec(timeout, this);
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
