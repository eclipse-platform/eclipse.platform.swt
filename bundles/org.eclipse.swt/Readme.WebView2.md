# WebView2 Support for SWT

WebView2 is a runtime component that provides an embeddable version of
Microsoft Edge.

WebView2 component is available from multiple sources:

* A stand-alone auto-updating runtime component with a choice of
  web and offline installers.
  (https://developer.microsoft.com/en-us/microsoft-edge/webview2/)

* A fixed-version archive with all the necessary files that can be bundled
  with the application.
  (Same link as above).

* Beta/Dev/Canary version of the Edge browser.
  (https://www.microsoftedgeinsider.com/en-us/download). This isn't suitable
  for production use, but might be necessary to test new pre-release
  features.

  _Note_: Stable Edge browser installations *don't* provide a WebView2 component.

See also: https://docs.microsoft.com/en-us/microsoft-edge/webview2/concepts/distribution.

## Using WebView2

There are no extra dependencies beyond the WebView2 runtime itself.

On Windows, the default `Browser` backend is Internet Explorer.
To use the WebView2 backend, pass the `SWT.EDGE` style flag when creating
a `Browser` instance. Alternatively, set the system property
`org.eclipse.swt.browser.DefaultType` to `edge` to globally change the
default. (Non-Windows platforms will ignore this value).

_Note_: If WebView2 backend is requested but the runtime isn't found,
the `Browser` will automatically fall back to the Internet Explorer backend.

### Browser Directory

WebView2 backend will automatically locate runtimes and Edge installations.
The path to the Edge binary directory can also be set manually using the
`org.eclipse.swt.browser.EdgeDir` system property. This is also
required when bundling fixed-version WebView2 binaries.

### User Directory

WebView creates a user data directory to stores caches and
persistent data like cookies and localStorage. All WebView2 instances in
an application and all instances of the same application share this directory.

The default user directory location is `%LOCALAPPDATA%\<AppName>\WebView2`,
where `<AppName>` is defined with `Display.setAppName()`. This location can
be overridden on a per-process basis by setting the
`org.eclipse.swt.browser.EdgeDataDir` system property.

See also: https://docs.microsoft.com/en-us/microsoft-edge/webview2/concepts/userdatafolder

### Other System Properties

The property `org.eclipse.swt.browser.EdgeArgs` defines command line
arguments to be passed directly to the Chromium process.
For a list of available arguments (unofficial) see
https://peter.sh/experiments/chromium-command-line-switches/.

The property `org.eclipse.swt.browser.EdgeLanguage` is a language or
language+country code that defines the browser UI language and preferred
language for HTTP requests (`Accept-Languages` header).
Example values: `en`, `ja`, `en-GB`, `de-AT`.

_Note_: All of the properties described above must be set before the first
instance of the `Browser` with `SWT.EDGE` style is created.

Informational property `org.eclipse.swt.browser.EdgeVersion` contains the
version of the browser currently in use.

## Limitation and Caveats

Due to API mismatch between SWT and WebView2, some features are limited or
not available.

* `AuthenticationListener`<br/>
  Unsupported. Missing upstream API.
  (https://github.com/MicrosoftEdge/WebView2Feedback/issues/120).

* `StatusTextListener`<br/>
  Unsupported. Conceptually obsolete.

* `VisibilityWindowListener.hide`<br/>
  Unsupported. Conceptually obsolete.

* `ProgressListener.changed`<br/>
  Unsupported. Missing upstream API.

* `ProgressListener.completed`<br/>
  Fires for the top level document only.
  On Edge version 88 and later it matches the `DOMContentLoaded` event, on earlier
  versions it matches the `load` event.

* `LocationListener.changing`, `OpenWindowListener.open`<br/>
  These events may return values and have to run synchronously.
  Calling `evaluate()` and `getText()` from their handlers is impossible
  and will throw an exception.
  (This can only be fixed by exposing the asynchronous `evaluate()` method).

* `LocationListener.changed`<br/>
  Fires for the top document only. Doesn't fire when using `setText()` (TODO).

* `KeyListener`, `MouseListener`<br/>
  Unsupported. Missing upstream API.
  (https://github.com/MicrosoftEdge/WebView2Feedback/issues/112).
  Emulation with script callbacks is possible.

* `evaluate(String script, boolean trusted)`<br/>
  The parameter `trusted` is ignored. Everything runs in the same security
  context scoped to a given user directory.

* `execute(String script)`<br/>
  Execution is always asynchronous. You can't observe evaluation effects
  immediately after the `execute()` call. See `evaluate()` for synchronous
  script evaluation.

* `getText()`<br/>
  Returns the live contents of HTML document as seen by the browser.
  This can differ from what was set with `setText()` due to browser processing
  and script execution.<br/>
  _Note_: Implemented as `evaluate("return document.documentElement.outerHTML")`.

* `setText()`<br/>
  This method uses `data:` URLs internally (WebView2 implementation detail)
  and these URLs might appear in the `LocationEvent.url` field.

* `getCookie()`, `setCookie()`<br/>
  Unsupported. WebView doesn't have a global cookie manager. Cookie access
  is provided by individual browser views and required `Browser` API additions.

* `setUrl(String url, String postData, String[] headers)`<br/>
  The parameters `headers` and `postData` require Edge 88 or later
  (currently in Beta).

* `close()`<br/>
  Unsupported. Missing upstream API.

## Known issues in WebView2

* When using `OpenWindowListener`, calling `window.open()` with an invalid URL
  might glitch the WebView.
  (https://github.com/MicrosoftEdge/WebView2Feedback/issues/762).

## Potential API additions

Here are some potentially useful WebView2 features that could be exposed through
API additions, `Widget.setData` or system properties:

* An option to delete the user data directory on exit.
* Getting and setting page zoom.
* Options to disable: page zoom, dev tools, built-in context menus,
  built-in error pages, microphone, camera, geolocation, notifications, sensors,
  clipboard access.
* An option to provide own script dialogs (alert/confirm/prompt).
* `Window.postMessage()` communication as a clean, asynchronous alternative
  to `BrowserFunction`.
* Access to request/response headers and body.
