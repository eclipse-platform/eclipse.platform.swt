/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Reading and writing to a SAFEARRAY
 * 
 * This example reads from a PostData object in a BeforeNavigate2 event and
 * creates a PostData object in a call to Navigate (32-bit win32 only).
 * NOTE: This snippet uses internal SWT packages that are
 * subject to change without notice.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

public class Snippet186 {

static int CodePage = OS.GetACP();

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new GridLayout(2, false));
	
	final Text text = new Text(shell, SWT.BORDER);
	text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	Button go = new Button(shell, SWT.PUSH);
	go.setText("Go");
	OleFrame oleFrame = new OleFrame(shell, SWT.NONE);
	oleFrame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	OleControlSite controlSite;
	OleAutomation automation;
	try {
		controlSite = new OleControlSite(oleFrame, SWT.NONE, "Shell.Explorer");
		automation = new OleAutomation(controlSite);
		controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	} catch (SWTException ex) {
		System.out.println("Unable to open activeX control");
		display.dispose();
		return;
	}
	
	final OleAutomation auto = automation;
	go.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			String url = text.getText();
			int[] rgdispid = auto.getIDsOfNames(new String[]{"Navigate", "URL"}); 
			int dispIdMember = rgdispid[0];
			Variant[] rgvarg = new Variant[1];
			rgvarg[0] = new Variant(url);
			int[] rgdispidNamedArgs = new int[1];
			rgdispidNamedArgs[0] = rgdispid[1];
			auto.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		}
	});
	
	
	// Read PostData whenever we navigate to a site that uses it
	int BeforeNavigate2 = 0xfa;
	controlSite.addEventListener(BeforeNavigate2, new OleListener() {
		public void handleEvent(OleEvent event) {
			Variant url = event.arguments[1];
			Variant postData = event.arguments[4];
			if (postData != null) {
				System.out.println("PostData = "+readSafeArray(postData)+", URL = "+url.getString());
			}
		}
	});
	
	// Navigate to this web site which uses post data to fill in the text field
	// and put the string "hello world" into the text box
	text.setText("file://"+Snippet186.class.getResource("Snippet186.html").getFile());
	int[] rgdispid = automation.getIDsOfNames(new String[]{"Navigate", "URL", "PostData"}); 
	int dispIdMember = rgdispid[0];	
	Variant[] rgvarg = new Variant[2];
	rgvarg[0] = new Variant(text.getText());
	rgvarg[1] = writeSafeArray("hello world");
	int[] rgdispidNamedArgs = new int[2];
	rgdispidNamedArgs[0] = rgdispid[1];
	rgdispidNamedArgs[1] = rgdispid[2];
	automation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

// The following structs are accessed in the readSafeArray and writeSafeArray
// functions:
//
// VARIANT:
// 		short vt
// 		short wReserved1
// 		short wReserved2
// 		short wReserved3
// 		int parray
//
// SAFEARRAY:
//      short cDims      // Count of dimensions in this array
//      short fFeatures  // Flags used by the SafeArray
//      int cbElements   // Size of an element of the array
//      int cLocks       // Number of times the array has been locked without corresponding unlock
//      int pvData       // Pointer to the data
//      SAFEARRAYBOUND[] rgsabound // One bound for each dimension
//
// SAFEARRAYBOUND:
//      int cElements    // the number of elements in the dimension
//      int lLbound      // the lower bound of the dimension 

static String readSafeArray(Variant variantByRef) {
	// Read a safearray that contains data of 
	// type VT_UI1 (unsigned shorts) which contains
	// a text stream.
    int pPostData = variantByRef.getByRef();
    short[] vt_type = new short[1];
    OS.MoveMemory(vt_type, pPostData, 2);
    String result = null;
    if (vt_type[0] == (short)(OLE.VT_BYREF | OLE.VT_VARIANT)) {
        int[] pVariant = new int[1];
        OS.MoveMemory(pVariant, pPostData + 8, 4);
        vt_type = new short[1];
        OS.MoveMemory(vt_type, pVariant[0], 2);
        if (vt_type[0] == (short)(OLE.VT_ARRAY | OLE.VT_UI1)) {
            int[] pSafearray = new int[1];
            OS.MoveMemory(pSafearray, pVariant[0] + 8, 4);
            short[] cDims = new short[1];
            OS.MoveMemory(cDims, pSafearray[0], 2);
            int[] pvData = new int[1];
            OS.MoveMemory(pvData, pSafearray[0] + 12, 4);
            int safearrayboundOffset = 0;
            for (int i = 0; i < cDims[0]; i++) {
                int[] cElements = new int[1];
                OS.MoveMemory(cElements, pSafearray[0] + 16 + safearrayboundOffset, 4);
                safearrayboundOffset += 8;
                int cchWideChar = OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED,  pvData[0], -1, null, 0);
				if (cchWideChar == 0) return null;
				char[] lpWideCharStr = new char [cchWideChar - 1];
				OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED,  pvData[0], -1, lpWideCharStr, lpWideCharStr.length);
				result = new String(lpWideCharStr);
            }
        }
    }
    return result;
}

static Variant writeSafeArray (String string) {
	// Create a one dimensional safearray containing two VT_UI1 values
	// where VT_UI1 is an unsigned char
	
	// Define cDims, fFeatures and cbElements
	short cDims = 1;
	short FADF_FIXEDSIZE = 0x10;
	short FADF_HAVEVARTYPE = 0x80;
	short fFeatures = (short)(FADF_FIXEDSIZE | FADF_HAVEVARTYPE);
	int cbElements = 1;
	// Create a pointer and copy the data into it
	int count = string.length();
	char[] chars = new char[count + 1];
	string.getChars(0, count, chars, 0);
	int cchMultiByte = OS.WideCharToMultiByte(CodePage, 0, chars, -1, null, 0, null, null);
	if (cchMultiByte == 0) return null;
	int pvData = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, cchMultiByte);
	OS.WideCharToMultiByte(CodePage, 0, chars, -1, pvData, cchMultiByte, null, null);
	int cElements1 = cchMultiByte;
	int lLbound1 = 0;
	// Create a safearray in memory
	// 12 bytes for cDims, fFeatures and cbElements + 4 bytes for pvData + number of dimensions * (size of safearraybound)
	int sizeofSafeArray = 12 + 4 + 1*8;
	int pSafeArray = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, sizeofSafeArray);
	// Copy the data into the safe array
	int offset = 0;
	OS.MoveMemory(pSafeArray + offset, new short[] {cDims}, 2); offset += 2;
	OS.MoveMemory(pSafeArray + offset, new short[] {fFeatures}, 2); offset += 2;
	OS.MoveMemory(pSafeArray + offset, new int[] {cbElements}, 4); offset += 4;
	OS.MoveMemory(pSafeArray + offset, new int[] {0}, 4); offset += 4;
	OS.MoveMemory(pSafeArray + offset, new int[] {pvData}, 4); offset += 4;
	OS.MoveMemory(pSafeArray + offset, new int[] {cElements1}, 4); offset += 4;
	OS.MoveMemory(pSafeArray + offset, new int[] {lLbound1}, 4); offset += 4;
	// Create a variant in memory to hold the safearray
	int pVariant = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, Variant.sizeof);
	short vt = (short)(OLE.VT_ARRAY | OLE.VT_UI1);
	OS.MoveMemory(pVariant, new short[] {vt}, 2);
	OS.MoveMemory(pVariant + 8, new int[]{pSafeArray}, 4);
	// Create a by ref variant
	Variant variantByRef = new Variant(pVariant, (short)(OLE.VT_BYREF | OLE.VT_VARIANT));
	return variantByRef;
}
}
