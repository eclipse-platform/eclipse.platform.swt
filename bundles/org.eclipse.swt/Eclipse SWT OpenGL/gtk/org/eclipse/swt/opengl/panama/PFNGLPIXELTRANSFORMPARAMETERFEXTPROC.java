// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPIXELTRANSFORMPARAMETERFEXTPROC {

    void apply(int x0, int x1, float x2);
    static MemoryAddress allocate(PFNGLPIXELTRANSFORMPARAMETERFEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPIXELTRANSFORMPARAMETERFEXTPROC.class, fi, constants$652.PFNGLPIXELTRANSFORMPARAMETERFEXTPROC$FUNC, "(IIF)V");
    }
    static MemoryAddress allocate(PFNGLPIXELTRANSFORMPARAMETERFEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPIXELTRANSFORMPARAMETERFEXTPROC.class, fi, constants$652.PFNGLPIXELTRANSFORMPARAMETERFEXTPROC$FUNC, "(IIF)V", scope);
    }
    static PFNGLPIXELTRANSFORMPARAMETERFEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, float x2) -> {
            try {
                constants$652.PFNGLPIXELTRANSFORMPARAMETERFEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


