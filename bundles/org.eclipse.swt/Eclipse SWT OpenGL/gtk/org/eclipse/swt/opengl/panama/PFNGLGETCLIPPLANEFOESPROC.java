// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETCLIPPLANEFOESPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLGETCLIPPLANEFOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETCLIPPLANEFOESPROC.class, fi, constants$450.PFNGLGETCLIPPLANEFOESPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETCLIPPLANEFOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETCLIPPLANEFOESPROC.class, fi, constants$450.PFNGLGETCLIPPLANEFOESPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETCLIPPLANEFOESPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$450.PFNGLGETCLIPPLANEFOESPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


