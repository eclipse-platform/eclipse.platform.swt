/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 *
 * Derived from the exact SWT style owners recorded in the recipe provenance.
 * Original platform policy, constant aliasing and bit precedence are retained.
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.function.IntUnaryOperator;
import org.eclipse.swt.SWT;

/** Stateless shared style strategies. Instances are per policy, never per widget/item. */
enum WidgetStylePolicy implements IntUnaryOperator {
	MENU_ITEM {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.CASCADE, 0);
		}
	},
	ORIENTATION {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
		}
	},
	MESSAGE_BOX {
		@Override public int applyAsInt (int style) {
			int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
			int bits = style & mask;
            if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) {
                return style;
            }
            if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) {
                return style;
            }
            if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
                return style;
            }
			style = (style & ~mask) | SWT.OK;
			return style;
		}
	},
	LABEL {
		@Override public int applyAsInt (int style) {
			style |= SWT.NO_FOCUS;
			if ((style & SWT.SEPARATOR) != 0) {
				style = StyleBits.normalize (style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
				return StyleBits.normalize (style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
			}
			return StyleBits.normalize (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
		}
	},
	TABLE_FULL_SELECTION {
		@Override public int applyAsInt (int style) {
			/*
			* Feature in Windows.  Even when WS_HSCROLL or
			* WS_VSCROLL is not specified, Windows creates
			* trees and tables with scroll bars.  The fix
			* is to set H_SCROLL and V_SCROLL.
			*
			* NOTE: This code appears on all platforms so that
			* applications have consistent scroll bar behavior.
			*/
			if ((style & SWT.NO_SCROLL) == 0) {
				style |= SWT.H_SCROLL | SWT.V_SCROLL;
			}
			/* This platform is always FULL_SELECTION */
			style |= SWT.FULL_SELECTION;
			return StyleBits.normalize (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
		}
	},
	WITHOUT_SCROLLBARS {
		@Override public int applyAsInt (int style) {
			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
		}
	},
	COLUMN_ALIGNMENT {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
		}
	},
	PROGRESS {
		@Override public int applyAsInt (int style) {
			style |= SWT.NO_FOCUS;
			return StyleBits.normalize (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
		}
	},
	SMOOTH_SASH {
		@Override public int applyAsInt (int style) {
			/*
			* Macintosh only supports smooth dragging.
			*/
			style |= SWT.SMOOTH;
			return StyleBits.normalize (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
		}
	},
	TRACKER {
		@Override public int applyAsInt (int style) {
			if ((style & (SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN)) == 0) {
				style |= SWT.LEFT | SWT.RIGHT | SWT.UP | SWT.DOWN;
			}
			return style;
		}
	},
	DECORATIONS {
		@Override public int applyAsInt (int style) {
			if ((style & SWT.NO_TRIM) != 0) {
				style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
			} else if ((style & SWT.NO_MOVE) != 0) {
				style |= SWT.TITLE;
			}
			if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
				style |= SWT.TITLE;
			}
			return style;
		}
	},
	NO_FOCUS_WITHOUT_SCROLLBARS {
		@Override public int applyAsInt (int style) {
			style |= SWT.NO_FOCUS;
			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
		}
	},
	TEXT {
		@Override public int applyAsInt (int style) {
			if ((style & SWT.SEARCH) != 0) {
				style |= SWT.SINGLE | SWT.BORDER;
				style &= ~SWT.PASSWORD;
				/*
				* NOTE: ICON_CANCEL has the same value as H_SCROLL and
				* ICON_SEARCH has the same value as V_SCROLL so they are
				* cleared because SWT.SINGLE is set.
				*/
			}
			if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
				style &= ~SWT.MULTI;
			}
			style = StyleBits.normalize (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
            if ((style & SWT.SINGLE) != 0) {
                style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
            }
			if ((style & SWT.WRAP) != 0) {
				style |= SWT.MULTI;
				style &= ~SWT.H_SCROLL;
			}
            if ((style & SWT.MULTI) != 0) {
                style &= ~SWT.PASSWORD;
            }
            if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) {
                return style;
            }
            if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
                return style | SWT.MULTI;
            }
			return style | SWT.SINGLE;
		}
	},
	BUTTON {
		@Override public int applyAsInt (int style) {
			style = StyleBits.normalize (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
			if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
				return StyleBits.normalize (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
			}
			if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
				return StyleBits.normalize (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
			}
			if ((style & SWT.ARROW) != 0) {
				style |= SWT.NO_FOCUS;
				return StyleBits.normalize (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
			}
			return style;
		}
	},
	COMBO {
		@Override public int applyAsInt (int style) {
			/*
			* Feature in Windows.  It is not possible to create
			* a combo box that has a border using Windows style
			* bits.  All combo boxes draw their own border and
			* do not use the standard Windows border styles.
			* Therefore, no matter what style bits are specified,
			* clear the BORDER bits so that the SWT style will
			* match the Windows widget.
			*
			* The Windows behavior is currently implemented on
			* all platforms.
			*/
			style &= ~SWT.BORDER;

			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
			style = StyleBits.normalize (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
            if ((style & SWT.SIMPLE) != 0) {
                return style & ~SWT.READ_ONLY;
            }
			return style;
		}
	},
	TAB_FOLDER {
		@Override public int applyAsInt (int style) {
			style = StyleBits.normalize (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
		}
	},
	LIST_SELECTION {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
		}
	},
	MENU {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
		}
	},
	TOOL_ITEM {
		@Override public int applyAsInt (int style) {
			return StyleBits.normalize (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
		}
	},
	DATE_TIME {
		@Override public int applyAsInt (int style) {
			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
			style = StyleBits.normalize (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
			style = StyleBits.normalize (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
            if ((style & SWT.DATE) == 0) {
                style &= ~SWT.DROP_DOWN;
            }
			return style;
		}
	},
	EXPAND_BAR_WINDOWS {
		@Override public int applyAsInt (int style) {
			style &= ~SWT.H_SCROLL;
			return style | SWT.NO_BACKGROUND;
		}
	},
	TOOLTIP {
		@Override public int applyAsInt (int style) {
			int mask = SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING;
            if ((style & mask) == 0) {
                return style;
            }
			return StyleBits.normalize (style, SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR, 0, 0, 0);
		}
	},
	TOOLBAR_WINDOWS {
		@Override public int applyAsInt (int style) {
            /*
            * On Windows, only flat tool bars can be traversed.
            */
            if ((style & SWT.FLAT) == 0) {
                style |= SWT.NO_FOCUS;
            }

            /*
            * A vertical tool bar cannot wrap because TB_SETROWS
            * fails when the toolbar has TBSTYLE_WRAPABLE.
            */
            if ((style & SWT.VERTICAL) != 0) {
                style &= ~SWT.WRAP;
            }

			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
		}
	},
	TABLE_WINDOWS {
		@Override public int applyAsInt (int style) {
			/*
			* Feature in Windows.  Even when WS_HSCROLL or
			* WS_VSCROLL is not specified, Windows creates
			* trees and tables with scroll bars.  The fix
			* is to set H_SCROLL and V_SCROLL.
			*
			* NOTE: This code appears on all platforms so that
			* applications have consistent scroll bar behavior.
			*/
			if ((style & SWT.NO_SCROLL) == 0) {
				style |= SWT.H_SCROLL | SWT.V_SCROLL;
			}
			return StyleBits.normalize (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
		}
	},
	TREE_WINDOWS {
		@Override public int applyAsInt (int style) {
			/*
			* Feature in Windows.  Even when WS_HSCROLL or
			* WS_VSCROLL is not specified, Windows creates
			* trees and tables with scroll bars.  The fix
			* is to set H_SCROLL and V_SCROLL.
			*
			* NOTE: This code appears on all platforms so that
			* applications have consistent scroll bar behavior.
			*/
			if ((style & SWT.NO_SCROLL) == 0) {
				style |= SWT.H_SCROLL | SWT.V_SCROLL;
			}
			/*
			* Note: Windows only supports TVS_NOSCROLL and TVS_NOHSCROLL.
			*/
			if ((style & SWT.H_SCROLL) != 0 && (style & SWT.V_SCROLL) == 0) {
				style |= SWT.V_SCROLL;
			}
			return StyleBits.normalize (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
		}
	},
	DECORATIONS_WINDOWS {
		@Override public int applyAsInt (int style) {
			if ((style & SWT.NO_TRIM) != 0) {
				style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.BORDER);
			} else if ((style & SWT.NO_MOVE) != 0) {
				style |= SWT.TITLE;
			}
			if ((style & (SWT.MENU | SWT.MIN | SWT.MAX | SWT.CLOSE)) != 0) {
				style |= SWT.TITLE;
			}

            /*
            * If either WS_MINIMIZEBOX or WS_MAXIMIZEBOX are set,
            * we must also set WS_SYSMENU or the buttons will not
            * appear.
            */
            if ((style & (SWT.MIN | SWT.MAX)) != 0) {
                style |= SWT.CLOSE;
            }

            /*
            * Both WS_SYSMENU and WS_CAPTION must be set in order
            * to for the system menu to appear.
            */
            if ((style & SWT.CLOSE) != 0) {
                style |= SWT.TITLE;
            }

			return style;
		}
	},
	TEXT_WINDOWS {
		@Override public int applyAsInt (int style) {
			if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
				style &= ~SWT.MULTI;
			}
			style = StyleBits.normalize (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
			/*
			 * NOTE: ICON_CANCEL and ICON_SEARCH have the same value as H_SCROLL and
			 * V_SCROLL. The meaning is determined by whether SWT.SEARCH is set.
			 */
			if ((style & SWT.SEARCH) != 0) {
				style |= SWT.SINGLE | SWT.BORDER;
				style &= ~(SWT.PASSWORD | SWT.WRAP);
			} else if ((style & SWT.SINGLE) != 0) {
				style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
			}
			if ((style & SWT.WRAP) != 0) {
				style |= SWT.MULTI;
				style &= ~SWT.H_SCROLL;
			}
            if ((style & SWT.MULTI) != 0) {
                style &= ~SWT.PASSWORD;
            }
            if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) {
                return style;
            }
            if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
                return style | SWT.MULTI;
            }
			return style | SWT.SINGLE;
		}
	},
	BUTTON_COMMAND {
		@Override public int applyAsInt (int style) {
			style = StyleBits.normalize (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, SWT.COMMAND);
			if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
				return StyleBits.normalize (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
			}
			if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
				return StyleBits.normalize (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
			}
			if ((style & SWT.ARROW) != 0) {
				style |= SWT.NO_FOCUS;
				return StyleBits.normalize (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
			}
			return style;
		}
	},
	COMPOSITE_GTK {
		@Override public int applyAsInt (int style) {
			style &= ~SWT.NO_BACKGROUND;
			style &= ~SWT.TRANSPARENT;
			return style;
		}
	},
	DATE_TIME_GTK {
		@Override public int applyAsInt (int style) {
			/*
			* Even though it is legal to create this widget
			* with scroll bars, they serve no useful purpose
			* because they do not automatically scroll the
			* widget's client area.  The fix is to clear
			* the SWT style.
			*/
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);

			style = StyleBits.normalize (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
            if ((style & SWT.DATE) == 0) {
                style &= ~SWT.DROP_DOWN;
            }
			return StyleBits.normalize (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
		}
	},
	EXPAND_BAR_EMULATED {
		@Override public int applyAsInt (int style) {
			return style & ~SWT.H_SCROLL;
		}
	},
	COOL_BAR_EMULATED {
		@Override public int applyAsInt (int style) {
			style |= SWT.NO_FOCUS;
			return (style | SWT.NO_REDRAW_RESIZE) & ~(SWT.V_SCROLL | SWT.H_SCROLL);
		}
	};
}
