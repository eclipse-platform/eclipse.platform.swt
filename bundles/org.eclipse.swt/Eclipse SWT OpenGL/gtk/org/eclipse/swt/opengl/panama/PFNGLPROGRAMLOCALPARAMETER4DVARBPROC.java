// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPROGRAMLOCALPARAMETER4DVARBPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLPROGRAMLOCALPARAMETER4DVARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMLOCALPARAMETER4DVARBPROC.class, fi, constants$327.PFNGLPROGRAMLOCALPARAMETER4DVARBPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLPROGRAMLOCALPARAMETER4DVARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMLOCALPARAMETER4DVARBPROC.class, fi, constants$327.PFNGLPROGRAMLOCALPARAMETER4DVARBPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLPROGRAMLOCALPARAMETER4DVARBPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$327.PFNGLPROGRAMLOCALPARAMETER4DVARBPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


