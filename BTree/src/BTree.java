import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author Tanmay
 * 
 */

public class BTree {

	protected static int degree = 2;
	
	protected static String filename = "sample2.txt";
	
	protected static Comparator<Node> comp = new Node.DepthComparator();

	protected static PriorityQueue<Node> Pr = new PriorityQueue<Node>(1000,comp);

	protected static Node searched_Node = null;

	protected static int array_of_elements[];

	protected static int start_search = 0, end_search = 0, temp = 0;

	protected static int depth = 0, search_flag = 0, MAX_DEPTH = 0,num_of_elements = 0;

	protected static int last_pointer = Integer.MAX_VALUE;

	protected static int num_of_input_elements = 0, total_elements = 0, z = 0;

	protected static int keys[] = new int[2 * degree];

	protected static ArrayList<Integer> duplicate = new ArrayList<Integer>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int choice_4_generation = 0;

		String choice;

		ArrayList<OneElement> Tree = new ArrayList<OneElement>();

		Scanner sc = new Scanner(System.in);

		//input_elements();
		
		read_input_file(filename);
		
		if(num_of_input_elements <= (2*degree))
		{
			System.out.println("More than " + (2*degree) + " " + "elements should be entered" + "\n" + "Aborting");
			System.exit(0);
		}

		for (int i = 0; i < num_of_input_elements; i++) {
			int key = generate_input_elements();
			if (!(key == 0 || checkduplicate(key))) {
				insert(key, Tree);
				num_of_elements++;
				total_elements++;
				System.out.println("Priority Queue ::");
				System.out.println(Pr);
				System.out.println();
			}
		}
		do {
			System.out.println("Insert a value::");
			int num_insert = sc.nextInt();
			final long startTime,endTime;
			if (!(checkduplicate(num_insert)|| num_insert < 0))
			{
				startTime = System.currentTimeMillis();
				insert(num_insert, Tree);
				endTime = System.currentTimeMillis();
				long timeTaken = endTime - startTime;
				System.out.println("Time Taken for insert::" +  timeTaken);
				System.out.println(Pr);
			}
			search(-10);
			delete();
			System.out
					.println(" Do you want to insert,search, or delete again(Y or N)::");
			choice = sc.next();
		} while (choice.equals("Y")||choice.equals("y"));

	}

	/**
	 * Inserts the key into the tree Populates the tree
	 * 
	 * @param key
	 * @param Tree
	 */
	private static void insert(int key, ArrayList<OneElement> Tree) {
		if (total_elements <= (2 * degree)) {
			if (num_of_elements < (2 * degree)) {
				OneElement t = new OneElement(key, depth, null, null);
				Tree.add(t);
			}

			else if (num_of_elements == (2 * degree)) {
				Collections.sort(Tree, new Comparator<OneElement>() {

					public int compare(OneElement d1, OneElement d2) {
						return Double.valueOf(d1.key)
								.compareTo((double) d2.key);
					}
				});

				OneElement t = new OneElement(key, depth, null, null);

				Tree.add(t);

				OneElement t1 = new OneElement(last_pointer--, depth, null,
						null);

				Tree.add(t1);

				Node n1 = new Node(Tree, depth, false, null, null);

				setEnvelope(n1);

				MAX_DEPTH = 0;

				Pr.add(n1);
				total_elements++;
				checkSplit(key, n1);

				num_of_elements = -1;
			}
		} else if (total_elements > (2 * degree)) {
			Iterator<Node> it = Pr.iterator();
			ArrayList<ListofKeys> list = new ArrayList<ListofKeys>();
			while (it.hasNext()) {
				Node n1 = (Node) it.next();
				if (n1.depth == MAX_DEPTH) {
					System.out.println("Nodes at depth of " + MAX_DEPTH);
					System.out.println(n1);
					for (int i = 0; i < n1.one.size() - 1; i++) {
						ListofKeys ls = new ListofKeys(n1.one.get(i).key, n1);
						list.add(ls);
					}
				}
			}
			Collections.sort(list, new Comparator<ListofKeys>() {

				public int compare(ListofKeys d1, ListofKeys d2) {
					return Double.valueOf(d1.key).compareTo((double) d2.key);
				}
			});
			if (key < list.get(0).key) {
				Node p = list.get(0).me_node;
				OneElement oel = new OneElement(key, list.get(0).me_node.depth,
						null, null);
				p.one.add(oel);
				setEnvelope(p);
				sortOneElement(p);
				checkSplit(key, p);
				return;
			} else if (key > list.get((list.size() - 1)).key) {
				Node p = list.get((list.size() - 1)).me_node;
				OneElement oel = new OneElement(key,
						list.get((list.size() - 1)).me_node.depth, null, null);
				p.one.add(oel);
				setEnvelope(p);
				sortOneElement(p);
				checkSplit(key, p);
			} else {
				for (int i = 0; i < list.size(); i++) {
					if (key > list.get(i).key && key < list.get(i + 1).key) {
						Node p = list.get(i).me_node;
						Node p1 = list.get(i + 1).me_node;
						if (p.equals(p1)) {
							OneElement oel = new OneElement(key,
									list.get(i).me_node.depth, null, null);
							p.one.add(oel);
							setEnvelope(p);
							sortOneElement(p);
							checkSplit(key, p);
							return;
						} else if (p.Parent.key < key) {
							OneElement oel = new OneElement(key,
									list.get(i + 1).me_node.depth, null, null);
							p1.one.add(oel);
							setEnvelope(p1);
							sortOneElement(p1);
							checkSplit(key, p1);
							return;
						} else if (p.Parent.key > key) {
							OneElement oel = new OneElement(key,
									list.get(i).me_node.depth, null, null);
							p.one.add(oel);
							setEnvelope(p);
							sortOneElement(p);
							checkSplit(key, p);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Sets the Envelope node for OneElement
	 * 
	 * @param n
	 */

	private static void setEnvelope(Node n) {
		Iterator<OneElement> it = n.one.iterator();
		while (it.hasNext()) {
			OneElement oe = (OneElement) it.next();
			oe.Envelope_Node = n;
		}
	}

	/**
	 * Checks if duplicate value is being generated
	 * 
	 * @param n1
	 * @return
	 */

	private static boolean checkduplicate(int key) {
		if (duplicate.isEmpty()) {
			duplicate.add(key);
			return false;
		} else {
			// System.out.println(duplicate);
			Iterator it = duplicate.iterator();
			while (it.hasNext()) {
				int key1 = (Integer) it.next();
				if (key1 == key) {
					System.out.println("Duplicate Entry");
					Collections.sort(duplicate);
					return true;
				}
			}

			duplicate.add(key);
			Collections.sort(duplicate);
			return false;
		}
	}

	/**
	 * Sorts out the elements of the node
	 * 
	 * @param n1
	 */

	private static void sortOneElement(Node n1) {
		if (n1.one.size() > (2 * degree + 1))
			n1.isEmpty = false;

		Collections.sort(n1.one, new Comparator<OneElement>() {

			public int compare(OneElement d1, OneElement d2) {
				return Double.valueOf(d1.key).compareTo((double) d2.key);
			}
		});
	}

	/**
	 * Checks for the split and splits the node if the size exceeds (2*degree+1)
	 * 
	 * @param key
	 * @param p
	 */

	private static void checkSplit(int key, Node child2) {
		ArrayList<OneElement> aoe = new ArrayList<OneElement>();

		if (child2.one.size() > (2 * degree + 1)) {

			int mid_key_pos = (int) Math.floor((child2.one.size() - 1) / 2);

			int mid_key = child2.one.get(mid_key_pos).key;

			Iterator<OneElement> it = child2.one.iterator();

			while (it.hasNext()) {

				OneElement e = (OneElement) it.next();

				if (e.key >= mid_key) {
					aoe.add(e);
					it.remove();
				}
			}

			OneElement t = new OneElement(last_pointer--, depth, null, null);

			child2.one.add(t);

			setEnvelope(child2);

			Node child1 = new Node(aoe, child2.depth, true, null, null);

			child2.Neighbor = child1;

			setEnvelope(child1);

			Pr.add(child1);

			Node Parent = null;

			if (child2.Parent != null)
				Parent = (Node) child2.Parent.Envelope_Node;

			if (Parent == null) {
				ArrayList<OneElement> aone1 = new ArrayList<OneElement>();

				OneElement oe = new OneElement(mid_key, 0, null, null);

				aone1.add(oe);

				OneElement t1 = new OneElement(last_pointer--, depth, null,
						null);

				aone1.add(t1);

				Node n = new Node(aone1, 0, true, null, child2.Parent);

				setEnvelope(n);

				Pr.add(n);

				for (int i = 0; i < aoe.size(); i++) {
					aoe.get(i).depth = aoe.get(i).depth + 1;
				}

				for (int k = 0; k < child2.one.size(); k++) {
					child2.one.get(k).depth = child2.one.get(k).depth + 1;
				}

				oe.child = child2;

				t1.child = child1;

				child2.Parent = oe;

				child1.Parent = t1;

				child2.depth = child2.depth + 1;

				child1.depth = child1.depth + 1;

				MAX_DEPTH = MAX_DEPTH + 1;

				return;
			}

			else if (Parent.one.size() <= (2 * degree)) {

				OneElement oe = new OneElement(mid_key, Parent.depth, null,
						null);

				Parent.one.add(oe);

				setEnvelope(Parent);

				sortOneElement(Parent);

				System.out.println("Parent Node " + Parent);

				System.out.println("Size of Parent Node " + Parent.one.size());

				child2.Parent = oe;

				oe.child = child2;

				Iterator<OneElement> it_put = Parent.one.iterator();
				while (it_put.hasNext()) {
					OneElement one_put = it_put.next();
					if (one_put.key == mid_key) {
						one_put = it_put.next();
						child1.Parent = one_put;
						one_put.child = child1;
					}
				}
				return;
			} else if (Parent.one.size() > (2 * degree)) {

				OneElement oe = new OneElement(mid_key, Parent.depth, null,
						null);

				Parent.one.add(oe);

				setEnvelope(Parent);

				sortOneElement(Parent);

				child2.Parent = oe;
				child1.Parent = Parent.one.get(Parent.one.size() - 1);
				oe.child = child2;
				Parent.one.get(Parent.one.size() - 1).child = child1;
				Node change = Parent;
				Iterator<Node> it_check_depth = Pr.iterator();
				while (it.hasNext()) {
					Node root = it_check_depth.next();
					if (root.depth == 0) {
						if (root.one.size() >= (2 * degree + 1)) {
							change_depth(change);
							break;
						} else
							break;
					}
				}
				if (total_elements == (2 * degree + 1))
					change_depth(change);
				checkSplit(mid_key, Parent);
			}
		}
	}

	/**
	 * 
	 * @param Parent
	 * @return
	 */
	private static void change_depth(Node Parent) {
		Iterator<OneElement> it_or = Parent.one.iterator();
		while (it_or.hasNext()) {
			OneElement n = it_or.next();
			Node child = n.child;
			if (child != null)
				child.depth = child.depth + 1;
			else if (child == null)
				break;
			change_depth(child);
		}

	}

	/**
	 * Delete a value from the tree
	 * 
	 * @return void
	 */

	private static void delete() {
		System.out.println("Enter the element to be deleted::");
		Scanner sc = new Scanner(System.in);
		int deletion_item = sc.nextInt();
		final long startTime = System.currentTimeMillis();
		search(deletion_item);
		final long endTime = System.currentTimeMillis();
		long timetaken = endTime - startTime;
		System.out.println("Time Taken for deletion::"+timetaken);
	}

	/**
	 * Search a value in the tree
	 * 
	 * @param element
	 * @returns a boolean value
	 */
	private static boolean search(int element) {
		
		Node se = null;
		final long startTime,endTime;
		int searchable = 0;

		if (element <= 0) {
			System.out.println(" Enter the element to be searched::");
			Scanner in = new Scanner(System.in);
			searchable = in.nextInt();

		} else
			searchable = element;

		Iterator<Node> it = Pr.iterator();
		startTime = System.currentTimeMillis();
		while (it.hasNext()) {
			Node n = it.next();
			if (n.depth == 0) {
				if (element <= 0) {
					search_flag = 0;
					se = search_element(searchable, n);
					endTime = System.currentTimeMillis();
					temp = searchable;
					System.out.println("Time taken for search::" + (long)(endTime - startTime));
					System.out.println(searched_Node);
				} else {
					search_flag = 0;
					se = search_element(searchable, n, true);
					System.out.println(" Tree " + Pr);
				}

				break;
			}
		}
		return false;
	}

	/**
	 * Search function for deletion
	 * 
	 * @param searchable
	 * @param child
	 * @param deletion
	 * @return the searched Node in which the delete element lies
	 */
	private static Node search_element(int searchable, Node child,
			boolean deletion) {
		Node parent, child_neighbor = null;
		for (int i = 0; i < child.one.size() - 1; i++) {
			if (search_flag == 0 && searchable == child.one.get(i).key
					&& child.depth == MAX_DEPTH) {
				System.out.println("Element found and deleting");
				if(duplicate.contains(child.one.get(i).key))
				{
					for(int  k =0; k <duplicate.size(); k++)
					{
						if(duplicate.get(k) == child.one.get(i).key)
							{
								duplicate.remove(k);
								break;
							}
					}
				}
				search_flag = 1;
				if (child.one.size() > (degree + 1)) {
					child.one.remove(i);
				} else if (child.one.size() <= (degree + 1)) {
					OneElement parent_one = child.Parent;
					parent = parent_one.Envelope_Node;
					if (child.Neighbor != null) {
						child.one.remove(i);
						for (int k = 0; k < child.one.size() - 1; k++) {
							OneElement e = child.one.remove(k);
							child.Neighbor.one.add(e);
							e.Envelope_Node = child.Neighbor;
						}
						sortOneElement(child.Neighbor);
						checkSplit(0, child.Neighbor);
						System.out.println(Pr);
					} else {
						for (int j = 0; j < parent.one.size(); j++) {
							if (parent.one.get(j).key == parent_one.key) {
								child_neighbor = parent.one.get(j - 1).child;
								child.one.remove(i);
								for (int k = 0; k < child.one.size() - 1; k++) {
									OneElement e = child.one.remove(k);
									e.Envelope_Node = child_neighbor;
									child_neighbor.one.add(e);
									sortOneElement(child_neighbor);
									checkSplit(0, child_neighbor);
								}
							}
						}
					}
					parent.one.remove(parent_one);
				}
				searched_Node = child;
				break;
			} else if (search_flag == 0 && searchable < child.one.get(0).key) {
				Node child1 = child.one.get(i).child;
				if (child1 == null)
					break;
				search_element(searchable, child1, true);
			} else if (search_flag == 0 && (child.one.get(i).key <= searchable)
					&& (searchable < child.one.get(i + 1).key)) {
				Node child1 = child.one.get(i + 1).child;
				if (child1 == null) {
					child1 = child.Neighbor;
					if (child1 == null)
						break;
				}
				search_element(searchable, child1, true);
			}
		}
		return null;
	}

	/**
	 * Recursively searches for the child node and the element in that
	 * 
	 * @param searchable
	 * @param child
	 * @return Node, the Node in which the searched element lies
	 */
	private static Node search_element(int searchable, Node child) {
		for (int i = 0; i < child.one.size() - 1; i++) {
			if (search_flag == 0 && searchable == child.one.get(i).key
					&& child.depth == MAX_DEPTH) {
				System.out.println("Element found");
				search_flag = 1;
				searched_Node = child;
				break;
			} else if (search_flag == 0 && searchable < child.one.get(0).key) {
				Node child1 = child.one.get(i).child;
				if (child1 == null)
					break;
				search_element(searchable, child1);
			} else if (search_flag == 0 && (child.one.get(i).key <= searchable)
					&& (searchable < child.one.get(i + 1).key)) {
				Node child1 = child.one.get(i + 1).child;
				if (child1 == null) {
					child1 = child.Neighbor;
					if (child.Neighbor == null)
						break;
				}
				search_element(searchable, child1);
			}
		}
		return null;
	}

	/**
	 * Generates random numbers to be inserted
	 * 
	 * @return for random number generation
	 */

	private static void generate_elements() {
		// System.out.println("Generating a random elements");
		System.out
				.println("Enter the number of elements you want to be entered::");
		Scanner in = new Scanner(System.in);
		num_of_input_elements = in.nextInt();
		array_of_elements = new int[num_of_input_elements];
		for (int k = 0; k < num_of_input_elements; k++) {
			int i = (int) ((Math.random() * 10.0));
			int random = (int) (Math.random() * 10.0 * i);
			System.out.println(random);
			array_of_elements[k] = random;
		}
	}

	/**
	 * Creation of tree Input the values manually into the tree
	 * 
	 */
	private static void input_elements() {
		System.out.println("Enter the number of elements you want to be entered::");
		Scanner in = new Scanner(System.in);
		num_of_input_elements = in.nextInt();
		array_of_elements = new int[num_of_input_elements];
		for (int i = 0; i < num_of_input_elements; i++) {
			System.out.println("Input the element::");
			Scanner le = new Scanner(System.in);
			int element = in.nextInt();
			array_of_elements[i] = element;
		}
	}

	
	/**
	 * Reads from the sample input file
	 * file
	 */
	private static void read_input_file(String file)
	{
		int k=0;
		String line = null;
		BufferedReader reader = null;
		FileInputStream fis = null;
		File f = new File(filename);
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		reader = new BufferedReader(new InputStreamReader(fis));
		try {
			line = reader.readLine();
			line.trim();
			num_of_input_elements = Integer.parseInt(line);
			array_of_elements = new int[num_of_input_elements];
			while((line = reader.readLine()) != null)
			{
				line.trim();
				array_of_elements[k++] = Integer.parseInt(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pass on the elements inputed one by one to the insert function
	 */
	protected static int generate_input_elements() {
		return array_of_elements[z++];
	}
}