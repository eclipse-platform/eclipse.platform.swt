"""Per-pixel differential of two binary PPM (P6) captures.

usage: python -B ppmdiff.py <oracle.ppm> <candidate.ppm> [tolerance] [diff.ppm]
Prints one TSV row: pixels, exact-equal, max channel delta, counts above
thresholds 8/32/64 and the tolerance verdict (fraction of pixels whose max
channel delta exceeds <tolerance>, default 48).
"""
import sys


def read(path):
    data = open(path, "rb").read()
    parts = data.split(b"\n", 3)
    w, h = map(int, parts[1].split())
    return w, h, parts[3]


def main():
    a_path, b_path = sys.argv[1], sys.argv[2]
    tol = int(sys.argv[3]) if len(sys.argv) > 3 else 48
    w, h, a = read(a_path)
    w2, h2, b = read(b_path)
    assert (w, h) == (w2, h2), "size mismatch"
    n = w * h
    exact = over8 = over32 = over64 = over_tol = 0
    max_delta = 0
    diff = bytearray(n * 3)
    for i in range(n):
        o = i * 3
        d = max(abs(a[o] - b[o]), abs(a[o + 1] - b[o + 1]), abs(a[o + 2] - b[o + 2]))
        if d == 0:
            exact += 1
        if d > 8:
            over8 += 1
        if d > 32:
            over32 += 1
        if d > 64:
            over64 += 1
        if d > tol:
            over_tol += 1
        if d > max_delta:
            max_delta = d
        diff[o] = diff[o + 1] = diff[o + 2] = min(255, d * 4)
    if len(sys.argv) > 4:
        open(sys.argv[4], "wb").write(b"P6\n%d %d\n255\n" % (w, h) + bytes(diff))
    print("\t".join(str(x) for x in [
        b_path.replace("\\", "/").split("/")[-1], n, exact, "%.4f" % (exact / n), max_delta,
        over8, over32, over64, tol, over_tol, "%.4f%%" % (100.0 * over_tol / n)]))


if __name__ == "__main__":
    main()
