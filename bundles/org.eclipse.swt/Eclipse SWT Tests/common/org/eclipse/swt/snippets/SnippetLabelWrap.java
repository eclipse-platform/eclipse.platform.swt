package org.eclipse.swt.snippets;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SnippetLabelWrap {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Label with Wrap Example");
        shell.setLayout(new FillLayout());

		Label label = new Label(shell, SWT.WRAP);
		label.setSize(200, 100);
        label.setText("This is a long text that should wrap within the label. Resize the window to see the wrapping effect.");

		Label label2 = new Label(shell, SWT.NONE);
		label2.setText("This is another label without wrap. It will not wrap even if the text is long.");
		label2.setSize(200, 50);

        shell.setSize(300, 150);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
