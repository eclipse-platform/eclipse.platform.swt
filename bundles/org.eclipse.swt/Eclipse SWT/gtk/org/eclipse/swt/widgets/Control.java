/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Stefan Xenos (Google) - bug 468854 - Add a requestLayout method to Control
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT, FLIP_TEXT_DIRECTION</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, MouseWheel, MouseHorizontalWheel, MouseVerticalWheel, Move,
 *     Paint, Resize, Traverse</dd>
 * </dl>
 * <p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#control">Control snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Control extends Widget implements Drawable {

	// allows to disable context menu entry for "insert emoji"
	static final boolean DISABLE_EMOJI = Boolean.getBoolean("SWT_GTK_INPUT_HINT_NO_EMOJI");

	long fixedHandle;
	long firstFixedHandle = 0;
	long keyController;
	long redrawWindow, enableWindow, provider;
	int drawCount, backgroundAlpha = 255;
	long dragGesture, zoomGesture, rotateGesture, panGesture;
	Composite parent;
	Cursor cursor;
	Menu menu;
	Image backgroundImage;
	Font font;
	Region region;
	long eventRegion;
	/**
	 * The handle to the Region, which is neccessary in the case
	 * that <code>region</code> is disposed before this Control.
	 */
	long regionHandle;
	String toolTipText;
	Object layoutData;
	Accessible accessible;
	Control labelRelation;
	String cssBackground, cssForeground = " ";
	boolean drawRegion;
	/**
	 * Cache the NO_BACKGROUND flag as it gets removed automatically in
	 * Composite. Only relevant for GTK3.10+ as we need it for Cairo setRegion()
	 * functionality. See bug 475784.
	 */
	boolean cachedNoBackground;
	/**
	 * Point for storing the (x, y) coordinate of the last input (click/scroll/etc.).
	 * This is useful for checking input event coordinates in methods that act on input,
	 * but do not receive coordinates (like gtk_clicked, for example). See bug 529431.
	 */
	Point lastInput = new Point(0, 0);

	LinkedList <Event> dragDetectionQueue;

	static Callback gestureZoom, gestureRotation, gestureSwipe, gestureBegin, gestureEnd;
	static {
		gestureZoom = new Callback (Control.class, "magnifyProc", void.class, new Type[] {
				long.class, double.class, long.class}); //$NON-NLS-1$
		gestureRotation = new Callback (Control.class, "rotateProc", void.class, new Type[] {
				long.class, double.class, double.class, long.class}); //$NON-NLS-1$
		gestureSwipe = new Callback (Control.class, "swipeProc", void.class, new Type[] {
				long.class, double.class, double.class, long.class}); //$NON-NLS-1$
		gestureBegin = new Callback (Control.class, "gestureBeginProc", void.class, new Type[] {
				long.class, long.class, long.class}); //$NON-NLS-1$
		gestureEnd = new Callback (Control.class, "gestureEndProc", void.class, new Type[] {
				long.class, long.class, long.class}); //$NON-NLS-1$
	}
	/**
	 * Bug 541635, 515396: GTK Wayland only flag to keep track whether mouse
	 * is currently pressed or released for DND.
	 */
	static boolean mouseDown;

	/**
	 * Flag to check the scale factor upon the first drawing of this Control.
	 * This is done by checking the scale factor of the Cairo surface in gtk_draw().
	 *
	 * Doing so provides an accurate scale factor, and will determine if this Control
	 * needs to be scaled manually by SWT. See bug 507020.
	 */
	boolean checkScaleFactor = true;

	/**
	 * True if GTK has autoscaled this Control, meaning SWT does not need to do any
	 * manual scaling. See bug 507020.
	 */
	boolean autoScale = true;

Control () {
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
 * @see SWT#BORDER
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

Font defaultFont () {
	return display.getSystemFont ();
}

GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).handle;
}

@Override
void deregister () {
	super.deregister ();
	if (fixedHandle != 0) display.removeWidget (fixedHandle);
	long imHandle = imHandle ();
	if (imHandle != 0) display.removeWidget (imHandle);
}

void drawBackground (Control control, long gdkResource, long cr, int x, int y, int width, int height) {
	long cairo = 0;
	long region = 0;
	if (GTK.GTK4) {
		// TODO: once Eclipse runs on GTK4, check for bug 547466.
		cairo = cr;
		if (gdkResource != 0) {
			cairo_rectangle_int_t regionRect = new cairo_rectangle_int_t ();
			int [] fetchedHeight = new int [1];
			int [] fetchedWidth = new int [1];
			if (GTK.GTK4) {
				gdk_surface_get_size(gdkResource, fetchedWidth, fetchedHeight);
			} else {
				gdk_window_get_size(gdkResource, fetchedWidth, fetchedHeight);
			}
			regionRect.x = 0;
			regionRect.y = 0;
			regionRect.width = fetchedWidth[0];
			regionRect.height = fetchedHeight[0];
			region = Cairo.cairo_region_create_rectangle(regionRect);
		}
	} else {
		cairo = cr != 0 ? cr : GDK.gdk_cairo_create(gdkResource);
	}
	/*
	 * It's possible that a client is using an SWT.NO_BACKGROUND Composite with custom painting
	 * and a region to provide "overlay" functionality. In this case we don't want to paint
	 * any background color, as it will likely break desired behavior. The fix is to paint
	 * this Control as transparent. See bug 475784.
	 */
	boolean noBackgroundRegion = drawRegion && hooks(SWT.Paint) && cachedNoBackground;
	if (cairo == 0) error (SWT.ERROR_NO_HANDLES);
	if (region != 0) {
		GDK.gdk_cairo_region(cairo, region);
		Cairo.cairo_clip(cairo);
	}
	if (control.backgroundImage != null) {
		Point pt = display.mapInPixels (this, control, 0, 0);
		Cairo.cairo_translate (cairo, -pt.x, -pt.y);
		x += pt.x;
		y += pt.y;
		long pattern = Cairo.cairo_pattern_create_for_surface (control.backgroundImage.surface);
		if (pattern == 0) error (SWT.ERROR_NO_HANDLES);
		Cairo.cairo_pattern_set_extend (pattern, Cairo.CAIRO_EXTEND_REPEAT);
		if ((style & SWT.MIRRORED) != 0) {
			double[] matrix = {-1, 0, 0, 1, 0, 0};
			Cairo.cairo_pattern_set_matrix(pattern, matrix);
		}
		Cairo.cairo_set_source (cairo, pattern);
		Cairo.cairo_pattern_destroy (pattern);
	} else {
		GdkRGBA rgba = control.getBackgroundGdkRGBA();
		if (noBackgroundRegion) {
			Cairo.cairo_set_source_rgba (cairo, 0.0, 0.0, 0.0, 0.0);
		} else {
			Cairo.cairo_set_source_rgba (cairo, rgba.red, rgba.green, rgba.blue, rgba.alpha);
		}
	}
	Cairo.cairo_rectangle (cairo, x, y, width, height);
	Cairo.cairo_fill (cairo);
	if (!GTK.GTK4 ) {
		if (cairo != cr) Cairo.cairo_destroy(cairo);
	}
}

boolean drawGripper (GC gc, int x, int y, int width, int height, boolean vertical) {
	long paintHandle = paintHandle ();
	long gdkResource = GTK.GTK4? gtk_widget_get_surface(paintHandle) : gtk_widget_get_window (paintHandle);
	if (gdkResource == 0) return false;
	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - width - x;
	long context = GTK.gtk_widget_get_style_context (paintHandle);
	GTK.gtk_style_context_save (context);
	GTK.gtk_style_context_add_class (context, GTK.GTK_STYLE_CLASS_PANE_SEPARATOR);
	GTK.gtk_style_context_set_state (context, GTK.GTK_STATE_FLAG_NORMAL);
	GTK.gtk_render_handle (context, gc.handle, x, y, width, height);
	GTK.gtk_style_context_restore (context);
	return true;
}

void drawWidget (GC gc) {
}

void enableWidget (boolean enabled) {
	GTK.gtk_widget_set_sensitive (handle, enabled);
}

long enterExitHandle () {
	return eventHandle ();
}

long eventHandle () {
	return handle;
}

long eventWindow () {
	long eventHandle = eventHandle ();
	GTK.gtk_widget_realize (eventHandle);
	return gtk_widget_get_window (eventHandle);
}

long eventSurface () {
	long eventHandle = eventHandle ();
	GTK.gtk_widget_realize (eventHandle);
	return gtk_widget_get_surface (eventHandle);
}

/**
 * GdkEventType constants different on GTK4 and GTK3.
 * This checks for GTK versions and return the correct constants defined in GDK.java
 * @return constant defined
 */
static int fixGdkEventTypeValues(int eventType) {
	if (GTK.GTK4) {
		switch (eventType) {
			case GDK.GDK4_EXPOSE: return GDK.GDK_EXPOSE;
			case GDK.GDK4_MOTION_NOTIFY: return GDK.GDK_MOTION_NOTIFY;
			case GDK.GDK4_BUTTON_PRESS: return GDK.GDK_BUTTON_PRESS;
			case GDK.GDK4_BUTTON_RELEASE: return GDK.GDK_BUTTON_RELEASE;
			case GDK.GDK4_KEY_PRESS: return GDK.GDK_KEY_PRESS;
			case GDK.GDK4_ENTER_NOTIFY: return GDK.GDK_ENTER_NOTIFY;
			case GDK.GDK4_LEAVE_NOTIFY: return GDK.GDK_LEAVE_NOTIFY;
			case GDK.GDK4_FOCUS_CHANGE: return GDK.GDK_FOCUS_CHANGE;
			case GDK.GDK4_CONFIGURE: return GDK.GDK_CONFIGURE;
			case GDK.GDK4_MAP: return GDK.GDK_MAP;
			case GDK.GDK4_UNMAP: return GDK.GDK_UNMAP;
		}
	}
	return eventType;
}

void fixFocus (Control focusControl) {
	Shell shell = getShell ();
	Control control = this;
	while (control != shell && (control = control.parent) != null) {
		if (control.setFocus ()) return;
	}
	shell.setSavedFocus (focusControl);
	long focusHandle = shell.vboxHandle;
	GTK.gtk_widget_set_can_focus (focusHandle, true);
	GTK.gtk_widget_grab_focus (focusHandle);
	// widget could be disposed at this point
	if (isDisposed ()) return;
	GTK.gtk_widget_set_can_focus (focusHandle, false);
}

void fixStyle () {
	if (fixedHandle != 0) fixStyle (fixedHandle);
}

void fixStyle (long handle) {
	/*
	* Feature in GTK.  Some GTK themes apply a different background to
	* the contents of a GtkNotebook.  However, in an SWT TabFolder, the
	* children are not parented below the GtkNotebook widget, and usually
	* have their own GtkFixed.  The fix is to look up the correct style
	* for a child of a GtkNotebook and apply its background to any GtkFixed
	* widgets that are direct children of an SWT TabFolder.
	*
	* Note that this has to be when the theme settings changes and that it
	* should not override the application background.
	*/
	if ((state & BACKGROUND) != 0) return;
	if ((state & THEME_BACKGROUND) == 0) return;
}

long focusHandle () {
	return handle;
}

long fontHandle () {
	return handle;
}

long gestureHandle () {
	return handle;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns the text direction of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the text direction style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.102
 */
public int getTextDirection() {
	checkWidget ();
	/* return the widget orientation */
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

boolean hasFocus () {
	return this == display.getFocusControl();
}

@Override
void hookEvents () {
	super.hookEvents();
	long focusHandle = focusHandle();
	hookKeyboardAndFocusSignals(focusHandle);
	hookMouseSignals(eventHandle());
	hookWidgetSignals(focusHandle);
	hookPaintSignals();
	connectIMSignals();

	/*Connect gesture signals */
	setZoomGesture();
	setDragGesture();
	setRotateGesture();

	long eventHandle = eventHandle ();

	/* Connect the event_after signal for both key and mouse */
	if (GTK.GTK4) {
		//TODO: GTK4 event-after
	} else {
		OS.g_signal_connect_closure_by_id(eventHandle, display.signalIds[EVENT_AFTER], 0, display.getClosure(EVENT_AFTER), false);
		if (focusHandle != eventHandle) {
			OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[EVENT_AFTER], 0, display.getClosure(EVENT_AFTER), false);
		}
	}
}

private void hookKeyboardAndFocusSignals(long focusHandle) {
	if (GTK.GTK4) {
		keyController = GTK4.gtk_event_controller_key_new();
		GTK4.gtk_widget_add_controller(focusHandle, keyController);
		GTK.gtk_event_controller_set_propagation_phase(keyController, GTK.GTK_PHASE_CAPTURE);
		OS.g_signal_connect(keyController, OS.key_pressed, display.keyPressReleaseProc, KEY_PRESSED);
		OS.g_signal_connect(keyController, OS.key_released, display.keyPressReleaseProc, KEY_RELEASED);

		long focusController = GTK4.gtk_event_controller_focus_new();
		GTK4.gtk_widget_add_controller(focusHandle, focusController);
		OS.g_signal_connect(focusController, OS.enter, display.focusProc, FOCUS_IN);
		OS.g_signal_connect(focusController, OS.leave, display.focusProc, FOCUS_OUT);
	} else {
		int focusMask = GDK.GDK_KEY_PRESS_MASK | GDK.GDK_KEY_RELEASE_MASK | GDK.GDK_FOCUS_CHANGE_MASK;
		GTK3.gtk_widget_add_events (focusHandle, focusMask);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[KEY_PRESS_EVENT], 0, display.getClosure(KEY_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[KEY_RELEASE_EVENT], 0, display.getClosure(KEY_RELEASE_EVENT), false);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[FOCUS_IN_EVENT], 0, display.getClosure(FOCUS_IN_EVENT), false);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[FOCUS_OUT_EVENT], 0, display.getClosure(FOCUS_OUT_EVENT), false);
	}
}

private void hookMouseSignals(long eventHandle) {
	long enterExitHandle = enterExitHandle();

	if (GTK.GTK4) {
		// Click & motion events are limited only to the target widget
		long clickGesture = GTK4.gtk_gesture_click_new();
		GTK.gtk_event_controller_set_propagation_phase(clickGesture, GTK.GTK_PHASE_TARGET);
		GTK.gtk_gesture_single_set_button(clickGesture, 0);
		GTK4.gtk_widget_add_controller(eventHandle, clickGesture);
		OS.g_signal_connect(clickGesture, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);
		OS.g_signal_connect(clickGesture, OS.released, display.gesturePressReleaseProc, GESTURE_RELEASED);

		long motionController = GTK4.gtk_event_controller_motion_new();
		GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);
		GTK4.gtk_widget_add_controller(eventHandle, motionController);
		OS.g_signal_connect(motionController, OS.motion, display.enterMotionProc, MOTION);

		long scrollController = GTK4.gtk_event_controller_scroll_new(GTK.GTK_EVENT_CONTROLLER_SCROLL_BOTH_AXES);
		GTK.gtk_event_controller_set_propagation_phase(scrollController, GTK.GTK_PHASE_TARGET);
		GTK4.gtk_widget_add_controller(eventHandle, scrollController);
		OS.g_signal_connect(scrollController, OS.scroll, display.scrollProc, SCROLL);

		long enterExitController = GTK4.gtk_event_controller_motion_new();
		GTK4.gtk_widget_add_controller(enterExitHandle, enterExitController);
		OS.g_signal_connect(enterExitController, OS.enter, display.enterMotionProc, ENTER);
		OS.g_signal_connect(enterExitController, OS.leave, display.leaveProc, LEAVE);
	} else {
		int eventMask = GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK | GDK.GDK_SCROLL_MASK | GDK.GDK_SMOOTH_SCROLL_MASK;
		GTK3.gtk_widget_add_events (eventHandle, eventMask);
		OS.g_signal_connect_closure_by_id(eventHandle, display.signalIds[MOTION_NOTIFY_EVENT], 0, display.getClosure(MOTION_NOTIFY_EVENT), false);
		OS.g_signal_connect_closure_by_id(eventHandle, display.signalIds[BUTTON_PRESS_EVENT], 0, display.getClosure(BUTTON_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id(eventHandle, display.signalIds[BUTTON_RELEASE_EVENT], 0, display.getClosure(BUTTON_RELEASE_EVENT), false);
		OS.g_signal_connect_closure_by_id(eventHandle, display.signalIds[SCROLL_EVENT], 0, display.getClosure(SCROLL_EVENT), false);

		int enterExitMask = GDK.GDK_ENTER_NOTIFY_MASK | GDK.GDK_LEAVE_NOTIFY_MASK;
		GTK3.gtk_widget_add_events (enterExitHandle, enterExitMask);
		OS.g_signal_connect_closure_by_id(enterExitHandle, display.signalIds[ENTER_NOTIFY_EVENT], 0, display.getClosure(ENTER_NOTIFY_EVENT), false);
		OS.g_signal_connect_closure_by_id(enterExitHandle, display.signalIds[LEAVE_NOTIFY_EVENT], 0, display.getClosure(LEAVE_NOTIFY_EVENT), false);

		/*
		* Feature in GTK3.  Events such as mouse move are propagate up
		* the widget hierarchy and are seen by the parent.  This is the
		* correct GTK behavior but not correct for SWT.  The fix is to
		* hook a signal after and stop the propagation using a negative
		* event number to distinguish this case.
		*
		* The signal is hooked to the fixedHandle to catch events sent to
		* lightweight widgets.
		*/
		long blockHandle = fixedHandle != 0 ? fixedHandle : eventHandle;
		OS.g_signal_connect_closure_by_id(blockHandle, display.signalIds[BUTTON_PRESS_EVENT], 0, display.getClosure(BUTTON_PRESS_EVENT_INVERSE), true);
		OS.g_signal_connect_closure_by_id(blockHandle, display.signalIds[BUTTON_RELEASE_EVENT], 0, display.getClosure(BUTTON_RELEASE_EVENT_INVERSE), true);
		OS.g_signal_connect_closure_by_id(blockHandle, display.signalIds[MOTION_NOTIFY_EVENT], 0, display.getClosure(MOTION_NOTIFY_EVENT_INVERSE), true);
	}
}

private void hookWidgetSignals(long focusHandle) {
	OS.g_signal_connect_closure_by_id(handle, display.signalIds[REALIZE], 0, display.getClosure(REALIZE), true);
	OS.g_signal_connect_closure_by_id(handle, display.signalIds[UNREALIZE], 0, display.getClosure(UNREALIZE), false);

	OS.g_signal_connect_closure_by_id(topHandle(), display.signalIds[MAP], 0, display.getClosure(MAP), true);

	if (!GTK.GTK4) {
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[POPUP_MENU], 0, display.getClosure(POPUP_MENU), false);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[SHOW_HELP], 0, display.getClosure(SHOW_HELP), false);
		OS.g_signal_connect_closure_by_id(focusHandle, display.signalIds[FOCUS], 0, display.getClosure(FOCUS), false);
	}
}

private void hookPaintSignals() {
	long paintHandle = paintHandle();

	if (GTK.GTK4) {
		long widgetClass = GTK.GTK_WIDGET_GET_CLASS(paintHandle());
		GtkWidgetClass widgetClassStruct = new GtkWidgetClass();

		OS.memmove(widgetClassStruct, widgetClass);
		widgetClassStruct.snapshot = display.snapshotDrawProc;
		OS.memmove(widgetClass, widgetClassStruct);
	} else {
		int paintMask = GDK.GDK_EXPOSURE_MASK;
		GTK3.gtk_widget_add_events (paintHandle, paintMask);
		OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [DRAW], 0, display.getClosure (EXPOSE_EVENT_INVERSE), false);
		OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [DRAW], 0, display.getClosure (DRAW), true);
		OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [STYLE_UPDATED], 0, display.getClosure (STYLE_UPDATED), false);
	}
}

private void connectIMSignals() {
	long imHandle = imHandle();
	if (imHandle != 0) {
		OS.g_signal_connect_closure(imHandle, OS.commit, display.getClosure(COMMIT), false);
		OS.g_signal_connect_closure(imHandle, OS.preedit_changed, display.getClosure(PREEDIT_CHANGED), false);
	}
}

