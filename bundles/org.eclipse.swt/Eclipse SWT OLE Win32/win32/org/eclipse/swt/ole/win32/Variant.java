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
package org.eclipse.swt.ole.win32;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
/**
 *
 * A Variant is a generic OLE mechanism for passing data of different types via a common interface.
 *
 * <p>It is used within the OleAutomation object for getting a property, setting a property or invoking
 * a method on an OLE Control or OLE Document.
 *
 */
public final class Variant
{
	/**
	 * A variant always takes up 16 bytes, no matter what you 
	 * store in it. Objects, strings, and arrays are not physically 
	 * stored in the Variant; in these cases, four bytes of the 
	 * Variant are used to hold either an object reference, or a 
	 * pointer to the string or array. The actual data are stored elsewhere.
	 */
	public static final int sizeof = 16;
	private short type; // OLE.VT_* type
	
	private boolean booleanData;
	private float   floatData;
	private int     intData;
	private short   shortData;
	private String  stringData;
	private int     byRefPtr;
	private IDispatch dispatchData;
	private IUnknown unknownData;

/**
 * Create an empty Variant object with type VT_EMPTY.
 * 
 * @since 2.0
 */	
public Variant(){
	type = COM.VT_EMPTY;
}
/**
 * Create a Variant object which represents a Java float as a VT_R4.
 *
 * @param val the Java float value that this Variant represents
 *
 */
public Variant(float val) {
	type = COM.VT_R4;
	floatData = val;
	
}
/**
 * Create a Variant object which represents a Java int as a VT_I4.
 *
 * @param val the Java int value that this Variant represents
 *
 */
 public Variant(int val) {
	type = COM.VT_I4;
	intData = val;
}
/**
 * Create a Variant object which contains a reference to the data being transferred.
 *
 * <p>When creating a VT_BYREF Variant, you must give the full Variant type 
 * including VT_BYREF such as
 * 
 * <pre><code>short byRefType = OLE.VT_BSTR | OLE.VT_BYREF</code></pre>.
 *
 * @param ptr a pointer to the data being transferred.
 * @param byRefType the type of the data being transferred such as OLE.VT_BSTR | OLE.VT_BYREF
 *
 */
public Variant(int ptr, short byRefType) {
	type = byRefType;
	byRefPtr = ptr;
}
/**
 * Create a Variant object which represents an IDispatch interface as a VT_Dispatch.
 *
 * @param automation the OleAutomation object that this Variant represents
 * 
 */
public Variant(OleAutomation automation) {
	type = COM.VT_DISPATCH;
	dispatchData = new IDispatch(automation.getAddress());
}
/**
 * Create a Variant object which represents an IDispatch interface as a VT_Dispatch.
 * <p>The caller is expected to have appropriately invoked unknown.AddRef() before creating 
 * this Variant.
 * 
 * @since 2.0
 * 
 * @param idispatch the IDispatch object that this Variant represents
 * 
 */
public Variant(IDispatch idispatch) {
	type = COM.VT_DISPATCH;
	dispatchData = idispatch;
}
/**
 * Create a Variant object which represents an IUnknown interface as a VT_UNKNOWN.
 *
 * <p>The caller is expected to have appropriately invoked unknown.AddRef() before creating 
 * this Variant.
 *
 * @param unknown the IUnknown object that this Variant represents
 *
 */
public Variant(IUnknown unknown) {
	type = COM.VT_UNKNOWN;
	unknownData = unknown;
}
/**
 * Create a Variant object which represents a Java String as a VT_BSTR.
 *
 * @param string the Java String value that this Variant represents
 *
 */
public Variant(String string) {
	type = COM.VT_BSTR;
	stringData = string;
}
/**
 * Create a Variant object which represents a Java short as a VT_I2.
 *
 * @param val the Java short value that this Variant represents
 *
 */
public Variant(short val) {
	type = COM.VT_I2;
	shortData = val;
}
/**
 * Create a Variant object which represents a Java boolean as a VT_BOOL.
 *
 * @param val the Java boolean value that this Variant represents
 *
 */
public Variant(boolean val) {
	type = COM.VT_BOOL;
	booleanData = val;
}

/**
 * Calling dispose will release resources associated with this Variant.
 * If the resource is an IDispatch or IUnknown interface, Release will be called.
 * If the resource is a ByRef pointer, nothing is released.
 * 
 * @since 2.1
 */
public void dispose() {
	if ((type & COM.VT_BYREF) == COM.VT_BYREF) {
		return;
	}
		
	switch (type) {
		case COM.VT_EMPTY :
		case COM.VT_BOOL :
		case COM.VT_R4 :
		case COM.VT_I4 :
		case COM.VT_I2 :
		case COM.VT_BSTR :
			break;
		case COM.VT_DISPATCH :
			dispatchData.Release();
			break;
		case COM.VT_UNKNOWN :
			unknownData.Release();
			break;
	}
	
}
/**
 * Returns the OleAutomation object represented by this Variant.
 *
 * <p>If this Variant does not contain an OleAutomation object, an attempt is made to
 * coerce the Variant type into an OleAutomation object.  If this fails, an error is
 * thrown.  Note that OleAutomation objects must be disposed when no longer
 * needed.
 *
 * @return the OleAutomation object represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into an OleAutomation object</li>
 * </ul>
 */
public OleAutomation getAutomation() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_DISPATCH) {
		return new OleAutomation(dispatchData);
	}
	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_DISPATCH);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant autoVar = new Variant();
		autoVar.setData(newPtr);
		return autoVar.getAutomation();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr); // Note: This must absolutely be done AFTER the 
		                          // OleAutomation object is created as Variant Clear 
		                          // will result in a Release being performed on the 
		                          // Dispatch object
		OS.GlobalFree(newPtr);
	}
}
/**
 * Returns the IDispatch object represented by this Variant.
 *
 * <p>If this Variant does not contain an IDispatch object, an attempt is made to
 * coerce the Variant type into an IDIspatch object.  If this fails, an error is
 * thrown.
 *
 * @since 2.0
 * 
 * @return the IDispatch object represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into an IDispatch object</li>
 * </ul>
 */
