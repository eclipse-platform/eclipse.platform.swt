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


import org.eclipse.swt.ole.win32.*;

/**
 * Wrapper for an OleAutomation object used to send commands
 * to a Win32 "Shell.Explorer" OLE control.
 * 
 * Instances of this class manage the setup, typical use and teardown of
 * a simple web browser.
 */
class OleWebBrowser {
	/* See the Windows Platform SDK documentation for more information about the
	 * OLE control used here and its usage.
	 */
	// Generated from typelib filename: shdocvw.dll

	// Constants for WebBrowser CommandStateChange
	public static final int CSC_UPDATECOMMANDS = -1;
	public static final int CSC_NAVIGATEFORWARD = 1;
	public static final int CSC_NAVIGATEBACK = 2;

	// COnstants for Web Browser ReadyState
	public static final int READYSTATE_UNINITIALIZED = 0;
	public static final int READYSTATE_LOADING = 1;
	public static final int READYSTATE_LOADED = 2;
	public static final int READYSTATE_INTERACTIVE = 3;
	public static final int READYSTATE_COMPLETE = 4;
	
	// Web Browser Control Events 
	public static final int BeforeNavigate        = 100; // Fired when a new hyperlink is being navigated to.
	public static final int NavigateComplete      = 101; // Fired when the document being navigated to becomes visible and enters the navigation stack.
	public static final int StatusTextChange      = 102; // Statusbar text changed.
	public static final int ProgressChange        = 108; // Fired when download progress is updated.
	public static final int DownloadComplete      = 104; // Download of page complete.
	public static final int CommandStateChange    = 105; // The enabled state of a command changed
	public static final int DownloadBegin         = 106; // Download of a page started.
	public static final int NewWindow             = 107; // Fired when a new window should be created.
	public static final int TitleChange           = 113; // Document title changed.
	public static final int FrameBeforeNavigate   = 200; // Fired when a new hyperlink is being navigated to in a frame.
	public static final int FrameNavigateComplete = 201; // Fired when a new hyperlink is being navigated to in a frame.
	public static final int FrameNewWindow        = 204; // Fired when a new window should be created.
	public static final int Quit                  = 103; // Fired when application is quiting.
	public static final int WindowMove            = 109; // Fired when window has been moved.
	public static final int WindowResize          = 110; // Fired when window has been sized.
	public static final int WindowActivate        = 111; // Fired when window has been activated.
	public static final int PropertyChange        = 112; // Fired when the PutProperty method has been called.

	// Web Browser properties
	public static final int DISPID_READYSTATE = -525;

	private OleAutomation  oleAutomation;

	/**
	 * Creates a Web browser control.
	 * <p>
	 * Typical use:<br>
	 * <code>
	 * OleControlSite oleControlSite = new OleControlSite(oleFrame, style, "Shell.Explorer");<br>
	 * OleAutomation oleAutomation = new OleAutomation(oleControlSite);<br>
	 * OleWebBrowser webBrowser = new OleWebBrowser(oleControlSite, oleAutomation);<br>
	 * </code>
	 * 
     * @param oleAutomation the OleAutomation object for this control.
     * @param oleControlSite the OleControlSite object for this control.
	 */
	public OleWebBrowser(OleAutomation oleAutomation) {
		this.oleAutomation = oleAutomation;
	}
	
	
	/**
	 * Disposes of the Web browser control.
	 */
	public void dispose() {
		if (oleAutomation != null) oleAutomation.dispose();
		oleAutomation = null;
	}
	
	/*
	 * Interact with the Control via OLE Automation
	 * 
	 * Note: You can hard code the DISPIDs if you know them beforehand
	 *       this is of course the fastest way, but you increase coupling
	 *       to the control.
	 */
	 
	/**
	 * Returns the current web page title.
	 * 
	 * @return the current web page title String
	 */
	public String getLocationName() {
		// dispid=210, type=PROPGET, name="LocationName"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"LocationName"}); 
		int dispIdMember = rgdispid[0];
		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR) return null;
		return pVarResult.getString();
	}
	
	/**
	 * Returns the current URL.
	 * 
	 * @return the current URL String
	 */
	public String getLocationURL() {
		// dispid=211, type=PROPGET, name="LocationURL"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"LocationURL"}); 
		int dispIdMember = rgdispid[0];
		
		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR) return null;
		return pVarResult.getString();
	}
	
	/**
	 * Returns the current state of the control.
	 * 
	 * @return the current state of the control, one of:
	 *         READYSTATE_UNINITIALIZED;
	 *         READYSTATE_LOADING;
	 *         READYSTATE_LOADED;
	 *         READYSTATE_INTERACTIVE;
	 *         READYSTATE_COMPLETE.
	 */
	public int getReadyState() {
		// dispid=4294966771, type=PROPGET, name="ReadyState"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"ReadyState"}); 
		int dispIdMember = rgdispid[0];
		
		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_I4) return -1;
		return pVarResult.getInt();
	}
	
	/**
	 * Navigates backwards through previously visited web sites.
	 */
	public void GoBack() {
	
		// dispid=100, type=METHOD, name="GoBack"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoBack"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}
	
	/**
	 * Navigates backwards through previously visited web sites.
	 */
	public void GoForward() {
	
		// dispid=101, type=METHOD, name="GoForward"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoForward"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}
	
	/**
	 * Navigates to home page.
	 */
	public void GoHome() {
		// dispid=102, type=METHOD, name="GoHome"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoHome"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}
	
	/**
	 * Navigates to user-specified Web search gateway.
	 */
	public void GoSearch() {
		// dispid=103, type=METHOD, name="GoSearch"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoSearch"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}
	
	/**
	 * Navigates to a particular URL.
	 */
	public void Navigate(String url) {
		// dispid=104, type=METHOD, name="Navigate"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Navigate", "URL"}); 
		int dispIdMember = rgdispid[0];
		
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(url);
		int[] rgdispidNamedArgs = new int[1];
		rgdispidNamedArgs[0] = rgdispid[1]; // identifier of argument
		oleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
	}
	
	/**
	 * Refreshes the currently viewed page.
	 *
	 * @return the platform-defined result code for the "Refresh" method invocation
	 */
	public void Refresh(){
		// dispid= 4294966746, type=METHOD, name="Refresh"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Refresh"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invokeNoReply(dispIdMember);
	}
	
	/**
	 * Aborts loading of the currnet page.
	 *
	 * @return the platform-defined result code for the "Stop" method invocation
	 */
	public void Stop() {
		// dispid=106, type=METHOD, name="Stop"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Stop"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}	
}
