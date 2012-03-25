/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.UnsupportedEncodingException;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;

class External {
	public static final String EXTERNAL_IID_STR =
		"ded01d20-ba6f-11dd-ad8b-0800200c9a66"; //$NON-NLS-1$

	public static final nsID EXTERNAL_IID =
		new nsID(EXTERNAL_IID_STR);

	XPCOMObject supports;
	XPCOMObject external;
	XPCOMObject classInfo;
	XPCOMObject securityCheckedComponent;
	XPCOMObject scriptObjectOwner;
	XPCOMObject xpcScriptable;
	int refCount = 0;
	
	static Callback CallJavaProc;
	
	static {
		CallJavaProc = new Callback (External.class, "CallJava", 3); //$NON-NLS-1$
		if (CallJavaProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	}

External () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

static int /*long*/ CallJava (int /*long*/ cx, int /*long*/ argc, int /*long*/ vp) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	//byte[] aContractID = MozillaDelegate.wcsToMbcs (null, "@mozilla.org/js/xpc/XPConnect;1", true);
	nsID NS_IXPCONNECT_CID = new nsID ("CB6593E0-F9B2-11d2-BDD6-000064657374");
	nsID NS_IXPCONNECT_IID = new nsID ("a995b541-d514-43f1-ac0e-f49746c0b063");
	rc = serviceManager.GetService(NS_IXPCONNECT_CID, NS_IXPCONNECT_IID, result);
	nsIXPConnect connect = new nsIXPConnect(result[0]);
	result[0] = 0;
	
	
	//int /*long*/[] result = new int /*long*/[1];
	XPCOM.memmove(result, vp + 2 * C.PTR_SIZEOF, C.PTR_SIZEOF);
	int /*long*/ arrayPtr = result[0];
	result[0] = 0;
	
	XPCOM.memmove(result, arrayPtr, C.PTR_SIZEOF);
	int /*long*/ args = result[0];
	result[0] = 0;

	int /*long*/ asdf4 = XPCOM.JSVAL_TO_IMPL2(Mozilla.jsLibPathBytes, arrayPtr);
	System.out.println(asdf4);
	asdf4 = XPCOM.JSVAL_TO_IMPL2(Mozilla.jsLibPathBytes, args);
	System.out.println(asdf4);

	int /*long*/ type2 = XPCOM.JS_TypeOfValue(Mozilla.jsLibPathBytes, cx, args);
	System.out.println(type2);
	int /*long*/ stringPtr = XPCOM.JS_ValueToString(Mozilla.jsLibPathBytes, cx, args);
	int length  = (int)XPCOM.JS_GetStringLength(Mozilla.jsLibPathBytes, stringPtr);
	int /*long*/ nativePtr = XPCOM.JS_GetStringCharsZ(Mozilla.jsLibPathBytes, cx, stringPtr);
	char[] dest = new char[length];
	XPCOM.memmove (dest, nativePtr, length * 2);
	System.out.println(new String (dest));
	
	
	args += C.PTR_SIZEOF;
	type2 = XPCOM.JS_TypeOfValue(Mozilla.jsLibPathBytes, cx, args);
	System.out.println(type2);
	stringPtr = XPCOM.JS_ValueToString(Mozilla.jsLibPathBytes, cx, args);
	length  = (int)XPCOM.JS_GetStringLength(Mozilla.jsLibPathBytes, stringPtr);
	nativePtr = XPCOM.JS_GetStringCharsZ(Mozilla.jsLibPathBytes, cx, stringPtr);
	dest = new char[length];
	XPCOM.memmove (dest, nativePtr, length * 2);
	System.out.println(new String (dest));

	args += C.PTR_SIZEOF;
	type2 = XPCOM.JS_TypeOfValue(Mozilla.jsLibPathBytes, cx, args);
	System.out.println(type2);
	stringPtr = XPCOM.JS_ValueToString(Mozilla.jsLibPathBytes, cx, args);
	length  = (int)XPCOM.JS_GetStringLength(Mozilla.jsLibPathBytes, stringPtr);
	nativePtr = XPCOM.JS_GetStringCharsZ(Mozilla.jsLibPathBytes, cx, stringPtr);
	dest = new char[length];
	XPCOM.memmove (dest, nativePtr, length * 2);
	System.out.println(new String (dest));

	rc = connect.JSValToVariant(cx, args, result);
	nsIVariant variant = new nsIVariant(result[0]);
	short[] type = new short[1];
	rc = variant.GetDataType(type);
	Object asdf = convertToJava(variant, type[0]);
	System.out.println(asdf);
	
	Object functionId = convertToJava(args);
	args += C.PTR_SIZEOF;
	Object token = convertToJava(args);
	
	for (int i = 0; i < argc; i++) {
		
	}

	return 0;
}

static Object convertToJava(int /*long*/ jsVal) {
	/*
typedef uint8 JSValueType;
#define JSVAL_TYPE_DOUBLE            ((uint8)0x00)
#define JSVAL_TYPE_INT32             ((uint8)0x01)
#define JSVAL_TYPE_UNDEFINED         ((uint8)0x02)
#define JSVAL_TYPE_BOOLEAN           ((uint8)0x03)
#define JSVAL_TYPE_MAGIC             ((uint8)0x04)
#define JSVAL_TYPE_STRING            ((uint8)0x05)
#define JSVAL_TYPE_NULL              ((uint8)0x06)
#define JSVAL_TYPE_OBJECT            ((uint8)0x07)
#define JSVAL_TYPE_UNKNOWN           ((uint8)0x20)
#define JSVAL_TYPE_NONFUNOBJ         ((uint8)0x57)
#define JSVAL_TYPE_FUNOBJ            ((uint8)0x67)
#define JSVAL_TYPE_STRORNULL         ((uint8)0x77)
#define JSVAL_TYPE_OBJORNULL         ((uint8)0x78)
#define JSVAL_TYPE_BOXED             ((uint8)0x79)
#define JSVAL_TYPE_UNINITIALIZED     ((uint8)0x7d)

jsval_layout PRIVATE_PTR_TO_JSVAL_IMPL(void *ptr)

	*/
	return null;
	
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};

