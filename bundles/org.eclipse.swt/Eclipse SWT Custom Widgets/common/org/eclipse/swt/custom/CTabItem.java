/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represent a page in a notebook widget.
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SWT.CLOSE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#ctabfolder">CTabFolder, CTabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class CTabItem extends Item {
	CTabFolder parent;
	int x,y,width,height = 0;
	Control control; // the tab page

	String toolTipText;
	String shortenedText;
	int shortenedTextWidth;

	// Appearance
	Font font;
	Color foreground;
	Color selectionForeground;
	Image disabledImage;

	Rectangle closeRect = new Rectangle(0, 0, 0, 0);
	int closeImageState = SWT.BACKGROUND;
	int state = SWT.NONE;
	boolean showClose = false;
	boolean showDirty = false;
	boolean showing = false;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CTabFolder</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @param parent a CTabFolder which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#getStyle()
 */
public CTabItem (CTabFolder parent, int style) {
	this(parent, style, parent.getItemCount());
}
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CTabFolder</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param parent a CTabFolder which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#getStyle()
 */
public CTabItem (CTabFolder parent, int style, int index) {
	super (parent, style);
	showClose = (style & SWT.CLOSE) != 0;
	parent.createItem (this, index);
}


@Override
public void dispose() {
	if (isDisposed ()) return;
	//if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	parent.destroyItem(this);
	super.dispose();
	parent = null;
	control = null;
	toolTipText = null;
	shortenedText = null;
	font = null;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	/*
	 * This call is intentionally commented out, to allow this getter method to be
	 * called from a thread which is different from one that created the widget.
	 */
	//checkWidget();
	parent.runUpdate();
	return new Rectangle(x, y, width, height);
}
/**
* Gets the control that is displayed in the content area of the tab item.
*
* @return the control
*
* @exception SWTException <ul>
*    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
*    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
* </ul>
*/
public Control getControl () {
	checkWidget();
	return control;
}
/**
 * Get the image displayed in the tab if the tab is disabled.
 *
 * @return the disabled image or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @deprecated the disabled image is not used
 */
@Deprecated
public Image getDisabledImage(){
	checkWidget();
	return disabledImage;
}
/**
 * Returns the foreground color that the receiver will use to paint textual information.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.114
 */
public Color getForeground () {
	checkWidget ();
	if (foreground != null) return foreground;
	return parent.getForeground();
}

/**
 * Returns the selection foreground color that the receiver will use to paint textual information.
 *
 * @return the receiver's selection foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.114
 */
public Color getSelectionForeground () {
	checkWidget ();
	if (selectionForeground != null) return selectionForeground;
	return parent.getSelectionForeground();
}

/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 *  @since 3.0
 */
public Font getFont() {
	checkWidget();
	if (font != null) return font;
	return parent.getFont();
}
/**
 * Returns the receiver's parent, which must be a <code>CTabFolder</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CTabFolder getParent () {
	/*
	 * This call is intentionally commented out, to allow this getter method to be
	 * called from a thread which is different from one that created the widget.
	 */
	//checkWidget();
	return parent;
}
/**
 * Returns <code>true</code> to indicate that the receiver's close button should be shown.
 * Otherwise return <code>false</code>. The initial value is defined by the style (SWT.CLOSE)
 * that was used to create the receiver.
 *
 * @return <code>true</code> if the close button should be shown
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getShowClose() {
	checkWidget();
	return showClose;
}

/**
 * Returns <code>true</code> to indicate that the dirty indicator should be shown.
 * Otherwise return <code>false</code>.
 *
 * @return <code>true</code> if the dirty indicatorn should be shown
 *
 * @since 3.129
 */
