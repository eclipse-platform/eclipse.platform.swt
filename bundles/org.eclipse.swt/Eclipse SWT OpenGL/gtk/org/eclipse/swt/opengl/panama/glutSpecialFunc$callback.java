// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface glutSpecialFunc$callback {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(glutSpecialFunc$callback fi) {
        return RuntimeHelper.upcallStub(glutSpecialFunc$callback.class, fi, constants$941.glutSpecialFunc$callback$FUNC, "(III)V");
    }
    static MemoryAddress allocate(glutSpecialFunc$callback fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(glutSpecialFunc$callback.class, fi, constants$941.glutSpecialFunc$callback$FUNC, "(III)V", scope);
    }
    static glutSpecialFunc$callback ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$941.glutSpecialFunc$callback$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


