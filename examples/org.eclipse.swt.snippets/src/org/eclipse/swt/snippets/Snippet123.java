/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
 
/*
 * OLE and ActiveX example snippet: get events from IE control (win32 only)
 * 
 * This snippet only runs as-is on 32-bit architectures because it uses
 * java integers to represent native pointers.  "long" comments are included
 * throughout the snippet to show where int should be changed to long in
 * order to run on a 64-bit architecture.
 * 
 * NOTE: This snippet uses internal SWT packages that are
 * subject to change without notice.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.OS;

public class Snippet123 {

public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	OleControlSite controlSite;
	try {
		OleFrame frame = new OleFrame(shell, SWT.NONE);
		controlSite = new OleControlSite(frame, SWT.NONE, "Shell.Explorer");
		controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	} catch (SWTError e) {
		System.out.println("Unable to open activeX control");
		display.dispose();
		return;
	}
	shell.open();
	
	// IWebBrowser
	final OleAutomation webBrowser = new OleAutomation(controlSite);

	// When a new document is loaded, access the document object for the new page.
	int DownloadComplete = 104;
	controlSite.addEventListener(DownloadComplete, new OleListener() {
		public void handleEvent(OleEvent event) {
			int[] htmlDocumentID = webBrowser.getIDsOfNames(new String[]{"Document"}); 
			if (htmlDocumentID == null) return;
			Variant pVarResult = webBrowser.getProperty(htmlDocumentID[0]);
			if (pVarResult == null || pVarResult.getType() == 0) return;
			//IHTMLDocument2
			OleAutomation htmlDocument = pVarResult.getAutomation();

			// Request to be notified of click, double click and key down events
			EventDispatch myDispatch = new EventDispatch(EventDispatch.onclick);
			IDispatch idispatch = new IDispatch(myDispatch.getAddress());
			Variant dispatch = new Variant(idispatch);
			htmlDocument.setProperty(EventDispatch.onclick, dispatch);

			myDispatch = new EventDispatch(EventDispatch.ondblclick);
			idispatch = new IDispatch(myDispatch.getAddress());
			dispatch = new Variant(idispatch);
			htmlDocument.setProperty(EventDispatch.ondblclick, dispatch);

			myDispatch = new EventDispatch(EventDispatch.onkeydown);
			idispatch = new IDispatch(myDispatch.getAddress());
			dispatch = new Variant(idispatch);
			htmlDocument.setProperty(EventDispatch.onkeydown, dispatch);
			
			//Remember to release OleAutomation Object
			htmlDocument.dispose();
		}
	});
	
	// Navigate to a web site
	int[] ids = webBrowser.getIDsOfNames(new String[]{"Navigate", "URL"}); 
	Variant[] rgvarg = new Variant[] {new Variant("http://www.google.com")};
	int[] rgdispidNamedArgs = new int[]{ids[1]};
	webBrowser.invoke(ids[0], rgvarg, rgdispidNamedArgs);
		
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	//Remember to release OleAutomation Object
	webBrowser.dispose();
	display.dispose();
	
}
}
// EventDispatch implements a simple IDispatch interface which will be called 
// when the event is fired.
class EventDispatch {
	private COMObject iDispatch;
	private int refCount = 0;
	private int eventID;
	
	final static int onhelp = 0x8001177d;
	final static int onclick = 0x80011778;
	final static int ondblclick = 0x80011779;
	final static int onkeyup = 0x80011776;
	final static int onkeydown = 0x80011775;
	final static int onkeypress = 0x80011777;
	final static int onmouseup = 0x80011773;
	final static int onmousedown = 0x80011772;
	final static int onmousemove = 0x80011774;
	final static int onmouseout = 0x80011771;
	final static int onmouseover = 0x80011770;
	final static int onreadystatechange = 0x80011789;
	final static int onafterupdate = 0x80011786;
	final static int onrowexit= 0x80011782;
	final static int onrowenter = 0x80011783;
	final static int ondragstart = 0x80011793;
	final static int onselectstart = 0x80011795;

	EventDispatch(int eventID) {
		this.eventID = eventID;
		createCOMInterfaces();
	}
	int /*long*/ getAddress() {
		return iDispatch.getAddress();
	}
	private void createCOMInterfaces() {
		iDispatch = new COMObject(new int[]{2, 0, 0, 1, 3, 4, 8}){
			public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
			public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
			public int /*long*/ method2(int /*long*/[] args) {return Release();}
			// method3 GetTypeInfoCount - not implemented
			// method4 GetTypeInfo - not implemented
			// method5 GetIDsOfNames - not implemented
			public int /*long*/ method6(int /*long*/[] args) {return Invoke((int)/*64*/args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], args[4], args[5], args[6], args[7]);}
		};
	}
	private void disposeCOMInterfaces() {
		if (iDispatch != null)
			iDispatch.dispose();
		iDispatch = null;
		
	}
	private int AddRef() {
		refCount++;
		return refCount;
	}
	private int Invoke(int dispIdMember, int /*long*/ riid, int lcid, int dwFlags, int /*long*/ pDispParams, int /*long*/ pVarResult, int /*long*/ pExcepInfo, int /*long*/ pArgErr)	{
		switch (eventID) {
			case onhelp: System.out.println("onhelp"); break;
			case onclick: System.out.println("onclick"); break;
			case ondblclick: System.out.println("ondblclick"); break;
			case onkeyup: System.out.println("onkeyup"); break;
			case onkeydown: System.out.println("onkeydown"); break;
			case onkeypress: System.out.println("onkeypress"); break;
			case onmouseup: System.out.println("onmouseup"); break;
			case onmousedown: System.out.println("onmousedown"); break;
			case onmousemove: System.out.println("onmousemove"); break;
			case onmouseout: System.out.println("onmouseout"); break;
			case onmouseover: System.out.println("onmouseover"); break;
			case onreadystatechange: System.out.println("onreadystatechange"); break;
			case onafterupdate: System.out.println("onafterupdate"); break;
			case onrowexit: System.out.println("onrowexit"); break;
			case onrowenter: System.out.println("onrowenter"); break;
			case ondragstart: System.out.println("ondragstart"); break;
			case onselectstart: System.out.println("onselectstart"); break;
		}
		return COM.S_OK;
	}
	private int QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {
		if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
		GUID guid = new GUID();
		COM.MoveMemory(guid, riid, GUID.sizeof);
	
		if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDispatch)) {
			COM.MoveMemory(ppvObject, new int /*long*/[] {iDispatch.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}
		COM.MoveMemory(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
		return COM.E_NOINTERFACE;
	}
	int Release() {
		refCount--;
		if (refCount == 0) {
			disposeCOMInterfaces();
		}
		return refCount;
	}
}

