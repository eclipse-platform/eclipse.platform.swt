Instructions to build the update site:

1) Import projects
	ssh://<user>@git.eclipse.org/gitroot/platform/eclipse.platform.swt/bundles/org.eclipse.swt.tools
	ssh://<user>@git.eclipse.org/gitroot/platform/eclipse.platform.swt/bundles/org.eclipse.swt.tools/feature
	ssh://<user>@git.eclipse.org/gitroot/www.eclipse.org/swt
2) Select org.eclipse.swt.tools.feature in Package Explorer
3) Popup menu on Package Explorer and select "Export"
4) Choose "Deployable features" and click "Next"
5) Check "Directory" and enter the path to "<git>/www.eclipse.org/swt/updates/<current_version>"
6) Click "Finish"
7) Refresh <git>/www.eclipse.org/swt and commit changes
8) Wait the changes to be mirrored to the www.eclipse.org/swt to check for updates