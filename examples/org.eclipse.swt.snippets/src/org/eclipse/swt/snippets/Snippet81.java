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
 * OLE and ActiveX example snippet: browse the typelibinfo for a program id (win32 only)
 * NOTE: This snippet uses internal SWT packages that are
 * subject to change without notice.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

public class Snippet81 {
	
public static void main(String[] args) {
	
	if (args.length == 0) {
		System.out.println("Usage: java Main <program id>");
		return;
	}

	String progID = args[0];
	
	Display display = new Display();
	Shell shell = new Shell(display);

	OleFrame frame = new OleFrame(shell, SWT.NONE);
	OleControlSite site = null;
	OleAutomation auto = null;
	try {
		site = new OleControlSite(frame, SWT.NONE, progID);	
		auto = new OleAutomation(site);
	} catch (SWTException ex) {
		System.out.println("Unable to open type library for "+progID);
		display.dispose();
		return;
	}
		
	printTypeInfo(auto);
	
	auto.dispose();
	shell.dispose();
	display.dispose();

}

private static void printTypeInfo(OleAutomation auto) {
	TYPEATTR typeattr = auto.getTypeInfoAttributes();
	if (typeattr != null) {		
		if (typeattr.cFuncs > 0) System.out.println("Functions :\n");
		for (int i = 0; i < typeattr.cFuncs; i++) {			
			OleFunctionDescription data = auto.getFunctionDescription(i);
			String argList = "";
			int firstOptionalArgIndex = data.args.length - data.optionalArgCount;
			for (int j = 0; j < data.args.length; j++) {
				argList += "[";
				if (j >= firstOptionalArgIndex) argList += "optional, ";
				argList += getDirection(data.args[j].flags)+"] "+getTypeName(data.args[j].type)+" "+data.args[j].name;
				if ( j < data.args.length - 1) argList += ", ";
			}			
			System.out.println(getInvokeKind(data.invokeKind)+" (id = "+data.id+") : "
					        +"\n\tSignature   : "+getTypeName(data.returnType)+" "+data.name+"("+argList+")"
			                    +"\n\tDescription : "+data.documentation
			                    +"\n\tHelp File   : "+data.helpFile+"\n");
		}
		
		if (typeattr.cVars > 0) System.out.println("\n\nVariables  :\n");
		for (int i = 0; i < typeattr.cVars; i++) {
			OlePropertyDescription data = auto.getPropertyDescription(i);
			System.out.println("PROPERTY (id = "+data.id+") :"
			                    +"\n\tName : "+data.name
			                    +"\n\tType : "+getTypeName(data.type)+"\n");
		}
	}
}
private static String getTypeName(int type) {
	switch (type) {
		case OLE.VT_BOOL : return "boolean";
		case OLE.VT_R4 : return "float";
		case OLE.VT_R8 : return "double";
		case OLE.VT_I4 : return "int";
		case OLE.VT_DISPATCH : return "IDispatch";
		case OLE.VT_UNKNOWN : return "IUnknown";
		case OLE.VT_I2 : return "short";
		case OLE.VT_BSTR : return "String";
		case OLE.VT_VARIANT : return "Variant";
		case OLE.VT_CY : return "Currency";
		case OLE.VT_DATE : return "Date";
		case OLE.VT_UI1 : return "unsigned char";
		case OLE.VT_UI4 : return "unsigned int";
		case OLE.VT_USERDEFINED : return "UserDefined";
		case OLE.VT_HRESULT : return "int";
		case OLE.VT_VOID : return "void";
		
		case OLE.VT_BYREF | OLE.VT_BOOL : return "boolean *";
		case OLE.VT_BYREF | OLE.VT_R4 : return "float *";
		case OLE.VT_BYREF | OLE.VT_R8 : return "double *";
		case OLE.VT_BYREF | OLE.VT_I4 : return "int *";
		case OLE.VT_BYREF | OLE.VT_DISPATCH : return "IDispatch *";
		case OLE.VT_BYREF | OLE.VT_UNKNOWN : return "IUnknown *";
		case OLE.VT_BYREF | OLE.VT_I2 : return "short *";
		case OLE.VT_BYREF | OLE.VT_BSTR : return "String *";
		case OLE.VT_BYREF | OLE.VT_VARIANT : return "Variant *";
		case OLE.VT_BYREF | OLE.VT_CY : return "Currency *";
		case OLE.VT_BYREF | OLE.VT_DATE : return "Date *";
		case OLE.VT_BYREF | OLE.VT_UI1 : return "unsigned char *";
		case OLE.VT_BYREF | OLE.VT_UI4 : return "unsigned int *";
		case OLE.VT_BYREF | OLE.VT_USERDEFINED : return "UserDefined *";
	}
	return "unknown "+ type;	
}
private static String getDirection(int direction){
	String dirString = "";
	boolean comma = false;
	if ((direction & OLE.IDLFLAG_FIN) != 0) {
		dirString += "in";
		comma = true;
	}
	if ((direction & OLE.IDLFLAG_FOUT) != 0){
		if (comma) dirString += ", ";
		dirString += "out";
		comma = true;
	}
	if ((direction & OLE.IDLFLAG_FLCID) != 0){
		if (comma) dirString += ", ";
		dirString += "lcid";
		comma = true;
	}
	if ((direction & OLE.IDLFLAG_FRETVAL) != 0){
		if (comma) dirString += ", "; 
		dirString += "retval";
	}
	
	return dirString;
}
private static String getInvokeKind(int invKind) {
	switch (invKind) {
		case OLE.INVOKE_FUNC : return "METHOD";
		case OLE.INVOKE_PROPERTYGET : return "PROPERTY GET";
		case OLE.INVOKE_PROPERTYPUT : return "PROPERTY PUT";
		case OLE.INVOKE_PROPERTYPUTREF : return "PROPERTY PUT BY REF";
	}
	return "unknown "+invKind;
}
}
