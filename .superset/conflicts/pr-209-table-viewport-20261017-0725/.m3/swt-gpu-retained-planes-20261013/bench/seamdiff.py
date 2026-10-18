"""Compare DamageSeamProbe region captures (CPU only, deterministic).

usage: python -B seamdiff.py <off.bin> <candidate.bin> <label>
Prints one TSV row per region (1 border ring, 2 scrollbar, 3 margin plane,
4 tile seams): frames, frames differing from the stock oracle at the same frame,
pixels differing, and frames whose region changed versus the candidate's own
first frame (border, scrollbar and margin must never change during the run).
"""
import struct
import sys

NAMES = {1: "border", 2: "scrollbar", 3: "margin", 4: "seams"}


def frames(path):
    data = open(path, "rb").read()
    o = 0
    out = []
    while True:
        (w,) = struct.unpack_from(">i", data, o)
        o += 4
        if w == -1:
            return out
        (h,) = struct.unpack_from(">i", data, o)
        o += 4
        regions = {1: bytearray(), 2: bytearray(), 3: bytearray(), 4: bytearray()}
        # records until the trailing trim int: each record is 4 bytes (region, r, g, b)
        n_pixels = None
        frame_regions = regions
        # read records: region byte 1..4; the trailer int's first byte is 0 for small trims
        while True:
            region = data[o]
            if region not in (1, 2, 3, 4):
                break
            frame_regions[region] += data[o + 1:o + 4]
            o += 4
        o += 4  # trim int
        out.append(frame_regions)


def main():
    off, cand, label = frames(sys.argv[1]), frames(sys.argv[2]), sys.argv[3]
    n = min(len(off), len(cand))
    for r in (1, 2, 3, 4):
        diff_frames = diff_px = self_change = 0
        for i in range(n):
            a, b = off[i][r], cand[i][r]
            if len(a) != len(b):
                diff_frames += 1
                diff_px += max(len(a), len(b)) // 3
                continue
            d = sum(1 for k in range(0, len(a), 3) if a[k:k + 3] != b[k:k + 3])
            if d:
                diff_frames += 1
                diff_px += d
            if r != 4 and cand[i][r] != cand[0][r]:
                self_change += 1
        print("\t".join(str(x) for x in [label, NAMES[r], n, diff_frames, diff_px, self_change if r != 4 else "-"]))


if __name__ == "__main__":
    main()
