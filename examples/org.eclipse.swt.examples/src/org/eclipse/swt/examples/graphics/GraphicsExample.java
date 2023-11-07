/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.examples.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This class is the main class of the graphics application. Various "tabs" are
 * created and made visible by this class.
 */
public class GraphicsExample {

	Composite parent;
	GraphicsTab[] tabs;				// tabs to be found in the application
	GraphicsTab tab;				// the current tab
	GraphicsBackground background;	// used to store information about the background

	ToolBar toolBar;				// toolbar that contains backItem and dbItem
	Tree tabList;					// tree structure of tabs
	Text tabDesc;					// multi-line text widget that displays a tab description
	Sash hSash, vSash;
	Canvas canvas;
	Composite tabControlPanel;
	ToolItem backItem, dbItem;		// background, double buffer items
	Menu backMenu;					// background menu item

	List<Image> resources;			// stores resources that will be disposed
	List<GraphicsTab> tabs_in_order;		// stores GraphicsTabs in the order that they appear in the tree
	boolean animate = true;			// whether animation should happen

	static boolean advanceGraphics, advanceGraphicsInit;

	static final int MARGIN = 5;
	static final int SASH_SPACING = 1;
	static final int TIMER = 30;
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("examples_graphics"); //$NON-NLS-1$

/*
 * Default constructor is needed so that example launcher can create an instance.
 */
public GraphicsExample() {
	super();
}

public GraphicsExample(final Composite parent) {
	this.parent = parent;
	resources = new ArrayList<>();
	createControls(parent);
	setTab(tab);
	startAnimationTimer();
}

boolean checkAdvancedGraphics() {
	if (advanceGraphicsInit) return advanceGraphics;
	advanceGraphicsInit = true;
	Display display = parent.getDisplay();
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

void createControls(final Composite parent) {
	tabs = createTabs();
	createToolBar(parent);
	createTabList(parent);
	hSash = new Sash(parent, SWT.HORIZONTAL);
	createTabDesc(parent);
	vSash = new Sash(parent, SWT.VERTICAL);
	createCanvas(parent);
	createControlPanel(parent);

	FormData data;
	FormLayout layout = new FormLayout();
	parent.setLayout(layout);

	data = new FormData();
	data.left = new FormAttachment(0, MARGIN);
	data.top = new FormAttachment(0, MARGIN);
	data.right = new FormAttachment(100, -MARGIN);
	toolBar.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(0, MARGIN);
	data.top = new FormAttachment(toolBar, MARGIN);
	data.right = new FormAttachment(vSash, -SASH_SPACING);
	data.bottom = new FormAttachment(hSash, -SASH_SPACING);
	tabList.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(0, MARGIN);
	int offset = parent.getBounds().height - tabDesc.computeSize(SWT.DEFAULT, tabDesc.getLineHeight() * 10).y;
	data.top = new FormAttachment(null, offset);
	data.right = new FormAttachment(vSash, -SASH_SPACING);
	hSash.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(0, MARGIN);
	data.top = new FormAttachment(hSash, SASH_SPACING);
	data.right = new FormAttachment(vSash, -SASH_SPACING);
	data.bottom = new FormAttachment(100, -MARGIN);
	tabDesc.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(null, tabList.computeSize(SWT.DEFAULT, SWT.DEFAULT).x + 50);
	data.top = new FormAttachment(toolBar, MARGIN);
	data.bottom = new FormAttachment(100, -MARGIN);
	vSash.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(vSash, SASH_SPACING);
	data.top = new FormAttachment(toolBar, MARGIN);
	data.right = new FormAttachment(100, -MARGIN);
	data.bottom = new FormAttachment(tabControlPanel);
	canvas.setLayoutData(data);

	data = new FormData();
	data.left = new FormAttachment(vSash, SASH_SPACING);
	data.right = new FormAttachment(100, -MARGIN);
	data.bottom = new FormAttachment(100, -MARGIN);
	tabControlPanel.setLayoutData(data);

	vSash.addListener(SWT.Selection, event -> {
		Rectangle rect = hSash.getParent().getClientArea();
		event.x = Math.min (Math.max (event.x, 60), rect.width - 60);
		if (event.detail != SWT.DRAG) {
			FormData data1 = (FormData)vSash.getLayoutData();
			data1.left.offset = event.x;
			vSash.requestLayout();
			animate = true;
		} else {
			animate = false;
		}
	});
	hSash.addListener (SWT.Selection, event -> {
		Rectangle rect = vSash.getParent().getClientArea();
		event.y = Math.min (Math.max (event.y, tabList.getLocation().y + 60), rect.height - 60);
		if (event.detail != SWT.DRAG) {
			FormData data1 = (FormData)hSash.getLayoutData();
			data1.top.offset = event.y;
			hSash.requestLayout();
		}
	});
}

void createCanvas(Composite parent) {
	int style = SWT.NO_BACKGROUND;
	if (dbItem.getSelection()) style |= SWT.DOUBLE_BUFFERED;
	canvas = new Canvas(parent, style);
	canvas.addListener(SWT.Paint, event -> {
		GC gc = event.gc;
		Rectangle rect = canvas.getClientArea();
		Device device = gc.getDevice();
		Pattern pattern = null;
		if (background.getBgColor1() != null) {
			if (background.getBgColor2() != null) { // gradient
				pattern = new Pattern(device, 0, 0, rect.width,
						rect.height,
						background.getBgColor1(),
						background.getBgColor2());
				gc.setBackgroundPattern(pattern);
			} else {	// solid color
				gc.setBackground(background.getBgColor1());
			}
		} else if (background.getBgImage() != null) {		// image
			pattern = new Pattern(device, background.getBgImage());
			gc.setBackgroundPattern(pattern);
		}
		gc.fillRectangle(rect);
		GraphicsTab tab = getTab();
		if (tab != null) tab.paint(gc, rect.width, rect.height);
		if (pattern != null) pattern.dispose();
	});
}

void recreateCanvas() {
	if (dbItem.getSelection() == ((canvas.getStyle() & SWT.DOUBLE_BUFFERED) != 0)) return;
	Object data = canvas.getLayoutData();
	if (canvas != null) canvas.dispose();
	createCanvas(parent);
	canvas.setLayoutData(data);
	parent.layout(true, true);
}

/**
 * Creates the control panel
 */
void createControlPanel(Composite parent) {
	Group group;
	tabControlPanel = group = new Group(parent, SWT.NONE);
	group.setText(getResourceString("Settings")); //$NON-NLS-1$
	tabControlPanel.setLayout(new RowLayout());
}

void createToolBar(final Composite parent) {
	final Display display = parent.getDisplay();

	toolBar = new ToolBar(parent, SWT.FLAT);

	ToolItem  back = new ToolItem(toolBar, SWT.PUSH);
	back.setText(getResourceString("Back")); //$NON-NLS-1$
	back.setImage(loadImage(display, "back.gif")); //$NON-NLS-1$

	back.addListener(SWT.Selection, event -> {
		int index = tabs_in_order.indexOf(tab) - 1;
		if (index < 0)
			index = tabs_in_order.size() - 1;
		setTab(tabs_in_order.get(index));
	});

	ToolItem  next = new ToolItem(toolBar, SWT.PUSH);
	next.setText(getResourceString("Next")); //$NON-NLS-1$
	next.setImage(loadImage(display, "next.gif")); //$NON-NLS-1$
	next.addListener(SWT.Selection, event -> {
		int index = (tabs_in_order.indexOf(tab) + 1)%tabs_in_order.size();
		setTab(tabs_in_order.get(index));
	});

	ColorMenu colorMenu = new ColorMenu();

	// setup items to be contained in the background menu
	colorMenu.setColorItems(true);
	colorMenu.setPatternItems(checkAdvancedGraphics());
	colorMenu.setGradientItems(checkAdvancedGraphics());

	// create the background menu
	backMenu = colorMenu.createMenu(parent, gb -> {
		background = gb;
		backItem.setImage(gb.getThumbNail());
		if (canvas != null) canvas.redraw();
	});

	// initialize the background to the first item in the menu
	background = (GraphicsBackground)backMenu.getItem(0).getData();

	// background tool item
	backItem = new ToolItem(toolBar, SWT.PUSH);
	backItem.setText(getResourceString("Background")); //$NON-NLS-1$
	backItem.setImage(background.getThumbNail());
	backItem.addListener(SWT.Selection, event -> {
		if (event.widget == backItem) {
			final ToolItem toolItem = (ToolItem) event.widget;
			final ToolBar  toolBar = toolItem.getParent();
			Rectangle toolItemBounds = toolItem.getBounds();
			Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
			backMenu.setLocation(point.x, point.y + toolItemBounds.height);
			backMenu.setVisible(true);
		}
	});

	// double buffer tool item
	dbItem = new ToolItem(toolBar, SWT.CHECK);
	dbItem.setText(getResourceString("DoubleBuffer")); //$NON-NLS-1$
	dbItem.setImage(loadImage(display, "db.gif")); //$NON-NLS-1$
	dbItem.addListener(SWT.Selection, event -> setDoubleBuffered(dbItem.getSelection()));
}

/**
 * Creates and returns a thumbnail image.
 *
 * @param device
 * 			a device
 * @param name
 * 			filename of the image
 */
static Image createThumbnail(Device device, String name) {
	Image image = new Image(device, name);
	Rectangle src = image.getBounds();
	Image result = null;
	if (src.width != 16 || src.height != 16) {
		result = new Image(device, 16, 16);
		GC gc = new GC(result);
		Rectangle dest = result.getBounds();
		gc.drawImage(image, src.x, src.y, src.width, src.height, dest.x, dest.y, dest.width, dest.height);
		gc.dispose();
	}
	if (result != null) {
		image.dispose();
		return result;
	}
	return image;
}

/**
 * Creates an image based on a gradient pattern made up of two colors.
 *
 * @param device - The Device
 * @param color1 - The first color used to create the image
 * @param color2 - The second color used to create the image
 *
 * */
static Image createImage(Device device, Color color1, Color color2, int width, int height) {
	Image image = new Image(device, width, height);
	GC gc = new GC(image);
	Rectangle rect = image.getBounds();
	Pattern pattern = new Pattern(device, rect.x, rect.y, rect.width - 1,
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
 * @param device - The Device
 * @param color - The color used to create the image
 *
 * */
static Image createImage(Device device, Color color) {
	Image image = new Image(device, 16, 16);
	GC gc = new GC(image);
	gc.setBackground(color);
	Rectangle rect = image.getBounds();
	gc.fillRectangle(rect);
	if (color.equals(device.getSystemColor(SWT.COLOR_BLACK))) {
		gc.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
	}
	gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height - 1);
	gc.dispose();
	return image;
}

void createTabList(Composite parent) {
	tabList = new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	Arrays.sort(tabs, (tab0, tab1) -> tab0.getText().compareTo(tab1.getText()));
	HashSet<String> set = new HashSet<>();
	for (GraphicsTab tab : tabs) {
		set.add(tab.getCategory());
	}
	String[] categories = new String[set.size()];
	set.toArray(categories);
	Arrays.sort(categories);
	for (String text : categories) {
		TreeItem item = new TreeItem(tabList, SWT.NONE);
		item.setText(text);
	}
	tabs_in_order = new ArrayList<>();
	TreeItem[] items = tabList.getItems();
	for (TreeItem item : items) {
		for (GraphicsTab tab : tabs) {
			if (item.getText().equals(tab.getCategory())) {
				TreeItem item1 = new TreeItem(item, SWT.NONE);
				item1.setText(tab.getText());
				item1.setData(tab);
				tabs_in_order.add(tab);
			}
		}
	}
	tabList.addListener(SWT.Selection, event -> {
		TreeItem item = (TreeItem)event.item;
		if (item != null) {
			GraphicsTab gt = (GraphicsTab)item.getData();
			if (gt == tab) return;
			setTab((GraphicsTab)item.getData());
		}
	});
}

/**
 * Creates the multi-line text widget that will contain the tab description.
 * */
void createTabDesc(Composite parent) {
	tabDesc = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
	tabDesc.setEditable(false);
	tabDesc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
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
		new AlphaTab(this),
		new BallTab(this),
		new CountDownTab(this),
		new CurvesSWTTab(this),
		new CurvesTab(this),
		new CustomFontTab(this),
		new FontBounceTab(this),
		new GradientTab(this),
		new ImageTransformTab(this),
		new ShapesTab(this),
		new MazeTab(this),
		new RGBTab(this),
		new SpiralTab(this),
		new CardsTab(this),
		new LineCapTab(this),
		new InterpolationTab(this),
		new PathClippingTab(this),
		new PathClippingAnimTab(this),
		new LineStyleTab(this),
		new LineJoinTab(this),
		new RegionClippingTab(this),
		new CustomAlphaTab(this),
		new TextAntialiasTab(this),
		new GraphicAntialiasTab(this),
		new ImageFlipTab(this),
		new ImageScaleTab(this),
		new PathTab(this),
	};
}

/**
 * Disposes all resources created by the receiver.
 */
public void dispose() {
	if (tabs != null) {
		for (GraphicsTab tab : tabs) {
			tab.dispose();
		}
	}
	tabs = null;
	if (resources != null) {
		for (Image image : resources) {
			if (image != null) {
				image.dispose();
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
	for (TreeItem item : items) {
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

static Image loadImage (Device device, Class<GraphicsExample> clazz, String string) {
	InputStream stream = clazz.getResourceAsStream (string);
	if (stream == null) return null;
	Image image = null;
	try {
		image = new Image (device, stream);
	} catch (SWTException ex) {
	} finally {
		try {
			stream.close ();
		} catch (IOException ex) {}
	}
	return image;
}

Image loadImage(Device device, String name) {
	Image image = loadImage(device, GraphicsExample.class, name);
	if (image != null) resources.add(image);
	return image;
}

public Shell open(final Display display) {
	Shell shell = new Shell(display);
	shell.setText(getResourceString("GraphicsExample")); //$NON-NLS-1$
	final GraphicsExample example = new GraphicsExample(shell);
	shell.addListener(SWT.Close, event -> example.dispose());
	shell.open();
	return shell;
}

/**
 * Redraws the current tab.
 */
public void redraw() {
	canvas.redraw();
}

/**
 * Sets wheter the canvas is double buffered or not.
 */
public void setDoubleBuffered(boolean doubleBuffered) {
	dbItem.setSelection(doubleBuffered);
	recreateCanvas();
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
	for (Control control: children) {
		control.dispose();
	}
	if (this.tab != null) this.tab.dispose();
	this.tab = tab;
	if (tab != null) {
		setDoubleBuffered(tab.getDoubleBuffered());
		tab.createControlPanel(tabControlPanel);
		tabDesc.setText(tab.getDescription());
	} else {
		tabDesc.setText("");
	}
	FormData data = (FormData)tabControlPanel.getLayoutData();
	children = tabControlPanel.getChildren();
	if (children.length != 0) {
		data.top = null;
	} else {
		data.top = new FormAttachment(100, -MARGIN);
	}
	parent.layout(true, true);
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
	final Display display = parent.getDisplay();
	display.timerExec(TIMER, new Runnable() {
		@Override
		public void run() {
			if (canvas.isDisposed()) return;
			int timeout = TIMER;
			GraphicsTab tab = getTab();
			if (tab instanceof AnimatedGraphicsTab) {
				AnimatedGraphicsTab animTab = (AnimatedGraphicsTab) tab;
				if (animate && animTab.getAnimation()) {
					Rectangle rect = canvas.getClientArea();
					animTab.next(rect.width, rect.height);
					canvas.redraw();
					canvas.update();
				}
				timeout =  animTab.getAnimationTime();
			}
			display.timerExec(timeout, this);
		}
	});
}

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new GraphicsExample().open(display);
	while (shell != null && !shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
