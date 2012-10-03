

import java.util.ArrayList;

public class Node  {

	public int row;
	public int col;
	public int depth;
	public double cost;
	public Node parent;
	public String dir;
	public ArrayList<Integer> Message_Obtained=new ArrayList<Integer>();
	public Node(int r, int c,int dep,double cos,Node Parent,ArrayList<Integer> G,String direction)
	{
		row = r;
		col = c;
		depth=dep;
		cost=cos;
		parent=Parent;
		dir=direction;
		Message_Obtained=G;
	}
	public String toString()
	{
		return String.format("%d %d %f", row, col, cost);
	}
}