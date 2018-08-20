/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.examples.hoverhelp;


import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
/**
 * This example demonstrates how to implement hover help feedback
 * using the MouseTrackListener.
 */
public class HoverHelp {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_hoverhelp");

	static final int
		hhiInformation = 0,
		hhiWarning = 1;
	static final String[] imageLocations = {
		"information.gif",
		"warning.gif"
	};
	Image images[];

	/**
	 * Runs main program.
	 */
	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new HoverHelp().open(display);
		// Event loop
		while (shell != null && ! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		// Cleanup
		display.dispose();

	}

	/**
	 * Opens the main program.
	 */
	public Shell open(Display display) {
		// Load the images
		Class<HoverHelp> clazz = HoverHelp.class;
		try {
			if (images == null) {
				images = new Image[imageLocations.length];

				for (int i = 0; i < imageLocations.length; ++i) {
					try (InputStream stream = clazz.getResourceAsStream(imageLocations[i])) {
						ImageData source = new ImageData(stream);
						ImageData mask = source.getTransparencyMask();
						images[i] = new Image(display, source, mask);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			System.err.println(getResourceString("error.CouldNotLoadResources", ex.getMessage()));
			return null;
		}

		// Create the window
		Shell shell = new Shell();
		createPartControl(shell);
		shell.addDisposeListener(e -> {
			/* Free resources */
			if (images != null) {
				for (final Image image : images) {
					if (image != null) image.dispose();
				}
				images = null;
			}
		});
		shell.pack();
		shell.open();
		return shell;
	}

	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Gets a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public String getResourceString(String key, Object... args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Creates the example
	 */
	public void createPartControl(Composite frame) {
		final ToolTipHandler tooltip = new ToolTipHandler(frame.getShell());

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		frame.setLayout(layout);

		String platform = SWT.getPlatform();
		String helpKey = "F1";
		if (platform.equals("gtk")) helpKey = "Ctrl+F1";
		if (platform.equals("cocoa")) helpKey = "Help";

		ToolBar bar = new ToolBar (frame, SWT.BORDER);
		for (int i=0; i<5; i++) {
			ToolItem item = new ToolItem (bar, SWT.PUSH);
			item.setText (getResourceString("ToolItem.text", new Object[] { Integer.valueOf(i) }));
			item.setData ("TIP_TEXT", getResourceString("ToolItem.tooltip",
				new Object[] { item.getText(), helpKey }));
			item.setData ("TIP_HELPTEXTHANDLER", (ToolTipHelpTextHandler) widget -> {
				Item item1 = (Item) widget;
				return getResourceString("ToolItem.help", new Object[] { item1.getText() });
			});
		}
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		bar.setLayoutData(gridData);
		tooltip.activateHoverHelp(bar);

		Table table = new Table (frame, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TableItem item = new TableItem (table, SWT.PUSH);
			item.setText (getResourceString("Item", new Object[] { Integer.valueOf(i) }));
			item.setData ("TIP_IMAGE", images[hhiInformation]);
			item.setText (getResourceString("TableItem.text", new Object[] { Integer.valueOf(i) }));
			item.setData ("TIP_TEXT", getResourceString("TableItem.tooltip",
				new Object[] { item.getText(), helpKey }));
			item.setData ("TIP_HELPTEXTHANDLER", (ToolTipHelpTextHandler) widget -> {
				Item item1 = (Item) widget;
				return getResourceString("TableItem.help", new Object[] { item1.getText() });
			});
		}
		table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		tooltip.activateHoverHelp(table);

		Tree tree = new Tree (frame, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TreeItem item = new TreeItem (tree, SWT.PUSH);
			item.setText (getResourceString("Item", new Object[] { Integer.valueOf(i) }));
			item.setData ("TIP_IMAGE", images[hhiWarning]);
			item.setText (getResourceString("TreeItem.text", new Object[] { Integer.valueOf(i) }));
			item.setData ("TIP_TEXT", getResourceString("TreeItem.tooltip",
				new Object[] { item.getText(), helpKey}));
			item.setData ("TIP_HELPTEXTHANDLER", (ToolTipHelpTextHandler) widget -> {
				Item item1 = (Item) widget;
				return getResourceString("TreeItem.help", new Object[] { item1.getText() });
			});
		}
		tree.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		tooltip.activateHoverHelp(tree);

		Button button = new Button (frame, SWT.PUSH);
		button.setText (getResourceString("Hello.text"));
		button.setData ("TIP_TEXT", getResourceString("Hello.tooltip"));
		tooltip.activateHoverHelp(button);
	}

	/**
	 * Emulated tooltip handler
	 * Notice that we could display anything in a tooltip besides text and images.
	 * For instance, it might make sense to embed large tables of data or buttons linking
	 * data under inspection to material elsewhere, or perform dynamic lookup for creating
	 * tooltip text on the fly.
	 */
	protected static class ToolTipHandler {
		private Shell  parentShell;
		private Shell  tipShell;
		private Label  tipLabelImage, tipLabelText;
		private Widget tipWidget; // widget this tooltip is hovering over
		private Point  tipPosition; // the position being hovered over

		/**
		 * Creates a new tooltip handler
		 *
		 * @param parent the parent Shell
		 */
		public ToolTipHandler(Shell parent) {
			final Display display = parent.getDisplay();
			this.parentShell = parent;

			tipShell = new Shell(parent, SWT.ON_TOP | SWT.TOOL);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.marginWidth = 2;
			gridLayout.marginHeight = 2;
			tipShell.setLayout(gridLayout);

			tipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

			tipLabelImage = new Label(tipShell, SWT.NONE);
			tipLabelImage.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			tipLabelImage.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			tipLabelImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));

			tipLabelText = new Label(tipShell, SWT.NONE);
			tipLabelText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			tipLabelText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			tipLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
		}

		/**
		 * Enables customized hover help for a specified control
		 *
		 * @control the control on which to enable hoverhelp
		 */
		public void activateHoverHelp(final Control control) {
			/*
			 * Get out of the way if we attempt to activate the control underneath the tooltip
			 */
			control.addMouseListener(MouseListener.mouseDownAdapter(e -> {
				if (tipShell.isVisible())
					tipShell.setVisible(false);
			}));

			/*
			 * Trap hover events to pop-up tooltip
			 */
			control.addMouseTrackListener(new MouseTrackAdapter () {
				@Override
				public void mouseExit(MouseEvent e) {
					if (tipShell.isVisible()) tipShell.setVisible(false);
					tipWidget = null;
				}
				@Override
				public void mouseHover (MouseEvent event) {
					Point pt = new Point (event.x, event.y);
					Widget widget = event.widget;
					if (widget instanceof ToolBar) {
						ToolBar w = (ToolBar) widget;
						widget = w.getItem (pt);
					}
					if (widget instanceof Table) {
						Table w = (Table) widget;
						widget = w.getItem (pt);
					}
					if (widget instanceof Tree) {
						Tree w = (Tree) widget;
						widget = w.getItem (pt);
					}
					if (widget == null) {
						tipShell.setVisible(false);
						tipWidget = null;
						return;
					}
					if (widget == tipWidget) return;
					tipWidget = widget;
					tipPosition = control.toDisplay(pt);
					String text = (String) widget.getData("TIP_TEXT");
					Image image = (Image) widget.getData("TIP_IMAGE");
					tipLabelText.setText(text != null ? text : "");
					tipLabelImage.setImage(image); // accepts null
					tipShell.pack();
					setHoverLocation(tipShell, tipPosition);
					tipShell.setVisible(true);
				}
			});

			/*
			 * Trap F1 Help to pop up a custom help box
			 */
			control.addHelpListener(event -> {
				if (tipWidget == null) return;
				ToolTipHelpTextHandler handler = (ToolTipHelpTextHandler)
					tipWidget.getData("TIP_HELPTEXTHANDLER");
				if (handler == null) return;
				String text = handler.getHelpText(tipWidget);
				if (text == null) return;

				if (tipShell.isVisible()) {
					tipShell.setVisible(false);
					Shell helpShell = new Shell(parentShell, SWT.SHELL_TRIM);
					helpShell.setLayout(new FillLayout());
					Label label = new Label(helpShell, SWT.NONE);
					label.setText(text);
					helpShell.pack();
					setHoverLocation(helpShell, tipPosition);
					helpShell.open();
				}
			});
		}

		/**
		 * Sets the location for a hovering shell
		 * @param shell the object that is to hover
		 * @param position the position of a widget to hover over
		 * @return the top-left location for a hovering box
		 */
		private void setHoverLocation(Shell shell, Point position) {
			Rectangle displayBounds = shell.getDisplay().getBounds();
			Rectangle shellBounds = shell.getBounds();
			shellBounds.x = Math.max(Math.min(position.x, displayBounds.width - shellBounds.width), 0);
			shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height - shellBounds.height), 0);
			shell.setBounds(shellBounds);
		}
	}

	/**
	 * ToolTip help handler
	 */
	protected interface ToolTipHelpTextHandler {
		/**
		 * Get help text
		 * @param widget the widget that is under help
		 * @return a help text string
		 */
		public String getHelpText(Widget widget);
	}
}
