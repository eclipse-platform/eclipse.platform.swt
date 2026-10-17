/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/

/*
 * Direct2D shim of the gpu drawn planes (docs/m3/RENDER_MODES.md, leaf R5). SWT's own natives cannot create a
 * Direct2D factory (COM.VtblCall passes the object first, CallWindowProc truncates its second argument), so this
 * library does the few things Java cannot: create the factories and a hardware DC render target, keep fonts and
 * bitmaps, and replay one frame of packed int operations (org.eclipse.swt.internal.render.Direct2D) in one call.
 * Everything else (layout, damage, caches of what to draw) stays in Java.
 */
#include <jni.h>
#include <windows.h>
#include <d2d1.h>
#include <dwrite.h>
#include <vector>

namespace {

enum { OP_FILL = 1, OP_TEXT = 2, OP_BITMAP = 3, OP_POLYGON = 4, OP_FOCUS = 5 };
enum { KIND_FONT = 0, KIND_BITMAP = 1 };

struct Bitmap {
	std::vector<BYTE> pixels;
	UINT32 width = 0, height = 0;
	ID2D1Bitmap *device = NULL;
	bool used = false;
};

struct Context {
	ID2D1Factory *factory = NULL;
	IDWriteFactory *write = NULL;
	ID2D1DCRenderTarget *target = NULL;
	ID2D1SolidColorBrush *brush = NULL;
	bool hardware = true;
	std::vector<IDWriteTextFormat *> fonts;
	std::vector<Bitmap> bitmaps;
};

template <class T> void release (T *&object) {
	if (object) object->Release ();
	object = NULL;
}

D2D1_COLOR_F color (jint rgb) {
	return D2D1::ColorF (((rgb >> 16) & 0xFF) / 255.0f, ((rgb >> 8) & 0xFF) / 255.0f, (rgb & 0xFF) / 255.0f, 1.0f);
}

D2D1_RECT_F rect (const jint *op) {
	return D2D1::RectF ((FLOAT) op [0], (FLOAT) op [1], (FLOAT) (op [0] + op [2]), (FLOAT) (op [1] + op [3]));
}

/* the device-dependent objects of the render target; recreated after D2DERR_RECREATE_TARGET */
void dropTarget (Context *c) {
	for (Bitmap &b : c->bitmaps) release (b.device);
	release (c->brush);
	release (c->target);
}

HRESULT ensureTarget (Context *c) {
	if (c->target) return S_OK;
	D2D1_RENDER_TARGET_PROPERTIES props = D2D1::RenderTargetProperties (
		c->hardware ? D2D1_RENDER_TARGET_TYPE_HARDWARE : D2D1_RENDER_TARGET_TYPE_DEFAULT,
		D2D1::PixelFormat (DXGI_FORMAT_B8G8R8A8_UNORM, D2D1_ALPHA_MODE_IGNORE), 96.0f, 96.0f,
		D2D1_RENDER_TARGET_USAGE_NONE, D2D1_FEATURE_LEVEL_DEFAULT);
	HRESULT hr = c->factory->CreateDCRenderTarget (&props, &c->target);
	if (FAILED (hr) && c->hardware) {
		c->hardware = false;
		props.type = D2D1_RENDER_TARGET_TYPE_DEFAULT;
		hr = c->factory->CreateDCRenderTarget (&props, &c->target);
	}
	if (FAILED (hr)) return hr;
	c->target->SetAntialiasMode (D2D1_ANTIALIAS_MODE_ALIASED);
	c->target->SetTextAntialiasMode (D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE);
	return c->target->CreateSolidColorBrush (D2D1::ColorF (0, 0, 0, 1), &c->brush);
}

ID2D1Bitmap *deviceBitmap (Context *c, jint id) {
	if (id < 0 || id >= (jint) c->bitmaps.size () || !c->bitmaps [id].used) return NULL;
	Bitmap &b = c->bitmaps [id];
	if (!b.device) {
		D2D1_BITMAP_PROPERTIES props = D2D1::BitmapProperties (D2D1::PixelFormat (DXGI_FORMAT_B8G8R8A8_UNORM, D2D1_ALPHA_MODE_PREMULTIPLIED), 96.0f, 96.0f);
		c->target->CreateBitmap (D2D1::SizeU (b.width, b.height), b.pixels.data (), b.width * 4, &props, &b.device);
	}
	return b.device;
}

void text (Context *c, const jint *op, const jchar *chars) {
	jint font = op [5];
	if (font < 0 || font >= (jint) c->fonts.size () || !c->fonts [font]) return;
	IDWriteTextFormat *format = c->fonts [font];
	jint align = op [6];
	format->SetTextAlignment (align == 1 ? DWRITE_TEXT_ALIGNMENT_CENTER : align == 2 ? DWRITE_TEXT_ALIGNMENT_TRAILING : DWRITE_TEXT_ALIGNMENT_LEADING);
	c->brush->SetColor (color (op [4]));
	c->target->DrawText ((const WCHAR *) chars + op [7], (UINT32) op [8], format, rect (op), c->brush, D2D1_DRAW_TEXT_OPTIONS_CLIP, DWRITE_MEASURING_MODE_GDI_NATURAL);
}

void polygon (Context *c, const jint *op) {
	jint count = op [1];
	if (count < 3) return;
	ID2D1PathGeometry *path = NULL;
	if (FAILED (c->factory->CreatePathGeometry (&path))) return;
	ID2D1GeometrySink *sink = NULL;
	if (SUCCEEDED (path->Open (&sink))) {
		sink->BeginFigure (D2D1::Point2F ((FLOAT) op [2], (FLOAT) op [3]), D2D1_FIGURE_BEGIN_FILLED);
		for (jint i = 1; i < count; i++) sink->AddLine (D2D1::Point2F ((FLOAT) op [2 + 2 * i], (FLOAT) op [3 + 2 * i]));
		sink->EndFigure (D2D1_FIGURE_END_CLOSED);
		sink->Close ();
		release (sink);
		c->brush->SetColor (color (op [0]));
		c->target->SetAntialiasMode (D2D1_ANTIALIAS_MODE_PER_PRIMITIVE);
		c->target->FillGeometry (path, c->brush);
		c->target->SetAntialiasMode (D2D1_ANTIALIAS_MODE_ALIASED);
	}
	release (path);
}

/* a dotted focus rectangle: every other pixel of the border, as DrawFocusRect */
void focus (Context *c, const jint *op) {
	c->brush->SetColor (D2D1::ColorF (0, 0, 0, 1));
	jint x = op [0], y = op [1], w = op [2], h = op [3];
	for (jint i = 0; i < w; i += 2) {
		c->target->FillRectangle (D2D1::RectF ((FLOAT) (x + i), (FLOAT) y, (FLOAT) (x + i + 1), (FLOAT) (y + 1)), c->brush);
		c->target->FillRectangle (D2D1::RectF ((FLOAT) (x + i), (FLOAT) (y + h - 1), (FLOAT) (x + i + 1), (FLOAT) (y + h)), c->brush);
	}
	for (jint i = 0; i < h; i += 2) {
		c->target->FillRectangle (D2D1::RectF ((FLOAT) x, (FLOAT) (y + i), (FLOAT) (x + 1), (FLOAT) (y + i + 1)), c->brush);
		c->target->FillRectangle (D2D1::RectF ((FLOAT) (x + w - 1), (FLOAT) (y + i), (FLOAT) (x + w), (FLOAT) (y + i + 1)), c->brush);
	}
}

/* operand counts after the opcode; a polygon has 2 + 2 * count */
jint operands (const jint *ops, jint at) {
	switch (ops [at]) {
		case OP_FILL: return 5;
		case OP_TEXT: return 9;
		case OP_BITMAP: return 5;
		case OP_POLYGON: return 2 + 2 * ops [at + 2];
		case OP_FOCUS: return 4;
		default: return -1;
	}
}

}

