// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$919 {

    static final FunctionDescriptor glBlendEquationSeparateATI$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT
    );
    static final MethodHandle glBlendEquationSeparateATI$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "glBlendEquationSeparateATI",
        "(II)V",
        constants$919.glBlendEquationSeparateATI$FUNC, false
    );
    static final FunctionDescriptor PFNGLBLENDEQUATIONSEPARATEATIPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT
    );
    static final MethodHandle PFNGLBLENDEQUATIONSEPARATEATIPROC$MH = RuntimeHelper.downcallHandle(
        "(II)V",
        constants$919.PFNGLBLENDEQUATIONSEPARATEATIPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLEGLIMAGETARGETTEXTURE2DOESPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLEGLIMAGETARGETTEXTURE2DOESPROC$MH = RuntimeHelper.downcallHandle(
        "(ILjdk/incubator/foreign/MemoryAddress;)V",
        constants$919.PFNGLEGLIMAGETARGETTEXTURE2DOESPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLEGLIMAGETARGETRENDERBUFFERSTORAGEOESPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_POINTER
    );
}


