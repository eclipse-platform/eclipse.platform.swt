package org.eclipse.swt.internal.mozilla;

public class nsIWidget extends nsISupports {
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 83;
	
	public nsIWidget(int address) {
		super(address);
	}
	
	public int Create(nsIWidget aParent, int aRect, int aHandleEventFunction, int aContext, int aAppShell, int aToolkit, int aInitData) {
		int aParentAddress = aParent != null ? aParent.getAddress() : 0;
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aParentAddress, aRect, aHandleEventFunction, aContext, aAppShell, aToolkit, aInitData);
	}
	
	public int Create(int aParent, int aRect, int aHandleEventFunction, int aContext, int aAppShell, int aToolkit, int aInitData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aParent, aRect, aHandleEventFunction, aContext, aAppShell, aToolkit, aInitData);
	}
	
	public int GetClientData(int[] aClientData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aClientData);
	}
	
	public int SetClientData(int[] aClientData) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aClientData);
	}
	
	public int Destroy() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress());
	}
	
	public int SetParent(int aNewParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aNewParent);
	}
	
	public int GetParent() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress());
	}
	
	public int GetChildren() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress());
	}
	
	public int Show(boolean aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aState);
	}
	
	public int SetModal(boolean aModal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aModal);
	}
	
	public int IsVisible(boolean aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aState);
	}
	
	public int ConstrainPosition(boolean aAllowSlop, int[] aX, int[] aY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aAllowSlop, aX, aY);
	}
	
	public int Move(int aX, int aY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aX, aY); 
	}
	
	public int Resize(int aWidth, int aHeight, boolean aRepaint) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aWidth, aHeight, aRepaint); 
	}
	
	public int Resize(int aX, int aY, int aWidth, int aHeight, boolean aRepaint) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aX, aY, aWidth, aHeight, aRepaint);
	}
	
	public int SetZIndex(int aZIndex) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aZIndex);
	}
	
	public int GetZIndex(int[] aZindex) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), aZindex);
	}
	
	public int PlaceBehind(int aWidget, boolean aActivate) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), aWidget, aActivate); 
	}
	
	public int SetSizeMode(int aMode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aMode); 
	}
	
	public int GetSizeMode(int[] aMode) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aMode);
	}
	
	public int Enable(boolean aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aState);
	}
	
	public int IsEnabled(boolean[] aState) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aState);
	}
	
	public int SetFocus(boolean aRaise) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), aRaise);
	}
	
	public int GetBounds(int aRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aRect);
	}
	
	public int GetScreenBounds(int aRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 25, getAddress(), aRect);
	}
	
	public int GetClientBounds(int aRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), aRect);
	}
	
	public int GetBorderSize(int[] aWidth, int[] aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), aWidth, aHeight);
	}
	
	public int GetForegroundColor() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 28, getAddress()); 
	}
	
	public int SetForegroundColor(int aColor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 29, getAddress(), aColor);
	}
	
	public int GetBackgroundColor() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 30, getAddress());
	}
	
	public int SetBackgroundColor(int aColor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 31, getAddress(), aColor);
	}
	
	public int GetFont() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 32, getAddress());
	}
	
	public int SetFont(int aFont) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 33, getAddress(), aFont);
	}
	
	public int GetCursor() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 34, getAddress());
	}
	
	public int SetCursor(int aCursor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 35, getAddress(), aCursor);
	}
	
	public int GetWindowType(int aWindowType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 36, getAddress(), aWindowType);
	}
	
	public int SetWindowTranslucency(boolean aTranslucent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 37, getAddress(), aTranslucent);
	}
	
	public int GetWindowTranslucency(boolean[] aTranslucent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 38, getAddress(), aTranslucent);
	}
	
	public int UpdateTranslucentWindowAlpha(int aRect, int[] aAlphas) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 39, getAddress(), aRect, aAlphas);
	}
	
	public int HideWindowChrome(boolean aShouldHide) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 40, getAddress(), aShouldHide);
	}
	
	public int MakeFullScreen(boolean aFullScreen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), aFullScreen);
	}
	
	public int Validate() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 42, getAddress());
	}
	
	public int Invalidate(boolean aIsSynchronous) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 43, getAddress(), aIsSynchronous);
	}
	
	public int Invalidate(int aRect, boolean aIsSynchronous) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 44, getAddress(), aRect, aIsSynchronous);
	}
	
	public int InvalidateRegion(int aRegion, boolean aIsSynchronous) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 45, getAddress());
	}
	
	public int Update() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 46, getAddress());
	}
	
	public int AddMouseListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 47, getAddress(), aListener);
	}
	
	public int AddEventListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 48, getAddress(), aListener);
	}
	
	public int AddMenuListener(int aListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 49, getAddress(), aListener);
	}
	
	public int GetToolkit() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 50, getAddress());
	}
	
	public int SetColorMap(int aColorMap) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 51, getAddress(), aColorMap);
	}
	
	public int Scroll(int aDx, int aDy, int aClipRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 52, getAddress(), aDx, aDy, aClipRect);
	}
	
	public int ScrollWidgets(int aDx, int aDy) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 53, getAddress(), aDx, aDy);
	}
	
	public int ScrollRect(int aScrRect, int aDx, int aDy) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 54, getAddress(), aScrRect, aDx, aDy);
	}

	public void AddChild(int aChild) {
		XPCOM.VtblCallNoRet(super.LAST_METHOD_ID + 55, getAddress(), aChild);
	}
	
	public void RemoveChild(int aChild) {
		XPCOM.VtblCallNoRet(super.LAST_METHOD_ID + 56, getAddress(), aChild);
	}

	public int GetNativeData(int aDataType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 57, getAddress(), aDataType);
	}
	
	public void FreeNativeData(int data, int aDataType) {
		XPCOM.VtblCallNoRet(super.LAST_METHOD_ID + 58, getAddress(), data, aDataType);
	}
	
	public int GetRenderingContext() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 59, getAddress());
	}
	
	public int GetDeviceContext() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 60, getAddress());
	}
	
	public int GetAppShell() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 61, getAddress());
	}
	
	public int SetBorderStyle(int aBorderStyle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 62, getAddress(), aBorderStyle);
	}
	
	public int SetTitle(int aTitle) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 63, getAddress(), aTitle);
	}
	
	public int SetIcon(int anIconSpec) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 64, getAddress(), anIconSpec);
	}
	
	public int SetMenuBar(int aMenuBar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 65, getAddress(), aMenuBar);
	}
	
	public int ShowMenuBar(boolean aShow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 66, getAddress(), aShow);
	}
	
	public int WidgetToScreen(int aOldRect, int aNewRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 67, getAddress(), aOldRect, aNewRect);
	}
	
	public int ScreenToWidget(int aOldRect, int aNewRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 68, getAddress(), aOldRect, aNewRect);
	}
	
	public int BeginResizingChildren() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 69, getAddress());
	}
	
	public int EndResizingChidren() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 70, getAddress());
	}
	
	public int GetPreferredSize(int aWidth, int aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 71, getAddress(), aWidth, aHeight);
	}
	
	public int SetPreferredSize(int aWidth, int aHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 72, getAddress(), aWidth, aHeight);
	}
	
	public int DispatchEvent(int event, int aStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 73, getAddress(), event, aStatus);
	}
	
	public int Paint(int aRenderingContext, int aDirtyRect) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 74, getAddress(), aRenderingContext, aDirtyRect);
	}
	
	public int EnableDragDrop(boolean aEnable) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 75, getAddress(), aEnable);
	}
	
	public void ConvertToDeviceCoordinates(int[] aX, int[] aY) {
		XPCOM.VtblCallNoRet(super.LAST_METHOD_ID + 76, getAddress(), aX, aY);
	}
	
	public int CaptureMouse(boolean aCapture) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 77, getAddress(), aCapture);
	}
	
	public int GetWindowClass(char[] aClass) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 78, getAddress(), aClass);
	}
	
	public int SetWindowClass(char[] aClass) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 79, getAddress(), aClass);
	}
	
	public int CaptureRollupEvents(int aListener, boolean aDoCapture, boolean aConsumeRollupEvent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 80, getAddress(), aListener, aDoCapture, aConsumeRollupEvent);
	}
	
	public int ModalEventFilter(boolean aRealEvent, int aEvent, boolean[] aForWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 81, getAddress(), aRealEvent, aEvent, aForWindow);
	}
	
	public int GetAttention() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 82, getAddress());
	}
	
	public int GetLastInputEventTime(int[] aTime) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 83, getAddress(), aTime);
	}

}
