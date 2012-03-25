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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIXPCScriptable extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 22;

	public static final String NS_IXPCSCRIPTABLE_IID_STR =
		"a40ce52e-2d8c-400f-9af2-f8784a656070";

	public static final nsID NS_IXPCSCRIPTABLE_IID =
		new nsID(NS_IXPCSCRIPTABLE_IID_STR);

	public nsIXPCScriptable(int /*long*/ address) {
		super(address);
	}

	public static final int WANT_PRECREATE = 1;

	public static final int WANT_CREATE = 2;

	public static final int WANT_POSTCREATE = 4;

	public static final int WANT_ADDPROPERTY = 8;

	public static final int WANT_DELPROPERTY = 16;

	public static final int WANT_GETPROPERTY = 32;

	public static final int WANT_SETPROPERTY = 64;

	public static final int WANT_ENUMERATE = 128;

	public static final int WANT_NEWENUMERATE = 256;

	public static final int WANT_NEWRESOLVE = 512;

	public static final int WANT_CONVERT = 1024;

	public static final int WANT_FINALIZE = 2048;

	public static final int WANT_CHECKACCESS = 4096;

	public static final int WANT_CALL = 8192;

	public static final int WANT_CONSTRUCT = 16384;

	public static final int WANT_HASINSTANCE = 32768;

	public static final int WANT_TRACE = 65536;

	public static final int USE_JSSTUB_FOR_ADDPROPERTY = 131072;

	public static final int USE_JSSTUB_FOR_DELPROPERTY = 262144;

	public static final int USE_JSSTUB_FOR_SETPROPERTY = 524288;

	public static final int DONT_ENUM_STATIC_PROPS = 1048576;

	public static final int DONT_ENUM_QUERY_INTERFACE = 2097152;

	public static final int DONT_ASK_INSTANCE_FOR_SCRIPTABLE = 4194304;

	public static final int CLASSINFO_INTERFACES_ONLY = 8388608;

	public static final int ALLOW_PROP_MODS_DURING_RESOLVE = 16777216;

	public static final int ALLOW_PROP_MODS_TO_PROTOTYPE = 33554432;

	public static final int DONT_SHARE_PROTOTYPE = 67108864;

	public static final int DONT_REFLECT_INTERFACE_NAMES = 134217728;

	public static final int WANT_EQUALITY = 268435456;

	public static final int WANT_OUTER_OBJECT = 536870912;

	public static final int USE_STUB_EQUALITY_HOOK = 1073741824;

	public static final int RESERVED = 2147483648;

	public int GetClassName(int /*long*/[] aClassName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aClassName);
	}

	public int GetScriptableFlags(int[] aScriptableFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aScriptableFlags);
	}

	public int PreCreate(int /*long*/ nativeObj, int /*long*/ cx, int /*long*/ globalObj, int /*long*/[] parentObj) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), nativeObj, cx, globalObj, parentObj);
	}

	public int Create(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), wrapper, cx, obj);
	}

	public int PostCreate(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), wrapper, cx, obj);
	}

	public int AddProperty(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), wrapper, cx, obj, id, vp, _retval);
	}

	public int DelProperty(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), wrapper, cx, obj, id, vp, _retval);
	}

	public int GetProperty(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), wrapper, cx, obj, id, vp, _retval);
	}

	public int SetProperty(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), wrapper, cx, obj, id, vp, _retval);
	}

	public int Enumerate(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), wrapper, cx, obj, _retval);
	}

	public int NewEnumerate(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int enum_op, int /*long*/ statep, int /*long*/ idp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), wrapper, cx, obj, enum_op, statep, idp, _retval);
	}

	public int NewResolve(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int flags, int /*long*/[] objp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), wrapper, cx, obj, id, flags, objp, _retval);
	}

	public int Convert(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int type, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), wrapper, cx, obj, type, vp, _retval);
	}

	public int Finalize(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), wrapper, cx, obj);
	}

	public int CheckAccess(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, !ERROR UNKNOWN C TYPE <jsid >! id, int mode, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), wrapper, cx, obj, id, mode, vp, _retval);
	}

	public int Call(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int argc, int /*long*/ argv, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), wrapper, cx, obj, argc, argv, vp, _retval);
	}

	public int Construct(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int argc, int /*long*/ argv, int /*long*/ vp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), wrapper, cx, obj, argc, argv, vp, _retval);
	}

	public int HasInstance(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int /*long*/ val, int /*long*/ bp, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), wrapper, cx, obj, val, bp, _retval);
	}

	public int Trace(int /*long*/ wrapper, int /*long*/ trc, int /*long*/ obj) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), wrapper, trc, obj);
	}

	public int Equality(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int /*long*/ val, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), wrapper, cx, obj, val, _retval);
	}

	public int OuterObject(int /*long*/ wrapper, int /*long*/ cx, int /*long*/ obj, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), wrapper, cx, obj, _retval);
	}

	public int PostCreatePrototype(int /*long*/ cx, int /*long*/ proto) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), cx, proto);
	}
}