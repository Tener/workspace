package breakthrough_vtcplayer;

import breakthrough.BreakthroughMove;

/**
 * A BreakthroughMove that also contains a score for how well
 * it evaluates...
 * 
 * @author Chris Edeste, Tanmay Mathur, Srivatsan Varadharajan
 * CSE 486-586
 * Breakthrough
 * Game Project
 *
 */
public class ScoredBreakthroughMove extends BreakthroughMove {
	public double score;
	
	/**
	 * Regular constructor - set everything
	 * @param r1 startRow
	 * @param c1 startCol
	 * @param r2 endingRow
	 * @param c2 endingCol
	 * @param score the eval score that tells how good this move is
	 */
	public ScoredBreakthroughMove(int r1, int c1, int r2, int c2, double score) {
		super(r1, c1, r2, c2);
		this.score = score;
	}

	/**
	 * Constructor - only set score; everything else 0
	 * @param score the eval score that tells how good this move is
	 */
	public ScoredBreakthroughMove(double score) {
		super(0, 0, 0, 0);
		this.score = score;
	}
	
	/**
	 * Constructor - everything 0
	 */
	public ScoredBreakthroughMove() {
		super(0, 0, 0, 0);
		this.score = 0;
	}
	
	/**
	 * Set everything
	 * @param r1 startRow
	 * @param c1 startCol
	 * @param r2 endingRow
	 * @param c2 endingCol
	 * @param score the eval score that tells how good this move is
	 */
	public void set(int r1, int c1, int r2, int c2, double score) {
		this.startRow = r1;
		this.startCol = c1;
		this.endingRow = r2;
		this.endingCol = c2;
		this.score = score;
	}
	
	/**
	 * Just set the score
	 * @param score the eval score that tells how good this move is
	 */
	public void set(double score) {
		this.score = score;
	}
	
	public Object clone() { 
		return new ScoredBreakthroughMove(startRow, startCol, endingRow, endingCol, score); 
	}
	
	public String toMyString() { 
		return startRow + " " + startCol + " " + endingRow + " " + endingCol + " " + score;
	}

}
