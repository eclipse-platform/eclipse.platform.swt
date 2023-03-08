/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE, NO_RADIO_GROUP, EMBEDDED, DOUBLE_BUFFERED</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: The <code>NO_BACKGROUND</code>, <code>NO_FOCUS</code>, <code>NO_MERGE_PAINTS</code>,
 * and <code>NO_REDRAW_RESIZE</code> styles are intended for use with <code>Canvas</code>.
 * They can be used with <code>Composite</code> if you are drawing your own, but their
 * behavior is undefined if they are used with subclasses of <code>Composite</code> other
 * than <code>Canvas</code>.
 * </p><p>
 * Note: The <code>CENTER</code> style, although undefined for composites, has the
 * same value as <code>EMBEDDED</code> which is used to embed widgets from other
 * widget toolkits into SWT.  On some operating systems (GTK), this may cause
 * the children of this composite to be obscured.
 * </p><p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 * @see <a href="http://www.eclipse.org/swt/snippets/#composite">Composite snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Composite extends Scrollable {
	/**
	 * the handle to the OS resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long  embeddedHandle;
	long imHandle, socketHandle;
	Layout layout;
	Control[] tabList;
	int layoutCount, backgroundMode;
	/**
	 * When this field is set, it indicates that a child widget of this Composite
	 * needs to have its clip set to its allocation. This is because on GTK3.20+
	 * some widgets (like Combo) have their clips merged with that of their parent.
	 */
	long fixClipHandle;
	/**
	 * If fixClipHandle is set, then the fixClipMap HashMap contains children
	 * of fixClipHandle that also need to have their clips adjusted.
	 *
	 * <p>Each key is a Control which needs to have its clip adjusted, and each value
	 * is an array of handles (descendants of the Control) ordered by widget hierarchy.
	 * This array will be traversed in-order to adjust the clipping of each element.
	 * See bug 500703 and 535323.</p>
	 */
	Map<Control, long []> fixClipMap = new HashMap<> ();

	static final String NO_INPUT_METHOD = "org.eclipse.swt.internal.gtk.noInputMethod"; //$NON-NLS-1$
	Shell popupChild;
	/**
	 * If set to {@code true}, child widgets with negative y coordinate GTK allocation
	 * will not be drawn. Only relevant if such child widgets are being
	 * drawn via propagateDraw(), such as Tree/Table editing widgets.
	 *
	 * See bug 535978 and bug 547986.
	 */
	boolean noChildDrawing = false;
	/**
	 * A HashMap of child widgets that keeps track of which child has had their
	 * GdkWindow lowered/raised. Only relevant if such child widgets are being
	 * drawn via propagateDraw(), such as Tree/Table editing widgets.
	 *
	 * See bug 535978.
	 */
	HashMap<Widget, Boolean> childrenLowered = new HashMap<>();

Composite () {
	/* Do nothing */
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_FOCUS
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_RADIO_GROUP
 * @see SWT#EMBEDDED
 * @see SWT#DOUBLE_BUFFERED
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, checkStyle (style));
	/*
	 * Cache the NO_BACKGROUND flag for use in the Cairo setRegion()
	 * implementation. Only relevant to GTK3.10+, see bug 475784.
	 */
	if ((style & SWT.NO_BACKGROUND) != 0) {
		cachedNoBackground = true;
	}
}

static int checkStyle (int style) {
	style &= ~SWT.NO_BACKGROUND;
	style &= ~SWT.TRANSPARENT;
	return style;
}

Control[] _getChildren () {
	long parentHandle = parentingHandle();

	if (GTK.GTK4) {
		ArrayList<Control> childrenList = new ArrayList<>();
		for (long child = GTK4.gtk_widget_get_first_child(parentHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			Widget childWidget = display.getWidget(child);
			if (childWidget != null && childWidget instanceof Control && childWidget != this) {
				childrenList.add((Control)childWidget);
			}
		}

		return childrenList.toArray(new Control[childrenList.size()]);
	} else {
		long list = GTK3.gtk_container_get_children (parentHandle);
		if (list == 0) return new Control [0];
		int count = OS.g_list_length (list);
		Control [] children = new Control [count];
		int i = 0;
		long temp = list;
		while (temp != 0) {
			long handle = OS.g_list_data (temp);
			if (handle != 0) {
				Widget widget = display.getWidget (handle);
				if (widget != null && widget != this) {
					if (widget instanceof Control) {
						children [i++] = (Control) widget;
					}
				}
			}
			temp = OS.g_list_next (temp);
		}
		OS.g_list_free (list);
		if (i == count) return children;
		Control [] newChildren = new Control [i];
		System.arraycopy (children, 0, newChildren, 0, i);
		return newChildren;
	}
}

Control [] _getTabList () {
	if (tabList == null) return tabList;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) count++;
	}
	if (count == tabList.length) return tabList;
	Control [] newList = new Control [count];
	int index = 0;
	for (int i=0; i<tabList.length; i++) {
		if (!tabList [i].isDisposed ()) {
			newList [index++] = tabList [i];
		}
	}
	tabList = newList;
	return tabList;
}

/**
 * Clears any data that has been cached by a Layout for all widgets that
 * are in the parent hierarchy of the changed control up to and including the
 * receiver.  If an ancestor does not have a layout, it is skipped.
 *
 * @param changed an array of controls that changed state and require a recalculation of size
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @deprecated use {@link Composite#layout(Control[], int)} instead
 * @since 3.1
 */
@Deprecated
public void changed (Control[] changed) {
	layout(changed, SWT.DEFER);
}

@Override
void checkBuffered () {
	if ((style & SWT.DOUBLE_BUFFERED) == 0 && (style & SWT.NO_BACKGROUND) != 0) {
		return;
	}
	super.checkBuffered();
}

