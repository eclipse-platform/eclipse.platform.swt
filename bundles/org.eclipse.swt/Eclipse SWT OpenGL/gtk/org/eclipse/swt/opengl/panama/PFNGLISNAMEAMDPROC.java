// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLISNAMEAMDPROC {

    byte apply(int x0, int x1);
    static MemoryAddress allocate(PFNGLISNAMEAMDPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLISNAMEAMDPROC.class, fi, constants$469.PFNGLISNAMEAMDPROC$FUNC, "(II)B");
    }
    static MemoryAddress allocate(PFNGLISNAMEAMDPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLISNAMEAMDPROC.class, fi, constants$469.PFNGLISNAMEAMDPROC$FUNC, "(II)B", scope);
    }
    static PFNGLISNAMEAMDPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1) -> {
            try {
                return (byte)constants$469.PFNGLISNAMEAMDPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


