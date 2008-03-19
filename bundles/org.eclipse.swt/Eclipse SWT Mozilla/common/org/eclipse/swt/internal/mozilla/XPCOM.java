/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

import org.eclipse.swt.internal.*;

public class XPCOM extends C {
	public static final String MOZILLA_FIVE_HOME = "MOZILLA_FIVE_HOME"; //$NON-NLS-1$
	public static final String MOZILLA_PLUGIN_PATH = "MOZ_PLUGIN_PATH"; //$NON-NLS-1$
	public static final String CONTENT_MAYBETEXT = "application/x-vnd.mozilla.maybe-text"; //$NON-NLS-1$
	public static final String CONTENT_MULTIPART = "multipart/x-mixed-replace"; //$NON-NLS-1$
	public static final String DOMEVENT_FOCUS = "focus"; //$NON-NLS-1$
	public static final String DOMEVENT_UNLOAD = "unload"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEDOWN = "mousedown"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEUP = "mouseup"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEMOVE = "mousemove"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEDRAG = "draggesture"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEWHEEL = "DOMMouseScroll"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEOVER = "mouseover"; //$NON-NLS-1$
	public static final String DOMEVENT_MOUSEOUT = "mouseout"; //$NON-NLS-1$
	public static final String DOMEVENT_KEYUP = "keyup"; //$NON-NLS-1$
	public static final String DOMEVENT_KEYDOWN = "keydown"; //$NON-NLS-1$
	public static final String DOMEVENT_KEYPRESS = "keypress"; //$NON-NLS-1$
	
	/* CID constants */
	public static final nsID NS_APPSHELL_CID =	new nsID("2d96b3df-c051-11d1-a827-0040959a28c9"); //$NON-NLS-1$
	public static final nsID NS_CATEGORYMANAGER_CID = new nsID("16d222a6-1dd2-11b2-b693-f38b02c021b2"); //$NON-NLS-1$
	public static final nsID NS_DOWNLOAD_CID = new nsID("e3fa9D0a-1dd1-11b2-bdef-8c720b597445"); //$NON-NLS-1$
	public static final nsID NS_FILEPICKER_CID = new nsID("54ae32f8-1dd2-11b2-a209-df7c505370f8"); //$NON-NLS-1$
	public static final nsID NS_HELPERAPPLAUNCHERDIALOG_CID = new nsID("f68578eb-6ec2-4169-ae19-8c6243f0abe1"); //$NON-NLS-1$
	public static final nsID NS_INPUTSTREAMCHANNEL_CID = new nsID("6ddb050c-0d04-11d4-986e-00c04fa0cf4a"); //$NON-NLS-1$
	public static final nsID NS_IOSERVICE_CID =	new nsID("9ac9e770-18bc-11d3-9337-00104ba0fd40"); //$NON-NLS-1$
	public static final nsID NS_LOADGROUP_CID = new nsID("e1c61582-2a84-11d3-8cce-0060b0fc14a3"); //$NON-NLS-1$
	public static final nsID NS_PROMPTSERVICE_CID = new nsID("a2112d6a-0e28-421f-b46a-25c0b308cbd0"); //$NON-NLS-1$

