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
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

import org.eclipse.swt.internal.*;

public class XPCOM {
	
	/* Mozilla version */
	static final int MAJOR = 1;
	static final int MINOR = 4;
	static final String SUPPORTED_VERSION = MAJOR + "." + MINOR; //$NON-NLS-1$
		
	static {
		/* Load the SWT Mozilla library */
		Library.loadLibrary ("swt-mozilla"); //$NON-NLS-1$
	}

	/* NsWidgetCID constants */
	public static final nsID NS_APPSHELL_CID =	new nsID("2d96b3df-c051-11d1-a827-0040959a28c9"); //$NON-NLS-1$

	/* XPCOM constants */
	public static final int NS_OK =  0;
	public static final int NS_COMFALSE = 1;
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

	static final String NS_ERROR_NOT_INITIALIZED_MSG = "Not initialized."; //$NON-NLS-1$
	static final String NS_ERROR_ALREADY_INITIALIZED_MSG = "Already initialized."; //$NON-NLS-1$
	static final String NS_ERROR_NOT_IMPLEMENTED_MSG = "Not implemented."; //$NON-NLS-1$
	static final String NS_NOINTERFACE_MSG = "No interface."; //$NON-NLS-1$
	static final String NS_ERROR_NO_INTERFACE_MSG = NS_NOINTERFACE_MSG;
	static final String NS_ERROR_INVALID_POINTER_MSG = "Invalid pointer."; //$NON-NLS-1$
	static final String NS_ERROR_ABORT_MSG = "Abort."; //$NON-NLS-1$
	static final String NS_ERROR_FAILURE_MSG = "Failure."; //$NON-NLS-1$
	static final String NS_ERROR_UNEXPECTED_MSG = "Unexpected."; //$NON-NLS-1$
	static final String NS_ERROR_OUT_OF_MEMORY_MSG = "Out of memory."; //$NON-NLS-1$
	static final String NS_ERROR_ILLEGAL_VALUE_MSG = "Illegal value."; //$NON-NLS-1$
	static final String NS_ERROR_NO_AGGREGATION_MSG = "No aggregation."; //$NON-NLS-1$
	static final String NS_ERROR_NOT_AVAILABLE_MSG = "Not available."; //$NON-NLS-1$
	static final String NS_ERROR_FACTORY_NOT_REGISTERED_MSG = "Factory not registered."; //$NON-NLS-1$
	static final String NS_ERROR_FACTORY_REGISTER_AGAIN_MSG = "Factory already registered."; //$NON-NLS-1$
	static final String NS_ERROR_FACTORY_NOT_LOADED_MSG = "Factory not loaded."; //$NON-NLS-1$
	static final String NS_ERROR_FACTORY_NO_SIGNATURE_SUPPORT_MSG = "Factory no signature support."; //$NON-NLS-1$
	static final String NS_ERROR_FACTORY_EXISTS_MSG = "Factory already exists."; //$NON-NLS-1$
	
	
public static String errorMsg (int code) {
	String msg = "XPCOM error ";
	switch (code) {
		case NS_ERROR_NOT_INITIALIZED : msg += NS_ERROR_NOT_INITIALIZED_MSG; break;
		case NS_ERROR_ALREADY_INITIALIZED : msg += NS_ERROR_ALREADY_INITIALIZED_MSG; break;
		case NS_ERROR_NOT_IMPLEMENTED : msg += NS_ERROR_NOT_IMPLEMENTED_MSG; break;
		case NS_ERROR_NO_INTERFACE : msg += NS_ERROR_NO_INTERFACE_MSG; break;
		case NS_ERROR_INVALID_POINTER : msg += NS_ERROR_INVALID_POINTER_MSG; break;
		case NS_ERROR_ABORT : msg += NS_ERROR_ABORT_MSG; break;
		case NS_ERROR_FAILURE : msg += NS_ERROR_FAILURE_MSG; break;
		case NS_ERROR_UNEXPECTED : msg += NS_ERROR_UNEXPECTED_MSG; break;
		case NS_ERROR_OUT_OF_MEMORY : msg += NS_ERROR_OUT_OF_MEMORY_MSG; break;
		case NS_ERROR_ILLEGAL_VALUE : msg += NS_ERROR_ILLEGAL_VALUE_MSG; break;
		case NS_ERROR_NO_AGGREGATION : msg += NS_ERROR_NO_AGGREGATION_MSG; break;
		case NS_ERROR_NOT_AVAILABLE : msg += NS_ERROR_NOT_AVAILABLE_MSG; break;
		case NS_ERROR_FACTORY_NOT_REGISTERED : msg += NS_ERROR_FACTORY_NOT_REGISTERED_MSG; break;
		case NS_ERROR_FACTORY_REGISTER_AGAIN : msg += NS_ERROR_FACTORY_REGISTER_AGAIN_MSG; break;
		case NS_ERROR_FACTORY_NOT_LOADED : msg += NS_ERROR_FACTORY_NOT_LOADED_MSG; break;
		case NS_ERROR_FACTORY_NO_SIGNATURE_SUPPORT : msg += NS_ERROR_FACTORY_NO_SIGNATURE_SUPPORT_MSG; break;
		case NS_ERROR_FACTORY_EXISTS : msg += NS_ERROR_FACTORY_EXISTS_MSG; break;
		default: msg += "("+code+")"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	return msg;
}

public static final native void memmove(nsID dest, int src, int nbytes);
public static final native void memmove(int dest, nsID src, int nbytes);
public static final native void memmove(int dest, int[] src, int nbytes);
public static final native void memmove(int[] dest, int src, int nbytes);
public static final native void memmove(byte[] dest, int src, int nbytes);
public static final native void memmove(char[] dest, int src, int nbytes);
public static final native int NS_GetComponentManager(int[] result);
public static final native int NS_GetServiceManager(int[] result);
public static final native int NS_InitEmbedding(int aMozBinDirectory, int aAppFileLocProvider);
public static final native int NS_NewLocalFile(String path, boolean followLinks, int[] nsILocalFile);
public static final native int NS_NewSingletonEnumerator(int localFile,int[] enum);
public static final native int NS_TermEmbedding();
public static final native int nsCRT_strlen_PRUnichar(int s);
public static final native int nsCString_new();	
public static final native void nsCString_delete(int ptr);
public static final native int nsCString_Length(int ptr);
public static final native int nsCString_get(int ptr);
public static final native void nsID_delete(int ptr);
public static final native int nsID_new();
public static final native boolean nsID_Parse(int ptr, String aIDStr);
public static final native boolean nsID_Equals(int ptr, int other);
public static final native int nsRect_new(int aX, int aY, int aWidth, int aHeight);
public static final native void nsRect_delete(int ptr);
public static final native int nsString_new();
public static final native int nsString_new(char[] aString);
public static final native void nsString_delete(int ptr);
public static final native int nsString_Length(int ptr);
public static final native int nsString_get(int ptr);
public static final native void PR_Free(int ptr);
public static final native String PR_GetEnv(String name); 
public static final native int PR_Malloc(int Length);
public static final native int strlen(int s);

static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0, byte[] arg1, byte[] arg2, int arg3);
static final native int VtblCall(int fnNumber, int ppVtbl);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, byte[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0);
static final native void VtblCallNoRet(int fnNumber, int ppVtbl, int arg0);
static final native void VtblCallNoRet(int fnNumber, int ppVtbl, int arg0, int arg1);
static final native void VtblCallNoRet(int fnNumber, int ppVtbl, int[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, nsID arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, int arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5);
static final native int VtblCall(int fnNumber, int ppVtbl, int[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1,int arg2, int arg3, boolean arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int[] arg0, int[] arg1,int[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, boolean[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0,boolean[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, boolean[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, int arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, int arg1, int arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0,boolean[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0,boolean arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, long arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, float arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, double arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, long[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, double[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, short[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, float[] arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, short arg0, float arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, short arg0, float[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, short arg0, int arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean arg2,int arg3, int arg4, int arg5,int arg6, int arg7, int arg8, boolean arg9, boolean arg10, boolean arg11,boolean arg12, short arg13, int arg14);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean arg2, int arg3, int arg4, int arg5, int arg6, short arg7);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, short[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, short[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, byte[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, char[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0,int arg1,boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0,int arg1,boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0,boolean arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0,boolean arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, nsID arg1, int arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, nsID arg1, int arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1,boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, byte[] arg1,boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1,int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1,char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, byte[] arg1,char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1,char[] arg2, boolean arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int[] arg0, int arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, boolean arg1, boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean[] arg0, boolean[] arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, boolean arg2, int arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, boolean arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1, char[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean arg2, int arg3, boolean arg4, boolean arg5, boolean arg6, boolean arg7, int arg8, int arg9);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, int[] arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1, int arg2, char[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean  arg2, int arg3, int arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6, boolean[] arg7);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2, int arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int[] arg1, int[] arg2, int[] arg3, int[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, char[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, char[] arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2, char[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, boolean arg3, boolean[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, boolean arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, boolean arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, boolean[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, int arg2, char[] arg3, int[] arg4, boolean[] arg5);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2, boolean[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, int arg2, char[] arg3, char[] arg4, char[] arg5, char[] arg6, boolean[] arg7, int[] arg8);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, char[] arg1, char[] arg2, boolean[] arg3, boolean[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, nsID arg1, boolean arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, char[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, char[] arg0, int arg1, int arg2, int arg3, int arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, boolean arg1, int arg2, boolean[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, boolean arg1);
static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0, nsID arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0, int arg1, nsID arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, int arg1, nsID arg2, int[] arg3);
static final native int VtblCall(int fnNumber, int ppVtbl, nsID arg0, nsID arg1, boolean[] arg2);	
static final native int VtblCall(int fnNumber, int ppVtbl, byte[] arg0, nsID arg1, boolean[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, int[] arg1, int[] arg2);
static final native int VtblCall(int fnNumber, int ppVtbl, int arg0, short arg1, short arg2, short arg3, boolean[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, short arg1, short arg2, short arg3, boolean[] arg4);
static final native int VtblCall(int fnNumber, int ppVtbl, boolean arg0, int arg1, boolean[] arg2);

}


