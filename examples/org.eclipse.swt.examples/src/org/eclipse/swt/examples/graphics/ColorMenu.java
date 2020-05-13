/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This class utilizes the factory design pattern to create menus that may
 * contain color items, pattern items and gradient items. To create a menu:
 * first set the menu items that you wish to have appear in the menu by calling
 * the setters (setColorItems(), setPatternItems(), setGradientItems()), and
 * then call createMenu() to get an instance of a menu. By default, the menu
 * will contain color items.
 */
public class ColorMenu {

	boolean enableColorItems, enablePatternItems, enableGradientItems;

	public ColorMenu() {
		enableColorItems = true;
	}

	/**
	 * Method used to specify whether or not the color menu items will appear in
	 * the menu.
	 *
	 * @param enable
	 *            A boolean flag - true to make the color menu items visible in
	 *            the menu; false otherwise.
	 */
	public void setColorItems(boolean enable) {
		enableColorItems = enable;
	}

	/**
	 * @return true if color menu items are contained in the menu; false otherwise.
	 * */
	public boolean getColorItems() {
		return enableColorItems;
	}

	/**
	 * Method used to specify whether or not the pattern menu items will appear
	 * in the menu.
	 *
	 * @param enable
	 *            A boolean flag - true to make the pattern menu items visible
	 *            in the menu; false otherwise.
	 */
	public void setPatternItems(boolean enable) {
		enablePatternItems = enable;
	}

	/**
	 * @return true if pattern menu items are contained in the menu; false otherwise.
	 * */
	public boolean getPatternItems() {
		return enablePatternItems;
	}

	/**
	 * Method used to specify whether or not the gradient menu items will appear
	 * in the menu.
	 *
	 * @param enable
	 *            A boolean flag - true to make the gradient menu items visible
	 *            in the menu; false otherwise.
	 */
	public void setGradientItems(boolean enable) {
		enableGradientItems = enable;
	}

	/**
	 * @return true if gradient menu items are contained in the menu; false otherwise.
	 */
	public boolean getGradientItems() {
		return enableGradientItems;
	}

	/**
	 * Creates and returns the menu based on the settings provided via
	 * setColorItems(), setPatternItems() and setGradientItems()
	 *
	 * @return A menu based on the settings
	 */
	public Menu createMenu(Control parent, ColorListener cl) {
		Menu menu = new Menu(parent);

		MenuItemListener menuItemListener = createMenuItemListener(parent);
		menu.addListener(SWT.Selection, menuItemListener);
		menu.addListener(SWT.Dispose, menuItemListener);
		menuItemListener.setColorListener(cl);

		if (enableColorItems) {
			addColorItems(menu, menuItemListener, menuItemListener.getMenuResources());
		}
		if (enablePatternItems) {
			addPatternItems(menu, menuItemListener, menuItemListener.getMenuResources());
		}
		if (enableGradientItems) {
			addGradientItems(menu, menuItemListener);
		}
		return menu;
	}

	/** Adds the colors items to the menu. */
	private void addColorItems(Menu menu, MenuItemListener menuListener,
			List<Resource> menuResources) {
		Display display = menu.getDisplay();

		if (menu.getItemCount() != 0) {
			new MenuItem(menu, SWT.SEPARATOR);
		}

		// color names
		String[] names = new String[]{
			GraphicsExample.getResourceString("White"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Black"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Red"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Green"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Blue"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Yellow"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Cyan"), //$NON-NLS-1$
		};

		// colors needed for the background menu
		Color[] colors = new Color[]{
			display.getSystemColor(SWT.COLOR_WHITE),
			display.getSystemColor(SWT.COLOR_BLACK),
			display.getSystemColor(SWT.COLOR_RED),
			display.getSystemColor(SWT.COLOR_GREEN),
			display.getSystemColor(SWT.COLOR_BLUE),
			display.getSystemColor(SWT.COLOR_YELLOW),
			display.getSystemColor(SWT.COLOR_CYAN),
		};

		// add standard color items to menu
		for (int i = 0; i < names.length; i++) {
			MenuItem item = new MenuItem(menu, SWT.NONE);
			item.setText(names[i]);
			item.addListener(SWT.Selection, menuListener);
			Color color = colors[i];
			GraphicsBackground gb = new GraphicsBackground();
			Image image = GraphicsExample.createImage(display, color);
			gb.setBgColor1(color);
			gb.setBgImage(image);
			gb.setThumbNail(image);
			menuResources.add(image);
			item.setImage(image);
			item.setData(gb);
		}

		// add custom color item to menu
		menuListener.customColorMI = new MenuItem(menu, SWT.NONE);
		menuListener.customColorMI.setText(GraphicsExample.getResourceString("CustomColor")); //$NON-NLS-1$
		menuListener.customColorMI.addListener(SWT.Selection, menuListener);
		GraphicsBackground gb = new GraphicsBackground();
		menuListener.customColorMI.setData(gb);
	}

