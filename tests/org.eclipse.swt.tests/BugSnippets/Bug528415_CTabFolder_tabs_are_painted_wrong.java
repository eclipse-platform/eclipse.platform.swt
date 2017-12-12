import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: CTabItem in CTabFolder disappears on mouse over.
 * Steps to reproduce:
 * <ol>
 * <li>mouse over a tab which has style SWT.RIGHT_TO_LEFT</li>
 * </ol>
 * Expected results: Tab is visible during and after mouse-over.
 * Actual results: Tab is not visible during and after mouse-over, until another shell becomes active.
 */
public class Bug528415_CTabFolder_tabs_are_painted_wrong {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 200);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setText("Bug 528415: tab hovering is broken");

		CTabFolder tabFolder = new CTabFolder(shell, SWT.RIGHT_TO_LEFT);
		CTabItem tab1 = new CTabItem(tabFolder, SWT.NONE);
		tab1.setText("Bad Tab 1");
		CTabItem tab2 = new CTabItem(tabFolder, SWT.NONE);
		tab2.setText("Bad Tab 2");

		tabFolder = new CTabFolder(shell, SWT.LEFT_TO_RIGHT);
		tab1 = new CTabItem(tabFolder, SWT.NONE);
		tab1.setText("Good Tab 1");
		tab2 = new CTabItem(tabFolder, SWT.NONE);
		tab2.setText("Good Tab 2");

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