@Override
protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	display.runSkin();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			changed |= (state & LAYOUT_CHANGED) != 0;
			size = DPIUtil.autoScaleUp(layout.computeSize (this, DPIUtil.autoScaleDown(wHint), DPIUtil.autoScaleDown(hHint), changed));
			state &= ~LAYOUT_CHANGED;
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize (wHint, hHint, changed);
		if (size.x == 0) size.x = DEFAULT_WIDTH;
		if (size.y == 0) size.y = DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = DPIUtil.autoScaleUp (computeTrim (0, 0, DPIUtil.autoScaleDown(size.x), DPIUtil.autoScaleDown(size.y)));
	return new Point (trim.width, trim.height);
}

@Override
Widget [] computeTabList () {
	Widget result [] = super.computeTabList ();
	if (result.length == 0) return result;
	Control [] list = tabList != null ? _getTabList () : _getChildren ();
	for (int i=0; i<list.length; i++) {
		Control child = list [i];
		Widget [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Widget [] newResult = new Widget [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	return result;
}

@Override
void createHandle (int index) {
	state |= HANDLE | CANVAS | CHECK_SUBWINDOW;
	boolean scrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	if (!scrolled) state |= THEME_BACKGROUND;
	createHandle (index, true, scrolled || (style & SWT.BORDER) != 0);
}

@Override
int applyThemeBackground () {
	return (backgroundAlpha == 0 || (style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) ? 1 : 0;
}

void createHandle (int index, boolean fixed, boolean scrolled) {
	if (scrolled) {
		if (fixed) {
			fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
			if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
			if (!GTK.GTK4) GTK3.gtk_widget_set_has_window(fixedHandle, true);
		}

		long vadj = GTK.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (vadj == 0) error (SWT.ERROR_NO_HANDLES);
		long hadj = GTK.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (hadj == 0) error (SWT.ERROR_NO_HANDLES);

		if (GTK.GTK4) {
			scrolledHandle = GTK4.gtk_scrolled_window_new();
			GTK.gtk_scrolled_window_set_hadjustment(scrolledHandle, hadj);
			GTK.gtk_scrolled_window_set_vadjustment(scrolledHandle, vadj);
			GTK.gtk_widget_set_hexpand(scrolledHandle, true);
			GTK.gtk_widget_set_vexpand(scrolledHandle, true);
		} else {
			scrolledHandle = GTK3.gtk_scrolled_window_new (hadj, vadj);
		}
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}

	handle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
	if (handle == 0) error(SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		GTK4.gtk_widget_set_focusable(handle, true);
	} else {
		GTK3.gtk_widget_set_has_window(handle, true);
	}
	GTK.gtk_widget_set_can_focus(handle, true);

	if ((style & SWT.EMBEDDED) == 0) {
		if ((state & CANVAS) != 0) {
			/* Prevent an input method context from being created for the Browser widget */
			if (display.getData (NO_INPUT_METHOD) == null) {
				imHandle = GTK.gtk_im_multicontext_new ();
				if (imHandle == 0) error (SWT.ERROR_NO_HANDLES);
			}
		}
	}
	if (scrolled) {
		if (fixed) {
			if (GTK.GTK4) {
				OS.swt_fixed_add(fixedHandle, scrolledHandle);
			} else {
				GTK3.gtk_container_add (fixedHandle, scrolledHandle);
			}
		}
		if (GTK.GTK4) {
			GTK4.gtk_scrolled_window_set_child(scrolledHandle, handle);
		} else {
			/*
			* Force the scrolledWindow to have a single child that is
			* not scrolled automatically.  Calling gtk_container_add()
			* seems to add the child correctly but cause a warning.
			*/
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			GTK3.gtk_container_add (scrolledHandle, handle);
			display.setWarnings (warnings);
		}

		int hsp = (style & SWT.H_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
		int vsp = (style & SWT.V_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
		GTK.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
		if (hasBorder ()) {
			if (GTK.GTK4) {
				GTK4.gtk_scrolled_window_set_has_frame(scrolledHandle, true);
			} else {
				GTK3.gtk_scrolled_window_set_shadow_type (scrolledHandle, GTK.GTK_SHADOW_ETCHED_IN);
			}
		}
	}
	if ((style & SWT.EMBEDDED) != 0) {
		if (OS.isWayland()) {
			if (Device.DEBUG) {
				new SWTError(SWT.ERROR_INVALID_ARGUMENT,"SWT.EMBEDDED is currently not yet supported in Wayland. \nPlease "
					+ "refer to https://bugs.eclipse.org/bugs/show_bug.cgi?id=514487 for development status.").printStackTrace();
			}
		} else {
			if (GTK.GTK4) {
				// From Emmanuele Bassi:
				// "[Embedding external windows is] not possible any more. Socket/plug were available only on X11,
				// and foreign windowing system surfaces were a massive complication in the backend code."
				if (Device.DEBUG) {
					new SWTError (SWT.ERROR_INVALID_ARGUMENT, "SWT.EMBEDDED is not supported for GTK >= 4.")
							.printStackTrace ();
				}
			} else {
				socketHandle = GTK.gtk_socket_new ();
				if (socketHandle == 0) error (SWT.ERROR_NO_HANDLES);
				GTK3.gtk_container_add (handle, socketHandle);
			}
		}
	}

	if (!GTK.GTK4) {
		if ((style & SWT.NO_REDRAW_RESIZE) != 0 && (style & SWT.RIGHT_TO_LEFT) == 0) {
			GTK3.gtk_widget_set_redraw_on_allocate (handle, false);
		}
		/*
		* Bug in GTK.  When a widget is double buffered and the back
		* pixmap is null, the double buffer pixmap is filled with the
		* background of the widget rather than the current contents of
		* the screen.  If nothing is drawn during an expose event,
		* the pixels are altered.  The fix is to clear double buffering
		* when NO_BACKGROUND is set and DOUBLE_BUFFERED
		* is not explicitly set.
		*/
		if ((style & SWT.DOUBLE_BUFFERED) == 0 && (style & SWT.NO_BACKGROUND) != 0) {
			GTK3.gtk_widget_set_double_buffered (handle, false);
		}
	}
}

/**
 * Iterates though the array of child widgets that need to have their clips
 * adjusted, and calls Control.adjustChildClipping() on it.
 *
 * The default implementation in Composite is: if a child has a negative clip, adjust it.
 * Also check if the child's allocation is negative, and adjust it as necessary.
 *
 * <p>If the array is empty this method just returns. See bug 500703, and 539367.</p>
 */
void fixClippings () {
	if (fixClipHandle == 0 || fixClipMap.isEmpty()) {
		return;
	} else {
		Control [] children = _getChildren();
		for (Control child : children) {
			if (fixClipMap.containsKey(child)) {
				long [] childHandles = fixClipMap.get(child);
				for (long widget : childHandles) {
					child.adjustChildClipping(widget);
				}
			}
		}
	}
}

@Override
void adjustChildClipping (long widget) {
	GtkRequisition minimumSize = new GtkRequisition ();
	GtkRequisition naturalSize = new GtkRequisition ();
	GtkAllocation clip = new GtkAllocation ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation(widget, allocation);
	GTK3.gtk_widget_get_clip(widget, clip);
	/*
	 * If the clip is negative, add the x coordinate to the width
	 * and set the x coordinate to 0.
	 */
	if (clip.x < 0) {
		/*
		 * Some "transient" widgets like menus get allocations of
		 * {-1, -1, 1, 1}. Check to make sure this isn't the case
		 * before proceeding.
		 */
		if (allocation.x < -1 && (allocation.width > 1 || allocation.height > 1)) {
			// Adjust the allocation just like the clip, if it's negative
			allocation.width = allocation.width + allocation.x;
			allocation.x = 0;
			// Call gtk_widget_get_preferred_size() to prevent warnings
			GTK.gtk_widget_get_preferred_size(widget, minimumSize, naturalSize);
			// Allocate and queue a resize event
			gtk_widget_size_allocate(widget, allocation, -1);
			GTK.gtk_widget_queue_resize(widget);
		}
	}
	// Adjust the clip
	GTK3.gtk_widget_set_clip(widget, allocation);
}

@Override
long gtk_draw (long widget, long cairo) {
	long context = GTK.gtk_widget_get_style_context(widget);
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation (widget, allocation);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	// We specify a 0 value for x & y as we want the whole widget to be
	// colored, not some portion of it.
	GTK.gtk_render_background(context, cairo, 0, 0, width, height);
	// If fixClipHandle is set: iterate through the children of widget
	// and set their clips to be that of their allocation
	if (widget == fixClipHandle) fixClippings();
	return super.gtk_draw(widget, cairo);
}


@Override
boolean mustBeVisibleOnInitBounds() {
	// Bug 540298: if we return false, we will be invisible if the size is
	// not set, but our layout will not properly work, so that not all children
	// will be shown properly
	return true;
}

@Override
void deregister () {
	super.deregister ();
	if (socketHandle != 0) display.removeWidget (socketHandle);
}

/**
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background.
 *
 * <p>The <code>offsetX</code> and <code>offsetY</code> are used to map from
 * the <code>gc</code> origin to the origin of the parent image background. This is useful
 * to ensure proper alignment of the image background.</p>
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param offsetX the image background x offset
 * @param offsetY the image background y offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public void drawBackground (GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
	checkWidget();
	Rectangle rect = DPIUtil.autoScaleUp(new Rectangle (x, y, width, height));
	offsetX = DPIUtil.autoScaleUp(offsetX);
	offsetY = DPIUtil.autoScaleUp(offsetY);
	drawBackgroundInPixels(gc, rect.x, rect.y, rect.width, rect.height, offsetX, offsetY);
}

void drawBackgroundInPixels (GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	Control control = findBackgroundControl ();
	if (control != null) {
		GCData data = gc.getGCData ();
		long cairo = data.cairo;
		Cairo.cairo_save (cairo);
		if (control.backgroundImage != null) {
			Point pt = display.mapInPixels (this, control, 0, 0);
			Cairo.cairo_translate (cairo, -pt.x - offsetX, -pt.y - offsetY);
			x += pt.x + offsetX;
			y += pt.y + offsetY;
			long surface = control.backgroundImage.surface;
			if (surface == 0) error (SWT.ERROR_NO_HANDLES);
			Cairo.cairo_surface_reference(surface);
			long pattern = Cairo.cairo_pattern_create_for_surface (surface);
			if (pattern == 0) error (SWT.ERROR_NO_HANDLES);
			Cairo.cairo_pattern_set_extend (pattern, Cairo.CAIRO_EXTEND_REPEAT);
			if ((data.style & SWT.MIRRORED) != 0) {
				double[] matrix = {-1, 0, 0, 1, 0, 0};
				Cairo.cairo_pattern_set_matrix(pattern, matrix);
			}
			Cairo.cairo_set_source (cairo, pattern);
			Cairo.cairo_surface_destroy (surface);
			Cairo.cairo_pattern_destroy (pattern);
		} else {
			GdkRGBA rgba = control.getBackgroundGdkRGBA ();
			Cairo.cairo_set_source_rgba (cairo, rgba.red, rgba.green, rgba.blue, rgba.alpha);
		}
		Cairo.cairo_rectangle (cairo, x, y, width, height);
		Cairo.cairo_fill (cairo);
		Cairo.cairo_restore (cairo);
	} else {
		gc.fillRectangle(DPIUtil.autoScaleDown(new Rectangle(x, y, width, height)));

	}
}

@Override
void enableWidget (boolean enabled) {
	if ((state & CANVAS) != 0) return;
	super.enableWidget (enabled);
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : parent.findDeferredControl ();
}

@Override
Menu [] findMenus (Control control) {
	if (control == this) return new Menu [0];
	Menu result [] = super.findMenus (control);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		Menu [] menuList = child.findMenus (control);
		if (menuList.length != 0) {
			Menu [] newResult = new Menu [result.length + menuList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (menuList, 0, newResult, result.length, menuList.length);
			result = newResult;
		}
	}
	return result;
}

@Override
void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	super.fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		children [i].fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
}

@Override
void fixParentGdkResource() {
	// Changes to this method should be verified via
	// org.eclipse.swt.tests.gtk/*/Bug510803_TabFolder_TreeEditor_Regression.java (part two)
	for (Control child : _getChildren()) {
		child.fixParentGdkResource();
	}
}

@Override
void fixModal(long group, long modalGroup)  {
	Control[] controls = _getChildren ();
	for (int i = 0; i < controls.length; i++) {
		controls[i].fixModal (group, modalGroup);
	}
}

@Override
void fixStyle () {
	super.fixStyle ();
	if (scrolledHandle == 0) fixStyle (handle);
	Control[] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].fixStyle ();
	}
}

void fixTabList (Control control) {
	if (tabList == null) return;
	int count = 0;
	for (int i=0; i<tabList.length; i++) {
		if (tabList [i] == control) count++;
	}
	if (count == 0) return;
	Control [] newList = null;
	int length = tabList.length - count;
	if (length != 0) {
		newList = new Control [length];
		int index = 0;
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] != control) {
				newList [index++] = tabList [i];
			}
		}
	}
	tabList = newList;
}

