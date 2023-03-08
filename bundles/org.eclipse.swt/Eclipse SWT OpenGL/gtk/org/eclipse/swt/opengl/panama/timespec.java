// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public class timespec {

    static final MemoryLayout $struct$LAYOUT = MemoryLayout.structLayout(
        C_LONG.withName("tv_sec"),
        C_LONG.withName("tv_nsec")
    ).withName("timespec");
    public static MemoryLayout $LAYOUT() {
        return timespec.$struct$LAYOUT;
    }
    static final VarHandle tv_sec$VH = $struct$LAYOUT.varHandle(long.class, MemoryLayout.PathElement.groupElement("tv_sec"));
    public static VarHandle tv_sec$VH() {
        return timespec.tv_sec$VH;
    }
    public static long tv_sec$get(MemorySegment seg) {
        return (long)timespec.tv_sec$VH.get(seg);
    }
    public static void tv_sec$set( MemorySegment seg, long x) {
        timespec.tv_sec$VH.set(seg, x);
    }
    public static long tv_sec$get(MemorySegment seg, long index) {
        return (long)timespec.tv_sec$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void tv_sec$set(MemorySegment seg, long index, long x) {
        timespec.tv_sec$VH.set(seg.asSlice(index*sizeof()), x);
    }
    static final VarHandle tv_nsec$VH = $struct$LAYOUT.varHandle(long.class, MemoryLayout.PathElement.groupElement("tv_nsec"));
    public static VarHandle tv_nsec$VH() {
        return timespec.tv_nsec$VH;
    }
    public static long tv_nsec$get(MemorySegment seg) {
        return (long)timespec.tv_nsec$VH.get(seg);
    }
    public static void tv_nsec$set( MemorySegment seg, long x) {
        timespec.tv_nsec$VH.set(seg, x);
    }
    public static long tv_nsec$get(MemorySegment seg, long index) {
        return (long)timespec.tv_nsec$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void tv_nsec$set(MemorySegment seg, long index, long x) {
        timespec.tv_nsec$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocate(ResourceScope scope) { return allocate(SegmentAllocator.ofScope(scope)); }
    public static MemorySegment allocateArray(int len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment allocateArray(int len, ResourceScope scope) {
        return allocateArray(len, SegmentAllocator.ofScope(scope));
    }
    public static MemorySegment ofAddress(MemoryAddress addr, ResourceScope scope) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, scope); }
}


