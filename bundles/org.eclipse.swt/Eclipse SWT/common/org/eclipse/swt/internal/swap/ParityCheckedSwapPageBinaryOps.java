// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/ParityCheckedSwapPageBinaryOps.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/** Exact Java-oracle verification wrapper for swap-page binary operations. */
public final class ParityCheckedSwapPageBinaryOps implements SwapPageBinaryOps {
  private final SwapPageBinaryOps delegate;

  public ParityCheckedSwapPageBinaryOps(SwapPageBinaryOps delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  @Override public String id() { return "parity(" + delegate.id() + ")"; }
  @Override public boolean nativeBackend() { return delegate.nativeBackend(); }

  @Override
  public int copy(ByteBuffer source, ByteBuffer target) {
    return verify("copy", source, target, 1, JavaSwapPageBinaryOps.INSTANCE::copy, delegate::copy);
  }

  @Override
  public int deltaEncodeMonotonicLongs(ByteBuffer sourceLongs, ByteBuffer target) {
    return verify("deltaEncodeMonotonicLongs", sourceLongs, target, 1,
        JavaSwapPageBinaryOps.INSTANCE::deltaEncodeMonotonicLongs,
        delegate::deltaEncodeMonotonicLongs);
  }

  @Override
  public int deltaDecodeMonotonicLongs(ByteBuffer encoded, ByteBuffer targetLongs) {
    return verify("deltaDecodeMonotonicLongs", encoded, targetLongs, Long.BYTES,
        JavaSwapPageBinaryOps.INSTANCE::deltaDecodeMonotonicLongs,
        delegate::deltaDecodeMonotonicLongs);
  }

  @FunctionalInterface
  private interface Operation { int apply(ByteBuffer source, ByteBuffer target); }

  private static int verify(
      String operation,
      ByteBuffer source,
      ByteBuffer target,
      int outputBytesPerUnit,
      Operation oracle,
      Operation candidate) {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(target, "target");

    ByteBuffer oracleSource = heapCopy(source);
    ByteBuffer oracleTarget = ByteBuffer.allocate(target.remaining());
    int oracleUnits = oracle.apply(oracleSource, oracleTarget);

    ByteBuffer candidateSource = source.duplicate();
    ByteBuffer candidateTarget = target.duplicate();
    int candidateUnits = candidate.apply(candidateSource, candidateTarget);
    if (candidateUnits != oracleUnits) {
      throw new IllegalStateException(
          operation + " Java/backend result mismatch: " + oracleUnits + " != " + candidateUnits);
    }

    int outputBytes;
    try {
      outputBytes = Math.multiplyExact(candidateUnits, outputBytesPerUnit);
    } catch (ArithmeticException overflow) {
      throw new IllegalStateException(operation + " backend returned invalid result size", overflow);
    }
    byte[] expected = range(oracleTarget, 0, outputBytes);
    byte[] actual = range(candidateTarget, target.position(), outputBytes);
    if (!Arrays.equals(expected, actual)) {
      throw new IllegalStateException(operation + " Java/backend output mismatch");
    }
    return candidateUnits;
  }

  private static ByteBuffer heapCopy(ByteBuffer source) {
    ByteBuffer duplicate = source.duplicate();
    byte[] bytes = new byte[duplicate.remaining()];
    duplicate.get(bytes);
    return ByteBuffer.wrap(bytes);
  }

  private static byte[] range(ByteBuffer buffer, int start, int length) {
    if (length < 0 || start < 0 || start > buffer.limit() - length) {
      throw new IllegalStateException("backend returned invalid output length");
    }
    ByteBuffer duplicate = buffer.duplicate();
    duplicate.position(start);
    duplicate.limit(start + length);
    byte[] bytes = new byte[length];
    duplicate.get(bytes);
    return bytes;
  }
}