void fixZOrder () {
	if ((state & CANVAS) != 0) return;
	long parentHandle = parentingHandle ();
	if (GTK.GTK4) {
		/* TODO: GTK4 parent does not hold the list of children it has created (parent-children relationship can only be done
		 * with GdkPopup) Will need to consider if we can get children some other way or not have to do fixZOrder at all
		 * and use GdkPopup autohide feature.
		 */
	} else {
		long parentWindow = gtk_widget_get_window (parentHandle);
		if (parentWindow == 0) return;
		long [] userData = new long [1];
		long windowList = GDK.gdk_window_get_children (parentWindow);
		if (windowList != 0) {
			long windows = windowList;
			while (windows != 0) {
				long window = OS.g_list_data (windows);
				if (window != redrawWindow) {
					GDK.gdk_window_get_user_data (window, userData);
					if (userData [0] == 0 || OS.G_OBJECT_TYPE (userData [0]) != display.gtk_fixed_get_type ()) {
						GDK.gdk_window_lower (window);
					}
				}
				windows = OS.g_list_next (windows);
			}
			OS.g_list_free (windowList);
		}
	}
}

@Override
long focusHandle () {
	if (socketHandle != 0) return socketHandle;
	return super.focusHandle ();
}

@Override
boolean forceFocus (long focusHandle) {
	if (socketHandle != 0) GTK.gtk_widget_set_can_focus (focusHandle, true);
	boolean result = super.forceFocus (focusHandle);
	if (socketHandle != 0) GTK.gtk_widget_set_can_focus (focusHandle, false);
	return result;
}

