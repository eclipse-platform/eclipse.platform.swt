/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
 *     Latha Patil (ETAS GmbH) - Custom draw Sash widget using SkijaGC
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent a selectable user interface object that
 * allows the user to drag a rubber banded outline of the sash within the parent
 * control.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL, SMOOTH</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#sash">Sash snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Sash extends CustomControl {
	int startX, startY, lastX, lastY;
	final static int INCREMENT = 1;
	final static int PAGE_INCREMENT = 9;
	private final SashRenderer sashRenderer;
	Shell dragOverlay;
	private static final int DRAG_OVERLAY_ALPHA = 150;
	private Image bandImage;
	private Rectangle bandImageBounds;
	private Cursor resizeCursor;
	private boolean resizeCursorEnabled;
	private final Listener listener;
	boolean isGTK;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see SWT#SMOOTH
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Sash(Composite parent, int style) {
		super(parent, checkStyle(style));
		isGTK = "gtk".equals(SWT.getPlatform());

		listener = event -> {
			switch (event.type) {
				case SWT.KeyDown -> onKeyDown(event);
				case SWT.MouseDown -> onLeftMouseDown(event);
				case SWT.MouseMove -> onMouseMove(event);
				case SWT.MouseExit -> onMouseExit(event);
				case SWT.MouseUp -> onLeftMouseUp(event);
				case SWT.Paint -> onPaint(event);
				case SWT.Resize -> {
					getParent().redraw();
					redraw();
				}
				case SWT.Traverse -> onTraverse(event);
			}
		};

		int[] events = { SWT.KeyDown, SWT.MouseDown, SWT.MouseMove, SWT.MouseUp, SWT.Paint, SWT.Resize, SWT.Traverse,
				SWT.MouseExit };

		for (int eventType : events) {
			if (isGTK && eventType != SWT.Paint) {
				parent.addListener(eventType, listener);
			}
			addListener(eventType, listener);
		}
		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		sashRenderer = rendererFactory.createSashRenderer(this);
	}

	/**
	 * Handles the `SWT.Traverse` event to prevent traversal when arrow keys are
	 * used. This ensures that the arrow keys are processed by the `SWT.KeyDown`
	 * listener instead of moving focus to other controls.
	 *
	 * @param event the traverse event containing details about the traversal action
	 */
	private void onTraverse(Event event) {
		// Prevent traversal for arrow keys
		if (event.detail == SWT.TRAVERSE_ARROW_PREVIOUS || event.detail == SWT.TRAVERSE_ARROW_NEXT) {
			event.doit = false;
		}
	}

	/**
	 * Handles the `SWT.KeyDown` event to allow keyboard navigation of the sash.
	 * Arrow keys are used to move the sash, and the movement step is determined by
	 * whether the CTRL key is pressed (small step) or not (page increment).
	 *
	 * @param event the key event containing details about the key press
	 */
	private void onKeyDown(Event event) {
		KeyEvent keyEvent = new KeyEvent(event);
		int key = keyEvent.keyCode;
		boolean leftMouseButtonPressed = (keyEvent.stateMask & SWT.BUTTON1) != 0;

		if (leftMouseButtonPressed) {
			return;
		}
		boolean isArrowKey = switch (key) {
		case SWT.ARROW_LEFT, SWT.ARROW_RIGHT, SWT.ARROW_UP, SWT.ARROW_DOWN -> true;
		default -> false;
		};
		// Only move sash if Arrow key is pressed (with or without Ctrl)
		if (!isArrowKey) {
			return;
		}
		boolean isRelevantKey = (isHorizontal() && (key == SWT.ARROW_UP || key == SWT.ARROW_DOWN))
				|| (!isHorizontal() && (key == SWT.ARROW_LEFT || key == SWT.ARROW_RIGHT));
		if (!isRelevantKey) {
			return;
		}
		int step = sashStepCalculation(keyEvent);
		Rectangle sashBounds = isGTK ? getBounds() : sashRenderer.getSashBounds();
		Point newSashPosition = calculateNewSashPosition(step, sashBounds);

		if (newSashPosition.x == sashBounds.x && newSashPosition.y == sashBounds.y) {
			return;
		}
		Event selectionEvent = sendSelectionEvent(
				new Rectangle(newSashPosition.x, newSashPosition.y, sashBounds.width, sashBounds.height));
		if (selectionEvent.doit) {
			asyncRedraw(this);
		}
	}

	private Point calculateNewSashPosition(int step, Rectangle sashBounds) {
		Rectangle clientArea = parent.getClientArea();
		int newX = sashBounds.x, newY = sashBounds.y;
		if (isHorizontal()) {
			newX = sashBounds.x + sashBounds.width / 2;
			newY = Math.min(Math.max(clientArea.y, newY + step), clientArea.height - sashBounds.height);
		} else {
			newX = Math.min(Math.max(clientArea.x, newX + step), clientArea.width - sashBounds.width);
			newY = sashBounds.y + sashBounds.height / 2;
		}
		return new Point(newX, newY);
	}

	private int sashStepCalculation(KeyEvent keyEvent) {
		int key = keyEvent.keyCode;
		boolean mirrored = (parent.getStyle() & SWT.MIRRORED) != 0;
		boolean ctrlPressed = (keyEvent.stateMask & SWT.CTRL) != 0;
		int step = ctrlPressed ? INCREMENT : PAGE_INCREMENT;
		if (isHorizontal()) {
			step = (key == SWT.ARROW_UP) ? -step : step;
		} else {
			step = (key == SWT.ARROW_LEFT) ? -step : step;
			step = mirrored ? -step : step;
		}
		return step;
	}

	/**
	 * Handles the `SWT.MouseDown` event for the left mouse button. This method
	 * initiates the dragging process for the sash when the left mouse button is
	 * pressed. It sets up the initial drag state, including the starting position
	 * and the sash's current position. If the `SWT.SMOOTH` style is not set, a drag
	 * overlay is created to visually represent the sash's movement.
	 *
	 * @param e the mouse event containing details about the mouse action
	 */
	private void onLeftMouseDown(Event e) {
		if (e.button != 1) {
			return;
		}
		Rectangle sashBounds = isGTK ? getBounds() : sashRenderer.getSashBounds();
		if (sashBounds == null) {
			return;
		}
		if (isGTK && e.widget == getParent()) {
			if (!(sashBounds.contains(e.x, e.y))) {
				return;
			}
			e = convertToSash(e);
		}
		sashRenderer.setDragging(true);
		startX = e.x;
		startY = e.y;
		lastX = sashBounds.x;
		lastY = sashBounds.y;

		Event event = sendSelectionEvent(new Rectangle(lastX, lastY, sashBounds.width, sashBounds.height));
		if (!isSmooth()) {
			event.detail = SWT.DRAG;
		}
		if (event.doit && !isSmooth()) {
			dragOverlay = createDragOverlay();
			createBandImage();
			redrawDragOverlay();
		}
	}

	private Event convertToSash(Event e) {
		Event ret = new Event();
		var b = getBounds();
		ret.widget = this;
		ret.x = e.x - b.x;
		ret.y = e.y - b.y;
		ret.doit = e.doit;
		return ret;
	}

	private void createBandImage() {
		if (bandImage != null) {
			bandImage.dispose();
		}
		Rectangle bounds = isGTK ? getBounds() : sashRenderer.getSashBounds();

		if (bounds.width == 0 || bounds.height == 0) {
			return;
		}

		bandImageBounds = new Rectangle(0, 0, bounds.width, bounds.height);
		bandImage = new Image(getDisplay(), bandImageBounds);

		GC gc = new GC(bandImage);
		Drawing.drawWithGC(this, gc, g -> {
			sashRenderer.drawBand(g, bandImageBounds);
		});
		gc.dispose();
	}

	private Event sendSelectionEvent(Rectangle sashBounds) {
		Event event = new Event();
		event.setBounds(new Rectangle(sashBounds.x, sashBounds.y, sashBounds.width, sashBounds.height));
		sendSelectionEvent(SWT.Selection, event, true);
		return event;
	}

	private Shell createDragOverlay() {
		Shell overlay = new Shell(parent.getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.ON_TOP);
		overlay.setAlpha(DRAG_OVERLAY_ALPHA);
		final Point leftTop = parent.toDisplay(0, 0);
		final Rectangle clientArea = parent.getClientArea();
		overlay.setBounds(leftTop.x, leftTop.y, clientArea.width, clientArea.height);
		overlay.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
		overlay.setVisible(true);
		return overlay;
	}

	private void redrawDragOverlay() {
		dragOverlay.addListener(SWT.Paint, dragEvent -> {
			if (bandImage != null) {
				// Band flicker and moves slowly using below commented API
//				Drawing.drawWithGC(dragOverlay, dragEvent.gc, gc -> {
//					gc.drawImage(bandImage, 0, 0, bandImageBounds.width, bandImageBounds.height, lastX, lastY,
//							bandImageBounds.width, bandImageBounds.height);
//				});
				dragEvent.gc.drawImage(bandImage, 0, 0, bandImageBounds.width, bandImageBounds.height, lastX, lastY,
						bandImageBounds.width, bandImageBounds.height);
			}
		});
		asyncRedraw(dragOverlay);
	}

	/**
	 * Handles the `SWT.MouseUp` event for the left mouse button. This method
	 * finalizes the dragging process for the sash when the left mouse button is
	 * released. It stops the dragging state, disposes of the drag overlay if it
	 * exists, and sends a selection event. The method also ensures the sash is
	 * redrawn after the drag operation is completed.
	 *
	 * @param event the mouse event containing details about the mouse action
	 */
	private void onLeftMouseUp(Event event) {
		if (event.button != 1 || !sashRenderer.isDragging()) {
			return;
		}
		sashRenderer.setDragging(false);
		if ((!isSmooth() && dragOverlay != null && !dragOverlay.isDisposed())) {
			dragOverlay.dispose();
			dragOverlay = null;
		}
		Rectangle sashRectangle = sashRenderer.getSashBounds();
		sendSelectionEvent(new Rectangle(lastX, lastY, sashRectangle.width, sashRectangle.height));

		if (bandImage != null) {
			bandImage.dispose();
			bandImage = null;
		}
		if (!isDisposed()) {
			asyncRedraw(this);
		}
	}

	/**
	 * Handles the `SWT.MouseMove` event. This method updates the cursor to indicate
	 * resizing and processes the dragging of the sash if the dragging state is
	 * active. It calculates the new position of the sash based on the mouse
	 * movement and updates the sash's bounds. If the `SWT.SMOOTH` style is set, it
	 * sends a selection event with the updated bounds. Otherwise, it updates the
	 * drag overlay or redraws the sash.
	 *
	 * @param event the mouse event containing details about the mouse movement
	 */
	private void onMouseMove(Event event) {
		boolean dragging = sashRenderer.isDragging();
		boolean eventFromParent = event.widget == getParent();

		Rectangle sashBounds = getBounds();
		if (sashBounds == null) {
			return;
		}
		boolean insideSash = isGTK ? sashBounds.contains(event.x, event.y)
				: sashBounds.contains(event.x + sashBounds.x, event.y + sashBounds.y);

		if (isGTK && eventFromParent) {
			event = convertToSash(event);
		}
		// Show resize cursor when inside bounds
		if (insideSash && !resizeCursorEnabled) {
			if (resizeCursor == null || resizeCursor.isDisposed()) {
				resizeCursor = new Cursor(display, isHorizontal() ? SWT.CURSOR_SIZENS : SWT.CURSOR_SIZEWE);
			}
			parent.setCursor(resizeCursor);
			setCursor(resizeCursor);
			resizeCursorEnabled = true;
		}

		// Hide resize cursor when mouse leaves and not dragging
		if (!insideSash && resizeCursorEnabled && !dragging) {
			parent.setCursor(null);
			setCursor(null);
			resizeCursorEnabled = false;
		}

		if (!sashRenderer.isDragging()) {
			return;
		}

		Rectangle clientArea = getParent().getClientArea();
		Point currentPoint = getCurrentPoint(event, sashBounds);
		int newX = lastX, newY = lastY;

		if (!isHorizontal()) {
			int maxX = clientArea.width - sashBounds.width;
			newX = Math.min(Math.max(0, currentPoint.x - startX), maxX);
		} else {
			int maxY = clientArea.height - sashBounds.height;
			newY = Math.min(Math.max(0, currentPoint.y - startY), maxY);
		}

		if (newX == lastX && newY == lastY) {
			return;
		}
		if (isSmooth()) {
			Event selectionEvent = sendSelectionEvent(new Rectangle(newX, newY, sashBounds.width, sashBounds.height));
			if (isDisposed() || !selectionEvent.doit) {
				return;
			}
			Rectangle bounds = selectionEvent.getBounds();
			lastX = bounds.x;
			lastY = bounds.y;
		} else {
			lastX = newX;
			lastY = newY;
		}
		sashRenderer.setSashBounds(lastX, lastY, sashBounds.width, sashBounds.height);
		Control target = isSmooth() ? this : dragOverlay;
		if (!isSmooth()) {
			dragOverlay.setVisible(true);
		}
		asyncRedraw(target);
	}

	private void onMouseExit(Event event) {
		if (!sashRenderer.isDragging() && this.resizeCursorEnabled) {
			parent.setCursor(null);
			setCursor(null);
			this.resizeCursorEnabled = false;
		}
	}

	private boolean isSmooth() {
		return (style & SWT.SMOOTH) != 0;
	}

	/**
	 * Calculates and returns the current point based on the given event and sash
	 * bounds. This method determines the appropriate coordinates for the current
	 * mouse position, taking into account whether the `SWT.SMOOTH` style is
	 * applied.
	 *
	 * @param event      the mouse event containing the current x and y coordinates
	 * @param sashBounds the bounds of the sash, used for coordinate adjustments
	 * @return a `Point` object representing the current x and y coordinates
	 */
	private Point getCurrentPoint(Event event, Rectangle sashBounds) {
		if (isSmooth()) {
			if (event.x < sashBounds.x) {
				event.x += sashBounds.x;
			}
			if (event.y < sashBounds.y) {
				event.y += sashBounds.y;
			}
			return new Point(event.x, event.y);
		}

		return getDisplay().map(this, dragOverlay, event.x, event.y);
	}

	private void asyncRedraw(Control control) {
		Display.getDefault().asyncExec(() -> {
			if (!control.isDisposed()) {
				control.redraw();
			}
		});
	}

	/**
	 * Handles the `SWT.Paint` event. This method is responsible for rendering the
	 * sash. It sets the bounds of the sash and delegates the actual painting to the
	 * `SashRenderer` instance.
	 *
	 * @param event the paint event containing details about the painting action
	 */
	private void onPaint(Event event) {
		Rectangle sashBounds = getBounds();
		sashRenderer.setSashBounds(sashBounds.x, sashBounds.y, sashBounds.width, sashBounds.height);
		Drawing.drawWithGC(this, event.gc, sashRenderer::paint);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the control is selected by the user, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the x, y, width, and height
	 * fields of the event object are valid. If the receiver is being dragged, the
	 * event object detail field contains the value <code>SWT.DRAG</code>.
	 * <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the control is
	 *                 selected by the user
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	/**
	 * Validates and returns the appropriate style for the Sash widget. This method
	 * ensures that only one of the mutually exclusive styles (HORIZONTAL or
	 * VERTICAL) is applied. If no valid style is provided, it defaults to 0.
	 *
	 * @param style the style value to be checked
	 * @return the validated style value, ensuring only one of the valid styles is
	 *         applied
	 */
	static int checkStyle(int style) {
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is selected by the user.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	/**
	 * Computes and returns the default size of the Sash widget. This method
	 * delegates the computation to the `SashRenderer` instance.
	 *
	 * @return a `Point` object representing the default width and height of the
	 *         Sash
	 */
	@Override
	protected Point computeDefaultSize() {
		return sashRenderer.computeDefaultSize();
	}

	/**
	 * Determines whether the Sash is oriented horizontally.
	 *
	 * @return `true` if the Sash is horizontal, `false` otherwise
	 */
	public boolean isHorizontal() {
		return (style & SWT.VERTICAL) == 0;
	}

	/**
	 * This method returns the `SashRenderer` instance used for rendering the Sash.
	 *
	 * @return the `ControlRenderer` instance for the Sash
	 */
	@Override
	protected ControlRenderer getRenderer() {
		return sashRenderer;
	}

	@Override
	public void dispose() {
		if (this.resizeCursor != null && !this.resizeCursor.isDisposed()) {
			this.resizeCursor.dispose();
		}
		if (isGTK && !getParent().isDisposed()) {
			parent.removeListener(SWT.KeyDown, listener);
			parent.removeListener(SWT.MouseDown, listener);
			parent.removeListener(SWT.MouseMove, listener);
			parent.removeListener(SWT.MouseUp, listener);
			parent.removeListener(SWT.MouseExit, listener);
			parent.removeListener(SWT.Resize, listener);
			parent.removeListener(SWT.Traverse, listener);
		}
		super.dispose();
	}
}