// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIBL2DEXTPROC {

    void apply(int x0, double x1, double x2);
    static MemoryAddress allocate(PFNGLVERTEXATTRIBL2DEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBL2DEXTPROC.class, fi, constants$680.PFNGLVERTEXATTRIBL2DEXTPROC$FUNC, "(IDD)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIBL2DEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBL2DEXTPROC.class, fi, constants$680.PFNGLVERTEXATTRIBL2DEXTPROC$FUNC, "(IDD)V", scope);
    }
    static PFNGLVERTEXATTRIBL2DEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, double x1, double x2) -> {
            try {
                constants$680.PFNGLVERTEXATTRIBL2DEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


