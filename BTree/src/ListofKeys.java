/**
 * 
 * @author Tanmay
 *
 */
public class ListofKeys {

	protected int key;
	protected Node me_node;

	/**
	 * 
	 * @param key
	 * @param Parent
	 */
	public ListofKeys(int key, Node me_node)
	{
		this.key = key;
		this.me_node = me_node;
	}

	@Override
	public String toString() {
		return "ListofKeys [key=" + key + ", Me_Node=" + me_node + "]";
	}
	
}