public IDispatch getDispatch() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_DISPATCH) {
		return dispatchData;
	}
	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_DISPATCH);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant autoVar = new Variant();
		autoVar.setData(newPtr);
		return autoVar.getDispatch();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr); // Note: This must absolutely be done AFTER the 
		                          // OleAutomation object is created as Variant Clear 
		                          // will result in a Release being performed on the 
		                          // Dispatch object
		OS.GlobalFree(newPtr);
	}
}
/**
 * Returns the Java boolean represented by this Variant.
 *
 * <p>If this Variant does not contain a Java boolean, an attempt is made to
 * coerce the Variant type into a Java boolean.  If this fails, an error is thrown.
 *
 * @return the Java boolean represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into a boolean</li>
 * </ul>
 *
 */
public boolean getBoolean() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_BOOL) {
		return booleanData;
	}

	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_BOOL);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant boolVar = new Variant();
		boolVar.setData(newPtr);
		return boolVar.getBoolean();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr);
		OS.GlobalFree(newPtr);
	}
}
/**
 * Returns a pointer to the referenced data represented by this Variant.
 *
 * <p>If this Variant does not contain a reference to data, zero is returned.
 *
 * @return a pointer to the referenced data represented by this Variant or 0
 *
 */
public int getByRef() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if ((type & COM.VT_BYREF)== COM.VT_BYREF) {
		return byRefPtr;
	}
		
	return 0;
}
void getData(int pData){
	if (pData == 0) OLE.error(OLE.ERROR_OUT_OF_MEMORY);
	
	COM.VariantInit(pData);

	if ((type & COM.VT_BYREF) == COM.VT_BYREF) {
		COM.MoveMemory(pData, new short[] {type}, 2);
		COM.MoveMemory(pData + 8, new int[]{byRefPtr}, 4);
		return;
	}

	switch (type) {
		case COM.VT_EMPTY :
			break;
		case COM.VT_BOOL :
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new int[]{(booleanData) ? COM.VARIANT_TRUE : COM.VARIANT_FALSE}, 2);
			break;
		case COM.VT_R4 :
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new float[]{floatData}, 4);
			break;
		case COM.VT_I4 :
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new int[]{intData}, 4);
			break;
		case COM.VT_DISPATCH :
			dispatchData.AddRef();
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new int[]{dispatchData.getAddress()}, 4);
			break;
		case COM.VT_UNKNOWN :
			unknownData.AddRef();
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new int[]{unknownData.getAddress()}, 4);
			break;
		case COM.VT_I2 :
			COM.MoveMemory(pData, new short[] {type}, 2);
			COM.MoveMemory(pData + 8, new short[]{shortData}, 2);
			break;
		case COM.VT_BSTR :
			COM.MoveMemory(pData, new short[] {type}, 2);
			char[] data = (stringData+"\0").toCharArray();
			int ptr = COM.SysAllocString(data);
			COM.MoveMemory(pData + 8, new int[] {ptr}, 4);
			break;
	
		default :
			OLE.error(SWT.ERROR_NOT_IMPLEMENTED);
	}
}
/**
 * Returns the Java float represented by this Variant.
 *
 * <p>If this Variant does not contain a Java float, an attempt is made to
 * coerce the Variant type into a Java float.  If this fails, an error is thrown.
 *
 * @return the Java float represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into a float</li>
 * </ul>
 */
