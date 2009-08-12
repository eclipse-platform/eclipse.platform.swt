/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * OleAutomation provides a generic mechanism for accessing functionality that is 
 * specific to a particular ActiveX Control or OLE Document.
 *
 * <p>The OLE Document or ActiveX Control must support the IDispatch interface in order to provide
 * OleAutomation support. The additional functionality provided by the OLE Object is specified in 
 * its IDL file.  The additional methods can either be to get property values (<code>getProperty</code>), 
 * to set property values (<code>setProperty</code>) or to invoke a method (<code>invoke</code> or
 * <code>invokeNoReply</code>).  Arguments are passed around in the form of <code>Variant</code> 
 * objects.
 *
 * <p>Here is a sample IDL fragment:
 *
 * <pre>
 *	interface IMyControl : IDispatch
 *	{
 *		[propget, id(0)] HRESULT maxFileCount([retval, out] int *c);
 *		[propput, id(0)] HRESULT maxFileCount([in] int c);
 *		[id(1)]	HRESULT AddFile([in] BSTR fileName);
 *	};
 * </pre>
 *
 * <p>An example of how to interact with this extended functionality is shown below:
 *
 * <code><pre>
 *	OleAutomation automation = new OleAutomation(myControlSite);
 *
 *	// Look up the ID of the maxFileCount parameter
 *	int[] rgdispid = automation.getIDsOfNames(new String[]{"maxFileCount"});
 *	int maxFileCountID = rgdispid[0];
 *
 *	// Set the property maxFileCount to 100:
 *	if (automation.setProperty(maxFileCountID, new Variant(100))) {
 *		System.out.println("Max File Count was successfully set.");
 *	}
 *
 *	// Get the new value of the maxFileCount parameter:
 *	Variant pVarResult = automation.getProperty(maxFileCountID);
 *	if (pVarResult != null) {
 *		System.out.println("Max File Count is "+pVarResult.getInt());
 *	}
 *
 *	// Invoke the AddFile method
 *	// Look up the IDs of the AddFile method and its parameter
 *	rgdispid = automation.getIDsOfNames(new String[]{"AddFile", "fileName"}); 
 *	int dispIdMember = rgdispid[0];
 *	int[] rgdispidNamedArgs = new int[] {rgdispid[1]};
 *
 *	// Convert arguments to Variant objects
 *	Variant[] rgvarg = new Variant[1];
 *	String fileName = "C:\\testfile";
 * 	rgvarg[0] = new Variant(fileName);
 *
 *	// Call the method
 *	Variant pVarResult = automation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
 *
 *	// Check the return value
 * 	if (pVarResult == null || pVarResult.getInt() != OLE.S_OK){
 * 		System.out.println("Failed to add file "+fileName);
 *	}
 *
 *	automation.dispose();
 *
 * </pre></code>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#ole">OLE and ActiveX snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: OLEExample, OleWebBrowser</a>
 */
