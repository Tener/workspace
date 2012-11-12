package breakthrough_vtcplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import breakthrough.*;
import game.*;

/**
 * A Game player that uses iterative deepening alpha beta pruning with
 * a transposition table 
 * to play breakthrough.
 * 
 * Does not work for deeper levels (greater than like 5).
 * It consumes too much memory...  Fix me if you have time please.
 * Probably abandon this...
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class TranspositionBreakthroughPlayer extends IterativeDeepeningBreakthroughPlayer {
	
	public TranspositionBreakthroughPlayer(String nickname) {
		super(nickname);
	}
	
	/**
	 * Does a depth limited alpha beta pruning decision search.
	 * @param brd current board state
	 * @param currDepth current depth in the search
	 * @param mvStack new ArrayList
	 * @return move recommendation
	 */
	protected ScoredBreakthroughMove timedAlphaBeta(EqualBreakthroughState brd, int currDepth,
												    ArrayList<ScoredBreakthroughMove> mvStack,
												    Hashtable<String,Integer> tt,
												    double alpha, double beta) {
		if (currDepth >= mvStack.size()) { mvStack.add(new ScoredBreakthroughMove()); } // auto grow
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean isTerminal = terminalValue(brd, mvStack.get(currDepth));
		
		if (elapsedTime() > DESIRED_MOVE_TIME) {
			return null; // get out of here if timed out
		}
		
		if (isTerminal) {
			;
		} else if (currDepth == depthLimit) {
			mvStack.get(currDepth).set(evalBoard(brd));
		} else {
			double bestScore = (toMaximize ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
			ScoredBreakthroughMove bestMove = mvStack.get(currDepth);
			if ((currDepth+1) >= mvStack.size()) { mvStack.add(new ScoredBreakthroughMove()); } // auto grow
			ScoredBreakthroughMove nextMove = mvStack.get((currDepth+1));
			
			bestMove.set(bestScore);
			
			ArrayList<ScoredBreakthroughMove> possMvs = getPossibleMoves(brd);
			Collections.shuffle(possMvs);
			for (int i=0; i<possMvs.size(); i++) {
				BreakthroughMove currMv = possMvs.get(i);
				
				//copy original occupant
				char curOccupant = brd.board[currMv.endingRow][currMv.endingCol];
				
				// Make move on board
				brd.makeMove(currMv);
				String brdWithMv = Util.toString(brd.board);
				if ((tt.get(brdWithMv) != null) && (tt.get(brdWithMv) >= currDepth) ) {
					brd.undoMove(currMv, curOccupant);
					continue; 
				}
				
				// Check out worth of this move
				ScoredBreakthroughMove result = timedAlphaBeta(brd, currDepth+1, mvStack, tt, alpha, beta);
				if (result == null) { return result; } // get out of here if timed out
				
				// Undo the move
				brd.undoMove(currMv, curOccupant);
				
				// Check out the results, relative to what we've seen before
				if (toMaximize && nextMove.score > bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
				} else if (!toMaximize && nextMove.score < bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
				}
				
				// Update alpha and beta. Perform pruning, if possible.
				if (!toMaximize) {
					beta = Math.min(bestMove.score, beta);
					if (bestMove.score <= alpha || bestMove.score == -MAX_EVAL_SCORE) {
						break;
					}
				} else {
					alpha = Math.max(bestMove.score, alpha);
					if (bestMove.score >= beta || bestMove.score == MAX_EVAL_SCORE) {
						break;
					}
				}
				
				if (currDepth < 4) { tt.put(brdWithMv, currDepth); }
			}
		}
		
		return mvStack.get(currDepth);
	}
	
	/**
	 * Does a iterative deepening alpha beta pruning decision search. It leaves it's
	 * move recommendation at mvStack[currDepth]. currDepth = 0 when complete...
	 * @param state current board state
	 */
	protected ScoredBreakthroughMove iterativeDeepening(EqualBreakthroughState state) {
		ScoredBreakthroughMove bestMove = null;
		startTimer = System.currentTimeMillis();
		for (int i=0; i<MAX_DEPTH; i++) {
			depthLimit = i+1; // depth must be greater than 0
			ScoredBreakthroughMove result = timedAlphaBeta(state, 0,
														   new ArrayList<ScoredBreakthroughMove>(),
														   new Hashtable<String,Integer>(),
														   Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (result == null) {
				System.out.println("Timed out at depth:"+depthLimit+" Elapsed:"+elapsedTime()+":"+DESIRED_MOVE_TIME);
				break;
			} else { 
				bestMove = result;
			
				double lapsedTime = elapsedTime();
				if (i >= lastTimeAtDepth.size()) { lastTimeAtDepth.add(i, lapsedTime); }
				else { lastTimeAtDepth.set(i, ((lapsedTime+lastTimeAtDepth.get(i))/2)); }
			
				if (stopAtDeepth(state.numMoves, lapsedTime)) { break; }
			}
		}
		return bestMove;
	}
	
	@Override
	public GameMove getMove(GameState state, String lastMv) {
		return iterativeDeepening((EqualBreakthroughState)state);
	}

	/**
	 * @param args program arguments
	 * Don't Run this one, please run IterativeDeepeningBreakthroughPlayer
	 */
	//public static void main(String[] args) {
	//	GamePlayer p = new TranspositionBreakthroughPlayer("VTC_PLAYER_AB-TR: ");
	//	p.compete(args);
	//}

}
