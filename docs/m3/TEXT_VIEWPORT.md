# Text (multi-line) viewport: measure only (leaf L10)

Status: measured on win32 (invisible desktop); no code change. The native EDIT control keeps its own lines in every
mode that keeps native pixels.

Plan row: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 7, rank 10 ("in `viewport` mode the
EDIT control stays native unless the oracle shows a document-proportional cost").

## Oracle

`Test_org_eclipse_swt_widgets_Text_ScaleOracle` (opt-in, `-Dswt.text.oracle=true`) builds one `SWT.MULTI | V_SCROLL |
H_SCROLL` Text of 600 x 400 with `setTextLimit (Text.LIMIT)` and measures, at 10^4, 10^5 and 10^6 lines of the form
`line <n>`: `setText` (three times), `getText`, `getLineCount`, `setTopIndex` to the middle, `getCaretLineNumber`,
`setSelection` at the end, a full paint, `append` of one line and `insert` of a few characters (seven times each).
Medians in microseconds, run of 2026-10-17 (jar `aw21`; three desktops shared with other agents, so the absolute
numbers carry load noise):

| Jar / mode | lines | setText | getText | getLineCount | setTopIndex | caret line | select end | paint | append | insert |
|---|---|---|---|---|---|---|---|---|---|---|
| official 3.135.0 | 10^4 | 10 572 | 427 | 5 | 1 728 | 10 | 2 021 | 1 335 | 6 872 | 1 874 |
| official 3.135.0 | 10^5 | 32 598 | 4 300 | 3 | 2 801 | 13 | 5 430 | 1 471 | 3 949 | 1 738 |
| official 3.135.0 | 10^6 | 181 993 | 36 696 | 5 | 2 114 | 9 | 2 068 | 1 476 | 3 246 | 2 170 |
| this SWT, original | 10^6 | 101 987 | 25 270 | 1 | 1 514 | 6 | 1 068 | 855 | 1 782 | 918 |
| this SWT, viewport | 10^6 | 137 108 | 33 089 | 4 | 2 054 | 9 | 2 859 | 1 386 | 3 336 | 1 880 |

## Reading

- Only `setText` and `getText` grow with the document, and both must: they copy every character once
  (`Display.withCrLf`, `SetWindowText`; `GetWindowText`). 10^6 lines (8.9 million characters) take 0.1-0.18 s to set
  and 25-37 ms to read.
- Everything else is flat from 10^4 to 10^6 lines: line count, scroll, caret line, selection, paint, append and
  insert. The EDIT control already keeps its own line index and paints only the visible lines; SWT adds no work that
  grows with the document on these paths (`Text.append` sends `EM_SETSEL`, `EM_REPLACESEL` and `EM_SCROLLCARET` for
  the appended text only).
- So the viewport mode of Text is the native EDIT control, unchanged, as the plan expected. The drawn modes
  (`custom`, `m3`, `gpu`) are owned by the render-modes work.

## Non-claims

- Segments listeners (`SWT.Segments`) and Verify listeners were not measured; both process the changed text only.
- Single-line Text, wrapping (`SWT.WRAP`) and very long lines (one line of 10^6 characters) were not measured.
- GTK and Cocoa are not measured here.
