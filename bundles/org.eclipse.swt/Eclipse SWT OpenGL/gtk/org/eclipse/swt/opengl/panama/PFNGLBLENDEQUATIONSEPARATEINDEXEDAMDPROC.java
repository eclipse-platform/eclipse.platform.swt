// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC.class, fi, constants$454.PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC$FUNC, "(III)V");
    }
    static MemoryAddress allocate(PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC.class, fi, constants$454.PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC$FUNC, "(III)V", scope);
    }
    static PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$454.PFNGLBLENDEQUATIONSEPARATEINDEXEDAMDPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


