// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLINSTRUMENTSBUFFERSGIXPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLINSTRUMENTSBUFFERSGIXPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLINSTRUMENTSBUFFERSGIXPROC.class, fi, constants$890.PFNGLINSTRUMENTSBUFFERSGIXPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLINSTRUMENTSBUFFERSGIXPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLINSTRUMENTSBUFFERSGIXPROC.class, fi, constants$890.PFNGLINSTRUMENTSBUFFERSGIXPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLINSTRUMENTSBUFFERSGIXPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$890.PFNGLINSTRUMENTSBUFFERSGIXPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


