// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLWRITEMASKEXTPROC {

    void apply(int x0, int x1, int x2, int x3, int x4, int x5);
    static MemoryAddress allocate(PFNGLWRITEMASKEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLWRITEMASKEXTPROC.class, fi, constants$686.PFNGLWRITEMASKEXTPROC$FUNC, "(IIIIII)V");
    }
    static MemoryAddress allocate(PFNGLWRITEMASKEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLWRITEMASKEXTPROC.class, fi, constants$686.PFNGLWRITEMASKEXTPROC$FUNC, "(IIIIII)V", scope);
    }
    static PFNGLWRITEMASKEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4, int x5) -> {
            try {
                constants$686.PFNGLWRITEMASKEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


