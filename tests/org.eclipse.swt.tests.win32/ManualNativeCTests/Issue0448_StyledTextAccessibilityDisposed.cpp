#include <SDKDDKVer.h>
#include <windows.h>

#include <atlbase.h>
#include <uiautomation.h>

#include <string>
#include <vector>

#define WINDOW_CLASS	L"SwtSnippetsClass"

std::wstring g_output;
IUIAutomation* g_automation = 0;
HFONT g_monoFont = CreateFont(-12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, L"Courier New");

std::wstring StringFormat(const WCHAR* a_Format, ...)
{
	va_list vaList;
	va_start(vaList, a_Format);

	WCHAR buffer[256];
	vswprintf_s(buffer, a_Format, vaList);
	va_end(vaList);

	return buffer;
}

class Accessible
{
public:
	CComPtr<IUIAutomationElement>		AutomationElement;
	CComPtr<IUIAutomationTextPattern>	TextPattern;
	CComPtr<IUIAutomationTextRange>		TextRange;

	bool FromCursor()
	{
		POINT cursorPos;
		GetCursorPos(&cursorPos);
		return SUCCEEDED(g_automation->ElementFromPoint(cursorPos, &this->AutomationElement));
	}

	std::wstring Test()
	{
		std::wstring result;
		HRESULT hr;

		result += StringFormat(L"%p = IUIAutomationElement\n", (const IUIAutomationElement*)this->AutomationElement);

		// ----------------

		IUIAutomationTextPattern* textPattern = NULL;
		hr = this->AutomationElement->GetCurrentPatternAs(UIA_TextPatternId, IID_PPV_ARGS(&textPattern));
		if (!SUCCEEDED(hr))
			result += StringFormat(L" +      %08X = IUIAutomationElement::GetCurrentPatternAs(UIA_TextPatternId)\n", hr);
		else if (!textPattern)
			result +=              L" +        <NULL> = IUIAutomationElement::GetCurrentPatternAs(UIA_TextPatternId)\n";
		else
			result +=              L" +            OK = IUIAutomationElement::GetCurrentPatternAs(UIA_TextPatternId)\n";

		if (textPattern)
		{
			if (!this->TextPattern)
				this->TextPattern = textPattern;

			textPattern->Release();
			textPattern = 0;
		}

		// ----------------

		if (!this->TextPattern)
			return result;

		IUIAutomationTextRange* textRange = NULL;
		hr = this->TextPattern->get_DocumentRange(&textRange);
		if (!SUCCEEDED(hr))
			result += StringFormat(L" +      %08X = IUIAutomationTextPattern::get_DocumentRange()\n", hr);
		else if (!textRange)
			result +=              L" +        <NULL> = IUIAutomationTextPattern::get_DocumentRange()\n";
		else
			result +=              L" +            OK = IUIAutomationTextPattern::get_DocumentRange()\n";

		if (textRange)
		{
			if (!this->TextRange)
				this->TextRange = textRange;

			textRange->Release();
			textRange = 0;
		}

		// ----------------

		if (!this->TextRange)
			return result;

		CComBSTR string;
		hr = this->TextRange->GetText(12, &string);
		if (!SUCCEEDED(hr))
			result += StringFormat(L" +      %08X = IUIAutomationTextRange::GetText()\n", hr);
		else
		{
			for (WCHAR* currChar = string; *currChar; currChar++)
			{
				if (*currChar < 0x20)
					*currChar = 0x20;
			}

			result += StringFormat(L" +'%12s' = IUIAutomationTextRange::GetText()\n", (const WCHAR*)string);
		}

		return result;
	}
};

std::vector<Accessible> g_pinned;

void ON_WM_CREATE(HWND a_HWND)
{
	CoInitialize(NULL);

	HRESULT hr = CoCreateInstance(__uuidof(CUIAutomation), NULL, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&g_automation));
	if (!SUCCEEDED(hr))
		DebugBreak();

	SetTimer(a_HWND, 1, 100, nullptr);

	RegisterHotKey(a_HWND, 1, MOD_WIN, VK_F2);
}

void ON_WM_HOTKEY(HWND a_HWND)
{
	Accessible accessible;
	if (accessible.FromCursor())
		g_pinned.push_back(accessible);
}

void ON_WM_TIMER(HWND a_HWND)
{
	g_output.clear();

	g_output += L"Pinned: (Press Win+F2 to add)\n";
	for (Accessible& accessible : g_pinned)
	{
		g_output += accessible.Test();
	}
	g_output += L"\n";

	g_output += L"Mouse cursor:\n";
	Accessible accessible;
	if (accessible.FromCursor())
		g_output += accessible.Test();

	RedrawWindow(a_HWND, 0, 0, RDW_ERASE | RDW_INVALIDATE);
}

void ON_WM_PAINT(HWND a_HWND)
{
	PAINTSTRUCT ps;
	BeginPaint(a_HWND, &ps);

	SelectObject(ps.hdc, g_monoFont);

	RECT clientRect;
	GetClientRect(a_HWND, &clientRect);
	InflateRect(&clientRect, -10, -10);
	DrawText(ps.hdc, g_output.c_str(), (int)g_output.length(), &clientRect, DT_LEFT);

	EndPaint(a_HWND, &ps);
}

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
	case WM_CREATE:
		ON_WM_CREATE(hWnd);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	case WM_TIMER:
		ON_WM_TIMER(hWnd);
		break;
	case WM_HOTKEY:
		ON_WM_HOTKEY(hWnd);
		break;
	case WM_PAINT:
		ON_WM_PAINT(hWnd);
		return TRUE;
	}

	return DefWindowProc(hWnd, message, wParam, lParam);
}

// Boilerplate code
int APIENTRY wWinMain(HINSTANCE hInstance, HINSTANCE, LPWSTR, int)
{
	SetProcessDPIAware();

	{
		WNDCLASSEXW wcex;
		ZeroMemory(&wcex, sizeof(wcex));
		wcex.cbSize = sizeof(WNDCLASSEX);
		wcex.style = CS_HREDRAW | CS_VREDRAW;
		wcex.lpfnWndProc = WndProc;
		wcex.hInstance = hInstance;
		wcex.hCursor = LoadCursor(nullptr, IDC_ARROW);
		wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
		wcex.lpszClassName = WINDOW_CLASS;
		RegisterClassExW(&wcex);
	}

	HWND window = CreateWindowExW
	(
		WS_EX_COMPOSITED,
		WINDOW_CLASS, WINDOW_CLASS,
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT, 600, 200,
		nullptr, nullptr, hInstance, nullptr
	);

	ShowWindow(window, SW_SHOW);

	MSG msg;
	while (GetMessage(&msg, nullptr, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return (int)msg.wParam;
}
