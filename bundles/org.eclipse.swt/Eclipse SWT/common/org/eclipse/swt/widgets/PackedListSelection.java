/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.AbstractList;
import java.util.Arrays;

/**
 * Parent-owned selection of integer row coordinates for List. The public List API
 * exposes primitive int[] coordinates; individual selected rows are not widgets.
 * The ordinary case stores an ordered int[] without resident Integer boxes.
 * Full selection and drag ranges use one span and sparse omitted coordinates,
 * with the original enumeration order preserved even for reverse mouse drags.
 * A non-span insertion materializes that exact order before modifying it.
 *
 * <p>Not a new widget, viewport, event owner, or reference-count authority.</p>
 */
final class PackedListSelection extends AbstractList<Integer> {
    private static final int[] EMPTY = new int[0];
    private static final int MAX_ARRAY = Integer.MAX_VALUE - 8;
    private static final int RETAIN_WHEN_EMPTY = 4096;
    private int[] ids = EMPTY;
    private int count;
    private boolean spanMode;
    private int first, step, spanLength;
    /** Sorted ascending by logical coordinate, independent of span direction. */
    private int[] omitted = EMPTY;
    private int omittedCount;

    /** Constant-residency full selection; enumeration remains ascending. */
    void selectAll(int logicalCount) {
        if (logicalCount < 0) throw new IllegalArgumentException("negative logical count");
        span(0, 1, logicalCount);
    }

    /** Exactly reproduces the mouse-drag selection order without a boxed range list. */
    void selectRange(int anchor, int target) {
        long length = Math.abs((long) target - anchor) + 1L;
        if (length > MAX_ARRAY) throw new IllegalArgumentException("range exceeds array limit");
        span(anchor, target < anchor ? -1 : 1, (int) length);
    }

    /** Checks ordered equality as used by the widget's no-op mouse-move path. */
    boolean sameRange(int anchor, int target) {
        long length = Math.abs((long) target - anchor) + 1L;
        if (length != count) return false;
        int direction = target < anchor ? -1 : 1;
        if (spanMode && omittedCount == 0) {
            return first == anchor && step == direction;
        }
        for (int n = 0; n < count; n++) {
            if (getInt(n) != (int) ((long) anchor + (long) n * direction)) return false;
        }
        return true;
    }

    int[] selectedIndices() {
        int[] result = new int[count];
        if (!spanMode) {
            System.arraycopy(ids, 0, result, 0, count);
            return result;
        }
        // A requested public snapshot visits each logical row just once.
        // Its allocation is contract-mandated; cache residency is not.
        int omission = step > 0 ? 0 : omittedCount - 1;
        int write = 0;
        for (int ordinal = 0; ordinal < spanLength; ordinal++) {
            int row = at(ordinal);
            if (omittedCount > 0 && omission >= 0 && omission < omittedCount
                    && omitted[omission] == row) {
                omission += step;
            } else {
                result[write++] = row;
            }
        }
        if (write != count) throw new IllegalStateException("selection accounting drift");
        return result;
    }

    @Override public int size() { return count; }

    @Override public Integer get(int index) { return getInt(index); }

    int getInt(int index) {
        check(index);
        if (!spanMode) return ids[index];
        if (omittedCount == 0) return at(index);
        // The selected-rank predicate is monotonic. Binary search across the
        // original span avoids scanning millions of rows for sparse exclusions.
        int low = index, high = (int) Math.min((long) spanLength - 1L,
                (long) index + omittedCount);
        while (low < high) {
            int mid = low + ((high - low) >>> 1);
            int included = mid + 1 - omittedThrough(mid);
            if (included > index) high = mid;
            else low = mid + 1;
        }
        return at(low);
    }

    /** Primitive event-path lookup; no Integer object for mouse/key events. */
    boolean has(int row) {
        if (!spanMode) {
            for (int n = 0; n < count; n++) if (ids[n] == row) return true;
            return false;
        }
        long offset = step == 1 ? (long) row - first : (long) first - row;
        return offset >= 0 && offset < spanLength
                && Arrays.binarySearch(omitted, 0, omittedCount, row) < 0;
    }

    @Override public boolean contains(Object value) {
        return value instanceof Integer boxed && has(boxed);
    }

    /** Primitive append, preserving insertion order, including duplicates. */
    void addInt(int row) { insertInt(count, row); }

    /** Reinsert a sparse exclusion directly when its original ordinal is restored. */
    void insertInt(int index, int row) {
        if (index < 0 || index > count) throw new IndexOutOfBoundsException(index);
        if (count == MAX_ARRAY) throw new IllegalStateException("selection capacity exhausted");
        if (spanMode && restoreExcluded(index, row)) return;
        materialize();
        ensureIds(count + 1);
        System.arraycopy(ids, index, ids, index + 1, count - index);
        ids[index] = row;
        count++;
        modCount++;
    }

