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
#include "string.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_wpf_OS_##func

static JavaVM *jvm = NULL;

/*												*/
/* SWT Handle Table     						*/
/*												*/
#ifndef GCHANDLE_TABLE
#define GCHANDLE_STACKS
public ref class SWTObjectTable {
private:
	static int nextHandle = 0;
	static array<Object^>^table = nullptr;
	static Object^ mutex = gcnew Object();

#ifdef GCHANDLE_STACKS
	static array<int>^exceptions = nullptr;
#endif
	
public:
static int ToHandle(Object^ obj) {
	if (obj == nullptr) return 0;
	System::Threading::Monitor::Enter(mutex);
	if (table == nullptr || nextHandle == -1) {
		int length = 0;
		if (table != nullptr) length = table->GetLength(0);		
		int newLength = length * 2;
		if (newLength < 1024) newLength = 1024;
//		System::Console::Error->WriteLine("\t\t***grow={1}", length, newLength);
		Array::Resize(table, newLength);
#ifdef GCHANDLE_STACKS
		Array::Resize(exceptions, newLength);
#endif
		for (int i=length; i<newLength-1; i++) table[i] = i + 1;
		table[newLength-1] = -1;
		nextHandle = length;
	}
	int handle = nextHandle;
	nextHandle = (int)(Int32)table[handle];
	table[handle] = obj;
#ifdef GCHANDLE_STACKS
	if (jvm) {
		JNIEnv* env;
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
		jclass exceptionClass = env->FindClass("java/lang/Exception");
		exceptions[handle] = (int)env->NewGlobalRef(env->NewObject(exceptionClass, env->GetMethodID(exceptionClass, "<init>", "()V")));
	}
#endif
	System::Threading::Monitor::Exit(mutex);
	return handle + 1;	
}

static Object^ ToObject(int handle) {
	System::Threading::Monitor::Enter(mutex);
	Object^ result = nullptr;
	if (handle > 0) result = table[handle - 1];
	System::Threading::Monitor::Exit(mutex);
	return result;
}

static void Free(int handle) {
	System::Threading::Monitor::Enter(mutex);
	if (handle > 0) {
		table[handle - 1] = nextHandle;
		nextHandle = handle - 1;
#ifdef GCHANDLE_STACKS
		if (exceptions[handle - 1] != 0) {
			JNIEnv* env;
			if (IS_JNI_1_2) {
				jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
			}
			env->DeleteGlobalRef((jobject)exceptions[handle - 1]);
			exceptions[handle - 1] = 0;
		}
#endif
	}
	System::Threading::Monitor::Exit(mutex);
}

static void Dump() {
	System::Threading::Monitor::Enter(mutex);
	for (int i=0; i<table->GetLength(0); i++) {
		if (table[i]->GetType() != Int32::typeid) {
			System::Console::Error->WriteLine("LEAK -> {0}={1} type={2}", i + 1, table[i], table[i]->GetType());
#ifdef GCHANDLE_STACKS
			if (exceptions[i] != 0) {
				JNIEnv* env;
				jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
				jclass exceptionClass = env->FindClass("java/lang/Throwable");
				jmethodID mid = env->GetMethodID(exceptionClass, "printStackTrace", "()V");
				if (mid != NULL) env->CallVoidMethod((jobject)exceptions[i], mid, 0);
			}
#endif
		}
	}
	System::Threading::Monitor::Exit(mutex);
}

};
#endif // GCHANDLE_TABLE

extern "C" {

#ifdef GCHANDLE_TABLE

jint GCHandle_GetHandle(Object^ obj) {
	return obj == nullptr ? 0 : (int)GCHandle::ToIntPtr(GCHandle::Alloc(obj));
}

#else

int SWTObjectTable_ToHandle(Object^ obj) {
	return SWTObjectTable::ToHandle(obj);
}

Object^ SWTObjectTable_ToObject(int handle) {
	return SWTObjectTable::ToObject(handle);
}

void SWTObjectTable_Free(int handle) {
	return SWTObjectTable::Free(handle);
}

#endif // GCHANDLE_TABLE

} // extern "C" ends

/*												*/
/* JNI Ref Cookie       						*/
/*												*/
public ref class JniRefCookie {
public:
	jobject object;
	
	JniRefCookie (JNIEnv* env, jint jniRef) {
		if (jvm == NULL) env->GetJavaVM(&jvm);
		this->object = env->NewGlobalRef((jobject)jniRef);
	}

	~JniRefCookie() {
		this->!JniRefCookie();
	}

	!JniRefCookie() {
		if (object == NULL) return; 	
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			env->DeleteGlobalRef(object);
			if (detach) jvm->DetachCurrentThread();
		}
	}
};


/*												*/
/* Animation									*/
/*												*/

public ref class SWTAnimator : FrameworkElement {
public:
	static DependencyProperty^ DoubleValueProperty = DependencyProperty::Register("DoubleValue", double::typeid, SWTAnimator::typeid, gcnew PropertyMetadata(gcnew PropertyChangedCallback(OnPropertyChanged)));
	static DependencyProperty^ IntValueProperty = DependencyProperty::Register("IntValue", Int32::typeid, SWTAnimator::typeid, gcnew PropertyMetadata(gcnew PropertyChangedCallback(OnPropertyChanged)));
private:
	jmethodID OnPropertyChangedMID;
	JniRefCookie^ cookie;
public:		
	SWTAnimator(JNIEnv* env, jint jniRef) {
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (object) {
			jclass javaClass = env->GetObjectClass(object);
			OnPropertyChangedMID = env->GetMethodID(javaClass, "OnPropertyChanged", "(II)V");
		}
	}
	
	static void OnPropertyChanged(DependencyObject^ obj, DependencyPropertyChangedEventArgs args) {
		SWTAnimator^ animator = (SWTAnimator^) obj;
		animator->callin(obj, args);
	}
	
private:
	void callin (DependencyObject^ obj, DependencyPropertyChangedEventArgs args) {
		jobject object = cookie->object;
		if (object == NULL || OnPropertyChangedMID == NULL) return;
		JNIEnv* env;
		int result = 0;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				int arg0 = TO_HANDLE(obj);
				int arg1 = TO_HANDLE(args);
				env->CallVoidMethod(object, OnPropertyChangedMID, arg0, arg1);
				FREE_HANDLE(arg0);
				FREE_HANDLE(arg1);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}
};

