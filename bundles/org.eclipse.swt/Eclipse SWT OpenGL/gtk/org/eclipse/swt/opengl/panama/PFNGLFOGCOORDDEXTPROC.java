// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLFOGCOORDDEXTPROC {

    void apply(double x0);
    static MemoryAddress allocate(PFNGLFOGCOORDDEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLFOGCOORDDEXTPROC.class, fi, constants$624.PFNGLFOGCOORDDEXTPROC$FUNC, "(D)V");
    }
    static MemoryAddress allocate(PFNGLFOGCOORDDEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLFOGCOORDDEXTPROC.class, fi, constants$624.PFNGLFOGCOORDDEXTPROC$FUNC, "(D)V", scope);
    }
    static PFNGLFOGCOORDDEXTPROC ofAddress(MemoryAddress addr) {
        return (double x0) -> {
            try {
                constants$624.PFNGLFOGCOORDDEXTPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


