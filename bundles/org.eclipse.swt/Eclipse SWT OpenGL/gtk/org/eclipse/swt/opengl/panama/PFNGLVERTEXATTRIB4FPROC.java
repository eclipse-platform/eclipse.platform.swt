// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIB4FPROC {

    void apply(int x0, float x1, float x2, float x3, float x4);
    static MemoryAddress allocate(PFNGLVERTEXATTRIB4FPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB4FPROC.class, fi, constants$141.PFNGLVERTEXATTRIB4FPROC$FUNC, "(IFFFF)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIB4FPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB4FPROC.class, fi, constants$141.PFNGLVERTEXATTRIB4FPROC$FUNC, "(IFFFF)V", scope);
    }
    static PFNGLVERTEXATTRIB4FPROC ofAddress(MemoryAddress addr) {
        return (int x0, float x1, float x2, float x3, float x4) -> {
            try {
                constants$141.PFNGLVERTEXATTRIB4FPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


