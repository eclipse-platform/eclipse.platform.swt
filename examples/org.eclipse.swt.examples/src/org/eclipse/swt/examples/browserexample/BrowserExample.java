/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

import java.text.*;
import java.util.*;

public class BrowserExample {
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_browser");
	static Shell shell;
	int index;
	boolean busy;
	Image images[];
	Text location;
	Browser browser;
	static final String[] imageLocations = {
			"eclipse01.bmp", "eclipse02.bmp", "eclipse03.bmp", "eclipse04.bmp", "eclipse05.bmp",
			"eclipse06.bmp", "eclipse07.bmp", "eclipse08.bmp", "eclipse09.bmp", "eclipse10.bmp",
			"eclipse11.bmp", "eclipse12.bmp",};
	
/**
 * Creates an instance of a ControlExample embedded inside
 * the supplied parent Composite.
 * 
 * @param parent the container of the example
 */
public BrowserExample(Composite parent) {
	initResources();
	final Display display = parent.getDisplay();
	GridLayout gridLayout = new GridLayout();
	gridLayout.numColumns = 3;
	parent.setLayout(gridLayout);
	ToolBar toolbar = new ToolBar(parent, SWT.NONE);
	final ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
	itemBack.setText(getResourceString("Back"));
	final ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
	itemForward.setText(getResourceString("Forward"));
	final ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
	itemStop.setText(getResourceString("Stop"));
	final ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
	itemRefresh.setText(getResourceString("Refresh"));
	final ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
	itemGo.setText(getResourceString("Go"));
		
	GridData data = new GridData();
	data.horizontalSpan = 2;
	toolbar.setLayoutData(data);

	final Canvas canvas = new Canvas(parent, SWT.NO_BACKGROUND);
	data = new GridData();
	data.horizontalAlignment = GridData.END;
	Rectangle rect = images[0].getBounds();
	data.widthHint = rect.width;
	data.heightHint = rect.height;
	data.verticalSpan = 2;
	canvas.setLayoutData(data);
	canvas.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
			e.gc.drawImage(images[index], 0, 0);
		}
	});
	canvas.addListener(SWT.MouseDown, new Listener() {
		public void handleEvent(Event e) {
			browser.setUrl(getResourceString("Startup"));
		}
	});
	
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
	
	Label labelAddress = new Label(parent, SWT.NONE);
	labelAddress.setText(getResourceString("Address"));
	
	location = new Text(parent, SWT.BORDER);
	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	data.horizontalSpan = 1;
	data.grabExcessHorizontalSpace = true;
	location.setLayoutData(data);

	data = new GridData();
	data.horizontalAlignment = GridData.FILL;
	data.verticalAlignment = GridData.FILL;
	data.horizontalSpan = 3;
	data.grabExcessHorizontalSpace = true;
	data.grabExcessVerticalSpace = true;
	try {
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(data);
	} catch (SWTError e) {
		/* Browser widget could not be instantiated */
		Label label = new Label(parent, SWT.CENTER);
		label.setText(getResourceString("BrowserNotCreated"));
		label.setLayoutData(data);
	}

	final Label status = new Label(parent, SWT.NONE);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.horizontalSpan = 2;
	status.setLayoutData(data);
	
	final ProgressBar progressBar = new ProgressBar(parent, SWT.NONE);
	data = new GridData();
	data.horizontalAlignment = GridData.END;
	progressBar.setLayoutData(data);

	if (browser != null) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem)event.widget;
				if (item == itemBack) browser.back(); 
				else if (item == itemForward) browser.forward();
				else if (item == itemStop) browser.stop();
				else if (item == itemRefresh) browser.refresh();
				else if (item == itemGo) browser.setUrl(location.getText());
			}
		};
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0) return;                            
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
				busy = event.current != event.total;
				if (!busy) {
					index = 0;
					canvas.redraw();
				}
			}
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
				busy = false;
				index = 0;
				canvas.redraw();
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				status.setText(event.text);	
			}
		});
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				busy = true;
				location.setText(event.location);
			}
			public void changing(LocationEvent event) {
			}
		});
		itemBack.addListener(SWT.Selection, listener);
		itemForward.addListener(SWT.Selection, listener);
		itemStop.addListener(SWT.Selection, listener);
		itemRefresh.addListener(SWT.Selection, listener);
		itemGo.addListener(SWT.Selection, listener);
		location.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				browser.setUrl(location.getText());
			}
		});
		
		browser.setUrl(getResourceString("Startup"));
	}
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

/**
 * Gets a string from the resource bundle and binds it
 * with the given arguments. If the key is not found,
 * return the key.
 */
static String getResourceString(String key, Object[] args) {
	try {
		return MessageFormat.format(getResourceString(key), args);
	} catch (MissingResourceException e) {
		return key;
	} catch (NullPointerException e) {
		return "!" + key + "!";
	}
}

/**
 * Disposes of all resources associated with a particular
 * instance of the BrowserExample.
 */	
public void dispose() {
	freeResources();
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
 * Grabs input focus.
 */
public void setFocus() {
	location.setFocus();
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
					ImageData source = new ImageData(clazz.getResourceAsStream(imageLocations[i]));
					ImageData mask = source.getTransparencyMask();
					images[i] = new Image(null, source, mask);
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
	final Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	BrowserExample instance = new BrowserExample(shell);
	shell.setText(getResourceString("window.title"));
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	instance.dispose();
	display.dispose();
}
}