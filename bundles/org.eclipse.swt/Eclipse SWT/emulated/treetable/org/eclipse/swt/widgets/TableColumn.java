package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
/**
 * Instances of this class represent a column in a table widget.
 *  <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TableColumn extends Item {
	static final int FIRST = 0;							// index of the first column
	static final int FILL = -1;							// index that identifies the column used to 
														// fill space not used by other columns.
	private static final int DEFAULT_WIDTH = 10;

	private Table parent;
	private int index;									// 0-based column index
	private Rectangle bounds = new Rectangle(0, 0, 0, 0);
	private boolean isDefaultWidth = true;
	private boolean resize = true;

/**
 * Create a new TableColumn without adding it to the parent.
 * Currently used to create fill columns and default columns.
 * @see createFillColumn
 * @see createDefaultColumn
 * @param parent - Table widget the new instance will be a child of.
 */
TableColumn(Table parent) {
	super(parent, SWT.NULL);
	this.parent = parent;		
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
public TableColumn(Table parent, int style) {
	this(parent, style, checkNull(parent).getColumnCount());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
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
public TableColumn(Table parent, int style, int index) {
	super(parent, checkStyle (style), index);
	
	this.parent = parent;
	if (index < 0 || index > parent.getColumnCount()) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	setIndex(index);	
	parent.addColumn(this);
	setWidth(DEFAULT_WIDTH);
	setDefaultWidth(true);
	addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event event) {disposeColumn();}
	});
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
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}
/**
 * Throw an SWT.ERROR_NULL_ARGUMENT exception if 'table' is null.
 * Otherwise return 'table'
 */
static Table checkNull(Table table) {
	if (table == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return table;
}
static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
 * Create a new instance of TableColumn that acts as a default column
 * if the user does not create a TableColumn.
 * @param parent - Table widget the new instance will be a child of.
 */
static TableColumn createDefaultColumn(Table parent) {
	TableColumn defaultColumn = new TableColumn(parent);
	
	defaultColumn.setIndex(FIRST);
	defaultColumn.setWidth(DEFAULT_WIDTH);
	defaultColumn.setDefaultWidth(true);
	return defaultColumn;
}
/**
 * Create a new instance of TableColumn that acts as the rightmost 
 * fill column in a Table. The new object is not added to the parent
 * like a regular column is.
 * @param parent - Table widget the new instance will be a child of.
 */
static TableColumn createFillColumn(Table parent) {
	TableColumn fillColumn = new TableColumn(parent);
	
	fillColumn.setIndex(FILL);
	return fillColumn;
}
/**
 * Remove the receiver from its parent
 */
void disposeColumn() {
	getParent().removeColumn(this);
}
/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Answer the bounding rectangle of the receiver.
 */
Rectangle getBounds() {
	return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);	// copy the object to prevent changes
}
public Display getDisplay() {
	if (parent == null) {		// access parent field directly to prevent endless recursion
		error(SWT.ERROR_WIDGET_DISPOSED);
	}
	return parent.getDisplay();
}
/**
 * Answer the index of the receiver. Specifies the position of the
 * receiver relative to other columns in the parent.
 */
int getIndex() {
	return index;
}
/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent() {
	checkWidget();
	return parent;
}
/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable() {
	checkWidget();
	return resize;
}
/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	return getBounds().width;
}
/**
 * Set the colun bounds.
 */
void internalSetBounds(Rectangle newBounds) {
	bounds = newBounds;
}
/**
 * Answer whether the column has a default width or if a width has been 
 * set by the user.
 * @return 
 *  true=column width is a default width set internally
 *	false=column width has been set by the user.
 */
boolean isDefaultWidth() {
	return isDefaultWidth;
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
 */
public void pack() {
	checkWidget();
	Table parent = getParent();
	int index = parent.indexOf(this);

	if (getIndex() != TableColumn.FILL && index != -1) {
		setWidth(parent.getPreferredColumnWidth(index));
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * be notified when the control is selected.
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
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);
}
/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment(int alignment) {
	checkWidget();
	int index = getIndex();
	
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) != 0 && index != 0) { // ignore calls for the first column to match Windows behavior
		style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		getParent().getHeader().redraw(index);	
	}
}
/**
 * Set the bounding rectangle of the receiver to 'newBounds'.
 * Notify the table widget if the column width changes.
 * @param newBounds - the new bounding rectangle of the receiver,
 *	consisting of x, y, width, height
 */
void setBounds(Rectangle newBounds) {
	if (newBounds.width != bounds.width) {
		if (isDefaultWidth() == true) {
			setDefaultWidth(false);
		}
		getParent().columnChange(this, newBounds);
	}
	else {
		// columnChange causes update (via scroll) which may flush redraw 
		// based on old bounds. Setting bounds after notifying table fixes 1GABZR5
		// Table sets column bounds at appropriate time when called above with 
		// width change. Only set bounds when table was not called. Fixes 1GCGDPB
		bounds = newBounds;
	}
}
/**
 * Set whether the column has a default width or if a width has been 
 * set by the user.
 * @param isDefaultWidth
 *	true=column width is a default width set internally
 *	false=column width has been set by the user
 */
void setDefaultWidth(boolean isDefaultWidth) {
	this.isDefaultWidth = isDefaultWidth;
}
public void setImage(Image image) {
	super.setImage(image);
	Header header = parent.getHeader();
	header.setHeaderHeight();
	header.redraw();
	parent.redraw();
}
/**
 * Set the index of the receiver to 'newIndex'. The index specifies the
 * position of the receiver relative to other columns in the parent.
 */
void setIndex(int newIndex) {
	this.index = newIndex;
}
/**
 * Sets the resizable attribute.  A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable(boolean resize) {
	checkWidget();
	this.resize = resize;
}
public void setText(String newText) {
	checkWidget();
	int index = getIndex();
	
	if (newText == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (index != FILL && (text == null || text.equals(newText) == false)) {
		super.setText(newText);
		getParent().getHeader().redraw(index);
	}	
}
/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth(int width) {
	checkWidget();
	Rectangle bounds = getBounds();
	int oldWidth = bounds.width;
	int redrawX;

	if (width != oldWidth) {
		redrawX = bounds.x;
		bounds.width = width;
		setBounds(bounds);
		// redraw at old column position if column was resized wider.
		// fixes focus rectangle. 
		redrawX += Math.min(width, oldWidth);
		parent.redraw(																
			redrawX - 2, 0, 
			2, parent.getClientArea().height, false);	// redraw 2 pixels wide to redraw item focus rectangle and grid line
	}
}
}
