/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;


public class IAccessible extends IDispatch {

public IAccessible(int address) {
	super(address);
}

public int get_accParent(int ppdispParent) {
	return COM.VtblCall(7, address, ppdispParent);
}
public int get_accChildCount(int pcountChildren) {
	return COM.VtblCall(8, address, pcountChildren);
}
public int get_accChild(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int ppdispChild) {
	return COM.VtblCall(9, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, ppdispChild);
}
public int get_accName(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszName) {
	return COM.VtblCall(10, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszName);
}
public int get_accValue(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszValue) {
	return COM.VtblCall(11, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszValue);
}
public int get_accDescription(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDescription) {
	return COM.VtblCall(12, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszDescription);
}
public int get_accRole(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pvarRole) {
	return COM.VtblCall(13, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pvarRole);
}
public int get_accState(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pvarState) {
	return COM.VtblCall(14, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pvarState);
}
public int get_accHelp(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszHelp) {
	return COM.VtblCall(15, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszHelp);
}
public int get_accHelpTopic(int pszHelpFile, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pidTopic) {
	return COM.VtblCall(16, address, pszHelpFile, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pidTopic);
}
public int get_accKeyboardShortcut(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszKeyboardShortcut) {
	return COM.VtblCall(17, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszKeyboardShortcut);
}
public int get_accFocus(int pvarChild) {
	return COM.VtblCall(18, address, pvarChild);
}
public int get_accSelection(int pvarChildren) {
	return COM.VtblCall(19, address, pvarChildren);
}
public int get_accDefaultAction(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int pszDefaultAction) {
	return COM.VtblCall(20, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, pszDefaultAction);
}
public int accSelect(int flagsSelect, int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
	return COM.VtblCall(21, address, flagsSelect, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
}
public int accLocation(int pxLeft, int pyTop, int pcxWidth, int pcyHeight,
	int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
	return COM.VtblCall(22, address, pxLeft, pyTop, pcxWidth, pcyHeight, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
}
public int accNavigate(int navDir, int varStart_vt, int varStart_reserved1, int varStart_lVal, int varStart_reserved2, int pvarEndUpAt) {
	return COM.VtblCall(23, address, navDir, varStart_vt, varStart_reserved1, varStart_lVal, varStart_reserved2, pvarEndUpAt);
}
public int accHitTest(int xLeft, int yTop, int pvarChild) {
	return COM.VtblCall(24, address, xLeft, yTop, pvarChild);
}
public int accDoDefaultAction(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2) {
	return COM.VtblCall(25, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2);
}
public int put_accName(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int szName) {
	return COM.VtblCall(26, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, szName);
}
public int put_accValue(int varChild_vt, int varChild_reserved1, int varChild_lVal, int varChild_reserved2, int szValue) {
	return COM.VtblCall(27, address, varChild_vt, varChild_reserved1, varChild_lVal, varChild_reserved2, szValue);
}
}