	public static final String NS_CONTEXTSTACK_CONTRACTID = "@mozilla.org/js/xpc/ContextStack;1"; //$NON-NLS-1$
	public static final String NS_COOKIEMANAGER_CONTRACTID = "@mozilla.org/cookiemanager;1"; //$NON-NLS-1$
	public static final String NS_DIRECTORYSERVICE_CONTRACTID = "@mozilla.org/file/directory_service;1"; //$NON-NLS-1$
	public static final String NS_DOMSERIALIZER_CONTRACTID = "@mozilla.org/xmlextras/xmlserializer;1"; //$NON-NLS-1$
	public static final String NS_DOWNLOAD_CONTRACTID = "@mozilla.org/download;1"; //$NON-NLS-1$
	public static final String NS_FILEPICKER_CONTRACTID = "@mozilla.org/filepicker;1"; //$NON-NLS-1$
	public static final String NS_HELPERAPPLAUNCHERDIALOG_CONTRACTID = "@mozilla.org/helperapplauncherdialog;1"; //$NON-NLS-1$
	public static final String NS_MEMORY_CONTRACTID = "@mozilla.org/xpcom/memory-service;1"; //$NON-NLS-1$
	public static final String NS_OBSERVER_CONTRACTID = "@mozilla.org/observer-service;1"; //$NON-NLS-1$
	public static final String NS_PREFLOCALIZEDSTRING_CONTRACTID = "@mozilla.org/pref-localizedstring;1"; //$NON-NLS-1$
	public static final String NS_PREFSERVICE_CONTRACTID = "@mozilla.org/preferences-service;1"; //$NON-NLS-1$
	public static final String NS_PROMPTSERVICE_CONTRACTID = "@mozilla.org/embedcomp/prompt-service;1"; //$NON-NLS-1$
	public static final String NS_TRANSFER_CONTRACTID = "@mozilla.org/transfer;1"; //$NON-NLS-1$
	public static final String NS_WEBNAVIGATIONINFO_CONTRACTID = "@mozilla.org/webnavigation-info;1"; //$NON-NLS-1$
	public static final String NS_WINDOWWATCHER_CONTRACTID = "@mozilla.org/embedcomp/window-watcher;1"; //$NON-NLS-1$

	/* directory service constants */
	public static final String NS_APP_APPLICATION_REGISTRY_DIR = "AppRegD"; //$NON-NLS-1$
	public static final String NS_APP_CACHE_PARENT_DIR = "cachePDir"; //$NON-NLS-1$
	public static final String NS_APP_HISTORY_50_FILE = "UHist"; //$NON-NLS-1$
	public static final String NS_APP_LOCALSTORE_50_FILE = "LclSt"; //$NON-NLS-1$
	public static final String NS_APP_PLUGINS_DIR_LIST = "APluginsDL"; //$NON-NLS-1$
	public static final String NS_APP_PREF_DEFAULTS_50_DIR = "PrfDef"; //$NON-NLS-1$
	public static final String NS_APP_PREFS_50_DIR = "PrefD"; //$NON-NLS-1$
	public static final String NS_APP_PREFS_50_FILE = "PrefF"; //$NON-NLS-1$
	public static final String NS_APP_USER_CHROME_DIR = "UChrm"; //$NON-NLS-1$
	public static final String NS_APP_USER_MIMETYPES_50_FILE = "UMimTyp"; //$NON-NLS-1$
	public static final String NS_APP_USER_PROFILE_50_DIR = "ProfD"; //$NON-NLS-1$
	public static final String NS_GRE_COMPONENT_DIR = "GreComsD"; //$NON-NLS-1$
	public static final String NS_GRE_DIR = "GreD"; //$NON-NLS-1$
	public static final String NS_OS_CURRENT_PROCESS_DIR = "CurProcD"; //$NON-NLS-1$
	public static final String NS_OS_HOME_DIR = "Home"; //$NON-NLS-1$
	public static final String NS_OS_TEMP_DIR = "TmpD"; //$NON-NLS-1$
	public static final String NS_XPCOM_COMPONENT_DIR = "ComsD"; //$NON-NLS-1$
	public static final String NS_XPCOM_CURRENT_PROCESS_DIR = "XCurProcD"; //$NON-NLS-1$
	public static final String NS_XPCOM_INIT_CURRENT_PROCESS_DIR = "MozBinD"; //$NON-NLS-1$

