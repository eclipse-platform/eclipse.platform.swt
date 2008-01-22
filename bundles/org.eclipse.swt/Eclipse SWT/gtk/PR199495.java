import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class PR199495 {
	public static void main(String[] args){
		Display display = new Display ();
		final Shell shell = new Shell(display);
		FontData fd0 = display.getFontList("Sans", true)[0];
		FontData fd1 = new FontData("Sans", 0, SWT.NONE);
		System.out.println(fd0);
		System.out.println(fd1);
		Font f0 = new Font (display, fd0);
		Font f1 = new Font (display, fd1);
		Label label = new Label(shell,SWT.BORDER);
		label.setFont(f0);
		label.setText("Felipe");
		label = new Label(shell,SWT.BORDER);
		label.setFont(f1);
		label.setText("Heidrich");
		shell.setLayout(new FillLayout());
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	}
}
