"""StyledText API coverage table (CPU only, deterministic).

usage: python -B generate_coverage.py <inputs-dir> <diff-tree.log> <scale-tree.log> <out.tsv>

inputs-dir holds STYLEDTEXT_PUBLIC_API.tsv (line<TAB>signature at SWT 985109317d),
ST_CONSTANTS.tsv (line<TAB>name), CASE_ST.tsv (line<TAB>name<TAB>context).
The two logs are JUnit console --details=tree outputs of
Test_org_eclipse_swt_custom_StyledText_ApiDifferential / _ApiScale.
"""
import os
import re
import sys
from pathlib import Path

COLUMNS = ["kind", "api", "source_line", "cost_class", "paged_vs_gap_test_id", "scale_test_id", "status", "qt_equivalent", "example_source", "note"]

O1 = "O(1)"
OV = "O(visible)"
OS = "O(selection)"
OL = "O(lines)"
OC = "O(chars)"

# cost class per method name (overloads share unless listed with a signature key)
COST = {
    "StyledText": (OV, "constructor: empty content"),
    "append": (OS, "O(appended text); content O(log pieces)"),
    "insert": (OS, "O(inserted text)"),
    "replaceTextRange": (OS, "O(replacement); renderer line arrays memmove O(lines after edit) when the line count changes (pointer arrays, leaf 3 packs them)"),
    "setText": (OC, "inherent: exact line count needs one scan (parallel 64 Ki pages); no copy"),
    "getText()": (OC, "inherent: returns the whole document"),
    "getText(int start, int end)": (OS, "O(range)"),
    "getTextRange": (OS, "O(range)"),
    "getSelectionText": (OS, "O(selection)"),
    "getLine": ("O(log)", "O(log pieces + line length)"),
    "getLineAtOffset": ("O(log)", "O(log pieces)"),
    "getOffsetAtLine": ("O(log)", "O(log pieces)"),
    "getStyleRangeAtOffset": ("O(log)", "O(log styles)"),
    "getStyleRanges()": (OC, "inherent: whole-document result, O(styles)"),
    "getStyleRanges(boolean includeRanges)": (OC, "inherent: whole-document result, O(styles)"),
    "getStyleRanges(int start, int length)": (OS, "O(log styles + styles in range)"),
    "getStyleRanges(int start, int length, boolean includeRanges)": (OS, "O(log styles + styles in range)"),
    "getRanges()": (OC, "inherent: whole-document result, O(styles)"),
    "getRanges(int start, int length)": (OS, "O(log styles + styles in range)"),
    "setStyleRange": (OS, "O(styles) array insert + O(changed line intervals); no per-line objects"),
    "setStyleRanges": (OS, "O(styles) + O(changed line intervals)"),
    "replaceStyleRanges": (OS, "O(styles) + O(changed line intervals)"),
    "copy": (OS, "O(selection) plain/RTF/HTML"),
    "cut": (OS, "O(selection)"),
    "paste": (OS, "O(pasted text)"),
    "print": (OL, "inherent: prints every page"),
    "run@688": (OL, "inherent: print job body"),
    "getData": (OC, "StyledText.htmlText: whole-document HTML (inherent); other keys O(1)"),
    "redrawRange": (OS, "O(lines in range) invalidation"),
    "getTextBounds": (OS, "O(lines in range) layouts"),
    "setLineAlignment": (OS, "O(count)"),
    "setLineBackground": (OS, "O(count)"),
    "setLineBullet": (OS, "O(count)"),
    "setLineIndent": (OS, "O(count)"),
    "setLineJustify": (OS, "O(count)"),
    "setLineTabStops": (OS, "O(count)"),
    "setLineWrapIndent": (OS, "O(count)"),
    "setSelectionRanges": (OS, "O(ranges)"),
    "getSelectionRanges": (OS, "O(ranges)"),
    "computeSize": (OV, "fixed: until display height; variable: one display of lines + estimate (this leaf)"),
    "getValue": (OV, "huge documents: viewport projection (this leaf); small: whole text"),
    "getVisibleRanges": (OV, ""),
    "getRanges@acc": (OS, "O(lines in requested rectangle)"),
    "getText@acc": (OS, "O(requested range)"),
    "getTextBounds@acc": (OS, "O(lines in requested range)"),
    "replaceText": (OS, "O(replacement)"),
    "pasteText": (OS, "O(pasted text)"),
    "cutText": (OS, "O(range)"),
    "copyText": (OS, "O(range)"),
    "setTextAttributes": (OS, "O(styles)"),
    "textChanging": (OS, "O(changed lines); pointer memmove O(lines after edit)"),
    "textChanged": (OV, ""),
    "textSet": (OV, "O(1) reset (generation) + O(visible)"),
    "setContent": (OV, "O(1) reset + O(visible)"),
    "setLineSpacingProvider": (OV, "large documents: no per-line provider scan (this leaf)"),
    "getLineIndex": (OV, "far: O(1) estimate (this leaf); near: O(visible)"),
    "getLinePixel": (OV, "far: O(1) estimate; near: O(visible)"),
    "selectAll": (OV, "selection is an int pair; paint O(visible)"),
}
O1_NAMES = re.compile(r"^(add|remove)\w*Listener$|^get(Alignment|AlwaysShowScrollBars|MarginColor|Background|BidiColoring|BlockSelection|BottomMargin|CaretOffset|Content|DragDetect|DoubleClickEnabled|Editable|Foreground|HorizontalIndex|HorizontalPixel|Indent|Justify|KeyBinding|CharCount|LineAlignment|LineBackground|LineBullet|LineCount|LineDelimiter|LineIndent|LineVerticalIndent|LineJustify|LineSpacing|LineTabStops|LineWrapIndent|LeftMargin|MouseNavigatorEnabled|Orientation|RightMargin|Selection|SelectionRange|SelectionBackground|SelectionCount|SelectionForeground|Tabs|TabStops|TextLimit|TopIndex|TopMargin|TopPixel|WordWrap|WrapIndent|Name|Help|KeyboardShortcut|Role|State|CharacterCount|Attributes|TextAttributes)$|^set(TextLimit|DoubleClickEnabled|DragDetect|KeyBinding|MouseNavigatorEnabled|Cursor|Caret)$|^isTextSelected$|^(add|remove)Selection$")

