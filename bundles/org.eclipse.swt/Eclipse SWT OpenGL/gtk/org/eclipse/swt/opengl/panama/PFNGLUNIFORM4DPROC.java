// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLUNIFORM4DPROC {

    void apply(int x0, double x1, double x2, double x3, double x4);
    static MemoryAddress allocate(PFNGLUNIFORM4DPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLUNIFORM4DPROC.class, fi, constants$208.PFNGLUNIFORM4DPROC$FUNC, "(IDDDD)V");
    }
    static MemoryAddress allocate(PFNGLUNIFORM4DPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLUNIFORM4DPROC.class, fi, constants$208.PFNGLUNIFORM4DPROC$FUNC, "(IDDDD)V", scope);
    }
    static PFNGLUNIFORM4DPROC ofAddress(MemoryAddress addr) {
        return (int x0, double x1, double x2, double x3, double x4) -> {
            try {
                constants$208.PFNGLUNIFORM4DPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


