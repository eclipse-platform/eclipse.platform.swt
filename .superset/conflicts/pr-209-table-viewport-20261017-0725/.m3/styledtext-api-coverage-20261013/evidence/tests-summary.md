# StyledText API coverage leaf: test summary (Win32 host, JDK 21.0.9, SWT 3.135 natives), 2026-10-13

- Test_org_eclipse_swt_custom_StyledText_ApiDifferential (paged vs gap, print-to-file job on): [ 221 tests found ] [ 221 tests successful ] [ 0 tests failed ] 
- Test_org_eclipse_swt_custom_StyledText_ApiScale (1,000,001 lines, fixed + wrap, layout bound 4096): [ 442 tests found ] [ 442 tests successful ] [ 0 tests failed ] 
  - max layouts per op: wrap autoscroll-left:run 630; slowest op: fixed getData-htmlText 5276 ms
- Existing StyledText classes + PagedContent + SingleControl, paged default: [ 300 tests found ] [ 1 tests aborted ] [ 298 tests successful ] [ 1 tests failed ]  methodName = 'test_backspaceAndDelete' 
- same, content=gap: [ 300 tests found ] [ 1 tests aborted ] [ 299 tests successful ] [ 0 tests failed ]  
- print-to-file job (Microsoft Print to PDF, page 1): PagedContent and DefaultContent PDFs both 86,276 bytes (PDF IDs/timestamps differ)
- examples exerciser (29 StyledText snippets, CustomControlExample StyledText tab with each style toggle, TextEditor; 1,000,000 lines appended; 13 ops each): 54 widget runs = 49 PASSED, 3 CLIENT_AREA_BOUND (CustomControlExample sizes the widget to its preferred height: 65,514-65,535 px client area, 4,286-8,932 layouts per op over that client area), 2 SNIPPET_ASSUMPTION_SAME_ON_GAP (Snippet332 BidiSegmentListener returns invalid segments for lines without "Eclipse"; identical IllegalArgumentException with content=gap)
