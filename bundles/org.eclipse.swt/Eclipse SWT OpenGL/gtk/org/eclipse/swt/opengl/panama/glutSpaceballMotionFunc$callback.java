// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface glutSpaceballMotionFunc$callback {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(glutSpaceballMotionFunc$callback fi) {
        return RuntimeHelper.upcallStub(glutSpaceballMotionFunc$callback.class, fi, constants$948.glutSpaceballMotionFunc$callback$FUNC, "(III)V");
    }
    static MemoryAddress allocate(glutSpaceballMotionFunc$callback fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(glutSpaceballMotionFunc$callback.class, fi, constants$948.glutSpaceballMotionFunc$callback$FUNC, "(III)V", scope);
    }
    static glutSpaceballMotionFunc$callback ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$948.glutSpaceballMotionFunc$callback$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


