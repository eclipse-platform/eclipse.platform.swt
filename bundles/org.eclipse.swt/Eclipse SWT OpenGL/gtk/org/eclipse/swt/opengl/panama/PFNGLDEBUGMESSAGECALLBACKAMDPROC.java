// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLDEBUGMESSAGECALLBACKAMDPROC {

    void apply(jdk.incubator.foreign.MemoryAddress x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLDEBUGMESSAGECALLBACKAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLDEBUGMESSAGECALLBACKAMDPROC.class, fi, constants$452.PFNGLDEBUGMESSAGECALLBACKAMDPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLDEBUGMESSAGECALLBACKAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLDEBUGMESSAGECALLBACKAMDPROC.class, fi, constants$452.PFNGLDEBUGMESSAGECALLBACKAMDPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLDEBUGMESSAGECALLBACKAMDPROC ofAddress(MemoryAddress addr) {
        return (jdk.incubator.foreign.MemoryAddress x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$452.PFNGLDEBUGMESSAGECALLBACKAMDPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


