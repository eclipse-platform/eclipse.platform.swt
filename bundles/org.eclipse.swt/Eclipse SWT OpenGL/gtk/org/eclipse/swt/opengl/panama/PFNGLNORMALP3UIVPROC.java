// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLNORMALP3UIVPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLNORMALP3UIVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLNORMALP3UIVPROC.class, fi, constants$202.PFNGLNORMALP3UIVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLNORMALP3UIVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLNORMALP3UIVPROC.class, fi, constants$202.PFNGLNORMALP3UIVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLNORMALP3UIVPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$202.PFNGLNORMALP3UIVPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


