/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/** Synthetic immutable logical tree; no SWT peers or retained spelling arrays. */
final class M3TreeDemandModel {
    static final int GROUPS = 12;
    static final int PACKAGES = 128;
    static final int FILES = 256;
    static final int PAGE_SIZE = 24;
    static final long LOGICAL_NODES = (long) GROUPS * (1 + PACKAGES + (long) PACKAGES * FILES);

    record Key(int group, int pack, int file) {
        Key {
            if (group < 0 || group >= GROUPS
                    || pack < -1 || pack >= PACKAGES
                    || file < -1 || file >= FILES
                    || (pack == -1 && file != -1)) {
                throw new IllegalArgumentException("invalid logical tree address");
            }
        }
    }

    record Description(String title, String kind, String address) {}

    static Key root(int group) { return new Key(group, -1, -1); }

    static int children(Key parent) {
        Objects.requireNonNull(parent, "parent");
        if (parent.pack() < 0) return PACKAGES;
        return parent.file() < 0 ? FILES : 0;
    }

    static Key child(Key parent, int logicalIndex) {
        int count = children(parent);
        if (logicalIndex < 0 || logicalIndex >= count) {
            throw new IndexOutOfBoundsException("logical child " + logicalIndex);
        }
        return parent.pack() < 0
                ? new Key(parent.group(), logicalIndex, -1)
                : new Key(parent.group(), parent.pack(), logicalIndex);
    }

    static Description describe(Key key) {
        Objects.requireNonNull(key, "key");
        if (key.pack() < 0) {
            return new Description("Module " + (key.group() + 1),
                    "12 synthetic modules", "module:" + key.group());
        }
        if (key.file() < 0) {
            return new Description("Package " + (key.pack() + 1),
                    "256 logical files", key.group() + "/" + key.pack());
        }
        return new Description("File-" + String.format("%04d", key.file() + 1) + ".java",
                "Source atom", key.group() + "/" + key.pack() + "/" + key.file());
    }

    /**
     * Controller of SetData-equivalent demand, independent of SWT.VIRTUAL.
     * Re-entrant requests for distinct logical addresses are allowed. A same-address
     * cycle or provider exception cannot poison subsequent requests.
     */
    static final class Mediator {
        private final ArrayDeque<Key> context = new ArrayDeque<>();
        private final Set<Key> inFlight = new HashSet<>();
        private long completed;
        private long refused;

        void deliver(Key key, int logicalIndex, BiConsumer<Key, Integer> callback) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(callback, "callback");
            if (logicalIndex < 0) throw new IndexOutOfBoundsException("negative SetData index");
            if (!inFlight.add(key)) {
                refused++;
                throw new IllegalStateException("reentrant SetData for " + key);
            }
            context.push(key);
            try {
                callback.accept(key, logicalIndex);
                completed++;
            } finally {
                Key popped = context.pop();
                if (!popped.equals(key) || !inFlight.remove(key)) {
                    throw new IllegalStateException("SetData context corrupted");
                }
            }
        }

        Key current() { return context.peek(); }
        int depth() { return context.size(); }
        long completed() { return completed; }
        long refused() { return refused; }
    }

    private M3TreeDemandModel() {}
}
