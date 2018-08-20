/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browser.demos.views;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.examples.browser.demos.BrowserDemoPlugin;
import org.eclipse.swt.examples.browser.demos.Pawns;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabItem;

public class PawnTab {

	Action pawnAction;
	Action editAction;
	
	Browser browser = null;
	ReversiListener reversiListener;

	final static int TYPE_WELCOME = 1;
	final static int TYPE_START = 2;
	final static int TYPE_BOARD = 3;
	final static int TYPE_BOARD_OVER = 4;

	final static String URL = "http://www.org.eclipse.swt.examples.browser.demos.pawns/";
	static String PLUGIN_PATH = BrowserDemoPlugin.PLUGIN_PATH;
	static String CSS_FOLDER = "css2";
	static String URL_CSS = PLUGIN_PATH+CSS_FOLDER+"/style.css";
	static String URL_WELCOME =PLUGIN_PATH+CSS_FOLDER+"/welcome.html";

	final static String ACTION_START_2_PLAYERS = "actionstart2players";
	final static String ACTION_START_1_PLAYER = "actionstart1player";
	final static String ACTION_WHITE = "actionwhite";
	final static String ACTION_BLACK = "actionblack";
	final static String ACTION_THEME = "actiontheme";

	static byte[][] game = null;
	static boolean isWhite = true;
	static int cntWhite = 0, cntBlack = 0;
	static boolean computer = false;
	static int cx, cy;
	final static byte EMPTY = 0;
	final static byte WHITE = 1;
	final static byte BLACK = 2;
	final static byte WALL = 3;
	
	static Pawns ttr = null;
	static int[] move = new int[2];
	
	public PawnTab(TabItem item) {		
		try {
			browser = new Browser(item.getParent(), SWT.NONE);
		} catch (SWTError e) {
			e.printStackTrace();
			return;
		}
		item.setText("Pawns");
		item.setControl(browser);

		Menu menu = new Menu(browser.getShell(), SWT.POP_UP);
		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText("End Game");
		item2.addListener(SWT.Selection, e -> {
			game = null;
			isWhite = true;
			cntWhite = 0; cntBlack = 0;
			ttr = null;
			browser.setUrl(URL_WELCOME);
		});
		browser.setMenu(menu);
		
		reversiListener = new ReversiListener();
		browser.addLocationListener(reversiListener);
		browser.setUrl(URL_WELCOME);
	}

	static String getHtml(int type) {
		String html = null;
		switch (type) {
		case TYPE_BOARD:
		case TYPE_BOARD_OVER: {
			html = "<html><header><link rel=\"stylesheet\" type=\"text/css\" href=\""+URL_CSS+"\"></header><body><div class=\"board\"><table><tbody>";
			String classPlayerWhite = "playerwhite", classPlayerBlack = "playerblack";
			if (type == TYPE_BOARD_OVER) {
				if (cntWhite > cntBlack) {
					classPlayerWhite += " winner";
					classPlayerBlack += " loser";
				} else {
					classPlayerWhite += " loser";
					classPlayerBlack += " winner";
				}
			}
			String white = "<td class=\"white\"/>";
			String black = "<td class=\"black\"/>";
			String wall = "<td class=\"wall\"/>";
			for (int i = 0; i < game.length; i++) {
				html += "<tr>";
				for (int j = 0; j < game[0].length; j++) {
					switch (game[i][j]) {
						case EMPTY: {
							String empty = "<td class=\""+(isWhite ? "whitelink" : "blacklink")+"\">"+ (isWhite || (!isWhite && !computer) ? "<a href=\""+URL+(isWhite ? ACTION_WHITE : ACTION_BLACK)+"/xx"+i+"yy"+j+"\" class=\"empty\"/>" : "<div class=\"empty\"/>")+"</td>";
							html += empty; break;
						}
						case WHITE: html += white; break;
						case BLACK: html += black; break;
						case WALL: html += wall; break;
					}
				}
				html +="</tr>";
			}
			html += "</tbody></table></div>";
			html += "<div class=\""+classPlayerWhite+"\">"+cntWhite+"</div>";
			html += "<div class=\""+classPlayerBlack+"\">"+cntBlack+"</div>";
			html += "</body></html>";
			break;
		}
		}
		return html;
	}