public float getFloat() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_R4) {
		return floatData;
	}

	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_R4);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant floatVar = new Variant();
		floatVar.setData(newPtr);
		return floatVar.getFloat();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr);
		OS.GlobalFree(newPtr);
	}
	
}
/**
 * Returns the Java int represented by this Variant.
 *
 * <p>If this Variant does not contain a Java int, an attempt is made to
 * coerce the Variant type into a Java int.  If this fails, an error is thrown.
 *
 * @return the Java int represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into a int</li>
 * </ul>
 */
public int getInt() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_I4) {
		return intData;
	}
		
	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_I4);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant intVar = new Variant();
		intVar.setData(newPtr);
		return intVar.getInt();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr);
		OS.GlobalFree(newPtr);
	}
}
/**
 * Returns the Java short represented by this Variant.
 *
 * <p>If this Variant does not contain a Java short, an attempt is made to
 * coerce the Variant type into a Java short.  If this fails, an error is thrown.
 *
 * @return the Java short represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into a short</li>
 * </ul>
 */
public short getShort() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_I2) {
		return shortData;
	}
		
	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_I2);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant shortVar = new Variant();
		shortVar.setData(newPtr);
		return shortVar.getShort();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr);
		OS.GlobalFree(newPtr);
	}
	
}
/**
 * Returns the Java String represented by this Variant.
 *
 * <p>If this Variant does not contain a Java String, an attempt is made to
 * coerce the Variant type into a Java String.  If this fails, an error is thrown.
 *
 * @return the Java String represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into a String</li>
 * </ul>
 */
public String getString() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_BSTR) {
		return stringData;
	}

	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_BSTR);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);

		Variant stringVar = new Variant();
		stringVar.setData(newPtr);
		return stringVar.getString();
			
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr);
		OS.GlobalFree(newPtr);
	}
}
/**
 * Returns the type of the variant type.  This will be an OLE.VT_* value or
 * a bitwise combination of OLE.VT_* values as in the case of 
 * OLE.VT_BSTR | OLE.VT_BYREF.
 * 
 * @return the type of the variant data
 * 
 * @since 2.0
 */
