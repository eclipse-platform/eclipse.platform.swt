// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC {

    void apply(int x0, long x1, long x2);
    static MemoryAddress allocate(PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC.class, fi, constants$480.PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC$FUNC, "(IJJ)V");
    }
    static MemoryAddress allocate(PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC.class, fi, constants$480.PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC$FUNC, "(IJJ)V", scope);
    }
    static PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC ofAddress(MemoryAddress addr) {
        return (int x0, long x1, long x2) -> {
            try {
                constants$480.PFNGLFLUSHMAPPEDBUFFERRANGEAPPLEPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


