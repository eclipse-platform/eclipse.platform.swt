// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETPERFMONITORCOUNTERDATAAMDPROC {

    void apply(int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3, jdk.incubator.foreign.MemoryAddress x4);
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERDATAAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERDATAAMDPROC.class, fi, constants$473.PFNGLGETPERFMONITORCOUNTERDATAAMDPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERDATAAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERDATAAMDPROC.class, fi, constants$473.PFNGLGETPERFMONITORCOUNTERDATAAMDPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETPERFMONITORCOUNTERDATAAMDPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3, jdk.incubator.foreign.MemoryAddress x4) -> {
            try {
                constants$473.PFNGLGETPERFMONITORCOUNTERDATAAMDPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


