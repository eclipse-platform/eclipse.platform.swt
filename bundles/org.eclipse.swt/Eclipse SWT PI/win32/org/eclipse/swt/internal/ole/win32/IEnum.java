package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class IEnum extends IUnknown
{
public IEnum(int address) {
	super(address);
}
public int Clone( int[] ppenum  ){
	return COM.VtblCall(6, address, ppenum);
}
public int Next(int celt, int rgelt, int[] pceltFetched  ){
	return COM.VtblCall(3, address, celt, rgelt, pceltFetched);
}
public int Reset() {
	return COM.VtblCall(5, address);
}
public int Skip(int celt){
	return COM.VtblCall(4, address, celt);
}
}
