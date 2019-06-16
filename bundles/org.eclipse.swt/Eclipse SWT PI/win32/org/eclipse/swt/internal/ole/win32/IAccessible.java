/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class IAccessible extends IDispatch {

public IAccessible(long address) {
	super(address);
}

public int get_accParent(long ppdispParent) {
	return OS.VtblCall(7, address, ppdispParent);
}
public int get_accChildCount(long pcountChildren) {
	return OS.VtblCall(8, address, pcountChildren);
}
public int get_accChild(long variant, long ppdispChild) {
	return COM.VtblCall_VARIANTP(9, address, variant, ppdispChild);
}
public int get_accName(long variant, long pszName) {
	return COM.VtblCall_VARIANTP(10, address, variant, pszName);
}
public int get_accValue(long variant, long pszValue) {
	return COM.VtblCall_VARIANTP(11, address, variant, pszValue);
}
public int get_accDescription(long variant, long pszDescription) {
	return COM.VtblCall_VARIANTP(12, address, variant, pszDescription);
}
public int get_accRole(long variant, long pvarRole) {
	return COM.VtblCall_VARIANTP(13, address, variant, pvarRole);
}
public int get_accState(long variant, long pvarState) {
	return COM.VtblCall_VARIANTP(14, address, variant, pvarState);
}
public int get_accHelp(long variant, long pszHelp) {
	return COM.VtblCall_VARIANTP(15, address, variant, pszHelp);
}
public int get_accHelpTopic(long pszHelpFile, long variant, long pidTopic) {
	return COM.VtblCall_PVARIANTP(16, address, pszHelpFile, variant, pidTopic);
}
public int get_accKeyboardShortcut(long variant, long pszKeyboardShortcut) {
	return COM.VtblCall_VARIANTP(17, address, variant, pszKeyboardShortcut);
}
public int get_accFocus(long pvarChild) {
	return OS.VtblCall(18, address, pvarChild);
}
public int get_accSelection(long pvarChildren) {
	return OS.VtblCall(19, address, pvarChildren);
}
public int get_accDefaultAction(long variant, long pszDefaultAction) {
	return COM.VtblCall_VARIANTP(20, address, variant, pszDefaultAction);
}
public int accSelect(int flagsSelect, long variant) {
	return COM.VtblCall_IVARIANT(21, address, flagsSelect, variant);
}
public int accLocation(long pxLeft, long pyTop, long pcxWidth, long pcyHeight, long variant) {
	return COM.VtblCall_PPPPVARIANT(22, address, pxLeft, pyTop, pcxWidth, pcyHeight, variant);
}
public int accNavigate(int navDir, long variant, long pvarEndUpAt) {
	return COM.VtblCall_IVARIANTP(23, address, navDir, variant, pvarEndUpAt);
}
public int accHitTest(int xLeft, int yTop, long pvarChild) {
	return COM.VtblCall(24, address, xLeft, yTop, pvarChild);
}
public int accDoDefaultAction(long variant) {
	return COM.VtblCall_VARIANT(25, address, variant);
}
public int put_accName(long variant, long szName) {
	return COM.VtblCall_VARIANTP(26, address, variant, szName);
}
public int put_accValue(long variant, long szValue) {
	return COM.VtblCall_VARIANTP(27, address, variant, szValue);
}
}
