# Third-party notices: SWT visibility gate (2026-10-13)

## Chromium (BSD-3-Clause), adapted algorithm

`bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/OcclusionTracker.java`
adapts the top-level occlusion calculation from Chromium:

- `ui/aura/native_window_occlusion_tracker_win.cc` (sha256 `d28386391e6acad308711a41c1975a8f7ed7d724eef99b8f2e46f7be8dfcd815`)
- `ui/gfx/win/hwnd_util.cc` `IsWindowVisibleAndFullyOpaque` (sha256 recorded in FOSS_REUSE_DECISION.tsv)

at commit `8255ebcb24fde9e699a1a73006bcda34049fea80`. The SWT code is a Java
re-expression of the front-to-back z-order walk and the opaque-occluder
predicate; no Chromium source text is copied. License (LICENSE sha256
`368cca1106be99d39ecd32a38d8305585d802a475effb66380b91ffc9bcf709b`):

```
Copyright 2015 The Chromium Authors

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of Google LLC nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```

## Pattern-only references (no code copied)

- Mozilla Firefox `widget/windows/WinWindowOcclusionTracker.*` (MPL-2.0): read to
  confirm the algorithm; file-level copyleft, nothing taken.
- Qt `qtbase/src/widgets/kernel/qwidget.cpp` (LGPL-3.0/GPL-2.0+/commercial):
  show-time layout activation pattern only.
- Avalonia `Visual.cs` (MIT), WPF `UIElement.cs` (MIT): effective-visibility and
  dirty-flag patterns only.
