// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.eclipse.swt.internal.ViewportDamageRecorder;
import org.eclipse.swt.internal.ViewportDamageRegion;
import org.junit.jupiter.api.Test;

/**
 * Headless contracts for the damage region and recorder. The region is compared with a per-pixel
 * bitmap oracle; the recorder is fed with synthetic RFB FramebufferUpdate messages, the same
 * rectangle list a VNC server reports for the area a paint pass changed.
 */
public class Test_ViewportDamage {
	private static final int ORIGIN = -8, SIZE = 80;

	/** One bit per pixel on a small grid with negative coordinates. */
	private static final class Bitmap {
		final boolean [] px = new boolean [SIZE * SIZE];

		void add(int x, int y, int w, int h) {
			for (int j = Math.max(y, ORIGIN); j < Math.min(y + h, ORIGIN + SIZE); j++) {
				for (int i = Math.max(x, ORIGIN); i < Math.min(x + w, ORIGIN + SIZE); i++) {
					px[(j - ORIGIN) * SIZE + (i - ORIGIN)] = true;
				}
			}
		}

		long count() {
			long n = 0;
			for (boolean b : px) if (b) n++;
			return n;
		}

		boolean covers(int x, int y, int w, int h) {
			for (int j = y; j < y + h; j++) {
				for (int i = x; i < x + w; i++) {
					if (i < ORIGIN || j < ORIGIN || i >= ORIGIN + SIZE || j >= ORIGIN + SIZE) return false;
					if (!px[(j - ORIGIN) * SIZE + (i - ORIGIN)]) return false;
				}
			}
			return true;
		}

		long within(int x, int y, int w, int h) {
			Bitmap box = new Bitmap();
			box.add(x, y, w, h);
			long n = 0;
			for (int k = 0; k < px.length; k++) if (px[k] && box.px[k]) n++;
			return n;
		}
	}

	private static void assertDisjoint(ViewportDamageRegion r) {
		for (int a = 0; a < r.rectCount(); a++) {
			for (int b = a + 1; b < r.rectCount(); b++) {
				boolean apart = r.x(a) >= r.x(b) + r.width(b) || r.x(b) >= r.x(a) + r.width(a)
						|| r.y(a) >= r.y(b) + r.height(b) || r.y(b) >= r.y(a) + r.height(a);
				assertTrue(apart, "rectangles " + a + " and " + b + " overlap");
			}
		}
	}

	@Test
	public void regionMatchesPixelOracleOnRandomRectangles() {
		Random random = new Random(0xD41A9EL);
		for (int round = 0; round < 400; round++) {
			ViewportDamageRegion region = new ViewportDamageRegion(4096);
			Bitmap oracle = new Bitmap();
			for (int step = 0; step < 12; step++) {
				int x = random.nextInt(48) - 8, y = random.nextInt(48) - 8;
				int w = random.nextInt(22) - 1, h = random.nextInt(22) - 1;
				region.add(x, y, w, h);
				if (w > 0 && h > 0) oracle.add(x, y, w, h);
				assertEquals(oracle.count(), region.area(), "area after " + step + " adds, round " + round);
				assertDisjoint(region);
			}
			int px = random.nextInt(40) - 8, py = random.nextInt(40) - 8, pw = 1 + random.nextInt(12), ph = 1 + random.nextInt(12);
			assertEquals(oracle.covers(px, py, pw, ph), region.covers(px, py, pw, ph));
			assertEquals(oracle.within(px, py, pw, ph), region.areaWithin(px, py, pw, ph));
			ViewportDamageRegion other = new ViewportDamageRegion(4096);
			Bitmap otherOracle = new Bitmap();
			for (int k = 0; k < 3; k++) {
				int x = random.nextInt(48) - 8, y = random.nextInt(48) - 8, w = 4 + random.nextInt(30), h = 4 + random.nextInt(30);
				other.add(x, y, w, h);
				otherOracle.add(x, y, w, h);
			}
			boolean subset = true;
			for (int k = 0; k < oracle.px.length; k++) if (oracle.px[k] && !otherOracle.px[k]) subset = false;
			assertEquals(subset, region.isSubsetOf(other), "subset, round " + round);
		}
	}