	/* XPCOM constants */
	public static final int NS_OK =  0;
	public static final int NS_COMFALSE = 1;
	public static final int NS_BINDING_ABORTED = 0x804B0002;
	public static final int NS_ERROR_BASE = 0xc1f30000;
	public static final int NS_ERROR_NOT_INITIALIZED =  NS_ERROR_BASE + 1;
	public static final int NS_ERROR_ALREADY_INITIALIZED = NS_ERROR_BASE + 2;
	public static final int NS_ERROR_NOT_IMPLEMENTED =  0x80004001;
	public static final int NS_NOINTERFACE =  0x80004002;
	public static final int NS_ERROR_NO_INTERFACE =  NS_NOINTERFACE;
	public static final int NS_ERROR_INVALID_POINTER =  0x80004003;
	public static final int NS_ERROR_NULL_POINTER = NS_ERROR_INVALID_POINTER;
	public static final int NS_ERROR_ABORT = 0x80004004;
	public static final int NS_ERROR_FAILURE = 0x80004005;
	public static final int NS_ERROR_UNEXPECTED = 0x8000ffff;
	public static final int NS_ERROR_OUT_OF_MEMORY = 0x8007000e;
	public static final int NS_ERROR_ILLEGAL_VALUE = 0x80070057;
	public static final int NS_ERROR_INVALID_ARG = NS_ERROR_ILLEGAL_VALUE;
	public static final int NS_ERROR_NO_AGGREGATION = 0x80040110;
	public static final int NS_ERROR_NOT_AVAILABLE = 0x80040111;
	public static final int NS_ERROR_FACTORY_NOT_REGISTERED = 0x80040154;
	public static final int NS_ERROR_FACTORY_REGISTER_AGAIN = 0x80040155;
	public static final int NS_ERROR_FACTORY_NOT_LOADED = 0x800401f8;
	public static final int NS_ERROR_FACTORY_NO_SIGNATURE_SUPPORT = NS_ERROR_BASE + 0x101;
	public static final int NS_ERROR_FACTORY_EXISTS = NS_ERROR_BASE + 0x100;
	public static final int NS_ERROR_HTMLPARSER_UNRESOLVEDDTD = 0x804e03f3;
	public static final int NS_ERROR_FILE_NOT_FOUND = 0x80520012;
	public static final int NS_ERROR_FILE_UNRECOGNIZED_PATH = 0x80520001;

public static final native void memmove(nsID dest, int /*long*/ src, int nbytes);
public static final native void memmove(int /*long*/ dest, nsID src, int nbytes);
public static final native int NS_GetComponentManager(int /*long*/[] result);
public static final native int NS_GetServiceManager(int /*long*/[] result);
public static final native int NS_InitXPCOM2(int /*long*/ result, int /*long*/ binDirectory, int /*long*/ appFileLocationProvider);
public static final native int NS_NewLocalFile(int /*long*/ path, int followLinks, int /*long*/[] result);
public static final native int strlen_PRUnichar(int /*long*/ s);
public static final native int /*long*/ nsEmbedCString_new();
public static final native int /*long*/ nsEmbedCString_new(byte[] aString, int length);
public static final native int /*long*/ nsEmbedCString_new(int aString, int length);
public static final native void nsEmbedCString_delete(int /*long*/ ptr);
public static final native int nsEmbedCString_Length(int /*long*/ ptr);
public static final native int /*long*/ nsEmbedCString_get(int /*long*/ ptr);
public static final native void nsID_delete(int /*long*/ ptr);
public static final native int /*long*/ nsID_new();
public static final native int nsID_Equals(int /*long*/ ptr, int /*long*/ other);
public static final native int /*long*/ nsEmbedString_new();
public static final native int /*long*/ nsEmbedString_new(char[] aString);
public static final native void nsEmbedString_delete(int /*long*/ ptr);
public static final native int nsEmbedString_Length(int /*long*/ ptr);
public static final native int /*long*/ nsEmbedString_get(int /*long*/ ptr);
public static final native int XPCOMGlueStartup(byte[] place);
public static final native int XPCOMGlueShutdown();

public static final native int /*long*/ Call(int /*long*/ ptr, int /*long*/ aInStream, int /*long*/ aClosure, byte[] aFromSegment, int aToOffset, int aCount, int[] aWriteCount);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, float arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, float[] arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long[] arg0);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, short[] arg0);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long [] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, long[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, long arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, nsID arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, nsID arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, long[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, int arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, long arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, long[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0, char[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, byte[] arg1);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, int[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long [] arg0, long [] arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, long[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, long[] arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, nsID arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0, int arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0, long arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, long arg1, long arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, long[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, nsID arg1, long [] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, nsID arg1, long[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1, long[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int[] arg1, long[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, long[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, nsID arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, nsID arg1, long arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, byte[] arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, byte[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, byte[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1, int arg2);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, byte[] arg1, int arg2);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, nsID arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, long arg1, nsID arg2, long[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1, int arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, byte[] arg1, long arg2, long[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, long [] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, char[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2, char[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2, int arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, int arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int[] arg0, int[] arg1, int[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, byte[] arg1, byte[] arg2, int arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, byte[] arg1, byte[] arg2, long arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, int arg1, nsID arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, long arg1, nsID arg2, long [] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, int[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, long [] arg2, int[] arg3);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0, int arg1, int arg2, int arg3, int arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, char[] arg0, int arg1, long arg2, long arg3, long arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, long arg3, long arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int[] arg1, int[] arg2, int[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, char[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, char[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, int arg2, int[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, int arg1, long arg2, long [] arg3, int[] arg4);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, byte[] arg1, byte[] arg2, byte[] arg3, int arg4, int[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, byte[] arg1, byte[] arg2, byte[] arg3, long arg4, long [] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, char[] arg2, int arg3, long arg4, int arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, char[] arg2, int arg3, long arg4, int arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, char[] arg2, long arg3, long arg4, long arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, char[] arg2, long arg3, long arg4, long arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2, int arg3, int arg4, int arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, byte[] arg1, byte[] arg2, int arg3, byte[] arg4, byte[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, nsID arg0, byte[] arg1, byte[] arg2, long arg3, byte[] arg4, byte[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, long arg2, long arg3, long arg4, long arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, char[] arg3, int[] arg4, int[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, char[] arg3, int[] arg4, int[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4, int[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4, long[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int[] arg4, int[] arg5);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int arg1, int arg2, long arg3, int[] arg4, long [] arg5);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, int arg3, int[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, int arg3, long[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, int[] arg3, char[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, long[] arg3, char[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, char[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, int arg2, long arg3, char[] arg4, int[] arg5, int[] arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, long arg4, int arg5, int arg6);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6);

static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, int[] arg3, int[] arg4, char[] arg5, int[] arg6, int[] arg7);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, long[] arg3, long [] arg4, char[] arg5, int[] arg6, int[] arg7);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, char[] arg6, int[] arg7, int[] arg8);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, long arg3, int arg4, long arg5, char[] arg6, int[] arg7, long [] arg8);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int arg1, int arg2, long arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, char[] arg1, char[] arg2, int arg3, char[] arg4, char[] arg5, char[] arg6, char[] arg7, int[] arg8, int[] arg9);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, char[] arg1, char[] arg2, int arg3, char[] arg4, char[] arg5, char[] arg6, char[] arg7, int[] arg8, int[] arg9);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, char[] arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int[] arg10, int[] arg11);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, int arg3, char[] arg4, long arg5, long arg6, int arg7, long arg8, int arg9, long [] arg10, long [] arg11);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, char[] arg4, byte[] arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int[] arg11, int[] arg12);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, long arg1, long arg2, int arg3, char[] arg4, byte[] arg5, long arg6, long arg7, int arg8, long arg9, int arg10, long [] arg11, long [] arg12);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, short arg13, int arg14);
static final native int VtblCall(int fnNumber, int /*long*/ ppVtbl, long arg0, int arg1, int arg2, long arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, short arg13, long arg14);
}