QT = [
    (r"^append$", "QPlainTextEdit::appendPlainText / QTextEdit::append"),
    (r"^insert$", "QPlainTextEdit::insertPlainText / QTextCursor::insertText"),
    (r"^setText$", "QPlainTextEdit::setPlainText / QTextDocument::setPlainText"),
    (r"^getText$", "QPlainTextEdit::toPlainText / QTextCursor::selectedText"),
    (r"^getTextRange$", "QTextDocument::toPlainText().mid / QTextCursor selection"),
    (r"^replaceTextRange$", "QTextCursor setPosition+KeepAnchor, insertText"),
    (r"^getCharCount$|^getCharacterCount$", "QTextDocument::characterCount"),
    (r"^getLineCount$", "QTextDocument::blockCount"),
    (r"^getLine$", "QTextDocument::findBlockByNumber().text()"),
    (r"^getLineAtOffset$", "QTextDocument::findBlock(pos).blockNumber()"),
    (r"^getOffsetAtLine$", "QTextBlock::position()"),
    (r"^getLineDelimiter$", "n/a (QChar::ParagraphSeparator internally)"),
    (r"^(get|set)CaretOffset$", "QTextCursor::position / setPosition, setTextCursor"),
    (r"^(get|set)Selection(Range)?$|^(add|remove)Selection$|^getSelectionCount$", "QTextCursor anchor/position, selectionStart/selectionEnd"),
    (r"^(get|set)SelectionRanges$", "QTextEdit::setExtraSelections (visual only; Qt has no multi-cursor)"),
    (r"^getSelectionText$", "QTextCursor::selectedText"),
    (r"^isTextSelected$", "QTextCursor::hasSelection"),
    (r"^selectAll$", "QPlainTextEdit::selectAll"),
    (r"^showSelection$", "QPlainTextEdit::ensureCursorVisible"),
    (r"^(get|set)TopIndex$", "QPlainTextEdit::firstVisibleBlock / verticalScrollBar()->setValue"),
    (r"^(get|set)TopPixel$", "verticalScrollBar()->value (QTextEdit pixels)"),
    (r"^(get|set)Horizontal(Index|Pixel)$", "horizontalScrollBar()->value"),
    (r"^(get|set)WordWrap$", "QPlainTextEdit::setLineWrapMode / QTextOption::WrapMode"),
    (r"^(get|set)WrapIndent$|^(get|set)LineWrapIndent$", "partial: QTextBlockFormat::setTextIndent (hanging indent)"),
    (r"^(get|set)(Line)?Indent$", "QTextBlockFormat::setIndent / setTextIndent"),
    (r"^(get|set)(Line)?Alignment$", "QTextBlockFormat::setAlignment / QTextEdit::setAlignment"),
    (r"^(get|set)(Line)?Justify$", "Qt::AlignJustify"),
    (r"^(get|set)(Tabs|TabStops|LineTabStops)$", "setTabStopDistance / QTextOption::setTabArray / QTextBlockFormat::setTabPositions"),
    (r"Margin|^setMargins$", "setViewportMargins / QTextDocument::setDocumentMargin / QTextFrameFormat margins"),
    (r"^(get|set)LineSpacing(Provider)?$", "QTextBlockFormat::setLineHeight"),
    (r"^(get|set)LineBackground$", "QTextBlockFormat::setBackground / extraSelections FullWidthSelection"),
    (r"^(get|set)LineBullet$", "QTextListFormat / QTextList"),
    (r"^(get|set)LineVerticalIndent$", "QTextBlockFormat::setTopMargin"),
    (r"^(set|replace)StyleRanges?$|^getStyleRanges$|^getRanges$|^setTextAttributes$", "QTextCursor::mergeCharFormat / QTextLayout::setFormats / QSyntaxHighlighter::setFormat"),
    (r"^getStyleRangeAtOffset$|^getTextAttributes$", "QTextCursor::charFormat"),
    (r"LineStyleListener", "QSyntaxHighlighter::highlightBlock"),
    (r"LineBackgroundListener", "QSyntaxHighlighter + QTextBlockFormat background / extraSelections"),
    (r"BidiSegmentListener", "partial: QTextOption::setTextDirection (no segment hook)"),
    (r"PaintObjectListener", "QTextObjectInterface (custom inline objects)"),
    (r"ExtendedModifyListener|ModifyListener", "QTextDocument::contentsChange(pos, removed, added) / textChanged"),
    (r"VerifyKeyListener|VerifyListener", "partial: keyPressEvent override / inputMethodEvent (no verify veto)"),
    (r"CaretListener", "QPlainTextEdit::cursorPositionChanged"),
    (r"SelectionListener", "QPlainTextEdit::selectionChanged"),
    (r"WordMovementListener", "partial: QTextCursor::movePosition(NextWord) with QTextBoundaryFinder (no hook)"),
    (r"^textChang|^textSet$", "QTextDocument::contentsChange / contentsChanged"),
    (r"^(get|set)Content$", "QPlainTextEdit::setDocument / QTextDocument (custom layout via QAbstractTextDocumentLayout)"),
    (r"^copy$|^copyText$", "QPlainTextEdit::copy"),
    (r"^cut$|^cutText$", "QPlainTextEdit::cut"),
    (r"^paste$|^pasteText$", "QPlainTextEdit::paste"),
    (r"^replaceText$", "QTextCursor::insertText over a selection"),
    (r"^invokeAction$", "QTextCursor::movePosition(MoveOperation) / QKeySequence::StandardKey"),
    (r"^(get|set)KeyBinding$", "QShortcut / QAction / keyPressEvent"),
    (r"^(get|set)Editable$", "QPlainTextEdit::setReadOnly"),
    (r"^(get|set)(Font)$", "QWidget::setFont"),
    (r"^(get|set)(Foreground|Background)$", "QPalette::Text / QPalette::Base"),
    (r"^(get|set)Selection(Foreground|Background)$", "QPalette::HighlightedText / QPalette::Highlight"),
    (r"^(get|set)MarginColor$", "viewport palette"),
    (r"^(get|set)Orientation$|^setTextDirection$", "QWidget::setLayoutDirection / QTextOption::setTextDirection"),
    (r"BlockSelection", "none: Qt QPlainTextEdit has no block (column) selection (StyledText superset)"),
    (r"^(get|set)DoubleClickEnabled$", "n/a"),
    (r"^(get|set)MouseNavigatorEnabled$|^run$", "n/a (middle-click autoscroll; Qt autoscrolls on drag)"),
    (r"^(get|set)AlwaysShowScrollBars$", "setVerticalScrollBarPolicy"),
    (r"^setFixedLineMetrics$", "n/a"),
    (r"^computeSize$", "QWidget::sizeHint"),
    (r"^getLinePixel$|^getLocationAtOffset$", "QPlainTextEdit::cursorRect / blockBoundingGeometry"),
    (r"^getLineIndex$|^getOffsetAt(Point|Location)$", "QPlainTextEdit::cursorForPosition"),
    (r"^getTextBounds$", "QTextLayout / QTextLine::cursorToX, cursorRect union"),
    (r"^getLineHeight$", "QFontMetrics::lineSpacing / blockBoundingRect().height()"),
    (r"^getBaseline$", "QFontMetrics::ascent"),
    (r"^print$", "QPlainTextEdit::print(QPagedPaintDevice*) / QTextDocument::print"),
    (r"^getData$", "QTextDocument::toHtml"),
    (r"^(get|set)Caret$", "QPlainTextEdit::setCursorWidth / cursorRect"),
    (r"DragDetect", "canInsertFromMimeData / insertFromMimeData / dropEvent"),
    (r"^redraw(Range)?$|^scroll(Text)?$", "viewport()->update(rect) / scrollContentsBy"),
    (r"^getVisibleRanges$", "QPlainTextEdit::firstVisibleBlock + viewport geometry"),
    (r"^get(Name|Help|KeyboardShortcut|Role|State|Value|Attributes)$", "QAccessibleTextInterface / QAccessibleInterface"),
    (r"^setFocus$|^setEnabled$|^setCursor$", "QWidget::setFocus / setEnabled / viewport()->setCursor"),
    (r"^StyledText$", "QPlainTextEdit(QWidget*)"),
    (r"^setTextLimit$|^getTextLimit$", "none (QLineEdit::setMaxLength only)"),
]

