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
 *
 * Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/SwapStore.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
 *******************************************************************************/
package org.eclipse.swt.internal.swap;

import java.io.IOException;
import java.util.Map;

/** Immutable, namespace/codec-bound durable records. Implementations must commit atomically. */
public interface SwapStore extends AutoCloseable {
	record Head(long revision, long recordId) {
		public Head {
			if (revision < 0 || (revision == 0 ? recordId != -1 : recordId < 0)) {
				throw new IllegalArgumentException("invalid head");
			}
		}
	}
	record Publication(String name, long expectedRevision, long recordId) {
		public Publication {
			requireName(name);
			if (expectedRevision < 0 || expectedRevision == Long.MAX_VALUE || recordId < 0) {
				throw new IllegalArgumentException("invalid publication");
			}
		}
	}
	/** Null means absent, never an empty record. Returned arrays are caller-owned. */
	byte[] read(long id) throws IOException;
	/** Exact duplicate writes are idempotent; conflicting bytes must roll back the whole batch. */
	void write(Map<Long, byte[]> records, Publication publication) throws IOException;
	default void write(Map<Long, byte[]> records) throws IOException { write(records, null); }
	Head head(String name) throws IOException;
	/** Bounded ascending keyset page; afterExclusive=-1 starts enumeration. */
	long[] ids(long afterExclusive, int limit) throws IOException;
	long count() throws IOException;

	/**
	 * Whether this namespace can transactionally delete old records that are not referenced by
	 * any publication head.
	 */
	default boolean supportsUnreferencedPrune() { return false; }

	/**
	 * Delete at most {@code maxDeletes} unreferenced records below {@code beforeExclusive}.
	 * Publication targets must never be deleted.
	 */
	default int pruneUnreferencedBefore(long beforeExclusive, int maxDeletes) throws IOException {
		throw new UnsupportedOperationException("unreferenced pruning is not supported");
	}

	int maxRecordBytes();
	int maxBatchBytes();
	int maxBatchRecords();
	@Override void close() throws IOException;

	static void requireId(long id) {
		if (id < 0) throw new IllegalArgumentException("negative record id");
	}
	static String requireName(String name) {
		if (name == null || !name.matches("[A-Za-z0-9._:/-]{1,128}")) {
			throw new IllegalArgumentException("name must contain 1..128 ASCII identifier characters");
		}
		return name;
	}
}
