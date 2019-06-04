org.eclipse.swt.snippets
========================

Small examples for the usage of SWT.

Setting the classpath:
----------------------

To compile this project, you need to set the classpath specific for your operating and windowing system.
For this, rename one of the following files to `.classpath`:

* .classpath_win32 - Windows
* .classpath_cocoa - Mac OS X
* .classpath_gtk - Linux and all Unix variants

To see these files, you may have to remove the filter for ".* resources":
* In the Project Explorer: view menu > Customize View... > Filters
* In the Package Explorer: view menu > Filters...


Tips for writing Snippets:
--------------------------

* Every Snippet should set a window title (`Shell#setText(title)`) even if it's only a generic "Snippet &lt;number&gt;".
* Snippets should never use `System.exit()`. This can interfere the operation of SnippetLauncher or SnippetExplorer.
* Snippets should always use `new Display()` to create the display and not create `Display` or `Shell` in a static initializer.
  This can interfere the operation of SnippetExplorer and sometimes SnippetLauncher. Especially the SnippetExplorer can run
  Snippets multiple times without reloading the Snippet class. A static initialized shell is disposed after the first run and
  might not reinitialized on following launches.
* If a Snippet is started through SnippetLauncher or SnippetExplorer there is no guarantee a thread started with `setDaemon(true)`
  is stopped immediately when the Snippet ends. At best a Snippet stops all threads on exit.
* To find the new Snippet easily you should add it to the SWT Snippet website.
  * Website: https://www.eclipse.org/swt/snippets/
  * Source: https://git.eclipse.org/c/www.eclipse.org/swt.git/tree/snippets/index.php
* If appropriate create a preview image for the Snippet. (used by SnippetExplorer and on the Snippet website)
* The Snippet explanation comment should not be placed above the imports (as many existing Snippets do). Eclipse's organize
  imports feature may place new imports above the comment and therefore hide the explanation when imports are collapsed.
