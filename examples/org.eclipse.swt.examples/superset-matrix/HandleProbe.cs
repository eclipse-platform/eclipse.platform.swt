// Copyright (c) 2026 Hitesh Soliwal and others.
// SPDX-License-Identifier: EPL-2.0
//
// Prints "<kernel handles> <GDI objects> <USER objects>" for a Windows process.
// Built by run-matrix.sh with the .NET Framework csc.exe; the matrix runner
// calls it through -Dsuperset.handleProbe=<path>.
using System;
using System.Diagnostics;
using System.Runtime.InteropServices;

static class HandleProbe
{
    [DllImport("user32.dll")]
    static extern uint GetGuiResources(IntPtr process, uint flags);

    static int Main(string[] args)
    {
        Process p = Process.GetProcessById(int.Parse(args[0]));
        Console.WriteLine(p.HandleCount + " " + GetGuiResources(p.Handle, 0) + " " + GetGuiResources(p.Handle, 1));
        return 0;
    }
}
