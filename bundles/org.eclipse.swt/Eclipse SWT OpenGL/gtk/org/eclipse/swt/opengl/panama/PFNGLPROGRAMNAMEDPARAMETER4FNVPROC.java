// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPROGRAMNAMEDPARAMETER4FNVPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2, float x3, float x4, float x5, float x6);
    static MemoryAddress allocate(PFNGLPROGRAMNAMEDPARAMETER4FNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMNAMEDPARAMETER4FNVPROC.class, fi, constants$746.PFNGLPROGRAMNAMEDPARAMETER4FNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;FFFF)V");
    }
    static MemoryAddress allocate(PFNGLPROGRAMNAMEDPARAMETER4FNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMNAMEDPARAMETER4FNVPROC.class, fi, constants$746.PFNGLPROGRAMNAMEDPARAMETER4FNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;FFFF)V", scope);
    }
    static PFNGLPROGRAMNAMEDPARAMETER4FNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2, float x3, float x4, float x5, float x6) -> {
            try {
                constants$746.PFNGLPROGRAMNAMEDPARAMETER4FNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