public ref class SWTAnimation : DoubleAnimation {
private:
	JniRefCookie^ cookie;
	jmethodID GetCurrentValueCoreMID;
public: 
	SWTAnimation (JNIEnv* env, jint jniRef) {
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (object) {
			jclass javaClass = env->GetObjectClass(object);
			GetCurrentValueCoreMID = env->GetMethodID(javaClass, "GetCurrentValueCore", "(D)V");
		}
	}
private:
	SWTAnimation (JniRefCookie^ cookie, jmethodID methodID) {
		this->cookie = cookie;
		this->GetCurrentValueCoreMID = methodID;
	}
protected:
	virtual Freezable^ CreateInstanceCore () override {
		return gcnew SWTAnimation(cookie, GetCurrentValueCoreMID);
	}
	virtual double GetCurrentValueCore(double fromVal, double toVal, AnimationClock^ clock) override {
		Nullable<TimeSpan> currentTime = clock->CurrentTime;
		if (currentTime.HasValue) {
			double ms = currentTime.Value.TotalMilliseconds;
			callin(ms);
		}
		return DoubleAnimation::GetCurrentValueCore(fromVal, toVal, clock);
	}
private:
	void callin (double args) {
		jobject object = cookie->object;
		if (object == NULL || GetCurrentValueCoreMID == NULL) return;
		JNIEnv* env;
		int result = 0;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				env->CallVoidMethod(object, GetCurrentValueCoreMID, args);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}	
};

/*												*/
/* Table and Tree Classes						*/
/*												*/

