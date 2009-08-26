Instructions to build the update size:

1) check out
	:extssh:<user>@dev.eclipse.org:/cvsroot/eclipse/org.eclipse.swt.tools
	:extssh:<user>@dev.eclipse.org:/cvsroot/eclipse/org.eclipse.swt.tools/feature
	:extssh:<user>@dev.eclipse.org:/cvsroot/org.eclipse/www/swt
2) Popup menu on Package Explorer and select "Export"
3) Choose "Deployable features" and click "Next"
4) Check org.eclipse.swt.tools.feature
5) Check "Directory" and enter the path to "<workspace>\swt\updates\<current_version>"
6) Click "Finish"
7) Refresh <workspace>\swt\updates\<current_version> and commit changes
8) Wait the changes to be mirrored to the www.eclipse.org/swt to check for updates