// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPRESENTFRAMEKEYEDNVPROC {

    void apply(int x0, long x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10);
    static MemoryAddress allocate(PFNGLPRESENTFRAMEKEYEDNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPRESENTFRAMEKEYEDNVPROC.class, fi, constants$802.PFNGLPRESENTFRAMEKEYEDNVPROC$FUNC, "(IJIIIIIIIII)V");
    }
    static MemoryAddress allocate(PFNGLPRESENTFRAMEKEYEDNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPRESENTFRAMEKEYEDNVPROC.class, fi, constants$802.PFNGLPRESENTFRAMEKEYEDNVPROC$FUNC, "(IJIIIIIIIII)V", scope);
    }
    static PFNGLPRESENTFRAMEKEYEDNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, long x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10) -> {
            try {
                constants$802.PFNGLPRESENTFRAMEKEYEDNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


