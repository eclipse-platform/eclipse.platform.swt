// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETACTIVEATTRIBARBPROC {

    void apply(int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3, jdk.incubator.foreign.MemoryAddress x4, jdk.incubator.foreign.MemoryAddress x5, jdk.incubator.foreign.MemoryAddress x6);
    static MemoryAddress allocate(PFNGLGETACTIVEATTRIBARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETACTIVEATTRIBARBPROC.class, fi, constants$400.PFNGLGETACTIVEATTRIBARBPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETACTIVEATTRIBARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETACTIVEATTRIBARBPROC.class, fi, constants$400.PFNGLGETACTIVEATTRIBARBPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETACTIVEATTRIBARBPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3, jdk.incubator.foreign.MemoryAddress x4, jdk.incubator.foreign.MemoryAddress x5, jdk.incubator.foreign.MemoryAddress x6) -> {
            try {
                constants$400.PFNGLGETACTIVEATTRIBARBPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