/**
 * Returns the receiver's background drawing mode. This
 * will be one of the following constants defined in class
 * <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERIT_FORCE</code>.
 *
 * @return the background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 *
 * @since 3.2
 */
public int getBackgroundMode () {
	checkWidget ();
	return backgroundMode;
}

/**
 * Returns a (possibly empty) array containing the receiver's children.
 * Children are returned in the order that they are drawn.  The topmost
 * control appears at the beginning of the array.  Subsequent controls
 * draw beneath this control and appear later in the array.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return an array of children
 *
 * @see Control#moveAbove
 * @see Control#moveBelow
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget();
	return _getChildren ();
}

int getChildrenCount () {
	int count = 0;

	if (GTK.GTK4) {
		for (long child = GTK4.gtk_widget_get_first_child(handle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			count++;
		}
	} else {
		/*
		* NOTE: The current implementation will count
		* non-registered children.
		*/
		long list = GTK3.gtk_container_get_children(handle);
		if (list != 0) {
			count = OS.g_list_length(list);
			OS.g_list_free(list);
		}
	}

	return count;
}

@Override
Rectangle getClientAreaInPixels () {
	checkWidget();
	if ((state & CANVAS) != 0) {
		if ((state & ZERO_WIDTH) != 0 && (state & ZERO_HEIGHT) != 0) {
			return new Rectangle (0, 0, 0, 0);
		}
		if(RESIZE_ON_GETCLIENTAREA) {
			forceResize ();
		}
		long clientHandle = clientHandle ();
		GtkAllocation allocation = new GtkAllocation();
		GTK.gtk_widget_get_allocation (clientHandle, allocation);
		int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
		int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
		return new Rectangle (0, 0, width, height);
	}
	return super.getClientAreaInPixels();
}

/**
 * Returns layout which is associated with the receiver, or
 * null if one has not been set.
 *
 * @return the receiver's layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Layout getLayout () {
	checkWidget();
	return layout;
}

/**
 * Returns <code>true</code> if the receiver has deferred
 * the performing of layout, and <code>false</code> otherwise.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLayoutDeferred(boolean)
 * @see #isLayoutDeferred()
 *
 * @since 3.1
 */
public boolean getLayoutDeferred () {
	checkWidget ();
	return layoutCount > 0 ;
}

/**
 * Gets the (possibly empty) tabbing order for the control.
 *
 * @return tabList the ordered list of controls representing the tab order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTabList
 */
public Control [] getTabList () {
	checkWidget ();
	Control [] tabList = _getTabList ();
	if (tabList == null) {
		int count = 0;
		Control [] list =_getChildren ();
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) count++;
		}
		tabList = new Control [count];
		int index = 0;
		for (int i=0; i<list.length; i++) {
			if (list [i].isTabGroup ()) {
				tabList [index++] = list [i];
			}
		}
	}
	return tabList;
}

@Override
long gtk_button_press_event (long widget, long event) {
	long result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) == 0 && hooksKeys ()) {
			int [] eventButton = new int [1];
			GDK.gdk_event_get_button(event, eventButton);

			if (eventButton[0] == 1) {
				if (getChildrenCount () == 0) setFocus ();
			}
		}
	}
	return result;
}



