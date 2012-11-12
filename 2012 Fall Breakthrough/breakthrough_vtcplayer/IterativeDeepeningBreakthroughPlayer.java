package breakthrough_vtcplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import breakthrough.*;
import game.*;

/**
 * A Game player that uses iterative deepening alpha beta pruning
 * to play breakthrough.
 * Uses timeouts and keeps track of its remaining game time to make moves,
 * in an effort to never time out and get DQed.
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class IterativeDeepeningBreakthroughPlayer extends AlphaBetaBreakthroughPlayer {
	public static final Params iterativeParams = new Params("breakthrough_vtcplayer" + File.separatorChar + "config" + File.separatorChar + "breakthrough.txt");
	// What we know about the environment
	protected static final int GAME_LIMIT = BreakthroughState.gameParams.integer("GAMETIME");
	protected static final int FINAL_PLAY_LIMIT = BreakthroughState.gameParams.integer("MAXMOVETIME");
	protected static final int MOVE_LIMIT = BreakthroughState.gameParams.integer("MOVETIME");
	
	// Our knobs and levers - adjust to suit
	protected static final int MAX_MOVES = ( (BreakthroughState.N*(BreakthroughState.N-2)) + (BreakthroughState.N*(BreakthroughState.N-1)) ); // Max moves a game can take
	protected static final int APPROX_MOVES = (int)(MAX_MOVES * Double.parseDouble(iterativeParams.string("APPROX_MOVES"))); // Guess how many moves a game will take - less than MAX_MOVES
	protected static final double DESIRED_MOVE_TIME = (double)GAME_LIMIT/APPROX_MOVES; // Guess how much time to spend on a move
	protected static final int MOVE_BUFFER = ( (MAX_MOVES - APPROX_MOVES) ); // survive this many moves more than APPROX_MOVES
	protected static final int MIN_DEPTH = iterativeParams.integer("MIN_DEPTH"); // always go at least this deep
	protected static final int MAX_DEPTH = iterativeParams.integer("MAX_DEPTH"); // never go deeper 
	
	// record/state keeping - reset each game...
	protected double gameTimeRemaining;
	protected ArrayList<Double> lastTimeAtDepth;
	protected long startTimer;
	
	public IterativeDeepeningBreakthroughPlayer(String nickname) {
		super(nickname, 1); // Alpha Beta needs to go to at least depth 1
	}

	/**
	 * This is called at the start of a new game. This should be relatively
	 * fast, as the player must be ready to respond to a move request, which
	 * should come shortly thereafter. The side being played (HOME or AWAY)
	 * is stored in the side data member. Default behavior is to do nothing. 
	 * @param opponent Name of the opponent being played
	 */
	public void startGame(String opponent) {
		gameTimeRemaining = (double)GAME_LIMIT;
		lastTimeAtDepth = new ArrayList<Double>();
	}
	
	/**
	 * Called to inform the player how long the last move took. This can
	 * be used to calibrate the player's search depth. Default behavior is
	 * to do nothing.
	 * @param secs Time for the server to receive the last move
	 */
	public void timeOfLastMove(double secs) {
		gameTimeRemaining -= secs;
		System.out.println("Time Left:"+gameTimeRemaining);
	}
	
	/**
	 * Decide if we should go to the next depth in the tree.
	 * Based on time constraints and move number...so far
	 * @param numMoves
	 * @param elapsedTime
	 * @return true if we should stop iterating at this depth, else false
	 */
	protected boolean stopAtDeepth(int numMoves, double lapsedTime) {
		System.out.println("Depth:" + depthLimit + " Moves:" + numMoves +" Elapsed Time:"+lapsedTime);
		int i = (depthLimit-1); // depth must be greater than 0
		double elapsedAfterNext = ((i+1) >= lastTimeAtDepth.size()) ? // have we been at next depth?
								  lapsedTime+lastTimeAtDepth.get(i) : // no - then use current depth to guess - has to be greater
								  lapsedTime+lastTimeAtDepth.get((i+1)); // yes - then use max time used at that depth
		
		// rules to determine if we should continue
		
		// only go to depth 2 on first 2 moves
		if ((numMoves <= 3) && (depthLimit >= 2)){ System.out.println("Initial moves"); return true; }
		
		// always go to depth MIN_DEPTH
		if (depthLimit < MIN_DEPTH) { System.out.println("Always go depth:"+MIN_DEPTH); return false; }
		
		// never exceed max sec minus buffer per move
		if ( (elapsedAfterNext > (Math.min(gameTimeRemaining, FINAL_PLAY_LIMIT) - MOVE_BUFFER)) ||
			 (elapsedAfterNext > (MOVE_LIMIT - MOVE_BUFFER)) )
		{ System.out.println("Reaching GAME LIMIT"); return true; }
		
		// stop after exceeding DESIRED_MOVE_TIME
		if (lapsedTime > DESIRED_MOVE_TIME) { System.out.println("Will use DESIRED MOVE TIME:"+lapsedTime+":"+DESIRED_MOVE_TIME); return true; }
		
		// default if no rule matches - go deeper
		return false;
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
			
			ArrayList<ScoredBreakthroughMove> possMvs = getPossibleMoves(brd);
			Collections.shuffle(possMvs);
			for (int i=0; i<possMvs.size(); i++) {
				BreakthroughMove currMv = possMvs.get(i);
				
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
	
	/**
	 * How much time has passed?
	 * @return
	 */
	protected double elapsedTime() {
		long diff = System.currentTimeMillis() - startTimer; 
		return diff / 1000.0;
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
	 * Run this one please.   This is our best player.
	 */
	public static void main(String[] args) {
		GamePlayer p = new IterativeDeepeningBreakthroughPlayer("VTC_PLAYER_AB-ID: ");
		p.compete(args);
	}

}
