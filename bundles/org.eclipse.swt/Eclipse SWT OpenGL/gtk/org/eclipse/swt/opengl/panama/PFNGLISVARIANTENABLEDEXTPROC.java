// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLISVARIANTENABLEDEXTPROC {

    byte apply(int x0, int x1);
    static MemoryAddress allocate(PFNGLISVARIANTENABLEDEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLISVARIANTENABLEDEXTPROC.class, fi, constants$693.PFNGLISVARIANTENABLEDEXTPROC$FUNC, "(II)B");
    }
    static MemoryAddress allocate(PFNGLISVARIANTENABLEDEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLISVARIANTENABLEDEXTPROC.class, fi, constants$693.PFNGLISVARIANTENABLEDEXTPROC$FUNC, "(II)B", scope);
    }
    static PFNGLISVARIANTENABLEDEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1) -> {
            try {
                return (byte)constants$693.PFNGLISVARIANTENABLEDEXTPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