boolean hooksPaint () {
	return hooks (SWT.Paint) || filters (SWT.Paint);
}

@Override
long hoverProc (long widget) {
	int[] x = new int[1], y = new int[1], mask = new int[1];
	if (GTK.GTK4) {
		double[] xDouble = new double[1], yDouble = new double[1];
		display.getPointerPosition(xDouble, yDouble);

		x[0] = (int)xDouble[0];
		y[0] = (int)yDouble[0];
	} else {
		display.getWindowPointerPosition(0, x, y, mask);
	}

	if (containedInRegion(x[0], y[0])) return 0;
	sendMouseEvent(SWT.MouseHover, 0, 0, x[0], y[0], false, mask[0]);

	/* Always return zero in order to cancel the hover timer */
	return 0;
}

@Override
long topHandle() {
	if (fixedHandle != 0) return fixedHandle;
	return super.topHandle ();
}

long paintHandle () {
	long topHandle = topHandle ();
	if (GTK.GTK4) return topHandle;
	long paintHandle = handle;
	while (paintHandle != topHandle) {
		if (GTK3.gtk_widget_get_has_window(paintHandle)) break;
		paintHandle = GTK.gtk_widget_get_parent (paintHandle);
	}
	return paintHandle;
}

@Override
long paintWindow () {
	long paintHandle = paintHandle ();
	GTK.gtk_widget_realize (paintHandle);
	if (GTK.GTK4) {
		return gtk_widget_get_surface (paintHandle);
	} else {
		return gtk_widget_get_window (paintHandle);
	}
}

@Override
long paintSurface () {
	long paintHandle = paintHandle ();
	GTK.gtk_widget_realize (paintHandle);
	return gtk_widget_get_surface (paintHandle);
}

/**
 * Prints the receiver and all children.
 *
 * @param gc the gc where the drawing occurs
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
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
 * @since 3.4
 */
public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	long topHandle = topHandle ();
	GTK.gtk_widget_realize (topHandle);
	/*
	 * Feature in GTK: gtk_widget_draw() will only draw if the
	 * widget's priv->alloc_needed field is set to TRUE. Since
	 * this field is private and inaccessible, get and set the
	 * allocation to trigger it to be TRUE. See bug 530969.
	 */
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation(topHandle, allocation);
	// Prevent allocation warnings
	GTK.gtk_widget_get_preferred_size(topHandle, null, null);
	GTK3.gtk_widget_size_allocate(topHandle, allocation);
	GTK3.gtk_widget_draw(topHandle, gc.handle);
	return true;
}

void printWidget (GC gc, long drawable, int depth, int x, int y) {
	boolean obscured = (state & OBSCURED) != 0;
	state &= ~OBSCURED;
	long topHandle = topHandle ();
	long gdkResource = GTK.GTK4 ? gtk_widget_get_surface(topHandle) : gtk_widget_get_window (topHandle);
	printWindow (true, this, gc, drawable, depth, gdkResource, x, y);
	if (obscured) state |= OBSCURED;
}

void printWindow (boolean first, Control control, GC gc, long drawable, int depth, long window, int x, int y) {
	/*
	 * TODO: this needs to be re-implemented for GTK3 as it uses GdkDrawable which is gone.
	 * See: https://developer.gnome.org/gtk3/stable/ch26s02.html#id-1.6.3.4.7
	 */
	return;
}

/**
 * Returns the preferred size (in points) of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size of the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

Point computeSizeInPixels (int wHint, int hHint) {
	return computeSizeInPixels (wHint, hHint, true);
}

Widget computeTabGroup () {
	if (isTabGroup()) return this;
	return parent.computeTabGroup ();
}

Widget[] computeTabList() {
	if (isTabGroup()) {
		if (getVisible() && getEnabled()) {
			return new Widget[] {this};
		}
	}
	return new Widget[0];
}

Control computeTabRoot () {
	Control[] tabList = parent._getTabList();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

void checkBuffered () {
	style |= SWT.DOUBLE_BUFFERED;
}

void checkBackground () {
	Shell shell = getShell ();
	if (this == shell) return;
	state &= ~PARENT_BACKGROUND;
	Composite composite = parent;
	do {
		int mode = composite.backgroundMode;
		if (mode != SWT.INHERIT_NONE || backgroundAlpha == 0) {
			if (mode == SWT.INHERIT_DEFAULT || backgroundAlpha == 0) {
				Control control = this;
				do {
					if ((control.state & THEME_BACKGROUND) == 0) {
						return;
					}
					control = control.parent;
				} while (control != composite);
			}
			state |= PARENT_BACKGROUND;
			return;
		}
		if (composite == shell) break;
		composite = composite.parent;
	} while (true);
}

void checkBorder () {
	if (getBorderWidthInPixels () == 0) style &= ~SWT.BORDER;
}

void checkMirrored () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) style |= SWT.MIRRORED;
}

/**
 * Convenience method for checking whether an (x, y) coordinate is in the set
 * region. Only relevant for GTK3.10+.
 *
 * @param x an x coordinate
 * @param y a y coordinate
 * @return true if the coordinate (x, y) is in the region, false otherwise
 */
boolean containedInRegion (int x, int y) {
	if (drawRegion && eventRegion != 0) {
		return Cairo.cairo_region_contains_point(eventRegion, x, y);
	}
	return false;
}

@Override
void createWidget(int index) {
	state |= DRAG_DETECT;
	checkOrientation(parent);
	super.createWidget(index);
	checkBackground();
	if ((state & PARENT_BACKGROUND) != 0) setParentBackground();
	checkBuffered();
	showWidget();
	setInitialBounds();
	setZOrder(null, false, false);
	if (!GTK.GTK4) setRelations();
	checkMirrored();
	checkBorder();
}

/**
 * Returns the preferred size (in points) of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
 * </p><p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained.
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
 * @return the preferred size of the control.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	wHint = DPIUtil.autoScaleUp(wHint);
	hHint = DPIUtil.autoScaleUp(hHint);
	return DPIUtil.autoScaleDown (computeSizeInPixels (wHint, hHint, changed));
}

Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	return computeNativeSize (handle, wHint, hHint, changed);
}

Point computeNativeSize (long h, int wHint, int hHint, boolean changed) {
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
		GtkRequisition requisition = new GtkRequisition ();
		GTK.gtk_widget_get_preferred_size (h, null, requisition);
		width = requisition.width;
		height = requisition.height;
	} else if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int [] natural_size = new int [1];
		if (wHint == SWT.DEFAULT) {
			if (GTK.GTK4) {
				GTK4.gtk_widget_measure(h, GTK.GTK_ORIENTATION_HORIZONTAL, height, null, natural_size, null, null);
			} else {
				GTK3.gtk_widget_get_preferred_width_for_height (h, height, null, natural_size);
			}
			width = natural_size [0];
		} else {
			if (GTK.GTK4) {
				GTK4.gtk_widget_measure(h, GTK.GTK_ORIENTATION_VERTICAL, width, null, natural_size, null, null);
			} else {
				GTK3.gtk_widget_get_preferred_height_for_width (h, width, null, natural_size);
			}
			height = natural_size [0];
		}
	}
	return new Point(width, height);
}

void forceResize () {
	/*
	* Force size allocation on all children of this widget's
	* topHandle.  Note that all calls to gtk_widget_size_allocate()
	* must be preceded by a call to gtk_widget_size_request().
	*/
	long topHandle = topHandle ();
	GtkRequisition requisition = new GtkRequisition ();
	gtk_widget_get_preferred_size (topHandle, requisition);
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation(topHandle, allocation);
	gtk_widget_size_allocate(topHandle, allocation, -1);
}

/**
 * Returns the accessible object for the receiver.
 * <p>
 * If this is the first time this object is requested,
 * then the object is created and returned. The object
 * returned by getAccessible() does not need to be disposed.
 * </p>
 *
 * @return the accessible object
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Accessible#addAccessibleListener
 * @see Accessible#addAccessibleControlListener
 *
 * @since 2.0
 */
public Accessible getAccessible () {
	checkWidget ();
	return _getAccessible ();
}

Accessible _getAccessible () {
	if (accessible == null) {
		accessible = Accessible.internal_new_Accessible (this);
	}
	return accessible;
}

/**
 * Returns a rectangle describing the receiver's size and location in points
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a shell. In this case, the location is
 * relative to the display.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	return DPIUtil.autoScaleDown(getBoundsInPixels());
}

Rectangle getBoundsInPixels () {
	checkWidget();
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 :allocation.height;
	if ((parent.style & SWT.MIRRORED) != 0) x = parent.getClientWidth () - width - x;
	return new Rectangle (x, y, width, height);
}

/**
 * Sets the receiver's size and location in points to the rectangular
 * area specified by the argument. The <code>x</code> and
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	rect = DPIUtil.autoScaleUp(rect);
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true);
}

void setBoundsInPixels (Rectangle rect) {
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true);
}

/**
 * Sets the receiver's size and location in points to the rectangular
 * area specified by the arguments. The <code>x</code> and
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the <code>x</code>
 * and <code>y</code> arguments are relative to the display.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	Rectangle rect = DPIUtil.autoScaleUp(new Rectangle (x, y, width, height));
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true);
}

void setBoundsInPixels (int x, int y, int width, int height) {
	checkWidget();
	setBounds (x, y, Math.max (0, width), Math.max (0, height), true, true);
}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

void moveHandle (int x, int y) {
	long topHandle = topHandle ();
	long parentHandle = parent.parentingHandle ();
	OS.swt_fixed_move (parentHandle, topHandle, x, y);
}

void resizeHandle (int width, int height) {
	long topHandle = topHandle ();
	OS.swt_fixed_resize (GTK.gtk_widget_get_parent (topHandle), topHandle, width, height);
	if (topHandle != handle) {
		Point sizes = resizeCalculationsGTK3 (handle, width, height);
		width = sizes.x;
		height = sizes.y;
		OS.swt_fixed_resize (GTK.gtk_widget_get_parent (handle), handle, width, height);
	}
}

Point resizeCalculationsGTK3 (long widget, int width, int height) {
	Point sizes = new Point (width, height);
	/*
	 * Feature in GTK3.20+: size calculations take into account GtkCSSNode
	 * elements which we cannot access. If the to-be-allocated size minus
	 * these elements is < 0, allocate the preferred size instead. See bug 486068.
	 */
	GtkRequisition minimumSize = new GtkRequisition();
	GtkRequisition naturalSize = new GtkRequisition();
	GTK.gtk_widget_get_preferred_size(widget, minimumSize, naturalSize);
	/*
	 * Use the smallest of the minimum/natural sizes to prevent oversized
	 * widgets.
	 */
	int smallestWidth = Math.min(minimumSize.width, naturalSize.width);
	int smallestHeight = Math.min(minimumSize.height, naturalSize.height);
	sizes.x = (width - (smallestWidth - width)) < 0 ? smallestWidth : width;
	sizes.y = (height - (smallestHeight - height)) < 0 ? smallestHeight : height;
	return sizes;
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	// bug in GTK3 the crashes new shell only. See bug 472743
	width = Math.min(width, (2 << 14) - 1);
	height = Math.min(height, (2 << 14) - 1);

	long topHandle = topHandle ();
	boolean sendMove = move;
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	if ((parent.style & SWT.MIRRORED) != 0) {
		int clientWidth = parent.getClientWidth ();
		int oldWidth = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
		int oldX = clientWidth - oldWidth - allocation.x;
		if (move) {
			sendMove &= x != oldX;
			x = clientWidth - (resize ? width : oldWidth) - x;
		} else {
			move = true;
			x = clientWidth - (resize ? width : oldWidth) - oldX;
			y = allocation.y;
		}
	}
	boolean sameOrigin = true, sameExtent = true;
	if (move) {
		int oldX = allocation.x;
		int oldY = allocation.y;
		sameOrigin = x == oldX && y == oldY;
		if (!sameOrigin) {
			if (!GTK.GTK4) {
				if (enableWindow != 0) {
					GDK.gdk_window_move (enableWindow, x, y);
				}
			}
			moveHandle (x, y);
		}
	}
	int clientWidth = 0;
	if (resize) {
		int oldWidth = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
		int oldHeight = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
		sameExtent = width == oldWidth && height == oldHeight;
		if (!sameExtent && (style & SWT.MIRRORED) != 0) clientWidth = getClientWidth ();
		if (!sameExtent && !(width == 0 && height == 0)) {
			int newWidth = Math.max (1, width);
			int newHeight = Math.max (1, height);
			if (!GTK.GTK4) {
				if (redrawWindow != 0) {
					GDK.gdk_window_resize (redrawWindow, newWidth, newHeight);
				}
				if (enableWindow != 0) {
					GDK.gdk_window_resize (enableWindow, newWidth, newHeight);
				}
			}
			resizeHandle (newWidth, newHeight);
		}
	}
	if (!sameOrigin || !sameExtent) {
		/*
		* Cause a size allocation this widget's topHandle.  Note that
		* all calls to gtk_widget_size_allocate() must be preceded by
		* a call to gtk_widget_size_request().
		*/
		GtkRequisition requisition = new GtkRequisition ();
		gtk_widget_get_preferred_size (topHandle, requisition);
		if (move) {
			allocation.x = x;
			allocation.y = y;
		}
		if (resize) {
			allocation.width = width;
			allocation.height = height;
		}
		/*
		 * The widget needs to be shown before its size is allocated
		 * in GTK3.8, otherwise its allocation return 0
		 * 		See org.eclipse.swt.tests.gtk.snippets.Bug497705_setBoundsAfterSetVisible
		 * Note: gtk_widget_hide sometimes destroy focus on previously focused control,
		 * this might cause unnecessary focus change events. In case this happens, try remove
		 * gtk_widget_hide call or avoid setting bounds on invisible widgets.
		 */
		if (!GTK.gtk_widget_get_visible(topHandle))  {
			Control focusControl = display.getFocusControl();
			GTK.gtk_widget_show(topHandle);
			gtk_widget_get_preferred_size (topHandle, requisition);
			gtk_widget_size_allocate(topHandle, allocation, -1);
			GTK.gtk_widget_hide(topHandle);
			/* Bug 540002: Showing and hiding widget causes original focused control to loose focus,
			 * Reset focus to original focused control after dealing with allocation.
			 */
			if (focusControl != null && display.getFocusControl() != focusControl) {
				focusControl.setFocus();
			}
		} else {
			if (GTK.GTK4) {
				GTK4.gtk_widget_size_allocate (topHandle, allocation, -1);
			} else {
				// Prevent GTK+ allocation warnings, preferred size should be retrieved before setting allocation size.
				GTK.gtk_widget_get_preferred_size(topHandle, null, null);
				GTK3.gtk_widget_size_allocate (topHandle, allocation);
			}
		}
	}
	/*
	* Bug in GTK.  Widgets cannot be sized smaller than 1x1.
	* The fix is to hide zero-sized widgets and show them again
	* when they are resized larger.
	*/
	if (!sameExtent) {
		state = (width == 0) ? state | ZERO_WIDTH : state & ~ZERO_WIDTH;
		state = (height == 0) ? state | ZERO_HEIGHT : state & ~ZERO_HEIGHT;
		if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) != 0) {
			if (!GTK.GTK4) {
				if (enableWindow != 0) {
					GDK.gdk_window_hide(enableWindow);
				}
			}

			GTK.gtk_widget_hide(topHandle);
		} else {
			if ((state & HIDDEN) == 0) {
				if (!GTK.GTK4) {
					if (enableWindow != 0) {
						GDK.gdk_window_show_unraised(enableWindow);
					}
				}

				GTK.gtk_widget_show(topHandle);
			}
		}

		if ((style & SWT.MIRRORED) != 0) moveChildren (clientWidth);
	}
	int result = 0;
	if (move && !sameOrigin) {
		Control control = findBackgroundControl ();
		if (control != null && control.backgroundImage != null) {
			if (isVisible ()) redrawWidget (0, 0, 0, 0, true, true, true);
		}
		if (sendMove) sendEvent (SWT.Move);
		result |= MOVED;
	}
	if (resize && !sameExtent) {
		sendEvent (SWT.Resize);
		result |= RESIZED;
	}
	return result;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent in points (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is
 * relative to the display.
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget();
	return DPIUtil.autoScaleDown(getLocationInPixels());
}

Point getLocationInPixels () {
	checkWidget();
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	if ((parent.style & SWT.MIRRORED) != 0) {
		int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
		x = parent.getClientWidth () - width - x;
	}
	return new Point (x, y);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is
 * relative to the display.
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	location = DPIUtil.autoScaleUp(location);
	setBounds (location.x, location.y, 0, 0, true, false);
}

void setLocationInPixels (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (location.x, location.y, 0, 0, true, false);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is
 * relative to the display.
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation(int x, int y) {
	checkWidget();
	Point loc = DPIUtil.autoScaleUp(new Point (x, y));
	setBounds (loc.x, loc.y, 0, 0, true, false);
}

void setLocationInPixels(int x, int y) {
	checkWidget();
	setBounds (x, y, 0, 0, true, false);
}

/**
 * Returns a point describing the receiver's size in points. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget();
	return DPIUtil.autoScaleDown(getSizeInPixels());
}

Point getSizeInPixels () {
	checkWidget();
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	return new Point (width, height);
}

/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param size the new size in points for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	size = DPIUtil.autoScaleUp(size);
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true);
}

void setSizeInPixels (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true);
}

/**
 * Sets the shape of the control to the region specified
 * by the argument.  When the argument is null, the
 * default shape of the control is restored.
 *
 * @param region the region that defines the shape of the control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setRegion (Region region) {
	checkWidget ();
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	long shape_region = (region == null) ? 0 : region.handle;
	this.region = region;
	long topHandle = topHandle ();

	if (OS.G_OBJECT_TYPE(topHandle) == GTK.GTK_TYPE_WINDOW()) {
		GTK3.gtk_widget_shape_combine_region(topHandle, shape_region);
		/*
		 * Bug in GTK: on Wayland, pixels in window outside shape_region
		 * is black instead of transparent when the widget is fully opaque (i.e. opacity == 1.0)
		 * This is a hack to force the outside pixels to be transparent on Wayland so that
		 * Part-Drag on Eclipse does not cause black-out. See Bug 535083.
		 */
		if (OS.isWayland()) {
			double alpha = GTK.gtk_widget_get_opacity(topHandle);
			if (alpha == 1) alpha = 0.99;
			GTK.gtk_widget_set_opacity(topHandle, alpha);
		}
	} else {
		drawRegion = (this.region != null && this.region.handle != 0);
		if (drawRegion) {
			cairoCopyRegion(this.region);
		} else {
			cairoDisposeRegion();
		}
		GTK.gtk_widget_queue_draw(topHandle());
	}
}

void setRelations () {
	long parentHandle = parent.parentingHandle();
	long handle = 0;

	if (GTK.GTK4) {
		int count = 0;
		for (long child = GTK4.gtk_widget_get_first_child(parentHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			count++;
		}

		if (count > 1) {
			handle = GTK4.gtk_widget_get_prev_sibling(GTK4.gtk_widget_get_last_child(parentHandle));
		}
	} else {
		long list = GTK3.gtk_container_get_children (parentHandle);
		if (list == 0) return;
		int count = OS.g_list_length (list);
		if (count > 1) {
			/*
			 * the receiver is the last item in the list, so its predecessor will
			 * be the second-last item in the list
			 */
			handle = OS.g_list_nth_data (list, count - 2);
		}
		OS.g_list_free (list);
	}

	if (handle != 0) {
		Widget widget = display.getWidget (handle);
		if (widget != null && widget != this) {
			if (widget instanceof Control) {
				Control sibling = (Control)widget;
				sibling.addRelation (this);
			}
		}
	}
}

/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param width the new width in points for the receiver
 * @param height the new height in points for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget();
	Point size = DPIUtil.autoScaleUp(new Point (width, height));
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true);
}

void setSizeInPixels (int width, int height) {
	checkWidget();
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), false, true);
}


@Override
boolean isActive () {
	return getShell ().getModalShell () == null && display.getModalDialog () == null;
}