public short getType() {
	return type;
}
/**
 * Returns the IUnknown object represented by this Variant.
 *
 * <p>If this Variant does not contain an IUnknown object, an attempt is made to
 * coerce the Variant type into an IUnknown object.  If this fails, an error is
 * thrown.
 *
 * @return the IUnknown object represented by this Variant
 *
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant can not be coerced into 
 *			an IUnknown object</li>
 * </ul>
 */
public IUnknown getUnknown() {
	if (type == COM.VT_EMPTY) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, -1);
	}
	if (type == COM.VT_UNKNOWN) {
		return unknownData;
	}

	// try to coerce the value to the desired type
	int oldPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	int newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, sizeof);
	try {
		getData(oldPtr);
		int result = COM.VariantChangeType(newPtr, oldPtr, (short) 0, COM.VT_UNKNOWN);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE, result);
		Variant unknownVar = new Variant();
		unknownVar.setData(newPtr);
		return unknownVar.getUnknown();
	} finally {
		COM.VariantClear(oldPtr);
		OS.GlobalFree(oldPtr);
		COM.VariantClear(newPtr); // Note: This must absolutely be done AFTER the 
		                          // IUnknown object is created as Variant Clear 
		                          // will result in a Release being performed on the 
		                          // Dispatch object
		OS.GlobalFree(newPtr);
	}
}
/**
 * Update the by reference value of this variant with a new boolean value.
 * 
 * @param val the new boolean value
 * 
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant is not 
 *			a (VT_BYREF | VT_BOOL) object</li>
 * </ul>
 *
 * @since 2.1
 */
public void setByRef(boolean val) {
	if ((type & COM.VT_BYREF) == 0 || (type & COM.VT_BOOL) == 0) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE);
	}
	COM.MoveMemory(byRefPtr, new short[]{val ? COM.VARIANT_TRUE : COM.VARIANT_FALSE}, 2);
}
/**
 * Update the by reference value of this variant with a new float value.
 * 
 * @param val the new float value
 * 
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant is not 
 *			a (VT_BYREF | VT_R4) object</li>
 * </ul>
 *
 * @since 2.1
 */
public void setByRef(float val) {
	if ((type & COM.VT_BYREF) == 0 || (type & COM.VT_R4) == 0) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE);
	}
	COM.MoveMemory(byRefPtr, new float[]{val}, 4);	
}
/**
 * Update the by reference value of this variant with a new integer value.
 * 
 * @param val the new integer value
 * 
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant is not a (VT_BYREF | VT_I4) object</li>
 * </ul>
 *
 * @since 2.1
 */
public void setByRef(int val) {
	if ((type & COM.VT_BYREF) == 0 || (type & COM.VT_I4) == 0) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE);
	}
	COM.MoveMemory(byRefPtr, new int[]{val}, 4);
}
/**
 * Update the by reference value of this variant with a new short value.
 * 
 * @param val the new short value
 * 
 * @exception SWTException <ul>
 *     <li>ERROR_CANNOT_CHANGE_VARIANT_TYPE when type of Variant is not a (VT_BYREF | VT_I2) object
 * </ul>
 *
 * @since 2.1
 */
public void setByRef(short val) {
	if ((type & COM.VT_BYREF) == 0 || (type & COM.VT_I2) == 0) {
		OLE.error(OLE.ERROR_CANNOT_CHANGE_VARIANT_TYPE);
	}
	COM.MoveMemory(byRefPtr, new short[]{val}, 2);
}

