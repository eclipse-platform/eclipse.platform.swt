// SPDX-License-Identifier: EPL-2.0
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

/**
 * Smoke runner for the m3/integration-20261013 builds. Runs an unmodified example's {@code main} on the
 * main thread. A watcher waits for the example's Display, lets painting settle, then on the UI thread
 * prints every open shell with {@code Control.print} (and every tab of a TabFolder/CTabFolder, when the
 * shell has one), hashes the printed pixels (SHA-256 of ImageData.data), saves a PNG, and closes the
 * shells so {@code main} returns.
 *
 * <p>usage: {@code IntegrationSmoke <out.tsv> <png-dir> <settle-ms> <main-class> [args...]}
 *
 * <p>Row status: PASS (printed, no exception), PRINT_FALSE (Control.print returned false), FAIL (an
 * exception reached the example's loop or the runner). Exit code 0 when every row is PASS, else 1.
 */
public final class IntegrationSmoke {

	private final File out;
	private final File pngDir;
	private final int settle;
	private final String example;
	private final List<String> rows = new ArrayList<>();
	private final AtomicInteger uiExceptions = new AtomicInteger();
	private volatile boolean done;

	private IntegrationSmoke(File out, File pngDir, int settle, String example) {
		this.out = out;
		this.pngDir = pngDir;
		this.settle = settle;
		this.example = example;
	}

	public static void main(String[] args) throws Exception {
		IntegrationSmoke smoke = new IntegrationSmoke(new File(args[0]), new File(args[1]), Integer.parseInt(args[2]), args[3]);
		String[] rest = new String[args.length - 4];
		System.arraycopy(args, 4, rest, 0, rest.length);
		System.exit(smoke.run(rest));
	}

	private int run(String[] mainArgs) throws IOException {
		pngDir.mkdirs();
		Thread ui = Thread.currentThread();
		Thread watcher = new Thread(() -> watch(ui), "smoke-watcher");
		watcher.setDaemon(true);
		watcher.start();
		long t0 = System.nanoTime();
		String failure = null;
		try {
			Method main = Class.forName(example).getMethod("main", String[].class);
			main.invoke(null, (Object) mainArgs);
		} catch (java.lang.reflect.InvocationTargetException e) {
			failure = String.valueOf(e.getCause());
		} catch (ReflectiveOperationException | RuntimeException e) {
			failure = String.valueOf(e);
		}
		long ms = (System.nanoTime() - t0) / 1_000_000;
		if (failure != null || !done) {
			rows.add(row(example, "-", "FAIL", "-", "-", ms, failure == null ? "main returned before capture" : failure));
		}
		boolean header = !out.exists();
		try (PrintWriter w = new PrintWriter(new FileWriter(out, true))) {
			if (header) {
				w.println("example\titem\tstatus\tprint_sha256\tsize\tmillis\tui_exceptions\tdetail");
			}
			for (String r : rows) {
				w.println(r.replace("@EXC@", Integer.toString(uiExceptions.get())));
			}
		}
		return rows.stream().allMatch(r -> r.split("\t")[2].equals("PASS")) && uiExceptions.get() == 0 ? 0 : 1;
	}

	private void watch(Thread ui) {
		Display display = null;
		for (int i = 0; i < 600 && display == null; i++) {
			display = Display.findDisplay(ui);
			if (display == null) {
				sleep(100);
			}
		}
		if (display == null) {
			return;
		}
		Display d = display;
		sleep(settle);
		d.asyncExec(() -> {
			d.setRuntimeExceptionHandler(e -> { uiExceptions.incrementAndGet(); e.printStackTrace(); });
			capture(d);
		});
	}

	private void capture(Display display) {
		for (Shell shell : display.getShells()) {
			if (shell.isDisposed() || !shell.isVisible()) {
				continue;
			}
			String title = shell.getText().isEmpty() ? "shell" : shell.getText();
			Control folder = findFolder(shell);
			if (folder instanceof TabFolder tf) {
				for (int i = 0; i < tf.getItemCount(); i++) {
					tf.setSelection(i);
					tf.notifyListeners(SWT.Selection, null);
					printShell(display, shell, title + " / " + tf.getItem(i).getText());
				}
			} else if (folder instanceof CTabFolder cf) {
				for (int i = 0; i < cf.getItemCount(); i++) {
					cf.setSelection(i);
					cf.notifyListeners(SWT.Selection, null);
					printShell(display, shell, title + " / " + cf.getItem(i).getText());
				}
			} else {
				printShell(display, shell, title);
			}
		}
		done = true;
		for (Shell shell : display.getShells()) {
			shell.dispose();
		}
	}

	private void printShell(Display display, Shell shell, String item) {
		long t0 = System.nanoTime();
		spin(display, 300);
		Point size = shell.getSize();
		Image image = new Image(display, Math.max(1, size.x), Math.max(1, size.y));
		GC gc = new GC(image);
		boolean printed;
		try {
			printed = shell.print(gc);
		} finally {
			gc.dispose();
		}
		ImageData data = image.getImageData();
		image.dispose();
		String hash = sha256(data.data);
		String file = example.substring(example.lastIndexOf('.') + 1) + "-" + rows.size() + ".png";
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { data };
		loader.save(new File(pngDir, file).getPath(), SWT.IMAGE_PNG);
		long ms = (System.nanoTime() - t0) / 1_000_000;
		rows.add(row(example, item.replace('\t', ' '), printed ? "PASS" : "PRINT_FALSE", hash, size.x + "x" + size.y, ms, file));
	}

	private static Control findFolder(Composite parent) {
		for (Control c : parent.getChildren()) {
			if (c instanceof TabFolder || c instanceof CTabFolder) {
				return c;
			}
			if (c instanceof Composite comp) {
				Control found = findFolder(comp);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private static void spin(Display display, int ms) {
		long end = System.currentTimeMillis() + ms;
		while (System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) {
				sleep(5);
			}
		}
	}

	private static String row(String example, String item, String status, String hash, String size, long ms, String detail) {
		return String.join("\t", example, item, status, hash, size, Long.toString(ms), "@EXC@", detail.replace('\t', ' ').replace('\n', ' '));
	}

	private static String sha256(byte[] bytes) {
		try {
			StringBuilder sb = new StringBuilder();
			for (byte b : MessageDigest.getInstance("SHA-256").digest(bytes)) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
