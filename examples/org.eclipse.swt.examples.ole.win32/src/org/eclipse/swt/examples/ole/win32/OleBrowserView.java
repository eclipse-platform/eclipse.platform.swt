/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.ole.win32;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

/**
 * Ole uses <code>org.eclipse.swt</code> to demonstrate Win32 OLE / ActiveX
 * integration.
 * 
 * @see ViewPart
 */
public class OleBrowserView extends ViewPart {

	private Composite      displayArea;

	private OleFrame       webFrame;
	private OleWebBrowser  webBrowser;
	private Text           webUrl;
	private OleControlSite webControlSite;
	private ProgressBar    webProgress;
	private Label          webStatus;
	private Button         webNavigateButton;
	
	private ToolItem webCommandBackward;
	private ToolItem webCommandForward;
	private ToolItem webCommandHome;
	private ToolItem webCommandStop;
	private ToolItem webCommandRefresh;
	private ToolItem webCommandSearch;
	
	private boolean activated = false;

	private static final int DISPID_AMBIENT_DLCONTROL = -5512;
	private static final int DLCTL_NO_SCRIPTS = 0x80;
	
	/**
	 * Constructs the OLE browser view.
	 */
	public OleBrowserView() {
		OlePlugin.initResources();
	}

	/**
	 * Creates the example.
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite parent) {
		displayArea = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		displayArea.setLayout(gridLayout);
		
		createToolbar();
		createBrowserFrame();		
		createStatusArea();
		createBrowserControl();	
	}
	
	/**
	 * Cleanup
	 */
	public void dispose() {
		if (activated) {
			webControlSite.deactivateInPlaceClient();
			activated = false;
		}
		if (webBrowser != null) webBrowser.dispose();
		webBrowser = null;
		super.dispose();
	}
	
	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus()  {
		webUrl.setFocus();
	}

