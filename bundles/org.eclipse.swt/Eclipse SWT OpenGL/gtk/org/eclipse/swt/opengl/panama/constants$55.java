// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$55 {

    static final FunctionDescriptor glPassThrough$FUNC = FunctionDescriptor.ofVoid(
        C_FLOAT
    );
    static final MethodHandle glPassThrough$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glPassThrough",
        "(F)V",
        constants$55.glPassThrough$FUNC, false
    );
    static final FunctionDescriptor glSelectBuffer$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_POINTER
    );
    static final MethodHandle glSelectBuffer$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glSelectBuffer",
        "(ILjdk/incubator/foreign/MemoryAddress;)V",
        constants$55.glSelectBuffer$FUNC, false
    );
    static final FunctionDescriptor glInitNames$FUNC = FunctionDescriptor.ofVoid();
    static final MethodHandle glInitNames$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glInitNames",
        "()V",
        constants$55.glInitNames$FUNC, false
    );
    static final FunctionDescriptor glLoadName$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle glLoadName$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glLoadName",
        "(I)V",
        constants$55.glLoadName$FUNC, false
    );
    static final FunctionDescriptor glPushName$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle glPushName$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glPushName",
        "(I)V",
        constants$55.glPushName$FUNC, false
    );
    static final FunctionDescriptor glPopName$FUNC = FunctionDescriptor.ofVoid();
    static final MethodHandle glPopName$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glPopName",
        "()V",
        constants$55.glPopName$FUNC, false
    );
}