    @Override public boolean add(Integer value) {
        addInt(java.util.Objects.requireNonNull(value, "row"));
        return true;
    }

    @Override public void add(int index, Integer value) {
        insertInt(index, java.util.Objects.requireNonNull(value, "row"));
    }

    private boolean restoreExcluded(int index, int row) {
        int location = lowerBound(row);
        if (location >= omittedCount || omitted[location] != row) return false;
        long ordinal = step == 1 ? (long) row - first : (long) first - row;
        if (ordinal < 0 || ordinal >= spanLength) return false;
        int excludedBefore = step == 1 ? location : omittedCount - location - 1;
        if (ordinal - excludedBefore != index) return false;
        System.arraycopy(omitted, location + 1, omitted, location,
                omittedCount - location - 1);
        omitted[--omittedCount] = 0;
        count++;
        modCount++;
        return true;
    }

    /** Primitive first-matching removal. */
    boolean removeValue(int row) {
        if (spanMode) {
            if (!has(row)) return false;
            exclude(row);
            return true;
        }
        for (int i = 0; i < count; i++) {
            if (ids[i] == row) { removeAt(i); return true; }
        }
        return false;
    }

    @Override public boolean remove(Object value) {
        return value instanceof Integer boxed && removeValue(boxed);
    }

    @Override public Integer remove(int index) {
        int value = getInt(index);
        if (spanMode) exclude(value);
        else removeAt(index);
        return value;
    }

    @Override public void clear() {
        count = 0;
        spanMode = false;
        spanLength = 0;
        omittedCount = 0;
        if (ids.length > RETAIN_WHEN_EMPTY) ids = EMPTY;
        if (omitted.length > RETAIN_WHEN_EMPTY) omitted = EMPTY;
        modCount++;
    }

    private void span(int start, int direction, int length) {
        if (length < 0 || (direction != 1 && direction != -1))
            throw new IllegalArgumentException("invalid selection span");
        first = start;
        step = direction;
        spanLength = length;
        count = length;
        omittedCount = 0;
        spanMode = true;
        if (ids.length > RETAIN_WHEN_EMPTY) ids = EMPTY;
        if (omitted.length > RETAIN_WHEN_EMPTY) omitted = EMPTY;
        modCount++;
    }

    private int at(int ordinal) {
        return (int) ((long) first + (long) ordinal * step);
    }

    private int omittedThrough(int ordinal) {
        int row = at(ordinal);
        if (step > 0) return upperBound(row);
        return omittedCount - lowerBound(row);
    }

    private int lowerBound(int row) {
        int low = 0, high = omittedCount;
        while (low < high) {
            int mid = low + ((high - low) >>> 1);
            if (omitted[mid] < row) low = mid + 1;
            else high = mid;
        }
        return low;
    }

    private int upperBound(int row) {
        int low = 0, high = omittedCount;
        while (low < high) {
            int mid = low + ((high - low) >>> 1);
            if (omitted[mid] <= row) low = mid + 1;
            else high = mid;
        }
        return low;
    }

    private void exclude(int value) {
        int location = lowerBound(value);
        ensureOmitted(omittedCount + 1);
        System.arraycopy(omitted, location, omitted, location + 1, omittedCount - location);
        omitted[location] = value;
        omittedCount++;
        count--;
        modCount++;
    }

    private void removeAt(int index) {
        System.arraycopy(ids, index + 1, ids, index, count - index - 1);
        ids[--count] = 0;
        modCount++;
    }

    private void materialize() {
        if (!spanMode) return;
        ids = selectedIndices();
        spanMode = false;
        spanLength = 0;
        omittedCount = 0;
    }

    private void ensureIds(int minimum) {
        if (minimum <= ids.length) return;
        ids = Arrays.copyOf(ids, capacity(ids.length, minimum));
    }

    private void ensureOmitted(int minimum) {
        if (minimum <= omitted.length) return;
        omitted = Arrays.copyOf(omitted, capacity(omitted.length, minimum));
    }

    /** Automatic Xmx-based slack decision occurs only at actual primitive-array growth. */
    private static int capacity(int current, int minimum) {
        if (minimum < 0 || minimum > MAX_ARRAY) throw new IllegalStateException("selection capacity exhausted");
        long geometric = Math.max((long) minimum, Math.max(8L, (long) current + (current >>> 1) + 1L));
        geometric = Math.min(MAX_ARRAY, geometric);
        Runtime vm = Runtime.getRuntime();
        long available = Math.max(0L, vm.maxMemory() - (vm.totalMemory() - vm.freeMemory()));
        return (int) (available <= geometric * Integer.BYTES * 2L ? minimum : geometric);
    }

    private void check(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException(index);
    }
}