	/**
	 * Creates the Web browser toolbar.
	 */
	private void createToolbar() {
		// Add a toolbar
		ToolBar bar = new ToolBar(displayArea, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		bar.setLayoutData(gridData);
		
		// Add a button to navigate backwards through previously visited web sites
		webCommandBackward = new ToolItem(bar, SWT.NONE);
		webCommandBackward.setToolTipText(OlePlugin.getResourceString("browser.Back.tooltip"));
		webCommandBackward.setText(OlePlugin.getResourceString("browser.Back.text"));
		webCommandBackward.setImage(OlePlugin.images[OlePlugin.biBack]);
		webCommandBackward.setEnabled(false);
		webCommandBackward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.GoBack();
			}
		});
	
		// Add a button to navigate forward through previously visited web sites
		webCommandForward = new ToolItem(bar, SWT.NONE);
		webCommandForward.setToolTipText(OlePlugin.getResourceString("browser.Forward.tooltip"));
		webCommandForward.setText(OlePlugin.getResourceString("browser.Forward.text"));
		webCommandForward.setImage(OlePlugin.images[OlePlugin.biForward]);
		webCommandForward.setEnabled(false);
		webCommandForward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.GoForward();
			}
		});

		// Add a separator
		new ToolItem(bar, SWT.SEPARATOR);
		
		// Add a button to navigate to the Home page
		webCommandHome = new ToolItem(bar, SWT.NONE);
		webCommandHome.setToolTipText(OlePlugin.getResourceString("browser.Home.tooltip"));
		webCommandHome.setText(OlePlugin.getResourceString("browser.Home.text"));
		webCommandHome.setImage(OlePlugin.images[OlePlugin.biHome]);
		webCommandHome.setEnabled(false);
		webCommandHome.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.GoHome();
			}
		});

		// Add a button to abort web page loading
		webCommandStop = new ToolItem(bar, SWT.NONE);
		webCommandStop.setToolTipText(OlePlugin.getResourceString("browser.Stop.tooltip"));
		webCommandStop.setText(OlePlugin.getResourceString("browser.Stop.text"));
		webCommandStop.setImage(OlePlugin.images[OlePlugin.biStop]);
		webCommandStop.setEnabled(false);
		webCommandStop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.Stop();
			}
		});

		// Add a button to refresh the current web page
		webCommandRefresh = new ToolItem(bar, SWT.NONE);
		webCommandRefresh.setToolTipText(OlePlugin.getResourceString("browser.Refresh.tooltip"));
		webCommandRefresh.setText(OlePlugin.getResourceString("browser.Refresh.text"));
		webCommandRefresh.setImage(OlePlugin.images[OlePlugin.biRefresh]);
		webCommandRefresh.setEnabled(false);
		webCommandRefresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.Refresh();
			}
		});

		// Add a separator
		new ToolItem(bar, SWT.SEPARATOR);

		// Add a button to search the web
		webCommandSearch = new ToolItem(bar, SWT.NONE);
		webCommandSearch.setToolTipText(OlePlugin.getResourceString("browser.Search.tooltip"));
		webCommandSearch.setText(OlePlugin.getResourceString("browser.Search.text"));
		webCommandSearch.setImage(OlePlugin.images[OlePlugin.biSearch]);
		webCommandSearch.setEnabled(false);
		webCommandSearch.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (webBrowser == null) return;
				webBrowser.GoSearch();
			}
		});

		// Add a text area for Users to enter a url
		Composite addressBar = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		addressBar.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		addressBar.setLayout(gridLayout);

		Label addressLabel = new Label(addressBar, SWT.NONE);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		addressLabel.setLayoutData(gridData);
		addressLabel.setText(OlePlugin.getResourceString("browser.Address.label"));
		addressLabel.setFont(OlePlugin.browserFont);
		
		webUrl = new Text(addressBar, SWT.SINGLE | SWT.BORDER);
		webUrl.setFont(OlePlugin.browserFont);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		webUrl.setLayoutData(gridData);
		webUrl.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				webNavigateButton.getShell().setDefaultButton(webNavigateButton);
			}
		});
	
		// Add a button to navigate to the web site specified in the Text area defined above
		webNavigateButton = new Button(addressBar, SWT.PUSH);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		webNavigateButton.setLayoutData(gridData);
		webNavigateButton.setText(OlePlugin.getResourceString("browser.Go.text"));
		webNavigateButton.setFont(OlePlugin.browserFont);
		webNavigateButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (webBrowser == null) return;
				webBrowser.Navigate(webUrl.getText());
			}
		});
	}

	/**
	 * Creates the Web browser OleFrame.
	 */
	private void createBrowserFrame() {
		// Every control must have an associated OleFrame:
		webFrame = new OleFrame(displayArea, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		webFrame.setLayoutData(gridData);
	}
	
	/**
	 * Creates the Web browser status area.
	 */
	private void createStatusArea() {
		// Add a progress bar to display downloading progress information
		webProgress = new ProgressBar(displayArea, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.verticalAlignment = GridData.FILL;
		webProgress.setLayoutData(gridData);		

		// Add a label for displaying status messages as they are received from the control
		webStatus = new Label(displayArea, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		webStatus.setLayoutData(gridData);
		webStatus.setFont(OlePlugin.browserFont);
	}	

	/**
	 * Creates Web browser control.
	 */
	private void createBrowserControl() {
		try {
			// Create an Automation object for access to extended capabilities
			webControlSite = new OleControlSite(webFrame, SWT.NONE, "Shell.Explorer");
			Variant download = new Variant(DLCTL_NO_SCRIPTS);
			webControlSite.setSiteProperty(DISPID_AMBIENT_DLCONTROL, download);
			OleAutomation oleAutomation = new OleAutomation(webControlSite);
			webBrowser = new OleWebBrowser(oleAutomation);
		} catch (SWTException ex) {
			// Creation may have failed because control is not installed on machine
			Label label = new Label(webFrame, SWT.BORDER);
			OlePlugin.logError(OlePlugin.getResourceString("error.CouldNotCreateBrowserControl"), ex);
			label.setText(OlePlugin.getResourceString("error.CouldNotCreateBrowserControl"));
			return;
		}

		// Respond to ProgressChange events by updating the Progress bar
		webControlSite.addEventListener(OleWebBrowser.ProgressChange, new OleListener() {
			public void handleEvent(OleEvent event) {
				Variant progress = event.arguments[0];
				Variant maxProgress = event.arguments[1];
				if (progress == null || maxProgress == null)
					return;
				webProgress.setMaximum(maxProgress.getInt());
				webProgress.setSelection(progress.getInt());
			}
		});
		
		// Respond to StatusTextChange events by updating the Status Text label
		webControlSite.addEventListener(OleWebBrowser.StatusTextChange, new OleListener() {
			public void handleEvent(OleEvent event) {
				Variant statusText = event.arguments[0];
				if (statusText == null)	return;
				String text = statusText.getString();
				if (text != null)
					webStatus.setText(text);
			}
		});
		
		// Listen for changes to the ready state and print out the current state 
		webControlSite.addPropertyListener(OleWebBrowser.DISPID_READYSTATE, new OleListener() {
			public void handleEvent(OleEvent event) {
				if (event.detail == OLE.PROPERTY_CHANGING) return;
				int state = webBrowser.getReadyState();
				switch (state) {
					case OleWebBrowser.READYSTATE_UNINITIALIZED:
						webStatus.setText(
							OlePlugin.getResourceString("browser.State.Uninitialized.text"));
						webCommandBackward.setEnabled(false);
						webCommandForward.setEnabled(false);
						webCommandHome.setEnabled(false);
						webCommandRefresh.setEnabled(false);
						webCommandStop.setEnabled(false);
						webCommandSearch.setEnabled(false);
						break;
					case OleWebBrowser.READYSTATE_LOADING:
						webStatus.setText(
							OlePlugin.getResourceString("browser.State.Loading.text"));
						webCommandHome.setEnabled(true);
						webCommandRefresh.setEnabled(true);
						webCommandStop.setEnabled(true);
						webCommandSearch.setEnabled(true);
						break;
					case OleWebBrowser.READYSTATE_LOADED:
						webStatus.setText(
							OlePlugin.getResourceString("browser.State.Loaded.text"));
						webCommandStop.setEnabled(true);
						break;
					case OleWebBrowser.READYSTATE_INTERACTIVE:
						webStatus.setText(
							OlePlugin.getResourceString("browser.State.Interactive.text"));
						webCommandStop.setEnabled(true);
						break;
					case OleWebBrowser.READYSTATE_COMPLETE:
						webStatus.setText(
							OlePlugin.getResourceString("browser.State.Complete.text"));
						webCommandStop.setEnabled(false);
						break;
				}
			}
		});

		// Listen for changes to the active command states
		webControlSite.addEventListener(OleWebBrowser.CommandStateChange, new OleListener() {
			public void handleEvent(OleEvent event) {
				if (event.type != OleWebBrowser.CommandStateChange) return;
				final int commandID =
					(event.arguments[0] != null) ? event.arguments[0].getInt() : 0;
				final boolean commandEnabled =
					(event.arguments[1] != null) ? event.arguments[1].getBoolean() : false;
				
				switch (commandID) {
					case OleWebBrowser.CSC_NAVIGATEBACK:
					 	webCommandBackward.setEnabled(commandEnabled);
					 	break;
					case OleWebBrowser.CSC_NAVIGATEFORWARD:
					 	webCommandForward.setEnabled(commandEnabled);
						break;
				}
			}
		});

		// in place activate the ActiveX control		
		activated = (webControlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);
		if (activated) webBrowser.GoHome();
	}
}