	@Test
	public void adjacentRectanglesMergeAndRepeatsAddNothing() {
		ViewportDamageRegion r = new ViewportDamageRegion();
		r.add(0, 0, 10, 5);
		r.add(0, 5, 10, 5);
		assertEquals(1, r.rectCount());
		assertEquals(100, r.area());
		r.add(10, 0, 5, 10);
		assertEquals(1, r.rectCount());
		assertEquals(150, r.area());
		r.add(2, 2, 3, 3);
		r.add(0, 0, 15, 10);
		assertEquals(1, r.rectCount());
		assertEquals(150, r.area());
		r.add(3, 3, 0, 9);
		r.add(3, 3, 9, -1);
		assertEquals(150, r.area());
	}

	@Test
	public void overflowCollapsesToTheBoundingBox() {
		ViewportDamageRegion r = new ViewportDamageRegion(3);
		r.add(0, 0, 2, 2);
		r.add(10, 0, 2, 2);
		r.add(20, 0, 2, 2);
		assertFalse(r.overflowed());
		r.add(30, 30, 2, 2);
		assertTrue(r.overflowed());
		assertEquals(1, r.rectCount());
		assertEquals(32L * 32L, r.area());
		assertTrue(r.covers(0, 0, 2, 2));
		r.add(-5, -5, 1, 1);
		assertEquals(1, r.rectCount());
		assertEquals(37L * 37L, r.area());
		r.clear();
		assertFalse(r.overflowed());
		assertTrue(r.isEmpty());
	}

	@Test
	public void copyDamagesOnlyTheDestination() {
		ViewportDamageRegion r = new ViewportDamageRegion();
		r.addCopy(0, 0, 0, 20, 100, 50);
		assertEquals(1, r.copyCount());
		assertEquals(5000, r.copiedPixels());
		assertTrue(r.covers(0, 20, 100, 50));
		assertFalse(r.covers(0, 0, 100, 20));
		assertEquals(5000, r.area());
	}

	@Test
	public void regionRejectsOutOfRangeAndBadIndex() {
		ViewportDamageRegion r = new ViewportDamageRegion();
		assertThrows(IllegalArgumentException.class, () -> r.add(Integer.MAX_VALUE - 5, 0, 10, 10));
		assertThrows(IllegalArgumentException.class, () -> r.add(0, 0, Integer.MAX_VALUE, 10));
		assertThrows(IllegalArgumentException.class, () -> new ViewportDamageRegion(0));
		assertThrows(IndexOutOfBoundsException.class, () -> r.x(0));
		assertThrows(IllegalArgumentException.class, () -> r.boundingBox(new int [3]));
	}

