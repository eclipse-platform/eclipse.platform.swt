// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETTEXPARAMETERIIVPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETTEXPARAMETERIIVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETTEXPARAMETERIIVPROC.class, fi, constants$163.PFNGLGETTEXPARAMETERIIVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETTEXPARAMETERIIVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETTEXPARAMETERIIVPROC.class, fi, constants$163.PFNGLGETTEXPARAMETERIIVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETTEXPARAMETERIIVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$163.PFNGLGETTEXPARAMETERIIVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


