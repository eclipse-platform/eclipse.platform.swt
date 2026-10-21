#!/usr/bin/env python3
# Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0
"""Read-only receiver proof. Requires actual Maven-generated output, never substitutes a template."""
from pathlib import Path
import argparse, hashlib, json, os, platform, subprocess, tempfile
HERE = Path(__file__).resolve().parent
RESOURCE = HERE / '../../src/main/resources/com/synexia/rewrite/hash-pinned-java/swt-scrollbar-full-range-20261020'
TARGET = Path('bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/ViewportScrollBarGeometry.java')
BEFORE = '0b8ede7af0551b7afaa270b9723bb405fad315f4f0ddb6e8dfc4db3267e30b44'
AFTER = 'df4f8498b1f633f2ffeae310f761ecaa0ea686e392557b8cfe2c684864a7f9b4'

def seal(path, digest):
    data = path.read_bytes()
    if hashlib.sha256(data).hexdigest() != digest: raise RuntimeError('source drift: ' + str(path))
    return data

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--receiver', type=Path)
    args = parser.parse_args()
    generated = HERE / 'target/generated-receiver' / TARGET
    before = RESOURCE / 'ViewportScrollBarGeometry.before.java.txt'
    seal(before, BEFORE); seal(generated, AFTER)
    if args.receiver: seal(args.receiver / TARGET, AFTER)
    evidence = HERE / 'target/evidence'; evidence.mkdir(parents=True, exist_ok=True)
    def run(command, label):
        result = subprocess.run(list(map(str, command)), text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=180)
        (evidence / (label + '.log')).write_text(result.stdout)
        if result.returncode: raise RuntimeError(label + ' failed: ' + result.stdout)
        print(result.stdout, end='')
        return result.stdout
    run(['java', '-version'], 'java-version')
    with tempfile.TemporaryDirectory(prefix='swt-full-range-') as temporary:
        root = Path(temporary); signatures = {}
        for name, source, mode in [('baseline', before, 'baseline'), ('generated', generated, 'strict')]:
            work = root / name; work.mkdir(); classes = work / 'classes'; classes.mkdir()
            # Baseline text fixture receives its original compiler filename; no production worktree writes.
            actual_source = work / 'ViewportScrollBarGeometry.java'
            actual_source.write_bytes(source.read_bytes())
            run(['javac', '--release', '21', '-proc:none', '-Xlint:all', '-Werror', '-d', classes,
                 actual_source, HERE / 'proof/ScrollbarFullRangeProof.java'], name + '-compile')
            for suffix, flags in [('tiered', []), ('c1', ['-XX:TieredStopAtLevel=1'])]:
                run(['java', '-ea', '-Xmx128m', *flags, '-cp', classes,
                     'org.eclipse.swt.widgets.ScrollbarFullRangeProof', mode], name + '-' + suffix)
            signatures[name] = run(['javap', '-s', '-classpath', classes,
                                   'org.eclipse.swt.widgets.ViewportScrollBarGeometry'], name + '-api')
        if signatures['baseline'] != signatures['generated']: raise RuntimeError('package/public ABI drift')
    receipt = {'platform': platform.platform(), 'before': BEFORE, 'after': AFTER,
               'SDK_OUTPUT_CONSUMED': True, 'EXACT_RECEIVER': bool(args.receiver),
               'checksPerMode': 5723783, 'packagePublicDescriptors': 'IDENTICAL',
               'nativeWidgetTests': 'NOT_RUN', 'SwtTychoBuild': 'NOT_RUN', 'fullM3Runtime': 'NOT_RUN'}
    (evidence / 'receipt.json').write_text(json.dumps(receipt, indent=2) + '\n')
    print('CANONICAL_GENERATED_SCROLLBAR_GEOMETRY_PASS')

if __name__ == '__main__': main()