public final class OleAutomation {
	private IUnknown objIUnknown;
	private IDispatch objIDispatch;
	private String exceptionDescription;
	private ITypeInfo objITypeInfo;
	
OleAutomation(IDispatch idispatch) {
	if (idispatch == null) OLE.error(OLE.ERROR_INVALID_INTERFACE_ADDRESS);
	objIDispatch = idispatch;
	objIDispatch.AddRef();
	
	int /*long*/[] ppv = new int /*long*/[1];
	/* GetTypeInfo([in] iTInfo, [in] lcid, [out] ppTInfo) 
	 * AddRef has already been called on ppTInfo by the callee and must be released by the caller. 
	 */
	int result = objIDispatch.GetTypeInfo(0, COM.LOCALE_USER_DEFAULT, ppv);
	if (result == OLE.S_OK) {
		objITypeInfo = new ITypeInfo(ppv[0]);
	}
}
/**
 * Creates an OleAutomation object for the specified client.
 *
 * @param clientSite the site for the OLE Document or ActiveX Control whose additional functionality 
 *        you need to access
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_INVALID_INTERFACE_ADDRESS when called with an invalid client site
 *	</ul>
 */
public OleAutomation(OleClientSite clientSite) {
	if (clientSite == null) OLE.error(OLE.ERROR_INVALID_INTERFACE_ADDRESS);
	objIDispatch = clientSite.getAutomationObject();

	int /*long*/[] ppv = new int /*long*/[1];
	/* GetTypeInfo([in] iTInfo, [in] lcid, [out] ppTInfo) 
	 * AddRef has already been called on ppTInfo by the callee and must be released by the caller. 
	 */
	int result = objIDispatch.GetTypeInfo(0, COM.LOCALE_USER_DEFAULT, ppv);
	if (result == OLE.S_OK) {
		objITypeInfo = new ITypeInfo(ppv[0]);
	}
}
/**
 * Creates an OleAutomation object for the specified progID.
 *
 * @param progId the unique program identifier of an OLE Document application; 
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 *               
 * @exception SWTException
 * <ul><li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_INTERFACE_NOT_FOUND when the OLE object specified does not implement IDispatch
 * </ul>
 * 
 * @since 3.6
 */
public OleAutomation(String progId) {
	try {
		OS.OleInitialize(0);
		GUID appClsid = getClassID(progId);
		if (appClsid == null) {
			OS.OleUninitialize();
			OLE.error(OLE.ERROR_INVALID_CLASSID);
		}
		
		int[] ppvObject = new int[1];
		int result = COM.CoCreateInstance(appClsid, 0, COM.CLSCTX_INPROC_SERVER, COM.IIDIUnknown, ppvObject); 
		if (result != COM.S_OK) {
			OS.OleUninitialize();
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		}
		objIUnknown = new IUnknown(ppvObject[0]);
		
		ppvObject[0] = 0;
		result = objIUnknown.QueryInterface(COM.IIDIDispatch, ppvObject); 
		if (result != COM.S_OK) OLE.error(OLE.ERROR_INTERFACE_NOT_FOUND);
		objIDispatch = new IDispatch(ppvObject[0]);

		ppvObject[0] = 0;
		result = objIDispatch.GetTypeInfo(0, COM.LOCALE_USER_DEFAULT, ppvObject);
		if (result == OLE.S_OK) {
			objITypeInfo = new ITypeInfo(ppvObject[0]);
		}
	} catch (SWTException e) {
		dispose();
		throw e;
	}
}
/**
 * Disposes the automation object.
 * <p>
 * This method releases the IDispatch interface on the OLE Document or ActiveX Control.
 * Do not use the OleAutomation object after it has been disposed.
 */
public void dispose() {

	if (objIDispatch != null){
		objIDispatch.Release();
	}
	objIDispatch = null;
	
	if (objITypeInfo != null){
		objITypeInfo.Release();
	}
	objITypeInfo = null;
	
	if (objIUnknown != null){
		objIUnknown.Release();
		OS.OleUninitialize();
	}
	objIUnknown = null;
}
int /*long*/ getAddress() {	
	return objIDispatch.getAddress();
}
GUID getClassID(String clientName) {
	// create a GUID struct to hold the result
	GUID guid = new GUID();

	// create a null terminated array of char
	char[] buffer = null;
	if (clientName != null) {
		int count = clientName.length();
		buffer = new char[count + 1];
		clientName.getChars(0, count, buffer, 0);
	}
	if (COM.CLSIDFromProgID(buffer, guid) != COM.S_OK){
		int result = COM.CLSIDFromString(buffer, guid);
		if (result != COM.S_OK) return null;
	}
	return guid;
}
/**
 * Returns the fully qualified name of the Help file for the given member ID.
 * 
 * @param dispId the member ID whose Help file is being retrieved.
 * @return a string representing the fully qualified name of a Help 
 * file or null.
 */
public String getHelpFile(int dispId) {
	if (objITypeInfo == null) return null;
	String[] file = new String[1];
	int rc = objITypeInfo.GetDocumentation(dispId, null, null, null, file );
	if (rc == OLE.S_OK) return file[0];
	return null;	
}
/**
 * Returns the documentation string for the given member ID.
 * 
 * @param dispId the member ID in which the documentation is being retrieved.
 * @return the documentation string if it exists; otherwise return null.
 */
public String getDocumentation(int dispId) {
	if (objITypeInfo == null) return null;
	String[] doc = new String[1];
	int rc = objITypeInfo.GetDocumentation(dispId, null, doc, null, null );
	if (rc == OLE.S_OK) return doc[0];
	return null;
}
/**
 * Returns the property description of a variable at the given index.
 * 
 * @param index the index of a variable whose property is being retrieved.
 * @return an OlePropertyDescription for a variable at the given index.
 */
public OlePropertyDescription getPropertyDescription(int index) {
	if (objITypeInfo == null) return null;
	int /*long*/[] ppVarDesc = new int /*long*/[1];
	int rc = objITypeInfo.GetVarDesc(index, ppVarDesc);
	if (rc != OLE.S_OK) return null;
	VARDESC vardesc = new VARDESC();
	COM.MoveMemory(vardesc, ppVarDesc[0], VARDESC.sizeof);
	
	OlePropertyDescription data = new OlePropertyDescription();
	data.id = vardesc.memid;
	data.name = getName(vardesc.memid);
	data.type = vardesc.elemdescVar_tdesc_vt;
	if (data.type == OLE.VT_PTR) {
		short[] vt = new short[1];
		COM.MoveMemory(vt, vardesc.elemdescVar_tdesc_union + OS.PTR_SIZEOF, 2);
		data.type = vt[0];
	}
	data.flags = vardesc.wVarFlags;
	data.kind = vardesc.varkind;
	data.description = getDocumentation(vardesc.memid);
	data.helpFile = getHelpFile(vardesc.memid);
	
	objITypeInfo.ReleaseVarDesc(ppVarDesc[0]);
	return data;
}
/**
 * Returns the description of a function at the given index.
 * 
 * @param index the index of a function whose property is being retrieved.
 * @return an OleFunctionDescription for a function at the given index.
 */
public OleFunctionDescription getFunctionDescription(int index) {
	if (objITypeInfo == null) return null;
	int /*long*/[] ppFuncDesc = new int /*long*/[1];
	int rc = objITypeInfo.GetFuncDesc(index, ppFuncDesc);
	if (rc != OLE.S_OK) return null;
	FUNCDESC funcdesc = new FUNCDESC();
	COM.MoveMemory(funcdesc, ppFuncDesc[0], FUNCDESC.sizeof);
	
	OleFunctionDescription data = new OleFunctionDescription();
	
	data.id = funcdesc.memid;
	data.optionalArgCount = funcdesc.cParamsOpt;
	data.invokeKind = funcdesc.invkind;
	data.funcKind = funcdesc.funckind;
	data.flags = funcdesc.wFuncFlags;
	data.callingConvention = funcdesc.callconv;
	data.documentation = getDocumentation(funcdesc.memid);
	data.helpFile = getHelpFile(funcdesc.memid);
	
	String[] names = getNames(funcdesc.memid, funcdesc.cParams + 1);
	if (names.length > 0) {
		data.name = names[0];
	}
	data.args = new OleParameterDescription[funcdesc.cParams];
	for (int i = 0; i < data.args.length; i++) {
		data.args[i] = new OleParameterDescription();
		if (names.length > i + 1) {
			data.args[i].name = names[i + 1];
		}
		//TODO 0- use structures
		short[] vt = new short[1];	
		COM.MoveMemory(vt, funcdesc.lprgelemdescParam + i * COM.ELEMDESC_sizeof() + OS.PTR_SIZEOF, 2);				
		if (vt[0] == OLE.VT_PTR) {
			int /*long*/ [] pTypedesc = new int /*long*/ [1];
			COM.MoveMemory(pTypedesc, funcdesc.lprgelemdescParam + i * COM.ELEMDESC_sizeof(), OS.PTR_SIZEOF);
			short[] vt2 = new short[1];
			COM.MoveMemory(vt2, pTypedesc[0] + OS.PTR_SIZEOF, 2);
			vt[0] = (short)(vt2[0] | COM.VT_BYREF);
		}
		data.args[i].type = vt[0];
		short[] wParamFlags = new short[1];
		COM.MoveMemory(wParamFlags, funcdesc.lprgelemdescParam + i * COM.ELEMDESC_sizeof() + COM.TYPEDESC_sizeof () + OS.PTR_SIZEOF, 2);
		data.args[i].flags = wParamFlags[0];	
	}
	
	data.returnType = funcdesc.elemdescFunc_tdesc_vt;
	if (data.returnType == OLE.VT_PTR) {
		short[] vt = new short[1];
		COM.MoveMemory(vt, funcdesc.elemdescFunc_tdesc_union + OS.PTR_SIZEOF, 2);
		data.returnType = vt[0];
	}

	objITypeInfo.ReleaseFuncDesc(ppFuncDesc[0]);
	return data;
}
/**
 * Returns the type info of the current object referenced by the automation.
 * The type info contains information about the object such as the function descriptions,
 * the member descriptions and attributes of the type.
 * 
 * @return the type info of the receiver
 */
public TYPEATTR getTypeInfoAttributes() {
	if (objITypeInfo == null) return null;
	int /*long*/ [] ppTypeAttr = new int /*long*/ [1];
	int rc = objITypeInfo.GetTypeAttr(ppTypeAttr);
	if (rc != OLE.S_OK) return null;
	TYPEATTR typeattr = new TYPEATTR();
	COM.MoveMemory(typeattr, ppTypeAttr[0], TYPEATTR.sizeof);
	objITypeInfo.ReleaseTypeAttr(ppTypeAttr[0]);
	return typeattr;
}
/**
 * Returns the name of the given member ID.
 * 
 * @param dispId the member ID in which the name is being retrieved.
 * @return the name if it exists; otherwise return null.
 */
public String getName(int dispId) {
	if (objITypeInfo == null) return null;
	String[] name = new String[1];
	int rc = objITypeInfo.GetDocumentation(dispId, name, null, null, null );
	if (rc == OLE.S_OK) return name[0];
	return null;
}
/**
 * Returns the name of a function and parameter names for the specified function ID.
 * 
 * @param dispId the function ID in which the name and parameters are being retrieved.
 * @param maxSize the maximum number of names to retrieve.
 * @return an array of name containing the function name and the parameter names
 */
public String[] getNames(int dispId, int maxSize) {
	if (objITypeInfo == null) return new String[0];
	String[] names = new String[maxSize];
	int[] count = new int[1];
	int rc = objITypeInfo.GetNames(dispId, names, maxSize, count);
	if (rc == OLE.S_OK) {
		String[] newNames = new String[count[0]];
		System.arraycopy(names, 0, newNames, 0, count[0]);
		return newNames;
	}
	return new String[0];
}
/**
 * Returns the positive integer values (IDs) that are associated with the specified names by the
 * IDispatch implementor.  If you are trying to get the names of the parameters in a method, the first 
 * String in the names array must be the name of the method followed by the names of the parameters.
 *
 * @param names an array of names for which you require the identifiers
 *
 * @return positive integer values that are associated with the specified names in the same
 *         order as the names where provided; or null if the names are unknown
 */
public int[] getIDsOfNames(String[] names) {

	int[] rgdispid = new int[names.length];
	int result = objIDispatch.GetIDsOfNames(new GUID(), names, names.length, COM.LOCALE_USER_DEFAULT, rgdispid);
	if (result != COM.S_OK) return null;
	
	return rgdispid;
}
/**
 * Returns a description of the last error encountered.
 *
 * @return a description of the last error encountered
 */
public String getLastError() {
	
	return exceptionDescription;

}
/**
 * Returns the value of the property specified by the dispIdMember.
 *
 * @param dispIdMember the ID of the property as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @return the value of the property specified by the dispIdMember or null
 */
public Variant getProperty(int dispIdMember) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_PROPERTYGET, null, null, pVarResult);
	return (result == OLE.S_OK) ? pVarResult : null;
}
/**
 * Returns the value of the property specified by the dispIdMember.
 *
 * @param dispIdMember the ID of the property as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 * 
 * @return the value of the property specified by the dispIdMember or null
 * 
 * @since 2.0
 */
