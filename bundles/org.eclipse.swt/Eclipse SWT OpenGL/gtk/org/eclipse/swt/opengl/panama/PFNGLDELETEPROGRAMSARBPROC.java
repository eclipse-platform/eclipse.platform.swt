// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLDELETEPROGRAMSARBPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLDELETEPROGRAMSARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLDELETEPROGRAMSARBPROC.class, fi, constants$324.PFNGLDELETEPROGRAMSARBPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLDELETEPROGRAMSARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLDELETEPROGRAMSARBPROC.class, fi, constants$324.PFNGLDELETEPROGRAMSARBPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLDELETEPROGRAMSARBPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$324.PFNGLDELETEPROGRAMSARBPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