public ref class SWTDockPanel : DockPanel {
public: 
	static DependencyProperty^ JNIRefProperty = DependencyProperty::Register("JNIRef", Int32::typeid, SWTDockPanel::typeid);
private:
	jmethodID OnRenderMID;
	JniRefCookie^ cookie;

	void callin (jmethodID mid, DrawingContext^ context) {
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (cookie == nullptr) {
				cookie = gcnew JniRefCookie(env, (jint)(Int32)GetValue(SWTDockPanel::JNIRefProperty));
			}
			jobject object = cookie->object;
			if (object == NULL) { 
			 	return;
			}
			if (mid == NULL){
				jclass javaClass = env->GetObjectClass(object);
				OnRenderMID = env->GetMethodID(javaClass, "OnRender", "(II)V");
				mid = OnRenderMID;
			}
			if (!env->ExceptionOccurred()) {
				int arg0 = TO_HANDLE(this); 
				int arg1 = TO_HANDLE(context);
				env->CallVoidMethod(object, mid, arg0, arg1);
				FREE_HANDLE(arg0);
				FREE_HANDLE(arg1);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}
protected:
	virtual void OnRender(DrawingContext^ drawingContext) override {
		this->DockPanel::OnRender(drawingContext);
		callin(OnRenderMID, drawingContext);
	}
};

public ref class SWTTreeView : TreeView {
private:
	JniRefCookie^ cookie;
	jmethodID HandlerMID;
	
	void callin (jmethodID mid, RoutedPropertyChangedEventArgs<Object^>^ args) {
		jobject object = cookie->object;
		if (object == NULL || mid == NULL) return;
		JNIEnv* env;
		int result = 0;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				int arg0 = TO_HANDLE(args);
				env->CallVoidMethod(object, mid, arg0);
				FREE_HANDLE(arg0);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}	
protected:
	virtual void OnSelectedItemChanged (RoutedPropertyChangedEventArgs<Object^>^ args) override {
		callin(HandlerMID, args);
		TreeView::OnSelectedItemChanged(args);
	}
public:
	SWTTreeView (JNIEnv* env, jint jniRef) {
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (object) {
			jclass javaClass = env->GetObjectClass(object);
			HandlerMID = env->GetMethodID(javaClass, "OnSelectedItemChanged", "(I)V");
		}
	}	
};

public ref class SWTTreeViewRowPresenter : GridViewRowPresenter {
private: 
	TreeView^ tree;
	static void Handle_Loaded (Object^ sender, RoutedEventArgs^ args) {
		SWTTreeViewRowPresenter^ presenter = (SWTTreeViewRowPresenter^) sender;
		presenter->InvalidateMeasure ();
	}
public:
	SWTTreeViewRowPresenter (Object^ treeview) {
		this->tree = (TreeView^)treeview;
		Loaded += gcnew RoutedEventHandler (&SWTTreeViewRowPresenter::Handle_Loaded);
	}	
protected:
	virtual Size MeasureOverride (Size constraint) override {
		Point point = TranslatePoint(*gcnew Point (0,0), tree);
		Size size = GridViewRowPresenter::MeasureOverride (constraint);
		size.Width -= Math::Min (point.X, size.Width);
		return size;
	}
	virtual Size ArrangeOverride (Size arrangeSize) override {
		double availableWidth = arrangeSize.Width;
		int i = VisualChildrenCount - 1;
		for (; i>=0; i--) {
			UIElement^ child = (UIElement^) GetVisualChild (i);
			GridViewColumn^ column = Columns [i];
			double x = 0;
			double width = 0;
			if (i == 0) {
				width = availableWidth;
			} else {				
				width = column->ActualWidth;
				//if (Double::IsNaN(width)) width = 0;
				x = availableWidth - width;
			}
			width = Math::Max (0.0, width);
			Point^ loc = gcnew Point (x, 0);
			Size^ size = gcnew Size (width, arrangeSize.Height);
			Rect^ rect = gcnew Rect (*loc, *size);
			child->Arrange (*rect);
			availableWidth -= width;
		}
		return arrangeSize;
	}
};


/*												*/
/* Canvas										*/
/*												*/

public ref class SWTCanvas : Canvas {
private:
	DrawingVisual^ _visual;
	jmethodID OnRenderMID;
	JniRefCookie^ cookie;

	void callin (jmethodID mid, DrawingContext^ context) {
		jobject object = cookie->object;
		if (object == NULL || mid == NULL) return;
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				int arg0 = TO_HANDLE(context);
				env->CallVoidMethod(object, mid, arg0);
				FREE_HANDLE(arg0);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}
	void updateVisual(DrawingVisual^ visual) {
		if (_visual != nullptr) {
			RemoveVisualChild(_visual);
		}
		_visual = visual;
		if (_visual != nullptr) {
			AddVisualChild(_visual);
		}
	}
	
protected:
	virtual void OnRender(DrawingContext^ drawingContext) override {
		this->Canvas::OnRender(drawingContext);
		callin(OnRenderMID, drawingContext);
	}
	
	property int VisualChildrenCount {
		virtual int get () override {
			int count = this->Canvas::VisualChildrenCount;
			if (_visual != nullptr) count++;
			return count;
		}
	}
	
	virtual Visual^ GetVisualChild(int index) override {
		if (_visual != nullptr) {
			//int count = this->Canvas::VisualChildrenCount;
			//if (index == count) return _visual;
			if (index == 0) return _visual;
			return this->Canvas::GetVisualChild(index - 1);
		}
		return this->Canvas::GetVisualChild(index);
	}

public:
	SWTCanvas (JNIEnv* env, jint jniRef) {
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (object) {
			jclass javaClass = env->GetObjectClass(object);
			OnRenderMID = env->GetMethodID(javaClass, "OnRender", "(I)V");
		}
		_visual = nullptr;
	}
	property DrawingVisual^ Visual {
		DrawingVisual^ get() {
			return _visual;
		}
		void set(DrawingVisual^ visual) {
			updateVisual(visual);
		}
	}
};

/*												*/
/* Cursor Support Class							*/
/*												*/

public ref class SWTSafeHandle : SafeHandle {
private:
	bool _isIcon;
public:
	SWTSafeHandle (IntPtr handle, bool isIcon) : SafeHandle ((IntPtr)-1, true) {
		this->handle = handle;
		_isIcon = isIcon;
	}
	[DllImport("user32.dll", SetLastError = true)]
	static bool DestroyIcon(int hIcon);

	[DllImport("user32.dll", SetLastError = true)]
	static bool DestroyCursor(int hCursor);
	
	virtual bool ReleaseHandle () override {
		bool result;
		if (_isIcon) {
			result = DestroyIcon((int)handle);
		} else {
			result = DestroyCursor((int)handle);
		}
		handle = (IntPtr)(-1);
		return result;
	}
	property bool IsInvalid {
		virtual bool get() override { return (int)handle == -1; }
	}
};

/*												*/
/* Text Layout Classes							*/
/*												*/

public ref class SWTTextEmbeddedObject : TextEmbeddedObject {
private:
	int _length;
	double _width, _height, _baseline;
	TextRunProperties^ _properties;

public:
	SWTTextEmbeddedObject (TextRunProperties^ properties, int length, double width, double height, double baseline) {
		_properties = properties;
		_length = length;
		_width = width;
		_height = height;
		_baseline = baseline;
	}

	virtual void Draw(DrawingContext^ drawingContext, Point origin, bool rightToLeft, bool sideways) override {
	}
	virtual System::Windows::Rect ComputeBoundingBox(bool rightToLeft, bool sideways) override {
		return System::Windows::Rect(0, 0, _width, _height);
	}
	virtual TextEmbeddedObjectMetrics^ Format(double remainingParagraphWidth) override {
		return gcnew TextEmbeddedObjectMetrics(_width, _height, _baseline);
	}
	property LineBreakCondition BreakAfter {
		virtual LineBreakCondition get() override { return LineBreakCondition::BreakAlways; }
	}
	property LineBreakCondition BreakBefore {
		virtual LineBreakCondition get() override { return LineBreakCondition::BreakAlways; }
	}
	property bool HasFixedSize {
		virtual bool get() override { return true; }
	}
	property System::Windows::Media::TextFormatting::CharacterBufferReference CharacterBufferReference {
		virtual System::Windows::Media::TextFormatting::CharacterBufferReference get() override { throw gcnew Exception("The method or operation is not implemented."); }
	}
	property int Length {
		virtual int get() override { return _length; }
	}
	property TextRunProperties^ Properties {
		virtual TextRunProperties^ get() override { return _properties; }
	}
};

public ref class SWTTextParagraphProperties : TextParagraphProperties {
private:
	System::Windows::FlowDirection _flowDirection;
	System::Windows::TextAlignment _textAlignment;
	bool _firstLineInParagraph;
	TextRunProperties^ _defaultTextRunProperties;
	System::Windows::TextWrapping _textWrap;
	double _indent;
	double _paragraphIndent;
	double _lineHeight;
	System::Collections::Generic::IList<TextTabProperties^>^ _tabs;

public:
	SWTTextParagraphProperties (
		System::Windows::FlowDirection flowDirection,
		System::Windows::TextAlignment textAlignment,
		bool firstLineInParagraph,
		TextRunProperties^ defaultTextRunProperties,
		System::Windows::TextWrapping textWrap,
		double lineHeight,
		double indent,
		System::Collections::Generic::IList<TextTabProperties^>^ tabs)
	{
		_flowDirection = flowDirection;
		_textAlignment = textAlignment;
		_firstLineInParagraph = firstLineInParagraph;
		_defaultTextRunProperties = defaultTextRunProperties;
		_textWrap = textWrap;
		_lineHeight = lineHeight;
		_indent = indent;
		_tabs = tabs;
	}
	property System::Windows::FlowDirection FlowDirection {
		virtual System::Windows::FlowDirection get() override { return _flowDirection; }
	}
	property System::Windows::TextAlignment TextAlignment {
		virtual System::Windows::TextAlignment get() override { return _textAlignment; }
	}
	property bool FirstLineInParagraph {
		virtual bool get() override { return _firstLineInParagraph; }
	}
	property TextRunProperties^ DefaultTextRunProperties {
		virtual TextRunProperties^ get() override { return _defaultTextRunProperties; }
	}
	property System::Windows::TextWrapping TextWrapping {
		virtual System::Windows::TextWrapping get() override { return _textWrap; }
	}
	property double LineHeight {
		virtual double get() override { return _lineHeight; }
	}
	property double Indent {
		virtual double get() override { return _indent; }
	}
	property System::Windows::Media::TextFormatting::TextMarkerProperties^ TextMarkerProperties {
		virtual System::Windows::Media::TextFormatting::TextMarkerProperties^ get() override { return nullptr; }
	}
	property double ParagraphIndent {
		virtual double get() override { return _paragraphIndent; }
	}
	property System::Collections::Generic::IList<TextTabProperties^>^ Tabs {
		virtual System::Collections::Generic::IList<TextTabProperties^>^ get() override { return _tabs; }
	}
};

public ref class SWTTextRunProperties : TextRunProperties {
private:
	System::Windows::Media::Typeface^ _typeface;
	double _emSize;
	double _emHintingSize;
	System::Windows::TextDecorationCollection^ _textDecorations;
	System::Windows::Media::Brush^ _foregroundBrush;
	System::Windows::Media::Brush^ _backgroundBrush;
	System::Windows::BaselineAlignment _baselineAlignment;
	System::Globalization::CultureInfo^ _culture;

public:
	SWTTextRunProperties (
		System::Windows::Media::Typeface^ typeface,
		double size,
		double hintingSize,
		System::Windows::TextDecorationCollection^ textDecorations,
		System::Windows::Media::Brush^  forgroundBrush,
		System::Windows::Media::Brush^ backgroundBrush,
		System::Windows::BaselineAlignment baselineAlignment,
		System::Globalization::CultureInfo^ culture)
	{
		_typeface = typeface;
		_emSize = size;
		_emHintingSize = hintingSize;
		_textDecorations = textDecorations;
		_foregroundBrush = forgroundBrush;
		_backgroundBrush = backgroundBrush;
		_baselineAlignment = baselineAlignment;
		_culture = culture;
	}
	property System::Windows::Media::Typeface^ Typeface {
		virtual System::Windows::Media::Typeface^ get() override { return _typeface; }
	}
	property double FontRenderingEmSize {
		virtual double get() override { return _emSize; }
	}
	property double FontHintingEmSize {
		virtual double get() override { return _emHintingSize; }
	}
	property System::Windows::TextDecorationCollection^ TextDecorations {
		virtual System::Windows::TextDecorationCollection^ get() override { return _textDecorations; }
	}
	property System::Windows::Media::Brush^ ForegroundBrush {
		virtual System::Windows::Media::Brush^ get() override { return _foregroundBrush; }
		void set(System::Windows::Media::Brush^ brush) { _foregroundBrush = brush; }
	}
	property System::Windows::Media::Brush^ BackgroundBrush {
		virtual System::Windows::Media::Brush^ get() override { return _backgroundBrush; }
	}
	property System::Windows::BaselineAlignment BaselineAlignment {
		virtual System::Windows::BaselineAlignment get() override { return _baselineAlignment; }
	}
	property System::Globalization::CultureInfo^ CultureInfo {
		virtual System::Globalization::CultureInfo^ get() override { return _culture; }
	}
	property System::Windows::Media::TextFormatting::TextRunTypographyProperties^ TypographyProperties {
		virtual System::Windows::Media::TextFormatting::TextRunTypographyProperties^ get() override { return nullptr; }
	}
	property System::Windows::Media::TextEffectCollection^ TextEffects {
		virtual System::Windows::Media::TextEffectCollection^ get() override { return nullptr; }
	}
	property System::Windows::Media::NumberSubstitution^ NumberSubstitution {
		virtual System::Windows::Media::NumberSubstitution^ get() override { return nullptr; }
	}
};

public ref class SWTTextSource : TextSource {
private:
	jmethodID GetTextRunMID, GetPrecedingTextMID;
	JniRefCookie^ cookie;
		
public:
	SWTTextSource (JNIEnv* env, jint jniRef) {	
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (object) {
			jclass javaClass = env->GetObjectClass(object);
			GetTextRunMID = env->GetMethodID(javaClass, "GetTextRun", "(I)I");
			GetPrecedingTextMID = env->GetMethodID(javaClass, "GetPrecedingText", "(I)I");
		}
	}
	Object^ callin (jmethodID mid, int arg0) {
		jobject object = cookie->object;
		if (object == NULL || mid == NULL) return nullptr;
		JNIEnv* env;
		bool detach = false;
		int result = 0;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				result = env->CallIntMethod(object, mid, arg0);
			}
			if (detach) jvm->DetachCurrentThread();
		}
		return TO_OBJECT (result);
	}
	
	virtual TextRun^ GetTextRun(int textSourceCharacterIndex) override {
		return (TextRun^) callin(GetTextRunMID, textSourceCharacterIndex);
	}
	virtual TextSpan<CultureSpecificCharacterBufferRange^>^ GetPrecedingText(int textSourceCharacterIndexLimit) override {
		return (TextSpan<CultureSpecificCharacterBufferRange^>^) callin(GetPrecedingTextMID, textSourceCharacterIndexLimit);
	}
	virtual int GetTextEffectCharacterIndexFromTextSourceCharacterIndex(int textSourceCharacterIndex) override {
		return 0;
	}
};

