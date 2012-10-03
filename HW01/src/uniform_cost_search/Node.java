package uniform_cost_search;
import java.util.ArrayList;
import java.util.Comparator;

public class Node  {

	public int row,col,depth;
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
		return(UCS.Max_Row-row + "," + col + "," + cost + "," + dir +"," + Message_Obtained);
	}
	public static class CostComparator implements Comparator<Node> {
		public int compare(Node a, Node b)
		{
			return (int) ((int) (a.cost) - (b.cost));
		}
	}
	public static class ColComparator implements Comparator<Node> {
		public int compare(Node a, Node b)
		{
			return 0;
		}
	}
	public static class DepthComparator implements Comparator<Node> {
		public int compare(Node a, Node b)
		{
			return a.depth-b.depth;
		}
}
}