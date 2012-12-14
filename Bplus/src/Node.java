import java.util.ArrayList;
import java.util.Comparator;


public class Node {

	
	public int depth;
	public boolean isEmpty;
    public ArrayList<OneElement> one = new ArrayList<OneElement>();
    public Node Neighbor;
    public Node child;
	public OneElement Parent;
	/**
	 * 
	 * @param key
	 * @param pointer
	 * @param depth
	 */
	public Node(ArrayList<OneElement> one, int depth, boolean isEmpty, Node Neighbor, OneElement Parent )
	{
		this.one = one;
		this.depth = depth;
		this.Neighbor = Neighbor;
		this.isEmpty = isEmpty;
		this.Parent=Parent;
		
	}
	
	

	@Override
	public String toString() {
		return "Node [depth=" + depth + ", one=" + one + ", Parent=" + Parent
				+ "]";
	}



	public static class DepthComparator implements Comparator<Node>
	{
		public int compare(Node o1, Node o2) 
		{
			return o2.depth - o1.depth;
		}
		
	}
}	

