/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * SWT OpenGL snippet: use LWJGL to draw to an SWT GLCanvas
 *
 * The drawing uses shaders and vertex buffers instead of the fixed function
 * pipeline, so it runs both with a legacy OpenGL 2.1 context (macOS) and
 * with a core profile context (Wayland, GTK4).
 *
 * For a list of all SWT example snippets see
 * https://eclipse.dev/eclipse/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;
import org.lwjgl.opengl.*;

public class Snippet195 {
	static final String VERTEX_SHADER = """
			uniform mat4 mvp;
			IN vec3 position;
			void main() {
				gl_Position = mvp * vec4(position, 1.0);
			}
			""";
	static final String FRAGMENT_SHADER = """
			uniform vec3 color;
			void main() {
				FRAG_COLOR = vec4(color, 1.0);
			}
			""";

	/* GLSL 1.50 for OpenGL 3.2 and later, which includes core profiles, GLSL 1.20 for OpenGL 2.1 */
	static final String VERTEX_HEADER_150 = """
			#version 150
			#define IN in
			""";
	static final String VERTEX_HEADER_120 = """
			#version 120
			#define IN attribute
			""";
	static final String FRAGMENT_HEADER_150 = """
			#version 150
			out vec4 fragColor;
			#define FRAG_COLOR fragColor
			""";
	static final String FRAGMENT_HEADER_120 = """
			#version 120
			#define FRAG_COLOR gl_FragColor
			""";

	static int program, mvpLocation, colorLocation, vertexArray, buffer;
	static float[] projection = identity();

	static int compile(int type, String source, boolean glsl150) {
		String header;
		if (type == GL30.GL_VERTEX_SHADER) {
			header = glsl150 ? VERTEX_HEADER_150 : VERTEX_HEADER_120;
		} else {
			header = glsl150 ? FRAGMENT_HEADER_150 : FRAGMENT_HEADER_120;
		}
		int shader = GL30.glCreateShader(type);
		GL30.glShaderSource(shader, header + source);
		GL30.glCompileShader(shader);
		if (GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			throw new IllegalStateException(GL30.glGetShaderInfoLog(shader));
		}
		return shader;
	}

