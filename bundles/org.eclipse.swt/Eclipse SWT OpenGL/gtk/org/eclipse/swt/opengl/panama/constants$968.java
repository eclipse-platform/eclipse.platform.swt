// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$968 {

    static final FunctionDescriptor lcong48_r$FUNC = FunctionDescriptor.of(C_INT,
        C_POINTER,
        C_POINTER
    );
    static final MethodHandle lcong48_r$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "lcong48_r",
        "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)I",
        constants$968.lcong48_r$FUNC, false
    );
    static final FunctionDescriptor malloc$FUNC = FunctionDescriptor.of(C_POINTER,
        C_LONG
    );
    static final MethodHandle malloc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "malloc",
        "(J)Ljdk/incubator/foreign/MemoryAddress;",
        constants$968.malloc$FUNC, false
    );
    static final FunctionDescriptor calloc$FUNC = FunctionDescriptor.of(C_POINTER,
        C_LONG,
        C_LONG
    );
    static final MethodHandle calloc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "calloc",
        "(JJ)Ljdk/incubator/foreign/MemoryAddress;",
        constants$968.calloc$FUNC, false
    );
    static final FunctionDescriptor realloc$FUNC = FunctionDescriptor.of(C_POINTER,
        C_POINTER,
        C_LONG
    );
    static final MethodHandle realloc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "realloc",
        "(Ljdk/incubator/foreign/MemoryAddress;J)Ljdk/incubator/foreign/MemoryAddress;",
        constants$968.realloc$FUNC, false
    );
    static final FunctionDescriptor free$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle free$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "free",
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$968.free$FUNC, false
    );
    static final FunctionDescriptor alloca$FUNC = FunctionDescriptor.of(C_POINTER,
        C_LONG
    );
    static final MethodHandle alloca$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "alloca",
        "(J)Ljdk/incubator/foreign/MemoryAddress;",
        constants$968.alloca$FUNC, false
    );
}


