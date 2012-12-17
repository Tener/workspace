/**
 * 
 * @author Tanmay
 *
 */
public class OneElement {

	/**
	 * @param args
	 */
	public int key;
	public int depth;
    public Node Envelope_Node;
    public Node child;
	/**
	 * 
	 * @param key
	 * @param pointer
	 * @param depth
	 */
	public OneElement(int key, int depth, Node Envelope_Node, Node child)
	{
		this.key=key;
		this.depth=depth;
		this.Envelope_Node = Envelope_Node;
		this.child = child;
	}

	@Override
	public String toString() {
		return "OneElement [key=" + key + "]" ;
	}
	
}
