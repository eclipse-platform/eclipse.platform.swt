# SPDX-License-Identifier: Apache-2.0
"""Existing SWT/OpenCV oracle shared by CI and the combined SWT/Nebula runner."""
# Copyright 2026 Hitesh Soliwal and contributors
# Modified in 2026 to validate explicit bounded Table scenes, resource probes and native observer control.
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
    names = {
        'additionalCpuWorkIsDetectedIndependentlyOfElapsedTime',
        'coldMillionRowsStayWithinHeapNativeAndCpuBudgets',
        'collapseExpansionDoesNotAccumulateNativeResidency',
        'legacyDistantPrefixIsDetectedByNativeViewBudget',
        'logicalColdMillionRowsStayWithinBudgets',
        'logicalCollapseExpansionStaysWithinBudgets',
        'logicalDistantAccessDoesNotAllocateNativePrefix',
        'logicalDistantPaintedScrollingStaysWithinBudgets',
        'repeatedPaintedScrollingStaysWithinMemoryAndCpuBudgets',
        'touchedNativeAllocationIsDetectedIndependentlyOfJavaHeap',
        'boundedTableColdMillionRowsStayWithinBudgets',
        'boundedTableDistantAccessDoesNotAllocateNativePrefix',
        'boundedTableDistantPaintedScrollingStaysWithinBudgets',
        'defaultTableFullModelIsDetectedByNativeViewBudget'}
    counts = qualified_suite(test_root / 'target/surefire-reports/TEST-org.eclipse.swt.tests.junit.ViewportResourceRegressionTest.xml',
                             'org.eclipse.swt.tests.junit.ViewportResourceRegressionTest', names)
    scenarios = ('cold', 'scroll', 'collapse', 'logical-cold', 'logical-far', 'logical-scroll', 'logical-collapse',
                 'table-cold', 'table-far', 'table-scroll')
    expected = [(scenario, trial) for scenario in scenarios for trial in range(3)]
    expected += [(scenario, 0) for scenario in
                 ('native-control', 'cpu-control', 'native-prefix-control', 'table-native-view-control')]
    hashes = {}
    for scenario, trial in expected:
        path = output / f'{scenario}-{trial}.properties'
        data = path.read_bytes()
        facts = dict(line.split('=', 1) for line in data.decode().splitlines() if '=' in line and not line.startswith('#'))
        require(facts.get('schema') == 'swt-viewport-resources/2' and facts.get('scenario') == scenario,
                'Resource receipt identity: ' + path.name)
        require(facts.get('platform') == 'gtk', 'Resource platform: ' + path.name)
        table = scenario.startswith('table-')
        require(facts.get('widget') == ('Table' if table else 'Tree'), 'Resource widget: ' + path.name)
        model = ('explicit-logical-opt-in' if scenario.startswith('logical-') else
                 'explicit-bounded-opt-in' if table and scenario != 'table-native-view-control' else 'default')
        require(facts.get('model') == model, 'Resource model: ' + path.name)
        for key in ('heapDeltaBytes', 'rssDeltaBytes', 'cpuNanos', 'maxStepCpuNanos', 'peakNativeViewRows'):
            require(int(facts[key]) >= 0, 'Invalid resource observation: ' + key)
        if table:
            require(int(facts['logicalRows']) == 1_000_000, 'Table logical row count: ' + path.name)
            require(0 <= int(facts['residentRows']) <= 512, 'Table facade budget: ' + path.name)
            if scenario == 'table-native-view-control':
                require(int(facts['peakNativeViewRows']) == 1_000_000 and
                        int(facts['nativeViewRows']) == 1_000_000,
                        'Table native observer failed to detect the default full model')
                require(int(facts['windowSamples']) == 0, 'Default Table control was mislabeled as bounded')
            else:
                require(int(facts['windowSamples']) > 0, 'Table viewport was not sampled: ' + path.name)
                require(0 < int(facts['peakNativeViewRows']) <= 512, 'Table native row budget: ' + path.name)
                require(0 <= int(facts['peakResidentRows']) <= 512, 'Table peak facade budget: ' + path.name)
                require(0 <= int(facts['setDataCalls']) <= 512, 'Table callback budget: ' + path.name)
                top, visible, overscan = (int(facts[key]) for key in ('topIndex', 'visibleRows', 'overscanRows'))
                require(0 <= top < 1_000_000 and visible >= 0 and overscan > 0, 'Table window geometry: ' + path.name)
                start = max(0, top - overscan)
                count = min(1_000_000, top + visible + overscan) - start
                require(int(facts['paintStart']) == start and
                        int(facts['paintRowCount']) == count and int(facts['nativeViewRows']) == count,
                        'Table native window differs from visible plus overscan rows: ' + path.name)
                if scenario == 'table-scroll':
                    require(int(facts['steps']) == 96 and int(facts['paintEvents']) >= 96,
                            'Table scrolling did not paint every measured step')
                    require(int(facts['scrollbarSteps']) == 48 and int(facts['directScrollSteps']) == 48,
                            'Both Table logical scrolling entrypoints must be measured')
                    require(top == 900_000, 'Table did not finish at the distant logical row')
        hashes[path.name] = hashlib.sha256(data).hexdigest()
    receipt = {'status': 'PASS', 'junit': counts, 'resourceArtifacts': hashes,
               'budgetAuthority': 'ViewportResourceRegressionTest (unchanged)', 'manualQaRequired': False}
    receipt_path.write_text(json.dumps(receipt, indent=2) + '\n')
    print(json.dumps(receipt, sort_keys=True))
    return receipt


