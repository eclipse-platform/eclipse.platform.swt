/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public IAccessible(long /*int*/ address) {
	super(address);
}

public int get_accParent(long /*int*/ ppdispParent) {
	return COM.VtblCall(7, address, ppdispParent);
}
public int get_accChildCount(long /*int*/ pcountChildren) {
	return COM.VtblCall(8, address, pcountChildren);
}
public int get_accChild(long /*int*/ variant, long /*int*/ ppdispChild) {
	return COM.VtblCall_VARIANTP(9, address, variant, ppdispChild);
}
public int get_accName(long /*int*/ variant, long /*int*/ pszName) {
	return COM.VtblCall_VARIANTP(10, address, variant, pszName);
}
public int get_accValue(long /*int*/ variant, long /*int*/ pszValue) {
	return COM.VtblCall_VARIANTP(11, address, variant, pszValue);
}
public int get_accDescription(long /*int*/ variant, long /*int*/ pszDescription) {
	return COM.VtblCall_VARIANTP(12, address, variant, pszDescription);
}
public int get_accRole(long /*int*/ variant, long /*int*/ pvarRole) {
	return COM.VtblCall_VARIANTP(13, address, variant, pvarRole);
}
public int get_accState(long /*int*/ variant, long /*int*/ pvarState) {
	return COM.VtblCall_VARIANTP(14, address, variant, pvarState);
}
public int get_accHelp(long /*int*/ variant, long /*int*/ pszHelp) {
	return COM.VtblCall_VARIANTP(15, address, variant, pszHelp);
}
public int get_accHelpTopic(long /*int*/ pszHelpFile, long /*int*/ variant, long /*int*/ pidTopic) {
	return COM.VtblCall_PVARIANTP(16, address, pszHelpFile, variant, pidTopic);
}
public int get_accKeyboardShortcut(long /*int*/ variant, long /*int*/ pszKeyboardShortcut) {
	return COM.VtblCall_VARIANTP(17, address, variant, pszKeyboardShortcut);
}
public int get_accFocus(long /*int*/ pvarChild) {
	return COM.VtblCall(18, address, pvarChild);
}
public int get_accSelection(long /*int*/ pvarChildren) {
	return COM.VtblCall(19, address, pvarChildren);
}
public int get_accDefaultAction(long /*int*/ variant, long /*int*/ pszDefaultAction) {
	return COM.VtblCall_VARIANTP(20, address, variant, pszDefaultAction);
}
public int accSelect(int flagsSelect, long /*int*/ variant) {
	return COM.VtblCall_IVARIANT(21, address, flagsSelect, variant);
}
public int accLocation(long /*int*/ pxLeft, long /*int*/ pyTop, long /*int*/ pcxWidth, long /*int*/ pcyHeight, long /*int*/ variant) {
	return COM.VtblCall_PPPPVARIANT(22, address, pxLeft, pyTop, pcxWidth, pcyHeight, variant);
}
public int accNavigate(int navDir, long /*int*/ variant, long /*int*/ pvarEndUpAt) {
	return COM.VtblCall_IVARIANTP(23, address, navDir, variant, pvarEndUpAt);
}
public int accHitTest(int xLeft, int yTop, long /*int*/ pvarChild) {
	return COM.VtblCall(24, address, xLeft, yTop, pvarChild);
}
public int accDoDefaultAction(long /*int*/ variant) {
	return COM.VtblCall_VARIANT(25, address, variant);
}
public int put_accName(long /*int*/ variant, long /*int*/ szName) {
	return COM.VtblCall_VARIANTP(26, address, variant, szName);
}
public int put_accValue(long /*int*/ variant, long /*int*/ szValue) {
	return COM.VtblCall_VARIANTP(27, address, variant, szValue);
}
}
