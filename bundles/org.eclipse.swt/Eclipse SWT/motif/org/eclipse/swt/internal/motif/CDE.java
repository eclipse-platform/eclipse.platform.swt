package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class CDE extends OS {

public static final String DtDTS_DA_ACTION_LIST   = "ACTIONS";
public static final String DtDTS_DA_ICON          = "ICON";
public static final String DtDTS_DA_MIME_TYPE     = "MIME_TYPE";
public static final String DtDTS_DA_NAME_TEMPLATE = "NAME_TEMPLATE";


public static final native boolean DtAppInitialize( 
	int appContext, 
	int display, 
	int topWiget,
	byte[] appName, 
	byte[] appClass ); 
public static final native void DtDbLoad();
public static final native int  DtDtsDataTypeNames();
public static final native int  DtDtsFileToDataType( byte[] fileName );
public static final native boolean DtDtsDataTypeIsAction( byte[] dataType );
public static final native int  DtDtsDataTypeToAttributeValue(
	byte[] dataType, 
	byte[] attrName, 
	byte[] optName );
public static final native void DtDtsFreeDataType(int dataType);
public static final native void DtDtsFreeDataTypeNames( int dataTypeList );
public static final native void DtDtsFreeAttributeValue( int attrValue );
public static final native int  DtActionInvoke(
	int    topWidget,
	byte[] action,
	byte[] fileName,   // TBD: argument should be: DtActionArg[] args
	int    argCount,
	byte[] termOpts,
	byte[] execHost,
	byte[] contextDir,
	int    useIndicator,
	int    callback,
	int    clientData );
	
// Utility methods for processing CDE related structures.

public static final native int listElementAt( int nameList, int index );
}
