// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC {

    void apply(int x0, int x1, int x2, int x3, int x4);
    static MemoryAddress allocate(PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC.class, fi, constants$171.PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC$FUNC, "(IIIII)V");
    }
    static MemoryAddress allocate(PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC.class, fi, constants$171.PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC$FUNC, "(IIIII)V", scope);
    }
    static PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4) -> {
            try {
                constants$171.PFNGLRENDERBUFFERSTORAGEMULTISAMPLEPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


