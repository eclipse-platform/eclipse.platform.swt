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
package org.eclipse.swt.widgets;

 
import java.awt.Component;
import java.awt.MouseInfo;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.UIThreadUtils;

/**
 * Instances of this class are user interface objects that contain
 * menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show </dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified.
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Menu extends Widget {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public JComponent handle;
	
//	int x, y, hwndCB, id0, id1;
  int x, y;
	boolean hasLocation;
	MenuItem cascade;
	Decorations parent;
//	ImageList imageList;
	
//	/* Resource ID for SHMENUBARINFO */
//	static final int ID_PPC = 100;
//	
//	/* SmartPhone SoftKeyBar resource ids */
//	static final int ID_SPMM = 102;
//	static final int ID_SPBM = 103;
//	static final int ID_SPMB = 104;
//	static final int ID_SPBB = 105;
//	static final int ID_SPSOFTKEY0 = 106; 
//	static final int ID_SPSOFTKEY1 = 107;

/**
 * Constructs a new instance of this class given its parent,
 * and sets the style for the instance so that the instance
 * will be a popup menu on the given parent's shell.
 *
 * @param parent a control which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#POP_UP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Control parent) {
	this (checkNull (parent).menuShell (), SWT.POP_UP);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Decorations</code>) and a style value
 * describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a decorations control which will be the parent of the new instance (cannot be null)
 * @param style the style of menu to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BAR
 * @see SWT#DROP_DOWN
 * @see SWT#POP_UP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Decorations parent, int style) {
	this (parent, checkStyle (style), null);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Menu</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent.
 *
 * @param parentMenu a menu which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (Menu parentMenu) {
	this (checkNull (parentMenu).parent, SWT.DROP_DOWN);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>MenuItem</code>) and sets the style
 * for the instance so that the instance will be a drop-down
 * menu on the given parent's parent menu.
 *
 * @param parentItem a menu item which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Menu (MenuItem parentItem) {
	this (checkNull (parentItem).parent);
}

Menu (Decorations parent, int style, JMenuBar handle) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.handle = handle;
	createWidget ();
}

//void _setVisible (boolean visible) {
//	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
//	int hwndParent = parent.handle;
//	if (visible) {
//		int flags = OS.TPM_LEFTBUTTON;
//		if (OS.GetKeyState (OS.VK_LBUTTON) >= 0) flags |= OS.TPM_RIGHTBUTTON;
//		if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.TPM_RIGHTALIGN;
//		if ((parent.style & SWT.MIRRORED) != 0) {
//			flags &= ~OS.TPM_RIGHTALIGN;
//			if ((style & SWT.LEFT_TO_RIGHT) != 0) flags |= OS.TPM_RIGHTALIGN;
//		}
//		int nX = x, nY = y;
//		if (!hasLocation) {
//			int pos = OS.GetMessagePos ();
//			nX = (short) (pos & 0xFFFF);
//			nY = (short) (pos >> 16);
//		}
//		/*
//		* Feature in Windows.  It is legal use TrackPopupMenu()
//		* to display an empty menu as long as menu items are added
//		* inside of WM_INITPOPUPMENU.  If no items are added, then
//		* TrackPopupMenu() fails and does not send an indication
//		* that the menu has been closed.  This is not strictly a
//		* bug but leads to unwanted behavior when application code
//		* assumes that every WM_INITPOPUPMENU will eventually result
//		* in a WM_MENUSELECT, wParam=0xFFFF0000, lParam=0 to indicate
//		* that the menu has been closed.  The fix is to detect the
//		* case when TrackPopupMenu() fails and the number of items in
//		* the menu is zero and issue a fake WM_MENUSELECT.
//		*/
//		boolean success = OS.TrackPopupMenu (handle, flags, nX, nY, 0, hwndParent, null);
//		if (!success && GetMenuItemCount (handle) == 0) {
//			OS.SendMessage (hwndParent, OS.WM_MENUSELECT, 0xFFFF0000, 0);
//		}
//	} else {
//		OS.SendMessage (hwndParent, OS.WM_CANCELMODE, 0, 0);
//	}
//}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when menus are hidden or shown, by sending it
 * one of the messages defined in the <code>MenuListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #removeMenuListener
 */
