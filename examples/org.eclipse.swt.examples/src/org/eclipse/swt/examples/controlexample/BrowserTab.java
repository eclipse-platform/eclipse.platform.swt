/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class BrowserTab extends Tab {

	/* Example widgets and groups that contain them */
	Browser browser;
	Group browserGroup;
	
	/* Style widgets added to the "Style" group */
	Button mozillaButton, webKitButton;
	
	String errorMessage, lastText, lastUrl;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	BrowserTab(ControlExample instance) {
		super(instance);
	}
	
	void createBackgroundModeGroup () {
		// Browser does not need a background mode group.
	}
	
	void createColorAndFontGroup () {
		// Browser does not need a color and font group.
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the browser */
		browserGroup = new Group (exampleGroup, SWT.NONE);
		browserGroup.setLayout (new GridLayout ());
		browserGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		browserGroup.setText ("Browser");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (mozillaButton.getSelection ()) style |= SWT.MOZILLA;
		if (webKitButton.getSelection ()) style |= SWT.WEBKIT;
		
		/* Create the example widgets */
		try {
			browser = new Browser (browserGroup, style);
		} catch (SWTError e) { // Probably missing browser
			try {
				browser = new Browser (browserGroup, style & ~(SWT.MOZILLA | SWT.WEBKIT));
			} catch (SWTError e2) { // Unsupported platform
				errorMessage = e.getMessage();
				return;
			}
			MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			String resourceString = (style & SWT.MOZILLA) != 0 ? "MozillaNotFound" : "WebKitNotFound";
			dialog.setMessage(ControlExample.getResourceString(resourceString, new String [] {e.getMessage()}));
			dialog.open();
		}

		if (lastUrl != null) {
			browser.setUrl(lastUrl);
		} else if (lastText != null) {
			browser.setText(lastText);
		} else {
	        InputStream htmlStream= ControlExample.class.getResourceAsStream("browser-content.html");
			BufferedReader br= new BufferedReader(new InputStreamReader(htmlStream));
			StringBuffer sb= new StringBuffer(300);
			try {
				int read= 0;
				while ((read= br.read()) != -1)
					sb.append((char) read);
			} catch (IOException e) {
				log(e.getMessage());
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					log(e.getMessage());
				}
			}
			String text= sb.toString();
			browser.setText(text);
		}
		lastText = lastUrl = null;
	}
	
	/**
	 * Creates the "Other" group.  This is typically
	 * a child of the "Control" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
		backgroundImageButton.dispose ();
	}
	
	/**
	 * Creates the "Size" group.  The "Size" group contains
	 * controls that allow the user to change the size of
	 * the example widgets.
	 */
	void createSizeGroup () {
		super.createSizeGroup ();
		
		/* Set the default state for Browser to fill horizontally & vertically. */
		fillHButton.setSelection (true);
		fillVButton.setSelection (true);
	}

	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		mozillaButton = new Button (styleGroup, SWT.CHECK);
		mozillaButton.setText ("SWT.MOZILLA");
		mozillaButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				webKitButton.setSelection(false);
			}
		});
		webKitButton = new Button (styleGroup, SWT.CHECK);
		webKitButton.setText ("SWT.WEBKIT");
		webKitButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mozillaButton.setSelection(false);
			}
		});
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (final TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		/*
		 * Add a selection listener to the tabFolder to bring up a
		 * dialog if this platform does not support the Browser.
		 */
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (errorMessage != null && tabFolder.getSelection()[0].getText().equals(getTabText())) {
					MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					dialog.setMessage(ControlExample.getResourceString("BrowserNotFound", new String [] {errorMessage}));
					dialog.open();
				}
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		/* store the state of the Browser if applicable */
		if (browser != null) {
			String url = browser.getUrl();
			if (url.length() > 0 && !url.equals("about:blank")) { //$NON-NLS-1$
				lastUrl = url;
			} else {
				String text = browser.getText();
				if (text.length() > 0) {
					lastText = text;
				}
			}
		}
		super.disposeExampleWidgets();	
	}

	public static String getContents(InputStream in) throws IOException {
		BufferedReader br= new BufferedReader(new InputStreamReader(in));
		
		StringBuffer sb= new StringBuffer(300);
		try {
			int read= 0;
			while ((read= br.read()) != -1)
				sb.append((char) read);
		} finally {
			br.close();
		}
		return sb.toString();
	}

	/**
	 * Gets the list of custom event names.
	 * 
	 * @return an array containing custom event names
	 */
	String [] getCustomEventNames () {
		return new String [] {"CloseWindowListener", "LocationListener", "OpenWindowListener",
				"ProgressListener", "StatusTextListener", "TitleListener", "VisibilityWindowListener"};
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		if (browser != null) return new Widget [] {browser};
		return super.getExampleWidgets();
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Text", "Url", "ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Browser";
	}
	
	/**
	 * Hooks the custom listener specified by eventName.
	 */
	void hookCustomListener (final String eventName) {
		if (browser == null) return;
		if (eventName == "CloseWindowListener") {
			browser.addCloseWindowListener (new CloseWindowListener () {
				public void close(WindowEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "LocationListener") {
			browser.addLocationListener (new LocationListener () {
				public void changed(LocationEvent event) {
					log (eventName + ".changed", event);
				}
				public void changing(LocationEvent event) {
					log (eventName + ".changing", event);
				}
			});
		}
		if (eventName == "OpenWindowListener") {
			browser.addOpenWindowListener (new OpenWindowListener () {
				public void open(WindowEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "ProgressListener") {
			browser.addProgressListener (new ProgressListener () {
				public void changed(ProgressEvent event) {
					log (eventName + ".changed", event);
				}
				public void completed(ProgressEvent event) {
					log (eventName + ".completed", event);
				}
			});
		}
		if (eventName == "StatusTextListener") {
			browser.addStatusTextListener (new StatusTextListener () {
				public void changed(StatusTextEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "TitleListener") {
			browser.addTitleListener (new TitleListener () {
				public void changed(TitleEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "VisibilityWindowListener") {
			browser.addVisibilityWindowListener (new VisibilityWindowListener () {
				public void hide(WindowEvent event) {
					log (eventName + ".hide", event);
				}
				public void show(WindowEvent event) {
					log (eventName + ".show", event);
				}
			});
		}
	}

	boolean rtlSupport() {
		return false;
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		mozillaButton.setSelection (browser == null ? false : (browser.getStyle () & SWT.MOZILLA) != 0);
		webKitButton.setSelection (browser == null ? false : (browser.getStyle () & SWT.WEBKIT) != 0);
		borderButton.setSelection (browser == null ? false : (browser.getStyle () & SWT.BORDER) != 0);
	}
}
