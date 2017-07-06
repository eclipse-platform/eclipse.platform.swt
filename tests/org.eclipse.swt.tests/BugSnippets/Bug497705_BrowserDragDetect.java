

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * Title: Browser does not send DragDetect
 * How to run: Run snippet. Try to drag text from the browser (left), onto Label (right)
 * Expected results: "Drag started" should be printed in the console.
 * Actual results: Nothing is printed in console. DragSource has no impact on browser.
 * GTK version(s): all
 *
 * This snippet has been adopted from Snippet78
 */
public class Bug497705_BrowserDragDetect {

	public static void main (String [] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		final Browser b = new Browser (shell, SWT.BORDER);
		b.setText ("TEXT");
		final Label label2 = new Label (shell, SWT.BORDER);
		setDrag (b); //doesn't seem to have an effect.
		setDrop (label2);
		shell.setSize (200, 200);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	public static void setDrag(final Browser browser) {

		Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource (browser, operations);
		source.setTransfer(types);
		source.addDragListener (new DragSourceListener () {
			@Override
			public void dragStart(DragSourceEvent event) {
				event.doit = (browser.getText ().length () != 0);
				System.out.println("Drag started"); //not ran.
			}
			@Override
			public void dragSetData (DragSourceEvent event) {
				event.data = browser.getText ();
			}
			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE)
					browser.setText ("");
			}
		});
	}

	public static void setDrop (final Label label) {
		Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		DropTarget target = new DropTarget(label, operations);
		target.setTransfer(types);
		target.addDropListener (new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				label.setText ((String) event.data);
			}
		});
	}
}
