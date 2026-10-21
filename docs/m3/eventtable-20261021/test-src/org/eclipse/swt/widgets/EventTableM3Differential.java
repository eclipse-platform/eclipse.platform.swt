/*
 * SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 */
package org.eclipse.swt.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.eclipse.swt.SWT;

/** Exact original-SWT event table differential, including reentrant listener edits. */
public final class EventTableM3Differential {
    private static long assertions;
    private EventTableM3Differential() { }

    private static void check(boolean result, String message) {
        assertions++;
        if (!result) throw new AssertionError(message);
    }

    interface Surface {
        void hook(int type, Listener listener);
        void unhook(int type, Listener listener);
        void unhookTyped(int type, java.util.EventListener listener);
        Listener[] getListeners(int type);
        boolean hooks(int type);
        void send(Event event);
        int size();
        int[] types();
        Listener[] listeners();
        int highWater();
    }

    private static Surface current() {
        EventTable table = new EventTable();
        return new Surface() {
            public void hook(int type, Listener listener) { table.hook(type, listener); }
            public void unhook(int type, Listener listener) { table.unhook(type, listener); }
            public void unhookTyped(int type, java.util.EventListener listener) { table.unhook(type, listener); }
            public Listener[] getListeners(int type) { return table.getListeners(type); }
            public boolean hooks(int type) { return table.hooks(type); }
            public void send(Event event) { table.sendEvent(event); }
            public int size() { return table.size(); }
            public int[] types() { return table.types; }
            public Listener[] listeners() { return table.listeners; }
            public int highWater() { return table.highWater; }
        };
    }

    private static Surface original() {
        LegacyEventTable table = new LegacyEventTable();
        return new Surface() {
            public void hook(int type, Listener listener) { table.hook(type, listener); }
            public void unhook(int type, Listener listener) { table.unhook(type, listener); }
            public void unhookTyped(int type, java.util.EventListener listener) { table.unhook(type, listener); }
            public Listener[] getListeners(int type) { return table.getListeners(type); }
            public boolean hooks(int type) { return table.hooks(type); }
            public void send(Event event) { table.sendEvent(event); }
            public int size() { return table.size(); }
            public int[] types() { return table.types; }
            public Listener[] listeners() { return table.listeners; }
            public int highWater() { return -1; }
        };
    }

    private static final class Driver {
        private final Surface surface;
        private final Map<Integer, Listener> registered = new HashMap<>();
        private final IdentityHashMap<Listener, Integer> identity = new IdentityHashMap<>();
        private final List<Integer> delivery = new ArrayList<>();
        private final Map<Integer, Boolean> fired = new HashMap<>();
        private String failure = "";

        Driver(Surface surface) { this.surface = surface; }

        void hook(int type, int id) { hook(type, id, 0); }

        void hook(int type, int id, int action) {
            Listener listener = event -> {
                delivery.add(id);
                switch (action) {
                    case 1 -> { // add during dispatch, exactly once
                        if (fired.putIfAbsent(id, true) == null)
                            hook(event.type, 50_000 + id);
                    }
                    case 2 -> { // unhook the currently firing listener
                        surface.unhook(event.type, registered.get(id));
                    }
                    case 3 -> { // nested dispatch during callback
                        if (fired.putIfAbsent(id, true) == null) send(event.type);
                    }
                    case 4 -> event.type = SWT.None;
                    case 5 -> throw new IllegalStateException("event failure " + id);
                    case 6 -> throw new AssertionError("event error " + id);
                    default -> { }
                }
            };
            registered.put(id, listener);
            identity.put(listener, id);
            surface.hook(type, listener);
        }

        void unhook(int type, int id) {
            surface.unhook(type, registered.get(id));
        }

        void hookTyped(int type, int id, java.util.EventListener token) {
            TypedListener typed = new TypedListener(token);
            registered.put(id, typed);
            identity.put(typed, id);
            surface.hook(type, typed);
        }

        void unhookTyped(int type, java.util.EventListener token) {
            surface.unhookTyped(type, token);
        }

        String send(int type) {
            Event event = new Event();
            event.type = type;
            try {
                surface.send(event);
                failure = "";
            } catch (Throwable error) {
                failure = error.getClass().getName() + ":" + error.getMessage()
                        + ":" + error.getSuppressed().length;
            }
            return failure + ":finalType=" + event.type;
        }

