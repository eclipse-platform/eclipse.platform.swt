package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Title: app crash
 * <p>
 *
 * How to run: run this class as java application.
 * <p>
 *
 * Bug description: when {@link TreeItem#setImage(Image)} is called within an
 * {@link SWT#SetData} event handler for a {@link SWT#VIRTUAL} tree, then a JVM
 * crash can happen because of use-after-free gtk3 renderer in
 * {@code Tree.cellDataProc()}.
 *
 * <pre>
 * Native frames: (J=compiled Java code, j=interpreted, Vv=VM code, C=native code)
 * C  [libgobject-2.0.so.0+0x3b251]  g_type_check_instance_is_fundamentally_a+0x11
 * C  [libswt-pi3-gtk-4958r2.so+0x4b609]  Java_org_eclipse_swt_internal_gtk_OS_g_1object_1set__J_3BJJ+0x4a
 *
 * Java frames: (J=compiled Java code, j=interpreted, Vv=VM code)
 * J 11988  org.eclipse.swt.internal.gtk.OS.g_object_set(J[BJJ)V (0 bytes)
 * J 10921 c1 org.eclipse.swt.widgets.Tree.cellDataProc(JJJJJ)J (486 bytes)
 * J 10920 c1 org.eclipse.swt.widgets.Display.cellDataProc(JJJJJ)J (29 bytes)
 * v  ~StubRoutines::call_stub
 * J 11619  org.eclipse.swt.internal.gtk3.GTK3.gtk_main_iteration_do(Z)Z (0 bytes)
 * J 11623 c1 org.eclipse.swt.widgets.Display.readAndDispatch()Z (88 bytes)
 * </pre>
 *
 * Please note that the crash isn't guaranteed to happen.<br>
 * Why: the causes of the bug is use-after-free in native code. <br>
 * The app crash happens when in-between "free" and "use" the released memory is
 * allocated and modified by some other native code.<br>
 * For example some if some pointer accessed during "use" is changed to contain
 * address to inaccessible memory, then the app will crash with SIGSEGV.<br>
 * The problem is that the native code in-between "free" and "use" (i.e. jvm and
 * 3d-party libraries) is too complex to predict.
 *
 * Expected results: image is shown in the tree, no crashes, no errors.
 * <p>
 *
 * GTK Version(s): 3.24.37
 */
public class Issue678_JvmCrashOnTreeItemSetImage {
	private static final int NUM_ITERATIONS = 100;

	public static void main (String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setSize (400, 300);
		shell.setLayout (new FillLayout ());
		shell.open ();
		Image image = new Image (display, 20, 20);
		for (int i = 0; i < NUM_ITERATIONS; i++) {
			Tree tree = new Tree (shell, SWT.VIRTUAL);
			tree.addListener (SWT.SetData, e -> {
				TreeItem item = (TreeItem) e.item;
				item.setText (0, "A");
				// for some reason sleeping increases probability of crash
				try {
					Thread.sleep (50);
				} catch (InterruptedException ex) {
					throw new RuntimeException (ex);
				}
				item.setImage (image); // <-- this is the critical line!
			});
			tree.setItemCount (1);
			shell.layout ();
			waitUntilIdle ();
			tree.dispose ();
		}
		display.dispose ();
	}

	private static void waitUntilIdle () {
		long lastActive = System.currentTimeMillis ();
		while (true) {
			if (Thread.interrupted ()) {
				throw new AssertionError ();
			}
			if (Display.getCurrent ().readAndDispatch ()) {
				lastActive = System.currentTimeMillis ();
			} else {
				if (lastActive + 10 < System.currentTimeMillis ()) {
					return;
				}
				Thread.yield ();
			}
		}
	}
}