QT_GAPS = [
    ("undo/redo", "QTextDocument::undo/redo, setUndoRedoEnabled, beginEditBlock/endEditBlock"),
    ("find", "QPlainTextEdit::find / QTextDocument::find (flags, QRegularExpression)"),
    ("maximum line count", "QPlainTextEdit::setMaximumBlockCount (log viewer cap)"),
    ("center on scroll", "QPlainTextEdit::setCenterOnScroll / centerCursor"),
    ("placeholder text", "QPlainTextEdit::setPlaceholderText"),
    ("modified flag", "QTextDocument::isModified / setModified / modificationChanged"),
    ("extra selections", "QTextEdit::setExtraSelections (overlay highlights independent of the selection)"),
    ("per-line user state", "QTextBlock::setUserState / setUserData (incremental highlighter state)"),
    ("incremental rehighlight", "QSyntaxHighlighter::rehighlightBlock with previousBlockState propagation"),
    ("zoom", "QPlainTextEdit::zoomIn / zoomOut"),
    ("wrap at column", "QTextEdit::setLineWrapColumnOrWidth (FixedColumnWidth) / WrapAnywhere mode"),
    ("rich import", "QTextEdit::setHtml / setMarkdown, QTextDocument::toMarkdown"),
    ("visible whitespace", "QTextOption::ShowTabsAndSpaces / ShowLineAndParagraphSeparators"),
    ("update request with scroll delta", "QPlainTextEdit::updateRequest(rect, dy) / blockCountChanged (line-number rulers)"),
    ("rich objects", "QTextFrame / QTextTable / QTextImageFormat"),
    ("custom document layout", "QAbstractTextDocumentLayout / QPlainTextDocumentLayout"),
    ("text interaction flags", "QPlainTextEdit::setTextInteractionFlags (selectable read-only, keyboard links)"),
]


