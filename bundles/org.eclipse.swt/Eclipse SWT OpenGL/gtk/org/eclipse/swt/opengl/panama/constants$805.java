// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$805 {

    static final FunctionDescriptor PFNGLPRIMITIVERESTARTINDEXNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT
    );
    static final MethodHandle PFNGLPRIMITIVERESTARTINDEXNVPROC$MH = RuntimeHelper.downcallHandle(
        "(I)V",
        constants$805.PFNGLPRIMITIVERESTARTINDEXNVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLQUERYRESOURCENVPROC$FUNC = FunctionDescriptor.of(C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLQUERYRESOURCENVPROC$MH = RuntimeHelper.downcallHandle(
        "(IIILjdk/incubator/foreign/MemoryAddress;)I",
        constants$805.PFNGLQUERYRESOURCENVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLGENQUERYRESOURCETAGNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLGENQUERYRESOURCETAGNVPROC$MH = RuntimeHelper.downcallHandle(
        "(ILjdk/incubator/foreign/MemoryAddress;)V",
        constants$805.PFNGLGENQUERYRESOURCETAGNVPROC$FUNC, false
    );
}


