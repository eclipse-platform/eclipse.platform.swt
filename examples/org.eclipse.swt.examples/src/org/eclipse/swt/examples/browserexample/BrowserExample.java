/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browserexample;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.*;

public class BrowserExample {
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_browser");
	int index;
	boolean busy;
	Image images[];
	Image icon = null;
	boolean title = false;
	Composite parent;
	Text locationBar;
	Browser browser;
	ToolBar toolbar;
	Canvas canvas;
	ToolItem itemBack, itemForward;
	Label status;
	ProgressBar progressBar;
	SWTError error = null;

	static final String[] imageLocations = {
			"eclipse01.bmp", "eclipse02.bmp", "eclipse03.bmp", "eclipse04.bmp", "eclipse05.bmp",
			"eclipse06.bmp", "eclipse07.bmp", "eclipse08.bmp", "eclipse09.bmp", "eclipse10.bmp",
			"eclipse11.bmp", "eclipse12.bmp",};
	static final String iconLocation = "document.gif";
		
	public BrowserExample(Composite parent, boolean top) {
		this.parent = parent;
		try {
			browser = new Browser(parent, SWT.BORDER);
		} catch (SWTError e) {
			error = e;
			/* Browser widget could not be instantiated */
			parent.setLayout(new FillLayout());
			Label label = new Label(parent, SWT.CENTER | SWT.WRAP);
			label.setText(getResourceString("BrowserNotCreated"));
			parent.layout(true);
			return;
		}
		initResources();
		final Display display = parent.getDisplay();
		browser.setData("org.eclipse.swt.examples.browserexample.BrowserApplication", this);
		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				Shell shell = new Shell(display);
				if (icon != null) shell.setImage(icon);
				shell.setLayout(new FillLayout());
				BrowserExample app = new BrowserExample(shell, false);
				app.setShellDecoration(icon, true);
				event.browser = app.getBrowser();
			}
		});
		if (top) {
			browser.setUrl(getResourceString("Startup"));
			show(false, null, null, true, true, true, true);
		} else {
			browser.addVisibilityWindowListener(new VisibilityWindowListener() {
				public void hide(WindowEvent e) {
				}
				public void show(WindowEvent e) {
					Browser browser = (Browser)e.widget;
					BrowserExample app = (BrowserExample)browser.getData("org.eclipse.swt.examples.browserexample.BrowserApplication");
					app.show(true, e.location, e.size, e.addressBar, e.menuBar, e.statusBar, e.toolBar);
				}
			});
			browser.addCloseWindowListener(new CloseWindowListener() {
				public void close(WindowEvent event) {
					Browser browser = (Browser)event.widget;
					Shell shell = browser.getShell();
					shell.close();
				}
			});
		}
	}

	/**
	 * Disposes of all resources associated with a particular
	 * instance of the BrowserApplication.
	 */	
	public void dispose() {
		freeResources();
	}
	
	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}
	
	public SWTError getError() { return error; }
	
	public Browser getBrowser() { return browser; }
	
	public void setShellDecoration(Image icon, boolean title) {
		this.icon = icon;
		this.title = title;
	}
	
	void show(boolean owned, Point location, Point size, boolean addressBar, boolean menuBar, boolean statusBar, boolean toolBar) {
		final Shell shell = browser.getShell();
		if (owned) {
			if (location != null) shell.setLocation(location);
			if (size != null) shell.setSize(shell.computeSize(size.x, size.y));
		}
		FormData data = null;
		if (toolBar) {
			toolbar = new ToolBar(parent, SWT.NONE);
			data = new FormData();
			data.top = new FormAttachment(0, 5);
			toolbar.setLayoutData(data);
			itemBack = new ToolItem(toolbar, SWT.PUSH);
			itemBack.setText(getResourceString("Back"));
			itemForward = new ToolItem(toolbar, SWT.PUSH);
			itemForward.setText(getResourceString("Forward"));
			final ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
			itemStop.setText(getResourceString("Stop"));
			final ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
			itemRefresh.setText(getResourceString("Refresh"));
			final ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
			itemGo.setText(getResourceString("Go"));
			
			itemBack.setEnabled(browser.isBackEnabled());
			itemForward.setEnabled(browser.isForwardEnabled());
			Listener listener = new Listener() {
				public void handleEvent(Event event) {
					ToolItem item = (ToolItem)event.widget;
					if (item == itemBack) browser.back(); 
					else if (item == itemForward) browser.forward();
					else if (item == itemStop) browser.stop();
					else if (item == itemRefresh) browser.refresh();
					else if (item == itemGo) browser.setUrl(locationBar.getText());
				}
			};
			itemBack.addListener(SWT.Selection, listener);
			itemForward.addListener(SWT.Selection, listener);
			itemStop.addListener(SWT.Selection, listener);
			itemRefresh.addListener(SWT.Selection, listener);
			itemGo.addListener(SWT.Selection, listener);

			canvas = new Canvas(parent, SWT.NO_BACKGROUND);
			data = new FormData();
			data.width = 24;
			data.height = 24;
			data.top = new FormAttachment(0, 5);
			data.right = new FormAttachment(100, -5);
			canvas.setLayoutData(data);
			
			final Rectangle rect = images[0].getBounds();
			canvas.addListener(SWT.Paint, new Listener() {
				public void handleEvent(Event e) {
					Point pt = ((Canvas)e.widget).getSize();
					e.gc.drawImage(images[index], 0, 0, rect.width, rect.height, 0, 0, pt.x, pt.y);			
				}
			});
			canvas.addListener(SWT.MouseDown, new Listener() {
				public void handleEvent(Event e) {
					browser.setUrl(getResourceString("Startup"));
				}
			});
			
			final Display display = parent.getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					if (canvas.isDisposed()) return;
					if (busy) {
						index++;
						if (index == images.length) index = 0;
						canvas.redraw();
					}
					display.timerExec(150, this);
				}
			});
		}
		if (addressBar) {
			locationBar = new Text(parent, SWT.BORDER);
			data = new FormData();
			if (toolbar != null) {
				data.top = new FormAttachment(toolbar, 0, SWT.TOP);
				data.left = new FormAttachment(toolbar, 5, SWT.RIGHT);
				data.right = new FormAttachment(canvas, -5, SWT.DEFAULT);			
			} else {
				data.top = new FormAttachment(0, 0);
				data.left = new FormAttachment(0, 0);
				data.right = new FormAttachment(100, 0);			
			}
			locationBar.setLayoutData(data);
			locationBar.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event e) {
					browser.setUrl(locationBar.getText());
				}
			});
		}
		if (statusBar) {
			status = new Label(parent, SWT.NONE);
			progressBar = new ProgressBar(parent, SWT.NONE);
			
			data = new FormData();
			data.left = new FormAttachment(0, 5);
			data.right = new FormAttachment(progressBar, 0, SWT.DEFAULT);
			data.bottom = new FormAttachment(100, -5);
			status.setLayoutData(data);
			
			data = new FormData();
			data.right = new FormAttachment(100, -5);
			data.bottom = new FormAttachment(100, -5);
			progressBar.setLayoutData(data);
			
			browser.addStatusTextListener(new StatusTextListener() {
				public void changed(StatusTextEvent event) {
					status.setText(event.text);	
				}
			});
		}
		parent.setLayout(new FormLayout());

		Control aboveBrowser = toolBar ? (Control)canvas : (addressBar ? (Control)locationBar : null);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = aboveBrowser != null ? new FormAttachment(aboveBrowser, 5, SWT.DEFAULT) : new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = status != null ? new FormAttachment(status, -5, SWT.DEFAULT) : new FormAttachment(100, 0);
		browser.setLayoutData(data);
			
		if (statusBar || toolBar) {
			browser.addProgressListener(new ProgressListener() {
				public void changed(ProgressEvent event) {
					if (event.total == 0) return;                            
					int ratio = event.current * 100 / event.total;
					if (progressBar != null) progressBar.setSelection(ratio);
					busy = event.current != event.total;
					if (!busy) {
						index = 0;
						if (canvas != null) canvas.redraw();
					}
				}
				public void completed(ProgressEvent event) {
					if (progressBar != null) progressBar.setSelection(0);
					busy = false;
					index = 0;
					if (canvas != null) {
						itemBack.setEnabled(browser.isBackEnabled());
						itemForward.setEnabled(browser.isForwardEnabled());
						canvas.redraw();
					}
				}
			});
		}
		if (addressBar || statusBar || toolBar) {
			browser.addLocationListener(new LocationListener() {
				public void changed(LocationEvent event) {
					busy = true;
					if (event.top && locationBar != null) locationBar.setText(event.location);
				}
				public void changing(LocationEvent event) {
				}
			});
		}
		if (title) {
			browser.addTitleListener(new TitleListener() {
				public void changed(TitleEvent event) {
					shell.setText(event.title+" - "+getResourceString("window.title"));
				}
			});
		}
		parent.layout(true);
		if (owned) shell.open();
	}

	/**
	 * Grabs input focus
	 */
	public void focus() {
		if (locationBar != null) locationBar.setFocus();
		else if (browser != null) browser.setFocus();
		else parent.setFocus();
	}
	
	/**
	 * Frees the resources
	 */
	void freeResources() {
		if (images != null) {
			for (int i = 0; i < images.length; ++i) {
				final Image image = images[i];
				if (image != null) image.dispose();
			}
			images = null;
		}
	}
	
	/**
	 * Loads the resources
	 */
	void initResources() {
		final Class clazz = this.getClass();
		if (resourceBundle != null) {
			try {
				if (images == null) {
					images = new Image[imageLocations.length];
					for (int i = 0; i < imageLocations.length; ++i) {
						InputStream sourceStream = clazz.getResourceAsStream(imageLocations[i]);
						ImageData source = new ImageData(sourceStream);
						ImageData mask = source.getTransparencyMask();
						images[i] = new Image(null, source, mask);
						try {
							sourceStream.close();
						} catch (IOException e) {
							e.printStackTrace ();
						}
					}
				}
				return;
			} catch (Throwable t) {
			}
		}
		String error = (resourceBundle != null) ? getResourceString("error.CouldNotLoadResources") : "Unable to load resources";
		freeResources();
		throw new RuntimeException(error);
	}
	
	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText(getResourceString("window.title"));
		InputStream stream = BrowserExample.class.getResourceAsStream(iconLocation);
		Image icon = new Image(display, stream);
		shell.setImage(icon);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BrowserExample app = new BrowserExample(shell, true);
		app.setShellDecoration(icon, true);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		icon.dispose();
		app.dispose();
		display.dispose();
	}
}