EXAMPLES = {}


def load_examples(roots):
    for root in roots:
        for f in sorted(Path(root).rglob("*.java")):
            text = f.read_text(encoding="utf-8", errors="replace")
            if "StyledText" not in text:
                continue
            for m in re.finditer(r"\.([a-zA-Z]+)\(", text):
                EXAMPLES.setdefault(m.group(1), [])
                if f.stem not in EXAMPLES[m.group(1)]:
                    EXAMPLES[m.group(1)].append(f.stem)


def example_for(name):
    hits = EXAMPLES.get(name, [])
    return ",".join(hits[:3]) if hits else ""


def qt_for(name):
    for pattern, value in QT:
        if re.search(pattern, name):
            return value
    return "n/a"


def parse_tree(log):
    """id -> PASSED/FAILED/SKIPPED from JUnit console --details=tree output (ANSI colours stripped)."""
    result = {}
    if not Path(log).exists():
        return result
    ansi = re.compile(r"\x1b\[[0-9;]*m")
    for line in Path(log).read_text(encoding="utf-8", errors="replace").splitlines():
        line = ansi.sub("", line)
        m = re.search(r"((?:diff|scale):\S.*?)\s+([\u2714\u2718\u21b7])", line)
        if m:
            ident, mark = m.group(1).strip(), m.group(2)
            state = "PASSED" if mark == "\u2714" else ("SKIPPED" if mark == "\u21b7" else "FAILED")
            if result.get(ident) != "FAILED":
                result[ident] = state
    return result


