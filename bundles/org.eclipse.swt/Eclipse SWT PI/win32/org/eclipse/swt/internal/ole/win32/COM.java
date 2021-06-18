/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class COM extends OS {
	/** GUID Constants */
	public static final GUID CLSID_CMultiLanguage = IIDFromString("{275c23e2-3747-11d0-9fea-00aa003f8646}");
	public static final GUID CLSID_DestinationList = IIDFromString ("{77f10cf0-3db5-4966-b520-b7c54fd35ed6}"); //$NON-NLS-1$
	public static final GUID CLSID_DragDropHelper = COM.IIDFromString("{4657278A-411B-11d2-839A-00C04FD918D0}"); //$NON-NLS-1$
	public static final GUID CLSID_EnumerableObjectCollection = IIDFromString ("{2d3468c1-36a7-43b6-ac24-d3f02fd9607a}"); //$NON-NLS-1$
	public static final GUID CLSID_FileOpenDialog = IIDFromString("{DC1C5A9C-E88A-4dde-A5A1-60F82A20AEF7}"); //$NON-NLS-1$
	public static final GUID CLSID_FileSaveDialog = IIDFromString("{C0B4E2F3-BA21-4773-8DBA-335EC946EB8B}"); //$NON-NLS-1$
	public static final GUID CLSID_ShellLink = IIDFromString ("{00021401-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID CLSID_TaskbarList = IIDFromString ("{56FDF344-FD6D-11d0-958A-006097C9A090}"); //$NON-NLS-1$
	public static final GUID CLSID_TF_InputProcessorProfiles = IIDFromString("{33C53A50-F456-4884-B049-85FD643ECFED}"); //$NON-NLS-1$
	public static final GUID GUID_TFCAT_TIP_KEYBOARD = IIDFromString("{34745C63-B2F0-4784-8B67-5E12C8701A31}"); //$NON-NLS-1$
	public static final GUID IID_ICustomDestinationList = IIDFromString ("{6332debf-87b5-4670-90c0-5e57b408a49e}"); //$NON-NLS-1$
	public static final GUID IID_IDropTargetHelper = COM.IIDFromString("{4657278B-411B-11D2-839A-00C04FD918D0}"); //$NON-NLS-1$
	public static final GUID IID_IFileOpenDialog = IIDFromString("{d57c7288-d4ad-4768-be02-9d969532d960}"); //$NON-NLS-1$
	public static final GUID IID_IFileSaveDialog = IIDFromString("{84bccd23-5fde-4cdb-aea4-af64b83d78ab}"); //$NON-NLS-1$
	public static final GUID IID_IMLangFontLink2 = IIDFromString("{DCCFC162-2B38-11d2-B7EC-00C04F8F5D9A}");
	public static final GUID IID_IObjectArray = IIDFromString ("{92CA9DCD-5622-4bba-A805-5E9F541BD8C9}"); //$NON-NLS-1$
	public static final GUID IID_IObjectCollection = IIDFromString ("{5632b1a4-e38a-400a-928a-d4cd63230295}"); //$NON-NLS-1$
	public static final GUID IID_IPropertyStore = IIDFromString ("{886d8eeb-8cf2-4446-8d02-cdba1dbdcf99}"); //$NON-NLS-1$
	public static final GUID IID_IShellItem = IIDFromString("{43826d1e-e718-42ee-bc55-a1e261c37bfe}"); //$NON-NLS-1$
	public static final GUID IID_IShellLinkW = IIDFromString ("{000214F9-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IID_ITaskbarList3 = IIDFromString ("{ea1afb91-9e28-4b86-90e9-9e9f8a5eefaf}"); //$NON-NLS-1$
	public static final GUID IID_ITfDisplayAttributeProvider = IIDFromString("{fee47777-163c-4769-996a-6e9c50ad8f54}"); //$NON-NLS-1$
	public static final GUID IID_ITfInputProcessorProfiles = IIDFromString("{1F02B6C5-7842-4EE6-8A0B-9A24183A95CA}"); //$NON-NLS-1$
	public static final GUID IIDJavaBeansBridge = COM.IIDFromString("{8AD9C840-044E-11D1-B3E9-00805F499D93}"); //$NON-NLS-1$
	public static final GUID IIDShockwaveActiveXControl = COM.IIDFromString("{166B1BCA-3F9C-11CF-8075-444553540000}"); //$NON-NLS-1$
	public static final GUID IIDIAccessible = IIDFromString("{618736E0-3C3D-11CF-810C-00AA00389B71}"); //$NON-NLS-1$
	public static final GUID IIDIAdviseSink = IIDFromString("{0000010F-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIClassFactory = IIDFromString("{00000001-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIClassFactory2 = IIDFromString("{B196B28F-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIConnectionPointContainer = IIDFromString("{B196B284-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIDataObject = IIDFromString("{0000010E-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIDispatch = IIDFromString("{00020400-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIDispatchEx = IIDFromString("{A6EF9860-C720-11D0-9337-00A0C90DCAA9}"); //$NON-NLS-1$
	public static final GUID IIDIDocHostUIHandler = IIDFromString("{BD3F23C0-D43E-11CF-893B-00AA00BDCE1A}"); //$NON-NLS-1$
	public static final GUID IIDIDocHostShowUI = IIDFromString("{C4D244B0-D43E-11CF-893B-00AA00BDCE1A}"); //$NON-NLS-1$
	public static final GUID IIDIDropSource = IIDFromString("{00000121-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIDropTarget = IIDFromString("{00000122-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIEnumFORMATETC = IIDFromString("{00000103-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIEnumVARIANT = IIDFromString("{00020404-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final /*GUID*/ String IIDIHTMLDocumentEvents2 = /*IIDFromString(*/"{3050F613-98B5-11CF-BB82-00AA00BDCE0B}"/*)*/;
	public static final GUID IIDIInternetSecurityManager = IIDFromString("{79eac9ee-baf9-11ce-8c82-00aa004ba90b}"); //$NON-NLS-1$
	public static final GUID IIDIAuthenticate = IIDFromString("{79eac9d0-baf9-11ce-8c82-00aa004ba90b}"); //$NON-NLS-1$
	public static final GUID IIDIJScriptTypeInfo = IIDFromString("{C59C6B12-F6C1-11CF-8835-00A0C911E8B2}"); //$NON-NLS-1$
	public static final GUID IIDIOleClientSite = IIDFromString("{00000118-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIOleCommandTarget = IIDFromString("{B722BCCB-4E68-101B-A2BC-00AA00404770}"); //$NON-NLS-1$
	public static final GUID IIDIOleControl = IIDFromString("{B196B288-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIOleControlSite = IIDFromString("{B196B289-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIOleDocument = IIDFromString("{B722BCC5-4E68-101B-A2BC-00AA00404770}"); //$NON-NLS-1$
	public static final GUID IIDIOleDocumentSite = IIDFromString("{B722BCC7-4E68-101B-A2BC-00AA00404770}"); //$NON-NLS-1$
	public static final GUID IIDIOleInPlaceFrame = IIDFromString("{00000116-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIOleInPlaceObject = IIDFromString("{00000113-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIOleInPlaceSite = IIDFromString("{00000119-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIOleLink = IIDFromString("{0000011D-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIOleObject = IIDFromString("{00000112-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIPersist = IIDFromString("{0000010C-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIPersistFile = IIDFromString("{0000010B-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIPersistStorage = IIDFromString("{0000010A-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIPersistStreamInit = IIDFromString("{7FD52380-4E07-101B-AE2D-08002B2EC713}"); //$NON-NLS-1$
	public static final GUID IIDIPropertyNotifySink = IIDFromString("{9BFBBC02-EFF1-101A-84ED-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIProvideClassInfo = IIDFromString("{B196B283-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIProvideClassInfo2 = IIDFromString("{A6BC3AC0-DBAA-11CE-9DE3-00AA004BB851}"); //$NON-NLS-1$
	public static final GUID IIDIServiceProvider = IIDFromString("{6d5140c1-7436-11ce-8034-00aa006009fa}"); //$NON-NLS-1$
	public static final GUID IIDISpecifyPropertyPages = IIDFromString("{B196B28B-BAB4-101A-B69C-00AA00341D07}"); //$NON-NLS-1$
	public static final GUID IIDIUnknown = IIDFromString("{00000000-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IIDIViewObject2 = IIDFromString("{00000127-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID CGID_DocHostCommandHandler = IIDFromString("{f38bc242-b950-11d1-8918-00c04fc2c836}"); //$NON-NLS-1$
	public static final GUID CGID_Explorer = IIDFromString("{000214D0-0000-0000-C000-000000000046}"); //$NON-NLS-1$
	public static final GUID IID_ICoreWebView2Environment2 = IIDFromString("{41F3632B-5EF4-404F-AD82-2D606C5A9A21}"); //$NON-NLS-1$
	public static final GUID IID_ICoreWebView2_2 = IIDFromString("{9E8F0CF8-E670-4B5E-B2BC-73E061E3184C}"); //$NON-NLS-1$

	// IA2 related GUIDS
	public static final GUID IIDIAccessible2 = IIDFromString("{E89F726E-C4F4-4c19-BB19-B647D7FA8478}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleRelation = IIDFromString("{7CDF86EE-C3DA-496a-BDA4-281B336E1FDC}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleAction = IIDFromString("{B70D9F59-3B5A-4dba-AB9E-22012F607DF5}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleComponent = IIDFromString("{1546D4B0-4C98-4bda-89AE-9A64748BDDE4}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleValue = IIDFromString("{35855B5B-C566-4fd0-A7B1-E65465600394}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleText = IIDFromString("{24FD2FFB-3AAD-4a08-8335-A3AD89C0FB4B}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleEditableText = IIDFromString("{A59AA09A-7011-4b65-939D-32B1FB5547E3}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleHyperlink = IIDFromString("{01C20F2B-3DD2-400f-949F-AD00BDAB1D41}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleHypertext = IIDFromString("{6B4F8BBF-F1F2-418a-B35E-A195BC4103B9}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleTable = IIDFromString("{35AD8070-C20C-4fb4-B094-F4F7275DD469}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleTable2 = IIDFromString("{6167f295-06f0-4cdd-a1fa-02e25153d869}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleTableCell = IIDFromString("{594116B1-C99F-4847-AD06-0A7A86ECE645}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleImage = IIDFromString("{FE5ABB3D-615E-4f7b-909F-5F0EDA9E8DDE}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleApplication = IIDFromString("{D49DED83-5B25-43F4-9B95-93B44595979E}"); //$NON-NLS-1$
	public static final GUID IIDIAccessibleContext = IIDFromString("{77A123E4-5794-44e0-B8BF-DE600C9D29BD}"); //$NON-NLS-1$

	/** Constants */
	public static final int CF_TEXT = 1;
	public static final int CF_BITMAP = 2;
	public static final int CF_METAFILEPICT = 3;
	public static final int CF_SYLK = 4;
	public static final int CF_DIF = 5;
	public static final int CF_TIFF = 6;
	public static final int CF_OEMTEXT = 7;
	public static final int CF_DIB = 8;
	public static final int CF_PALETTE = 9;
	public static final int CF_PENDATA = 10;
	public static final int CF_RIFF = 11;
	public static final int CF_WAVE = 12;
	public static final int CF_UNICODETEXT = 13;
	public static final int CF_ENHMETAFILE = 14;
	public static final int CF_HDROP = 15;
	public static final int CF_LOCALE = 16;
	public static final int CF_MAX = 17;
	public static final int CLSCTX_INPROC_HANDLER = 2;
	public static final int CLSCTX_INPROC_SERVER = 1;
	public static final int CLSCTX_LOCAL_SERVER = 4;
	public static final int DATADIR_GET = 1;
	public static final int DATADIR_SET = 2;
	public static final int DISPATCH_CONSTRUCT = 0x4000;
	public static final int DISP_E_EXCEPTION = 0x80020009;
	public static final int DISP_E_MEMBERNOTFOUND = -2147352573;
	public static final int DISP_E_UNKNOWNINTERFACE = 0x80020001;
	public static final int DISP_E_UNKNOWNNAME = 0x80020006;
	public static final int DISPID_AMBIENT_BACKCOLOR = -701;
	public static final int DISPID_AMBIENT_FONT = -703;
	public static final int DISPID_AMBIENT_FORECOLOR = -704;
	public static final int DISPID_AMBIENT_LOCALEID = -705;
	public static final int DISPID_AMBIENT_MESSAGEREFLECT = -706;
	public static final int DISPID_AMBIENT_OFFLINEIFNOTCONNECTED = -5501;
	public static final int DISPID_AMBIENT_SHOWGRABHANDLES = -711;
	public static final int DISPID_AMBIENT_SHOWHATCHING = -712;
	public static final int DISPID_AMBIENT_SILENT = -5502;
	public static final int DISPID_AMBIENT_SUPPORTSMNEMONICS = -714;
	public static final int DISPID_AMBIENT_UIDEAD = -710;
	public static final int DISPID_AMBIENT_USERMODE = -709;
	public static final int DISPID_BACKCOLOR = -501;
	public static final int DISPID_FONT = -512;
	public static final int DISPID_FONT_BOLD = 3;
	public static final int DISPID_FONT_CHARSET = 8;
	public static final int DISPID_FONT_ITALIC = 4;
	public static final int DISPID_FONT_NAME = 0;
	public static final int DISPID_FONT_SIZE = 2;
	public static final int DISPID_FONT_STRIKE = 6;
	public static final int DISPID_FONT_UNDER = 5;
	public static final int DISPID_FONT_WEIGHT = 7;
	public static final int DISPID_FORECOLOR = -513;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONDBLCLICK = 0xFFFFFDA7;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONDRAGEND = 0x80010015;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONDRAGSTART = 0x8001000B;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONKEYDOWN = 0xFFFFFDA6;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONKEYPRESS = 0xFFFFFDA5;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONKEYUP = 0xFFFFFDA4;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOUT = 0x80010009;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOVER = 0x80010008;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEMOVE = 0xFFFFFDA2;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEDOWN = 0xFFFFFDA3;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEUP = 0xFFFFFDA1;
	public static final int DISPID_HTMLDOCUMENTEVENTS_ONMOUSEWHEEL = 1033;

	public static final int DRAGDROP_S_DROP = 0x00040100;
	public static final int DRAGDROP_S_CANCEL = 0x00040101;
	public static final int DRAGDROP_S_USEDEFAULTCURSORS = 0x00040102;
	public static final int DROPEFFECT_NONE = 0;
	public static final int DROPEFFECT_COPY = 1;
	public static final int DROPEFFECT_MOVE = 2;
	public static final int DROPEFFECT_LINK = 4;
	public static final int DV_E_FORMATETC = -2147221404;
	public static final int DV_E_STGMEDIUM = -2147221402;
	public static final int DV_E_TYMED = -2147221399;
	public static final int DVASPECT_CONTENT = 1;
	public static final int E_ACCESSDENIED = 0x80070005;
	public static final int E_FAIL = -2147467259;
	public static final int E_INVALIDARG = -2147024809;
	public static final int E_NOINTERFACE = -2147467262;
	public static final int E_NOTIMPL = -2147467263;
	public static final int E_NOTSUPPORTED = 0x80040100;
	public static final int E_OUTOFMEMORY = -2147024882;
	public static final int GMEM_FIXED = 0;
	public static final int GMEM_ZEROINIT = 64;
	public static final int GUIDKIND_DEFAULT_SOURCE_DISP_IID = 1;
	public static final int IMPLTYPEFLAG_FDEFAULT = 1;
	public static final int IMPLTYPEFLAG_FRESTRICTED = 4;
	public static final int IMPLTYPEFLAG_FSOURCE = 2;
	public static final int LOCALE_USER_DEFAULT = 2048;
	public static final int OLECLOSE_NOSAVE = 1;
	public static final int OLECLOSE_SAVEIFDIRTY = 0;
	public static final int OLEEMBEDDED = 1;
	public static final int OLELINKED = 0;
	public static final int OLERENDER_DRAW = 1;
	public static final int REGDB_E_CLASSNOTREG = 0x80040154;
	public static final int S_FALSE = 1;
	public static final int S_OK = 0;
	public static final int STGC_DEFAULT = 0;
	public static final int STGM_CREATE = 0x00001000;
	public static final int STGM_DELETEONRELEASE = 0x04000000;
	public static final int STGM_DIRECT = 0x00000000;
	public static final int STGM_READ = 0x00000000;
	public static final int STGM_READWRITE = 0x00000002;
	public static final int STGM_SHARE_EXCLUSIVE = 0x00000010;
	public static final int STGM_TRANSACTED = 0x00010000;
	public static final int TYMED_HGLOBAL = 1;
	public static final short DISPATCH_METHOD = 0x1;
	public static final short DISPATCH_PROPERTYGET = 0x2;
	public static final short DISPATCH_PROPERTYPUT = 0x4;
	public static final short DISPATCH_PROPERTYPUTREF = 0x8;
	public static final short DISPID_PROPERTYPUT = -3;
	public static final short DISPID_UNKNOWN = -1;
	public static final short DISPID_VALUE = 0;
	public static final short VT_BOOL = 11;
	public static final short VT_BSTR = 8;
	public static final short VT_BYREF = 16384;
	public static final short VT_DATE = 7;
	public static final short VT_DISPATCH = 9;
	public static final short VT_EMPTY = 0;
	public static final short VT_I1 = 16;
	public static final short VT_I2 = 2;
	public static final short VT_I4 = 3;
	public static final short VT_I8 = 20;
	public static final short VT_NULL = 1;
	public static final short VT_R4 = 4;
	public static final short VT_R8 = 5;
	public static final short VT_UI1 = 17;
	public static final short VT_UI2 = 18;
	public static final short VT_UI4 = 19;
	public static final short VT_UNKNOWN = 13;
	public static final short VT_VARIANT = 12;

	public static final int COREWEBVIEW2_MOVE_FOCUS_REASON_PROGRAMMATIC = 0;
	public static final int COREWEBVIEW2_MOVE_FOCUS_REASON_NEXT = 1;
	public static final int COREWEBVIEW2_MOVE_FOCUS_REASON_PREVIOUS = 2;

	public static boolean FreeUnusedLibraries = true;

private static GUID IIDFromString(String lpsz) {
	int length = lpsz.length();
	char[] buffer = new char[length + 1];
	lpsz.getChars(0, length, buffer, 0);
	GUID lpiid = new GUID();
	if (COM.IIDFromString(buffer, lpiid) == COM.S_OK) return lpiid;
	return null;
}

/** Natives */

/**
 * @param lpszProgID cast=(LPCOLESTR),flags=no_out
 * @param pclsid flags=no_in
 */
public static final native int CLSIDFromProgID(char[] lpszProgID, GUID pclsid);
/**
 * @param lpsz cast=(LPOLESTR),flags=no_out
 * @param pclsid flags=no_in
 */
public static final native int CLSIDFromString(char[] lpsz, GUID pclsid);
/**
 * @param rclsid flags=no_out
 * @param pUnkOuter cast=(LPUNKNOWN)
 * @param riid flags=no_out
 * @param ppv cast=(LPVOID *)
 */
public static final native int CoCreateInstance(GUID rclsid, long pUnkOuter, int dwClsContext, GUID riid, long[] ppv);
public static final native void CoFreeUnusedLibraries();
/**
 * @param rclsid flags=no_out
 * @param pServerInfo cast=(COSERVERINFO *)
 * @param riid flags=no_out
 * @param ppv cast=(LPVOID *)
 */
public static final native int CoGetClassObject(GUID rclsid, int dwClsContext, long pServerInfo, GUID riid, long[] ppv);
/**
 * @param pUnk cast=(IUnknown *)
 * @param fLock cast=(BOOL)
 * @param fLastUnlockReleases cast=(BOOL)
 */
public static final native int CoLockObjectExternal(long pUnk, boolean fLock, boolean fLastUnlockReleases);
/**
 * @param pDataObject cast=(IDataObject *)
 * @param pDropSource cast=(IDropSource *)
 * @param pdwEffect cast=(LPDWORD)
 */
public static final native int DoDragDrop(long pDataObject, long pDropSource, int dwOKEffect, int[] pdwEffect);
/**
 * @param szFileName cast=(LPCWSTR),flags=no_out
 * @param clsid flags=no_in
 */
public static final native int GetClassFile(char[] szFileName, GUID clsid);
/**
 * @param lpsz cast=(LPOLESTR),flags=no_out
 * @param lpiid flags=no_in
 */
public static final native int IIDFromString(char[] lpsz, GUID lpiid);
/**
 * @param rguid1 flags=no_out
 * @param rguid2 flags=no_out
 */
public static final native boolean IsEqualGUID(GUID rguid1, GUID rguid2);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory(long Destination, FORMATETC Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory(long DestinationPtr, OLEINPLACEFRAMEINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory(long Destination, STGMEDIUM Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory(STGMEDIUM Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory(DISPPARAMS Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory(FORMATETC Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory(GUID Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory(TYPEATTR Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory(RECT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory(FUNCDESC Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory(VARDESC Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory(VARIANT Destination, long Source, int Length);
/**
 * @param rclsid flags=no_out
 * @param riid flags=no_out
 * @param pFormatEtc flags=no_out
 * @param pClientSite cast=(IOleClientSite *)
 * @param pStg cast=(IStorage *)
 * @param ppvObject cast=(void **)
 */
public static final native int OleCreate(GUID rclsid, GUID riid, int renderopt, FORMATETC pFormatEtc, long pClientSite, long pStg, long[] ppvObject);
/**
 * @param rclsid flags=no_out
 * @param lpszFileName cast=(LPCOLESTR),flags=no_out
 * @param riid flags=no_out
 * @param pFormatEtc flags=no_out
 * @param pClientSite cast=(LPOLECLIENTSITE)
 * @param pStg cast=(LPSTORAGE)
 * @param ppvObj cast=(LPVOID *)
 */
public static final native int OleCreateFromFile(GUID rclsid, char[] lpszFileName, GUID riid, int renderopt, FORMATETC pFormatEtc, long pClientSite, long pStg, long[] ppvObj);
/**
 * @param hwndOwner cast=(HWND)
 * @param lpszCaption cast=(LPCOLESTR),flags=no_out
 * @param lplpUnk cast=(LPUNKNOWN FAR*)
 * @param lpPageClsID cast=(LPCLSID)
 * @param lcid cast=(LCID)
 * @param lpvReserved cast=(LPVOID)
 */
public static final native int OleCreatePropertyFrame(long hwndOwner,int x, int y, char[] lpszCaption, int cObjects, long[] lplpUnk, int cPages, long lpPageClsID, int lcid, int dwReserved, long lpvReserved);
/**
 * @param pUnk cast=(LPUNKNOWN)
 * @param dwAspect cast=(DWORD)
 * @param hdcDraw cast=(HDC)
 * @param lprcBounds cast=(LPRECT)
 */
public static final native int OleDraw(long pUnk, int dwAspect, long hdcDraw, long lprcBounds);
public static final native int OleFlushClipboard();
/** @param ppDataObject cast=(IDataObject **) */
public static final native int OleGetClipboard(long[] ppDataObject);
/** @param pDataObject cast=(IDataObject *) */
public static final native int OleIsCurrentClipboard(long pDataObject);
/** @param pObject cast=(LPOLEOBJECT) */
public static final native boolean OleIsRunning(long pObject);
/** @param pUnknown cast=(LPUNKNOWN) */
public static final native int OleRun(long pUnknown);
/**
 * @param pPS cast=(IPersistStorage *)
 * @param pStg cast=(IStorage *)
 */
public static final native int OleSave(long pPS, long pStg, boolean fSameAsLoad);
/** @param pDataObject cast=(IDataObject *) */
public static final native int OleSetClipboard(long pDataObject);
/** @param pUnk cast=(LPUNKNOWN) */
public static final native int OleSetContainedObject(long pUnk, boolean fContained);
/**
 * @param holemenu cast=(HOLEMENU)
 * @param hwndFrame cast=(HWND)
 * @param hwndActiveObject cast=(HWND)
 * @param lpFrame cast=(LPOLEINPLACEFRAME)
 * @param lpActiveObj cast=(LPOLEINPLACEACTIVEOBJECT)
 */
public static final native int OleSetMenuDescriptor(long holemenu, long hwndFrame, long hwndActiveObject, long lpFrame, long lpActiveObj);
/**
 * @param clr cast=(OLE_COLOR)
 * @param hpal cast=(HPALETTE)
 * @param pcolorref cast=(COLORREF *)
 */
public static final native int OleTranslateColor(int clr, long hpal, int[] pcolorref);
/**
 * Custom native function.
 * @param pszName cast=(PCWSTR)
 * @param ppidl cast=(PIDLIST_ABSOLUTE *)
 */
public static final native int PathToPIDL (char [] pszName, long [] ppidl);
/**
 * @param clsid flags=no_out
 * @param lplpszProgID cast=(LPOLESTR *)
 */
public static final native int ProgIDFromCLSID(GUID clsid, long[] lplpszProgID);
/**
 * @param hwnd cast=(HWND)
 * @param pDropTarget cast=(IDropTarget *)
 */
public static final native int RegisterDragDrop(long hwnd, long pDropTarget);
/** @param pmedium cast=(STGMEDIUM *) */
public static final native void ReleaseStgMedium(long pmedium);
/** @param hwnd cast=(HWND) */
public static final native int RevokeDragDrop(long hwnd);
/**
 * @param pszName cast=(PCWSTR),flags=no_out
 * @param pbc cast=(IBindCtx *)
 * @param riid flags=no_out
 * @param ppv cast=(void **)
 */
public static final native int SHCreateItemFromParsingName (char [] pszName, long pbc, GUID riid, long [] ppv);
/**
 * @param pwcsName cast=(const WCHAR *),flags=no_out
 * @param ppstgOpen cast=(IStorage **)
 */
public static final native int StgCreateDocfile(char[] pwcsName, int grfMode, int reserved, long[] ppstgOpen);
/** @param pInit cast=(BYTE *),flags=critical */
public static final native long SHCreateMemStream(byte[] pInit, int cbInit);
/** @param pwcsName cast=(const WCHAR *),flags=no_out */
public static final native int StgIsStorageFile(char[] pwcsName);
/**
 * @param pwcsName cast=(const WCHAR *),flags=no_out
 * @param pstgPriority cast=(IStorage *)
 * @param snbExclude cast=(SNB)
 * @param ppstgOpen cast=(IStorage **)
 */
public static final native int StgOpenStorage(char[] pwcsName, long pstgPriority, int grfMode, long snbExclude, int reserved, long[] ppstgOpen);
/** @param sz cast=(OLECHAR *),flags=no_out critical */
public static final native long SysAllocString(char [] sz);
/** @param sz cast=(OLECHAR *) */
public static final native long SysAllocStringLen(char [] sz, int ui);
/** @param bstr cast=(BSTR) */
public static final native void SysFreeString(long bstr);
/** @param bstr cast=(BSTR) */
public static final native int SysStringByteLen(long bstr);
/** @param bstr cast=(BSTR) */
public static final native int SysStringLen(long bstr);
/**
 * @param pvargDest cast=(VARIANTARG FAR* )
 * @param pvarSrc cast=(VARIANTARG FAR* )
 * @param vt cast=(VARTYPE)
 */
public static final native int VariantChangeType(long pvargDest, long pvarSrc, short wFlags, short vt);
/** @param pvarg cast=(VARIANTARG FAR* ) */
public static final native int VariantClear(long pvarg);
/** @param pvarg cast=(VARIANTARG FAR* ) */
public static final native void VariantInit(long pvarg);
/** @param pStg cast=(IStorage *) */
public static final native int WriteClassStg(long pStg, GUID rclsid);

/**
 * @method flags=dynamic
 * @param browserExecutableFolder flags=no_out
 * @param userDataFolder flags=no_out
 */
public static final native int CreateCoreWebView2EnvironmentWithOptions(char[] browserExecutableFolder, char[] userDataFolder, long environmentOptions, long environmentCreatedHandler);
/** @method flags=no_gen */
public static final native long CreateSwtWebView2Callback(ICoreWebView2SwtCallback handler);
/** @method flags=no_gen */
public static final native long CreateSwtWebView2Host(ICoreWebView2SwtHost host);
/** @method flags=no_gen */
public static final native long CreateSwtWebView2Options();

public static final native int VtblCall(int fnNumber, long ppVtbl);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, int arg2, long[] arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long arg1, int arg2, long[] arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, int arg1, int arg2, long[] arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, int arg1, int arg2, int[] arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, int[] arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, long[] arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long[] arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, TF_DISPLAYATTRIBUTE arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long arg1, long arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, long arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, int arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, long arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, long[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, PROPERTYKEY arg0, long arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, int[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, int[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, char[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, POINT arg2, int arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, int[] arg0, GUID arg1, long[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, POINT arg1, long arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, POINT arg0, int arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, int arg1, int arg2, int arg3, long[] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, long arg1, int arg2, int arg3, long[] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] arg0, long arg1, int arg2, int arg3, int arg4, long[] arg5);
public static final native int VtblCall(int fnNumber, long ppVtbl, char[] uri, char[] method, long l, char[] headers, long[] request);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, int arg1, long[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, long[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, int arg1, int[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, int arg1, long arg2, SIZE arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, GUID arg2, long arg3, long [] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, GUID arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, GUID arg1, long arg2, long arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, GUID arg1, int arg2, int arg3, DISPPARAMS arg4, long arg5, EXCEPINFO arg6, int[] arg7);
public static final native int VtblCall(int fnNumber, long ppVtbl, MSG arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, MSG arg1, long arg2, int arg3, long arg4, RECT arg5);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, SIZE arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, int arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, CAUUID arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, CONTROLINFO arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, FORMATETC arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, FORMATETC arg0, STGMEDIUM arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0, long[] arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0, GUID arg1, long[] arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0, long arg1, int arg2, int arg3, int[] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0, int arg1, int arg2, long arg3, long arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, GUID arg0, int arg1, OLECMD arg2, long arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, GUID arg1, GUID arg2, GUID arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, LICINFO arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, RECT arg0, long arg1, int arg2);
public static final native int VtblCall(int fnNumber, long ppVtbl, long arg0, long arg1, long arg2, long arg3, long arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, RECT arg0, RECT arg1);
public static final native int VtblCall(int fnNumber, long ppVtbl, RECT arg0);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long[] arg1, long[] arg2, int[] arg3, long[] arg4);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, long[] arg1, int arg2, int[] arg3);
public static final native int VtblCall(int fnNumber, long ppVtbl, int arg0, int arg1, int arg2, DISPPARAMS arg3, long arg4, EXCEPINFO arg5, long arg6);
public static final native int VtblCall(int fnNumber, long address, char[] arg0, char[] arg1, char[] arg2, char[] arg3, long[] arg4);
public static final native int VtblCall(int fnNumber, long address, double arg0);

/** @param arg0 flags=struct */
public static final native int VtblCall(int fnNumber, long ppVtbl, RECT arg0, long arg1, long arg2);
/** @param arg0 flags=struct */
public static final native int VtblCall_put_Bounds(int fnNumber, long ppVtbl, RECT arg0);

/** Accessibility constants */
public static final int CHILDID_SELF = 0;
public static final int CO_E_OBJNOTCONNECTED = 0x800401FD;
public static final int ROLE_SYSTEM_MENUBAR = 0x2;
public static final int ROLE_SYSTEM_SCROLLBAR = 0x3;
public static final int ROLE_SYSTEM_ALERT = 0x8;
public static final int ROLE_SYSTEM_WINDOW = 0x9;
public static final int ROLE_SYSTEM_CLIENT = 0xa;
public static final int ROLE_SYSTEM_MENUPOPUP = 0xb;
public static final int ROLE_SYSTEM_MENUITEM = 0xc;
public static final int ROLE_SYSTEM_TOOLTIP = 0xd;
public static final int ROLE_SYSTEM_DOCUMENT = 0xf;
public static final int ROLE_SYSTEM_DIALOG = 0x12;
public static final int ROLE_SYSTEM_GROUPING = 0x14;
public static final int ROLE_SYSTEM_SEPARATOR = 0x15;
public static final int ROLE_SYSTEM_TOOLBAR = 0x16;
public static final int ROLE_SYSTEM_STATUSBAR = 0x17;
public static final int ROLE_SYSTEM_TABLE = 0x18;
public static final int ROLE_SYSTEM_COLUMNHEADER = 0x19;
public static final int ROLE_SYSTEM_ROWHEADER = 0x1a;
public static final int ROLE_SYSTEM_COLUMN = 0x1b;
public static final int ROLE_SYSTEM_ROW = 0x1c;
public static final int ROLE_SYSTEM_CELL = 0x1d;
public static final int ROLE_SYSTEM_LINK = 0x1e;
public static final int ROLE_SYSTEM_LIST = 0x21;
public static final int ROLE_SYSTEM_LISTITEM = 0x22;
public static final int ROLE_SYSTEM_OUTLINE = 0x23;
public static final int ROLE_SYSTEM_OUTLINEITEM = 0x24;
public static final int ROLE_SYSTEM_PAGETAB = 0x25;
public static final int ROLE_SYSTEM_GRAPHIC = 0x28;
public static final int ROLE_SYSTEM_STATICTEXT = 0x29;
public static final int ROLE_SYSTEM_TEXT = 0x2a;
public static final int ROLE_SYSTEM_PUSHBUTTON = 0x2b;
public static final int ROLE_SYSTEM_CHECKBUTTON = 0x2c;
public static final int ROLE_SYSTEM_RADIOBUTTON = 0x2d;
public static final int ROLE_SYSTEM_COMBOBOX = 0x2e;
public static final int ROLE_SYSTEM_DROPLIST = 0x2f;
public static final int ROLE_SYSTEM_PROGRESSBAR = 0x30;
public static final int ROLE_SYSTEM_SLIDER = 0x33;
public static final int ROLE_SYSTEM_SPINBUTTON = 0x34;
public static final int ROLE_SYSTEM_ANIMATION = 0x36;
public static final int ROLE_SYSTEM_PAGETABLIST = 0x3c;
public static final int ROLE_SYSTEM_CLOCK = 0x3d;
public static final int ROLE_SYSTEM_SPLITBUTTON = 0x3e;

public static final int STATE_SYSTEM_NORMAL = 0;
public static final int STATE_SYSTEM_UNAVAILABLE = 0x1;
public static final int STATE_SYSTEM_SELECTED = 0x2;
public static final int STATE_SYSTEM_FOCUSED = 0x4;
public static final int STATE_SYSTEM_PRESSED = 0x8;
public static final int STATE_SYSTEM_CHECKED = 0x10;
public static final int STATE_SYSTEM_MIXED = 0x20;
public static final int STATE_SYSTEM_READONLY = 0x40;
public static final int STATE_SYSTEM_HOTTRACKED = 0x80;
public static final int STATE_SYSTEM_EXPANDED = 0x200;
public static final int STATE_SYSTEM_COLLAPSED = 0x400;
public static final int STATE_SYSTEM_BUSY = 0x800;
public static final int STATE_SYSTEM_INVISIBLE = 0x8000;
public static final int STATE_SYSTEM_OFFSCREEN = 0x10000;
public static final int STATE_SYSTEM_SIZEABLE = 0x20000;
public static final int STATE_SYSTEM_FOCUSABLE = 0x100000;
public static final int STATE_SYSTEM_SELECTABLE = 0x200000;
public static final int STATE_SYSTEM_LINKED = 0x400000;
public static final int STATE_SYSTEM_MULTISELECTABLE = 0x1000000;

public static final int EVENT_OBJECT_SELECTIONWITHIN = 0x8009;
public static final int EVENT_OBJECT_STATECHANGE = 0x800A;
public static final int EVENT_OBJECT_LOCATIONCHANGE = 0x800B;
public static final int EVENT_OBJECT_NAMECHANGE = 0x800C;
public static final int EVENT_OBJECT_DESCRIPTIONCHANGE = 0x800D;
public static final int EVENT_OBJECT_VALUECHANGE = 0x800E;
public static final int EVENT_OBJECT_TEXTSELECTIONCHANGED = 0x8014;

/* IA2 additional constants */
public static final int IA2_COORDTYPE_SCREEN_RELATIVE = 0;
public static final int IA2_COORDTYPE_PARENT_RELATIVE = 1;

public static final int IA2_STATE_ACTIVE = 0x00000001;
public static final int IA2_STATE_SINGLE_LINE = 0x00002000;
public static final int IA2_STATE_MULTI_LINE = 0x00000200;
public static final int IA2_STATE_REQUIRED = 0x00000800;
public static final int IA2_STATE_INVALID_ENTRY = 0x00000040;
public static final int IA2_STATE_SUPPORTS_AUTOCOMPLETION = 0x00008000;
public static final int IA2_STATE_EDITABLE = 0x00000008;

public static final int IA2_EVENT_DOCUMENT_LOAD_COMPLETE = 0x00000105;
public static final int IA2_EVENT_DOCUMENT_LOAD_STOPPED = 0x00000106;
public static final int IA2_EVENT_DOCUMENT_RELOAD = 0x00000107;
public static final int IA2_EVENT_PAGE_CHANGED = 0x00000111;
public static final int IA2_EVENT_SECTION_CHANGED = 0x00000112;
public static final int IA2_EVENT_ACTION_CHANGED = 0x00000101;
public static final int IA2_EVENT_HYPERLINK_START_INDEX_CHANGED = 0x0000010d;
public static final int IA2_EVENT_HYPERLINK_END_INDEX_CHANGED = 0x00000108;
public static final int IA2_EVENT_HYPERLINK_ANCHOR_COUNT_CHANGED = 0x00000109;
public static final int IA2_EVENT_HYPERLINK_SELECTED_LINK_CHANGED = 0x0000010a;
public static final int IA2_EVENT_HYPERLINK_ACTIVATED = 0x0000010b;
public static final int IA2_EVENT_HYPERTEXT_LINK_SELECTED = 0x0000010c;
public static final int IA2_EVENT_HYPERTEXT_LINK_COUNT_CHANGED = 0x0000010f;
public static final int IA2_EVENT_ATTRIBUTE_CHANGED = 0x00000110;
public static final int IA2_EVENT_TABLE_CAPTION_CHANGED = 0x00000113;
public static final int IA2_EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED = 0x00000114;
public static final int IA2_EVENT_TABLE_COLUMN_HEADER_CHANGED = 0x00000115;
public static final int IA2_EVENT_TABLE_CHANGED = 0x00000116;
public static final int IA2_EVENT_TABLE_ROW_DESCRIPTION_CHANGED = 0x00000117;
public static final int IA2_EVENT_TABLE_ROW_HEADER_CHANGED = 0x00000118;
public static final int IA2_EVENT_TABLE_SUMMARY_CHANGED = 0x00000119;
public static final int IA2_EVENT_TEXT_ATTRIBUTE_CHANGED = 0x0000011a;
public static final int IA2_EVENT_TEXT_CARET_MOVED = 0x0000011b;
public static final int IA2_EVENT_TEXT_COLUMN_CHANGED = 0x0000011d;
public static final int IA2_EVENT_TEXT_INSERTED = 0x0000011e;
public static final int IA2_EVENT_TEXT_REMOVED = 0x0000011f;

public static final int IA2_TEXT_BOUNDARY_CHAR = 0;
public static final int IA2_TEXT_BOUNDARY_WORD = 1;
public static final int IA2_TEXT_BOUNDARY_SENTENCE = 2;
public static final int IA2_TEXT_BOUNDARY_PARAGRAPH = 3;
public static final int IA2_TEXT_BOUNDARY_LINE = 4;
public static final int IA2_TEXT_BOUNDARY_ALL = 5;

public static final int IA2_TEXT_OFFSET_LENGTH = -1;
public static final int IA2_TEXT_OFFSET_CARET =  -2;

public static final int IA2_SCROLL_TYPE_TOP_LEFT = 0;
public static final int IA2_SCROLL_TYPE_BOTTOM_RIGHT = 1;
public static final int IA2_SCROLL_TYPE_TOP_EDGE = 2;
public static final int IA2_SCROLL_TYPE_BOTTOM_EDGE = 3;
public static final int IA2_SCROLL_TYPE_LEFT_EDGE = 4;
public static final int IA2_SCROLL_TYPE_RIGHT_EDGE = 5;
public static final int IA2_SCROLL_TYPE_ANYWHERE = 6;

/** Accessibility natives */

/**
 * @param hwnd cast=(HWND)
 * @param riidInterface flags=no_out
 * @param ppvObject cast=(LPVOID *)
 */
public static final native int CreateStdAccessibleObject (long hwnd, int idObject, GUID riidInterface, long[] ppvObject);
/**
 * @param riid flags=no_out
 * @param pAcc cast=(LPUNKNOWN)
 */
public static final native long LresultFromObject (GUID riid, long wParam, long pAcc);

/* sizeof's */
public static final native int CAUUID_sizeof();
public static final native int CONTROLINFO_sizeof();
public static final native int DISPPARAMS_sizeof();
public static final native int ELEMDESC_sizeof();
public static final native int EXCEPINFO_sizeof();
public static final native int FORMATETC_sizeof();
public static final native int FUNCDESC_sizeof();
public static final native int GUID_sizeof();
public static final native int LICINFO_sizeof();
public static final native int OLECMD_sizeof();
public static final native int OLEINPLACEFRAMEINFO_sizeof();
public static final native int STGMEDIUM_sizeof();
public static final native int TYPEATTR_sizeof();
public static final native int TYPEDESC_sizeof();
public static final native int VARDESC_sizeof();
public static final native int VARIANT_sizeof();

}
