# SPDX-License-Identifier: Apache-2.0
"""Existing SWT/OpenCV oracle shared by CI and the combined SWT/Nebula runner."""
# Copyright 2026 Hitesh Soliwal and contributors
import argparse
import hashlib
import json
from pathlib import Path
import xml.etree.ElementTree as ET



def require(condition, message):
    if not condition:
        raise AssertionError(message)


def expected_frame(scale, background):
    frame = np.full((80 * scale, 120 * scale, 3), background, dtype=np.uint8)
    frame[10 * scale:50 * scale, 40 * scale:80 * scale] = (0, 255, 0)
    frame[58 * scale:70 * scale, 90 * scale:110 * scale] = (0, 255, 255)
    return frame


def check_canary(first, second):
    require(first is not None and second is not None, 'Cannot decode canary PNGs')
    require(first.shape == second.shape, 'Canary dimensions changed')
    height, width, channels = first.shape
    require(channels == 3 and width % 120 == 0 and height % 80 == 0,
            'Canary cropped or invalid dimensions')
    scale = width // 120
    require(scale > 0 and height // 80 == scale, 'Nonuniform canary scale')
    for label, actual, background in (('first', first, (0, 0, 255)),
                                       ('second', second, (255, 0, 0))):
        expected = expected_frame(scale, background)
        delta = cv2.absdiff(actual, expected)
        require(int(delta.max()) == 0, label + ': stale, blank, shifted or misclipped frame')
        green = cv2.inRange(actual, (0, 255, 0), (0, 255, 0))
        count, _, stats, _ = cv2.connectedComponentsWithStats(green, connectivity=8)
        require(count == 2, label + ': overlap must be one connected component')
        require(stats[1].tolist() == [40 * scale, 10 * scale, 40 * scale,
                                      40 * scale, 1600 * scale * scale],
                label + ': overlap bounds or area changed')
    changed = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(first, second), cv2.COLOR_BGR2GRAY))
    require(changed == (120 * 80 - 40 * 40 - 20 * 12) * scale * scale,
            'Repaint changed the wrong area')
    return {'scale': scale, 'repaintPixels': changed, 'exactGeometry': True}


def negative_controls():
    first = expected_frame(1, (0, 0, 255))
    second = expected_frame(1, (255, 0, 0))
    check_canary(first, second)
    # Exercise the persisted-image oracle with defects independent of SWT assertions.
    missing_overlap = second.copy()
    missing_overlap[10:50, 40:80] = (255, 0, 0)
    leaking_clip = second.copy()
    leaking_clip[50:80, 80:120] = (0, 255, 255)
    defects = {'blank': np.zeros_like(second), 'stale': first,
               'shifted': np.roll(second, 3, axis=1), 'cropped': second[:, :-1],
               'missing-overlap': missing_overlap, 'clip-leak': leaking_clip}
    for label, broken in defects.items():
        try:
            check_canary(first, broken)
        except AssertionError:
            continue
        raise AssertionError('Validator admitted negative control: ' + label)
    return sorted(defects)



