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
package org.eclipse.swt.ole.win32;

import org.eclipse.swt.*;
import java.io.File;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;


/**
 *
 * OLE contains all the constants used to create an ActiveX Control or an OLE Document.
 *
 * <p>Definitions for these constants can be found in MSDN.
 *
 */
public class OLE extends SWT {

	public static final int S_FALSE = 1; // Used for functions that semantically return a Boolean FALSE result to indicate that the function succeeded.
	public static final int S_OK    = 0; // Function succeeded.
	public static final int E_FAIL = -2147467259;  // Unspecified failure.
	public static final int E_INVALIDARG = -2147024809; // Invalid argument
	public static final int E_NOINTERFACE = -2147467262;  // QueryInterface did not recognize the requested interface.
	public static final int E_NOTIMPL = -2147467263; // Not implemented
	
	public static final String IID_IUNKNOWN = "{00000000-0000-0000-C000-000000000046}"; //$NON-NLS-1$
	public static final String IID_IDISPATCH = "{00020400-0000-0000-C000-000000000046}"; //$NON-NLS-1$
	
	// Verbs that can be invoked on this client
	public static final int OLEIVERB_DISCARDUNDOSTATE = -6; // close the OLE object and discard the undo state
	public static final int OLEIVERB_HIDE             = -3; // hide the OLE object
	public static final int OLEIVERB_INPLACEACTIVATE  = -5; // open the OLE for editing in-place
	public static final int OLEIVERB_OPEN             = -2; // open the OLE object for editing in a separate window
	public static final int OLEIVERB_PRIMARY          =  0; // opens the OLE object for editing
	public static final int OLEIVERB_PROPERTIES       = -7; // request the OLE object properties dialog
	public static final int OLEIVERB_SHOW             = -1; // show the OLE object
	public static final int OLEIVERB_UIACTIVATE       = -4; // activate the UI for the OLE object

	public static final int PROPERTY_CHANGING = 0;
	public static final int PROPERTY_CHANGED = 1;

	/**
	 * Error code for OleError - No specific error information available
	 */
	public static final int HRESULT_UNSPECIFIED       = 0;
	/**
	 * Error code for OleError - Failed to create file
	 */
	public static final int ERROR_CANNOT_CREATE_FILE = 1000;
	/**
	 * Error code for OleError - Failed to create Ole Client
	 */
	public static final int ERROR_CANNOT_CREATE_OBJECT = 1001;
	/**
	 * Error code for OleError - File does not exist, is not accessible to user or does not have the correct format
	 */
	public static final int ERROR_CANNOT_OPEN_FILE = 1002;
	/**
	 * Error code for OleError - Failed to find requested interface on OLE Object
	 */
	public static final int ERROR_INTERFACE_NOT_FOUND = 1003;
	/**
	 * Error code for OleError - Class ID not found in registry
	 */
	public static final int ERROR_INVALID_CLASSID = 1004;
	/**
	 * Error code for OleError - Failed to get the class factory for the specified classID
	 */
	public static final int ERROR_CANNOT_ACCESS_CLASSFACTORY = 1005;
	/**
	 * Error code for OleError - Failed to create Licensed instance
	 */
	public static final int ERROR_CANNOT_CREATE_LICENSED_OBJECT = 1006;
	/**
	 * Error code for OleError - Out of Memory
	 */
	public static final int ERROR_OUT_OF_MEMORY = 1007;
	/**
	 * Error code for OleError - Failed to change Variant type
	 */
	public static final int ERROR_CANNOT_CHANGE_VARIANT_TYPE = 1010;
	/**
	 * Error code for OleError - Invalid address received for Ole Interface
	 */
	public static final int ERROR_INVALID_INTERFACE_ADDRESS = 1011;
	/**
	 * Error code for OleError - Unable to find Application
	 */
	public static final int ERROR_APPLICATION_NOT_FOUND = 1013;
	/**
	 * Error code for OleError - Action can not be performed
	 */
	public static final int ERROR_ACTION_NOT_PERFORMED = 1014;

