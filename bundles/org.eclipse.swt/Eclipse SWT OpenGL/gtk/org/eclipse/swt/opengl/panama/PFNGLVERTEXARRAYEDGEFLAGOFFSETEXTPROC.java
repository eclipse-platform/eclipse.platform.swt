// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC {

    void apply(int x0, int x1, int x2, long x3);
    static MemoryAddress allocate(PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC.class, fi, constants$603.PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC$FUNC, "(IIIJ)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC.class, fi, constants$603.PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC$FUNC, "(IIIJ)V", scope);
    }
    static PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, long x3) -> {
            try {
                constants$603.PFNGLVERTEXARRAYEDGEFLAGOFFSETEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