public void addMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Hide,typedListener);
	addListener (SWT.Show,typedListener);
}

static Control checkNull (Control control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

static Menu checkNull (Menu menu) {
	if (menu == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return menu;
}

static MenuItem checkNull (MenuItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

static int checkStyle (int style) {
	return checkBits (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
}

void createHandle () {
	if (handle != null) return;
	if ((style & SWT.BAR) != 0) {
    JMenuBar menuBar = new JMenuBar();
    handle = menuBar;
	} else if((style & SWT.POP_UP) != 0) {
    JPopupMenu popup = new JPopupMenu();
    // TODO: check if a component listener for visibility is needed
    popup.addPopupMenuListener(new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent e) {
      }
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if(!hooks(SWT.Hide)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
          Event event = new Event();
          event.widget = Menu.this;
          sendEvent(SWT.Hide, event);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if(!hooks(SWT.Show)) return;
        UIThreadUtils.startExclusiveSection(getDisplay());
        if(isDisposed()) {
          UIThreadUtils.stopExclusiveSection();
          return;
        }
        try {
          Event event = new Event();
          event.widget = Menu.this;
          sendEvent(SWT.Show, event);
        } catch(Throwable t) {
          UIThreadUtils.storeException(t);
        } finally {
          UIThreadUtils.stopExclusiveSection();
        }
      }
    });
    handle = popup;
  } else if((style & SWT.DROP_DOWN) != 0) {
    handle = new JMenu();
	}
}

void createItem (MenuItem item, int index) {
  int count = handle.getComponentCount();
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	display.addMenuItem (item);
  handle.add(item.handle, index);
//  handle.invalidate();
//  handle.validate();
//  handle.repaint();

//	if (!success) {
//		display.removeMenuItem (item);
//		error (SWT.ERROR_ITEM_NOT_ADDED);
//	}
//	redraw ();
}
	
void createWidget () {
	checkOrientation (parent);
	createHandle ();
	parent.addMenu (this);
}

///*
//* Currently not used.
//*/
//int defaultBackground () {
//	return OS.GetSysColor (OS.COLOR_MENU);
//}
//
///*
//* Currently not used.
//*/
//int defaultForeground () {
//	return OS.GetSysColor (OS.COLOR_MENUTEXT);
//}
//
//void destroyAccelerators () {
//	parent.destroyAccelerators ();
//}

void destroyItem (MenuItem item) {
  java.awt.Component comp = item.handle.getParent();
  handle.remove(item.handle);
  if(comp instanceof JPopupMenu) {
    ((JPopupMenu)comp).pack();
  }
//	redraw ();
}

void destroyWidget () {
//	int hMenu = handle, hCB = hwndCB;
	releaseHandle ();
//	if (OS.IsWinCE && hCB != 0) {
//		OS.CommandBar_Destroy (hCB);
//	} else {
//		if (hMenu != 0) OS.DestroyMenu (hMenu);
//	}
}

void fixMenus (Decorations newParent) {
	MenuItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		items [i].fixMenus (newParent);
	}
	parent.removeMenu (this);
	newParent.addMenu (this);
	this.parent = newParent;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a menu or a shell. In this case, the
 * location is relative to the display.
 * <p>
 * Note that the bounds of a menu or menu item are undefined when
 * the menu is not visible.  This is because most platforms compute
 * the bounds of a menu dynamically just before it is displayed.
 * </p>
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
/*public*/ Rectangle getBounds () {
	checkWidget ();
  java.awt.Rectangle bounds = handle.getBounds();
  return new Rectangle (bounds.x, bounds.y, bounds.width, bounds.height);
//	if (OS.IsWinCE) return new Rectangle (0, 0, 0, 0);
//	if ((style & SWT.BAR) != 0) {
//		if (parent.menuBar != this) {
//			return new Rectangle (0, 0, 0, 0);
//		}
//		int hwndShell = parent.handle;
//		MENUBARINFO info = new MENUBARINFO ();
//		info.cbSize = MENUBARINFO.sizeof;
//		if (OS.GetMenuBarInfo (hwndShell, OS.OBJID_MENU, 0, info)) {
//			int width = info.right - info.left;
//			int height = info.bottom - info.top;
//			return new Rectangle (info.left, info.top, width, height);
//		}
//	} else {
//		int count = GetMenuItemCount (handle);
//		if (count != 0) {
//			RECT rect1 = new RECT ();
//			if (OS.GetMenuItemRect (0, handle, 0, rect1)) {
//				RECT rect2 = new RECT ();
//				if (OS.GetMenuItemRect (0, handle, count - 1, rect2)) {
//					int x = rect1.left - 2, y = rect1.top - 2;
//					int width = (rect2.right - rect2.left) + 4;
//					int height = (rect2.bottom - rect1.top) + 4;
//					return new Rectangle (x, y, width, height);
//				}
//			}
//		}
//	}
//	return new Rectangle (0, 0, 0, 0);
}

/**
 * Returns the default menu item or null if none has
 * been previously set.
 *
 * @return the default menu item.
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getDefaultItem () {
	checkWidget ();
  // TODO: implement that?
//	if (OS.IsWinCE) return null;
//	int id = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
//	if (id == -1) return null;
//	MENUITEMINFO info = new MENUITEMINFO ();
//	info.cbSize = MENUITEMINFO.sizeof;
//	info.fMask = OS.MIIM_ID;
//	if (OS.GetMenuItemInfo (handle, id, false, info)) {
//		return display.getMenuItem (info.wID);
//	}
	return null;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
  return handle.isEnabled();
//	return (state & DISABLED) == 0;
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getItem (int index) {
	checkWidget ();
  if(index < 0) error (SWT.ERROR_INVALID_RANGE);
  int result = 0;
  int count = handle.getComponentCount();
  if(index > count) error (SWT.ERROR_INVALID_RANGE);
  for(int i=0; i<count; i++) {
    MenuItem item = display.getMenuItem ((JComponent)handle.getComponent(i));
    if(item != null) {
      if(result == index) return item;
      result++;
    }
  }
  error (SWT.ERROR_INVALID_RANGE);
  return null;
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
  int count = handle.getComponentCount();
  int result = 0;
  for(int i=0; i<count; i++) {
    MenuItem item = display.getMenuItem ((JComponent)handle.getComponent(i));
    if(item != null) {
      result++;
    }
  }
	return result;
}

/**
 * Returns a (possibly empty) array of <code>MenuItem</code>s which
 * are the items in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem [] getItems () {
	checkWidget ();
  int count = handle.getComponentCount();
  java.util.List arrayList = new ArrayList(count);
  for(int i=0; i<count; i++) {
    MenuItem item = display.getMenuItem ((JComponent)handle.getComponent(i));
    if(item != null) {
      arrayList.add(item);
    }
  }
  return (MenuItem[])arrayList.toArray(new MenuItem[0]);
}

//int GetMenuItemCount (int handle) {
//	checkWidget ();
//	if (OS.IsWinCE) {
//		if ((OS.IsPPC || OS.IsSP) && hwndCB != 0) {
//			return OS.IsSP ? 2 : OS.SendMessage (hwndCB, OS.TB_BUTTONCOUNT, 0, 0);
//		}
//		int count = 0;
//		MENUITEMINFO info = new MENUITEMINFO ();
//		info.cbSize = MENUITEMINFO.sizeof;
//		while (OS.GetMenuItemInfo (handle, count, true, info)) count++;
//		return count;
//	}
//	return OS.GetMenuItemCount (handle);
//}

String getNameText () {
	String result = "";
	MenuItem [] items = getItems ();
	int length = items.length;
	if (length > 0) {
		for (int i=0; i<length-1; i++) {
			result = result + items [i].getNameText() + ", ";
		}
		result = result + items [length-1].getNameText ();
	}
	return result;
}

/**
 * Returns the receiver's parent, which must be a <code>Decorations</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Decorations getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>MenuItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public MenuItem getParentItem () {
	checkWidget ();
	return cascade;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>Menu</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getParentMenu () {
	checkWidget ();
	if (cascade != null) return cascade.parent;
	return null;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget ();
	return parent.getShell ();
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
  if(!handle.isVisible()) {
    return false;
  }
	if ((style & SWT.BAR) != 0) {
		return this == parent.menuShell ().menuBar;
	}
	if ((style & SWT.POP_UP) != 0) {
		Menu [] popups = display.popups;
		if (popups == null) return false;
		for (int i=0; i<popups.length; i++) {
			if (popups [i] == this) return true;
		}
	}
  Shell shell = getShell ();
  Menu menu = shell.activeMenu;
  while (menu != null && menu != this) {
    menu = menu.getParentMenu ();
  }
  return this == menu;
}

//int imageIndex (Image image) {
//	if (hwndCB == 0 || image == null) return OS.I_IMAGENONE;
//	if (imageList == null) {
//		int hOldList = OS.SendMessage (hwndCB, OS.TB_GETIMAGELIST, 0, 0);
//		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
//		Rectangle bounds = image.getBounds ();
//		imageList = display.getImageList (new Point (bounds.width, bounds.height));
//		int index = imageList.indexOf (image);
//		if (index == -1) index = imageList.add (image);
//		int hImageList = imageList.getHandle ();
//		OS.SendMessage (hwndCB, OS.TB_SETIMAGELIST, 0, hImageList);
//		return index;
//	}
//	int index = imageList.indexOf (image);
//	if (index != -1) return index;
//	return imageList.add (image);
//}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (MenuItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return -1;
  int componentCount = handle.getComponentCount();
  for(int i=0; i<componentCount; i++) {
    if(handle.getComponent(i) == item.handle) {
      return i;
    }
  }
	return -1;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled menu is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	Menu parentMenu = getParentMenu ();
	if (parentMenu == null) return getEnabled ();
	return getEnabled () && parentMenu.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

//void redraw () {
//  if (!isVisible ()) return;
//	if ((style & SWT.BAR) != 0) {
//		display.addBar (this);
//	} else {
//		update ();
//	}
//}

void releaseHandle () {
  super.releaseHandle ();
  handle = null;
}

void releaseChildren (boolean destroy) {
  MenuItem [] items = getItems ();
  for (int i=0; i<items.length; i++) {
    MenuItem item = items [i];
    if (item != null && !item.isDisposed ()) {
      item.release (false);
    }
  }
  super.releaseChildren (destroy);
}

void releaseParent () {
  super.releaseParent ();
	if (cascade != null) cascade.releaseMenu ();
	if ((style & SWT.BAR) != 0) {
		display.removeBar (this);
		if (this == parent.menuBar) {
			parent.setMenuBar (null);
		}
	} else {
		if ((style & SWT.POP_UP) != 0) {
			display.removePopup (this);
		}
	}
}

void releaseWidget () {
	super.releaseWidget ();
	if (parent != null) parent.removeMenu (this);
	parent = null;
	cascade = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the menu events are generated for the control.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuListener
 * @see #addMenuListener
 */
public void removeMenuListener (MenuListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Hide, listener);
	eventTable.unhook (SWT.Show, listener);
}

/**
 * Sets the default menu item to the argument or removes
 * the default emphasis when the argument is <code>null</code>.
 * 
 * @param item the default menu item or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu item has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDefaultItem (MenuItem item) {
  // TODO: implement that?
//	checkWidget ();
//	int newID = -1;
//	if (item != null) {
//		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
//		if (item.parent != this) return;
//		newID = item.id;
//	}
//	if (OS.IsWinCE) return;
//	int oldID = OS.GetMenuDefaultItem (handle, OS.MF_BYCOMMAND, OS.GMDI_USEDISABLED);
//	if (newID == oldID) return;
//	OS.SetMenuDefaultItem (handle, newID, OS.MF_BYCOMMAND);
//	redraw ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled menu is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
  handle.setEnabled(enabled);
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
	this.x = x;
	this.y = y;
	hasLocation = true;
}

/**
 * Sets the location of the receiver, which must be a popup,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of popup menus.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
  JPopupMenu popup = (JPopupMenu)handle;
	if (visible) {
    display.addPopup (this);
    java.awt.Point location;
    if(hasLocation) {
      location = new java.awt.Point(x, y);
    } else {
      if(Compatibility.IS_JAVA_5_OR_GREATER) {
        location = MouseInfo.getPointerInfo().getLocation();
      } else {
        location = new java.awt.Point(0, 0);
      }
    }
    Shell shell = getShell();
    if(shell != null) {
      Component shellHandle = shell.handle;
      if(shellHandle.isShowing()) {
        SwingUtilities.convertPointFromScreen(location, shellHandle);
        popup.show(shellHandle, location.x, location.y);
        return;
      }
    }
    popup.show(null, location.x, location.y);
	} else {
    display.removePopup (this);
    popup.setVisible(false);
//		_setVisible (false);
	}
}

//void update () {
//	if (OS.IsPPC || OS.IsSP) return;
//	if (OS.IsHPC) {
//		/*
//		* Each time a menu has been modified, the command menu bar
//		* must be redrawn or it won't update properly.  For example,
//		* a submenu will not drop down.
//		*/
//		Menu menuBar = parent.menuBar;
//		if (menuBar != null) {
//			Menu menu = this;
//			while (menu != null && menu != menuBar) {
//				menu = menu.getParentMenu ();
//			}
//			if (menu == menuBar) {
//				OS.CommandBar_DrawMenuBar (menuBar.hwndCB, 0);
//				OS.CommandBar_Show (menuBar.hwndCB, true);
//			}
//		}
//		return;
//	}
//	if (OS.IsWinCE) return;
//	if ((style & SWT.BAR) != 0) {
//		if (this == parent.menuBar) OS.DrawMenuBar (parent.handle);
//		return;
//	}
//	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
//		return;
//	}
//	boolean hasCheck = false, hasImage = false;
//	MenuItem [] items = getItems ();
//	for (int i=0; i<items.length; i++) {
//		MenuItem item = items [i];
//		if (item.image != null) {
//			if ((hasImage = true) && hasCheck) break;
//		}
//		if ((item.style & (SWT.CHECK | SWT.RADIO)) != 0) {
//			if ((hasCheck = true) && hasImage) break;
//		}
//	}
//	
//	/*
//	* Bug in Windows.  If a menu contains items that have
//	* images and can be checked, Windows does not include
//	* the width of the image and the width of the check when
//	* computing the width of the menu.  When the longest item
//	* does not have an image, the label and the accelerator
//	* text can overlap.  The fix is to use SetMenuItemInfo()
//	* to indicate that all items have a bitmap and then include
//	* the width of the widest bitmap in WM_MEASURECHILD.
//	* 
//	* NOTE:  This work around causes problems on Windows 98.
//	* Under certain circumstances that have yet to be isolated,
//	* some menus can become huge and blank.  For now, do not
//	* run the code on Windows 98.
//	*/	
//	if (!OS.IsWin95) {
//		MENUITEMINFO info = new MENUITEMINFO ();
//		info.cbSize = MENUITEMINFO.sizeof;
//		info.fMask = OS.MIIM_BITMAP;
//		for (int i=0; i<items.length; i++) {
//			MenuItem item = items [i];
//			if ((style & SWT.SEPARATOR) == 0) {
//				if (item.image == null) {
//					info.hbmpItem = hasImage ? OS.HBMMENU_CALLBACK : 0;
//					OS.SetMenuItemInfo (handle, item.id, false, info);
//				}
//			}
//		}
//	}
//
//	/* Update the menu to hide or show the space for bitmaps */
//	MENUINFO lpcmi = new MENUINFO ();
//	lpcmi.cbSize = MENUINFO.sizeof;
//	lpcmi.fMask = OS.MIM_STYLE;
//	OS.GetMenuInfo (handle, lpcmi);
//	if (hasImage && !hasCheck) {
//		lpcmi.dwStyle |= OS.MNS_CHECKORBMP;
//	} else {
//		lpcmi.dwStyle &= ~OS.MNS_CHECKORBMP;
//	}
//	OS.SetMenuInfo (handle, lpcmi);
//}

}
