// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$151 {

    static final FunctionDescriptor PFNGLENDCONDITIONALRENDERPROC$FUNC = FunctionDescriptor.ofVoid();
    static final MethodHandle PFNGLENDCONDITIONALRENDERPROC$MH = RuntimeHelper.downcallHandle(
        "()V",
        constants$151.PFNGLENDCONDITIONALRENDERPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLVERTEXATTRIBIPOINTERPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLVERTEXATTRIBIPOINTERPROC$MH = RuntimeHelper.downcallHandle(
        "(IIIILjdk/incubator/foreign/MemoryAddress;)V",
        constants$151.PFNGLVERTEXATTRIBIPOINTERPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLGETVERTEXATTRIBIIVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLGETVERTEXATTRIBIIVPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$151.PFNGLGETVERTEXATTRIBIIVPROC$FUNC, false
    );
}


