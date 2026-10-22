// Copyright 2026 Hitesh Soliwal and contributors.
// SPDX-License-Identifier: Apache-2.0
import java.util.Random;

/** CPU-only event-order model. Not an SWT/GTK4 native display test. */
public final class SwtGtk4ListEventModelProof {
    private static final int RETURN = 13;
    private static final int KEYPAD_ENTER = 65421;
    private static long checks, legacyDiscrepancies;
    private static final class State {
        int mouseDown, defaultSelection;
        boolean pending, legacyActivated;
        void candidatePress(int clicks) {
            // Model the superclass dispatch reached for every press, including a single click.
            ++mouseDown;
            pending = clicks == 2;
        }
        void candidateRowActivated() {
            if (pending) ++defaultSelection;
            pending = false;
        }
        void candidateKey(int key, boolean modified) {
            pending = false;
            if ((key == RETURN || key == KEYPAD_ENTER) && !modified) ++defaultSelection;
        }
        void originalPress(int clicks) {
            if (clicks == 1) return;
            ++mouseDown;
            if (clicks == 2 && legacyActivated) { ++defaultSelection; legacyActivated = false; }
        }
        void originalRowActivated() { legacyActivated = true; }
    }
    private static void equal(int a, int b) {
        ++checks;
        if (a != b) throw new AssertionError("expected=" + a + ", observed=" + b);
    }
    private static void testFixedCases() {
        State candidate = new State(), legacy = new State();
        candidate.candidatePress(1);
        legacy.originalPress(1);
        equal(1, candidate.mouseDown);
        equal(0, legacy.mouseDown);
        ++legacyDiscrepancies;
        candidate.candidatePress(2);
        legacy.originalPress(2);
        candidate.candidateRowActivated();
        legacy.originalRowActivated();
        equal(1, candidate.defaultSelection);
        equal(0, legacy.defaultSelection);
        ++legacyDiscrepancies;
        candidate.candidatePress(1);
        legacy.originalPress(1);
        equal(1, candidate.defaultSelection);
        candidate.candidateKey(RETURN, false);
        equal(2, candidate.defaultSelection);
        candidate.candidateKey(KEYPAD_ENTER, false);
        equal(3, candidate.defaultSelection);
        candidate.candidateKey(RETURN, true);
        equal(3, candidate.defaultSelection);
    }
    public static void main(String[] args) {
        testFixedCases();
        Random random = new Random(0x4d3347544b344cL);
        for (int i = 0; i < 100_000; i++) {
            State state = new State(), legacy = new State();
            int expectedDown = 0, expectedDefault = 0;
            for (int step = 0; step < 12; step++) {
                int kind = random.nextInt(5);
                if (kind < 2) {
                    int clicks = 1 + kind;
                    state.candidatePress(clicks);
                    legacy.originalPress(clicks);
                    ++expectedDown;
                } else if (kind == 2) {
                    if (state.pending) ++expectedDefault;
                    state.candidateRowActivated();
                    legacy.originalRowActivated();
                } else {
                    int key = random.nextBoolean() ? RETURN : KEYPAD_ENTER;
                    boolean modifiers = random.nextInt(5) == 0;
                    state.candidateKey(key, modifiers);
                    if (!modifiers) ++expectedDefault;
                }
                equal(expectedDown, state.mouseDown);
                equal(expectedDefault, state.defaultSelection);
            }
            if (state.mouseDown != legacy.mouseDown || state.defaultSelection != legacy.defaultSelection) {
                ++legacyDiscrepancies;
            }
        }
        if (legacyDiscrepancies < 1000) throw new AssertionError("negative oracle lost");
        System.out.println("SWT_GTK4_EVENT_MODEL_PASS traces=100000 checks=" + checks
                + " legacy_divergences=" + legacyDiscrepancies);
    }
}