	public static final int OLECMDF_SUPPORTED    = 1;
	public static final int OLECMDF_ENABLED      = 2; 
	public static final int OLECMDF_LATCHED      = 4;
	public static final int OLECMDF_NINCHED      = 8;

	public static final int OLECMDTEXTF_NONE      = 0;
	public static final int OLECMDTEXTF_NAME      = 1;
	public static final int OLECMDTEXTF_STATUS    = 2;

	public static final int OLECMDEXECOPT_DODEFAULT        = 0;
	public static final int OLECMDEXECOPT_PROMPTUSER       = 1;
	public static final int OLECMDEXECOPT_DONTPROMPTUSER   = 2;     
	public static final int OLECMDEXECOPT_SHOWHELP         = 3;
	
	public static final int OLECMDID_OPEN              = 1;
	public static final int OLECMDID_NEW               = 2;
	public static final int OLECMDID_SAVE              = 3;      
	public static final int OLECMDID_SAVEAS            = 4; 
	public static final int OLECMDID_SAVECOPYAS        = 5;      
	public static final int OLECMDID_PRINT             = 6; 
	public static final int OLECMDID_PRINTPREVIEW      = 7;      
	public static final int OLECMDID_PAGESETUP         = 8; 
	public static final int OLECMDID_SPELL             = 9;     
	public static final int OLECMDID_PROPERTIES        = 10; 
	public static final int OLECMDID_CUT               = 11;      
	public static final int OLECMDID_COPY              = 12; 
	public static final int OLECMDID_PASTE             = 13;      
	public static final int OLECMDID_PASTESPECIAL      = 14; 
	public static final int OLECMDID_UNDO              = 15;      
	public static final int OLECMDID_REDO              = 16; 
	public static final int OLECMDID_SELECTALL         = 17;      
	public static final int OLECMDID_CLEARSELECTION    = 18; 
	public static final int OLECMDID_ZOOM              = 19;      
	public static final int OLECMDID_GETZOOMRANGE      = 20;
	public static final int OLECMDID_UPDATECOMMANDS    = 21;     
	public static final int OLECMDID_REFRESH           = 22;
	public static final int OLECMDID_STOP              = 23;     
	public static final int OLECMDID_HIDETOOLBARS      = 24;
	public static final int OLECMDID_SETPROGRESSMAX    = 25;     
	public static final int OLECMDID_SETPROGRESSPOS    = 26;
	public static final int OLECMDID_SETPROGRESSTEXT   = 27;     
	public static final int OLECMDID_SETTITLE          = 28;
	public static final int OLECMDID_SETDOWNLOADSTATE  = 29;     
	public static final int OLECMDID_STOPDOWNLOAD      = 30;

	/* Ole Property Description flags */
	public static int VARFLAG_FREADONLY = 0x1;
	public static int VARFLAG_FSOURCE = 0x2;
	public static int VARFLAG_FBINDABLE = 0x4;
	public static int VARFLAG_FREQUESTEDIT = 0x8;
	public static int VARFLAG_FDISPLAYBIND = 0x10;
	public static int VARFLAG_FDEFAULTBIND = 0x20;
	public static int VARFLAG_FHIDDEN = 0x40;
	public static int VARFLAG_FRESTRICTED = 0x80;
	public static int VARFLAG_FDEFAULTCOLLELEM = 0x100;
	public static int VARFLAG_FUIDEFAULT = 0x200;
	public static int VARFLAG_FNONBROWSABLE = 0x400;
	public static int VARFLAG_FREPLACEABLE = 0x800;
	public static int VARFLAG_FIMMEDIATEBIND = 0x1000;

	/* Ole Property Description kind */
	public static int VAR_PERINSTANCE = 0;
	public static int VAR_STATIC = 1;
	public static int VAR_CONST = 2;
	public static int VAR_DISPATCH = 3;
	