public boolean getShowDirty() {
	checkWidget();
	return showClose;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	if (toolTipText == null && shortenedText != null) {
		String text = getText();
		if (!shortenedText.equals(text)) return text;
	}
	return toolTipText;
}
/**
* Returns <code>true</code> if the item will be rendered in the visible area of the CTabFolder. Returns false otherwise.
*
*  @return <code>true</code> if the item will be rendered in the visible area of the CTabFolder. Returns false otherwise.
*
*  @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
* @since 3.0
*/
public boolean isShowing () {
	checkWidget();
	return showing;
}

/**
 * Sets the control that is used to fill the client area of
 * the tab folder when the user selects the tab item.
 *
 * @param control the new control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.getParent() != parent) SWT.error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control != null && !this.control.isDisposed()) {
		this.control.setVisible(false);
	}
	this.control = control;
	if (this.control != null) {
		int index = parent.indexOf (this);
		if (index == parent.getSelectionIndex ()){
			this.control.setBounds(parent.getClientArea ());
			this.control.setVisible(true);
		} else {
			int selectedIndex = parent.getSelectionIndex();
			Control selectedControl = null;
			if (selectedIndex != -1) {
				selectedControl = parent.getItem(selectedIndex).control;
			}
			if (this.control != selectedControl) {
				this.control.setVisible(false);
			}
		}
	}
}
/**
 * Sets the image that is displayed if the tab item is disabled.
 * Null will clear the image.
 *
 * @param image the image to be displayed when the item is disabled or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @deprecated This image is not used
 */
@Deprecated
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.disabledImage = image;
}
boolean setFocus () {
	return control != null && !control.isDisposed() && control.setFocus ();
}
/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setFont (Font font){
	checkWidget();
	if (font != null && font.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (font == null && this.font == null) return;
	if (font != null && font.equals(this.font)) return;
	this.font = font;
	parent.updateFolder(CTabFolder.UPDATE_TAB_HEIGHT | CTabFolder.REDRAW_TABS);
}

/**
 * Sets the foreground color that the receiver will use to paint textual information
 * for this item to the color specified by the argument, or to the default foreground color
 * for that kind of control if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.114
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (color == foreground) return;
	foreground = color;
	parent.updateFolder(CTabFolder.REDRAW_TABS);
}

/**
 * Sets the selection foreground color that the receiver will use to paint textual information
 * for this item to the color specified by the argument, or to the default selection foreground color
 * for that kind of control if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.114
 */
public void setSelectionForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (color == selectionForeground) return;
	selectionForeground = color;
	parent.updateFolder(CTabFolder.REDRAW_TABS);
}

@Override
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	Image oldImage = getImage();
	if (image == null && oldImage == null) return;
	if (image != null && image.equals(oldImage)) return;
	super.setImage(image);
	parent.updateFolder(CTabFolder.UPDATE_TAB_HEIGHT | CTabFolder.REDRAW_TABS);
}
/**
 * Sets to <code>true</code> to indicate that the receiver's close button should be shown.
 * If the parent (CTabFolder) was created with SWT.CLOSE style, changing this value has
 * no effect.
 *
 * @param close the new state of the close button
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setShowClose(boolean close) {
	checkWidget();
	if (showClose == close) return;
	showClose = close;
	parent.updateFolder(CTabFolder.REDRAW_TABS);
}

/**
 * Sets to <code>true</code> to indicate that the dirty indicator should be shown.
 *
 * @param dirty the new value whether the dirty indicator shall be shown
 *
 * @since 3.129
 */
public void setShowDirty(boolean dirty) {
	checkWidget();
	if (showDirty == dirty) return;
	showDirty = dirty;
	parent.updateFolder(CTabFolder.REDRAW_TABS);
}

/**
 * Sets the text to display on the tab.
 * A carriage return '\n' allows to display multi line text.
 *
 * @param string the tab name
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setText (String string) {
	checkWidget();
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals(getText())) return;
	super.setText(string);
	shortenedText = null;
	shortenedTextWidth = 0;
	parent.updateFolder(CTabFolder.UPDATE_TAB_HEIGHT | CTabFolder.REDRAW_TABS);
}
/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

}
