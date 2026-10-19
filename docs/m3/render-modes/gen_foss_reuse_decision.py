# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Writes docs/m3/render-modes/FOSS_REUSE_DECISION.tsv (LF, tab separated)."""
import sys

COLUMNS = ["capability", "candidate_repo", "commit", "source_paths", "license", "contract_fit", "decision",
           "rejected_reason", "adapted_hashes", "notice_path"]

W = "bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/"
C = "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/"
SW = "SWTSwing/Eclipse SWT PI/swing/org/eclipse/swt/internal/swing/"
PSW = "bundles/org.eclipse.swt/Eclipse SWT PI/swing/org/eclipse/swt/internal/swing/"
I31 = "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/"
SC = "bundles/org.eclipse.swt.skia/src/org/eclipse/swt/internal/"
HSK = "shared/java/"

ROWS = [
    # the model and the planner: in-repository, reused as is
    ["viewport_model_single_truth", "hsoliwal/eclipse.platform.swt (this repository)", "cbc458de66",
     W + "TreeViewport.java;" + W + "TableViewport.java;" + W + "ListViewport.java;" + C + "widgets/VirtualTreeTopology.java;VirtualTreeVisibleProjection.java;VirtualTreeViewport.java;VirtualScrollMetrics.java;VirtualTreeResidentPlan.java",
     "EPL-2.0", "drawn modes are viewport modes with a different plane: same topology, projection, lanes, scroll metrics, resident window, facades and planner phases (capture, plan, retire/insert, commit, bind, restore focus)",
     "REUSE_DIRECT", "-", "src:2a519e79d7d3,11d650baedf7,fb4cd2e62bbb", "same repository"],
    ["mode_selector", "hsoliwal/eclipse.platform.swt (this repository)", "cbc458de66", C + "internal/WidgetMode.java",
     "EPL-2.0", "WidgetMode.of (\"Tree\", VIEWPORT, CUSTOM, SKIJA, GPU) picks the plane; the mediator keeps its own VIEWPORT query, so it stays on in every drawn mode",
     "REUSE_DIRECT", "-", "src:19c6f78d2948", "same repository"],
    ["gpu_op_stream", "hsoliwal/eclipse.platform.swt (this repository)", "cbc458de66", C + "internal/gpu/OpStream.java",
     "EPL-2.0", "packed int command stream (fill, line, text, clip); the gpu plane records one frame and replays it in one native call",
     "REUSE_DIRECT", "-", "src:1ec477dc675b", "same repository"],
    ["native_theme_parts", "Windows uxtheme (system API through SWT's existing OS natives)", "-",
     "OS.OpenThemeData/DrawThemeBackground/GetThemePartSize (Explorer::TreeView TVP_TREEITEM, TVP_GLYPH, TVP_HOTGLYPH; BP_CHECKBOX)",
     "system API", "native theme of the drawn planes: selection, hot and focus backgrounds, expander glyphs and check boxes are the platform's own parts; skija/gpu planes draw them from a part-image cache",
     "REUSE_DIRECT", "-", "-", "-"],
    # Initiative 31 renderers and tricks
    ["nattable_cell_clip", "eclipse-nattable/nattable (donor catalogue TRICKS.md trick 1)", "NAT-CELL-PAINTER",
     "org.eclipse.nebula.widgets.nattable.painter.layer.CellLayerPainter.java:63-97,184-199", "EPL-2.0",
     "paint only the rows and cells inside the damage clip, mapped to a range by arithmetic; applied in DrawnPlane.drawRows (first damaged row by division, stop below the damage)",
     "PATTERN_ONLY", "pattern applied; no source copied", "-", "-"],
    ["draw2d_scroll_blit", "eclipse/gef Draw2d (donor catalogue TRICKS.md trick 2)", "GEFC-FIGURECANVAS-BLIT",
     "org.eclipse.draw2d.FigureCanvas.java:410-495", "EPL-2.0",
     "blit the back buffer by the scroll delta and repaint only the exposed strip; optimization leaf R7 of the drawn planes (today the whole damage the mediator invalidates is repainted)",
     "PATTERN_ONLY", "planned for R7; no source copied", "-", "-"],
    ["owner_draw_protocol", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "TreeItemRenderer.java:287-347", "EPL-2.0", "MeasureItem, EraseItem, PaintItem order with the clip set to the cell; drawn planes emit SWT's own event fields",
     "PATTERN_ONLY", "renderer is bound to I31's own Tree (HashMap per item, own arrangement); our planes paint from the viewport model", "-", "-"],
    ["custom_widget_classes", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "Tree.java;Table.java;List.java;TreeItemsHandler.java;TableItemsHandler.java", "EPL-2.0",
     "complete custom-drawn Tree/Table/List with their own model and input", "NO_FIT",
     "a second model per widget contradicts 'viewport is king' (one model, one planner); keyboard parity missing (Tree/Table keys TODO, List Up/Down only)", "-", "-"],
    ["paint_cull_and_row_damage", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "TreeItemsHandler.java:65-164;TreeItem.java:1003-1031", "EPL-2.0", "paint only rows that intersect the damage, stop at the first row below it; row-rect invalidation",
     "PATTERN_ONLY", "arithmetic over the resident window replaces the loop; rows come from the plan, not from items", "-", "-"],
    ["colour_provider", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "ColorProvider.java;DefaultColorProvider.java", "EPL-2.0", "key to colour table for drawn widgets",
     "PATTERN_ONLY", "theme values are packed ints resolved once per theme; keys follow Swing/FlatLaf names", "src:4ffc6c0e1b88,67722fe66284", "-"],
    ["renderer_factory_seam", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "RendererFactory.java;DefaultRendererFactory.java", "EPL-2.0", "one renderer per widget chosen by the look and feel",
     "PATTERN_ONLY", "a theme here is a defaults table plus one row renderer shared by Tree, Table and List", "src:b39cd6708350", "-"],
    ["initiative31_look", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     I31 + "TreeItemRenderer.java:136-170 (drawArrow);DefaultCheckboxRenderer.java:147-176 (drawCheckbox);DefaultColorProvider.java;Table.java:109 (SELECTION_COLOR)", "EPL-2.0",
     "the initiative31 theme of the drawn planes: grey triangle expanders, rounded check boxes filled when checked, E0EEFE selection, yellow hover",
     "ADAPT", "-", "src:ef92445b6730,4cca2934e769,67722fe66284,7205c8eda707",
     "Initiative31Look.java keeps EPL-2.0 with the Initiative 31 notice; Synexia as contributor"],
    ["skijagc_png_commit", "swt-initiative31/prototype-skija", "146eee2a23faec511de96f2539f7d650ef24300e",
     "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/graphics/SkijaGC.java:83-95,137-148,369-388", "EPL-2.0",
     "full-size raster plus copyArea plus PNG encode/decode per paint", "REJECT", "per-frame O(control area) allocation and codec", "-", "-"],
    ["text_image_lru", "swt-initiative31/skija-canvas", "10e6086c6f053ed175106e4483c8af918d7cf226",
     SC + "skia/SkiaResources.java:41-75,87-89,358-425;" + SC + "graphics/SkiaTextDrawing.java:119-243", "EPL-2.0",
     "text cache keyed by (string, font, colour); evicted entries are closed", "ADAPT", "-", "src:70c1de8c190a,2240ba787124",
     "EPL-2.0 header retained in the adapted class"],
    ["gl_surface_damage_snapshot", "swt-initiative31/skija-canvas", "10e6086c6f053ed175106e4483c8af918d7cf226",
     SC + "skia/SkiaGlCanvasExtension.java:109-308", "EPL-2.0", "persistent GL surface, damage union and last-frame reuse for the OpenGL gpu plane (non-win32)",
     "ADAPT", "-", "src:3609abe580cc", "EPL-2.0 header retained in the adapted class"],
    # Skija (HumbleUI)
    ["m3_raster_direct", "HumbleUI/Skija", "44c99d7a92fe68a29b3233f8ca55b0d29d8f6f68",
     HSK + "Surface.java(makeRasterDirect,notifyContentWillChange)", "Apache-2.0", "the M3 renderer (m3 mode) draws into the pixels of a DIB section; the damage is BitBlt-ed; no copy, no codec",
     "REUSE_DIRECT", "-", "-", "THIRD_PARTY_NOTICES: Skija Apache-2.0 (library call, jars 0.116.3 on the class path)"],
    ["m3_text", "HumbleUI/Skija", "44c99d7a92fe68a29b3233f8ca55b0d29d8f6f68",
     HSK + "Font.java;Typeface.java;TextLine.java;FontMgr.java", "Apache-2.0", "M3 renderer text: typeface from the SWT font's face name, TextLine shaped once and cached (LRU)",
     "REUSE_DIRECT", "-", "-", "THIRD_PARTY_NOTICES: Skija Apache-2.0"],
    ["m3_gl_context", "HumbleUI/Skija", "44c99d7a92fe68a29b3233f8ca55b0d29d8f6f68",
     HSK + "DirectContext.java(makeGL);BackendRenderTarget.java;Surface.java(wrapBackendRenderTarget)", "Apache-2.0",
     "gpu plane on every platform (Java only, rule 13): OpenGL context through SWT's own WGL natives on a hidden popup, offscreen render target, readPixels into the DIB; probe: 0.72 ms per frame with read back, 833 ms first setup",
     "REUSE_DIRECT", "-", "-", "THIRD_PARTY_NOTICES: Skija Apache-2.0"],
    ["m3_d3d_context", "HumbleUI/Skija", "44c99d7a92fe68a29b3233f8ca55b0d29d8f6f68", HSK + "DirectContext.java(makeDirect3D)", "Apache-2.0",
     "Skia over Direct3D 12", "REJECT", "needs a D3D12 adapter, device and queue created first (HumbleUI/JWM does it in C++); from Java only through the FFM API; the OpenGL context already serves the gpu plane", "-", "-"],
    # Direct2D route
    ["direct2d_dc_render_target", "Windows SDK (d2d1.dll, dwrite.dll)", "-",
     "ID2D1Factory::CreateDCRenderTarget;ID2D1DCRenderTarget::BindDC;IDWriteFactory::CreateTextFormat", "system API",
     "gpu plane on win32: hardware DC render target bound to the paint DC (also WM_PRINT); probe on the hidden desktop: correct pixels, 3.0 ms per 300x380 frame of 20 text rows, 1.3 s first device creation",
     "REJECT", "the JNI shim committed in R5 is removed under brief rule 13 (Java only, no native builds); Direct2D from Java works through the FFM API (route 2 probe: 1.22 ms per hardware frame) but needs --enable-preview on JDK 21", "-", "-"],
    ["dawn_webgpu", "dawn.googlesource.com/dawn", "67d4d6e64b0755d638f76549794692badfdbc8ab",
     "include/webgpu/webgpu.h;src/dawn/native (D3D12, Vulkan, Metal backends)", "BSD-3-Clause (LICENSE, The Dawn & Tint Authors)",
     "a WebGPU device over D3D12, Vulkan or Metal as one gpu plane for every platform (Skia Graphite can draw through it)",
     "NO_FIT", "C++ library with its own build (CMake/GN) and no Java binding: it would need JNI bindings and a native build per platform; the gpu plane draws rows and text, which Direct2D/DirectWrite (win32, system) and Skia GL (elsewhere, Skija) already give; revisit when a Java binding of Skia Graphite over Dawn exists", "-", "-"],
    ["swt_com_vtblcall", "hsoliwal/eclipse.platform.swt (this repository)", "cbc458de66",
     "bundles/org.eclipse.swt/Eclipse SWT PI/win32/org/eclipse/swt/internal/ole/win32/COM.java(VtblCall);OS.java(CallWindowProc)", "EPL-2.0",
     "call Direct2D through SWT's existing natives, no new JNI", "REJECT",
     "VtblCall passes the object as the first argument, CallWindowProc truncates the second argument to 32 bits: neither can call D2D1CreateFactory(enum, REFIID, options, out); float arguments in XMM registers are not passed", "-", "-"],
    ["jna_function_calls", "java-native-access/jna", "5.14.0 (local maven)", "com.sun.jna.Function", "Apache-2.0 OR LGPL-2.1",
     "generic native calls without a shim", "REJECT", "new runtime dependency of SWT and per-call marshaling; the shim is smaller", "-", "-"],
    ["panama_ffm", "openjdk/jdk", "21", "java.lang.foreign", "GPL-2.0 WITH Classpath-exception-2.0", "native calls without JNI",
     "PATTERN_ONLY", "preview API in JDK 21 (--enable-preview); probed: drives Direct2D from Java (route 2), kept for SWT on JDK 22+ or M3jdk21 with preview", "-", "-"],
    # Swing family (lead 2026-10-17)
    ["swing_row_renderer_pattern", "Chrriis/SWTSwing", "82bad8de1753098edbbe0d0f004a4c615da931f8",
     SW + "CTree.java;CTable.java;CList.java;DefaultTreeTableCellRenderer.java;TreeTableCellRenderer.java", "EPL-2.0",
     "SWT Tree/Table/List on Swing: one cell renderer stamps every row; selection and user colours per cell; PaintItem from the cell painter",
     "PATTERN_ONLY", "painting goes through Swing components (AWT toolkit, a second UI thread); our planes draw with GC/Skija/Direct2D", "src:72bace249ae3,184c27a801a8,83b64e24f31a,16e6e06f03ba", "-"],
    ["swing_lookandfeel_keys", "Chrriis/SWTSwing", "82bad8de1753098edbbe0d0f004a4c615da931f8",
     SW + "LookAndFeelUtils.java", "EPL-2.0", "SWT system colours and fonts answered from UIManager keys of the active Swing look and feel",
     "PATTERN_ONLY", "the swing theme keeps the UIManager key names in its defaults table; values are measured once from JDK Swing renders, no UIManager at run time", "src:43b77bdfa973", "-"],
    ["swing_event_focus_mapping", "swt-initiative31/prototype-swing", "354887cc2cbf6b330911f5c4da3a0395feafa9c3",
     PSW + "CTree.java;CTable.java;CList.java", "EPL-2.0", "Swing mouse/key/focus events translated to SWT events (same sources as SWTSwing, moved into the SWT bundle layout)",
     "PATTERN_ONLY", "input stays with the viewport behaviour plane in stage 1; reference for stage 2 (drawn plane input)", "src:cd197d6d49c9,b74c59a5a801,5ad466e48ca7", "-"],
    ["swing_plaf_metrics", "openjdk/jdk (javax.swing.plaf.metal, nimbus)", "21", "MetalLookAndFeel;OceanTheme;NimbusLookAndFeel", "GPL-2.0 WITH Classpath-exception-2.0",
     "Metal/Nimbus colours, row height, handle glyphs for the swing theme", "PATTERN_ONLY", "licence keeps source out of EPL files; numbers are measured from Swing renders by a probe", "-", "-"],
    ["swingwt_reverse", "sxhdroid/SwingWT", "092b5aa07a323a8322af7e406d3f88dd85da0a60", "-", "CPL-1.0 OR LGPL-2.0-or-later (COPYING)",
     "Swing/AWT API on SWT (the reverse direction)", "NO_FIT", "not a drawing donor; licence review required before any reuse; pattern source for the later M3JDK phase", "-", "-"],
    ["dj_native_swing", "Chrriis/DJ-Native-Swing", "99666a095df1bd51211dc06879c394aee6aa48cf", "DJNativeSwing/licence.txt", "LGPL-2.1-or-later",
     "native SWT components embedded in Swing", "NO_FIT", "embedding, not drawing; LGPL keeps source out of EPL files", "-", "-"],
    ["swt_awt_bridge", "hsoliwal/eclipse.platform.swt (this repository)", "cbc458de66",
     "bundles/org.eclipse.swt/Eclipse SWT AWT/win32/org/eclipse/swt/awt/SWT_AWT.java", "EPL-2.0", "AWT frames inside SWT composites",
     "NO_FIT", "embedding, not drawing", "src:e5c74b35080b", "-"],
    ["eclipse_albireo", "Eclipse Albireo (archived Eclipse Technology project)", "-", "org.eclipse.albireo.core (SwingControl)", "EPL-1.0",
     "Swing controls embedded in SWT through SWT_AWT", "NO_FIT", "embedding, not drawing; no GitHub mirror found (only a copy inside intelligentautomation/proteus)", "-", "-"],
    ["flatlaf_properties", "JFormDesigner/FlatLaf", "6b9b766659bb7307c3a4c54e615e734112035bd0",
     "flatlaf-core/src/main/resources/com/formdev/flatlaf/FlatLaf.properties;UIDefaultsLoader.java", "Apache-2.0",
     "properties-based defaults with references and derived colours", "PATTERN_ONLY", "two built-in themes need no loader yet; the key names follow it so a loader can be added (look-and-feel LF2)", "-", "-"],
]


def main(path):
    lines = ["\t".join(COLUMNS)]
    for row in ROWS:
        assert len(row) == len(COLUMNS), row[0]
        assert all("\t" not in cell and "\n" not in cell for cell in row), row[0]
        lines.append("\t".join(row))
    with open(path, "w", encoding="utf-8", newline="") as out:
        out.write("\n".join(lines) + "\n")


if __name__ == "__main__":
    main(sys.argv[1])