@Override
public boolean isAutoScalable () {
	return autoScale;
}


/*
 * Answers a boolean indicating whether a Label that precedes the receiver in
 * a layout should be read by screen readers as the recevier's label.
 */
boolean isDescribedByLabel () {
	return true;
}

boolean isFocusHandle (long widget) {
	return widget == focusHandle ();
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#moveBelow
 * @see Composite#getChildren
 */
public void moveAbove (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		if (this == control) return;
	}
	setZOrder (control, true, true);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#moveAbove
 * @see Composite#getChildren
 */
public void moveBelow (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		if (this == control) return;
	}
	setZOrder (control, false, true);
}

void moveChildren (int oldWidth) {
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack () {
	pack (true);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 * <p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained.
 * </p>
 *
 * @param changed whether or not the receiver's contents have changed
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack (boolean changed) {
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

/**
 * Sets the layout data associated with the receiver to the argument.
 *
 * @param layoutData the new layout data for the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
 * @param x the x coordinate in points to be translated
 * @param y the y coordinate in points to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public Point toControl(int x, int y) {
	checkWidget();
	int[] origin_x = new int[1], origin_y = new int[1];
	if (GTK.GTK4) {
		Point origin = getControlOrigin();
		origin_x[0] = origin.x;
		origin_y[0] = origin.y;
	} else {
		long window = eventWindow();
		GDK.gdk_window_get_origin(window, origin_x, origin_y);
	}

	x -= DPIUtil.autoScaleDown(origin_x[0]);
	y -= DPIUtil.autoScaleDown(origin_y[0]);
	if ((style & SWT.MIRRORED) != 0) x = DPIUtil.autoScaleDown(getClientWidth()) - x;

	return new Point(x, y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toControl (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toControl (point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public Point toDisplay(int x, int y) {
	checkWidget();
	int[] origin_x = new int[1], origin_y = new int[1];
	if (GTK.GTK4) {
		Point origin = getControlOrigin();
		origin_x[0] = origin.x;
		origin_y[0] = origin.y;
	} else {
		long window = eventWindow();
		GDK.gdk_window_get_origin(window, origin_x, origin_y);
	}

	if ((style & SWT.MIRRORED) != 0) x = DPIUtil.autoScaleDown(getClientWidth()) - x;
	x += DPIUtil.autoScaleDown(origin_x[0]);
	y += DPIUtil.autoScaleDown(origin_y[0]);

	return new Point(x, y);
}

Point toDisplayInPixels(int x, int y) {
	checkWidget();

	int[] origin_x = new int[1], origin_y = new int[1];
	if (GTK.GTK4) {
		Point origin = getControlOrigin();
		origin_x[0] = origin.x;
		origin_y[0] = origin.y;
	} else {
		long window = eventWindow();
		GDK.gdk_window_get_origin(window, origin_x, origin_y);
	}

	if ((style & SWT.MIRRORED) != 0) x = getClientWidth() - x;
	x += origin_x[0];
	y += origin_y[0];

	return new Point(x, y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toDisplay (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplay (point.x, point.y);
}

Point toDisplayInPixels (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplayInPixels (point.x, point.y);
}

/**
 * GTK4 only function to replace gdk_surface_get_origin
 * @return the origin of the Control's SWTFixed container relative to the Shell
 */
Point getControlOrigin() {
	double[] originX = new double[1], originY = new double[1];
	boolean success = GTK4.gtk_widget_translate_coordinates(fixedHandle, getShell().shellHandle, 0, 0, originX, originY);

	return success ? new Point((int)originX[0], (int)originY[0]) : new Point(0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when a drag gesture occurs, by sending it
 * one of the messages defined in the <code>DragDetectListener</code>
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
 * @see DragDetectListener
 * @see #removeDragDetectListener
 *
 * @since 3.3
 */
public void addDragDetectListener (DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.DragDetect,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control gains or loses focus, by sending
 * it one of the messages defined in the <code>FocusListener</code>
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
 * @see FocusListener
 * @see #removeFocusListener
 */
public void addFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when gesture events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>GestureListener</code> interface.
 * <p>
 * NOTE: If <code>setTouchEnabled(true)</code> has previously been
 * invoked on the receiver then <code>setTouchEnabled(false)</code>
 * must be invoked on it to specify that gesture events should be
 * sent instead of touch events.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
 * SWT doesn't send Gesture or Touch events on GTK.
 * </p>
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
 * @see GestureListener
 * @see #removeGestureListener
 * @see #setTouchEnabled
 *
 * @since 3.7
 */
public void addGestureListener (GestureListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Gesture, typedListener);
}

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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
 * interface.
 * <p>
 * When a key listener is added to a control, the control
 * will take part in widget traversal.  By default, all
 * traversal keys (such as the tab key and so on) are
 * delivered to the control.  In order for a control to take
 * part in traversal, it should listen for traversal events.
 * Otherwise, the user can traverse into a control but not
 * out.  Note that native controls such as table and tree
 * implement key traversal in the operating system.  It is
 * not necessary to add traversal listeners for these controls,
 * unless you want to override the default traversal.
 * </p>
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when mouse buttons are pressed and released, by sending
 * it one of the messages defined in the <code>MouseListener</code>
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
 * @see MouseListener
 * @see #removeMouseListener
 */
public void addMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse moves, by sending it one of the
 * messages defined in the <code>MouseMoveListener</code>
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
 * @see MouseMoveListener
 * @see #removeMouseMoveListener
 */
public void addMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse passes or hovers over controls, by sending
 * it one of the messages defined in the <code>MouseTrackListener</code>
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
 * @see MouseTrackListener
 * @see #removeMouseTrackListener
 */
public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse wheel is scrolled, by sending
 * it one of the messages defined in the
 * <code>MouseWheelListener</code> interface.
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
 * @see MouseWheelListener
 * @see #removeMouseWheelListener
 *
 * @since 3.3
 */
public void addMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseWheel, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver needs to be painted, by sending it
 * one of the messages defined in the <code>PaintListener</code>
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
 * @see PaintListener
 * @see #removePaintListener
 */
public void addPaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);

	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Paint, typedListener);
}

/**
 * Allows Controls to adjust the clipping of themselves or
 * their children.
 *
 * @param widget the handle to the widget
 */
void adjustChildClipping (long widget) {
}

void addRelation (Control control) {
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when touch events occur, by sending it
 * one of the messages defined in the <code>TouchListener</code>
 * interface.
 * <p>
 * NOTE: You must also call <code>setTouchEnabled(true)</code> to
 * specify that touch events should be sent, which will cause gesture
 * events to not be sent.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
 * SWT doesn't send Gesture or Touch events on GTK.
 * </p>
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
 * @see TouchListener
 * @see #removeTouchListener
 * @see #setTouchEnabled
 *
 * @since 3.7
 */
public void addTouchListener (TouchListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Touch,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when traversal events occur, by sending it
 * one of the messages defined in the <code>TraverseListener</code>
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
 * @see TraverseListener
 * @see #removeTraverseListener
 */
public void addTraverseListener (TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when a drag gesture occurs.
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
 * @see DragDetectListener
 * @see #addDragDetectListener
 *
 * @since 3.3
 */
public void removeDragDetectListener(DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.DragDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see FocusListener
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when gesture events are generated for the control.
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
 * @see GestureListener
 * @see #addGestureListener
 *
 * @since 3.7
 */
public void removeGestureListener (GestureListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Gesture, listener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see MouseListener
 * @see #addMouseListener
 */
public void removeMouseListener (MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse moves.
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
 * @see MouseMoveListener
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see MouseTrackListener
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse wheel is scrolled.
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
 * @see MouseWheelListener
 * @see #addMouseWheelListener
 *
 * @since 3.3
 */
public void removeMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseWheel, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/*
 * Remove "Labelled by" relation from the receiver.
 */
void removeRelation () {
	if (!isDescribedByLabel ()) return;		/* there will not be any */
	if (labelRelation != null) {
		_getAccessible().removeRelation (ACC.RELATION_LABELLED_BY, labelRelation._getAccessible());
		labelRelation = null;
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when touch events occur.
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
 * @see TouchListener
 * @see #addTouchListener
 *
 * @since 3.7
 */
public void removeTouchListener(TouchListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Touch, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 *
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 *
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DragDetectListener
 * @see #addDragDetectListener
 *
 * @see #getDragDetect
 * @see #setDragDetect
 *
 * @since 3.3
 */
public boolean dragDetect (Event event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 *
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 *
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DragDetectListener
 * @see #addDragDetectListener
 *
 * @see #getDragDetect
 * @see #setDragDetect
 *
 * @since 3.3
 */
public boolean dragDetect (MouseEvent event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

boolean dragDetect (int button, int count, int stateMask, int x, int y) {
	if (button != 1 || count != 1) return false;
	if (!dragDetect (x, y, false, true, null)) {
		return false;
	}
	return sendDragEvent (button, stateMask, x, y, true);
}

boolean dragDetect (int x, int y, boolean filter, boolean dragOnTimeout, boolean [] consume) {
	// Historically, SWT detected drag&drop on X11 this way:
	//   1) on left mouse down event, begin a modal event loop
	//      In GTK, this is `button-press-event` signal.
	//      In SWT, this is `Control#gtk_button_press_event()`
	//   2) In the loop, wait for mouse to move far enough to trigger drag&drop action
	//   3) If drag&drop is not triggered within 500ms, or mouse button released, or Esc pressed,
	//      and a few other conditions, consider it NOT drag&drop and a mere mouse click.
	//
	// However, on Wayland, it is no longer possible: as long as SWT does not
	// return from `button-press-event` signal handler, it is not possible to
	// receive ANY mouse events from `gdk_event_get()`. Not mouse movements,
	// not even mouse up event. This causes SWT to pointlessly loop for 500ms
	// after each mouse down.
	//
	// In Bug 503431, approach was changed to trigger drag&drop from MouseMove signal instead
	//   In GTK, this is `motion-notify-event` signal.
	//   In SWT, this is `Control#gtk_motion_notify_event()`
	//
	// Initially the new code was supposed to work on both X11 and Wayland, but unfortunately
	// the new implementation had its problems:
	// * Bug 503431 - `StyledText` wants to know if DND has started right after calling `dragDetect()`
	//   This required the workaround where `MouseDown` event is not reported until DND decision is made.
	// * Bug 503431 - multi-select `Tree`, `Table`, `List` drag&drop broken (only drags one item)
	//   This is because GTK itself doesn't support multi-item DND.
	// * Bug 503431 - various mistakes
	//   For example, there was a `Callback` leak. It was fixed soon after.
	// * Issue 400  - wrong mouse events reporting
	//   This is what I'm fixing now.
	//
	// As a result, in Bug 510446, the new implementation was limited to Wayland only, so that more
	// common back then X11 could continue to work without bugs.
	//
	// I hope that I fixed the last of the problems on Wayland now (2023-03-11),
	// and X11 code could finally be thrown away. But I don't think I dare to try it right now.

	if (OS.isWayland()) {
		// Don't drag if mouse is not down. This condition is not as
		// trivial as it seems, see Bug 541635 where drag is tested
		// after drag already completed and mouse is released.
		if (!mouseDown) {
			return false;
		}

		double [] offsetX = new double[1];
		double [] offsetY = new double [1];
		double [] startX = new double[1];
		double [] startY = new double [1];
		if (!GTK.gtk_gesture_drag_get_start_point(dragGesture, startX, startY)) {
			return false;
		}

		GTK.gtk_gesture_drag_get_offset(dragGesture, offsetX, offsetY);
		if (GTK3.gtk_drag_check_threshold(handle, (int)startX[0], (int) startY[0], (int) startX[0]
				+ (int) offsetX[0], (int) startY[0] + (int) offsetY[0])) {
			return true;
		}

		return false;
	} else {
		boolean dragging = false;
		boolean quit = false;
		//428852 DND workaround for GTK3.
		//Gtk3 no longer sends motion events on the same control during thread sleep
		//before a drag started. This is due to underlying gdk changes.
		//Thus for gtk3 we check mouse coords manually
		//Note, input params x/y are relative, the two points below are absolute coords.
		Point startPos = null;
		Point currPos = null;
		startPos = display.getCursorLocationInPixels();

		while (!quit) {
			long eventPtr = 0;
			/*
			* There should be an event on the queue already, but
			* in cases where there isn't one, stop trying after
			* half a second.
			*/
			long timeout = System.currentTimeMillis() + 500;
			display.sendPreExternalEventDispatchEvent();
			while (System.currentTimeMillis() < timeout) {
				eventPtr = GTK.GTK4 ? GTK3.gtk_get_current_event() : GDK.gdk_event_get ();
				if (eventPtr != 0) {
					break;
				} else {
					currPos = display.getCursorLocationInPixels();
					dragging = GTK3.gtk_drag_check_threshold (handle,
								startPos.x, startPos.y, currPos.x, currPos.y);
					if (dragging) break;
				}
			}
			display.sendPostExternalEventDispatchEvent();
			if (dragging) return true;  //428852
			if (eventPtr == 0) return dragOnTimeout;
			int eventType = GDK.gdk_event_get_event_type(eventPtr);
			eventType = fixGdkEventTypeValues(eventType);
			switch (eventType) {
				case GDK.GDK_MOTION_NOTIFY: {
					long gdkResource = gdk_event_get_surface_or_window(eventPtr);

					int [] state = new int[1];
					double [] eventX = new double[1];
					double [] eventY = new double[1];
					if (GTK.GTK4) {
						state[0] = GDK.gdk_event_get_modifier_state(eventPtr);
						GDK.gdk_event_get_position(eventPtr, eventX, eventY);
					} else {
						GDK.gdk_event_get_state(eventPtr, state);
						GDK.gdk_event_get_coords(eventPtr, eventX, eventY);
					}

					if ((state[0] & GDK.GDK_BUTTON1_MASK) != 0) {
						if (GTK3.gtk_drag_check_threshold (handle, x, y, (int) eventX[0], (int) eventY[0])) {
							dragging = true;
							quit = true;
						}
					} else {
						quit = true;
					}
					int [] newX = new int [1], newY = new int [1];
					if (GTK.GTK4) {
						double[] newXDouble = new double[1], newYDouble = new double[1];
						display.getPointerPosition(newXDouble, newYDouble);

						newX[0] = (int)newXDouble[0];
						newY[0] = (int)newYDouble[0];
					} else {
						display.getWindowPointerPosition(gdkResource, newX, newY, null);
					}
					break;
				}
				case GDK.GDK_KEY_PRESS:
				case GDK.GDK_KEY_RELEASE: {
					int [] eventKeyval = new int [1];
					if (GTK.GTK4) {
						eventKeyval[0] = GDK.gdk_key_event_get_keyval(eventPtr);
					} else {
						GDK.gdk_event_get_keyval(eventPtr, eventKeyval);
					}

					if (eventKeyval[0] == GDK.GDK_Escape) quit = true;
					break;
				}
				case GDK.GDK_BUTTON_RELEASE:
				case GDK.GDK_BUTTON_PRESS:
				case GDK.GDK_2BUTTON_PRESS:
				case GDK.GDK_3BUTTON_PRESS: {
					if (GTK.GTK4) {
						long display = GDK.gdk_display_get_default();
						GDK.gdk_display_put_event(display, eventPtr);
					} else {
						GDK.gdk_event_put (eventPtr);
					}
					quit = true;
					break;
				}
				default:
					GTK3.gtk_main_do_event (eventPtr);
			}
			gdk_event_free (eventPtr);
		}
		return dragging;
	}
}

boolean filterKey (long event) {
	long imHandle = imHandle ();
	if (imHandle != 0) {
		if (GTK.GTK4)
			return GTK4.gtk_im_context_filter_keypress (imHandle, event);
		else
			return GTK3.gtk_im_context_filter_keypress (imHandle, event);
	}
	return false;
}

Control findBackgroundControl () {
	if (((state & BACKGROUND) != 0 || backgroundImage != null) && backgroundAlpha > 0) return this;
	return (parent != null && (state & PARENT_BACKGROUND) != 0) ? parent.findBackgroundControl () : null;
}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

/**
 * In some situations, a control has a non-standard parent GdkWindow (Note gDk, not gTk).
 * E.g, an TreeEditor who's parent is a Tree should have the Tree Viewer's inner bin as parent window.
 *
 * Note, composites should treat this differently and take child controls into consideration.
 */
void fixParentGdkResource() {
	// Changes to this method should be verified via
	// org.eclipse.swt.tests.gtk/*/Bug510803_TabFolder_TreeEditor_Regression.java (part one)
	parent.setParentGdkResource(this);
}

/**
 * Native gtkwidget re-parenting in SWT on Gtk3 needs to be handled in a special way because
 * some controls have non-standard GdkWindow as parents. (E.g ControlEditors), and other controls
 * like TabItem and ExpandBar use reparenting to preserve proper hierarchy for correct event traversal (like dnd).
 *
 * Note, GdkWindows != GtkWindows.
 *
 * You should never call gtk_widget_reparent() directly or reparent widgets outside this method,
 * otherwise you can break TabItem/TreeEditors.
 *
 * @param control that should be reparented.
 * @param newParentHandle pointer/handle to the new GtkWidget parent.
 */
static void gtk_widget_reparent (Control control, long newParentHandle) {
	// Changes to this method should be verified via both parts in:
	// org.eclipse.swt.tests.gtk/*/Bug510803_TabFolder_TreeEditor_Regression.java
	long widget = control.topHandle();
	long parentContainer = GTK.gtk_widget_get_parent (widget);
	assert parentContainer != 0 : "Improper use of Control.gtk_widget_reparent. Widget currently has no parent.";
	if (parentContainer != 0) {

		if (GTK.GTK4) {
			OS.g_object_ref(widget);
			OS.swt_fixed_remove(parentContainer, widget);
			OS.swt_fixed_add(newParentHandle, widget);
			OS.g_object_unref(widget);
		} else {
			// gtk_widget_reparent (..) is deprecated as of Gtk 3.14 and removed in Gtk4.
			// However, the current alternative of removing/adding widget from/to a container causes errors. (see note below).
			// TODO - research a better way to reparent. See 534089.
			GTK3.gtk_widget_reparent(widget, newParentHandle);

			// Removing/Adding containers doesn't seem to reparent sub-gdkWindows properly and throws errors.
			// Steps to reproduce:
			//  - From bug 534089, download the first attachment plugin: "Plug-in to reproduce the problem with"
			//  - Import it into your eclipse. Launch a child eclipse with this plugin. Ensure child workspace is cleaned upon launch so that you see welcome screen.
			//  - Upon closing the welcome screen, you will see an eclipse error message: "org.eclipse.swt.SWTError: No more handles"
			//  - The following is printed into the console: 'gdk_window_new(): parent is destroyed'
			// After some research, I found that gtk_widget_repartent(..) also reparents sub-windows, but moving widget between containers doesn't do this,
			// This seems to leave some gdkWindows with incorrect parents.
//				OS.g_object_ref (widget);
//				GTK.gtk_container_remove (parentContainer, widget);
//				GTK.gtk_container_add (newParentHandle, widget);
//				OS.g_object_unref (widget);

			control.fixParentGdkResource();
		}
	}
}

void fixModal(long group, long modalGroup) {
}

/**
 * Forces the receiver to have the <em>keyboard focus</em>, causing
 * all keyboard events to be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setFocus
 */
public boolean forceFocus () {
	checkWidget();
	if (display.focusEvent == SWT.FocusOut) return false;
	Shell shell = getShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible ()) return false;
	if (display.getActiveShell() != shell && !Display.isActivateShellOnForceFocus()) return false;
	shell.bringToTop (false);
	return forceFocus (focusHandle ());
}

boolean forceFocus (long focusHandle) {
	if (GTK.gtk_widget_has_focus (focusHandle)) return true;
	/* When the control is zero sized it must be realized */
	GTK.gtk_widget_realize (focusHandle);
	GTK.gtk_widget_grab_focus (focusHandle);
	// widget could be disposed at this point
	if (isDisposed ()) return false;
	Shell shell = getShell ();
	long shellHandle = shell.shellHandle;
	long handle = GTK.gtk_window_get_focus (shellHandle);
	while (handle != 0) {
		if (handle == focusHandle) {
			/* Cancel any previous ignoreFocus requests */
			display.ignoreFocus = false;
			return true;
		}
		Widget widget = display.getWidget (handle);
		if (widget != null && widget instanceof Control) {
			return widget == this;
		}
		handle = GTK.gtk_widget_get_parent (handle);
	}
	return false;
}

/**
 * Returns the receiver's background color.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on some versions of Windows the background of a TabFolder,
 * is a gradient rather than a solid color.
 * </p>
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget();
	Color color;
	if (backgroundAlpha == 0) {
		color = Color.gtk_new (display, this.getBackgroundGdkRGBA (), 0);
		return color;
	} else {
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		return Color.gtk_new (display, control.getBackgroundGdkRGBA (), backgroundAlpha);
	}
}

GdkRGBA getBackgroundGdkRGBA () {
	return getBgGdkRGBA ();
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public Image getBackgroundImage () {
	checkWidget ();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.backgroundImage;
}

GdkRGBA getContextBackgroundGdkRGBA () {
	if ((state & BACKGROUND) == 0) {
		return defaultBackground();
	}
	if (provider != 0) {
		return display.gtk_css_parse_background (display.gtk_css_provider_to_string(provider), null);
	} else {
		return defaultBackground();
	}
}

GdkRGBA getContextColorGdkRGBA () {
	return display.gtk_css_parse_foreground(display.gtk_css_provider_to_string(provider), null);
}

GdkRGBA getBgGdkRGBA () {
	return getContextBackgroundGdkRGBA ();
}

GdkRGBA getBaseGdkRGBA () {
	return getContextBackgroundGdkRGBA ();
}

/**
 * Returns the receiver's border width in points.
 *
 * @return the border width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getBorderWidth () {
	return DPIUtil.autoScaleDown(getBorderWidthInPixels());
}

int getBorderWidthInPixels () {
	checkWidget();
	return 0;
}

int getClientWidth () {
	if (handle == 0 || (state & ZERO_WIDTH) != 0) return 0;
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation (handle, allocation);
	return allocation.width;
}

/**
 * Returns the receiver's cursor, or null if it has not been set.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @return the receiver's cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public Cursor getCursor () {
	checkWidget ();
	return cursor;
}

/**
 * Returns <code>true</code> if the receiver is detecting
 * drag gestures, and  <code>false</code> otherwise.
 *
 * @return the receiver's drag detect state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public boolean getDragDetect () {
	checkWidget ();
	return (state & DRAG_DETECT) != 0;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
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
	return (state & DISABLED) == 0;
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
 */
public Font getFont () {
	checkWidget();
	return font != null ? font : defaultFont ();
}

/**
 * @return A newly allocated <code>PangoFontDescription*</code>, caller
 *         must free it with {@link OS#pango_font_description_free}.
 */
long getFontDescription () {
	long fontHandle = fontHandle ();
	long [] fontDesc = new long [1];
	long context = GTK.gtk_widget_get_style_context (fontHandle);
	if ("ppc64le".equals(System.getProperty("os.arch"))) {
		// Unlike 'gtk_style_context_get()', 'gtk_style_context_get_font()'
		// returns pointer owned by GTK. The workaround is to make a copy of the data.
		long gtkOwnedPointer = GTK3.gtk_style_context_get_font(context, GTK.GTK_STATE_FLAG_NORMAL);
		return OS.pango_font_description_copy(gtkOwnedPointer);
	} else {
		if (GTK.GTK4) {
			long[] fontPtr = new long[1];
			long settings = GTK.gtk_settings_get_default();
			OS.g_object_get(settings, GTK.gtk_style_property_font, fontPtr, 0);
			if (fontPtr[0] != 0) {
				int length = C.strlen(fontPtr[0]);
				if (length != 0) {
					byte[] fontString = new byte[length + 1];
					C.memmove(fontString, fontPtr[0], length);
					OS.g_free(fontPtr[0]);
					return OS.pango_font_description_from_string(fontString);
				}
			}

			return 0;
		} else {
			GTK.gtk_style_context_save(context);
			GTK.gtk_style_context_set_state(context, GTK.GTK_STATE_FLAG_NORMAL);
			GTK3.gtk_style_context_get(context, GTK.GTK_STATE_FLAG_NORMAL, GTK.gtk_style_property_font, fontDesc, 0);
			GTK.gtk_style_context_restore(context);
			return fontDesc [0];
		}
	}
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getForeground () {
	checkWidget();
	Color color;
	color = Color.gtk_new (display, getForegroundGdkRGBA ());
	return color;
}

GdkRGBA getForegroundGdkRGBA () {
	return getContextColorGdkRGBA();
}

Point getIMCaretPos () {
	return new Point (0, 0);
}

/**
 * Returns layout data which is associated with the receiver.
 *
 * @return the receiver's layout data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Object getLayoutData () {
	checkWidget();
	return layoutData;
}

/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not. All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget();
	return menu;
}

/**
 * Returns the receiver's monitor.
 *
 * @return the receiver's monitor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget ();
	Monitor[] monitors = display.getMonitors ();
	long displayHandle = GDK.gdk_display_get_default ();
	if (displayHandle != 0) {
		long monitor;
		if (GTK.GTK4) {
			monitor = GDK.gdk_display_get_monitor_at_surface(displayHandle, paintSurface ());
		} else {
			monitor = GDK.gdk_display_get_monitor_at_window(displayHandle, paintWindow ());
		}
		long toCompare;
		for (int i = 0; i < monitors.length; i++) {
			toCompare = GDK.gdk_display_get_monitor(displayHandle, i);
			if (toCompare == monitor) {
				return monitors[i];
			}
		}
	}
	return display.getPrimaryMonitor ();
}

/**
 * Returns the receiver's parent, which must be a <code>Composite</code>
 * or null when the receiver is a shell that was created with null or
 * a display for a parent.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Composite getParent () {
	checkWidget();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

/**
 * Returns the region that defines the shape of the control,
 * or null if the control has the default shape.
 *
 * @return the region that defines the shape of the shell (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public Region getRegion () {
	checkWidget ();
	return region;
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
public Shell getShell() {
	checkWidget();
	return _getShell();
}

Shell _getShell() {
	return parent._getShell();
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
	return toolTipText;
}

/**
 * Returns <code>true</code> if this control is set to send touch events, or
 * <code>false</code> if it is set to send gesture events instead.  This method
 * also returns <code>false</code> if a touch-based input device is not detected
 * (this can be determined with <code>Display#getTouchEnabled()</code>).  Use
 * {@link #setTouchEnabled(boolean)} to switch the events that a control sends
 * between touch events and gesture events.
 *
 * @return <code>true</code> if the control is set to send touch events, or <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTouchEnabled
 * @see Display#getTouchEnabled
 *
 * @since 3.7
 */
public boolean getTouchEnabled() {
	checkWidget();
	return false;
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
	checkWidget();
	return (state & HIDDEN) == 0;
}

Point getThickness (long widget) {
	int xthickness = 0, ythickness = 0;
	GtkBorder tmp = new GtkBorder();
	long context = GTK.gtk_widget_get_style_context (widget);
	int state_flag = GTK.gtk_widget_get_state_flags(widget);
	gtk_style_context_get_padding(context, state_flag, tmp);
	GTK.gtk_style_context_save (context);
	GTK.gtk_style_context_add_class (context, GTK.GTK_STYLE_CLASS_FRAME);
	xthickness += tmp.left;
	ythickness += tmp.top;
	int state = GTK.gtk_widget_get_state_flags(widget);
	gtk_style_context_get_border (context, state, tmp);
	xthickness += tmp.left;
	ythickness += tmp.top;
	GTK.gtk_style_context_restore (context);
	return new Point (xthickness, ythickness);
}

void gtk_style_context_get_padding(long context, int state, GtkBorder padding) {
	if (GTK.GTK4) {
		GTK4.gtk_style_context_get_padding(context, padding);
	} else {
		GTK3.gtk_style_context_get_padding(context, state, padding);
	}
}

void gtk_style_context_get_border (long context, int state, GtkBorder padding) {
	if (GTK.GTK4) {
		GTK4.gtk_style_context_get_border(context, padding);
	} else {
		GTK3.gtk_style_context_get_border(context, state, padding);
	}
}

/**
 * Handling multi-press event on GTK4
 */
@Override
void gtk_gesture_press_event (long gesture, int n_press, double x, double y, long event) {
	mouseDown = true;

	int eventButton = GDK.gdk_button_event_get_button(event);
	int eventTime = GDK.gdk_event_get_time(event);
	int eventState = GDK.gdk_event_get_modifier_state(event);

	display.clickCount = n_press;
	if (n_press == 1) {
		sendMouseEvent(SWT.MouseDown, eventButton, n_press, 0, false, eventTime, x, y, false, eventState);
		if ((state & MENU) == 0) {
			if (eventButton == 3) {
				showMenu ((int)x, (int)y);
			}
		}
	} else if (n_press == 2) {
		sendMouseEvent(SWT.MouseDoubleClick, eventButton, n_press, 0, false, eventTime, x, y, false, eventState);
	}
}

@Override
void gtk_gesture_release_event (long gesture, int n_press, double x, double y, long event) {
	mouseDown = false;

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_position(event, eventX, eventY);

	int eventButton = GDK.gdk_button_event_get_button(event);
	int eventTime = GDK.gdk_event_get_time(event);
	int eventState = GDK.gdk_event_get_modifier_state(event);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return;
	sendMouseEvent(SWT.MouseUp, eventButton, display.clickCount, 0, false, eventTime, 0, 0, false, eventState);
}

@Override
long gtk_button_press_event (long widget, long event) {
	return gtk_button_press_event (widget, event, true);
}

boolean wantDragDropDetection () {
	// Drag&drop detection has its costs: when mouse button is pressed,
	// mouse events are not reported until SWT can decide if drag&drop
	// was triggered or not. For some applications, this could be a problem.
	// Consider for example a graphical drawing application: it would expect
	// to begin drawing as soon as mouse button is pressed.
	// If app is interested in drag&drop, it will likely listen to `DragDetect`.
	return hooks (SWT.DragDetect);
}

long gtk_button_press_event (long widget, long event, boolean sendMouseDown) {
	mouseDown = true;

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int eventType = GDK.gdk_event_get_event_type(event);

	int [] eventButton = new int [1];
	int [] eventState = new int[1];
	GDK.gdk_event_get_button(event, eventButton);
	GDK.gdk_event_get_state(event, eventState);

	int eventTime = GDK.gdk_event_get_time(event);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;
	if (eventType == GDK.GDK_3BUTTON_PRESS) return 0;

	/*
	* When a shell is created with SWT.ON_TOP and SWT.NO_FOCUS,
	* do not activate the shell when the user clicks on the
	* the client area or on the border or a control within the
	* shell that does not take focus.
	*/
	Shell shell = _getShell ();
	if (((shell.style & SWT.ON_TOP) != 0) && (((shell.style & SWT.NO_FOCUS) == 0) || ((style & SWT.NO_FOCUS) == 0))) {
		shell.forceActive();
	}
	long result = 0;
	if (eventType == GDK.GDK_BUTTON_PRESS) {
		boolean dragging = false;
		display.clickCount = 1;
		long nextEvent = GDK.gdk_event_peek();
		if (nextEvent != 0) {
			int peekedEventType = GDK.GDK_EVENT_TYPE (nextEvent);
			if (peekedEventType == GDK.GDK_2BUTTON_PRESS) display.clickCount = 2;
			if (peekedEventType == GDK.GDK_3BUTTON_PRESS) display.clickCount = 3;
			gdk_event_free (nextEvent);
		}

		// See comment in #dragDetect()
		if (OS.isX11()) {
			if ((state & DRAG_DETECT) != 0 && wantDragDropDetection ()) {
				if (eventButton[0] == 1) {
					boolean [] consume = new boolean [1];
					if (dragDetect ((int) eventX[0], (int) eventY[0], true, true, consume)) {
						dragging = true;
						if (consume [0]) result = 1;
					}
					if (isDisposed ()) return 1;
				}
			}
		}
		if (sendMouseDown) {
			boolean mouseEventSent = !sendMouseEvent(SWT.MouseDown, eventButton[0], display.clickCount, 0, false, eventTime, eventRX[0], eventRY[0], false, eventState[0]);

			if (mouseEventSent) {
				result = 1;
			}
		}
		if (isDisposed ()) return 1;

		// See comment in #dragDetect()
		if (OS.isX11()) {
			if (dragging) {
				Point scaledEvent = DPIUtil.autoScaleDown(new Point((int)eventX[0], (int) eventY[0]));
				sendDragEvent (eventButton[0], eventState[0], scaledEvent.x, scaledEvent.y, false);
				if (isDisposed ()) return 1;
			}
		}
		/*
		* Pop up the context menu in the button press event for widgets
		* that have default operating system menus in order to stop the
		* operating system from displaying the menu if necessary.
		*/
		if ((state & MENU) != 0) {
			if (eventButton[0] == 3) {
				if (showMenu ((int)eventRX[0], (int)eventRY[0])) {
					result = 1;
				}
			}
		}
	} else {
		display.clickCount = 2;
		result = sendMouseEvent (SWT.MouseDoubleClick, eventButton[0], display.clickCount, 0, false, eventTime, eventRX[0],eventRY[0], false, eventState[0]) ? 0 : 1;
		if (isDisposed ()) return 1;
	}
	if (!shell.isDisposed ()) shell.setActiveControl (this, SWT.MouseDown);
	return result;
}

@Override
long gtk_button_release_event (long widget, long event) {
	mouseDown = false;

	double[] eventX = new double[1];
	double[] eventY = new double[1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int[] eventButton = new int[1];
	int[] eventState = new int[1];
	GDK.gdk_event_get_button(event, eventButton);
	GDK.gdk_event_get_state(event, eventState);

	int eventTime = GDK.gdk_event_get_time(event);

	double[] eventRX = new double[1];
	double[] eventRY = new double[1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;
	return sendMouseEvent(SWT.MouseUp, eventButton[0], display.clickCount, 0, false, eventTime, eventRX[0], eventRY[0], false, eventState[0]) ? 0 : 1;
}

@Override
long gtk_commit (long imcontext, long text) {
	if (text == 0) return 0;
	int length = C.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	C.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (buffer);
	sendIMKeyEvent (SWT.KeyDown, 0, chars);
	return 0;
}



@Override
void gtk4_enter_event(long controller, double x, double y, long event) {
	/*
	 * Set tool tip for this shell, and also null tooltip for shell
	 * if control being entered does not have a tooltip text set.
	 */
	byte [] buffer = null;
	if (toolTipText != null && toolTipText.length() != 0) {
		char [] chars = fixMnemonic (toolTipText, false, true);
		buffer = Converter.wcsToMbcs (chars, true);
	}
	long toolHandle = getShell().handle;
	GTK.gtk_widget_set_tooltip_text (toolHandle, buffer);

	if (display.currentControl == this) return;

	// Disconnect previous current Control and send MouseExit event to it
	if (display.currentControl != null && !display.currentControl.isDisposed()) {
		display.removeMouseHoverTimeout(display.currentControl.handle);
		display.currentControl.sendMouseEvent(SWT.MouseExit, 0, 0, x, y, false, 0);
	}

	// Set display's current control and send MouseEnter event
	if (!isDisposed()) {
		display.currentControl = this;
		sendMouseEvent(SWT.MouseEnter, 0, 0, x, y, false, 0);
	}
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	/*
	 * Feature in GTK. Children of a shell will inherit and display the shell's
	 * tooltip if they do not have a tooltip of their own. The fix is to use the
	 * new tooltip API in GTK 2.12 to null the shell's tooltip when the control
	 * being entered does not have any tooltip text set.
	 */
	byte [] buffer = null;
	if (toolTipText != null && toolTipText.length() != 0) {
		char [] chars = fixMnemonic (toolTipText, false, true);
		buffer = Converter.wcsToMbcs (chars, true);
	}
	long toolHandle = getShell().handle;
	GTK.gtk_widget_set_tooltip_text (toolHandle, buffer);

	if (display.currentControl == this) return 0;
	int [] state = new int [1];
	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_state(event, state);
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int time = GDK.gdk_event_get_time(event);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;

	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	long childGdkResource = 0;
	int [] crossingMode = new int[1];
	GTK3.memmove(gdkEvent, event, GdkEventCrossing.sizeof);
	crossingMode[0] = gdkEvent.mode;
	childGdkResource = gdkEvent.subwindow;

	/*
	 * It is possible to send out too many enter/exit events if entering a
	 * control through a subwindow. The fix is to return without sending any
	 * events if the GdkEventCrossing subwindow field is set and the control
	 * requests to check the field.
	 */
	if (childGdkResource != 0 && checkSubwindow ()) return 0;
	if (crossingMode [0] != GDK.GDK_CROSSING_NORMAL && crossingMode[0] != GDK.GDK_CROSSING_UNGRAB) return 0;
	if ((state[0] & (GDK.GDK_BUTTON1_MASK | GDK.GDK_BUTTON2_MASK | GDK.GDK_BUTTON3_MASK)) != 0) return 0;
	if (display.currentControl != null && !display.currentControl.isDisposed ()) {
		display.removeMouseHoverTimeout (display.currentControl.handle);
		display.currentControl.sendMouseEvent (SWT.MouseExit,  0, time, eventRX[0], eventRY[0], false, state[0]);
	}
	if (!isDisposed ()) {
		display.currentControl = this;
		return sendMouseEvent (SWT.MouseEnter, 0, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
	}
	return 0;
}

boolean checkSubwindow () {
	return false;
}

@Override
long gtk_event_after (long widget, long gdkEvent) {
	int eventType = GDK.gdk_event_get_event_type(gdkEvent);
	eventType = fixGdkEventTypeValues(eventType);
	switch (eventType) {
		case GDK.GDK_BUTTON_PRESS: {
			if (widget != eventHandle ()) break;
			/*
			* Pop up the context menu in the event_after signal to allow
			* the widget to process the button press.  This allows widgets
			* such as GtkTreeView to select items before a menu is shown.
			*/
			if ((state & MENU) == 0) {
				double [] eventRX = new double [1];
				double [] eventRY = new double [1];
				GDK.gdk_event_get_root_coords(gdkEvent, eventRX, eventRY);

				int [] eventButton = new int [1];
				GDK.gdk_event_get_button(gdkEvent, eventButton);

				if (eventButton[0] == 3) {
					showMenu ((int) eventRX[0], (int) eventRY[0]);
				}
			}
			break;
		}
		case GDK.GDK_FOCUS_CHANGE: {
			if (!isFocusHandle (widget)) break;
			boolean [] focusIn = new boolean [1];
			GdkEventFocus gdkEventFocus = new GdkEventFocus ();
			GTK3.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
			focusIn[0] = gdkEventFocus.in != 0;

			/*
			 * Feature in GTK. The GTK combo box popup under some window managers
			 * is implemented as a GTK_MENU.  When it pops up, it causes the combo
			 * box to lose focus when focus is received for the menu.  The
			 * fix is to check the current grab handle and see if it is a GTK_MENU
			 * and ignore the focus event when the menu is both shown and hidden.
			 *
			 * NOTE: This code runs for all menus.
			 */
			Display display = this.display;
			if (focusIn[0]) {
				if (display.ignoreFocus) {
					display.ignoreFocus = false;
					break;
				}
			} else {
				display.ignoreFocus = false;
				long grabHandle = GTK3.gtk_grab_get_current ();
				if (grabHandle != 0) {
					if (OS.G_OBJECT_TYPE (grabHandle) == GTK3.GTK_TYPE_MENU ()) {
						display.ignoreFocus = true;
						break;
					}
				}
			}
			sendFocusEvent (focusIn[0] ? SWT.FocusIn : SWT.FocusOut);
			break;
		}
	}
	return 0;
}

/**
 * Copies the region at the Cairo level, as we need to re-use these resources
 * after the Region object is disposed.
 *
 * @param region the Region object to copy to this Control
 */
void cairoCopyRegion (Region region) {
	if (region == null || region.isDisposed() || region.handle == 0) return;
	regionHandle = Cairo.cairo_region_copy(region.handle);
	return;
}

void cairoDisposeRegion () {
	if (regionHandle != 0) Cairo.cairo_region_destroy(regionHandle);
	if (eventRegion != 0) Cairo.cairo_region_destroy(eventRegion);
	regionHandle = 0;
	eventRegion = 0;
}
/**
 * Convenience method that applies a region to the Control using cairo_clip.
 *
 * @param cairo the cairo context to apply the region to
 */
void cairoClipRegion (long cairo) {
	GdkRectangle rect = new GdkRectangle ();
	GDK.gdk_cairo_get_clip_rectangle (cairo, rect);
	long regionHandle = this.regionHandle;
	// Disposal check just in case
	if (regionHandle == 0) {
		drawRegion = false;
		return;
	}
	/*
	 * These gdk_region_* functions actually map to the proper
	 * cairo_* functions in os.h.
	 */
	cairo_rectangle_int_t cairoRect = new cairo_rectangle_int_t();
	cairoRect.convertFromGdkRectangle(rect);
	long actualRegion = Cairo.cairo_region_create_rectangle(cairoRect);
	Cairo.cairo_region_subtract(actualRegion, regionHandle);
	// Draw the Shell bg using cairo, only if it's a different color
	Shell shell = getShell();
	Color shellBg = shell.getBackground();
	if (shellBg != this.getBackground()) {
		GdkRGBA rgba = shellBg.handle;
		Cairo.cairo_set_source_rgba (cairo, rgba.red, rgba.green, rgba.blue, rgba.alpha);
	} else {
		Cairo.cairo_set_source_rgba (cairo, 0.0, 0.0, 0.0, 0.0);
	}
	GDK.gdk_cairo_region(cairo, actualRegion);
	Cairo.cairo_clip(cairo);
	Cairo.cairo_paint(cairo);
	eventRegion = actualRegion;
}



@Override
void gtk4_draw(long widget, long cairo, Rectangle bounds) {
	if (!hooksPaint()) return;

	GCData data = new GCData();
	data.cairo = cairo;
	GC gc = GC.gtk_new(this, data);

	Event event = new Event();
	event.count = 1;
	event.gc = gc;
	event.setBounds(bounds);

	drawWidget(gc);
	sendEvent(SWT.Paint, event);
	gc.dispose();
	event.gc = null;
}

@Override
long gtk_draw (long widget, long cairo) {
	if (checkScaleFactor) {
		long surface = Cairo.cairo_get_target(cairo);
		if (surface != 0) {
			double [] sx = new double [1];
			double [] sy = new double [1];
			Cairo.cairo_surface_get_device_scale(surface, sx, sy);
			long display = GDK.gdk_display_get_default();
			long monitor = GDK.gdk_display_get_monitor_at_point(display, 0, 0);
			int scale = GDK.gdk_monitor_get_scale_factor(monitor);
			autoScale = !(scale == Math.round(sx[0]));
			checkScaleFactor = false;
		}
	}
	if ((state & OBSCURED) != 0) return 0;
	GdkRectangle rect = new GdkRectangle ();
	GDK.gdk_cairo_get_clip_rectangle (cairo, rect);
	/*
	 * Modify the drawing of the widget with cairo_clip.
	 * Doesn't modify input handling at this time.
	 * See bug 529431.
	 */
	if (drawRegion) {
		cairoClipRegion(cairo);
	}
	if (!hooksPaint ()) return 0;
	Event event = new Event ();
	event.count = 1;
	Rectangle eventBounds = DPIUtil.autoScaleDown (new Rectangle (rect.x, rect.y, rect.width, rect.height));
	if ((style & SWT.MIRRORED) != 0) eventBounds.x = DPIUtil.autoScaleDown (getClientWidth ()) - eventBounds.width - eventBounds.x;
	event.setBounds (eventBounds);
	GCData data = new GCData ();
	/*
	 * Pass the region into the GCData so that GC.fill* methods can be aware of the region
	 * and clip themselves accordingly. Only relevant on GTK3.10+, see bug 475784.
	 */
	if (drawRegion) data.regionSet = eventRegion;
//	data.damageRgn = gdkEvent.region;
	data.cairo = cairo;
	GC gc = event.gc = GC.gtk_new (this, data);
	// Note: use GC#setClipping(x,y,width,height) because GC#setClipping(Rectangle) got broken by bug 446075
	gc.setClipping (eventBounds.x, eventBounds.y, eventBounds.width, eventBounds.height);
	drawWidget (gc);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 0;
}

@Override
long gtk_focus (long widget, long directionType) {
	/* Stop GTK traversal for every widget */
	return 1;
}

@Override
long gtk_focus_in_event (long widget, long event) {
	// widget could be disposed at this point
	if (handle != 0) {
		Control oldControl = display.imControl;
		if (oldControl != this)  {
			if (oldControl != null && !oldControl.isDisposed ()) {
				long oldIMHandle = oldControl.imHandle ();
				if (oldIMHandle != 0) GTK.gtk_im_context_reset (oldIMHandle);
			}
		}
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			long imHandle = imHandle ();
			if (imHandle != 0) GTK.gtk_im_context_focus_in (imHandle);
		}
	}
	return 0;
}

@Override
void gtk4_focus_enter_event(long controller, long event) {
	super.gtk4_focus_enter_event(controller, event);

	sendFocusEvent(SWT.FocusIn);
}

@Override
void gtk4_focus_window_event(long handle, long event) {
	super.gtk4_focus_window_event(handle, event);

	if(firstFixedHandle == 0) {
		long child = handle;
		//3rd child of shell will be SWTFixed
		for(int i = 0; i<3; i++) {
			child = GTK4.gtk_widget_get_first_child(child);
		}
		firstFixedHandle = child != 0 ? child:0;
	}

	if(firstFixedHandle !=0 && GTK.gtk_widget_has_focus(firstFixedHandle)) {
		if(event == SWT.FocusIn)sendFocusEvent(SWT.FocusIn);
		else sendFocusEvent(SWT.FocusOut);
	}
}

@Override
long gtk_focus_out_event (long widget, long event) {
	// widget could be disposed at this point
	if (handle != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			long imHandle = imHandle ();
			if (imHandle != 0) {
				GTK.gtk_im_context_focus_out (imHandle);
			}
		}
	}
	return 0;
}

@Override
void gtk4_focus_leave_event(long controller, long event) {
	super.gtk4_focus_leave_event(controller, event);

	sendFocusEvent(SWT.FocusOut);
}

@Override
boolean gtk4_key_press_event(long controller, int keyval, int keycode, int state, long event) {
	if (!hasFocus()) return false;

	if (translateMnemonic(keyval, event)) return true;
	if (isDisposed()) return false;

	if (filterKey(event)) return true;
	if (isDisposed()) return false;

	if (translateTraversal(event)) return true;
	if (isDisposed()) return false;

	return super.gtk4_key_press_event(controller, keyval, keycode, state, event);
}

@Override
long gtk_key_press_event (long widget, long event) {
	int [] eventKeyval = new int [1];
	GDK.gdk_event_get_keyval(event, eventKeyval);

	if (!hasFocus ()) {
		/*
		* Feature in GTK.  On AIX, the IME window deactivates the current shell and even
		* though the widget receiving the key event is not in focus, it should filter the input in
		* order to get it committed.  The fix is to detect that the widget shell is not active
		* and call filterKey() only.
		*/
		if (display.getActiveShell () == null) {
			if (filterKey (event)) return 1;
		}
		return 0;
	}
	if (translateMnemonic (eventKeyval[0], event)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;

	if (filterKey (event)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;

	if (translateTraversal (event)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	return super.gtk_key_press_event (widget, event);
}

@Override
void gtk4_key_release_event(long controller, int keyval, int keycode, int state, long event) {
	if (!hasFocus()) return;

	long imContext = imHandle();
	if (imContext != 0) {
		GTK4.gtk_im_context_filter_keypress(imContext, event);
	}

	super.gtk4_key_release_event(controller, keyval, keycode, state, event);
}

@Override
long gtk_key_release_event (long widget, long event) {
	if (!hasFocus ()) return 0;
	long imHandle = imHandle ();
	if (imHandle != 0) {
		if (GTK3.gtk_im_context_filter_keypress(imHandle, event)) return 1;
	}
	return super.gtk_key_release_event (widget, event);
}

@Override
void gtk4_leave_event(long controller, long event) {
	if (display.currentControl != this) return;

	display.removeMouseHoverTimeout(handle);

	if (sendLeaveNotify() || display.getCursorControl() == null) {
		sendMouseEvent(SWT.MouseExit, 0, 0, 0, 0, false, 0);
	}
}

@Override
long gtk_leave_notify_event (long widget, long event) {
	if (display.currentControl != this) return 0;
	int [] state = new int [1];
	GDK.gdk_event_get_state(event, state);

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int time = GDK.gdk_event_get_time(event);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;

	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	int [] crossingMode = new int[1];
	GTK3.memmove(gdkEvent, event, GdkEventCrossing.sizeof);
	crossingMode[0] = gdkEvent.mode;

	display.removeMouseHoverTimeout (handle);
	int result = 0;
	if (sendLeaveNotify() || display.getCursorControl () == null) {
		if (crossingMode[0] != GDK.GDK_CROSSING_NORMAL && crossingMode[0] != GDK.GDK_CROSSING_UNGRAB) return 0;
		if ((state[0] & (GDK.GDK_BUTTON1_MASK | GDK.GDK_BUTTON2_MASK | GDK.GDK_BUTTON3_MASK)) != 0) return 0;
		result = sendMouseEvent (SWT.MouseExit, 0, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
		display.currentControl = null;
	}
	return result;
}

@Override
long gtk_mnemonic_activate (long widget, long arg1) {
	int result = 0;
	long eventPtr = GTK3.gtk_get_current_event ();
	if (eventPtr != 0) {
		int type = GDK.gdk_event_get_event_type(eventPtr);
		type = fixGdkEventTypeValues(type);
		if (type == GDK.GDK_KEY_PRESS) {
			Control focusControl = display.getFocusControl ();
			long focusHandle = focusControl != null ? focusControl.focusHandle () : 0;
			if (focusHandle != 0) {
				display.mnemonicControl = this;
				GTK3.gtk_widget_event (focusHandle, eventPtr);
				display.mnemonicControl = null;
			}
			result = 1;
		}
		gdk_event_free (eventPtr);
	}
	return result;
}

@Override
void gtk4_motion_event(long controller, double x, double y, long event) {
	if (this == display.currentControl && (hooks(SWT.MouseHover) || filters(SWT.MouseHover))) {
		display.addMouseHoverTimeout(handle);
	}

	int time = GDK.gdk_event_get_time(event);
	int state = GDK.gdk_event_get_modifier_state(event);
	boolean isHint = false;

	if (this != display.currentControl) {
		if (display.currentControl != null && !display.currentControl.isDisposed()) {
			display.removeMouseHoverTimeout(display.currentControl.handle);
			/*
			 *  Note: for GTK4, the call to display.mapInPixels function was removed due to the
			 *  inability to get the origin of surfaces. Testing needs to be done to see if
			 *  the x, y, coordinates suffice.
			 */
			display.currentControl.sendMouseEvent(SWT.MouseExit, 0, time, x, y, isHint, state);
		}
		if (!isDisposed()) {
			display.currentControl = this;
			sendMouseEvent(SWT.MouseEnter, 0, time, x, y, isHint, state);
		}
	}

	sendMouseEvent(SWT.MouseMove, 0, time, x, y, isHint, state);
}

@Override
long gtk_motion_notify_event (long widget, long event) {
	int result;

	double[] eventX = new double[1];
	double[] eventY = new double[1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	lastInput.x = (int)eventX[0];
	lastInput.y = (int)eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;

	// See comment in #dragDetect()
	if ((dragDetectionQueue != null) && OS.isWayland()) {
		boolean dragging = false;
		if ((state & DRAG_DETECT) != 0 && wantDragDropDetection ()) {
				boolean [] consume = new boolean [1];
				if (dragDetect ((int) eventX[0], (int) eventY[0], true, true, consume)) {
					dragging = true;
					if (consume [0]) result = 1;
				if (isDisposed ()) return 1;
			} else {
			}
		}
		if (dragging) {
			GTK3.gtk_event_controller_handle_event(dragGesture,event);
			int eventType = GDK.gdk_event_get_event_type(event);
			if (eventType == GDK.GDK_3BUTTON_PRESS) return 0;

			Point scaledEvent = DPIUtil.autoScaleDown(new Point((int)eventX[0], (int) eventY[0]));

			int [] eventButton = new int [1];
			int [] eventState = new int [1];
			if (GTK.GTK4) {
				eventButton[0] = GDK.gdk_button_event_get_button(event);
				eventState[0] = GDK.gdk_event_get_modifier_state(event);
			} else {
				GDK.gdk_event_get_button(event, eventButton);
				GDK.gdk_event_get_state(event, eventState);
			}

			if (sendDragEvent (eventButton[0], eventState[0], scaledEvent.x, scaledEvent.y, false)){
				return 1;
			}
		}
	}

	if (this == display.currentControl && (hooks(SWT.MouseHover) || filters(SWT.MouseHover))) {
		display.addMouseHoverTimeout(handle);
	}

	int time = GDK.gdk_event_get_time(event);
	double x, y;
	int [] state = new int [1];
	boolean isHint = false;

	if (GTK.GTK4) {
		state[0] = GDK.gdk_event_get_modifier_state(event);
		x = eventX[0];
		y = eventY[0];
	} else {
		double [] eventRX = new double[1];
		double [] eventRY = new double[1];
		GDK.gdk_event_get_root_coords(event, eventRX, eventRY);
		x = eventRX[0];
		y = eventRY[0];

		GdkEventMotion gdkEvent = new GdkEventMotion();
		GTK3.memmove(gdkEvent, event, GdkEventMotion.sizeof);
		state[0] = gdkEvent.state;
		isHint = gdkEvent.is_hint != 0;

		if (isHint) {
			int [] pointer_x = new int [1], pointer_y = new int [1], mask = new int [1];
			long window = eventWindow ();
			display.getWindowPointerPosition (window, pointer_x, pointer_y, mask);
			x = pointer_x [0];
			y = pointer_y [0];
			state[0] = mask [0];
		}
	}

	if (this != display.currentControl) {
		if (display.currentControl != null && !display.currentControl.isDisposed ()) {
			display.removeMouseHoverTimeout(display.currentControl.handle);
			Point pt = display.mapInPixels(this, display.currentControl, (int)x, (int)y);
			display.currentControl.sendMouseEvent(SWT.MouseExit,  0, time, pt.x, pt.y, isHint, state[0]);
		}
		if (!isDisposed ()) {
			display.currentControl = this;
			sendMouseEvent(SWT.MouseEnter, 0, time, x, y, isHint, state[0]);
		}
	}

	result = sendMouseEvent(SWT.MouseMove, 0, time, x, y, isHint, state[0]) ? 0 : 1;
	return result;
}

@Override
long gtk_popup_menu (long widget) {
	if (!hasFocus()) return 0;
	int [] x = new int [1], y = new int [1];
	if (GTK.GTK4) {
		/*
		 * TODO: calling gdk_window_get_device_position() with a 0
		 * for the GdkWindow uses gdk_get_default_root_window(),
		 * which doesn't exist on GTK4.
		 */
	} else {
		display.getWindowPointerPosition (0, x, y, null);
	}
	return showMenu (x [0], y [0], SWT.MENU_KEYBOARD) ? 1 : 0;
}

@Override
long gtk_preedit_changed (long imcontext) {
	display.showIMWindow (this);
	return 0;
}

@Override
long gtk_realize (long widget) {
	if (!GTK.GTK4) {
		long imHandle = imHandle ();
		if (imHandle != 0) {
			long window = gtk_widget_get_window (paintHandle ());
			GTK.gtk_im_context_set_client_window (imHandle, window);
		}
	}
	if (backgroundImage != null) {
		setBackgroundSurface (backgroundImage);
	}
	return 0;
}

@Override
boolean gtk4_scroll_event(long controller, double dx, double dy, long event) {
	boolean handled = false;

	int time = GDK.gdk_event_get_time(event);
	int state = GDK.gdk_event_get_modifier_state(event);

	int direction = GDK.gdk_scroll_event_get_direction(event);
	boolean discreteScrolling = direction != GDK.GDK_SCROLL_SMOOTH;

	// Note about GTK4: Scroll events do not provide coordinates of the event (gdk_event_get_position returns Nan)
	if (discreteScrolling) {
		switch (direction) {
			case GDK.GDK_SCROLL_UP:
				return !sendMouseEvent(SWT.MouseWheel, 0, 3, SWT.SCROLL_LINE, true, time, 0, 0, false, state);
			case GDK.GDK_SCROLL_DOWN:
				return !sendMouseEvent(SWT.MouseWheel, 0, -3, SWT.SCROLL_LINE, true, time, 0, 0, false, state);
			case GDK.GDK_SCROLL_LEFT:
				return !sendMouseEvent(SWT.MouseHorizontalWheel, 0, 3, 0, true, time, 0, 0, false, state);
			case GDK.GDK_SCROLL_RIGHT:
				return !sendMouseEvent(SWT.MouseHorizontalWheel, 0, -3, 0, true, time, 0, 0, false, state);
		}
	} else {
		double[] delta_x = new double[1], delta_y = new double [1];
		GDK.gdk_scroll_event_get_deltas(event, delta_x, delta_y);

		if (delta_x[0] != 0) {
			handled = !sendMouseEvent(SWT.MouseHorizontalWheel, 0, (int)(-3 * delta_x[0]), 0, true, time, 0, 0, false, state);
		}
		if (delta_y[0] != 0) {
			handled = !sendMouseEvent(SWT.MouseWheel, 0, (int)(-3 * delta_y[0]), SWT.SCROLL_LINE, true, time, 0, 0, false, state);
		}

		return handled;
	}

	return false;
}

@Override
long gtk_scroll_event (long widget, long eventPtr) {
	long result = 0;

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(eventPtr, eventX, eventY);

	int [] state = new int [1];
	GDK.gdk_event_get_state(eventPtr, state);

	double [] eventRX = new double[1];
	double [] eventRY = new double[1];
	GDK.gdk_event_get_root_coords(eventPtr, eventRX, eventRY);

	int time = GDK.gdk_event_get_time(eventPtr);

	int [] direction = new int[1];
	boolean fetched = GDK.gdk_event_get_scroll_direction(eventPtr, direction);

	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];

	if (containedInRegion(lastInput.x, lastInput.y)) return 0;

	if (fetched) {
		switch (direction[0]) {
			case GDK.GDK_SCROLL_UP:
				return sendMouseEvent (SWT.MouseWheel, 0, 3, SWT.SCROLL_LINE, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
			case GDK.GDK_SCROLL_DOWN:
				return sendMouseEvent (SWT.MouseWheel, 0, -3, SWT.SCROLL_LINE, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
			case GDK.GDK_SCROLL_LEFT:
				return sendMouseEvent (SWT.MouseHorizontalWheel, 0, 3, 0, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
			case GDK.GDK_SCROLL_RIGHT:
				return sendMouseEvent (SWT.MouseHorizontalWheel, 0, -3, 0, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1;
		}
	} else {
		double[] delta_x = new double[1], delta_y = new double [1];
		boolean deltasAvailable = GDK.gdk_event_get_scroll_deltas (eventPtr, delta_x, delta_y);

		if (deltasAvailable) {
			if (delta_x [0] != 0) {
				result = (sendMouseEvent (SWT.MouseHorizontalWheel, 0, (int)(-3 * delta_x [0]), 0, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1);
			}
			if (delta_y [0] != 0) {
				result = (sendMouseEvent (SWT.MouseWheel, 0, (int)(-3 * delta_y [0]), SWT.SCROLL_LINE, true, time, eventRX[0], eventRY[0], false, state[0]) ? 0 : 1);
			}
		}
	}
	return result;
}

@Override
long gtk3_show_help (long widget, long helpType) {
	if (!hasFocus ()) return 0;
	return sendHelpEvent (helpType) ? 1 : 0;
}

@Override
long gtk_style_updated (long widget) {
	if (backgroundImage != null) {
		setBackgroundSurface (backgroundImage);
	}
	return 0;
}

@Override
long gtk_unrealize (long widget) {
	if (!GTK.GTK4) {
		long imHandle = imHandle ();
		if (imHandle != 0) GTK.gtk_im_context_set_client_window (imHandle, 0);
	}
	return 0;
}

/**
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data
 * @return the platform specific GC handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public long internal_new_GC (GCData data) {
	checkWidget ();
	long gdkResource = GTK.GTK4 ? paintSurface () : paintWindow ();
	if (gdkResource == 0) error (SWT.ERROR_NO_HANDLES);
	long gc = data.cairo;
	if (gc != 0) {
		Cairo.cairo_reference (gc);
	} else {
		if (GTK.GTK4) {
			long surface = GDK.gdk_surface_create_similar_surface(gdkResource, Cairo.CAIRO_CONTENT_COLOR_ALPHA, data.width, data.height);
			gc = Cairo.cairo_create(surface);
		} else {
			gc = GDK.gdk_cairo_create (gdkResource);
		}
	}
	if (gc == 0) error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		} else {
			if ((data.style & SWT.RIGHT_TO_LEFT) != 0) {
				data.style |= SWT.MIRRORED;
			}
		}
		data.drawable = gdkResource;
		data.device = display;

		Control control = findBackgroundControl ();
		if (control == null) control = this;
		data.font = font != null ? font : defaultFont ();
		data.foregroundRGBA = getForegroundGdkRGBA ();
		data.backgroundRGBA = control.getBackgroundGdkRGBA ();
	}
	return gc;
}

long imHandle () {
	return 0;
}

/**
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public void internal_dispose_GC (long hDC, GCData data) {
	checkWidget ();
	Cairo.cairo_destroy (hDC);
}

/**
 * Returns <code>true</code> if the underlying operating
 * system supports this reparenting, otherwise <code>false</code>
 *
 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isReparentable () {
	checkWidget();
	return true;
}
boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscurred by a parent or sibling.
	*/
	if (!isVisible ()) return false;
	Control control = this;
	while (control != null) {
		Point size = control.getSizeInPixels ();
		if (size.x == 0 || size.y == 0) {
			return false;
		}
		control = control.parent;
	}
	return true;
}
boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	int code = traversalCode (0, 0);
	if ((code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0) return false;
	return (code & (SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_TAB_NEXT)) != 0;
}
boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
	int code = traversalCode (0, 0);
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are enabled.  Otherwise, <code>false</code> is returned.
 * A disabled control is typically not selectable from the user
 * interface and draws with an inactive or "grayed" look.
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
	return getEnabled () && parent.isEnabled ();
}

boolean isFocusAncestor (Control control) {
	while (control != null && control != this && !(control instanceof Shell)) {
		control = control.parent;
	}
	return control == this;
}

/**
 * Returns <code>true</code> if the receiver has the user-interface
 * focus, and <code>false</code> otherwise.
 *
 * @return the receiver's focus state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isFocusControl () {
	checkWidget();
	Control focusControl = display.focusControl;
	if (focusControl != null && !focusControl.isDisposed ()) {
		return this == focusControl;
	}
	return hasFocus ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are visible. Otherwise, <code>false</code> is returned.
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
	checkWidget();
	return getVisible () && parent.isVisible ();
}

Decorations menuShell () {
	return parent.menuShell ();
}

boolean mnemonicHit (char key) {
	return false;
}

boolean mnemonicMatch (char key) {
	return false;
}

@Override
void register () {
	super.register ();
	if (fixedHandle != 0) display.addWidget (fixedHandle, this);
	long imHandle = imHandle ();
	if (imHandle != 0) display.addWidget (imHandle, this);
}

/**
 * Requests that this control and all of its ancestors be repositioned by
 * their layouts at the earliest opportunity. This should be invoked after
 * modifying the control in order to inform any dependent layouts of
 * the change.
 * <p>
 * The control will not be repositioned synchronously. This method is
 * fast-running and only marks the control for future participation in
 * a deferred layout.
 * <p>
 * Invoking this method multiple times before the layout occurs is an
 * inexpensive no-op.
 *
 * @since 3.105
 */
public void requestLayout () {
	getShell ().layout (new Control[] {this}, SWT.DEFER);
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 * <p>
 * Schedules a paint request if the invalidated area is visible
 * or becomes visible later. It is not necessary for the caller
 * to explicitly call {@link #update()} after calling this method,
 * but depending on the platform, the automatic repaints may be
 * delayed considerably.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw () {
	checkWidget();
	redraw (false);
}

void redraw (boolean all) {
//	checkWidget();
	if (!GTK.gtk_widget_get_visible (topHandle ())) return;
	redrawWidget (0, 0, 0, 0, true, all, false);
}

/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn.
 * The next time a paint request is processed, that area of
 * the receiver will be painted, including the background.
 * If the <code>all</code> flag is <code>true</code>, any
 * children of the receiver which intersect with the specified
 * area will also paint their intersecting areas. If the
 * <code>all</code> flag is <code>false</code>, the children
 * will not be painted.
 * <p>
 * Schedules a paint request if the invalidated area is visible
 * or becomes visible later. It is not necessary for the caller
 * to explicitly call {@link #update()} after calling this method,
 * but depending on the platform, the automatic repaints may be
 * delayed considerably.
 * </p>
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget();
	Rectangle rect = DPIUtil.autoScaleUp(new Rectangle(x, y, width, height));
	redrawInPixels(rect.x, rect.y, rect.width, rect.height, all);
}

void redrawInPixels (int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (!GTK.gtk_widget_get_visible (topHandle ())) return;
	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - width - x;
	redrawWidget (x, y, width, height, false, all, false);
}

void redrawChildren () {
}

void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean all, boolean trim) {
	if (!GTK.gtk_widget_get_realized(handle)) return;
	GdkRectangle rect = new GdkRectangle ();
	if (GTK.GTK4) {
		long surface = paintSurface ();
		if (redrawAll) {
			int [] w = new int [1], h = new int [1];
			gdk_surface_get_size (surface, w, h);
			rect.width = w [0];
			rect.height = h [0];
		} else {
			rect.x = x;
			rect.y = y;
			rect.width = Math.max (0, width);
			rect.height = Math.max (0, height);
		}
		/* TODO: GTK4 no ability to invalidate surfaces, may need to keep track of
		 * invalid regions ourselves and do gdk_surface_queue_expose. Will need a different way to force redraws
		 * New "render" signal? */
	} else {
		long window = paintWindow ();
		if (redrawAll) {
			int [] w = new int [1], h = new int [1];
			gdk_window_get_size (window, w, h);
			rect.width = w [0];
			rect.height = h [0];
		} else {
			rect.x = x;
			rect.y = y;
			rect.width = Math.max (0, width);
			rect.height = Math.max (0, height);
		}
		GDK.gdk_window_invalidate_rect (window, rect, all);
	}
}

@Override
void release (boolean destroy) {
	Control next = null, previous = null;
	if (destroy && parent != null) {
		Control[] children = parent._getChildren ();
		int index = 0;
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		if (index > 0) {
			previous = children [index - 1];
		}
		if (index + 1 < children.length) {
			next = children [index + 1];
			next.removeRelation ();
		}
		removeRelation ();
	}
	super.release (destroy);
	if (destroy) {
		if (previous != null && next != null) previous.addRelation (next);
	}
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	fixedHandle = 0;
	parent = null;
	cairoDisposeRegion();
}

@Override
void releaseParent () {
	parent.removeControl (this);
}

@Override
void releaseWidget () {
	boolean hadFocus = display.getFocusControl() == this;
	super.releaseWidget ();
	if (hadFocus) fixFocus (this);
	if (display.currentControl == this) display.currentControl = null;
	display.removeMouseHoverTimeout (handle);
	if (!GTK.GTK4) {
		long imHandle = imHandle ();
		if (imHandle != 0 && GTK.GTK_IS_IM_CONTEXT(imHandle)) {
			GTK.gtk_im_context_reset (imHandle);
			GTK.gtk_im_context_set_client_window (imHandle, 0);
		}
		if (enableWindow != 0) {
			GDK.gdk_window_set_user_data (enableWindow, 0);
			GDK.gdk_window_destroy (enableWindow);
			enableWindow = 0;
		}
		redrawWindow = 0;
	}
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	cursor = null;
	toolTipText = null;
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
	region = null;
	if (dragGesture != 0) {
		OS.g_object_unref(dragGesture);
		dragGesture = 0;
	}
	if (rotateGesture != 0) {
		OS.g_object_unref(rotateGesture);
		rotateGesture = 0;
	}
	if (zoomGesture != 0) {
		OS.g_object_unref(zoomGesture);
		zoomGesture = 0;
	}
}

@Override
void destroyWidget() {
	if (GTK.GTK4) {
		// Remove widget from hierarchy  by removing it from parent container
		if (parent != null) {
			long currHandle = topHandle();
			if(GTK.GTK_IS_WINDOW(currHandle)) {
				GTK4.gtk_window_destroy(currHandle);
			}
			else {
				OS.swt_fixed_remove(parent.parentingHandle(), fixedHandle);
			}
		}
		releaseHandle();
	} else {
		super.destroyWidget();
	}
}

/**
 * GTK3 only, do not call on GTK4.
 * @param window a GdkWindow
 * @param sibling the sibling thereof, or 0
 * @param above a boolean setting for whether the window
 * should be raised or lowered
 */
void restackWindow (long window, long sibling, boolean above) {
	GDK.gdk_window_restack (window, sibling, above);
}

void flushQueueOnDnd() {
	// Case where mouse motion triggered a DnD:
	// Send only initial MouseDown but not the MouseMove events that were used
	// to determine DnD threshold.
	// This is to preserve backwards Cocoa/Win32 compatibility.
	Event mouseDownEvent = dragDetectionQueue.getFirst();
	mouseDownEvent.data = Boolean.valueOf(true); // force send MouseDown to avoid subsequent MouseMove before MouseDown.
	dragDetectionQueue = null;
	sendOrPost(SWT.MouseDown, mouseDownEvent);
}

boolean sendDragEvent (int button, int stateMask, int x, int y, boolean isStateMask) {
	if (OS.isWayland() && dragDetectionQueue != null) {
		// Flush events used to detect drag&drop just before sending `DragDetect` event.
		// This is to maintain the same order of events as on other platforms.
		flushQueueOnDnd();
	}

	Event event = new Event ();
	event.button = button;
	Rectangle eventRect = new Rectangle (x, y, 0, 0);
	event.setBounds (eventRect);
	if ((style & SWT.MIRRORED) != 0) event.x = DPIUtil.autoScaleDown(getClientWidth ()) - event.x;
	if (isStateMask) {
		event.stateMask = stateMask;
	} else {
		setInputState (event, stateMask);
	}
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
}

void sendFocusEvent (int type) {
	Shell shell = _getShell ();
	Display display = this.display;
	display.focusControl = this;
	display.focusEvent = type;
	sendEvent (type);
	display.focusControl = null;
	display.focusEvent = SWT.None;
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/
	if (!shell.isDisposed ()) {
		switch (type) {
			case SWT.FocusIn:
				shell.setActiveControl (this);
				break;
			case SWT.FocusOut:
				if (shell != display.activeShell) {
					shell.setActiveControl (null);
				}
				break;
		}
	}
}

 boolean sendGestureEvent (int stateMask, int detail, int x, int y, double delta) {
	if (containedInRegion(x, y)) return false;
	switch (detail) {
	case SWT.GESTURE_ROTATE: {
		return sendGestureEvent(stateMask, detail, x, y, delta, 0, 0, 0);
	}
	case SWT.GESTURE_MAGNIFY: {
		return sendGestureEvent(stateMask, detail, x, y, 0,0,0, delta);
	}
	case SWT.GESTURE_BEGIN: {
		return sendGestureEvent(stateMask, detail, x, y, 0, 0, 0, delta);
	}
	case SWT.GESTURE_END: {
		return sendGestureEvent(stateMask, detail, 0, 0, 0, 0, 0, 0);
	}
	default:
		//case not supported.
		return false;
	}
}

boolean sendGestureEvent (int stateMask, int detail, int x, int y, double xDirection, double yDirection) {
	if (containedInRegion(x, y)) return false;
	if (detail == SWT.GESTURE_SWIPE) {
		return sendGestureEvent(stateMask, detail, x, y, 0, (int)xDirection, (int)yDirection, 0);
	} else return false;
}

boolean sendGestureEvent (int stateMask, int detail, int x, int y, double rotation, int xDirection, int yDirection, double magnification) {
	if (containedInRegion(x, y)) return false;
	Event event = new Event ();
	event.stateMask = stateMask;
	event.detail = detail;
	event.x = x;
	event.y = y;
	switch (detail) {
		case SWT.GESTURE_ROTATE: {
			event.rotation = rotation;
			break;
		}
		case SWT.GESTURE_MAGNIFY: {
			event.magnification = magnification;
			break;
		}
		case SWT.GESTURE_SWIPE: {
			event.xDirection = xDirection;
			event.yDirection = yDirection;
			break;
		}
		case SWT.GESTURE_BEGIN:
		case SWT.GESTURE_END: {
			break;
		}
	}
	postEvent(SWT.Gesture, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendHelpEvent (long helpType) {
	Control control = this;
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			return true;
		}
		control = control.parent;
	}
	return false;
}

boolean sendLeaveNotify() {
	return false;
}

boolean sendMouseEvent (int type, int button, int time, double x, double y, boolean is_hint, int state) {
	if (containedInRegion((int) x, (int) y)) return true;
	return sendMouseEvent (type, button, 0, 0, false, time, x, y, is_hint, state);
}

/*
 * @return
 * 	true - event sending not canceled by user.
 *  false - event sending canceled by user.
 */
boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, int time, double x, double y, boolean is_hint, int state) {
	if (containedInRegion((int) x, (int) y)) return true;
	if (!hooks (type) && !filters (type)) {
		/*
		 * On Wayland, MouseDown events are cached for DnD purposes, but
		 * unfortunately this breaks simple cases with a single MouseDown
		 * listener. The MouseDown event is cached but never sent, as MouseUp
		 * isn't hooked and thus the logic to send cached events is never run.
		 *
		 * The solution is to check for MouseUp events even if MouseUp isn't
		 * hooked. We can check the queue and flush it by sending the MouseDown
		 * event, similar to the way the caching logic does it when receiving a
		 * MouseMove event. See bug 529126.
		 */
		if (OS.isWayland() && dragDetectionQueue != null) {
			/*
			 * The first event in the queue will always be a MouseDown, as
			 * the queue is only ever created if a MouseDown event is being cached.
			 * Thus, if the queue only has one element, it is guaranteed to be a
			 * MouseDown event. More than 1 element implies MouseMove: let the caching
			 * logic handle this case.
			 */
			if (type == SWT.MouseUp && dragDetectionQueue.size() == 1) {
				Event mouseDownEvent = dragDetectionQueue.getFirst();
				dragDetectionQueue = null;
				sendOrPost(SWT.MouseDown, mouseDownEvent);
			}
		}
		/* This checks for Wayland, a previous MouseDown || MouseMove in the
		 * dragDetectionQueue and it checks if the current event is MouseMove
		 * This will prevent them from not being queued, which caused
		 * Bug 576215 - [Wayland] Mouse events not received as on other platforms.
		 * In x11 this will always return true as before.
		 */
		if( (OS.isX11() || (dragDetectionQueue == null) || (type != SWT.MouseMove)) ) return true;
	}
	Event event = new Event ();
	event.time = time;
	event.button = button;
	event.detail = detail;
	event.count = count;
	if (is_hint) {
		// coordinates are already window-relative, see #gtk_motion_notify_event(..) and bug 94502
		Rectangle eventRect = new Rectangle ((int)x, (int)y, 0, 0);
		event.setBounds (DPIUtil.autoScaleDown (eventRect));
	} else {
		int [] origin_x = new int [1], origin_y = new int [1];
		Rectangle eventRect;
		if (GTK.GTK4) {
//			long surface = eventSurface ();
//			GDK.gdk_surface_get_origin (surface, origin_x, origin_y);
//			eventRect = new Rectangle ((int)x - origin_x [0], (int)y - origin_y [0], 0, 0);
			eventRect = new Rectangle ((int)x, (int)y, 0, 0);
			event.setBounds (DPIUtil.autoScaleDown (eventRect));
		} else {
			long window = eventWindow ();
			GDK.gdk_window_get_origin (window, origin_x, origin_y);
			eventRect = new Rectangle ((int)x - origin_x [0], (int)y - origin_y [0], 0, 0);
			event.setBounds (DPIUtil.autoScaleDown (eventRect));
		}
	}
	if ((style & SWT.MIRRORED) != 0) event.x = DPIUtil.autoScaleDown (getClientWidth ()) - event.x;
	setInputState (event, state);

	/**
	 * Bug 510446:
	 * For Wayland support, Drag detection is now done in mouseMove (as does gtk internally).
	 *
	 * However, traditionally external widgets (e.g StyledText or non-SWT widgets) expect to
	 * know if a drag has started by the time mouseDown is sent.
	 * As such, for backwards compatibility with external widgets (e.g StyledText.java), we
	 * delay sending of SWT.MouseDown (and also queue up SWT.MouseMove) until we know if a
	 * drag started or not.
	 *
	 * Technical notes:
	 * - To ensure we follow 'send/post' contract as per parameter, we
	 *   temporarily utilize event.data to hold send/post flag.
	 *   There's also logic in place such that mouseDown/mouseMotion is always sent before mouseUp.
	 * - On Gtk3x11 it's not due to hacky implementation of DnD.
	 *   On Wayland mouseMove is once again sent during DnD as per improved architecture.
	 */
	event.data = Boolean.valueOf(send);
	if (OS.isWayland()) {
		if (type == SWT.MouseDown) {
			if (wantDragDropDetection ()) {
				// Delay MouseDown
				dragDetectionQueue = new LinkedList<>();
				dragDetectionQueue.add(event);
				return true; // event never canceled as not yet sent.
			}
		} else {
			if (dragDetectionQueue != null) {
				switch (type) {
				case SWT.MouseMove:
					if (dragDetect (event.x, event.y, false, true, null)) {
						// Note that if drag&drop is initiated, `dragDetect()` will no longer return true,
						// because GTK considers gesture to be complete and inactive. In this case, code
						// in #sendDragEvent() will flush the queue. This code path is used in the case when
						// someone is listening to `DragDetect` but decided not to initiate drag&drop.
						flushQueueOnDnd();
					} else {
						dragDetectionQueue.add(event);
					}
					break;
				case SWT.MouseUp:
					// Case where mouse up was released before DnD threshold was hit.
					mouseDown = false;
					// Decide if we should send or post the queued up MouseDown and MouseMovement events.
					// If mouseUp is send, then send all. If mouseUp is post, then decide based on previous
					// send flag.
					boolean sendOrPostAll = send ? true : (Boolean) dragDetectionQueue.getFirst().data;
					dragDetectionQueue.forEach(queuedEvent -> queuedEvent.data = Boolean.valueOf(sendOrPostAll));

					// Flush queued up MouseDown/MouseMotion events, so they are triggered before MouseUp
					sendOrPost(SWT.MouseDown, dragDetectionQueue.removeFirst());
					dragDetectionQueue.forEach(queuedEvent -> sendOrPost(SWT.MouseMove, queuedEvent));
					dragDetectionQueue = null;
				}
			}
		}
	}
	return sendOrPost(type, event);
}

private boolean sendOrPost(int type, Event event) {
	assert event.data != null : "event.data should have been a Boolean, but received null";
	boolean send = (Boolean) event.data;
	event.data = null;

	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	return event.doit;
}

/**
 * Not direct gtk api, but useful to have them combined as they are usually called together.
 * @param widget the GTK reference.
 * @param hAlign is of type GTKAlign enum. See OS.java
 * @param vAlign is of type GTKAlign enum. See OS.java
 */
void gtk_widget_set_align(long widget, int hAlign, int vAlign) {
	GTK.gtk_widget_set_halign (widget, hAlign);
	GTK.gtk_widget_set_valign (widget, vAlign);
}

void gtk_label_set_align(long label, float xAlign, float yAlign) {
	GTK.gtk_label_set_xalign(label, xAlign);
	GTK.gtk_label_set_yalign(label, yAlign);
}

void setBackground () {
	if ((state & BACKGROUND) == 0 && backgroundImage == null) {
		if ((state & PARENT_BACKGROUND) != 0) {
			setParentBackground ();
		} else {
			setWidgetBackground ();
		}
		redrawWidget (0, 0, 0, 0, true, false, false);
	}
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground (Color color) {
	checkWidget ();
	_setBackground (color);
	if (color != null) {
		this.updateBackgroundMode ();
	}
}

private void _setBackground (Color color) {
	if (((state & BACKGROUND) == 0) && color == null) return;
	if (color != null && color.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	boolean set = false;
	GdkRGBA rgba = null;
	if (color != null) {
		rgba = color.handle;
		backgroundAlpha = color.getAlpha();
	}
	set = true;
	if (set) {
		if (color == null) {
			state &= ~BACKGROUND;
		} else {
			state |= BACKGROUND;
		}
		setBackgroundGdkRGBA (rgba);
	}
	redrawChildren ();
}

void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	GdkRGBA selectedBackground = display.getSystemColor(SWT.COLOR_LIST_SELECTION).handle;
	// Form background string
	String name = display.gtk_widget_class_get_css_name(handle);
	String css = name + " {background-color: " + display.gtk_rgba_to_css_string(rgba) + ";}\n"
			+ name + ":selected" + " {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}";

		// Cache background
		cssBackground = css;

		// Apply background color and any cached foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css (context, finalCss);
}

void gtk_css_provider_load_from_css (long context, String css) {
	/* Utility function. */
	//@param css : a 'css java' string like "{\nbackground: red;\n}".

	if (provider == 0) {
		provider = GTK.gtk_css_provider_new ();
		GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		OS.g_object_unref (provider);
	}
	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (css, true), -1, null);
	}
}

void setBackgroundGdkRGBA(GdkRGBA rgba) {
	setBackgroundGdkRGBA (handle, rgba);
}

void setBackgroundGdkRGBA (long handle, GdkRGBA rgba) {
	double alpha = 1.0;
	if (rgba == null) {
		if ((state & PARENT_BACKGROUND) != 0) {
			alpha = 0;
			Control control = findBackgroundControl();
			if (control == null) control = this;
			rgba = control.getBackgroundGdkRGBA();
		}
	} else {
		alpha = backgroundAlpha;
	}
	if (rgba != null) {
		rgba.alpha = alpha / (float)255;
	}

	long context = GTK.gtk_widget_get_style_context(handle);
	setBackgroundGdkRGBA(context, handle, rgba);
	if (!GTK.GTK4) GTK3.gtk_style_context_invalidate(context);
}
/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
 * </p>
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setBackgroundImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (image == backgroundImage && backgroundAlpha > 0) return;
	backgroundAlpha = 255;
	this.backgroundImage = image;
	if (backgroundImage != null) {
		setBackgroundSurface (backgroundImage);
		redrawWidget (0, 0, 0, 0, true, false, false);
	} else {
		setWidgetBackground ();
	}
	redrawChildren ();
}

void setBackgroundSurface (Image image) {
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.  Note that on some platforms,
 * a mouse button must currently be down for capture to be assigned.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	checkWidget();
	/* FIXME !!!!! */
	/*
	if (capture) {
		OS.gtk_widget_grab_focus (handle);
	} else {
		OS.gtk_widget_grab_default (handle);
	}
	*/
}
/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument, or to the default cursor for that kind of control
 * if the argument is null.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @param cursor the new cursor (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget();
	if (cursor != null && cursor.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	setCursor (cursor != null ? cursor.handle : 0);
}

void setCursor (long cursor) {
	if (GTK.GTK4) {
		long eventHandle = eventHandle ();
		GTK4.gtk_widget_set_cursor (eventHandle, cursor);
	} else {
		long window = eventWindow ();
		if (window != 0) {
			GDK.gdk_window_set_cursor (window, cursor);
			update(false, true);
		}
	}
}

/**
 * Sets the receiver's drag detect state. If the argument is
 * <code>true</code>, the receiver will detect drag gestures,
 * otherwise these gestures will be ignored.
 *
 * @param dragDetect the new drag detect state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public void setDragDetect (boolean dragDetect) {
	checkWidget ();
	if (dragDetect) {
		state |= DRAG_DETECT;
	} else {
		state &= ~DRAG_DETECT;
	}
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
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
	checkWidget();
	if (((state & DISABLED) == 0) == enabled) return;
	Control control = null;
	boolean fixFocus = false;
	if (!enabled) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	enableWidget (enabled);
	if (isDisposed ()) return;

	if (!GTK.GTK4) {
		if (enabled) {
			if (enableWindow != 0) {
				cleanupEnableWindow();
			}
		} else {
			GTK.gtk_widget_realize (handle);
			long parentHandle = parent.eventHandle ();
			long window = parent.eventWindow ();
			long topHandle = topHandle ();
			GdkWindowAttr attributes = new GdkWindowAttr ();
			GtkAllocation allocation = new GtkAllocation ();
			GTK.gtk_widget_get_allocation (topHandle, allocation);
			attributes.x = allocation.x;
			attributes.y = allocation.y;
			attributes.width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
			attributes.height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
			attributes.event_mask = (0xFFFFFFFF & ~OS.ExposureMask);
			attributes.wclass = GDK.GDK_INPUT_ONLY;
			attributes.window_type = GDK.GDK_WINDOW_CHILD;
			enableWindow = GTK3.gdk_window_new (window, attributes, GDK.GDK_WA_X | GDK.GDK_WA_Y);
			if (enableWindow != 0) {
				GDK.gdk_window_set_user_data (enableWindow, parentHandle);
				restackWindow (enableWindow, gtk_widget_get_window (topHandle), true);
				if (GTK.gtk_widget_get_visible (topHandle)) GDK.gdk_window_show_unraised (enableWindow);
			}
		}
	}

	if (fixFocus) fixFocus (control);
}

void cleanupEnableWindow() {
	GDK.gdk_window_set_user_data (enableWindow, 0);
	GDK.gdk_window_destroy (enableWindow);
	enableWindow = 0;
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>,
 * such that all keyboard events will be delivered to it.  Focus
 * reassignment will respect applicable platform constraints.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #forceFocus
 */
public boolean setFocus () {
	checkWidget();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return forceFocus ();
}

/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
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
 */
public void setFont (Font font) {
	checkWidget();
	if (((state & FONT) == 0) && font == null) return;
	this.font = font;
	long fontDesc;
	if (font == null) {
		fontDesc = defaultFont ().handle;
	} else {
		if (font.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		fontDesc = font.handle;
	}
	if (font == null) {
		state &= ~FONT;
	} else {
		state |= FONT;
	}
	setFontDescription (fontDesc);
}

void setFontDescription (long font) {
	setFontDescription (handle, font);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setForeground (Color color) {
	checkWidget();
	if (((state & FOREGROUND) == 0) && color == null) return;
	if (color != null && color.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	boolean set = false;
	set = !getForeground().equals(color);
	if (set) {
		if (color == null) {
			state &= ~FOREGROUND;
		} else {
			state |= FOREGROUND;
		}
		GdkRGBA rgba = color == null ? null : color.handle;
		setForegroundGdkRGBA (rgba);
	}
}

void setForegroundGdkRGBA (GdkRGBA rgba) {
	setForegroundGdkRGBA (handle, rgba);
}

void setForegroundGdkRGBA (long handle, GdkRGBA rgba) {
	GdkRGBA toSet;
	if (rgba != null) {
		toSet = rgba;
	} else {
		toSet = display.COLOR_WIDGET_FOREGROUND_RGBA;
	}
	long context = GTK.gtk_widget_get_style_context (handle);
	// Form foreground string
	String color = display.gtk_rgba_to_css_string(toSet);
	String name = display.gtk_widget_class_get_css_name(handle);
	GdkRGBA selectedForeground = display.COLOR_LIST_SELECTION_TEXT_RGBA;
	String selection = !name.contains("treeview") ? " selection" : ":selected";
	String css = "* {color: " + color + ";}\n"
			+ name + selection + " {color: " + display.gtk_rgba_to_css_string(selectedForeground) + ";}";

		// Cache foreground color
		cssForeground = css;

		// Apply foreground color and any cached background color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.FOREGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

void setInitialBounds () {
	if ((state & ZERO_WIDTH) != 0 && (state & ZERO_HEIGHT) != 0) {
		if (!GTK.GTK4) {
			/*
			* Feature in GTK.  On creation, each widget's allocation is
			* initialized to a position of (-1, -1) until the widget is
			* first sized.  The fix is to set the value to (0, 0) as
			* expected by SWT.
			*/
			long topHandle = topHandle();

			GtkAllocation allocation = new GtkAllocation();
			if ((parent.style & SWT.MIRRORED) != 0) {
				allocation.x = parent.getClientWidth();
			} else {
				allocation.x = 0;
			}
			allocation.y = 0;

			if (mustBeVisibleOnInitBounds ()) {
				GTK.gtk_widget_set_visible(topHandle, true);
			}
			// Prevent GTK+ allocation warnings, preferred size should be retrieved before setting allocation size.
			GTK.gtk_widget_get_preferred_size(topHandle, null, null);
			GTK3.gtk_widget_size_allocate (topHandle, allocation);
		}
	} else {
		resizeHandle (1, 1);
		forceResize ();
	}
}

/**
 * Widgets with unusual bounds calculation behavior can override this method
 * to return {@code true} if the widget must be visible during call to
 * {@link #setInitialBounds()}.
 *
 * @return {@code false} by default on modern GTK 3 versions (3.20+).
 */
boolean mustBeVisibleOnInitBounds() {
	return false;
}

/*
 * Sets the receivers Drag Gestures in order to do drag detection correctly for
 * X11/Wayland window managers after GTK3.14.
 * TODO currently phase is set to BUBBLE = 2. Look into using groups perhaps.
 */
private void setDragGesture () {
	dragGesture = GTK.gtk_gesture_drag_new (handle);
	GTK.gtk_event_controller_set_propagation_phase (dragGesture,
	        2);
	GTK.gtk_gesture_single_set_button (dragGesture, 0);
	OS.g_signal_connect(dragGesture, OS.begin, gestureBegin.getAddress(), this.handle);
	OS.g_signal_connect(dragGesture, OS.end, gestureEnd.getAddress(), this.handle);
	return;
}

//private void setPanGesture () {
///* TODO: Panning gesture requires a GtkOrientation object. Need to discuss what orientation should be default. */
//}

private void setRotateGesture () {
	rotateGesture = GTK.gtk_gesture_rotate_new(handle);
	GTK.gtk_event_controller_set_propagation_phase (rotateGesture,
	        2);
	OS.g_signal_connect (rotateGesture, OS.angle_changed, gestureRotation.getAddress(), this.handle);
	OS.g_signal_connect(rotateGesture, OS.begin, gestureBegin.getAddress(), this.handle);
	OS.g_signal_connect(rotateGesture, OS.end, gestureEnd.getAddress(), this.handle);
	return;
}

private void setZoomGesture () {
	zoomGesture = GTK.gtk_gesture_zoom_new(handle);
	GTK.gtk_event_controller_set_propagation_phase (zoomGesture,
	        2);
	OS.g_signal_connect(zoomGesture, OS.scale_changed, gestureZoom.getAddress(), this.handle);
	OS.g_signal_connect(zoomGesture, OS.begin, gestureBegin.getAddress(), this.handle);
	OS.g_signal_connect(zoomGesture, OS.end, gestureEnd.getAddress(), this.handle);
	return;
}

static Control getControl(long handle) {
	Display display = Display.findDisplay(Thread.currentThread());
	if (display ==null || display.isDisposed()) return null;
	Widget widget = display.findWidget(handle);
	if (widget == null) return null;
	return (Control) widget;
}

static void rotateProc(long gesture, double angle, double angle_delta, long user_data) {
	if (GTK.gtk_gesture_is_recognized(gesture)) {
		int [] state = new int[1];
		double [] x = new double[1];
		double [] y = new double[1];
		GTK3.gtk_get_current_event_state(state);
		GTK.gtk_gesture_get_point(gesture, GTK.gtk_gesture_get_last_updated_sequence(gesture), x, y);
		/*
		 * Returning delta is off by two decimal points and is returning negative numbers on
		 * counter clockwise rotations from GTK. From the java doc of GestureEvent.rotation,
		 * we have to invert the rotation number so that positive/negative numbers are returned
		 * correctly (inverted).
		 */
		double delta = -(GTK.gtk_gesture_rotate_get_angle_delta(gesture)*100);
		Control control = getControl(user_data);
		control.sendGestureEvent(state[0], SWT.GESTURE_ROTATE, (int) x[0], (int) y[0], delta);
	}
}

static void magnifyProc(long gesture, double zoom, long user_data) {
	if (GTK.gtk_gesture_is_recognized(gesture)) {
		int [] state = new int[1];
		double [] x = new double[1];
		double [] y = new double[1];
		GTK3.gtk_get_current_event_state(state);
		GTK.gtk_gesture_get_point(gesture, GTK.gtk_gesture_get_last_updated_sequence(gesture), x, y);
		double delta = GTK.gtk_gesture_zoom_get_scale_delta(gesture);
		Control control = getControl(user_data);
		control.sendGestureEvent(state[0], SWT.GESTURE_MAGNIFY, (int) x[0], (int) y[0], delta);
	}
}

static void swipeProc(long gesture, double velocity_x, double velocity_y, long user_data) {
	if (GTK.gtk_gesture_is_recognized(gesture)) {
		double [] xVelocity = new double [1];
		double [] yVelocity = new double [1];
		if (GTK.gtk_gesture_swipe_get_velocity(gesture, xVelocity, yVelocity)) {
			int [] state = new int[1];
			double [] x = new double[1];
			double [] y = new double[1];
			GTK3.gtk_get_current_event_state(state);
			GTK.gtk_gesture_get_point(gesture, GTK.gtk_gesture_get_last_updated_sequence(gesture), x, y);
			Control control = getControl(user_data);
			control.sendGestureEvent(state[0], SWT.GESTURE_SWIPE, (int) x[0], (int) y[0], xVelocity[0], yVelocity[0]);
		}
	}
}

static void gestureBeginProc(long gesture, long sequence, long user_data) {
	if (GTK.gtk_gesture_is_recognized(gesture)) {
		int [] state = new int[1];
		double [] x = new double[1];
		double [] y = new double[1];
		GTK3.gtk_get_current_event_state(state);
		GTK.gtk_gesture_get_point(gesture, sequence, x, y);
		Control control = getControl(user_data);
		control.sendGestureEvent(state[0], SWT.GESTURE_BEGIN, (int) x[0], (int) y[0], 0);
	}
}

static void gestureEndProc(long gesture, long sequence, long user_data) {
	if (GTK.gtk_gesture_is_recognized(gesture)) {
		int [] state = new int[1];
		double [] x = new double[1];
		double [] y = new double[1];
		GTK3.gtk_get_current_event_state(state);
		GTK.gtk_gesture_get_point(gesture, GTK.gtk_gesture_get_last_updated_sequence(gesture), x, y);
		Control control = getControl(user_data);
		control.sendGestureEvent(state[0], SWT.GESTURE_END, (int) x[0], (int) y[0], 0);
	}
}
/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 * <p>
 * Note: Disposing of a control that has a pop up menu will
 * dispose of the menu.  To avoid this behavior, set the
 * menu to null before the control is disposed.
 * </p>
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget();
	if (menu != null) {
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

@Override
void setOrientation (boolean create) {
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (handle != 0) GTK.gtk_widget_set_direction (handle, dir);
		if (fixedHandle != 0) GTK.gtk_widget_set_direction (fixedHandle, dir);
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public void setOrientation (int orientation) {
	checkWidget ();
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	setOrientation (false);
	style &= ~SWT.MIRRORED;
	checkMirrored ();
}

/**
 * Changes the parent of the widget to be the one provided.
 * Returns <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget ();
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	GTK.gtk_widget_realize (parent.handle);
	long topHandle = topHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (topHandle, allocation);
	int x = allocation.x;
	int y = allocation.y;
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	if ((this.parent.style & SWT.MIRRORED) != 0) {
		x =  this.parent.getClientWidth () - width - x;
	}
	if ((parent.style & SWT.MIRRORED) != 0) {
		x = parent.getClientWidth () - width - x;
	}
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	Menu [] menus = oldShell.findMenus (this);
	if (oldShell != newShell || oldDecorations != newDecorations) {
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);

		if (!GTK.GTK4) {
			newDecorations.fixAccelGroup ();
			oldDecorations.fixAccelGroup ();
		}
	}
	long newParent = parent.parentingHandle();
	gtk_widget_reparent(this, newParent);
	OS.swt_fixed_move (newParent, topHandle, x, y);
	/*
	* Restore the original widget size since GTK does not keep it.
	*/
	resizeHandle(width, height);
	/*
	* Cause a size allocation this widget's topHandle.  Note that
	* all calls to gtk_widget_size_allocate() must be preceded by
	* a call to gtk_widget_size_request().
	*/
	GtkRequisition requisition = new GtkRequisition ();
	gtk_widget_get_preferred_size (topHandle, requisition);
	allocation.x = x;
	allocation.y = y;
	allocation.width = width;
	allocation.height = height;
	gtk_widget_size_allocate(topHandle, allocation, -1);
	this.parent = parent;
	setZOrder (null, false, true);
	reskin (SWT.ALL);
	return true;
}

void setParentBackground () {
	setBackgroundGdkRGBA (handle, null);
	if (fixedHandle != 0) setBackgroundGdkRGBA (fixedHandle, null);
}

void setParentGdkResource (Control child) {
}

boolean setRadioSelection (boolean value) {
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.  Nested calls
 * to this method are stacked.
 * <p>
 * Note: This operation is a hint and may not be supported on some
 * platforms or for some widgets.
 * </p>
 *
 * @param redraw the new redraw state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw(int, int, int, int, boolean)
 * @see #update()
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
			if (!GTK.GTK4) {
				if (redrawWindow != 0) {
					long window = paintWindow ();
					/* Explicitly hiding the window avoids flicker on GTK+ >= 2.6 */
					GDK.gdk_window_hide (redrawWindow);
					GDK.gdk_window_destroy (redrawWindow);
					GDK.gdk_window_set_events (window, GTK3.gtk_widget_get_events (paintHandle ()));
					redrawWindow = 0;
				}
			}
		}
	} else {
		if (drawCount++ == 0) {
			if (GTK.gtk_widget_get_realized (handle)) {
				Rectangle bounds = getBoundsInPixels ();
				if (!GTK.GTK4) {
					long window = paintWindow ();
					GdkWindowAttr attributes = new GdkWindowAttr ();
					attributes.width = bounds.width;
					attributes.height = bounds.height;
					attributes.event_mask = GDK.GDK_EXPOSURE_MASK;
					attributes.window_type = GDK.GDK_WINDOW_CHILD;
					redrawWindow = GTK3.gdk_window_new (window, attributes, 0);
					if (redrawWindow != 0) {
						int mouseMask = GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK |
							GDK.GDK_ENTER_NOTIFY_MASK | GDK.GDK_LEAVE_NOTIFY_MASK |
							GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_POINTER_MOTION_HINT_MASK |
							GDK.GDK_BUTTON_MOTION_MASK | GDK.GDK_BUTTON1_MOTION_MASK |
							GDK.GDK_BUTTON2_MOTION_MASK | GDK.GDK_BUTTON3_MOTION_MASK;
						GDK.gdk_window_set_events (window, GDK.gdk_window_get_events (window) & ~mouseMask);
						GDK.gdk_window_show (redrawWindow);
					}
				}
			}
		}
	}
}

@Override
boolean setTabItemFocus (boolean next) {
	if (!isShowing ()) return false;
	return forceFocus ();
}

/**
 * Sets the base text direction (a.k.a. "paragraph direction") of the receiver,
 * which must be one of the constants <code>SWT.LEFT_TO_RIGHT</code>,
 * <code>SWT.RIGHT_TO_LEFT</code>, or <code>SWT.AUTO_TEXT_DIRECTION</code>.
 * <p>
 * <code>setOrientation</code> would override this value with the text direction
 * that is consistent with the new orientation.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * It doesn't set the base text direction on GTK and Cocoa.
 * </p>
 *
 * @param textDirection the base text direction style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see SWT#AUTO_TEXT_DIRECTION
 * @see SWT#FLIP_TEXT_DIRECTION
 *
 * @since 3.102
 */
public void setTextDirection(int textDirection) {
	checkWidget ();
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

	if (!Objects.equals(string, toolTipText)) {
		toolTipText = string;
		setToolTipText(_getShell(), string);
	}
}

void setToolTipText(Shell shell, String newString) {
	/*
	* Feature in GTK.  In order to prevent children widgets
	* from inheriting their parent's tooltip, the tooltip is
	* a set on a shell only. In order to force the shell tooltip
	* to update when a new tip string is set, the existing string
	* in the tooltip is set to null, followed by running a query.
	* The real tip text can then be set.
	*
	* Note that this will only run if the control for which the
	* tooltip is being set is the current control (i.e. the control
	* under the pointer).
	*/
	if (display.currentControl == this) {
		setToolTipText(shell.handle, newString);
	}
}

/**
 * Sets whether this control should send touch events (by default controls do not).
 * Setting this to <code>false</code> causes the receiver to send gesture events
 * instead.  No exception is thrown if a touch-based input device is not
 * detected (this can be determined with <code>Display#getTouchEnabled()</code>).
 *
 * @param enabled the new touch-enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    </ul>
 *
 * @see Display#getTouchEnabled
 *
 * @since 3.7
 */
public void setTouchEnabled(boolean enabled) {
	checkWidget();
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
	checkWidget();
	if (((state & HIDDEN) == 0) == visible) return;
	long topHandle = topHandle();
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		state &= ~HIDDEN;
		if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) == 0) {
			if (!GTK.GTK4) {
				if (enableWindow != 0) GDK.gdk_window_show_unraised(enableWindow);
			}
			GTK.gtk_widget_show (topHandle);
		}
	} else {
		/*
		* Bug in GTK.  Invoking gtk_widget_hide() on a widget that has
		* focus causes a focus_out_event to be sent. If the client disposes
		* the widget inside the event, GTK GP's.  The fix is to reassign focus
		* before hiding the widget.
		*
		* NOTE: In order to stop the same widget from taking focus,
		* temporarily clear and set the GTK_VISIBLE flag.
		*/
		Control control = null;
		boolean fixFocus = false;
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
		state |= HIDDEN;
		if (fixFocus) {
			GTK.gtk_widget_set_can_focus (topHandle, false);
			fixFocus (control);
			if (isDisposed ()) return;
			GTK.gtk_widget_set_can_focus (topHandle, true);
		}
		GTK.gtk_widget_hide (topHandle);
		if (isDisposed ()) return;
		if (!GTK.GTK4) {
			if (enableWindow != 0) GDK.gdk_window_hide(enableWindow);
		}

		sendEvent (SWT.Hide);
	}
}

void setZOrder (Control sibling, boolean above, boolean fixRelations) {
	setZOrder (sibling, above, fixRelations, true);
}

void setZOrder (Control sibling, boolean above, boolean fixRelations, boolean fixChildren) {
	int index = 0, siblingIndex = 0, oldNextIndex = -1;
	Control[] children = null;
	if (fixRelations) {
		/* determine the receiver's and sibling's indexes in the parent */
		children = parent._getChildren ();
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		if (sibling != null) {
			while (siblingIndex < children.length) {
				if (children [siblingIndex] == sibling) break;
				siblingIndex++;
			}
		}
		/* remove "Labelled by" relationships that will no longer be valid */
		removeRelation ();
		if (index + 1 < children.length) {
			oldNextIndex = index + 1;
			children [oldNextIndex].removeRelation ();
		}
		if (sibling != null) {
			if (above) {
				sibling.removeRelation ();
			} else {
				if (siblingIndex + 1 < children.length) {
					children [siblingIndex + 1].removeRelation ();
				}
			}
		}
	}

	long topHandle = topHandle ();
	long siblingHandle = sibling != null ? sibling.topHandle () : 0;
	if (GTK.GTK4) {
		//TODO: Test GTK3 behavior then implement, probably using gdk_toplevel_lower
	} else {
		long window = gtk_widget_get_window (topHandle);
		if (window != 0) {
			long siblingWindow = 0;
			if (sibling != null) {
				if (above && sibling.enableWindow != 0) {
					siblingWindow = enableWindow;
				} else {
					siblingWindow = GTK3.gtk_widget_get_window (siblingHandle);
				}
			}
			long redrawWindow = fixChildren ? parent.redrawWindow : 0;
			if (OS.isWayland () || (siblingWindow == 0 && (!above || redrawWindow == 0))) {
				if (above) {
					GDK.gdk_window_raise (window);
					if (redrawWindow != 0) GDK.gdk_window_raise (redrawWindow);
					if (enableWindow != 0) GDK.gdk_window_raise (enableWindow);
				} else {
					if (enableWindow != 0) GDK.gdk_window_lower (enableWindow);
					GDK.gdk_window_lower (window);
				}
			} else {
				long siblingW = siblingWindow != 0 ? siblingWindow : redrawWindow;
				boolean stack_mode = above;
				if (redrawWindow != 0 && siblingWindow == 0) stack_mode = false;
				restackWindow (window, siblingW, stack_mode);
				if (enableWindow != 0) {
					restackWindow (enableWindow, window, true);
				}
			}
		}
	}
	if (fixChildren) {
		if (above) {
			parent.moveAbove (topHandle, siblingHandle);
		} else {
			parent.moveBelow (topHandle, siblingHandle);
		}
	}
	/*  Make sure that the parent internal windows are on the bottom of the stack	*/
	if (!above && fixChildren) 	parent.fixZOrder ();

	if (fixRelations) {
		/* determine the receiver's new index in the parent */
		if (sibling != null) {
			if (above) {
				index = siblingIndex - (index < siblingIndex ? 1 : 0);
			} else {
				index = siblingIndex + (siblingIndex < index ? 1 : 0);
			}
		} else {
			if (above) {
				index = 0;
			} else {
				index = children.length - 1;
			}
		}

		/* add new "Labelled by" relations as needed */
		children = parent._getChildren ();
		if (0 < index) {
			children [index - 1].addRelation (this);
		}
		if (index + 1 < children.length) {
			addRelation (children [index + 1]);
		}
		if (oldNextIndex != -1) {
			if (oldNextIndex <= index) oldNextIndex--;
			/* the last two conditions below ensure that duplicate relations are not hooked */
			if (0 < oldNextIndex && oldNextIndex != index && oldNextIndex != index + 1) {
				children [oldNextIndex - 1].addRelation (children [oldNextIndex]);
			}
		}
	}
}

void setWidgetBackground  () {
	GdkRGBA rgba = (state & BACKGROUND) != 0 ? getBackgroundGdkRGBA () : null;
	if (fixedHandle != 0) setBackgroundGdkRGBA (fixedHandle, rgba);
	setBackgroundGdkRGBA (handle, rgba);
}

boolean showMenu (int x, int y) {
	return showMenu (x, y, SWT.MENU_MOUSE);
}

boolean showMenu (int x, int y, int detail) {
	Event event = new Event ();
	Rectangle eventRect = new Rectangle (x, y, 0, 0);
	event.setBounds (DPIUtil.autoScaleDown (eventRect));
	event.detail = detail;
	sendEvent (SWT.MenuDetect, event);
	//widget could be disposed at this point
	if (isDisposed ()) return false;
	if (event.doit) {
		if (menu != null && !menu.isDisposed ()) {
			if (GTK.GTK4) {

				long temp = 0;
				if (GTK.gtk_widget_get_parent(menu.handle) != 0) {
					temp = OS.g_object_ref(menu.handle);
					GTK.gtk_widget_unparent(menu.handle);
				}
				GTK.gtk_widget_set_parent(menu.handle, this.handle);
				if (temp != 0) OS.g_object_unref(temp);


				menu.setLocationInPixels(x, y);
				menu.setVisible(true);

				return true;
			} else {
				Rectangle rect = DPIUtil.autoScaleUp (event.getBounds ());
				if (rect.x != x || rect.y != y) {
					menu.setLocationInPixels (rect.x, rect.y);
				}
				menu.setVisible (true);
				return true;
			}

		}
	}
	return false;
}

void showWidget () {
	// Comment this line to disable zero-sized widgets
	state |= ZERO_WIDTH | ZERO_HEIGHT;
	long topHandle = topHandle ();
	long parentHandle = parent.parentingHandle ();
	parent.setParentGdkResource (this);
	if (GTK.GTK4) {
		OS.swt_fixed_add(parentHandle, topHandle);
	} else {
		GTK3.gtk_container_add(parentHandle, topHandle);
	}
	if (handle != 0 && handle != topHandle) GTK.gtk_widget_show (handle);
	if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) == 0) {
		if (fixedHandle != 0) GTK.gtk_widget_show (fixedHandle);
	}
	if (fixedHandle != 0) fixStyle (fixedHandle);
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
				if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
				}
			}
		}
	}
}

/**
 * Based on the argument, perform one of the expected platform
 * traversal action. The argument should be one of the constants:
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
 *
 * @param traversal the type of traversal
 * @return true if the traversal succeeded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean traverse (int traversal) {
	checkWidget ();
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

/**
 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
 *
 * <p>Valid traversal values are
 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
 * event is created with standard values based on the KeyDown event.  If
 * <code>traversal</code> is one of the other traversal constants then the Traverse
 * event is created with this detail, and its <code>doit</code> is taken from the
 * KeyDown event.
 * </p>
 *
 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
 * this from <code>event</code>
 * @param event the KeyDown event
 *
 * @return <code>true</code> if the traversal succeeded
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public boolean traverse (int traversal, Event event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return traverse (traversal, event.character, event.keyCode, event.keyLocation, event.stateMask, event.doit);
}

/**
 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
 *
 * <p>Valid traversal values are
 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
 * event is created with standard values based on the KeyDown event.  If
 * <code>traversal</code> is one of the other traversal constants then the Traverse
 * event is created with this detail, and its <code>doit</code> is taken from the
 * KeyDown event.
 * </p>
 *
 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
 * this from <code>event</code>
 * @param event the KeyDown event
 *
 * @return <code>true</code> if the traversal succeeded
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public boolean traverse (int traversal, KeyEvent event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return traverse (traversal, event.character, event.keyCode, event.keyLocation, event.stateMask, event.doit);
}

boolean traverse (int traversal, char character, int keyCode, int keyLocation, int stateMask, boolean doit) {
	if (traversal == SWT.TRAVERSE_NONE) {
		switch (keyCode) {
			case SWT.ESC: {
				traversal = SWT.TRAVERSE_ESCAPE;
				doit = true;
				break;
			}
			case SWT.CR: {
				traversal = SWT.TRAVERSE_RETURN;
				doit = true;
				break;
			}
			case SWT.ARROW_DOWN:
			case SWT.ARROW_RIGHT: {
				traversal = SWT.TRAVERSE_ARROW_NEXT;
				doit = false;
				break;
			}
			case SWT.ARROW_UP:
			case SWT.ARROW_LEFT: {
				traversal = SWT.TRAVERSE_ARROW_PREVIOUS;
				doit = false;
				break;
			}
			case SWT.TAB: {
				traversal = (stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
				doit = true;
				break;
			}
			case SWT.PAGE_DOWN: {
				if ((stateMask & SWT.CTRL) != 0) {
					traversal = SWT.TRAVERSE_PAGE_NEXT;
					doit = true;
				}
				break;
			}
			case SWT.PAGE_UP: {
				if ((stateMask & SWT.CTRL) != 0) {
					traversal = SWT.TRAVERSE_PAGE_PREVIOUS;
					doit = true;
				}
				break;
			}
			default: {
				if (character != 0 && (stateMask & (SWT.ALT | SWT.CTRL)) == SWT.ALT) {
					traversal = SWT.TRAVERSE_MNEMONIC;
					doit = true;
				}
				break;
			}
		}
	}

	Event event = new Event ();
	event.character = character;
	event.detail = traversal;
	event.doit = doit;
	event.keyCode = keyCode;
	event.keyLocation = keyLocation;
	event.stateMask = stateMask;
	Shell shell = getShell ();

	boolean all = false;
	switch (traversal) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS: {
			all = true;
			// FALL THROUGH
		}
		case SWT.TRAVERSE_ARROW_NEXT:
		case SWT.TRAVERSE_ARROW_PREVIOUS:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS: {
			/* traversal is a valid traversal action */
			break;
		}
		case SWT.TRAVERSE_MNEMONIC: {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
		default: {
			/* traversal is not a valid traversal action */
			return false;
		}
	}

	Control control = this;
	do {
		if (control.traverse (event)) return true;
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}

boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = this == display.mnemonicControl || mnemonicMatch (event.character);
	return traverse (event);
}

boolean translateMnemonic (int keyval, long event) {
	long key = GDK.gdk_keyval_to_unicode (keyval);
	int [] state = new int[1];
	if (GTK.GTK4) {
		state[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_state(event, state);
	}

	if (key < 0x20) return false;
	if (state[0] == 0) {
		int code = traversalCode (keyval, event);
		if ((code & SWT.TRAVERSE_MNEMONIC) == 0) return false;
	} else {
		Shell shell = _getShell ();
		int mask = GDK.GDK_CONTROL_MASK | GDK.GDK_SHIFT_MASK | GDK.GDK_MOD1_MASK;
		if (GTK.GTK4) {
			if (state[0] != GDK.GDK_MOD1_MASK) return false;
		} else {
			if ((state[0] & mask) != GTK3.gtk_window_get_mnemonic_modifier (shell.shellHandle)) return false;
		}
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		Event javaEvent = new Event ();
		javaEvent.detail = SWT.TRAVERSE_MNEMONIC;
		if (setKeyState (javaEvent, event)) {
			return translateMnemonic (javaEvent, null) || shell.translateMnemonic (javaEvent, this);
		}
	}
	return false;
}

boolean translateTraversal (long event) {
	int detail = SWT.TRAVERSE_NONE;
	int [] eventKeyval = new int [1];
	int [] eventState = new int[1];
	if (GTK.GTK4) {
		eventKeyval[0] = GDK.gdk_key_event_get_keyval(event);
		eventState[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_keyval(event, eventKeyval);
		GDK.gdk_event_get_state(event, eventState);
	}

	int key = eventKeyval[0];
	int code = traversalCode (key, event);
	boolean all = false;
	switch (key) {
		case GDK.GDK_Escape: {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case GDK.GDK_KP_Enter:
		case GDK.GDK_Return: {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case GDK.GDK_ISO_Left_Tab:
		case GDK.GDK_Tab: {
			boolean next = (eventState[0] & GDK.GDK_SHIFT_MASK) == 0;
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case GDK.GDK_Up:
		case GDK.GDK_Left:
		case GDK.GDK_Down:
		case GDK.GDK_Right: {
			boolean next = key == GDK.GDK_Down || key == GDK.GDK_Right;
			if (parent != null && (parent.style & SWT.MIRRORED) != 0) {
				if (key == GDK.GDK_Left || key == GDK.GDK_Right) next = !next;
			}
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case GDK.GDK_Page_Up:
		case GDK.GDK_Page_Down: {
			all = true;
			if ((eventState[0] & GDK.GDK_CONTROL_MASK) == 0) return false;
			detail = key == GDK.GDK_Page_Down ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	Event javaEvent = new Event ();
	javaEvent.doit = (code & detail) != 0;
	javaEvent.detail = detail;
	javaEvent.time = GDK.gdk_event_get_time(event);
	if (!setKeyState (javaEvent, event)) return false;
	Shell shell = getShell ();
	Control control = this;
	do {
		if (control.traverse (javaEvent)) return true;
		if (!javaEvent.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}

int traversalCode (int key, long event) {
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT |  SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	return code;
}

boolean traverse (Event event) {
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the traverse
	* event.  If this happens, return true to stop further
	* event processing.
	*/
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:			return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Widget group = computeTabGroup ();
	Widget [] list = root.computeTabList ();
	int length = list.length;
	int index = 0;
	while (index < length) {
		if (list [index] == group) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = ((index + offset + length) % length)) != start) {
		Widget widget = list [index];
		if (!widget.isDisposed () && widget.setTabGroupFocus (next)) {
			return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus (next);
}

boolean traverseItem (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus (next)) return true;
		}
	}
	return false;
}

boolean traverseReturn () {
	return false;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseMnemonic (char key) {
	return mnemonicHit (key);
}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns. If there
 * are no outstanding paint request, this method does
 * nothing.
 * <p>Note:</p>
 * <ul>
 * <li>This method does not cause a redraw.</li>
 * <li>Some OS versions forcefully perform automatic deferred painting.
 * This method does nothing in that case.</li>
 * </ul>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw()
 * @see #redraw(int, int, int, int, boolean)
 * @see PaintListener
 * @see SWT#Paint
 */
public void update () {
	checkWidget ();
	update (false, true);

}

void update (boolean all, boolean flush) {
	if(GTK.GTK4) GTK.gtk_widget_queue_draw(handle);
	if (!GTK.gtk_widget_get_visible (topHandle ())) return;
	if (!GTK.gtk_widget_get_realized (handle)) return;
	long window = paintWindow ();
	if (flush) display.flushExposes (window, all);
}

void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}

void updateLayout (boolean all) {
	/* Do nothing */
}

@Override
long windowProc (long handle, long arg0, long user_data) {
	switch ((int)user_data) {
		case EXPOSE_EVENT_INVERSE: {
			if ((state & OBSCURED) != 0) break;
			Control control = findBackgroundControl ();
			boolean draw = control != null && control.backgroundImage != null;
			if (!draw && (state & CANVAS) != 0) {
				draw = (state & BACKGROUND) == 0;
			}
			if (draw) {
				long cairo = arg0;
				GdkRectangle rect = new GdkRectangle ();
				GDK.gdk_cairo_get_clip_rectangle (cairo, rect);
				if (control == null) control = this;
				long gdkResource;
				if (GTK.GTK4) {
					gdkResource = GTK4.gtk_native_get_surface(GTK4.gtk_widget_get_native (handle));
					drawBackground (control, gdkResource, cairo, rect.x, rect.y, rect.width, rect.height);
				} else {
					gdkResource = GTK3.gtk_widget_get_window(handle);
					if (gdkResource != 0) {
						drawBackground (control, gdkResource, 0, rect.x, rect.y, rect.width, rect.height);
					} else {
						drawBackground (control, 0, cairo, rect.x, rect.y, rect.width, rect.height);
					}
				}
			}
			break;
		}
		case DRAW: {
			if (paintHandle() == handle && drawRegion) {
				return gtk_draw(handle, arg0);
			}
		}
	}
	return super.windowProc (handle, arg0, user_data);
}

/**
 * Gets the position of the top left corner of the control in root window (display) coordinates.
 * GTK3 only, do not call on GTK4.
 *
 * @return the origin
 */
Point getWindowOrigin () {
	int [] x = new int [1];
	int [] y = new int [1];

	long window = eventWindow ();
	GDK.gdk_window_get_origin (window, x, y);

	return new Point (x [0], y [0]);
}

/**
 * Gets the position of the top left corner of the control in root window (display) coordinates.
 * GTK4 only, do not call on GTK3.
 *
 * @return the origin
 */
Point getSurfaceOrigin () {
	double[] originX = new double[1], originY = new double[1];
	boolean success = GTK4.gtk_widget_translate_coordinates(fixedHandle, getShell().shellHandle, 0, 0, originX, originY);

	return success ? new Point((int)originX[0], (int)originY[0]) : new Point(0, 0);
}
}
