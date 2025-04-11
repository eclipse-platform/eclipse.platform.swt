package org.eclipse.swt.widgets;

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

public class List extends Scrollable {
	static final int INSET = 3;
	boolean addedUCC = false;

	private final java.util.List<String> items = new ArrayList<>();
	private java.util.List<Integer> selectedItems = new ArrayList<>();

	private int topIndex;
	private Integer lastSelectedItem = 0;
	private int lineHeight;

	private static final String DUMMY_ITEM_TEXT = "a";

	public List(Composite parent, int style) {
		super(parent, checkStyle(style));
		addListeners();
	}

	private void addListeners() {
		final Listener listener = event -> {
			switch (event.type) {
				case SWT.Paint -> paintControl(event);
				case SWT.MouseDown -> onMouseDown(event);
				case SWT.MouseUp -> onMouseUp(event);
				case SWT.MouseWheel -> onMouseWheel(event);
				case SWT.Resize -> onResize();
				case SWT.KeyUp -> onKeyReleased(event);
				case SWT.Dispose -> dispose();
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseWheel, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.Dispose, listener);

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addListener(SWT.Selection, this::onScrollBarChange);
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addListener(SWT.Selection, e -> {
				topIndex = verticalBar.getSelection();
				onScrollBarChange(e);
			});
		}
	}

	private void onResize() {
		updateScrollBarWithTextSize();
		redraw();
	}

	private void onMouseWheel(Event e) {
		if (verticalBar != null) {
			int scrollAmount = e.count > 0 ? -1 : 1;
			this.topIndex = Math.max(0,
					Math.min(this.topIndex + scrollAmount, this.items.size() - getVisibleLineCount()));
			redraw();
		}
	}

	private int getVisibleLineCount() {
		Rectangle clientArea = getClientArea();
		return (lineHeight > 0) ? clientArea.height / lineHeight : 0;
	}

	private void paintControl(Event event) {
		if (!isVisible()) {
			return;
		}

		Drawing.drawWithGC(this, event.gc, gc -> doPaint(gc));
	}

	private void doPaint(GC gc) {
		Rectangle r = getBounds();
		if (r.width == 0 && r.height == 0) {
			return;
		}

		final Rectangle clientArea = getClientArea();
		drawBackground(clientArea, gc);

		if ((style & SWT.BORDER) != 0) {
			int borderSize = getBorderWidth();
			clientArea.x += borderSize;
			clientArea.y += borderSize;
		}

		lineHeight = getLineHeight(gc);
		int x = clientArea.x;
		int y = clientArea.y;
		for (int i = topIndex; i < this.items.size(); i++) {
			drawTextLine(i, x, y, gc, clientArea);
			y += lineHeight;
		}
	}

	private void drawBackground(Rectangle clientArea, GC gc) {

	    if ((state & Widget.PARENT_BACKGROUND) == 0) {

		if (getBackground() == null) {
		    // white is the default color for lists
		    gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}
	    }

	    gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width - 1, clientArea.height - 1);

