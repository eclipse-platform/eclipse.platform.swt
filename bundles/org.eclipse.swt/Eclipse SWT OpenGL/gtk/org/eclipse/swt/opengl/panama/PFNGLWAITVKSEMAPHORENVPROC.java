// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLWAITVKSEMAPHORENVPROC {

    void apply(long x0);
    static MemoryAddress allocate(PFNGLWAITVKSEMAPHORENVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLWAITVKSEMAPHORENVPROC.class, fi, constants$739.PFNGLWAITVKSEMAPHORENVPROC$FUNC, "(J)V");
    }
    static MemoryAddress allocate(PFNGLWAITVKSEMAPHORENVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLWAITVKSEMAPHORENVPROC.class, fi, constants$739.PFNGLWAITVKSEMAPHORENVPROC$FUNC, "(J)V", scope);
    }
    static PFNGLWAITVKSEMAPHORENVPROC ofAddress(MemoryAddress addr) {
        return (long x0) -> {
            try {
                constants$739.PFNGLWAITVKSEMAPHORENVPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


