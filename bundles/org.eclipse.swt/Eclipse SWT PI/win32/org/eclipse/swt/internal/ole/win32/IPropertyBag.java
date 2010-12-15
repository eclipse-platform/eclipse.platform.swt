package org.eclipse.swt.internal.ole.win32;

public class IPropertyBag extends IUnknown {

public IPropertyBag(int /*long*/ address) {
	super(address);
}

public int Read (int /*long*/ pszPropName, int /*long*/ pVar, int /*long*/[] pErrorLog) {
	return COM.VtblCall(3, getAddress(), pszPropName, pVar, pErrorLog);
}

public int Write (int /*long*/ propName, int /*long*/[] var, int /*long*/[] errorLog) {
	return COM.VtblCall(4, getAddress(), propName, var, errorLog);
}

}