	/** Adds the pattern items to the menu. */
	private void addPatternItems(Menu menu, MenuItemListener menuListener,
			List<Resource> menuResources) {
		Display display = menu.getDisplay();

		if (menu.getItemCount() != 0) {
			new MenuItem(menu, SWT.SEPARATOR);
		}

		// pattern names
		String[] names = new String[]{
			GraphicsExample.getResourceString("Pattern1"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Pattern2"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Pattern3"), //$NON-NLS-1$
		};

		// pattern images
		Image[] images = new Image[]{
			loadImage(display, "pattern1.jpg", menuResources),
			loadImage(display, "pattern2.jpg", menuResources),
			loadImage(display, "pattern3.jpg", menuResources),
		};

		// add the pre-defined patterns to the menu
		for (int i = 0; i < names.length; i++) {
			MenuItem item = new MenuItem(menu, SWT.NONE);
			item.setText(names[i]);
			item.addListener(SWT.Selection, menuListener);
			Image image = images[i];
			GraphicsBackground gb = new GraphicsBackground();
			gb.setBgImage(image);
			gb.setThumbNail(image);
			item.setImage(image);
			item.setData(gb);
		}

		// add the custom pattern item
		menuListener.customPatternMI = new MenuItem(menu, SWT.NONE);
		menuListener.customPatternMI.setText(GraphicsExample.getResourceString("CustomPattern")); //$NON-NLS-1$
		menuListener.customPatternMI.addListener(SWT.Selection, menuListener);
		GraphicsBackground gb = new GraphicsBackground();
		menuListener.customPatternMI.setData(gb);
	}

	/** Adds the gradient menu item. */
	private void addGradientItems(Menu menu, MenuItemListener menuListener) {
		if (menu.getItemCount() != 0) {
			new MenuItem(menu, SWT.SEPARATOR);
		}
		menuListener.customGradientMI = new MenuItem(menu, SWT.NONE);
		menuListener.customGradientMI.setText(GraphicsExample.getResourceString("Gradient")); //$NON-NLS-1$
		menuListener.customGradientMI.addListener(SWT.Selection, menuListener);
		GraphicsBackground gb = new GraphicsBackground();
		menuListener.customGradientMI.setData(gb);
	}

	/** Creates and returns the listener for menu items. */
	private MenuItemListener createMenuItemListener(final Control parent) {
		return new MenuItemListener(parent);
	}

	/**
	 * Creates and returns an instance of Image using on the path of an image.
	 *
	 * @param display
	 *            A Display
	 * @param name
	 *            The path of the image file
	 * @param resources
	 *            The list of resources of the menu
	 */
	private Image loadImage(Display display, String name, List<Resource> resources) {
		Image image = GraphicsExample.loadImage(display, GraphicsExample.class, name);
		if (image != null) resources.add(image);
		return image;
	}

	/**
	 * An inner class used as a listener for MenuItems added to the menu.
	 */
	static class MenuItemListener implements Listener {
		MenuItem customColorMI, customPatternMI, customGradientMI;	// custom menu items
		Control parent;
		Image customImage, customImageThumb;
		Color customColor;
		GraphicsBackground background;	// used to store information about the background
		ColorListener colorListener;
		List<Resource> resources;

