// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPROGRAMUNIFORM3IEXTPROC {

    void apply(int x0, int x1, int x2, int x3, int x4);
    static MemoryAddress allocate(PFNGLPROGRAMUNIFORM3IEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMUNIFORM3IEXTPROC.class, fi, constants$575.PFNGLPROGRAMUNIFORM3IEXTPROC$FUNC, "(IIIII)V");
    }
    static MemoryAddress allocate(PFNGLPROGRAMUNIFORM3IEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMUNIFORM3IEXTPROC.class, fi, constants$575.PFNGLPROGRAMUNIFORM3IEXTPROC$FUNC, "(IIIII)V", scope);
    }
    static PFNGLPROGRAMUNIFORM3IEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4) -> {
            try {
                constants$575.PFNGLPROGRAMUNIFORM3IEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


