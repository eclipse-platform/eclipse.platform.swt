"""Generate UPSTREAM_DEFECTS.tsv: the differential steps where the official SWT jar answers differently from this SWT
because of an upstream defect (docs/m3/TABLE_MEDIATED_VIEWPORT.md, section "Upstream defects").

Usage: python -I upstream_defects.py <official recording dir> <default-mode recording dir> <out.tsv>

The default-mode recording is this SWT in the default (viewport) mode recorded with -Dswt.viewport.record=true. The
original mode is stock (rule 12 of the viewport agent brief): its recording equals the official one line for line
(table_differential_references.sh checks it), so the default mode is compared with the official recording here. Every
step whose official line differs from the default-mode line is listed, with both answers. The only accepted
cause is the R4_38 keyed-store defect of VIRTUAL tables: a seed may diverge only in the VIRTUAL variant and only
after a range removal ("remove a..b"); any other divergence stops the generator, so it is never listed silently.
Test_org_eclipse_swt_widgets_Table_ViewportDifferential accepts exactly the listed steps when it compares with either
stock recording (official or original).
"""
import os, sys

VIRTUAL_VARIANT = '2'
CAUSE_THROW = ('R4_38 Table.java:392 _removeItems shifts the later keys by right - left (items held in the range) '
               'instead of end - start (rows removed); a stale key >= count throws in _checkGrow at Table.java:193, '
               'called from _getItem at :295 (getItems :2611) or createItem at :1767')
CAUSE_STALE = ('R4_38 Table.java:392 _removeItems leaves the later keys too large: rows after a removed range answer '
               'another row\'s item or none, so items, SetData and the state after them differ')


def lines(path):
    with open(path, encoding='utf-8', newline='') as f:
        return f.read().replace('\r\n', '\n').split('\n')


def main(official, original, out):
    rows = []
    for name in sorted(os.listdir(official)):
        if not name.endswith('.txt'):
            continue
        stock, ours = lines(os.path.join(official, name)), lines(os.path.join(original, name))
        if not stock[0].startswith('# reference official'):
            sys.exit(name + ': not an official recording')
        diverging = [i for i in range(1, max(len(stock), len(ours))) if stock[i:i + 1] != ours[i:i + 1]]
        if not diverging:
            continue
        variant, seed = name[len('table-variant'):-len('.txt')].split('-seed')
        before = [line for line in ours[1:diverging[0] + 1] if line.startswith('remove ') and '..' in line.split(' |')[0]]
        if variant != VIRTUAL_VARIANT or not before:
            sys.exit(name + ': divergence at step %d is not the keyed-store defect' % (diverging[0] - 1))
        for i in diverging:
            if '\t' in stock[i] + ours[i]:
                sys.exit(name + ': tab in an answer')
            cause = CAUSE_THROW if stock[i].startswith('error java.lang.ArrayIndexOutOfBoundsException') else CAUSE_STALE
            rows.append('\t'.join((variant, seed, str(i - 1), stock[i], ours[i], cause)))
        print('%s: %d steps from step %d (after %s)' % (name, len(diverging), diverging[0] - 1, before[-1].split(' |')[0]))
    with open(out, 'w', encoding='utf-8', newline='\n') as f:
        f.write('variant\tseed\tstep\tstock_answer\tour_answer\tupstream_cause\n')
        f.write(''.join(row + '\n' for row in rows))
    print('%d rows -> %s' % (len(rows), out))


if __name__ == '__main__':
    main(*sys.argv[1:4])
