# StyledText JUnit runs on this Win32 host (JDK 21.0.9, SWT 3.135 natives), 2026-10-13
## final run (chain2)
tests-before: [ 293 tests found ] [ 0 tests skipped ] [ 1 tests aborted ] [ 291 tests successful ] [ 1 tests failed ]  failing: methodName = 'test_backspaceAndDelete' 
tests-after-paged: [ 298 tests found ] [ 0 tests skipped ] [ 1 tests aborted ] [ 296 tests successful ] [ 1 tests failed ]  failing: methodName = 'test_backspaceAndDelete' 
tests-after-gap: [ 293 tests found ] [ 0 tests skipped ] [ 1 tests aborted ] [ 291 tests successful ] [ 1 tests failed ]  failing: methodName = 'test_backspaceAndDelete' 
## test_backspaceAndDelete reruns (Display.post OS input, focus dependent)
base 1 [ 270 tests successful ] [ 1 tests failed ] 
base 2 [ 271 tests successful ] [ 0 tests failed ] 
base 3 [ 270 tests successful ] [ 1 tests failed ] 
after 1 [ 271 tests successful ] [ 0 tests failed ] 
after 2 [ 271 tests successful ] [ 0 tests failed ] 
after 3 [ 270 tests successful ] [ 1 tests failed ] 
## discrimination of the idle-pass regression test
prefix compile exit=0
[ 4 tests successful ] [ 1 tests failed ] 
## opt-in 10M-line proof
[ 2 tests found ] [ 2 tests successful ] [ 0 tests failed ] 
## recipe
"checks":["exact-postimages:6","fixed-point","drift-refused","missing-source-refused"] rewrite=8.63.4 owner blobs = develop 5a1ad952 (M3HashPinnedJavaSnapshotRecipe 18c43e7a, M3EditScope 27436081, M3ScopeInference 2768fe58)
