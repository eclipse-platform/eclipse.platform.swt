/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.toolbar.*;

/**
 * Instances of this class represent a selectable user interface object that
 * represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN may
 * be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	/**
	 * The renderer interface for the {@link ToolItem}.
	 *
	 * All {@link ToolItem} renderers have to implement this interface to work with
	 * the ToolItem.
	 */
	public interface ToolItemRenderer {

		/**
		 * Renders the {@link ToolItem}.
		 *
		 * @param gc     GC to render with.
		 * @param bounds Bounds of the rendering. x and y are always 0.
		 */
		void render(GC gc, Rectangle bounds);

		/**
		 * Returns the size of the rendered {@link ToolItem}.
		 *
		 * @return The size as a {@link Point}.
		 */
		Point getSize();

		/**
		 * Sets the with of the separator. Only relevant for a separator
		 * {@link ToolItem}.
		 *
		 * @param width The width in pixel.
		 */
		default void setSeparatorWidth(int width) {
		}

		/**
		 * Indicates if the given location is on the button area of the
		 * {@link ToolItem}.
		 *
		 * @param location The location to check.
		 * @return true if on the button are, false otherwise.
		 */
		default boolean isOnButton(Point location) {
			return false;
		}

		/**
		 * Indicates if the given location is on the arrow area of the {@link ToolItem}.
		 *
		 * @param location The location to check.
		 * @return true if on the arrow are, false otherwise.
		 */
		default boolean isOnArrow(Point location) {
			return false;
		}
	}

	/**
	 * The mouse state of the {@link ToolItem}
	 */
	public enum MouseState {
		IDLE, HOVER, DOWN
	}

	private final ToolItemRenderer renderer;

	private ToolBar parent;
	private Control control;
	private String toolTipText;
	private boolean enabled = true;
	private boolean isSelected;
	private Image disabledImage;
	private Image hotImage;

	private Color foregroundColor;
	private Color backgroundColor;

	private MouseState mouseState = MouseState.IDLE;

	private Rectangle bounds = new Rectangle(0, 0, 0, 0);

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>ToolBar</code>) and a style value describing its behavior and
	 * appearance. The item is added to the end of the items maintained by its
	 * parent.
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
	 * @see SWT#PUSH
	 * @see SWT#CHECK
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ToolItem(ToolBar parent, int style) {
		super(checkParent(parent), checkStyle(style));
		this.parent = parent;
		this.renderer = createRenderer();

		parent.createItem(this, parent.getItemCount());
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>ToolBar</code>), a style value describing its behavior and appearance,
	 * and the index at which to place it in the items maintained by its parent.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent   a composite control which will be the parent of the new instance
	 *              (cannot be null)
	 * @param style the style of control to construct
	 * @param index the zero-relative index to store the receiver in its parent
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the parent (inclusive)</li>
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
	 * @see SWT#PUSH
	 * @see SWT#CHECK
	 * @see SWT#RADIO
	 * @see SWT#SEPARATOR
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ToolItem(ToolBar parent, int style, int index) {
		super(checkParent(parent), checkStyle(style));
		this.parent = parent;
		this.renderer = createRenderer();

		parent.createItem(this, index);
	}

	private ToolItemRenderer createRenderer() {
		return switch (style) {
			case SWT.CHECK, SWT.PUSH, SWT.RADIO -> new ToolItemButtonRenderer(parent, this);
			case SWT.SEPARATOR -> new ToolItemSeparatorRenderer(parent);
			case SWT.DROP_DOWN -> new ToolItemDropDownRenderer(parent, this);
			default -> throw new IllegalArgumentException("Invalid Style: " + style);
		};
	}

	private static ToolBar checkParent(ToolBar parent) {
		if (parent == null) {
			throw new IllegalArgumentException();
		}
		return parent;
	}

	@Override
	public void dispose() {
		super.dispose();
		parent.notifyItemDisposed(this);
		state |= DISPOSED;
	}

	/**
	 * Returns the {@link MouseState} of this ToolItem.
	 */
	public MouseState getState() {
		return mouseState;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the control is selected by the user, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called when the mouse is over the arrow
	 * portion of a drop-down tool, the event object detail field contains the value
	 * <code>SWT.ARROW</code>. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * <p>
	 * When the <code>SWT.RADIO</code> style bit is set, the
	 * <code>widgetSelected</code> method is also called when the receiver loses
	 * selection because another item in the same radio group was selected by the
	 * user. During <code>widgetSelected</code> the application can use
	 * <code>getSelection()</code> to determine the current selected state of the
	 * receiver.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the control is
	 *                 selected by the user,
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

	static int checkStyle(int style) {
		return checkBits(style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
	}

	@Override
	void destroyWidget() {
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative to
	 * its parent.
	 *
	 * @return the receiver's bounding rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Rectangle getBounds() {
		checkWidget();
		return bounds;
	}

	/**
	 * Returns the control that is used to fill the bounds of the item when the item
	 * is a <code>SEPARATOR</code>.
	 *
	 * @return the control
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Control getControl() {
		checkWidget();
		return control;
	}

	/**
	 * Returns the receiver's disabled image if it has one, or null if it does not.
	 * <p>
	 * The disabled image is displayed when the receiver is disabled.
	 * </p>
	 *
	 * @return the receiver's disabled image
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Image getDisabledImage() {
		checkWidget();
		return disabledImage;
	}

	/**
	 * Returns the receiver's background color.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform. For
	 * example, on some versions of Windows the background of a TabFolder, is a
	 * gradient rather than a solid color.
	 * </p>
	 *
	 * @return the background color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.120
	 */
	public Color getBackground() {
		checkWidget();
		return backgroundColor;
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
	 * otherwise. A disabled control is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		checkWidget();
		return enabled;
	}

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 *
	 * @return the receiver's foreground color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.120
	 */
	public Color getForeground() {
		checkWidget();
		return foregroundColor;
	}

	/**
	 * Returns the receiver's hot image if it has one, or null if it does not.
	 * <p>
	 * The hot image is displayed when the mouse enters the receiver.
	 * </p>
	 *
	 * @return the receiver's hot image
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Image getHotImage() {
		checkWidget();
		return hotImage;
	}

	/**
	 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public ToolBar getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it is
	 * selected when it is checked (which some platforms draw as a pushed in
	 * button). If the receiver is of any other type, this method returns false.
	 * </p>
	 *
	 * @return the selection state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		return isSelected;
	}

	/**
	 * Returns the receiver's tool tip text, or null if it has not been set.
	 *
	 * @return the receiver's tool tip text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getToolTipText() {
		checkWidget();
		return toolTipText;
	}

	/**
	 * Gets the width of the receiver.
	 *
	 * @return the width
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getWidth() {
		checkWidget();
		return renderer.getSize().x;
	}

	public int getHeight() {
		checkWidget();
		return getSize().y;
	}

	/**
	 * Returns the rendered size of the {@link ToolItem}.
	 *
	 * @return The size as {@link Point}.
	 */
	public Point getSize() {
		return renderer.getSize();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled control is typically not selectable from the user interface and
	 * draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		checkWidget();
		return getEnabled() && parent.isEnabled();
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
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Sets the receiver's background color to the color specified by the argument,
	 * or to the default system color for the control if the argument is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
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
	 * @since 3.120
	 */
	public void setBackground(Color color) {
		checkWidget();
		backgroundColor = color;
	}

	/**
	 * Sets the control that is used to fill the bounds of the item when the item is
	 * a <code>SEPARATOR</code>.
	 *
	 * @param control the new control
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     control has been disposed</li>
	 *                                     <li>ERROR_INVALID_PARENT - if the control
	 *                                     is not in the same widget tree</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setControl(Control control) {
		checkWidget();
		this.control = control;
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables it
	 * otherwise.
	 * <p>
	 * A disabled control is typically not selectable from the user interface and
	 * draws with an inactive or "grayed" look.
	 * </p>
	 *
	 * @param enabled the new enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setEnabled(boolean enabled) {
		checkWidget();
		this.enabled = enabled;
	}

	/**
	 * Sets the receiver's disabled image to the argument, which may be null
	 * indicating that no disabled image should be displayed.
	 * <p>
	 * The disabled image is displayed when the receiver is disabled.
	 * </p>
	 *
	 * @param image the disabled image to display on the receiver (may be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the image
	 *                                     has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setDisabledImage(Image image) {
		checkWidget();
		this.disabledImage = image;
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the argument,
	 * or to the default system color for the control if the argument is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
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
	 * @since 3.120
	 */
	public void setForeground(Color color) {
		checkWidget();
		foregroundColor = color;
	}

	/**
	 * Sets the receiver's hot image to the argument, which may be null indicating
	 * that no hot image should be displayed.
	 * <p>
	 * The hot image is displayed when the mouse enters the receiver.
	 * </p>
	 *
	 * @param image the hot image to display on the receiver (may be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the image
	 *                                     has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setHotImage(Image image) {
		checkWidget();
		this.hotImage = image;
	}

	/**
	 * Sets the selection state of the receiver.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it is
	 * selected when it is checked (which some platforms draw as a pushed in
	 * button).
	 * </p>
	 *
	 * @param selected the new selection state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSelection(boolean selected) {
		checkWidget();
		internalSelect();
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default, causing no
	 * tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip. To
	 * display a single '&amp;' in the tool tip, the character '&amp;' can be
	 * escaped by doubling it in the string.
	 * </p>
	 * <p>
	 * NOTE: This operation is a hint and behavior is platform specific, on Windows
	 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip
	 * text are not shown in tooltip.
	 * </p>
	 *
	 * @param string the new tool tip text (or null)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setToolTipText(String string) {
		checkWidget();
		toolTipText = string;
	}

	/**
	 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
	 *
	 * @param width the new width. If the new value is <code>SWT.DEFAULT</code>, the
	 *              width is a fixed-width area whose amount is determined by the
	 *              platform. If the new value is 0 a vertical or horizontal line
	 *              will be drawn, depending on the setting of the corresponding
	 *              style bit (<code>SWT.VERTICAL</code> or
	 *              <code>SWT.HORIZONTAL</code>). If the new value is
	 *              <code>SWT.SEPARATOR_FILL</code> a variable-width space is
	 *              inserted that acts as a spring between the two adjoining items
	 *              which will push them out to the extent of the containing
	 *              ToolBar.
	 *
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setWidth(int width) {
		checkWidget();
		if (!isSeparator()) {
			return;
		}
		if (width < 0) {
			return;
		}
		renderer.setSeparatorWidth(width);
	}


	int getStyleType() {
		return style & (SWT.CHECK | SWT.PUSH | SWT.RADIO | SWT.SEPARATOR | SWT.DROP_DOWN);
	}

	private void NOT_IMPLEMENTED() {
		System.err.println(Thread.currentThread().getStackTrace()[2] + " not implemented yet!");
	}

	public void render(GC gc, Rectangle bounds) {
		Control control = getControl();
		if (control != null && isSeparator()) {
			control.setBounds(bounds);
		}

		renderer.render(gc, bounds);
		this.bounds = bounds;
	}

	/**
	 * Notifies the item that the mouse has moved within the {@link ToolBar}.
	 *
	 * @param location The location of the mouse.
	 * @return true if this changed the state of the item somehow.
	 */
	public boolean notifyMouseMove(Point location) {
		MouseState newState;
		if (getBounds().contains(location)) {
			newState = MouseState.HOVER;
		} else {
			newState = MouseState.IDLE;
		}

		return updateMouseState(newState);
	}

	/**
	 * Notifies the item that the mouse has moved out of the {@link ToolBar}.
	 *
	 * @return true if this changed the state of the item somehow.
	 */
	public boolean notifyMouseExit() {
		return updateMouseState(MouseState.IDLE);
	}

	/**
	 * Notifies the item that the mouse was pressed down within the {@link ToolBar}.
	 *
	 * @param location The location of the mouse.
	 * @return true if this changed the state of the item somehow.
	 */
	public boolean notifyMouseDown(Point location) {
		MouseState newState;
		if (renderer.isOnButton(location)) {
			newState = MouseState.DOWN;
		} else if (renderer.isOnArrow(location)) {
			newState = MouseState.IDLE;
			Event event = new Event();
			event.detail = SWT.ARROW;
			event.setLocation(location.x, location.y);
			event.stateMask |= SWT.NO_FOCUS;
			sendEvent(SWT.Selection, event);
		} else {
			newState = MouseState.IDLE;
		}

		return updateMouseState(newState);
	}

	private boolean updateMouseState(MouseState mouseState) {
		if (this.mouseState == mouseState) {
			return false;
		}

		this.mouseState = mouseState;
		return true;
	}

	/**
	 * Notifies the item that the mouse was pressed up within the {@link ToolBar}.
	 *
	 * @param location The location of the mouse.
	 * @return true if this changed the state of the item somehow.
	 */
	public boolean notifyMouseUp(Point location) {
		MouseState newState;
		if (renderer.isOnButton(location)) {
			if (mouseState == MouseState.DOWN) {
				sendEvent(SWT.Selection, new Event());
				internalSelect();
			}
			newState = MouseState.HOVER;
		} else {
			newState = MouseState.IDLE;
		}

		if (mouseState != newState) {
			mouseState = newState;
			return true;
		} else {
			return false;
		}
	}

	private void internalSelect() {
		switch (this.style) {
		case SWT.CHECK -> isSelected = !isSelected;
		case SWT.RADIO -> {
			isSelected = true;
			parent.radioItemSelected(this);
		}
		}
	}

	public boolean isSeparator() {
		return (style & SWT.SEPARATOR) == SWT.SEPARATOR;
	}

	public boolean isSelected() {
		return isSelected;
	}

	void internalUnselect() {
		if (isSelected) {
			isSelected = false;
			sendEvent(SWT.Selection, new Event());
		}
	}
}