@Override
boolean gtk4_key_press_event(long controller, int keyval, int keycode, int state, long event) {
	boolean handled = super.gtk4_key_press_event(controller, keyval, keycode, state, event);
	if (!handled) {
		/*
		* Feature in GTK.  The default behavior when the return key
		* is pressed is to select the default button.  This is not the
		* expected behavior for Composite and its subclasses.  The
		* fix is to avoid calling the default handler.
		*/
		if ((state & CANVAS) != 0) {
			switch (keyval) {
				case GDK.GDK_Return:
				case GDK.GDK_KP_Enter:
					return true;
			}
		}
	}

	return handled;
}

@Override
long gtk_key_press_event (long widget, long event) {
	long result = super.gtk_key_press_event (widget, event);
	if (result != 0) return result;
	/*
	* Feature in GTK.  The default behavior when the return key
	* is pressed is to select the default button.  This is not the
	* expected behavior for Composite and its subclasses.  The
	* fix is to avoid calling the default handler.
	*/
	if ((state & CANVAS) != 0 && socketHandle == 0) {
		int [] eventKeyval = new int [1];
		GDK.gdk_event_get_keyval(event, eventKeyval);

		switch (eventKeyval[0]) {
			case GDK.GDK_Return:
			case GDK.GDK_KP_Enter: return 1;
		}
	}
	return result;
}

@Override
long gtk_focus (long widget, long directionType) {
	if (widget == socketHandle) return 0;
	return super.gtk_focus (widget, directionType);
}

@Override
long gtk_focus_in_event (long widget, long event) {
	long result = super.gtk_focus_in_event (widget, event);
	return (state & CANVAS) != 0 ? 1 : result;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	long result = super.gtk_focus_out_event (widget, event);
	return (state & CANVAS) != 0 ? 1 : result;
}

@Override
long gtk_map (long widget) {
	fixZOrder ();
	return 0;
}

@Override
long gtk_realize (long widget) {
	long result = super.gtk_realize (widget);
	if (socketHandle != 0) {
		embeddedHandle = GTK.gtk_socket_get_id (socketHandle);
	}
	return result;
}

@Override
long gtk_scroll_child (long widget, long scrollType, long horizontal) {
	/* Stop GTK scroll child signal for canvas */
	OS.g_signal_stop_emission_by_name (widget, OS.scroll_child);
	if (GTK.GTK4) {
		// GtkScrollBar moved out of GtkRange, get GtkScrollType from this signal instead
		if (horizontalBar != null ) horizontalBar.detail = (int) scrollType;
		if (verticalBar != null) verticalBar.detail = (int) scrollType;
	}
	return 1;
}

@Override
long gtk_style_updated (long widget) {
	long result = super.gtk_style_updated (widget);
	if ((style & SWT.NO_BACKGROUND) != 0) {
		//TODO: implement this on GTK3 as pixmaps are gone.
	}
	return result;
}

boolean hasBorder () {
	return (style & SWT.BORDER) != 0;
}

@Override
void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		if (!GTK.GTK4) GTK3.gtk_widget_add_events (handle, GDK.GDK_POINTER_MOTION_HINT_MASK);
		if (scrolledHandle != 0) {
			OS.g_signal_connect_closure (scrolledHandle, OS.scroll_child, display.getClosure (SCROLL_CHILD), false);
		}
	}
}

boolean hooksKeys () {
	return hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
}

@Override
long imHandle () {
	return imHandle;
}

/**
 * Returns <code>true</code> if the receiver or any ancestor
 * up to and including the receiver's nearest ancestor shell
 * has deferred the performing of layouts.  Otherwise, <code>false</code>
 * is returned.
 *
 * @return the receiver's deferred layout state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLayoutDeferred(boolean)
 * @see #getLayoutDeferred()
 *
 * @since 3.1
 */
public boolean isLayoutDeferred () {
	checkWidget ();
	return findDeferredControl () != null;
}

