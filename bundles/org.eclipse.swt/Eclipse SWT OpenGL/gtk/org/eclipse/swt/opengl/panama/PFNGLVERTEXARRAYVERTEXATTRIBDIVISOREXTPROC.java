// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC.class, fi, constants$621.PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC$FUNC, "(III)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC.class, fi, constants$621.PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC$FUNC, "(III)V", scope);
    }
    static PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$621.PFNGLVERTEXARRAYVERTEXATTRIBDIVISOREXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


