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
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAttributeEvent;
import org.eclipse.swt.accessibility.AccessibleAttributeListener;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEditableTextEvent;
import org.eclipse.swt.accessibility.AccessibleEditableTextListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.accessibility.AccessibleTextAttributeEvent;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.accessibility.AccessibleTextExtendedListener;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.StyledTextDropTargetEffect;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.IME;

/**
 * Catalogue of StyledText API operations shared by the paged-vs-gap
 * differential test and the million-line scale test. Every operation uses
 * positions relative to the document so it applies to any size. Ids are the API
 * names; the coverage table maps API items to these ids.
 */
final class StyledTextApiOps {

	/** Inherent cost of an operation; the scale test bounds layouts for every class. */
	enum Cost { VIEW, SELECTION, CHARS }

	record Op(String id, Cost cost, Consumer<StyledText> action) {
	}

	private StyledTextApiOps() {
	}

	static int line(StyledText t, double fraction) {
		return (int) Math.min(t.getLineCount() - 1, Math.max(0, Math.round((t.getLineCount() - 1) * fraction)));
	}

	static int off(StyledText t, double fraction) {
		return t.getOffsetAtLine(line(t, fraction));
	}

	/** Offset of the first character of line(fraction) plus up to k characters inside that line. */
	static int inLine(StyledText t, double fraction, int k) {
		int line = line(t, fraction);
		return t.getOffsetAtLine(line) + Math.min(k, t.getLine(line).length());
	}

	static StyleRange style(int start, int length, int fontStyle, Color fg, Color bg) {
		StyleRange s = new StyleRange(start, length, fg, bg, fontStyle);
		return s;
	}

	static Color color(StyledText t, int id) {
		return t.getDisplay().getSystemColor(id);
	}

