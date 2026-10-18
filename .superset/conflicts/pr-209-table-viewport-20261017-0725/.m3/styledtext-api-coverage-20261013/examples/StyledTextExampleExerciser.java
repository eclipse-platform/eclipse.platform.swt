/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Runs an SWT example or snippet unmodified (its own main, its own event loop)
 * and, once its shell shows a StyledText, appends a 1,000,000-line document to
 * every multi-line StyledText and drives viewport operations on it, counting
 * the line layouts each operation requests. For CustomControlExample it selects
 * the StyledText tab and toggles each style checkbox, exercising every style
 * combination the tab offers. One TSV row per example widget configuration.
 *
 * <p>usage: StyledTextExampleExerciser &lt;out.tsv&gt; &lt;mainClass&gt; [args...]</p>
 */
public final class StyledTextExampleExerciser {
	static final int LINES = Integer.getInteger("exerciser.lines", 1_000_000);
	static final int BOUND = 4096;
	static Path out;
	static String example;

	public static void main(String[] args) throws Exception {
		out = Path.of(args[0]);
		example = args[1];
		String[] rest = new String[args.length - 2];
		System.arraycopy(args, 2, rest, 0, rest.length);
		Thread main = Thread.currentThread();
		Thread watcher = new Thread(() -> watch(main), "exerciser");
		watcher.setDaemon(true);
		watcher.start();
		Class<?> type = Class.forName(example);
		Method entry = type.getMethod("main", String[].class);
		entry.invoke(null, (Object) rest);
	}

	static void watch(Thread main) {
		long deadline = System.currentTimeMillis() + 60_000;
		Display display = null;
		while (display == null && System.currentTimeMillis() < deadline) {
			display = Display.findDisplay(main);
			sleep(100);
		}
		if (display == null) {
			row("no-display", "", 0, 0, 0, "NO_DISPLAY");
			System.exit(2);
		}
		Display d = display;
		boolean[] done = { false };
		while (!done[0] && System.currentTimeMillis() < deadline) {
			sleep(300);
			d.syncExec(() -> {
				boolean tabbed = example.endsWith("CustomControlExample");
				boolean ready = tabbed ? d.getShells().length > 0 && d.getShells()[0].isVisible() : !all(d).isEmpty();
				if (!done[0] && ready) {
					done[0] = true;
					try {
						exercise(d);
					} catch (Throwable t) {
						row("exercise", "", 0, 0, 0, "ERROR " + t);
						t.printStackTrace();
					} finally {
						for (Shell shell : d.getShells()) {
							shell.dispose();
						}
					}
				}
			});
		}
		if (!done[0]) {
			row("no-styledtext", "", 0, 0, 0, "NO_STYLEDTEXT_WITHIN_60S");
			d.asyncExec(() -> {
				for (Shell shell : d.getShells()) {
					shell.dispose();
				}
			});
		}
		sleep(3000);
		System.exit(0);
	}

	static void exercise(Display d) {
		if (example.endsWith("CustomControlExample")) {
			exerciseControlExampleTab(d);
			return;
		}
		int index = 0;
		for (StyledText text : all(d)) {
			drive(text, "widget" + index++);
		}
	}

	/** Selects the StyledText tab and toggles every style checkbox of its style group, one at a time. */
	static void exerciseControlExampleTab(Display d) {
		for (Shell shell : d.getShells()) {
			for (Control c : descendants(shell)) {
				if (c instanceof TabFolder folder) {
					for (TabItem item : folder.getItems()) {
						if (item.getText().contains("StyledText")) {
							folder.setSelection(item);
							Event e = new Event();
							e.item = item;
							folder.notifyListeners(SWT.Selection, e);
						}
					}
				}
				if (c instanceof CTabFolder folder) {
					for (CTabItem item : folder.getItems()) {
						if (item.getText().contains("StyledText")) {
							folder.setSelection(item);
						}
					}
				}
			}
		}
		pump(d, 500);
		List<Button> styles = new ArrayList<>();
		for (Shell shell : d.getShells()) {
			for (Control c : descendants(shell)) {
				if (c instanceof Button b && (b.getStyle() & SWT.CHECK) != 0 && b.isVisible() && b.getText().startsWith("SWT.")) {
					styles.add(b);
				}
			}
		}
		drive(first(d), "tab-default");
		for (Button style : styles) {
			if (style.isDisposed()) {
				continue;
			}
			style.setSelection(!style.getSelection());
			style.notifyListeners(SWT.Selection, new Event());
			pump(d, 300);
			StyledText text = first(d);
			if (text != null) {
				drive(text, "toggle " + style.getText() + "=" + style.getSelection() + " style=0x" + Integer.toHexString(text.getStyle()));
			}
			if (!style.isDisposed()) {
				style.setSelection(!style.getSelection());
				style.notifyListeners(SWT.Selection, new Event());
				pump(d, 200);
			}
		}
	}

	static StyledText first(Display d) {
		List<StyledText> list = all(d);
		return list.isEmpty() ? null : list.get(0);
	}

