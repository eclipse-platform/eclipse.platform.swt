package org.eclipse.swt.internal.image;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

class PngFileReadState extends Object {
	boolean readIHDR;
	boolean readPLTE;
	boolean readIDAT;
	boolean readIEND;
	
	// Non - critical chunks
	boolean readBKGD;
	boolean readTRNS;
	
	// Set to true after IDATs have been read.
	boolean readPixelData;
}