/*												*/
/* Event Handler Class							*/
/*												*/

delegate void NoArgsDelegate ();

ref class SWTHandler {
private:
	jmethodID mid;
	JniRefCookie^ cookie;
	
public:
	SWTHandler() {
	}
	
	SWTHandler(JNIEnv* env, jint jniRef, jstring method, char* signature) {
		const char * methodString;	
		cookie = gcnew JniRefCookie(env, jniRef);
		jobject object = cookie->object;
		if (method) methodString = (const char *) env->GetStringUTFChars(method, NULL);
		if (object && methodString) {
			jclass javaClass = env->GetObjectClass(object);    
			mid = env->GetMethodID(javaClass, methodString, signature);
		}
		if (method && methodString) env->ReleaseStringUTFChars(method, methodString);
	}
	
	void EventHandler (Object^ sender, EventArgs^ e) {
		jobject object = cookie->object;	
		if (object == NULL || mid == NULL) return; 	
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				//TODO alloc sender causes handle table corruption
				int arg0 = TO_HANDLE(sender);
				int arg1 = TO_HANDLE(e);
				env->CallVoidMethod(object, mid, arg0, arg1);
				//env->CallVoidMethod(object, mid, 0, arg1);
				FREE_HANDLE(arg0);
				FREE_HANDLE(arg1);
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}
	
	void TimerHandler (Object^ sender, EventArgs^ e) {
		jobject object = cookie->object;
		if (object == NULL || mid == NULL) return; 	
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				if (sender->GetType() == DispatcherTimer::typeid) {
					DispatcherTimer^ timer = (DispatcherTimer^)sender;
					int index = (int)timer->Tag;
					int arg1 = TO_HANDLE(e);
					env->CallVoidMethod(object, mid, index, arg1);
					FREE_HANDLE(arg1);
				}
			}
			if (detach) jvm->DetachCurrentThread();
		}
	}
	
	void CancelEventHandler (Object^ sender, CancelEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void RoutedEventHandler (Object^ sender, RoutedEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void RoutedPropertyChangedEventHandler  (Object^ sender, RoutedPropertyChangedEventArgs<double>^ e) {
		EventHandler (sender, e);
	}

	void RoutedPropertyChangedEventHandlerObject  (Object^ sender, RoutedPropertyChangedEventArgs<Object^>^ e) {
		EventHandler (sender, e);
	}

	void ExecutedRoutedEventHandler (Object^ sender, ExecutedRoutedEventArgs^ e) {
		EventHandler (sender, e);	
	}
		
	void DispatcherHookEventHandler (Object^ sender, DispatcherHookEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void SizeChangedEventHandler (Object^ sender, SizeChangedEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void ScrollEventHandler (Object^ sender, ScrollEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void SelectionChangedEventHandler (Object^ sender, SelectionChangedEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void KeyEventHandler (Object^ sender, KeyEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void MouseEventHandler (Object^ sender, MouseEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void FormsMouseEventHandler (Object^ sender, System::Windows::Forms::MouseEventArgs^ e) {
		EventHandler (sender, e);	
	}
		
	void MouseButtonEventHandler (Object^ sender, MouseButtonEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void MouseWheelEventHandler (Object^ sender, MouseWheelEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void TextCompositionEventHandler (Object^ sender, TextCompositionEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void TextChangedEventHandler (Object^ sender, TextChangedEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void KeyboardFocusChangedEventHandler (Object^ sender, KeyboardFocusChangedEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void ContextMenuEventHandler (Object^ sender, ContextMenuEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void DragEventHandler (Object^ sender, DragEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void DragDeltaEventHandler (Object^ sender, DragDeltaEventArgs^ e) {
		EventHandler (sender, e);	
	}
		
	void GiveFeedbackEventHandler (Object^ sender, GiveFeedbackEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void QueryContinueDragEventHandler (Object^ sender, QueryContinueDragEventArgs^ e) {
		EventHandler (sender, e);	
	}
		
	void WebBrowserNavigatingEventHandler (Object^ sender, System::Windows::Forms::WebBrowserNavigatingEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void WebBrowserNavigatedEventHandler (Object^ sender, System::Windows::Forms::WebBrowserNavigatedEventArgs^ e) {
		EventHandler (sender, e);	
	}
	
	void WebBrowserProgressChangedEventHandler (Object^ sender, System::Windows::Forms::WebBrowserProgressChangedEventArgs^ e) {
		EventHandler (sender, e);	
	}	
	
	void WebBrowserDocumentCompletedEventHandler (Object^ sender, System::Windows::Forms::WebBrowserDocumentCompletedEventArgs^ e) {
		EventHandler (sender, e);	
	}

	void NoArgsDelegate() {	
		jobject object = cookie->object;
		if (object == NULL || mid == NULL) return; 	
		JNIEnv* env;
		bool detach = false;

#ifdef JNI_VERSION_1_2
		if (IS_JNI_1_2) {
			jvm->GetEnv((void **)&env, JNI_VERSION_1_2);
		}
#endif
		if (env == NULL) {
			jvm->AttachCurrentThread((void **)&env, NULL);
			if (IS_JNI_1_2) detach = true;
		}
		if (env != NULL) {
			if (!env->ExceptionOccurred()) {
				env->CallVoidMethod(object, mid);
			}
			if (detach) jvm->DetachCurrentThread();
		}	
	}
};

#define HANDLER_CONSTRUCTOR(name, type, signature) extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1##name##) (JNIEnv *env, jclass that, jint arg0, jstring arg1); \
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1##name##) (JNIEnv *env, jclass that, jint arg0, jstring arg1) { \
	jint rc = 0; \
	OS_NATIVE_ENTER(env, that, gcnew_1##name##_FUNC); \
	rc = (jint)TO_HANDLE(gcnew type (gcnew SWTHandler(env, arg0, arg1, signature), &SWTHandler::##name##)); \
	OS_NATIVE_EXIT(env, that, gcnew_1##name##_FUNC); \
	return rc; \
} \

#ifndef NO_gcnew_1EventHandler
HANDLER_CONSTRUCTOR (EventHandler, EventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1CancelEventHandler
HANDLER_CONSTRUCTOR (CancelEventHandler, CancelEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1ExecutedRoutedEventHandler
HANDLER_CONSTRUCTOR (ExecutedRoutedEventHandler, ExecutedRoutedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1DispatcherHookEventHandler
HANDLER_CONSTRUCTOR (DispatcherHookEventHandler, DispatcherHookEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1SizeChangedEventHandler
HANDLER_CONSTRUCTOR (SizeChangedEventHandler, SizeChangedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1ScrollEventHandler
HANDLER_CONSTRUCTOR (ScrollEventHandler, ScrollEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1SelectionChangedEventHandler
HANDLER_CONSTRUCTOR (SelectionChangedEventHandler, SelectionChangedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1KeyEventHandler
HANDLER_CONSTRUCTOR (KeyEventHandler, KeyEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1MouseEventHandler
HANDLER_CONSTRUCTOR (MouseEventHandler, MouseEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1FormsMouseEventHandler
HANDLER_CONSTRUCTOR (FormsMouseEventHandler, System::Windows::Forms::MouseEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1MouseButtonEventHandler
HANDLER_CONSTRUCTOR (MouseButtonEventHandler, MouseButtonEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1MouseWheelEventHandler
HANDLER_CONSTRUCTOR (MouseWheelEventHandler, MouseWheelEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1TextCompositionEventHandler
HANDLER_CONSTRUCTOR (TextCompositionEventHandler, TextCompositionEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1TextChangedEventHandler
HANDLER_CONSTRUCTOR (TextChangedEventHandler, TextChangedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1KeyboardFocusChangedEventHandler
HANDLER_CONSTRUCTOR (KeyboardFocusChangedEventHandler, KeyboardFocusChangedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1ContextMenuEventHandler
HANDLER_CONSTRUCTOR (ContextMenuEventHandler, ContextMenuEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1RoutedEventHandler
HANDLER_CONSTRUCTOR (RoutedEventHandler, RoutedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1DragEventHandler
HANDLER_CONSTRUCTOR (DragEventHandler, DragEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1DragDeltaEventHandler
HANDLER_CONSTRUCTOR (DragDeltaEventHandler, DragDeltaEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1GiveFeedbackEventHandler
HANDLER_CONSTRUCTOR (GiveFeedbackEventHandler, GiveFeedbackEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1QueryContinueDragEventHandler
HANDLER_CONSTRUCTOR (QueryContinueDragEventHandler, QueryContinueDragEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1RoutedPropertyChangedEventHandlerObject
HANDLER_CONSTRUCTOR (RoutedPropertyChangedEventHandlerObject, RoutedPropertyChangedEventHandler<Object^>, "(II)V")
#endif

#ifndef NO_gcnew_1RoutedPropertyChangedEventHandler
HANDLER_CONSTRUCTOR (RoutedPropertyChangedEventHandler, RoutedPropertyChangedEventHandler<double>, "(II)V")
#endif

#ifndef NO_gcnew_1WebBrowserNavigatingEventHandler
HANDLER_CONSTRUCTOR (WebBrowserNavigatingEventHandler, System::Windows::Forms::WebBrowserNavigatingEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1WebBrowserNavigatedEventHandler
HANDLER_CONSTRUCTOR (WebBrowserNavigatedEventHandler, System::Windows::Forms::WebBrowserNavigatedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1WebBrowserProgressChangedEventHandler
HANDLER_CONSTRUCTOR (WebBrowserProgressChangedEventHandler, System::Windows::Forms::WebBrowserProgressChangedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1WebBrowserDocumentCompletedEventHandler
HANDLER_CONSTRUCTOR (WebBrowserDocumentCompletedEventHandler, System::Windows::Forms::WebBrowserDocumentCompletedEventHandler, "(II)V")
#endif

#ifndef NO_gcnew_1NoArgsDelegate
HANDLER_CONSTRUCTOR (NoArgsDelegate, NoArgsDelegate, "()V")
#endif

// special cases
#ifndef NO_gcnew_1TimerHandler
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TimerHandler) (JNIEnv *env, jclass that, jint arg0, jstring arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1TimerHandler) (JNIEnv *env, jclass that, jint arg0, jstring arg1) {
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1TimerHandler_FUNC);
	rc = (jint)TO_HANDLE(gcnew EventHandler (gcnew SWTHandler(env, arg0, arg1, "(II)V"), &SWTHandler::TimerHandler));
	OS_NATIVE_EXIT(env, that, gcnew_1TimerHandler_FUNC);
	return rc;
}
#endif

/*												*/
/* Custom Classes Constructors and Functions	*/
/*												*/

#ifndef NO_gcnew_1SWTCanvas
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTCanvas)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTCanvas)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTCanvas_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTCanvas(env, arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTCanvas_FUNC);
	return rc;
}
#endif

#ifndef NO_SWTDockPanel_1JNIRefProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SWTDockPanel_1JNIRefProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SWTDockPanel_1JNIRefProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SWTDockPanel_1JNIRefProperty_FUNC);
	rc = (jint)TO_HANDLE(SWTDockPanel::JNIRefProperty);
	OS_NATIVE_EXIT(env, that, SWTDockPanel_1JNIRefProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_SWTDockPanel_1typeid
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SWTDockPanel_1typeid)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SWTDockPanel_1typeid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SWTDockPanel_1typeid_FUNC);
	rc = (jint)TO_HANDLE(SWTDockPanel::typeid);
	OS_NATIVE_EXIT(env, that, SWTDockPanel_1typeid_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTSafeHandle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTSafeHandle)(JNIEnv *env, jclass that, jint arg0, jboolean arg1);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTSafeHandle)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTSafeHandle_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTSafeHandle((IntPtr)arg0, (bool)arg1));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTSafeHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTextSource
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextSource)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextSource)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTextSource_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTextSource(env, arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTextSource_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTextParagraphProperties
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextParagraphProperties)(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6, int arg7);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextParagraphProperties)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6, int arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTextParagraphProperties_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTextParagraphProperties((FlowDirection)arg0, (TextAlignment)arg1, arg2, (TextRunProperties^)TO_OBJECT(arg3), (TextWrapping)arg4, arg5, arg6, (System::Collections::Generic::IList<TextTabProperties^>^)TO_OBJECT(arg7)));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTextParagraphProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTextEmbeddedObject
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextEmbeddedObject)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jdouble arg4);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextEmbeddedObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3, jdouble arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTextEmbeddedObject_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTextEmbeddedObject((TextRunProperties^)TO_OBJECT(arg0), arg1, arg2, arg3, arg4));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTextEmbeddedObject_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTextRunProperties
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextRunProperties)(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTextRunProperties)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTextRunProperties_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTextRunProperties((Typeface^)TO_OBJECT(arg0), arg1, arg2, (TextDecorationCollection^)TO_OBJECT(arg3), (Brush^)TO_OBJECT(arg4), (Brush^)TO_OBJECT(arg5), (BaselineAlignment)arg6, (CultureInfo^)TO_OBJECT(arg7)));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTextRunProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTreeView
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTreeView)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTreeView) 
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTreeView_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTreeView(env, arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTreeView_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTTreeViewRowPresenter
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTreeViewRowPresenter)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTTreeViewRowPresenter) 
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTTreeViewRowPresenter_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTTreeViewRowPresenter(TO_OBJECT(arg0)));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTTreeViewRowPresenter_FUNC);
	return rc;
}
#endif

#ifndef NO_gcnew_1SWTAnimator
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTAnimator)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTAnimator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTAnimator_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTAnimator(env, arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTAnimator_FUNC);
	return rc;
}
#endif

#ifndef NO_SWTTextRunProperties_1ForegroundBrush
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SWTTextRunProperties_1ForegroundBrush)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(SWTTextRunProperties_1ForegroundBrush)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SWTTextRunProperties_1ForegroundBrush_FUNC);
	((SWTTextRunProperties^)TO_OBJECT(arg0))->ForegroundBrush = ((Brush^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, SWTTextRunProperties_1ForegroundBrush_FUNC);
}
#endif

#ifndef NO_SWTCanvas_1Visual__I
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SWTCanvas_1Visual__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(SWTCanvas_1Visual__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SWTCanvas_1Visual__I_FUNC);
	rc = (jint)TO_HANDLE(((SWTCanvas^)TO_OBJECT(arg0))->Visual);
	OS_NATIVE_EXIT(env, that, SWTCanvas_1Visual__I_FUNC);
	return rc;
}
#endif

#ifndef NO_SWTCanvas_1Visual__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(SWTCanvas_1Visual__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(SWTCanvas_1Visual__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SWTCanvas_1Visual__II_FUNC);
	((SWTCanvas^)TO_OBJECT(arg0))->Visual = ((DrawingVisual^)TO_OBJECT(arg1));
	OS_NATIVE_EXIT(env, that, SWTCanvas_1Visual__II_FUNC);
}
#endif

#ifndef NO_JNIGetObject
extern "C" JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jobject rc = 0;
	OS_NATIVE_ENTER(env, that, JNIGetObject_FUNC);
	rc = (jobject)arg0;
	OS_NATIVE_EXIT(env, that, JNIGetObject_FUNC);
	return rc;
}
#endif

#ifndef NO_GCHandle_1Free
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GCHandle_1Free) (JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(GCHandle_1Free) (JNIEnv *env, jclass that, jint arg0) {
	OS_NATIVE_ENTER(env, that, GCHandle_1Free_FUNC);
	FREE_HANDLE(arg0);
	OS_NATIVE_EXIT(env, that, GCHandle_1Free_FUNC);
}
#endif

#ifndef NO_GCHandle_1Dump
extern "C" JNIEXPORT void JNICALL OS_NATIVE(GCHandle_1Dump) (JNIEnv *env, jclass that);
JNIEXPORT void JNICALL OS_NATIVE(GCHandle_1Dump) (JNIEnv *env, jclass that) {
	OS_NATIVE_ENTER(env, that, GCHandle_1Dump_FUNC);
#ifndef GCHANDLE_TABLE	
	SWTObjectTable::Dump();
#endif	
	OS_NATIVE_EXIT(env, that, GCHandle_1Dump_FUNC);
}
#endif

#ifndef NO_GCHandle_1Alloc
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GCHandle_1Alloc) (JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GCHandle_1Alloc) (JNIEnv *env, jclass that, jint arg0) {
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GCHandle_1Alloc_FUNC);
	rc = TO_HANDLE (TO_OBJECT (arg0));	
	OS_NATIVE_EXIT(env, that, GCHandle_1Alloc_FUNC);
	return rc;
}
#endif

#ifndef NO_GCHandle_1ToHandle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(GCHandle_1ToHandle) (JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(GCHandle_1ToHandle) (JNIEnv *env, jclass that, jint arg0) {
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GCHandle_1ToHandle_FUNC);
#ifdef GCHANDLE_TABLE	
	rc = arg0; 
#else
	GCHandle gc = GCHandle::FromIntPtr((IntPtr)arg0);
	rc = TO_HANDLE (gc.Target);
	gc.Free();
#endif
	OS_NATIVE_EXIT(env, that, GCHandle_1ToHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_Bitmap_1GetHicon
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Bitmap_1GetHicon)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Bitmap_1GetHicon)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Bitmap_1GetHicon_FUNC);
	rc = (jint)(int)((System::Drawing::Bitmap^)TO_OBJECT(arg0))->GetHicon();
	OS_NATIVE_EXIT(env, that, Bitmap_1GetHicon_FUNC);
	return rc;
}
#endif

#ifndef NO_Icon_1FromHandle
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(Icon_1FromHandle)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(Icon_1FromHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Icon_1FromHandle_FUNC);
	rc = (jint)TO_HANDLE(System::Drawing::Icon::FromHandle((IntPtr)(int)arg0));
	OS_NATIVE_EXIT(env, that, Icon_1FromHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_memcpy___3CII
extern "C" JNIEXPORT void JNICALL OS_NATIVE(memcpy___3CII)(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	pin_ptr<wchar_t> lparg1; 
	OS_NATIVE_ENTER(env, that, memcpy___3CII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (jchar *)env->GetPrimitiveArrayCritical(arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	}
	if (arg2 > 0) {
		lparg1 = &((array<wchar_t>^)TO_OBJECT(arg1))[0];
		memcpy(lparg0, lparg1, arg2);
	}
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) env->ReleasePrimitiveArrayCritical(arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, memcpy___3CII_FUNC);
}
#endif

#ifndef NO_memcpy__I_3BI
extern "C" JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3BI)(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	pin_ptr<Byte> lparg0; 
	OS_NATIVE_ENTER(env, that, memcpy__I_3BI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (jbyte*)env->GetPrimitiveArrayCritical(arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	}
	if (arg2 > 0) {
		lparg0 = &((array<Byte>^)TO_OBJECT(arg0))[0];
		memcpy(lparg0, lparg1, arg2);	
	}
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) env->ReleasePrimitiveArrayCritical(arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, memcpy__I_3BI_FUNC);
}
#endif

#ifndef NO_memcpy___3BII
extern "C" JNIEXPORT void JNICALL OS_NATIVE(memcpy___3BII)(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2);
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	pin_ptr<Byte> lparg1; 
	OS_NATIVE_ENTER(env, that, memcpy___3BII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (jbyte*)env->GetPrimitiveArrayCritical(arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	}
	if (arg2 > 0) {
		lparg1 = &((array<Byte>^)TO_OBJECT(arg1))[0];
		memcpy(lparg0, lparg1, arg2);
	}
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) env->ReleasePrimitiveArrayCritical(arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, memcpy___3BII_FUNC);
}
#endif

#ifndef NO_ToggleButton_1IsCheckedNullSetter
extern "C" JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1IsCheckedNullSetter)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL OS_NATIVE(ToggleButton_1IsCheckedNullSetter)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ToggleButton_1IsCheckedNullSetter_FUNC);
	((ToggleButton^)TO_OBJECT(arg0))->IsChecked = Nullable<bool>(); 
	OS_NATIVE_EXIT(env, that, ToggleButton_1IsCheckedNullSetter_FUNC);
}
#endif

#ifndef NO_SWTAnimator_1DoubleValueProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SWTAnimator_1DoubleValueProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SWTAnimator_1DoubleValueProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SWTAnimator_1DoubleValueProperty_FUNC);
	rc = (jint)TO_HANDLE(SWTAnimator::DoubleValueProperty);
	OS_NATIVE_EXIT(env, that, SWTAnimator_1DoubleValueProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_SWTAnimator_1IntValueProperty
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(SWTAnimator_1IntValueProperty)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL OS_NATIVE(SWTAnimator_1IntValueProperty)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SWTAnimator_1IntValueProperty_FUNC);
	rc = (jint)TO_HANDLE(SWTAnimator::IntValueProperty);
	OS_NATIVE_EXIT(env, that, SWTAnimator_1IntValueProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_Timeline_1BeginTime__II
extern "C" JNIEXPORT void JNICALL OS_NATIVE(Timeline_1BeginTime__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT void JNICALL OS_NATIVE(Timeline_1BeginTime__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, Timeline_1BeginTime__II_FUNC);
	TimeSpan^ span = (TimeSpan^)TO_OBJECT(arg1);
	Timeline^ timeline = (Timeline^)TO_OBJECT(arg0);
	timeline->BeginTime =  (Nullable<TimeSpan>(span));
	OS_NATIVE_EXIT(env, that, Timeline_1BeginTime__II_FUNC);
}
#endif

#ifndef NO_gcnew_1SWTAnimation
extern "C" JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTAnimation)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL OS_NATIVE(gcnew_1SWTAnimation)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, gcnew_1SWTAnimation_FUNC);
	rc = (jint)TO_HANDLE(gcnew SWTAnimation(env, arg0));
	OS_NATIVE_EXIT(env, that, gcnew_1SWTAnimation_FUNC);
	return rc;
}
#endif
