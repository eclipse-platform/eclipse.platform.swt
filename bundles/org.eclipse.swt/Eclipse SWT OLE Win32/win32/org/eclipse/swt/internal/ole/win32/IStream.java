package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IStream extends IUnknown {
public IStream(int address) {
	super(address);
}
public int Clone(
	int[] ppstm  //Pointer to location for pointer to the new stream object
){
	return COM.VtblCall(13, address, ppstm);
}
public int Commit( int grfCommitFlags  //Specifies how changes are committed
){
	return COM.VtblCall(8, address, grfCommitFlags);
}
public int Read(int pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(3, address, pv, cb, pcbWritten);
}
public int Revert(){
	return COM.VtblCall(9, address);
}
public int Write(int pv, int cb, int[] pcbWritten) {
	return COM.VtblCall(4, address, pv, cb, pcbWritten);
}
}
