// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLMATRIXMULT3X2FNVPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLMATRIXMULT3X2FNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXMULT3X2FNVPROC.class, fi, constants$795.PFNGLMATRIXMULT3X2FNVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLMATRIXMULT3X2FNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXMULT3X2FNVPROC.class, fi, constants$795.PFNGLMATRIXMULT3X2FNVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLMATRIXMULT3X2FNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$795.PFNGLMATRIXMULT3X2FNVPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


