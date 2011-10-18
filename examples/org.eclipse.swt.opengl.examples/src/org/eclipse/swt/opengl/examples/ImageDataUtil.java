/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class ImageDataUtil {
	/**
	 * Alpha mode, values 0 - 255 specify global alpha level
	 */
	static final int
		ALPHA_OPAQUE = 255,           // Fully opaque (ignores any alpha data)
		ALPHA_TRANSPARENT = 0,        // Fully transparent (ignores any alpha data)
		ALPHA_CHANNEL_SEPARATE = -1,  // Use alpha channel from separate alphaData
		ALPHA_CHANNEL_SOURCE = -2,    // Use alpha channel embedded in sourceData
		ALPHA_MASK_UNPACKED = -3,     // Use transparency mask formed by bytes in alphaData (non-zero is opaque)
		ALPHA_MASK_PACKED = -4,       // Use transparency mask formed by packed bits in alphaData
		ALPHA_MASK_INDEX = -5,        // Consider source palette indices transparent if in alphaData array
		ALPHA_MASK_RGB = -6;          // Consider source RGBs transparent if in RGB888 format alphaData array
		
	/**
	 * Data types (internal)
	 */
	private static final int
		// direct / true color formats with arbitrary masks & shifts
		TYPE_GENERIC_8 = 0,
		TYPE_GENERIC_16_MSB = 1,
		TYPE_GENERIC_16_LSB = 2,
		TYPE_GENERIC_24 = 3,
		TYPE_GENERIC_32_MSB = 4,
		TYPE_GENERIC_32_LSB = 5;
		
	/**
	 * Byte and bit order constants.
	 */
	static final int LSB_FIRST = 0;
	static final int MSB_FIRST = 1;
	
	/**
	 * Blit operation bits to be OR'ed together to specify the desired operation.
	 */
	static final int
		BLIT_SRC = 1,     // copy source directly, else applies logic operations
		BLIT_ALPHA = 2,   // enable alpha blending
		BLIT_DITHER = 4;  // enable dithering in low color modes
		
	/**
	 * Arbitrary channel width data to 8-bit conversion table.
	 */
	static final byte[][] ANY_TO_EIGHT = new byte[9][];
	static {
		for (int b = 0; b < 9; ++b) {
			byte[] data = ANY_TO_EIGHT[b] = new byte[1 << b];
			if (b == 0) continue;
			int inc = 0;
			for (int bit = 0x10000; (bit >>= b) != 0;) inc |= bit;
			for (int v = 0, p = 0; v < 0x10000; v+= inc) data[p++] = (byte)(v >> 8);
		}
	}

	/**
	 * Blits a direct palette image into a direct palette image.
	 * <p>
	 * Note: When the source and destination depth, order and masks
	 * are pairwise equal and the blitter operation is BLIT_SRC,
	 * the masks are ignored.  Hence when not changing the image
	 * data format, 0 may be specified for the masks.
	 * </p>
	 * 
	 * @param op the blitter operation: a combination of BLIT_xxx flags
	 *        (see BLIT_xxx constants)
	 * @param srcData the source byte array containing image data
	 * @param srcDepth the source depth: one of 8, 16, 24, 32
	 * @param srcStride the source number of bytes per line
	 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
	 *        ignored if srcDepth is not 16 or 32
	 * @param srcX the top-left x-coord of the source blit region
	 * @param srcY the top-left y-coord of the source blit region
	 * @param srcWidth the width of the source blit region
	 * @param srcHeight the height of the source blit region
	 * @param srcRedMask the source red channel mask
	 * @param srcGreenMask the source green channel mask
	 * @param srcBlueMask the source blue channel mask
	 * @param alphaMode the alpha blending or mask mode, may be
	 *        an integer 0-255 for global alpha; ignored if BLIT_ALPHA
	 *        not specified in the blitter operations
	 *        (see ALPHA_MODE_xxx constants)
	 * @param alphaData the alpha blending or mask data, varies depending
	 *        on the value of alphaMode and sometimes ignored
	 * @param alphaStride the alpha data number of bytes per line
	 * @param alphaX the top-left x-coord of the alpha blit region
	 * @param alphaY the top-left y-coord of the alpha blit region
	 * @param destData the destination byte array containing image data
	 * @param destDepth the destination depth: one of 8, 16, 24, 32
	 * @param destStride the destination number of bytes per line
	 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
	 *        ignored if destDepth is not 16 or 32
	 * @param destX the top-left x-coord of the destination blit region
	 * @param destY the top-left y-coord of the destination blit region
	 * @param destWidth the width of the destination blit region
	 * @param destHeight the height of the destination blit region
	 * @param destRedMask the destination red channel mask
	 * @param destGreenMask the destination green channel mask
	 * @param destBlueMask the destination blue channel mask
	 * @param flipX if true the resulting image is flipped along the vertical axis
	 * @param flipY if true the resulting image is flipped along the horizontal axis
	 */
	static void blit(int op,
		byte[] srcData, int srcDepth, int srcStride, int srcOrder,
		int srcX, int srcY, int srcWidth, int srcHeight,
		int srcRedMask, int srcGreenMask, int srcBlueMask,
		int alphaMode, byte[] alphaData, int alphaStride, int alphaX, int alphaY,
		byte[] destData, int destDepth, int destStride, int destOrder,
		int destX, int destY, int destWidth, int destHeight,
		int destRedMask, int destGreenMask, int destBlueMask,
		boolean flipX, boolean flipY) {
		if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

		// these should be supplied as params later
		final int srcAlphaMask = 0, destAlphaMask = 0;

		/*** Prepare scaling data ***/
		final int dwm1 = destWidth - 1;
		final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
		final int dhm1 = destHeight - 1;
		final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

		/*** Prepare source-related data ***/
		final int sbpp, stype;
		switch (srcDepth) {
			case 8:
				sbpp = 1;
				stype = TYPE_GENERIC_8;
				break;
			case 16:
				sbpp = 2;
				stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
				break;
			case 24:
				sbpp = 3;
				stype = TYPE_GENERIC_24;
				break;
			case 32:
				sbpp = 4;
				stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
				break;
			default:
				//throw new IllegalArgumentException("Invalid source type");
				return;
		}			
		int spr = srcY * srcStride + srcX * sbpp;

		/*** Prepare destination-related data ***/
		final int dbpp, dtype;
		switch (destDepth) {
			case 8:
				dbpp = 1;
				dtype = TYPE_GENERIC_8;
				break;
			case 16:
				dbpp = 2;
				dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
				break;
			case 24:
				dbpp = 3;
				dtype = TYPE_GENERIC_24;
				break;
			case 32:
				dbpp = 4;
				dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
				break;
			default:
				//throw new IllegalArgumentException("Invalid destination type");
				return;
		}			
		int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
		final int dprxi = (flipX) ? -dbpp : dbpp;
		final int dpryi = (flipY) ? -destStride : destStride;

		/*** Prepare special processing data ***/
		int apr;
		if ((op & BLIT_ALPHA) != 0) {
			switch (alphaMode) {
				case ALPHA_MASK_UNPACKED:
				case ALPHA_CHANNEL_SEPARATE:
					if (alphaData == null) alphaMode = 0x10000;
					apr = alphaY * alphaStride + alphaX;
					break;
				case ALPHA_MASK_PACKED:
					if (alphaData == null) alphaMode = 0x10000;
					alphaStride <<= 3;
					apr = alphaY * alphaStride + alphaX;
					break;
				case ALPHA_MASK_INDEX:
					//throw new IllegalArgumentException("Invalid alpha type");
					return;
				case ALPHA_MASK_RGB:
					if (alphaData == null) alphaMode = 0x10000;
					apr = 0;
					break;
				default:
					alphaMode = (alphaMode << 16) / 255; // prescale
				case ALPHA_CHANNEL_SOURCE:
					apr = 0;
					break;
			}
		} else {
			alphaMode = 0x10000;
			apr = 0;
		}

		/*** Blit ***/
		int dp = dpr;
		int sp = spr;
		if ((alphaMode == 0x10000) && (stype == dtype) &&
			(srcRedMask == destRedMask) && (srcGreenMask == destGreenMask) &&
			(srcBlueMask == destBlueMask) && (srcAlphaMask == destAlphaMask)) {
			/*** Fast blit (straight copy) ***/
			switch (sbpp) {
				case 1:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = srcData[sp];
							sp += (sfx >>> 16);
						}
					}
					break;					
				case 2:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = srcData[sp];
							destData[dp + 1] = srcData[sp + 1];
							sp += (sfx >>> 16) * 2;
						}
					}
					break;
				case 3:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = srcData[sp];
							destData[dp + 1] = srcData[sp + 1];
							destData[dp + 2] = srcData[sp + 2];
							sp += (sfx >>> 16) * 3;
						}
					}
					break;
				case 4:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = srcData[sp];
							destData[dp + 1] = srcData[sp + 1];
							destData[dp + 2] = srcData[sp + 2];
							destData[dp + 3] = srcData[sp + 3];
							sp += (sfx >>> 16) * 4;
						}
					}
					break;
			}
			return;
		}
		/*** Comprehensive blit (apply transformations) ***/
		final int srcRedShift = getChannelShift(srcRedMask);
		final byte[] srcReds = ANY_TO_EIGHT[getChannelWidth(srcRedMask, srcRedShift)];
		final int srcGreenShift = getChannelShift(srcGreenMask);
		final byte[] srcGreens = ANY_TO_EIGHT[getChannelWidth(srcGreenMask, srcGreenShift)];
		final int srcBlueShift = getChannelShift(srcBlueMask);
		final byte[] srcBlues = ANY_TO_EIGHT[getChannelWidth(srcBlueMask, srcBlueShift)];
		final int srcAlphaShift = getChannelShift(srcAlphaMask);
		final byte[] srcAlphas = ANY_TO_EIGHT[getChannelWidth(srcAlphaMask, srcAlphaShift)];

		final int destRedShift = getChannelShift(destRedMask);
		final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
		final byte[] destReds = ANY_TO_EIGHT[destRedWidth];
		final int destRedPreShift = 8 - destRedWidth;
		final int destGreenShift = getChannelShift(destGreenMask);
		final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
		final byte[] destGreens = ANY_TO_EIGHT[destGreenWidth];
		final int destGreenPreShift = 8 - destGreenWidth;
		final int destBlueShift = getChannelShift(destBlueMask);
		final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
		final byte[] destBlues = ANY_TO_EIGHT[destBlueWidth];
		final int destBluePreShift = 8 - destBlueWidth;
		final int destAlphaShift = getChannelShift(destAlphaMask);
		final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
		final byte[] destAlphas = ANY_TO_EIGHT[destAlphaWidth];
		final int destAlphaPreShift = 8 - destAlphaWidth;

		int ap = apr, alpha = alphaMode;
		int r = 0, g = 0, b = 0, a = 0;
		int rq = 0, gq = 0, bq = 0, aq = 0;
		for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
				sp = spr += (sfy >>> 16) * srcStride,
				ap = apr += (sfy >>> 16) * alphaStride,
				sfy = (sfy & 0xffff) + sfyi,
				dp = dpr += dpryi) {
			for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
					dp += dprxi,
					sfx = (sfx & 0xffff) + sfxi) {
				/*** READ NEXT PIXEL ***/
				switch (stype) {
					case TYPE_GENERIC_8: {
						final int data = srcData[sp] & 0xff;
						sp += (sfx >>> 16);
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_MSB: {
						final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
						sp += (sfx >>> 16) * 2;
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_LSB: {
						final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
						sp += (sfx >>> 16) * 2;
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_24: {
						final int data = (( ((srcData[sp] & 0xff) << 8) |
							(srcData[sp + 1] & 0xff)) << 8) |
							(srcData[sp + 2] & 0xff);
						sp += (sfx >>> 16) * 3;
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_MSB: {
						final int data = (( (( ((srcData[sp] & 0xff) << 8) |
							(srcData[sp + 1] & 0xff)) << 8) |
							(srcData[sp + 2] & 0xff)) << 8) |
							(srcData[sp + 3] & 0xff);
						sp += (sfx >>> 16) * 4;
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_LSB: {
						final int data = (( (( ((srcData[sp + 3] & 0xff) << 8) |
							(srcData[sp + 2] & 0xff)) << 8) |
							(srcData[sp + 1] & 0xff)) << 8) |
							(srcData[sp] & 0xff);
						sp += (sfx >>> 16) * 4;
						r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
						g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
						b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
						a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
					} break;
				}

				/*** DO SPECIAL PROCESSING IF REQUIRED ***/
				switch (alphaMode) {
					case ALPHA_CHANNEL_SEPARATE:
						alpha = ((alphaData[ap] & 0xff) << 16) / 255;
						ap += (sfx >> 16);
						break;
					case ALPHA_CHANNEL_SOURCE:
						alpha = (a << 16) / 255;
						break;
					case ALPHA_MASK_UNPACKED:
						alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
						ap += (sfx >> 16);
						break;						
					case ALPHA_MASK_PACKED:
						alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
						ap += (sfx >> 16);
						break;
					case ALPHA_MASK_RGB:
						alpha = 0x10000;
						for (int i = 0; i < alphaData.length; i += 3) {
							if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
								alpha = 0x0000;
								break;
							}
						}
						break;
				}
				if (alpha != 0x10000) {
					if (alpha == 0x0000) continue;
					switch (dtype) {
						case TYPE_GENERIC_8: {
							final int data = destData[dp] & 0xff;
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
						case TYPE_GENERIC_16_MSB: {
							final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
						case TYPE_GENERIC_16_LSB: {
							final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
						case TYPE_GENERIC_24: {
							final int data = (( ((destData[dp] & 0xff) << 8) |
								(destData[dp + 1] & 0xff)) << 8) |
								(destData[dp + 2] & 0xff);
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
						case TYPE_GENERIC_32_MSB: {
							final int data = (( (( ((destData[dp] & 0xff) << 8) |
								(destData[dp + 1] & 0xff)) << 8) |
								(destData[dp + 2] & 0xff)) << 8) |
								(destData[dp + 3] & 0xff);
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
						case TYPE_GENERIC_32_LSB: {
							final int data = (( (( ((destData[dp + 3] & 0xff) << 8) |
								(destData[dp + 2] & 0xff)) << 8) |
								(destData[dp + 1] & 0xff)) << 8) |
								(destData[dp] & 0xff);
							rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
							gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
							bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
							aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
						} break;
					}
					// Perform alpha blending
					a = aq + ((a - aq) * alpha >> 16);
					r = rq + ((r - rq) * alpha >> 16);
					g = gq + ((g - gq) * alpha >> 16);
					b = bq + ((b - bq) * alpha >> 16);
				}

				/*** WRITE NEXT PIXEL ***/
				final int data = 
					(r >>> destRedPreShift << destRedShift) |
					(g >>> destGreenPreShift << destGreenShift) |
					(b >>> destBluePreShift << destBlueShift) |
					(a >>> destAlphaPreShift << destAlphaShift);
				switch (dtype) {
					case TYPE_GENERIC_8: {
						destData[dp] = (byte) data;
					} break;
					case TYPE_GENERIC_16_MSB: {
						destData[dp] = (byte) (data >>> 8);
						destData[dp + 1] = (byte) (data & 0xff);
					} break;
					case TYPE_GENERIC_16_LSB: {
						destData[dp] = (byte) (data & 0xff);
						destData[dp + 1] = (byte) (data >>> 8);
					} break;
					case TYPE_GENERIC_24: {
						destData[dp] = (byte) (data >>> 16);
						destData[dp + 1] = (byte) (data >>> 8);
						destData[dp + 2] = (byte) (data & 0xff);
					} break;
					case TYPE_GENERIC_32_MSB: {
						destData[dp] = (byte) (data >>> 24);
						destData[dp + 1] = (byte) (data >>> 16);
						destData[dp + 2] = (byte) (data >>> 8);
						destData[dp + 3] = (byte) (data & 0xff);
					} break;
					case TYPE_GENERIC_32_LSB: {
						destData[dp] = (byte) (data & 0xff);
						destData[dp + 1] = (byte) (data >>> 8);
						destData[dp + 2] = (byte) (data >>> 16);
						destData[dp + 3] = (byte) (data >>> 24);
					} break;
				}
			}
		}			
	}

	/**
	 * Computes the required channel shift from a mask.
	 */
	static int getChannelShift(int mask) {
		if (mask == 0) return 0;
		int i;
		for (i = 0; ((mask & 1) == 0) && (i < 32); ++i) {
			mask >>>= 1;
		}
		return i;
	}
	
	/**
	 * Computes the required channel width (depth) from a mask.
	 */
	static int getChannelWidth(int mask, int shift) {
		if (mask == 0) return 0;
		int i;
		mask >>>= shift;
		for (i = shift; ((mask & 1) != 0) && (i < 32); ++i) {
			mask >>>= 1;
		}
		return i - shift;
	}

	public static ImageData convertImageData (ImageData source) {
		PaletteData palette = new PaletteData (0xff0000, 0xff00, 0xff);
		ImageData newSource = new ImageData (source.width, source.height, 24, palette);

		ImageDataUtil.blit (
			1,
			source.data,
			source.depth,
			source.bytesPerLine,
			(source.depth != 16) ? MSB_FIRST : LSB_FIRST,
			0,
			0,
			source.width,
			source.height,
			source.palette.redMask,
			source.palette.greenMask,
			source.palette.blueMask,
			255,
			null,
			0,
			0,
			0,
			newSource.data,
			newSource.depth,
			newSource.bytesPerLine,
			(newSource.depth != 16) ? MSB_FIRST : LSB_FIRST,
			0,
			0,
			newSource.width,
			newSource.height,
			newSource.palette.redMask,
			newSource.palette.greenMask,
			newSource.palette.blueMask,
			false,
			true);

		return newSource;
	}
}