	static List<Op> catalogue() {
		List<Op> ops = new ArrayList<>();
		// ---- text
		ops.add(new Op("append", Cost.VIEW, t -> t.append("appended line\nsecond")));
		ops.add(new Op("insert", Cost.VIEW, t -> { t.setCaretOffset(inLine(t, 0.3, 2)); t.insert("INS\r\nX"); }));
		ops.add(new Op("replaceTextRange", Cost.VIEW, t -> t.replaceTextRange(inLine(t, 0.5, 1), 3, "rep\n")));
		ops.add(new Op("replaceTextRange-crlf", Cost.VIEW, t -> t.replaceTextRange(inLine(t, 0.6, 0), 0, "a\rb\r\nc\n")));
		ops.add(new Op("setTextLimit", Cost.VIEW, t -> t.setTextLimit(t.getCharCount() + 64)));
		ops.add(new Op("type-keys", Cost.VIEW, t -> { t.setCaretOffset(inLine(t, 0.2, 1)); key(t, 'q', 'q'); key(t, SWT.CR, SWT.CR); key(t, SWT.BS, SWT.BS); key(t, SWT.DEL, SWT.DEL); }));
		ops.add(new Op("setTextLimit-reset", Cost.VIEW, t -> t.setTextLimit(-1)));
		// ---- caret and selection
		ops.add(new Op("setCaretOffset", Cost.VIEW, t -> t.setCaretOffset(off(t, 0.75))));
		ops.add(new Op("setSelection(int)", Cost.VIEW, t -> t.setSelection(off(t, 0.1))));
		ops.add(new Op("setSelection(int,int)", Cost.VIEW, t -> t.setSelection(off(t, 0.1), inLine(t, 0.12, 3))));
		ops.add(new Op("setSelection(Point)", Cost.VIEW, t -> t.setSelection(new Point(inLine(t, 0.4, 4), off(t, 0.4)))));
		ops.add(new Op("setSelectionRange", Cost.VIEW, t -> t.setSelectionRange(off(t, 0.9), 5)));
		ops.add(new Op("setSelectionRanges", Cost.VIEW, t -> t.setSelectionRanges(new int[] { off(t, 0.2), 2, off(t, 0.5), 3, off(t, 0.8), 0 })));
		ops.add(new Op("showSelection", Cost.VIEW, StyledText::showSelection));
		ops.add(new Op("selectAll", Cost.SELECTION, StyledText::selectAll));
		ops.add(new Op("setSelection-collapse", Cost.VIEW, t -> t.setSelection(off(t, 0.5))));
		// ---- scrolling
		ops.add(new Op("setTopIndex", Cost.VIEW, t -> t.setTopIndex(line(t, 0.66))));
		ops.add(new Op("setTopPixel", Cost.VIEW, t -> t.setTopPixel(t.getTopPixel() / 3)));
		ops.add(new Op("setHorizontalIndex", Cost.VIEW, t -> t.setHorizontalIndex(4)));
		ops.add(new Op("setHorizontalPixel", Cost.VIEW, t -> t.setHorizontalPixel(0)));
		ops.add(new Op("scroll", Cost.VIEW, t -> t.scroll(0, 0, 0, 10, 200, 60, true)));
		// ---- styles
		ops.add(new Op("setStyleRange", Cost.VIEW, t -> t.setStyleRange(style(inLine(t, 0.0, 0), 5, SWT.BOLD, color(t, SWT.COLOR_RED), null))));
		ops.add(new Op("setStyleRange-features", Cost.VIEW, t -> t.setStyleRange(features(t, inLine(t, 0.05, 1), 6))));
		ops.add(new Op("setStyleRange-link", Cost.VIEW, t -> { StyleRange s = style(inLine(t, 0.07, 0), 4, SWT.NORMAL, null, null); s.underline = true; s.underlineStyle = SWT.UNDERLINE_LINK; s.data = "link"; t.setStyleRange(s); }));
		ops.add(new Op("setStyleRange-metrics", Cost.VIEW, t -> { StyleRange s = style(inLine(t, 0.08, 0), 1, SWT.NORMAL, null, null); s.metrics = new GlyphMetrics(10, 4, 12); t.setStyleRange(s); }));
		ops.add(new Op("setStyleRange-rise-font", Cost.VIEW, t -> { StyleRange s = style(inLine(t, 0.09, 0), 3, SWT.ITALIC, null, null); s.rise = 3; s.font = t.getFont(); t.setStyleRange(s); }));
		ops.add(new Op("setStyleRange-span", Cost.VIEW, t -> t.setStyleRange(style(off(t, 0.1), off(t, 0.9) - off(t, 0.1), SWT.NORMAL, color(t, SWT.COLOR_DARK_GREEN), null))));
		ops.add(new Op("setStyleRanges(StyleRange[])", Cost.VIEW, t -> t.setStyleRanges(new StyleRange[] { style(off(t, 0.3), 2, SWT.BOLD, null, null), style(off(t, 0.31), 3, SWT.ITALIC, null, color(t, SWT.COLOR_YELLOW)) })));
		ops.add(new Op("setStyleRanges(int[],StyleRange[])", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.BOLD, color(t, SWT.COLOR_BLUE), null); t.setStyleRanges(new int[] { off(t, 0.4), 2, off(t, 0.41), 4 }, new StyleRange[] { s, s }); }));
		ops.add(new Op("setStyleRanges(int,int,int[],StyleRange[])", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.ITALIC, null, null); int a = off(t, 0.4); t.setStyleRanges(a, off(t, 0.45) - a, new int[] { inLine(t, 0.42, 1), 2 }, new StyleRange[] { s }); }));
		ops.add(new Op("replaceStyleRanges", Cost.VIEW, t -> { int a = off(t, 0.3); t.replaceStyleRanges(a, off(t, 0.32) - a, new StyleRange[] { style(inLine(t, 0.31, 0), 2, SWT.BOLD, color(t, SWT.COLOR_MAGENTA), null) }); }));
		ops.add(new Op("setStyleRanges-clear", Cost.VIEW, t -> t.setStyleRanges(new StyleRange[0])));
		ops.add(new Op("setStyleRange-again", Cost.VIEW, t -> t.setStyleRange(style(inLine(t, 0.5, 0), 4, SWT.BOLD, null, null))));
		// ---- line attributes
		ops.add(new Op("setLineAlignment", Cost.VIEW, t -> t.setLineAlignment(line(t, 0.1), 2, SWT.CENTER)));
		ops.add(new Op("setLineBackground", Cost.VIEW, t -> t.setLineBackground(line(t, 0.12), 3, color(t, SWT.COLOR_CYAN))));
		ops.add(new Op("setLineBullet-dot", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.NORMAL, null, null); s.metrics = new GlyphMetrics(0, 0, 16); t.setLineBullet(line(t, 0.2), 2, new Bullet(ST.BULLET_DOT, s)); }));
		ops.add(new Op("setLineBullet-number", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.NORMAL, null, null); s.metrics = new GlyphMetrics(0, 0, 30); t.setLineBullet(line(t, 0.22), 3, new Bullet(ST.BULLET_NUMBER | ST.BULLET_TEXT, s)); }));
		ops.add(new Op("setLineBullet-letters", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.NORMAL, null, null); s.metrics = new GlyphMetrics(0, 0, 20); t.setLineBullet(line(t, 0.25), 1, new Bullet(ST.BULLET_LETTER_LOWER, s)); t.setLineBullet(line(t, 0.26), 1, new Bullet(ST.BULLET_LETTER_UPPER, s)); }));
		ops.add(new Op("setLineBullet-custom-text", Cost.VIEW, t -> { StyleRange s = style(0, 0, SWT.NORMAL, null, null); s.metrics = new GlyphMetrics(0, 0, 24); Bullet b = new Bullet(ST.BULLET_CUSTOM | ST.BULLET_TEXT, s); b.text = "-> "; t.setLineBullet(line(t, 0.27), 1, b); }));
		ops.add(new Op("setLineIndent", Cost.VIEW, t -> t.setLineIndent(line(t, 0.3), 2, 12)));
		ops.add(new Op("setLineVerticalIndent", Cost.VIEW, t -> t.setLineVerticalIndent(line(t, 0.32), 6)));
		ops.add(new Op("setLineJustify", Cost.VIEW, t -> t.setLineJustify(line(t, 0.34), 1, true)));
		ops.add(new Op("setLineTabStops", Cost.VIEW, t -> t.setLineTabStops(line(t, 0.36), 1, new int[] { 30, 60 })));
		ops.add(new Op("setLineWrapIndent", Cost.VIEW, t -> t.setLineWrapIndent(line(t, 0.38), 2, 16)));
		ops.add(new Op("setLineBullet-clear", Cost.VIEW, t -> t.setLineBullet(line(t, 0.2), 1, null)));
		// ---- global attributes
		ops.add(new Op("setAlignment", Cost.VIEW, t -> t.setAlignment(SWT.RIGHT)));
		ops.add(new Op("setAlignment-reset", Cost.VIEW, t -> t.setAlignment(SWT.LEFT)));
		ops.add(new Op("setJustify", Cost.VIEW, t -> t.setJustify(true)));
		ops.add(new Op("setJustify-reset", Cost.VIEW, t -> t.setJustify(false)));
		ops.add(new Op("setIndent", Cost.VIEW, t -> t.setIndent(8)));
		ops.add(new Op("setWrapIndent", Cost.VIEW, t -> t.setWrapIndent(10)));
		ops.add(new Op("setTabs", Cost.VIEW, t -> t.setTabs(6)));
		ops.add(new Op("setTabStops", Cost.VIEW, t -> t.setTabStops(new int[] { 40, 90 })));
		ops.add(new Op("setMargins", Cost.VIEW, t -> t.setMargins(3, 4, 5, 6)));
		ops.add(new Op("setLeftMargin", Cost.VIEW, t -> t.setLeftMargin(7)));
		ops.add(new Op("setRightMargin", Cost.VIEW, t -> t.setRightMargin(8)));
		ops.add(new Op("setTopMargin", Cost.VIEW, t -> t.setTopMargin(2)));
		ops.add(new Op("setBottomMargin", Cost.VIEW, t -> t.setBottomMargin(2)));
		ops.add(new Op("setLineSpacing", Cost.VIEW, t -> t.setLineSpacing(2)));
		ops.add(new Op("setLineSpacing-reset", Cost.VIEW, t -> t.setLineSpacing(0)));
		ops.add(new Op("setLineSpacingProvider", Cost.VIEW, t -> t.setLineSpacingProvider(line -> line % 7 == 0 ? Integer.valueOf(4) : null)));
		ops.add(new Op("setLineSpacingProvider-reset", Cost.VIEW, t -> t.setLineSpacingProvider(null)));
		ops.add(new Op("setWordWrap", Cost.VIEW, t -> t.setWordWrap(!t.getWordWrap())));
		ops.add(new Op("setWordWrap-back", Cost.VIEW, t -> t.setWordWrap(!t.getWordWrap())));
		ops.add(new Op("setEditable", Cost.VIEW, t -> t.setEditable(false)));
		ops.add(new Op("type-readonly", Cost.VIEW, t -> key(t, 'z', 'z')));
		ops.add(new Op("setEditable-reset", Cost.VIEW, t -> t.setEditable(true)));
		ops.add(new Op("setDoubleClickEnabled", Cost.VIEW, t -> t.setDoubleClickEnabled(false)));
		ops.add(new Op("setMouseNavigatorEnabled", Cost.VIEW, t -> t.setMouseNavigatorEnabled(true)));
		ops.add(new Op("setMouseNavigatorEnabled-reset", Cost.VIEW, t -> t.setMouseNavigatorEnabled(false)));
		ops.add(new Op("setOrientation", Cost.VIEW, t -> t.setOrientation(SWT.RIGHT_TO_LEFT)));
		ops.add(new Op("setOrientation-reset", Cost.VIEW, t -> t.setOrientation(SWT.LEFT_TO_RIGHT)));
		ops.add(new Op("setTextDirection", Cost.VIEW, t -> t.setTextDirection(SWT.RIGHT_TO_LEFT)));
		ops.add(new Op("setTextDirection-reset", Cost.VIEW, t -> t.setTextDirection(SWT.LEFT_TO_RIGHT)));
		ops.add(new Op("setBidiColoring", Cost.VIEW, t -> t.setBidiColoring(true)));
		ops.add(new Op("setFont", Cost.VIEW, t -> t.setFont(t.getDisplay().getSystemFont())));
		ops.add(new Op("setForeground", Cost.VIEW, t -> t.setForeground(color(t, SWT.COLOR_DARK_BLUE))));
		ops.add(new Op("setBackground", Cost.VIEW, t -> t.setBackground(color(t, SWT.COLOR_WHITE))));
		ops.add(new Op("setSelectionForeground", Cost.VIEW, t -> t.setSelectionForeground(color(t, SWT.COLOR_WHITE))));
		ops.add(new Op("setSelectionBackground", Cost.VIEW, t -> t.setSelectionBackground(color(t, SWT.COLOR_DARK_MAGENTA))));
		ops.add(new Op("setMarginColor", Cost.VIEW, t -> t.setMarginColor(color(t, SWT.COLOR_GRAY))));
		ops.add(new Op("setAlwaysShowScrollBars", Cost.VIEW, t -> t.setAlwaysShowScrollBars(false)));
		ops.add(new Op("setAlwaysShowScrollBars-reset", Cost.VIEW, t -> t.setAlwaysShowScrollBars(true)));
		ops.add(new Op("setFixedLineMetrics", Cost.VIEW, t -> { GC gc = new GC(t); try { t.setFixedLineMetrics(gc.getFontMetrics()); } finally { gc.dispose(); } }));
		ops.add(new Op("setFixedLineMetrics-reset", Cost.VIEW, t -> t.setFixedLineMetrics(null)));
		ops.add(new Op("setKeyBinding", Cost.VIEW, t -> t.setKeyBinding('k' | SWT.MOD1, ST.SELECT_LINE_END)));
		ops.add(new Op("setCursor", Cost.VIEW, t -> t.setCursor(t.getDisplay().getSystemCursor(SWT.CURSOR_HAND))));
		ops.add(new Op("setCursor-reset", Cost.VIEW, t -> t.setCursor((Cursor) null)));
		ops.add(new Op("setCaret", Cost.VIEW, t -> { Caret c = new Caret(t, SWT.NONE); c.setSize(2, t.getLineHeight()); t.setCaret(c); }));
		ops.add(new Op("setDragDetect", Cost.VIEW, t -> t.setDragDetect(false)));
		ops.add(new Op("setEnabled", Cost.VIEW, t -> t.setEnabled(false)));
		ops.add(new Op("setEnabled-reset", Cost.VIEW, t -> t.setEnabled(true)));
		ops.add(new Op("setFocus", Cost.VIEW, StyledText::setFocus));
		// ---- block selection and actions
		ops.add(new Op("setBlockSelection", Cost.VIEW, t -> t.setBlockSelection(true)));
		ops.add(new Op("setBlockSelectionBounds(int,int,int,int)", Cost.VIEW, t -> t.setBlockSelectionBounds(t.getLeftMargin(), t.getLinePixel(t.getTopIndex()) + t.getTopPixel(), 60, 3 * t.getLineHeight())));
		ops.add(new Op("setBlockSelectionBounds(Rectangle)", Cost.VIEW, t -> t.setBlockSelectionBounds(new Rectangle(t.getLeftMargin() + 4, t.getTopPixel() + 2, 40, 2 * t.getLineHeight()))));
		for (int action : new int[] { ST.SELECT_LINE_DOWN, ST.SELECT_COLUMN_NEXT, ST.SELECT_WORD_NEXT, ST.SELECT_LINE_END, ST.SELECT_LINE_UP,
				ST.SELECT_COLUMN_PREVIOUS, ST.SELECT_WORD_PREVIOUS, ST.SELECT_LINE_START, ST.SELECT_PAGE_DOWN, ST.SELECT_PAGE_UP,
				ST.SELECT_WINDOW_END, ST.SELECT_WINDOW_START, ST.SELECT_TEXT_END, ST.SELECT_TEXT_START, ST.LINE_DOWN, ST.COLUMN_NEXT,
				ST.WORD_NEXT, ST.LINE_END, ST.LINE_UP, ST.COLUMN_PREVIOUS, ST.WORD_PREVIOUS, ST.LINE_START, ST.PAGE_DOWN, ST.PAGE_UP,
				ST.WINDOW_END, ST.WINDOW_START, ST.TEXT_END, ST.TEXT_START, ST.SELECT_ALL, ST.DELETE_NEXT, ST.DELETE_PREVIOUS,
				ST.DELETE_WORD_NEXT, ST.DELETE_WORD_PREVIOUS, ST.TOGGLE_OVERWRITE, ST.TOGGLE_OVERWRITE }) {
			Cost cost = action == ST.SELECT_ALL || action == ST.SELECT_TEXT_END || action == ST.SELECT_TEXT_START ? Cost.SELECTION : Cost.VIEW;
			ops.add(new Op("block:invokeAction:" + actionName(action), cost, t -> {
				if (action != ST.SELECT_ALL && action != ST.SELECT_TEXT_END && action != ST.SELECT_TEXT_START) {
					if (t.getBlockSelection()) {
						t.setBlockSelectionBounds(t.getLeftMargin(), t.getLinePixel(line(t, 0.5)) + t.getTopPixel(), 50, 2 * t.getLineHeight());
					} else {
						// word wrap disables block selection: act on a collapsed caret, never on a whole-document selection
						t.setSelection(inLine(t, 0.5, 3));
					}
				}
				t.invokeAction(action);
			}));
		}
		for (int action : new int[] { ST.COPY, ST.CUT, ST.PASTE }) {
			ops.add(new Op("block:invokeAction:" + actionName(action), Cost.SELECTION, t -> {
				if (t.getBlockSelection()) {
					t.setBlockSelectionBounds(t.getLeftMargin(), t.getLinePixel(line(t, 0.4)) + t.getTopPixel(), 40, 2 * t.getLineHeight());
				} else {
					t.setSelectionRange(inLine(t, 0.4, 1), 3);
				}
				t.invokeAction(action);
			}));
		}
		ops.add(new Op("block:type-keys", Cost.VIEW,t -> { t.setBlockSelectionBounds(t.getLeftMargin(), t.getTopPixel(), 30, 2 * t.getLineHeight()); key(t, 'w', 'w'); }));
		ops.add(new Op("toggle:invokeAction:TOGGLE_BLOCKSELECTION", Cost.VIEW, t -> t.invokeAction(ST.TOGGLE_BLOCKSELECTION)));
		ops.add(new Op("setBlockSelection-reset", Cost.VIEW, t -> t.setBlockSelection(false)));
		for (int action : new int[] { ST.LINE_DOWN, ST.LINE_UP, ST.LINE_START, ST.LINE_END, ST.COLUMN_PREVIOUS, ST.COLUMN_NEXT, ST.PAGE_UP,
				ST.PAGE_DOWN, ST.WORD_PREVIOUS, ST.WORD_NEXT, ST.TEXT_START, ST.TEXT_END, ST.WINDOW_START, ST.WINDOW_END,
				ST.SELECT_LINE_UP, ST.SELECT_LINE_DOWN, ST.SELECT_LINE_START, ST.SELECT_LINE_END, ST.SELECT_COLUMN_PREVIOUS,
				ST.SELECT_COLUMN_NEXT, ST.SELECT_PAGE_UP, ST.SELECT_PAGE_DOWN, ST.SELECT_WORD_PREVIOUS, ST.SELECT_WORD_NEXT,
				ST.SELECT_WINDOW_START, ST.SELECT_WINDOW_END, ST.DELETE_PREVIOUS, ST.DELETE_NEXT, ST.DELETE_WORD_PREVIOUS,
				ST.DELETE_WORD_NEXT, ST.TOGGLE_OVERWRITE, ST.TOGGLE_OVERWRITE, ST.SELECT_TEXT_START, ST.SELECT_TEXT_END, ST.SELECT_ALL }) {
			Cost cost = action == ST.SELECT_ALL || action == ST.SELECT_TEXT_END || action == ST.SELECT_TEXT_START ? Cost.SELECTION : Cost.VIEW;
			ops.add(new Op("invokeAction:" + actionName(action), cost, t -> {
				if (action != ST.SELECT_ALL && action != ST.SELECT_TEXT_END && action != ST.SELECT_TEXT_START) {
					t.setSelection(inLine(t, 0.55, 3));
				}
				t.invokeAction(action);
			}));
		}
		ops.add(new Op("multi:invokeAction:SELECT_WORD_NEXT", Cost.VIEW, t -> { t.setSelectionRanges(new int[] { off(t, 0.2), 0, off(t, 0.6), 0 }); t.invokeAction(ST.SELECT_WORD_NEXT); }));
		ops.add(new Op("multi:type-keys", Cost.VIEW, t -> { t.setSelectionRanges(new int[] { off(t, 0.25), 0, off(t, 0.65), 0 }); key(t, 'm', 'm'); }));
		// ---- clipboard (system clipboard; each op copies and pastes inside one widget)
		ops.add(new Op("copy+paste", Cost.SELECTION, t -> { t.setSelectionRange(off(t, 0.3), 12); t.copy(); t.setCaretOffset(off(t, 0.7)); t.paste(); }));
		ops.add(new Op("copy(int)", Cost.SELECTION, t -> { t.setSelectionRange(off(t, 0.33), 8); t.copy(DND.CLIPBOARD); }));
		ops.add(new Op("cut", Cost.SELECTION, t -> { t.setSelectionRange(off(t, 0.35), 6); t.cut(); }));
		ops.add(new Op("invokeAction:COPY", Cost.SELECTION, t -> { t.setSelectionRange(off(t, 0.36), 4); t.invokeAction(ST.COPY); }));
		ops.add(new Op("invokeAction:CUT", Cost.SELECTION, t -> { t.setSelectionRange(off(t, 0.37), 4); t.invokeAction(ST.CUT); }));
		ops.add(new Op("invokeAction:PASTE", Cost.SELECTION, t -> { t.setCaretOffset(off(t, 0.38)); t.invokeAction(ST.PASTE); }));
		// ---- redraw and geometry
		ops.add(new Op("redraw", Cost.VIEW, StyledText::redraw));
		ops.add(new Op("redraw(int,int,int,int,boolean)", Cost.VIEW, t -> t.redraw(0, 0, 50, 50, true)));
		ops.add(new Op("redrawRange", Cost.VIEW, t -> t.redrawRange(off(t, 0.5), 20, true)));
		ops.add(new Op("computeSize", Cost.VIEW, t -> t.computeSize(SWT.DEFAULT, SWT.DEFAULT, true)));
		ops.add(new Op("computeSize-wHint", Cost.VIEW, t -> t.computeSize(150, SWT.DEFAULT, true)));
		// ---- mouse autoscroll timers (the four Runnable.run() bodies); timing-dependent state is restored
		ops.add(new Op("autoscroll-down:run", Cost.VIEW, t -> autoscroll(t, SWT.DOWN)));
		ops.add(new Op("autoscroll-up:run", Cost.VIEW, t -> autoscroll(t, SWT.UP)));
		ops.add(new Op("autoscroll-right:run", Cost.VIEW, t -> autoscroll(t, SWT.RIGHT)));
		ops.add(new Op("autoscroll-left:run", Cost.VIEW, t -> autoscroll(t, SWT.LEFT)));
		// ---- whole-document HTML through getData("StyledText.htmlText") (HTMLWriter)
		ops.add(new Op("getData-htmlText", Cost.CHARS, t -> { Object html = t.getData("StyledText.htmlText"); if (!(html instanceof String h) || !h.contains("<")) throw new IllegalStateException("html"); }));
		// ---- IME (synthetic composition events through the widget's IME object)
		ops.add(new Op("ime-composition", Cost.VIEW, t -> ime(t)));
		// ---- content
		ops.add(new Op("setContent-custom", Cost.CHARS, t -> { StringContent c = new StringContent(); c.setText(t.getText()); t.setContent(c); }));
		ops.add(new Op("setText", Cost.CHARS, t -> t.setText(t.getText())));
		return ops;
	}

	/** Every TextStyle/StyleRange field set at once. */
	static StyleRange features(StyledText t, int start, int length) {
		StyleRange s = new StyleRange();
		s.start = start;
		s.length = length;
		s.fontStyle = SWT.BOLD | SWT.ITALIC;
		s.foreground = color(t, SWT.COLOR_DARK_RED);
		s.background = color(t, SWT.COLOR_YELLOW);
		s.underline = true;
		s.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
		s.underlineColor = color(t, SWT.COLOR_BLUE);
		s.strikeout = true;
		s.strikeoutColor = color(t, SWT.COLOR_GREEN);
		s.borderStyle = SWT.BORDER_DOT;
		s.borderColor = color(t, SWT.COLOR_BLACK);
		s.data = "data";
		return s;
	}

	static String actionName(int action) {
		for (Field f : ST.class.getFields()) {
			try {
				if (f.getType() == int.class && f.getInt(null) == action && !f.getName().startsWith("BULLET")) {
					return f.getName();
				}
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		return Integer.toString(action);
	}

	static void key(StyledText t, char character, int keyCode) {
		Event e = new Event();
		e.character = character;
		e.keyCode = keyCode;
		e.doit = true;
		t.notifyListeners(SWT.KeyDown, e);
		Event up = new Event();
		up.character = character;
		up.keyCode = keyCode;
		t.notifyListeners(SWT.KeyUp, up);
	}

	/** Drags the mouse outside the client area so StyledText's autoscroll timer runs, then restores state. */
	static void autoscroll(StyledText t, int direction) {
		int caret = t.getCaretOffset();
		int top = t.getTopIndex();
		int hpx = t.getHorizontalPixel();
		boolean wrap = t.getWordWrap();
		if (wrap && (direction == SWT.LEFT || direction == SWT.RIGHT)) {
			t.setWordWrap(false);
		}
		t.setTopIndex(line(t, 0.5));
		t.setHorizontalPixel(direction == SWT.LEFT ? 40 : 0);
		Rectangle area = t.getClientArea();
		int before = t.getCaretOffset();
		int topBefore = t.getTopIndex();
		int hBefore = t.getHorizontalPixel();
		mouse(t, SWT.MouseDown, area.width / 2, area.height / 2);
		int x = area.width / 2, y = area.height / 2;
		switch (direction) {
		case SWT.DOWN -> y = area.height + 30;
		case SWT.UP -> y = -30;
		case SWT.RIGHT -> x = area.width + 30;
		default -> x = -30;
		}
		mouse(t, SWT.MouseMove, x, y);
		Display display = t.getDisplay();
		long end = System.currentTimeMillis() + 400;
		while (System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		boolean moved = t.getTopIndex() != topBefore || t.getHorizontalPixel() != hBefore || t.getCaretOffset() != before;
		mouse(t, SWT.MouseUp, x, y);
		if (!moved) {
			throw new IllegalStateException("autoscroll " + direction + " did not run");
		}
		t.setWordWrap(wrap);
		t.setSelection(caret);
		t.setTopIndex(top);
		t.setHorizontalPixel(hpx);
	}

	static void mouse(StyledText t, int type, int x, int y) {
		Event e = new Event();
		e.button = 1;
		e.count = 1;
		e.x = x;
		e.y = y;
		e.stateMask = type == SWT.MouseMove ? SWT.BUTTON1 : 0;
		t.notifyListeners(type, e);
	}

	/**
	 * Emulates an IME composition: the platform IME object's composition state
	 * (start offset, caret, text, commit count) is set reflectively, as the native
	 * input method would, then the composition events are sent: change, selection
	 * and offset queries, commit.
	 */
	static void ime(StyledText t) {
		IME ime = t.getIME();
		if (ime == null) {
			return;
		}
		int at = inLine(t, 0.45, 1);
		try {
			imeState(ime, at, 2, "ime", 0);
			Event e = new Event();
			e.detail = SWT.COMPOSITION_CHANGED;
			e.start = at;
			e.end = at;
			e.text = "ime";
			ime.notifyListeners(SWT.ImeComposition, e);
			Event q = new Event();
			q.detail = SWT.COMPOSITION_SELECTION;
			ime.notifyListeners(SWT.ImeComposition, q);
			Event o = new Event();
			o.detail = SWT.COMPOSITION_OFFSET;
			Point p = t.toDisplay(10, 10);
			o.x = p.x;
			o.y = p.y;
			ime.notifyListeners(SWT.ImeComposition, o);
			imeState(ime, at, 3, "IME", 3);
			Event commit = new Event();
			commit.detail = SWT.COMPOSITION_CHANGED;
			commit.start = at;
			commit.end = at + 3;
			commit.text = "IME";
			ime.notifyListeners(SWT.ImeComposition, commit);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				imeState(ime, -1, 0, "", 0);
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private static void imeState(IME ime, int start, int caret, String text, int commit) throws ReflectiveOperationException {
		set(ime, "startOffset", start);
		set(ime, "caretOffset", caret);
		set(ime, "commitCount", commit);
		Field f = IME.class.getDeclaredField("text");
		f.setAccessible(true);
		f.set(ime, text);
	}

	private static void set(Object target, String name, int value) throws ReflectiveOperationException {
		Field f = target.getClass().getDeclaredField(name);
		f.setAccessible(true);
		f.setInt(target, value);
	}

	// ------------------------------------------------------------------ accessibility (reflection into the platform Accessible)

	@SuppressWarnings("unchecked")
	static <T> T accessibleListener(StyledText t, String field, Class<T> type) {
		try {
			Accessible accessible = t.getAccessible();
			Field f = Accessible.class.getDeclaredField(field);
			f.setAccessible(true);
			List<Object> list = (List<Object>) f.get(accessible);
			for (Object o : list) {
				if (type.isInstance(o)) {
					return (T) o;
				}
			}
			throw new IllegalStateException("no " + type.getSimpleName());
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	/** Runs every StyledText accessibility adapter method and returns a canonical transcript. */
	static String accessibility(StyledText t) {
		focus(t);
		Accessible a = t.getAccessible();
		StringBuilder out = new StringBuilder();
		AccessibleListener names = accessibleListener(t, "accessibleListeners", AccessibleListener.class);
		AccessibleEvent ae = new AccessibleEvent(a);
		names.getName(ae);
		out.append("name=").append(ae.result);
		ae = new AccessibleEvent(a);
		names.getHelp(ae);
		out.append(" help=").append(ae.result);
		ae = new AccessibleEvent(a);
		names.getKeyboardShortcut(ae);
		out.append(" shortcut=").append(ae.result);
		AccessibleControlListener control = accessibleListener(t, "accessibleControlListeners", AccessibleControlListener.class);
		AccessibleControlEvent ce = new AccessibleControlEvent(a);
		ce.childID = ACC.CHILDID_SELF;
		control.getRole(ce);
		out.append(" role=").append(ce.detail);
		ce = new AccessibleControlEvent(a);
		ce.childID = ACC.CHILDID_SELF;
		control.getState(ce);
		out.append(" state=").append(ce.detail);
		ce = new AccessibleControlEvent(a);
		ce.childID = ACC.CHILDID_SELF;
		control.getValue(ce);
		out.append(" value#=").append(ce.result == null ? -1 : ce.result.hashCode());
		AccessibleTextExtendedListener text = accessibleListener(t, "accessibleTextExtendedListeners", AccessibleTextExtendedListener.class);
		AccessibleTextEvent te = new AccessibleTextEvent(a);
		text.getCaretOffset(te);
		out.append(" caret=").append(te.offset);
		te = new AccessibleTextEvent(a);
		text.getCharacterCount(te);
		out.append(" count=").append(te.count);
		te = new AccessibleTextEvent(a);
		text.getSelectionCount(te);
		out.append(" selections=").append(te.count);
		te = new AccessibleTextEvent(a);
		te.index = 0;
		text.getSelection(te);
		out.append(" sel=").append(te.start).append(',').append(te.end);
		te = new AccessibleTextEvent(a);
		text.getSelectionRange(te);
		out.append(" selrange=").append(te.offset).append(',').append(te.length);
		int base = inLine(t, 0.5, 0);
		for (int type : new int[] { ACC.TEXT_BOUNDARY_CHAR, ACC.TEXT_BOUNDARY_WORD, ACC.TEXT_BOUNDARY_LINE, ACC.TEXT_BOUNDARY_SENTENCE,
				ACC.TEXT_BOUNDARY_PARAGRAPH, ACC.TEXT_BOUNDARY_ALL }) {
			te = new AccessibleTextEvent(a);
			te.start = base;
			te.end = Math.min(t.getCharCount(), base + 5);
			te.type = type;
			te.count = type == ACC.TEXT_BOUNDARY_ALL ? 0 : 1;
			text.getText(te);
			out.append(" text").append(type).append('=').append(te.start).append(',').append(te.end).append(':')
					.append(te.result == null ? "null" : Integer.toString(te.result.hashCode()));
		}
		te = new AccessibleTextEvent(a);
		te.start = base;
		te.end = Math.min(t.getCharCount(), base + 3);
		text.getTextBounds(te);
		Point o0 = t.toDisplay(0, 0);
		out.append(" bounds=").append(te.x - o0.x).append(',').append(te.y - o0.y).append(',').append(te.width).append(',').append(te.height);
		te = new AccessibleTextEvent(a);
		te.x = 5;
		te.y = 5;
		Point d = t.toDisplay(5, 5);
		te.x = d.x;
		te.y = d.y;
		text.getOffsetAtPoint(te);
		out.append(" atpoint=").append(te.offset);
		te = new AccessibleTextEvent(a);
		text.getVisibleRanges(te);
		out.append(" visible=").append(te.ranges == null ? "null" : Arrays.toString(te.ranges));
		te = new AccessibleTextEvent(a);
		Point origin = t.toDisplay(0, 0);
		te.x = origin.x;
		te.y = origin.y;
		te.width = 100;
		te.height = 3 * t.getLineHeight();
		text.getRanges(te);
		out.append(" ranges=").append(te.ranges == null ? "null" : Arrays.toString(te.ranges));
		te = new AccessibleTextEvent(a);
		te.offset = base;
		text.setCaretOffset(te);
		out.append(" setcaret=").append(te.result).append('/').append(t.getCaretOffset());
		te = new AccessibleTextEvent(a);
		te.start = base;
		te.end = base + 2;
		text.addSelection(te);
		te = new AccessibleTextEvent(a);
		te.index = 0;
		te.start = base;
		te.end = base + 3;
		text.setSelection(te);
		out.append(" setsel=").append(t.getSelection());
		te = new AccessibleTextEvent(a);
		te.index = 0;
		text.removeSelection(te);
		out.append(" removesel=").append(t.getSelection());
		te = new AccessibleTextEvent(a);
		te.start = base;
		te.end = base + 2;
		te.type = ACC.SCROLL_TYPE_TOP_LEFT;
		text.scrollText(te);
		out.append(" scroll=").append(t.getTopIndex());
		AccessibleAttributeListener attributes = accessibleListener(t, "accessibleAttributeListeners", AccessibleAttributeListener.class);
		AccessibleAttributeEvent atte = new AccessibleAttributeEvent(a);
		attributes.getAttributes(atte);
		out.append(" attrs=").append(atte.leftMargin).append(',').append(atte.indent).append(',').append(atte.alignment);
		AccessibleTextAttributeEvent tae = new AccessibleTextAttributeEvent(a);
		tae.offset = base;
		attributes.getTextAttributes(tae);
		out.append(" textattrs=").append(tae.start).append(',').append(tae.end);
		AccessibleEditableTextListener editable = accessibleListener(t, "accessibleEditableTextListeners", AccessibleEditableTextListener.class);
		AccessibleEditableTextEvent ee = new AccessibleEditableTextEvent(a);
		ee.start = base;
		ee.end = base + 1;
		ee.string = "A";
		editable.replaceText(ee);
		out.append(" replace=").append(ee.result);
		ee = new AccessibleEditableTextEvent(a);
		ee.start = base;
		ee.end = base + 2;
		editable.copyText(ee);
		ee = new AccessibleEditableTextEvent(a);
		ee.start = base + 3;
		editable.pasteText(ee);
		ee = new AccessibleEditableTextEvent(a);
		ee.start = base;
		ee.end = base + 1;
		editable.cutText(ee);
		out.append(" cut=").append(ee.result);
		AccessibleTextAttributeEvent set = new AccessibleTextAttributeEvent(a);
		set.start = base;
		set.end = base + 2;
		TextStyle style = new TextStyle();
		style.foreground = color(t, SWT.COLOR_RED);
		set.textStyle = style;
		editable.setTextAttributes(set);
		out.append(" setattrs=").append(set.result);
		return out.toString();
	}

	// ------------------------------------------------------------------ drag and drop effect

	/** Drives StyledTextDropTargetEffect through enter/over/leave with a reflectively built DropTargetEvent. */
	static String dropEffect(StyledText t) {
		DropTarget target = new DropTarget(t, DND.DROP_COPY | DND.DROP_MOVE);
		try {
			target.setTransfer(TextTransfer.getInstance());
			if (!(target.getDropTargetEffect() instanceof StyledTextDropTargetEffect effect)) {
				throw new IllegalStateException("StyledText drop effect missing: " + target.getDropTargetEffect());
			}
			Class<?> dndEventClass = Class.forName("org.eclipse.swt.dnd.DNDEvent");
			Constructor<?> dndCtor = dndEventClass.getDeclaredConstructor();
			dndCtor.setAccessible(true);
			Object dnd = dndCtor.newInstance();
			Point p = t.toDisplay(20, t.getLineHeight() * 2);
			Event base = (Event) dnd;
			base.widget = target;
			base.x = p.x;
			base.y = p.y;
			base.time = 1;
			Field feedback = dndEventClass.getField("feedback");
			feedback.setAccessible(true);
			feedback.setInt(dnd, DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL);
			Constructor<DropTargetEvent> ctor = DropTargetEvent.class.getDeclaredConstructor(dndEventClass);
			ctor.setAccessible(true);
			DropTargetEvent event = ctor.newInstance(dnd);
			effect.dragEnter(event);
			effect.dragOver(event);
			effect.dropAccept(event);
			effect.dragLeave(event);
			return "caret=" + t.getCaretOffset() + " top=" + t.getTopIndex();
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		} finally {
			target.dispose();
		}
	}

	// ------------------------------------------------------------------ snapshots

	static String snapshot(StyledText t, boolean withText, boolean withPixels) {
		if (withPixels) {
			focus(t);
		}
		StringBuilder s = new StringBuilder();
		int lines = t.getLineCount();
		s.append("chars=").append(t.getCharCount()).append(" lines=").append(lines).append(" delim=").append(t.getLineDelimiter().length());
		if (withText) {
			s.append(" text#=").append(t.getText().hashCode());
			s.append(" text(a,b)#=").append(t.getText(0, Math.max(0, Math.min(t.getCharCount(), 40) - 1)).hashCode());
		}
		s.append(" caret=").append(t.getCaretOffset()).append(" sel=").append(t.getSelection()).append(" selRange=").append(t.getSelectionRange())
				.append(" ranges=").append(Arrays.toString(t.getSelectionRanges())).append(" selCount=").append(t.getSelectionCount())
				.append(" selText#=").append(t.getSelectionText().hashCode()).append(" textSelected=").append(t.isTextSelected());
		s.append(" top=").append(t.getTopIndex()).append(" topPx=").append(t.getTopPixel()).append(" hIdx=").append(t.getHorizontalIndex())
				.append(" hPx=").append(t.getHorizontalPixel());
		s.append(" wrap=").append(t.getWordWrap()).append(" editable=").append(t.getEditable()).append(" tabs=").append(t.getTabs())
				.append(" tabStops=").append(Arrays.toString(t.getTabStops())).append(" margins=").append(t.getLeftMargin()).append(',')
				.append(t.getTopMargin()).append(',').append(t.getRightMargin()).append(',').append(t.getBottomMargin())
				.append(" indent=").append(t.getIndent()).append(" wrapIndent=").append(t.getWrapIndent()).append(" justify=").append(t.getJustify())
				.append(" align=").append(t.getAlignment()).append(" spacing=").append(t.getLineSpacing()).append(" lineHeight=").append(t.getLineHeight())
				.append(" baseline=").append(t.getBaseline()).append(" limit=").append(t.getTextLimit()).append(" dbl=").append(t.getDoubleClickEnabled())
				.append(" mouseNav=").append(t.getMouseNavigatorEnabled()).append(" orient=").append(t.getOrientation()).append(" bidi=").append(t.getBidiColoring())
				.append(" block=").append(t.getBlockSelection()).append(" blockBounds=").append(t.getBlockSelectionBounds())
				.append(" fg=").append(t.getForeground()).append(" bg=").append(t.getBackground()).append(" selFg=").append(t.getSelectionForeground())
				.append(" selBg=").append(t.getSelectionBackground()).append(" marginColor=").append(t.getMarginColor())
				.append(" alwaysScroll=").append(t.getAlwaysShowScrollBars()).append(" dragDetect=").append(t.getDragDetect())
				.append(" keyK=").append(t.getKeyBinding('k' | SWT.MOD1)).append(" keyEnd=").append(t.getKeyBinding(SWT.END));
		int[] probe = { 0, line(t, 0.1), line(t, 0.2), line(t, 0.25), line(t, 0.27), line(t, 0.3), line(t, 0.32), line(t, 0.36), line(t, 0.5), lines - 1 };
		for (int i : probe) {
			Bullet b = t.getLineBullet(i);
			s.append(" L").append(i).append('[').append(t.getOffsetAtLine(i)).append(':').append(t.getLine(i).hashCode())
					.append(" a").append(t.getLineAlignment(i)).append(" i").append(t.getLineIndent(i)).append(" w").append(t.getLineWrapIndent(i))
					.append(" j").append(t.getLineJustify(i)).append(" v").append(t.getLineVerticalIndent(i))
					.append(" bg").append(t.getLineBackground(i)).append(" b").append(b == null ? "null" : b.type + "/" + b.text)
					.append(" ts").append(Arrays.toString(t.getLineTabStops(i))).append(']');
		}
		int mid = inLine(t, 0.5, 2);
		s.append(" lineAt=").append(t.getLineAtOffset(mid)).append(" lh(off)=").append(t.getLineHeight(mid)).append(" base(off)=").append(t.getBaseline(mid))
				.append(" loc=").append(t.getLocationAtOffset(mid)).append(" linePx=").append(t.getLinePixel(line(t, 0.5)))
				.append(" lineIdx=").append(t.getLineIndex(17)).append(" atPoint=").append(t.getOffsetAtPoint(new Point(12, 9)))
				.append(" bounds=").append(t.getTextBounds(mid, Math.min(t.getCharCount() - 1, mid + 4)))
				.append(" textRange#=").append(t.getTextRange(mid, Math.min(5, t.getCharCount() - mid)).hashCode());
		try {
			s.append(" atLocation=").append(t.getOffsetAtLocation(new Point(12, 9)));
		} catch (IllegalArgumentException e) {
			s.append(" atLocation=IAE");
		}
		StyleRange at = t.getStyleRangeAtOffset(mid);
		s.append(" styleAt=").append(at);
		int a = off(t, 0.0), b = off(t, 0.6);
		s.append(" styles(a,b)=").append(Arrays.toString(t.getStyleRanges(a, b - a))).append(" styles(a,b,false)#=")
				.append(Arrays.hashCode(t.getStyleRanges(a, b - a, false))).append(" ranges(a,b)=").append(Arrays.toString(t.getRanges(a, b - a)));
		if (withText) {
			s.append(" styles()=").append(Arrays.toString(t.getStyleRanges())).append(" styles(true)#=").append(Arrays.toString(t.getStyleRanges(true)).hashCode())
					.append(" ranges()=").append(Arrays.toString(t.getRanges()));
		}
		if (withPixels) {
			s.append(" pixels=").append(pixels(t));
		}
		return s.toString();
	}

	/** Gives t keyboard focus so focus-dependent state and rendering are compared symmetrically. */
	static void focus(StyledText t) {
		t.getShell().setActive();
		t.forceFocus();
		Display display = t.getDisplay();
		long deadline = System.currentTimeMillis() + 500;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// let activation and focus events settle
		}
	}

	/**
	 * SHA-256 prefix of the control's own rendering with the caret hidden: the harness render
	 * ({@link ViewportDifferential#render}, WM_PRINT on win32), which is synchronous; Control.print
	 * captures the composited surface on win32 and was not stable between captures.
	 */
	static String pixels(StyledText t) {
		Caret caret = t.getCaret();
		boolean visible = caret != null && caret.getVisible();
		if (caret != null) {
			caret.setVisible(false);
		}
		try {
			ImageData data = ViewportDifferential.render(t);
			// client area only: borders and scrollbars carry hover/focus theme state of the desktop
			Rectangle trim = t.computeTrim(0, 0, 0, 0);
			Rectangle client = t.getClientArea();
			int x0 = Math.max(0, -trim.x), y0 = Math.max(0, -trim.y);
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] px = new byte[4];
			for (int y = y0; y < Math.min(data.height, y0 + client.height); y++) {
				for (int x = x0; x < Math.min(data.width, x0 + client.width); x++) {
					int v = data.getPixel(x, y);
					px[0] = (byte) (v >>> 24); px[1] = (byte) (v >>> 16); px[2] = (byte) (v >>> 8); px[3] = (byte) v;
					sha.update(px);
				}
			}
			String hash = HexFormat.of().formatHex(sha.digest(), 0, 8);
			String dump = System.getProperty("swt.styledtext.dumpPixels");
			if (dump != null) {
				org.eclipse.swt.graphics.ImageLoader loader = new org.eclipse.swt.graphics.ImageLoader();
				loader.data = new ImageData[] { data };
				loader.save(dump + "/" + hash + ".png", SWT.IMAGE_PNG);
			}
			return hash;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			if (caret != null) {
				caret.setVisible(visible);
			}
		}
	}

	/** Observable count of line layouts requested from the renderer (package-private counter). */
	static long layoutRequests(StyledText t) {
		try {
			Field rendererField = StyledText.class.getDeclaredField("renderer");
			rendererField.setAccessible(true);
			Object renderer = rendererField.get(t);
			Field count = renderer.getClass().getDeclaredField("layoutRequests");
			count.setAccessible(true);
			return count.getLong(renderer);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	/** A minimal custom StyledTextContent (setContent with a client implementation). */
	static final class StringContent implements StyledTextContent {
		private final List<TextChangeListener> listeners = new ArrayList<>();
		private String text = "";
		private int[] starts = { 0 };

		@Override
		public void addTextChangeListener(TextChangeListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeTextChangeListener(TextChangeListener listener) {
			listeners.remove(listener);
		}

		@Override
		public int getCharCount() {
			return text.length();
		}

		@Override
		public String getLine(int lineIndex) {
			int start = starts[lineIndex];
			int end = lineIndex + 1 < starts.length ? starts[lineIndex + 1] : text.length();
			while (end > start && (text.charAt(end - 1) == '\n' || text.charAt(end - 1) == '\r')) {
				end--;
			}
			return text.substring(start, end);
		}

		@Override
		public int getLineAtOffset(int offset) {
			int index = Arrays.binarySearch(starts, offset);
			return index >= 0 ? index : -index - 2;
		}

		@Override
		public int getLineCount() {
			return starts.length;
		}

		@Override
		public String getLineDelimiter() {
			return "\n";
		}

		@Override
		public int getOffsetAtLine(int lineIndex) {
			return starts[lineIndex];
		}

		@Override
		public String getTextRange(int start, int length) {
			return text.substring(start, start + length);
		}

		@Override
		public void replaceTextRange(int start, int replaceLength, String newText) {
			TextChangingEvent changing = new TextChangingEvent(this);
			changing.start = start;
			changing.newText = newText;
			changing.replaceCharCount = replaceLength;
			changing.newCharCount = newText.length();
			changing.replaceLineCount = getLineAtOffset(start + replaceLength) - getLineAtOffset(start);
			changing.newLineCount = (int) newText.chars().filter(c -> c == '\n').count();
			for (TextChangeListener l : List.copyOf(listeners)) {
				l.textChanging(changing);
			}
			index(text.substring(0, start) + newText + text.substring(start + replaceLength));
			for (TextChangeListener l : List.copyOf(listeners)) {
				l.textChanged(new TextChangedEvent(this));
			}
		}

		@Override
		public void setText(String newText) {
			index(newText.replace("\r\n", "\n").replace('\r', '\n'));
			for (TextChangeListener l : List.copyOf(listeners)) {
				l.textSet(new TextChangedEvent(this));
			}
		}

		private void index(String value) {
			text = value;
			List<Integer> list = new ArrayList<>();
			list.add(0);
			for (int i = value.indexOf('\n'); i >= 0; i = value.indexOf('\n', i + 1)) {
				list.add(i + 1);
			}
			starts = list.stream().mapToInt(Integer::intValue).toArray();
		}
	}

	/** Prints a line-style event in canonical form (for LineStyleListener coverage). */
	static String describe(LineStyleEvent e) {
		return e.lineOffset + ":" + e.lineText.length();
	}

	/** Font derived from the widget font with a style flag (for StyleRange.font). */
	static Font derived(StyledText t, int style) {
		FontData[] data = t.getFont().getFontData();
		for (FontData d : data) {
			d.setStyle(style);
		}
		return new Font(t.getDisplay(), data);
	}

	static Method method(Class<?> type, String name, Class<?>... parameters) throws NoSuchMethodException {
		Method m = type.getDeclaredMethod(name, parameters);
		m.setAccessible(true);
		return m;
	}
}
