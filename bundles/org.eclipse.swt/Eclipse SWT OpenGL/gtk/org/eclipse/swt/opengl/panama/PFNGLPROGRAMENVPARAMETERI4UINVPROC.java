// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPROGRAMENVPARAMETERI4UINVPROC {

    void apply(int x0, int x1, int x2, int x3, int x4, int x5);
    static MemoryAddress allocate(PFNGLPROGRAMENVPARAMETERI4UINVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMENVPARAMETERI4UINVPROC.class, fi, constants$758.PFNGLPROGRAMENVPARAMETERI4UINVPROC$FUNC, "(IIIIII)V");
    }
    static MemoryAddress allocate(PFNGLPROGRAMENVPARAMETERI4UINVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMENVPARAMETERI4UINVPROC.class, fi, constants$758.PFNGLPROGRAMENVPARAMETERI4UINVPROC$FUNC, "(IIIIII)V", scope);
    }
    static PFNGLPROGRAMENVPARAMETERI4UINVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4, int x5) -> {
            try {
                constants$758.PFNGLPROGRAMENVPARAMETERI4UINVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


