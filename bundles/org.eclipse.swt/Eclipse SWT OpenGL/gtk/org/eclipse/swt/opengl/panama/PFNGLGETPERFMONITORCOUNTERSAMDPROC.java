// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETPERFMONITORCOUNTERSAMDPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1, jdk.incubator.foreign.MemoryAddress x2, int x3, jdk.incubator.foreign.MemoryAddress x4);
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERSAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERSAMDPROC.class, fi, constants$470.PFNGLGETPERFMONITORCOUNTERSAMDPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETPERFMONITORCOUNTERSAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETPERFMONITORCOUNTERSAMDPROC.class, fi, constants$470.PFNGLGETPERFMONITORCOUNTERSAMDPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETPERFMONITORCOUNTERSAMDPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1, jdk.incubator.foreign.MemoryAddress x2, int x3, jdk.incubator.foreign.MemoryAddress x4) -> {
            try {
                constants$470.PFNGLGETPERFMONITORCOUNTERSAMDPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


