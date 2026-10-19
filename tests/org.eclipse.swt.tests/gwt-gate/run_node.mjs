/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
import fs from 'node:fs';
import vm from 'node:vm';
const file = process.argv[2];
const lines = [];
const sandbox = {
  console: { log: (s) => lines.push(String(s)), error: (s) => lines.push('ERR ' + s), warn: () => {} },
  setTimeout, clearTimeout, setInterval, clearInterval,
};
const el = () => ({ getAttribute: () => null, appendChild() {}, removeChild() {}, style: {}, parentNode: null, src: '' });
sandbox.document = {
  currentScript: { src: 'http://localhost/gate/gate.nocache.js', tagName: 'SCRIPT' }, getElementById: () => null, getElementsByTagName: () => [], write() {}, createElement: el, readyState: 'complete',
  addEventListener() {}, removeEventListener() {}, body: el(), documentElement: el(), location: { href: 'http://localhost/' },
};
sandbox.location = sandbox.document.location;
sandbox.window = sandbox; sandbox.self = sandbox; sandbox.$wnd = sandbox; sandbox.navigator = { userAgent: 'node' };
sandbox.addEventListener = () => {}; sandbox.removeEventListener = () => {};
vm.createContext(sandbox);
vm.runInContext(fs.readFileSync(file, 'utf8'), sandbox, { filename: file });
setTimeout(() => { console.log(lines.join('\n') || 'NO OUTPUT'); process.exit(0); }, 300);
