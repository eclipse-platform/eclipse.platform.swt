// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$60 {

    static final FunctionDescriptor glResetHistogram$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle glResetHistogram$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glResetHistogram",
        "(I)V",
        constants$60.glResetHistogram$FUNC, false
    );
    static final FunctionDescriptor glGetHistogram$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_CHAR,
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle glGetHistogram$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glGetHistogram",
        "(IBIILjdk/incubator/foreign/MemoryAddress;)V",
        constants$60.glGetHistogram$FUNC, false
    );
    static final FunctionDescriptor glGetHistogramParameterfv$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle glGetHistogramParameterfv$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glGetHistogramParameterfv",
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$60.glGetHistogramParameterfv$FUNC, false
    );
    static final FunctionDescriptor glGetHistogramParameteriv$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle glGetHistogramParameteriv$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glGetHistogramParameteriv",
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$60.glGetHistogramParameteriv$FUNC, false
    );
    static final FunctionDescriptor glMinmax$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_CHAR
    );
    static final MethodHandle glMinmax$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glMinmax",
        "(IIB)V",
        constants$60.glMinmax$FUNC, false
    );
    static final FunctionDescriptor glResetMinmax$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle glResetMinmax$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glResetMinmax",
        "(I)V",
        constants$60.glResetMinmax$FUNC, false
    );
}


