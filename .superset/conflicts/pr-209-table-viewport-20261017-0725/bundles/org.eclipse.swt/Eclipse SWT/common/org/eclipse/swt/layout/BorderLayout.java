/*******************************************************************************
 * Copyright (c) 2022 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.layout;

import static org.eclipse.swt.SWT.*;

import java.util.*;
import java.util.AbstractMap.*;
import java.util.List;
import java.util.Map.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.stream.IntStream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * {@link BorderLayout} places controls in five regions
 *
 * <pre>
 * +--------------------------------+
 * |       NORTH / TOP              |
 * +---+------------------------+---+
 * |   |                        |   |
 * | W |                        | E |
 * | E |                        | A |
 * | S |                        | S |
 * | T |                        | T |
 * | / |                        | / |
 * | L |       CENTER           | R |
 * | E |                        | I |
 * | F |                        | G |
 * | T |                        | H |
 * |   |                        | T |
 * |   |                        |   |
 * +---+------------------------+---+
 * |       SOUTH / BOTTOM           |
 * +--------------------------------+
 * </pre>
 *
 * The controls at the NORTH/SOUTH borders get their preferred heights, the
 * controls at the EAST/WEST get their preferred widths and the center region
 * grow/shrink according to the remaining space. If more than one control is
 * placed inside a region the controls are equally distributed across their axis
 * where the grow (CENTER controlled by the {@link BorderLayout#type} value)
 *
 * @since 3.119
 */
public class BorderLayout extends Layout {

	private static final String LAYOUT_KEY = BorderLayout.class.getName() + ".layoutData";
	private static final ToIntFunction<Point> WIDTH = p -> p.x;
	private static final ToIntFunction<Point> HEIGHT = p -> p.y;

