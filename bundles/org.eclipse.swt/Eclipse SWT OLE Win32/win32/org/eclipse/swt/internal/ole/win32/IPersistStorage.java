package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IPersistStorage extends IPersist
{
public IPersistStorage(int address) {
	super(address);
}
public int IsDirty() {
	return COM.VtblCall(4, address);
}
public int InitNew(int pStg) {
	return COM.VtblCall(5, address, pStg);
}
public int Load(int pStg) {
	return COM.VtblCall(6, address, pStg);
}
public int Save(int pStgSave, boolean fSameAsLoad) {
	return COM.VtblCall(7, address, pStgSave, fSameAsLoad);
}
public int SaveCompleted(int pStgNew) {
	return COM.VtblCall(8, address, pStgNew);
}
public int HandsOffStorage(){
	return COM.VtblCall(9, address);
}
}