def check_bounded_table_scene(name, facts, pixels):
    expected = {
        'bounded-table-top': (0, (64, 96, 128)),
        'bounded-table-distant': (900_000, (238, 28, 32)),
        'bounded-table-scrollbar': (100_000, (225, 231, 32)),
        'bounded-table-narrow': (100_000, (225, 231, 32))}
    top, rgb = expected[name]
    require(facts['bounded.widget'] == 'Table' and facts['bounded.model'] == 'explicit-bounded-opt-in',
            name + ': the screenshot must qualify the explicit bounded Table mode')
    require(int(facts['bounded.logicalRows']) == 2_000_000, name + ': logical row count')
    require(int(facts['bounded.topIndex']) == top and int(facts['bounded.hitIndex']) == top,
            name + ': wrong logical row or native hit translation')
    extent, body = (int(facts['bounded.' + key]) for key in ('rowExtent', 'bodyExtent'))
    require(extent > 0 and body > 0, name + ': empty viewport geometry')
    visible = (body + extent - 1) // extent
    overscan = int(facts['bounded.overscanRows'])
    require(overscan > 0 and int(facts['bounded.visibleRows']) == visible, name + ': missing visible/overscan rows')
    start, end = max(0, top - overscan), min(2_000_000, top + visible + overscan)
    require(int(facts['bounded.paintStart']) == start and int(facts['bounded.paintEndExclusive']) == end,
            name + ': incorrect physical window projection')
    require(int(facts['bounded.nativeRows']) == end - start and 0 < end - start <= 512,
            name + ': attached native rows do not match the viewport and overscan')
    for key in ('residentRows', 'setDataCalls'):
        require(0 <= int(facts['bounded.' + key]) <= 512, name + ': unbounded ' + key)
    require(int(facts['bounded.paintEvents']) > 0, name + ': the table did not paint')
    require(int(facts['bounded.selectionCount']) == 4 and facts['bounded.pinnedChecked'] == 'true' and
            facts['bounded.pinnedIndex'] == '1900000', name + ': logical selection/pinned state changed')
    require(int(facts['v.maximum']) == 2_000_000 * extent and int(facts['v.selection']) == top * extent and
            int(facts['v.thumb']) == body, name + ': scrollbar range no longer belongs to the logical model')
    width, height = (int(facts['bounded.physical' + key]) for key in ('Width', 'Height'))
    require(0 < width <= 960 and 0 < height <= 640, name + ': physical Table exceeded the viewport')
    x, y = (int(facts['bounded.marker.' + axis]) for axis in ('x', 'y'))
    require(0 <= x < width and 0 <= y < height, name + ': native background marker is outside the capture')
    require(facts['bounded.marker.rgb'] == ','.join(str(value) for value in rgb), name + ': wrong row color label')
    pixel_x, pixel_y = x * pixels.shape[1] // width, y * pixels.shape[0] // height
    patch = pixels[max(0, pixel_y - 1):pixel_y + 2, max(0, pixel_x - 1):pixel_x + 2]
    require(patch.size > 0 and bool(np.all(patch == np.array(rgb[::-1], dtype=np.uint8))),
            name + ': persisted native screen pixels contain a stale or misplaced logical row')
    return {'topIndex': top, 'visibleRows': visible, 'overscanRows': overscan, 'nativeRows': end - start}


