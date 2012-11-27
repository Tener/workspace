import java.util.ArrayList;


public class BPTree {

	protected static ArrayList<Node> Tree = new ArrayList<Node>();
	
	protected static int num_of_elements = 0;
	
	protected static int degree = 2;
	
	protected static int keys[] = new int[2*degree];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		insert(12);
	}
	
	/**
	 * 
	 * @param key
	 */
	private static void insert(int key)
	{
		if(num_of_elements <= (2*degree))
		{
			keys[num_of_elements] = key;
		}
		
		if(num_of_elements == (2*degree))
		{
			Node element = new Node(keys,null);
			Tree.add(element);
			num_of_elements = -1;
		}
		
		num_of_elements++;
	}
	
	void delete()
	{
		
	}
	
	void search()
	{
		
	}

}