	static void drive(StyledText text, String label) {
		if (text == null) {
			return;
		}
		if ((text.getStyle() & SWT.SINGLE) != 0) {
			row(label, "SINGLE", 0, 0, 0, "SKIPPED_SINGLE_LINE");
			return;
		}
		long totalStart = System.nanoTime();
		long[] max = { 0 };
		List<String> failures = new ArrayList<>();
		Consumer<String> fail = failures::add;
		StringBuilder b = new StringBuilder(LINES * 34);
		for (int i = 0; i < LINES; i++) {
			b.append("exerciser line ").append(i).append(" alpha beta\tgamma").append('\n');
		}
		op(text, "append-1M", t -> t.append(b.toString()), max, fail);
		op(text, "setTopIndex-mid", t -> t.setTopIndex(t.getLineCount() / 2), max, fail);
		op(text, "setTopIndex-end", t -> t.setTopIndex(t.getLineCount() - 1), max, fail);
		op(text, "caret-end", t -> { t.setSelection(t.getCharCount()); t.showSelection(); }, max, fail);
		op(text, "page-up", t -> t.invokeAction(ST.PAGE_UP), max, fail);
		op(text, "select-all", t -> t.invokeAction(ST.SELECT_ALL), max, fail);
		op(text, "collapse", t -> t.setSelection(t.getOffsetAtLine(t.getLineCount() / 3)), max, fail);
		op(text, "type", t -> { Event e = new Event(); e.character = 'x'; e.keyCode = 'x'; t.notifyListeners(SWT.KeyDown, e); }, max, fail);
		op(text, "wrap-toggle", t -> t.setWordWrap(!t.getWordWrap()), max, fail);
		op(text, "far-jump", t -> t.setTopIndex(t.getLineCount() * 3 / 4), max, fail);
		op(text, "wrap-restore", t -> t.setWordWrap(!t.getWordWrap()), max, fail);
		op(text, "computeSize", t -> t.computeSize(SWT.DEFAULT, SWT.DEFAULT), max, fail);
		op(text, "top", t -> t.setTopIndex(0), max, fail);
		long millis = (System.nanoTime() - totalStart) / 1_000_000;
		row(label, "style=0x" + Integer.toHexString(text.getStyle()) + " content=" + text.getContent().getClass().getSimpleName()
				+ " clientHeight=" + text.getClientArea().height + " displayHeight=" + text.getDisplay().getClientArea().height,
				text.getLineCount(), max[0], millis, failures.isEmpty() ? "PASSED" : "FAILED " + String.join("; ", failures));
	}

	static void op(StyledText text, String name, Consumer<StyledText> action, long[] max, Consumer<String> fail) {
		if (text.isDisposed()) {
			fail.accept(name + ":disposed");
			return;
		}
		long before = layouts(text);
		Thread ui = Thread.currentThread();
		java.util.Map<String, Integer> hot = new java.util.TreeMap<>();
		boolean[] sampling = { Boolean.getBoolean("exerciser.sample") };
		Thread sampler = null;
		if (sampling[0]) {
			sampler = new Thread(() -> {
				while (sampling[0]) {
					StackTraceElement[] st = ui.getStackTrace();
					StringBuilder key = new StringBuilder();
					int n = 0;
					for (StackTraceElement e : st) {
						if (e.getClassName().startsWith("org.eclipse.swt.custom.") && n < 6) {
							key.append(e.getMethodName()).append(':').append(e.getLineNumber()).append(" < ");
							n++;
						}
					}
					if (n > 0) {
						synchronized (hot) {
							hot.merge(key.toString(), 1, Integer::sum);
						}
					}
					sleep(5);
				}
			});
			sampler.setDaemon(true);
			sampler.start();
		}
		try {
			action.accept(text);
			text.update();
			pump(text.getDisplay(), 100);
		} catch (RuntimeException e) {
			fail.accept(name + ":" + e);
			return;
		}
		long n = layouts(text) - before;
		sampling[0] = false;
		if (sampler != null && n > BOUND) {
			synchronized (hot) {
				hot.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).limit(5)
						.forEach(e -> System.out.println("HOT " + name + " " + e.getValue() + " " + e.getKey()));
			}
		}
		max[0] = Math.max(max[0], n);
		if (n > BOUND) {
			fail.accept(name + " laid out " + n + " lines");
		}
	}

	static long layouts(StyledText t) {
		try {
			Field r = StyledText.class.getDeclaredField("renderer");
			r.setAccessible(true);
			Object renderer = r.get(t);
			Field f = renderer.getClass().getDeclaredField("layoutRequests");
			f.setAccessible(true);
			return f.getLong(renderer);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	static void pump(Display d, long millis) {
		long end = System.currentTimeMillis() + millis;
		while (System.currentTimeMillis() < end) {
			if (!d.readAndDispatch()) {
				sleep(5);
			}
		}
	}

	static List<StyledText> all(Display d) {
		List<StyledText> list = new ArrayList<>();
		for (Shell shell : d.getShells()) {
			for (Control c : descendants(shell)) {
				if (c instanceof StyledText s && !s.isDisposed() && s.isVisible()) {
					list.add(s);
				}
			}
		}
		return list;
	}

	static List<Control> descendants(Composite root) {
		List<Control> out = new ArrayList<>();
		for (Control c : root.getChildren()) {
			out.add(c);
			if (c instanceof Composite comp) {
				out.addAll(descendants(comp));
			}
		}
		return out;
	}

	static synchronized void row(String label, String detail, int lines, long maxLayouts, long millis, String status) {
		String line = String.join("\t", example, label, detail, Integer.toString(lines), Long.toString(maxLayouts), Long.toString(millis), status) + "\n";
		try {
			Files.writeString(out, line, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		System.out.print(line);
	}

	static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