def check_java2d(output):
    """Reuse the viewport lane's decoded-pixel and component operators for Java2D."""
    facts = json.loads((output / 'capture-metadata.json').read_text())
    require(facts['captureApi'] == 'GC(Display).copyArea', 'Java2D capture API')
    require(facts['backend'] == 'gtk', 'Java2D unqualified backend')
    require(facts['scrollbarMode'] == 'SWT.NONE', 'Java2D controlled scrollbar geometry')
    width, height = facts['pixelSize']
    require(isinstance(width, int) and isinstance(height, int)
            and 0 < width <= 256 and 0 < height <= 192, 'Java2D viewport bounds')
    require(len(facts['screenBounds']) == 4
            and [width, height] == facts['screenBounds'][2:], 'Java2D pixel geometry is not 1:1')
    require(facts['dpi'][0] > 0 and facts['dpi'][1] > 0, 'Java2D DPI metadata')
    require(facts['origin'] == [29, 37], 'Java2D pan origin')
    require(facts['coverBounds'] == [31, 27, 73, 51], 'Java2D overlap bounds')
    require(facts['rendererCalls'] > 0, 'Java2D no renderer calls')
    names = ('before', 'panned', 'covered', 'uncovered', 'fresh')
    seals = {capture['name']: capture['sha256'] for capture in facts['captures']}
    require(len(facts['captures']) == len(names) and set(seals) == set(names),
            'Java2D missing, duplicate or unexpected capture')
    images = {}
    for name in names:
        path = output / (name + '.png')
        require(hashlib.sha256(path.read_bytes()).hexdigest() == seals[name], name + ': Java2D hash')
        images[name] = cv2.imread(str(path), cv2.IMREAD_COLOR)

    def tiles(origin):
        rows, columns = np.indices((height, width), dtype=np.int64)
        columns = (columns + origin[0]) // 16
        rows = (rows + origin[1]) // 16
        red = 40 + (columns * 37 + rows * 17) % 176
        green = 35 + (columns * 13 + rows * 41) % 180
        blue = 45 + (columns * 29 + rows * 23) % 170
        return np.stack((blue, green, red), axis=2).astype(np.uint8)

    initial, panned = tiles((0, 0)), tiles((29, 37))
    covered = panned.copy()
    covered[27:78, 31:104] = (0, 255, 0)
    expected = {'before': initial, 'panned': panned, 'covered': covered,
                'uncovered': panned, 'fresh': panned}

    def validate(frames):
        for name in names:
            actual = frames[name]
            require(actual is not None and actual.shape == (height, width, 3), name + ': Java2D cropped frame')
            require(int(cv2.absdiff(actual, expected[name]).max()) == 0,
                    name + ': Java2D stale, blank, shifted, overlap or clip mismatch')
            require(cv2.countNonZero(cv2.Canny(actual, 40, 120)) >= 20, name + ': Java2D blank frame')
        green = cv2.inRange(frames['covered'], (0, 255, 0), (0, 255, 0))
        count, _, stats, _ = cv2.connectedComponentsWithStats(green, connectivity=8)
        require(count == 2 and stats[1].tolist() == [31, 27, 73, 51, 3723],
                'Java2D overlap component geometry')
        require(cv2.countNonZero(cv2.cvtColor(cv2.absdiff(frames['before'], frames['panned']), cv2.COLOR_BGR2GRAY)) > 20,
                'Java2D pan did not repaint')
        return True

    validate(images)
    leaking = images['covered'].copy()
    leaking[78:84, 104:116] = (0, 255, 0)
    defects = {'blank': ('fresh', np.zeros_like(images['fresh'])),
               'stale': ('fresh', images['before']),
               'shifted': ('panned', np.roll(images['panned'], 3, axis=1)),
               'cropped': ('panned', images['panned'][:, :-1]),
               'missing-overlap': ('covered', images['panned']),
               'clip-leak': ('covered', leaking)}
    controls = output / 'negative-controls'
    controls.mkdir(exist_ok=True)
    rejected = []
    for label, (name, broken) in defects.items():
        path = controls / (label + '.png')
        require(cv2.imwrite(str(path), broken), label + ': failed control write')
        candidate = dict(images)
        candidate[name] = cv2.imread(str(path), cv2.IMREAD_COLOR)
        try:
            validate(candidate)
        except AssertionError:
            rejected.append(label)
        else:
            raise AssertionError('Java2D validator admitted negative control: ' + label)
    receipt = {'status': 'PASS', 'opencv': cv2.__version__, 'numpy': np.__version__,
               'captureApi': facts['captureApi'], 'screenBounds': facts['screenBounds'],
               'dpi': facts['dpi'], 'pixelSize': [width, height], 'captures': seals,
               'exactTilePixels': width * height * 5, 'overlapPixels': 3723,
               'negativeControlsRejected': sorted(rejected),
               'scope': 'GTK3/X11 1:1 screen pixels, pan/cover/uncover/fresh parity'}
    return receipt


def qualified_suite(report, class_name, expected_names):
    suite = ET.parse(report).getroot()
    counts = {key: int(suite.get(key, '-1')) for key in ('tests', 'failures', 'errors', 'skipped')}
    require(counts == {'tests': len(expected_names), 'failures': 0, 'errors': 0, 'skipped': 0},
            'JUnit counters: ' + str(counts))
    cases = list(suite.iter('testcase'))
    require(len(cases) == len(expected_names), 'JUnit testcase count disagrees with counters')
    seen = set()
    for case in cases:
        name = case.get('name', '').removesuffix('()')
        require(case.get('classname') == class_name, 'Unexpected JUnit class')
        require(name in expected_names and name not in seen, 'Missing, duplicate or unexpected JUnit method: ' + name)
        require(not any(case.find(tag) is not None for tag in ('failure', 'error', 'skipped')),
                'Unqualified JUnit method: ' + name)
        seen.add(name)
    require(not any(list(suite.iter(tag)) for tag in ('failure', 'error', 'skipped')),
            'JUnit report contains failed or skipped execution')
    return counts