public Variant getProperty(int dispIdMember, Variant[] rgvarg) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_PROPERTYGET, rgvarg, null, pVarResult);
	return (result == OLE.S_OK) ? pVarResult : null;
	
}
/**
 * Returns the value of the property specified by the dispIdMember.
 *
 * @param dispIdMember the ID of the property as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 * 
 * @param rgdispidNamedArgs an array of identifiers for the arguments specified in rgvarg; the
 *        parameter IDs must be in the same order as their corresponding values;
 *        all arguments must have an identifier - identifiers can be obtained using 
 *        OleAutomation.getIDsOfNames
 * 
 * @return the value of the property specified by the dispIdMember or null
 * 
 * @since 2.0
 */
public Variant getProperty(int dispIdMember, Variant[] rgvarg, int[] rgdispidNamedArgs) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_PROPERTYGET, rgvarg, rgdispidNamedArgs, pVarResult);
	return (result == OLE.S_OK) ? pVarResult : null;
}
public boolean equals(Object object) {
	if (object == this) return true;
	if (object instanceof OleAutomation) {
		if (objIDispatch == null) return false;
		OleAutomation oleAutomation = ((OleAutomation) object); 
		if (oleAutomation.objIDispatch == null) return false;
		int address1 = objIDispatch.getAddress();
		int address2 = oleAutomation.objIDispatch.getAddress();
		return address1 == address2;
	}
	return false;
}
/** 
 * Invokes a method on the OLE Object; the method has no parameters.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @return the result of the method or null if the method failed to give result information
 */
