// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLNORMALPOINTERVINTELPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLNORMALPOINTERVINTELPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLNORMALPOINTERVINTELPROC.class, fi, constants$707.PFNGLNORMALPOINTERVINTELPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLNORMALPOINTERVINTELPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLNORMALPOINTERVINTELPROC.class, fi, constants$707.PFNGLNORMALPOINTERVINTELPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLNORMALPOINTERVINTELPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$707.PFNGLNORMALPOINTERVINTELPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


