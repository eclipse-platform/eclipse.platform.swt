// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLINVALIDATEBUFFERSUBDATAPROC {

    void apply(int x0, long x1, long x2);
    static MemoryAddress allocate(PFNGLINVALIDATEBUFFERSUBDATAPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLINVALIDATEBUFFERSUBDATAPROC.class, fi, constants$257.PFNGLINVALIDATEBUFFERSUBDATAPROC$FUNC, "(IJJ)V");
    }
    static MemoryAddress allocate(PFNGLINVALIDATEBUFFERSUBDATAPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLINVALIDATEBUFFERSUBDATAPROC.class, fi, constants$257.PFNGLINVALIDATEBUFFERSUBDATAPROC$FUNC, "(IJJ)V", scope);
    }
    static PFNGLINVALIDATEBUFFERSUBDATAPROC ofAddress(MemoryAddress addr) {
        return (int x0, long x1, long x2) -> {
            try {
                constants$257.PFNGLINVALIDATEBUFFERSUBDATAPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