	classInfo = new XPCOMObject (new int[] {2, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return getInterfaces (args[0], args[1]);}
		public int /*long*/ method4 (int /*long*/[] args) {return getHelperForLanguage ((int)/*64*/args[0], args[1]);}
		public int /*long*/ method5 (int /*long*/[] args) {return getContractID (args[0]);}
		public int /*long*/ method6 (int /*long*/[] args) {return getClassDescription (args[0]);}
		public int /*long*/ method7 (int /*long*/[] args) {return getClassID (args[0]);}
		public int /*long*/ method8 (int /*long*/[] args) {return getImplementationLanguage (args[0]);}
		public int /*long*/ method9 (int /*long*/[] args) {return getFlags (args[0]);}
		public int /*long*/ method10 (int /*long*/[] args) {return getClassIDNoAlloc (args[0]);}
	};

	securityCheckedComponent = new XPCOMObject (new int[] {2, 0, 0, 2, 3, 3, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return canCreateWrapper (args[0], args[1]);}
		public int /*long*/ method4 (int /*long*/[] args) {return canCallMethod (args[0], args[1], args[2]);}
		public int /*long*/ method5 (int /*long*/[] args) {return canGetProperty (args[0], args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return canSetProperty (args[0], args[1], args[2]);}
	};

	external = new XPCOMObject (new int[] {2, 0, 0, 4}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return callJava ((int)/*64*/args[0], args[1], args[2], args[3]);}
	};

