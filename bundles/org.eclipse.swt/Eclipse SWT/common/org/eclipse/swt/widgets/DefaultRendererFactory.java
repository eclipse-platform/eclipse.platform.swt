/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

class DefaultRendererFactory implements RendererFactory {
	@Override
	public ButtonRenderer createCheckboxRenderer(Button button) {
		return new DefaultCheckboxRenderer(button);
	}

	@Override
	public ButtonRenderer createRadioButtonRenderer(Button button) {
		return new DefaultRadioButtonRenderer(button);
	}

	@Override
	public ButtonRenderer createArrowButtonRenderer(Button button) {
		return new DefaultArrowButtonRenderer(button);
	}

	@Override
	public ButtonRenderer createPushToggleButtonRenderer(Button button) {
		return new DefaultButtonRenderer(button);
	}

	@Override
	public ScaleRenderer createScaleRenderer(Scale scale) {
		return new DefaultScaleRenderer(scale);
	}

	@Override
	public LabelRenderer createLabelRenderer(Label label) {
		return new BasicLabelRenderer(label);
	}

	@Override
	public SliderRenderer createSliderRenderer(Slider slider) {
		return new DefaultSliderRenderer(slider);
	}

	@Override
	public LinkRenderer createLinkRenderer(Link link) {
		return new DefaultLinkRenderer(link);
	}

	@Override
	public CoolBarRenderer createCoolBarRenderer(CoolBar coolBar) {
		return new DefaultCoolBarRenderer(coolBar);
	}

	@Override
	public ExpandBarRenderer createExpandBarRenderer(ExpandBar expandBar) {
		return new DefaultExpandBarRenderer(expandBar);
	}
}
