// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC {

    void apply(int x0, int x1, int x2, int x3, int x4, int x5, long x6);
    static MemoryAddress allocate(PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC.class, fi, constants$606.PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC$FUNC, "(IIIIIIJ)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC.class, fi, constants$606.PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC$FUNC, "(IIIIIIJ)V", scope);
    }
    static PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4, int x5, long x6) -> {
            try {
                constants$606.PFNGLVERTEXARRAYVERTEXATTRIBIOFFSETEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


