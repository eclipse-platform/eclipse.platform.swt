// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIB4NUBPROC {

    void apply(int x0, byte x1, byte x2, byte x3, byte x4);
    static MemoryAddress allocate(PFNGLVERTEXATTRIB4NUBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB4NUBPROC.class, fi, constants$139.PFNGLVERTEXATTRIB4NUBPROC$FUNC, "(IBBBB)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIB4NUBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB4NUBPROC.class, fi, constants$139.PFNGLVERTEXATTRIB4NUBPROC$FUNC, "(IBBBB)V", scope);
    }
    static PFNGLVERTEXATTRIB4NUBPROC ofAddress(MemoryAddress addr) {
        return (int x0, byte x1, byte x2, byte x3, byte x4) -> {
            try {
                constants$139.PFNGLVERTEXATTRIB4NUBPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