def verify_resources(test_root):
    output = test_root / 'target/viewport-resources'
    receipt_path = output / 'resource-validation.json'
    receipt_path.unlink(missing_ok=True)
    names = set(['additionalCpuWorkIsDetectedIndependentlyOfElapsedTime', 'coldMillionRowsStayWithinHeapNativeAndCpuBudgets', 'collapseExpansionDoesNotAccumulateNativeResidency', 'legacyDistantPrefixIsDetectedByNativeViewBudget', 'logicalColdMillionRowsStayWithinBudgets', 'logicalCollapseExpansionStaysWithinBudgets', 'logicalDistantAccessDoesNotAllocateNativePrefix', 'logicalDistantPaintedScrollingStaysWithinBudgets', 'repeatedPaintedScrollingStaysWithinMemoryAndCpuBudgets', 'touchedNativeAllocationIsDetectedIndependentlyOfJavaHeap'])
    names.update(['java2DColdVisibleRasterStaysWithinHeapNativeAndCpuBudgets', 'java2DPaintedDistantScrollingStaysWithinMemoryAndCpuBudgets', 'java2DPaintedResizeAndZoomStayWithinMemoryAndCpuBudgets'])
    counts = qualified_suite(test_root / 'target/surefire-reports/TEST-org.eclipse.swt.tests.junit.ViewportResourceRegressionTest.xml',
                             'org.eclipse.swt.tests.junit.ViewportResourceRegressionTest', names)
    scenarios = ('cold', 'scroll', 'collapse', 'logical-cold', 'logical-far', 'logical-scroll', 'logical-collapse',
                 'java2d-cold', 'java2d-scroll', 'java2d-resize-zoom')
    expected = [(scenario, trial) for scenario in scenarios for trial in range(3)]
    expected += [(scenario, 0) for scenario in ('native-control', 'cpu-control', 'native-prefix-control')]
    hashes = {}
    for scenario, trial in expected:
        path = output / f'{scenario}-{trial}.properties'
        data = path.read_bytes()
        facts = dict(line.split('=', 1) for line in data.decode().splitlines() if '=' in line and not line.startswith('#'))
        is_java2d = scenario.startswith('java2d-')
        schema = 'swt-viewport-resources/3' if is_java2d else 'swt-viewport-resources/2'
        require(facts.get('schema') == schema and facts.get('scenario') == scenario,
                'Resource receipt identity: ' + path.name)
        require(facts.get('platform') == 'gtk', 'Resource platform: ' + path.name)
        for key in ('heapDeltaBytes', 'rssDeltaBytes', 'cpuNanos', 'maxStepCpuNanos'):
            require(int(facts[key]) >= 0, 'Invalid resource observation: ' + key)
        if is_java2d:
            require(facts.get('model') == 'java2d-visible-raster', 'Java2D resource model')
            require(int(facts['logicalWidth']) == int(facts['logicalHeight']) == 220_000_000,
                    'Java2D logical extent')
            steps = 1 if scenario == 'java2d-cold' else 96
            require(int(facts['steps']) == steps, 'Java2D measured steps')
            for key in ('paintEvents', 'rendererCalls', 'rasterBoundChecks', 'pinnedChildChecks'):
                require(int(facts[key]) >= steps, 'Java2D incomplete measured work: ' + key)
            require(0 < int(facts['maxRasterPixels']) <= int(facts['maxClientPixels']) <= 480 * 320,
                    'Java2D unbounded raster')
            require(int(facts['heapDeltaBytes']) <= 8 * 1024 * 1024
                    and int(facts['rssDeltaBytes']) <= 24 * 1024 * 1024, 'Java2D memory budget')
        else:
            require(int(facts['peakNativeViewRows']) >= 0, 'Invalid native-view observation')
        hashes[path.name] = hashlib.sha256(data).hexdigest()
    receipt = {'status': 'PASS', 'junit': counts, 'resourceArtifacts': hashes,
               'budgetAuthority': 'ViewportResourceRegressionTest (unchanged)', 'manualQaRequired': False}
    receipt_path.write_text(json.dumps(receipt, indent=2) + '\n')
    print(json.dumps(receipt, sort_keys=True))
    return receipt


