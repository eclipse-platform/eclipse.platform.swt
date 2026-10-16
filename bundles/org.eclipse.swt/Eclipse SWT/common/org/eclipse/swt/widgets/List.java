package org.eclipse.swt.widgets;

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

public class List extends NativeBasedCustomScrollable {
	static final int INSET = 3;

	private final java.util.List<String> items = new ArrayList<>();
	private final PackedListSelection selectedItems = new PackedListSelection();

	private int topIndex;
	private int lastSelectedItem = 0;
	private boolean leftMousePressed;
	private int leftMouseDownStartSelection = -1;
	private final ListRenderer renderer;
	private long contentVersion;
	private long tailAppendVersion = Long.MIN_VALUE;

	private static final String DUMMY_ITEM_TEXT = "a";

	public List(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.renderer = new DefaultListRenderer(this);
		addListeners();
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

    /** Parent-owned change signals; they do not allocate per-item measurement objects. */
	long contentGeneration() { return contentVersion; }
	long lastAppendGeneration() { return tailAppendVersion; }

	private void contentChanged(boolean tailAppend) {
		if (contentVersion == Long.MAX_VALUE) {
			contentVersion = 0L;
			tailAppendVersion = Long.MIN_VALUE;
			if (renderer instanceof DefaultListRenderer concrete) concrete.invalidateMetrics();
		} else {
			contentVersion++;
			tailAppendVersion = tailAppend ? contentVersion : Long.MIN_VALUE;
		}
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
			case SWT.Activate -> updateScrollBarWithTextSize();
			case SWT.MouseMove -> onMouseMove(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseWheel, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.Dispose, listener);
		addListener(SWT.Activate, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.Resize, listener);

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addListener(SWT.Selection, e -> redraw());
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addListener(SWT.Selection, e -> {
				topIndex = verticalBar.getSelection();
				redraw();
			});
		}
	}

	private void onMouseMove(Event event) {
		if (this.leftMousePressed) {
			toggleSelection(event);
		}
	}

	private void onResize() {
		if (renderer instanceof DefaultListRenderer concrete) concrete.invalidateMetrics();
		updateScrollBarWithTextSize();
		redraw();
	}

	private void onMouseWheel(Event e) {
		if (verticalBar == null || e.count == 0) return;
		long candidate = (long) topIndex + (e.count > 0 ? -1L : 1L);
		setTopIndex((int) Math.min((long) items.size(), Math.max(0L, candidate)));
	}

	@Override
	public void redraw() {
		super.redraw();
	}

	private int getVisibleLineCount() {
		Rectangle clientArea = getClientArea();
		return (getLineHeight() > 0) ? clientArea.height / getLineHeight() : 0;
	}

	private void paintControl(Event event) {
		if (!isVisible()) {
			return;
		}

		Drawing.drawWithGC(this, event.gc, gc -> renderer.paint(gc));
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
		if (this.selectedItems.has(newIndex)) {
			this.selectedItems.removeValue(newIndex - offset);
		} else {
			this.selectedItems.addInt(newIndex);
		}
		this.lastSelectedItem = newIndex;
	}

	private void moveSelection(int offset) {
		if (this.selectedItems.size() == 1) {
			int currentIndex = selectedItems.getInt(0);
			selectedItems.clear();
			selectedItems.addInt(calculateNewIndex(currentIndex, offset));
		}
	}

	private int calculateNewIndex(int currentIndex, int offset) {
		int newIndex = currentIndex + offset;
		return Math.max(0, Math.min(newIndex, items.size() - 1));
	}

	private void onMouseDown(Event e) {
		if (e.button == 1) {
			this.leftMousePressed = true;
			this.leftMouseDownStartSelection = getTextLocation(e.y);

			toggleSelection(e);
		}
		redraw();
	}

	private void onMouseUp(Event e) {
		if (e.button == 1) {
			this.leftMousePressed = false;
			this.leftMouseDownStartSelection = -1;
		}
	}