	static void init(GLCapabilities caps) {
		program = GL30.glCreateProgram();
		GL30.glAttachShader(program, compile(GL30.GL_VERTEX_SHADER, VERTEX_SHADER, caps.OpenGL32));
		GL30.glAttachShader(program, compile(GL30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER, caps.OpenGL32));
		GL30.glBindAttribLocation(program, 0, "position");
		GL30.glLinkProgram(program);
		if (GL30.glGetProgrami(program, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			throw new IllegalStateException(GL30.glGetProgramInfoLog(program));
		}
		mvpLocation = GL30.glGetUniformLocation(program, "mvp");
		colorLocation = GL30.glGetUniformLocation(program, "color");

		/* A core profile only draws with a vertex array object bound */
		if (caps.OpenGL30) {
			vertexArray = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vertexArray);
		}
		buffer = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, buffer);
		GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(0);
	}

	/* The edges of the torus as line segments, the outlines of the quads of the original quad strips */
	static float[] torus(float r, float R, int nsides, int rings) {
		float[] vertices = new float[rings * nsides * 4 * 3];
		int index = 0;
		for (int i = 0; i < rings; i++) {
			for (int j = 0; j < nsides; j++) {
				float[] p = torusPoint(r, R, i, j, nsides, rings);
				float[] nextRing = torusPoint(r, R, i + 1, j, nsides, rings);
				float[] nextSide = torusPoint(r, R, i, j + 1, nsides, rings);
				for (float[] point : new float[][] {p, nextRing, p, nextSide}) {
					System.arraycopy(point, 0, vertices, index, 3);
					index += 3;
				}
			}
		}
		return vertices;
	}

	static float[] torusPoint(float r, float R, int ring, int side, int nsides, int rings) {
		float theta = 2.0f * (float) Math.PI * ring / rings;
		float phi = 2.0f * (float) Math.PI * side / nsides;
		float dist = R + r * (float) Math.cos(phi);
		return new float[] {(float) Math.cos(theta) * dist, -(float) Math.sin(theta) * dist, r * (float) Math.sin(phi)};
	}

	/* Matrices are column major, as OpenGL expects them */
	static float[] identity() {
		return new float[] {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
	}

	static float[] multiply(float[] a, float[] b) {
		float[] result = new float[16];
		for (int column = 0; column < 4; column++) {
			for (int row = 0; row < 4; row++) {
				for (int k = 0; k < 4; k++) {
					result[column * 4 + row] += a[k * 4 + row] * b[column * 4 + k];
				}
			}
		}
		return result;
	}

	/* Same as glFrustum */
	static float[] frustum(float left, float right, float bottom, float top, float near, float far) {
		return new float[] {
			2 * near / (right - left), 0, 0, 0,
			0, 2 * near / (top - bottom), 0, 0,
			(right + left) / (right - left), (top + bottom) / (top - bottom), -(far + near) / (far - near), -1,
			0, 0, -2 * far * near / (far - near), 0};
	}

	/* Same as glTranslatef */
	static float[] translate(float x, float y, float z) {
		float[] result = identity();
		result[12] = x;
		result[13] = y;
		result[14] = z;
		return result;
	}

	/* Same as glRotatef */
	static float[] rotate(float angle, float x, float y, float z) {
		float length = (float) Math.sqrt(x * x + y * y + z * z);
		x /= length;
		y /= length;
		z /= length;
		float c = (float) Math.cos(Math.toRadians(angle));
		float s = (float) Math.sin(Math.toRadians(angle));
		float t = 1 - c;
		return new float[] {
			t * x * x + c, t * x * y + s * z, t * x * z - s * y, 0,
			t * x * y - s * z, t * y * y + c, t * y * z + s * x, 0,
			t * x * z + s * y, t * y * z - s * x, t * z * z + c, 0,
			0, 0, 0, 1};
	}

	public static void main(String [] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new FillLayout());
		GLData data = new GLData ();
		data.doubleBuffer = true;
		final GLCanvas canvas = new GLCanvas(comp, SWT.NONE, data);

		canvas.setCurrent();
		GLCapabilities caps = GL.createCapabilities();
		init(caps);

		canvas.addListener(SWT.Resize, event -> {
			Rectangle bounds = canvas.getBounds();
			float fAspect = (float) bounds.width / (float) bounds.height;
			canvas.setCurrent();
			GL.createCapabilities();
			/* On GTK the framebuffer is in pixels, which the monitor zoom converts the bounds to */
			int zoom = "gtk".equals(SWT.getPlatform()) ? canvas.getMonitor().getZoom() : 100;
			GL30.glViewport(0, 0, bounds.width * zoom / 100, bounds.height * zoom / 100);
			float near = 0.5f;
			float bottom = -near * (float) Math.tan(45.f / 2);
			float left = fAspect * bottom;
			projection = frustum(left, -left, bottom, -bottom, near, 400.f);
		});

		GL30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL30.glClearDepth(1.0);
		/* Wide lines are not available in a forward compatible context */
		if (!caps.forwardCompatible) GL30.glLineWidth(2);
		GL30.glEnable(GL30.GL_DEPTH_TEST);

		shell.setText("SWT/LWJGL Example");
		shell.setSize(640, 480);
		shell.open();

		final Runnable run = new Runnable() {
			int rot = 0;
			@Override
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					GL.createCapabilities();
					GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
					GL30.glClearColor(.3f, .5f, .8f, 1.0f);
					float frot = rot;
					float[] modelView = translate(0.0f, 0.0f, -10.0f);
					modelView = multiply(modelView, rotate(0.15f * rot, 2.0f * frot, 10.0f * frot, 1.0f));
					modelView = multiply(modelView, rotate(0.3f * rot, 3.0f * frot, 1.0f * frot, 1.0f));
					rot++;
					GL30.glUseProgram(program);
					GL30.glUniformMatrix4fv(mvpLocation, false, multiply(projection, modelView));
					GL30.glUniform3f(colorLocation, 0.9f, 0.9f, 0.9f);
					float[] vertices = torus(1, 1.9f + ((float) Math.sin((0.004f * frot))), 15, 15);
					if (vertexArray != 0) GL30.glBindVertexArray(vertexArray);
					GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, buffer);
					GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STREAM_DRAW);
					GL30.glDrawArrays(GL30.GL_LINES, 0, vertices.length / 3);
					canvas.swapBuffers();
					display.asyncExec(this);
				}
			}
		};
		canvas.addListener(SWT.Paint, event -> run.run());
		display.asyncExec(run);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
