package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class TreeItem extends Item {
	public int handle;
	Tree parent;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, 0, OS.TVI_LAST);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style, int index) {
	super (parent, style);
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	int hItem = OS.TVI_FIRST;
	if (index != 0) {
		int count = 1, hwnd = parent.handle;
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		while (hItem != 0 && count < index) {
			hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
			count++;
		}
		if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	};
	parent.createItem (this, 0, hItem);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style) {
	super (checkNull (parentItem).parent, style);
	parent = parentItem.parent;
	int hItem = parentItem.handle;
	parent.createItem (this, hItem, OS.TVI_LAST);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style, int index) {
	super (checkNull (parentItem).parent, style);
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	parent = parentItem.parent;
	int hItem = OS.TVI_FIRST;
	int hParent = parentItem.handle;
	if (index != 0) {
		int count = 1, hwnd = parent.handle;
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent);
		while (hItem != 0 && count < index) {
			hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
			count++;
		}
		if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	}
	parent.createItem (this, hParent, hItem);
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void destroyWidget () {
	super.destroyWidget ();
	releaseHandle ();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	rect.left = handle;
	OS.SendMessage (hwnd, OS.TVM_GETITEMRECT, 1, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return false;
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	int result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return (result != 0) && (((tvItem.state >> 12) & 1) == 0);
}

public Display getDisplay () {
	Tree parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 * <p>
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget ();
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.hItem = handle;
	tvItem.mask = OS.TVIF_STATE;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return (tvItem.state & OS.TVIS_EXPANDED) != 0;
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return false;
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	int result = OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return (result != 0) && ((tvItem.state >> 12) > 2);
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.
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
	int count = 0;
	int hwnd = parent.handle;
	int hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	while (hItem != 0) {
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		count++;
	}
	return count;
}

/**
 * Returns an array of <code>TreeItem</code>s which are the
 * direct item children of the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	int count = 0;
	int hwnd = parent.handle;
	int hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	while (hItem != 0) {
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		count++;
	}
	int index = 0;
	TreeItem [] result = new TreeItem [count];
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
	while (tvItem.hItem != 0) {
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		result [index++] = parent.items [tvItem.lParam];
		tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, tvItem.hItem);
	}
	return result;
}

/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, handle);
	if (tvItem.hItem == 0) return null;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	return parent.items [tvItem.lParam];
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseHandle () {
	handle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

/**
 * Sets the checked state of the receiver.
 * <p>
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	int state = tvItem.state >> 12;
	if (checked) {
		if ((state & 0x1) != 0) state++;
	} else {
		if ((state & 0x1) == 0) --state;
	}
	tvItem.state = state << 12;
	OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
}

/**
 * Sets the expanded state of the receiver.
 * <p>
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded (boolean expanded) {
	checkWidget ();
	int hwnd = parent.handle;
	/*
	* Feature in Windows.  When the user collapses the root
	* of a subtree that has the focus item, Windows moves
	* the selection to the root of the subtree and issues
	* a TVN_SELCHANGED to inform the programmer that the
	* seletion has changed.  When the programmer collapses
	* the same subtree using TVM_EXPAND, Windows does not
	* send the selection changed notification.  This is not
	* stricly wrong but is inconsistent.  The fix is to notice
	* that the selection has changed and issue the event.
	*/
	int hOldItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	parent.ignoreExpand = true;
	OS.SendMessage (hwnd, OS.TVM_EXPAND, expanded ? OS.TVE_EXPAND : OS.TVE_COLLAPSE, handle);
	parent.ignoreExpand = false;
	int hNewItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if (hNewItem != hOldItem) {
		Event event = new Event ();
		if (hNewItem != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
			tvItem.hItem = hNewItem;
			if (OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem) != 0) {
				event.item = parent.items [tvItem.lParam];	
			}
		}
		parent.sendEvent (SWT.Selection, event);
	}
}

/**
 * Sets the grayed state of the receiver.
 * <p>
 *
 * @param checked the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
	tvItem.hItem = handle;
	OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
	int state = tvItem.state >> 12;
	if (grayed) {
		if (state <= 2) state +=2;
	} else {
		if (state > 2) state -=2;
	}
	tvItem.state = state << 12;
	OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
}

public void setImage (Image image) {
	checkWidget ();
	super.setImage (image);
	int hwnd = parent.handle;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE;
	tvItem.iImage = parent.imageIndex (image);
	tvItem.iSelectedImage = tvItem.iImage;
	tvItem.hItem = handle;
	int result = OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
}

/**
 * This label will be displayed to the right of the bitmap, 
 * or, if the receiver doesn't have a bitmap to the right of 
 * the horizontal hierarchy connector line.
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	int hwnd = parent.handle;
	int hHeap = OS.GetProcessHeap ();
	TCHAR buffer = new TCHAR (parent.getCodePage (), string, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount); 
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_TEXT;
	tvItem.hItem = handle;
	tvItem.pszText = pszText;
	int result = OS.SendMessage (hwnd, OS.TVM_SETITEM, 0, tvItem);
	OS.HeapFree (hHeap, 0, pszText);
}

}
