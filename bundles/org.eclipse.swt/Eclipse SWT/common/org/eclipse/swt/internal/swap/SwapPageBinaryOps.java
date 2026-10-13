// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/SwapPageBinaryOps.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.nio.ByteBuffer;

/**
 * Primitive page transforms for deferred swap codecs.
 *
 * <p>Implementations operate on the caller buffers' remaining ranges without changing their
 * positions or limits. Delta lanes are fixed-width little-endian signed Java longs constrained to
 * nonnegative monotonic coordinates. The encoded form is: unsigned-varint count followed by
 * unsigned-varint first value and unsigned-varint successive deltas.</p>
 *
 * <p>Source content, buffer state, and memory lifetime must remain stable for the call; these
 * operations do not synchronize external buffer access. Read-only targets are rejected, including
 * zero-length writes. Malformed input, insufficient capacity, and overlap-scratch allocation
 * failure leave target bytes unchanged. Overlapping ranges have source-snapshot semantics. The
 * caller positions, limits, marks, and byte orders are preserved. Previously accepted non-minimal
 * varints remain accepted; encoded wire bytes are unchanged.</p>
 *
 * <p>Validation precedes mutation. JNI uses no scratch for disjoint direct ranges and allocates
 * exact output scratch only on overlap. The Java oracle snapshots opaque possible aliases. These
 * temporary buffers are not charged to a cache residency budget; callers must bound page size.
 * Independent overlapping file mappings with distinct virtual addresses are outside the JNI
 * alias contract; use views of one mapping or the Java oracle for that case.</p>
 */
public interface SwapPageBinaryOps {
	String id();

	boolean nativeBackend();

	int copy(ByteBuffer source, ByteBuffer target);

	int deltaEncodeMonotonicLongs(ByteBuffer sourceLongs, ByteBuffer target);

	int deltaDecodeMonotonicLongs(ByteBuffer encoded, ByteBuffer targetLongs);
}