	private void toggleSelection(Event e) {
		int clickedLine = getTextLocation(e.y);

		if (e.type == SWT.MouseMove && this.leftMousePressed && (style & SWT.MULTI) != 0) {
			// Preserve the original ordered (including reverse) drag selection.
			// A logical range is represented without one Integer per selected row.
			if (this.selectedItems.sameRange(leftMouseDownStartSelection, clickedLine)) {
				return;
			}
			this.selectedItems.selectRange(leftMouseDownStartSelection, clickedLine);
		} else if (e.type == SWT.MouseMove && this.leftMousePressed && (style & SWT.MULTI) == 0) {
			if (this.selectedItems.has(clickedLine)) {
				return;
			}

			this.selectedItems.clear();
			this.selectedItems.addInt(clickedLine);
		} else {
			if ((e.stateMask & SWT.CTRL) != 0) {
				if (this.selectedItems.has(clickedLine)) {
					// remove the object not the index.
					this.selectedItems.removeValue(clickedLine);
				} else {
					this.selectedItems.addInt(clickedLine);
					this.lastSelectedItem = clickedLine;
				}
			} else {
				this.selectedItems.clear();
				this.selectedItems.addInt(clickedLine);
				this.lastSelectedItem = clickedLine;
			}
		}
		sendSelectionEvent(SWT.Selection);
		redraw();
	}

	private int getTextLocation(int selectedY) {
		final int itemCount = items.size();
		if (getLineHeight() < 1) {
			return itemCount - 1;
		}

		Rectangle clientArea = getClientArea();
		int y = Math.max(selectedY + clientArea.y, 0);
		if ((style & SWT.BORDER) != 0) {
			int borderSize = getBorderWidth();
			y -= borderSize;
		}

		int lineOnArea = Math.min(y / getLineHeight(), itemCount - 1);
		int lineOfText = lineOnArea + this.topIndex;
		return Math.min(lineOfText, itemCount - 1);
	}