	scriptObjectOwner = new XPCOMObject (new int[] {2, 0, 0, 2, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {
			// int /*long*/ globalObject = XPCOM.nsIScriptContext_GetGlobalObject(args[0]);
			int /*long*/ nativeContext = XPCOM.nsIScriptContext_GetNativeContext (args[0]);
			if (Mozilla.jsLibPathBytes == null) {
				String mozillaPath = Mozilla.getMozillaPath () + MozillaDelegate.getJSLibraryName() + '\0';
				try {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ("UTF-8"); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e) {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ();
				}
			}
			int /*long*/ globalJSObject = XPCOM.JS_GetGlobalObject (Mozilla.jsLibPathBytes, nativeContext);
			int /*long*/ globalJSObject1 = XPCOM.JS_GetGlobalObject (Mozilla.jsLibPathBytes, args[0]);
			int /*long*/ globalJSObject2 = XPCOM.JS_GetGlobalForScopeChain (Mozilla.jsLibPathBytes, nativeContext);
//			int /*long*/ globalJSObject3 = XPCOM.JS_GetGlobalForScopeChain (Mozilla.jsLibPathBytes, args[0]);
			int /*long*/ newObject = XPCOM.JS_NewObject(Mozilla.jsLibPathBytes, nativeContext, 0, 0, globalJSObject);
			
			byte[] functionName = MozillaDelegate.wcsToMbcs(null, "callJava", true);
			int /*long*/ newFunction = XPCOM.JS_DefineFunction(Mozilla.jsLibPathBytes, nativeContext, newObject, functionName, CallJavaProc.getAddress(), 3, /*JSPROP_ENUMERATE*/ 0x1 | /*JSPROP_READONLY*/ 0x2 | /*JSPROP_PERMANENT*/ 0x4);
			System.out.println(newFunction);
			
			XPCOM.memmove(args[1], new int /*long*/[] {newObject}, C.PTR_SIZEOF);
			return XPCOM.NS_OK;
		}
		public int /*long*/ method4 (int /*long*/[] args) {
			return XPCOM.NS_OK;
			//	return SetScriptObject (args[0]);}
		}
	};

	xpcScriptable = new XPCOMObject (new int[] {2, 0, 0, 1, 1, 4, 3, 3, 6, 6, 6, 6, 4, 7, 7, 6, 3, 7, 7, 7, 6, 3, 5, 4, 2}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {
			int /*long*/[] result = new int /*long*/[1];
			int rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
			serviceManager.Release ();

			nsIMemory memory = new nsIMemory (result[0]);
			result[0] = 0;
			byte[] bytes = MozillaDelegate.wcsToMbcs (null, "External", true); //$NON-NLS-1$
			int /*long*/ ptr = memory.Alloc (bytes.length);
			C.memmove (ptr, bytes, bytes.length);
			C.memmove(args[0], new int /*long*/[] {ptr}, C.PTR_SIZEOF);
			return XPCOM.NS_OK;}
		public int /*long*/ method4 (int /*long*/[] args) {
			System.out.println("Get scriptable flags");
			XPCOM.memmove(args[0], new int[] {/*WANT_NEWRESOLVE 512 |*/  /* WANT_GETPROPERTY */ 32 | /* WANT_CALL */ 8192}, 4);
		//	XPCOM.memmove(args[0], new int[] {0xFFFF}, 4);
			return XPCOM.NS_OK;}
		public int /*long*/ method5 (int /*long*/[] args) {
			System.out.println("pre-create");
			return XPCOM.NS_OK;}
		public int /*long*/ method6 (int /*long*/[] args) {
			System.out.println("create");
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method7 (int /*long*/[] args) {
			System.out.println("post-create");
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method8 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method9 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method10 (int /*long*/[] args) {
			System.out.println("Get property");
//			int /*long*/ nativeContext = XPCOM.nsIScriptContext_GetNativeContext (args[0]);
			if (Mozilla.jsLibPathBytes == null) {
				String mozillaPath = Mozilla.getMozillaPath () + MozillaDelegate.getJSLibraryName() + '\0';
				try {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ("UTF-8"); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e) {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ();
				}
			}
//			int /*long*/ globalJSObject = XPCOM.JS_GetGlobalObject (Mozilla.jsLibPathBytes, nativeContext);
//			int /*long*/ newObject = XPCOM.JS_NewObject(Mozilla.jsLibPathBytes, nativeContext, 0, 0, globalJSObject);
			
			int /*long*/ stringPtr = XPCOM.JS_ValueToString(Mozilla.jsLibPathBytes, args[1], args[3]);
			int /*long*/ stringLength = XPCOM.JS_GetStringLength(Mozilla.jsLibPathBytes, stringPtr);
			int /*long*/ nativePtr = XPCOM.JS_GetStringCharsZ(Mozilla.jsLibPathBytes, args[1], stringPtr);
			
			int length = XPCOM.strlen_PRUnichar (nativePtr);
			char[] dest = new char[length];
			XPCOM.memmove (dest, nativePtr, length * 2);
			System.out.println(new String (dest));

			
			
			byte[] functionName = MozillaDelegate.wcsToMbcs(null, "callJava", true);
			int /*long*/ newFunction = XPCOM.JS_DefineFunction(Mozilla.jsLibPathBytes, /*nativeContext*/args[1], /*newObject*/args[2], functionName, CallJavaProc.getAddress(), 3, /*JSPROP_ENUMERATE*/ 0x1 | /*JSPROP_READONLY*/ 0x2 | /*JSPROP_PERMANENT*/ 0x4);
			System.out.println(newFunction);
			
			return /* PR_TRUE */ 1;}

		public int /*long*/ method11 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method12 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method13 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method14 (int /*long*/[] args) {
			System.out.println("New Resolve");
			
			int /*long*/ nativeContext = XPCOM.nsIScriptContext_GetNativeContext (args[0]);
			if (Mozilla.jsLibPathBytes == null) {
				String mozillaPath = Mozilla.getMozillaPath () + MozillaDelegate.getJSLibraryName() + '\0';
				try {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ("UTF-8"); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e) {
					Mozilla.jsLibPathBytes = mozillaPath.getBytes ();
				}
			}
			
			int /*long*/ stringPtr = XPCOM.JS_ValueToString(Mozilla.jsLibPathBytes, /*args[1]*/nativeContext, args[3]);
			int /*long*/ stringLength = XPCOM.JS_GetStringLength(Mozilla.jsLibPathBytes, stringPtr);
			int /*long*/ nativePtr = XPCOM.JS_GetStringCharsZ(Mozilla.jsLibPathBytes, /*args[1]*/nativeContext, stringPtr);
			
			int length = XPCOM.strlen_PRUnichar (nativePtr);
			char[] dest = new char[length];
			XPCOM.memmove (dest, nativePtr, length /** 2*/);
			System.out.println(new String (dest));
			
//			int /*long*/ globalJSObject = XPCOM.JS_GetGlobalObject (Mozilla.jsLibPathBytes, nativeContext);
//			int /*long*/ newObject = XPCOM.JS_NewObject(Mozilla.jsLibPathBytes, nativeContext, 0, 0, globalJSObject);
			
			byte[] functionName = MozillaDelegate.wcsToMbcs(null, "callJava", true);
			int /*long*/ newFunction = XPCOM.JS_DefineFunction(Mozilla.jsLibPathBytes, /*nativeContext*/args[1], /*newObject*/args[2], functionName, CallJavaProc.getAddress(), 3, /*JSPROP_ENUMERATE*/ 0x1 | /*JSPROP_READONLY*/ 0x2 | /*JSPROP_PERMANENT*/ 0x4);
			System.out.println(newFunction);
			
			XPCOM.memmove(args[5], new int /*long*/[] {args[2]}, C.PTR_SIZEOF);
			return /* PR_TRUE */ 1;}
		public int /*long*/ method15 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method16 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method17 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method18 (int /*long*/[] args) {
			System.out.println("Call");
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method19 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method20 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method21 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method22 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method23 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
		public int /*long*/ method24 (int /*long*/[] args) {
			return XPCOM.NS_ERROR_NOT_IMPLEMENTED;}
	};

}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (external != null) {
		external.dispose ();
		external = null;	
	}
}

int /*long*/ getAddress () {
	return external.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		System.out.println("QI'd for nsISupports");
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIClassInfo.NS_ICLASSINFO_IID)) {
		System.out.println("QI'd for nsIClassInfo");
		XPCOM.memmove (ppvObject, new int /*long*/[] {classInfo.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsISecurityCheckedComponent.NS_ISECURITYCHECKEDCOMPONENT_IID)) {
		System.out.println("QI'd for nsISecurityCheckedComponent");
		XPCOM.memmove (ppvObject, new int /*long*/[] {securityCheckedComponent.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (EXTERNAL_IID)) {
		System.out.println("QI'd for EXTERNAL_IID");
		XPCOM.memmove (ppvObject, new int /*long*/[] {external.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}

	if (!Mozilla.IsPre_4) {
		nsID NS_ISCRIPT_OBJECT_OWNER_IID = new nsID("8f6bca7e-ce42-11d1-b724-00600891d8c9");
		if (guid.Equals(NS_ISCRIPT_OBJECT_OWNER_IID)) {
			System.out.println("QI'd for NS_ISCRIPT_OBJECT_OWNER");
			XPCOM.memmove (ppvObject, new int /*long*/[] {scriptObjectOwner.getAddress ()}, C.PTR_SIZEOF);
			AddRef();
			return XPCOM.NS_OK;
		}
	}
	
//	nsID NS_ASDF_IID = new nsID("3eedba38-8d22-41e1-817a-0e43e165b664");
//	if (guid.Equals (NS_ASDF_IID)) {
//		System.out.println("QI'd for nsIScriptObjectPrincipal");
//		XPCOM.memmove (ppvObject, new int /*long*/[] {external.getAddress ()}, C.PTR_SIZEOF);
//		AddRef();
//		return XPCOM.NS_OK;
//	}

//	if (!Mozilla.IsPre_4) {
//		nsID nsIXPCScriptable_IID = new nsID("a40ce52e-2d8c-400f-9af2-f8784a656070");
//		if (guid.Equals (nsIXPCScriptable_IID)) {
//			System.out.println("QI'd for nsIXPCScriptable_IID");
//			XPCOM.memmove (ppvObject, new int /*long*/[] {xpcScriptable.getAddress ()}, C.PTR_SIZEOF);
//			AddRef();
//			return XPCOM.NS_OK;
//		}
//	}
	
	System.out.println("no answer for: " + guid.toString());
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

/* nsIClassInfo */

int getClassDescription (int /*long*/ _retValue) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, "external", true); //$NON-NLS-1$
	int /*long*/ ptr = memory.Alloc (bytes.length);
	C.memmove (ptr, bytes, bytes.length);
	C.memmove (_retValue, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
	memory.Release ();

	return XPCOM.NS_OK;
}

int getClassID (int /*long*/ _retValue) {
	return XPCOM.NS_OK;
}

int getClassIDNoAlloc (int /*long*/ _retValue) {
	return XPCOM.NS_OK;
}

int getContractID (int /*long*/ _retValue) {
	return XPCOM.NS_OK;
}

int getFlags (int /*long*/ flags) {
	C.memmove (flags, new int[] {nsIClassInfo.THREADSAFE/*MAIN_THREAD_ONLY*/}, 4); /* PRUint32 */
	return XPCOM.NS_OK;
}

int getHelperForLanguage (int language, int /*long*/ _retValue) {
	C.memmove (_retValue, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int getImplementationLanguage (int /*long*/ _retValue) {
	C.memmove (_retValue, new int[] {5}, 4); /* nsIProgrammingLanguage.JAVA */ /* PRUint */ // nsIProgrammingLanguage::CPLUSPLUS
	return XPCOM.NS_OK;
}


int getInterfaces (int /*long*/ count, int /*long*/ array) {
	//C.memset(6, 6, 6);
	System.out.println("---------------------------");
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	int /*long*/ securityCheckedComponentIID = memory.Alloc (nsID.sizeof);
	XPCOM.memmove (securityCheckedComponentIID, nsISecurityCheckedComponent.NS_ISECURITYCHECKEDCOMPONENT_IID, nsID.sizeof);
	int /*long*/ externalIID = memory.Alloc (nsID.sizeof);
	XPCOM.memmove (externalIID, EXTERNAL_IID, nsID.sizeof);
	int /*long*/ ptrArray = memory.Alloc (3 * C.PTR_SIZEOF);
	C.memmove (ptrArray, new int /*long*/[] {securityCheckedComponentIID}, C.PTR_SIZEOF);
	C.memmove (ptrArray + C.PTR_SIZEOF, new int /*long*/[] {externalIID}, C.PTR_SIZEOF);
	
	nsID NS_ASDF_IID = new nsID("a40ce52e-2d8c-400f-9af2-f8784a656070");
	int /*long*/ asdfIID = memory.Alloc (nsID.sizeof);
	XPCOM.memmove (asdfIID, NS_ASDF_IID, nsID.sizeof);
	C.memmove (ptrArray + 2 * C.PTR_SIZEOF, new int /*long*/[] {asdfIID}, C.PTR_SIZEOF);
	
	
	C.memmove (array, new int /*long*/[] {ptrArray}, C.PTR_SIZEOF);
	memory.Release ();

	C.memmove (count, new int[] {3}, 4); /* PRUint */
	return XPCOM.NS_OK;
}

/* nsISecurityCheckedComponent */

int canCreateWrapper (int /*long*/ iid, int /*long*/ _retVal) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, "allAccess", true); //$NON-NLS-1$
	int /*long*/ ptr = memory.Alloc (bytes.length);
	C.memmove (ptr, bytes, bytes.length);
	C.memmove (_retVal, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
	memory.Release ();

	return XPCOM.NS_OK;
}

int canCallMethod (int /*long*/ iid, int /*long*/ methodName, int /*long*/ _retVal) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	int length = XPCOM.strlen_PRUnichar (methodName);
	char[] dest = new char[length];
	XPCOM.memmove (dest, methodName, length * 2);
	String string = new String (dest);
	byte[] bytes;
	if (string.equals("callJava")) { //$NON-NLS-1$
		bytes = MozillaDelegate.wcsToMbcs (null, "allAccess", true); //$NON-NLS-1$ 
	} else {
		bytes = MozillaDelegate.wcsToMbcs (null, "noAccess", true); //$NON-NLS-1$
	}
	int /*long*/ ptr = memory.Alloc (bytes.length);
	C.memmove (ptr, bytes, bytes.length);
	C.memmove (_retVal, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
	memory.Release ();

	return XPCOM.NS_OK;
}

int canGetProperty (int /*long*/ iid, int /*long*/ propertyName, int /*long*/ _retVal) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, "noAccess", true); //$NON-NLS-1$
	int /*long*/ ptr = memory.Alloc (bytes.length);
	C.memmove (ptr, bytes, bytes.length);
	C.memmove (_retVal, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
	memory.Release ();

	return XPCOM.NS_OK;
}

int canSetProperty (int /*long*/ iid, int /*long*/ propertyName, int /*long*/ _retVal) {
	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
	serviceManager.Release ();

	nsIMemory memory = new nsIMemory (result[0]);
	result[0] = 0;
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, "noAccess", true); //$NON-NLS-1$
	int /*long*/ ptr = memory.Alloc (bytes.length);
	C.memmove (ptr, bytes, bytes.length);
	C.memmove (_retVal, new int /*long*/[] {ptr}, C.PTR_SIZEOF);
	memory.Release ();

	return XPCOM.NS_OK;
}

/* external */

static Object convertToJava (nsIVariant variant, short type) {
	switch (type) {
		case nsIDataType.VTYPE_EMPTY:
		case nsIDataType.VTYPE_VOID:
			return null;
		case nsIDataType.VTYPE_EMPTY_ARRAY:
			return new Object[0];
		case nsIDataType.VTYPE_BOOL:
			int[] boolResult = new int[1]; /*PRInt32*/
			int rc = variant.GetAsBool (boolResult);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			return new Boolean (boolResult[0] != 0);
		case nsIDataType.VTYPE_INT32:
			int[] intResult = new int[1]; /*PRInt32*/
			rc = variant.GetAsInt32 (intResult);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			return new Double (intResult[0]);
		case nsIDataType.VTYPE_DOUBLE:
			int /*long*/ doubleReturn = C.malloc (8);
			rc = variant.GetAsDouble (doubleReturn);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			double[] doubleResult = new double[1];
			C.memmove (doubleResult, doubleReturn, 8);
			C.free (doubleReturn);
			return new Double (doubleResult[0]);
		case nsIDataType.VTYPE_WSTRING_SIZE_IS:
			int[] size = new int[1]; /* PRInt32 */
			int /*long*/[] wString = new int /*long*/[1];
			rc = variant.GetAsWStringWithSize (size, wString);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			char[] chars = new char[size[0]];
			C.memmove (chars, wString[0], size[0] * 2);
			return new String (chars);
		case nsIDataType.VTYPE_ARRAY:
			Object[] arrayReturn = new Object[0];
			int /*long*/ iid = C.malloc (nsID.sizeof);
			C.memset (iid, 0, nsID.sizeof);
			int[] count = new int[1]; /* PRUint32 */
			short[] currentType = new short[1];
			int /*long*/[] ptr = new int /*long*/[1];
			rc = variant.GetAsArray (currentType, iid, count, ptr);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			if (ptr[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
			nsID id = new nsID ();
			XPCOM.memmove (id, iid, nsID.sizeof);
			C.free (iid);

			int /*long*/[] result = new int /*long*/[1];
			rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MEMORY_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, nsIMemory.NS_IMEMORY_IID, result);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);		
			serviceManager.Release ();

			nsIMemory memory = new nsIMemory (result[0]);
			result[0] = 0;

			if (id.Equals (nsIVariant.NS_IVARIANT_IID)) {
				arrayReturn = new Object[count[0]];
				for (int i = 0; i < count[0]; i++) {
					int /*long*/[] arrayPtr = new int /*long*/[1];
					C.memmove (arrayPtr, ptr[0] + i * C.PTR_SIZEOF, C.PTR_SIZEOF);
					nsISupports supports = new nsISupports (arrayPtr[0]);
					rc = supports.QueryInterface (nsIVariant.NS_IVARIANT_IID, result);
					if (rc != XPCOM.NS_OK) Mozilla.error (rc);
					if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

					nsIVariant currentVariant = new nsIVariant (result[0]);
					result[0] = 0;
					currentType[0] = 0;
					rc = currentVariant.GetDataType (currentType);
					if (rc != XPCOM.NS_OK) Mozilla.error (rc);
					try {
						arrayReturn[i] = convertToJava (currentVariant, currentType[0]);
						currentVariant.Release ();
					} catch (IllegalArgumentException e) {
						/* invalid argument value type */
						currentVariant.Release ();
						memory.Free (ptr[0]);
						memory.Release ();
						throw e;
					}
				}
			} else {
				switch (currentType[0]) {
					case nsIDataType.VTYPE_DOUBLE:
						arrayReturn = new Object[count[0]];
						for (int i = 0; i < count[0]; i++) {
							double[] doubleValue = new double[1];
							C.memmove (doubleValue, ptr[0] + i * 8, 8);
							arrayReturn[i] = new Double (doubleValue[0]);
						}
						break;
					case nsIDataType.VTYPE_BOOL:
						arrayReturn = new Object[count[0]];
						for (int i = 0; i < count[0]; i++) {
							int[] boolValue = new int[1]; /* PRUInt32 */
							C.memmove (boolValue, ptr[0] + i * 4, 4);
							arrayReturn[i] = new Boolean (boolValue[0] != 0);
						}
						break;
					case nsIDataType.VTYPE_INT32:
						arrayReturn = new Object[count[0]];
						for (int i = 0; i < count[0]; i++) {
							int[] intValue = new int[1]; /* PRInt32 */
							C.memmove (intValue, ptr[0] + i * 4, 4);
							arrayReturn[i] = new Double (intValue[0]);
						}
						break;
					case nsIDataType.VTYPE_WCHAR_STR:
						arrayReturn = new Object[count[0]];
						for (int i = 0; i < count[0]; i++) {
							int /*long*/ currentPtr = ptr[0] + i * C.PTR_SIZEOF;
							int /*long*/[] stringPtr = new int /*long*/[1]; 
							C.memmove (stringPtr, currentPtr, C.PTR_SIZEOF);
							int length = XPCOM.strlen_PRUnichar (stringPtr[0]);
							char[] dest = new char[length];
							XPCOM.memmove (dest, stringPtr[0], length * 2);
							arrayReturn[i] = new String (dest);
						}
						break;
					default:
						memory.Free (ptr[0]);
						memory.Release ();
						SWT.error (SWT.ERROR_INVALID_ARGUMENT);
				}
			}
			memory.Free (ptr[0]);
			memory.Release ();
			return arrayReturn;
	}
	SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return null;
}

nsIVariant convertToJS (Object value, nsIComponentManager componentManager) {
	int /*long*/[] result = new int /*long*/[1];
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_VARIANT_CONTRACTID, true);
	int rc = componentManager.CreateInstanceByContractID (aContractID, 0, nsIWritableVariant.NS_IWRITABLEVARIANT_IID, result);
	nsIWritableVariant variant = new nsIWritableVariant (result[0]);
	result[0] = 0;

	if (value == null) {
		rc = variant.SetAsEmpty ();
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		return variant;
	}
	if (value instanceof String) {
		String stringValue = (String)value;
		int length = stringValue.length ();
		char[] chars = new char[length];
		stringValue.getChars (0, length, chars, 0);
		rc = variant.SetAsWStringWithSize (length, chars);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		return variant;
	}
	if (value instanceof Boolean) {
		Boolean booleanValue = (Boolean)value;
		rc = variant.SetAsBool (booleanValue.booleanValue () ? 1 : 0);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		return variant;
	}
	if (value instanceof Number) {
		Number numberValue = (Number)value;
		rc = variant.SetAsDouble (numberValue.doubleValue ());
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		return variant;
	}
	if (value instanceof Object[]) {
		Object[] arrayValue = (Object[])value;
		int length = arrayValue.length;
		if (length == 0) {
			rc = variant.SetAsEmptyArray ();
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		} else {
			int /*long*/ arrayPtr = C.malloc (C.PTR_SIZEOF * length);
			for (int i = 0; i < length; i++) {
				Object currentObject = arrayValue[i];
				try {
					nsIVariant currentVariant = convertToJS (currentObject, componentManager);
					C.memmove (arrayPtr + C.PTR_SIZEOF * i, new int /*long*/[] {currentVariant.getAddress ()}, C.PTR_SIZEOF);
				} catch (SWTException e) {
					/* invalid return value type */
					C.free (arrayPtr);
					variant.Release ();
					/* release the variants that had previously been added to the array */
					for (int j = 0; j < i; j++) {
						int /*long*/[] ptr = new int /*long*/[1];
						C.memmove (ptr, arrayPtr + C.PTR_SIZEOF * j, C.PTR_SIZEOF);
						new nsISupports (ptr[0]).Release ();
					}
					throw e;
				}
			}
			int /*long*/ idPtr = C.malloc (nsID.sizeof);
			XPCOM.memmove (idPtr, nsIVariant.NS_IVARIANT_IID, nsID.sizeof);
			rc = variant.SetAsArray (nsIDataType.VTYPE_INTERFACE_IS, idPtr, length, arrayPtr);
			C.free (idPtr);
			C.free (arrayPtr);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		}
		return variant;
	}

	variant.Release ();
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return null;
}

int callJava (int functionId, int /*long*/ tokenVariant, int /*long*/ args, int /*long*/ returnPtr) {
	Object key = new Integer (functionId);
	BrowserFunction function = (BrowserFunction)Mozilla.AllFunctions.get (key);
	Object returnValue = null;

	if (function != null) {
		try {
			short[] type = new short[1]; /* PRUint16 */
			nsIVariant variant = new nsIVariant (tokenVariant);
			int rc = variant.GetDataType (type);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			Object temp = convertToJava (variant, type[0]);
			type[0] = 0;
			if (temp instanceof String) {
				String token = (String)temp;
				if (token.equals (function.token)) {
					variant = new nsIVariant (args);
					rc = variant.GetDataType (type);
					if (rc != XPCOM.NS_OK) Mozilla.error (rc);
					temp = convertToJava (variant, type[0]);
					if (temp instanceof Object[]) {
						Object[] arguments = (Object[])temp;
						try {
							returnValue = function.function (arguments);
						} catch (Exception e) {
							/* exception during function invocation */
							returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			/* invalid argument value type */
			if (function.isEvaluate) {
				/* notify the evaluate function so that a java exception can be thrown */
				function.function (new String[] {WebBrowser.CreateErrorString (new SWTException (SWT.ERROR_INVALID_RETURN_VALUE).getLocalizedMessage ())});
			}
			returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
		}
	}

	int /*long*/[] result = new int /*long*/[1];
	int rc = XPCOM.NS_GetComponentManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	nsIComponentManager componentManager = new nsIComponentManager (result[0]);
	result[0] = 0;
	nsIVariant variant;
	try {
		variant = convertToJS (returnValue, componentManager);
	} catch (SWTException e) {
		/* invalid return value type */
		variant = convertToJS (WebBrowser.CreateErrorString (e.getLocalizedMessage ()), componentManager);
	}
	componentManager.Release ();
	C.memmove (returnPtr, new int /*long*/[] {variant.getAddress ()}, C.PTR_SIZEOF);

	return XPCOM.NS_OK;
}
}