@Override
boolean isTabGroup() {
	if ((state & CANVAS) != 0) return true;
	return super.isTabGroup();
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * Use of this method is discouraged since it is the least-efficient
 * way to trigger a layout. The use of <code>layout(true)</code>
 * discards all cached layout information, even from controls which
 * have not changed. It is much more efficient to invoke
 * {@link Control#requestLayout()} on every control which has changed
 * in the layout than it is to invoke this method on the layout itself.
 * </p>
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	checkWidget ();
	layout (true);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children.
 * If the argument is <code>true</code> the layout must not rely
 * on any information it has cached about the immediate children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's
 * children has changed state since the last layout.
 * If the receiver does not have a layout, do nothing.
 * <p>
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
 * </p>
 * <p>
 * If a child is resized as a result of a call to layout, the
 * resize event will invoke the layout of the child.  The layout
 * will cascade down through all child widgets in the receiver's widget
 * tree until a child is encountered that does not resize.  Note that
 * a layout due to a resize will not flush any cached information
 * (same as <code>layout(false)</code>).
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget ();
	if (layout == null) return;
	layout (changed, false);
}

/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children.
 * If the changed argument is <code>true</code> the layout must not rely
 * on any information it has cached about its children. If it
 * is <code>false</code> the layout may (potentially) optimize the
 * work it is doing by assuming that none of the receiver's
 * children has changed state since the last layout.
 * If the all argument is <code>true</code> the layout will cascade down
 * through all child widgets in the receiver's widget tree, regardless of
 * whether the child has changed size.  The changed argument is applied to
 * all layouts.  If the all argument is <code>false</code>, the layout will
 * <em>not</em> cascade down through all child widgets in the receiver's widget
 * tree.  However, if a child is resized as a result of a call to layout, the
 * resize event will invoke the layout of the child.  Note that
 * a layout due to a resize will not flush any cached information
 * (same as <code>layout(false)</code>).
 * <p>
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (boolean changed, boolean all) {
	checkWidget ();
	if (layout == null && !all) return;
	markLayout (changed, all);
	updateLayout (all);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that
 * are in the parent hierarchy of the changed control up to and including the
 * receiver.  The layouts in the hierarchy must not rely on any information
 * cached about the changed control or any of its ancestors.  The layout may
 * (potentially) optimize the work it is doing by assuming that none of the
 * peers of the changed control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * <p>
 * It is normally more efficient to invoke {@link Control#requestLayout()}
 * on every control which has changed in the layout than it is to invoke
 * this method on the layout itself. Clients are encouraged to use
 * {@link Control#requestLayout()} where possible instead of calling
 * this method.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed a control that has had a state change which requires a recalculation of its size
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void layout (Control [] changed) {
	checkWidget ();
	if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
	layout (changed, SWT.NONE);
}

/**
 * Forces a lay out (that is, sets the size and location) of all widgets that
 * are in the parent hierarchy of the changed control up to and including the
 * receiver.
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * </p>
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be laid out</dd>
 * <dt><b>SWT.CHANGED</b></dt>
 * <dd>the layout must flush its caches</dd>
 * <dt><b>SWT.DEFER</b></dt>
 * <dd>layout will be deferred</dd>
 * </dl>
 * <p>
 * When the <code>changed</code> array is specified, the flags <code>SWT.ALL</code>
 * and <code>SWT.CHANGED</code> have no effect. In this case, the layouts in the
 * hierarchy must not rely on any information cached about the changed control or
 * any of its ancestors.  The layout may (potentially) optimize the
 * work it is doing by assuming that none of the peers of the changed
 * control have changed state since the last layout.
 * If an ancestor does not have a layout, skip it.
 * </p>
 * <p>
 * When the <code>changed</code> array is not specified, the flag <code>SWT.ALL</code>
 * indicates that the whole widget tree should be laid out. And the flag
 * <code>SWT.CHANGED</code> indicates that the layouts should flush any cached
 * information for all controls that are laid out.
 * </p>
 * <p>
 * The <code>SWT.DEFER</code> flag always causes the layout to be deferred by
 * calling <code>Composite.setLayoutDeferred(true)</code> and scheduling a call
 * to <code>Composite.setLayoutDeferred(false)</code>, which will happen when
 * appropriate (usually before the next event is handled). When this flag is set,
 * the application should not call <code>Composite.setLayoutDeferred(boolean)</code>.
 * </p>
 * <p>
 * Note: Layout is different from painting. If a child is
 * moved or resized such that an area in the parent is
 * exposed, then the parent will paint. If no child is
 * affected, the parent will not paint.
 * </p>
 *
 * @param changed a control that has had a state change which requires a recalculation of its size
 * @param flags the flags specifying how the layout should happen
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the controls in changed is null or has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public void layout (Control [] changed, int flags) {
	checkWidget ();
	if (changed != null) {
		for (int i=0; i<changed.length; i++) {
			Control control = changed [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			boolean ancestor = false;
			Composite composite = control.parent;
			while (composite != null) {
				ancestor = composite == this;
				if (ancestor) break;
				composite = composite.parent;
			}
			if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
		}
		int updateCount = 0;
		Composite [] update = new Composite [16];
		for (int i=0; i<changed.length; i++) {
			Control child = changed [i];
			Composite composite = child.parent;
			// Update layout when the list of children has changed.
			// See bug 497812.
			child.markLayout(false, false);
			while (child != this) {
				if (composite.layout != null) {
					composite.state |= LAYOUT_NEEDED;
					if (!composite.layout.flushCache (child)) {
						composite.state |= LAYOUT_CHANGED;
					}
				}
				if (updateCount == update.length) {
					Composite [] newUpdate = new Composite [update.length + 16];
					System.arraycopy (update, 0, newUpdate, 0, update.length);
					update = newUpdate;
				}
				child = update [updateCount++] = composite;
				composite = child.parent;
			}
		}
		if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		for (int i=updateCount-1; i>=0; i--) {
			update [i].updateLayout (false);
		}
	} else {
		if (layout == null && (flags & SWT.ALL) == 0) return;
		markLayout ((flags & SWT.CHANGED) != 0, (flags & SWT.ALL) != 0);
		if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
			setLayoutDeferred (true);
			display.addLayoutDeferred (this);
		}
		updateLayout ((flags & SWT.ALL) != 0);
	}
}

@Override
void markLayout (boolean changed, boolean all) {
	if (layout != null) {
		state |= LAYOUT_NEEDED;
		if (changed) state |= LAYOUT_CHANGED;
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].markLayout (changed, all);
		}
	}
}

void moveAbove (long child, long sibling) {
	if (child == sibling) return;
	long parentHandle = parentingHandle ();
	OS.swt_fixed_restack (parentHandle, child, sibling, true);
	return;
}

void moveBelow (long child, long sibling) {
	if (child == sibling) return;
	long parentHandle = parentingHandle ();
	if (sibling == 0 && parentHandle == fixedHandle) {
		moveAbove (child, scrolledHandle != 0  ? scrolledHandle : handle);
		return;
	}
	OS.swt_fixed_restack (parentHandle, child, sibling, false);
	return;
}

@Override
void moveChildren(int oldWidth) {
	Control[] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children[i];
		long topHandle = child.topHandle ();
		GtkAllocation allocation = new GtkAllocation();
		GTK.gtk_widget_get_allocation (topHandle, allocation);
		int x = allocation.x;
		int y = allocation.y;
		int controlWidth = (child.state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
		if (oldWidth > 0) x = oldWidth - controlWidth - x;
		int clientWidth = getClientWidth ();
		x = clientWidth - controlWidth - x;
		if (!GTK.GTK4) {
			if (child.enableWindow != 0) {
				GDK.gdk_window_move (child.enableWindow, x, y);
			}
		}
		child.moveHandle (x, y);
		/*
		* Cause a size allocation this widget's topHandle.  Note that
		* all calls to gtk_widget_size_allocate() must be preceded by
		* a call to gtk_widget_size_request().
		*/
		GtkRequisition requisition = new GtkRequisition ();
		gtk_widget_get_preferred_size (topHandle, requisition);
		allocation.x = x;
		allocation.y = y;
		gtk_widget_size_allocate(topHandle, allocation, -1);
		Control control = child.findBackgroundControl ();
		if (control != null && control.backgroundImage != null) {
			if (child.isVisible ()) child.redrawWidget (0, 0, 0, 0, true, true, true);
		}
	}
}

Point minimumSize (int wHint, int hHint, boolean changed) {
	Control [] children = _getChildren ();
	/*
	 * Since getClientArea can be overridden by subclasses, we cannot
	 * call getClientAreaInPixels directly.
	 */
	Rectangle clientArea = DPIUtil.autoScaleUp(getClientArea ());
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = DPIUtil.autoScaleUp(children [i].getBounds ());
		width = Math.max (width, rect.x - clientArea.x + rect.width);
		height = Math.max (height, rect.y - clientArea.y + rect.height);
	}
	return new Point (width, height);
}

long parentingHandle () {
	if ((state & CANVAS) != 0) return handle;
	return fixedHandle != 0 ? fixedHandle : handle;
}

@Override
void printWidget (GC gc, long drawable, int depth, int x, int y) {
	Region oldClip = new Region (gc.getDevice ());
	Region newClip = new Region (gc.getDevice ());
	Point loc = DPIUtil.autoScaleDown(new Point (x, y));
	gc.getClipping (oldClip);
	Rectangle rect = getBounds ();
	newClip.add (oldClip);
	newClip.intersect (loc.x, loc.y, rect.width, rect.height);
	gc.setClipping (newClip);
	super.printWidget (gc, drawable, depth, x, y);
	Rectangle clientRect = getClientAreaInPixels ();
	Point pt = display.mapInPixels (this, parent, clientRect.x, clientRect.y);
	clientRect.x = x + pt.x - rect.x;
	clientRect.y = y + pt.y - rect.y;
	newClip.intersect (DPIUtil.autoScaleDown(clientRect));
	gc.setClipping (newClip);
	Control [] children = _getChildren ();
	for (int i=children.length-1; i>=0; --i) {
		Control child = children [i];
		if (child.getVisible ()) {
			Point location = child.getLocationInPixels ();
			child.printWidget (gc, drawable, depth, x + location.x, y + location.y);
		}
	}
	gc.setClipping (oldClip);
	oldClip.dispose ();
	newClip.dispose ();
}

/**
 * Connects this widget's fixedHandle to the "draw" signal.<br>
 * NOTE: only the "draw" (EXPOSE) signal is connected, not EXPOSE_EVENT_INVERSE.
 */
void connectFixedHandleDraw () {
	long paintHandle = fixedHandle;
	int paintMask = GDK.GDK_EXPOSURE_MASK;
	GTK3.gtk_widget_add_events (paintHandle, paintMask);

	OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [DRAW], 0, display.getClosure (DRAW), true);
}

