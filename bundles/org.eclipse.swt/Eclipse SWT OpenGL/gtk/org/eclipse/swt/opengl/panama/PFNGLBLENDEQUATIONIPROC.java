// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLBLENDEQUATIONIPROC {

    void apply(int x0, int x1);
    static MemoryAddress allocate(PFNGLBLENDEQUATIONIPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLBLENDEQUATIONIPROC.class, fi, constants$205.PFNGLBLENDEQUATIONIPROC$FUNC, "(II)V");
    }
    static MemoryAddress allocate(PFNGLBLENDEQUATIONIPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLBLENDEQUATIONIPROC.class, fi, constants$205.PFNGLBLENDEQUATIONIPROC$FUNC, "(II)V", scope);
    }
    static PFNGLBLENDEQUATIONIPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1) -> {
            try {
                constants$205.PFNGLBLENDEQUATIONIPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


