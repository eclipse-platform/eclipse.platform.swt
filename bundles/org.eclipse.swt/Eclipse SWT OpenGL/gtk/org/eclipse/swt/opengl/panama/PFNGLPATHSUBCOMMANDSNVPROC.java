// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPATHSUBCOMMANDSNVPROC {

    void apply(int x0, int x1, int x2, int x3, jdk.incubator.foreign.MemoryAddress x4, int x5, int x6, jdk.incubator.foreign.MemoryAddress x7);
    static MemoryAddress allocate(PFNGLPATHSUBCOMMANDSNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPATHSUBCOMMANDSNVPROC.class, fi, constants$781.PFNGLPATHSUBCOMMANDSNVPROC$FUNC, "(IIIILjdk/incubator/foreign/MemoryAddress;IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLPATHSUBCOMMANDSNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPATHSUBCOMMANDSNVPROC.class, fi, constants$781.PFNGLPATHSUBCOMMANDSNVPROC$FUNC, "(IIIILjdk/incubator/foreign/MemoryAddress;IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLPATHSUBCOMMANDSNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, jdk.incubator.foreign.MemoryAddress x4, int x5, int x6, jdk.incubator.foreign.MemoryAddress x7) -> {
            try {
                constants$781.PFNGLPATHSUBCOMMANDSNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6, x7);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