	/**
	 * type specifies how controls will be positioned within the center region.
	 *
	 * The default value is {@link SWT#HORIZONTAL}.
	 *
	 * Possible values are:
	 * <ul>
	 * <li>{@link SWT#HORIZONTAL}: Position the controls horizontally from left to
	 * right</li>
	 * <li>{@link SWT#VERTICAL}: Position the controls vertically from top to
	 * bottom</li>
	 * </ul>
	 */
	public int type = SWT.HORIZONTAL;
	/**
	 * marginWidth specifies the number of points of horizontal margin that will be
	 * placed along the left and right edges of the layout.
	 *
	 * The default value is 0.
	 */
	public int marginWidth = 0;
	/**
	 * marginHeight specifies the number of points of vertical margin that will be
	 * placed along the top and bottom edges of the layout.
	 *
	 * The default value is 0.
	 */
	public int marginHeight = 0;
	/**
	 * spacing specifies the number of points between the edge of one region and its
	 * neighboring regions.
	 *
	 * The default value is 0.
	 */
	public int spacing = 0;
	/**
	 * controlSpacing specifies the number of points between the edge of one control
	 * and its neighboring control inside a region.
	 *
	 * The default value is 0.
	 */
	public int controlSpacing = 0;
	/**
	 * If the width of the {@link SWT#LEFT} and {@link SWT#RIGHT} region exceeds the
	 * available space this factor is used to distribute the size to the controls,
	 * valid values range between [0 ... 1]
	 *
	 * The default value is 0.5 (equal distribution of available space)
	 */
	public double widthDistributionFactor = 0.5;
	/**
	 * If the height of the {@link SWT#TOP} and {@link SWT#BOTTOM} region exceeds
	 * the available space this factor is used to distribute the size to the
	 * controls, valid values range between [0 ... 1]
	 *
	 * The default value is 0.5 (equal distribution of available space)
	 */
	public double heightDistributionFactor = 0.5;

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		if (hHint > SWT.DEFAULT && wHint > SWT.DEFAULT) {
			return new Point(wHint, hHint);
		}
		Stream<Entry<Control, BorderData>> children = Arrays.stream(composite.getChildren())//
				.map(control -> borderDataControl(control, flushCache));
		Map<Integer, List<Entry<Control, BorderData>>> regionMap = children
				.collect(Collectors.groupingBy(BorderLayout::region));
		int width;
		if (wHint <= SWT.DEFAULT) {
			Builder widthBuilder = IntStream.builder();
			int northWidth = getTotal(WIDTH, TOP, regionMap);
			int southWidth = getTotal(WIDTH, BOTTOM, regionMap);
			int centerWidth;
			if (type == SWT.HORIZONTAL) {
				centerWidth = getTotal(WIDTH, CENTER, regionMap);
			} else {
				centerWidth = getMax(WIDTH, CENTER, regionMap);
			}
			int westWidth = getMax(WIDTH, LEFT, regionMap);
			int eastWidth = getMax(WIDTH, RIGHT, regionMap);
			int middleWidth = westWidth + centerWidth + eastWidth;
			if (centerWidth > 0) {
				if (westWidth > 0) {
					middleWidth += spacing;
				}
				if (eastWidth > 0) {
					middleWidth += spacing;
				}
			} else if (westWidth > 0 && eastWidth > 0) {
				middleWidth += spacing;
			}
			widthBuilder.add(middleWidth);
			widthBuilder.add(northWidth);
			widthBuilder.add(southWidth);
			width = widthBuilder.build().max().orElse(0) + 2 * marginWidth;
		} else {
			width = wHint;
		}
		int height;
		if (hHint <= SWT.DEFAULT) {
			Builder heightBuilder = IntStream.builder();
			int northHeight = getMax(HEIGHT, TOP, regionMap);
			int southHeight = getMax(HEIGHT, BOTTOM, regionMap);
			int westHeight = getTotal(HEIGHT, LEFT, regionMap);
			int eastHeight = getTotal(HEIGHT, RIGHT, regionMap);
			int centerHeight;
			if (type == SWT.HORIZONTAL) {
				centerHeight = getMax(HEIGHT, CENTER, regionMap);
			} else {
				centerHeight = getTotal(HEIGHT, CENTER, regionMap);
			}
			if (centerHeight > 0) {
				if (northHeight > 0) {
					centerHeight += spacing;
				}
				if (southHeight > 0) {
					centerHeight += spacing;
				}
			}
			if (westHeight > 0) {
				if (northHeight > 0) {
					westHeight += spacing;
				}
				if (southHeight > 0) {
					westHeight += spacing;
				}
			}
			if (eastHeight > 0) {
				if (northHeight > 0) {
					eastHeight += spacing;
				}
				if (southHeight > 0) {
					eastHeight += spacing;
				}
			}
			int sum = northHeight + southHeight;
			heightBuilder.add(westHeight + sum);
			heightBuilder.add(centerHeight + sum);
			heightBuilder.add(eastHeight + sum);
			height = heightBuilder.build().max().orElse(0) + 2 * marginHeight;
		} else {
			height = hHint;
		}
		return new Point(width, height);
	}

	/**
	 * Calculates the total W/H according to the extractor
	 *
	 * @param extractor either {@link #WIDTH} or {@link #HEIGHT}
	 * @param region    the region to compute
	 * @param regionMap the map of regions
	 * @return the total W/H including the {@link #controlSpacing}
	 */
	private int getTotal(ToIntFunction<Point> extractor, int region,
			Map<Integer, List<Entry<Control, BorderData>>> regionMap) {
		List<Entry<Control, BorderData>> list = regionMap.getOrDefault(region, Collections.emptyList());
		if (list.isEmpty()) {
			return 0;
		}
		return list.stream().mapToInt(entry -> extractor.applyAsInt(entry.getValue().getSize(entry.getKey()))).sum()
				+ ((list.size() - 1) * controlSpacing);
	}

	private static int getMax(ToIntFunction<Point> extractor, int region,
			Map<Integer, List<Entry<Control, BorderData>>> regionMap) {
		List<Entry<Control, BorderData>> list = regionMap.getOrDefault(region, Collections.emptyList());
		return getMax(extractor, list, SWT.DEFAULT, SWT.DEFAULT, false);
	}

	private static int getMax(ToIntFunction<Point> extractor, List<Entry<Control, BorderData>> list, int maxW, int maxH,
			boolean flushCache) {
		if (list.isEmpty()) {
			return 0;
		}
		if (maxW != SWT.DEFAULT || maxH != SWT.DEFAULT) {
			// we need to compute a restricted size to at least one of the given sizes
			return list.stream()
					.mapToInt(entry -> extractor.applyAsInt(entry.getValue().computeSize(entry.getKey(),maxW, maxH, flushCache))).max()
					.orElse(0);
		}
		return list.stream().mapToInt(entry -> extractor.applyAsInt(entry.getValue().getSize(entry.getKey()))).max()
				.orElse(0);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		Rectangle clientArea = composite.getClientArea();
		int clientX = clientArea.x + marginWidth;
		int clientY = clientArea.y + marginHeight;
		int clientWidth = clientArea.width - 2 * marginWidth;
		int clientHeight = clientArea.height - 2 * marginHeight;
		Stream<Entry<Control, BorderData>> children = Arrays.stream(composite.getChildren())//
				.map(control -> borderDataControl(control, flushCache));
		Map<Integer, List<Entry<Control, BorderData>>> regionMap = children
				.collect(Collectors.groupingBy(BorderLayout::region));
		regionMap.getOrDefault(SWT.NONE, Collections.emptyList())
				.forEach(entry -> entry.getKey().setBounds(clientX, clientY, 0, 0));
		List<Entry<Control, BorderData>> northList = regionMap.getOrDefault(TOP, Collections.emptyList());
		List<Entry<Control, BorderData>> southList = regionMap.getOrDefault(BOTTOM, Collections.emptyList());
		List<Entry<Control, BorderData>> westList = regionMap.getOrDefault(LEFT, Collections.emptyList());
		List<Entry<Control, BorderData>> eastList = regionMap.getOrDefault(RIGHT, Collections.emptyList());
		List<Entry<Control, BorderData>> centerList = regionMap.getOrDefault(CENTER, Collections.emptyList());
		int northControlCount = northList.size();
		int northPerControlWidth = northControlCount > 0 ?(clientWidth - (northControlCount - 1) * controlSpacing) / northControlCount:0;
		int northControlHeight = getMax(HEIGHT, northList, northPerControlWidth, SWT.DEFAULT, flushCache);
		int southControlCount = southList.size();
		int southPerControlWidth =southControlCount > 0? (clientWidth - (southControlCount - 1) * controlSpacing) / southControlCount:0;
		int southControlHeight = getMax(HEIGHT, southList,southPerControlWidth,SWT.DEFAULT, flushCache);
		if (northControlHeight + southControlHeight > clientHeight) {
			int distributionSize = (int) (clientHeight * heightDistributionFactor);
			if (northControlHeight > distributionSize) {
				northControlHeight = distributionSize;
			}
			southControlHeight = clientHeight - northControlHeight;
		}
		int centerControlHeight = clientHeight - northControlHeight - southControlHeight;
		int westControlCount = westList.size();
		int westControlWidth = getMax(WIDTH, westList, -1, -1, flushCache);
		int eastControlCount = eastList.size();
		int eastControlWidth = getMax(WIDTH, eastList, -1, -1, flushCache);
		if (westControlWidth + eastControlWidth > clientWidth) {
			int distributionSize = (int) (clientWidth * widthDistributionFactor);
			if (westControlWidth > distributionSize) {
				westControlWidth = distributionSize;
			}
			eastControlWidth = clientWidth - westControlWidth;
		}
		int centerControlWidth = clientWidth - westControlWidth - eastControlWidth;
		int centerControlCount = centerList.size();
		// Full width and preferred height for NORTH and SOUTH if possible
		if (northControlCount > 0) {
			int x = clientX;
			int y = clientY;
			for (Entry<Control, BorderData> entry : northList) {
				entry.getKey().setBounds(x, y, northPerControlWidth, northControlHeight);
				x += northPerControlWidth + controlSpacing;
			}
		}
		if (southControlCount > 0) {
			int x = clientX;
			int y = clientY + centerControlHeight + northControlHeight;
			for (Entry<Control, BorderData> entry : southList) {
				entry.getKey().setBounds(x, y, southPerControlWidth, southControlHeight);
				x += southPerControlWidth + controlSpacing;
			}
		}
		// remaining height for WEST and EAST, preferred width for WEST and EAST if
		// possible ...
		if (westControlCount > 0) {
			int x = clientX;
			int y = clientY + northControlHeight;
			int h = clientHeight - northControlHeight - southControlHeight;
			if (northControlCount > 0) {
				y += spacing;
				h -= spacing;
			}
			if (southControlCount > 0) {
				h -= spacing;
			}
			int controlHeight = (h - (westControlCount - 1) * controlSpacing) / westControlCount;
			for (Entry<Control, BorderData> entry : westList) {
				entry.getKey().setBounds(x, y, westControlWidth, controlHeight);
				y += controlHeight + controlSpacing;
			}
		}
		if (eastControlCount > 0) {
			int x = clientX + centerControlWidth + westControlWidth;
			int y = clientY + northControlHeight;
			int h = clientHeight - northControlHeight - southControlHeight;
			if (northControlCount > 0) {
				y += spacing;
				h -= spacing;
			}
			if (southControlCount > 0) {
				h -= spacing;
			}
			int controlHeight = (h - (eastControlCount - 1) * controlSpacing) / eastControlCount;
			for (Entry<Control, BorderData> entry : eastList) {
				entry.getKey().setBounds(x, y, eastControlWidth, controlHeight);
				y += controlHeight + controlSpacing;
			}
		}
		// remaining height and width for CENTER
		if (centerControlCount > 0) {
			int x = clientX + westControlWidth;
			int y = clientY + northControlHeight;
			int h = centerControlHeight;
			int w = centerControlWidth;
			if (westControlCount > 0) {
				x += spacing;
				w -= spacing;
			}
			if (eastControlCount > 0) {
				w -= spacing;
			}
			if (northControlCount > 0) {
				y += spacing;
				h -= spacing;
			}
			if (southControlCount > 0) {
				h -= spacing;
			}
			int controlHeight;
			int controlWidth;
			if (type == SWT.HORIZONTAL) {
				controlHeight = h;
				controlWidth = (w - (centerControlCount - 1) * controlSpacing) / centerControlCount;
			} else {
				controlWidth = w;
				controlHeight = (h - (centerControlCount - 1) * controlSpacing) / centerControlCount;
			}
			for (Entry<Control, BorderData> entry : centerList) {
				entry.getKey().setBounds(x, y, controlWidth, controlHeight);
				if (type == SWT.HORIZONTAL) {
					x += controlWidth + controlSpacing;
				} else {
					y += controlHeight + controlSpacing;
				}
			}
		}
	}

	private <C extends Control> Entry<C, BorderData> borderDataControl(C control, boolean flushCache) {
		Object layoutData = control.getLayoutData();
		if (layoutData instanceof BorderData borderData) {
			if (flushCache) {
				borderData.flushCache(control);
			}
			return new SimpleEntry<>(control, borderData);
		} else {
			BorderData borderData = flushCache ? null : (BorderData) control.getData(LAYOUT_KEY);
			if (borderData == null) {
				control.setData(LAYOUT_KEY, borderData = new BorderData());
			}
			return new SimpleEntry<>(control, borderData);
		}
	}

	private static int region(Entry<Control, BorderData> entry) {
		BorderData borderData = entry.getValue();
		if (borderData == null) {
			// we assume all controls without explicit data to be placed in the center area
			return SWT.CENTER;
		}
		return borderData.getRegion();
	}

	@Override
	public String toString() {
		return "BorderLayout [" //
				+ "type=" + ((type == SWT.HORIZONTAL) ? "SWT.HORIZONTAL" : "SWT.VERTICAL") //
				+ ", marginWidth=" + marginWidth //
				+ ", marginHeight=" + marginHeight //
				+ ", spacing=" + spacing //
				+ ", controlSpacing=" + controlSpacing //
				+ ", widthDistributionFactor=" + widthDistributionFactor//
				+ ", heightDistributionFactor=" + heightDistributionFactor //
				+ "]";
	}
}
