package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

public class MenuItem extends Item {
	Menu parent, menu;
	int id, accelerator;

public MenuItem (Menu parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

public MenuItem (Menu parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

public void _setEnabled (boolean enabled) {
	short [] outIndex = new short [1];
	OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex);
	int outMenuRef [] = new int [1];
	OS.GetMenuItemHierarchicalMenu (parent.handle, outIndex [0], outMenuRef);
	if (enabled) {
		if (outMenuRef [0] != 0) OS.EnableMenuItem (outMenuRef [0], (short) 0);
		OS.EnableMenuCommand (parent.handle, id);
	} else {
		if (outMenuRef [0] != 0) OS.DisableMenuItem (outMenuRef [0], (short) 0);
		OS.DisableMenuCommand (parent.handle, id);
	}
}

public void addArmListener (ArmListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Arm, typedListener);
}

public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
}

public int getAccelerator () {
	checkWidget ();
	return accelerator;
}

public Display getDisplay () {
	Menu parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

public Menu getMenu () {
	checkWidget ();
	return menu;
}

String getNameText () {
	if ((style & SWT.SEPARATOR) != 0) return "|";
	return super.getNameText ();
}

public Menu getParent () {
	checkWidget ();
	return parent;
}

public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	char [] outMark = new char [1];
	if (OS.GetMenuCommandMark (parent.handle, id, outMark) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	return outMark [0] != 0;
}

int kEventProcessCommand (int nextHandler, int theEvent, int userData) {
	//TEMPORARY CODE
	if (!isEnabled ()) return OS.noErr;

	if ((style & SWT.CHECK) != 0) {
		setSelection (!getSelection ());
	} else {
		if ((style & SWT.RADIO) != 0) {
			if ((parent.getStyle () & SWT.NO_RADIO_GROUP) != 0) {
				setSelection (!getSelection ());
			} else {
				selectRadio ();
			}
		}
	}
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	Event event = new Event ();
	setInputState (event, (short) 0, OS.GetCurrentEventButtonState (), modifiers [0]);
	postEvent (SWT.Selection, event);
	return OS.noErr;
}

public boolean isEnabled () {
	return getEnabled () && parent.isEnabled ();
}

int keyGlyph (int key) {
	switch (key) {
		case SWT.BS: return OS.kMenuDeleteLeftGlyph;
		case SWT.CR: return OS.kMenuReturnGlyph;
		case SWT.DEL: return OS.kMenuDeleteRightGlyph;
		case SWT.ESC: return OS.kMenuEscapeGlyph;
		case SWT.LF: return OS.kMenuReturnGlyph;
		case SWT.TAB: return OS.kMenuTabRightGlyph;
		case ' ': return OS.kMenuBlankGlyph;
//		case ' ': return OS.kMenuSpaceGlyph;
		case SWT.ALT: return OS.kMenuOptionGlyph;
		case SWT.SHIFT: return OS.kMenuShiftGlyph;
		case SWT.CONTROL: return OS.kMenuControlISOGlyph;
		case SWT.COMMAND: return OS.kMenuCommandGlyph;
		case SWT.ARROW_UP: return OS.kMenuUpArrowGlyph;
		case SWT.ARROW_DOWN: return OS.kMenuDownArrowGlyph;
		case SWT.ARROW_LEFT: return OS.kMenuLeftArrowGlyph;
		case SWT.ARROW_RIGHT: return OS.kMenuRightArrowGlyph;
		case SWT.PAGE_UP: return OS.kMenuPageUpGlyph;
		case SWT.PAGE_DOWN: return OS.kMenuPageDownGlyph;
		case SWT.F1: return OS.kMenuF1Glyph;
		case SWT.F2: return OS.kMenuF2Glyph;
		case SWT.F3: return OS.kMenuF3Glyph;
		case SWT.F4: return OS.kMenuF4Glyph;
		case SWT.F5: return OS.kMenuF5Glyph;
		case SWT.F6: return OS.kMenuF6Glyph;
		case SWT.F7: return OS.kMenuF7Glyph;
		case SWT.F8: return OS.kMenuF8Glyph;
		case SWT.F9: return OS.kMenuF9Glyph;
		case SWT.F10: return OS.kMenuF10Glyph;
		case SWT.F11: return OS.kMenuF11Glyph;
		case SWT.F12: return OS.kMenuF12Glyph;
	}
	return OS.kMenuNullGlyph;
}

void releaseChild () {
	super.releaseChild ();
	if (menu != null) menu.dispose ();
	menu = null;
	parent.destroyItem (this);
}

void releaseWidget () {
	if (menu != null) {
		menu.releaseWidget ();
		menu.destroyWidget ();
	} else {
		if ((parent.style & SWT.BAR) != 0) {
//			short [] outIndex = new short [1];
//			if (OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex) == OS.noErr) {
//				int [] outMenuRef = new int [1];
//				OS.GetMenuItemHierarchicalMenu (parent.handle, outIndex [0], outMenuRef);
//				if (outMenuRef [0] != 0) {
//					OS.DeleteMenu (OS.GetMenuID (outMenuRef [0]));
//					OS.DisposeMenu (outMenuRef [0]);
//				}
//			}
		}
	}
	menu = null;
	super.releaseWidget ();
	accelerator = 0;
	if (this == parent.defaultItem) parent.defaultItem = null;
	Display display = getDisplay ();
	display.removeMenuItem (this);
	parent = null;
}

public void removeArmListener (ArmListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Arm, listener);
}

