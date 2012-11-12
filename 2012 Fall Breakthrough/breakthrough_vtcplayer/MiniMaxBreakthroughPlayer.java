package breakthrough_vtcplayer;

import java.util.ArrayList;
import java.util.Collections;

import breakthrough.*;
import game.*;

/**
 * A Game player that uses mini max
 * to play breakthrough.
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class MiniMaxBreakthroughPlayer extends GamePlayer {
	public static final int MAX_EVAL_SCORE = (((BreakthroughState.N*(BreakthroughState.N-1)) + (BreakthroughState.N*(BreakthroughState.N-2))) - 1);
	protected int depthLimit;
	
	public MiniMaxBreakthroughPlayer(String nickname, int depthLimit) {
		super(nickname, new EqualBreakthroughState(), false);
		this.depthLimit = depthLimit;
	}

	/**
	 * Initializes mvStack.
	 */
	public void init() {
	}
	
	/**
	 * Is the game complete at this brd state?  If it is, the
	 * evaluation values for these boards is recorded (i.e., 0 for a draw
	 * +X, for a HOME win and -X for an AWAY win.
	 * @param brd Breakthrough board to be examined
	 * @param mv where to place the score information; move is irrelevant
	 * @return true if the brd is in a terminal state
	 */
	protected boolean terminalValue(GameState brd, ScoredBreakthroughMove mv){
		GameState.Status status = brd.getStatus();
		boolean isTerminal = true;
		
		if (status == GameState.Status.HOME_WIN) {
			mv.set(MAX_EVAL_SCORE);
		} else if (status == GameState.Status.AWAY_WIN) {
			mv.set(- MAX_EVAL_SCORE);
		} else if (status == GameState.Status.DRAW) {
			mv.set(0);
		} else {
			isTerminal = false;
		}
		return isTerminal;
	}
	
	/**
	 * Counts the number of spaces away from opponents home row for each piece.
	 * @param brd board to be evaluated
	 * @param who
	 * @return score
	 *
	private static int eval(BreakthroughState brd, char who) {
		return 0;
	}*/
	
	/**
	 * The heuristic evaluation function that determines the chances of winning...
	 * @param brd board to be evaluated
	 * @return score that indicates our chances of winning
	 */
	protected static int evalBoard(BreakthroughState brd) {
		int score = 0;
		for (int r=0; r<BreakthroughState.N; r++) {
			for (int c=0; c<BreakthroughState.N; c++) {
				if (brd.board[r][c] == BreakthroughState.homeSym) {
					score += (r+1);
				} else if (brd.board[r][c] == BreakthroughState.awaySym) {
					score -= (BreakthroughState.N-r);
				}
			}
		}
		
		if (Math.abs(score) > MAX_EVAL_SCORE) {
			System.err.println("Problem with eval");
			System.exit(0);
		}
		return score;
	}
	
	/**
	 * Get an ArrayList of all possible moves that we can make given state.
	 * @param state
	 * @return
	 */
	protected ArrayList<ScoredBreakthroughMove> getPossibleMoves(GameState state) {
		BreakthroughState board = (BreakthroughState)state;
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
		return list;
	}
	
	/**
	 * Does a depth limited minimax decision search.
	 * @param brd current board state
	 * @param currDepth current depth in the search
	 * @param mvStack new ArrayList
	 * @return move recommendation
	 */
	private ScoredBreakthroughMove minimax(EqualBreakthroughState brd, int currDepth, ArrayList<ScoredBreakthroughMove> mvStack) {
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
				minimax(brd, currDepth+1, mvStack);
				
				// Undo the move
				brd.undoMove(currMv, curOccupant);
				
				// Check out the results, relative to what we've seen before
				if (toMaximize && nextMove.score > bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
				} else if (!toMaximize && nextMove.score < bestMove.score) {
					bestMove.set(currMv.startRow, currMv.startCol, currMv.endingRow, currMv.endingCol, nextMove.score);
				}
			}
		}
		
		return mvStack.get(currDepth);
	}
	
	@Override
	public GameMove getMove(GameState state, String lastMv) {
		return minimax((EqualBreakthroughState)state, 0, new ArrayList<ScoredBreakthroughMove>());
	}

	/**
	 * @param args program arguments
	 * Don't Run this one, please run IterativeDeepeningBreakthroughPlayer
	 */
	//public static void main(String[] args) {
	//	int depth = 5;
	//	GamePlayer p = new MiniMaxBreakthroughPlayer("VTC_PLAYER_MM: " + depth, depth);
	//	p.compete(args);
	//}

}
