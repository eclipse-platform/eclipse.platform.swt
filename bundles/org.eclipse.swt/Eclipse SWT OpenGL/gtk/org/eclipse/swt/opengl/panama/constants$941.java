// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$941 {

    static final FunctionDescriptor glutSpecialFunc$callback$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT
    );
    static final MethodHandle glutSpecialFunc$callback$MH = RuntimeHelper.downcallHandle(
        "(III)V",
        constants$941.glutSpecialFunc$callback$FUNC, false
    );
    static final FunctionDescriptor glutSpecialFunc$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle glutSpecialFunc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glutSpecialFunc",
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$941.glutSpecialFunc$FUNC, false
    );
    static final FunctionDescriptor glutReshapeFunc$callback$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT
    );
    static final MethodHandle glutReshapeFunc$callback$MH = RuntimeHelper.downcallHandle(
        "(II)V",
        constants$941.glutReshapeFunc$callback$FUNC, false
    );
    static final FunctionDescriptor glutReshapeFunc$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle glutReshapeFunc$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glutReshapeFunc",
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$941.glutReshapeFunc$FUNC, false
    );
}


