/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.examples.browser.demos;

public class Pawns {

	/* Current board representation in compacted form */
	byte[] game = new byte[64];
	/* Best move */
	int bestIndex = -1;
	/* Related best score */
	int bestScore = Integer.MIN_VALUE;
	/* Estimated strategic value of each cell based on proximity to walls */
	static int[] gameWallWeight = new int[64];
	Thread thread = null;
	boolean threadStop = false;
	
	final static byte EMPTY = 0;
	final static byte WHITE = 1;
	final static byte BLACK = 2;
	final static byte WALL = 3;
	
public Pawns() {
}

/* Provide the current game and ignitiate the search of the best move for the given type
 * Must return immediately as it will be called from the UI thread.
 * The UI thread will fetch the best move any time thereafter.
 */
public void playRequest(byte[][] game, int type) {
	threadStop = true;
	synchronized (this) {
		bestIndex = -1;
		bestScore = Integer.MIN_VALUE;
		convert(game, this.game);
		initPawnBorders(this.game, gameWallWeight);
		/* Quickly compute a legal move */
		for (int i = 0; i < this.game.length; i++) {
			if (this.game[i] == EMPTY) {
				bestIndex = i;
				break;
			}
		}
		new Thread() {
			@Override
			public void run() {
				synchronized(Pawns.this) {
					threadStop = false;
					int[] result = new int[2];
					/* if long time, must check for threadStop and exit early */ 
					evalBest(Pawns.this.game, BLACK, 2, result);
					bestIndex = result[0];
					bestScore = result[1];
				}
			}
		}.start();
	}
}

/* Fetch best move in natural coordinates for the board previously given in
 * the call to playRequest.
 */
public void getBestMove(int[] point) {
	convert(bestIndex, point);
	threadStop = true;
}

/* Given an expanded representation of the board, format internal compact mode */
static void convert(byte[][] board, byte[] g) {
	for (int i = 0; i < board.length; i++) System.arraycopy(board[i], 0, g, i * 8, 8);
}
/* Update given compact model based on player move in natural coordinates */
static void set(byte[] g, int x, int y, byte type) {
	g[x*8+y] = type;
}
/* Given an index in compact representation, return natural coordinates */
static void convert(int index, /*out [0] x [1] y */int[] point) {
	point[0] = index / 8;
	point[1] = index % 8;
}
/* Given an index into the compact model and the neighbour code,
 * return the index of the corresponding neighbour index.
 * Returns -1 if there is no neighbour.
 * 
 * Neighbour code for the index X
 * 0 1 2
 * 3 X 4
 * 5 6 7 
 */
static int getNeighbourIndex(byte[] g, int index, int neighbour) {
	if (index < 0 || index >= g.length) return -1;
	int result = -1;
	switch (neighbour) {
		case 0: result = index < 8 || index % 8 == 0 ? -1 : index - 9; break;
		case 1: result = index < 8 ? -1 : index - 8; break;
		case 2: result = index < 8 || index % 8 == 7 ? -1 : index - 7; break;
		case 3: result = index % 8 == 0 ? -1 : index - 1; break;
		case 4: result = index % 8 == 7 ? -1 : index + 1; break;
		case 5: result = index % 8 == 0 || index >= 56 ? -1 : index + 7; break;
		case 6: result = index >= 56 ? -1 : index + 8; break;
		case 7: result = index % 8 == 7 || index >= 56 ? -1 : index + 9; break;
	}
	return result;
}
/* Make the player type play at index on given compact board 
 * Compute all pawns that must be reversed.
 */
static void play(byte[] g, int index, byte type) {
	byte opponentType = type == WHITE ? BLACK : WHITE;
	for (int neighbour = 0; neighbour <= 7; neighbour++) {
		int nIndex = getNeighbourIndex(g, index, neighbour);
		int[] reversiIndeces = new int[6];
		int nReversi = 0;
		while (nIndex != -1 && nReversi < 6 && g[nIndex] == opponentType) {
			reversiIndeces[nReversi] = nIndex;
			nReversi++;
			nIndex = getNeighbourIndex(g, nIndex, neighbour);			
		}
		if (nReversi > 0 && nIndex != -1 && g[nIndex] == type) {
			for (int i = 0; i < nReversi; i++) g[reversiIndeces[i]] = type;
		}
	}
	g[index] = type;
}
/* Evaluate the given compact model based on pawns distribution 
 * High means white has advantage. Below zero means black has advantage.
 */
static int eval(byte[] g) {
	int cntWhite = 0, cntBlack = 0, cntEmpty = 0;
	int cntWhiteWallAdvantage = 0, cntBlackWallAdvantage = 0;
	for (int i = 0; i < 64; i++) {
		if (g[i] == WHITE) {
			cntWhite++;
			cntWhiteWallAdvantage += gameWallWeight[i];
		}
		else if (g[i] == BLACK) {
			cntBlack++;
			cntBlackWallAdvantage += gameWallWeight[i];
		}
		else if (g[i] == EMPTY) cntEmpty++;
	}
	if (cntEmpty == 0) {
		if (cntWhite > cntBlack) return Integer.MAX_VALUE; /* White wins */
		if (cntWhite < cntBlack) return Integer.MIN_VALUE; /* Black wins */
		return 0; /* Stalemate */
	}
	return cntWhite + cntWhiteWallAdvantage - cntBlack - cntBlackWallAdvantage;
}

/* Recognize pawns protected by walls or borders 
 * TBD - note this should be called only once for each cell and stored
 * in a separate byte[] gWallGain
 * */
static void initPawnBorders(byte[] g, int[] gameWallWeight) {
	/* A pawn has 8 neighbours on 4 axes.
	 * Strategic pawns have one side of each axis protected by a wall and the other
	 * side not closed by a wall.
	 * A pawn cannot be reversed when each of its 4 axes are protected by a wall on
	 * one side. Pawns that have more than 4 walls are less interesting since they
	 * are not open enough to the board.
	 * 
	 * Nbr walls, nbr axis covered, estimated value
	 * 0 n/a 0
	 * 1 1 2
	 * 2 1 1
	 * 2 2 6
	 * 3 2 4
	 * 4 2 2
	 * 3 3 9
	 * 4 3 8
	 * 4 4 16
	 * 5 4 14
	 * 6 4 9
	 * 7 4 6
	 * 8 4 0
	 */
	int[] nTypes = new int[8];
	for (int i = 0; i < 64; i++) {
		int nWalls = 0;
		int nAxis = 0;
		for (int n = 0; n < 8; n++) {
			int nIndex = getNeighbourIndex(g, i, n);
			nTypes[n] = nIndex != -1 ? g[nIndex] : WALL;
			if (nTypes[n] == WALL) nWalls++;
		}
		int score = nWalls;
		if (nWalls > 0) {
			if (nTypes[0] == WALL || nTypes[7] == WALL) nAxis++;
			if (nTypes[1] == WALL || nTypes[6] == WALL) nAxis++;
			if (nTypes[2] == WALL || nTypes[5] == WALL) nAxis++;
			if (nTypes[4] == WALL || nTypes[3] == WALL) nAxis++;
			switch (nAxis) {
				case 4: switch (nWalls) { case 4: score = 16; break; case 5: score = 14; break; case 6: score = 9; case 7: score = 6; break; case 8: score = 0; break;} break;
				case 3: switch (nWalls) { case 3: score = 9; break; case 4: score = 8;} break;
				case 2: switch (nWalls) { case 2: score = 6; break; case 3: score = 4; break; case 4: score = 2; } break;
				case 1: switch (nWalls) { case 1: score = 2; break; case 2: score = 1; break;} break;
			}
		}
		gameWallWeight[i] = score;
	}
}

/* Evaluate the best move for player type for the given board, doing a depth 1 search */
static void evalBest(byte[] g, byte type, int depth, /* out [0] best move, [1] minimax */int[] result) {
	byte[] tmp = new byte[64];
	byte opponentType = type == WHITE ? BLACK : WHITE;
	result[0] = -1; result[1] = Integer.MIN_VALUE;
	for (int i = 0; i < 64; i++) {
		if (g[i] == EMPTY) {
			System.arraycopy(g, 0, tmp, 0, 64);
			play(tmp, i, type);
			int score = eval(tmp);
			if (depth > 1) {
				int[] tmpResult = new int[2];
				evalBest(tmp, opponentType, depth - 1, tmpResult);
				score = tmpResult[1];
			}
			if ((type == WHITE && score > result[1]) || (type == BLACK && score < result[1]) || result[0] == -1) {
				result[0] = i;
				result[1] = score;
			}
		}
	}
}
}
