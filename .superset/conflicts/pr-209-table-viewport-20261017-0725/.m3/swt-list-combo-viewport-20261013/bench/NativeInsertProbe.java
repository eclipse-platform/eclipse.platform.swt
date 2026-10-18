import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

public class NativeInsertProbe {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		TCHAR cls = new TCHAR ("LISTBOX", true);
		int base = OS.WS_CHILD | OS.WS_VSCROLL | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT | 0x2000 | 0x10;
		for (int sel : new int [] {0, OS.LBS_EXTENDEDSEL, OS.LBS_MULTIPLESEL}) {
			long h = OS.CreateWindowEx (0, cls, null, base | sel, 0, 0, 200, 200, shell.handle, 0, OS.GetModuleHandle (null), null);
			StringBuilder b = new StringBuilder ("sel=" + sel + " ");
			b.append ("setcount5=").append (OS.SendMessage (h, 0x1A7, 5, 0)).append (' ');
			b.append ("ins0=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 0, 0)).append (' ');
			b.append ("ins3=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 3, 0)).append (' ');
			b.append ("insEnd=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 7, 0)).append (' ');
			b.append ("insM1=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, -1, 0)).append (' ');
			b.append ("add=").append (OS.SendMessage (h, OS.LB_ADDSTRING, 0, 0)).append (' ');
			b.append ("ins3data=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 3, 12345)).append (' ');
			b.append ("count=").append (OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0)).append (' ');
			b.append ("cursel(4)=").append (OS.SendMessage (h, OS.LB_SETCURSEL, 4, 0)).append (' ');
			b.append ("insAfterSel=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 2, 0)).append (' ');
			b.append ("cur=").append (OS.SendMessage (h, OS.LB_GETCURSEL, 0, 0)).append (' ');
			b.append ("caret=").append (OS.SendMessage (h, OS.LB_GETCARETINDEX, 0, 0)).append (' ');
			b.append ("del0=").append (OS.SendMessage (h, OS.LB_DELETESTRING, 0, 0)).append (' ');
			b.append ("count=").append (OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0)).append (' ');
			b.append ("reset=").append (OS.SendMessage (h, OS.LB_RESETCONTENT, 0, 0)).append (' ');
			b.append ("ins0empty=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 0, 0)).append (' ');
			b.append ("ins1=").append (OS.SendMessage (h, OS.LB_INSERTSTRING, 1, 0)).append (' ');
			b.append ("count=").append (OS.SendMessage (h, OS.LB_GETCOUNT, 0, 0)).append (' ');
			System.out.println (b);
			OS.DestroyWindow (h);
		}
		shell.dispose ();
		display.dispose ();
	}
}
