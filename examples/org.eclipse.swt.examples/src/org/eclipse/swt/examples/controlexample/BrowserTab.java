/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
package org.eclipse.swt.examples.controlexample;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Widget;

class BrowserTab extends Tab {

	/* Example widgets and groups that contain them */
	Browser browser;
	Group browserGroup;

	/* Style widgets added to the "Style" group */
	Button webKitButton;

	String errorMessage, lastText, lastUrl;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	BrowserTab(ControlExample instance) {
		super(instance);
	}

	@Override
	void createBackgroundModeGroup () {
		// Browser does not need a background mode group.
	}

	@Override
	void createColorAndFontGroup () {
		// Browser does not need a color and font group.
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
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
	@Override
	void createExampleWidgets () {

		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (webKitButton.getSelection ()) style |= SWT.WEBKIT;

		/* Create the example widgets */
		try {
			browser = new Browser (browserGroup, style);
		} catch (SWTError e) { // Probably missing browser
			try {
				browser = new Browser (browserGroup, style & ~(SWT.WEBKIT));
			} catch (SWTError e2) { // Unsupported platform
				errorMessage = e.getMessage();
				return;
			}
			MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			String resourceString = "WebKitNotFound";
			dialog.setMessage(ControlExample.getResourceString(resourceString, e.getMessage()));
			dialog.open();
		}

		if (lastUrl != null) {
			browser.setUrl(lastUrl);
		} else if (lastText != null) {
			browser.setText(lastText);
		} else {
			StringBuilder sb= new StringBuilder(300);

			try (InputStream htmlStream = ControlExample.class.getResourceAsStream("browser-content.html");
					BufferedReader br = new BufferedReader(new InputStreamReader(htmlStream))) {
				int read = 0;
				while ((read = br.read()) != -1)
					sb.append((char) read);
			} catch (IOException e) {
				log(e.getMessage());
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
	@Override
	void createOtherGroup () {
		super.createOtherGroup ();
		backgroundImageButton.dispose ();
	}

	/**
	 * Creates the "Size" group.  The "Size" group contains
	 * controls that allow the user to change the size of
	 * the example widgets.
	 */
	@Override
	void createSizeGroup () {
		super.createSizeGroup ();

		/* Set the default state for Browser to fill horizontally & vertically. */
		fillHButton.setSelection (true);
		fillVButton.setSelection (true);
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
	void createStyleGroup () {
		super.createStyleGroup ();

		/* Create the extra widgets */
		webKitButton = new Button (styleGroup, SWT.CHECK);
		webKitButton.setText ("SWT.WEBKIT");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}

	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	@Override
	Composite createTabFolderPage (final TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(ControlListener.controlResizedAdapter(e-> setExampleWidgetSize ()));

		/*
		 * Add a selection listener to the tabFolder to bring up a
		 * dialog if this platform does not support the Browser.
		 */
		tabFolder.addSelectionListener(widgetSelectedAdapter(e -> {
			if (errorMessage != null && tabFolder.getSelection()[0].getText().equals(getTabText())) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				dialog.setMessage(ControlExample.getResourceString("BrowserNotFound", errorMessage));
				dialog.open();
			}
		}));

		return tabFolderPage;
	}

	/**
	 * Disposes the "Example" widgets.
	 */
	@Override
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
		StringBuilder sb= new StringBuilder(300);
		try (BufferedReader br= new BufferedReader(new InputStreamReader(in))) {
			int read= 0;
			while ((read= br.read()) != -1)
				sb.append((char) read);
		}
		return sb.toString();
	}

	/**
	 * Gets the list of custom event names.
	 *
	 * @return an array containing custom event names
	 */
	@Override
	String [] getCustomEventNames () {
		return new String [] {"AuthenticationListener", "CloseWindowListener", "LocationListener",
				"OpenWindowListener", "ProgressListener", "StatusTextListener", "TitleListener",
				"VisibilityWindowListener"};
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		if (browser != null) return new Widget [] {browser};
		return super.getExampleWidgets();
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Text", "Url", "ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "Browser";
	}

	/**
	 * Hooks the custom listener specified by eventName.
	 */
	@Override
	void hookCustomListener (final String eventName) {
		if (browser == null) return;
		if (eventName == "AuthenticationListener") {
			browser.addAuthenticationListener(event -> log (eventName, event));
		}
		if (eventName == "CloseWindowListener") {
			browser.addCloseWindowListener (event -> log (eventName, event));
		}
		if (eventName == "LocationListener") {
			browser.addLocationListener (new LocationListener () {
				@Override
				public void changed(LocationEvent event) {
					log (eventName + ".changed", event);
				}
				@Override
				public void changing(LocationEvent event) {
					log (eventName + ".changing", event);
				}
			});
		}
		if (eventName == "OpenWindowListener") {
			browser.addOpenWindowListener (event -> log (eventName, event));
		}
		if (eventName == "ProgressListener") {
			browser.addProgressListener (new ProgressListener () {
				@Override
				public void changed(ProgressEvent event) {
					log (eventName + ".changed", event);
				}
				@Override
				public void completed(ProgressEvent event) {
					log (eventName + ".completed", event);
				}
			});
		}
		if (eventName == "StatusTextListener") {
			browser.addStatusTextListener (event -> log (eventName, event));
		}
		if (eventName == "TitleListener") {
			browser.addTitleListener (event -> log (eventName, event));
		}
		if (eventName == "VisibilityWindowListener") {
			browser.addVisibilityWindowListener (new VisibilityWindowListener () {
				@Override
				public void hide(WindowEvent event) {
					log (eventName + ".hide", event);
				}
				@Override
				public void show(WindowEvent event) {
					log (eventName + ".show", event);
				}
			});
		}
	}

	@Override
	boolean rtlSupport() {
		return false;
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		webKitButton.setSelection (browser == null ? false : (browser.getStyle () & SWT.WEBKIT) != 0);
		borderButton.setSelection (browser == null ? false : (browser.getStyle () & SWT.BORDER) != 0);
	}
}
