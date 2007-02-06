/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_wpf_H
#define INC_wpf_H

#include <string.h>
#include <windows.h>

#using <mscorlib.dll>

using namespace System;
using namespace System::ComponentModel;
using namespace System::Collections;
using namespace System::Globalization;
using namespace System::Windows;
using namespace System::Windows::Data;
using namespace System::Windows::Documents;
using namespace System::Runtime::InteropServices;
using namespace System::Windows::Input;
//using namespace System::Windows::Interop;
using namespace System::Windows::Shapes;
using namespace System::Windows::Media;
using namespace System::Windows::Media::Animation;
using namespace System::Windows::Media::Imaging;
using namespace System::Windows::Media::TextFormatting;
using namespace System::Windows::Controls;
using namespace System::Windows::Controls::Primitives;
using namespace System::Windows::Threading;
using namespace Microsoft::Win32;

extern "C" {
extern jint GCHandle_GetHandle(Object^obj);
}
#define TO_HANDLE(arg) GCHandle_GetHandle(arg)
#define TO_HANDLE_STRUCT(arg) GCHandle_GetHandle(arg)
//#define TO_HANDLE(arg) (arg != nullptr ? (int)GCHandle::ToIntPtr(GCHandle::Alloc(arg)) : 0)
//#define TO_HANDLE_STRUCT(arg) (int)GCHandle::ToIntPtr(GCHandle::Alloc(arg))
#define TO_OBJECT(arg) (arg != 0 ? (GCHandle::FromIntPtr((IntPtr)arg)).Target : nullptr)

#ifndef NATIVE_STATS
#define OS_NATIVE_ENTER(env, that, func) \
	try {  
#define OS_NATIVE_EXIT(env, that, func) \
	} catch (Exception^ e) { \
		jclass threadClass = env->FindClass("java/lang/Thread");  \
		jmethodID dumpStackID = env->GetStaticMethodID(threadClass, "dumpStack", "()V");  \
		System::Console::WriteLine(e); \
		System::Console::WriteLine("Java: "); \
		if (dumpStackID != NULL) env->CallStaticVoidMethod(threadClass, dumpStackID, 0);  \
	}
#endif

#endif /* INC_wpf_H */