void setData(int pData){
	if (pData == 0) OLE.error(OLE.ERROR_INVALID_ARGUMENT);

	short[] dataType = new short[1];
	COM.MoveMemory(dataType, pData, 2);
	type = dataType[0];

	if ((type & COM.VT_BYREF) == COM.VT_BYREF) {
		int[] newByRefPtr = new int[1];
		OS.MoveMemory(newByRefPtr, pData + 8, 4);
		byRefPtr = newByRefPtr[0];
		return;
	}
	
	switch (type) {
		case COM.VT_EMPTY :
			break;
		case COM.VT_BOOL :
			short[] newBooleanData = new short[1];
			COM.MoveMemory(newBooleanData, pData + 8, 2);
			booleanData = (newBooleanData[0] != COM.VARIANT_FALSE);
			break;
		case COM.VT_R4 :
			float[] newFloatData = new float[1];
			COM.MoveMemory(newFloatData, pData + 8, 4);
			floatData = newFloatData[0];
			break;
		case COM.VT_I4 :
			int[] newIntData = new int[1];
			OS.MoveMemory(newIntData, pData + 8, 4);
			intData = newIntData[0];
			break;
		case COM.VT_DISPATCH : {
			int[] ppvObject = new int[1];
			OS.MoveMemory(ppvObject, pData + 8, 4);
			if (ppvObject[0] == 0) {
				type = COM.VT_EMPTY;
				break;
			}
			dispatchData = new IDispatch(ppvObject[0]);
			dispatchData.AddRef();
			break;
		}
		case COM.VT_UNKNOWN : {
			int[] ppvObject = new int[1];
			OS.MoveMemory(ppvObject, pData + 8, 4);
			if (ppvObject[0] == 0) {
				type = COM.VT_EMPTY;
				break;
			}
			unknownData = new IUnknown(ppvObject[0]);
			unknownData.AddRef();
			break;
		}
		case COM.VT_I2 :
			short[] newShortData = new short[1];
			COM.MoveMemory(newShortData, pData + 8, 2);
			shortData = newShortData[0];
			break;
		case COM.VT_BSTR :
			// get the address of the memory in which the string resides
			int[] hMem = new int[1];
			OS.MoveMemory(hMem, pData + 8, 4);
			if (hMem[0] == 0) {
				type = COM.VT_EMPTY;
				break;
			}
			// Get the size of the string from the OS - the size is expressed in number
			// of bytes - each unicode character is 2 bytes.
			int size = COM.SysStringByteLen(hMem[0]);
			if (size > 0){
				// get the unicode character array from the global memory and create a String
				char[] buffer = new char[(size + 1) /2]; // add one to avoid rounding errors
				COM.MoveMemory(buffer, hMem[0], size);
				stringData = new String(buffer);
			} else {
				stringData = ""; //$NON-NLS-1$
			}
			break;
	
		default :
			// try coercing it into one of the known forms
			int newPData = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, Variant.sizeof);
			if (COM.VariantChangeType(newPData, pData, (short) 0, COM.VT_R4) == COM.S_OK) {
				setData(newPData);
			} else if (COM.VariantChangeType(newPData, pData, (short) 0, COM.VT_I4) == COM.S_OK) {
				setData(newPData);
			} else if (COM.VariantChangeType(newPData, pData, (short) 0, COM.VT_BSTR) == COM.S_OK) {
				setData(newPData);
			}
			COM.VariantClear(newPData);
			OS.GlobalFree(newPData);
			break;
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
    switch (type) {
	    case COM.VT_BOOL :
	        return "VT_BOOL{"+booleanData+"}";
	    case COM.VT_I2 :
	        return "VT_I2{"+shortData+"}";
	    case COM.VT_I4 :
	        return "VT_I4{"+intData+"}";
	    case COM.VT_R4 :
	        return "VT_R4{"+floatData+"}";
	    case COM.VT_BSTR :
	        return "VT_BSTR{"+stringData+"}";
	    case COM.VT_DISPATCH :
	        return "VT_DISPATCH{"+(dispatchData == null ? 0 : dispatchData.getAddress())+"}";
	    case COM.VT_UNKNOWN :
	        return "VT_UNKNOWN{"+(unknownData == null ? 0 : unknownData.getAddress())+"}";
	    case COM.VT_EMPTY :
	        return "VT_EMPTY";
	 }
    if ((type & COM.VT_BYREF) != 0) {
        return "VT_BYREF|"+(type & ~COM.VT_BYREF)+"{"+byRefPtr+"}";
    }
    return "Unsupported Type "+type;
}
}
