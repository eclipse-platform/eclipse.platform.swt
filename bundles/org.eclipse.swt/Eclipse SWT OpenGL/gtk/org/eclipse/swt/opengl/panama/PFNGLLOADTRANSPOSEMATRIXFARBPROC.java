// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLLOADTRANSPOSEMATRIXFARBPROC {

    void apply(jdk.incubator.foreign.MemoryAddress x0);
    static MemoryAddress allocate(PFNGLLOADTRANSPOSEMATRIXFARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLLOADTRANSPOSEMATRIXFARBPROC.class, fi, constants$377.PFNGLLOADTRANSPOSEMATRIXFARBPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLLOADTRANSPOSEMATRIXFARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLLOADTRANSPOSEMATRIXFARBPROC.class, fi, constants$377.PFNGLLOADTRANSPOSEMATRIXFARBPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLLOADTRANSPOSEMATRIXFARBPROC ofAddress(MemoryAddress addr) {
        return (jdk.incubator.foreign.MemoryAddress x0) -> {
            try {
                constants$377.PFNGLLOADTRANSPOSEMATRIXFARBPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


