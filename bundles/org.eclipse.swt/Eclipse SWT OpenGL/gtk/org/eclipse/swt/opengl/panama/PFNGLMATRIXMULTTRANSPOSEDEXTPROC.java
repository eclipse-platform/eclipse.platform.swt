// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLMATRIXMULTTRANSPOSEDEXTPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLMATRIXMULTTRANSPOSEDEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXMULTTRANSPOSEDEXTPROC.class, fi, constants$570.PFNGLMATRIXMULTTRANSPOSEDEXTPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLMATRIXMULTTRANSPOSEDEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXMULTTRANSPOSEDEXTPROC.class, fi, constants$570.PFNGLMATRIXMULTTRANSPOSEDEXTPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLMATRIXMULTTRANSPOSEDEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$570.PFNGLMATRIXMULTTRANSPOSEDEXTPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