/**
 * <p>Propagates draw events from a parent container to its children using
 * gtk_container_propagate_draw(). This method only works if the fixedHandle
 * has been connected to the "draw" signal, and only propagates draw events
 * to other siblings of handle (i.e. other children of fixedHandle, but not
 * handle itself).</p>
 *
 * <p>It's useful to propagate draw events to other child widgets for things
 * like Table/Tree editors, or other scenarios where a widget is a child of
 * a non-standard container widget (i.e., not a direct child of a Composite).</p>
 *
 * @param container the parent container, i.e. fixedHandle
 * @param cairo the cairo context provided by GTK
 */
void propagateDraw (long container, long cairo) {
	if (container == fixedHandle) {
		if (GTK.GTK4) {
			for (long child = GTK4.gtk_widget_get_first_child(container); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
				//TODO: GTK4 no gtk_container_propagate_draw. Possibly not required at all.
			}
		} else {
			long list = GTK3.gtk_container_get_children (container);
			long temp = list;
			while (temp != 0) {
				long child = OS.g_list_data (temp);
				if (child != 0) {
					Widget widget = display.getWidget (child);
					if (widget != this) {
						if (noChildDrawing) {
							Boolean childLowered = childrenLowered.get(widget);
							if (childLowered == null) {
								childrenLowered.put(widget, false);
								childLowered = false;
							}
							GtkAllocation allocation = new GtkAllocation ();
							GTK.gtk_widget_get_allocation(child, allocation);
							if (allocation.y < 0) {
								if (!childLowered) {
									long window = gtk_widget_get_window(child);
									GDK.gdk_window_lower(window);
									childrenLowered.put(widget, true);
								}
							} else {
								if (childLowered) {
									long window = gtk_widget_get_window(child);
									GDK.gdk_window_raise(window);
									childrenLowered.put(widget, false);
								}
								GTK3.gtk_container_propagate_draw(container, child, cairo);
							}
						} else {
							GTK3.gtk_container_propagate_draw(container, child, cairo);
						}
					}
				}
				temp = OS.g_list_next (temp);
			}
			OS.g_list_free (list);
		}
	}
}

@Override
void redrawChildren () {
	super.redrawChildren ();
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		Control child = children [i];
		if ((child.state & PARENT_BACKGROUND) != 0) {
			child.redrawWidget (0, 0, 0, 0, true, false, true);
			child.redrawChildren ();
		}
	}
}

@Override
void register () {
	super.register ();
	if (socketHandle != 0) display.addWidget (socketHandle, this);
}

@Override
void releaseChildren (boolean destroy) {
	try (ExceptionStash exceptions = new ExceptionStash ()) {
		for (Control child : _getChildren ()) {
			if (child == null || child.isDisposed ())
				continue;

			try {
				child.release (false);
			} catch (Error | RuntimeException ex) {
				exceptions.stash (ex);
			}
		}
		super.releaseChildren (destroy);
	}
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	socketHandle = embeddedHandle = 0;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (imHandle != 0) OS.g_object_unref (imHandle);
	imHandle = 0;
	layout = null;
	tabList = null;
}

void removeControl (Control control) {
	fixTabList (control);
}

@Override
void reskinChildren (int flags) {
	super.reskinChildren (flags);
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null) child.reskin (flags);
	}
}

@Override
void resizeHandle (int width, int height) {
	super.resizeHandle (width, height);
	if (socketHandle != 0) {
		OS.swt_fixed_resize (handle, socketHandle, width, height);
	}
}