extern "C" {

JNIEXPORT jlong JNICALL Java_org_eclipse_swt_internal_render_Direct2D_create (JNIEnv *, jclass, jint hardware) {
	Context *c = new Context ();
	c->hardware = hardware != 0;
	if (FAILED (D2D1CreateFactory (D2D1_FACTORY_TYPE_SINGLE_THREADED, __uuidof (ID2D1Factory), NULL, (void **) &c->factory))
		|| FAILED (DWriteCreateFactory (DWRITE_FACTORY_TYPE_SHARED, __uuidof (IDWriteFactory), (IUnknown **) &c->write))) {
		release (c->write);
		release (c->factory);
		delete c;
		return 0;
	}
	return (jlong) c;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_render_Direct2D_font (JNIEnv *env, jclass, jlong context, jcharArray face, jfloat size, jint weight, jint italic) {
	Context *c = (Context *) context;
	jsize length = env->GetArrayLength (face);
	std::vector<WCHAR> name (length + 1, 0);
	env->GetCharArrayRegion (face, 0, length, (jchar *) name.data ());
	IDWriteTextFormat *format = NULL;
	HRESULT hr = c->write->CreateTextFormat (name.data (), NULL, (DWRITE_FONT_WEIGHT) (weight == 0 ? 400 : weight),
		italic ? DWRITE_FONT_STYLE_ITALIC : DWRITE_FONT_STYLE_NORMAL, DWRITE_FONT_STRETCH_NORMAL, size, L"", &format);
	if (FAILED (hr)) return -1;
	format->SetWordWrapping (DWRITE_WORD_WRAPPING_NO_WRAP);
	format->SetParagraphAlignment (DWRITE_PARAGRAPH_ALIGNMENT_CENTER);
	for (size_t i = 0; i < c->fonts.size (); i++) {
		if (!c->fonts [i]) {
			c->fonts [i] = format;
			return (jint) i;
		}
	}
	c->fonts.push_back (format);
	return (jint) c->fonts.size () - 1;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_render_Direct2D_bitmap (JNIEnv *env, jclass, jlong context, jbyteArray bgra, jint width, jint height) {
	Context *c = (Context *) context;
	size_t slot = c->bitmaps.size ();
	for (size_t i = 0; i < c->bitmaps.size (); i++) {
		if (!c->bitmaps [i].used) {
			slot = i;
			break;
		}
	}
	if (slot == c->bitmaps.size ()) c->bitmaps.emplace_back ();
	Bitmap &b = c->bitmaps [slot];
	b.width = (UINT32) width;
	b.height = (UINT32) height;
	b.pixels.resize ((size_t) width * height * 4);
	env->GetByteArrayRegion (bgra, 0, width * height * 4, (jbyte *) b.pixels.data ());
	b.used = true;
	return (jint) slot;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_render_Direct2D_free (JNIEnv *, jclass, jlong context, jint kind, jint id) {
	Context *c = (Context *) context;
	if (kind == KIND_FONT && id >= 0 && id < (jint) c->fonts.size ()) {
		release (c->fonts [id]);
	} else if (kind == KIND_BITMAP && id >= 0 && id < (jint) c->bitmaps.size ()) {
		Bitmap &b = c->bitmaps [id];
		release (b.device);
		std::vector<BYTE> ().swap (b.pixels);
		b.used = false;
	}
}

/*
 * Replays one frame: binds the render target to the rectangle (x, y, width, height) of the device context, so the
 * operations, which are in the device context's coordinates, are translated by (-x, -y). Answers the HRESULT of
 * EndDraw (or of the first failing step).
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_render_Direct2D_frame (JNIEnv *env, jclass, jlong context, jlong hdc,
		jint x, jint y, jint width, jint height, jintArray opsArray, jint length, jcharArray textArray) {
	Context *c = (Context *) context;
	HRESULT hr = ensureTarget (c);
	if (FAILED (hr)) return hr;
	RECT bounds = {x, y, x + width, y + height};
	hr = c->target->BindDC ((HDC) hdc, &bounds);
	if (FAILED (hr)) return hr;
	jint *ops = (jint *) env->GetPrimitiveArrayCritical (opsArray, NULL);
	jchar *chars = (jchar *) env->GetPrimitiveArrayCritical (textArray, NULL);
	c->target->BeginDraw ();
	c->target->SetTransform (D2D1::Matrix3x2F::Translation ((FLOAT) -x, (FLOAT) -y));
	for (jint at = 0; at < length;) {
		jint count = operands (ops, at);
		if (count < 0) break;
		const jint *op = ops + at + 1;
		switch (ops [at]) {
			case OP_FILL:
				c->brush->SetColor (color (op [4]));
				c->target->FillRectangle (rect (op), c->brush);
				break;
			case OP_TEXT:
				text (c, op, chars);
				break;
			case OP_BITMAP: {
				ID2D1Bitmap *bitmap = deviceBitmap (c, op [4]);
				if (bitmap) c->target->DrawBitmap (bitmap, rect (op), 1.0f, D2D1_BITMAP_INTERPOLATION_MODE_NEAREST_NEIGHBOR, NULL);
				break;
			}
			case OP_POLYGON:
				polygon (c, op);
				break;
			case OP_FOCUS:
				focus (c, op);
				break;
		}
		at += 1 + count;
	}
	hr = c->target->EndDraw ();
	env->ReleasePrimitiveArrayCritical (textArray, chars, JNI_ABORT);
	env->ReleasePrimitiveArrayCritical (opsArray, ops, JNI_ABORT);
	if (hr == (HRESULT) D2DERR_RECREATE_TARGET) dropTarget (c);
	return hr;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_render_Direct2D_hardware (JNIEnv *, jclass, jlong context) {
	return ((Context *) context)->hardware ? 1 : 0;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_render_Direct2D_dispose (JNIEnv *, jclass, jlong context) {
	Context *c = (Context *) context;
	if (!c) return;
	dropTarget (c);
	for (IDWriteTextFormat *&format : c->fonts) release (format);
	release (c->write);
	release (c->factory);
	delete c;
}

}
