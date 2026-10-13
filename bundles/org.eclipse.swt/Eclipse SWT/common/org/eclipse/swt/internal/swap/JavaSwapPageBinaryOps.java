// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/JavaSwapPageBinaryOps.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.util.Objects;

/** Exact Java oracle for deferred-page primitive transforms. */
final class JavaSwapPageBinaryOps implements SwapPageBinaryOps {
  static final JavaSwapPageBinaryOps INSTANCE = new JavaSwapPageBinaryOps();

  private JavaSwapPageBinaryOps() {}

  @Override
  public String id() {
    return "java";
  }

  @Override
  public boolean nativeBackend() {
    return false;
  }

  @Override
  public int copy(ByteBuffer source, ByteBuffer target) {
    ByteBuffer input = Objects.requireNonNull(source, "source").duplicate();
    ByteBuffer output = writable(target, "target").duplicate();
    int bytes = input.remaining();
    if (output.remaining() < bytes) throw new BufferOverflowException();
    // ByteBuffer's bulk transfer preserves overlapping source content, like memmove.
    output.put(input);
    return bytes;
  }

  @Override
  public int deltaEncodeMonotonicLongs(ByteBuffer sourceLongs, ByteBuffer target) {
    ByteBuffer input = Objects.requireNonNull(sourceLongs, "sourceLongs")
        .duplicate().order(ByteOrder.LITTLE_ENDIAN);
    ByteBuffer output = writable(target, "target").duplicate();
    // Validate the complete source and exact output size before the first target write.
    int required = encodedBytes(input, output.remaining());
    input = stableInput(input, output, required).order(ByteOrder.LITTLE_ENDIAN);
    int count = input.remaining() / Long.BYTES;
    writeUnsignedVarLong(count, output);
    long previous = 0L;
    for (int index = 0; index < count; index++) {
      long value = input.getLong();
      writeUnsignedVarLong(value - previous, output);
      previous = value;
    }
    return required;
  }

  @Override
  public int deltaDecodeMonotonicLongs(ByteBuffer encoded, ByteBuffer targetLongs) {
    ByteBuffer input = Objects.requireNonNull(encoded, "encoded").duplicate();
    ByteBuffer output = writable(targetLongs, "targetLongs")
        .duplicate().order(ByteOrder.LITTLE_ENDIAN);
    int count = decodedCount(input, output.remaining());
    input = stableInput(input, output, count * Long.BYTES);
    readUnsignedVarLong(input);
    long previous = 0L;
    for (int index = 0; index < count; index++) {
      previous += readUnsignedVarLong(input);
      output.putLong(previous);
    }
    return count;
  }

  /** Shared Java ingress fence; JNI must not bypass ByteBuffer read-only semantics. */
  static ByteBuffer writable(ByteBuffer target, String name) {
    Objects.requireNonNull(target, name);
    if (target.isReadOnly()) throw new ReadOnlyBufferException();
    return target;
  }

  private static int encodedBytes(ByteBuffer source, int capacity) {
    ByteBuffer input = source.duplicate().order(ByteOrder.LITTLE_ENDIAN);
    if ((input.remaining() & (Long.BYTES - 1)) != 0) {
      throw new IllegalArgumentException("long lane byte length must be a multiple of 8");
    }
    long required = varintBytes(input.remaining() / Long.BYTES);
    long previous = 0L;
    while (input.hasRemaining()) {
      long value = input.getLong();
      if (value < 0L || value < previous) {
        throw new IllegalArgumentException("long lane must be nonnegative and monotonic");
      }
      required += varintBytes(value - previous);
      previous = value;
    }
    if (required > capacity) throw new BufferOverflowException();
    return (int) required;
  }

  private static int decodedCount(ByteBuffer source, int capacity) {
    ByteBuffer input = source.duplicate();
    long countLong = readUnsignedVarLong(input);
    if (countLong > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("decoded long count exceeds Java buffer range");
    }
    if (countLong * Long.BYTES > capacity) throw new BufferOverflowException();
    int count = (int) countLong;
    long previous = 0L;
    for (int index = 0; index < count; index++) {
      long delta = readUnsignedVarLong(input);
      if (delta > Long.MAX_VALUE - previous) {
        throw new IllegalArgumentException("decoded delta overflows signed long coordinate");
      }
      previous += delta;
    }
    if (input.hasRemaining()) throw new IllegalArgumentException("trailing bytes after delta page");
    return count;
  }

  /**
   * Heap aliases can be established exactly. Direct aliases and read-only heap backing are opaque
   * to supported Java APIs: conservatively snapshot in those cases, after validation. Distinct
   * writable heap arrays and heap/direct pairs need no scratch. No address/Unsafe lookup is used.
   */
  private static ByteBuffer stableInput(ByteBuffer source, ByteBuffer target, int writtenBytes) {
    boolean mayAlias = source.isDirect() == target.isDirect();
    if (source.hasArray() && target.hasArray()) {
      long from = (long) source.arrayOffset() + source.position();
      long to = (long) target.arrayOffset() + target.position();
      mayAlias = source.array() == target.array()
          && from < to + writtenBytes && to < from + source.remaining();
    }
    if (!mayAlias || source.remaining() == 0 || writtenBytes == 0) return source.duplicate();
    byte[] bytes = new byte[source.remaining()];
    source.duplicate().get(bytes);
    return ByteBuffer.wrap(bytes);
  }

  private static int varintBytes(long value) {
    int bytes = 1;
    while ((value >>>= 7) != 0L) bytes++;
    return bytes;
  }

  private static void writeUnsignedVarLong(long value, ByteBuffer target) {
    if (value < 0L) throw new IllegalArgumentException("negative unsigned varint");
    long remaining = value;
    do {
      if (!target.hasRemaining()) throw new BufferOverflowException();
      int bits = (int) (remaining & 0x7fL);
      remaining >>>= 7;
      target.put((byte) (bits | (remaining == 0L ? 0 : 0x80)));
    } while (remaining != 0L);
  }

  private static long readUnsignedVarLong(ByteBuffer source) {
    long value = 0L;
    for (int byteIndex = 0; byteIndex < 9; byteIndex++) {
      if (!source.hasRemaining()) throw new IllegalArgumentException("truncated unsigned varint");
      int current = source.get() & 0xff;
      value |= (long) (current & 0x7f) << (byteIndex * 7);
      if ((current & 0x80) == 0) return value;
    }
    throw new IllegalArgumentException("unsigned varint exceeds signed long range");
  }
}
