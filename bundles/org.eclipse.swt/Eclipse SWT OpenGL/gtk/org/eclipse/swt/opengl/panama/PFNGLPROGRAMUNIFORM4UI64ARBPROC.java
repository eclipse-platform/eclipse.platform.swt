// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPROGRAMUNIFORM4UI64ARBPROC {

    void apply(int x0, int x1, long x2, long x3, long x4, long x5);
    static MemoryAddress allocate(PFNGLPROGRAMUNIFORM4UI64ARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMUNIFORM4UI64ARBPROC.class, fi, constants$342.PFNGLPROGRAMUNIFORM4UI64ARBPROC$FUNC, "(IIJJJJ)V");
    }
    static MemoryAddress allocate(PFNGLPROGRAMUNIFORM4UI64ARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPROGRAMUNIFORM4UI64ARBPROC.class, fi, constants$342.PFNGLPROGRAMUNIFORM4UI64ARBPROC$FUNC, "(IIJJJJ)V", scope);
    }
    static PFNGLPROGRAMUNIFORM4UI64ARBPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, long x2, long x3, long x4, long x5) -> {
            try {
                constants$342.PFNGLPROGRAMUNIFORM4UI64ARBPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


