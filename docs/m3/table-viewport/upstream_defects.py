"""Generate UPSTREAM_DEFECTS.tsv: the differential steps where the official SWT jar answers differently from this SWT
because of an upstream defect (docs/m3/TABLE_MEDIATED_VIEWPORT.md, section "Upstream defects").

Usage: python -I upstream_defects.py <official recording dir> <default-mode recording dir> <out.tsv>

The default-mode recording is this SWT in the default (viewport) mode recorded with -Dswt.viewport.record=true. The
original mode is stock (rule 12 of the viewport agent brief): its recording equals the official one line for line
(table_differential_references.sh checks it), so the default mode is compared with the official recording here. Every
step whose official line differs from the default-mode line is listed, with both answers. Two R4_38 defects of the
keyed item store of VIRTUAL Tables are the accepted causes: a seed may diverge only in a VIRTUAL variant, and only
after a range removal ("remove a..b") or from an insert that throws ArrayIndexOutOfBoundsException; any other
divergence stops the generator, so it is never listed silently.
Test_org_eclipse_swt_widgets_Table_ViewportDifferential accepts exactly the listed steps when it compares with either
stock recording (official or original).
"""
import os, sys

VIRTUAL_VARIANTS = ('2', '8')  # MULTI | VIRTUAL with the API script, and with the reentrancy script
CAUSE_THROW = ('R4_38 Table.java:392 _removeItems shifts the later keys by right - left (items held in the range) '
               'instead of end - start (rows removed); a stale key >= count throws in _checkGrow at Table.java:193, '
               'called from _getItem at :295 (getItems :2611) or createItem at :1767')
# rule 12 of the viewport agent brief: every listed step names the stock Javadoc it contradicts
JAVADOC_REMOVE = ('Table.remove(int,int) (R4_38 Table.java:3160): "Removes the items from the receiver which are between the '
                  'given zero-relative start and end indices (inclusive)."; indexOf(TableItem) (:2948): "returns the index of '
                  'that item"; getItems() (:2605): "Returns a (possibly empty) array of TableItems which are the items in the '
                  'receiver."')
CAUSE_INSERT = ('R4_38 Table.java:1767 createItem grows the keyed store (_checkGrow) before LVM_INSERTITEM (:1787); inside '
                'a listener the insert repaints and LVN_GETDISPINFO makes items in the store (_getItem from :6552; '
                'evidence/InsertReplayProbe: keys 2/4 before, 20/20 at the throw), so _insertItem (:1790) copies past a full '
                'keys array at :347 (ArrayIndexOutOfBoundsException) and the inserted item is missing from the store')
CAUSE_INSERT_STALE = ('R4_38 Table.java:347 after the failed insert the keyed store misses the inserted item and the native '
                      'row: rows, items, SetData and the state after them differ')
JAVADOC_INSERT = ('TableItem(Table,int,int) (R4_38 TableItem.java:115): "Constructs a new instance of this class given its parent '
                  '... and the index at which to place it in the items maintained by its parent."; its exceptions are '
                  'ERROR_NULL_ARGUMENT, ERROR_INVALID_RANGE, ERROR_THREAD_INVALID_ACCESS and ERROR_INVALID_SUBCLASS only')
API_INSERT = 'TableItem(Table,int,int) inside a listener of a VIRTUAL Table; then getItem, getItems, indexOf, SetData'
INSERT_THROW = 'error java.lang.ArrayIndexOutOfBoundsException arraycopy: last destination index'
# rule 12 columns: the widget and the API whose answer differs
WIDGET, API_REMOVE = 'Table', 'remove(int,int); then getItem, getItems, indexOf, SetData'
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
        insert = INSERT_THROW in stock[diverging[0]].split(' | ')[0]
        if variant not in VIRTUAL_VARIANTS or not (before or insert):
            sys.exit(name + ': divergence at step %d is not a keyed-store defect' % (diverging[0] - 1))
        for i in diverging:
            if '\t' in stock[i] + ours[i]:
                sys.exit(name + ': tab in an answer')
            if insert:
                cause = CAUSE_INSERT if i == diverging[0] else CAUSE_INSERT_STALE
                rows.append('\t'.join((variant, seed, str(i - 1), stock[i], ours[i], cause, JAVADOC_INSERT, WIDGET, API_INSERT)))
            else:
                cause = CAUSE_THROW if stock[i].startswith('error java.lang.ArrayIndexOutOfBoundsException') else CAUSE_STALE
                rows.append('\t'.join((variant, seed, str(i - 1), stock[i], ours[i], cause, JAVADOC_REMOVE, WIDGET, API_REMOVE)))
        print('%s: %d steps from step %d (%s)' % (name, len(diverging), diverging[0] - 1,
                                                  'insert throws' if insert else 'after ' + before[-1].split(' |')[0]))
    with open(out, 'w', encoding='utf-8', newline='\n') as f:
        f.write('variant\tseed\tstep\tstock_answer\tour_answer\tupstream_cause\tstock_javadoc\twidget\tapi\n')
        f.write(''.join(row + '\n' for row in rows))
    print('%d rows -> %s' % (len(rows), out))


if __name__ == '__main__':
    main(*sys.argv[1:4])
