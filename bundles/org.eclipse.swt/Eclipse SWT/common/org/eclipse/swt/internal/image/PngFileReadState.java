package org.eclipse.swt.internal.image;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