def main(test_root=Path(".")):
    global cv2, np
    import cv2
    import numpy as np
    output = test_root / "target/screenshots/viewport"
    (output / "opencv-validation.json").unlink(missing_ok=True)
    rejected = negative_controls()
    report = test_root / 'target/surefire-reports/TEST-org.eclipse.swt.tests.junit.ViewportScreenshotRegressionTest.xml'
    counts = qualified_suite(report, 'org.eclipse.swt.tests.junit.ViewportScreenshotRegressionTest', {
        'test_viewportScreenshotRegression', 'test_logicalScrolledContentSnapshotsAtTenMillionRows', 'test_swtScreenCaptureTracksVisiblePixels',
        'test_screenCaptureRejectsInvalidTargets', 'test_capturePathsReleaseGraphicsOnSuccessAndFailure',
        'test_boundedTableSnapshotsUseLogicalScrollbarAndOverscan'})
    lifetime = dict(line.split('=', 1) for line in (test_root / 'target/capture-qualification/graphics-lifetime.properties').read_text().splitlines())
    for key, expected in {'capturePaths': '3', 'successfulCaptures': '48', 'writeFailures': '3',
                          'positiveLeakControls': '2', 'retainedGraphicsDelta': '0'}.items():
        require(lifetime[key] == expected, 'Graphics resource qualification: ' + key)
    scenes = ('table-checked-selection', 'tree-pinned-expanded', 'tree-pinned-collapsed',
              'tree-pinned-restored', 'viewport-vertical-scroll', 'viewport-horizontal-scroll',
              'viewport-narrow', 'viewport-wide', 'viewport-affine-clip-stroke',
              'logical-viewport-top', 'logical-viewport-deep',
              'logical-viewport-horizontal', 'logical-viewport-narrow',
              'bounded-table-top', 'bounded-table-distant', 'bounded-table-scrollbar', 'bounded-table-narrow')
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
    bounded = {name: check_bounded_table_scene(name, scene_facts[name], images[name])
               for name in scenes if name.startswith('bounded-table-')}
    table_top, table_far, table_bar, table_narrow = (images['bounded-table-' + suffix]
                                                    for suffix in ('top', 'distant', 'scrollbar', 'narrow'))
    require(table_top.shape == table_far.shape == table_bar.shape, 'Bounded Table scroll geometry drift')
    table_distant_delta = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(table_top, table_far), cv2.COLOR_BGR2GRAY))
    table_scrollbar_delta = cv2.countNonZero(cv2.cvtColor(cv2.absdiff(table_far, table_bar), cv2.COLOR_BGR2GRAY))
    require(table_distant_delta > 20 and table_scrollbar_delta > 20,
            'Distant or scrollbar-driven Table screenshots are stale')
    require(table_narrow.shape[0] < table_bar.shape[0] and table_narrow.shape[1] < table_bar.shape[1],
            'Bounded Table resize capture is stale')
    receipt = {'status': 'PASS', 'opencv': cv2.__version__, 'numpy': np.__version__,
               'junit': counts, 'sceneArtifacts': metrics, 'canary': canary,
               'scrollChangedPixels': scroll_delta, 'logicalScrollChangedPixels': logical_delta,
               'logicalHorizontalChangedPixels': horizontal_delta, 'negativeControlsRejected': rejected,
               'boundedTableScenes': bounded, 'boundedTableDistantChangedPixels': table_distant_delta,
               'boundedTableScrollbarChangedPixels': table_scrollbar_delta,
               'graphicsLifetime': lifetime, 'manualQaRequired': False, 'scope': 'GTK3/X11 controlled geometry and scene sanity; no cross-platform golden approval'}
    (output / 'opencv-validation.json').write_text(json.dumps(receipt, indent=2) + '\n')
    print(json.dumps(receipt, sort_keys=True))


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--test-root', type=Path, default=Path('.'))
    parser.add_argument('--resources', action='store_true')
    options = parser.parse_args()
    (verify_resources if options.resources else main)(options.test_root)

