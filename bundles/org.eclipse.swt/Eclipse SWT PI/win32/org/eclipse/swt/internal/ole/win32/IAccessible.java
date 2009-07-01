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

public IAccessible(int /*long*/ address) {
	super(address);
}

public int get_accParent(int /*long*/ ppdispParent) {
	return COM.VtblCall(7, address, ppdispParent);
}
public int get_accChildCount(int /*long*/ pcountChildren) {
	return COM.VtblCall(8, address, pcountChildren);
}
public int get_accChild(int /*long*/ variant, int /*long*/ ppdispChild) {
	return COM.VtblCall_VARIANTP(9, address, variant, ppdispChild);
}
public int get_accName(int /*long*/ variant, int /*long*/ pszName) {
	return COM.VtblCall_VARIANTP(10, address, variant, pszName);
}
public int get_accValue(int /*long*/ variant, int /*long*/ pszValue) {
	return COM.VtblCall_VARIANTP(11, address, variant, pszValue);
}
public int get_accDescription(int /*long*/ variant, int /*long*/ pszDescription) {
	return COM.VtblCall_VARIANTP(12, address, variant, pszDescription);
}
public int get_accRole(int /*long*/ variant, int /*long*/ pvarRole) {
	return COM.VtblCall_VARIANTP(13, address, variant, pvarRole);
}
public int get_accState(int /*long*/ variant, int /*long*/ pvarState) {
	return COM.VtblCall_VARIANTP(14, address, variant, pvarState);
}
public int get_accHelp(int /*long*/ variant, int /*long*/ pszHelp) {
	return COM.VtblCall_VARIANTP(15, address, variant, pszHelp);
}
public int get_accHelpTopic(int /*long*/ pszHelpFile, int /*long*/ variant, int /*long*/ pidTopic) {
	return COM.VtblCall_PVARIANTP(16, address, pszHelpFile, variant, pidTopic);
}
public int get_accKeyboardShortcut(int /*long*/ variant, int /*long*/ pszKeyboardShortcut) {
	return COM.VtblCall_VARIANTP(17, address, variant, pszKeyboardShortcut);
}
public int get_accFocus(int /*long*/ pvarChild) {
	return COM.VtblCall(18, address, pvarChild);
}
public int get_accSelection(int /*long*/ pvarChildren) {
	return COM.VtblCall(19, address, pvarChildren);
}
public int get_accDefaultAction(int /*long*/ variant, int /*long*/ pszDefaultAction) {
	return COM.VtblCall_VARIANTP(20, address, variant, pszDefaultAction);
}
public int accSelect(int flagsSelect, int /*long*/ variant) {
	return COM.VtblCall_IVARIANT(21, address, flagsSelect, variant);
}
public int accLocation(int /*long*/ pxLeft, int /*long*/ pyTop, int /*long*/ pcxWidth, int /*long*/ pcyHeight, int /*long*/ variant) {
	return COM.VtblCall_PPPPVARIANT(22, address, pxLeft, pyTop, pcxWidth, pcyHeight, variant);
}
public int accNavigate(int navDir, int /*long*/ variant, int /*long*/ pvarEndUpAt) {
	return COM.VtblCall_IVARIANTP(23, address, navDir, variant, pvarEndUpAt);
}
public int accHitTest(int xLeft, int yTop, int /*long*/ pvarChild) {
	return COM.VtblCall(24, address, xLeft, yTop, pvarChild);
}
public int accDoDefaultAction(int /*long*/ variant) {
	return COM.VtblCall_VARIANT(25, address, variant);
}
public int put_accName(int /*long*/ variant, int /*long*/ szName) {
	return COM.VtblCall_VARIANTP(26, address, variant, szName);
}
public int put_accValue(int /*long*/ variant, int /*long*/ szValue) {
	return COM.VtblCall_VARIANTP(27, address, variant, szValue);
}
}
