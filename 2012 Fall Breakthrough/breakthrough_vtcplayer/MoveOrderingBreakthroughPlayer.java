package breakthrough_vtcplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Comparator;

import breakthrough.*;
import game.*;

/**
 * A Game player that uses iterative deepening alpha beta pruning with
 * move ordering 
 * to play breakthrough.
 * 
 * Does not work for deeper levels (greater than like 5).
 * It consumes too much memory...  Fix me if you have time please.
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class MoveOrderingBreakthroughPlayer extends IterativeDeepeningBreakthroughPlayer {
	public static class MinComparator implements Comparator<ScoredBreakthroughMove> {
		public int compare(ScoredBreakthroughMove a, ScoredBreakthroughMove b)
		{
			if (a.score < b.score) return -1;
	        if (a.score > b.score) return 1;
	        return 0;
		}
	}
	public static class MaxComparator implements Comparator<ScoredBreakthroughMove> {
		public int compare(ScoredBreakthroughMove a, ScoredBreakthroughMove b)
		{
			if (a.score < b.score) return 1;
	        if (a.score > b.score) return -1;
	        return 0;
		}
	}
	protected Hashtable<String,ArrayList<ScoredBreakthroughMove>> possibleMoves;
	
	
	public MoveOrderingBreakthroughPlayer(String nickname) {
		super(nickname);
	}
	
	/**
	 * Get an ArrayList of all possible moves that we can make given state.
	 * @param state
	 * @return
	 */
	protected ArrayList<ScoredBreakthroughMove> getPossibleMoves(GameState state, int currDepth) {
		BreakthroughState board = (EqualBreakthroughState)state;
		if (possibleMoves.get(Util.toString(board.board)) != null) {
			System.out.println("Matched board:\n" +Util.toString(board.board)+"\nEND");
			return possibleMoves.get(Util.toString(board.board));
		}
		
		ArrayList<ScoredBreakthroughMove> list = new ArrayList<ScoredBreakthroughMove>();
		ScoredBreakthroughMove mv = new ScoredBreakthroughMove();
		int dir = state.who == GameState.Who.HOME ? +1 : -1;
		for (int r=0; r<BreakthroughState.N; r++) {
			for (int c=0; c<BreakthroughState.N; c++) {
				mv.startRow = r;
				mv.startCol = c;
				mv.endingRow = r+dir; mv.endingCol = c;
				if (board.moveOK(mv)) {
					list.add((ScoredBreakthroughMove)mv.clone());
				}
				mv.endingRow = r+dir; mv.endingCol = c+1;
				if (board.moveOK(mv)) {
					list.add((ScoredBreakthroughMove)mv.clone());
				}
				mv.endingRow = r+dir; mv.endingCol = c-1;
				if (board.moveOK(mv)) {
					list.add((ScoredBreakthroughMove)mv.clone());
				}
			}
		}
		
		if (currDepth < 4) { possibleMoves.put(Util.toString(board.board), list); }
		return list;
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
			
			ArrayList<ScoredBreakthroughMove> possMvs = getPossibleMoves(brd, currDepth);
			if (toMaximize) { Collections.sort(possMvs, new MaxComparator()); }
			else { Collections.sort(possMvs, new MinComparator()); }
			
			//for(int i=0;i<possMvs.size();i++)
			//{ System.out.println(possMvs.get(i).score);}
			//System.out.println("Current Board:\n" +Util.toString(brd.board)+"\nEND");
			for (int i=0; i<possMvs.size(); i++) {
				ScoredBreakthroughMove currMv = possMvs.get(i);
				
				//copy original occupant
				char curOccupant = brd.board[currMv.endingRow][currMv.endingCol];
				
				// Make move on board
				brd.makeMove(currMv);
				
				// Check out worth of this move
				ScoredBreakthroughMove result = timedAlphaBeta(brd, currDepth+1, mvStack, alpha, beta);
				if (result == null) { return result; } // get out of here if timed out
				
				// Undo the move
				brd.undoMove(currMv, curOccupant);
				
				// Check out the results, relative to what we've seen before
				if (toMaximize && nextMove.score > bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
					currMv.set(nextMove.score);
				} else if (!toMaximize && nextMove.score < bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
					currMv.set(nextMove.score);
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
		possibleMoves = new Hashtable<String,ArrayList<ScoredBreakthroughMove>>();
		for (int i=0; i<MAX_DEPTH; i++) {
			depthLimit = i+1; // depth must be greater than 0
			ScoredBreakthroughMove result = timedAlphaBeta(state, 0,
														   new ArrayList<ScoredBreakthroughMove>(),
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
	//	GamePlayer p = new MoveOrderingBreakthroughPlayer("VTC_PLAYER_AB-MO: ");
	//	p.compete(args);
	//}

}