public Variant invoke(int dispIdMember) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, null, null, pVarResult);
	return (result == COM.S_OK) ? pVarResult : null;
}
/** 
 * Invokes a method on the OLE Object; the method has no optional parameters.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 *
 * @return the result of the method or null if the method failed to give result information
 */
public Variant invoke(int dispIdMember, Variant[] rgvarg) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, rgvarg, null, pVarResult);
	return (result == COM.S_OK) ? pVarResult : null;
}
/** 
 * Invokes a method on the OLE Object; the method has optional parameters.  It is not
 * necessary to specify all the optional parameters, only include the parameters for which
 * you are providing values.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 *
 * @param rgdispidNamedArgs an array of identifiers for the arguments specified in rgvarg; the
 *        parameter IDs must be in the same order as their corresponding values;
 *        all arguments must have an identifier - identifiers can be obtained using 
 *        OleAutomation.getIDsOfNames
 *
 * @return the result of the method or null if the method failed to give result information
 */
public Variant invoke(int dispIdMember, Variant[] rgvarg, int[] rgdispidNamedArgs) {
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, rgvarg, rgdispidNamedArgs, pVarResult);
	return (result == COM.S_OK) ? pVarResult : null;
}
private int invoke(int dispIdMember, int wFlags, Variant[] rgvarg, int[] rgdispidNamedArgs, Variant pVarResult) {

	// get the IDispatch interface for the control
	if (objIDispatch == null) return COM.E_FAIL;
	
	// create a DISPPARAMS structure for the input parameters
	DISPPARAMS pDispParams = new DISPPARAMS();
	// store arguments in rgvarg
	if (rgvarg != null && rgvarg.length > 0) {
		pDispParams.cArgs = rgvarg.length;
		pDispParams.rgvarg = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, VARIANT.sizeof * rgvarg.length);
		int offset = 0;
		for (int i = rgvarg.length - 1; i >= 0 ; i--) {
			rgvarg[i].getData(pDispParams.rgvarg + offset);
			offset += VARIANT.sizeof;
		}
	}

	// if arguments have ids, store the ids in rgdispidNamedArgs
	if (rgdispidNamedArgs != null && rgdispidNamedArgs.length > 0) {
		pDispParams.cNamedArgs = rgdispidNamedArgs.length;
		pDispParams.rgdispidNamedArgs = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, 4 * rgdispidNamedArgs.length);
		int offset = 0;
		for (int i = rgdispidNamedArgs.length; i > 0; i--) {
			COM.MoveMemory(pDispParams.rgdispidNamedArgs + offset, new int[] {rgdispidNamedArgs[i-1]}, 4);
			offset += 4;
		}
	}

	// invoke the method
	EXCEPINFO excepInfo = new EXCEPINFO();
	int[] pArgErr = new int[1];
	int /*long*/ pVarResultAddress = 0;
	if (pVarResult != null)	pVarResultAddress = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
	int result = objIDispatch.Invoke(dispIdMember, new GUID(), COM.LOCALE_USER_DEFAULT, wFlags, pDispParams, pVarResultAddress, excepInfo, pArgErr);

	if (pVarResultAddress != 0){
		pVarResult.setData(pVarResultAddress);
		COM.VariantClear(pVarResultAddress);
		OS.GlobalFree(pVarResultAddress);
	}
	
	// free the Dispparams resources
	if (pDispParams.rgdispidNamedArgs != 0){
		OS.GlobalFree(pDispParams.rgdispidNamedArgs);
	}
	if (pDispParams.rgvarg != 0) {
		int offset = 0;
		for (int i = 0, length = rgvarg.length; i < length; i++){
			COM.VariantClear(pDispParams.rgvarg + offset);
			offset += VARIANT.sizeof;
		}
		OS.GlobalFree(pDispParams.rgvarg);
	}

	// save error string and cleanup EXCEPINFO
	manageExcepinfo(result, excepInfo);
		
	return result;
}
/** 
 * Invokes a method on the OLE Object; the method has no parameters.  In the early days of OLE, 
 * the IDispatch interface was not well defined and some applications (mainly Word) did not support 
 * a return value.  For these applications, call this method instead of calling
 * <code>public void invoke(int dispIdMember)</code>.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @exception org.eclipse.swt.SWTException <ul>
 *		<li>ERROR_ACTION_NOT_PERFORMED when method invocation fails
 *	</ul>
 */
