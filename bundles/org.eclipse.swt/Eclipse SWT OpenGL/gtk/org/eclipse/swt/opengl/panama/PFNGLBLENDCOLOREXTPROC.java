// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLBLENDCOLOREXTPROC {

    void apply(float x0, float x1, float x2, float x3);
    static MemoryAddress allocate(PFNGLBLENDCOLOREXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLBLENDCOLOREXTPROC.class, fi, constants$518.PFNGLBLENDCOLOREXTPROC$FUNC, "(FFFF)V");
    }
    static MemoryAddress allocate(PFNGLBLENDCOLOREXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLBLENDCOLOREXTPROC.class, fi, constants$518.PFNGLBLENDCOLOREXTPROC$FUNC, "(FFFF)V", scope);
    }
    static PFNGLBLENDCOLOREXTPROC ofAddress(MemoryAddress addr) {
        return (float x0, float x1, float x2, float x3) -> {
            try {
                constants$518.PFNGLBLENDCOLOREXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