	    if ((style & SWT.BORDER) != 0 && isEnabled()) {
		Color foreground = gc.getForeground();
		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		gc.drawLine(clientArea.x, clientArea.y + clientArea.height - 1,
			clientArea.x + clientArea.x + clientArea.width - 1, clientArea.y + clientArea.height - 1);
		gc.setForeground(foreground);
	    }
	}

	private void drawTextLine(int lineNumber, int x, int y, GC gc, Rectangle clientArea) {
		String text = items.get(lineNumber);
		Point textExtent = gc.textExtent(text);

		int _x = calculateHorizontalAlignment(x, textExtent, clientArea);
		if (horizontalBar != null) {
			_x -= horizontalBar.getSelection();
		}

		if (isEnabled()) {
			if (selectedItems.contains(lineNumber)) {
				drawSelectedText(text, gc, _x, y);
			} else {
				gc.drawText(text, _x, y, true);
			}
		} else {
			Color foreground = gc.getForeground();
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.drawText(text, _x, y, true);
			gc.setForeground(foreground);
		}
	}

	private void drawSelectedText(String text, GC gc, int _x, int _y) {
		Color background = gc.getBackground();
		Color foreground = gc.getForeground();
		gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
		gc.drawText(text, _x, _y);
		gc.setForeground(foreground);
		gc.setBackground(background);
	}

	private int calculateHorizontalAlignment(int x, Point textExtent, Rectangle clientArea) {
		if ((style & SWT.CENTER) != 0) {
			return (clientArea.width - textExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			return clientArea.width - textExtent.x;
		}
		return x;
	}

	private void onKeyReleased(Event event) {
		boolean isShiftPressed = (event.stateMask & SWT.SHIFT) != 0;
		switch (event.keyCode) {
		case SWT.ARROW_DOWN -> navigateSelection(1, isShiftPressed);
		case SWT.ARROW_UP -> navigateSelection(-1, isShiftPressed);
		default -> {
		}
		}
		redraw();
	}

	private void navigateSelection(int direction, boolean extendSelection) {
		if (extendSelection) {
			extendSelectionRange(direction);
		} else {
			moveSelection(direction);
		}
	}

	private void extendSelectionRange(int offset) {
		int newIndex = calculateNewIndex(this.lastSelectedItem, offset);
		if (this.selectedItems.contains(newIndex)) {
			this.selectedItems.remove(Integer.valueOf(newIndex - offset));
		} else {
			this.selectedItems.add(newIndex);
		}
		this.lastSelectedItem = newIndex;
	}

	private void moveSelection(int offset) {
		if (this.selectedItems.size() == 1) {
			int currentIndex = selectedItems.iterator().next();
			selectedItems.clear();
			selectedItems.add(calculateNewIndex(currentIndex, offset));
		}
	}

	private int calculateNewIndex(int currentIndex, int offset) {
		int newIndex = currentIndex + offset;
		return Math.max(0, Math.min(newIndex, items.size() - 1));
	}

	private void onScrollBarChange(Event e) {
		redraw();
	}

	private void onMouseDown(Event e) {
	}

	private void onMouseUp(Event e) {
		if ((e.stateMask & SWT.BUTTON1) != 0) {
			toggleSelection(e);
		}
		redraw();
	}

	private void toggleSelection(Event e) {
		int clickedLine = getTextLocation(e.y);
		if ((e.stateMask & SWT.CTRL) != 0) {
			if (this.selectedItems.contains(clickedLine)) {
				this.selectedItems.remove(Integer.valueOf(clickedLine));
			} else {
				this.selectedItems.add(clickedLine);
				this.lastSelectedItem = clickedLine;
			}
		} else {
			this.selectedItems.clear();
			this.selectedItems.add(clickedLine);
			this.lastSelectedItem = clickedLine;
		}
		sendSelectionEvent(SWT.Selection);
	}

	private int getTextLocation(int selectedY) {
		final int itemCount = items.size();
		if (lineHeight < 1) {
			return itemCount - 1;
		}

		Rectangle clientArea = getClientArea();
		int y = Math.max(selectedY + clientArea.y, 0);
		if ((style & SWT.BORDER) != 0) {
			int borderSize = getBorderWidth();
			y -= borderSize;
		}

		int lineOnArea = Math.min(y / lineHeight, itemCount - 1);
		int lineOfText = lineOnArea + this.topIndex;
		return Math.min(lineOfText, itemCount - 1);
	}

	private int getLineHeight(GC gc) {
		checkWidget();
		return gc.getFontMetrics().getHeight();
	}

	public void add(String string) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);

		this.items.add(string);
		updateScrollBarWithTextSize();
		redraw();
	}

	public int getLineHeight() {
		checkWidget();
		int height = lineHeight;
		if (height == 0) {
			GC gc = new GC(this);
			height = getLineHeight(gc);
			gc.dispose();
		}
		return height;
	}

	private Point computeTextSize() {
		GC gc = new GC(this);
		gc.setFont(getFont());
		int width = 0;
		Point size = null;
		for (String line : this.items) {
			size = gc.textExtent(line);
			width = Math.max(width, size.x);
		}
		gc.dispose();
		int height = size != null ? size.y * this.items.size() : 0;
		if (horizontalBar != null) {
			height += horizontalBar.getSize().y;
		}
		if (verticalBar != null) {
			width += verticalBar.getSize().x;
		}
		return new Point(width, height);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point size = computeTextSize();
		if ((style & SWT.BORDER) != 0) {
			final int borderWidth = getBorderWidth();
			size.x += 2 * borderWidth + INSET;
			size.y += 2 * borderWidth;
		}
		return size;
	}

	private void updateScrollBarWithTextSize() {
		Rectangle clientArea = getClientArea();
		Point maxTextSize = computeTextSize();

		if (verticalBar != null) {
			int thumb = clientArea.height / getLineHeight();
			verticalBar.setMaximum(this.items.size() - 1);
			verticalBar.setMinimum(0);
			verticalBar.setThumb(thumb);
			verticalBar.setVisible(maxTextSize.y > clientArea.height);
		}

		if (horizontalBar != null) {
			horizontalBar.setMaximum(maxTextSize.x);
			horizontalBar.setMinimum(0);
			horizontalBar.setThumb(clientArea.width / maxTextSize.x);
			horizontalBar.setVisible(maxTextSize.x > clientArea.width);
		}
	}

	public void add(String string, int index) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (index < 0 || index > this.items.size())
			error(SWT.ERROR_INVALID_RANGE);
		this.items.add(index, string);
		updateScrollBarWithTextSize();
		redraw();
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
	}

	public void deselect(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		for (int index : indices) {
			deselect(index);
		}
	}

	public void deselect(int index) {
		checkWidget();
		if (index != -1) {
			this.selectedItems.remove(Integer.valueOf(index));
		}
	}

	public void deselect(int start, int end) {
		checkWidget();
		if (start >= 0 && end >= 0 && start <= end && start < items.size()) {
			for (int i = start; i <= end; i++) {
				deselect(i);
			}
		}
	}

	public void deselectAll() {
		checkWidget();
		this.selectedItems.clear();
	}

	public int getFocusIndex() {
		checkWidget();
		if (this.items.isEmpty()) {
			return -1;
		}
		if (this.selectedItems.isEmpty()) {
			return -1;
		}
		return this.selectedItems.get(0);
	}

	public String getItem(int index) {
		checkWidget();
		if (index < 0 || index >= this.items.size())
			error(SWT.ERROR_INVALID_RANGE);
		return this.items.get(index);
	}

	public int getItemCount() {
		checkWidget();
		return this.items.size();
	}

	public int getItemHeight() {
		checkWidget();
		return DPIUtil.autoScaleDown(getItemHeightInPixels());
	}

	public int getItemHeightInPixels() {
	    checkWidget();
	    String referenceText = this.items.isEmpty() ? DUMMY_ITEM_TEXT : this.items.get(0);
	    GC gc = new GC(this);
	    gc.setFont(getFont());
	    int itemHeight = gc.textExtent(referenceText).y;
	    gc.dispose();
	    if (itemHeight <= 0) error(SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	    return itemHeight;
	}

	public String[] getItems() {
		checkWidget();
		int count = getItemCount();
		String[] result = new String[count];
		for (int i = 0; i < count; i++)
			result[i] = getItem(i);
		return result;
	}

	public String[] getSelection() {
		checkWidget();
		int[] indices = getSelectionIndices();
		String[] result = new String[indices.length];
		for (int i = 0; i < indices.length; i++) {
			result[i] = getItem(indices[i]);
		}
		return result;
	}

	public int getSelectionCount() {
		checkWidget();
		return this.selectedItems.size();
	}

	public int getSelectionIndex() {
		checkWidget();
		return (getSelectionIndices().length > 0) ? getSelectionIndices()[0] : -1;
	}

	public int[] getSelectionIndices() {
		checkWidget();
		return this.selectedItems.stream().mapToInt(Integer::intValue).toArray();
	}

	public int getTopIndex() {
		checkWidget();
		return (int) this.topIndex;
	}

	public int indexOf(String string) {
		return indexOf(string, 0);
	}

	public int indexOf(String string, int start) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		int count = getItemCount();
		if (!(0 <= start && start < count)) return -1;
		for (int i = start; i < count; i++)
			if (string.equals(getItem(i)))
				return i;
		return -1;
	}

	public boolean isSelected(int index) {
		checkWidget();
		return selectedItems.contains(index);
	}

	public void remove(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (indices.length == 0) return;
		for (int index : indices)
			if (index < 0 || index >= this.items.size())
				error(SWT.ERROR_INVALID_RANGE);

		// Sort indices in descending order and remove duplicates
	    int[] uniqueSortedIndices = IntStream.of(indices).distinct().boxed()
	        .sorted((a, b) -> Integer.compare(b, a)).mapToInt(Integer::intValue).toArray();

	    for (int index : uniqueSortedIndices) this.items.remove(index);
		redraw();
	}

	public void remove(int index) {
		checkWidget();
		if (index < 0 || index >= this.items.size())
			error(SWT.ERROR_INVALID_ARGUMENT);
		this.items.remove(index);
		redraw();
	}

	public void remove(int start, int end) {
		checkWidget();
		if (start > end) return;

		if (start < 0 || end >= this.items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
			return;
		}
		if (start == 0 && end == this.items.size() - 1) {
			removeAll();
			return;
		}
		for (int i = end; i >= start; i--) {
			remove(i);
		}
		redraw();
	}

	public void remove(String string) {
	    checkWidget();
	    if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	    int index = indexOf(string, 0);
	    if (index == -1) error(SWT.ERROR_INVALID_ARGUMENT);
	    remove(index);
	}

	public void removeAll() {
		checkWidget();
		this.items.clear();
		redraw();
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	public void select(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		int length = indices.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
			return;
		}
		select(indices, false);
	}

	void select(int[] indices, boolean scroll) {
		int i = 0;
		while (i < indices.length) {
			int index = indices[i];
			if (index != -1) {
				select(index, false);
			}
			i++;
		}
		if (scroll) {
			showSelection();
		}
	}

	public void select(int index) {
		checkWidget();
		select(index, false);
	}

	void select(int index, boolean scroll) {
	    if (index < 0 || index >= this.items.size()) return;

	    if ((this.style & SWT.SINGLE) != 0) {
	        this.selectedItems.clear();
	        this.selectedItems.add(index);
	    } else {
	        if (!this.selectedItems.contains(index)) {
	            int insertPos = 0;
	            while (insertPos < this.selectedItems.size() && this.selectedItems.get(insertPos) < index) insertPos++;
	            this.selectedItems.add(insertPos, index);
	        }
	    }

	    this.lastSelectedItem = index;
	    redraw();
	}

	public void select(int start, int end) {
		checkWidget();
		if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) {
			return;
		}
		int count = this.items.size();
		if (count == 0 || start >= count) {
			return;
		}
		start = Math.max(0, start);
		end = Math.min(end, count - 1);
		if ((style & SWT.SINGLE) != 0) {
			select(start, false);
		} else {
			select(start, end, false);
		}
	}

	void select(int start, int end, boolean scroll) {
		if (start == end) {
			select(start, scroll);
			return;
		}
		for (int i = start; i <= end; i++) {
			select(i, scroll);
		}

		if (scroll) {
			showSelection();
		}
	}

	public void selectAll() {
		if ((style & SWT.SINGLE) != 0)
			return;
		this.selectedItems.clear();
		for (int i = 0; i < this.items.size(); i++) {
			this.selectedItems.add(i);
			this.lastSelectedItem = i;
		}
	}

	void setFocusIndex(int index) {
		checkWidget();
		if (index < 0 || index >= this.items.size()) {
			return;
		}

		selectedItems.clear();
		selectedItems.add(index);
		lastSelectedItem = index;
		redraw();
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		super.setFont(font);
		if ((style & SWT.H_SCROLL) != 0) {
			setScrollWidth();
		}
	}

	public void setItem(int index, String string) {
		checkWidget();
		if (index < 0 || index >= this.items.size())
			error(SWT.ERROR_INVALID_ARGUMENT);
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		this.items.set(index, string);
		redraw();
	}

	public void setItems(String... items) {
		checkWidget();
		if (items == null) error(SWT.ERROR_NULL_ARGUMENT);
		for (String item : items) {
			if (item == null) error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.items.clear();
		this.items.addAll(Arrays.asList(items));
		redraw();
	}

	private int getTextWidth(String text) {
		GC gc = new GC(this);
		int width = gc.textExtent(text).x;
		gc.dispose();
		return width;
	}

	/**
	 * Calculates the scroll width depending on the item with the highest width
	 */
	void setScrollWidth() {
		int newWidth = 0;
		for (String line : this.items) {
			newWidth = Math.max(newWidth, getTextWidth(line));
		}
		if (horizontalBar != null) {
			horizontalBar.setMaximum(newWidth + INSET);
		}
	}

	void setScrollWidth(char[] buffer, boolean grow) {
		GC gc = new GC(this);
		gc.setFont(getFont());
		Point textExtent = gc.textExtent(new String(buffer));
		gc.dispose();

		setScrollWidth(textExtent.x, grow);
	}

	void setScrollWidth(int newWidth, boolean grow) {
		newWidth += INSET;
		int width = getCurrentScrollWidth();
		if (grow) {
			if (newWidth <= width) {
				return;
			}
			if (horizontalBar != null) {
				horizontalBar.setMaximum(newWidth);
			}
		} else {
			if (newWidth < width) {
				return;
			}
			setScrollWidth();
		}
	}

	private int getCurrentScrollWidth() {
		if (getHorizontalBar() != null) {
			return getHorizontalBar().getMaximum();
		}
		return 0;
	}

	public void setSelection(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		deselectAll();
		int length = indices.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1))
			return;
		select(indices, true);
	}

	public void setSelection(String[] items) {
		checkWidget();
		if (items == null) error(SWT.ERROR_NULL_ARGUMENT);
		deselectAll();
		int length = items.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1))
			return;
		for (int i = 0; i < this.items.size(); i++) {
			for (String item : items) {
				if (this.items.get(i).equals(item)) {
					select(i);
					if ((style & SWT.SINGLE) != 0)
						return;
					break;
				}
			}
		}
	}

	public void setSelection(int index) {
		checkWidget();
		deselectAll();
		select(index, true);
	}

	public void setSelection(int start, int end) {
	    checkWidget();
	    deselectAll();
	    if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	    int count = this.items.size();
	    if (count == 0 || start >= count) return;
	    start = Math.max(0, start);
	    end = Math.min(end, count - 1);
	    select(start, end, true);
	}

	public void setTopIndex(int index) {
	    checkWidget();
	    if (index < 0 || index > this.items.size()) return;
	    this.topIndex = index;
	}

	public void showSelection() {
		if (this.selectedItems.isEmpty() || this.items.isEmpty()) {
			return;
		}

		int selectedIndex = this.selectedItems.get(0);

		if (selectedIndex < 0 || selectedIndex >= this.items.size()) {
			return;
		}

		Rectangle clientArea = getClientArea();
		int lineHeight = getLineHeight();

		int visibleStartIndex = this.topIndex;
		int visibleEndIndex = Math.min(this.topIndex + clientArea.height / lineHeight, this.items.size() - 1);

		if (selectedIndex >= visibleStartIndex && selectedIndex <= visibleEndIndex) {
			return;
		}

		int centerOffset = (clientArea.height / lineHeight) / 2;
		this.topIndex = Math.max(0,
				Math.min(selectedIndex - centerOffset, this.items.size() - clientArea.height / lineHeight));

		redraw();
	}
}