public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void selectRadio () {
	int index = 0;
	MenuItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

public void setAccelerator (int accelerator) {
	checkWidget ();
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex) != OS.noErr) {
		return;
	}
	boolean update = (this.accelerator == 0 && accelerator != 0) || (this.accelerator != 0 && accelerator == 0);
	this.accelerator = accelerator;
	boolean inSetVirtualKey = false;
	int inModifiers = OS.kMenuNoModifiers, inGlyph = OS.kMenuNullGlyph, inKey = 0;
	if (accelerator != 0) {
		inKey = accelerator & SWT.KEY_MASK;
		inGlyph = keyGlyph (inKey);
		int virtualKey = Display.untranslateKey (inKey);
		if (inKey == ' ') virtualKey = 49;
		if (virtualKey != 0) {
			inSetVirtualKey = true;
			inKey = virtualKey;
		} else {
			inKey = Character.toUpperCase ((char)inKey);
		}
		inModifiers = (byte) OS.kMenuNoCommandModifier;
		if ((accelerator & SWT.SHIFT) != 0) inModifiers |= OS.kMenuShiftModifier;
		if ((accelerator & SWT.CONTROL) != 0) inModifiers |= OS.kMenuControlModifier;
		if ((accelerator & SWT.COMMAND) != 0) inModifiers &= ~OS.kMenuNoCommandModifier;
		if ((accelerator & SWT.ALT) != 0) inModifiers |= OS.kMenuOptionModifier;
	}
	OS.SetMenuItemModifiers (parent.handle, outIndex [0], (byte)inModifiers);
	OS.SetMenuItemCommandKey (parent.handle, outIndex [0], inSetVirtualKey, (char)inKey);
	OS.SetMenuItemKeyGlyph (parent.handle, outIndex [0], (short)inGlyph);
	if (update) updateText ();
}

public void setEnabled (boolean enabled) {
	checkWidget ();
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	_setEnabled (enabled);
}

public void setImage (Image image) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex) != OS.noErr) return;
	int imageHandle = image != null ? image.handle : 0;
	byte type = image != null ? (byte)OS.kMenuCGImageRefType : (byte)OS.kMenuNoIcon;
	OS.SetMenuItemIconHandle (parent.handle, outIndex [0], type, imageHandle);
}

public void setMenu (Menu menu) {
	checkWidget ();

	/* Check to make sure the new menu is valid */
	if ((style & SWT.CASCADE) == 0) {
		error (SWT.ERROR_MENUITEM_NOT_CASCADE);
	}
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.DROP_DOWN) == 0) {
			error (SWT.ERROR_MENU_NOT_DROP_DOWN);
		}
		if (menu.parent != parent.parent) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	
	/* Assign the new menu */
	Menu oldMenu = this.menu;
	if (oldMenu == menu) return;
	if (oldMenu != null) oldMenu.cascade = null;
	this.menu = menu;
	
	/* Update the menu in the OS */
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex) != OS.noErr) {
		error (SWT.ERROR_CANNOT_SET_MENU);
	}
	int outMenuRef [] = new int [1];
	if (menu == null) {
		if ((parent.style & SWT.BAR) != 0) {
//			Display display = getDisplay ();
//			short menuID = display.nextMenuId ();
//			if (OS.CreateNewMenu (menuID, 0, outMenuRef) != OS.noErr) {
//				error (SWT.ERROR_NO_HANDLES);
//			}
		}
	} else {
		menu.cascade = this;
		if ((parent.style & SWT.BAR) != 0) {
			if (oldMenu == null) {
//				OS.GetMenuItemHierarchicalMenu (parent.handle, outIndex [0], outMenuRef);
//				if (outMenuRef [0] != 0) {
//					OS.DeleteMenu (OS.GetMenuID (outMenuRef [0]));
//					OS.DisposeMenu (outMenuRef [0]);
//				}
			}
		}
		outMenuRef [0] = menu.handle;
		int [] outString = new int [1];
		if (OS.CopyMenuItemTextAsCFString (parent.handle, outIndex [0], outString) != OS.noErr) {
			error (SWT.ERROR_CANNOT_SET_MENU);
		}
		OS.SetMenuTitleWithCFString (outMenuRef [0], outString [0]);
		OS.CFRelease (outString [0]);
	}
	if (OS.SetMenuItemHierarchicalMenu (parent.handle, outIndex [0], outMenuRef [0]) != OS.noErr) {
		error (SWT.ERROR_CANNOT_SET_MENU);
	}
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

public void setSelection (boolean selected) {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	int inMark = selected ? ((style & SWT.RADIO) != 0) ? OS.diamondMark : OS.checkMark : 0;
	if (OS.SetMenuCommandMark (parent.handle, id, (char) inMark) != OS.noErr) {
		error (SWT.ERROR_CANNOT_SET_SELECTION);
	}
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	updateText ();
}

void updateText () {
	if ((style & SWT.SEPARATOR) != 0) return;
	short [] outIndex = new short [1];
	if (OS.GetIndMenuItemWithCommandID (parent.handle, id, 1, null, outIndex) != OS.noErr) {
		error (SWT.ERROR_CANNOT_SET_TEXT);
	}
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int i=0, j=0;
	while (i < buffer.length) {
		if (accelerator != 0 && buffer [i] == '\t') break;
		if ((buffer [j++] = buffer [i++]) == Mnemonic) {
			if (i == buffer.length) {continue;}
			if (buffer [i] == Mnemonic) {i++; continue;}
			j--;
		}
	}
	int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (str == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetMenuItemTextWithCFString (parent.handle, outIndex [0], str);
	int [] outHierMenu = new int [1];
	OS.GetMenuItemHierarchicalMenu (parent.handle, outIndex [0], outHierMenu);
	if (outHierMenu [0] != 0) OS.SetMenuTitleWithCFString (outHierMenu [0], str);
	OS.CFRelease (str);
}
}

