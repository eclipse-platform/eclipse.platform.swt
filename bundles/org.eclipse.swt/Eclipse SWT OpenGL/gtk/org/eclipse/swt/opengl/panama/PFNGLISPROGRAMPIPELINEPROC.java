// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLISPROGRAMPIPELINEPROC {

    byte apply(int x0);
    static MemoryAddress allocate(PFNGLISPROGRAMPIPELINEPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLISPROGRAMPIPELINEPROC.class, fi, constants$225.PFNGLISPROGRAMPIPELINEPROC$FUNC, "(I)B");
    }
    static MemoryAddress allocate(PFNGLISPROGRAMPIPELINEPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLISPROGRAMPIPELINEPROC.class, fi, constants$225.PFNGLISPROGRAMPIPELINEPROC$FUNC, "(I)B", scope);
    }
    static PFNGLISPROGRAMPIPELINEPROC ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                return (byte)constants$225.PFNGLISPROGRAMPIPELINEPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