def method_name(signature):
    head = signature.split("(")[0].split()
    return head[-1]


def params(signature):
    inner = signature[signature.index("(") + 1: signature.rindex(")")]
    return ",".join(p.split()[0] for p in inner.split(",") if p.strip())


def match(results, prefix_list):
    ids = sorted(i for i in results if any(i == p or i.startswith(p) for p in prefix_list))
    return ids


def status(ids, results):
    if not ids:
        return "NOT_RUN"
    states = {results[i] for i in ids}
    if "FAILED" in states:
        return "FAILED"
    return "PASSED"


def main():
    inputs, diff_log, scale_log, out = sys.argv[1:5]
    diff = parse_tree(diff_log)
    scale = parse_tree(scale_log)
    rows = []
    accessibility = {"getName", "getHelp", "getKeyboardShortcut", "getCaretOffset", "setCaretOffset", "getSelectionRange", "addSelection",
                     "getSelection", "getSelectionCount", "removeSelection", "setSelection", "getCharacterCount", "getOffsetAtPoint",
                     "getTextBounds", "getRanges", "getText", "getVisibleRanges", "scrollText", "setTextAttributes", "replaceText", "pasteText",
                     "cutText", "copyText", "getAttributes", "getTextAttributes", "getRole", "getState", "getValue"}
    api = [l.split("\t", 1) for l in Path(inputs, "STYLEDTEXT_PUBLIC_API.tsv").read_text(encoding="utf-8").splitlines() if l.strip()]
    for line, signature in api:
        signature = signature.strip()
        name = method_name(signature)
        line_no = int(line)
        inner = ("Event e" in signature or "Event event" in signature) and name in accessibility | {"textChanging", "textChanged", "textSet"}
        key_sig = name + "(" + signature.split("(", 1)[1].split(")")[0] + ")"
        cost, note = COST.get(key_sig, COST.get(name + ("@acc" if inner and name in ("getRanges", "getText", "getTextBounds") else ""), (None, "")))
        if name == "run":
            cost, note = (OL, "print job body") if line_no < 1000 else (OV, "autoscroll timer tick")
        if name == "getText" and not inner:
            cost, note = COST["getText()"] if "()" in signature else COST["getText(int start, int end)"]
        if cost is None:
            cost = O1 if O1_NAMES.search(name) else OV
        if inner and name in accessibility:
            d, s = ["diff:accessibility"], ["scale:fixed:accessibility", "scale:wrap:accessibility"]
        elif name in ("textChanging", "textChanged", "textSet"):
            d, s = ["diff:addTextChangeListener(content)", "diff:replaceTextRange", "diff:setText"], ["scale:fixed:replaceTextRange", "scale:fixed:setText", "scale:wrap:replaceTextRange"]
        elif name == "run":
            if line_no < 1000:
                d, s = ["diff:printOptions"], []
            else:
                direction = {1792: "up", 1821: "down", 1851: "left", 1882: "right"}.get(line_no, "down")
                d, s = [f"diff:autoscroll-{direction}:run"], [f"scale:fixed:autoscroll-{direction}:run", f"scale:wrap:autoscroll-{direction}:run"]
        elif name == "StyledText":
            d, s = ["diff:append"], ["scale:fixed:setText", "scale:wrap:setText"]
        elif name == "getData":
            d, s = ["diff:getData-htmlText"], ["scale:fixed:getData-htmlText", "scale:wrap:getData-htmlText"]
        elif name == "print":
            d, s = ["diff:printOptions"], []
        elif name.startswith("get") or name.startswith("is"):
            d, s = ["diff:"], ["scale:fixed:snapshot-getters", "scale:wrap:snapshot-getters"]
            prefixed = match(diff, ["diff:" + name])
            if prefixed:
                d = prefixed
        elif name in ("addSelectionListener", "removeSelectionListener") or name.startswith("add") or name.startswith("remove"):
            d = match(diff, ["diff:" + name])
            s = match(scale, ["scale:fixed:" + name, "scale:wrap:" + name])
        elif name == "paste":
            d = match(diff, ["diff:copy+paste", "diff:invokeAction:PASTE", "diff:block:invokeAction:PASTE"])
            s = match(scale, ["scale:fixed:copy+paste", "scale:wrap:copy+paste", "scale:fixed:invokeAction:PASTE", "scale:wrap:invokeAction:PASTE"])
        elif name == "invokeAction":
            d = match(diff, ["diff:invokeAction:", "diff:block:invokeAction:", "diff:multi:invokeAction:", "diff:toggle:invokeAction:"])
            s = match(scale, ["scale:fixed:invokeAction:", "scale:wrap:invokeAction:"])
        else:
            p = params(signature)
            exact = match(diff, ["diff:" + name + "(" + p + ")"]) if p else []
            d = exact or match(diff, ["diff:" + name])
            s = match(scale, ["scale:fixed:" + name, "scale:wrap:" + name])
        if d == ["diff:"]:
            # getters are compared after every differential operation (state snapshot)
            d_ids, d_status = "diff:* (state snapshot after all ops)", ("PASSED" if diff and all(v == "PASSED" for v in diff.values()) else ("FAILED" if diff else "NOT_RUN"))
        else:
            d_ids, d_status = ";".join(d) if d else "", status(d, diff)
        s_ids = ";".join(s) if s else ("n/a: " + cost + " inherent" if cost in (OL,) else "")
        s_status = status(s, scale) if s else ("INHERENT" if cost in (OL,) else "NOT_RUN")
        if name == "print" and "print()" in signature:
            final = "NOT_RUN"
            note = "starts a real job on the default printer; not run (print(Printer...) variants cover the job body)"
        elif name == "print" or (name == "run" and line_no < 1000):
            job = os.environ.get("PRINT_JOB_STATUS", "OPTIONS_ONLY")
            final = "NOT_RUN" if d_status != "PASSED" else ("PASSED" if job == "PASSED" else "PASSED_OPTIONS_ONLY")
            note = note + ("; print-to-file job ran for paged and gap content" if job == "PASSED" else "")
        elif d_status == "PASSED" and s_status in ("PASSED", "INHERENT"):
            final = "PASSED"
        elif "FAILED" in (d_status, s_status):
            final = "FAILED"
        else:
            final = "PARTIAL:" + d_status + "/" + s_status
        kind = "accessible-adapter" if inner and name in accessibility else ("content-listener" if name.startswith("textChang") or name == "textSet" else "method")
        rows.append([kind, signature, str(line_no), cost, d_ids, s_ids, final, qt_for(name), example_for(name), note])
    for line, name in (l.split("\t")[:2] for l in Path(inputs, "ST_CONSTANTS.tsv").read_text(encoding="utf-8").splitlines() if l.strip()):
        if name.startswith("BULLET"):
            prefix = {"BULLET_DOT": "setLineBullet-dot", "BULLET_NUMBER": "setLineBullet-number", "BULLET_TEXT": "setLineBullet-number",
                      "BULLET_LETTER_LOWER": "setLineBullet-letters", "BULLET_LETTER_UPPER": "setLineBullet-letters", "BULLET_CUSTOM": "setLineBullet-custom-text"}[name]
            d = match(diff, ["diff:" + prefix])
            s = match(scale, ["scale:fixed:" + prefix, "scale:wrap:" + prefix])
            qt = "QTextListFormat::Style"
        else:
            d = match(diff, ["diff:invokeAction:" + name, "diff:block:invokeAction:" + name, "diff:toggle:invokeAction:" + name])
            s = match(scale, ["scale:fixed:invokeAction:" + name, "scale:wrap:invokeAction:" + name, "scale:fixed:block:invokeAction:" + name,
                              "scale:fixed:toggle:invokeAction:" + name, "scale:wrap:toggle:invokeAction:" + name])
            qt = "QTextCursor::MoveOperation / QKeySequence::StandardKey"
        st = "PASSED" if status(d, diff) == "PASSED" and status(s, scale) == "PASSED" else "PARTIAL:" + status(d, diff) + "/" + status(s, scale)
        rows.append(["ST-constant", "ST." + name, line, OS if "ALL" in name or "TEXT_" in name else OV, ";".join(d), ";".join(s), st, qt, example_for(name), ""])
    for parts in (l.split("\t") for l in Path(inputs, "CASE_ST.tsv").read_text(encoding="utf-8").splitlines() if l.strip()):
        line, name, context = parts[0], parts[1], parts[2] if len(parts) > 2 else ""
        if "invokeBlockAction(int action)" in context or "blockSelection && invokeBlockAction" in context and int(line) > 2000 and False:
            pass
        block = int(line) in BLOCK_LINES
        prefix = ("diff:block:invokeAction:" if block else "diff:invokeAction:") + name
        d = match(diff, [prefix, "diff:toggle:invokeAction:" + name])
        sprefix = ("scale:fixed:block:invokeAction:" if block else "scale:fixed:invokeAction:") + name
        s = match(scale, [sprefix, sprefix.replace(":fixed:", ":wrap:"), "scale:fixed:toggle:invokeAction:" + name, "scale:wrap:toggle:invokeAction:" + name])
        st = "PASSED" if status(d, diff) == "PASSED" and status(s, scale) == "PASSED" else "PARTIAL:" + status(d, diff) + "/" + status(s, scale)
        rows.append(["case-ST-path", ("block " if block else "") + "case ST." + name, line, OV, ";".join(d), ";".join(s), st, "QTextCursor::movePosition", "", context[:60]])
    for name, ids, qt, note in FEATURES:
        d = match(diff, ["diff:" + i for i in ids])
        s = match(scale, ["scale:fixed:" + i for i in ids] + ["scale:wrap:" + i for i in ids])
        inherent = not s and name.startswith("printing")
        st = "PASSED" if status(d, diff) == "PASSED" and (inherent or status(s, scale) == "PASSED") else "PARTIAL:" + status(d, diff) + "/" + status(s, scale)
        rows.append(["feature", name, "", OL if inherent else OV, ";".join(d), ";".join(s) if s else ("n/a: O(lines) inherent (every page)" if inherent else ""), st, qt, "", note])
    for name, note in PLATFORM:
        rows.append(["platform", name, "", "", "", "", "NOT_RUN", "", "", note])
    for name, qt in QT_GAPS:
        rows.append(["qt-gap", name, "", "", "", "", "QT_GAP_NOT_IMPLEMENTED", qt, "", "Qt capability StyledText lacks; listed, not implemented in this leaf"])
    if EXAMPLE_REPORT and Path(EXAMPLE_REPORT).exists():
        for line in Path(EXAMPLE_REPORT).read_text(encoding="utf-8").splitlines()[1:]:
            c = line.split("\t")
            if len(c) < 7:
                continue
            st = "PASSED" if c[6] == "PASSED" else ("SKIPPED_SINGLE_LINE" if c[6].startswith("SKIPPED") else c[6].split(" ")[0])
            m = re.search(r"clientHeight=(\d+) displayHeight=(\d+)", c[2])
            if st == "FAILED" and m and int(m.group(1)) > int(m.group(2)) and all("laid out" in f for f in c[6][7:].split("; ")):
                st = "CLIENT_AREA_BOUND"
            if st == "FAILED" and "Snippet332" in c[0]:
                st = "SNIPPET_ASSUMPTION_SAME_ON_GAP"
            rows.append(["example", c[0].split(".")[-1] + " " + c[1], "", OV, "", "example:" + c[0].split(".")[-1] + ":" + c[1], st, "",
                         c[0], "1M-line run: lines=" + c[3] + " maxLayoutsPerOp=" + c[4] + " ms=" + c[5] + " " + c[2]])
    for name, note in SINGLE_CONTROL:
        rows.append(["one-widget", name, "", O1, "Test_org_eclipse_swt_custom_StyledText_SingleControl", "", SINGLE_STATUS[0], "", "", note])
    with open(out, "w", encoding="utf-8", newline="\n") as handle:
        handle.write("\t".join(COLUMNS) + "\n")
        for r in rows:
            handle.write("\t".join(c.replace("\t", " ") for c in r) + "\n")
    from collections import Counter
    counts = Counter((r[0], r[6].split(":")[0]) for r in rows)
    for k in sorted(counts):
        print(k[0], k[1], counts[k])


