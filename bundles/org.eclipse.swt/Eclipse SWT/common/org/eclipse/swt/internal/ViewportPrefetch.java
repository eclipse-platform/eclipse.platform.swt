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
package org.eclipse.swt.internal;

/**
 * Optional row requests for the owning SWT.VIRTUAL control. This is not a
 * second viewport or widget: the native control owns scroll state and events.
 * Only a debounced prefetch callback uses this planner; visible paint/SetData
 * always follows the toolkit's existing synchronous path.
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportPrefetch {
    private ViewportPrefetch() { }

    /** Three half-open, disjoint, ordered optional ranges. No logical row objects. */
    public record Bands(int firstFrom, int firstTo, int secondFrom, int secondTo,
            int pageFrom, int pageTo) {
        public Bands {
            if (firstFrom < 0 || firstFrom > firstTo || secondFrom < 0 || secondFrom > secondTo
                    || pageFrom < 0 || pageFrom > pageTo) {
                throw new IllegalArgumentException("invalid optional row interval");
            }
        }
        public boolean empty() {
            return firstFrom == firstTo && secondFrom == secondTo && pageFrom == pageTo;
        }
    }

    /** Runtime pressure is sampled once per delayed prefetch, never per item or event. */
    public static Bands forRuntime(int count, int top, int visibleRows, int overscanRows,
            int directionalPages, boolean upward) {
        if (count == 0 && top == 0 && visibleRows == 0) {
            return plan(count, top, visibleRows, overscanRows, directionalPages, upward, 0L, 1L);
        }
        Runtime runtime = Runtime.getRuntime();
        return plan(count, top, visibleRows, overscanRows, directionalPages, upward,
                runtime.totalMemory() - runtime.freeMemory(), runtime.maxMemory());
    }

    /** Pure bounded plan: unknown/invalid heap telemetry forbids speculative work. */
    public static Bands plan(int count, int top, int visibleRows, int overscanRows,
            int directionalPages, boolean upward, long usedHeap, long maxHeap) {
        if (count < 0 || top < 0 || top > count || visibleRows < 0
                || overscanRows < 0 || directionalPages < 0) {
            throw new IllegalArgumentException("invalid logical row geometry");
        }
        if (count == 0 || top == count || visibleRows == 0) {
            return new Bands(top, top, top, top, top, top);
        }
        int pressure = pressureLevel(usedHeap, maxHeap);
        int margin = pressure == 2 ? 0 : pressure == 1 ? overscanRows / 2 : overscanRows;
        long visibleEnd = Math.min((long) count, (long) top + visibleRows);
        int before = (int) Math.max(0L, (long) top - margin);
        int after = (int) Math.min((long) count, visibleEnd + margin);
        int visibleEndInt = (int) visibleEnd;
        long pageRows = pressure == 0 ? (long) visibleRows * directionalPages : 0L;
        int pageBefore = (int) Math.max(0L, (long) before - pageRows);
        int pageAfter = (int) Math.min((long) count, (long) after + pageRows);
        return upward
                ? new Bands(before, top, visibleEndInt, after, pageBefore, before)
                : new Bands(visibleEndInt, after, before, top, after, pageAfter);
    }

    private static int pressureLevel(long used, long maximum) {
        if (maximum <= 0 || used < 0 || used > maximum) return 2;
        long available = maximum - used;
        if (available <= maximum / 8L) return 2;
        if (available <= maximum / 4L) return 1;
        return 0;
    }
}
