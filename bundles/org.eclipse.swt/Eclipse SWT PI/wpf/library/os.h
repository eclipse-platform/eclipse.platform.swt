/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

#using <mscorlib.dll>

using namespace System;
using namespace System::ComponentModel;
using namespace System::Collections;
using namespace System::Collections::ObjectModel;
using namespace System::Globalization;
using namespace System::Reflection;
using namespace System::Windows;
using namespace System::Windows::Data;
using namespace System::Windows::Documents;
using namespace System::Runtime::InteropServices;
using namespace System::Windows::Input;
using namespace System::Windows::Interop;
using namespace System::Windows::Shapes;
using namespace System::Windows::Media;
using namespace System::Windows::Media::Animation;
using namespace System::Windows::Media::Effects;
using namespace System::Windows::Media::Imaging;
using namespace System::Windows::Media::TextFormatting;
using namespace System::Windows::Controls;
using namespace System::Windows::Controls::Primitives;
using namespace System::Windows::Threading;
using namespace Microsoft::Win32;
using namespace System::Windows::Markup;
using namespace System::Xml;

/* This is need to avoid clr compiler warnings */
extern "C" {
struct _jfieldID {
};
struct _jmethodID {
};
}

#define GCHANDLE_TABLE

#ifdef GCHANDLE_TABLE

extern "C" {
extern jint GCHandle_GetHandle(Object^obj);
}
#define TO_HANDLE(arg) GCHandle_GetHandle(arg)
//#define TO_HANDLE(arg) (arg != nullptr ? (int)GCHandle::ToIntPtr(GCHandle::Alloc(arg)) : 0)
#define TO_OBJECT(arg) (arg != 0 ? (GCHandle::FromIntPtr((IntPtr)arg)).Target : nullptr)
#define FREE_HANDLE(arg) if (arg != 0) (GCHandle::FromIntPtr((IntPtr)arg)).Free()

#else

extern "C" {
extern int SWTObjectTable_ToHandle(Object^obj);
extern Object^ SWTObjectTable_ToObject(int handle);
extern void SWTObjectTable_Free(int handle);
}
#define TO_HANDLE(arg) SWTObjectTable_ToHandle(arg)
#define TO_OBJECT(arg) SWTObjectTable_ToObject(arg)
#define FREE_HANDLE(arg) SWTObjectTable_Free(arg)

#endif

#ifndef NATIVE_STATS
#define OS_NATIVE_ENTER(env, that, func) \
	try {  
#define OS_NATIVE_EXIT(env, that, func) \
	} catch (Exception^ e) { \
		jclass threadClass = env->FindClass("java/lang/Thread");  \
		jmethodID dumpStackID = env->GetStaticMethodID(threadClass, "dumpStack", "()V");  \
		System::Console::Error->WriteLine(e); \
		System::Console::Error->WriteLine("Java: "); \
		if (dumpStackID != NULL) env->CallStaticVoidMethod(threadClass, dumpStackID, 0);  \
	}
#endif

#endif /* INC_wpf_H */
