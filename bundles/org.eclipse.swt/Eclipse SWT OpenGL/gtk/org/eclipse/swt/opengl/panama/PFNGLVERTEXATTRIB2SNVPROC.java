// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIB2SNVPROC {

    void apply(int x0, short x1, short x2);
    static MemoryAddress allocate(PFNGLVERTEXATTRIB2SNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB2SNVPROC.class, fi, constants$851.PFNGLVERTEXATTRIB2SNVPROC$FUNC, "(ISS)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIB2SNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB2SNVPROC.class, fi, constants$851.PFNGLVERTEXATTRIB2SNVPROC$FUNC, "(ISS)V", scope);
    }
    static PFNGLVERTEXATTRIB2SNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, short x1, short x2) -> {
            try {
                constants$851.PFNGLVERTEXATTRIB2SNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


