// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIBI2UIPROC {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(PFNGLVERTEXATTRIBI2UIPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBI2UIPROC.class, fi, constants$154.PFNGLVERTEXATTRIBI2UIPROC$FUNC, "(III)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIBI2UIPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBI2UIPROC.class, fi, constants$154.PFNGLVERTEXATTRIBI2UIPROC$FUNC, "(III)V", scope);
    }
    static PFNGLVERTEXATTRIBI2UIPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$154.PFNGLVERTEXATTRIBI2UIPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