		public MenuItemListener(Control parent){
			this.parent = parent;
			resources = new ArrayList<>();
		}
		/**
		 * Method used to set the ColorListener
		 *
		 * @param cl
		 *            A ColorListener
		 * @see org.eclipse.swt.examples.graphics.ColorListener.java
		 */
		public void setColorListener(ColorListener cl) {
			this.colorListener = cl;
		}

		public List<Resource> getMenuResources() {
			return resources;
		}

		@Override
		public void handleEvent(Event event) {
			switch (event.type) {

			case SWT.Dispose:
				for (Resource resource : resources) {
					resource.dispose();
				}
				resources = new ArrayList<>();
				break;
			case SWT.Selection:
				Display display = event.display;
				MenuItem item = (MenuItem) event.widget;
				if (customColorMI == item) {
					ColorDialog dialog = new ColorDialog(parent.getShell());
					if (customColor != null && !customColor.isDisposed()) {
						dialog.setRGB(customColor.getRGB());
					}
					RGB rgb = dialog.open();
					if (rgb == null) return;
					if (customColor != null) customColor.dispose();
					customColor = new Color(rgb);
					if (customPatternMI != null) customPatternMI.setImage(null);
					if (customGradientMI != null) customGradientMI.setImage(null);
					if (customImage != null) customImage.dispose();
					customImage = GraphicsExample.createImage(display, customColor);
					GraphicsBackground gb = new GraphicsBackground();
					gb.setBgImage(customImage);
					gb.setThumbNail(customImage);
					gb.setBgColor1(customColor);
					item.setData(gb);
					item.setImage(customImage);
					resources.add(customColor);
					resources.add(customImage);
				} else if (customPatternMI == item) {
					FileDialog dialog = new FileDialog(parent.getShell());
					dialog.setFilterExtensions(new String[] { "*.jpg", "*.gif",	"*.*" });
					String name = dialog.open();
					if (name == null) return;
					if (customColorMI != null) customColorMI.setImage(null);
					if (customGradientMI != null) customGradientMI.setImage(null);
					if (customColor != null) customColor.dispose();
					if (customImage != null) customImage.dispose();
					if (customImageThumb != null) customImageThumb.dispose();
					customImage = new Image(display, name);
					customImageThumb = GraphicsExample.createThumbnail(display, name);
					GraphicsBackground gb = new GraphicsBackground();
					gb.setBgImage(customImage);
					gb.setThumbNail(customImageThumb);
					item.setData(gb);
					item.setImage(customImageThumb);
					resources.add(customImageThumb);
				} else if (customGradientMI == item) {
					GradientDialog dialog = new GradientDialog(parent.getShell());
					if (background != null) {
						if (background.getBgColor1() != null)
							dialog.setFirstRGB(background.getBgColor1().getRGB());
						if (background.getBgColor2() != null)
							dialog.setSecondRGB(background.getBgColor2().getRGB());
					}
					if (dialog.open() != SWT.OK) return;
					Color colorA = new Color(dialog.getFirstRGB());
					Color colorB = new Color(dialog.getSecondRGB());
					if (colorA == null || colorB == null) return;
					if (customColorMI != null) customColorMI.setImage(null);
					if (customPatternMI != null) customPatternMI.setImage(null);
					if (customColor != null) customColor.dispose();
					if (customImage != null) customImage.dispose();
					customImage = GraphicsExample.createImage(display, colorA,
							colorB, 16, 16);
					GraphicsBackground gb = new GraphicsBackground();
					gb.setBgImage(customImage);
					gb.setThumbNail(customImage);
					gb.setBgColor1(colorA);
					gb.setBgColor2(colorB);
					item.setData(gb);
					item.setImage(customImage);
					resources.add(colorA);
					resources.add(colorB);
					resources.add(customImage);
				} else {
					if (customColorMI != null) customColorMI.setImage(null);
					if (customPatternMI != null) customPatternMI.setImage(null);
					if (customGradientMI != null) customGradientMI.setImage(null);
				}
				background = (GraphicsBackground) item.getData();
				colorListener.setColor(background);
				break;
			}
		}
	}
}
