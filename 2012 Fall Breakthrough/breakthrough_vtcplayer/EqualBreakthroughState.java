package breakthrough_vtcplayer;

import game.GameMove;
import game.GameState;
import game.Util;
import breakthrough.BreakthroughMove;
import breakthrough.BreakthroughState;

/**
 * A BreakthroughState that has an equals method as well as other
 * enhancements...
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class EqualBreakthroughState extends BreakthroughState {

	public EqualBreakthroughState() {
		super();
	}

	public void undoMove(GameMove m, char curOccupant)
	{
		BreakthroughMove mv = (BreakthroughMove)m;
		char PLAYER = who == GameState.Who.HOME ? awaySym : homeSym;

		board[mv.startRow][mv.startCol] = PLAYER;
		board[mv.endingRow][mv.endingCol] = curOccupant;
		
		numMoves--;
		togglePlayer();
		status = GameState.Status.GAME_ON;
	}
	
	public boolean equals(Object obj) {
		EqualBreakthroughState other = (EqualBreakthroughState)obj;
		for(int i=0;i < BreakthroughState.N;i++)
		{
			for(int j=0;j<BreakthroughState.N;j++)
			{
				if(!(this.board[i][j] == other.board[i][j]))
					return false;
			}
		}
		return true;
	}
	
	public Object clone(){
		EqualBreakthroughState res = new EqualBreakthroughState();
		res.copyInfo(this);
		Util.copy(res.board, board);
		return res;
	}
}