	/* Ole Parameter Description flags */
	public static short IDLFLAG_NONE = 0;
	public static short IDLFLAG_FIN = 1;
	public static short IDLFLAG_FOUT = 2;
	public static short IDLFLAG_FLCID = 4;
	public static short IDLFLAG_FRETVAL = 8;
		
	/* Ole Description types */
	public static final short VT_BOOL = 11;     // Boolean; True=-1, False=0.
	public static final short VT_BSTR = 8;      // Binary String.
	public static final short VT_BYREF = 16384; // By reference - must be combined with one of the other VT values
	public static final short VT_CY = 6;        // Currency.
	public static final short VT_DATE = 7;      // Date.
	public static final short VT_DISPATCH = 9;  // IDispatch
	public static final short VT_EMPTY = 0;     // Not specified.
	public static final short VT_ERROR = 10;    // Scodes.
	public static final short VT_I2 = 2;        // 2-byte signed int.
	public static final short VT_I4 = 3;        // 4-byte signed int.
	public static final short VT_NULL = 1;      // Null.
	public static final short VT_R4 = 4;        // 4-byte real.
	public static final short VT_R8 = 5;        // 8-byte real.
	public static final short VT_UI1 = 17;      // Unsigned char.
	public static final short VT_UI4 = 19;      // Unsigned int.
	public static final short VT_UNKNOWN = 13;  // IUnknown FAR*.
	public static final short VT_VARIANT = 12;  // VARIANT FAR*.
	public static final short VT_PTR = 26;
	public static final short VT_USERDEFINED = 29;
	public static final short VT_HRESULT = 25;
	public static final short VT_DECIMAL = 14;
	public static final short VT_I1 = 16;
	public static final short VT_UI2 = 18;
	public static final short VT_I8 = 20;
	public static final short VT_UI8 = 21;
	public static final short VT_INT = 22;
	public static final short VT_UINT = 23;
	public static final short VT_VOID = 24;
	public static final short VT_SAFEARRAY = 27;
	public static final short VT_CARRAY = 28;
	public static final short VT_LPSTR = 30;
	public static final short VT_LPWSTR = 31;
	public static final short VT_RECORD = 36;
	public static final short VT_FILETIME = 64;
	public static final short VT_BLOB = 65;
	public static final short VT_STREAM = 66;
	public static final short VT_STORAGE = 67;
	public static final short VT_STREAMED_OBJECT = 68;
	public static final short VT_STORED_OBJECT = 69;
	public static final short VT_BLOB_OBJECT = 70;
	public static final short VT_CF = 71;
	public static final short VT_CLSID = 72;
	public static final short VT_VERSIONED_STREAM = 73;
	public static final short VT_BSTR_BLOB = 0xfff;
	public static final short VT_VECTOR = 0x1000;
	public static final short VT_ARRAY = 0x2000;

	/* Ole Function Description Invoke Kind values */
	public static final int INVOKE_FUNC = 1;
	public static final int INVOKE_PROPERTYGET = 2;
	public static final int INVOKE_PROPERTYPUT = 4;
	public static final int INVOKE_PROPERTYPUTREF = 8;
	
	/* Ole Function Description function kind */
	public static final int FUNC_VIRTUAL = 0;
	public static final int FUNC_PUREVIRTUAL = 1;
	public static final int FUNC_NONVIRTUAL = 2;
	public static final int FUNC_STATIC = 3;
	public static final int FUNC_DISPATCH = 4;
	
	/* Ole Function Description function flags */	
	public static final short FUNCFLAG_FRESTRICTED = 1;
	public static final short FUNCFLAG_FSOURCE = 0x2;
	public static final short FUNCFLAG_FBINDABLE = 0x4;
	public static final short FUNCFLAG_FREQUESTEDIT = 0x8;
	public static final short FUNCFLAG_FDISPLAYBIND = 0x10;
	public static final short FUNCFLAG_FDEFAULTBIND = 0x20;
  	public static final short FUNCFLAG_FHIDDEN = 0x40;
	public static final short FUNCFLAG_FUSESGETLASTERROR = 0x80;
	public static final short FUNCFLAG_FDEFAULTCOLLELEM = 0x100;
	public static final short FUNCFLAG_FUIDEFAULT = 0x200;
	public static final short FUNCFLAG_FNONBROWSABLE = 0x400;
	public static final short FUNCFLAG_FREPLACEABLE = 0x800;
	public static final short FUNCFLAG_FIMMEDIATEBIND = 0x1000;
	
