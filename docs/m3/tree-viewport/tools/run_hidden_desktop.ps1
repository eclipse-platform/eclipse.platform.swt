# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Runs a command on a separate, invisible desktop of the interactive window station (WinSta0\m3-ui-tests),
# so UI tests never move the user's mouse, steal focus or put windows on the user's desktop.
# GDI/uxtheme rendering, WM_PRINT captures and window messages work there; real input (SendInput,
# Display.post, SetCursorPos) does not reach a desktop that is not the input desktop.
# Usage: run_hidden_desktop.ps1 -TimeoutSec 900 -Log <file> -CommandLine "<cmd line>" [-Desktop <name>]
# Each ui_lock.sh slot has its own desktop, so concurrent runs never share windows or focus.
param ([int] $TimeoutSec = 900, [Parameter(Mandatory)] [string] $Log, [Parameter(Mandatory)] [string] $CommandLine, [string] $Desktop = 'm3-ui-tests')

Add-Type -TypeDefinition @"
using System;
using System.Runtime.InteropServices;
public static class HiddenDesktop {
	[StructLayout(LayoutKind.Sequential, CharSet = CharSet.Unicode)]
	struct STARTUPINFO {
		public int cb; public string lpReserved; public string lpDesktop; public string lpTitle;
		public int dwX, dwY, dwXSize, dwYSize, dwXCountChars, dwYCountChars, dwFillAttribute, dwFlags;
		public short wShowWindow, cbReserved2; public IntPtr lpReserved2, hStdInput, hStdOutput, hStdError;
	}
	[StructLayout(LayoutKind.Sequential)]
	struct PROCESS_INFORMATION { public IntPtr hProcess, hThread; public int dwProcessId, dwThreadId; }
	[DllImport("user32.dll", CharSet = CharSet.Unicode, SetLastError = true)]
	static extern IntPtr CreateDesktopW(string name, IntPtr device, IntPtr devmode, int flags, uint access, IntPtr sa);
	[DllImport("user32.dll")] static extern bool CloseDesktop(IntPtr desktop);
	[DllImport("kernel32.dll", CharSet = CharSet.Unicode, SetLastError = true)]
	static extern bool CreateProcessW(string app, System.Text.StringBuilder cmd, IntPtr pa, IntPtr ta, bool inherit, uint flags,
		IntPtr env, string dir, ref STARTUPINFO si, out PROCESS_INFORMATION pi);
	[DllImport("kernel32.dll")] static extern uint WaitForSingleObject(IntPtr handle, uint millis);
	[DllImport("kernel32.dll")] static extern bool GetExitCodeProcess(IntPtr process, out uint code);
	[DllImport("kernel32.dll")] static extern bool CloseHandle(IntPtr handle);

	/** Exit code of the command; -1 setup failure (message in error), -2 timeout (pid in pid). */
	public static int Run(string desktopName, string app, string commandLine, int timeoutMs, out string error, out int pid) {
		error = null; pid = 0;
		IntPtr desktop = CreateDesktopW(desktopName, IntPtr.Zero, IntPtr.Zero, 0, 0x10000000, IntPtr.Zero);
		if (desktop == IntPtr.Zero) { error = "CreateDesktop " + Marshal.GetLastWin32Error(); return -1; }
		try {
			STARTUPINFO si = new STARTUPINFO();
			si.cb = Marshal.SizeOf(typeof(STARTUPINFO));
			si.lpDesktop = "WinSta0\\" + desktopName;
			PROCESS_INFORMATION pi;
			if (!CreateProcessW(app, new System.Text.StringBuilder(commandLine), IntPtr.Zero, IntPtr.Zero, false, 0x08000000, IntPtr.Zero, null, ref si, out pi)) {
				error = "CreateProcess " + Marshal.GetLastWin32Error(); return -1;
			}
			pid = pi.dwProcessId;
			try {
				if (WaitForSingleObject(pi.hProcess, (uint) timeoutMs) != 0) return -2;
				uint code; GetExitCodeProcess(pi.hProcess, out code); return (int) code;
			} finally { CloseHandle(pi.hThread); CloseHandle(pi.hProcess); }
		} finally { CloseDesktop(desktop); }
	}
}
"@

$cmd = '"' + $env:ComSpec + '" /d /s /c "' + $CommandLine + ' > "' + $Log + '" 2>&1"'
$err = $null; $procId = 0
$code = [HiddenDesktop]::Run($Desktop, $env:ComSpec, $cmd, $TimeoutSec * 1000, [ref] $err, [ref] $procId)
if ($code -eq -1) { Write-Output "hidden desktop: $err"; exit 5 }
if ($code -eq -2) {
	& taskkill.exe /PID $procId /T /F | Out-Null
	Write-Output "hidden desktop: timed out after $TimeoutSec s; process tree killed"
	exit 124
}
exit $code
