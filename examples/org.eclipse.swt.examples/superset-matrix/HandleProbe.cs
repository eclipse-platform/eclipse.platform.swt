/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *
 * Prints "<kernel handles> <GDI objects> <USER objects>" for a Windows process.
 * Built by run-matrix.sh with the .NET Framework csc.exe; the matrix runner
 * calls it through -Dsuperset.handleProbe=<path>.
 *******************************************************************************/
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