def main(test_root=Path("."), *, require_java2d=False):
    output = test_root / "target/screenshots/viewport"
    java2d_output = test_root / "target/screenshots/java2d"
    (output / "opencv-validation.json").unlink(missing_ok=True)
    if require_java2d:
        (java2d_output / "opencv-validation.json").unlink(missing_ok=True)
    global cv2, np
    import cv2
    import numpy as np
    java2d_counts, java2d = None, None
    if require_java2d:
        java2d_counts = qualified_suite(
            test_root / 'target/surefire-reports/TEST-org.eclipse.swt.tests.junit.Test_org_eclipse_swt_awt_Java2DCanvas.xml',
            'org.eclipse.swt.tests.junit.Test_org_eclipse_swt_awt_Java2DCanvas', {
                'test_logicalExtentKeepsRasterVisibleSizedAndComposesOrigin',
                'test_logicalExtentRejectsNegativeArgumentsWithoutChangingPriorState',
                'test_partialInvalidationReusesPixelsAndExposureDoesNotInvokeRenderer',
                'test_panPartialThenFullExposureMatchesFreshNativeTransfer',
                'test_transformsAreDefensiveFiniteAndCanBeSingular',
                'test_alphaPresentationClearsPreviousNativePixels',
                'test_rendererFailureDoesNotPresentPartialPixelsAndCanRecover',
                'test_presentationRestoresCallerTransformStateAndExactClip',
                'test_wrongThreadDisposedResizeAndReentrantInvalidationContracts',
                'test_ownedNativeResourcesReturnToBaselineAndObserverDetectsRetention',
                'test_screenPanCoverUncoverEqualsFreshFrame',
            })
        java2d = check_java2d(java2d_output)
    rejected = negative_controls()
    report = test_root / 'target/surefire-reports/TEST-org.eclipse.swt.tests.junit.ViewportScreenshotRegressionTest.xml'
    counts = qualified_suite(report, 'org.eclipse.swt.tests.junit.ViewportScreenshotRegressionTest', {
        'test_viewportScreenshotRegression', 'test_logicalScrolledContentSnapshotsAtTenMillionRows', 'test_swtScreenCaptureTracksVisiblePixels',
        'test_screenCaptureRejectsInvalidTargets', 'test_capturePathsReleaseGraphicsOnSuccessAndFailure'})
    lifetime = dict(line.split('=', 1) for line in (test_root / 'target/capture-qualification/graphics-lifetime.properties').read_text().splitlines())
    for key, expected in {'capturePaths': '3', 'successfulCaptures': '48', 'writeFailures': '3',
                          'positiveLeakControls': '2', 'retainedGraphicsDelta': '0'}.items():
        require(lifetime[key] == expected, 'Graphics resource qualification: ' + key)
    scenes = ('table-checked-selection', 'tree-pinned-expanded', 'tree-pinned-collapsed',
              'tree-pinned-restored', 'viewport-vertical-scroll', 'viewport-horizontal-scroll',
              'viewport-narrow', 'viewport-wide', 'viewport-affine-clip-stroke',
              'logical-viewport-top', 'logical-viewport-deep',
              'logical-viewport-horizontal', 'logical-viewport-narrow')
    metrics, images, scene_facts = {}, {}, {}
    for name in scenes:
        facts = dict(line.split('=', 1) for line in (output / (name + '.txt')).read_text().splitlines() if '=' in line)
        scene_facts[name] = facts
        require(facts['capture.api'] == 'SWT.GC(Display).copyArea', name + ': capture API')
        require(facts['capture.coordinateSpace'] == 'display-logical', name + ': coordinates')
        require(facts['capture.backend'] == 'x11', name + ': unqualified backend')
        for suffix, key in (('-screen.png', 'capture.sha256'), ('.png', 'screenshot.sha256')):
            path = output / (name + suffix)
            data = path.read_bytes()
            require(data.startswith(bytes.fromhex('89504e470d0a1a0a')), str(path))
            require(hashlib.sha256(data).hexdigest() == facts[key], str(path) + ': hash mismatch')
            pixels = cv2.imread(str(path), cv2.IMREAD_COLOR)
            require(pixels is not None and pixels.size > 0, str(path) + ': decode failed')
            edges = cv2.countNonZero(cv2.Canny(pixels, 40, 120))
            require(edges >= 20, str(path) + ': blank or missing scene geometry')
            metrics[name + suffix] = {'width': pixels.shape[1], 'height': pixels.shape[0], 'edgePixels': edges}
            if suffix == '-screen.png':
                require(facts['capture.pixels'] == str(pixels.shape[1]) + 'x' + str(pixels.shape[0]), name + ': dimensions')
                images[name] = pixels
    before, after = (cv2.imread(str(output / ('capture-canary-' + suffix + '.png')))
                     for suffix in ('first', 'second'))
    canary = check_canary(before, after)
    vertical, horizontal = images['viewport-vertical-scroll'], images['viewport-horizontal-scroll']
    require(vertical.shape == horizontal.shape, 'Scroll frame dimensions changed')
    scroll_delta = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(vertical, horizontal), cv2.COLOR_BGR2GRAY))
    require(scroll_delta > 20, 'Horizontal scrolling produced a stale frame')
    narrow, wide = images['viewport-narrow'], images['viewport-wide']
    require(narrow.shape[0] < wide.shape[0] and narrow.shape[1] < wide.shape[1], 'Resize capture is stale')
    logical_scenes = ('logical-viewport-top', 'logical-viewport-deep',
                      'logical-viewport-horizontal', 'logical-viewport-narrow')
    for name in logical_scenes:
        facts = scene_facts[name]
        require(facts['logical.rowCount'] == '10000000', name + ': logical row count')
        require('220000000' in facts['logical.extent'], name + ': logical extent')
        require(int(facts['logical.maximumRowsInOnePaint']) <= 80,
                name + ': unbounded viewport painting')
        require(int(facts['logical.bodyPaints']) > 0, name + ': body was not painted')
    require(scene_facts['logical-viewport-top']['logical.headerOriginX'] == '0',
            'Initial header projection')
    require(scene_facts['logical-viewport-deep']['logical.headerOriginX'] == '0',
            'Vertical scroll moved header projection')
    require(scene_facts['logical-viewport-horizontal']['logical.headerOriginX'] == '620',
            'Horizontal scroll did not move header projection')
    require(int(scene_facts['logical-viewport-deep']['logical.firstPaintedRow']) > 4_500_000,
            'Deep viewport painted the wrong logical rows')
    top = images['logical-viewport-top']
    deep = images['logical-viewport-deep']
    shifted = images['logical-viewport-horizontal']
    require(top.shape == deep.shape == shifted.shape, 'Logical viewport geometry drift')
    logical_delta = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(top, deep), cv2.COLOR_BGR2GRAY))
    horizontal_delta = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(deep, shifted), cv2.COLOR_BGR2GRAY))
    require(logical_delta > 20, 'Distant logical viewport screenshot is stale')
    require(horizontal_delta > 20, 'Logical horizontal viewport screenshot is stale')
    narrow_logical = images['logical-viewport-narrow']
    require(narrow_logical.shape[0] < deep.shape[0] and narrow_logical.shape[1] < deep.shape[1],
            'Logical viewport resize capture is stale')
    receipt = {'status': 'PASS', 'opencv': cv2.__version__, 'numpy': np.__version__,
               'junit': counts, 'sceneArtifacts': metrics, 'canary': canary,
               'scrollChangedPixels': scroll_delta, 'logicalScrollChangedPixels': logical_delta,
               'logicalHorizontalChangedPixels': horizontal_delta, 'negativeControlsRejected': rejected,
               'graphicsLifetime': lifetime, 'java2dRequired': require_java2d,
               'java2d': java2d, 'java2dJUnit': java2d_counts,
               'manualQaRequired': False, 'scope': 'GTK3/X11 controlled geometry and scene sanity; no cross-platform golden approval'}
    if require_java2d:
        (java2d_output / 'opencv-validation.json').write_text(json.dumps(java2d, indent=2) + '\n')
    (output / 'opencv-validation.json').write_text(json.dumps(receipt, indent=2) + '\n')
    print(json.dumps(receipt, sort_keys=True))


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--test-root', type=Path, default=Path('.'))
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument('--resources', action='store_true')
    mode.add_argument('--java2d', action='store_true', help='Require the Java2D JUnit suite and screen captures too')
    options = parser.parse_args()
    if options.resources:
        verify_resources(options.test_root)
    else:
        main(options.test_root, require_java2d=options.java2d)

