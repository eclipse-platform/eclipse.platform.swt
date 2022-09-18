/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

#include <SDKDDKVer.h>
#include <windows.h>
#include <commctrl.h>

#include <stdio.h>

#include <algorithm>

#define WINDOW_CLASS	L"SwtSnippetsClass"
#define IDC_EDIT		1000

HWND g_Edit = 0;
WNDPROC g_EditWndProc = 0;

#pragma comment(linker, "/SUBSYSTEM:console /ENTRY:mainCRTStartup")

DWORD HklToU32(HKL value)
{
	const uint64_t u64 = (const uint64_t)value;
	if (u64 & 0xFFFFFFFF'00000000)
		wprintf(L"WARNING: non-u32 HKL value: %016I64X\n", u64);

	const uint32_t u32 = (const uint32_t)u64;
	return u32;
}

void DumpCommonScanCodes()
{
	#define DUMP_VK_SCANCODE(x) wprintf(L"%04X = " #x "\n", MapVirtualKey(x, MAPVK_VK_TO_VSC_EX));

	DUMP_VK_SCANCODE(VK_LMENU);
	DUMP_VK_SCANCODE(VK_RMENU);
	DUMP_VK_SCANCODE(VK_LCONTROL);
	DUMP_VK_SCANCODE(VK_LSHIFT);
	DUMP_VK_SCANCODE(VK_LWIN);

	DUMP_VK_SCANCODE('1');
	DUMP_VK_SCANCODE('2');
	DUMP_VK_SCANCODE('3');
	DUMP_VK_SCANCODE('4');
	DUMP_VK_SCANCODE('5');
	DUMP_VK_SCANCODE('6');
	DUMP_VK_SCANCODE('7');
	DUMP_VK_SCANCODE('8');
	DUMP_VK_SCANCODE('9');
	DUMP_VK_SCANCODE('0');

	DUMP_VK_SCANCODE('Q');
	DUMP_VK_SCANCODE('W');
	DUMP_VK_SCANCODE('E');
	DUMP_VK_SCANCODE('R');
	DUMP_VK_SCANCODE('T');
	DUMP_VK_SCANCODE('Y');
}

// This is to debug what various APIs return for key presses in different keyboard layouts
void OnWmKey(const WCHAR* a_Message, WPARAM a_WParam, LPARAM a_LParam)
{
	// In order to reduce clutter in output, skip pure modifier keys
	switch (a_WParam)
	{
	case VK_SHIFT:
	case VK_CONTROL:
	case VK_MENU:
		return;
	}

	// In order to reduce clutter in output, skip repeats
	const LPARAM wasDown   = (a_LParam >> 30) & 1;
	const LPARAM isPressed = (a_LParam >> 31) & 1;
	if (wasDown != isPressed)
		return;

	const HKL hkl = GetKeyboardLayout(GetCurrentThreadId());
	WCHAR klid[64];
	GetKeyboardLayoutName(klid);

	const WCHAR char1 = MapVirtualKey((UINT)a_WParam, MAPVK_VK_TO_CHAR);
	const WCHAR char2 = MapVirtualKeyEx((UINT)a_WParam, MAPVK_VK_TO_CHAR, hkl);

	// ToUnicode() alters internal keyboard state, which affects dead key processing
	const bool USE_TO_UNICODE = false;
	WCHAR toUnicode[16];
	if (!USE_TO_UNICODE)
		wcscpy_s(toUnicode, L"<disabled>");
	else
	{
		BYTE keyboard[256];
		if (!GetKeyboardState(keyboard))
			wprintf(L"ERROR: GetKeyboardState failed\n");

		int toUnicodeChars = ToUnicode((UINT)a_WParam, 0, keyboard, toUnicode, 16, 0);
		if (toUnicodeChars < 0)
			toUnicodeChars = 0;
		toUnicode[toUnicodeChars] = 0;
	}

	WCHAR keyName[16];
	if (!GetKeyNameText((LONG)a_LParam, keyName, 16))
		wcscpy_s(keyName, L"<error>");

	WORD scanCode = LOBYTE(HIWORD(a_LParam));
	if (a_LParam & (1 << 24)) {
		// Extended scan code
		scanCode |= 0xE000;
	}

	wprintf(
		L"%s: HKL=%08X KLID=%s VK=%04X scan=%04X MapVirtualKey()=%04X '%c' MapVirtualKeyEx(hkl)=%04X '%c' ToUnicode=%04X '%s' GetKeyNameText()='%s'\n",
		a_Message,
		HklToU32(hkl),
		klid,
		(DWORD)a_WParam,
		scanCode,
		char1, (char1 >= 0x20) ? char1 : ' ',
		char2, (char2 >= 0x20) ? char2 : ' ',
		USE_TO_UNICODE ? toUnicode[0] : 0, (toUnicode[0] >= 0x20) ? toUnicode : L"",
		keyName
	);
}

// This is to debug what various APIs return for key presses in different keyboard layouts
void OnWmChar(const WCHAR* a_Message, WPARAM a_WParam, LPARAM a_LParam)
{
	// In order to reduce clutter in output, skip repeats
	const LPARAM wasDown   = (a_LParam >> 30) & 1;
	const LPARAM isPressed = (a_LParam >> 31) & 1;
	if (wasDown != isPressed)
		return;

	WORD scanCode = LOBYTE(HIWORD(a_LParam));
	if (a_LParam & (1 << 24)) {
		// Extended scan code
		scanCode |= 0xE000;
	}

	const HKL hkl = GetKeyboardLayout(GetCurrentThreadId());
	WCHAR klid[64];
	GetKeyboardLayoutName(klid);

	const WCHAR wchar = LOWORD(a_WParam);
	wprintf(
		L"%s: HKL=%08X KLID=%s         scan=%04X                                                             Char=%04X '%c'\n",
		a_Message,
		HklToU32(hkl),
		klid,
		scanCode,
		wchar, (wchar >= 0x20) ? wchar : L' '
	);
}

void HandleInputMessages(UINT a_Message, WPARAM a_WParam, LPARAM a_LParam);

LRESULT CALLBACK EditSubclassProc(HWND a_HWND, UINT a_Message, WPARAM a_WParam, LPARAM a_LParam)
{
	HandleInputMessages(a_Message, a_WParam, a_LParam);
	return CallWindowProc(g_EditWndProc, a_HWND, a_Message, a_WParam, a_LParam);
}

void OnWmCreate(HWND a_HWND)
{
	DumpCommonScanCodes();

	// This is to test how mnemonics work with different keyboard layouts.
	// Findings: Mnemonics only work in matching layouts.
	{
		HMENU submenu1 = CreateMenu();
		InsertMenu(submenu1, 0, MF_STRING, 1001, L"Submenu1:1");
		InsertMenu(submenu1, 0, MF_STRING, 1002, L"Submenu1:2");

		HMENU submenu2 = CreateMenu();
		InsertMenu(submenu2, 0, MF_STRING, 2001, L"Submenu2:1");
		InsertMenu(submenu2, 0, MF_STRING, 2002, L"Submenu2:2");

		HMENU menu = CreateMenu();
		InsertMenu(menu, 0, MF_STRING | MF_POPUP, (UINT_PTR)submenu1, L"&Test");
		InsertMenu(menu, 0, MF_STRING | MF_POPUP, (UINT_PTR)submenu2, L"&Проверка");
		SetMenu(a_HWND, menu);
	}

	// An Edit field to test keyboard input
	g_Edit = CreateWindowEx(0, WC_EDIT, 0, WS_CHILD | WS_VISIBLE, 0, 0, 10, 10, a_HWND, (HMENU)IDC_EDIT, 0, 0);
	g_EditWndProc = (WNDPROC)SetWindowLongPtr(g_Edit, GWLP_WNDPROC, (LONG_PTR)EditSubclassProc);
}

void OnWmSize(HWND a_HWND)
{
	RECT clientRect;
	GetClientRect(a_HWND, &clientRect);
	SetWindowPos(g_Edit, 0, clientRect.left, clientRect.top, clientRect.right - clientRect.left, clientRect.bottom - clientRect.top, SWP_NOZORDER);
}

void HandleInputMessages(UINT a_Message, WPARAM a_WParam, LPARAM a_LParam)
{
	switch (a_Message)
	{
	case WM_KEYDOWN:
		OnWmKey(L"WM_KEYDOWN    ", a_WParam, a_LParam);
		break;
	case WM_KEYUP:
		OnWmKey(L"WM_KEYUP      ", a_WParam, a_LParam);
		break;
	case WM_SYSKEYDOWN:
		OnWmKey(L"WM_SYSKEYDOWN ", a_WParam, a_LParam);
		break;
	case WM_SYSKEYUP:
		OnWmKey(L"WM_SYSKEYUP   ", a_WParam, a_LParam);
		break;
	case WM_CHAR:
		OnWmChar(L"WM_CHAR       ", a_WParam, a_LParam);
		break;
	case WM_DEADCHAR:
		OnWmChar(L"WM_DEADCHAR   ", a_WParam, a_LParam);
		break;
	case WM_SYSCHAR:
		OnWmChar(L"WM_SYSCHAR    ", a_WParam, a_LParam);
		break;
	case WM_SYSDEADCHAR:
		OnWmChar(L"WM_SYSDEADCHAR", a_WParam, a_LParam);
		break;
	case WM_IME_CHAR:
		OnWmChar(L"WM_IME_CHAR   ", a_WParam, a_LParam);
		break;
	}
}

LRESULT CALLBACK WndProc(HWND a_HWND, UINT a_Message, WPARAM a_WParam, LPARAM a_LParam)
{
	switch (a_Message)
	{
	case WM_CREATE:
		OnWmCreate(a_HWND);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	case WM_SIZE:
		OnWmSize(a_HWND);
		break;
	}

	HandleInputMessages(a_Message, a_WParam, a_LParam);

	return DefWindowProc(a_HWND, a_Message, a_WParam, a_LParam);
}

// Boilerplate code
int main()
{
	SetProcessDPIAware();

	{
		WNDCLASSEXW wcex;
		ZeroMemory(&wcex, sizeof(wcex));
		wcex.cbSize = sizeof(WNDCLASSEX);
		wcex.style = CS_HREDRAW | CS_VREDRAW;
		wcex.lpfnWndProc = WndProc;
		wcex.hInstance = 0;
		wcex.hCursor = LoadCursor(nullptr, IDC_ARROW);
		wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
		wcex.lpszClassName = WINDOW_CLASS;
		RegisterClassExW(&wcex);
	}

	HWND window = CreateWindowW
	(
		WINDOW_CLASS, WINDOW_CLASS, WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT, 300, 100,
		nullptr, nullptr, 0, nullptr
	);

	ShowWindow(window, SW_SHOW);

	MSG msg;
	while (GetMessage(&msg, nullptr, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}

	return 0;
}