public void invokeNoReply(int dispIdMember) {
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, null, null, null);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_ACTION_NOT_PERFORMED, result);
}
/** 
 * Invokes a method on the OLE Object; the method has no optional parameters.  In the early days of OLE, 
 * the IDispatch interface was not well defined and some applications (mainly Word) did not support 
 * a return value.  For these applications, call this method instead of calling
 * <code>public void invoke(int dispIdMember, Variant[] rgvarg)</code>.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 *
 * @exception org.eclipse.swt.SWTException <ul>
 *		<li>ERROR_ACTION_NOT_PERFORMED when method invocation fails
 *	</ul>
 */
public void invokeNoReply(int dispIdMember, Variant[] rgvarg) {
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, rgvarg, null, null);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_ACTION_NOT_PERFORMED, result);
}
/** 
 * Invokes a method on the OLE Object; the method has optional parameters.  It is not
 * necessary to specify all the optional parameters, only include the parameters for which
 * you are providing values.  In the early days of OLE, the IDispatch interface was not well 
 * defined and some applications (mainly Word) did not support a return value.  For these 
 * applications, call this method instead of calling
 * <code>public void invoke(int dispIdMember, Variant[] rgvarg, int[] rgdispidNamedArgs)</code>.
 *
 * @param dispIdMember the ID of the method as specified by the IDL of the ActiveX Control; the
 *        value for the ID can be obtained using OleAutomation.getIDsOfNames
 *
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *        read only unless the Variant is a By Reference Variant type.
 *
 * @param rgdispidNamedArgs an array of identifiers for the arguments specified in rgvarg; the
 *        parameter IDs must be in the same order as their corresponding values;
 *        all arguments must have an identifier - identifiers can be obtained using 
 *        OleAutomation.getIDsOfNames
 *
 * @exception org.eclipse.swt.SWTException <ul>
 *		<li>ERROR_ACTION_NOT_PERFORMED when method invocation fails
 *	</ul>
 */