/**
 * Sets the background drawing mode to the argument which should
 * be one of the following constants defined in class <code>SWT</code>:
 * <code>INHERIT_NONE</code>, <code>INHERIT_DEFAULT</code>,
 * <code>INHERIT_FORCE</code>.
 *
 * @param mode the new background mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 *
 * @since 3.2
 */
public void setBackgroundMode (int mode) {
	checkWidget ();
	backgroundMode = mode;
	Control[] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();
	}
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {

	/* Bug 487757
	 * Deal with bug where for Gtk3.8+, ScrolledComposites appear empty or internal widgets don't
	 * get sized properly. When resizing shell, widgets appear or grow into correct size.
	 *
	 * The issue is that upon widget creation ZERO_HEIGHT/WIDTH is assigned to widget's state (createWidget());
	 * then if you call setVisible(false), setVisible(true) before a layout operation,
	 * in the setVisible(true) call, the widget is never shown. In Gtk3.8+ hidden widgets have a
	 * size of 0x0, and as such allocation is not done correctly until you manually re-size the parent.
	 *
	 * To resolve the issue we check if widget is not HIDDEN on SWT side, but is hidden on GTK side. If it
	 * is, then show it. This correctly initializes layout of internal widgets.
	 *
	 * Future note: If you observe non Composite widgets to have incorrect sizing on first display
	 * but fix themselfes upon first resize by mouse, then
	 * consider moving this fix higher into Control's setBound(...) method instead.
	 */
	long topHandle = topHandle ();
	if (fixedHandle != 0 && handle != 0
			&& getVisible() && !GTK.gtk_widget_get_visible(topHandle) //if SWT State is not HIDDEN, but widget is hidden on GTK side.
			&& topHandle == fixedHandle && width > 0 && height > 0 && resize) {
		GTK.gtk_widget_show(topHandle);
	}

	int result = super.setBounds (x, y, width, height, move, resize);
	if ((result & RESIZED) != 0 && layout != null) {
		markLayout (false, false);
		updateLayout (false);
	}
	return result;
}

@Override
public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	this.layout = layout;
}

/**
 * If the argument is <code>true</code>, causes subsequent layout
 * operations in the receiver or any of its children to be ignored.
 * No layout of any kind can occur in the receiver or any of its
 * children until the flag is set to false.
 * Layout operations that occurred while the flag was
 * <code>true</code> are remembered and when the flag is set to
 * <code>false</code>, the layout operations are performed in an
 * optimized manner.  Nested calls to this method are stacked.
 *
 * @param defer the new defer state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #layout(boolean)
 * @see #layout(Control[])
 *
 * @since 3.1
 */
public void setLayoutDeferred (boolean defer) {
	checkWidget();
	if (!defer) {
		if (--layoutCount == 0) {
			if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
				updateLayout (true);
			}
		}
	} else {
		layoutCount++;
	}
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if (!create) {
		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		int orientation = style & flags;
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children[i].setOrientation (orientation);
		}
		if (((style & SWT.RIGHT_TO_LEFT) != 0) != ((style & SWT.MIRRORED) != 0)) {
			moveChildren (-1);
		}
	}
}

@Override
boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	boolean changed = super.setScrollBarVisible (bar, visible);
	if (changed && layout != null) {
		markLayout (false, false);
		updateLayout (false);
	}
	return changed;
}

@Override
boolean setTabGroupFocus (boolean next) {
	if (isTabItem ()) return setTabItemFocus (next);
	boolean takeFocus = (style & SWT.NO_FOCUS) == 0;
	if ((state & CANVAS) != 0) takeFocus = hooksKeys ();
	if (socketHandle != 0) takeFocus = true;
	if (takeFocus  && setTabItemFocus (next)) return true;
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		/*
		 * It is unlikely but possible that a child is disposed at this point, for more
		 * details refer bug 381668.
		 */
		if (!child.isDisposed() && child.isTabItem () && child.setTabItemFocus (next)) return true;
	}
	return false;
}

@Override
boolean setTabItemFocus (boolean next) {
	if (!super.setTabItemFocus (next)) return false;
	if (socketHandle != 0) {
		int direction = next ? GTK.GTK_DIR_TAB_FORWARD : GTK.GTK_DIR_TAB_BACKWARD;
		GTK.gtk_widget_child_focus (socketHandle, direction);
	}
	return true;
}

/**
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order or null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li>
 *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabList (Control [] tabList) {
	checkWidget ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			Control control = tabList [i];
			if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			if (control.parent != this) error (SWT.ERROR_INVALID_PARENT);
		}
		Control [] newList = new Control [tabList.length];
		System.arraycopy (tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	}
	this.tabList = tabList;
}

@Override
void showWidget () {
	super.showWidget ();
	if (socketHandle != 0) {
		GTK.gtk_widget_show (socketHandle);
		embeddedHandle = GTK.gtk_socket_get_id (socketHandle);
	}
	if (scrolledHandle == 0) fixStyle (handle);
}

@Override
boolean checkSubwindow () {
	return (state & CHECK_SUBWINDOW) != 0;
}

@Override
boolean translateMnemonic (Event event, Control control) {
	if (super.translateMnemonic (event, control)) return true;
	if (control != null) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			if (child.translateMnemonic (event, control)) return true;
		}
	}
	return false;
}

@Override
int traversalCode(int key, long event) {
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) return 0;
		if (hooksKeys ()) return 0;
	}
	return super.traversalCode (key, event);
}

@Override
boolean translateTraversal (long event) {
	if (socketHandle != 0) return false;
	return super.translateTraversal (event);
}

@Override
void updateBackgroundMode () {
	super.updateBackgroundMode ();
	Control [] children = _getChildren ();
	for (int i = 0; i < children.length; i++) {
		children [i].updateBackgroundMode ();
	}
}

@Override
void updateLayout (boolean all) {
	Composite parent = findDeferredControl ();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin();
		layout.layout (this, changed);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			children [i].updateLayout (all);
		}
	}
}

@Override
public String toString() {
	return super.toString() + " [layout=" + layout + "]";
}

}
