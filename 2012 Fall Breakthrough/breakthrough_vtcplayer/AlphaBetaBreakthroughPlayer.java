package breakthrough_vtcplayer;

import java.util.ArrayList;
import java.util.Collections;

import breakthrough.*;
import game.*;

/**
 * A Game player that uses alpha beta pruning
 * to play breakthrough.
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class AlphaBetaBreakthroughPlayer extends MiniMaxBreakthroughPlayer {

	public AlphaBetaBreakthroughPlayer(String nickname, int depthLimit) {
		super(nickname, depthLimit);
	}
	
	/**
	 * Does a depth limited alpha beta pruning decision search.
	 * @param brd current board state
	 * @param currDepth current depth in the search
	 * @param mvStack new ArrayList
	 * @return move recommendation
	 */
	protected ScoredBreakthroughMove alphaBeta(EqualBreakthroughState brd, int currDepth, ArrayList<ScoredBreakthroughMove> mvStack, double alpha, double beta) {
		if (currDepth >= mvStack.size()) { mvStack.add(new ScoredBreakthroughMove()); } // auto grow
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean isTerminal = terminalValue(brd, mvStack.get(currDepth));
		
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
				
				// Check out worth of this move
				alphaBeta(brd, currDepth+1, mvStack, alpha, beta);
				
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
			}
		}
		
		return mvStack.get(currDepth);
	}
	
	@Override
	public GameMove getMove(GameState state, String lastMv) {
		return alphaBeta((EqualBreakthroughState)state, 0, new ArrayList<ScoredBreakthroughMove>(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * @param args program arguments
	 * Don't Run this one, please run IterativeDeepeningBreakthroughPlayer
	 */
	//public static void main(String[] args) {
	//	int depth = 8;
	//	GamePlayer p = new AlphaBetaBreakthroughPlayer("VTC_PLAYER_AB: " + depth, depth);
	//	p.compete(args);
	//}

}
