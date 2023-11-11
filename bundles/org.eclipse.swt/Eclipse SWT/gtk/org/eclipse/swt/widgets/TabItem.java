/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class represent a selectable user interface object
 * corresponding to a tab for a page in a tab folder.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabItem extends Item {
	long labelHandle, imageHandle, pageHandle;
	long cssProvider;
	Control control;
	TabFolder parent;
	String toolTipText;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>TabFolder</code>) and a style value
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
public TabItem (TabFolder parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>TabFolder</code>), a style value
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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
public TabItem (TabFolder parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	createWidget (index);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createWidget (int index) {
	parent.createItem (this, index);
	setOrientation (true);
	hookEvents ();
	register ();
	text = "";
}

@Override
void deregister() {
	super.deregister ();
	if (labelHandle != 0) display.removeWidget (labelHandle);
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
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
 *
 * @since 3.4
 */
public Rectangle getBounds () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getBoundsInPixels ());
}

Rectangle getBoundsInPixels () {
	checkWidget();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (handle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	if ((parent.style & SWT.MIRRORED) != 0) x = parent.getClientWidth () - width - x;
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the control that is used to fill the client area of
 * the tab folder when the user selects the tab item.  If no
 * control has been set, return <code>null</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget ();
	return control;
}

/**
 * Returns the receiver's parent, which must be a <code>TabFolder</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TabFolder getParent () {
	checkWidget ();
	return parent;
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
	checkWidget ();
	return toolTipText;
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	parent.gtk_enter_notify_event (widget, event);
	return 0;
}

@Override
long gtk_mnemonic_activate (long widget, long arg1) {
	return parent.gtk_mnemonic_activate (widget, arg1);
}

@Override
void hookEvents () {
	super.hookEvents ();
	if (labelHandle != 0) OS.g_signal_connect_closure_by_id (labelHandle, display.signalIds [MNEMONIC_ACTIVATE], 0, display.getClosure (MNEMONIC_ACTIVATE), false);
	if (GTK.GTK4) {
		long motionController = GTK4.gtk_event_controller_motion_new();
		GTK4.gtk_widget_add_controller(handle, motionController);
		GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);

		long enterMotionAddress = display.enterMotionCallback.getAddress();
		OS.g_signal_connect (motionController, OS.enter, enterMotionAddress, ENTER);
	} else {
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.getClosure (ENTER_NOTIFY_EVENT), false);
	}
}

@Override
void register () {
	super.register ();
	if (labelHandle != 0) display.addWidget (labelHandle, this);
}


@Override
void release (boolean destroy) {
	//Since controls are now nested under the tabItem,
	//tabItem is responsible for it's release.
	if (control != null && !control.isDisposed ()) {
		Control.gtk_widget_reparent (control, parent.parentingHandle());
	}
	super.release (destroy);
}

@Override
void releaseHandle() {
	super.releaseHandle();
	pageHandle = labelHandle = imageHandle = 0;
	cssProvider = 0;
	parent = null;
}

@Override
void releaseParent () {
	super.releaseParent ();
	int index = parent.indexOf (this);
	if (index == parent.getSelectionIndex ()) {
		if (control != null) control.setVisible (false);
	}
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
public void setControl(Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error(SWT.ERROR_INVALID_PARENT);
	}

	if (GTK.GTK4) {
		this.control = control;

		if (control != null) {
			long widget = control.topHandle();
			long widgetParentContainer = GTK.gtk_widget_get_parent(widget);
			if (widgetParentContainer != 0) {
				OS.g_object_ref(widget);
				OS.swt_fixed_remove(widgetParentContainer, widget);
				OS.swt_fixed_add(pageHandle, widget);
				OS.g_object_unref(widget);
			}
		}
	} else {
		if (control != null) {
			// To understand why we reparent, see implementation note about bug 454936 at the start of TabFolder.
			Control.gtk_widget_reparent (control, pageHandle);
		}

		Control oldControl = this.control, newControl = control;
		this.control = control;
		int index = parent.indexOf (this), selectionIndex = parent.getSelectionIndex();
		if (index != selectionIndex) {
			if (newControl != null) {
				if (selectionIndex != -1) {
					Control selectedControl = parent.getItem(selectionIndex).getControl();
					if (selectedControl == newControl) return;
				}
				newControl.setVisible(false);
				return;
			}
		}
		if (newControl != null) {
			newControl.setBoundsInPixels (parent.getClientAreaInPixels ());
			newControl.setVisible (true);
		}

		if ((oldControl != null) && (oldControl != newControl)) {
			Control.gtk_widget_reparent (oldControl, parent.parentingHandle());
			if (newControl != null) {
				oldControl.setVisible (false);
			}
		}
	}
}

void setFontDescription (long font) {
	setFontDescription (labelHandle, font);
}

void setForegroundRGBA (GdkRGBA rgba) {
	setForegroundGdkRGBA (labelHandle, rgba);
	setForegroundGdkRGBA (imageHandle, rgba);
}

void setForegroundGdkRGBA (long handle, GdkRGBA rgba) {
	GdkRGBA toSet = new GdkRGBA();
	if (rgba != null) {
		toSet = rgba;
	} else {
		toSet = display.COLOR_WIDGET_FOREGROUND_RGBA;
	}
	long context = GTK.gtk_widget_get_style_context (handle);
	// Form foreground string
	String color = display.gtk_rgba_to_css_string(toSet);
	String css = "* {color: " + color + ";}";

	// Cache and apply foreground color
	parent.cssForeground = css;
	gtk_css_provider_load_from_css(context, css);
}

/**
 * @param context the GtkStyleContext belonging to the widget which the CSS is to be applied to
 * @param css Java string representing the CSS
 */
void gtk_css_provider_load_from_css(long styleContext, String css) {
	if (cssProvider == 0) {
		cssProvider = GTK.gtk_css_provider_new();
		GTK.gtk_style_context_add_provider(styleContext, cssProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		OS.g_object_unref(cssProvider);
	}

	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data(cssProvider, Converter.javaStringToCString(css), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data(cssProvider, Converter.javaStringToCString(css), -1, null);
	}
}

@Override
public void setImage (Image image) {
	checkWidget ();
	super.setImage (image);
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList == null) imageList = parent.imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}

		if (GTK.GTK4) {
			long pixbuf = ImageList.createPixbuf(image);
			long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
			OS.g_object_unref(pixbuf);
			GTK4.gtk_image_set_from_paintable(imageHandle, texture);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, image.surface);
		}
		GTK.gtk_widget_show(imageHandle);
	} else {
		if (GTK.GTK4) {
			GTK4.gtk_image_clear(imageHandle);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, 0);
		}
		GTK.gtk_widget_hide(imageHandle);
	}
}

@Override
void setOrientation (boolean create) {
	if ((parent.style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (parent.style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (handle != 0) GTK.gtk_widget_set_direction (handle, dir);
		if (labelHandle != 0) GTK.gtk_widget_set_direction (labelHandle, dir);
		if (imageHandle != 0) GTK.gtk_widget_set_direction (imageHandle, dir);
		if (pageHandle != 0) GTK.gtk_widget_set_direction (pageHandle, dir);
	}
}

/**
 * Sets the receiver's text.  The string may include
 * the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] chars = fixMnemonic (string);
	byte [] buffer = Converter.wcsToMbcs (chars, true);
	GTK.gtk_label_set_text_with_mnemonic (labelHandle, buffer);
	if (string.length () != 0) {
		GTK.gtk_widget_show (labelHandle);
	} else {
		GTK.gtk_widget_hide (labelHandle);
	}
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
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText(String string) {
	checkWidget();

	toolTipText = string;
	setToolTipText(handle, string);
}

@Override
long dpiChanged(long object, long arg0) {
	super.dpiChanged(object, arg0);

	if (image != null) {
		image.internal_gtk_refreshImageForZoom();
		setImage(image);
	}

	return 0;
}
}