	/* Ole Function Description calling convention */
	public static final int CC_FASTCALL = 0;
	public static final int CC_CDECL = 1;
	public static final int CC_MSCPASCAL = 2;
	public static final int CC_PASCAL = 2;
	public static final int CC_MACPASCAL = 3;
	public static final int CC_STDCALL = 4;
	public static final int CC_FPFASTCALL = 5;
	public static final int CC_SYSCALL = 6;
	public static final int CC_MPWCDECL = 7;
	public static final int CC_MPWPASCAL = 8;
	public static final int CC_MAX = 9;
	
	static final String ERROR_NOT_IMPLEMENTED_MSG = "Required functionality not currently supported.";//$NON-NLS-1$
	static final String ERROR_CANNOT_CREATE_FILE_MSG = "Failed to create file.";//$NON-NLS-1$
	static final String ERROR_CANNOT_CREATE_OBJECT_MSG = "Failed to create Ole Client.";//$NON-NLS-1$
	static final String ERROR_CANNOT_OPEN_FILE_MSG = "File does not exist, is not accessible to user or does not have the correct format.";//$NON-NLS-1$
	static final String ERROR_INTERFACE_NOT_FOUND_MSG = "Failed to find requested interface on OLE Object.";//$NON-NLS-1$
	static final String ERROR_INVALID_CLASSID_MSG = "Class ID not found in registry";//$NON-NLS-1$
	static final String ERROR_CANNOT_ACCESS_CLASSFACTORY_MSG = "Failed to get the class factory for the specified classID";//$NON-NLS-1$
	static final String ERROR_CANNOT_CREATE_LICENSED_OBJECT_MSG = "Failed to create Licensed instance";//$NON-NLS-1$
	static final String ERROR_OUT_OF_MEMORY_MSG = "Out of Memory";//$NON-NLS-1$
	static final String ERROR_CANNOT_CHANGE_VARIANT_TYPE_MSG = "Failed to change Variant type";//$NON-NLS-1$
	static final String ERROR_INVALID_INTERFACE_ADDRESS_MSG = "Invalid address received for Ole Interface.";//$NON-NLS-1$
	static final String ERROR_APPLICATION_NOT_FOUND_MSG = "Unable to find Application.";//$NON-NLS-1$
	static final String ERROR_ACTION_NOT_PERFORMED_MSG = "Action can not be performed.";//$NON-NLS-1$


public static void error (int code) {
	error (code, 0);
}
public static void error (int code, int hresult) {
		
	switch (code) {
		/* Illegal Arguments (non-fatal) */
		case ERROR_INVALID_INTERFACE_ADDRESS :{
			throw new IllegalArgumentException (ERROR_INVALID_INTERFACE_ADDRESS_MSG);
		}
	
		/* SWT Errors (non-fatal) */
		case ERROR_CANNOT_CREATE_FILE : {
			String msg = ERROR_CANNOT_CREATE_FILE_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_CANNOT_CREATE_OBJECT : {
			String msg = ERROR_CANNOT_CREATE_OBJECT_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);//$NON-NLS-1$
		}
		case ERROR_CANNOT_OPEN_FILE : {
			String msg = ERROR_CANNOT_OPEN_FILE_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_INTERFACE_NOT_FOUND : {
			String msg = ERROR_INTERFACE_NOT_FOUND_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_INVALID_CLASSID : {
			String msg = ERROR_INVALID_CLASSID_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_CANNOT_ACCESS_CLASSFACTORY : {
			String msg = ERROR_CANNOT_ACCESS_CLASSFACTORY_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_CANNOT_CREATE_LICENSED_OBJECT : {
			String msg = ERROR_CANNOT_CREATE_LICENSED_OBJECT_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_CANNOT_CHANGE_VARIANT_TYPE : {
			String msg = ERROR_CANNOT_CHANGE_VARIANT_TYPE_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_APPLICATION_NOT_FOUND : {
			String msg = ERROR_APPLICATION_NOT_FOUND_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		case ERROR_ACTION_NOT_PERFORMED : {
			String msg = ERROR_ACTION_NOT_PERFORMED_MSG;
			if (hresult != 0) msg += " result = "+hresult;//$NON-NLS-1$
			throw new SWTException (code, msg);
		}
		
		/* OS Failure/Limit (fatal, may occur only on some platforms) */
		case ERROR_OUT_OF_MEMORY : {
			String msg = ERROR_ACTION_NOT_PERFORMED_MSG;
			if (hresult != 0) msg += " result = "+hresult; //$NON-NLS-1$
			throw new SWTError (code, msg);
		}
	}
			
	/* Unknown/Undefined Error */
	SWT.error(code);
}

/*
 * Finds the OLE program id that is associated with an extension.
 * The extension may or may not begin with a '.'.  On platforms
 * that do not support OLE, an empty string is returned.
 *
 * @param extension the program extension
 * @return a string that is the OLE program id or an empty string
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static String findProgramID (String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return ""; //$NON-NLS-1$
	
	if (extension.charAt (0) != '.') extension = "." + extension; //$NON-NLS-1$

	/* Use the character encoding for the default locale */
	TCHAR extensionKey = new TCHAR(0, extension, true);
	String result = getKeyValue(extensionKey);
	if (result != null) {
		// look for "<programID>\NotInsertable"
		TCHAR notInsertableKey = new TCHAR(0, result+"\\NotInsertable", true); //$NON-NLS-1$
		if (getKeyExists(notInsertableKey)) return ""; //$NON-NLS-1$
		// look for "<programID>\Insertable"
		TCHAR insertableKey = new TCHAR(0, result+"\\Insertable", true); //$NON-NLS-1$
		if (getKeyExists(insertableKey)) return result;
		// look for "<programID>\protocol\StdFileEditing\server"
		TCHAR serverKey = new TCHAR(0, result+"\\protocol\\StdFileEditing\\server", true); //$NON-NLS-1$
		if (getKeyExists(serverKey)) return result;
	}
	
	return ""; //$NON-NLS-1$
}
static String getKeyValue (TCHAR key) {
	int /*long*/ [] phkResult = new int /*long*/ [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}
	String result = null;
	int [] lpcbData = new int [1];
	if (OS.RegQueryValueEx (phkResult [0], (TCHAR) null, 0, null, (TCHAR) null, lpcbData) == 0) {
		int length = lpcbData [0] / TCHAR.sizeof;
		if (length == 0) {
			result = "";
		} else {
			/* Use the character encoding for the default locale */
			TCHAR lpData = new TCHAR (0, length);
			if (OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData) == 0) {
				length = Math.max(0, lpData.length () - 1);
				result = lpData.toString (0, length);
			}
		}
	}
	if (phkResult [0] != 0) OS.RegCloseKey (phkResult [0]);
	return result;
}
private static boolean getKeyExists (TCHAR key) {
	int /*long*/ [] phkResult = new int /*long*/ [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return false;
	}
	if (phkResult [0] != 0) OS.RegCloseKey (phkResult [0]);
	return true;
}
/**
 * Returns true if the specified file has an OLE Storage format.
 *
 * Note all empty files (regardless of extension) will return false.
 *
 * @param file the file to be checked
 *
 * @return true if this file has an OLE Storage format
 */
public static boolean isOleFile(File file) {
	if (file == null || !file.exists() || file.isDirectory())
		return false;

	return (COM.StgIsStorageFile((file.getAbsolutePath()+"\0").toCharArray()) == COM.S_OK);
}

}
