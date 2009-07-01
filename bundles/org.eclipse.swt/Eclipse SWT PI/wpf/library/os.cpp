/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_wpf_OS_##func

#ifndef NO_AccessText_1AccessKey
extern "C" JNIEXPORT jchar JNICALL OS_NATIVE(AccessText_1AccessKey)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jchar JNICALL OS_NATIVE(AccessText_1AccessKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	jchar rc = 0;
	OS_NATIVE_ENTER(env, that, AccessText_1AccessKey_FUNC);
	rc = (jchar)((AccessText^)TO_OBJECT(arg0))->AccessKey;
	OS_NATIVE_EXIT(env, that, AccessText_1AccessKey_FUNC);
	return rc;
}
#endif

#ifndef NO_AccessText_1Text
extern "C" JNIEXPORT void JNICALL OS_NATIVE(AccessText_1Text)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(AccessText_1Text)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, AccessText_1Text_FUNC);
	((AccessText^)TO_OBJECT(arg0))->Text = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, AccessText_1Text_FUNC);
}
#endif

#ifndef NO_AccessText_1TextWrapping
extern "C" JNIEXPORT void JNICALL OS_NATIVE(AccessText_1TextWrapping)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(AccessText_1TextWrapping)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, AccessText_1TextWrapping_FUNC);
	((AccessText^)TO_OBJECT(arg0))->TextWrapping = ((TextWrapping)arg1);
	OS_NATIVE_EXIT(env, that, AccessText_1TextWrapping_FUNC);
}
#endif

#ifndef NO_ApplicationCommands_1Cut
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Cut)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Cut)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ApplicationCommands_1Cut_FUNC);
	rc = (jint)TO_HANDLE(ApplicationCommands::Cut);
	OS_NATIVE_EXIT(env, that, ApplicationCommands_1Cut_FUNC);
	return rc;
}
#endif

#ifndef NO_ApplicationCommands_1Paste
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Paste)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Paste)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ApplicationCommands_1Paste_FUNC);
	rc = (jint)TO_HANDLE(ApplicationCommands::Paste);
	OS_NATIVE_EXIT(env, that, ApplicationCommands_1Paste_FUNC);
	return rc;
}
#endif

#ifndef NO_ApplicationCommands_1Redo
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Redo)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Redo)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ApplicationCommands_1Redo_FUNC);
	rc = (jint)TO_HANDLE(ApplicationCommands::Redo);
	OS_NATIVE_EXIT(env, that, ApplicationCommands_1Redo_FUNC);
	return rc;
}
#endif

#ifndef NO_ApplicationCommands_1Undo
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Undo)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ApplicationCommands_1Undo)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ApplicationCommands_1Undo_FUNC);
	rc = (jint)TO_HANDLE(ApplicationCommands::Undo);
	OS_NATIVE_EXIT(env, that, ApplicationCommands_1Undo_FUNC);
	return rc;
}
#endif

#ifndef NO_Application_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Application_1Current)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Application_1Current)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Application_1Current_FUNC);
	rc = (jint)TO_HANDLE(Application::Current);
	OS_NATIVE_EXIT(env, that, Application_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_Application_1Dispatcher
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Application_1Dispatcher)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Application_1Dispatcher)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Application_1Dispatcher_FUNC);
	rc = (jint)TO_HANDLE(((Application ^)TO_OBJECT(arg0))->Dispatcher);
	OS_NATIVE_EXIT(env, that, Application_1Dispatcher_FUNC);
	return rc;
}
#endif

#ifndef NO_Application_1Resources__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Application_1Resources__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Application_1Resources__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Application_1Resources__I_FUNC);
	rc = (jint)TO_HANDLE(((Application^)TO_OBJECT(arg0))->Resources);
	OS_NATIVE_EXIT(env, that, Application_1Resources__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Application_1Resources__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Application_1Resources__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Application_1Resources__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Application_1Resources__II_FUNC);
	((Application^)TO_OBJECT(arg0))->Resources = ((ResourceDictionary^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Application_1Resources__II_FUNC);
}
#endif

#ifndef NO_Application_1Run
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Application_1Run)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Application_1Run)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Application_1Run_FUNC);
	((Application^)TO_OBJECT(arg0))->Run();
	OS_NATIVE_EXIT(env, that, Application_1Run_FUNC);
}
#endif

#ifndef NO_Application_1Shutdown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Application_1Shutdown)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Application_1Shutdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Application_1Shutdown_FUNC);
	((Application^)TO_OBJECT(arg0))->Shutdown();
	OS_NATIVE_EXIT(env, that, Application_1Shutdown_FUNC);
}
#endif

#ifndef NO_Application_1ShutdownMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Application_1ShutdownMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Application_1ShutdownMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Application_1ShutdownMode_FUNC);
	((Application^)TO_OBJECT(arg0))->ShutdownMode = ((ShutdownMode)arg1);
	OS_NATIVE_EXIT(env, that, Application_1ShutdownMode_FUNC);
}
#endif

#ifndef NO_Application_1Windows
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Application_1Windows)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Application_1Windows)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Application_1Windows_FUNC);
	rc = (jint)TO_HANDLE(((Application^)TO_OBJECT(arg0))->Windows);
	OS_NATIVE_EXIT(env, that, Application_1Windows_FUNC);
	return rc;
}
#endif

#ifndef NO_ArrayList_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ArrayList_1Clear_FUNC);
	((ArrayList^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, ArrayList_1Clear_FUNC);
}
#endif

#ifndef NO_ArrayList_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ArrayList_1Count_FUNC);
	rc = (jint)((ArrayList^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, ArrayList_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_ArrayList_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, ArrayList_1Insert_FUNC);
	((ArrayList^)TO_OBJECT(arg0))->Insert(arg1, (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, ArrayList_1Insert_FUNC);
}
#endif

#ifndef NO_ArrayList_1RemoveAt
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1RemoveAt)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1RemoveAt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ArrayList_1RemoveAt_FUNC);
	((ArrayList^)TO_OBJECT(arg0))->RemoveAt(arg1);
	OS_NATIVE_EXIT(env, that, ArrayList_1RemoveAt_FUNC);
}
#endif

#ifndef NO_ArrayList_1ToArray
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1ToArray)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1ToArray)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ArrayList_1ToArray_FUNC);
	rc = (jint)TO_HANDLE(((ArrayList^)TO_OBJECT(arg0))->ToArray());
	OS_NATIVE_EXIT(env, that, ArrayList_1ToArray_FUNC);
	return rc;
}
#endif

#ifndef NO_ArrayList_1default__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1default__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(ArrayList_1default__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ArrayList_1default__II_FUNC);
	rc = (jint)TO_HANDLE(((ArrayList^)TO_OBJECT(arg0))->default[arg1]);
	OS_NATIVE_EXIT(env, that, ArrayList_1default__II_FUNC);
	return rc;
}
#endif

#ifndef NO_ArrayList_1default__III
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1default__III)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(ArrayList_1default__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, ArrayList_1default__III_FUNC);
	((ArrayList^)TO_OBJECT(arg0))->default[arg1] = ((Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, ArrayList_1default__III_FUNC);
}
#endif

#ifndef NO_Array_1CreateInstance
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Array_1CreateInstance)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Array_1CreateInstance)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Array_1CreateInstance_FUNC);
	rc = (jint)TO_HANDLE(Array::CreateInstance((Type^)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, Array_1CreateInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_Array_1GetLength
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Array_1GetLength)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Array_1GetLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Array_1GetLength_FUNC);
	rc = (jint)((Array^)TO_OBJECT(arg0))->GetLength(arg1);
	OS_NATIVE_EXIT(env, that, Array_1GetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_Array_1GetValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Array_1GetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Array_1GetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Array_1GetValue_FUNC);
	rc = (jint)TO_HANDLE(((Array^)TO_OBJECT(arg0))->GetValue(arg1));
	OS_NATIVE_EXIT(env, that, Array_1GetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_Array_1SetValue
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Array_1SetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(Array_1SetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, Array_1SetValue_FUNC);
	((Array^)TO_OBJECT(arg0))->SetValue((Object^)TO_OBJECT(arg1), arg2);
	OS_NATIVE_EXIT(env, that, Array_1SetValue_FUNC);
}
#endif

#ifndef NO_BevelBitmapEffect_1BevelWidth__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1BevelWidth__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1BevelWidth__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1BevelWidth__I_FUNC);
	rc = (jdouble)((BevelBitmapEffect^)TO_OBJECT(arg0))->BevelWidth;
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1BevelWidth__I_FUNC);
	return rc;
}
#endif

#ifndef NO_BevelBitmapEffect_1BevelWidth__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1BevelWidth__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1BevelWidth__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1BevelWidth__ID_FUNC);
	((BevelBitmapEffect^)TO_OBJECT(arg0))->BevelWidth = (arg1);
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1BevelWidth__ID_FUNC);
}
#endif

#ifndef NO_BevelBitmapEffect_1LightAngle__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1LightAngle__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1LightAngle__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1LightAngle__I_FUNC);
	rc = (jdouble)((BevelBitmapEffect^)TO_OBJECT(arg0))->LightAngle;
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1LightAngle__I_FUNC);
	return rc;
}
#endif

#ifndef NO_BevelBitmapEffect_1LightAngle__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1LightAngle__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1LightAngle__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1LightAngle__ID_FUNC);
	((BevelBitmapEffect^)TO_OBJECT(arg0))->LightAngle = (arg1);
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1LightAngle__ID_FUNC);
}
#endif

#ifndef NO_BevelBitmapEffect_1Smoothness__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1Smoothness__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(BevelBitmapEffect_1Smoothness__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1Smoothness__I_FUNC);
	rc = (jdouble)((BevelBitmapEffect^)TO_OBJECT(arg0))->Smoothness;
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1Smoothness__I_FUNC);
	return rc;
}
#endif

#ifndef NO_BevelBitmapEffect_1Smoothness__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1Smoothness__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(BevelBitmapEffect_1Smoothness__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, BevelBitmapEffect_1Smoothness__ID_FUNC);
	((BevelBitmapEffect^)TO_OBJECT(arg0))->Smoothness = (arg1);
	OS_NATIVE_EXIT(env, that, BevelBitmapEffect_1Smoothness__ID_FUNC);
}
#endif

#ifndef NO_Binding_1RelativeSource
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Binding_1RelativeSource)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Binding_1RelativeSource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Binding_1RelativeSource_FUNC);
	((Binding^)TO_OBJECT(arg0))->RelativeSource = ((RelativeSource^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Binding_1RelativeSource_FUNC);
}
#endif

#ifndef NO_BitmapDecoder_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapDecoder_1Create)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapDecoder_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapDecoder_1Create_FUNC);
	rc = (jint)TO_HANDLE(BitmapDecoder::Create((System::IO::Stream^)TO_OBJECT(arg0), (BitmapCreateOptions)arg1, (BitmapCacheOption)arg2));
	OS_NATIVE_EXIT(env, that, BitmapDecoder_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapDecoder_1Frames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapDecoder_1Frames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapDecoder_1Frames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapDecoder_1Frames_FUNC);
	rc = (jint)TO_HANDLE(((BitmapDecoder^)TO_OBJECT(arg0))->Frames);
	OS_NATIVE_EXIT(env, that, BitmapDecoder_1Frames_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapEffectGroup_1Children
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapEffectGroup_1Children)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapEffectGroup_1Children)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapEffectGroup_1Children_FUNC);
	rc = (jint)TO_HANDLE(((BitmapEffectGroup^)TO_OBJECT(arg0))->Children);
	OS_NATIVE_EXIT(env, that, BitmapEffectGroup_1Children_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapEncoder_1Frames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapEncoder_1Frames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapEncoder_1Frames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapEncoder_1Frames_FUNC);
	rc = (jint)TO_HANDLE(((BitmapEncoder^)TO_OBJECT(arg0))->Frames);
	OS_NATIVE_EXIT(env, that, BitmapEncoder_1Frames_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapEncoder_1Save
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapEncoder_1Save)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(BitmapEncoder_1Save)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, BitmapEncoder_1Save_FUNC);
	((BitmapEncoder^)TO_OBJECT(arg0))->Save((System::IO::Stream^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, BitmapEncoder_1Save_FUNC);
}
#endif

#ifndef NO_BitmapFrameCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapFrameCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(BitmapFrameCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, BitmapFrameCollection_1Add_FUNC);
	((System::Collections::Generic::IList<BitmapFrame^>^)TO_OBJECT(arg0))->Add((BitmapFrame^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, BitmapFrameCollection_1Add_FUNC);
}
#endif

#ifndef NO_BitmapFrameCollection_1default
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapFrameCollection_1default)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapFrameCollection_1default)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapFrameCollection_1default_FUNC);
	rc = (jint)TO_HANDLE(((System::Collections::Generic::IList<BitmapFrame^>^)TO_OBJECT(arg0))->default[arg1]);
	OS_NATIVE_EXIT(env, that, BitmapFrameCollection_1default_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapFrame_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapFrame_1Create)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapFrame_1Create)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapFrame_1Create_FUNC);
	rc = (jint)TO_HANDLE(BitmapFrame::Create((BitmapSource^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, BitmapFrame_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapImage_1BeginInit
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1BeginInit)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1BeginInit)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, BitmapImage_1BeginInit_FUNC);
	((BitmapImage^)TO_OBJECT(arg0))->BeginInit();
	OS_NATIVE_EXIT(env, that, BitmapImage_1BeginInit_FUNC);
}
#endif

#ifndef NO_BitmapImage_1CreateOptions
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1CreateOptions)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1CreateOptions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, BitmapImage_1CreateOptions_FUNC);
	((BitmapImage^)TO_OBJECT(arg0))->CreateOptions = ((BitmapCreateOptions)arg1);
	OS_NATIVE_EXIT(env, that, BitmapImage_1CreateOptions_FUNC);
}
#endif

#ifndef NO_BitmapImage_1EndInit
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1EndInit)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1EndInit)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, BitmapImage_1EndInit_FUNC);
	((BitmapImage^)TO_OBJECT(arg0))->EndInit();
	OS_NATIVE_EXIT(env, that, BitmapImage_1EndInit_FUNC);
}
#endif

#ifndef NO_BitmapImage_1UriSource
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1UriSource)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(BitmapImage_1UriSource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, BitmapImage_1UriSource_FUNC);
	((BitmapImage^)TO_OBJECT(arg0))->UriSource = ((Uri^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, BitmapImage_1UriSource_FUNC);
}
#endif

#ifndef NO_BitmapPalette_1Colors
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapPalette_1Colors)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapPalette_1Colors)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapPalette_1Colors_FUNC);
	rc = (jint)TO_HANDLE(((BitmapPalette^)TO_OBJECT(arg0))->Colors);
	OS_NATIVE_EXIT(env, that, BitmapPalette_1Colors_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1Clone
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Clone)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1Clone_FUNC);
	rc = (jint)TO_HANDLE(((BitmapSource^)TO_OBJECT(arg0))->Clone());
	OS_NATIVE_EXIT(env, that, BitmapSource_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1CopyPixels
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BitmapSource_1CopyPixels)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4);
JNIEXPORT void JNICALL OS_NATIVE(BitmapSource_1CopyPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, BitmapSource_1CopyPixels_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	((BitmapSource^)TO_OBJECT(arg0))->CopyPixels((Int32Rect)TO_OBJECT(arg1), (IntPtr)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, BitmapSource_1CopyPixels_FUNC);
}
#endif

#ifndef NO_BitmapSource_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Create)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5, jbyteArray arg6, jint arg7, jint arg8);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5, jbyteArray arg6, jint arg7, jint arg8)
{
	jbyte *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1Create_FUNC);
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)TO_HANDLE(BitmapSource::Create(arg0, arg1, arg2, arg3, (PixelFormat)TO_OBJECT(arg4), (BitmapPalette^)TO_OBJECT(arg5), (IntPtr)lparg6, arg7, arg8));
fail:
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, BitmapSource_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1Format
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Format)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Format)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1Format_FUNC);
	rc = (jint)TO_HANDLE(((BitmapSource^)TO_OBJECT(arg0))->Format);
	OS_NATIVE_EXIT(env, that, BitmapSource_1Format_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1Palette
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Palette)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1Palette)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1Palette_FUNC);
	rc = (jint)TO_HANDLE(((BitmapSource^)TO_OBJECT(arg0))->Palette);
	OS_NATIVE_EXIT(env, that, BitmapSource_1Palette_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1PixelHeight
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1PixelHeight)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1PixelHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1PixelHeight_FUNC);
	rc = (jint)((BitmapSource^)TO_OBJECT(arg0))->PixelHeight;
	OS_NATIVE_EXIT(env, that, BitmapSource_1PixelHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_BitmapSource_1PixelWidth
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1PixelWidth)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(BitmapSource_1PixelWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BitmapSource_1PixelWidth_FUNC);
	rc = (jint)((BitmapSource^)TO_OBJECT(arg0))->PixelWidth;
	OS_NATIVE_EXIT(env, that, BitmapSource_1PixelWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_BlurBitmapEffect_1Radius__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(BlurBitmapEffect_1Radius__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(BlurBitmapEffect_1Radius__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, BlurBitmapEffect_1Radius__I_FUNC);
	rc = (jdouble)((BlurBitmapEffect^)TO_OBJECT(arg0))->Radius;
	OS_NATIVE_EXIT(env, that, BlurBitmapEffect_1Radius__I_FUNC);
	return rc;
}
#endif

#ifndef NO_BlurBitmapEffect_1Radius__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(BlurBitmapEffect_1Radius__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(BlurBitmapEffect_1Radius__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, BlurBitmapEffect_1Radius__ID_FUNC);
	((BlurBitmapEffect^)TO_OBJECT(arg0))->Radius = (arg1);
	OS_NATIVE_EXIT(env, that, BlurBitmapEffect_1Radius__ID_FUNC);
}
#endif

#ifndef NO_Border_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Border_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Border_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Border_1typeid_FUNC);
	rc = (jint)TO_HANDLE(Border::typeid);
	OS_NATIVE_EXIT(env, that, Border_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Brush_1Opacity
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Brush_1Opacity)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Brush_1Opacity)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Brush_1Opacity_FUNC);
	((Brush^)TO_OBJECT(arg0))->Opacity = (arg1);
	OS_NATIVE_EXIT(env, that, Brush_1Opacity_FUNC);
}
#endif

#ifndef NO_Brushes_1Black
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Black)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Black)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1Black_FUNC);
	rc = (jint)TO_HANDLE(Brushes::Black);
	OS_NATIVE_EXIT(env, that, Brushes_1Black_FUNC);
	return rc;
}
#endif

#ifndef NO_Brushes_1LightSkyBlue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1LightSkyBlue)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1LightSkyBlue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1LightSkyBlue_FUNC);
	rc = (jint)TO_HANDLE(Brushes::LightSkyBlue);
	OS_NATIVE_EXIT(env, that, Brushes_1LightSkyBlue_FUNC);
	return rc;
}
#endif

#ifndef NO_Brushes_1Navy
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Navy)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Navy)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1Navy_FUNC);
	rc = (jint)TO_HANDLE(Brushes::Navy);
	OS_NATIVE_EXIT(env, that, Brushes_1Navy_FUNC);
	return rc;
}
#endif

#ifndef NO_Brushes_1Red
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Red)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Red)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1Red_FUNC);
	rc = (jint)TO_HANDLE(Brushes::Red);
	OS_NATIVE_EXIT(env, that, Brushes_1Red_FUNC);
	return rc;
}
#endif

#ifndef NO_Brushes_1Transparent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Transparent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1Transparent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1Transparent_FUNC);
	rc = (jint)TO_HANDLE(Brushes::Transparent);
	OS_NATIVE_EXIT(env, that, Brushes_1Transparent_FUNC);
	return rc;
}
#endif

#ifndef NO_Brushes_1White
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1White)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Brushes_1White)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Brushes_1White_FUNC);
	rc = (jint)TO_HANDLE(Brushes::White);
	OS_NATIVE_EXIT(env, that, Brushes_1White_FUNC);
	return rc;
}
#endif

#ifndef NO_ButtonBase_1Click
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ButtonBase_1Click)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ButtonBase_1Click)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ButtonBase_1Click_FUNC);
	((ButtonBase^)TO_OBJECT(arg0))->Click += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ButtonBase_1Click_FUNC);
}
#endif

#ifndef NO_ButtonBase_1ClickEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ButtonBase_1ClickEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ButtonBase_1ClickEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ButtonBase_1ClickEvent_FUNC);
	rc = (jint)TO_HANDLE(ButtonBase::ClickEvent);
	OS_NATIVE_EXIT(env, that, ButtonBase_1ClickEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_Button_1IsDefault__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Button_1IsDefault__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Button_1IsDefault__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Button_1IsDefault__I_FUNC);
	rc = (jboolean)((Button^)TO_OBJECT(arg0))->IsDefault;
	OS_NATIVE_EXIT(env, that, Button_1IsDefault__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Button_1IsDefault__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Button_1IsDefault__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Button_1IsDefault__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Button_1IsDefault__IZ_FUNC);
	((Button^)TO_OBJECT(arg0))->IsDefault = (arg1);
	OS_NATIVE_EXIT(env, that, Button_1IsDefault__IZ_FUNC);
}
#endif

#ifndef NO_Byte_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Byte_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Byte_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Byte_1typeid_FUNC);
	rc = (jint)TO_HANDLE(Byte::typeid);
	OS_NATIVE_EXIT(env, that, Byte_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_CancelEventArgs_1Cancel
extern "C" JNIEXPORT void JNICALL OS_NATIVE(CancelEventArgs_1Cancel)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(CancelEventArgs_1Cancel)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, CancelEventArgs_1Cancel_FUNC);
	((CancelEventArgs^)TO_OBJECT(arg0))->Cancel = (arg1);
	OS_NATIVE_EXIT(env, that, CancelEventArgs_1Cancel_FUNC);
}
#endif

#ifndef NO_Canvas_1GetLeft
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Canvas_1GetLeft)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Canvas_1GetLeft)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Canvas_1GetLeft_FUNC);
	rc = (jdouble)Canvas::GetLeft((UIElement^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Canvas_1GetLeft_FUNC);
	return rc;
}
#endif

#ifndef NO_Canvas_1GetTop
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Canvas_1GetTop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Canvas_1GetTop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Canvas_1GetTop_FUNC);
	rc = (jdouble)Canvas::GetTop((UIElement^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Canvas_1GetTop_FUNC);
	return rc;
}
#endif

#ifndef NO_Canvas_1LeftProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1LeftProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1LeftProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Canvas_1LeftProperty_FUNC);
	rc = (jint)TO_HANDLE(Canvas::LeftProperty);
	OS_NATIVE_EXIT(env, that, Canvas_1LeftProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Canvas_1SetLeft
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Canvas_1SetLeft)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Canvas_1SetLeft)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Canvas_1SetLeft_FUNC);
	Canvas::SetLeft((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Canvas_1SetLeft_FUNC);
}
#endif

#ifndef NO_Canvas_1SetTop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Canvas_1SetTop)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Canvas_1SetTop)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Canvas_1SetTop_FUNC);
	Canvas::SetTop((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Canvas_1SetTop_FUNC);
}
#endif

#ifndef NO_Canvas_1TopProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1TopProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1TopProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Canvas_1TopProperty_FUNC);
	rc = (jint)TO_HANDLE(Canvas::TopProperty);
	OS_NATIVE_EXIT(env, that, Canvas_1TopProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Canvas_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Canvas_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Canvas_1typeid_FUNC);
	rc = (jint)TO_HANDLE(Canvas::typeid);
	OS_NATIVE_EXIT(env, that, Canvas_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_CharacterHit_1FirstCharacterIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CharacterHit_1FirstCharacterIndex)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(CharacterHit_1FirstCharacterIndex)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CharacterHit_1FirstCharacterIndex_FUNC);
	rc = (jint)((CharacterHit^)TO_OBJECT(arg0))->FirstCharacterIndex;
	OS_NATIVE_EXIT(env, that, CharacterHit_1FirstCharacterIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_CharacterHit_1TrailingLength
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CharacterHit_1TrailingLength)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(CharacterHit_1TrailingLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CharacterHit_1TrailingLength_FUNC);
	rc = (jint)((CharacterHit^)TO_OBJECT(arg0))->TrailingLength;
	OS_NATIVE_EXIT(env, that, CharacterHit_1TrailingLength_FUNC);
	return rc;
}
#endif

#ifndef NO_CheckBox_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CheckBox_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(CheckBox_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CheckBox_1typeid_FUNC);
	rc = (jint)TO_HANDLE(CheckBox::typeid);
	OS_NATIVE_EXIT(env, that, CheckBox_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Clipboard_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1Clear)(JNIEnv *env, jclass that);
JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1Clear)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, Clipboard_1Clear_FUNC);
	Clipboard::Clear();
	OS_NATIVE_EXIT(env, that, Clipboard_1Clear_FUNC);
}
#endif

#ifndef NO_Clipboard_1ContainsData
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Clipboard_1ContainsData)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Clipboard_1ContainsData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Clipboard_1ContainsData_FUNC);
	rc = (jboolean)Clipboard::ContainsData((String^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Clipboard_1ContainsData_FUNC);
	return rc;
}
#endif

#ifndef NO_Clipboard_1GetData
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetData)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Clipboard_1GetData_FUNC);
	rc = (jint)TO_HANDLE(Clipboard::GetData((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Clipboard_1GetData_FUNC);
	return rc;
}
#endif

#ifndef NO_Clipboard_1GetDataObject
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetDataObject)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetDataObject)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Clipboard_1GetDataObject_FUNC);
	rc = (jint)TO_HANDLE(Clipboard::GetDataObject());
	OS_NATIVE_EXIT(env, that, Clipboard_1GetDataObject_FUNC);
	return rc;
}
#endif

#ifndef NO_Clipboard_1GetText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetText)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Clipboard_1GetText)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Clipboard_1GetText_FUNC);
	rc = (jint)TO_HANDLE(Clipboard::GetText());
	OS_NATIVE_EXIT(env, that, Clipboard_1GetText_FUNC);
	return rc;
}
#endif

#ifndef NO_Clipboard_1SetData
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1SetData)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1SetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Clipboard_1SetData_FUNC);
	Clipboard::SetData((String^)TO_OBJECT(arg0), (Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Clipboard_1SetData_FUNC);
}
#endif

#ifndef NO_Clipboard_1SetDataObject
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1SetDataObject)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Clipboard_1SetDataObject)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Clipboard_1SetDataObject_FUNC);
	Clipboard::SetDataObject((Object^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Clipboard_1SetDataObject_FUNC);
}
#endif

#ifndef NO_ColorDialog_1AnyColor
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1AnyColor)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1AnyColor)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ColorDialog_1AnyColor_FUNC);
	((System::Windows::Forms::ColorDialog^)TO_OBJECT(arg0))->AnyColor = (arg1);
	OS_NATIVE_EXIT(env, that, ColorDialog_1AnyColor_FUNC);
}
#endif

#ifndef NO_ColorDialog_1Color__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ColorDialog_1Color__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ColorDialog_1Color__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ColorDialog_1Color__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::ColorDialog^)TO_OBJECT(arg0))->Color);
	OS_NATIVE_EXIT(env, that, ColorDialog_1Color__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ColorDialog_1Color__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1Color__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1Color__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ColorDialog_1Color__II_FUNC);
	((System::Windows::Forms::ColorDialog^)TO_OBJECT(arg0))->Color = ((System::Drawing::Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ColorDialog_1Color__II_FUNC);
}
#endif

#ifndef NO_ColorDialog_1CustomColors__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ColorDialog_1CustomColors__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ColorDialog_1CustomColors__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ColorDialog_1CustomColors__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::ColorDialog^)TO_OBJECT(arg0))->CustomColors);
	OS_NATIVE_EXIT(env, that, ColorDialog_1CustomColors__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ColorDialog_1CustomColors__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1CustomColors__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColorDialog_1CustomColors__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ColorDialog_1CustomColors__II_FUNC);
	((System::Windows::Forms::ColorDialog^)TO_OBJECT(arg0))->CustomColors = ((array<int>^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ColorDialog_1CustomColors__II_FUNC);
}
#endif

#ifndef NO_ColorList_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColorList_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColorList_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ColorList_1Add_FUNC);
	((System::Collections::Generic::List<Color>^)TO_OBJECT(arg0))->Add((Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ColorList_1Add_FUNC);
}
#endif

#ifndef NO_ColorList_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ColorList_1Count_FUNC);
	rc = (jint)((System::Collections::Generic::IList<Color>^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, ColorList_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_ColorList_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ColorList_1Current_FUNC);
	rc = (jint)TO_HANDLE(((System::Collections::Generic::IEnumerator<Color>^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, ColorList_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_ColorList_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ColorList_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ColorList_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((System::Collections::Generic::IEnumerable<Color>^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, ColorList_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1A
extern "C" JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1A)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1A)
	(JNIEnv *env, jclass that, jint arg0)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, Color_1A_FUNC);
	rc = (jbyte)((Color^)TO_OBJECT(arg0))->A;
	OS_NATIVE_EXIT(env, that, Color_1A_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1B
extern "C" JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1B)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1B)
	(JNIEnv *env, jclass that, jint arg0)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, Color_1B_FUNC);
	rc = (jbyte)((Color^)TO_OBJECT(arg0))->B;
	OS_NATIVE_EXIT(env, that, Color_1B_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1FromArgb
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Color_1FromArgb)(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3);
JNIEXPORT jint JNICALL OS_NATIVE(Color_1FromArgb)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Color_1FromArgb_FUNC);
	rc = (jint)TO_HANDLE(Color::FromArgb(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, Color_1FromArgb_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1G
extern "C" JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1G)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1G)
	(JNIEnv *env, jclass that, jint arg0)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, Color_1G_FUNC);
	rc = (jbyte)((Color^)TO_OBJECT(arg0))->G;
	OS_NATIVE_EXIT(env, that, Color_1G_FUNC);
	return rc;
}
#endif

#ifndef NO_Color_1R
extern "C" JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1R)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jbyte JNICALL OS_NATIVE(Color_1R)
	(JNIEnv *env, jclass that, jint arg0)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, Color_1R_FUNC);
	rc = (jbyte)((Color^)TO_OBJECT(arg0))->R;
	OS_NATIVE_EXIT(env, that, Color_1R_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Black
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Black)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Black)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Black_FUNC);
	rc = (jint)TO_HANDLE(Colors::Black);
	OS_NATIVE_EXIT(env, that, Colors_1Black_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Blue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Blue)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Blue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Blue_FUNC);
	rc = (jint)TO_HANDLE(Colors::Blue);
	OS_NATIVE_EXIT(env, that, Colors_1Blue_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Cyan
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Cyan)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Cyan)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Cyan_FUNC);
	rc = (jint)TO_HANDLE(Colors::Cyan);
	OS_NATIVE_EXIT(env, that, Colors_1Cyan_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1DarkGray
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1DarkGray)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1DarkGray)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1DarkGray_FUNC);
	rc = (jint)TO_HANDLE(Colors::DarkGray);
	OS_NATIVE_EXIT(env, that, Colors_1DarkGray_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Green
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Green)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Green)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Green_FUNC);
	rc = (jint)TO_HANDLE(Colors::Green);
	OS_NATIVE_EXIT(env, that, Colors_1Green_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1LightSkyBlue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1LightSkyBlue)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1LightSkyBlue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1LightSkyBlue_FUNC);
	rc = (jint)TO_HANDLE(Colors::LightSkyBlue);
	OS_NATIVE_EXIT(env, that, Colors_1LightSkyBlue_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Lime
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Lime)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Lime)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Lime_FUNC);
	rc = (jint)TO_HANDLE(Colors::Lime);
	OS_NATIVE_EXIT(env, that, Colors_1Lime_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Magenta
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Magenta)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Magenta)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Magenta_FUNC);
	rc = (jint)TO_HANDLE(Colors::Magenta);
	OS_NATIVE_EXIT(env, that, Colors_1Magenta_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Maroon
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Maroon)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Maroon)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Maroon_FUNC);
	rc = (jint)TO_HANDLE(Colors::Maroon);
	OS_NATIVE_EXIT(env, that, Colors_1Maroon_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Navy
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Navy)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Navy)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Navy_FUNC);
	rc = (jint)TO_HANDLE(Colors::Navy);
	OS_NATIVE_EXIT(env, that, Colors_1Navy_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Olive
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Olive)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Olive)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Olive_FUNC);
	rc = (jint)TO_HANDLE(Colors::Olive);
	OS_NATIVE_EXIT(env, that, Colors_1Olive_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Purple
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Purple)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Purple)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Purple_FUNC);
	rc = (jint)TO_HANDLE(Colors::Purple);
	OS_NATIVE_EXIT(env, that, Colors_1Purple_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Red
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Red)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Red)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Red_FUNC);
	rc = (jint)TO_HANDLE(Colors::Red);
	OS_NATIVE_EXIT(env, that, Colors_1Red_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Silver
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Silver)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Silver)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Silver_FUNC);
	rc = (jint)TO_HANDLE(Colors::Silver);
	OS_NATIVE_EXIT(env, that, Colors_1Silver_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Teal
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Teal)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Teal)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Teal_FUNC);
	rc = (jint)TO_HANDLE(Colors::Teal);
	OS_NATIVE_EXIT(env, that, Colors_1Teal_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Transparent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Transparent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Transparent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Transparent_FUNC);
	rc = (jint)TO_HANDLE(Colors::Transparent);
	OS_NATIVE_EXIT(env, that, Colors_1Transparent_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1White
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1White)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1White)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1White_FUNC);
	rc = (jint)TO_HANDLE(Colors::White);
	OS_NATIVE_EXIT(env, that, Colors_1White_FUNC);
	return rc;
}
#endif

#ifndef NO_Colors_1Yellow
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Yellow)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Colors_1Yellow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Colors_1Yellow_FUNC);
	rc = (jint)TO_HANDLE(Colors::Yellow);
	OS_NATIVE_EXIT(env, that, Colors_1Yellow_FUNC);
	return rc;
}
#endif

#ifndef NO_ColumnDefinitionCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColumnDefinitionCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColumnDefinitionCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ColumnDefinitionCollection_1Add_FUNC);
	((ColumnDefinitionCollection^)TO_OBJECT(arg0))->Add((ColumnDefinition^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ColumnDefinitionCollection_1Add_FUNC);
}
#endif

#ifndef NO_ColumnDefinition_1Width
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ColumnDefinition_1Width)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ColumnDefinition_1Width)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ColumnDefinition_1Width_FUNC);
	((ColumnDefinition^)TO_OBJECT(arg0))->Width = ((GridLength)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ColumnDefinition_1Width_FUNC);
}
#endif

#ifndef NO_ComboBox_1IsDropDownOpen__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ComboBox_1IsDropDownOpen__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ComboBox_1IsDropDownOpen__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ComboBox_1IsDropDownOpen__I_FUNC);
	rc = (jboolean)((ComboBox^)TO_OBJECT(arg0))->IsDropDownOpen;
	OS_NATIVE_EXIT(env, that, ComboBox_1IsDropDownOpen__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ComboBox_1IsDropDownOpen__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ComboBox_1IsDropDownOpen__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ComboBox_1IsDropDownOpen__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ComboBox_1IsDropDownOpen__IZ_FUNC);
	((ComboBox^)TO_OBJECT(arg0))->IsDropDownOpen = (arg1);
	OS_NATIVE_EXIT(env, that, ComboBox_1IsDropDownOpen__IZ_FUNC);
}
#endif

#ifndef NO_ComboBox_1IsEditable
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ComboBox_1IsEditable)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ComboBox_1IsEditable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ComboBox_1IsEditable_FUNC);
	((ComboBox^)TO_OBJECT(arg0))->IsEditable = (arg1);
	OS_NATIVE_EXIT(env, that, ComboBox_1IsEditable_FUNC);
}
#endif

#ifndef NO_ComboBox_1SelectionBoxItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ComboBox_1SelectionBoxItem)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ComboBox_1SelectionBoxItem)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ComboBox_1SelectionBoxItem_FUNC);
	rc = (jint)TO_HANDLE(((ComboBox^)TO_OBJECT(arg0))->SelectionBoxItem);
	OS_NATIVE_EXIT(env, that, ComboBox_1SelectionBoxItem_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandManager_1AddPreviewExecutedHandler
extern "C" JNIEXPORT void JNICALL OS_NATIVE(CommandManager_1AddPreviewExecutedHandler)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(CommandManager_1AddPreviewExecutedHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CommandManager_1AddPreviewExecutedHandler_FUNC);
	CommandManager::AddPreviewExecutedHandler((UIElement^)TO_OBJECT(arg0), (ExecutedRoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, CommandManager_1AddPreviewExecutedHandler_FUNC);
}
#endif

#ifndef NO_CommonDialog_1ShowDialog
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(CommonDialog_1ShowDialog)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(CommonDialog_1ShowDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommonDialog_1ShowDialog_FUNC);
	rc = (jboolean)((CommonDialog^)TO_OBJECT(arg0))->ShowDialog((Window^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, CommonDialog_1ShowDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_CompositeCollection_1IndexOf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CompositeCollection_1IndexOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(CompositeCollection_1IndexOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CompositeCollection_1IndexOf_FUNC);
	rc = (jint)((CompositeCollection^)TO_OBJECT(arg0))->IndexOf((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, CompositeCollection_1IndexOf_FUNC);
	return rc;
}
#endif

#ifndef NO_CompositeCollection_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, CompositeCollection_1Insert_FUNC);
	((CompositeCollection^)TO_OBJECT(arg0))->Insert(arg1, (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, CompositeCollection_1Insert_FUNC);
}
#endif

#ifndef NO_CompositeCollection_1Remove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CompositeCollection_1Remove_FUNC);
	((CompositeCollection^)TO_OBJECT(arg0))->Remove((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, CompositeCollection_1Remove_FUNC);
}
#endif

#ifndef NO_CompositeCollection_1RemoveAt
extern "C" JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1RemoveAt)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(CompositeCollection_1RemoveAt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CompositeCollection_1RemoveAt_FUNC);
	((CompositeCollection^)TO_OBJECT(arg0))->RemoveAt(arg1);
	OS_NATIVE_EXIT(env, that, CompositeCollection_1RemoveAt_FUNC);
}
#endif

#ifndef NO_Console_1Beep
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Console_1Beep)(JNIEnv *env, jclass that);
JNIEXPORT void JNICALL OS_NATIVE(Console_1Beep)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, Console_1Beep_FUNC);
	Console::Beep();
	OS_NATIVE_EXIT(env, that, Console_1Beep_FUNC);
}
#endif

#ifndef NO_ContainerVisual_1Clip__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ContainerVisual_1Clip__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ContainerVisual_1Clip__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ContainerVisual_1Clip__I_FUNC);
	rc = (jint)TO_HANDLE(((ContainerVisual^)TO_OBJECT(arg0))->Clip);
	OS_NATIVE_EXIT(env, that, ContainerVisual_1Clip__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ContainerVisual_1Clip__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContainerVisual_1Clip__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContainerVisual_1Clip__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContainerVisual_1Clip__II_FUNC);
	((ContainerVisual^)TO_OBJECT(arg0))->Clip = ((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ContainerVisual_1Clip__II_FUNC);
}
#endif

#ifndef NO_ContentControl_1Content__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ContentControl_1Content__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ContentControl_1Content__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ContentControl_1Content__I_FUNC);
	rc = (jint)TO_HANDLE(((ContentControl^)TO_OBJECT(arg0))->Content);
	OS_NATIVE_EXIT(env, that, ContentControl_1Content__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ContentControl_1Content__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContentControl_1Content__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContentControl_1Content__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContentControl_1Content__II_FUNC);
	((ContentControl^)TO_OBJECT(arg0))->Content = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ContentControl_1Content__II_FUNC);
}
#endif

#ifndef NO_ContentPresenter_1Content
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ContentPresenter_1Content)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ContentPresenter_1Content)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ContentPresenter_1Content_FUNC);
	rc = (jint)TO_HANDLE(((ContentPresenter^)TO_OBJECT(arg0))->Content);
	OS_NATIVE_EXIT(env, that, ContentPresenter_1Content_FUNC);
	return rc;
}
#endif

#ifndef NO_ContentPresenter_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ContentPresenter_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ContentPresenter_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ContentPresenter_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ContentPresenter::typeid);
	OS_NATIVE_EXIT(env, that, ContentPresenter_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_ContextMenuEventArgs_1CursorLeft
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(ContextMenuEventArgs_1CursorLeft)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(ContextMenuEventArgs_1CursorLeft)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, ContextMenuEventArgs_1CursorLeft_FUNC);
	rc = (jdouble)((ContextMenuEventArgs^)TO_OBJECT(arg0))->CursorLeft;
	OS_NATIVE_EXIT(env, that, ContextMenuEventArgs_1CursorLeft_FUNC);
	return rc;
}
#endif

#ifndef NO_ContextMenuEventArgs_1CursorTop
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(ContextMenuEventArgs_1CursorTop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(ContextMenuEventArgs_1CursorTop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, ContextMenuEventArgs_1CursorTop_FUNC);
	rc = (jdouble)((ContextMenuEventArgs^)TO_OBJECT(arg0))->CursorTop;
	OS_NATIVE_EXIT(env, that, ContextMenuEventArgs_1CursorTop_FUNC);
	return rc;
}
#endif

#ifndef NO_ContextMenu_1Closed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Closed)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Closed)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1Closed_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->Closed += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ContextMenu_1Closed_FUNC);
}
#endif

#ifndef NO_ContextMenu_1HorizontalOffset
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1HorizontalOffset)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1HorizontalOffset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1HorizontalOffset_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->HorizontalOffset = (arg1);
	OS_NATIVE_EXIT(env, that, ContextMenu_1HorizontalOffset_FUNC);
}
#endif

#ifndef NO_ContextMenu_1IsOpen
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1IsOpen)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1IsOpen)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1IsOpen_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->IsOpen = (arg1);
	OS_NATIVE_EXIT(env, that, ContextMenu_1IsOpen_FUNC);
}
#endif

#ifndef NO_ContextMenu_1Opened
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Opened)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Opened)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1Opened_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->Opened += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ContextMenu_1Opened_FUNC);
}
#endif

#ifndef NO_ContextMenu_1Placement
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Placement)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1Placement)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1Placement_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->Placement = ((PlacementMode)arg1);
	OS_NATIVE_EXIT(env, that, ContextMenu_1Placement_FUNC);
}
#endif

#ifndef NO_ContextMenu_1VerticalOffset
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1VerticalOffset)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ContextMenu_1VerticalOffset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ContextMenu_1VerticalOffset_FUNC);
	((ContextMenu^)TO_OBJECT(arg0))->VerticalOffset = (arg1);
	OS_NATIVE_EXIT(env, that, ContextMenu_1VerticalOffset_FUNC);
}
#endif

#ifndef NO_Control_1Background
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1Background)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1Background)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1Background_FUNC);
	((Control^)TO_OBJECT(arg0))->Background = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1Background_FUNC);
}
#endif

#ifndef NO_Control_1BackgroundProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1BackgroundProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1BackgroundProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1BackgroundProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::BackgroundProperty);
	OS_NATIVE_EXIT(env, that, Control_1BackgroundProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1BorderBrushProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1BorderBrushProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1BorderBrushProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1BorderBrushProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::BorderBrushProperty);
	OS_NATIVE_EXIT(env, that, Control_1BorderBrushProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1BorderThickness
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1BorderThickness)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1BorderThickness)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1BorderThickness_FUNC);
	((Control^)TO_OBJECT(arg0))->BorderThickness = ((Thickness)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1BorderThickness_FUNC);
}
#endif

#ifndef NO_Control_1BorderThicknessProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1BorderThicknessProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1BorderThicknessProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1BorderThicknessProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::BorderThicknessProperty);
	OS_NATIVE_EXIT(env, that, Control_1BorderThicknessProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontFamily__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontFamily__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontFamily__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontFamily__I_FUNC);
	rc = (jint)TO_HANDLE(((Control^)TO_OBJECT(arg0))->FontFamily);
	OS_NATIVE_EXIT(env, that, Control_1FontFamily__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontFamily__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1FontFamily__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1FontFamily__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1FontFamily__II_FUNC);
	((Control^)TO_OBJECT(arg0))->FontFamily = ((FontFamily^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1FontFamily__II_FUNC);
}
#endif

#ifndef NO_Control_1FontFamilyProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontFamilyProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontFamilyProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontFamilyProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::FontFamilyProperty);
	OS_NATIVE_EXIT(env, that, Control_1FontFamilyProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontSize__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Control_1FontSize__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Control_1FontSize__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontSize__I_FUNC);
	rc = (jdouble)((Control^)TO_OBJECT(arg0))->FontSize;
	OS_NATIVE_EXIT(env, that, Control_1FontSize__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontSize__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1FontSize__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1FontSize__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1FontSize__ID_FUNC);
	((Control^)TO_OBJECT(arg0))->FontSize = (arg1);
	OS_NATIVE_EXIT(env, that, Control_1FontSize__ID_FUNC);
}
#endif

#ifndef NO_Control_1FontSizeProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontSizeProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontSizeProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontSizeProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::FontSizeProperty);
	OS_NATIVE_EXIT(env, that, Control_1FontSizeProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontStretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1FontStretch)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1FontStretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1FontStretch_FUNC);
	((Control^)TO_OBJECT(arg0))->FontStretch = ((FontStretch)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1FontStretch_FUNC);
}
#endif

#ifndef NO_Control_1FontStretchProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontStretchProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontStretchProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontStretchProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::FontStretchProperty);
	OS_NATIVE_EXIT(env, that, Control_1FontStretchProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontStyle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1FontStyle)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1FontStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1FontStyle_FUNC);
	((Control^)TO_OBJECT(arg0))->FontStyle = ((FontStyle)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1FontStyle_FUNC);
}
#endif

#ifndef NO_Control_1FontStyleProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontStyleProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontStyleProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontStyleProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::FontStyleProperty);
	OS_NATIVE_EXIT(env, that, Control_1FontStyleProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1FontWeight
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1FontWeight)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1FontWeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1FontWeight_FUNC);
	((Control^)TO_OBJECT(arg0))->FontWeight = ((FontWeight)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1FontWeight_FUNC);
}
#endif

#ifndef NO_Control_1FontWeightProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontWeightProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1FontWeightProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1FontWeightProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::FontWeightProperty);
	OS_NATIVE_EXIT(env, that, Control_1FontWeightProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1Foreground
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1Foreground)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1Foreground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1Foreground_FUNC);
	((Control^)TO_OBJECT(arg0))->Foreground = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1Foreground_FUNC);
}
#endif

#ifndef NO_Control_1ForegroundProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1ForegroundProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1ForegroundProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1ForegroundProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::ForegroundProperty);
	OS_NATIVE_EXIT(env, that, Control_1ForegroundProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1HorizontalContentAlignment__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1HorizontalContentAlignment__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1HorizontalContentAlignment__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1HorizontalContentAlignment__I_FUNC);
	rc = (jint)((Control ^)TO_OBJECT(arg0))->HorizontalContentAlignment;
	OS_NATIVE_EXIT(env, that, Control_1HorizontalContentAlignment__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1HorizontalContentAlignment__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1HorizontalContentAlignment__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1HorizontalContentAlignment__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1HorizontalContentAlignment__II_FUNC);
	((Control ^)TO_OBJECT(arg0))->HorizontalContentAlignment = ((HorizontalAlignment)arg1);
	OS_NATIVE_EXIT(env, that, Control_1HorizontalContentAlignment__II_FUNC);
}
#endif

#ifndef NO_Control_1MouseDoubleClick
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1MouseDoubleClick)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1MouseDoubleClick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1MouseDoubleClick_FUNC);
	((Control^)TO_OBJECT(arg0))->MouseDoubleClick += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1MouseDoubleClick_FUNC);
}
#endif

#ifndef NO_Control_1MouseDoubleClickEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1MouseDoubleClickEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1MouseDoubleClickEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1MouseDoubleClickEvent_FUNC);
	rc = (jint)TO_HANDLE(Control::MouseDoubleClickEvent);
	OS_NATIVE_EXIT(env, that, Control_1MouseDoubleClickEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1Padding__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1Padding__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1Padding__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1Padding__I_FUNC);
	rc = (jint)TO_HANDLE(((Control^)TO_OBJECT(arg0))->Padding);
	OS_NATIVE_EXIT(env, that, Control_1Padding__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1Padding__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1Padding__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1Padding__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1Padding__II_FUNC);
	((Control^)TO_OBJECT(arg0))->Padding = ((Thickness)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1Padding__II_FUNC);
}
#endif

#ifndef NO_Control_1PreviewMouseDoubleClick
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1PreviewMouseDoubleClick)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1PreviewMouseDoubleClick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1PreviewMouseDoubleClick_FUNC);
	((Control^)TO_OBJECT(arg0))->PreviewMouseDoubleClick += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1PreviewMouseDoubleClick_FUNC);
}
#endif

#ifndef NO_Control_1Template__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1Template__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1Template__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1Template__I_FUNC);
	rc = (jint)TO_HANDLE(((Control^)TO_OBJECT(arg0))->Template);
	OS_NATIVE_EXIT(env, that, Control_1Template__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1Template__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1Template__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1Template__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1Template__II_FUNC);
	((Control^)TO_OBJECT(arg0))->Template = ((ControlTemplate^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Control_1Template__II_FUNC);
}
#endif

#ifndef NO_Control_1TemplateProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Control_1TemplateProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Control_1TemplateProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Control_1TemplateProperty_FUNC);
	rc = (jint)TO_HANDLE(Control::TemplateProperty);
	OS_NATIVE_EXIT(env, that, Control_1TemplateProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Control_1VerticalContentAlignment
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Control_1VerticalContentAlignment)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Control_1VerticalContentAlignment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Control_1VerticalContentAlignment_FUNC);
	((Control^)TO_OBJECT(arg0))->VerticalContentAlignment = ((VerticalAlignment)arg1);
	OS_NATIVE_EXIT(env, that, Control_1VerticalContentAlignment_FUNC);
}
#endif

#ifndef NO_CultureInfo_1CurrentUICulture
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CultureInfo_1CurrentUICulture)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(CultureInfo_1CurrentUICulture)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CultureInfo_1CurrentUICulture_FUNC);
	rc = (jint)TO_HANDLE(CultureInfo::CurrentUICulture);
	OS_NATIVE_EXIT(env, that, CultureInfo_1CurrentUICulture_FUNC);
	return rc;
}
#endif

#ifndef NO_CursorInteropHelper_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(CursorInteropHelper_1Create)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(CursorInteropHelper_1Create)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CursorInteropHelper_1Create_FUNC);
	rc = (jint)TO_HANDLE(System::Windows::Interop::CursorInteropHelper::Create((SafeHandle^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, CursorInteropHelper_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1AppStarting
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1AppStarting)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1AppStarting)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1AppStarting_FUNC);
	rc = (jint)TO_HANDLE(Cursors::AppStarting);
	OS_NATIVE_EXIT(env, that, Cursors_1AppStarting_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1Arrow
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Arrow)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Arrow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1Arrow_FUNC);
	rc = (jint)TO_HANDLE(Cursors::Arrow);
	OS_NATIVE_EXIT(env, that, Cursors_1Arrow_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1Cross
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Cross)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Cross)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1Cross_FUNC);
	rc = (jint)TO_HANDLE(Cursors::Cross);
	OS_NATIVE_EXIT(env, that, Cursors_1Cross_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1Hand
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Hand)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Hand)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1Hand_FUNC);
	rc = (jint)TO_HANDLE(Cursors::Hand);
	OS_NATIVE_EXIT(env, that, Cursors_1Hand_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1Help
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Help)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Help)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1Help_FUNC);
	rc = (jint)TO_HANDLE(Cursors::Help);
	OS_NATIVE_EXIT(env, that, Cursors_1Help_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1IBeam
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1IBeam)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1IBeam)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1IBeam_FUNC);
	rc = (jint)TO_HANDLE(Cursors::IBeam);
	OS_NATIVE_EXIT(env, that, Cursors_1IBeam_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1No
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1No)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1No)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1No_FUNC);
	rc = (jint)TO_HANDLE(Cursors::No);
	OS_NATIVE_EXIT(env, that, Cursors_1No_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollE
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollE)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollE_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollE);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollE_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollN
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollN)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollN)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollN_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollN);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollN_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollNE
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollNE)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollNE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollNE_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollNE);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollNE_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollNW
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollNW)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollNW)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollNW_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollNW);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollNW_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollS
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollS)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollS)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollS_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollS);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollS_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollSE
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollSE)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollSE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollSE_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollSE);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollSE_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollSW
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollSW)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollSW)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollSW_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollSW);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollSW_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1ScrollW
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollW)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1ScrollW)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1ScrollW_FUNC);
	rc = (jint)TO_HANDLE(Cursors::ScrollW);
	OS_NATIVE_EXIT(env, that, Cursors_1ScrollW_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1SizeAll
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeAll)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeAll)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1SizeAll_FUNC);
	rc = (jint)TO_HANDLE(Cursors::SizeAll);
	OS_NATIVE_EXIT(env, that, Cursors_1SizeAll_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1SizeNESW
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNESW)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNESW)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1SizeNESW_FUNC);
	rc = (jint)TO_HANDLE(Cursors::SizeNESW);
	OS_NATIVE_EXIT(env, that, Cursors_1SizeNESW_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1SizeNS
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNS)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNS)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1SizeNS_FUNC);
	rc = (jint)TO_HANDLE(Cursors::SizeNS);
	OS_NATIVE_EXIT(env, that, Cursors_1SizeNS_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1SizeNWSE
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNWSE)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeNWSE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1SizeNWSE_FUNC);
	rc = (jint)TO_HANDLE(Cursors::SizeNWSE);
	OS_NATIVE_EXIT(env, that, Cursors_1SizeNWSE_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1SizeWE
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeWE)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1SizeWE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1SizeWE_FUNC);
	rc = (jint)TO_HANDLE(Cursors::SizeWE);
	OS_NATIVE_EXIT(env, that, Cursors_1SizeWE_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1UpArrow
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1UpArrow)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1UpArrow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1UpArrow_FUNC);
	rc = (jint)TO_HANDLE(Cursors::UpArrow);
	OS_NATIVE_EXIT(env, that, Cursors_1UpArrow_FUNC);
	return rc;
}
#endif

#ifndef NO_Cursors_1Wait
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Wait)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Cursors_1Wait)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Cursors_1Wait_FUNC);
	rc = (jint)TO_HANDLE(Cursors::Wait);
	OS_NATIVE_EXIT(env, that, Cursors_1Wait_FUNC);
	return rc;
}
#endif

#ifndef NO_DashStyles_1Dash
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Dash)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Dash)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DashStyles_1Dash_FUNC);
	rc = (jint)TO_HANDLE(DashStyles::Dash);
	OS_NATIVE_EXIT(env, that, DashStyles_1Dash_FUNC);
	return rc;
}
#endif

#ifndef NO_DashStyles_1DashDot
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1DashDot)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1DashDot)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DashStyles_1DashDot_FUNC);
	rc = (jint)TO_HANDLE(DashStyles::DashDot);
	OS_NATIVE_EXIT(env, that, DashStyles_1DashDot_FUNC);
	return rc;
}
#endif

#ifndef NO_DashStyles_1DashDotDot
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1DashDotDot)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1DashDotDot)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DashStyles_1DashDotDot_FUNC);
	rc = (jint)TO_HANDLE(DashStyles::DashDotDot);
	OS_NATIVE_EXIT(env, that, DashStyles_1DashDotDot_FUNC);
	return rc;
}
#endif

#ifndef NO_DashStyles_1Dot
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Dot)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Dot)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DashStyles_1Dot_FUNC);
	rc = (jint)TO_HANDLE(DashStyles::Dot);
	OS_NATIVE_EXIT(env, that, DashStyles_1Dot_FUNC);
	return rc;
}
#endif

#ifndef NO_DashStyles_1Solid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Solid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DashStyles_1Solid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DashStyles_1Solid_FUNC);
	rc = (jint)TO_HANDLE(DashStyles::Solid);
	OS_NATIVE_EXIT(env, that, DashStyles_1Solid_FUNC);
	return rc;
}
#endif

#ifndef NO_DataFormats_1Bitmap
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Bitmap)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Bitmap)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataFormats_1Bitmap_FUNC);
	rc = (jint)TO_HANDLE(DataFormats::Bitmap);
	OS_NATIVE_EXIT(env, that, DataFormats_1Bitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_DataFormats_1FileDrop
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1FileDrop)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1FileDrop)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataFormats_1FileDrop_FUNC);
	rc = (jint)TO_HANDLE(DataFormats::FileDrop);
	OS_NATIVE_EXIT(env, that, DataFormats_1FileDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_DataFormats_1Html
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Html)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Html)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataFormats_1Html_FUNC);
	rc = (jint)TO_HANDLE(DataFormats::Html);
	OS_NATIVE_EXIT(env, that, DataFormats_1Html_FUNC);
	return rc;
}
#endif

#ifndef NO_DataFormats_1Rtf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Rtf)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1Rtf)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataFormats_1Rtf_FUNC);
	rc = (jint)TO_HANDLE(DataFormats::Rtf);
	OS_NATIVE_EXIT(env, that, DataFormats_1Rtf_FUNC);
	return rc;
}
#endif

#ifndef NO_DataFormats_1UnicodeText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1UnicodeText)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DataFormats_1UnicodeText)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataFormats_1UnicodeText_FUNC);
	rc = (jint)TO_HANDLE(DataFormats::UnicodeText);
	OS_NATIVE_EXIT(env, that, DataFormats_1UnicodeText_FUNC);
	return rc;
}
#endif

#ifndef NO_DataObject_1GetData
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataObject_1GetData)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2);
JNIEXPORT jint JNICALL OS_NATIVE(DataObject_1GetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataObject_1GetData_FUNC);
	rc = (jint)TO_HANDLE(((DataObject^)TO_OBJECT(arg0))->GetData((String^)TO_OBJECT(arg1), arg2));
	OS_NATIVE_EXIT(env, that, DataObject_1GetData_FUNC);
	return rc;
}
#endif

#ifndef NO_DataObject_1GetDataPresent
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(DataObject_1GetDataPresent)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2);
JNIEXPORT jboolean JNICALL OS_NATIVE(DataObject_1GetDataPresent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DataObject_1GetDataPresent_FUNC);
	rc = (jboolean)((DataObject^)TO_OBJECT(arg0))->GetDataPresent((String^)TO_OBJECT(arg1), arg2);
	OS_NATIVE_EXIT(env, that, DataObject_1GetDataPresent_FUNC);
	return rc;
}
#endif

#ifndef NO_DataObject_1GetFormats
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DataObject_1GetFormats)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DataObject_1GetFormats)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataObject_1GetFormats_FUNC);
	rc = (jint)TO_HANDLE(((DataObject^)TO_OBJECT(arg0))->GetFormats(arg1));
	OS_NATIVE_EXIT(env, that, DataObject_1GetFormats_FUNC);
	return rc;
}
#endif

#ifndef NO_DataObject_1SetData
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DataObject_1SetData)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3);
JNIEXPORT void JNICALL OS_NATIVE(DataObject_1SetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, DataObject_1SetData_FUNC);
	((DataObject^)TO_OBJECT(arg0))->SetData((String^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2), arg3);
	OS_NATIVE_EXIT(env, that, DataObject_1SetData_FUNC);
}
#endif

#ifndef NO_DeleteGlobalRef
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DeleteGlobalRef)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(DeleteGlobalRef)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DeleteGlobalRef_FUNC);
	env->DeleteGlobalRef((jobject)arg0);
	OS_NATIVE_EXIT(env, that, DeleteGlobalRef_FUNC);
}
#endif

#ifndef NO_DependencyObject_1ClearValue
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DependencyObject_1ClearValue)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DependencyObject_1ClearValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DependencyObject_1ClearValue_FUNC);
	((DependencyObject^)TO_OBJECT(arg0))->ClearValue((DependencyProperty^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DependencyObject_1ClearValue_FUNC);
}
#endif

#ifndef NO_DependencyObject_1GetValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyObject_1GetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyObject_1GetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyObject_1GetValue_FUNC);
	rc = (jint)TO_HANDLE(((DependencyObject^)TO_OBJECT(arg0))->GetValue((DependencyProperty^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, DependencyObject_1GetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyObject_1GetValueDouble
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyObject_1GetValueDouble)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyObject_1GetValueDouble)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyObject_1GetValueDouble_FUNC);
	rc = (jdouble)((DependencyObject^)TO_OBJECT(arg0))->GetValue((DependencyProperty^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DependencyObject_1GetValueDouble_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyObject_1GetValueInt
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyObject_1GetValueInt)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyObject_1GetValueInt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyObject_1GetValueInt_FUNC);
	rc = (jint)((DependencyObject^)TO_OBJECT(arg0))->GetValue((DependencyProperty^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DependencyObject_1GetValueInt_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyObject_1SetValue
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DependencyObject_1SetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(DependencyObject_1SetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DependencyObject_1SetValue_FUNC);
	((DependencyObject^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, DependencyObject_1SetValue_FUNC);
}
#endif

#ifndef NO_DependencyPropertyChangedEventArgs_1NewValueDouble
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1NewValueDouble)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1NewValueDouble)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyChangedEventArgs_1NewValueDouble_FUNC);
	rc = (jdouble)((DependencyPropertyChangedEventArgs^)TO_OBJECT(arg0))->NewValue;
	OS_NATIVE_EXIT(env, that, DependencyPropertyChangedEventArgs_1NewValueDouble_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyChangedEventArgs_1NewValueInt
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1NewValueInt)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1NewValueInt)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyChangedEventArgs_1NewValueInt_FUNC);
	rc = (jint)((DependencyPropertyChangedEventArgs^)TO_OBJECT(arg0))->NewValue;
	OS_NATIVE_EXIT(env, that, DependencyPropertyChangedEventArgs_1NewValueInt_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyChangedEventArgs_1OldValueDouble
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1OldValueDouble)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1OldValueDouble)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyChangedEventArgs_1OldValueDouble_FUNC);
	rc = (jdouble)((DependencyPropertyChangedEventArgs^)TO_OBJECT(arg0))->OldValue;
	OS_NATIVE_EXIT(env, that, DependencyPropertyChangedEventArgs_1OldValueDouble_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyChangedEventArgs_1OldValueInt
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1OldValueInt)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyChangedEventArgs_1OldValueInt)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyChangedEventArgs_1OldValueInt_FUNC);
	rc = (jint)((DependencyPropertyChangedEventArgs^)TO_OBJECT(arg0))->OldValue;
	OS_NATIVE_EXIT(env, that, DependencyPropertyChangedEventArgs_1OldValueInt_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyDescriptor_1AddValueChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DependencyPropertyDescriptor_1AddValueChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(DependencyPropertyDescriptor_1AddValueChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DependencyPropertyDescriptor_1AddValueChanged_FUNC);
	((DependencyPropertyDescriptor^)TO_OBJECT(arg0))->AddValueChanged((Object^)TO_OBJECT(arg1), (EventHandler^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, DependencyPropertyDescriptor_1AddValueChanged_FUNC);
}
#endif

#ifndef NO_DependencyPropertyDescriptor_1DependencyProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1DependencyProperty)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1DependencyProperty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyDescriptor_1DependencyProperty_FUNC);
	rc = (jint)TO_HANDLE(((DependencyPropertyDescriptor^)TO_OBJECT(arg0))->DependencyProperty);
	OS_NATIVE_EXIT(env, that, DependencyPropertyDescriptor_1DependencyProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyDescriptor_1FromProperty__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1FromProperty__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1FromProperty__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyDescriptor_1FromProperty__I_FUNC);
	rc = (jint)TO_HANDLE(DependencyPropertyDescriptor::FromProperty((PropertyDescriptor^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, DependencyPropertyDescriptor_1FromProperty__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyDescriptor_1FromProperty__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1FromProperty__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1FromProperty__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyDescriptor_1FromProperty__II_FUNC);
	rc = (jint)TO_HANDLE(DependencyPropertyDescriptor::FromProperty((DependencyProperty^)TO_OBJECT(arg0), (Type^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, DependencyPropertyDescriptor_1FromProperty__II_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyPropertyDescriptor_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyPropertyDescriptor_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyPropertyDescriptor_1typeid_FUNC);
	rc = (jint)TO_HANDLE(DependencyPropertyDescriptor::typeid);
	OS_NATIVE_EXIT(env, that, DependencyPropertyDescriptor_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_DependencyProperty_1UnsetValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DependencyProperty_1UnsetValue)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DependencyProperty_1UnsetValue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DependencyProperty_1UnsetValue_FUNC);
	rc = (jint)TO_HANDLE(DependencyProperty::UnsetValue);
	OS_NATIVE_EXIT(env, that, DependencyProperty_1UnsetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherFrame_1Continue__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(DispatcherFrame_1Continue__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(DispatcherFrame_1Continue__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherFrame_1Continue__I_FUNC);
	rc = (jboolean)((DispatcherFrame^)TO_OBJECT(arg0))->Continue;
	OS_NATIVE_EXIT(env, that, DispatcherFrame_1Continue__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherFrame_1Continue__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherFrame_1Continue__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherFrame_1Continue__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherFrame_1Continue__IZ_FUNC);
	((DispatcherFrame^)TO_OBJECT(arg0))->Continue = (arg1);
	OS_NATIVE_EXIT(env, that, DispatcherFrame_1Continue__IZ_FUNC);
}
#endif

#ifndef NO_DispatcherHookEventArgs_1Operation
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DispatcherHookEventArgs_1Operation)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DispatcherHookEventArgs_1Operation)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherHookEventArgs_1Operation_FUNC);
	rc = (jint)TO_HANDLE(((DispatcherHookEventArgs ^)TO_OBJECT(arg0))->Operation);
	OS_NATIVE_EXIT(env, that, DispatcherHookEventArgs_1Operation_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherHooks_1DispatcherInactive
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1DispatcherInactive)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1DispatcherInactive)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherHooks_1DispatcherInactive_FUNC);
	((DispatcherHooks ^)TO_OBJECT(arg0))->DispatcherInactive += ((EventHandler ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherHooks_1DispatcherInactive_FUNC);
}
#endif

#ifndef NO_DispatcherHooks_1OperationAborted
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationAborted)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationAborted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherHooks_1OperationAborted_FUNC);
	((DispatcherHooks ^)TO_OBJECT(arg0))->OperationAborted += ((DispatcherHookEventHandler ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherHooks_1OperationAborted_FUNC);
}
#endif

#ifndef NO_DispatcherHooks_1OperationCompleted
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationCompleted)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationCompleted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherHooks_1OperationCompleted_FUNC);
	((DispatcherHooks ^)TO_OBJECT(arg0))->OperationCompleted += ((DispatcherHookEventHandler ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherHooks_1OperationCompleted_FUNC);
}
#endif

#ifndef NO_DispatcherHooks_1OperationPosted
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationPosted)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherHooks_1OperationPosted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherHooks_1OperationPosted_FUNC);
	((DispatcherHooks ^)TO_OBJECT(arg0))->OperationPosted += ((DispatcherHookEventHandler ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherHooks_1OperationPosted_FUNC);
}
#endif

#ifndef NO_DispatcherOperation_1Abort
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(DispatcherOperation_1Abort)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(DispatcherOperation_1Abort)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherOperation_1Abort_FUNC);
	rc = (jboolean)((DispatcherOperation^)TO_OBJECT(arg0))->Abort();
	OS_NATIVE_EXIT(env, that, DispatcherOperation_1Abort_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherOperation_1Priority__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DispatcherOperation_1Priority__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DispatcherOperation_1Priority__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherOperation_1Priority__I_FUNC);
	rc = (jint)((DispatcherOperation ^)TO_OBJECT(arg0))->Priority;
	OS_NATIVE_EXIT(env, that, DispatcherOperation_1Priority__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherOperation_1Priority__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherOperation_1Priority__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherOperation_1Priority__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherOperation_1Priority__II_FUNC);
	((DispatcherOperation ^)TO_OBJECT(arg0))->Priority = ((DispatcherPriority)arg1);
	OS_NATIVE_EXIT(env, that, DispatcherOperation_1Priority__II_FUNC);
}
#endif

#ifndef NO_DispatcherOperation_1Wait
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DispatcherOperation_1Wait)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DispatcherOperation_1Wait)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherOperation_1Wait_FUNC);
	rc = (jint)((DispatcherOperation^)TO_OBJECT(arg0))->Wait();
	OS_NATIVE_EXIT(env, that, DispatcherOperation_1Wait_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherTimer_1Interval
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Interval)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Interval)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Interval_FUNC);
	((DispatcherTimer^)TO_OBJECT(arg0))->Interval = ((TimeSpan)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Interval_FUNC);
}
#endif

#ifndef NO_DispatcherTimer_1Start
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Start)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Start)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Start_FUNC);
	((DispatcherTimer^)TO_OBJECT(arg0))->Start();
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Start_FUNC);
}
#endif

#ifndef NO_DispatcherTimer_1Stop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Stop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Stop)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Stop_FUNC);
	((DispatcherTimer^)TO_OBJECT(arg0))->Stop();
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Stop_FUNC);
}
#endif

#ifndef NO_DispatcherTimer_1Tag__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DispatcherTimer_1Tag__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DispatcherTimer_1Tag__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Tag__I_FUNC);
	rc = (jint)((DispatcherTimer^)TO_OBJECT(arg0))->Tag;
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Tag__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatcherTimer_1Tag__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Tag__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Tag__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Tag__II_FUNC);
	((DispatcherTimer^)TO_OBJECT(arg0))->Tag = (arg1);
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Tag__II_FUNC);
}
#endif

#ifndef NO_DispatcherTimer_1Tick
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Tick)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DispatcherTimer_1Tick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DispatcherTimer_1Tick_FUNC);
	((DispatcherTimer^)TO_OBJECT(arg0))->Tick += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DispatcherTimer_1Tick_FUNC);
}
#endif

#ifndef NO_Dispatcher_1BeginInvoke
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Dispatcher_1BeginInvoke)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Dispatcher_1BeginInvoke)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Dispatcher_1BeginInvoke_FUNC);
	rc = (jint)TO_HANDLE(((Dispatcher ^)TO_OBJECT(arg0))->BeginInvoke((DispatcherPriority)arg1, (Delegate ^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, Dispatcher_1BeginInvoke_FUNC);
	return rc;
}
#endif

#ifndef NO_Dispatcher_1Hooks
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Dispatcher_1Hooks)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Dispatcher_1Hooks)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Dispatcher_1Hooks_FUNC);
	rc = (jint)TO_HANDLE(((Dispatcher ^)TO_OBJECT(arg0))->Hooks);
	OS_NATIVE_EXIT(env, that, Dispatcher_1Hooks_FUNC);
	return rc;
}
#endif

#ifndef NO_Dispatcher_1PushFrame
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Dispatcher_1PushFrame)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Dispatcher_1PushFrame)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Dispatcher_1PushFrame_FUNC);
	Dispatcher::PushFrame((DispatcherFrame ^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Dispatcher_1PushFrame_FUNC);
}
#endif

#ifndef NO_DockPanel_1DockProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DockPanel_1DockProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DockPanel_1DockProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DockPanel_1DockProperty_FUNC);
	rc = (jint)TO_HANDLE(DockPanel::DockProperty);
	OS_NATIVE_EXIT(env, that, DockPanel_1DockProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_DockPanel_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DockPanel_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DockPanel_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DockPanel_1typeid_FUNC);
	rc = (jint)TO_HANDLE(DockPanel::typeid);
	OS_NATIVE_EXIT(env, that, DockPanel_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_DoubleAnimationUsingKeyFrames_1KeyFrames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DoubleAnimationUsingKeyFrames_1KeyFrames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DoubleAnimationUsingKeyFrames_1KeyFrames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DoubleAnimationUsingKeyFrames_1KeyFrames_FUNC);
	rc = (jint)TO_HANDLE(((DoubleAnimationUsingKeyFrames^)TO_OBJECT(arg0))->KeyFrames);
	OS_NATIVE_EXIT(env, that, DoubleAnimationUsingKeyFrames_1KeyFrames_FUNC);
	return rc;
}
#endif

#ifndef NO_DoubleAnimation_1From__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DoubleAnimation_1From__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DoubleAnimation_1From__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DoubleAnimation_1From__I_FUNC);
	rc = (jdouble)((DoubleAnimation^)TO_OBJECT(arg0))->From;
	OS_NATIVE_EXIT(env, that, DoubleAnimation_1From__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DoubleAnimation_1From__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DoubleAnimation_1From__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DoubleAnimation_1From__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DoubleAnimation_1From__ID_FUNC);
	((DoubleAnimation^)TO_OBJECT(arg0))->From = (arg1);
	OS_NATIVE_EXIT(env, that, DoubleAnimation_1From__ID_FUNC);
}
#endif

#ifndef NO_DoubleAnimation_1To__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DoubleAnimation_1To__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DoubleAnimation_1To__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DoubleAnimation_1To__I_FUNC);
	rc = (jdouble)((DoubleAnimation^)TO_OBJECT(arg0))->To;
	OS_NATIVE_EXIT(env, that, DoubleAnimation_1To__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DoubleAnimation_1To__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DoubleAnimation_1To__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DoubleAnimation_1To__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DoubleAnimation_1To__ID_FUNC);
	((DoubleAnimation^)TO_OBJECT(arg0))->To = (arg1);
	OS_NATIVE_EXIT(env, that, DoubleAnimation_1To__ID_FUNC);
}
#endif

#ifndef NO_DoubleCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DoubleCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DoubleCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DoubleCollection_1Add_FUNC);
	((DoubleCollection^)TO_OBJECT(arg0))->Add(arg1);
	OS_NATIVE_EXIT(env, that, DoubleCollection_1Add_FUNC);
}
#endif

#ifndef NO_DoubleKeyFrameCollection_1Add
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DoubleKeyFrameCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DoubleKeyFrameCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DoubleKeyFrameCollection_1Add_FUNC);
	rc = (jint)((DoubleKeyFrameCollection^)TO_OBJECT(arg0))->Add((DoubleKeyFrame^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DoubleKeyFrameCollection_1Add_FUNC);
	return rc;
}
#endif

#ifndef NO_DoubleKeyFrame_1KeyTime
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DoubleKeyFrame_1KeyTime)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DoubleKeyFrame_1KeyTime)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DoubleKeyFrame_1KeyTime_FUNC);
	((DoubleKeyFrame^)TO_OBJECT(arg0))->KeyTime = ((KeyTime)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DoubleKeyFrame_1KeyTime_FUNC);
}
#endif

#ifndef NO_DoubleKeyFrame_1Value
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DoubleKeyFrame_1Value)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DoubleKeyFrame_1Value)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DoubleKeyFrame_1Value_FUNC);
	((DoubleKeyFrame^)TO_OBJECT(arg0))->Value = (arg1);
	OS_NATIVE_EXIT(env, that, DoubleKeyFrame_1Value_FUNC);
}
#endif

#ifndef NO_DragDeltaEventArgs_1HorizontalChange
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragDeltaEventArgs_1HorizontalChange)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragDeltaEventArgs_1HorizontalChange)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragDeltaEventArgs_1HorizontalChange_FUNC);
	rc = (jint)((DragDeltaEventArgs^)TO_OBJECT(arg0))->HorizontalChange;
	OS_NATIVE_EXIT(env, that, DragDeltaEventArgs_1HorizontalChange_FUNC);
	return rc;
}
#endif

#ifndef NO_DragDeltaEventArgs_1VerticalChange
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragDeltaEventArgs_1VerticalChange)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragDeltaEventArgs_1VerticalChange)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragDeltaEventArgs_1VerticalChange_FUNC);
	rc = (jint)((DragDeltaEventArgs^)TO_OBJECT(arg0))->VerticalChange;
	OS_NATIVE_EXIT(env, that, DragDeltaEventArgs_1VerticalChange_FUNC);
	return rc;
}
#endif

#ifndef NO_DragDrop_1DoDragDrop
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragDrop_1DoDragDrop)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(DragDrop_1DoDragDrop)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragDrop_1DoDragDrop_FUNC);
	rc = (jint)DragDrop::DoDragDrop((DependencyObject^)TO_OBJECT(arg0), (Object^)TO_OBJECT(arg1), (DragDropEffects)arg2);
	OS_NATIVE_EXIT(env, that, DragDrop_1DoDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_DragEventArgs_1AllowedEffects
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1AllowedEffects)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1AllowedEffects)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragEventArgs_1AllowedEffects_FUNC);
	rc = (jint)((DragEventArgs^)TO_OBJECT(arg0))->AllowedEffects;
	OS_NATIVE_EXIT(env, that, DragEventArgs_1AllowedEffects_FUNC);
	return rc;
}
#endif

#ifndef NO_DragEventArgs_1Data
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1Data)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1Data)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragEventArgs_1Data_FUNC);
	rc = (jint)TO_HANDLE(((DragEventArgs^)TO_OBJECT(arg0))->Data);
	OS_NATIVE_EXIT(env, that, DragEventArgs_1Data_FUNC);
	return rc;
}
#endif

#ifndef NO_DragEventArgs_1Effects__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1Effects__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1Effects__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragEventArgs_1Effects__I_FUNC);
	rc = (jint)((DragEventArgs^)TO_OBJECT(arg0))->Effects;
	OS_NATIVE_EXIT(env, that, DragEventArgs_1Effects__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DragEventArgs_1Effects__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DragEventArgs_1Effects__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DragEventArgs_1Effects__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DragEventArgs_1Effects__II_FUNC);
	((DragEventArgs^)TO_OBJECT(arg0))->Effects = ((DragDropEffects)arg1);
	OS_NATIVE_EXIT(env, that, DragEventArgs_1Effects__II_FUNC);
}
#endif

#ifndef NO_DragEventArgs_1GetPosition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1GetPosition)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1GetPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragEventArgs_1GetPosition_FUNC);
	rc = (jint)TO_HANDLE(((DragEventArgs^)TO_OBJECT(arg0))->GetPosition((IInputElement^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, DragEventArgs_1GetPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_DragEventArgs_1KeyStates
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1KeyStates)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DragEventArgs_1KeyStates)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragEventArgs_1KeyStates_FUNC);
	rc = (jint)((DragEventArgs^)TO_OBJECT(arg0))->KeyStates;
	OS_NATIVE_EXIT(env, that, DragEventArgs_1KeyStates_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingColor_1FromArgb
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingColor_1FromArgb)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingColor_1FromArgb)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingColor_1FromArgb_FUNC);
	rc = (jint)TO_HANDLE(System::Drawing::Color::FromArgb(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, DrawingColor_1FromArgb_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingColor_1ToArgb
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingColor_1ToArgb)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingColor_1ToArgb)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingColor_1ToArgb_FUNC);
	rc = (jint)((System::Drawing::Color^)TO_OBJECT(arg0))->ToArgb();
	OS_NATIVE_EXIT(env, that, DrawingColor_1ToArgb_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingContext_1Close
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1Close)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1Close)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1Close_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->Close();
	OS_NATIVE_EXIT(env, that, DrawingContext_1Close_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawDrawing
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawDrawing)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawDrawing)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawDrawing_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawDrawing((System::Windows::Media::Drawing^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawDrawing_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawEllipse
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawEllipse)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jdouble arg5);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawEllipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jdouble arg5)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawEllipse_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawEllipse((Brush^)TO_OBJECT(arg1), (Pen^)TO_OBJECT(arg2), (Point)TO_OBJECT(arg3), arg4, arg5);
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawEllipse_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawGeometry
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawGeometry)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawGeometry_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawGeometry((Brush^)TO_OBJECT(arg1), (Pen^)TO_OBJECT(arg2), (Geometry^)TO_OBJECT(arg3));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawGeometry_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawImage
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawImage)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawImage_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawImage((ImageSource^)TO_OBJECT(arg1), (Rect)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawImage_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawLine
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawLine)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawLine_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawLine((Pen^)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2), (Point)TO_OBJECT(arg3));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawLine_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawRectangle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawRectangle)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawRectangle_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawRectangle((Brush^)TO_OBJECT(arg1), (Pen^)TO_OBJECT(arg2), (Rect)TO_OBJECT(arg3));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawRectangle_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawRoundedRectangle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawRoundedRectangle)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jdouble arg5);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawRoundedRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jdouble arg5)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawRoundedRectangle_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawRoundedRectangle((Brush^)TO_OBJECT(arg1), (Pen^)TO_OBJECT(arg2), (Rect)TO_OBJECT(arg3), arg4, arg5);
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawRoundedRectangle_FUNC);
}
#endif

#ifndef NO_DrawingContext_1DrawText
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawText)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1DrawText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1DrawText_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->DrawText((FormattedText^)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, DrawingContext_1DrawText_FUNC);
}
#endif

#ifndef NO_DrawingContext_1Pop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1Pop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1Pop)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1Pop_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->Pop();
	OS_NATIVE_EXIT(env, that, DrawingContext_1Pop_FUNC);
}
#endif

#ifndef NO_DrawingContext_1PushClip
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushClip)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushClip)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1PushClip_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->PushClip((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DrawingContext_1PushClip_FUNC);
}
#endif

#ifndef NO_DrawingContext_1PushOpacity
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushOpacity)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushOpacity)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1PushOpacity_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->PushOpacity(arg1);
	OS_NATIVE_EXIT(env, that, DrawingContext_1PushOpacity_FUNC);
}
#endif

#ifndef NO_DrawingContext_1PushTransform
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushTransform)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DrawingContext_1PushTransform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DrawingContext_1PushTransform_FUNC);
	((DrawingContext^)TO_OBJECT(arg0))->PushTransform((Transform^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DrawingContext_1PushTransform_FUNC);
}
#endif

#ifndef NO_DrawingFontFamily_1Name
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingFontFamily_1Name)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingFontFamily_1Name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingFontFamily_1Name_FUNC);
	rc = (jint)TO_HANDLE(((System::Drawing::FontFamily^)TO_OBJECT(arg0))->Name);
	OS_NATIVE_EXIT(env, that, DrawingFontFamily_1Name_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingVisual_1Drawing
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1Drawing)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1Drawing)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingVisual_1Drawing_FUNC);
	rc = (jint)TO_HANDLE(((DrawingVisual^)TO_OBJECT(arg0))->Drawing);
	OS_NATIVE_EXIT(env, that, DrawingVisual_1Drawing_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingVisual_1RenderOpen
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1RenderOpen)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1RenderOpen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingVisual_1RenderOpen_FUNC);
	rc = (jint)TO_HANDLE(((DrawingVisual^)TO_OBJECT(arg0))->RenderOpen());
	OS_NATIVE_EXIT(env, that, DrawingVisual_1RenderOpen_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawingVisual_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(DrawingVisual_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawingVisual_1typeid_FUNC);
	rc = (jint)TO_HANDLE(DrawingVisual::typeid);
	OS_NATIVE_EXIT(env, that, DrawingVisual_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Color__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(DropShadowBitmapEffect_1Color__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(DropShadowBitmapEffect_1Color__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Color__I_FUNC);
	rc = (jint)TO_HANDLE(((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Color);
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Color__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Color__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Color__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Color__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Color__II_FUNC);
	((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Color = ((Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Color__II_FUNC);
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Direction__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Direction__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Direction__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Direction__I_FUNC);
	rc = (jdouble)((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Direction;
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Direction__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Direction__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Direction__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Direction__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Direction__ID_FUNC);
	((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Direction = (arg1);
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Direction__ID_FUNC);
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Opacity__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Opacity__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Opacity__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Opacity__I_FUNC);
	rc = (jdouble)((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Opacity;
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Opacity__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Opacity__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Opacity__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Opacity__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Opacity__ID_FUNC);
	((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Opacity = (arg1);
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Opacity__ID_FUNC);
}
#endif

#ifndef NO_DropShadowBitmapEffect_1ShadowDepth__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1ShadowDepth__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1ShadowDepth__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1ShadowDepth__I_FUNC);
	rc = (jdouble)((DropShadowBitmapEffect^)TO_OBJECT(arg0))->ShadowDepth;
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1ShadowDepth__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1ShadowDepth__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1ShadowDepth__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1ShadowDepth__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1ShadowDepth__ID_FUNC);
	((DropShadowBitmapEffect^)TO_OBJECT(arg0))->ShadowDepth = (arg1);
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1ShadowDepth__ID_FUNC);
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Softness__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Softness__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(DropShadowBitmapEffect_1Softness__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Softness__I_FUNC);
	rc = (jdouble)((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Softness;
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Softness__I_FUNC);
	return rc;
}
#endif

#ifndef NO_DropShadowBitmapEffect_1Softness__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Softness__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(DropShadowBitmapEffect_1Softness__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, DropShadowBitmapEffect_1Softness__ID_FUNC);
	((DropShadowBitmapEffect^)TO_OBJECT(arg0))->Softness = (arg1);
	OS_NATIVE_EXIT(env, that, DropShadowBitmapEffect_1Softness__ID_FUNC);
}
#endif

#ifndef NO_Duration_1TimeSpan
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Duration_1TimeSpan)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Duration_1TimeSpan)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Duration_1TimeSpan_FUNC);
	rc = (jint)TO_HANDLE(((Duration^)TO_OBJECT(arg0))->TimeSpan);
	OS_NATIVE_EXIT(env, that, Duration_1TimeSpan_FUNC);
	return rc;
}
#endif

#ifndef NO_EditingCommands_1Backspace
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1Backspace)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1Backspace)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EditingCommands_1Backspace_FUNC);
	rc = (jint)TO_HANDLE(EditingCommands::Backspace);
	OS_NATIVE_EXIT(env, that, EditingCommands_1Backspace_FUNC);
	return rc;
}
#endif

#ifndef NO_EditingCommands_1Delete
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1Delete)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1Delete)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EditingCommands_1Delete_FUNC);
	rc = (jint)TO_HANDLE(EditingCommands::Delete);
	OS_NATIVE_EXIT(env, that, EditingCommands_1Delete_FUNC);
	return rc;
}
#endif

#ifndef NO_EditingCommands_1DeleteNextWord
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1DeleteNextWord)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1DeleteNextWord)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EditingCommands_1DeleteNextWord_FUNC);
	rc = (jint)TO_HANDLE(EditingCommands::DeleteNextWord);
	OS_NATIVE_EXIT(env, that, EditingCommands_1DeleteNextWord_FUNC);
	return rc;
}
#endif

#ifndef NO_EditingCommands_1DeletePreviousWord
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1DeletePreviousWord)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(EditingCommands_1DeletePreviousWord)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EditingCommands_1DeletePreviousWord_FUNC);
	rc = (jint)TO_HANDLE(EditingCommands::DeletePreviousWord);
	OS_NATIVE_EXIT(env, that, EditingCommands_1DeletePreviousWord_FUNC);
	return rc;
}
#endif

#ifndef NO_Environment_1ExpandEnvironmentVariables
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Environment_1ExpandEnvironmentVariables)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Environment_1ExpandEnvironmentVariables)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Environment_1ExpandEnvironmentVariables_FUNC);
	rc = (jint)TO_HANDLE(Environment::ExpandEnvironmentVariables((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Environment_1ExpandEnvironmentVariables_FUNC);
	return rc;
}
#endif

#ifndef NO_ExecutedRoutedEventArgs_1Command
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ExecutedRoutedEventArgs_1Command)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ExecutedRoutedEventArgs_1Command)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExecutedRoutedEventArgs_1Command_FUNC);
	rc = (jint)TO_HANDLE(((ExecutedRoutedEventArgs^)TO_OBJECT(arg0))->Command);
	OS_NATIVE_EXIT(env, that, ExecutedRoutedEventArgs_1Command_FUNC);
	return rc;
}
#endif

#ifndef NO_ExecutedRoutedEventArgs_1Handled
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ExecutedRoutedEventArgs_1Handled)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ExecutedRoutedEventArgs_1Handled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ExecutedRoutedEventArgs_1Handled_FUNC);
	((ExecutedRoutedEventArgs^)TO_OBJECT(arg0))->Handled = (arg1);
	OS_NATIVE_EXIT(env, that, ExecutedRoutedEventArgs_1Handled_FUNC);
}
#endif

#ifndef NO_Expander_1Collapsed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Expander_1Collapsed)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Expander_1Collapsed)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Expander_1Collapsed_FUNC);
	((Expander^)TO_OBJECT(arg0))->Collapsed += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Expander_1Collapsed_FUNC);
}
#endif

#ifndef NO_Expander_1Expanded
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Expander_1Expanded)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Expander_1Expanded)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Expander_1Expanded_FUNC);
	((Expander^)TO_OBJECT(arg0))->Expanded += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Expander_1Expanded_FUNC);
}
#endif

#ifndef NO_Expander_1IsExpanded__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Expander_1IsExpanded__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Expander_1IsExpanded__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Expander_1IsExpanded__I_FUNC);
	rc = (jboolean)((Expander^)TO_OBJECT(arg0))->IsExpanded;
	OS_NATIVE_EXIT(env, that, Expander_1IsExpanded__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Expander_1IsExpanded__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Expander_1IsExpanded__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Expander_1IsExpanded__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Expander_1IsExpanded__IZ_FUNC);
	((Expander^)TO_OBJECT(arg0))->IsExpanded = (arg1);
	OS_NATIVE_EXIT(env, that, Expander_1IsExpanded__IZ_FUNC);
}
#endif

#ifndef NO_FileDialog_1FileName
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1FileName)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1FileName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FileDialog_1FileName_FUNC);
	((FileDialog^)TO_OBJECT(arg0))->FileName = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FileDialog_1FileName_FUNC);
}
#endif

#ifndef NO_FileDialog_1FileNames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FileDialog_1FileNames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FileDialog_1FileNames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FileDialog_1FileNames_FUNC);
	rc = (jint)TO_HANDLE(((FileDialog^)TO_OBJECT(arg0))->FileNames);
	OS_NATIVE_EXIT(env, that, FileDialog_1FileNames_FUNC);
	return rc;
}
#endif

#ifndef NO_FileDialog_1Filter
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1Filter)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1Filter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FileDialog_1Filter_FUNC);
	((FileDialog^)TO_OBJECT(arg0))->Filter = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FileDialog_1Filter_FUNC);
}
#endif

#ifndef NO_FileDialog_1FilterIndex__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FileDialog_1FilterIndex__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FileDialog_1FilterIndex__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FileDialog_1FilterIndex__I_FUNC);
	rc = (jint)((FileDialog^)TO_OBJECT(arg0))->FilterIndex;
	OS_NATIVE_EXIT(env, that, FileDialog_1FilterIndex__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FileDialog_1FilterIndex__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1FilterIndex__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1FilterIndex__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FileDialog_1FilterIndex__II_FUNC);
	((FileDialog^)TO_OBJECT(arg0))->FilterIndex = (arg1);
	OS_NATIVE_EXIT(env, that, FileDialog_1FilterIndex__II_FUNC);
}
#endif

#ifndef NO_FileDialog_1InitialDirectory
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1InitialDirectory)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1InitialDirectory)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FileDialog_1InitialDirectory_FUNC);
	((FileDialog^)TO_OBJECT(arg0))->InitialDirectory = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FileDialog_1InitialDirectory_FUNC);
}
#endif

#ifndef NO_FileDialog_1Title
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1Title)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FileDialog_1Title)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FileDialog_1Title_FUNC);
	((FileDialog^)TO_OBJECT(arg0))->Title = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FileDialog_1Title_FUNC);
}
#endif

#ifndef NO_FileInfo_1DirectoryName
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FileInfo_1DirectoryName)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FileInfo_1DirectoryName)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FileInfo_1DirectoryName_FUNC);
	rc = (jint)TO_HANDLE(((System::IO::FileInfo^)TO_OBJECT(arg0))->DirectoryName);
	OS_NATIVE_EXIT(env, that, FileInfo_1DirectoryName_FUNC);
	return rc;
}
#endif

#ifndef NO_FileInfo_1Name
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FileInfo_1Name)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FileInfo_1Name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FileInfo_1Name_FUNC);
	rc = (jint)TO_HANDLE(((System::IO::FileInfo^)TO_OBJECT(arg0))->Name);
	OS_NATIVE_EXIT(env, that, FileInfo_1Name_FUNC);
	return rc;
}
#endif

#ifndef NO_File_1Exists
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(File_1Exists)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(File_1Exists)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, File_1Exists_FUNC);
	rc = (jboolean)System::IO::File::Exists((String^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, File_1Exists_FUNC);
	return rc;
}
#endif

#ifndef NO_File_1ReadAllText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(File_1ReadAllText)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(File_1ReadAllText)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, File_1ReadAllText_FUNC);
	rc = (jint)TO_HANDLE(System::IO::File::ReadAllText((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, File_1ReadAllText_FUNC);
	return rc;
}
#endif

#ifndef NO_FocusManager_1GetFocusScope
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FocusManager_1GetFocusScope)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FocusManager_1GetFocusScope)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FocusManager_1GetFocusScope_FUNC);
	rc = (jint)TO_HANDLE(FocusManager::GetFocusScope((DependencyObject^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, FocusManager_1GetFocusScope_FUNC);
	return rc;
}
#endif

#ifndef NO_FocusManager_1GetFocusedElement
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FocusManager_1GetFocusedElement)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FocusManager_1GetFocusedElement)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FocusManager_1GetFocusedElement_FUNC);
	rc = (jint)TO_HANDLE(FocusManager::GetFocusedElement((DependencyObject^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, FocusManager_1GetFocusedElement_FUNC);
	return rc;
}
#endif

#ifndef NO_FolderBrowserDialog_1Description
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FolderBrowserDialog_1Description)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FolderBrowserDialog_1Description)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FolderBrowserDialog_1Description_FUNC);
	((System::Windows::Forms::FolderBrowserDialog^)TO_OBJECT(arg0))->Description = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FolderBrowserDialog_1Description_FUNC);
}
#endif

#ifndef NO_FolderBrowserDialog_1SelectedPath__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FolderBrowserDialog_1SelectedPath__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FolderBrowserDialog_1SelectedPath__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FolderBrowserDialog_1SelectedPath__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::FolderBrowserDialog^)TO_OBJECT(arg0))->SelectedPath);
	OS_NATIVE_EXIT(env, that, FolderBrowserDialog_1SelectedPath__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FolderBrowserDialog_1SelectedPath__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FolderBrowserDialog_1SelectedPath__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FolderBrowserDialog_1SelectedPath__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FolderBrowserDialog_1SelectedPath__II_FUNC);
	((System::Windows::Forms::FolderBrowserDialog^)TO_OBJECT(arg0))->SelectedPath = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FolderBrowserDialog_1SelectedPath__II_FUNC);
}
#endif

#ifndef NO_FontDialog_1Color__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontDialog_1Color__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontDialog_1Color__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontDialog_1Color__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::FontDialog^)TO_OBJECT(arg0))->Color);
	OS_NATIVE_EXIT(env, that, FontDialog_1Color__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FontDialog_1Color__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1Color__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1Color__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FontDialog_1Color__II_FUNC);
	((System::Windows::Forms::FontDialog^)TO_OBJECT(arg0))->Color = ((System::Drawing::Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FontDialog_1Color__II_FUNC);
}
#endif

#ifndef NO_FontDialog_1Font__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontDialog_1Font__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontDialog_1Font__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontDialog_1Font__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::FontDialog^)TO_OBJECT(arg0))->Font);
	OS_NATIVE_EXIT(env, that, FontDialog_1Font__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FontDialog_1Font__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1Font__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1Font__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FontDialog_1Font__II_FUNC);
	((System::Windows::Forms::FontDialog^)TO_OBJECT(arg0))->Font = ((System::Drawing::Font^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FontDialog_1Font__II_FUNC);
}
#endif

#ifndef NO_FontDialog_1ShowColor
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1ShowColor)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(FontDialog_1ShowColor)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, FontDialog_1ShowColor_FUNC);
	((System::Windows::Forms::FontDialog^)TO_OBJECT(arg0))->ShowColor = (arg1);
	OS_NATIVE_EXIT(env, that, FontDialog_1ShowColor_FUNC);
}
#endif

#ifndef NO_FontFamily_1GetTypefaces
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontFamily_1GetTypefaces)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontFamily_1GetTypefaces)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontFamily_1GetTypefaces_FUNC);
	rc = (jint)TO_HANDLE(((FontFamily^)TO_OBJECT(arg0))->GetTypefaces());
	OS_NATIVE_EXIT(env, that, FontFamily_1GetTypefaces_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1LineSpacing
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FontFamily_1LineSpacing)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FontFamily_1LineSpacing)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FontFamily_1LineSpacing_FUNC);
	rc = (jdouble)((FontFamily^)TO_OBJECT(arg0))->LineSpacing;
	OS_NATIVE_EXIT(env, that, FontFamily_1LineSpacing_FUNC);
	return rc;
}
#endif

#ifndef NO_FontFamily_1Source
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontFamily_1Source)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontFamily_1Source)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontFamily_1Source_FUNC);
	rc = (jint)TO_HANDLE(((FontFamily^)TO_OBJECT(arg0))->Source);
	OS_NATIVE_EXIT(env, that, FontFamily_1Source_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStretch_1FromOpenTypeStretch
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStretch_1FromOpenTypeStretch)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontStretch_1FromOpenTypeStretch)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStretch_1FromOpenTypeStretch_FUNC);
	rc = (jint)TO_HANDLE(FontStretch::FromOpenTypeStretch(arg0));
	OS_NATIVE_EXIT(env, that, FontStretch_1FromOpenTypeStretch_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStretch_1ToOpenTypeStretch
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStretch_1ToOpenTypeStretch)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontStretch_1ToOpenTypeStretch)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStretch_1ToOpenTypeStretch_FUNC);
	rc = (jint)((FontStretch^)TO_OBJECT(arg0))->ToOpenTypeStretch();
	OS_NATIVE_EXIT(env, that, FontStretch_1ToOpenTypeStretch_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStretches_1Normal
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStretches_1Normal)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontStretches_1Normal)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStretches_1Normal_FUNC);
	rc = (jint)TO_HANDLE(FontStretches::Normal);
	OS_NATIVE_EXIT(env, that, FontStretches_1Normal_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStyles_1Italic
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Italic)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Italic)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStyles_1Italic_FUNC);
	rc = (jint)TO_HANDLE(FontStyles::Italic);
	OS_NATIVE_EXIT(env, that, FontStyles_1Italic_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStyles_1Normal
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Normal)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Normal)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStyles_1Normal_FUNC);
	rc = (jint)TO_HANDLE(FontStyles::Normal);
	OS_NATIVE_EXIT(env, that, FontStyles_1Normal_FUNC);
	return rc;
}
#endif

#ifndef NO_FontStyles_1Oblique
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Oblique)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontStyles_1Oblique)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontStyles_1Oblique_FUNC);
	rc = (jint)TO_HANDLE(FontStyles::Oblique);
	OS_NATIVE_EXIT(env, that, FontStyles_1Oblique_FUNC);
	return rc;
}
#endif

#ifndef NO_FontWeight_1FromOpenTypeWeight
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontWeight_1FromOpenTypeWeight)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontWeight_1FromOpenTypeWeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontWeight_1FromOpenTypeWeight_FUNC);
	rc = (jint)TO_HANDLE(FontWeight::FromOpenTypeWeight(arg0));
	OS_NATIVE_EXIT(env, that, FontWeight_1FromOpenTypeWeight_FUNC);
	return rc;
}
#endif

#ifndef NO_FontWeight_1ToOpenTypeWeight
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontWeight_1ToOpenTypeWeight)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FontWeight_1ToOpenTypeWeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontWeight_1ToOpenTypeWeight_FUNC);
	rc = (jint)((FontWeight^)TO_OBJECT(arg0))->ToOpenTypeWeight();
	OS_NATIVE_EXIT(env, that, FontWeight_1ToOpenTypeWeight_FUNC);
	return rc;
}
#endif

#ifndef NO_FontWeights_1Bold
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontWeights_1Bold)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontWeights_1Bold)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontWeights_1Bold_FUNC);
	rc = (jint)TO_HANDLE(FontWeights::Bold);
	OS_NATIVE_EXIT(env, that, FontWeights_1Bold_FUNC);
	return rc;
}
#endif

#ifndef NO_FontWeights_1Normal
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FontWeights_1Normal)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FontWeights_1Normal)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FontWeights_1Normal_FUNC);
	rc = (jint)TO_HANDLE(FontWeights::Normal);
	OS_NATIVE_EXIT(env, that, FontWeights_1Normal_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1FontFamily
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Font_1FontFamily)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Font_1FontFamily)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Font_1FontFamily_FUNC);
	rc = (jint)TO_HANDLE(((System::Drawing::Font^)TO_OBJECT(arg0))->FontFamily);
	OS_NATIVE_EXIT(env, that, Font_1FontFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1Size
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Font_1Size)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Font_1Size)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Font_1Size_FUNC);
	rc = (jint)((System::Drawing::Font^)TO_OBJECT(arg0))->Size;
	OS_NATIVE_EXIT(env, that, Font_1Size_FUNC);
	return rc;
}
#endif

#ifndef NO_Font_1Style
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Font_1Style)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Font_1Style)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Font_1Style_FUNC);
	rc = (jint)((System::Drawing::Font^)TO_OBJECT(arg0))->Style;
	OS_NATIVE_EXIT(env, that, Font_1Style_FUNC);
	return rc;
}
#endif

#ifndef NO_Fonts_1GetTypefaces
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Fonts_1GetTypefaces)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Fonts_1GetTypefaces)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Fonts_1GetTypefaces_FUNC);
	rc = (jint)TO_HANDLE(Fonts::GetTypefaces((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Fonts_1GetTypefaces_FUNC);
	return rc;
}
#endif

#ifndef NO_Fonts_1SystemTypefaces
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Fonts_1SystemTypefaces)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Fonts_1SystemTypefaces)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Fonts_1SystemTypefaces_FUNC);
	rc = (jint)TO_HANDLE(Fonts::SystemTypefaces);
	OS_NATIVE_EXIT(env, that, Fonts_1SystemTypefaces_FUNC);
	return rc;
}
#endif

#ifndef NO_FormattedText_1Baseline
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1Baseline)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1Baseline)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FormattedText_1Baseline_FUNC);
	rc = (jdouble)((FormattedText^)TO_OBJECT(arg0))->Baseline;
	OS_NATIVE_EXIT(env, that, FormattedText_1Baseline_FUNC);
	return rc;
}
#endif

#ifndef NO_FormattedText_1BuildGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FormattedText_1BuildGeometry)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(FormattedText_1BuildGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormattedText_1BuildGeometry_FUNC);
	rc = (jint)TO_HANDLE(((FormattedText^)TO_OBJECT(arg0))->BuildGeometry((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, FormattedText_1BuildGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_FormattedText_1BuildHighlightGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FormattedText_1BuildHighlightGeometry)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(FormattedText_1BuildHighlightGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormattedText_1BuildHighlightGeometry_FUNC);
	rc = (jint)TO_HANDLE(((FormattedText^)TO_OBJECT(arg0))->BuildHighlightGeometry((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, FormattedText_1BuildHighlightGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_FormattedText_1Height
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1Height)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FormattedText_1Height_FUNC);
	rc = (jdouble)((FormattedText^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, FormattedText_1Height_FUNC);
	return rc;
}
#endif

#ifndef NO_FormattedText_1SetTextDecorations
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FormattedText_1SetTextDecorations)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(FormattedText_1SetTextDecorations)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, FormattedText_1SetTextDecorations_FUNC);
	((FormattedText^)TO_OBJECT(arg0))->SetTextDecorations((TextDecorationCollection^)TO_OBJECT(arg1), arg2, arg3);
	OS_NATIVE_EXIT(env, that, FormattedText_1SetTextDecorations_FUNC);
}
#endif

#ifndef NO_FormattedText_1WidthIncludingTrailingWhitespace
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1WidthIncludingTrailingWhitespace)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FormattedText_1WidthIncludingTrailingWhitespace)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FormattedText_1WidthIncludingTrailingWhitespace_FUNC);
	rc = (jdouble)((FormattedText^)TO_OBJECT(arg0))->WidthIncludingTrailingWhitespace;
	OS_NATIVE_EXIT(env, that, FormattedText_1WidthIncludingTrailingWhitespace_FUNC);
	return rc;
}
#endif

#ifndef NO_FormsCommonDialog_1ShowDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FormsCommonDialog_1ShowDialog)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FormsCommonDialog_1ShowDialog)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormsCommonDialog_1ShowDialog_FUNC);
	rc = (jint)((System::Windows::Forms::CommonDialog^)TO_OBJECT(arg0))->ShowDialog();
	OS_NATIVE_EXIT(env, that, FormsCommonDialog_1ShowDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_FormsMouseEventArgs_1Button
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FormsMouseEventArgs_1Button)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FormsMouseEventArgs_1Button)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormsMouseEventArgs_1Button_FUNC);
	rc = (jint)((System::Windows::Forms::MouseEventArgs^)TO_OBJECT(arg0))->Button;
	OS_NATIVE_EXIT(env, that, FormsMouseEventArgs_1Button_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1CanGoBack
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1CanGoBack)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1CanGoBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Frame_1CanGoBack_FUNC);
	rc = (jboolean)((Frame^)TO_OBJECT(arg0))->CanGoBack;
	OS_NATIVE_EXIT(env, that, Frame_1CanGoBack_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1CanGoForward
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1CanGoForward)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1CanGoForward)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Frame_1CanGoForward_FUNC);
	rc = (jboolean)((Frame^)TO_OBJECT(arg0))->CanGoForward;
	OS_NATIVE_EXIT(env, that, Frame_1CanGoForward_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1CurrentSource
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Frame_1CurrentSource)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Frame_1CurrentSource)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Frame_1CurrentSource_FUNC);
	rc = (jint)TO_HANDLE(((Frame^)TO_OBJECT(arg0))->CurrentSource);
	OS_NATIVE_EXIT(env, that, Frame_1CurrentSource_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1GoBack
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1GoBack)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1GoBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Frame_1GoBack_FUNC);
	((Frame^)TO_OBJECT(arg0))->GoBack();
	OS_NATIVE_EXIT(env, that, Frame_1GoBack_FUNC);
}
#endif

#ifndef NO_Frame_1GoForward
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1GoForward)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1GoForward)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Frame_1GoForward_FUNC);
	((Frame^)TO_OBJECT(arg0))->GoForward();
	OS_NATIVE_EXIT(env, that, Frame_1GoForward_FUNC);
}
#endif

#ifndef NO_Frame_1Navigate
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1Navigate)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Frame_1Navigate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Frame_1Navigate_FUNC);
	rc = (jboolean)((Frame^)TO_OBJECT(arg0))->Navigate((Uri^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Frame_1Navigate_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1NavigationUIVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1NavigationUIVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1NavigationUIVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Frame_1NavigationUIVisibility_FUNC);
	((Frame^)TO_OBJECT(arg0))->NavigationUIVisibility = ((System::Windows::Navigation::NavigationUIVisibility)arg1);
	OS_NATIVE_EXIT(env, that, Frame_1NavigationUIVisibility_FUNC);
}
#endif

#ifndef NO_Frame_1Refresh
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1Refresh)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1Refresh)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Frame_1Refresh_FUNC);
	((Frame^)TO_OBJECT(arg0))->Refresh();
	OS_NATIVE_EXIT(env, that, Frame_1Refresh_FUNC);
}
#endif

#ifndef NO_Frame_1Source__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Frame_1Source__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Frame_1Source__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Frame_1Source__I_FUNC);
	rc = (jint)TO_HANDLE(((Frame^)TO_OBJECT(arg0))->Source);
	OS_NATIVE_EXIT(env, that, Frame_1Source__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Frame_1Source__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1Source__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1Source__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Frame_1Source__II_FUNC);
	((Frame^)TO_OBJECT(arg0))->Source = ((Uri^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Frame_1Source__II_FUNC);
}
#endif

#ifndef NO_Frame_1StopLoading
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Frame_1StopLoading)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Frame_1StopLoading)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Frame_1StopLoading_FUNC);
	((Frame^)TO_OBJECT(arg0))->StopLoading();
	OS_NATIVE_EXIT(env, that, Frame_1StopLoading_FUNC);
}
#endif

#ifndef NO_FrameworkContentElement_1Parent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1Parent)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1Parent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkContentElement_1Parent_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkContentElement^)TO_OBJECT(arg0))->Parent);
	OS_NATIVE_EXIT(env, that, FrameworkContentElement_1Parent_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkContentElement_1Tag__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1Tag__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1Tag__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkContentElement_1Tag__I_FUNC);
	rc = (jint)((FrameworkContentElement^)TO_OBJECT(arg0))->Tag;
	OS_NATIVE_EXIT(env, that, FrameworkContentElement_1Tag__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkContentElement_1Tag__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkContentElement_1Tag__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkContentElement_1Tag__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkContentElement_1Tag__II_FUNC);
	((FrameworkContentElement^)TO_OBJECT(arg0))->Tag = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkContentElement_1Tag__II_FUNC);
}
#endif

#ifndef NO_FrameworkContentElement_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkContentElement_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkContentElement_1typeid_FUNC);
	rc = (jint)TO_HANDLE(FrameworkContentElement::typeid);
	OS_NATIVE_EXIT(env, that, FrameworkContentElement_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElementFactory_1AppendChild
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1AppendChild)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1AppendChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1AppendChild_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->AppendChild((FrameworkElementFactory^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1AppendChild_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetBinding
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetBinding)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetBinding)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetBinding_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetBinding((DependencyProperty^)TO_OBJECT(arg1), (BindingBase^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetBinding_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValue__III
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValue__III)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValue__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValue__III_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValue__III_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValue__IIZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValue__IIZ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValue__IIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValue__IIZ_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValue__IIZ_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueDock
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueDock)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueDock)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueDock_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Dock)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueDock_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueInt
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueInt)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueInt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueInt_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueInt_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueOrientation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueOrientation)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueOrientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueOrientation_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Orientation)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueOrientation_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueStretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueStretch)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueStretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueStretch_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Stretch)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueStretch_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueVerticalAlignment
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueVerticalAlignment)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueVerticalAlignment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueVerticalAlignment_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (VerticalAlignment)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueVerticalAlignment_FUNC);
}
#endif

#ifndef NO_FrameworkElementFactory_1SetValueVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElementFactory_1SetValueVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElementFactory_1SetValueVisibility_FUNC);
	((FrameworkElementFactory^)TO_OBJECT(arg0))->SetValue((DependencyProperty^)TO_OBJECT(arg1), (Visibility)arg2);
	OS_NATIVE_EXIT(env, that, FrameworkElementFactory_1SetValueVisibility_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1ActualHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1ActualHeight)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1ActualHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ActualHeight_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->ActualHeight;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ActualHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1ActualHeightProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ActualHeightProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ActualHeightProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ActualHeightProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::ActualHeightProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ActualHeightProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1ActualWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1ActualWidth)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1ActualWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ActualWidth_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->ActualWidth;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ActualWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1ActualWidthProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ActualWidthProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ActualWidthProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ActualWidthProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::ActualWidthProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ActualWidthProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1BeginInit
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1BeginInit)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1BeginInit)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1BeginInit_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->BeginInit();
	OS_NATIVE_EXIT(env, that, FrameworkElement_1BeginInit_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1BringIntoView
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1BringIntoView)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1BringIntoView)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1BringIntoView_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->BringIntoView();
	OS_NATIVE_EXIT(env, that, FrameworkElement_1BringIntoView_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1ContextMenu
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenu)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ContextMenu_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->ContextMenu = ((ContextMenu^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ContextMenu_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1ContextMenuClosing
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenuClosing)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenuClosing)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ContextMenuClosing_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->ContextMenuClosing += ((ContextMenuEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ContextMenuClosing_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1ContextMenuOpening
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenuOpening)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ContextMenuOpening)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ContextMenuOpening_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->ContextMenuOpening += ((ContextMenuEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ContextMenuOpening_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1Cursor
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Cursor)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Cursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Cursor_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Cursor = ((Cursor^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Cursor_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1CursorProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1CursorProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1CursorProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1CursorProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::CursorProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1CursorProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1FindResource
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1FindResource)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1FindResource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1FindResource_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->FindResource((Object^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1FindResource_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1FlowDirection__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1FlowDirection__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1FlowDirection__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1FlowDirection__I_FUNC);
	rc = (jint)((FrameworkElement^)TO_OBJECT(arg0))->FlowDirection;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1FlowDirection__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1FlowDirection__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1FlowDirection__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1FlowDirection__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1FlowDirection__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->FlowDirection = ((FlowDirection)arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1FlowDirection__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1FocusVisualStyle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1FocusVisualStyle)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1FocusVisualStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1FocusVisualStyle_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->FocusVisualStyle = ((Style^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1FocusVisualStyle_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1GetBindingExpression
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1GetBindingExpression)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1GetBindingExpression)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1GetBindingExpression_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->GetBindingExpression((DependencyProperty^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1GetBindingExpression_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Height__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1Height__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1Height__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Height__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Height__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Height__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Height__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Height__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Height__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Height = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Height__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1HeightProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1HeightProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1HeightProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1HeightProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::HeightProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1HeightProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1HorizontalAlignment
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1HorizontalAlignment)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1HorizontalAlignment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1HorizontalAlignment_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->HorizontalAlignment = ((HorizontalAlignment)arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1HorizontalAlignment_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1IsLoaded
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(FrameworkElement_1IsLoaded)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(FrameworkElement_1IsLoaded)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1IsLoaded_FUNC);
	rc = (jboolean)((FrameworkElement^)TO_OBJECT(arg0))->IsLoaded;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1IsLoaded_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1LayoutTransform
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1LayoutTransform)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1LayoutTransform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1LayoutTransform_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->LayoutTransform = ((Transform^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1LayoutTransform_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1Loaded
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Loaded)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Loaded)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Loaded_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Loaded += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Loaded_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1Margin__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Margin__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Margin__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Margin__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Margin);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Margin__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Margin__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Margin__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Margin__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Margin__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Margin = ((Thickness)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Margin__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1MarginProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1MarginProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1MarginProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MarginProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::MarginProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MarginProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1MaxHeight__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MaxHeight__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MaxHeight__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MaxHeight__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->MaxHeight;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MaxHeight__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1MaxHeight__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MaxHeight__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MaxHeight__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MaxHeight__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->MaxHeight = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MaxHeight__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1MaxWidth__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MaxWidth__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MaxWidth__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MaxWidth__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->MaxWidth;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MaxWidth__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1MaxWidth__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MaxWidth__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MaxWidth__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MaxWidth__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->MaxWidth = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MaxWidth__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1MinHeight__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MinHeight__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MinHeight__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MinHeight__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->MinHeight;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MinHeight__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1MinHeight__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MinHeight__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MinHeight__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MinHeight__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->MinHeight = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MinHeight__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1MinWidth__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MinWidth__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1MinWidth__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MinWidth__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->MinWidth;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MinWidth__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1MinWidth__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MinWidth__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1MinWidth__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1MinWidth__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->MinWidth = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1MinWidth__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1Name
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Name)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Name_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Name);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Name_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1NameProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1NameProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1NameProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1NameProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::NameProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1NameProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Parent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Parent)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Parent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Parent_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Parent);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Parent_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1RegisterName
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1RegisterName)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1RegisterName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1RegisterName_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->RegisterName((String^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1RegisterName_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1RenderTransform__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1RenderTransform__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1RenderTransform__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1RenderTransform__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->RenderTransform);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1RenderTransform__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1RenderTransform__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1RenderTransform__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1RenderTransform__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1RenderTransform__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->RenderTransform = ((Transform^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1RenderTransform__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1Resources__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Resources__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Resources__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Resources__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Resources);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Resources__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Resources__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Resources__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Resources__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Resources__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Resources = ((ResourceDictionary^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Resources__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1SizeChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1SizeChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1SizeChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1SizeChanged_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->SizeChanged += ((SizeChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1SizeChanged_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1SizeChangedEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1SizeChangedEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1SizeChangedEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1SizeChangedEvent_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::SizeChangedEvent);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1SizeChangedEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Style__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Style__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Style__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Style__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Style);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Style__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Style__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Style__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Style__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Style__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Style = ((Style^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Style__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1StyleProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1StyleProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1StyleProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1StyleProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::StyleProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1StyleProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Tag__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Tag__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1Tag__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Tag__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->Tag);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Tag__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Tag__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Tag__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Tag__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Tag__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Tag = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Tag__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1TagProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1TagProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1TagProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1TagProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::TagProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1TagProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1ToolTip__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ToolTip__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1ToolTip__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ToolTip__I_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkElement^)TO_OBJECT(arg0))->ToolTip);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ToolTip__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1ToolTip__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ToolTip__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1ToolTip__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1ToolTip__II_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->ToolTip = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkElement_1ToolTip__II_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1VerticalAlignment
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1VerticalAlignment)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1VerticalAlignment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1VerticalAlignment_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->VerticalAlignment = ((VerticalAlignment)arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1VerticalAlignment_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1VerticalAlignmentProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1VerticalAlignmentProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1VerticalAlignmentProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1VerticalAlignmentProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::VerticalAlignmentProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1VerticalAlignmentProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Width__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1Width__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(FrameworkElement_1Width__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Width__I_FUNC);
	rc = (jdouble)((FrameworkElement^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Width__I_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1Width__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Width__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkElement_1Width__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkElement_1Width__ID_FUNC);
	((FrameworkElement^)TO_OBJECT(arg0))->Width = (arg1);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1Width__ID_FUNC);
}
#endif

#ifndef NO_FrameworkElement_1WidthProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1WidthProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1WidthProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1WidthProperty_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::WidthProperty);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1WidthProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkElement_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkElement_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkElement_1typeid_FUNC);
	rc = (jint)TO_HANDLE(FrameworkElement::typeid);
	OS_NATIVE_EXIT(env, that, FrameworkElement_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkTemplate_1FindName
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(FrameworkTemplate_1FindName)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(FrameworkTemplate_1FindName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrameworkTemplate_1FindName_FUNC);
	rc = (jint)TO_HANDLE(((FrameworkTemplate^)TO_OBJECT(arg0))->FindName((String^)TO_OBJECT(arg1), (FrameworkElement^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, FrameworkTemplate_1FindName_FUNC);
	return rc;
}
#endif

#ifndef NO_FrameworkTemplate_1VisualTree
extern "C" JNIEXPORT void JNICALL OS_NATIVE(FrameworkTemplate_1VisualTree)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(FrameworkTemplate_1VisualTree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, FrameworkTemplate_1VisualTree_FUNC);
	((FrameworkTemplate^)TO_OBJECT(arg0))->VisualTree = ((FrameworkElementFactory^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, FrameworkTemplate_1VisualTree_FUNC);
}
#endif

#ifndef NO_Freezable_1CanFreeze
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Freezable_1CanFreeze)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Freezable_1CanFreeze)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Freezable_1CanFreeze_FUNC);
	rc = (jboolean)((Freezable^)TO_OBJECT(arg0))->CanFreeze;
	OS_NATIVE_EXIT(env, that, Freezable_1CanFreeze_FUNC);
	return rc;
}
#endif

#ifndef NO_Freezable_1Clone
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Freezable_1Clone)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Freezable_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Freezable_1Clone_FUNC);
	rc = (jint)TO_HANDLE(((Freezable^)TO_OBJECT(arg0))->Clone());
	OS_NATIVE_EXIT(env, that, Freezable_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_Freezable_1Freeze
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Freezable_1Freeze)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Freezable_1Freeze)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Freezable_1Freeze_FUNC);
	((Freezable^)TO_OBJECT(arg0))->Freeze();
	OS_NATIVE_EXIT(env, that, Freezable_1Freeze_FUNC);
}
#endif

#ifndef NO_GeometryCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GeometryCollection_1Add_FUNC);
	((GeometryCollection^)TO_OBJECT(arg0))->Add((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GeometryCollection_1Add_FUNC);
}
#endif

#ifndef NO_GeometryCollection_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, GeometryCollection_1Clear_FUNC);
	((GeometryCollection^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, GeometryCollection_1Clear_FUNC);
}
#endif

#ifndef NO_GeometryCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GeometryCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GeometryCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GeometryCollection_1Count_FUNC);
	rc = (jint)((GeometryCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, GeometryCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_GeometryCollection_1Remove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GeometryCollection_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GeometryCollection_1Remove_FUNC);
	((GeometryCollection^)TO_OBJECT(arg0))->Remove((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GeometryCollection_1Remove_FUNC);
}
#endif

#ifndef NO_GeometryGroup_1Children__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GeometryGroup_1Children__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GeometryGroup_1Children__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GeometryGroup_1Children__I_FUNC);
	rc = (jint)TO_HANDLE(((GeometryGroup^)TO_OBJECT(arg0))->Children);
	OS_NATIVE_EXIT(env, that, GeometryGroup_1Children__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GeometryGroup_1Children__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GeometryGroup_1Children__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(GeometryGroup_1Children__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GeometryGroup_1Children__II_FUNC);
	rc = (jint)TO_HANDLE(((GeometryGroup^)TO_OBJECT(arg0))->Children[arg1]);
	OS_NATIVE_EXIT(env, that, GeometryGroup_1Children__II_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1Bounds
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Bounds)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Bounds)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1Bounds_FUNC);
	rc = (jint)TO_HANDLE(((Geometry^)TO_OBJECT(arg0))->Bounds);
	OS_NATIVE_EXIT(env, that, Geometry_1Bounds_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1Clone
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Clone)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1Clone_FUNC);
	rc = (jint)TO_HANDLE(((Geometry^)TO_OBJECT(arg0))->Clone());
	OS_NATIVE_EXIT(env, that, Geometry_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1FillContains
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1FillContains)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1FillContains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1FillContains_FUNC);
	rc = (jboolean)((Geometry^)TO_OBJECT(arg0))->FillContains((Point)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Geometry_1FillContains_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1FillContainsWithDetail
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1FillContainsWithDetail)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1FillContainsWithDetail)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1FillContainsWithDetail_FUNC);
	rc = (jint)((Geometry^)TO_OBJECT(arg0))->FillContainsWithDetail((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Geometry_1FillContainsWithDetail_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1GetFlattenedPathGeometry__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1GetFlattenedPathGeometry__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1GetFlattenedPathGeometry__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1GetFlattenedPathGeometry__I_FUNC);
	rc = (jint)TO_HANDLE(((Geometry^)TO_OBJECT(arg0))->GetFlattenedPathGeometry());
	OS_NATIVE_EXIT(env, that, Geometry_1GetFlattenedPathGeometry__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1GetFlattenedPathGeometry__IDI
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1GetFlattenedPathGeometry__IDI)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1GetFlattenedPathGeometry__IDI)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1GetFlattenedPathGeometry__IDI_FUNC);
	rc = (jint)TO_HANDLE(((Geometry^)TO_OBJECT(arg0))->GetFlattenedPathGeometry(arg1, (ToleranceType)arg2));
	OS_NATIVE_EXIT(env, that, Geometry_1GetFlattenedPathGeometry__IDI_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1IsEmpty
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1IsEmpty)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1IsEmpty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1IsEmpty_FUNC);
	rc = (jboolean)((Geometry^)TO_OBJECT(arg0))->IsEmpty();
	OS_NATIVE_EXIT(env, that, Geometry_1IsEmpty_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1StrokeContains
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1StrokeContains)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jboolean JNICALL OS_NATIVE(Geometry_1StrokeContains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1StrokeContains_FUNC);
	rc = (jboolean)((Geometry^)TO_OBJECT(arg0))->StrokeContains((Pen^)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, Geometry_1StrokeContains_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1Transform__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Transform__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Geometry_1Transform__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Geometry_1Transform__I_FUNC);
	rc = (jint)TO_HANDLE(((Geometry^)TO_OBJECT(arg0))->Transform);
	OS_NATIVE_EXIT(env, that, Geometry_1Transform__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Geometry_1Transform__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Geometry_1Transform__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Geometry_1Transform__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Geometry_1Transform__II_FUNC);
	((Geometry^)TO_OBJECT(arg0))->Transform = ((Transform^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Geometry_1Transform__II_FUNC);
}
#endif

#ifndef NO_GiveFeedbackEventArgs_1Effects
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GiveFeedbackEventArgs_1Effects)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GiveFeedbackEventArgs_1Effects)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GiveFeedbackEventArgs_1Effects_FUNC);
	rc = (jint)((GiveFeedbackEventArgs^)TO_OBJECT(arg0))->Effects;
	OS_NATIVE_EXIT(env, that, GiveFeedbackEventArgs_1Effects_FUNC);
	return rc;
}
#endif

#ifndef NO_GlyphRun_1BidiLevel
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GlyphRun_1BidiLevel)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GlyphRun_1BidiLevel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlyphRun_1BidiLevel_FUNC);
	rc = (jint)((GlyphRun^)TO_OBJECT(arg0))->BidiLevel;
	OS_NATIVE_EXIT(env, that, GlyphRun_1BidiLevel_FUNC);
	return rc;
}
#endif

#ifndef NO_GradientBrush_1MappingMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GradientBrush_1MappingMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GradientBrush_1MappingMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GradientBrush_1MappingMode_FUNC);
	((GradientBrush^)TO_OBJECT(arg0))->MappingMode = ((BrushMappingMode)arg1);
	OS_NATIVE_EXIT(env, that, GradientBrush_1MappingMode_FUNC);
}
#endif

#ifndef NO_GradientBrush_1SpreadMethod
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GradientBrush_1SpreadMethod)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GradientBrush_1SpreadMethod)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GradientBrush_1SpreadMethod_FUNC);
	((GradientBrush^)TO_OBJECT(arg0))->SpreadMethod = ((GradientSpreadMethod)arg1);
	OS_NATIVE_EXIT(env, that, GradientBrush_1SpreadMethod_FUNC);
}
#endif

#ifndef NO_GridViewColumnCollection_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnCollection_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnCollection_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1Clear_FUNC);
	((GridViewColumnCollection^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1Clear_FUNC);
}
#endif

#ifndef NO_GridViewColumnCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1Count_FUNC);
	rc = (jint)((GridViewColumnCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumnCollection_1IndexOf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1IndexOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1IndexOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1IndexOf_FUNC);
	rc = (jint)((GridViewColumnCollection ^)TO_OBJECT(arg0))->IndexOf((GridViewColumn^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1IndexOf_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumnCollection_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnCollection_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnCollection_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1Insert_FUNC);
	((GridViewColumnCollection^)TO_OBJECT(arg0))->Insert(arg1, (GridViewColumn^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1Insert_FUNC);
}
#endif

#ifndef NO_GridViewColumnCollection_1Remove
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(GridViewColumnCollection_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(GridViewColumnCollection_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1Remove_FUNC);
	rc = (jboolean)((GridViewColumnCollection^)TO_OBJECT(arg0))->Remove((GridViewColumn^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1Remove_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumnCollection_1default
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1default)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumnCollection_1default)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumnCollection_1default_FUNC);
	rc = (jint)TO_HANDLE(((GridViewColumnCollection^)TO_OBJECT(arg0))->default[arg1]);
	OS_NATIVE_EXIT(env, that, GridViewColumnCollection_1default_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumnHeader_1Content
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnHeader_1Content)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumnHeader_1Content)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewColumnHeader_1Content_FUNC);
	((GridViewColumnHeader^)TO_OBJECT(arg0))->Content = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumnHeader_1Content_FUNC);
}
#endif

#ifndef NO_GridViewColumn_1ActualWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(GridViewColumn_1ActualWidth)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(GridViewColumn_1ActualWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1ActualWidth_FUNC);
	rc = (jdouble)((GridViewColumn^)TO_OBJECT(arg0))->ActualWidth;
	OS_NATIVE_EXIT(env, that, GridViewColumn_1ActualWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumn_1CellTemplate__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1CellTemplate__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1CellTemplate__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1CellTemplate__I_FUNC);
	rc = (jint)TO_HANDLE(((GridViewColumn^)TO_OBJECT(arg0))->CellTemplate);
	OS_NATIVE_EXIT(env, that, GridViewColumn_1CellTemplate__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumn_1CellTemplate__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1CellTemplate__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1CellTemplate__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewColumn_1CellTemplate__II_FUNC);
	((GridViewColumn^)TO_OBJECT(arg0))->CellTemplate = ((DataTemplate^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumn_1CellTemplate__II_FUNC);
}
#endif

#ifndef NO_GridViewColumn_1Header__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1Header__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1Header__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1Header__I_FUNC);
	rc = (jint)TO_HANDLE(((GridViewColumn^)TO_OBJECT(arg0))->Header);
	OS_NATIVE_EXIT(env, that, GridViewColumn_1Header__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumn_1Header__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1Header__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1Header__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewColumn_1Header__II_FUNC);
	((GridViewColumn^)TO_OBJECT(arg0))->Header = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumn_1Header__II_FUNC);
}
#endif

#ifndef NO_GridViewColumn_1HeaderTemplate__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1HeaderTemplate__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1HeaderTemplate__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1HeaderTemplate__I_FUNC);
	rc = (jint)TO_HANDLE(((GridViewColumn^)TO_OBJECT(arg0))->HeaderTemplate);
	OS_NATIVE_EXIT(env, that, GridViewColumn_1HeaderTemplate__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumn_1HeaderTemplate__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1HeaderTemplate__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1HeaderTemplate__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewColumn_1HeaderTemplate__II_FUNC);
	((GridViewColumn^)TO_OBJECT(arg0))->HeaderTemplate = ((DataTemplate^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewColumn_1HeaderTemplate__II_FUNC);
}
#endif

#ifndef NO_GridViewColumn_1Width__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(GridViewColumn_1Width__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(GridViewColumn_1Width__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1Width__I_FUNC);
	rc = (jdouble)((GridViewColumn^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, GridViewColumn_1Width__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewColumn_1Width__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1Width__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewColumn_1Width__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewColumn_1Width__ID_FUNC);
	((GridViewColumn^)TO_OBJECT(arg0))->Width = (arg1);
	OS_NATIVE_EXIT(env, that, GridViewColumn_1Width__ID_FUNC);
}
#endif

#ifndef NO_GridViewColumn_1WidthProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1WidthProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewColumn_1WidthProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewColumn_1WidthProperty_FUNC);
	rc = (jint)TO_HANDLE(GridViewColumn::WidthProperty);
	OS_NATIVE_EXIT(env, that, GridViewColumn_1WidthProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewHeaderRowPresenter_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewHeaderRowPresenter_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewHeaderRowPresenter_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewHeaderRowPresenter_1typeid_FUNC);
	rc = (jint)TO_HANDLE(GridViewHeaderRowPresenter::typeid);
	OS_NATIVE_EXIT(env, that, GridViewHeaderRowPresenter_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewRowPresenterBase_1Columns
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewRowPresenterBase_1Columns)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewRowPresenterBase_1Columns)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewRowPresenterBase_1Columns_FUNC);
	((GridViewRowPresenterBase^)TO_OBJECT(arg0))->Columns = ((GridViewColumnCollection^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewRowPresenterBase_1Columns_FUNC);
}
#endif

#ifndef NO_GridViewRowPresenterBase_1ColumnsProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenterBase_1ColumnsProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenterBase_1ColumnsProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewRowPresenterBase_1ColumnsProperty_FUNC);
	rc = (jint)TO_HANDLE(GridViewRowPresenterBase::ColumnsProperty);
	OS_NATIVE_EXIT(env, that, GridViewRowPresenterBase_1ColumnsProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewRowPresenter_1Content__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenter_1Content__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenter_1Content__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewRowPresenter_1Content__I_FUNC);
	rc = (jint)TO_HANDLE(((GridViewRowPresenter^)TO_OBJECT(arg0))->Content);
	OS_NATIVE_EXIT(env, that, GridViewRowPresenter_1Content__I_FUNC);
	return rc;
}
#endif

#ifndef NO_GridViewRowPresenter_1Content__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridViewRowPresenter_1Content__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridViewRowPresenter_1Content__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridViewRowPresenter_1Content__II_FUNC);
	((GridViewRowPresenter^)TO_OBJECT(arg0))->Content = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridViewRowPresenter_1Content__II_FUNC);
}
#endif

#ifndef NO_GridViewRowPresenter_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenter_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(GridViewRowPresenter_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridViewRowPresenter_1typeid_FUNC);
	rc = (jint)TO_HANDLE(GridViewRowPresenter::typeid);
	OS_NATIVE_EXIT(env, that, GridViewRowPresenter_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_GridView_1AllowsColumnReorder
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridView_1AllowsColumnReorder)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridView_1AllowsColumnReorder)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, GridView_1AllowsColumnReorder_FUNC);
	((GridView^)TO_OBJECT(arg0))->AllowsColumnReorder = (arg1);
	OS_NATIVE_EXIT(env, that, GridView_1AllowsColumnReorder_FUNC);
}
#endif

#ifndef NO_GridView_1ColumnHeaderContainerStyle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GridView_1ColumnHeaderContainerStyle)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(GridView_1ColumnHeaderContainerStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GridView_1ColumnHeaderContainerStyle_FUNC);
	((GridView^)TO_OBJECT(arg0))->ColumnHeaderContainerStyle = ((Style^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, GridView_1ColumnHeaderContainerStyle_FUNC);
}
#endif

#ifndef NO_GridView_1Columns
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GridView_1Columns)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GridView_1Columns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GridView_1Columns_FUNC);
	rc = (jint)TO_HANDLE(((GridView^)TO_OBJECT(arg0))->Columns);
	OS_NATIVE_EXIT(env, that, GridView_1Columns_FUNC);
	return rc;
}
#endif

#ifndef NO_Grid_1ColumnDefinitions
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Grid_1ColumnDefinitions)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Grid_1ColumnDefinitions)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Grid_1ColumnDefinitions_FUNC);
	rc = (jint)TO_HANDLE(((Grid^)TO_OBJECT(arg0))->ColumnDefinitions);
	OS_NATIVE_EXIT(env, that, Grid_1ColumnDefinitions_FUNC);
	return rc;
}
#endif

#ifndef NO_Grid_1RowDefinitions
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Grid_1RowDefinitions)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Grid_1RowDefinitions)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Grid_1RowDefinitions_FUNC);
	rc = (jint)TO_HANDLE(((Grid^)TO_OBJECT(arg0))->RowDefinitions);
	OS_NATIVE_EXIT(env, that, Grid_1RowDefinitions_FUNC);
	return rc;
}
#endif

#ifndef NO_Grid_1SetColumn
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetColumn)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetColumn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Grid_1SetColumn_FUNC);
	Grid::SetColumn((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Grid_1SetColumn_FUNC);
}
#endif

#ifndef NO_Grid_1SetColumnSpan
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetColumnSpan)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetColumnSpan)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Grid_1SetColumnSpan_FUNC);
	Grid::SetColumnSpan((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Grid_1SetColumnSpan_FUNC);
}
#endif

#ifndef NO_Grid_1SetRow
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetRow)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetRow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Grid_1SetRow_FUNC);
	Grid::SetRow((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Grid_1SetRow_FUNC);
}
#endif

#ifndef NO_Grid_1SetRowSpan
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetRowSpan)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Grid_1SetRowSpan)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Grid_1SetRowSpan_FUNC);
	Grid::SetRowSpan((UIElement^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Grid_1SetRowSpan_FUNC);
}
#endif

#ifndef NO_HeaderedContentControl_1Header__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HeaderedContentControl_1Header__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(HeaderedContentControl_1Header__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HeaderedContentControl_1Header__I_FUNC);
	rc = (jint)TO_HANDLE(((HeaderedContentControl^)TO_OBJECT(arg0))->Header);
	OS_NATIVE_EXIT(env, that, HeaderedContentControl_1Header__I_FUNC);
	return rc;
}
#endif

#ifndef NO_HeaderedContentControl_1Header__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(HeaderedContentControl_1Header__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(HeaderedContentControl_1Header__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, HeaderedContentControl_1Header__II_FUNC);
	((HeaderedContentControl^)TO_OBJECT(arg0))->Header = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, HeaderedContentControl_1Header__II_FUNC);
}
#endif

#ifndef NO_HeaderedItemsControl_1Header__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HeaderedItemsControl_1Header__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(HeaderedItemsControl_1Header__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HeaderedItemsControl_1Header__I_FUNC);
	rc = (jint)TO_HANDLE(((HeaderedItemsControl^)TO_OBJECT(arg0))->Header);
	OS_NATIVE_EXIT(env, that, HeaderedItemsControl_1Header__I_FUNC);
	return rc;
}
#endif

#ifndef NO_HeaderedItemsControl_1Header__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(HeaderedItemsControl_1Header__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(HeaderedItemsControl_1Header__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, HeaderedItemsControl_1Header__II_FUNC);
	((HeaderedItemsControl^)TO_OBJECT(arg0))->Header = ((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, HeaderedItemsControl_1Header__II_FUNC);
}
#endif

#ifndef NO_HeaderedItemsControl_1HeaderTemplateProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HeaderedItemsControl_1HeaderTemplateProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(HeaderedItemsControl_1HeaderTemplateProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HeaderedItemsControl_1HeaderTemplateProperty_FUNC);
	rc = (jint)TO_HANDLE(HeaderedItemsControl::HeaderTemplateProperty);
	OS_NATIVE_EXIT(env, that, HeaderedItemsControl_1HeaderTemplateProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_HtmlDocument_1InvokeScript
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HtmlDocument_1InvokeScript)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(HtmlDocument_1InvokeScript)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HtmlDocument_1InvokeScript_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::HtmlDocument^)TO_OBJECT(arg0))->InvokeScript((String^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, HtmlDocument_1InvokeScript_FUNC);
	return rc;
}
#endif

#ifndef NO_HwndSource_1CompositionTarget
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HwndSource_1CompositionTarget)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(HwndSource_1CompositionTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HwndSource_1CompositionTarget_FUNC);
	rc = (jint)TO_HANDLE(((HwndSource^)TO_OBJECT(arg0))->CompositionTarget);
	OS_NATIVE_EXIT(env, that, HwndSource_1CompositionTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_HwndSource_1Handle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(HwndSource_1Handle)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(HwndSource_1Handle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HwndSource_1Handle_FUNC);
	rc = (jint)TO_HANDLE(((HwndSource^)TO_OBJECT(arg0))->Handle);
	OS_NATIVE_EXIT(env, that, HwndSource_1Handle_FUNC);
	return rc;
}
#endif

#ifndef NO_HwndTarget_1BackgroundColor
extern "C" JNIEXPORT void JNICALL OS_NATIVE(HwndTarget_1BackgroundColor)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(HwndTarget_1BackgroundColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, HwndTarget_1BackgroundColor_FUNC);
	((HwndTarget^)TO_OBJECT(arg0))->BackgroundColor = ((Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, HwndTarget_1BackgroundColor_FUNC);
}
#endif

#ifndef NO_Hyperlink_1Click
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Hyperlink_1Click)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Hyperlink_1Click)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Hyperlink_1Click_FUNC);
	((Hyperlink^)TO_OBJECT(arg0))->Click += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Hyperlink_1Click_FUNC);
}
#endif

#ifndef NO_ICollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ICollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ICollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ICollection_1Count_FUNC);
	rc = (jint)((ICollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, ICollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_IEnumerable_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IEnumerable_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IEnumerable_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IEnumerable_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerable ^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, IEnumerable_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_IEnumerator_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IEnumerator_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IEnumerator_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IEnumerator_1Current_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerator^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, IEnumerator_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_IEnumerator_1MoveNext
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(IEnumerator_1MoveNext)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(IEnumerator_1MoveNext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IEnumerator_1MoveNext_FUNC);
	rc = (jboolean)((IEnumerator^)TO_OBJECT(arg0))->MoveNext();
	OS_NATIVE_EXIT(env, that, IEnumerator_1MoveNext_FUNC);
	return rc;
}
#endif

#ifndef NO_IList_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(IList_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(IList_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, IList_1Add_FUNC);
	((IList^)TO_OBJECT(arg0))->Add((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, IList_1Add_FUNC);
}
#endif

#ifndef NO_IList_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(IList_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(IList_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, IList_1Clear_FUNC);
	((IList^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, IList_1Clear_FUNC);
}
#endif

#ifndef NO_IList_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IList_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IList_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IList_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IList^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, IList_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_IList_1IndexOf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IList_1IndexOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(IList_1IndexOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IList_1IndexOf_FUNC);
	rc = (jint)((IList^)TO_OBJECT(arg0))->IndexOf((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, IList_1IndexOf_FUNC);
	return rc;
}
#endif

#ifndef NO_IList_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(IList_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(IList_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, IList_1Insert_FUNC);
	((IList^)TO_OBJECT(arg0))->Insert(arg1, (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, IList_1Insert_FUNC);
}
#endif

#ifndef NO_IList_1Remove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(IList_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(IList_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, IList_1Remove_FUNC);
	((IList^)TO_OBJECT(arg0))->Remove((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, IList_1Remove_FUNC);
}
#endif

#ifndef NO_IList_1default
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IList_1default)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(IList_1default)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IList_1default_FUNC);
	rc = (jint)TO_HANDLE(((IList^)TO_OBJECT(arg0))->default[arg1]);
	OS_NATIVE_EXIT(env, that, IList_1default_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageSource_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ImageSource_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ImageSource_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageSource_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ImageSource::typeid);
	OS_NATIVE_EXIT(env, that, ImageSource_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1Source__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Image_1Source__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Image_1Source__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Image_1Source__I_FUNC);
	rc = (jint)TO_HANDLE(((Image^)TO_OBJECT(arg0))->Source);
	OS_NATIVE_EXIT(env, that, Image_1Source__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1Source__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Image_1Source__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Image_1Source__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Image_1Source__II_FUNC);
	((Image^)TO_OBJECT(arg0))->Source = ((ImageSource^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Image_1Source__II_FUNC);
}
#endif

#ifndef NO_Image_1SourceProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Image_1SourceProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Image_1SourceProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Image_1SourceProperty_FUNC);
	rc = (jint)TO_HANDLE(Image::SourceProperty);
	OS_NATIVE_EXIT(env, that, Image_1SourceProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1Stretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Image_1Stretch)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Image_1Stretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Image_1Stretch_FUNC);
	((Image^)TO_OBJECT(arg0))->Stretch = ((Stretch)arg1);
	OS_NATIVE_EXIT(env, that, Image_1Stretch_FUNC);
}
#endif

#ifndef NO_Image_1StretchProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Image_1StretchProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Image_1StretchProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Image_1StretchProperty_FUNC);
	rc = (jint)TO_HANDLE(Image::StretchProperty);
	OS_NATIVE_EXIT(env, that, Image_1StretchProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Image_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Image_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Image_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Image_1typeid_FUNC);
	rc = (jint)TO_HANDLE(Image::typeid);
	OS_NATIVE_EXIT(env, that, Image_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Imaging_1CreateBitmapSourceFromHIcon
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Imaging_1CreateBitmapSourceFromHIcon)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Imaging_1CreateBitmapSourceFromHIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Imaging_1CreateBitmapSourceFromHIcon_FUNC);
	rc = (jint)TO_HANDLE(System::Windows::Interop::Imaging::CreateBitmapSourceFromHIcon((IntPtr)arg0, (Int32Rect)TO_OBJECT(arg1), (BitmapSizeOptions^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, Imaging_1CreateBitmapSourceFromHIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_IndexedGlyphRunCollection_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRunCollection_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRunCollection_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IndexedGlyphRunCollection_1Current_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerator^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, IndexedGlyphRunCollection_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_IndexedGlyphRunCollection_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRunCollection_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRunCollection_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IndexedGlyphRunCollection_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerable^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, IndexedGlyphRunCollection_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_IndexedGlyphRun_1GlyphRun
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1GlyphRun)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1GlyphRun)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IndexedGlyphRun_1GlyphRun_FUNC);
	rc = (jint)TO_HANDLE(((IndexedGlyphRun^)TO_OBJECT(arg0))->GlyphRun);
	OS_NATIVE_EXIT(env, that, IndexedGlyphRun_1GlyphRun_FUNC);
	return rc;
}
#endif

#ifndef NO_IndexedGlyphRun_1TextSourceCharacterIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1TextSourceCharacterIndex)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1TextSourceCharacterIndex)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IndexedGlyphRun_1TextSourceCharacterIndex_FUNC);
	rc = (jint)((IndexedGlyphRun^)TO_OBJECT(arg0))->TextSourceCharacterIndex;
	OS_NATIVE_EXIT(env, that, IndexedGlyphRun_1TextSourceCharacterIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_IndexedGlyphRun_1TextSourceLength
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1TextSourceLength)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IndexedGlyphRun_1TextSourceLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IndexedGlyphRun_1TextSourceLength_FUNC);
	rc = (jint)((IndexedGlyphRun^)TO_OBJECT(arg0))->TextSourceLength;
	OS_NATIVE_EXIT(env, that, IndexedGlyphRun_1TextSourceLength_FUNC);
	return rc;
}
#endif

#ifndef NO_InlineCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(InlineCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(InlineCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, InlineCollection_1Add_FUNC);
	((InlineCollection^)TO_OBJECT(arg0))->Add((Inline^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, InlineCollection_1Add_FUNC);
}
#endif

#ifndef NO_InlineCollection_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(InlineCollection_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(InlineCollection_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, InlineCollection_1Clear_FUNC);
	((InlineCollection^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, InlineCollection_1Clear_FUNC);
}
#endif

#ifndef NO_InputEventArgs_1Timestamp
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(InputEventArgs_1Timestamp)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(InputEventArgs_1Timestamp)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InputEventArgs_1Timestamp_FUNC);
	rc = (jint)((InputEventArgs^)TO_OBJECT(arg0))->Timestamp;
	OS_NATIVE_EXIT(env, that, InputEventArgs_1Timestamp_FUNC);
	return rc;
}
#endif

#ifndef NO_Int32AnimationUsingKeyFrames_1KeyFrames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Int32AnimationUsingKeyFrames_1KeyFrames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Int32AnimationUsingKeyFrames_1KeyFrames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Int32AnimationUsingKeyFrames_1KeyFrames_FUNC);
	rc = (jint)TO_HANDLE(((Int32AnimationUsingKeyFrames^)TO_OBJECT(arg0))->KeyFrames);
	OS_NATIVE_EXIT(env, that, Int32AnimationUsingKeyFrames_1KeyFrames_FUNC);
	return rc;
}
#endif

#ifndef NO_Int32Animation_1From__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Int32Animation_1From__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Int32Animation_1From__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Int32Animation_1From__I_FUNC);
	rc = (jint)((Int32Animation^)TO_OBJECT(arg0))->From;
	OS_NATIVE_EXIT(env, that, Int32Animation_1From__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Int32Animation_1From__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Int32Animation_1From__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Int32Animation_1From__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Int32Animation_1From__II_FUNC);
	((Int32Animation^)TO_OBJECT(arg0))->From = (arg1);
	OS_NATIVE_EXIT(env, that, Int32Animation_1From__II_FUNC);
}
#endif

#ifndef NO_Int32Animation_1To__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Int32Animation_1To__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Int32Animation_1To__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Int32Animation_1To__I_FUNC);
	rc = (jint)((Int32Animation^)TO_OBJECT(arg0))->To;
	OS_NATIVE_EXIT(env, that, Int32Animation_1To__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Int32Animation_1To__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Int32Animation_1To__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Int32Animation_1To__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Int32Animation_1To__II_FUNC);
	((Int32Animation^)TO_OBJECT(arg0))->To = (arg1);
	OS_NATIVE_EXIT(env, that, Int32Animation_1To__II_FUNC);
}
#endif

#ifndef NO_Int32KeyFrame_1KeyTime
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Int32KeyFrame_1KeyTime)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Int32KeyFrame_1KeyTime)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Int32KeyFrame_1KeyTime_FUNC);
	((Int32KeyFrame^)TO_OBJECT(arg0))->KeyTime = ((KeyTime)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Int32KeyFrame_1KeyTime_FUNC);
}
#endif

#ifndef NO_Int32KeyFrame_1Value
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Int32KeyFrame_1Value)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Int32KeyFrame_1Value)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Int32KeyFrame_1Value_FUNC);
	((Int32KeyFrame^)TO_OBJECT(arg0))->Value = (arg1);
	OS_NATIVE_EXIT(env, that, Int32KeyFrame_1Value_FUNC);
}
#endif

#ifndef NO_Int32Rect_1Empty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Int32Rect_1Empty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Int32Rect_1Empty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Int32Rect_1Empty_FUNC);
	rc = (jint)TO_HANDLE(Int32Rect::Empty);
	OS_NATIVE_EXIT(env, that, Int32Rect_1Empty_FUNC);
	return rc;
}
#endif

#ifndef NO_IntPtr_1ToInt32
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(IntPtr_1ToInt32)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(IntPtr_1ToInt32)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IntPtr_1ToInt32_FUNC);
	rc = (jint)((IntPtr^)TO_OBJECT(arg0))->ToInt32();
	OS_NATIVE_EXIT(env, that, IntPtr_1ToInt32_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ItemCollection_1Add_FUNC);
	((ItemCollection^)TO_OBJECT(arg0))->Add((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ItemCollection_1Add_FUNC);
}
#endif

#ifndef NO_ItemCollection_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ItemCollection_1Clear_FUNC);
	((ItemCollection^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, ItemCollection_1Clear_FUNC);
}
#endif

#ifndef NO_ItemCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemCollection_1Count_FUNC);
	rc = (jint)((ItemCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, ItemCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1CurrentItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1CurrentItem)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1CurrentItem)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemCollection_1CurrentItem_FUNC);
	rc = (jint)TO_HANDLE(((ItemCollection^)TO_OBJECT(arg0))->CurrentItem);
	OS_NATIVE_EXIT(env, that, ItemCollection_1CurrentItem_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1CurrentPosition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1CurrentPosition)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1CurrentPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemCollection_1CurrentPosition_FUNC);
	rc = (jint)((ItemCollection^)TO_OBJECT(arg0))->CurrentPosition;
	OS_NATIVE_EXIT(env, that, ItemCollection_1CurrentPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1GetItemAt
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1GetItemAt)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1GetItemAt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemCollection_1GetItemAt_FUNC);
	rc = (jint)TO_HANDLE(((ItemCollection^)TO_OBJECT(arg0))->GetItemAt(arg1));
	OS_NATIVE_EXIT(env, that, ItemCollection_1GetItemAt_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1IndexOf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1IndexOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(ItemCollection_1IndexOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemCollection_1IndexOf_FUNC);
	rc = (jint)((ItemCollection^)TO_OBJECT(arg0))->IndexOf((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ItemCollection_1IndexOf_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemCollection_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, ItemCollection_1Insert_FUNC);
	((ItemCollection^)TO_OBJECT(arg0))->Insert(arg1, (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, ItemCollection_1Insert_FUNC);
}
#endif

#ifndef NO_ItemCollection_1Remove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ItemCollection_1Remove_FUNC);
	((ItemCollection^)TO_OBJECT(arg0))->Remove((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ItemCollection_1Remove_FUNC);
}
#endif

#ifndef NO_ItemCollection_1RemoveAt
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1RemoveAt)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemCollection_1RemoveAt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ItemCollection_1RemoveAt_FUNC);
	((ItemCollection^)TO_OBJECT(arg0))->RemoveAt(arg1);
	OS_NATIVE_EXIT(env, that, ItemCollection_1RemoveAt_FUNC);
}
#endif

#ifndef NO_ItemsControl_1HasItems
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ItemsControl_1HasItems)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ItemsControl_1HasItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ItemsControl_1HasItems_FUNC);
	rc = (jboolean)((ItemsControl^)TO_OBJECT(arg0))->HasItems;
	OS_NATIVE_EXIT(env, that, ItemsControl_1HasItems_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemsControl_1IsTextSearchEnabled
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1IsTextSearchEnabled)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1IsTextSearchEnabled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ItemsControl_1IsTextSearchEnabled_FUNC);
	((ItemsControl^)TO_OBJECT(arg0))->IsTextSearchEnabled = (arg1);
	OS_NATIVE_EXIT(env, that, ItemsControl_1IsTextSearchEnabled_FUNC);
}
#endif

#ifndef NO_ItemsControl_1ItemTemplate__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemsControl_1ItemTemplate__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ItemsControl_1ItemTemplate__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemsControl_1ItemTemplate__I_FUNC);
	rc = (jint)TO_HANDLE(((ItemsControl^)TO_OBJECT(arg0))->ItemTemplate);
	OS_NATIVE_EXIT(env, that, ItemsControl_1ItemTemplate__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemsControl_1ItemTemplate__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1ItemTemplate__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1ItemTemplate__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ItemsControl_1ItemTemplate__II_FUNC);
	((ItemsControl^)TO_OBJECT(arg0))->ItemTemplate = ((DataTemplate^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ItemsControl_1ItemTemplate__II_FUNC);
}
#endif

#ifndef NO_ItemsControl_1Items
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemsControl_1Items)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ItemsControl_1Items)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemsControl_1Items_FUNC);
	rc = (jint)TO_HANDLE(((ItemsControl^)TO_OBJECT(arg0))->Items);
	OS_NATIVE_EXIT(env, that, ItemsControl_1Items_FUNC);
	return rc;
}
#endif

#ifndef NO_ItemsControl_1ItemsSource
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1ItemsSource)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ItemsControl_1ItemsSource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ItemsControl_1ItemsSource_FUNC);
	((ItemsControl^)TO_OBJECT(arg0))->ItemsSource = ((IEnumerable^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ItemsControl_1ItemsSource_FUNC);
}
#endif

#ifndef NO_ItemsPresenter_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ItemsPresenter_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ItemsPresenter_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ItemsPresenter_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ItemsPresenter::typeid);
	OS_NATIVE_EXIT(env, that, ItemsPresenter_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyEventArgs_1IsDown
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsDown)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsDown)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KeyEventArgs_1IsDown_FUNC);
	rc = (jboolean)((KeyEventArgs^)TO_OBJECT(arg0))->IsDown;
	OS_NATIVE_EXIT(env, that, KeyEventArgs_1IsDown_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyEventArgs_1IsRepeat
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsRepeat)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsRepeat)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KeyEventArgs_1IsRepeat_FUNC);
	rc = (jboolean)((KeyEventArgs^)TO_OBJECT(arg0))->IsRepeat;
	OS_NATIVE_EXIT(env, that, KeyEventArgs_1IsRepeat_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyEventArgs_1IsToggled
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsToggled)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(KeyEventArgs_1IsToggled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KeyEventArgs_1IsToggled_FUNC);
	rc = (jboolean)((KeyEventArgs^)TO_OBJECT(arg0))->IsToggled;
	OS_NATIVE_EXIT(env, that, KeyEventArgs_1IsToggled_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyEventArgs_1Key
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyEventArgs_1Key)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyEventArgs_1Key)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyEventArgs_1Key_FUNC);
	rc = (jint)((KeyEventArgs^)TO_OBJECT(arg0))->Key;
	OS_NATIVE_EXIT(env, that, KeyEventArgs_1Key_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyEventArgs_1SystemKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyEventArgs_1SystemKey)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyEventArgs_1SystemKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyEventArgs_1SystemKey_FUNC);
	rc = (jint)((KeyEventArgs^)TO_OBJECT(arg0))->SystemKey;
	OS_NATIVE_EXIT(env, that, KeyEventArgs_1SystemKey_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyInterop_1VirtualKeyFromKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyInterop_1VirtualKeyFromKey)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyInterop_1VirtualKeyFromKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyInterop_1VirtualKeyFromKey_FUNC);
	rc = (jint)KeyInterop::VirtualKeyFromKey((Key)arg0);
	OS_NATIVE_EXIT(env, that, KeyInterop_1VirtualKeyFromKey_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyTime_1FromTimeSpan
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyTime_1FromTimeSpan)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyTime_1FromTimeSpan)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyTime_1FromTimeSpan_FUNC);
	rc = (jint)TO_HANDLE(KeyTime::FromTimeSpan((TimeSpan)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, KeyTime_1FromTimeSpan_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyTime_1Uniform
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyTime_1Uniform)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(KeyTime_1Uniform)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyTime_1Uniform_FUNC);
	rc = (jint)TO_HANDLE(KeyTime::Uniform);
	OS_NATIVE_EXIT(env, that, KeyTime_1Uniform_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyboardDevice_1Modifiers
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyboardDevice_1Modifiers)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyboardDevice_1Modifiers)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyboardDevice_1Modifiers_FUNC);
	rc = (jint)((KeyboardDevice^)TO_OBJECT(arg0))->Modifiers;
	OS_NATIVE_EXIT(env, that, KeyboardDevice_1Modifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyboardEventArgs_1KeyboardDevice
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(KeyboardEventArgs_1KeyboardDevice)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(KeyboardEventArgs_1KeyboardDevice)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyboardEventArgs_1KeyboardDevice_FUNC);
	rc = (jint)TO_HANDLE(((KeyboardEventArgs^)TO_OBJECT(arg0))->KeyboardDevice);
	OS_NATIVE_EXIT(env, that, KeyboardEventArgs_1KeyboardDevice_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyboardNavigation_1GetIsTabStop
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(KeyboardNavigation_1GetIsTabStop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(KeyboardNavigation_1GetIsTabStop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KeyboardNavigation_1GetIsTabStop_FUNC);
	rc = (jboolean)KeyboardNavigation::GetIsTabStop((DependencyObject^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, KeyboardNavigation_1GetIsTabStop_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyboardNavigation_1SetControlTabNavigation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetControlTabNavigation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetControlTabNavigation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, KeyboardNavigation_1SetControlTabNavigation_FUNC);
	KeyboardNavigation::SetControlTabNavigation((DependencyObject^)TO_OBJECT(arg0), (KeyboardNavigationMode)arg1);
	OS_NATIVE_EXIT(env, that, KeyboardNavigation_1SetControlTabNavigation_FUNC);
}
#endif

#ifndef NO_KeyboardNavigation_1SetDirectionalNavigation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetDirectionalNavigation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetDirectionalNavigation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, KeyboardNavigation_1SetDirectionalNavigation_FUNC);
	KeyboardNavigation::SetDirectionalNavigation((DependencyObject^)TO_OBJECT(arg0), (KeyboardNavigationMode)arg1);
	OS_NATIVE_EXIT(env, that, KeyboardNavigation_1SetDirectionalNavigation_FUNC);
}
#endif

#ifndef NO_KeyboardNavigation_1SetIsTabStop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetIsTabStop)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetIsTabStop)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, KeyboardNavigation_1SetIsTabStop_FUNC);
	KeyboardNavigation::SetIsTabStop((DependencyObject^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, KeyboardNavigation_1SetIsTabStop_FUNC);
}
#endif

#ifndef NO_KeyboardNavigation_1SetTabNavigation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetTabNavigation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(KeyboardNavigation_1SetTabNavigation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, KeyboardNavigation_1SetTabNavigation_FUNC);
	KeyboardNavigation::SetTabNavigation((DependencyObject^)TO_OBJECT(arg0), (KeyboardNavigationMode)arg1);
	OS_NATIVE_EXIT(env, that, KeyboardNavigation_1SetTabNavigation_FUNC);
}
#endif

#ifndef NO_Keyboard_1Focus
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1Focus)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1Focus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Keyboard_1Focus_FUNC);
	rc = (jint)TO_HANDLE(Keyboard::Focus((IInputElement^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Keyboard_1Focus_FUNC);
	return rc;
}
#endif

#ifndef NO_Keyboard_1FocusedElement
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1FocusedElement)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1FocusedElement)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Keyboard_1FocusedElement_FUNC);
	rc = (jint)TO_HANDLE(Keyboard::FocusedElement);
	OS_NATIVE_EXIT(env, that, Keyboard_1FocusedElement_FUNC);
	return rc;
}
#endif

#ifndef NO_Keyboard_1Modifiers
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1Modifiers)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Keyboard_1Modifiers)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Keyboard_1Modifiers_FUNC);
	rc = (jint)Keyboard::Modifiers;
	OS_NATIVE_EXIT(env, that, Keyboard_1Modifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_ListBoxItem_1IsSelected__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ListBoxItem_1IsSelected__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ListBoxItem_1IsSelected__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ListBoxItem_1IsSelected__I_FUNC);
	rc = (jboolean)((ListBoxItem^)TO_OBJECT(arg0))->IsSelected;
	OS_NATIVE_EXIT(env, that, ListBoxItem_1IsSelected__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ListBoxItem_1IsSelected__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListBoxItem_1IsSelected__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ListBoxItem_1IsSelected__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ListBoxItem_1IsSelected__IZ_FUNC);
	((ListBoxItem^)TO_OBJECT(arg0))->IsSelected = (arg1);
	OS_NATIVE_EXIT(env, that, ListBoxItem_1IsSelected__IZ_FUNC);
}
#endif

#ifndef NO_ListBox_1ScrollIntoView
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListBox_1ScrollIntoView)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ListBox_1ScrollIntoView)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ListBox_1ScrollIntoView_FUNC);
	((ListBox^)TO_OBJECT(arg0))->ScrollIntoView((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ListBox_1ScrollIntoView_FUNC);
}
#endif

#ifndef NO_ListBox_1SelectAll
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListBox_1SelectAll)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ListBox_1SelectAll)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ListBox_1SelectAll_FUNC);
	((ListBox^)TO_OBJECT(arg0))->SelectAll();
	OS_NATIVE_EXIT(env, that, ListBox_1SelectAll_FUNC);
}
#endif

#ifndef NO_ListBox_1SelectedItems
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ListBox_1SelectedItems)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ListBox_1SelectedItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ListBox_1SelectedItems_FUNC);
	rc = (jint)TO_HANDLE(((ListBox^)TO_OBJECT(arg0))->SelectedItems);
	OS_NATIVE_EXIT(env, that, ListBox_1SelectedItems_FUNC);
	return rc;
}
#endif

#ifndef NO_ListBox_1SelectionMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListBox_1SelectionMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ListBox_1SelectionMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ListBox_1SelectionMode_FUNC);
	((ListBox^)TO_OBJECT(arg0))->SelectionMode = ((SelectionMode)arg1);
	OS_NATIVE_EXIT(env, that, ListBox_1SelectionMode_FUNC);
}
#endif

#ifndef NO_ListBox_1UnselectAll
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListBox_1UnselectAll)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ListBox_1UnselectAll)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ListBox_1UnselectAll_FUNC);
	((ListBox^)TO_OBJECT(arg0))->UnselectAll();
	OS_NATIVE_EXIT(env, that, ListBox_1UnselectAll_FUNC);
}
#endif

#ifndef NO_ListViewItem_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ListViewItem_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ListViewItem_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ListViewItem_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ListViewItem::typeid);
	OS_NATIVE_EXIT(env, that, ListViewItem_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_ListView_1View
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ListView_1View)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ListView_1View)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ListView_1View_FUNC);
	((ListView^)TO_OBJECT(arg0))->View = ((ViewBase^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ListView_1View_FUNC);
}
#endif

#ifndef NO_MatrixTransform_1Matrix__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MatrixTransform_1Matrix__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MatrixTransform_1Matrix__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MatrixTransform_1Matrix__I_FUNC);
	rc = (jint)TO_HANDLE(((MatrixTransform^)TO_OBJECT(arg0))->Matrix);
	OS_NATIVE_EXIT(env, that, MatrixTransform_1Matrix__I_FUNC);
	return rc;
}
#endif

#ifndef NO_MatrixTransform_1Matrix__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MatrixTransform_1Matrix__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MatrixTransform_1Matrix__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MatrixTransform_1Matrix__II_FUNC);
	((MatrixTransform^)TO_OBJECT(arg0))->Matrix = ((Matrix)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MatrixTransform_1Matrix__II_FUNC);
}
#endif

#ifndef NO_Matrix_1Invert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1Invert)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1Invert)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Matrix_1Invert_FUNC);
	((Matrix^)TO_OBJECT(arg0))->Invert();
	OS_NATIVE_EXIT(env, that, Matrix_1Invert_FUNC);
}
#endif

#ifndef NO_Matrix_1IsIdentity
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Matrix_1IsIdentity)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Matrix_1IsIdentity)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1IsIdentity_FUNC);
	rc = (jboolean)((Matrix^)TO_OBJECT(arg0))->IsIdentity;
	OS_NATIVE_EXIT(env, that, Matrix_1IsIdentity_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1M11__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M11__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M11__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1M11__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->M11;
	OS_NATIVE_EXIT(env, that, Matrix_1M11__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1M11__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M11__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M11__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1M11__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->M11 = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1M11__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1M12__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M12__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M12__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1M12__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->M12;
	OS_NATIVE_EXIT(env, that, Matrix_1M12__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1M12__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M12__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M12__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1M12__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->M12 = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1M12__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1M21__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M21__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M21__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1M21__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->M21;
	OS_NATIVE_EXIT(env, that, Matrix_1M21__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1M21__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M21__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M21__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1M21__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->M21 = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1M21__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1M22__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M22__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1M22__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1M22__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->M22;
	OS_NATIVE_EXIT(env, that, Matrix_1M22__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1M22__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M22__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1M22__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1M22__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->M22 = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1M22__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1Multiply
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Matrix_1Multiply)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Matrix_1Multiply)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1Multiply_FUNC);
	rc = (jint)TO_HANDLE(Matrix::Multiply((Matrix)TO_OBJECT(arg0), (Matrix)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, Matrix_1Multiply_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1OffsetX__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1OffsetX__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1OffsetX__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1OffsetX__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->OffsetX;
	OS_NATIVE_EXIT(env, that, Matrix_1OffsetX__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1OffsetX__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1OffsetX__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1OffsetX__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1OffsetX__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->OffsetX = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1OffsetX__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1OffsetY__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1OffsetY__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Matrix_1OffsetY__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1OffsetY__I_FUNC);
	rc = (jdouble)((Matrix^)TO_OBJECT(arg0))->OffsetY;
	OS_NATIVE_EXIT(env, that, Matrix_1OffsetY__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1OffsetY__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1OffsetY__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1OffsetY__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1OffsetY__ID_FUNC);
	((Matrix^)TO_OBJECT(arg0))->OffsetY = (arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1OffsetY__ID_FUNC);
}
#endif

#ifndef NO_Matrix_1RotatePrepend
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1RotatePrepend)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1RotatePrepend)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Matrix_1RotatePrepend_FUNC);
	((Matrix^)TO_OBJECT(arg0))->RotatePrepend(arg1);
	OS_NATIVE_EXIT(env, that, Matrix_1RotatePrepend_FUNC);
}
#endif

#ifndef NO_Matrix_1ScalePrepend
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1ScalePrepend)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1ScalePrepend)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, Matrix_1ScalePrepend_FUNC);
	((Matrix^)TO_OBJECT(arg0))->ScalePrepend(arg1, arg2);
	OS_NATIVE_EXIT(env, that, Matrix_1ScalePrepend_FUNC);
}
#endif

#ifndef NO_Matrix_1SetIdentity
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1SetIdentity)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1SetIdentity)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Matrix_1SetIdentity_FUNC);
	((Matrix^)TO_OBJECT(arg0))->SetIdentity();
	OS_NATIVE_EXIT(env, that, Matrix_1SetIdentity_FUNC);
}
#endif

#ifndef NO_Matrix_1SkewPrepend
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1SkewPrepend)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1SkewPrepend)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, Matrix_1SkewPrepend_FUNC);
	((Matrix^)TO_OBJECT(arg0))->SkewPrepend(arg1, arg2);
	OS_NATIVE_EXIT(env, that, Matrix_1SkewPrepend_FUNC);
}
#endif

#ifndef NO_Matrix_1Transform
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Matrix_1Transform)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Matrix_1Transform)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Matrix_1Transform_FUNC);
	rc = (jint)TO_HANDLE(((Matrix^)TO_OBJECT(arg0))->Transform((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, Matrix_1Transform_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1TranslatePrepend
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Matrix_1TranslatePrepend)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2);
JNIEXPORT void JNICALL OS_NATIVE(Matrix_1TranslatePrepend)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, Matrix_1TranslatePrepend_FUNC);
	((Matrix^)TO_OBJECT(arg0))->TranslatePrepend(arg1, arg2);
	OS_NATIVE_EXIT(env, that, Matrix_1TranslatePrepend_FUNC);
}
#endif

#ifndef NO_MemberDescriptor_1Name
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MemberDescriptor_1Name)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MemberDescriptor_1Name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MemberDescriptor_1Name_FUNC);
	rc = (jint)TO_HANDLE(((MemberDescriptor^)TO_OBJECT(arg0))->Name);
	OS_NATIVE_EXIT(env, that, MemberDescriptor_1Name_FUNC);
	return rc;
}
#endif

#ifndef NO_MemoryStream_1ToArray
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MemoryStream_1ToArray)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MemoryStream_1ToArray)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MemoryStream_1ToArray_FUNC);
	rc = (jint)TO_HANDLE(((System::IO::MemoryStream^)TO_OBJECT(arg0))->ToArray());
	OS_NATIVE_EXIT(env, that, MemoryStream_1ToArray_FUNC);
	return rc;
}
#endif

#ifndef NO_MemoryStream_1Write
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MemoryStream_1Write)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(MemoryStream_1Write)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, MemoryStream_1Write_FUNC);
	((System::IO::MemoryStream^)TO_OBJECT(arg0))->Write((array<Byte>^)TO_OBJECT(arg1), arg2, arg3);
	OS_NATIVE_EXIT(env, that, MemoryStream_1Write_FUNC);
}
#endif

#ifndef NO_MenuItem_1Click
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1Click)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1Click)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1Click_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->Click += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MenuItem_1Click_FUNC);
}
#endif

#ifndef NO_MenuItem_1Icon
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1Icon)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1Icon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1Icon_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->Icon = ((Image^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MenuItem_1Icon_FUNC);
}
#endif

#ifndef NO_MenuItem_1InputGestureText
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1InputGestureText)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1InputGestureText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1InputGestureText_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->InputGestureText = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MenuItem_1InputGestureText_FUNC);
}
#endif

#ifndef NO_MenuItem_1IsCheckable
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1IsCheckable)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1IsCheckable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1IsCheckable_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->IsCheckable = (arg1);
	OS_NATIVE_EXIT(env, that, MenuItem_1IsCheckable_FUNC);
}
#endif

#ifndef NO_MenuItem_1IsChecked__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(MenuItem_1IsChecked__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(MenuItem_1IsChecked__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, MenuItem_1IsChecked__I_FUNC);
	rc = (jboolean)((MenuItem^)TO_OBJECT(arg0))->IsChecked;
	OS_NATIVE_EXIT(env, that, MenuItem_1IsChecked__I_FUNC);
	return rc;
}
#endif

#ifndef NO_MenuItem_1IsChecked__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1IsChecked__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1IsChecked__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1IsChecked__IZ_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->IsChecked = (arg1);
	OS_NATIVE_EXIT(env, that, MenuItem_1IsChecked__IZ_FUNC);
}
#endif

#ifndef NO_MenuItem_1SubmenuClosed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1SubmenuClosed)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1SubmenuClosed)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1SubmenuClosed_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->SubmenuClosed += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MenuItem_1SubmenuClosed_FUNC);
}
#endif

#ifndef NO_MenuItem_1SubmenuOpened
extern "C" JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1SubmenuOpened)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(MenuItem_1SubmenuOpened)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, MenuItem_1SubmenuOpened_FUNC);
	((MenuItem^)TO_OBJECT(arg0))->SubmenuOpened += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, MenuItem_1SubmenuOpened_FUNC);
}
#endif

#ifndef NO_Menu_1IsMainMenu
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Menu_1IsMainMenu)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Menu_1IsMainMenu)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Menu_1IsMainMenu_FUNC);
	((Menu^)TO_OBJECT(arg0))->IsMainMenu = (arg1);
	OS_NATIVE_EXIT(env, that, Menu_1IsMainMenu_FUNC);
}
#endif

#ifndef NO_MessageBox_1Show
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MessageBox_1Show)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4);
JNIEXPORT jint JNICALL OS_NATIVE(MessageBox_1Show)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MessageBox_1Show_FUNC);
	rc = (jint)MessageBox::Show((String^)TO_OBJECT(arg0), (String^)TO_OBJECT(arg1), (MessageBoxButton)arg2, (MessageBoxImage)arg3, (MessageBoxResult)arg4);
	OS_NATIVE_EXIT(env, that, MessageBox_1Show_FUNC);
	return rc;
}
#endif

#ifndef NO_MethodInfo_1Invoke
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MethodInfo_1Invoke)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(MethodInfo_1Invoke)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MethodInfo_1Invoke_FUNC);
	rc = (jint)TO_HANDLE(((MethodInfo^)TO_OBJECT(arg0))->Invoke((Object^)TO_OBJECT(arg1), (array<Object^>^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, MethodInfo_1Invoke_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseButtonEventArgs_1ButtonState
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ButtonState)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ButtonState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseButtonEventArgs_1ButtonState_FUNC);
	rc = (jint)((MouseButtonEventArgs^)TO_OBJECT(arg0))->ButtonState;
	OS_NATIVE_EXIT(env, that, MouseButtonEventArgs_1ButtonState_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseButtonEventArgs_1ChangedButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ChangedButton)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ChangedButton)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseButtonEventArgs_1ChangedButton_FUNC);
	rc = (jint)((MouseButtonEventArgs^)TO_OBJECT(arg0))->ChangedButton;
	OS_NATIVE_EXIT(env, that, MouseButtonEventArgs_1ChangedButton_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseButtonEventArgs_1ClickCount
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ClickCount)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseButtonEventArgs_1ClickCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseButtonEventArgs_1ClickCount_FUNC);
	rc = (jint)((MouseButtonEventArgs^)TO_OBJECT(arg0))->ClickCount;
	OS_NATIVE_EXIT(env, that, MouseButtonEventArgs_1ClickCount_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1GetPosition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1GetPosition)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1GetPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1GetPosition_FUNC);
	rc = (jint)TO_HANDLE(((MouseEventArgs^)TO_OBJECT(arg0))->GetPosition((IInputElement^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1GetPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1LeftButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1LeftButton)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1LeftButton)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1LeftButton_FUNC);
	rc = (jint)((MouseEventArgs^)TO_OBJECT(arg0))->LeftButton;
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1LeftButton_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1MiddleButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1MiddleButton)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1MiddleButton)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1MiddleButton_FUNC);
	rc = (jint)((MouseEventArgs^)TO_OBJECT(arg0))->MiddleButton;
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1MiddleButton_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1RightButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1RightButton)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1RightButton)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1RightButton_FUNC);
	rc = (jint)((MouseEventArgs^)TO_OBJECT(arg0))->RightButton;
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1RightButton_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1XButton1
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1XButton1)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1XButton1)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1XButton1_FUNC);
	rc = (jint)((MouseEventArgs^)TO_OBJECT(arg0))->XButton1;
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1XButton1_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseEventArgs_1XButton2
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1XButton2)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseEventArgs_1XButton2)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseEventArgs_1XButton2_FUNC);
	rc = (jint)((MouseEventArgs^)TO_OBJECT(arg0))->XButton2;
	OS_NATIVE_EXIT(env, that, MouseEventArgs_1XButton2_FUNC);
	return rc;
}
#endif

#ifndef NO_MouseWheelEventArgs_1Delta
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(MouseWheelEventArgs_1Delta)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(MouseWheelEventArgs_1Delta)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MouseWheelEventArgs_1Delta_FUNC);
	rc = (jint)((MouseWheelEventArgs^)TO_OBJECT(arg0))->Delta;
	OS_NATIVE_EXIT(env, that, MouseWheelEventArgs_1Delta_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1Captured
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1Captured)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1Captured)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1Captured_FUNC);
	rc = (jint)TO_HANDLE(Mouse::Captured);
	OS_NATIVE_EXIT(env, that, Mouse_1Captured_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1DirectlyOver
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1DirectlyOver)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1DirectlyOver)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1DirectlyOver_FUNC);
	rc = (jint)TO_HANDLE(Mouse::DirectlyOver);
	OS_NATIVE_EXIT(env, that, Mouse_1DirectlyOver_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1GetPosition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1GetPosition)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1GetPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1GetPosition_FUNC);
	rc = (jint)TO_HANDLE(Mouse::GetPosition((IInputElement^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Mouse_1GetPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1LeftButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1LeftButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1LeftButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1LeftButton_FUNC);
	rc = (jint)Mouse::LeftButton;
	OS_NATIVE_EXIT(env, that, Mouse_1LeftButton_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1MiddleButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1MiddleButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1MiddleButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1MiddleButton_FUNC);
	rc = (jint)Mouse::MiddleButton;
	OS_NATIVE_EXIT(env, that, Mouse_1MiddleButton_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1RightButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1RightButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1RightButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1RightButton_FUNC);
	rc = (jint)Mouse::RightButton;
	OS_NATIVE_EXIT(env, that, Mouse_1RightButton_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1SetCursor
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Mouse_1SetCursor)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Mouse_1SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1SetCursor_FUNC);
	rc = (jboolean)Mouse::SetCursor((Cursor^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Mouse_1SetCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1XButton1
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1XButton1)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1XButton1)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1XButton1_FUNC);
	rc = (jint)Mouse::XButton1;
	OS_NATIVE_EXIT(env, that, Mouse_1XButton1_FUNC);
	return rc;
}
#endif

#ifndef NO_Mouse_1XButton2
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1XButton2)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Mouse_1XButton2)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Mouse_1XButton2_FUNC);
	rc = (jint)Mouse::XButton2;
	OS_NATIVE_EXIT(env, that, Mouse_1XButton2_FUNC);
	return rc;
}
#endif

#ifndef NO_NameScope_1GetNameScope
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(NameScope_1GetNameScope)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(NameScope_1GetNameScope)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NameScope_1GetNameScope_FUNC);
	rc = (jint)TO_HANDLE(NameScope::GetNameScope((DependencyObject^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, NameScope_1GetNameScope_FUNC);
	return rc;
}
#endif

#ifndef NO_NameScope_1RegisterName
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NameScope_1RegisterName)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(NameScope_1RegisterName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, NameScope_1RegisterName_FUNC);
	((NameScope^)TO_OBJECT(arg0))->RegisterName((String^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, NameScope_1RegisterName_FUNC);
}
#endif

#ifndef NO_NameScope_1SetNameScope
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NameScope_1SetNameScope)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NameScope_1SetNameScope)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NameScope_1SetNameScope_FUNC);
	NameScope::SetNameScope((DependencyObject^)TO_OBJECT(arg0), (INameScope^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NameScope_1SetNameScope_FUNC);
}
#endif

#ifndef NO_NewGlobalRef
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(NewGlobalRef)(JNIEnv *env, jclass that, jobject arg0);
JNIEXPORT jint JNICALL OS_NATIVE(NewGlobalRef)
	(JNIEnv *env, jclass that, jobject arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewGlobalRef_FUNC);
	rc = (jint)env->NewGlobalRef(arg0);
	OS_NATIVE_EXIT(env, that, NewGlobalRef_FUNC);
	return rc;
}
#endif

#ifndef NO_NotifyIcon_1DoubleClick
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1DoubleClick)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1DoubleClick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1DoubleClick_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->DoubleClick += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NotifyIcon_1DoubleClick_FUNC);
}
#endif

#ifndef NO_NotifyIcon_1Icon
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Icon)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Icon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1Icon_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->Icon = ((System::Drawing::Icon^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NotifyIcon_1Icon_FUNC);
}
#endif

#ifndef NO_NotifyIcon_1MouseDown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1MouseDown)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1MouseDown)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1MouseDown_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->MouseDown += ((System::Windows::Forms::MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NotifyIcon_1MouseDown_FUNC);
}
#endif

#ifndef NO_NotifyIcon_1MouseUp
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1MouseUp)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1MouseUp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1MouseUp_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->MouseUp += ((System::Windows::Forms::MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NotifyIcon_1MouseUp_FUNC);
}
#endif

#ifndef NO_NotifyIcon_1Text
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Text)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Text)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1Text_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->Text = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, NotifyIcon_1Text_FUNC);
}
#endif

#ifndef NO_NotifyIcon_1Visible
extern "C" JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Visible)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(NotifyIcon_1Visible)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, NotifyIcon_1Visible_FUNC);
	((System::Windows::Forms::NotifyIcon^)TO_OBJECT(arg0))->Visible = (arg1);
	OS_NATIVE_EXIT(env, that, NotifyIcon_1Visible_FUNC);
}
#endif

#ifndef NO_Object_1Equals
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Object_1Equals)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Object_1Equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Object_1Equals_FUNC);
	rc = (jboolean)((Object ^)TO_OBJECT(arg0))->Equals((Object ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Object_1Equals_FUNC);
	return rc;
}
#endif

#ifndef NO_Object_1GetType
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Object_1GetType)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Object_1GetType)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Object_1GetType_FUNC);
	rc = (jint)TO_HANDLE(((Object ^)TO_OBJECT(arg0))->GetType());
	OS_NATIVE_EXIT(env, that, Object_1GetType_FUNC);
	return rc;
}
#endif

#ifndef NO_Object_1ToString
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Object_1ToString)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Object_1ToString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Object_1ToString_FUNC);
	rc = (jint)TO_HANDLE(((Object ^)TO_OBJECT(arg0))->ToString());
	OS_NATIVE_EXIT(env, that, Object_1ToString_FUNC);
	return rc;
}
#endif

#ifndef NO_ObservableCollectionGridViewColumn_1Move
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ObservableCollectionGridViewColumn_1Move)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(ObservableCollectionGridViewColumn_1Move)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, ObservableCollectionGridViewColumn_1Move_FUNC);
	((ObservableCollection<GridViewColumn^>^)TO_OBJECT(arg0))->Move(arg1, arg2);
	OS_NATIVE_EXIT(env, that, ObservableCollectionGridViewColumn_1Move_FUNC);
}
#endif

#ifndef NO_OpenFileDialog_1Multiselect
extern "C" JNIEXPORT void JNICALL OS_NATIVE(OpenFileDialog_1Multiselect)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(OpenFileDialog_1Multiselect)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, OpenFileDialog_1Multiselect_FUNC);
	((OpenFileDialog^)TO_OBJECT(arg0))->Multiselect = (arg1);
	OS_NATIVE_EXIT(env, that, OpenFileDialog_1Multiselect_FUNC);
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1GlowColor__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowColor__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowColor__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1GlowColor__I_FUNC);
	rc = (jint)TO_HANDLE(((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->GlowColor);
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1GlowColor__I_FUNC);
	return rc;
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1GlowColor__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowColor__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowColor__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1GlowColor__II_FUNC);
	((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->GlowColor = ((Color)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1GlowColor__II_FUNC);
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1GlowSize__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSize__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSize__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1GlowSize__I_FUNC);
	rc = (jdouble)((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->GlowSize;
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1GlowSize__I_FUNC);
	return rc;
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1GlowSize__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSize__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSize__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1GlowSize__ID_FUNC);
	((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->GlowSize = (arg1);
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1GlowSize__ID_FUNC);
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1GlowSizeProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSizeProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(OuterGlowBitmapEffect_1GlowSizeProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1GlowSizeProperty_FUNC);
	rc = (jint)TO_HANDLE(OuterGlowBitmapEffect::GlowSizeProperty);
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1GlowSizeProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1Opacity__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(OuterGlowBitmapEffect_1Opacity__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(OuterGlowBitmapEffect_1Opacity__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1Opacity__I_FUNC);
	rc = (jdouble)((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->Opacity;
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1Opacity__I_FUNC);
	return rc;
}
#endif

#ifndef NO_OuterGlowBitmapEffect_1Opacity__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1Opacity__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(OuterGlowBitmapEffect_1Opacity__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, OuterGlowBitmapEffect_1Opacity__ID_FUNC);
	((OuterGlowBitmapEffect^)TO_OBJECT(arg0))->Opacity = (arg1);
	OS_NATIVE_EXIT(env, that, OuterGlowBitmapEffect_1Opacity__ID_FUNC);
}
#endif

#ifndef NO_Panel_1Background__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1Background__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1Background__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1Background__I_FUNC);
	rc = (jint)TO_HANDLE(((Panel^)TO_OBJECT(arg0))->Background);
	OS_NATIVE_EXIT(env, that, Panel_1Background__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Panel_1Background__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Panel_1Background__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Panel_1Background__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Panel_1Background__II_FUNC);
	((Panel^)TO_OBJECT(arg0))->Background = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Panel_1Background__II_FUNC);
}
#endif

#ifndef NO_Panel_1BackgroundProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1BackgroundProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1BackgroundProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1BackgroundProperty_FUNC);
	rc = (jint)TO_HANDLE(Panel::BackgroundProperty);
	OS_NATIVE_EXIT(env, that, Panel_1BackgroundProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Panel_1Children
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1Children)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1Children)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1Children_FUNC);
	rc = (jint)TO_HANDLE(((Panel^)TO_OBJECT(arg0))->Children);
	OS_NATIVE_EXIT(env, that, Panel_1Children_FUNC);
	return rc;
}
#endif

#ifndef NO_Panel_1GetZIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1GetZIndex)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1GetZIndex)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1GetZIndex_FUNC);
	rc = (jint)Panel::GetZIndex((UIElement^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, Panel_1GetZIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_Panel_1HeightProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1HeightProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1HeightProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1HeightProperty_FUNC);
	rc = (jint)TO_HANDLE(Panel::HeightProperty);
	OS_NATIVE_EXIT(env, that, Panel_1HeightProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Panel_1SetZIndex
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Panel_1SetZIndex)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Panel_1SetZIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Panel_1SetZIndex_FUNC);
	Panel::SetZIndex((UIElement ^)TO_OBJECT(arg0), arg1);
	OS_NATIVE_EXIT(env, that, Panel_1SetZIndex_FUNC);
}
#endif

#ifndef NO_Panel_1WidthProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Panel_1WidthProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Panel_1WidthProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Panel_1WidthProperty_FUNC);
	rc = (jint)TO_HANDLE(Panel::WidthProperty);
	OS_NATIVE_EXIT(env, that, Panel_1WidthProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_PasswordBox_1MaxLength__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PasswordBox_1MaxLength__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PasswordBox_1MaxLength__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PasswordBox_1MaxLength__I_FUNC);
	rc = (jint)((PasswordBox^)TO_OBJECT(arg0))->MaxLength;
	OS_NATIVE_EXIT(env, that, PasswordBox_1MaxLength__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PasswordBox_1MaxLength__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1MaxLength__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1MaxLength__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PasswordBox_1MaxLength__II_FUNC);
	((PasswordBox^)TO_OBJECT(arg0))->MaxLength = (arg1);
	OS_NATIVE_EXIT(env, that, PasswordBox_1MaxLength__II_FUNC);
}
#endif

#ifndef NO_PasswordBox_1Password__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PasswordBox_1Password__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PasswordBox_1Password__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PasswordBox_1Password__I_FUNC);
	rc = (jint)TO_HANDLE(((PasswordBox^)TO_OBJECT(arg0))->Password);
	OS_NATIVE_EXIT(env, that, PasswordBox_1Password__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PasswordBox_1Password__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1Password__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1Password__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PasswordBox_1Password__II_FUNC);
	((PasswordBox^)TO_OBJECT(arg0))->Password = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PasswordBox_1Password__II_FUNC);
}
#endif

#ifndef NO_PasswordBox_1PasswordChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1PasswordChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1PasswordChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PasswordBox_1PasswordChanged_FUNC);
	((PasswordBox^)TO_OBJECT(arg0))->PasswordChanged += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PasswordBox_1PasswordChanged_FUNC);
}
#endif

#ifndef NO_PasswordBox_1PasswordChar__I
extern "C" JNIEXPORT jchar JNICALL OS_NATIVE(PasswordBox_1PasswordChar__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jchar JNICALL OS_NATIVE(PasswordBox_1PasswordChar__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jchar rc = 0;
	OS_NATIVE_ENTER(env, that, PasswordBox_1PasswordChar__I_FUNC);
	rc = (jchar)((PasswordBox^)TO_OBJECT(arg0))->PasswordChar;
	OS_NATIVE_EXIT(env, that, PasswordBox_1PasswordChar__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PasswordBox_1PasswordChar__IC
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1PasswordChar__IC)(JNIEnv *env, jclass that, jint arg0, jchar arg1);
JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1PasswordChar__IC)
	(JNIEnv *env, jclass that, jint arg0, jchar arg1)
{
	OS_NATIVE_ENTER(env, that, PasswordBox_1PasswordChar__IC_FUNC);
	((PasswordBox^)TO_OBJECT(arg0))->PasswordChar = (arg1);
	OS_NATIVE_EXIT(env, that, PasswordBox_1PasswordChar__IC_FUNC);
}
#endif

#ifndef NO_PasswordBox_1Paste
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1Paste)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(PasswordBox_1Paste)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PasswordBox_1Paste_FUNC);
	((PasswordBox^)TO_OBJECT(arg0))->Paste();
	OS_NATIVE_EXIT(env, that, PasswordBox_1Paste_FUNC);
}
#endif

#ifndef NO_PathFigureCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathFigureCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathFigureCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PathFigureCollection_1Add_FUNC);
	((PathFigureCollection^)TO_OBJECT(arg0))->Add((PathFigure^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PathFigureCollection_1Add_FUNC);
}
#endif

#ifndef NO_PathFigureCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathFigureCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathFigureCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathFigureCollection_1Count_FUNC);
	rc = (jint)((PathFigureCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, PathFigureCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_PathFigure_1IsClosed__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(PathFigure_1IsClosed__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(PathFigure_1IsClosed__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PathFigure_1IsClosed__I_FUNC);
	rc = (jboolean)((PathFigure^)TO_OBJECT(arg0))->IsClosed;
	OS_NATIVE_EXIT(env, that, PathFigure_1IsClosed__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PathFigure_1IsClosed__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathFigure_1IsClosed__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathFigure_1IsClosed__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, PathFigure_1IsClosed__IZ_FUNC);
	((PathFigure^)TO_OBJECT(arg0))->IsClosed = (arg1);
	OS_NATIVE_EXIT(env, that, PathFigure_1IsClosed__IZ_FUNC);
}
#endif

#ifndef NO_PathFigure_1Segments__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathFigure_1Segments__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathFigure_1Segments__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathFigure_1Segments__I_FUNC);
	rc = (jint)TO_HANDLE(((PathFigure^)TO_OBJECT(arg0))->Segments);
	OS_NATIVE_EXIT(env, that, PathFigure_1Segments__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PathFigure_1Segments__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathFigure_1Segments__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(PathFigure_1Segments__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathFigure_1Segments__II_FUNC);
	rc = (jint)TO_HANDLE(((PathFigure^)TO_OBJECT(arg0))->Segments[arg1]);
	OS_NATIVE_EXIT(env, that, PathFigure_1Segments__II_FUNC);
	return rc;
}
#endif

#ifndef NO_PathFigure_1StartPoint
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathFigure_1StartPoint)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathFigure_1StartPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PathFigure_1StartPoint_FUNC);
	((PathFigure^)TO_OBJECT(arg0))->StartPoint = ((Point)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PathFigure_1StartPoint_FUNC);
}
#endif

#ifndef NO_PathGeometry_1AddGeometry
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathGeometry_1AddGeometry)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathGeometry_1AddGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PathGeometry_1AddGeometry_FUNC);
	((PathGeometry^)TO_OBJECT(arg0))->AddGeometry((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PathGeometry_1AddGeometry_FUNC);
}
#endif

#ifndef NO_PathGeometry_1Bounds
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Bounds)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Bounds)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathGeometry_1Bounds_FUNC);
	rc = (jint)TO_HANDLE(((PathGeometry^)TO_OBJECT(arg0))->Bounds);
	OS_NATIVE_EXIT(env, that, PathGeometry_1Bounds_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGeometry_1Clone
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Clone)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathGeometry_1Clone_FUNC);
	rc = (jint)TO_HANDLE(((PathGeometry^)TO_OBJECT(arg0))->Clone());
	OS_NATIVE_EXIT(env, that, PathGeometry_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGeometry_1Figures__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Figures__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Figures__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathGeometry_1Figures__I_FUNC);
	rc = (jint)TO_HANDLE(((PathGeometry^)TO_OBJECT(arg0))->Figures);
	OS_NATIVE_EXIT(env, that, PathGeometry_1Figures__I_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGeometry_1Figures__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Figures__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(PathGeometry_1Figures__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathGeometry_1Figures__II_FUNC);
	rc = (jint)TO_HANDLE(((PathGeometry^)TO_OBJECT(arg0))->Figures[arg1]);
	OS_NATIVE_EXIT(env, that, PathGeometry_1Figures__II_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGeometry_1FillRule
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathGeometry_1FillRule)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathGeometry_1FillRule)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PathGeometry_1FillRule_FUNC);
	((PathGeometry^)TO_OBJECT(arg0))->FillRule = ((FillRule)arg1);
	OS_NATIVE_EXIT(env, that, PathGeometry_1FillRule_FUNC);
}
#endif

#ifndef NO_PathSegmentCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PathSegmentCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PathSegmentCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PathSegmentCollection_1Add_FUNC);
	((PathSegmentCollection^)TO_OBJECT(arg0))->Add((PathSegment^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PathSegmentCollection_1Add_FUNC);
}
#endif

#ifndef NO_PathSegmentCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PathSegmentCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PathSegmentCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PathSegmentCollection_1Count_FUNC);
	rc = (jint)((PathSegmentCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, PathSegmentCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_Path_1Data
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Path_1Data)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Path_1Data)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Path_1Data_FUNC);
	((Path^)TO_OBJECT(arg0))->Data = ((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Path_1Data_FUNC);
}
#endif

#ifndef NO_Path_1Fill
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Path_1Fill)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Path_1Fill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Path_1Fill_FUNC);
	((Path^)TO_OBJECT(arg0))->Fill = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Path_1Fill_FUNC);
}
#endif

#ifndef NO_Path_1Stretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Path_1Stretch)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Path_1Stretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Path_1Stretch_FUNC);
	((Path^)TO_OBJECT(arg0))->Stretch = ((Stretch)arg1);
	OS_NATIVE_EXIT(env, that, Path_1Stretch_FUNC);
}
#endif

#ifndef NO_Pen_1Brush__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Pen_1Brush__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Pen_1Brush__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Pen_1Brush__I_FUNC);
	rc = (jint)TO_HANDLE(((Pen^)TO_OBJECT(arg0))->Brush);
	OS_NATIVE_EXIT(env, that, Pen_1Brush__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Pen_1Brush__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1Brush__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1Brush__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1Brush__II_FUNC);
	((Pen^)TO_OBJECT(arg0))->Brush = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Pen_1Brush__II_FUNC);
}
#endif

#ifndef NO_Pen_1DashCap
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1DashCap)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1DashCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1DashCap_FUNC);
	((Pen^)TO_OBJECT(arg0))->DashCap = ((PenLineCap)arg1);
	OS_NATIVE_EXIT(env, that, Pen_1DashCap_FUNC);
}
#endif

#ifndef NO_Pen_1DashStyle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1DashStyle)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1DashStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1DashStyle_FUNC);
	((Pen^)TO_OBJECT(arg0))->DashStyle = ((DashStyle^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Pen_1DashStyle_FUNC);
}
#endif

#ifndef NO_Pen_1EndLineCap
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1EndLineCap)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1EndLineCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1EndLineCap_FUNC);
	((Pen^)TO_OBJECT(arg0))->EndLineCap = ((PenLineCap)arg1);
	OS_NATIVE_EXIT(env, that, Pen_1EndLineCap_FUNC);
}
#endif

#ifndef NO_Pen_1LineJoin
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1LineJoin)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1LineJoin)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1LineJoin_FUNC);
	((Pen^)TO_OBJECT(arg0))->LineJoin = ((PenLineJoin)arg1);
	OS_NATIVE_EXIT(env, that, Pen_1LineJoin_FUNC);
}
#endif

#ifndef NO_Pen_1MiterLimit
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1MiterLimit)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1MiterLimit)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1MiterLimit_FUNC);
	((Pen^)TO_OBJECT(arg0))->MiterLimit = (arg1);
	OS_NATIVE_EXIT(env, that, Pen_1MiterLimit_FUNC);
}
#endif

#ifndef NO_Pen_1StartLineCap
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1StartLineCap)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1StartLineCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1StartLineCap_FUNC);
	((Pen^)TO_OBJECT(arg0))->StartLineCap = ((PenLineCap)arg1);
	OS_NATIVE_EXIT(env, that, Pen_1StartLineCap_FUNC);
}
#endif

#ifndef NO_Pen_1Thickness
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Pen_1Thickness)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Pen_1Thickness)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Pen_1Thickness_FUNC);
	((Pen^)TO_OBJECT(arg0))->Thickness = (arg1);
	OS_NATIVE_EXIT(env, that, Pen_1Thickness_FUNC);
}
#endif

#ifndef NO_PixelFormat_1BitsPerPixel
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormat_1BitsPerPixel)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormat_1BitsPerPixel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormat_1BitsPerPixel_FUNC);
	rc = (jint)((PixelFormat^)TO_OBJECT(arg0))->BitsPerPixel;
	OS_NATIVE_EXIT(env, that, PixelFormat_1BitsPerPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgr101010
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr101010)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr101010)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgr101010_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgr101010);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgr101010_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgr24
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr24)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr24)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgr24_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgr24);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgr24_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgr32
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr32)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr32)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgr32_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgr32);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgr32_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgr555
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr555)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr555)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgr555_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgr555);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgr555_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgr565
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr565)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgr565)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgr565_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgr565);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgr565_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Bgra32
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgra32)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Bgra32)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Bgra32_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Bgra32);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Bgra32_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1BlackWhite
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1BlackWhite)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1BlackWhite)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1BlackWhite_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::BlackWhite);
	OS_NATIVE_EXIT(env, that, PixelFormats_1BlackWhite_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Default
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Default)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Default)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Default_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Default);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Default_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Indexed1
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed1)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed1)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Indexed1_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Indexed1);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Indexed1_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Indexed2
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed2)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed2)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Indexed2_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Indexed2);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Indexed2_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Indexed4
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed4)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed4)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Indexed4_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Indexed4);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Indexed4_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Indexed8
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed8)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Indexed8)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Indexed8_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Indexed8);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Indexed8_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Pbgra32
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Pbgra32)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Pbgra32)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Pbgra32_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Pbgra32);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Pbgra32_FUNC);
	return rc;
}
#endif

#ifndef NO_PixelFormats_1Rgb24
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Rgb24)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PixelFormats_1Rgb24)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PixelFormats_1Rgb24_FUNC);
	rc = (jint)TO_HANDLE(PixelFormats::Rgb24);
	OS_NATIVE_EXIT(env, that, PixelFormats_1Rgb24_FUNC);
	return rc;
}
#endif

#ifndef NO_PointCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PointCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(PointCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PointCollection_1Add_FUNC);
	((PointCollection^)TO_OBJECT(arg0))->Add((Point)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, PointCollection_1Add_FUNC);
}
#endif

#ifndef NO_Point_1X
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Point_1X)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Point_1X)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Point_1X_FUNC);
	rc = (jdouble)((Point^)TO_OBJECT(arg0))->X;
	OS_NATIVE_EXIT(env, that, Point_1X_FUNC);
	return rc;
}
#endif

#ifndef NO_Point_1Y
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Point_1Y)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Point_1Y)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Point_1Y_FUNC);
	rc = (jdouble)((Point^)TO_OBJECT(arg0))->Y;
	OS_NATIVE_EXIT(env, that, Point_1Y_FUNC);
	return rc;
}
#endif

#ifndef NO_Popup_1AllowsTransparency
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1AllowsTransparency)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1AllowsTransparency)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1AllowsTransparency_FUNC);
	((Popup^)TO_OBJECT(arg0))->AllowsTransparency = (arg1);
	OS_NATIVE_EXIT(env, that, Popup_1AllowsTransparency_FUNC);
}
#endif

#ifndef NO_Popup_1Child__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Popup_1Child__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Popup_1Child__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Popup_1Child__I_FUNC);
	rc = (jint)TO_HANDLE(((Popup^)TO_OBJECT(arg0))->Child);
	OS_NATIVE_EXIT(env, that, Popup_1Child__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Popup_1Child__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1Child__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1Child__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1Child__II_FUNC);
	((Popup^)TO_OBJECT(arg0))->Child = ((UIElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Popup_1Child__II_FUNC);
}
#endif

#ifndef NO_Popup_1Closed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1Closed)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1Closed)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1Closed_FUNC);
	((Popup^)TO_OBJECT(arg0))->Closed += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Popup_1Closed_FUNC);
}
#endif

#ifndef NO_Popup_1HorizontalOffset__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Popup_1HorizontalOffset__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Popup_1HorizontalOffset__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Popup_1HorizontalOffset__I_FUNC);
	rc = (jdouble)((Popup^)TO_OBJECT(arg0))->HorizontalOffset;
	OS_NATIVE_EXIT(env, that, Popup_1HorizontalOffset__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Popup_1HorizontalOffset__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1HorizontalOffset__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1HorizontalOffset__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1HorizontalOffset__ID_FUNC);
	((Popup^)TO_OBJECT(arg0))->HorizontalOffset = (arg1);
	OS_NATIVE_EXIT(env, that, Popup_1HorizontalOffset__ID_FUNC);
}
#endif

#ifndef NO_Popup_1IsOpen__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Popup_1IsOpen__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Popup_1IsOpen__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Popup_1IsOpen__I_FUNC);
	rc = (jboolean)((Popup^)TO_OBJECT(arg0))->IsOpen;
	OS_NATIVE_EXIT(env, that, Popup_1IsOpen__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Popup_1IsOpen__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1IsOpen__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1IsOpen__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1IsOpen__IZ_FUNC);
	((Popup^)TO_OBJECT(arg0))->IsOpen = (arg1);
	OS_NATIVE_EXIT(env, that, Popup_1IsOpen__IZ_FUNC);
}
#endif

#ifndef NO_Popup_1Opened
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1Opened)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1Opened)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1Opened_FUNC);
	((Popup^)TO_OBJECT(arg0))->Opened += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Popup_1Opened_FUNC);
}
#endif

#ifndef NO_Popup_1VerticalOffset__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Popup_1VerticalOffset__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Popup_1VerticalOffset__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Popup_1VerticalOffset__I_FUNC);
	rc = (jdouble)((Popup^)TO_OBJECT(arg0))->VerticalOffset;
	OS_NATIVE_EXIT(env, that, Popup_1VerticalOffset__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Popup_1VerticalOffset__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Popup_1VerticalOffset__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Popup_1VerticalOffset__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Popup_1VerticalOffset__ID_FUNC);
	((Popup^)TO_OBJECT(arg0))->VerticalOffset = (arg1);
	OS_NATIVE_EXIT(env, that, Popup_1VerticalOffset__ID_FUNC);
}
#endif

#ifndef NO_PresentationSource_1CurrentSources
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1CurrentSources)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1CurrentSources)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PresentationSource_1CurrentSources_FUNC);
	rc = (jint)TO_HANDLE(PresentationSource::CurrentSources);
	OS_NATIVE_EXIT(env, that, PresentationSource_1CurrentSources_FUNC);
	return rc;
}
#endif

#ifndef NO_PresentationSource_1FromVisual
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1FromVisual)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1FromVisual)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PresentationSource_1FromVisual_FUNC);
	rc = (jint)TO_HANDLE(PresentationSource::FromVisual((Visual^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, PresentationSource_1FromVisual_FUNC);
	return rc;
}
#endif

#ifndef NO_PresentationSource_1RootVisual
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1RootVisual)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(PresentationSource_1RootVisual)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PresentationSource_1RootVisual_FUNC);
	rc = (jint)TO_HANDLE(((PresentationSource^)TO_OBJECT(arg0))->RootVisual);
	OS_NATIVE_EXIT(env, that, PresentationSource_1RootVisual_FUNC);
	return rc;
}
#endif

#ifndef NO_ProgressBar_1IsIndeterminate__I
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1IsIndeterminate__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1IsIndeterminate__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ProgressBar_1IsIndeterminate__I_FUNC);
	((ProgressBar ^)TO_OBJECT(arg0))->IsIndeterminate;
	OS_NATIVE_EXIT(env, that, ProgressBar_1IsIndeterminate__I_FUNC);
}
#endif

#ifndef NO_ProgressBar_1IsIndeterminate__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1IsIndeterminate__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1IsIndeterminate__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ProgressBar_1IsIndeterminate__IZ_FUNC);
	((ProgressBar ^)TO_OBJECT(arg0))->IsIndeterminate = (arg1);
	OS_NATIVE_EXIT(env, that, ProgressBar_1IsIndeterminate__IZ_FUNC);
}
#endif

#ifndef NO_ProgressBar_1Orientation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1Orientation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ProgressBar_1Orientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ProgressBar_1Orientation_FUNC);
	((ProgressBar ^)TO_OBJECT(arg0))->Orientation = ((Orientation)arg1);
	OS_NATIVE_EXIT(env, that, ProgressBar_1Orientation_FUNC);
}
#endif

#ifndef NO_PropertyInfo_1SetValue
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PropertyInfo_1SetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(PropertyInfo_1SetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PropertyInfo_1SetValue_FUNC);
	((PropertyInfo^)TO_OBJECT(arg0))->SetValue((Object^)TO_OBJECT(arg1), (Object^)TO_OBJECT(arg2), (array<Object^>^)TO_OBJECT(arg3));
	OS_NATIVE_EXIT(env, that, PropertyInfo_1SetValue_FUNC);
}
#endif

#ifndef NO_PropertyInfo_1SetValueBoolean
extern "C" JNIEXPORT void JNICALL OS_NATIVE(PropertyInfo_1SetValueBoolean)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(PropertyInfo_1SetValueBoolean)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PropertyInfo_1SetValueBoolean_FUNC);
	((PropertyInfo^)TO_OBJECT(arg0))->PropertyInfo::SetValue((Object^)TO_OBJECT(arg1), (bool)arg2, (array<Object^>^)TO_OBJECT(arg3));
	OS_NATIVE_EXIT(env, that, PropertyInfo_1SetValueBoolean_FUNC);
}
#endif

#ifndef NO_QueryContinueDragEventArgs_1Action
extern "C" JNIEXPORT void JNICALL OS_NATIVE(QueryContinueDragEventArgs_1Action)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(QueryContinueDragEventArgs_1Action)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, QueryContinueDragEventArgs_1Action_FUNC);
	((QueryContinueDragEventArgs^)TO_OBJECT(arg0))->Action = ((DragAction)arg1);
	OS_NATIVE_EXIT(env, that, QueryContinueDragEventArgs_1Action_FUNC);
}
#endif

#ifndef NO_QueryContinueDragEventArgs_1EscapePressed
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(QueryContinueDragEventArgs_1EscapePressed)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(QueryContinueDragEventArgs_1EscapePressed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, QueryContinueDragEventArgs_1EscapePressed_FUNC);
	rc = (jboolean)((QueryContinueDragEventArgs^)TO_OBJECT(arg0))->EscapePressed;
	OS_NATIVE_EXIT(env, that, QueryContinueDragEventArgs_1EscapePressed_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1LargeChange__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1LargeChange__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1LargeChange__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, RangeBase_1LargeChange__I_FUNC);
	rc = (jdouble)((RangeBase ^)TO_OBJECT(arg0))->LargeChange;
	OS_NATIVE_EXIT(env, that, RangeBase_1LargeChange__I_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1LargeChange__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1LargeChange__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1LargeChange__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1LargeChange__ID_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->LargeChange = (arg1);
	OS_NATIVE_EXIT(env, that, RangeBase_1LargeChange__ID_FUNC);
}
#endif

#ifndef NO_RangeBase_1Maximum__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Maximum__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Maximum__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, RangeBase_1Maximum__I_FUNC);
	rc = (jdouble)((RangeBase ^)TO_OBJECT(arg0))->Maximum;
	OS_NATIVE_EXIT(env, that, RangeBase_1Maximum__I_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1Maximum__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Maximum__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Maximum__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1Maximum__ID_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->Maximum = (arg1);
	OS_NATIVE_EXIT(env, that, RangeBase_1Maximum__ID_FUNC);
}
#endif

#ifndef NO_RangeBase_1Minimum__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Minimum__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Minimum__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, RangeBase_1Minimum__I_FUNC);
	rc = (jdouble)((RangeBase ^)TO_OBJECT(arg0))->Minimum;
	OS_NATIVE_EXIT(env, that, RangeBase_1Minimum__I_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1Minimum__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Minimum__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Minimum__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1Minimum__ID_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->Minimum = (arg1);
	OS_NATIVE_EXIT(env, that, RangeBase_1Minimum__ID_FUNC);
}
#endif

#ifndef NO_RangeBase_1SmallChange__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1SmallChange__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1SmallChange__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, RangeBase_1SmallChange__I_FUNC);
	rc = (jdouble)((RangeBase ^)TO_OBJECT(arg0))->SmallChange;
	OS_NATIVE_EXIT(env, that, RangeBase_1SmallChange__I_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1SmallChange__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1SmallChange__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1SmallChange__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1SmallChange__ID_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->SmallChange = (arg1);
	OS_NATIVE_EXIT(env, that, RangeBase_1SmallChange__ID_FUNC);
}
#endif

#ifndef NO_RangeBase_1Value__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Value__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(RangeBase_1Value__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, RangeBase_1Value__I_FUNC);
	rc = (jdouble)((RangeBase ^)TO_OBJECT(arg0))->Value;
	OS_NATIVE_EXIT(env, that, RangeBase_1Value__I_FUNC);
	return rc;
}
#endif

#ifndef NO_RangeBase_1Value__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Value__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1Value__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1Value__ID_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->Value = (arg1);
	OS_NATIVE_EXIT(env, that, RangeBase_1Value__ID_FUNC);
}
#endif

#ifndef NO_RangeBase_1ValueChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1ValueChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RangeBase_1ValueChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RangeBase_1ValueChanged_FUNC);
	((RangeBase ^)TO_OBJECT(arg0))->ValueChanged += ((RoutedPropertyChangedEventHandler<double> ^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, RangeBase_1ValueChanged_FUNC);
}
#endif

#ifndef NO_Rect_1Contains
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Rect_1Contains)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Rect_1Contains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Rect_1Contains_FUNC);
	rc = (jboolean)((Rect^)TO_OBJECT(arg0))->Contains((Point)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Rect_1Contains_FUNC);
	return rc;
}
#endif

#ifndef NO_Rect_1Height__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Height__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Height__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Rect_1Height__I_FUNC);
	rc = (jdouble)((Rect^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, Rect_1Height__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Rect_1Height__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1Height__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1Height__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1Height__ID_FUNC);
	((Rect^)TO_OBJECT(arg0))->Height = (arg1);
	OS_NATIVE_EXIT(env, that, Rect_1Height__ID_FUNC);
}
#endif

#ifndef NO_Rect_1Intersect
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1Intersect)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1Intersect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1Intersect_FUNC);
	((Rect^)TO_OBJECT(arg0))->Intersect((Rect)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Rect_1Intersect_FUNC);
}
#endif

#ifndef NO_Rect_1Union
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1Union)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1Union)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1Union_FUNC);
	((Rect^)TO_OBJECT(arg0))->Union((Rect)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Rect_1Union_FUNC);
}
#endif

#ifndef NO_Rect_1Width__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Width__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Width__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Rect_1Width__I_FUNC);
	rc = (jdouble)((Rect^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, Rect_1Width__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Rect_1Width__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1Width__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1Width__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1Width__ID_FUNC);
	((Rect^)TO_OBJECT(arg0))->Width = (arg1);
	OS_NATIVE_EXIT(env, that, Rect_1Width__ID_FUNC);
}
#endif

#ifndef NO_Rect_1X__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1X__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1X__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Rect_1X__I_FUNC);
	rc = (jdouble)((Rect^)TO_OBJECT(arg0))->X;
	OS_NATIVE_EXIT(env, that, Rect_1X__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Rect_1X__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1X__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1X__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1X__ID_FUNC);
	((Rect^)TO_OBJECT(arg0))->X = (arg1);
	OS_NATIVE_EXIT(env, that, Rect_1X__ID_FUNC);
}
#endif

#ifndef NO_Rect_1Y__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Y__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Rect_1Y__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Rect_1Y__I_FUNC);
	rc = (jdouble)((Rect^)TO_OBJECT(arg0))->Y;
	OS_NATIVE_EXIT(env, that, Rect_1Y__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Rect_1Y__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Rect_1Y__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Rect_1Y__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Rect_1Y__ID_FUNC);
	((Rect^)TO_OBJECT(arg0))->Y = (arg1);
	OS_NATIVE_EXIT(env, that, Rect_1Y__ID_FUNC);
}
#endif

#ifndef NO_Rectangle_1Height
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Height)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Rectangle_1Height_FUNC);
	rc = (jint)((System::Drawing::Rectangle^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, Rectangle_1Height_FUNC);
	return rc;
}
#endif

#ifndef NO_Rectangle_1Width
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Width)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Width)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Rectangle_1Width_FUNC);
	rc = (jint)((System::Drawing::Rectangle^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, Rectangle_1Width_FUNC);
	return rc;
}
#endif

#ifndef NO_Rectangle_1X
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1X)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1X)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Rectangle_1X_FUNC);
	rc = (jint)((System::Drawing::Rectangle^)TO_OBJECT(arg0))->X;
	OS_NATIVE_EXIT(env, that, Rectangle_1X_FUNC);
	return rc;
}
#endif

#ifndef NO_Rectangle_1Y
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Y)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Rectangle_1Y)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Rectangle_1Y_FUNC);
	rc = (jint)((System::Drawing::Rectangle^)TO_OBJECT(arg0))->Y;
	OS_NATIVE_EXIT(env, that, Rectangle_1Y_FUNC);
	return rc;
}
#endif

#ifndef NO_RegistryKey_1GetSubKeyNames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1GetSubKeyNames)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1GetSubKeyNames)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegistryKey_1GetSubKeyNames_FUNC);
	rc = (jint)TO_HANDLE(((RegistryKey^)TO_OBJECT(arg0))->GetSubKeyNames());
	OS_NATIVE_EXIT(env, that, RegistryKey_1GetSubKeyNames_FUNC);
	return rc;
}
#endif

#ifndef NO_RegistryKey_1GetValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1GetValue)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1GetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegistryKey_1GetValue_FUNC);
	rc = (jint)TO_HANDLE(((RegistryKey^)TO_OBJECT(arg0))->GetValue((String^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, RegistryKey_1GetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_RegistryKey_1OpenSubKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1OpenSubKey)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(RegistryKey_1OpenSubKey)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegistryKey_1OpenSubKey_FUNC);
	rc = (jint)TO_HANDLE(((RegistryKey^)TO_OBJECT(arg0))->OpenSubKey((String^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, RegistryKey_1OpenSubKey_FUNC);
	return rc;
}
#endif

#ifndef NO_Registry_1ClassesRoot
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Registry_1ClassesRoot)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Registry_1ClassesRoot)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Registry_1ClassesRoot_FUNC);
	rc = (jint)TO_HANDLE(Registry::ClassesRoot);
	OS_NATIVE_EXIT(env, that, Registry_1ClassesRoot_FUNC);
	return rc;
}
#endif

#ifndef NO_RelativeSource_1AncestorType
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RelativeSource_1AncestorType)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RelativeSource_1AncestorType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RelativeSource_1AncestorType_FUNC);
	((RelativeSource^)TO_OBJECT(arg0))->AncestorType = ((Type^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, RelativeSource_1AncestorType_FUNC);
}
#endif

#ifndef NO_RenderOptions_1GetBitmapScalingMode
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RenderOptions_1GetBitmapScalingMode)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RenderOptions_1GetBitmapScalingMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RenderOptions_1GetBitmapScalingMode_FUNC);
	rc = (jint)RenderOptions::GetBitmapScalingMode((DependencyObject^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, RenderOptions_1GetBitmapScalingMode_FUNC);
	return rc;
}
#endif

#ifndef NO_RenderOptions_1SetBitmapScalingMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RenderOptions_1SetBitmapScalingMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RenderOptions_1SetBitmapScalingMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RenderOptions_1SetBitmapScalingMode_FUNC);
	RenderOptions::SetBitmapScalingMode((DependencyObject^)TO_OBJECT(arg0), (BitmapScalingMode)arg1);
	OS_NATIVE_EXIT(env, that, RenderOptions_1SetBitmapScalingMode_FUNC);
}
#endif

#ifndef NO_RenderOptions_1SetEdgeMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RenderOptions_1SetEdgeMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RenderOptions_1SetEdgeMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RenderOptions_1SetEdgeMode_FUNC);
	RenderOptions::SetEdgeMode((DependencyObject^)TO_OBJECT(arg0), (EdgeMode)arg1);
	OS_NATIVE_EXIT(env, that, RenderOptions_1SetEdgeMode_FUNC);
}
#endif

#ifndef NO_RenderTargetBitmap_1Render
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RenderTargetBitmap_1Render)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RenderTargetBitmap_1Render)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RenderTargetBitmap_1Render_FUNC);
	((RenderTargetBitmap^)TO_OBJECT(arg0))->Render((Visual^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, RenderTargetBitmap_1Render_FUNC);
}
#endif

#ifndef NO_RepeatBehavior_1Forever
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RepeatBehavior_1Forever)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(RepeatBehavior_1Forever)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RepeatBehavior_1Forever_FUNC);
	rc = (jint)TO_HANDLE(RepeatBehavior::Forever);
	OS_NATIVE_EXIT(env, that, RepeatBehavior_1Forever_FUNC);
	return rc;
}
#endif

#ifndef NO_ResourceDictionary_1Source
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ResourceDictionary_1Source)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ResourceDictionary_1Source)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ResourceDictionary_1Source_FUNC);
	((ResourceDictionary^)TO_OBJECT(arg0))->Source = ((Uri^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ResourceDictionary_1Source_FUNC);
}
#endif

#ifndef NO_RoutedEventArgs_1Handled
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RoutedEventArgs_1Handled)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(RoutedEventArgs_1Handled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, RoutedEventArgs_1Handled_FUNC);
	((RoutedEventArgs^)TO_OBJECT(arg0))->Handled = (arg1);
	OS_NATIVE_EXIT(env, that, RoutedEventArgs_1Handled_FUNC);
}
#endif

#ifndef NO_RoutedEventArgs_1OriginalSource
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1OriginalSource)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1OriginalSource)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RoutedEventArgs_1OriginalSource_FUNC);
	rc = (jint)TO_HANDLE(((RoutedEventArgs^)TO_OBJECT(arg0))->OriginalSource);
	OS_NATIVE_EXIT(env, that, RoutedEventArgs_1OriginalSource_FUNC);
	return rc;
}
#endif

#ifndef NO_RoutedEventArgs_1Source
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1Source)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1Source)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RoutedEventArgs_1Source_FUNC);
	rc = (jint)TO_HANDLE(((RoutedEventArgs^)TO_OBJECT(arg0))->Source);
	OS_NATIVE_EXIT(env, that, RoutedEventArgs_1Source_FUNC);
	return rc;
}
#endif

#ifndef NO_RoutedEventArgs_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(RoutedEventArgs_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RoutedEventArgs_1typeid_FUNC);
	rc = (jint)TO_HANDLE(RoutedEventArgs::typeid);
	OS_NATIVE_EXIT(env, that, RoutedEventArgs_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_RoutedPropertyChangedEventArgs_1NewValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RoutedPropertyChangedEventArgs_1NewValue)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RoutedPropertyChangedEventArgs_1NewValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RoutedPropertyChangedEventArgs_1NewValue_FUNC);
	rc = (jint)TO_HANDLE(((RoutedPropertyChangedEventArgs<Object^>^)TO_OBJECT(arg0))->NewValue);
	OS_NATIVE_EXIT(env, that, RoutedPropertyChangedEventArgs_1NewValue_FUNC);
	return rc;
}
#endif

#ifndef NO_RoutedPropertyChangedEventArgs_1OldValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(RoutedPropertyChangedEventArgs_1OldValue)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(RoutedPropertyChangedEventArgs_1OldValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RoutedPropertyChangedEventArgs_1OldValue_FUNC);
	rc = (jint)TO_HANDLE(((RoutedPropertyChangedEventArgs<Object^>^)TO_OBJECT(arg0))->OldValue);
	OS_NATIVE_EXIT(env, that, RoutedPropertyChangedEventArgs_1OldValue_FUNC);
	return rc;
}
#endif

#ifndef NO_RowDefinitionCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RowDefinitionCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RowDefinitionCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RowDefinitionCollection_1Add_FUNC);
	((RowDefinitionCollection^)TO_OBJECT(arg0))->Add((RowDefinition^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, RowDefinitionCollection_1Add_FUNC);
}
#endif

#ifndef NO_RowDefinition_1Height
extern "C" JNIEXPORT void JNICALL OS_NATIVE(RowDefinition_1Height)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(RowDefinition_1Height)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, RowDefinition_1Height_FUNC);
	((RowDefinition^)TO_OBJECT(arg0))->Height = ((GridLength)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, RowDefinition_1Height_FUNC);
}
#endif

#ifndef NO_Run_1Text
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Run_1Text)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Run_1Text)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Run_1Text_FUNC);
	((Run^)TO_OBJECT(arg0))->Text = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Run_1Text_FUNC);
}
#endif

#ifndef NO_SaveFileDialog_1OverwritePrompt
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SaveFileDialog_1OverwritePrompt)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(SaveFileDialog_1OverwritePrompt)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, SaveFileDialog_1OverwritePrompt_FUNC);
	((SaveFileDialog^)TO_OBJECT(arg0))->OverwritePrompt = (arg1);
	OS_NATIVE_EXIT(env, that, SaveFileDialog_1OverwritePrompt_FUNC);
}
#endif

#ifndef NO_Screen_1AllScreens
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Screen_1AllScreens)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Screen_1AllScreens)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Screen_1AllScreens_FUNC);
	rc = (jint)TO_HANDLE(System::Windows::Forms::Screen::AllScreens);
	OS_NATIVE_EXIT(env, that, Screen_1AllScreens_FUNC);
	return rc;
}
#endif

#ifndef NO_Screen_1Bounds
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Screen_1Bounds)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Screen_1Bounds)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Screen_1Bounds_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::Screen^)TO_OBJECT(arg0))->Bounds);
	OS_NATIVE_EXIT(env, that, Screen_1Bounds_FUNC);
	return rc;
}
#endif

#ifndef NO_Screen_1PrimaryScreen
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Screen_1PrimaryScreen)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Screen_1PrimaryScreen)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Screen_1PrimaryScreen_FUNC);
	rc = (jint)TO_HANDLE(System::Windows::Forms::Screen::PrimaryScreen);
	OS_NATIVE_EXIT(env, that, Screen_1PrimaryScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_Screen_1WorkingArea
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Screen_1WorkingArea)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Screen_1WorkingArea)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Screen_1WorkingArea_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::Screen^)TO_OBJECT(arg0))->WorkingArea);
	OS_NATIVE_EXIT(env, that, Screen_1WorkingArea_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollBar_1Orientation__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ScrollBar_1Orientation__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ScrollBar_1Orientation__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollBar_1Orientation__I_FUNC);
	rc = (jint)((ScrollBar^)TO_OBJECT(arg0))->Orientation;
	OS_NATIVE_EXIT(env, that, ScrollBar_1Orientation__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollBar_1Orientation__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1Orientation__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1Orientation__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollBar_1Orientation__II_FUNC);
	((ScrollBar^)TO_OBJECT(arg0))->Orientation = ((Orientation)arg1);
	OS_NATIVE_EXIT(env, that, ScrollBar_1Orientation__II_FUNC);
}
#endif

#ifndef NO_ScrollBar_1Scroll
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1Scroll)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1Scroll)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollBar_1Scroll_FUNC);
	((ScrollBar^)TO_OBJECT(arg0))->Scroll += ((ScrollEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ScrollBar_1Scroll_FUNC);
}
#endif

#ifndef NO_ScrollBar_1ViewportSize__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(ScrollBar_1ViewportSize__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(ScrollBar_1ViewportSize__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollBar_1ViewportSize__I_FUNC);
	rc = (jdouble)((ScrollBar^)TO_OBJECT(arg0))->ViewportSize;
	OS_NATIVE_EXIT(env, that, ScrollBar_1ViewportSize__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollBar_1ViewportSize__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1ViewportSize__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollBar_1ViewportSize__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollBar_1ViewportSize__ID_FUNC);
	((ScrollBar^)TO_OBJECT(arg0))->ViewportSize = (arg1);
	OS_NATIVE_EXIT(env, that, ScrollBar_1ViewportSize__ID_FUNC);
}
#endif

#ifndef NO_ScrollBar_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ScrollBar_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ScrollBar_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollBar_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ScrollBar::typeid);
	OS_NATIVE_EXIT(env, that, ScrollBar_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollEventArgs_1ScrollEventType
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ScrollEventArgs_1ScrollEventType)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ScrollEventArgs_1ScrollEventType)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollEventArgs_1ScrollEventType_FUNC);
	rc = (jint)((ScrollEventArgs^)TO_OBJECT(arg0))->ScrollEventType;
	OS_NATIVE_EXIT(env, that, ScrollEventArgs_1ScrollEventType_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollViewer_1ScrollToVerticalOffset
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1ScrollToVerticalOffset)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1ScrollToVerticalOffset)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollViewer_1ScrollToVerticalOffset_FUNC);
	((ScrollViewer^)TO_OBJECT(arg0))->ScrollToVerticalOffset(arg1);
	OS_NATIVE_EXIT(env, that, ScrollViewer_1ScrollToVerticalOffset_FUNC);
}
#endif

#ifndef NO_ScrollViewer_1SetHorizontalScrollBarVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1SetHorizontalScrollBarVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1SetHorizontalScrollBarVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollViewer_1SetHorizontalScrollBarVisibility_FUNC);
	ScrollViewer::SetHorizontalScrollBarVisibility((DependencyObject^)TO_OBJECT(arg0), (ScrollBarVisibility)arg1);
	OS_NATIVE_EXIT(env, that, ScrollViewer_1SetHorizontalScrollBarVisibility_FUNC);
}
#endif

#ifndef NO_ScrollViewer_1SetVerticalScrollBarVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1SetVerticalScrollBarVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ScrollViewer_1SetVerticalScrollBarVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ScrollViewer_1SetVerticalScrollBarVisibility_FUNC);
	ScrollViewer::SetVerticalScrollBarVisibility((DependencyObject^)TO_OBJECT(arg0), (ScrollBarVisibility)arg1);
	OS_NATIVE_EXIT(env, that, ScrollViewer_1SetVerticalScrollBarVisibility_FUNC);
}
#endif

#ifndef NO_ScrollViewer_1VerticalOffset
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(ScrollViewer_1VerticalOffset)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(ScrollViewer_1VerticalOffset)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollViewer_1VerticalOffset_FUNC);
	rc = (jdouble)((ScrollViewer^)TO_OBJECT(arg0))->VerticalOffset;
	OS_NATIVE_EXIT(env, that, ScrollViewer_1VerticalOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollViewer_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ScrollViewer_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ScrollViewer_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollViewer_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ScrollViewer::typeid);
	OS_NATIVE_EXIT(env, that, ScrollViewer_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectionChangedEventArgs_1AddedItems
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SelectionChangedEventArgs_1AddedItems)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(SelectionChangedEventArgs_1AddedItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectionChangedEventArgs_1AddedItems_FUNC);
	rc = (jint)TO_HANDLE(((SelectionChangedEventArgs^)TO_OBJECT(arg0))->AddedItems);
	OS_NATIVE_EXIT(env, that, SelectionChangedEventArgs_1AddedItems_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectionChangedEventArgs_1RemovedItems
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SelectionChangedEventArgs_1RemovedItems)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(SelectionChangedEventArgs_1RemovedItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectionChangedEventArgs_1RemovedItems_FUNC);
	rc = (jint)TO_HANDLE(((SelectionChangedEventArgs^)TO_OBJECT(arg0))->RemovedItems);
	OS_NATIVE_EXIT(env, that, SelectionChangedEventArgs_1RemovedItems_FUNC);
	return rc;
}
#endif

#ifndef NO_Selector_1IsSynchronizedWithCurrentItem
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Selector_1IsSynchronizedWithCurrentItem)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Selector_1IsSynchronizedWithCurrentItem)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Selector_1IsSynchronizedWithCurrentItem_FUNC);
	((Selector^)TO_OBJECT(arg0))->IsSynchronizedWithCurrentItem = (arg1);
	OS_NATIVE_EXIT(env, that, Selector_1IsSynchronizedWithCurrentItem_FUNC);
}
#endif

#ifndef NO_Selector_1SelectedIndex__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedIndex__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedIndex__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Selector_1SelectedIndex__I_FUNC);
	rc = (jint)((Selector^)TO_OBJECT(arg0))->SelectedIndex;
	OS_NATIVE_EXIT(env, that, Selector_1SelectedIndex__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Selector_1SelectedIndex__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Selector_1SelectedIndex__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Selector_1SelectedIndex__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Selector_1SelectedIndex__II_FUNC);
	((Selector^)TO_OBJECT(arg0))->SelectedIndex = (arg1);
	OS_NATIVE_EXIT(env, that, Selector_1SelectedIndex__II_FUNC);
}
#endif

#ifndef NO_Selector_1SelectedItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedItem)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedItem)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Selector_1SelectedItem_FUNC);
	rc = (jint)TO_HANDLE(((Selector^)TO_OBJECT(arg0))->SelectedItem);
	OS_NATIVE_EXIT(env, that, Selector_1SelectedItem_FUNC);
	return rc;
}
#endif

#ifndef NO_Selector_1SelectedValue
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedValue)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Selector_1SelectedValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Selector_1SelectedValue_FUNC);
	rc = (jint)TO_HANDLE(((Selector^)TO_OBJECT(arg0))->SelectedValue);
	OS_NATIVE_EXIT(env, that, Selector_1SelectedValue_FUNC);
	return rc;
}
#endif

#ifndef NO_Selector_1SelectionChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Selector_1SelectionChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Selector_1SelectionChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Selector_1SelectionChanged_FUNC);
	((Selector^)TO_OBJECT(arg0))->SelectionChanged += ((SelectionChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Selector_1SelectionChanged_FUNC);
}
#endif

#ifndef NO_SetterBaseCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SetterBaseCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(SetterBaseCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetterBaseCollection_1Add_FUNC);
	((SetterBaseCollection^)TO_OBJECT(arg0))->Add((SetterBase^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, SetterBaseCollection_1Add_FUNC);
}
#endif

#ifndef NO_Shape_1Fill
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Shape_1Fill)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Shape_1Fill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Shape_1Fill_FUNC);
	((Shape^)TO_OBJECT(arg0))->Fill = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Shape_1Fill_FUNC);
}
#endif

#ifndef NO_Shape_1Stroke
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Shape_1Stroke)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Shape_1Stroke)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Shape_1Stroke_FUNC);
	((Shape^)TO_OBJECT(arg0))->Stroke = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Shape_1Stroke_FUNC);
}
#endif

#ifndef NO_Shape_1StrokeThickness
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Shape_1StrokeThickness)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Shape_1StrokeThickness)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Shape_1StrokeThickness_FUNC);
	((Shape^)TO_OBJECT(arg0))->StrokeThickness = (arg1);
	OS_NATIVE_EXIT(env, that, Shape_1StrokeThickness_FUNC);
}
#endif

#ifndef NO_SizeChangedEventArgs_1NewSize
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SizeChangedEventArgs_1NewSize)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(SizeChangedEventArgs_1NewSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SizeChangedEventArgs_1NewSize_FUNC);
	rc = (jint)TO_HANDLE(((SizeChangedEventArgs^)TO_OBJECT(arg0))->NewSize);
	OS_NATIVE_EXIT(env, that, SizeChangedEventArgs_1NewSize_FUNC);
	return rc;
}
#endif

#ifndef NO_SizeChangedEventArgs_1PreviousSize
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SizeChangedEventArgs_1PreviousSize)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(SizeChangedEventArgs_1PreviousSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SizeChangedEventArgs_1PreviousSize_FUNC);
	rc = (jint)TO_HANDLE(((SizeChangedEventArgs^)TO_OBJECT(arg0))->PreviousSize);
	OS_NATIVE_EXIT(env, that, SizeChangedEventArgs_1PreviousSize_FUNC);
	return rc;
}
#endif

#ifndef NO_Size_1Height__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Size_1Height__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Size_1Height__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Size_1Height__I_FUNC);
	rc = (jdouble)((Size ^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, Size_1Height__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Size_1Height__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Size_1Height__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Size_1Height__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Size_1Height__ID_FUNC);
	((Size ^)TO_OBJECT(arg0))->Height = (arg1);
	OS_NATIVE_EXIT(env, that, Size_1Height__ID_FUNC);
}
#endif

#ifndef NO_Size_1Width__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Size_1Width__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Size_1Width__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Size_1Width__I_FUNC);
	rc = (jdouble)((Size ^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, Size_1Width__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Size_1Width__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Size_1Width__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Size_1Width__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Size_1Width__ID_FUNC);
	((Size ^)TO_OBJECT(arg0))->Width = (arg1);
	OS_NATIVE_EXIT(env, that, Size_1Width__ID_FUNC);
}
#endif

#ifndef NO_Slider_1Orientation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Slider_1Orientation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Slider_1Orientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Slider_1Orientation_FUNC);
	((Slider ^)TO_OBJECT(arg0))->Orientation = ((Orientation)arg1);
	OS_NATIVE_EXIT(env, that, Slider_1Orientation_FUNC);
}
#endif

#ifndef NO_Slider_1TickFrequency
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Slider_1TickFrequency)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Slider_1TickFrequency)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Slider_1TickFrequency_FUNC);
	((Slider^)TO_OBJECT(arg0))->TickFrequency = (arg1);
	OS_NATIVE_EXIT(env, that, Slider_1TickFrequency_FUNC);
}
#endif

#ifndef NO_Slider_1TickPlacement
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Slider_1TickPlacement)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Slider_1TickPlacement)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Slider_1TickPlacement_FUNC);
	((Slider^)TO_OBJECT(arg0))->TickPlacement = ((TickPlacement)arg1);
	OS_NATIVE_EXIT(env, that, Slider_1TickPlacement_FUNC);
}
#endif

#ifndef NO_SplineDoubleKeyFrame_1KeySpline
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SplineDoubleKeyFrame_1KeySpline)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(SplineDoubleKeyFrame_1KeySpline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SplineDoubleKeyFrame_1KeySpline_FUNC);
	((SplineDoubleKeyFrame^)TO_OBJECT(arg0))->KeySpline = ((KeySpline^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, SplineDoubleKeyFrame_1KeySpline_FUNC);
}
#endif

#ifndef NO_SplineInt32KeyFrame_1KeySpline
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SplineInt32KeyFrame_1KeySpline)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(SplineInt32KeyFrame_1KeySpline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SplineInt32KeyFrame_1KeySpline_FUNC);
	((SplineInt32KeyFrame^)TO_OBJECT(arg0))->KeySpline = ((KeySpline^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, SplineInt32KeyFrame_1KeySpline_FUNC);
}
#endif

#ifndef NO_StackPanel_1Orientation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(StackPanel_1Orientation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(StackPanel_1Orientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, StackPanel_1Orientation_FUNC);
	((StackPanel^)TO_OBJECT(arg0))->Orientation = ((Orientation)arg1);
	OS_NATIVE_EXIT(env, that, StackPanel_1Orientation_FUNC);
}
#endif

#ifndef NO_StackPanel_1OrientationProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(StackPanel_1OrientationProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(StackPanel_1OrientationProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StackPanel_1OrientationProperty_FUNC);
	rc = (jint)TO_HANDLE(StackPanel::OrientationProperty);
	OS_NATIVE_EXIT(env, that, StackPanel_1OrientationProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_StackPanel_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(StackPanel_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(StackPanel_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StackPanel_1typeid_FUNC);
	rc = (jint)TO_HANDLE(StackPanel::typeid);
	OS_NATIVE_EXIT(env, that, StackPanel_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Storyboard_1Begin
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Begin)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Begin)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1Begin_FUNC);
	((Storyboard^)TO_OBJECT(arg0))->Begin((FrameworkElement^)TO_OBJECT(arg1), arg2);
	OS_NATIVE_EXIT(env, that, Storyboard_1Begin_FUNC);
}
#endif

#ifndef NO_Storyboard_1Pause
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Pause)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Pause)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1Pause_FUNC);
	((Storyboard^)TO_OBJECT(arg0))->Pause((FrameworkElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Storyboard_1Pause_FUNC);
}
#endif

#ifndef NO_Storyboard_1Resume
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Resume)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Resume)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1Resume_FUNC);
	((Storyboard^)TO_OBJECT(arg0))->Resume((FrameworkElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Storyboard_1Resume_FUNC);
}
#endif

#ifndef NO_Storyboard_1SetTargetName
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1SetTargetName)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1SetTargetName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1SetTargetName_FUNC);
	Storyboard::SetTargetName((DependencyObject^)TO_OBJECT(arg0), (String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Storyboard_1SetTargetName_FUNC);
}
#endif

#ifndef NO_Storyboard_1SetTargetProperty
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1SetTargetProperty)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1SetTargetProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1SetTargetProperty_FUNC);
	Storyboard::SetTargetProperty((DependencyObject^)TO_OBJECT(arg0), (PropertyPath^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Storyboard_1SetTargetProperty_FUNC);
}
#endif

#ifndef NO_Storyboard_1Stop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Stop)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Storyboard_1Stop)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Storyboard_1Stop_FUNC);
	((Storyboard^)TO_OBJECT(arg0))->Stop((FrameworkElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Storyboard_1Stop_FUNC);
}
#endif

#ifndef NO_StreamGeometryContext_1BeginFigure
extern "C" JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1BeginFigure)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3);
JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1BeginFigure)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, StreamGeometryContext_1BeginFigure_FUNC);
	((StreamGeometryContext^)TO_OBJECT(arg0))->BeginFigure((Point)TO_OBJECT(arg1), arg2, arg3);
	OS_NATIVE_EXIT(env, that, StreamGeometryContext_1BeginFigure_FUNC);
}
#endif

#ifndef NO_StreamGeometryContext_1Close
extern "C" JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1Close)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1Close)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, StreamGeometryContext_1Close_FUNC);
	((StreamGeometryContext^)TO_OBJECT(arg0))->Close();
	OS_NATIVE_EXIT(env, that, StreamGeometryContext_1Close_FUNC);
}
#endif

#ifndef NO_StreamGeometryContext_1LineTo
extern "C" JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1LineTo)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3);
JNIEXPORT void JNICALL OS_NATIVE(StreamGeometryContext_1LineTo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, StreamGeometryContext_1LineTo_FUNC);
	((StreamGeometryContext^)TO_OBJECT(arg0))->LineTo((Point)TO_OBJECT(arg1), arg2, arg3);
	OS_NATIVE_EXIT(env, that, StreamGeometryContext_1LineTo_FUNC);
}
#endif

#ifndef NO_StreamGeometry_1Open
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(StreamGeometry_1Open)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(StreamGeometry_1Open)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StreamGeometry_1Open_FUNC);
	rc = (jint)TO_HANDLE(((StreamGeometry^)TO_OBJECT(arg0))->Open());
	OS_NATIVE_EXIT(env, that, StreamGeometry_1Open_FUNC);
	return rc;
}
#endif

#ifndef NO_String_1Length
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(String_1Length)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(String_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, String_1Length_FUNC);
	rc = (jint)((String^)TO_OBJECT(arg0))->Length;
	OS_NATIVE_EXIT(env, that, String_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO_String_1ToCharArray
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(String_1ToCharArray)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(String_1ToCharArray)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, String_1ToCharArray_FUNC);
	rc = (jint)TO_HANDLE(((String^)TO_OBJECT(arg0))->ToCharArray());
	OS_NATIVE_EXIT(env, that, String_1ToCharArray_FUNC);
	return rc;
}
#endif

#ifndef NO_String_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(String_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(String_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, String_1typeid_FUNC);
	rc = (jint)TO_HANDLE(String::typeid);
	OS_NATIVE_EXIT(env, that, String_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_Style_1Setters
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Style_1Setters)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Style_1Setters)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Style_1Setters_FUNC);
	rc = (jint)TO_HANDLE(((Style^)TO_OBJECT(arg0))->Setters);
	OS_NATIVE_EXIT(env, that, Style_1Setters_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ActiveBorderBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveBorderBrush)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveBorderBrush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ActiveBorderBrush_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ActiveBorderBrush);
	OS_NATIVE_EXIT(env, that, SystemColors_1ActiveBorderBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ActiveBorderColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveBorderColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveBorderColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ActiveBorderColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ActiveBorderColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ActiveBorderColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ActiveCaptionColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveCaptionColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveCaptionColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ActiveCaptionColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ActiveCaptionColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ActiveCaptionColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ActiveCaptionTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveCaptionTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ActiveCaptionTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ActiveCaptionTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ActiveCaptionTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ActiveCaptionTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlBrush)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlBrush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlBrush_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlBrush);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlDarkColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlDarkColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlDarkColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlDarkColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlDarkColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlDarkColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlDarkDarkColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlDarkDarkColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlDarkDarkColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlDarkDarkColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlDarkDarkColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlDarkDarkColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlLightColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlLightColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlLightColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlLightColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlLightColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlLightColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlLightLightColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlLightLightColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlLightLightColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlLightLightColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlLightLightColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlLightLightColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlTextBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlTextBrush)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlTextBrush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlTextBrush_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlTextBrush);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlTextBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1ControlTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1ControlTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1ControlTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::ControlTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1ControlTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1GradientActiveCaptionColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1GradientActiveCaptionColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1GradientActiveCaptionColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1GradientActiveCaptionColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::GradientActiveCaptionColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1GradientActiveCaptionColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1GradientInactiveCaptionColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1GradientInactiveCaptionColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1GradientInactiveCaptionColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1GradientInactiveCaptionColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::GradientInactiveCaptionColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1GradientInactiveCaptionColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1HighlightBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightBrush)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightBrush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1HighlightBrush_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::HighlightBrush);
	OS_NATIVE_EXIT(env, that, SystemColors_1HighlightBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1HighlightColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1HighlightColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::HighlightColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1HighlightColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1HighlightTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1HighlightTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1HighlightTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::HighlightTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1HighlightTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1InactiveCaptionColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InactiveCaptionColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InactiveCaptionColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1InactiveCaptionColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::InactiveCaptionColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1InactiveCaptionColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1InactiveCaptionTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InactiveCaptionTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InactiveCaptionTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1InactiveCaptionTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::InactiveCaptionTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1InactiveCaptionTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1InfoColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InfoColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InfoColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1InfoColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::InfoColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1InfoColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1InfoTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InfoTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1InfoTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1InfoTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::InfoTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1InfoTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1WindowColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1WindowColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1WindowColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1WindowColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::WindowColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1WindowColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemColors_1WindowTextColor
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1WindowTextColor)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemColors_1WindowTextColor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemColors_1WindowTextColor_FUNC);
	rc = (jint)TO_HANDLE(SystemColors::WindowTextColor);
	OS_NATIVE_EXIT(env, that, SystemColors_1WindowTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemFonts_1MessageFontFamily
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontFamily)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontFamily)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemFonts_1MessageFontFamily_FUNC);
	rc = (jint)TO_HANDLE(SystemFonts::MessageFontFamily);
	OS_NATIVE_EXIT(env, that, SystemFonts_1MessageFontFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemFonts_1MessageFontSize
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemFonts_1MessageFontSize)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemFonts_1MessageFontSize)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemFonts_1MessageFontSize_FUNC);
	rc = (jdouble)SystemFonts::MessageFontSize;
	OS_NATIVE_EXIT(env, that, SystemFonts_1MessageFontSize_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemFonts_1MessageFontStyle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontStyle)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontStyle)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemFonts_1MessageFontStyle_FUNC);
	rc = (jint)TO_HANDLE(SystemFonts::MessageFontStyle);
	OS_NATIVE_EXIT(env, that, SystemFonts_1MessageFontStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemFonts_1MessageFontWeight
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontWeight)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemFonts_1MessageFontWeight)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemFonts_1MessageFontWeight_FUNC);
	rc = (jint)TO_HANDLE(SystemFonts::MessageFontWeight);
	OS_NATIVE_EXIT(env, that, SystemFonts_1MessageFontWeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1HighContrast
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParameters_1HighContrast)(JNIEnv *env, jclass that);
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParameters_1HighContrast)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1HighContrast_FUNC);
	rc = (jboolean)SystemParameters::HighContrast;
	OS_NATIVE_EXIT(env, that, SystemParameters_1HighContrast_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1HorizontalScrollBarButtonWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1HorizontalScrollBarButtonWidth)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1HorizontalScrollBarButtonWidth)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1HorizontalScrollBarButtonWidth_FUNC);
	rc = (jdouble)SystemParameters::HorizontalScrollBarButtonWidth;
	OS_NATIVE_EXIT(env, that, SystemParameters_1HorizontalScrollBarButtonWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1HorizontalScrollBarHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1HorizontalScrollBarHeight)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1HorizontalScrollBarHeight)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1HorizontalScrollBarHeight_FUNC);
	rc = (jdouble)SystemParameters::HorizontalScrollBarHeight;
	OS_NATIVE_EXIT(env, that, SystemParameters_1HorizontalScrollBarHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1MinimumHorizontalDragDistance
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1MinimumHorizontalDragDistance)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1MinimumHorizontalDragDistance)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1MinimumHorizontalDragDistance_FUNC);
	rc = (jdouble)SystemParameters::MinimumHorizontalDragDistance;
	OS_NATIVE_EXIT(env, that, SystemParameters_1MinimumHorizontalDragDistance_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1MinimumVerticalDragDistance
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1MinimumVerticalDragDistance)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1MinimumVerticalDragDistance)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1MinimumVerticalDragDistance_FUNC);
	rc = (jdouble)SystemParameters::MinimumVerticalDragDistance;
	OS_NATIVE_EXIT(env, that, SystemParameters_1MinimumVerticalDragDistance_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1PrimaryScreenHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1PrimaryScreenHeight)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1PrimaryScreenHeight)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1PrimaryScreenHeight_FUNC);
	rc = (jdouble)SystemParameters::PrimaryScreenHeight;
	OS_NATIVE_EXIT(env, that, SystemParameters_1PrimaryScreenHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1PrimaryScreenWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1PrimaryScreenWidth)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1PrimaryScreenWidth)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1PrimaryScreenWidth_FUNC);
	rc = (jdouble)SystemParameters::PrimaryScreenWidth;
	OS_NATIVE_EXIT(env, that, SystemParameters_1PrimaryScreenWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1ThinHorizontalBorderHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1ThinHorizontalBorderHeight)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1ThinHorizontalBorderHeight)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1ThinHorizontalBorderHeight_FUNC);
	rc = (jdouble)SystemParameters::ThinHorizontalBorderHeight;
	OS_NATIVE_EXIT(env, that, SystemParameters_1ThinHorizontalBorderHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1ThinVerticalBorderWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1ThinVerticalBorderWidth)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1ThinVerticalBorderWidth)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1ThinVerticalBorderWidth_FUNC);
	rc = (jdouble)SystemParameters::ThinVerticalBorderWidth;
	OS_NATIVE_EXIT(env, that, SystemParameters_1ThinVerticalBorderWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VerticalScrollBarButtonHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VerticalScrollBarButtonHeight)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VerticalScrollBarButtonHeight)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VerticalScrollBarButtonHeight_FUNC);
	rc = (jdouble)SystemParameters::VerticalScrollBarButtonHeight;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VerticalScrollBarButtonHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VerticalScrollBarWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VerticalScrollBarWidth)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VerticalScrollBarWidth)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VerticalScrollBarWidth_FUNC);
	rc = (jdouble)SystemParameters::VerticalScrollBarWidth;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VerticalScrollBarWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VirtualScreenHeight
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenHeight)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenHeight)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VirtualScreenHeight_FUNC);
	rc = (jdouble)SystemParameters::VirtualScreenHeight;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VirtualScreenHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VirtualScreenLeft
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenLeft)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenLeft)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VirtualScreenLeft_FUNC);
	rc = (jdouble)SystemParameters::VirtualScreenLeft;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VirtualScreenLeft_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VirtualScreenTop
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenTop)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenTop)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VirtualScreenTop_FUNC);
	rc = (jdouble)SystemParameters::VirtualScreenTop;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VirtualScreenTop_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1VirtualScreenWidth
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenWidth)(JNIEnv *env, jclass that);
JNIEXPORT jdouble JNICALL OS_NATIVE(SystemParameters_1VirtualScreenWidth)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1VirtualScreenWidth_FUNC);
	rc = (jdouble)SystemParameters::VirtualScreenWidth;
	OS_NATIVE_EXIT(env, that, SystemParameters_1VirtualScreenWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1WheelScrollLines
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemParameters_1WheelScrollLines)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemParameters_1WheelScrollLines)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1WheelScrollLines_FUNC);
	rc = (jint)SystemParameters::WheelScrollLines;
	OS_NATIVE_EXIT(env, that, SystemParameters_1WheelScrollLines_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParameters_1WorkArea
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SystemParameters_1WorkArea)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SystemParameters_1WorkArea)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParameters_1WorkArea_FUNC);
	rc = (jint)TO_HANDLE(SystemParameters::WorkArea);
	OS_NATIVE_EXIT(env, that, SystemParameters_1WorkArea_FUNC);
	return rc;
}
#endif

#ifndef NO_TabControl_1TabStripPlacement
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TabControl_1TabStripPlacement)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TabControl_1TabStripPlacement)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TabControl_1TabStripPlacement_FUNC);
	((TabControl^)TO_OBJECT(arg0))->TabStripPlacement = ((Dock)arg1);
	OS_NATIVE_EXIT(env, that, TabControl_1TabStripPlacement_FUNC);
}
#endif

#ifndef NO_TabItem_1IsSelected
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(TabItem_1IsSelected)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(TabItem_1IsSelected)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TabItem_1IsSelected_FUNC);
	rc = (jboolean)((TabItem^)TO_OBJECT(arg0))->IsSelected;
	OS_NATIVE_EXIT(env, that, TabItem_1IsSelected_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1Background
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Background)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Background)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1Background_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->Background = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1Background_FUNC);
}
#endif

#ifndef NO_TextBlock_1BackgroundProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1BackgroundProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1BackgroundProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1BackgroundProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::BackgroundProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1BackgroundProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1FontFamily
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontFamily)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontFamily)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1FontFamily_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->FontFamily = ((FontFamily^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1FontFamily_FUNC);
}
#endif

#ifndef NO_TextBlock_1FontFamilyProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontFamilyProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontFamilyProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1FontFamilyProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::FontFamilyProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontFamilyProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1FontSize
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontSize)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontSize)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1FontSize_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->FontSize = (arg1);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontSize_FUNC);
}
#endif

#ifndef NO_TextBlock_1FontSizeProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontSizeProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontSizeProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1FontSizeProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::FontSizeProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontSizeProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1FontStretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontStretch)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontStretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1FontStretch_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->FontStretch = ((FontStretch)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1FontStretch_FUNC);
}
#endif

#ifndef NO_TextBlock_1FontStretchProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontStretchProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontStretchProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1FontStretchProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::FontStretchProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontStretchProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1FontStyle
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontStyle)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1FontStyle_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->FontStyle = ((FontStyle)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1FontStyle_FUNC);
}
#endif

#ifndef NO_TextBlock_1FontStyleProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontStyleProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontStyleProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1FontStyleProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::FontStyleProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontStyleProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1FontWeight
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontWeight)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1FontWeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1FontWeight_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->FontWeight = ((FontWeight)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1FontWeight_FUNC);
}
#endif

#ifndef NO_TextBlock_1FontWeightProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontWeightProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1FontWeightProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1FontWeightProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::FontWeightProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1FontWeightProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1Foreground
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Foreground)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Foreground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1Foreground_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->Foreground = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1Foreground_FUNC);
}
#endif

#ifndef NO_TextBlock_1ForegroundProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1ForegroundProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1ForegroundProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1ForegroundProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::ForegroundProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1ForegroundProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1Inlines
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1Inlines)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1Inlines)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1Inlines_FUNC);
	rc = (jint)TO_HANDLE(((TextBlock^)TO_OBJECT(arg0))->Inlines);
	OS_NATIVE_EXIT(env, that, TextBlock_1Inlines_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1Text__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1Text__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1Text__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1Text__I_FUNC);
	rc = (jint)TO_HANDLE(((TextBlock^)TO_OBJECT(arg0))->Text);
	OS_NATIVE_EXIT(env, that, TextBlock_1Text__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1Text__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Text__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBlock_1Text__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBlock_1Text__II_FUNC);
	((TextBlock^)TO_OBJECT(arg0))->Text = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBlock_1Text__II_FUNC);
}
#endif

#ifndef NO_TextBlock_1TextProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1TextProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1TextProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1TextProperty_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::TextProperty);
	OS_NATIVE_EXIT(env, that, TextBlock_1TextProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBlock_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextBlock_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBlock_1typeid_FUNC);
	rc = (jint)TO_HANDLE(TextBlock::typeid);
	OS_NATIVE_EXIT(env, that, TextBlock_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBoundsCollection_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBoundsCollection_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBoundsCollection_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBoundsCollection_1Current_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerator^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, TextBoundsCollection_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBoundsCollection_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBoundsCollection_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBoundsCollection_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBoundsCollection_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerable^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, TextBoundsCollection_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBounds_1Rectangle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBounds_1Rectangle)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBounds_1Rectangle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBounds_1Rectangle_FUNC);
	rc = (jint)TO_HANDLE(((TextBounds^)TO_OBJECT(arg0))->Rectangle);
	OS_NATIVE_EXIT(env, that, TextBounds_1Rectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBoxBase_1AcceptsReturn
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AcceptsReturn)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AcceptsReturn)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1AcceptsReturn_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->AcceptsReturn = (arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1AcceptsReturn_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1AcceptsTab
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AcceptsTab)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AcceptsTab)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1AcceptsTab_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->AcceptsTab = (arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1AcceptsTab_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1AppendText
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AppendText)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1AppendText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1AppendText_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->AppendText((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBoxBase_1AppendText_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1Copy
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Copy)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Copy)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1Copy_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->Copy();
	OS_NATIVE_EXIT(env, that, TextBoxBase_1Copy_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1Cut
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Cut)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Cut)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1Cut_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->Cut();
	OS_NATIVE_EXIT(env, that, TextBoxBase_1Cut_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1HorizontalScrollBarVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1HorizontalScrollBarVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1HorizontalScrollBarVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1HorizontalScrollBarVisibility_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->HorizontalScrollBarVisibility = ((ScrollBarVisibility)arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1HorizontalScrollBarVisibility_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1IsReadOnly__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(TextBoxBase_1IsReadOnly__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(TextBoxBase_1IsReadOnly__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TextBoxBase_1IsReadOnly__I_FUNC);
	rc = (jboolean)((TextBoxBase^)TO_OBJECT(arg0))->IsReadOnly;
	OS_NATIVE_EXIT(env, that, TextBoxBase_1IsReadOnly__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBoxBase_1IsReadOnly__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1IsReadOnly__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1IsReadOnly__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1IsReadOnly__IZ_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->IsReadOnly = (arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1IsReadOnly__IZ_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1Paste
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Paste)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1Paste)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1Paste_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->Paste();
	OS_NATIVE_EXIT(env, that, TextBoxBase_1Paste_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1ScrollToEnd
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1ScrollToEnd)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1ScrollToEnd)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1ScrollToEnd_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->ScrollToEnd();
	OS_NATIVE_EXIT(env, that, TextBoxBase_1ScrollToEnd_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1ScrollToVerticalOffset
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1ScrollToVerticalOffset)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1ScrollToVerticalOffset)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1ScrollToVerticalOffset_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->ScrollToVerticalOffset(arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1ScrollToVerticalOffset_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1SelectAll
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1SelectAll)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1SelectAll)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1SelectAll_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->SelectAll();
	OS_NATIVE_EXIT(env, that, TextBoxBase_1SelectAll_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1TextChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1TextChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1TextChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1TextChanged_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->TextChanged += ((TextChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBoxBase_1TextChanged_FUNC);
}
#endif

#ifndef NO_TextBoxBase_1VerticalOffset
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextBoxBase_1VerticalOffset)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextBoxBase_1VerticalOffset)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextBoxBase_1VerticalOffset_FUNC);
	rc = (jdouble)((TextBoxBase^)TO_OBJECT(arg0))->VerticalOffset;
	OS_NATIVE_EXIT(env, that, TextBoxBase_1VerticalOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBoxBase_1VerticalScrollBarVisibility
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1VerticalScrollBarVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBoxBase_1VerticalScrollBarVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBoxBase_1VerticalScrollBarVisibility_FUNC);
	((TextBoxBase^)TO_OBJECT(arg0))->VerticalScrollBarVisibility = ((ScrollBarVisibility)arg1);
	OS_NATIVE_EXIT(env, that, TextBoxBase_1VerticalScrollBarVisibility_FUNC);
}
#endif

#ifndef NO_TextBox_1CaretIndex__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1CaretIndex__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1CaretIndex__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1CaretIndex__I_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->CaretIndex;
	OS_NATIVE_EXIT(env, that, TextBox_1CaretIndex__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1CaretIndex__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1CaretIndex__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1CaretIndex__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1CaretIndex__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->CaretIndex = (arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1CaretIndex__II_FUNC);
}
#endif

#ifndef NO_TextBox_1GetFirstVisibleLineIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetFirstVisibleLineIndex)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetFirstVisibleLineIndex)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1GetFirstVisibleLineIndex_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->GetFirstVisibleLineIndex();
	OS_NATIVE_EXIT(env, that, TextBox_1GetFirstVisibleLineIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1GetLineIndexFromCharacterIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetLineIndexFromCharacterIndex)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetLineIndexFromCharacterIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1GetLineIndexFromCharacterIndex_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->GetLineIndexFromCharacterIndex(arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1GetLineIndexFromCharacterIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1GetRectFromCharacterIndex
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetRectFromCharacterIndex)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1GetRectFromCharacterIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1GetRectFromCharacterIndex_FUNC);
	rc = (jint)TO_HANDLE(((TextBox^)TO_OBJECT(arg0))->GetRectFromCharacterIndex(arg1));
	OS_NATIVE_EXIT(env, that, TextBox_1GetRectFromCharacterIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1LineCount
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1LineCount)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1LineCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1LineCount_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->LineCount;
	OS_NATIVE_EXIT(env, that, TextBox_1LineCount_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1MaxLength__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1MaxLength__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1MaxLength__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1MaxLength__I_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->MaxLength;
	OS_NATIVE_EXIT(env, that, TextBox_1MaxLength__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1MaxLength__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1MaxLength__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1MaxLength__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1MaxLength__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->MaxLength = (arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1MaxLength__II_FUNC);
}
#endif

#ifndef NO_TextBox_1ScrollToLine
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1ScrollToLine)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1ScrollToLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1ScrollToLine_FUNC);
	((TextBox^)TO_OBJECT(arg0))->ScrollToLine(arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1ScrollToLine_FUNC);
}
#endif

#ifndef NO_TextBox_1Select
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1Select)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1Select)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, TextBox_1Select_FUNC);
	((TextBox^)TO_OBJECT(arg0))->Select(arg1, arg2);
	OS_NATIVE_EXIT(env, that, TextBox_1Select_FUNC);
}
#endif

#ifndef NO_TextBox_1SelectedText__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectedText__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectedText__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1SelectedText__I_FUNC);
	rc = (jint)TO_HANDLE(((TextBox^)TO_OBJECT(arg0))->SelectedText);
	OS_NATIVE_EXIT(env, that, TextBox_1SelectedText__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1SelectedText__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectedText__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectedText__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1SelectedText__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->SelectedText = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBox_1SelectedText__II_FUNC);
}
#endif

#ifndef NO_TextBox_1SelectionLength__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectionLength__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectionLength__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1SelectionLength__I_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->SelectionLength;
	OS_NATIVE_EXIT(env, that, TextBox_1SelectionLength__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1SelectionLength__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectionLength__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectionLength__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1SelectionLength__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->SelectionLength = (arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1SelectionLength__II_FUNC);
}
#endif

#ifndef NO_TextBox_1SelectionStart__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectionStart__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1SelectionStart__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1SelectionStart__I_FUNC);
	rc = (jint)((TextBox^)TO_OBJECT(arg0))->SelectionStart;
	OS_NATIVE_EXIT(env, that, TextBox_1SelectionStart__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1SelectionStart__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectionStart__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1SelectionStart__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1SelectionStart__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->SelectionStart = (arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1SelectionStart__II_FUNC);
}
#endif

#ifndef NO_TextBox_1Text__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1Text__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextBox_1Text__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextBox_1Text__I_FUNC);
	rc = (jint)TO_HANDLE(((TextBox^)TO_OBJECT(arg0))->Text);
	OS_NATIVE_EXIT(env, that, TextBox_1Text__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TextBox_1Text__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1Text__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1Text__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1Text__II_FUNC);
	((TextBox^)TO_OBJECT(arg0))->Text = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextBox_1Text__II_FUNC);
}
#endif

#ifndef NO_TextBox_1TextWrapping
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextBox_1TextWrapping)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextBox_1TextWrapping)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextBox_1TextWrapping_FUNC);
	((TextBox^)TO_OBJECT(arg0))->TextWrapping = ((TextWrapping)arg1);
	OS_NATIVE_EXIT(env, that, TextBox_1TextWrapping_FUNC);
}
#endif

#ifndef NO_TextCompositionEventArgs_1ControlText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1ControlText)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1ControlText)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextCompositionEventArgs_1ControlText_FUNC);
	rc = (jint)TO_HANDLE(((TextCompositionEventArgs^)TO_OBJECT(arg0))->ControlText);
	OS_NATIVE_EXIT(env, that, TextCompositionEventArgs_1ControlText_FUNC);
	return rc;
}
#endif

#ifndef NO_TextCompositionEventArgs_1Handled
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextCompositionEventArgs_1Handled)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextCompositionEventArgs_1Handled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TextCompositionEventArgs_1Handled_FUNC);
	((TextCompositionEventArgs^)TO_OBJECT(arg0))->Handled = (arg1);
	OS_NATIVE_EXIT(env, that, TextCompositionEventArgs_1Handled_FUNC);
}
#endif

#ifndef NO_TextCompositionEventArgs_1SystemText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1SystemText)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1SystemText)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextCompositionEventArgs_1SystemText_FUNC);
	rc = (jint)TO_HANDLE(((TextCompositionEventArgs^)TO_OBJECT(arg0))->SystemText);
	OS_NATIVE_EXIT(env, that, TextCompositionEventArgs_1SystemText_FUNC);
	return rc;
}
#endif

#ifndef NO_TextCompositionEventArgs_1Text
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1Text)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextCompositionEventArgs_1Text)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextCompositionEventArgs_1Text_FUNC);
	rc = (jint)TO_HANDLE(((TextCompositionEventArgs^)TO_OBJECT(arg0))->Text);
	OS_NATIVE_EXIT(env, that, TextCompositionEventArgs_1Text_FUNC);
	return rc;
}
#endif

#ifndef NO_TextDecorationCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextDecorationCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextDecorationCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextDecorationCollection_1Add_FUNC);
	((TextDecorationCollection^)TO_OBJECT(arg0))->Add((TextDecoration^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextDecorationCollection_1Add_FUNC);
}
#endif

#ifndef NO_TextDecorations_1Strikethrough
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextDecorations_1Strikethrough)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextDecorations_1Strikethrough)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextDecorations_1Strikethrough_FUNC);
	rc = (jint)TO_HANDLE(TextDecorations::Strikethrough);
	OS_NATIVE_EXIT(env, that, TextDecorations_1Strikethrough_FUNC);
	return rc;
}
#endif

#ifndef NO_TextDecorations_1Underline
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextDecorations_1Underline)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextDecorations_1Underline)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextDecorations_1Underline_FUNC);
	rc = (jint)TO_HANDLE(TextDecorations::Underline);
	OS_NATIVE_EXIT(env, that, TextDecorations_1Underline_FUNC);
	return rc;
}
#endif

#ifndef NO_TextFormatter_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextFormatter_1Create)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TextFormatter_1Create)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextFormatter_1Create_FUNC);
	rc = (jint)TO_HANDLE(TextFormatter::Create());
	OS_NATIVE_EXIT(env, that, TextFormatter_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_TextFormatter_1FormatLine
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextFormatter_1FormatLine)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL OS_NATIVE(TextFormatter_1FormatLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextFormatter_1FormatLine_FUNC);
	rc = (jint)TO_HANDLE(((TextFormatter^)TO_OBJECT(arg0))->FormatLine((TextSource^)TO_OBJECT(arg1), arg2, arg3, (TextParagraphProperties^)TO_OBJECT(arg4), (TextLineBreak^)TO_OBJECT(arg5)));
	OS_NATIVE_EXIT(env, that, TextFormatter_1FormatLine_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Baseline
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Baseline)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Baseline)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1Baseline_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->Baseline;
	OS_NATIVE_EXIT(env, that, TextLine_1Baseline_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Draw
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextLine_1Draw)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT void JNICALL OS_NATIVE(TextLine_1Draw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, TextLine_1Draw_FUNC);
	((TextLine^)TO_OBJECT(arg0))->Draw((DrawingContext^)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2), (InvertAxes)arg3);
	OS_NATIVE_EXIT(env, that, TextLine_1Draw_FUNC);
}
#endif

#ifndef NO_TextLine_1GetCharacterHitFromDistance
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetCharacterHitFromDistance)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetCharacterHitFromDistance)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetCharacterHitFromDistance_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetCharacterHitFromDistance(arg1));
	OS_NATIVE_EXIT(env, that, TextLine_1GetCharacterHitFromDistance_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetDistanceFromCharacterHit
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1GetDistanceFromCharacterHit)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1GetDistanceFromCharacterHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetDistanceFromCharacterHit_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->GetDistanceFromCharacterHit((CharacterHit)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextLine_1GetDistanceFromCharacterHit_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetIndexedGlyphRuns
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetIndexedGlyphRuns)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetIndexedGlyphRuns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetIndexedGlyphRuns_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetIndexedGlyphRuns());
	OS_NATIVE_EXIT(env, that, TextLine_1GetIndexedGlyphRuns_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetNextCaretCharacterHit
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetNextCaretCharacterHit)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetNextCaretCharacterHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetNextCaretCharacterHit_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetNextCaretCharacterHit((CharacterHit)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, TextLine_1GetNextCaretCharacterHit_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetPreviousCaretCharacterHit
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetPreviousCaretCharacterHit)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetPreviousCaretCharacterHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetPreviousCaretCharacterHit_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetPreviousCaretCharacterHit((CharacterHit)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, TextLine_1GetPreviousCaretCharacterHit_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetTextBounds
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetTextBounds)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetTextBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetTextBounds_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetTextBounds(arg1, arg2));
	OS_NATIVE_EXIT(env, that, TextLine_1GetTextBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1GetTextLineBreak
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetTextLineBreak)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1GetTextLineBreak)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1GetTextLineBreak_FUNC);
	rc = (jint)TO_HANDLE(((TextLine^)TO_OBJECT(arg0))->GetTextLineBreak());
	OS_NATIVE_EXIT(env, that, TextLine_1GetTextLineBreak_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Height
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Height)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1Height_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->Height;
	OS_NATIVE_EXIT(env, that, TextLine_1Height_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Length
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1Length)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1Length_FUNC);
	rc = (jint)((TextLine^)TO_OBJECT(arg0))->Length;
	OS_NATIVE_EXIT(env, that, TextLine_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1NewlineLength
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1NewlineLength)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TextLine_1NewlineLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1NewlineLength_FUNC);
	rc = (jint)((TextLine^)TO_OBJECT(arg0))->NewlineLength;
	OS_NATIVE_EXIT(env, that, TextLine_1NewlineLength_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Start
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Start)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Start)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1Start_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->Start;
	OS_NATIVE_EXIT(env, that, TextLine_1Start_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1Width
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Width)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1Width)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1Width_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->Width;
	OS_NATIVE_EXIT(env, that, TextLine_1Width_FUNC);
	return rc;
}
#endif

#ifndef NO_TextLine_1WidthIncludingTrailingWhitespace
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1WidthIncludingTrailingWhitespace)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TextLine_1WidthIncludingTrailingWhitespace)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TextLine_1WidthIncludingTrailingWhitespace_FUNC);
	rc = (jdouble)((TextLine^)TO_OBJECT(arg0))->WidthIncludingTrailingWhitespace;
	OS_NATIVE_EXIT(env, that, TextLine_1WidthIncludingTrailingWhitespace_FUNC);
	return rc;
}
#endif

#ifndef NO_TextTabPropertiesCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TextTabPropertiesCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TextTabPropertiesCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TextTabPropertiesCollection_1Add_FUNC);
	((System::Collections::Generic::IList<TextTabProperties^>^)TO_OBJECT(arg0))->Add((TextTabProperties^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TextTabPropertiesCollection_1Add_FUNC);
}
#endif

#ifndef NO_Thickness_1Bottom
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Bottom)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Bottom)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Thickness_1Bottom_FUNC);
	rc = (jdouble)((Thickness^)TO_OBJECT(arg0))->Bottom;
	OS_NATIVE_EXIT(env, that, Thickness_1Bottom_FUNC);
	return rc;
}
#endif

#ifndef NO_Thickness_1Left
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Left)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Left)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Thickness_1Left_FUNC);
	rc = (jdouble)((Thickness^)TO_OBJECT(arg0))->Left;
	OS_NATIVE_EXIT(env, that, Thickness_1Left_FUNC);
	return rc;
}
#endif

#ifndef NO_Thickness_1Right
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Right)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Right)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Thickness_1Right_FUNC);
	rc = (jdouble)((Thickness^)TO_OBJECT(arg0))->Right;
	OS_NATIVE_EXIT(env, that, Thickness_1Right_FUNC);
	return rc;
}
#endif

#ifndef NO_Thickness_1Top
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Top)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Thickness_1Top)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Thickness_1Top_FUNC);
	rc = (jdouble)((Thickness^)TO_OBJECT(arg0))->Top;
	OS_NATIVE_EXIT(env, that, Thickness_1Top_FUNC);
	return rc;
}
#endif

#ifndef NO_Thumb_1DragDeltaEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Thumb_1DragDeltaEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(Thumb_1DragDeltaEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Thumb_1DragDeltaEvent_FUNC);
	rc = (jint)TO_HANDLE(Thumb::DragDeltaEvent);
	OS_NATIVE_EXIT(env, that, Thumb_1DragDeltaEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_TileBrush_1AlignmentX
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1AlignmentX)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1AlignmentX)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1AlignmentX_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->AlignmentX = ((AlignmentX)arg1);
	OS_NATIVE_EXIT(env, that, TileBrush_1AlignmentX_FUNC);
}
#endif

#ifndef NO_TileBrush_1AlignmentY
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1AlignmentY)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1AlignmentY)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1AlignmentY_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->AlignmentY = ((AlignmentY)arg1);
	OS_NATIVE_EXIT(env, that, TileBrush_1AlignmentY_FUNC);
}
#endif

#ifndef NO_TileBrush_1Stretch
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1Stretch)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1Stretch)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1Stretch_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->Stretch = ((Stretch)arg1);
	OS_NATIVE_EXIT(env, that, TileBrush_1Stretch_FUNC);
}
#endif

#ifndef NO_TileBrush_1TileMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1TileMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1TileMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1TileMode_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->TileMode = ((TileMode)arg1);
	OS_NATIVE_EXIT(env, that, TileBrush_1TileMode_FUNC);
}
#endif

#ifndef NO_TileBrush_1Viewport
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1Viewport)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1Viewport)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1Viewport_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->Viewport = ((Rect)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TileBrush_1Viewport_FUNC);
}
#endif

#ifndef NO_TileBrush_1ViewportUnits
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1ViewportUnits)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TileBrush_1ViewportUnits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TileBrush_1ViewportUnits_FUNC);
	((TileBrush^)TO_OBJECT(arg0))->ViewportUnits = ((BrushMappingMode)arg1);
	OS_NATIVE_EXIT(env, that, TileBrush_1ViewportUnits_FUNC);
}
#endif

#ifndef NO_TimeSpan_1FromMilliseconds
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TimeSpan_1FromMilliseconds)(JNIEnv *env, jclass that, jdouble arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TimeSpan_1FromMilliseconds)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TimeSpan_1FromMilliseconds_FUNC);
	rc = (jint)TO_HANDLE(TimeSpan::FromMilliseconds(arg0));
	OS_NATIVE_EXIT(env, that, TimeSpan_1FromMilliseconds_FUNC);
	return rc;
}
#endif

#ifndef NO_TimeSpan_1TotalMilliseconds
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(TimeSpan_1TotalMilliseconds)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(TimeSpan_1TotalMilliseconds)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, TimeSpan_1TotalMilliseconds_FUNC);
	rc = (jdouble)((TimeSpan^)TO_OBJECT(arg0))->TotalMilliseconds;
	OS_NATIVE_EXIT(env, that, TimeSpan_1TotalMilliseconds_FUNC);
	return rc;
}
#endif

#ifndef NO_TimelineGroup_1Children
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TimelineGroup_1Children)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TimelineGroup_1Children)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TimelineGroup_1Children_FUNC);
	rc = (jint)TO_HANDLE(((TimelineGroup^)TO_OBJECT(arg0))->Children);
	OS_NATIVE_EXIT(env, that, TimelineGroup_1Children_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1AccelerationRatio__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Timeline_1AccelerationRatio__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Timeline_1AccelerationRatio__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Timeline_1AccelerationRatio__I_FUNC);
	rc = (jdouble)((Timeline^)TO_OBJECT(arg0))->AccelerationRatio;
	OS_NATIVE_EXIT(env, that, Timeline_1AccelerationRatio__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1AccelerationRatio__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1AccelerationRatio__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1AccelerationRatio__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1AccelerationRatio__ID_FUNC);
	((Timeline^)TO_OBJECT(arg0))->AccelerationRatio = (arg1);
	OS_NATIVE_EXIT(env, that, Timeline_1AccelerationRatio__ID_FUNC);
}
#endif

#ifndef NO_Timeline_1AutoReverse
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1AutoReverse)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1AutoReverse)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1AutoReverse_FUNC);
	((Timeline^)TO_OBJECT(arg0))->AutoReverse = (arg1);
	OS_NATIVE_EXIT(env, that, Timeline_1AutoReverse_FUNC);
}
#endif

#ifndef NO_Timeline_1BeginTime__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Timeline_1BeginTime__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Timeline_1BeginTime__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Timeline_1BeginTime__I_FUNC);
	rc = (jint)TO_HANDLE(((Timeline^)TO_OBJECT(arg0))->BeginTime);
	OS_NATIVE_EXIT(env, that, Timeline_1BeginTime__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1Completed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1Completed)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1Completed)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1Completed_FUNC);
	((Timeline^)TO_OBJECT(arg0))->Completed += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Timeline_1Completed_FUNC);
}
#endif

#ifndef NO_Timeline_1DecelerationRatio__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Timeline_1DecelerationRatio__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Timeline_1DecelerationRatio__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Timeline_1DecelerationRatio__I_FUNC);
	rc = (jdouble)((Timeline^)TO_OBJECT(arg0))->DecelerationRatio;
	OS_NATIVE_EXIT(env, that, Timeline_1DecelerationRatio__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1DecelerationRatio__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1DecelerationRatio__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1DecelerationRatio__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1DecelerationRatio__ID_FUNC);
	((Timeline^)TO_OBJECT(arg0))->DecelerationRatio = (arg1);
	OS_NATIVE_EXIT(env, that, Timeline_1DecelerationRatio__ID_FUNC);
}
#endif

#ifndef NO_Timeline_1Duration__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Timeline_1Duration__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Timeline_1Duration__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Timeline_1Duration__I_FUNC);
	rc = (jint)TO_HANDLE(((Timeline^)TO_OBJECT(arg0))->Duration);
	OS_NATIVE_EXIT(env, that, Timeline_1Duration__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1Duration__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1Duration__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1Duration__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1Duration__II_FUNC);
	((Timeline^)TO_OBJECT(arg0))->Duration = ((Duration)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Timeline_1Duration__II_FUNC);
}
#endif

#ifndef NO_Timeline_1RepeatBehavior
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1RepeatBehavior)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1RepeatBehavior)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1RepeatBehavior_FUNC);
	((Timeline^)TO_OBJECT(arg0))->RepeatBehavior = ((RepeatBehavior)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Timeline_1RepeatBehavior_FUNC);
}
#endif

#ifndef NO_ToggleButton_1Checked
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1Checked)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1Checked)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToggleButton_1Checked_FUNC);
	((ToggleButton^)TO_OBJECT(arg0))->Checked += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ToggleButton_1Checked_FUNC);
}
#endif

#ifndef NO_ToggleButton_1CheckedEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1CheckedEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1CheckedEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1CheckedEvent_FUNC);
	rc = (jint)TO_HANDLE(ToggleButton::CheckedEvent);
	OS_NATIVE_EXIT(env, that, ToggleButton_1CheckedEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_ToggleButton_1IndeterminateEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IndeterminateEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IndeterminateEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1IndeterminateEvent_FUNC);
	rc = (jint)TO_HANDLE(ToggleButton::IndeterminateEvent);
	OS_NATIVE_EXIT(env, that, ToggleButton_1IndeterminateEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_ToggleButton_1IsChecked__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ToggleButton_1IsChecked__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ToggleButton_1IsChecked__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1IsChecked__I_FUNC);
	rc = (jboolean)((ToggleButton ^)TO_OBJECT(arg0))->IsChecked;
	OS_NATIVE_EXIT(env, that, ToggleButton_1IsChecked__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ToggleButton_1IsChecked__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1IsChecked__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1IsChecked__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ToggleButton_1IsChecked__IZ_FUNC);
	((ToggleButton ^)TO_OBJECT(arg0))->IsChecked = (arg1);
	OS_NATIVE_EXIT(env, that, ToggleButton_1IsChecked__IZ_FUNC);
}
#endif

#ifndef NO_ToggleButton_1IsCheckedProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IsCheckedProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IsCheckedProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1IsCheckedProperty_FUNC);
	rc = (jint)TO_HANDLE(ToggleButton::IsCheckedProperty);
	OS_NATIVE_EXIT(env, that, ToggleButton_1IsCheckedProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_ToggleButton_1IsThreeStateProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IsThreeStateProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1IsThreeStateProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1IsThreeStateProperty_FUNC);
	rc = (jint)TO_HANDLE(ToggleButton::IsThreeStateProperty);
	OS_NATIVE_EXIT(env, that, ToggleButton_1IsThreeStateProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_ToggleButton_1Unchecked
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1Unchecked)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1Unchecked)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToggleButton_1Unchecked_FUNC);
	((ToggleButton^)TO_OBJECT(arg0))->Unchecked += ((RoutedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ToggleButton_1Unchecked_FUNC);
}
#endif

#ifndef NO_ToggleButton_1UncheckedEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1UncheckedEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToggleButton_1UncheckedEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToggleButton_1UncheckedEvent_FUNC);
	rc = (jint)TO_HANDLE(ToggleButton::UncheckedEvent);
	OS_NATIVE_EXIT(env, that, ToggleButton_1UncheckedEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBarTray_1Background
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1Background)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1Background)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBarTray_1Background_FUNC);
	((ToolBarTray^)TO_OBJECT(arg0))->Background = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, ToolBarTray_1Background_FUNC);
}
#endif

#ifndef NO_ToolBarTray_1IsLocked__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ToolBarTray_1IsLocked__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ToolBarTray_1IsLocked__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBarTray_1IsLocked__I_FUNC);
	rc = (jboolean)((ToolBarTray^)TO_OBJECT(arg0))->IsLocked;
	OS_NATIVE_EXIT(env, that, ToolBarTray_1IsLocked__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBarTray_1IsLocked__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1IsLocked__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1IsLocked__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBarTray_1IsLocked__IZ_FUNC);
	((ToolBarTray^)TO_OBJECT(arg0))->IsLocked = (arg1);
	OS_NATIVE_EXIT(env, that, ToolBarTray_1IsLocked__IZ_FUNC);
}
#endif

#ifndef NO_ToolBarTray_1Orientation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1Orientation)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBarTray_1Orientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBarTray_1Orientation_FUNC);
	((ToolBarTray^)TO_OBJECT(arg0))->Orientation = ((Orientation)arg1);
	OS_NATIVE_EXIT(env, that, ToolBarTray_1Orientation_FUNC);
}
#endif

#ifndef NO_ToolBarTray_1ToolBars
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBarTray_1ToolBars)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBarTray_1ToolBars)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBarTray_1ToolBars_FUNC);
	rc = (jint)TO_HANDLE(((ToolBarTray^)TO_OBJECT(arg0))->ToolBars);
	OS_NATIVE_EXIT(env, that, ToolBarTray_1ToolBars_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1Band__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1Band__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1Band__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1Band__I_FUNC);
	rc = (jint)((ToolBar^)TO_OBJECT(arg0))->Band;
	OS_NATIVE_EXIT(env, that, ToolBar_1Band__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1Band__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1Band__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1Band__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBar_1Band__II_FUNC);
	((ToolBar^)TO_OBJECT(arg0))->Band = (arg1);
	OS_NATIVE_EXIT(env, that, ToolBar_1Band__II_FUNC);
}
#endif

#ifndef NO_ToolBar_1BandIndex__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1BandIndex__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1BandIndex__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1BandIndex__I_FUNC);
	rc = (jint)((ToolBar^)TO_OBJECT(arg0))->BandIndex;
	OS_NATIVE_EXIT(env, that, ToolBar_1BandIndex__I_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1BandIndex__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1BandIndex__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1BandIndex__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBar_1BandIndex__II_FUNC);
	((ToolBar^)TO_OBJECT(arg0))->BandIndex = (arg1);
	OS_NATIVE_EXIT(env, that, ToolBar_1BandIndex__II_FUNC);
}
#endif

#ifndef NO_ToolBar_1BandProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1BandProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1BandProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1BandProperty_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::BandProperty);
	OS_NATIVE_EXIT(env, that, ToolBar_1BandProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1ButtonStyleKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1ButtonStyleKey)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1ButtonStyleKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1ButtonStyleKey_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::ButtonStyleKey);
	OS_NATIVE_EXIT(env, that, ToolBar_1ButtonStyleKey_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1CheckBoxStyleKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1CheckBoxStyleKey)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1CheckBoxStyleKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1CheckBoxStyleKey_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::CheckBoxStyleKey);
	OS_NATIVE_EXIT(env, that, ToolBar_1CheckBoxStyleKey_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1HasOverflowItems
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(ToolBar_1HasOverflowItems)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(ToolBar_1HasOverflowItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1HasOverflowItems_FUNC);
	rc = (jboolean)((ToolBar^)TO_OBJECT(arg0))->HasOverflowItems;
	OS_NATIVE_EXIT(env, that, ToolBar_1HasOverflowItems_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1RadioButtonStyleKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1RadioButtonStyleKey)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1RadioButtonStyleKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1RadioButtonStyleKey_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::RadioButtonStyleKey);
	OS_NATIVE_EXIT(env, that, ToolBar_1RadioButtonStyleKey_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1SeparatorStyleKey
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1SeparatorStyleKey)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1SeparatorStyleKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1SeparatorStyleKey_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::SeparatorStyleKey);
	OS_NATIVE_EXIT(env, that, ToolBar_1SeparatorStyleKey_FUNC);
	return rc;
}
#endif

#ifndef NO_ToolBar_1SetOverflowMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1SetOverflowMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(ToolBar_1SetOverflowMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, ToolBar_1SetOverflowMode_FUNC);
	ToolBar::SetOverflowMode((DependencyObject^)TO_OBJECT(arg0), (OverflowMode)arg1);
	OS_NATIVE_EXIT(env, that, ToolBar_1SetOverflowMode_FUNC);
}
#endif

#ifndef NO_ToolBar_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(ToolBar_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToolBar_1typeid_FUNC);
	rc = (jint)TO_HANDLE(ToolBar::typeid);
	OS_NATIVE_EXIT(env, that, ToolBar_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_TransformCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TransformCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TransformCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TransformCollection_1Add_FUNC);
	((TransformCollection^)TO_OBJECT(arg0))->Add((Transform^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TransformCollection_1Add_FUNC);
}
#endif

#ifndef NO_TransformGroup_1Children
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TransformGroup_1Children)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TransformGroup_1Children)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TransformGroup_1Children_FUNC);
	rc = (jint)TO_HANDLE(((TransformGroup^)TO_OBJECT(arg0))->Children);
	OS_NATIVE_EXIT(env, that, TransformGroup_1Children_FUNC);
	return rc;
}
#endif

#ifndef NO_Transform_1Clone
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Transform_1Clone)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Transform_1Clone)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Transform_1Clone_FUNC);
	rc = (jint)TO_HANDLE(((Transform^)TO_OBJECT(arg0))->Clone());
	OS_NATIVE_EXIT(env, that, Transform_1Clone_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1CollapsedEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1CollapsedEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1CollapsedEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1CollapsedEvent_FUNC);
	rc = (jint)TO_HANDLE(TreeViewItem::CollapsedEvent);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1CollapsedEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1ExpandedEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1ExpandedEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1ExpandedEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1ExpandedEvent_FUNC);
	rc = (jint)TO_HANDLE(TreeViewItem::ExpandedEvent);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1ExpandedEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1HeaderTemplate__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplate__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplate__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1HeaderTemplate__I_FUNC);
	rc = (jint)TO_HANDLE(((TreeViewItem^)TO_OBJECT(arg0))->HeaderTemplate);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1HeaderTemplate__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1HeaderTemplate__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplate__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplate__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TreeViewItem_1HeaderTemplate__II_FUNC);
	((TreeViewItem^)TO_OBJECT(arg0))->HeaderTemplate = ((DataTemplate^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TreeViewItem_1HeaderTemplate__II_FUNC);
}
#endif

#ifndef NO_TreeViewItem_1HeaderTemplateProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplateProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1HeaderTemplateProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1HeaderTemplateProperty_FUNC);
	rc = (jint)TO_HANDLE(TreeViewItem::HeaderTemplateProperty);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1HeaderTemplateProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1IsExpanded__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(TreeViewItem_1IsExpanded__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(TreeViewItem_1IsExpanded__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1IsExpanded__I_FUNC);
	rc = (jboolean)((TreeViewItem^)TO_OBJECT(arg0))->IsExpanded;
	OS_NATIVE_EXIT(env, that, TreeViewItem_1IsExpanded__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1IsExpanded__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1IsExpanded__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1IsExpanded__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TreeViewItem_1IsExpanded__IZ_FUNC);
	((TreeViewItem^)TO_OBJECT(arg0))->IsExpanded = (arg1);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1IsExpanded__IZ_FUNC);
}
#endif

#ifndef NO_TreeViewItem_1IsSelected__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(TreeViewItem_1IsSelected__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(TreeViewItem_1IsSelected__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1IsSelected__I_FUNC);
	rc = (jboolean)((TreeViewItem^)TO_OBJECT(arg0))->IsSelected;
	OS_NATIVE_EXIT(env, that, TreeViewItem_1IsSelected__I_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeViewItem_1IsSelected__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1IsSelected__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(TreeViewItem_1IsSelected__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TreeViewItem_1IsSelected__IZ_FUNC);
	((TreeViewItem^)TO_OBJECT(arg0))->IsSelected = (arg1);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1IsSelected__IZ_FUNC);
}
#endif

#ifndef NO_TreeViewItem_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TreeViewItem_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeViewItem_1typeid_FUNC);
	rc = (jint)TO_HANDLE(TreeViewItem::typeid);
	OS_NATIVE_EXIT(env, that, TreeViewItem_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeView_1SelectedItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeView_1SelectedItem)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TreeView_1SelectedItem)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeView_1SelectedItem_FUNC);
	rc = (jint)TO_HANDLE(((TreeView^)TO_OBJECT(arg0))->SelectedItem);
	OS_NATIVE_EXIT(env, that, TreeView_1SelectedItem_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeView_1SelectedItemChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(TreeView_1SelectedItemChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(TreeView_1SelectedItemChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, TreeView_1SelectedItemChanged_FUNC);
	((TreeView^)TO_OBJECT(arg0))->SelectedItemChanged += ((RoutedPropertyChangedEventHandler<Object^>^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, TreeView_1SelectedItemChanged_FUNC);
}
#endif

#ifndef NO_TreeView_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TreeView_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(TreeView_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TreeView_1typeid_FUNC);
	rc = (jint)TO_HANDLE(TreeView::typeid);
	OS_NATIVE_EXIT(env, that, TreeView_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_TypeConverter_1ConvertFromString
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypeConverter_1ConvertFromString)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TypeConverter_1ConvertFromString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypeConverter_1ConvertFromString_FUNC);
	rc = (jint)TO_HANDLE(((TypeConverter^)TO_OBJECT(arg0))->ConvertFromString((String^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, TypeConverter_1ConvertFromString_FUNC);
	return rc;
}
#endif

#ifndef NO_TypeConverter_1ConvertToString
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypeConverter_1ConvertToString)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(TypeConverter_1ConvertToString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypeConverter_1ConvertToString_FUNC);
	rc = (jint)TO_HANDLE(((TypeConverter^)TO_OBJECT(arg0))->ConvertToString((Object^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, TypeConverter_1ConvertToString_FUNC);
	return rc;
}
#endif

#ifndef NO_TypeDescriptor_1GetConverter
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypeDescriptor_1GetConverter)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TypeDescriptor_1GetConverter)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypeDescriptor_1GetConverter_FUNC);
	rc = (jint)TO_HANDLE(TypeDescriptor::GetConverter((Object^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, TypeDescriptor_1GetConverter_FUNC);
	return rc;
}
#endif

#ifndef NO_TypeDescriptor_1GetProperties
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypeDescriptor_1GetProperties)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TypeDescriptor_1GetProperties)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypeDescriptor_1GetProperties_FUNC);
	rc = (jint)TO_HANDLE(TypeDescriptor::GetProperties((Object^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, TypeDescriptor_1GetProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_Type_1FullName
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Type_1FullName)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Type_1FullName)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Type_1FullName_FUNC);
	rc = (jint)TO_HANDLE(((Type^)TO_OBJECT(arg0))->FullName);
	OS_NATIVE_EXIT(env, that, Type_1FullName_FUNC);
	return rc;
}
#endif

#ifndef NO_Type_1GetMethod
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetMethod)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetMethod)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Type_1GetMethod_FUNC);
	rc = (jint)TO_HANDLE(((Type^)TO_OBJECT(arg0))->GetMethod((String^)TO_OBJECT(arg1), (BindingFlags)arg2));
	OS_NATIVE_EXIT(env, that, Type_1GetMethod_FUNC);
	return rc;
}
#endif

#ifndef NO_Type_1GetProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetProperty)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Type_1GetProperty_FUNC);
	rc = (jint)TO_HANDLE(((Type^)TO_OBJECT(arg0))->GetProperty((String^)TO_OBJECT(arg1), (BindingFlags)arg2));
	OS_NATIVE_EXIT(env, that, Type_1GetProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Type_1GetType
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetType)(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2);
JNIEXPORT jint JNICALL OS_NATIVE(Type_1GetType)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Type_1GetType_FUNC);
	rc = (jint)TO_HANDLE(Type::GetType((String^)TO_OBJECT(arg0), arg1, arg2));
	OS_NATIVE_EXIT(env, that, Type_1GetType_FUNC);
	return rc;
}
#endif

#ifndef NO_Type_1IsInstanceOfType
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Type_1IsInstanceOfType)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Type_1IsInstanceOfType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Type_1IsInstanceOfType_FUNC);
	rc = (jboolean)((Type^)TO_OBJECT(arg0))->IsInstanceOfType((Object^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Type_1IsInstanceOfType_FUNC);
	return rc;
}
#endif

#ifndef NO_TypefaceCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypefaceCollection_1Count_FUNC);
	rc = (jint)((System::Collections::Generic::ICollection<Typeface^>^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, TypefaceCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_TypefaceCollection_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypefaceCollection_1Current_FUNC);
	rc = (jint)TO_HANDLE(((System::Collections::Generic::IEnumerator<Typeface^>^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, TypefaceCollection_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_TypefaceCollection_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(TypefaceCollection_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TypefaceCollection_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((System::Collections::Generic::IEnumerable<Typeface^>^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, TypefaceCollection_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_Typeface_1FontFamily
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1FontFamily)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1FontFamily)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Typeface_1FontFamily_FUNC);
	rc = (jint)TO_HANDLE(((Typeface^)TO_OBJECT(arg0))->FontFamily);
	OS_NATIVE_EXIT(env, that, Typeface_1FontFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_Typeface_1Stretch
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Stretch)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Stretch)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Typeface_1Stretch_FUNC);
	rc = (jint)TO_HANDLE(((Typeface^)TO_OBJECT(arg0))->Stretch);
	OS_NATIVE_EXIT(env, that, Typeface_1Stretch_FUNC);
	return rc;
}
#endif

#ifndef NO_Typeface_1Style
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Style)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Style)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Typeface_1Style_FUNC);
	rc = (jint)TO_HANDLE(((Typeface^)TO_OBJECT(arg0))->Style);
	OS_NATIVE_EXIT(env, that, Typeface_1Style_FUNC);
	return rc;
}
#endif

#ifndef NO_Typeface_1Weight
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Weight)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Typeface_1Weight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Typeface_1Weight_FUNC);
	rc = (jint)TO_HANDLE(((Typeface^)TO_OBJECT(arg0))->Weight);
	OS_NATIVE_EXIT(env, that, Typeface_1Weight_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1Add
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Add)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Add_FUNC);
	((UIElementCollection^)TO_OBJECT(arg0))->Add((UIElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Add_FUNC);
}
#endif

#ifndef NO_UIElementCollection_1Clear
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Clear)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Clear)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Clear_FUNC);
	((UIElementCollection^)TO_OBJECT(arg0))->Clear();
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Clear_FUNC);
}
#endif

#ifndef NO_UIElementCollection_1Contains
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElementCollection_1Contains)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElementCollection_1Contains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Contains_FUNC);
	rc = (jboolean)((UIElementCollection^)TO_OBJECT(arg0))->Contains((UIElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Contains_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Count_FUNC);
	rc = (jint)((UIElementCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Current_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerator^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerable^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, UIElementCollection_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1IndexOf
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1IndexOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1IndexOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1IndexOf_FUNC);
	rc = (jint)((UIElementCollection^)TO_OBJECT(arg0))->IndexOf((UIElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElementCollection_1IndexOf_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElementCollection_1Insert
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Insert)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Insert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Insert_FUNC);
	((UIElementCollection^)TO_OBJECT(arg0))->Insert(arg1, (UIElement^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Insert_FUNC);
}
#endif

#ifndef NO_UIElementCollection_1Remove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Remove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElementCollection_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElementCollection_1Remove_FUNC);
	((UIElementCollection^)TO_OBJECT(arg0))->Remove((UIElement^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElementCollection_1Remove_FUNC);
}
#endif

#ifndef NO_UIElementCollection_1default
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1default)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(UIElementCollection_1default)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElementCollection_1default_FUNC);
	rc = (jint)TO_HANDLE(((UIElementCollection^)TO_OBJECT(arg0))->default[arg1]);
	OS_NATIVE_EXIT(env, that, UIElementCollection_1default_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1AddHandler
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1AddHandler)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1AddHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, UIElement_1AddHandler_FUNC);
	((UIElement^)TO_OBJECT(arg0))->AddHandler((RoutedEvent^)TO_OBJECT(arg1), (Delegate^)TO_OBJECT(arg2), arg3);
	OS_NATIVE_EXIT(env, that, UIElement_1AddHandler_FUNC);
}
#endif

#ifndef NO_UIElement_1AllowDrop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1AllowDrop)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1AllowDrop)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1AllowDrop_FUNC);
	((UIElement^)TO_OBJECT(arg0))->AllowDrop = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1AllowDrop_FUNC);
}
#endif

#ifndef NO_UIElement_1BeginAnimation
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1BeginAnimation)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1BeginAnimation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, UIElement_1BeginAnimation_FUNC);
	((UIElement^)TO_OBJECT(arg0))->BeginAnimation((DependencyProperty^)TO_OBJECT(arg1), (AnimationTimeline^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, UIElement_1BeginAnimation_FUNC);
}
#endif

#ifndef NO_UIElement_1BitmapEffect__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1BitmapEffect__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1BitmapEffect__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1BitmapEffect__I_FUNC);
	rc = (jint)TO_HANDLE(((UIElement^)TO_OBJECT(arg0))->BitmapEffect);
	OS_NATIVE_EXIT(env, that, UIElement_1BitmapEffect__I_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1BitmapEffect__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1BitmapEffect__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1BitmapEffect__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1BitmapEffect__II_FUNC);
	((UIElement^)TO_OBJECT(arg0))->BitmapEffect = ((BitmapEffect^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1BitmapEffect__II_FUNC);
}
#endif

#ifndef NO_UIElement_1CaptureMouse
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1CaptureMouse)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1CaptureMouse)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1CaptureMouse_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->CaptureMouse();
	OS_NATIVE_EXIT(env, that, UIElement_1CaptureMouse_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Clip
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Clip)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Clip)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Clip_FUNC);
	((UIElement^)TO_OBJECT(arg0))->Clip = ((Geometry^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1Clip_FUNC);
}
#endif

#ifndef NO_UIElement_1ClipToBounds
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1ClipToBounds)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1ClipToBounds)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1ClipToBounds_FUNC);
	((UIElement^)TO_OBJECT(arg0))->ClipToBounds = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1ClipToBounds_FUNC);
}
#endif

#ifndef NO_UIElement_1ClipToBoundsProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1ClipToBoundsProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1ClipToBoundsProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1ClipToBoundsProperty_FUNC);
	rc = (jint)TO_HANDLE(UIElement::ClipToBoundsProperty);
	OS_NATIVE_EXIT(env, that, UIElement_1ClipToBoundsProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1DesiredSize
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DesiredSize)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DesiredSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1DesiredSize_FUNC);
	rc = (jint)TO_HANDLE(((UIElement^)TO_OBJECT(arg0))->DesiredSize);
	OS_NATIVE_EXIT(env, that, UIElement_1DesiredSize_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1DragEnter
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragEnter)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragEnter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1DragEnter_FUNC);
	((UIElement^)TO_OBJECT(arg0))->DragEnter += ((DragEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1DragEnter_FUNC);
}
#endif

#ifndef NO_UIElement_1DragEnterEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragEnterEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragEnterEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1DragEnterEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::DragEnterEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1DragEnterEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1DragLeave
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragLeave)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragLeave)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1DragLeave_FUNC);
	((UIElement^)TO_OBJECT(arg0))->DragLeave += ((DragEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1DragLeave_FUNC);
}
#endif

#ifndef NO_UIElement_1DragLeaveEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragLeaveEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragLeaveEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1DragLeaveEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::DragLeaveEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1DragLeaveEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1DragOver
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragOver)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1DragOver)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1DragOver_FUNC);
	((UIElement^)TO_OBJECT(arg0))->DragOver += ((DragEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1DragOver_FUNC);
}
#endif

#ifndef NO_UIElement_1DragOverEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragOverEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DragOverEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1DragOverEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::DragOverEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1DragOverEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Drop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Drop)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Drop)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Drop_FUNC);
	((UIElement^)TO_OBJECT(arg0))->Drop += ((DragEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1Drop_FUNC);
}
#endif

#ifndef NO_UIElement_1DropEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DropEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1DropEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1DropEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::DropEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1DropEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Focus
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1Focus)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1Focus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1Focus_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->Focus();
	OS_NATIVE_EXIT(env, that, UIElement_1Focus_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Focusable
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Focusable)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Focusable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Focusable_FUNC);
	((UIElement^)TO_OBJECT(arg0))->Focusable = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1Focusable_FUNC);
}
#endif

#ifndef NO_UIElement_1GiveFeedback
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1GiveFeedback)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1GiveFeedback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1GiveFeedback_FUNC);
	((UIElement^)TO_OBJECT(arg0))->GiveFeedback += ((GiveFeedbackEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1GiveFeedback_FUNC);
}
#endif

#ifndef NO_UIElement_1GiveFeedbackEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1GiveFeedbackEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1GiveFeedbackEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1GiveFeedbackEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::GiveFeedbackEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1GiveFeedbackEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1InputHitTest
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1InputHitTest)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1InputHitTest)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1InputHitTest_FUNC);
	rc = (jint)TO_HANDLE(((UIElement^)TO_OBJECT(arg0))->InputHitTest((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, UIElement_1InputHitTest_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1InvalidateVisual
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1InvalidateVisual)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1InvalidateVisual)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, UIElement_1InvalidateVisual_FUNC);
	((UIElement^)TO_OBJECT(arg0))->InvalidateVisual();
	OS_NATIVE_EXIT(env, that, UIElement_1InvalidateVisual_FUNC);
}
#endif

#ifndef NO_UIElement_1IsEnabled__I
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsEnabled__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsEnabled__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsEnabled__I_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsEnabled;
	OS_NATIVE_EXIT(env, that, UIElement_1IsEnabled__I_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsEnabled__IZ
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1IsEnabled__IZ)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1IsEnabled__IZ)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1IsEnabled__IZ_FUNC);
	((UIElement^)TO_OBJECT(arg0))->IsEnabled = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1IsEnabled__IZ_FUNC);
}
#endif

#ifndef NO_UIElement_1IsFocused
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsFocused)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsFocused)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsFocused_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsFocused;
	OS_NATIVE_EXIT(env, that, UIElement_1IsFocused_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsHitTestVisible
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1IsHitTestVisible)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1IsHitTestVisible)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1IsHitTestVisible_FUNC);
	((UIElement^)TO_OBJECT(arg0))->IsHitTestVisible = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1IsHitTestVisible_FUNC);
}
#endif

#ifndef NO_UIElement_1IsKeyboardFocusWithin
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsKeyboardFocusWithin)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsKeyboardFocusWithin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsKeyboardFocusWithin_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsKeyboardFocusWithin;
	OS_NATIVE_EXIT(env, that, UIElement_1IsKeyboardFocusWithin_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsKeyboardFocused
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsKeyboardFocused)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsKeyboardFocused)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsKeyboardFocused_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsKeyboardFocused;
	OS_NATIVE_EXIT(env, that, UIElement_1IsKeyboardFocused_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsMeasureValid
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsMeasureValid)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsMeasureValid)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsMeasureValid_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsMeasureValid;
	OS_NATIVE_EXIT(env, that, UIElement_1IsMeasureValid_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsMouseOver
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsMouseOver)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsMouseOver)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsMouseOver_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsMouseOver;
	OS_NATIVE_EXIT(env, that, UIElement_1IsMouseOver_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1IsVisible
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsVisible)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(UIElement_1IsVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1IsVisible_FUNC);
	rc = (jboolean)((UIElement^)TO_OBJECT(arg0))->IsVisible;
	OS_NATIVE_EXIT(env, that, UIElement_1IsVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1KeyDown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1KeyDown)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1KeyDown)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1KeyDown_FUNC);
	((UIElement^)TO_OBJECT(arg0))->KeyDown += ((KeyEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1KeyDown_FUNC);
}
#endif

#ifndef NO_UIElement_1KeyUp
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1KeyUp)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1KeyUp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1KeyUp_FUNC);
	((UIElement^)TO_OBJECT(arg0))->KeyUp += ((KeyEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1KeyUp_FUNC);
}
#endif

#ifndef NO_UIElement_1LayoutUpdated
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1LayoutUpdated)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1LayoutUpdated)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1LayoutUpdated_FUNC);
	((UIElement^)TO_OBJECT(arg0))->LayoutUpdated += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1LayoutUpdated_FUNC);
}
#endif

#ifndef NO_UIElement_1LostKeyboardFocus
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1LostKeyboardFocus)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1LostKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1LostKeyboardFocus_FUNC);
	((UIElement^)TO_OBJECT(arg0))->LostKeyboardFocus += ((KeyboardFocusChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1LostKeyboardFocus_FUNC);
}
#endif

#ifndef NO_UIElement_1Measure
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Measure)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Measure)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Measure_FUNC);
	((UIElement ^)TO_OBJECT(arg0))->Measure((Size)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1Measure_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseDown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseDown)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseDown)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseDown_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseDown += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseDown_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseEnter
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseEnter)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseEnter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseEnter_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseEnter += ((MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseEnter_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseLeave
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseLeave)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseLeave)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseLeave_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseLeave += ((MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseLeave_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseMove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseMove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseMove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseMove_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseMove += ((MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseMove_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseUp
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseUp)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseUp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseUp_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseUp += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseUp_FUNC);
}
#endif

#ifndef NO_UIElement_1MouseWheel
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseWheel)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MouseWheel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MouseWheel_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MouseWheel += ((MouseWheelEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MouseWheel_FUNC);
}
#endif

#ifndef NO_UIElement_1MoveFocus
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MoveFocus)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1MoveFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1MoveFocus_FUNC);
	((UIElement^)TO_OBJECT(arg0))->MoveFocus((TraversalRequest^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1MoveFocus_FUNC);
}
#endif

#ifndef NO_UIElement_1Opacity__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(UIElement_1Opacity__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(UIElement_1Opacity__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1Opacity__I_FUNC);
	rc = (jdouble)((UIElement^)TO_OBJECT(arg0))->Opacity;
	OS_NATIVE_EXIT(env, that, UIElement_1Opacity__I_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Opacity__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Opacity__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Opacity__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Opacity__ID_FUNC);
	((UIElement^)TO_OBJECT(arg0))->Opacity = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1Opacity__ID_FUNC);
}
#endif

#ifndef NO_UIElement_1OpacityProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1OpacityProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1OpacityProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1OpacityProperty_FUNC);
	rc = (jint)TO_HANDLE(UIElement::OpacityProperty);
	OS_NATIVE_EXIT(env, that, UIElement_1OpacityProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1PreviewGotKeyboardFocus
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewGotKeyboardFocus)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewGotKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewGotKeyboardFocus_FUNC);
	((UIElement^)TO_OBJECT(arg0))->PreviewGotKeyboardFocus += ((KeyboardFocusChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewGotKeyboardFocus_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewKeyDown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewKeyDown)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewKeyDown)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewKeyDown_FUNC);
	((UIElement^)TO_OBJECT(arg0))->PreviewKeyDown += ((KeyEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewKeyDown_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewKeyUp
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewKeyUp)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewKeyUp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewKeyUp_FUNC);
	((UIElement^)TO_OBJECT(arg0))->PreviewKeyUp += ((KeyEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewKeyUp_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewLostKeyboardFocus
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewLostKeyboardFocus)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewLostKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewLostKeyboardFocus_FUNC);
	((UIElement^)TO_OBJECT(arg0))->PreviewLostKeyboardFocus += ((KeyboardFocusChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewLostKeyboardFocus_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewMouseDown
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseDown)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseDown)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewMouseDown_FUNC);
	((UIElement ^)TO_OBJECT(arg0))->PreviewMouseDown += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewMouseDown_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewMouseMove
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseMove)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseMove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewMouseMove_FUNC);
	((UIElement ^)TO_OBJECT(arg0))->PreviewMouseMove += ((MouseEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewMouseMove_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewMouseUp
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseUp)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseUp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewMouseUp_FUNC);
	((UIElement ^)TO_OBJECT(arg0))->PreviewMouseUp += ((MouseButtonEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewMouseUp_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewMouseWheel
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseWheel)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewMouseWheel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewMouseWheel_FUNC);
	((UIElement ^)TO_OBJECT(arg0))->PreviewMouseWheel += ((MouseWheelEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewMouseWheel_FUNC);
}
#endif

#ifndef NO_UIElement_1PreviewTextInput
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewTextInput)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1PreviewTextInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1PreviewTextInput_FUNC);
	((UIElement^)TO_OBJECT(arg0))->PreviewTextInput += ((TextCompositionEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1PreviewTextInput_FUNC);
}
#endif

#ifndef NO_UIElement_1QueryContinueDrag
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1QueryContinueDrag)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1QueryContinueDrag)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1QueryContinueDrag_FUNC);
	((UIElement^)TO_OBJECT(arg0))->QueryContinueDrag += ((QueryContinueDragEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1QueryContinueDrag_FUNC);
}
#endif

#ifndef NO_UIElement_1QueryContinueDragEvent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1QueryContinueDragEvent)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1QueryContinueDragEvent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1QueryContinueDragEvent_FUNC);
	rc = (jint)TO_HANDLE(UIElement::QueryContinueDragEvent);
	OS_NATIVE_EXIT(env, that, UIElement_1QueryContinueDragEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1ReleaseMouseCapture
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1ReleaseMouseCapture)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1ReleaseMouseCapture)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, UIElement_1ReleaseMouseCapture_FUNC);
	((UIElement^)TO_OBJECT(arg0))->ReleaseMouseCapture();
	OS_NATIVE_EXIT(env, that, UIElement_1ReleaseMouseCapture_FUNC);
}
#endif

#ifndef NO_UIElement_1RemoveHandler
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1RemoveHandler)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1RemoveHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, UIElement_1RemoveHandler_FUNC);
	((UIElement^)TO_OBJECT(arg0))->RemoveHandler((RoutedEvent^)TO_OBJECT(arg1), (Delegate^)TO_OBJECT(arg2));
	OS_NATIVE_EXIT(env, that, UIElement_1RemoveHandler_FUNC);
}
#endif

#ifndef NO_UIElement_1RenderSize
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1RenderSize)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1RenderSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1RenderSize_FUNC);
	rc = (jint)TO_HANDLE(((UIElement^)TO_OBJECT(arg0))->RenderSize);
	OS_NATIVE_EXIT(env, that, UIElement_1RenderSize_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1SnapsToDevicePixels
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1SnapsToDevicePixels)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1SnapsToDevicePixels)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1SnapsToDevicePixels_FUNC);
	((UIElement^)TO_OBJECT(arg0))->SnapsToDevicePixels = (arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1SnapsToDevicePixels_FUNC);
}
#endif

#ifndef NO_UIElement_1TextInput
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1TextInput)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1TextInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1TextInput_FUNC);
	((UIElement^)TO_OBJECT(arg0))->TextInput += ((TextCompositionEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, UIElement_1TextInput_FUNC);
}
#endif

#ifndef NO_UIElement_1TranslatePoint
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1TranslatePoint)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1TranslatePoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1TranslatePoint_FUNC);
	rc = (jint)TO_HANDLE(((UIElement^)TO_OBJECT(arg0))->TranslatePoint((Point)TO_OBJECT(arg1), (UIElement^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, UIElement_1TranslatePoint_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1UpdateLayout
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1UpdateLayout)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1UpdateLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, UIElement_1UpdateLayout_FUNC);
	((UIElement^)TO_OBJECT(arg0))->UpdateLayout();
	OS_NATIVE_EXIT(env, that, UIElement_1UpdateLayout_FUNC);
}
#endif

#ifndef NO_UIElement_1Visibility__I
extern "C" JNIEXPORT jbyte JNICALL OS_NATIVE(UIElement_1Visibility__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jbyte JNICALL OS_NATIVE(UIElement_1Visibility__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1Visibility__I_FUNC);
	rc = (jbyte)((UIElement^)TO_OBJECT(arg0))->Visibility;
	OS_NATIVE_EXIT(env, that, UIElement_1Visibility__I_FUNC);
	return rc;
}
#endif

#ifndef NO_UIElement_1Visibility__IB
extern "C" JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Visibility__IB)(JNIEnv *env, jclass that, jint arg0, jbyte arg1);
JNIEXPORT void JNICALL OS_NATIVE(UIElement_1Visibility__IB)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1)
{
	OS_NATIVE_ENTER(env, that, UIElement_1Visibility__IB_FUNC);
	((UIElement^)TO_OBJECT(arg0))->Visibility = ((Visibility)arg1);
	OS_NATIVE_EXIT(env, that, UIElement_1Visibility__IB_FUNC);
}
#endif

#ifndef NO_UIElement_1VisibilityProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1VisibilityProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(UIElement_1VisibilityProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UIElement_1VisibilityProperty_FUNC);
	rc = (jint)TO_HANDLE(UIElement::VisibilityProperty);
	OS_NATIVE_EXIT(env, that, UIElement_1VisibilityProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_VirtualizingStackPanel_1VerticalOffset
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(VirtualizingStackPanel_1VerticalOffset)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(VirtualizingStackPanel_1VerticalOffset)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, VirtualizingStackPanel_1VerticalOffset_FUNC);
	rc = (jdouble)((VirtualizingStackPanel^)TO_OBJECT(arg0))->VerticalOffset;
	OS_NATIVE_EXIT(env, that, VirtualizingStackPanel_1VerticalOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_VisualTreeHelper_1GetChild
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetChild)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VisualTreeHelper_1GetChild_FUNC);
	rc = (jint)TO_HANDLE(VisualTreeHelper::GetChild((DependencyObject^)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, VisualTreeHelper_1GetChild_FUNC);
	return rc;
}
#endif

#ifndef NO_VisualTreeHelper_1GetChildrenCount
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetChildrenCount)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetChildrenCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VisualTreeHelper_1GetChildrenCount_FUNC);
	rc = (jint)VisualTreeHelper::GetChildrenCount((DependencyObject^)TO_OBJECT(arg0));
	OS_NATIVE_EXIT(env, that, VisualTreeHelper_1GetChildrenCount_FUNC);
	return rc;
}
#endif

#ifndef NO_VisualTreeHelper_1GetParent
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetParent)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(VisualTreeHelper_1GetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VisualTreeHelper_1GetParent_FUNC);
	rc = (jint)TO_HANDLE(VisualTreeHelper::GetParent((DependencyObject^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, VisualTreeHelper_1GetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_Visual_1IsAncestorOf
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Visual_1IsAncestorOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Visual_1IsAncestorOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Visual_1IsAncestorOf_FUNC);
	rc = (jboolean)((Visual^)TO_OBJECT(arg0))->IsAncestorOf((DependencyObject^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Visual_1IsAncestorOf_FUNC);
	return rc;
}
#endif

#ifndef NO_Visual_1IsDescendantOf
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Visual_1IsDescendantOf)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jboolean JNICALL OS_NATIVE(Visual_1IsDescendantOf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Visual_1IsDescendantOf_FUNC);
	rc = (jboolean)((Visual^)TO_OBJECT(arg0))->IsDescendantOf((DependencyObject^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Visual_1IsDescendantOf_FUNC);
	return rc;
}
#endif

#ifndef NO_Visual_1PointFromScreen
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Visual_1PointFromScreen)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Visual_1PointFromScreen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Visual_1PointFromScreen_FUNC);
	rc = (jint)TO_HANDLE(((Visual^)TO_OBJECT(arg0))->PointFromScreen((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, Visual_1PointFromScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_Visual_1PointToScreen
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Visual_1PointToScreen)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(Visual_1PointToScreen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Visual_1PointToScreen_FUNC);
	rc = (jint)TO_HANDLE(((Visual^)TO_OBJECT(arg0))->PointToScreen((Point)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, Visual_1PointToScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowserDocumentCompletedEventArgs_1Url
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserDocumentCompletedEventArgs_1Url)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserDocumentCompletedEventArgs_1Url)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowserDocumentCompletedEventArgs_1Url_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowserDocumentCompletedEventArgs^)TO_OBJECT(arg0))->Url);
	OS_NATIVE_EXIT(env, that, WebBrowserDocumentCompletedEventArgs_1Url_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowserNavigatedEventArgs_1Url
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserNavigatedEventArgs_1Url)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserNavigatedEventArgs_1Url)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowserNavigatedEventArgs_1Url_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowserNavigatedEventArgs^)TO_OBJECT(arg0))->Url);
	OS_NATIVE_EXIT(env, that, WebBrowserNavigatedEventArgs_1Url_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowserNavigatingEventArgs_1Url
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserNavigatingEventArgs_1Url)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowserNavigatingEventArgs_1Url)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowserNavigatingEventArgs_1Url_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowserNavigatingEventArgs^)TO_OBJECT(arg0))->Url);
	OS_NATIVE_EXIT(env, that, WebBrowserNavigatingEventArgs_1Url_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowserProgressChangedEventArgs_1CurrentProgress
extern "C" JNIEXPORT jlong JNICALL OS_NATIVE(WebBrowserProgressChangedEventArgs_1CurrentProgress)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jlong JNICALL OS_NATIVE(WebBrowserProgressChangedEventArgs_1CurrentProgress)
	(JNIEnv *env, jclass that, jint arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowserProgressChangedEventArgs_1CurrentProgress_FUNC);
	rc = (jlong)((System::Windows::Forms::WebBrowserProgressChangedEventArgs^)TO_OBJECT(arg0))->CurrentProgress;
	OS_NATIVE_EXIT(env, that, WebBrowserProgressChangedEventArgs_1CurrentProgress_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowserProgressChangedEventArgs_1MaximumProgress
extern "C" JNIEXPORT jlong JNICALL OS_NATIVE(WebBrowserProgressChangedEventArgs_1MaximumProgress)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jlong JNICALL OS_NATIVE(WebBrowserProgressChangedEventArgs_1MaximumProgress)
	(JNIEnv *env, jclass that, jint arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowserProgressChangedEventArgs_1MaximumProgress_FUNC);
	rc = (jlong)((System::Windows::Forms::WebBrowserProgressChangedEventArgs^)TO_OBJECT(arg0))->MaximumProgress;
	OS_NATIVE_EXIT(env, that, WebBrowserProgressChangedEventArgs_1MaximumProgress_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1CanGoBack
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1CanGoBack)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1CanGoBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1CanGoBack_FUNC);
	rc = (jboolean)((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->CanGoBack;
	OS_NATIVE_EXIT(env, that, WebBrowser_1CanGoBack_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1CanGoForward
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1CanGoForward)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1CanGoForward)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1CanGoForward_FUNC);
	rc = (jboolean)((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->CanGoForward;
	OS_NATIVE_EXIT(env, that, WebBrowser_1CanGoForward_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1Document
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1Document)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1Document)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1Document_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Document);
	OS_NATIVE_EXIT(env, that, WebBrowser_1Document_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1DocumentCompleted
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentCompleted)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentCompleted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1DocumentCompleted_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->DocumentCompleted += ((System::Windows::Forms::WebBrowserDocumentCompletedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1DocumentCompleted_FUNC);
}
#endif

#ifndef NO_WebBrowser_1DocumentText__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1DocumentText__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1DocumentText__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1DocumentText__I_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->DocumentText);
	OS_NATIVE_EXIT(env, that, WebBrowser_1DocumentText__I_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1DocumentText__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentText__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentText__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1DocumentText__II_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->DocumentText = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1DocumentText__II_FUNC);
}
#endif

#ifndef NO_WebBrowser_1DocumentTitle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1DocumentTitle)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1DocumentTitle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1DocumentTitle_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->DocumentTitle);
	OS_NATIVE_EXIT(env, that, WebBrowser_1DocumentTitle_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1DocumentTitleChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentTitleChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1DocumentTitleChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1DocumentTitleChanged_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->DocumentTitleChanged += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1DocumentTitleChanged_FUNC);
}
#endif

#ifndef NO_WebBrowser_1GoBack
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1GoBack)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1GoBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1GoBack_FUNC);
	rc = (jboolean)((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->GoBack();
	OS_NATIVE_EXIT(env, that, WebBrowser_1GoBack_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1GoForward
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1GoForward)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(WebBrowser_1GoForward)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1GoForward_FUNC);
	rc = (jboolean)((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->GoForward();
	OS_NATIVE_EXIT(env, that, WebBrowser_1GoForward_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1Navigate
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigate)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1Navigate_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Navigate((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1Navigate_FUNC);
}
#endif

#ifndef NO_WebBrowser_1Navigated
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigated)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigated)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1Navigated_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Navigated += ((System::Windows::Forms::WebBrowserNavigatedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1Navigated_FUNC);
}
#endif

#ifndef NO_WebBrowser_1Navigating
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigating)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Navigating)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1Navigating_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Navigating += ((System::Windows::Forms::WebBrowserNavigatingEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1Navigating_FUNC);
}
#endif

#ifndef NO_WebBrowser_1ProgressChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1ProgressChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1ProgressChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1ProgressChanged_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->ProgressChanged += ((System::Windows::Forms::WebBrowserProgressChangedEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1ProgressChanged_FUNC);
}
#endif

#ifndef NO_WebBrowser_1ReadyState
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1ReadyState)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1ReadyState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1ReadyState_FUNC);
	rc = (jint)((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->ReadyState;
	OS_NATIVE_EXIT(env, that, WebBrowser_1ReadyState_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1Refresh
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Refresh)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Refresh)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1Refresh_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Refresh();
	OS_NATIVE_EXIT(env, that, WebBrowser_1Refresh_FUNC);
}
#endif

#ifndef NO_WebBrowser_1ScriptErrorsSuppressed
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1ScriptErrorsSuppressed)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1ScriptErrorsSuppressed)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1ScriptErrorsSuppressed_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->ScriptErrorsSuppressed = (arg1);
	OS_NATIVE_EXIT(env, that, WebBrowser_1ScriptErrorsSuppressed_FUNC);
}
#endif

#ifndef NO_WebBrowser_1StatusText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1StatusText)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1StatusText)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1StatusText_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->StatusText);
	OS_NATIVE_EXIT(env, that, WebBrowser_1StatusText_FUNC);
	return rc;
}
#endif

#ifndef NO_WebBrowser_1StatusTextChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1StatusTextChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1StatusTextChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1StatusTextChanged_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->StatusTextChanged += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WebBrowser_1StatusTextChanged_FUNC);
}
#endif

#ifndef NO_WebBrowser_1Stop
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Stop)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(WebBrowser_1Stop)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, WebBrowser_1Stop_FUNC);
	((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Stop();
	OS_NATIVE_EXIT(env, that, WebBrowser_1Stop_FUNC);
}
#endif

#ifndef NO_WebBrowser_1Url
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1Url)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WebBrowser_1Url)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WebBrowser_1Url_FUNC);
	rc = (jint)TO_HANDLE(((System::Windows::Forms::WebBrowser^)TO_OBJECT(arg0))->Url);
	OS_NATIVE_EXIT(env, that, WebBrowser_1Url_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowCollection_1Count
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1Count)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1Count)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WindowCollection_1Count_FUNC);
	rc = (jint)((WindowCollection^)TO_OBJECT(arg0))->Count;
	OS_NATIVE_EXIT(env, that, WindowCollection_1Count_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowCollection_1Current
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1Current)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1Current)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WindowCollection_1Current_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerator^)TO_OBJECT(arg0))->Current);
	OS_NATIVE_EXIT(env, that, WindowCollection_1Current_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowCollection_1GetEnumerator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1GetEnumerator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(WindowCollection_1GetEnumerator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WindowCollection_1GetEnumerator_FUNC);
	rc = (jint)TO_HANDLE(((IEnumerable^)TO_OBJECT(arg0))->GetEnumerator());
	OS_NATIVE_EXIT(env, that, WindowCollection_1GetEnumerator_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Activate
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Activate)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Activate)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Window_1Activate_FUNC);
	((Window^)TO_OBJECT(arg0))->Activate();
	OS_NATIVE_EXIT(env, that, Window_1Activate_FUNC);
}
#endif

#ifndef NO_Window_1Activated
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Activated)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Activated)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Activated_FUNC);
	((Window^)TO_OBJECT(arg0))->Activated += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Activated_FUNC);
}
#endif

#ifndef NO_Window_1AllowsTransparency
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1AllowsTransparency)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1AllowsTransparency)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1AllowsTransparency_FUNC);
	((Window^)TO_OBJECT(arg0))->AllowsTransparency = (arg1);
	OS_NATIVE_EXIT(env, that, Window_1AllowsTransparency_FUNC);
}
#endif

#ifndef NO_Window_1Close
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Close)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Close)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Window_1Close_FUNC);
	((Window^)TO_OBJECT(arg0))->Close();
	OS_NATIVE_EXIT(env, that, Window_1Close_FUNC);
}
#endif

#ifndef NO_Window_1Closing
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Closing)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Closing)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Closing_FUNC);
	((Window^)TO_OBJECT(arg0))->Closing += ((CancelEventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Closing_FUNC);
}
#endif

#ifndef NO_Window_1Deactivated
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Deactivated)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Deactivated)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Deactivated_FUNC);
	((Window^)TO_OBJECT(arg0))->Deactivated += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Deactivated_FUNC);
}
#endif

#ifndef NO_Window_1GetWindow
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Window_1GetWindow)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Window_1GetWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1GetWindow_FUNC);
	rc = (jint)TO_HANDLE(Window::GetWindow((DependencyObject^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, Window_1GetWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Hide
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Hide)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Hide)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Window_1Hide_FUNC);
	((Window^)TO_OBJECT(arg0))->Hide();
	OS_NATIVE_EXIT(env, that, Window_1Hide_FUNC);
}
#endif

#ifndef NO_Window_1Icon
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Icon)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Icon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Icon_FUNC);
	((Window^)TO_OBJECT(arg0))->Icon = ((ImageSource^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Icon_FUNC);
}
#endif

#ifndef NO_Window_1IsActive
extern "C" JNIEXPORT jboolean JNICALL OS_NATIVE(Window_1IsActive)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jboolean JNICALL OS_NATIVE(Window_1IsActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1IsActive_FUNC);
	rc = (jboolean)((Window^)TO_OBJECT(arg0))->IsActive;
	OS_NATIVE_EXIT(env, that, Window_1IsActive_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Left__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Window_1Left__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Window_1Left__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1Left__I_FUNC);
	rc = (jdouble)((Window^)TO_OBJECT(arg0))->Left;
	OS_NATIVE_EXIT(env, that, Window_1Left__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Left__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Left__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Left__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Left__ID_FUNC);
	((Window^)TO_OBJECT(arg0))->Left = (arg1);
	OS_NATIVE_EXIT(env, that, Window_1Left__ID_FUNC);
}
#endif

#ifndef NO_Window_1LocationChanged
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1LocationChanged)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1LocationChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1LocationChanged_FUNC);
	((Window^)TO_OBJECT(arg0))->LocationChanged += ((EventHandler^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1LocationChanged_FUNC);
}
#endif

#ifndef NO_Window_1Owner
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Owner)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Owner)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Owner_FUNC);
	((Window^)TO_OBJECT(arg0))->Owner = ((Window^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Owner_FUNC);
}
#endif

#ifndef NO_Window_1ResizeMode
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1ResizeMode)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1ResizeMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1ResizeMode_FUNC);
	((Window^)TO_OBJECT(arg0))->ResizeMode = ((ResizeMode)arg1);
	OS_NATIVE_EXIT(env, that, Window_1ResizeMode_FUNC);
}
#endif

#ifndef NO_Window_1Show
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Show)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Show)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, Window_1Show_FUNC);
	((Window^)TO_OBJECT(arg0))->Show();
	OS_NATIVE_EXIT(env, that, Window_1Show_FUNC);
}
#endif

#ifndef NO_Window_1ShowInTaskbar
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1ShowInTaskbar)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1ShowInTaskbar)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1ShowInTaskbar_FUNC);
	((Window^)TO_OBJECT(arg0))->ShowInTaskbar = (arg1);
	OS_NATIVE_EXIT(env, that, Window_1ShowInTaskbar_FUNC);
}
#endif

#ifndef NO_Window_1Title__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Window_1Title__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Window_1Title__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1Title__I_FUNC);
	rc = (jint)TO_HANDLE(((Window^)TO_OBJECT(arg0))->Title);
	OS_NATIVE_EXIT(env, that, Window_1Title__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Title__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Title__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Title__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Title__II_FUNC);
	((Window^)TO_OBJECT(arg0))->Title = ((String^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, Window_1Title__II_FUNC);
}
#endif

#ifndef NO_Window_1Top__I
extern "C" JNIEXPORT jdouble JNICALL OS_NATIVE(Window_1Top__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jdouble JNICALL OS_NATIVE(Window_1Top__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1Top__I_FUNC);
	rc = (jdouble)((Window ^)TO_OBJECT(arg0))->Top;
	OS_NATIVE_EXIT(env, that, Window_1Top__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1Top__ID
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1Top__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1Top__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1Top__ID_FUNC);
	((Window ^)TO_OBJECT(arg0))->Top = (arg1);
	OS_NATIVE_EXIT(env, that, Window_1Top__ID_FUNC);
}
#endif

#ifndef NO_Window_1WindowState__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Window_1WindowState__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Window_1WindowState__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1WindowState__I_FUNC);
	rc = (jint)((Window^)TO_OBJECT(arg0))->WindowState;
	OS_NATIVE_EXIT(env, that, Window_1WindowState__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1WindowState__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1WindowState__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1WindowState__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1WindowState__II_FUNC);
	((Window^)TO_OBJECT(arg0))->WindowState = ((WindowState)arg1);
	OS_NATIVE_EXIT(env, that, Window_1WindowState__II_FUNC);
}
#endif

#ifndef NO_Window_1WindowStyle__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Window_1WindowStyle__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Window_1WindowStyle__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Window_1WindowStyle__I_FUNC);
	rc = (jint)((Window^)TO_OBJECT(arg0))->WindowStyle;
	OS_NATIVE_EXIT(env, that, Window_1WindowStyle__I_FUNC);
	return rc;
}
#endif

#ifndef NO_Window_1WindowStyle__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Window_1WindowStyle__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Window_1WindowStyle__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Window_1WindowStyle__II_FUNC);
	((Window^)TO_OBJECT(arg0))->WindowStyle = ((WindowStyle)arg1);
	OS_NATIVE_EXIT(env, that, Window_1WindowStyle__II_FUNC);
}
#endif

#ifndef NO_WindowsFormsHost_1Child
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WindowsFormsHost_1Child)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(WindowsFormsHost_1Child)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, WindowsFormsHost_1Child_FUNC);
	((System::Windows::Forms::Integration::WindowsFormsHost^)TO_OBJECT(arg0))->Child = ((System::Windows::Forms::Control^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, WindowsFormsHost_1Child_FUNC);
}
#endif

#ifndef NO_WriteableBitmap_1WritePixels
extern "C" JNIEXPORT void JNICALL OS_NATIVE(WriteableBitmap_1WritePixels)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4);
JNIEXPORT void JNICALL OS_NATIVE(WriteableBitmap_1WritePixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, WriteableBitmap_1WritePixels_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	((WriteableBitmap^)TO_OBJECT(arg0))->WritePixels((Int32Rect)TO_OBJECT(arg1), (IntPtr)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, WriteableBitmap_1WritePixels_FUNC);
}
#endif

#ifndef NO_XamlReader_1Load
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(XamlReader_1Load)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(XamlReader_1Load)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XamlReader_1Load_FUNC);
	rc = (jint)TO_HANDLE(XamlReader::Load((XmlReader^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, XamlReader_1Load_FUNC);
	return rc;
}
#endif

#ifndef NO_XmlReader_1Create
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(XmlReader_1Create)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(XmlReader_1Create)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmlReader_1Create_FUNC);
	rc = (jint)TO_HANDLE(XmlReader::Create((System::IO::TextReader^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, XmlReader_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1AccessText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1AccessText)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1AccessText)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1AccessText_FUNC);
	rc = (jint)TO_HANDLE(gcnew AccessText());
	OS_NATIVE_EXIT(env, that, gcnew_1AccessText_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Application
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Application)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Application)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Application_FUNC);
	rc = (jint)TO_HANDLE(gcnew Application());
	OS_NATIVE_EXIT(env, that, gcnew_1Application_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ArcSegment
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ArcSegment)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jboolean arg3, jint arg4, jboolean arg5);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ArcSegment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jboolean arg3, jint arg4, jboolean arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ArcSegment_FUNC);
	rc = (jint)TO_HANDLE(gcnew ArcSegment((Point)TO_OBJECT(arg0), (Size)TO_OBJECT(arg1), arg2, arg3, (SweepDirection)arg4, arg5));
	OS_NATIVE_EXIT(env, that, gcnew_1ArcSegment_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ArrayList
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ArrayList)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ArrayList)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ArrayList_FUNC);
	rc = (jint)TO_HANDLE(gcnew ArrayList(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1ArrayList_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BevelBitmapEffect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BevelBitmapEffect)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BevelBitmapEffect)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BevelBitmapEffect_FUNC);
	rc = (jint)TO_HANDLE(gcnew BevelBitmapEffect());
	OS_NATIVE_EXIT(env, that, gcnew_1BevelBitmapEffect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BezierSegment
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BezierSegment)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BezierSegment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BezierSegment_FUNC);
	rc = (jint)TO_HANDLE(gcnew BezierSegment((Point)TO_OBJECT(arg0), (Point)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2), arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1BezierSegment_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Binding
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Binding)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Binding)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Binding_FUNC);
	rc = (jint)TO_HANDLE(gcnew Binding((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1Binding_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Bitmap
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Bitmap)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Bitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Bitmap_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)TO_HANDLE(gcnew System::Drawing::Bitmap(arg0, arg1, arg2, (System::Drawing::Imaging::PixelFormat)arg3, (IntPtr)lparg4));
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, gcnew_1Bitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BitmapEffectGroup
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapEffectGroup)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapEffectGroup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BitmapEffectGroup_FUNC);
	rc = (jint)TO_HANDLE(gcnew BitmapEffectGroup());
	OS_NATIVE_EXIT(env, that, gcnew_1BitmapEffectGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BitmapImage
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapImage)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapImage)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BitmapImage_FUNC);
	rc = (jint)TO_HANDLE(gcnew BitmapImage());
	OS_NATIVE_EXIT(env, that, gcnew_1BitmapImage_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BitmapPalette
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapPalette)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BitmapPalette)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BitmapPalette_FUNC);
	rc = (jint)TO_HANDLE(gcnew BitmapPalette((System::Collections::Generic::IList<Color>^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1BitmapPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1BlurBitmapEffect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BlurBitmapEffect)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1BlurBitmapEffect)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1BlurBitmapEffect_FUNC);
	rc = (jint)TO_HANDLE(gcnew BlurBitmapEffect());
	OS_NATIVE_EXIT(env, that, gcnew_1BlurBitmapEffect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Button
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Button)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Button)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Button_FUNC);
	rc = (jint)TO_HANDLE(gcnew Button());
	OS_NATIVE_EXIT(env, that, gcnew_1Button_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Canvas
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Canvas)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Canvas)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Canvas_FUNC);
	rc = (jint)TO_HANDLE(gcnew Canvas());
	OS_NATIVE_EXIT(env, that, gcnew_1Canvas_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1CharacterHit
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CharacterHit)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CharacterHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1CharacterHit_FUNC);
	rc = (jint)TO_HANDLE(gcnew CharacterHit(arg0, arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1CharacterHit_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1CheckBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CheckBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CheckBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1CheckBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew CheckBox());
	OS_NATIVE_EXIT(env, that, gcnew_1CheckBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ColorDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColorDialog)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColorDialog)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ColorDialog_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::ColorDialog());
	OS_NATIVE_EXIT(env, that, gcnew_1ColorDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ColorList
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColorList)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColorList)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ColorList_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Collections::Generic::List<Color>(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1ColorList_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ColumnDefinition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColumnDefinition)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ColumnDefinition)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ColumnDefinition_FUNC);
	rc = (jint)TO_HANDLE(gcnew ColumnDefinition());
	OS_NATIVE_EXIT(env, that, gcnew_1ColumnDefinition_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1CombinedGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CombinedGeometry)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CombinedGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1CombinedGeometry_FUNC);
	rc = (jint)TO_HANDLE(gcnew CombinedGeometry((GeometryCombineMode)arg0, (Geometry^)TO_OBJECT(arg1), (Geometry^)TO_OBJECT(arg2)));
	OS_NATIVE_EXIT(env, that, gcnew_1CombinedGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ComboBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ComboBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ComboBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ComboBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew ComboBox());
	OS_NATIVE_EXIT(env, that, gcnew_1ComboBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ComboBoxItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ComboBoxItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ComboBoxItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ComboBoxItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew ComboBoxItem());
	OS_NATIVE_EXIT(env, that, gcnew_1ComboBoxItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1CompositeCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CompositeCollection)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CompositeCollection)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1CompositeCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew CompositeCollection());
	OS_NATIVE_EXIT(env, that, gcnew_1CompositeCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ContentControl
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ContentControl)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ContentControl)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ContentControl_FUNC);
	rc = (jint)TO_HANDLE(gcnew ContentControl());
	OS_NATIVE_EXIT(env, that, gcnew_1ContentControl_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ContextMenu
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ContextMenu)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ContextMenu)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ContextMenu_FUNC);
	rc = (jint)TO_HANDLE(gcnew ContextMenu());
	OS_NATIVE_EXIT(env, that, gcnew_1ContextMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ControlTemplate
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ControlTemplate)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ControlTemplate)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ControlTemplate_FUNC);
	rc = (jint)TO_HANDLE(gcnew ControlTemplate());
	OS_NATIVE_EXIT(env, that, gcnew_1ControlTemplate_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1CroppedBitmap
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CroppedBitmap)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1CroppedBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1CroppedBitmap_FUNC);
	rc = (jint)TO_HANDLE(gcnew CroppedBitmap((BitmapSource^)TO_OBJECT(arg0), (Int32Rect)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1CroppedBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DashStyle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DashStyle)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DashStyle)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DashStyle_FUNC);
	rc = (jint)TO_HANDLE(gcnew DashStyle((DoubleCollection^)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1DashStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DataObject
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DataObject)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DataObject)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DataObject_FUNC);
	rc = (jint)TO_HANDLE(gcnew DataObject());
	OS_NATIVE_EXIT(env, that, gcnew_1DataObject_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DataTemplate
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DataTemplate)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DataTemplate)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DataTemplate_FUNC);
	rc = (jint)TO_HANDLE(gcnew DataTemplate());
	OS_NATIVE_EXIT(env, that, gcnew_1DataTemplate_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DiscreteDoubleKeyFrame__
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteDoubleKeyFrame__)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteDoubleKeyFrame__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DiscreteDoubleKeyFrame___FUNC);
	rc = (jint)TO_HANDLE(gcnew DiscreteDoubleKeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1DiscreteDoubleKeyFrame___FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DiscreteDoubleKeyFrame__DI
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteDoubleKeyFrame__DI)(JNIEnv *env, jclass that, jdouble arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteDoubleKeyFrame__DI)
	(JNIEnv *env, jclass that, jdouble arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DiscreteDoubleKeyFrame__DI_FUNC);
	rc = (jint)TO_HANDLE(gcnew DiscreteDoubleKeyFrame(arg0, (KeyTime)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1DiscreteDoubleKeyFrame__DI_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DiscreteInt32KeyFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteInt32KeyFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DiscreteInt32KeyFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DiscreteInt32KeyFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew DiscreteInt32KeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1DiscreteInt32KeyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DispatcherFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DispatcherFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DispatcherFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DispatcherFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew DispatcherFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1DispatcherFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DispatcherTimer
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DispatcherTimer)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DispatcherTimer)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DispatcherTimer_FUNC);
	rc = (jint)TO_HANDLE(gcnew DispatcherTimer());
	OS_NATIVE_EXIT(env, that, gcnew_1DispatcherTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DoubleAnimation
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleAnimation)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleAnimation)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DoubleAnimation_FUNC);
	rc = (jint)TO_HANDLE(gcnew DoubleAnimation());
	OS_NATIVE_EXIT(env, that, gcnew_1DoubleAnimation_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DoubleAnimationUsingKeyFrames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleAnimationUsingKeyFrames)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleAnimationUsingKeyFrames)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DoubleAnimationUsingKeyFrames_FUNC);
	rc = (jint)TO_HANDLE(gcnew DoubleAnimationUsingKeyFrames());
	OS_NATIVE_EXIT(env, that, gcnew_1DoubleAnimationUsingKeyFrames_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DoubleCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleCollection)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DoubleCollection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DoubleCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew DoubleCollection(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1DoubleCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DrawingVisual
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DrawingVisual)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DrawingVisual)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DrawingVisual_FUNC);
	rc = (jint)TO_HANDLE(gcnew DrawingVisual());
	OS_NATIVE_EXIT(env, that, gcnew_1DrawingVisual_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1DropShadowBitmapEffect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DropShadowBitmapEffect)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1DropShadowBitmapEffect)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1DropShadowBitmapEffect_FUNC);
	rc = (jint)TO_HANDLE(gcnew DropShadowBitmapEffect());
	OS_NATIVE_EXIT(env, that, gcnew_1DropShadowBitmapEffect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Duration
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Duration)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Duration)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Duration_FUNC);
	rc = (jint)TO_HANDLE(gcnew Duration((TimeSpan)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1Duration_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1EllipseGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1EllipseGeometry)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1EllipseGeometry)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1EllipseGeometry_FUNC);
	rc = (jint)TO_HANDLE(gcnew EllipseGeometry((Rect)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1EllipseGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Expander
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Expander)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Expander)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Expander_FUNC);
	rc = (jint)TO_HANDLE(gcnew Expander());
	OS_NATIVE_EXIT(env, that, gcnew_1Expander_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FileInfo
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FileInfo)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FileInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FileInfo_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::IO::FileInfo((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1FileInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FolderBrowserDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FolderBrowserDialog)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FolderBrowserDialog)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FolderBrowserDialog_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::FolderBrowserDialog());
	OS_NATIVE_EXIT(env, that, gcnew_1FolderBrowserDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Font
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Font)(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Font)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Font_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Drawing::Font((String^)TO_OBJECT(arg0), arg1, (System::Drawing::FontStyle)arg2));
	OS_NATIVE_EXIT(env, that, gcnew_1Font_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FontDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FontDialog)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FontDialog)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FontDialog_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::FontDialog());
	OS_NATIVE_EXIT(env, that, gcnew_1FontDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FontFamily
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FontFamily)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FontFamily)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FontFamily_FUNC);
	rc = (jint)TO_HANDLE(gcnew FontFamily((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1FontFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FormatConvertedBitmap
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FormatConvertedBitmap)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FormatConvertedBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FormatConvertedBitmap_FUNC);
	rc = (jint)TO_HANDLE(gcnew FormatConvertedBitmap((BitmapSource^)TO_OBJECT(arg0), (PixelFormat)TO_OBJECT(arg1), (BitmapPalette^)TO_OBJECT(arg2), arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1FormatConvertedBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FormattedText
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FormattedText)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jint arg5);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FormattedText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FormattedText_FUNC);
	rc = (jint)TO_HANDLE(gcnew FormattedText((String^)TO_OBJECT(arg0), (CultureInfo^)TO_OBJECT(arg1), (FlowDirection)arg2, (Typeface^)TO_OBJECT(arg3), arg4, (Brush^)TO_OBJECT(arg5)));
	OS_NATIVE_EXIT(env, that, gcnew_1FormattedText_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Frame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Frame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Frame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Frame_FUNC);
	rc = (jint)TO_HANDLE(gcnew Frame());
	OS_NATIVE_EXIT(env, that, gcnew_1Frame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FrameworkElementFactory__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FrameworkElementFactory__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FrameworkElementFactory__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FrameworkElementFactory__I_FUNC);
	rc = (jint)TO_HANDLE(gcnew FrameworkElementFactory((Type^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1FrameworkElementFactory__I_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1FrameworkElementFactory__II
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FrameworkElementFactory__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1FrameworkElementFactory__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1FrameworkElementFactory__II_FUNC);
	rc = (jint)TO_HANDLE(gcnew FrameworkElementFactory((Type^)TO_OBJECT(arg0), (String^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1FrameworkElementFactory__II_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GeometryGroup
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GeometryGroup)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GeometryGroup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GeometryGroup_FUNC);
	rc = (jint)TO_HANDLE(gcnew GeometryGroup());
	OS_NATIVE_EXIT(env, that, gcnew_1GeometryGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Grid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Grid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Grid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Grid_FUNC);
	rc = (jint)TO_HANDLE(gcnew Grid());
	OS_NATIVE_EXIT(env, that, gcnew_1Grid_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GridLength
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridLength)(JNIEnv *env, jclass that, jdouble arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridLength)
	(JNIEnv *env, jclass that, jdouble arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GridLength_FUNC);
	rc = (jint)TO_HANDLE(gcnew GridLength(arg0, (GridUnitType)arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1GridLength_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GridView
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridView)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridView)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GridView_FUNC);
	rc = (jint)TO_HANDLE(gcnew GridView());
	OS_NATIVE_EXIT(env, that, gcnew_1GridView_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GridViewColumn
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumn)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumn)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GridViewColumn_FUNC);
	rc = (jint)TO_HANDLE(gcnew GridViewColumn());
	OS_NATIVE_EXIT(env, that, gcnew_1GridViewColumn_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GridViewColumnCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumnCollection)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumnCollection)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GridViewColumnCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew GridViewColumnCollection());
	OS_NATIVE_EXIT(env, that, gcnew_1GridViewColumnCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GridViewColumnHeader
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumnHeader)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GridViewColumnHeader)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GridViewColumnHeader_FUNC);
	rc = (jint)TO_HANDLE(gcnew GridViewColumnHeader());
	OS_NATIVE_EXIT(env, that, gcnew_1GridViewColumnHeader_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1GroupBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GroupBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1GroupBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1GroupBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew GroupBox());
	OS_NATIVE_EXIT(env, that, gcnew_1GroupBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Hyperlink
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Hyperlink)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Hyperlink)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Hyperlink_FUNC);
	rc = (jint)TO_HANDLE(gcnew Hyperlink((Inline^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1Hyperlink_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Image
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Image)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Image)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Image_FUNC);
	rc = (jint)TO_HANDLE(gcnew Image());
	OS_NATIVE_EXIT(env, that, gcnew_1Image_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ImageBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ImageBrush)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ImageBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ImageBrush_FUNC);
	rc = (jint)TO_HANDLE(gcnew ImageBrush((ImageSource^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1ImageBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Int32
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Int32_FUNC);
	rc = (jint)TO_HANDLE(gcnew Int32(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1Int32_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Int32Animation
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32Animation)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32Animation)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Int32Animation_FUNC);
	rc = (jint)TO_HANDLE(gcnew Int32Animation());
	OS_NATIVE_EXIT(env, that, gcnew_1Int32Animation_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Int32AnimationUsingKeyFrames
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32AnimationUsingKeyFrames)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32AnimationUsingKeyFrames)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Int32AnimationUsingKeyFrames_FUNC);
	rc = (jint)TO_HANDLE(gcnew Int32AnimationUsingKeyFrames());
	OS_NATIVE_EXIT(env, that, gcnew_1Int32AnimationUsingKeyFrames_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Int32Rect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32Rect)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Int32Rect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Int32Rect_FUNC);
	rc = (jint)TO_HANDLE(gcnew Int32Rect(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1Int32Rect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1IntPtr
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1IntPtr)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1IntPtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1IntPtr_FUNC);
	rc = (jint)TO_HANDLE(gcnew IntPtr(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1IntPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1KeySpline
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1KeySpline)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1KeySpline)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1KeySpline_FUNC);
	rc = (jint)TO_HANDLE(gcnew KeySpline(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1KeySpline_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Label
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Label)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Label)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Label_FUNC);
	rc = (jint)TO_HANDLE(gcnew Label());
	OS_NATIVE_EXIT(env, that, gcnew_1Label_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1LineSegment
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LineSegment)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LineSegment)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1LineSegment_FUNC);
	rc = (jint)TO_HANDLE(gcnew LineSegment((Point)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1LineSegment_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1LinearDoubleKeyFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearDoubleKeyFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearDoubleKeyFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1LinearDoubleKeyFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew LinearDoubleKeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1LinearDoubleKeyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1LinearGradientBrush__IID
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearGradientBrush__IID)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearGradientBrush__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1LinearGradientBrush__IID_FUNC);
	rc = (jint)TO_HANDLE(gcnew LinearGradientBrush((Color)TO_OBJECT(arg0), (Color)TO_OBJECT(arg1), arg2));
	OS_NATIVE_EXIT(env, that, gcnew_1LinearGradientBrush__IID_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1LinearGradientBrush__IIII
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearGradientBrush__IIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearGradientBrush__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1LinearGradientBrush__IIII_FUNC);
	rc = (jint)TO_HANDLE(gcnew LinearGradientBrush((Color)TO_OBJECT(arg0), (Color)TO_OBJECT(arg1), (Point)TO_OBJECT(arg2), (Point)TO_OBJECT(arg3)));
	OS_NATIVE_EXIT(env, that, gcnew_1LinearGradientBrush__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1LinearInt32KeyFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearInt32KeyFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1LinearInt32KeyFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1LinearInt32KeyFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew LinearInt32KeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1LinearInt32KeyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ListBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ListBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew ListBox());
	OS_NATIVE_EXIT(env, that, gcnew_1ListBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ListBoxItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListBoxItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListBoxItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ListBoxItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew ListBoxItem());
	OS_NATIVE_EXIT(env, that, gcnew_1ListBoxItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ListView
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListView)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListView)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ListView_FUNC);
	rc = (jint)TO_HANDLE(gcnew ListView());
	OS_NATIVE_EXIT(env, that, gcnew_1ListView_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ListViewItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListViewItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ListViewItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ListViewItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew ListViewItem());
	OS_NATIVE_EXIT(env, that, gcnew_1ListViewItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Matrix
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Matrix)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Matrix)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Matrix_FUNC);
	rc = (jint)TO_HANDLE(gcnew Matrix(arg0, arg1, arg2, arg3, arg4, arg5));
	OS_NATIVE_EXIT(env, that, gcnew_1Matrix_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1MatrixTransform
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MatrixTransform)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MatrixTransform)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1MatrixTransform_FUNC);
	rc = (jint)TO_HANDLE(gcnew MatrixTransform((Matrix)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1MatrixTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1MemoryStream
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MemoryStream)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MemoryStream)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1MemoryStream_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::IO::MemoryStream());
	OS_NATIVE_EXIT(env, that, gcnew_1MemoryStream_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Menu
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Menu)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Menu)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Menu_FUNC);
	rc = (jint)TO_HANDLE(gcnew Menu());
	OS_NATIVE_EXIT(env, that, gcnew_1Menu_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1MenuItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MenuItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1MenuItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1MenuItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew MenuItem());
	OS_NATIVE_EXIT(env, that, gcnew_1MenuItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1NameScope
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1NameScope)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1NameScope)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1NameScope_FUNC);
	rc = (jint)TO_HANDLE(gcnew NameScope());
	OS_NATIVE_EXIT(env, that, gcnew_1NameScope_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1NotifyIcon
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1NotifyIcon)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1NotifyIcon)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1NotifyIcon_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::NotifyIcon());
	OS_NATIVE_EXIT(env, that, gcnew_1NotifyIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1OpenFileDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1OpenFileDialog)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1OpenFileDialog)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1OpenFileDialog_FUNC);
	rc = (jint)TO_HANDLE(gcnew OpenFileDialog());
	OS_NATIVE_EXIT(env, that, gcnew_1OpenFileDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1OuterGlowBitmapEffect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1OuterGlowBitmapEffect)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1OuterGlowBitmapEffect)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1OuterGlowBitmapEffect_FUNC);
	rc = (jint)TO_HANDLE(gcnew OuterGlowBitmapEffect());
	OS_NATIVE_EXIT(env, that, gcnew_1OuterGlowBitmapEffect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PasswordBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PasswordBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PasswordBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PasswordBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew PasswordBox());
	OS_NATIVE_EXIT(env, that, gcnew_1PasswordBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Path
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Path)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Path)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Path_FUNC);
	rc = (jint)TO_HANDLE(gcnew Path());
	OS_NATIVE_EXIT(env, that, gcnew_1Path_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PathFigure
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PathFigure)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PathFigure)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PathFigure_FUNC);
	rc = (jint)TO_HANDLE(gcnew PathFigure());
	OS_NATIVE_EXIT(env, that, gcnew_1PathFigure_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PathGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PathGeometry)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PathGeometry)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PathGeometry_FUNC);
	rc = (jint)TO_HANDLE(gcnew PathGeometry());
	OS_NATIVE_EXIT(env, that, gcnew_1PathGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Pen__
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Pen__)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Pen__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Pen___FUNC);
	rc = (jint)TO_HANDLE(gcnew Pen());
	OS_NATIVE_EXIT(env, that, gcnew_1Pen___FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Pen__ID
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Pen__ID)(JNIEnv *env, jclass that, jint arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Pen__ID)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Pen__ID_FUNC);
	rc = (jint)TO_HANDLE(gcnew Pen((Brush^)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1Pen__ID_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Point
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Point)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Point)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Point_FUNC);
	rc = (jint)TO_HANDLE(gcnew Point(arg0, arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1Point_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PointCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PointCollection)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PointCollection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PointCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew PointCollection(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1PointCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PolyLineSegment
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PolyLineSegment)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PolyLineSegment)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PolyLineSegment_FUNC);
	rc = (jint)TO_HANDLE(gcnew PolyLineSegment((PointCollection^)TO_OBJECT(arg0), arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1PolyLineSegment_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Popup
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Popup)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Popup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Popup_FUNC);
	rc = (jint)TO_HANDLE(gcnew Popup());
	OS_NATIVE_EXIT(env, that, gcnew_1Popup_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ProgressBar
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ProgressBar)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ProgressBar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ProgressBar_FUNC);
	rc = (jint)TO_HANDLE(gcnew ProgressBar());
	OS_NATIVE_EXIT(env, that, gcnew_1ProgressBar_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1PropertyPath
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PropertyPath)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1PropertyPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1PropertyPath_FUNC);
	rc = (jint)TO_HANDLE(gcnew PropertyPath((Object^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1PropertyPath_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1QuadraticBezierSegment
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1QuadraticBezierSegment)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1QuadraticBezierSegment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1QuadraticBezierSegment_FUNC);
	rc = (jint)TO_HANDLE(gcnew QuadraticBezierSegment((Point)TO_OBJECT(arg0), (Point)TO_OBJECT(arg1), arg2));
	OS_NATIVE_EXIT(env, that, gcnew_1QuadraticBezierSegment_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RadioButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RadioButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RadioButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RadioButton_FUNC);
	rc = (jint)TO_HANDLE(gcnew RadioButton());
	OS_NATIVE_EXIT(env, that, gcnew_1RadioButton_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Rect
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Rect)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Rect)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Rect_FUNC);
	rc = (jint)TO_HANDLE(gcnew Rect(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1Rect_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Rectangle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Rectangle)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Rectangle)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Rectangle_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Shapes::Rectangle());
	OS_NATIVE_EXIT(env, that, gcnew_1Rectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RectangleGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RectangleGeometry)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RectangleGeometry)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RectangleGeometry_FUNC);
	rc = (jint)TO_HANDLE(gcnew RectangleGeometry((Rect)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1RectangleGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RelativeSource
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RelativeSource)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RelativeSource)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RelativeSource_FUNC);
	rc = (jint)TO_HANDLE(gcnew RelativeSource((RelativeSourceMode)arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1RelativeSource_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RenderTargetBitmap
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RenderTargetBitmap)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RenderTargetBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RenderTargetBitmap_FUNC);
	rc = (jint)TO_HANDLE(gcnew RenderTargetBitmap(arg0, arg1, arg2, arg3, (PixelFormat)TO_OBJECT(arg4)));
	OS_NATIVE_EXIT(env, that, gcnew_1RenderTargetBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RepeatBehavior
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RepeatBehavior)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RepeatBehavior)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RepeatBehavior_FUNC);
	rc = (jint)TO_HANDLE(gcnew RepeatBehavior(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1RepeatBehavior_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RepeatButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RepeatButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RepeatButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RepeatButton_FUNC);
	rc = (jint)TO_HANDLE(gcnew RepeatButton());
	OS_NATIVE_EXIT(env, that, gcnew_1RepeatButton_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ResourceDictionary
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ResourceDictionary)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ResourceDictionary)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ResourceDictionary_FUNC);
	rc = (jint)TO_HANDLE(gcnew ResourceDictionary());
	OS_NATIVE_EXIT(env, that, gcnew_1ResourceDictionary_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1RowDefinition
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RowDefinition)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1RowDefinition)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1RowDefinition_FUNC);
	rc = (jint)TO_HANDLE(gcnew RowDefinition());
	OS_NATIVE_EXIT(env, that, gcnew_1RowDefinition_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Run
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Run)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Run)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Run_FUNC);
	rc = (jint)TO_HANDLE(gcnew Run());
	OS_NATIVE_EXIT(env, that, gcnew_1Run_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SaveFileDialog
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SaveFileDialog)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SaveFileDialog)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SaveFileDialog_FUNC);
	rc = (jint)TO_HANDLE(gcnew SaveFileDialog());
	OS_NATIVE_EXIT(env, that, gcnew_1SaveFileDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ScaleTransform
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScaleTransform)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScaleTransform)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ScaleTransform_FUNC);
	rc = (jint)TO_HANDLE(gcnew ScaleTransform(arg0, arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1ScaleTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ScrollBar
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScrollBar)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScrollBar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ScrollBar_FUNC);
	rc = (jint)TO_HANDLE(gcnew ScrollBar());
	OS_NATIVE_EXIT(env, that, gcnew_1ScrollBar_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ScrollViewer
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScrollViewer)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ScrollViewer)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ScrollViewer_FUNC);
	rc = (jint)TO_HANDLE(gcnew ScrollViewer());
	OS_NATIVE_EXIT(env, that, gcnew_1ScrollViewer_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Separator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Separator)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Separator)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Separator_FUNC);
	rc = (jint)TO_HANDLE(gcnew Separator());
	OS_NATIVE_EXIT(env, that, gcnew_1Separator_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Setter
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Setter)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Setter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Setter_FUNC);
	rc = (jint)TO_HANDLE(gcnew Setter((DependencyProperty^)TO_OBJECT(arg0), (Object^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1Setter_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SetterVisibility
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SetterVisibility)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SetterVisibility)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SetterVisibility_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Setter((DependencyProperty^)TO_OBJECT(arg0), (Visibility)arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1SetterVisibility_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Size__
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Size__)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Size__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Size___FUNC);
	rc = (jint)TO_HANDLE(gcnew Size());
	OS_NATIVE_EXIT(env, that, gcnew_1Size___FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Size__DD
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Size__DD)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Size__DD)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Size__DD_FUNC);
	rc = (jint)TO_HANDLE(gcnew Size(arg0, arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1Size__DD_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Slider
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Slider)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Slider)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Slider_FUNC);
	rc = (jint)TO_HANDLE(gcnew Slider());
	OS_NATIVE_EXIT(env, that, gcnew_1Slider_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SolidColorBrush
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SolidColorBrush)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SolidColorBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SolidColorBrush_FUNC);
	rc = (jint)TO_HANDLE(gcnew SolidColorBrush((Color)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1SolidColorBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SplineDoubleKeyFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SplineDoubleKeyFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SplineDoubleKeyFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SplineDoubleKeyFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew SplineDoubleKeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1SplineDoubleKeyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SplineInt32KeyFrame
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SplineInt32KeyFrame)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SplineInt32KeyFrame)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SplineInt32KeyFrame_FUNC);
	rc = (jint)TO_HANDLE(gcnew SplineInt32KeyFrame());
	OS_NATIVE_EXIT(env, that, gcnew_1SplineInt32KeyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1StackPanel
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StackPanel)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StackPanel)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1StackPanel_FUNC);
	rc = (jint)TO_HANDLE(gcnew StackPanel());
	OS_NATIVE_EXIT(env, that, gcnew_1StackPanel_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Storyboard
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Storyboard)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Storyboard)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Storyboard_FUNC);
	rc = (jint)TO_HANDLE(gcnew Storyboard());
	OS_NATIVE_EXIT(env, that, gcnew_1Storyboard_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1StreamGeometry
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StreamGeometry)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StreamGeometry)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1StreamGeometry_FUNC);
	rc = (jint)TO_HANDLE(gcnew StreamGeometry());
	OS_NATIVE_EXIT(env, that, gcnew_1StreamGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1String___3C
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1String___3C)(JNIEnv *env, jclass that, jcharArray arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1String___3C)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1String___3C_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)TO_HANDLE(gcnew String((const wchar_t *)lparg0));
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, gcnew_1String___3C_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1String___3CII
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1String___3CII)(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1String___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1String___3CII_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)TO_HANDLE(gcnew String((const wchar_t *)lparg0, arg1, arg2));
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, gcnew_1String___3CII_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1StringReader
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StringReader)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1StringReader)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1StringReader_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::IO::StringReader((String^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1StringReader_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Style
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Style)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Style)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Style_FUNC);
	rc = (jint)TO_HANDLE(gcnew Style());
	OS_NATIVE_EXIT(env, that, gcnew_1Style_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TabControl
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TabControl)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TabControl)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TabControl_FUNC);
	rc = (jint)TO_HANDLE(gcnew TabControl());
	OS_NATIVE_EXIT(env, that, gcnew_1TabControl_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TabItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TabItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TabItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TabItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew TabItem());
	OS_NATIVE_EXIT(env, that, gcnew_1TabItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TemplateBindingExtension
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TemplateBindingExtension)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TemplateBindingExtension)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TemplateBindingExtension_FUNC);
	rc = (jint)TO_HANDLE(gcnew TemplateBindingExtension((DependencyProperty^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1TemplateBindingExtension_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextBlock
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextBlock)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextBlock)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextBlock_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextBlock());
	OS_NATIVE_EXIT(env, that, gcnew_1TextBlock_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextBox
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextBox)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextBox_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextBox());
	OS_NATIVE_EXIT(env, that, gcnew_1TextBox_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextCharacters
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextCharacters)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextCharacters)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextCharacters_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextCharacters((String^)TO_OBJECT(arg0), arg1, arg2, (TextRunProperties^)TO_OBJECT(arg3)));
	OS_NATIVE_EXIT(env, that, gcnew_1TextCharacters_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextDecoration
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextDecoration)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jint arg3, jint arg4);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextDecoration)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextDecoration_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextDecoration((TextDecorationLocation)arg0, (Pen^)TO_OBJECT(arg1), arg2, (TextDecorationUnit)arg3, (TextDecorationUnit)arg4));
	OS_NATIVE_EXIT(env, that, gcnew_1TextDecoration_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextDecorationCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextDecorationCollection)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextDecorationCollection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextDecorationCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextDecorationCollection(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1TextDecorationCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextEndOfLine
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextEndOfLine)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextEndOfLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextEndOfLine_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextEndOfLine(arg0, (TextRunProperties^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1TextEndOfLine_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextEndOfParagraph
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextEndOfParagraph)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextEndOfParagraph)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextEndOfParagraph_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextEndOfParagraph(arg0, (TextRunProperties^)TO_OBJECT(arg1)));
	OS_NATIVE_EXIT(env, that, gcnew_1TextEndOfParagraph_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextTabProperties
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextTabProperties)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextTabProperties)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextTabProperties_FUNC);
	rc = (jint)TO_HANDLE(gcnew TextTabProperties((TextTabAlignment)arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1TextTabProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TextTabPropertiesCollection
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextTabPropertiesCollection)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TextTabPropertiesCollection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TextTabPropertiesCollection_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Collections::Generic::List<TextTabProperties^>(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1TextTabPropertiesCollection_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Thickness
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Thickness)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Thickness)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Thickness_FUNC);
	rc = (jint)TO_HANDLE(gcnew Thickness(arg0, arg1, arg2, arg3));
	OS_NATIVE_EXIT(env, that, gcnew_1Thickness_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TiffBitmapEncoder
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TiffBitmapEncoder)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TiffBitmapEncoder)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TiffBitmapEncoder_FUNC);
	rc = (jint)TO_HANDLE(gcnew TiffBitmapEncoder());
	OS_NATIVE_EXIT(env, that, gcnew_1TiffBitmapEncoder_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TimeSpan
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TimeSpan)(JNIEnv *env, jclass that, jlong arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TimeSpan)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TimeSpan_FUNC);
	rc = (jint)TO_HANDLE(gcnew TimeSpan(arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1TimeSpan_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ToggleButton
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToggleButton)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToggleButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ToggleButton_FUNC);
	rc = (jint)TO_HANDLE(gcnew ToggleButton());
	OS_NATIVE_EXIT(env, that, gcnew_1ToggleButton_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ToolBar
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToolBar)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToolBar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ToolBar_FUNC);
	rc = (jint)TO_HANDLE(gcnew ToolBar());
	OS_NATIVE_EXIT(env, that, gcnew_1ToolBar_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1ToolBarTray
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToolBarTray)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1ToolBarTray)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1ToolBarTray_FUNC);
	rc = (jint)TO_HANDLE(gcnew ToolBarTray());
	OS_NATIVE_EXIT(env, that, gcnew_1ToolBarTray_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TransformGroup
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TransformGroup)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TransformGroup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TransformGroup_FUNC);
	rc = (jint)TO_HANDLE(gcnew TransformGroup());
	OS_NATIVE_EXIT(env, that, gcnew_1TransformGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TranslateTransform
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TranslateTransform)(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TranslateTransform)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TranslateTransform_FUNC);
	rc = (jint)TO_HANDLE(gcnew TranslateTransform(arg0, arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1TranslateTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TraversalRequest
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TraversalRequest)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TraversalRequest)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TraversalRequest_FUNC);
	rc = (jint)TO_HANDLE(gcnew TraversalRequest((FocusNavigationDirection)arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1TraversalRequest_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TreeView
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TreeView)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TreeView)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TreeView_FUNC);
	rc = (jint)TO_HANDLE(gcnew TreeView());
	OS_NATIVE_EXIT(env, that, gcnew_1TreeView_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1TreeViewItem
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TreeViewItem)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TreeViewItem)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TreeViewItem_FUNC);
	rc = (jint)TO_HANDLE(gcnew TreeViewItem());
	OS_NATIVE_EXIT(env, that, gcnew_1TreeViewItem_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Typeface
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Typeface)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Typeface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Typeface_FUNC);
	rc = (jint)TO_HANDLE(gcnew Typeface((FontFamily^)TO_OBJECT(arg0), (FontStyle)TO_OBJECT(arg1), (FontWeight)TO_OBJECT(arg2), (FontStretch)TO_OBJECT(arg3)));
	OS_NATIVE_EXIT(env, that, gcnew_1Typeface_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Uri
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Uri)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Uri)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Uri_FUNC);
	rc = (jint)TO_HANDLE(gcnew Uri((String^)TO_OBJECT(arg0), (UriKind)arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1Uri_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1UserControl
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1UserControl)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1UserControl)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1UserControl_FUNC);
	rc = (jint)TO_HANDLE(gcnew UserControl());
	OS_NATIVE_EXIT(env, that, gcnew_1UserControl_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1WebBrowser
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WebBrowser)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WebBrowser)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1WebBrowser_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::WebBrowser());
	OS_NATIVE_EXIT(env, that, gcnew_1WebBrowser_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1Window
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Window)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1Window)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1Window_FUNC);
	rc = (jint)TO_HANDLE(gcnew Window());
	OS_NATIVE_EXIT(env, that, gcnew_1Window_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1WindowsFormsHost
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WindowsFormsHost)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WindowsFormsHost)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1WindowsFormsHost_FUNC);
	rc = (jint)TO_HANDLE(gcnew System::Windows::Forms::Integration::WindowsFormsHost());
	OS_NATIVE_EXIT(env, that, gcnew_1WindowsFormsHost_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1WriteableBitmap__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WriteableBitmap__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WriteableBitmap__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1WriteableBitmap__I_FUNC);
	rc = (jint)TO_HANDLE(gcnew WriteableBitmap((BitmapSource^)TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1WriteableBitmap__I_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1WriteableBitmap__IIDDII
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WriteableBitmap__IIDDII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1WriteableBitmap__IIDDII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1WriteableBitmap__IIDDII_FUNC);
	rc = (jint)TO_HANDLE(gcnew WriteableBitmap(arg0, arg1, arg2, arg3, (PixelFormat)TO_OBJECT(arg4), (BitmapPalette^)TO_OBJECT(arg5)));
	OS_NATIVE_EXIT(env, that, gcnew_1WriteableBitmap__IIDDII_FUNC);
	return rc;
}
#endif

