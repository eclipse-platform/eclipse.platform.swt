package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public class XmTextVerifyCallbackStruct extends XmAnyCallbackStruct {
	public byte doit;		// 8
	public int currInsert;	// 12
	public int newInsert;	// 16
	public int startPos;	// 20
	public int endPos;		// 24
	public int text;		// 28
	public static final int sizeof = 32;
}