public void invokeNoReply(int dispIdMember, Variant[] rgvarg, int[] rgdispidNamedArgs) {
	int result = invoke(dispIdMember, COM.DISPATCH_METHOD, rgvarg, rgdispidNamedArgs, null);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_ACTION_NOT_PERFORMED, result);
}
private void manageExcepinfo(int hResult, EXCEPINFO excepInfo) {

	if (hResult == COM.S_OK){
		exceptionDescription = "No Error"; //$NON-NLS-1$
		return;
	}

	// extract exception info
	if (hResult == COM.DISP_E_EXCEPTION) {
		if (excepInfo.bstrDescription != 0){
			int size = COM.SysStringByteLen(excepInfo.bstrDescription);
			char[] buffer = new char[(size + 1) /2];
			COM.MoveMemory(buffer, excepInfo.bstrDescription, size);
			exceptionDescription = new String(buffer);
		} else {
			exceptionDescription = "OLE Automation Error Exception "; //$NON-NLS-1$
			if (excepInfo.wCode != 0){
				exceptionDescription += "code = "+excepInfo.wCode; //$NON-NLS-1$
			} else if (excepInfo.scode != 0){
				exceptionDescription += "code = "+excepInfo.scode; //$NON-NLS-1$
			}
		}
	} else {
		exceptionDescription = "OLE Automation Error HResult : " + hResult; //$NON-NLS-1$
	}

	// cleanup EXCEPINFO struct
	if (excepInfo.bstrDescription != 0)
		COM.SysFreeString(excepInfo.bstrDescription);
	if (excepInfo.bstrHelpFile != 0)
		COM.SysFreeString(excepInfo.bstrHelpFile);
	if (excepInfo.bstrSource != 0)
		COM.SysFreeString(excepInfo.bstrSource);
}
/**
 * Sets the property specified by the dispIdMember to a new value.
 *
 * @param dispIdMember the ID of the property as specified by the IDL of the ActiveX Control; the
 *                     value for the ID can be obtained using OleAutomation.getIDsOfNames
 * @param rgvarg the new value of the property
 *
 * @return true if the operation was successful
 */