	@Test
	public void recorderTracksPaintedVersusMovedAndFullRepaints() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(100, 100);
		rec.paint(0, 0, 100, 10);
		rec.endFrame();
		rec.paint(0, 0, 100, 100);
		rec.endFrame();
		rec.paint(-50, -50, 60, 60);
		rec.endFrame();
		rec.copy(0, 10, 0, 0, 100, 90);
		rec.paint(0, 90, 100, 10);
		rec.endFrame();
		assertEquals(4, rec.frameCount());
		assertEquals(1000, rec.paintedPx(0));
		assertFalse(rec.isFullRepaint(0));
		assertTrue(rec.isFullRepaint(1));
		assertEquals(3600, rec.paintedPx(2));
		assertEquals(100, rec.visiblePaintedPx(2));
		assertEquals(10000, rec.damagePx(3));
		assertEquals(1000, rec.paintedPx(3));
		assertFalse(rec.isFullRepaint(3));
		assertEquals(1, rec.copyRects(3));
		assertEquals(1, rec.fullRepaints());
		assertEquals(10000, rec.maxPaintedPx());
		assertEquals(1000 + 10000 + 3600 + 1000, rec.totalPaintedPx());
	}

	@Test
	public void endFrameChecksDamageAgainstTheExpectedArea() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(100, 100);
		ViewportDamageRegion expected = new ViewportDamageRegion();
		expected.add(0, 0, 100, 10);
		rec.paint(10, 2, 50, 5);
		assertTrue(rec.endFrame(expected));
		rec.paint(10, 2, 50, 5);
		rec.paint(10, 50, 5, 5);
		assertFalse(rec.endFrame(expected));
		assertEquals(1, rec.violations());
		assertEquals(2, rec.frameCount());
	}

	@Test
	public void recorderCapsStoredFramesButKeepsTotals() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(10, 10);
		for (int i = 0; i < ViewportDamageRecorder.MAX_FRAMES + 5; i++) {
			rec.paint(0, 0, 1, 1);
			rec.endFrame();
		}
		assertEquals(5, rec.droppedFrames());
		assertEquals(ViewportDamageRecorder.MAX_FRAMES + 5L, rec.frameCount());
		assertEquals(ViewportDamageRecorder.MAX_FRAMES + 5L, rec.totalPaintedPx());
		assertThrows(IndexOutOfBoundsException.class, () -> rec.paintedPx(ViewportDamageRecorder.MAX_FRAMES));
		rec.reset();
		assertEquals(0, rec.frameCount());
	}

	/** Minimal RFB FramebufferUpdate reader: Raw and CopyRect only, as a client that advertises just those. */
	private static final class RfbFeed {
		static final int RAW = 0, COPY_RECT = 1, DESKTOP_SIZE = -223, LAST_RECT = -224, CURSOR = -239;

		static int feed(byte [] message, int bytesPerPixel, ViewportDamageRecorder sink) {
			ByteBuffer in = ByteBuffer.wrap(message);
			if (in.remaining() < 4 || in.get() != 0) throw new IllegalArgumentException("not a FramebufferUpdate");
			in.get();
			int rects = in.getShort() & 0xFFFF;
			int fed = 0;
			for (int i = 0; i < rects; i++) {
				if (in.remaining() < 12) throw new IllegalArgumentException("truncated rectangle header");
				int x = in.getShort() & 0xFFFF, y = in.getShort() & 0xFFFF, w = in.getShort() & 0xFFFF, h = in.getShort() & 0xFFFF;
				int encoding = in.getInt();
				if (encoding == LAST_RECT) break;
				fed += rectangle(in, encoding, x, y, w, h, bytesPerPixel, sink);
			}
			return fed;
		}

		private static int rectangle(ByteBuffer in, int encoding, int x, int y, int w, int h, int bpp, ViewportDamageRecorder sink) {
			switch (encoding) {
				case RAW:
					skip(in, (long) w * h * bpp);
					sink.paint(x, y, w, h);
					return 1;
				case COPY_RECT:
					if (in.remaining() < 4) throw new IllegalArgumentException("truncated CopyRect");
					int srcX = in.getShort() & 0xFFFF, srcY = in.getShort() & 0xFFFF;
					sink.copy(srcX, srcY, x, y, w, h);
					return 1;
				case DESKTOP_SIZE:
					sink.setViewport(w, h);
					return 0;
				case CURSOR:
					skip(in, (long) w * h * bpp + (long) ((w + 7) / 8) * h);
					return 0;
				default:
					throw new IllegalArgumentException("unsupported encoding " + encoding);
			}
		}

		private static void skip(ByteBuffer in, long bytes) {
			if (bytes > in.remaining()) throw new IllegalArgumentException("truncated payload");
			in.position(in.position() + (int) bytes);
		}
	}

	private static ByteBuffer message(int rects, int capacity) {
		ByteBuffer b = ByteBuffer.allocate(capacity);
		b.put((byte) 0).put((byte) 0).putShort((short) rects);
		return b;
	}

	private static void header(ByteBuffer b, int x, int y, int w, int h, int encoding) {
		b.putShort((short) x).putShort((short) y).putShort((short) w).putShort((short) h).putInt(encoding);
	}

	private static byte [] bytes(ByteBuffer b) {
		byte [] out = new byte [b.position()];
		System.arraycopy(b.array(), 0, out, 0, out.length);
		return out;
	}

	@Test
	public void blitScrollPaintsOnlyTheExposedStrip() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(200, 100);
		ByteBuffer b = message(2, 32768);
		header(b, 0, 0, 200, 80, RfbFeed.COPY_RECT);
		b.putShort((short) 0).putShort((short) 20);
		header(b, 0, 80, 200, 20, RfbFeed.RAW);
		b.put(new byte [200 * 20 * 4]);
		assertEquals(2, RfbFeed.feed(bytes(b), 4, rec));
		ViewportDamageRegion strip = new ViewportDamageRegion();
		strip.add(0, 80, 200, 20);
		assertTrue(rec.currentPainted().isSubsetOf(strip));
		ViewportDamageRegion viewport = new ViewportDamageRegion();
		viewport.add(0, 0, 200, 100);
		assertTrue(rec.endFrame(viewport));
		assertEquals(20000, rec.damagePx(0));
		assertEquals(4000, rec.paintedPx(0));
		assertFalse(rec.isFullRepaint(0), "a scroll that blits is not a full repaint");
		assertEquals(0, rec.fullRepaints());
	}

	@Test
	public void rfbFullUpdateIsAFullRepaintAndDesktopSizeResizesTheViewport() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(50, 50);
		ByteBuffer b = message(3, 8192);
		header(b, 0, 0, 100, 40, RfbFeed.DESKTOP_SIZE);
		header(b, 0, 0, 100, 40, RfbFeed.RAW);
		b.put(new byte [100 * 40 * 2]);
		header(b, 0, 0, 0, 0, RfbFeed.LAST_RECT);
		assertEquals(1, RfbFeed.feed(bytes(b), 2, rec));
		rec.endFrame();
		assertTrue(rec.isFullRepaint(0));
		assertEquals(1, rec.fullRepaints());
	}

	private static final int [] ZOOMS = {100, 125, 150, 175, 200};

	@Test
	public void pointRoundingIsFloorForStartAndCeilForEndAtEveryZoom() {
		Random random = new Random(0x2007A5L);
		for (int zoom : ZOOMS) {
			for (int i = 0; i < 2000; i++) {
				int p = random.nextInt(4001) - 2000;
				assertEquals(Math.floorDiv(p * zoom, 100), ViewportDamageRegion.pixelFloor(p, zoom), p + "@" + zoom);
				assertEquals(-Math.floorDiv(-p * zoom, 100), ViewportDamageRegion.pixelCeil(p, zoom), p + "@" + zoom);
			}
		}
		assertThrows(IllegalArgumentException.class, () -> ViewportDamageRegion.pixelFloor(1, 0));
		assertThrows(IllegalArgumentException.class, () -> ViewportDamageRegion.pixelCeil(1 << 28, 1600));
	}

	@Test
	public void pointRectangleContainsTheExactScaledRectangleWithinOnePixelPerEdge() {
		Random random = new Random(0xB0DE5L);
		for (int zoom : ZOOMS) {
			for (int i = 0; i < 500; i++) {
				int x = random.nextInt(400) - 200, y = random.nextInt(400) - 200, w = 1 + random.nextInt(300), h = 1 + random.nextInt(300);
				ViewportDamageRegion r = new ViewportDamageRegion();
				r.addPoints(x, y, w, h, zoom);
				assertEquals(1, r.rectCount());
				assertTrue(r.x(0) * 100 <= x * zoom && r.x(0) * 100 > x * zoom - 100, "left " + x + "@" + zoom);
				assertTrue((r.x(0) + r.width(0)) * 100 >= (x + w) * zoom && (r.x(0) + r.width(0)) * 100 < (x + w) * zoom + 100, "right");
				assertTrue(r.y(0) * 100 <= y * zoom && (r.y(0) + r.height(0)) * 100 >= (y + h) * zoom, "vertical");
			}
		}
	}

	@Test
	public void touchingPointRectanglesNeverLeaveAGapOrCountAPixelTwice() {
		Random random = new Random(0x5EA11L);
		for (int zoom : ZOOMS) {
			for (int i = 0; i < 500; i++) {
				int x = random.nextInt(200), y = random.nextInt(200), aw = 1 + random.nextInt(60), bw = 1 + random.nextInt(60), h = 1 + random.nextInt(60);
				ViewportDamageRegion r = new ViewportDamageRegion();
				r.addPoints(x, y, aw, h, zoom);
				r.addPoints(x + aw, y, bw, h, zoom);
				int left = ViewportDamageRegion.pixelFloor(x, zoom), right = ViewportDamageRegion.pixelCeil(x + aw + bw, zoom);
				int top = ViewportDamageRegion.pixelFloor(y, zoom), bottom = ViewportDamageRegion.pixelCeil(y + h, zoom);
				assertEquals(1, r.rectCount(), "merged to one rectangle at " + zoom + "%");
				assertTrue(r.covers(left, top, right - left, bottom - top), "no gap at " + zoom + "%");
				assertEquals((long) (right - left) * (bottom - top), r.area(), "no double count at " + zoom + "%");
			}
		}
	}

	@Test
	public void protectedAreaHitsAreCountedApartFromFullRepaints() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(100, 100);
		rec.protectedArea().add(90, 0, 10, 100);
		rec.protectedArea().add(0, 0, 90, 2);
		rec.paint(0, 2, 90, 98);
		rec.endFrame();
		rec.paint(0, 0, 100, 100);
		rec.endFrame();
		rec.paint(0, 2, 45, 98);
		rec.endFrame();
		assertTrue(rec.isFullRepaint(0), "all unprotected pixels painted");
		assertEquals(0, rec.protectedHitPx(0));
		assertTrue(rec.isFullRepaint(1));
		assertEquals(1180, rec.protectedHitPx(1));
		assertFalse(rec.isFullRepaint(2));
		assertEquals(0, rec.protectedHitPx(2));
		assertEquals(2, rec.fullRepaints());
		assertEquals(1, rec.protectedHitFrames());
		assertEquals(1180, rec.totalProtectedHitPx());
	}

	@Test
	public void protectedAreaInPointsRoundsLikeDamage() {
		for (int zoom : ZOOMS) {
			ViewportDamageRecorder rec = new ViewportDamageRecorder(ViewportDamageRegion.pixelCeil(200, zoom), ViewportDamageRegion.pixelCeil(100, zoom));
			rec.protectedArea().addPoints(190, 0, 10, 100, zoom);
			rec.current().addPoints(0, 0, 190, 100, zoom);
			rec.currentPainted().addPoints(0, 0, 190, 100, zoom);
			int borderPixels = ViewportDamageRegion.pixelCeil(190, zoom) - ViewportDamageRegion.pixelFloor(190, zoom);
			rec.endFrame();
			assertEquals((long) borderPixels * ViewportDamageRegion.pixelCeil(100, zoom), rec.protectedHitPx(0), "shared boundary column at " + zoom + "%");
		}
	}

	@Test
	public void rfbFeedRejectsMalformedMessages() {
		ViewportDamageRecorder rec = new ViewportDamageRecorder(10, 10);
		ByteBuffer wrongType = message(0, 8);
		byte [] w = bytes(wrongType);
		w[0] = 3;
		assertThrows(IllegalArgumentException.class, () -> RfbFeed.feed(w, 4, rec));
		ByteBuffer unsupported = message(1, 64);
		header(unsupported, 0, 0, 4, 4, 5);
		assertThrows(IllegalArgumentException.class, () -> RfbFeed.feed(bytes(unsupported), 4, rec));
		ByteBuffer truncated = message(1, 64);
		header(truncated, 0, 0, 4, 4, RfbFeed.RAW);
		assertThrows(IllegalArgumentException.class, () -> RfbFeed.feed(bytes(truncated), 4, rec));
		ByteBuffer shortHeader = message(2, 64);
		header(shortHeader, 0, 0, 1, 1, RfbFeed.COPY_RECT);
		shortHeader.putShort((short) 0).putShort((short) 0);
		assertThrows(IllegalArgumentException.class, () -> RfbFeed.feed(bytes(shortHeader), 4, rec));
	}
}