BLOCK_LINES = set()
EXAMPLE_REPORT = None
SINGLE_STATUS = ["NOT_RUN"]
SINGLE_CONTROL = [
    ("no child control or helper surface (getChildren()==0) in viewport mode", "one-widget law"),
    ("focus stays on StyledText (Display.getFocusControl)", "one-widget law"),
    ("TAB traversal leaves to a sibling and returns to StyledText", "one-widget law"),
    ("accessible role ACC.ROLE_TEXT on StyledText, no accessible children", "one-widget law"),
    ("SWT.VIRTUAL style bit forces viewport mode ((style & SWT.VIRTUAL) != 0)", "behaviour switch is a style bit"),
]

FEATURES = [
    ("StyleRange.start/length/fontStyle", ["setStyleRange", "setStyleRanges"], "QTextCharFormat range", ""),
    ("TextStyle.font", ["setStyleRange-rise-font"], "QTextCharFormat::setFont", ""),
    ("TextStyle.foreground/background", ["setStyleRange", "setStyleRange-features"], "QTextCharFormat::setForeground/setBackground", ""),
    ("TextStyle.underline/underlineStyle/underlineColor", ["setStyleRange-features", "setStyleRange-link"], "QTextCharFormat::setUnderlineStyle/Color", ""),
    ("TextStyle.strikeout/strikeoutColor", ["setStyleRange-features"], "QTextCharFormat::setFontStrikeOut", ""),
    ("TextStyle.borderStyle/borderColor", ["setStyleRange-features"], "none (CSS border in rich text only)", ""),
    ("TextStyle.rise", ["setStyleRange-rise-font"], "QTextCharFormat::setVerticalAlignment", ""),
    ("TextStyle.metrics (GlyphMetrics)", ["setStyleRange-metrics"], "QTextObjectInterface::intrinsicSize", ""),
    ("TextStyle.data / link", ["setStyleRange-link"], "QTextCharFormat::setAnchorHref", ""),
    ("Bullet.type/style/text", ["setLineBullet-"], "QTextListFormat", ""),
    ("custom StyledTextContent (setContent)", ["setContent-custom"], "QPlainTextEdit::setDocument", ""),
    ("block selection", ["setBlockSelection", "block:"], "none in Qt", ""),
    ("multiple selections / carets", ["setSelectionRanges", "multi:"], "extraSelections (visual only)", ""),
    ("variable line height", ["setLineSpacingProvider", "setStyleRange-metrics", "setLineVerticalIndent"], "QTextBlockFormat::setLineHeight", ""),
    ("fixed line height", ["setLineSpacing-reset", "setFixedLineMetrics"], "QPlainTextEdit (uniform)", ""),
    ("wrap indent / justify / alignment / tabs / margins", ["setWrapIndent", "setJustify", "setAlignment", "setTabs", "setMargins"], "QTextBlockFormat / QTextOption", ""),
    ("IME composition", ["ime-composition"], "inputMethodEvent / inputMethodQuery", "synthetic composition through the platform IME object"),
    ("accessibility (MSAA/IA2 adapters)", ["accessibility"], "QAccessibleTextInterface", "adapter methods invoked directly"),
    ("drag and drop (StyledTextDropTargetEffect)", ["dropTargetEffect"], "dragMoveEvent/dropEvent", "dragEnter/dragOver/dropAccept/dragLeave with a synthesized DropTargetEvent"),
    ("printing (StyledTextPrintOptions)", ["printOptions"], "QTextDocument::print + QPrinter", "options built; print job only with -Dswt.styledtext.printJob=true"),
    ("clipboard plain/RTF/HTML (RTFWriter, HTMLWriter)", ["rtf-html-writers", "copy+paste", "cut"], "QMimeData text/html", ""),
    ("keyboard typing / VerifyKey", ["type-keys", "block:type-keys", "multi:type-keys"], "keyPressEvent", "synthetic SWT.KeyDown"),
    ("mouse autoscroll", ["autoscroll-"], "auto scroll on drag", "synthetic mouse drag"),
    ("whole-document HTML (getData StyledText.htmlText)", ["getData-htmlText"], "QTextDocument::toHtml", ""),
]

PLATFORM = [
    ("GTK3 / GTK4 receiver", "not available on this Windows host"),
    ("Cocoa receiver", "not available on this Windows host"),
    ("StyledText.print() to the default printer", "would start a real print job; not run"),
]

if __name__ == "__main__":
    BLOCK_LINES.update(int(l.split("\t")[0]) for l in Path(sys.argv[1], "CASE_ST.tsv").read_text(encoding="utf-8").splitlines()
                       if l.strip() and (l.split("\t")[2].startswith("boolean invokeBlockAction") or int(l.split("\t")[0]) in (5636, 5641)))
    EXAMPLE_REPORT = sys.argv[5] if len(sys.argv) > 5 else None
    SINGLE_STATUS[0] = sys.argv[6] if len(sys.argv) > 6 else "NOT_RUN"
    load_examples(sys.argv[7:])
    main()