        int[] ids(Listener[] source) {
            if (source == null) return new int[0];
            int[] result = new int[source.length];
            for (int i = 0; i < source.length; i++)
                result[i] = source[i] == null ? -1 : identity.getOrDefault(source[i], -2);
            return result;
        }
    }

    private static final int[] TYPES = {SWT.None, 1, 2, 3, 4, 5, -1};

    private static void same(Driver old, Driver now, String step) {
        check(old.surface.size() == now.surface.size(), "live listeners " + step);
        check(Arrays.equals(old.surface.types(), now.surface.types()),
                "exact parent-owned type lane " + step);
        check(Arrays.equals(old.ids(old.surface.listeners()), now.ids(now.surface.listeners())),
                "exact reference lane/tombstones " + step);
        for (int type : TYPES) {
            check(old.surface.hooks(type) == now.surface.hooks(type),
                    "hooks(" + type + ") " + step);
            check(Arrays.equals(old.ids(old.surface.getListeners(type)),
                                now.ids(now.surface.getListeners(type))),
                    "getListeners(" + type + ") " + step);
        }
        int actualEnd = 0;
        int[] code = now.surface.types();
        if (code != null) {
            for (int index = 0; index < code.length; index++) {
                if (code[index] != SWT.None) actualEnd = index + 1;
            }
        }
        // The physical high-water invariant is independently checked through
        // the exact type lane and not the implementation's private fast path.
        check(now.surface.highWater() == actualEnd, "exact M3 high-water lane invariant");
    }

    private static void seededRandom() {
        Driver old = new Driver(original()), now = new Driver(current());
        Random random = new Random(0x4d335357545f4556L);
        int next = 1;
        for (int i = 0; i < 2_000; i++) {
            int type = TYPES[random.nextInt(TYPES.length)];
            int action = random.nextInt(10);
            if (action < 5) {
                old.hook(type, next);
                now.hook(type, next);
                next++;
            } else if (action < 8) {
                int id = random.nextInt(next + 4);
                old.unhook(type, id);
                now.unhook(type, id);
            } else {
                String first = old.send(type), second = now.send(type);
                check(first.equals(second), "event result " + i);
                check(old.delivery.equals(now.delivery), "ordered event callbacks " + i);
                old.delivery.clear();
                now.delivery.clear();
            }
            same(old, now, "random step " + i);
        }
    }

    private static void reentrantScript(int[] actions) {
        Driver old = new Driver(original()), now = new Driver(current());
        for (int i = 0; i < actions.length; i++) {
            old.hook(1, i + 1, actions[i]);
            now.hook(1, i + 1, actions[i]);
        }
        for (int i = 0; i < 4; i++) {
            String a = old.send(1), b = now.send(1);
            check(a.equals(b), "nested/throw outcome " + i);
            check(old.delivery.equals(now.delivery),
                    "nested event deliveries and order " + i);
            old.delivery.clear();
            now.delivery.clear();
            same(old, now, "after reentrant script " + i);
        }
    }

    private static void typedListenerIdentity() {
        Driver old = new Driver(original()), now = new Driver(current());
        java.util.EventListener token = new java.util.EventListener() { };
        old.hookTyped(2, 100, token);
        now.hookTyped(2, 100, token);
        old.hookTyped(3, 101, token);
        now.hookTyped(3, 101, token);
        same(old, now, "typed initial");
        old.unhookTyped(2, token);
        now.unhookTyped(2, token);
        same(old, now, "typed first removal");
        old.unhookTyped(3, token);
        now.unhookTyped(3, token);
        same(old, now, "typed second removal");
        old.hookTyped(SWT.None, 102, token);
        now.hookTyped(SWT.None, 102, token);
        same(old, now, "typed SWT.None tombstone");
        old.unhookTyped(SWT.None, token);
        now.unhookTyped(SWT.None, token);
        same(old, now, "typed SWT.None removal");
    }

    private static void directedReentrancy() {
        reentrantScript(new int[]{1, 0, 2, 0});
        reentrantScript(new int[]{3, 0, 2});
        reentrantScript(new int[]{0, 4, 0, 1});
        reentrantScript(new int[]{5, 0, 5, 0});
        reentrantScript(new int[]{6, 0, 6, 0});
        reentrantScript(new int[]{2, 1, 3, 5, 4});
    }

    public static void main(String[] args) {
        seededRandom();
        directedReentrancy();
        typedListenerIdentity();
        System.out.println("SWT_M3_EVENT_TABLE_PASS assertions=" + assertions);
    }
}
