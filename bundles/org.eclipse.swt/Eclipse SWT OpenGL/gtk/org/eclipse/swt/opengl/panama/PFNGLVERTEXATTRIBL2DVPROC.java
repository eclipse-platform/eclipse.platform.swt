// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIBL2DVPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLVERTEXATTRIBL2DVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBL2DVPROC.class, fi, constants$244.PFNGLVERTEXATTRIBL2DVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIBL2DVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBL2DVPROC.class, fi, constants$244.PFNGLVERTEXATTRIBL2DVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLVERTEXATTRIBL2DVPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$244.PFNGLVERTEXATTRIBL2DVPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


