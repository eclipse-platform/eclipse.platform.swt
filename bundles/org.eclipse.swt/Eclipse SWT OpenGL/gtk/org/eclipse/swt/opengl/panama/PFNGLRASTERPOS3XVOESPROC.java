// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLRASTERPOS3XVOESPROC {

    void apply(jdk.incubator.foreign.MemoryAddress x0);
    static MemoryAddress allocate(PFNGLRASTERPOS3XVOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLRASTERPOS3XVOESPROC.class, fi, constants$442.PFNGLRASTERPOS3XVOESPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLRASTERPOS3XVOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLRASTERPOS3XVOESPROC.class, fi, constants$442.PFNGLRASTERPOS3XVOESPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLRASTERPOS3XVOESPROC ofAddress(MemoryAddress addr) {
        return (jdk.incubator.foreign.MemoryAddress x0) -> {
            try {
                constants$442.PFNGLRASTERPOS3XVOESPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


