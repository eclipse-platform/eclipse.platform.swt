// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPASSTHROUGHXOESPROC {

    void apply(int x0);
    static MemoryAddress allocate(PFNGLPASSTHROUGHXOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPASSTHROUGHXOESPROC.class, fi, constants$439.PFNGLPASSTHROUGHXOESPROC$FUNC, "(I)V");
    }
    static MemoryAddress allocate(PFNGLPASSTHROUGHXOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPASSTHROUGHXOESPROC.class, fi, constants$439.PFNGLPASSTHROUGHXOESPROC$FUNC, "(I)V", scope);
    }
    static PFNGLPASSTHROUGHXOESPROC ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                constants$439.PFNGLPASSTHROUGHXOESPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


