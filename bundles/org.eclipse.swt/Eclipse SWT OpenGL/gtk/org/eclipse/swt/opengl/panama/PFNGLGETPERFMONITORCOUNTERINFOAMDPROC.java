// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETPERFMONITORCOUNTERINFOAMDPROC {

    void apply(int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3);
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERINFOAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERINFOAMDPROC.class, fi, constants$471.PFNGLGETPERFMONITORCOUNTERINFOAMDPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERINFOAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERINFOAMDPROC.class, fi, constants$471.PFNGLGETPERFMONITORCOUNTERINFOAMDPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETPERFMONITORCOUNTERINFOAMDPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3) -> {
            try {
                constants$471.PFNGLGETPERFMONITORCOUNTERINFOAMDPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


