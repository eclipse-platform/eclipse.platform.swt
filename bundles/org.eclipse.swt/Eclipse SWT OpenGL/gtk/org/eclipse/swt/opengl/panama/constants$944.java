// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$944 {

    static final FunctionDescriptor glutPassiveMotionFunc$callback$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT
    );
    static final MethodHandle glutPassiveMotionFunc$callback$MH = RuntimeHelper.downcallHandle(
        "(II)V",
        constants$944.glutPassiveMotionFunc$callback$FUNC, false
    );
    static final FunctionDescriptor glutPassiveMotionFunc$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle glutPassiveMotionFunc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glutPassiveMotionFunc",
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$944.glutPassiveMotionFunc$FUNC, false
    );
    static final FunctionDescriptor glutEntryFunc$callback$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle glutEntryFunc$callback$MH = RuntimeHelper.downcallHandle(
        "(I)V",
        constants$944.glutEntryFunc$callback$FUNC, false
    );
    static final FunctionDescriptor glutEntryFunc$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle glutEntryFunc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glutEntryFunc",
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$944.glutEntryFunc$FUNC, false
    );
}


