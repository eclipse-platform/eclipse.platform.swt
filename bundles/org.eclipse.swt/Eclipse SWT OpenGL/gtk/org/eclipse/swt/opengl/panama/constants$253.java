// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$253 {

    static final FunctionDescriptor PFNGLDRAWTRANSFORMFEEDBACKINSTANCEDPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT
    );
    static final MethodHandle PFNGLDRAWTRANSFORMFEEDBACKINSTANCEDPROC$MH = RuntimeHelper.downcallHandle(
        "(III)V",
        constants$253.PFNGLDRAWTRANSFORMFEEDBACKINSTANCEDPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLDRAWTRANSFORMFEEDBACKSTREAMINSTANCEDPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT,
        C_INT
    );
    static final MethodHandle PFNGLDRAWTRANSFORMFEEDBACKSTREAMINSTANCEDPROC$MH = RuntimeHelper.downcallHandle(
        "(IIII)V",
        constants$253.PFNGLDRAWTRANSFORMFEEDBACKSTREAMINSTANCEDPROC$FUNC, false
    );
    static final FunctionDescriptor GLDEBUGPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_POINTER,
        C_POINTER
    );
    static final MethodHandle GLDEBUGPROC$MH = RuntimeHelper.downcallHandle(
        "(IIIIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$253.GLDEBUGPROC$FUNC, false
    );
}


