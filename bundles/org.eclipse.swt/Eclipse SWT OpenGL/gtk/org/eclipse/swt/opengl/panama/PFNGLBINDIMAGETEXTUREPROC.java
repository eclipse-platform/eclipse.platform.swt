// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLBINDIMAGETEXTUREPROC {

    void apply(int x0, int x1, int x2, byte x3, int x4, int x5, int x6);
    static MemoryAddress allocate(PFNGLBINDIMAGETEXTUREPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLBINDIMAGETEXTUREPROC.class, fi, constants$251.PFNGLBINDIMAGETEXTUREPROC$FUNC, "(IIIBIII)V");
    }
    static MemoryAddress allocate(PFNGLBINDIMAGETEXTUREPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLBINDIMAGETEXTUREPROC.class, fi, constants$251.PFNGLBINDIMAGETEXTUREPROC$FUNC, "(IIIBIII)V", scope);
    }
    static PFNGLBINDIMAGETEXTUREPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, byte x3, int x4, int x5, int x6) -> {
            try {
                constants$251.PFNGLBINDIMAGETEXTUREPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


