// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLRENDERBUFFERSTORAGEEXTPROC {

    void apply(int x0, int x1, int x2, int x3);
    static MemoryAddress allocate(PFNGLRENDERBUFFERSTORAGEEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLRENDERBUFFERSTORAGEEXTPROC.class, fi, constants$627.PFNGLRENDERBUFFERSTORAGEEXTPROC$FUNC, "(IIII)V");
    }
    static MemoryAddress allocate(PFNGLRENDERBUFFERSTORAGEEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLRENDERBUFFERSTORAGEEXTPROC.class, fi, constants$627.PFNGLRENDERBUFFERSTORAGEEXTPROC$FUNC, "(IIII)V", scope);
    }
    static PFNGLRENDERBUFFERSTORAGEEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3) -> {
            try {
                constants$627.PFNGLRENDERBUFFERSTORAGEEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