public boolean setProperty(int dispIdMember, Variant rgvarg) {
	Variant[] rgvarg2 = new Variant[] {rgvarg};
	int[] rgdispidNamedArgs = new int[] {COM.DISPID_PROPERTYPUT};
	int dwFlags = COM.DISPATCH_PROPERTYPUT;
	if ((rgvarg.getType() & COM.VT_BYREF) == COM.VT_BYREF)
		dwFlags = COM.DISPATCH_PROPERTYPUTREF;
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, dwFlags, rgvarg2, rgdispidNamedArgs, pVarResult);
	return (result == COM.S_OK);
}
/**
 * Sets the property specified by the dispIdMember to a new value.
 *
 * @param dispIdMember the ID of the property as specified by the IDL of the ActiveX Control; the
 *                     value for the ID can be obtained using OleAutomation.getIDsOfNames
 * @param rgvarg an array of arguments for the method.  All arguments are considered to be
 *                     read only unless the Variant is a By Reference Variant type.
 *
 * @return true if the operation was successful
 *
 * @since 2.0
 */
public boolean setProperty(int dispIdMember, Variant[] rgvarg) {
	int[] rgdispidNamedArgs = new int[] {COM.DISPID_PROPERTYPUT};
	int dwFlags = COM.DISPATCH_PROPERTYPUT;
	for (int i = 0; i < rgvarg.length; i++) {
		if ((rgvarg[i].getType() & COM.VT_BYREF) == COM.VT_BYREF)
		dwFlags = COM.DISPATCH_PROPERTYPUTREF;
	}
	Variant pVarResult = new Variant();
	int result = invoke(dispIdMember, dwFlags, rgvarg, rgdispidNamedArgs, pVarResult);
	return (result == COM.S_OK);
}
}