	public void add(String string) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);

		this.items.add(string);
		contentChanged(true);
		updateScrollBarWithTextSize();
		redraw();
	}

	public int getLineHeight() {
		checkWidget();
		return renderer.getLineHeight();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point defaultSize = renderer.computeDefaultSize();
		int width = wHint == SWT.DEFAULT ? defaultSize.x : wHint;
		int height = hHint == SWT.DEFAULT ? defaultSize.y : hHint;

		return new Point(width, height);
	}

	private void updateScrollBarWithTextSize() {
		Rectangle clientArea = getClientArea();

		if (clientArea.width == 0 || clientArea.height == 0) {
			return;
		}

		Point maxTextSize = renderer instanceof DefaultListRenderer concrete
				? concrete.computeScrollSize() : renderer.computeTextSize();

		if (verticalBar != null) {
			int thumb = clientArea.height / getLineHeight();
			verticalBar.setThumb(thumb);
			verticalBar.setMaximum(this.items.size());
			verticalBar.setMinimum(0);
			verticalBar.setVisible(maxTextSize.y > clientArea.height);
			verticalBar.setIncrement(1);
			verticalBar.setPageIncrement(thumb);
		}

		if (horizontalBar != null) {
			horizontalBar.setMaximum(maxTextSize.x);
			horizontalBar.setMinimum(0);
			horizontalBar.setThumb(clientArea.width);
			horizontalBar.setVisible(maxTextSize.x > clientArea.width);
		}
	}

	public void add(String string, int index) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (index < 0 || index > this.items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
		boolean atTail = index == this.items.size();
		this.items.add(index, string);
		contentChanged(atTail);
		updateScrollBarWithTextSize();
		redraw();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
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
			this.selectedItems.removeValue(index);
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
		return this.selectedItems.getInt(0);
	}

	public String getItem(int index) {
		checkWidget();
		if (index < 0 || index >= this.items.size()) {
			error(SWT.ERROR_INVALID_RANGE);
		}
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
		Point size = Drawing.measure(this, gc -> {
			gc.setFont(getFont());
			return gc.textExtent(referenceText);
		});
		int itemHeight = size.y;
		if (itemHeight <= 0) error(SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
		return itemHeight;
	}

	public String[] getItems() {
		checkWidget();
		int count = getItemCount();
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = getItem(i);
		}
		return result;
	}

	public String[] getSelection() {
		checkWidget();
		// Only the API-required String[] is materialized. Selection remains parent-owned.
		int count = selectedItems.size();
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = getItem(selectedItems.getInt(i));
		}
		return result;
	}

	public int getSelectionCount() {
		checkWidget();
		return this.selectedItems.size();
	}

	public int getSelectionIndex() {
		checkWidget();
		// Scalar query must not allocate an O(selected-row-count) snapshot.
		return selectedItems.isEmpty() ? -1 : selectedItems.getInt(0);
	}

	public int[] getSelectionIndices() {
		checkWidget();
		return this.selectedItems.selectedIndices();
	}

	public int getTopIndex() {
		checkWidget();
		return this.topIndex;
	}

	public int indexOf(String string) {
		return indexOf(string, 0);
	}

	public int indexOf(String string, int start) {
		checkWidget();
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		int count = getItemCount();
		if (!(0 <= start && start < count)) return -1;
		for (int i = start; i < count; i++) {
			if (string.equals(getItem(i))) {
				return i;
			}
		}
		return -1;
	}

	public boolean isSelected(int index) {
		checkWidget();
		return selectedItems.has(index);
	}

	public void remove(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (indices.length == 0) return;
		for (int index : indices) {
			if (index < 0 || index >= this.items.size()) {
				error(SWT.ERROR_INVALID_RANGE);
			}
		}

		// Sort indices in descending order and remove duplicates
		int[] uniqueSortedIndices = IntStream.of(indices).distinct().boxed()
				.sorted((a, b) -> Integer.compare(b, a)).mapToInt(Integer::intValue).toArray();

		for (int index : uniqueSortedIndices) {
			this.items.remove(index);
		}
		contentChanged(false);
		redraw();
	}

	public void remove(int index) {
		checkWidget();
		if (index < 0 || index >= this.items.size()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.items.remove(index);
		contentChanged(false);
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
		contentChanged(false);
		this.selectedItems.clear();
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
			this.selectedItems.addInt(index);
		} else {
			if (!this.selectedItems.has(index)) {
				int insertPos = 0;
				while (insertPos < this.selectedItems.size() && this.selectedItems.getInt(insertPos) < index) insertPos++;
				this.selectedItems.insertInt(insertPos, index);
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
		if ((style & SWT.SINGLE) != 0) {
			return;
		}
		// Selection is a parent-owned ordered span, not millions of Integer objects.
		this.selectedItems.selectAll(this.items.size());
		if (!this.items.isEmpty()) this.lastSelectedItem = this.items.size() - 1;
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		super.setFont(font);
		if (renderer instanceof DefaultListRenderer defaultRenderer) {
			defaultRenderer.invalidateMetrics();
		}
		updateScrollBarWithTextSize();
		redraw();
	}

	public void setItem(int index, String string) {
		checkWidget();
		if (index < 0 || index >= this.items.size()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
		this.items.set(index, string);
		contentChanged(false);
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
		contentChanged(false);
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

	public void setSelection(int[] indices) {
		checkWidget();
		if (indices == null) error(SWT.ERROR_NULL_ARGUMENT);
		deselectAll();
		int length = indices.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
			return;
		}
		select(indices, true);
	}

	public void setSelection(String[] items) {
		checkWidget();
		if (items == null) error(SWT.ERROR_NULL_ARGUMENT);
		deselectAll();
		int length = items.length;
		if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
			return;
		}
		for (int i = 0; i < this.items.size(); i++) {
			for (String item : items) {
				if (this.items.get(i).equals(item)) {
					select(i);
					if ((style & SWT.SINGLE) != 0) {
						return;
					}
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
		int next = Math.min(index,
				Math.max(0, this.items.size() - Math.max(1, getVisibleLineCount())));
		if (this.topIndex == next) return;
		this.topIndex = next;
		if (verticalBar != null) verticalBar.setSelection(next);
		redraw();
	}

	public void showSelection() {
		if (this.selectedItems.isEmpty() || this.items.isEmpty()) {
			return;
		}

		int selectedIndex = this.selectedItems.getInt(0);

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
		setTopIndex(Math.max(0,
				Math.min(selectedIndex - centerOffset, this.items.size() - clientArea.height / lineHeight)));
	}

	public int getState() {
		return state;
	}
}