	public class ReversiListener implements LocationListener {
		@Override
		public void changed(LocationEvent e) {
		}
		@Override
		public void changing(LocationEvent e) {
			try {
			final Browser browser = (Browser)e.widget;
			if (e.location.indexOf(ACTION_START_1_PLAYER) != -1 || e.location.indexOf(ACTION_START_2_PLAYERS) != -1) {
				computer = e.location.indexOf(ACTION_START_1_PLAYER) != -1;
				game = new byte[8][8];
				if (computer) ttr = new Pawns();
				for (int i = 0; i < 5; i++) game[(int)(Math.random()*game.length)][(int)(Math.random()*game[0].length)] = WALL;
				e.display.asyncExec(() -> browser.setText(getHtml(TYPE_BOARD)));
				e.doit = false;
				return;
			}
			if (e.location.indexOf(ACTION_THEME) != -1) {
				int index = e.location.indexOf(ACTION_THEME) + ACTION_THEME.length() + 1;
				CSS_FOLDER = e.location.substring(index, index + 4);
				URL_CSS = PLUGIN_PATH+CSS_FOLDER+"/style.css";
				URL_WELCOME = PLUGIN_PATH+CSS_FOLDER+"/welcome.html";
				e.display.asyncExec(() -> browser.setUrl(URL_WELCOME));
				e.doit = false;
				return;
			}
			byte player = EMPTY;
			if (e.location.indexOf(ACTION_WHITE) != -1) player = WHITE;
			else if (e.location.indexOf(ACTION_BLACK) != -1) player = BLACK;
			if (player != EMPTY) {
				int index = e.location.indexOf("xx") + 2;
				int x = Integer.parseInt(e.location.substring(index, index + 1));
				index = e.location.indexOf("yy") + 2;
				int y = Integer.parseInt(e.location.substring(index, index + 1));
				boolean hasMore = add(x, y, player);
				isWhite = player != WHITE;
				browser.setText(getHtml(hasMore ? TYPE_BOARD : TYPE_BOARD_OVER));
				if (computer && hasMore && !isWhite) play(e.display, browser, 5000);
				e.doit = false;
			}
			}catch (Exception e1 ) {
				e1.printStackTrace();
			}
		}
	}

	public static boolean add(int x, int y, byte color) {
		game[x][y] = color;
		int cnt = Math.min(x, y), n = 0;
		int other_color = color == WHITE ? BLACK : WHITE;
		for (int d = 1; d <= cnt; d++) {
			if (game[x-d][y-d] == other_color) n++;
			else if (game[x-d][y-d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x-d][y-d] = color; break; }
		}
		cnt = Math.min(game.length - 1 - x, game[0].length - 1 - y); n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x+d][y+d] == other_color) n++;
			else if (game[x+d][y+d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x+d][y+d] = color; break; }
		}
		cnt = Math.min(game.length - 1 - x, y); n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x+d][y-d] == other_color) n++;
			else if (game[x+d][y-d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x+d][y-d] = color; break; }
		}
		cnt = Math.min(x, game[0].length - 1 - y); n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x-d][y+d] == other_color) n++;
			else if (game[x-d][y+d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x-d][y+d] = color; break; }
		}
		cnt = y; n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x][y-d] == other_color) n++;
			else if (game[x][y-d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x][y-d] = color; break; }
		}
		cnt = game[0].length - 1 - y; n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x][y+d] == other_color) n++;
			else if (game[x][y+d] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x][y+d] = color; break; }
		}
		cnt = x; n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x-d][y] == other_color) n++;
			else if (game[x-d][y] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x-d][y] = color; break; }
		}
		cnt = game.length - 1 - x; n = 0;
		for (int d = 1; d <= cnt; d++) {
			if (game[x+d][y] == other_color) n++;
			else if (game[x+d][y] != color) break;
			else { if (n > 0) for (d = 1; d <= n; d++) game[x+d][y] = color; break; }
		}

		boolean hasMore = false;
		cntWhite = 0; cntBlack = 0;
		for (int i = 0; i < game.length; i++)
			for (int j = 0; j < game[0].length; j++) {
				switch (game[i][j]) {
					case EMPTY: hasMore = true; break;
					case WHITE: cntWhite++; break;
					case BLACK: cntBlack++; break;
				}
			}
		return hasMore;
	}
	
	public static void play(final Display display, final Browser browser, int delay) {
		ttr.playRequest(game, BLACK);
		display.timerExec(3000, () -> {
			ttr.getBestMove(move);
			boolean hasMore = add(move[0], move[1], BLACK);
			isWhite = true;
			browser.setText(getHtml(hasMore ? TYPE_BOARD : TYPE_BOARD_OVER));
		});
	}
	
	public static void main(String[] args) {
	}
}
