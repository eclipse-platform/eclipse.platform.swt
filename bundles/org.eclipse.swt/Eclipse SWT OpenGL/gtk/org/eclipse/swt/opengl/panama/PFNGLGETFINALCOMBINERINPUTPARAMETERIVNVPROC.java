// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC.class, fi, constants$810.PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC.class, fi, constants$810.PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$810.PFNGLGETFINALCOMBINERINPUTPARAMETERIVNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


