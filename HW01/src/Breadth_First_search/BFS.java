package Breadth_First_search;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


public class BFS
{
	static char Graph[][]=new char[40][20];//Stores the graph
	static ArrayList<Integer> Goals=new ArrayList<Integer>();//Stores the goals{1,2,3,4}
	public static ArrayList<Integer> Message_Obtained=new ArrayList<Integer>();//Stores the Goals reached per node
	static String direction="";//Stores the direction each node is taking
	static int Max_Row,startx,starty,length,Max_Col;
	static double map_cost[][]=new double[40][20];//cost of each child
	static int goal_pos[][]=new int[40][20]; //Position of each goal
	static Queue<Node> open = new LinkedList<Node>();//Open list of type node 
	static Queue<Node> closed= new LinkedList<Node>();//Closed list of type node
	
	public static ArrayList<Integer> getGoals() 
	{
		return Goals;
	}

	public static void setGoals(ArrayList<Integer> goals) 
	{
		Goals = goals;
	}
	
	public static char[][] getGraph() 
	{
		return Graph;
	}

	public static void setGraph(char[][] graph) 
	{
		Graph = graph;
	}

	public static int getMax_Row() 
	{
		return Max_Row;
	}

	public static void setMax_Row(int max_Row) 
	{
		Max_Row = max_Row;
	}

	public static int getStartx() 
	{
		return startx;
	}

	public static void setStartx(int startx) 
	{
		BFS.startx = startx;
	}

	public static int getStarty() 
	{
		return starty;
	}

	public static void setStarty(int starty) 
	{
		BFS.starty = starty;
	}

	public static int getMax_Col() 
	{
		return Max_Col;
	}

	public static void setMax_Col(int max_Col) 
	{
		Max_Col = max_Col;
	}

	public static double[][] getMap_cost() 
	{
		return map_cost;
	}

	public static void setMap_cost(double[][] map_cost) 
	{
		BFS.map_cost = map_cost;
	}

	public static int[][] getGoal_pos() 
	{
		return goal_pos;
	}

	public static void setGoal_pos(int[][] goal_pos) 
	{
		BFS.goal_pos = goal_pos;
	}
	
	public static void main(String args[])
	{
		FileReader.filehandler();
		Node root=new Node((Max_Row-startx),starty,0,0,null,Message_Obtained,direction);//Creation of root node
		open.add(root);
		closed.add(root);
		Node current;
		do{
				Node n = open.remove();
				if (!contained(closed,n)) {
				addol(n, open);
				closed.add(n);
			}
			current=n;
		}while(!finalstate(current));
	}
	
	static boolean contained(Queue<Node> closed,Node n)
	{
		Queue<Node> test=new LinkedList<Node>();
		test.addAll(closed);
		if(test.size()==0)
			return false;
		else
		{
			Node node1=test.remove();
			while(test.size()>0)
			{
				if(node1.row==n.row && node1.col==n.col)
				{
					if(node1.Message_Obtained.equals(n.Message_Obtained))
						return true;
				}
				if(test.size()>0){node1=test.remove();}
				}
		return false;
		}
	}
	static boolean finalstate(Node n)
	{
		int flag=0;
		if(n.row==(Max_Row-startx) && n.col==starty)
		{
			Collections.sort(n.Message_Obtained);
			if(n.Message_Obtained.equals(Goals)){
			System.out.println(n.dir+"\n"+n.cost);
			flag=1;
			}
		}
		if(flag==1)return true;
		else return false;
	}
	static void checkmessage(Node child)
	{
		int goal_state=0,j,k=0;
		for(int i=0;i<Goals.size();i++){
			j=0;
			if(child.row==goal_pos[i][j] && child.col==goal_pos[i][j+1])
			{
				goal_state=Goals.get(i);
				//check in the message_obtained whether the goal state is in the child's message obtained.
				if(child.Message_Obtained.size()==0)
				{
					child.Message_Obtained.add(goal_state);
					break;
				}
				else if(!child.Message_Obtained.contains(goal_state))
				{
				child.Message_Obtained.add(goal_state);
				//break;
				}
			}
	}
	}
	static void addol(Node Parent,Queue<Node> open)
	{
		int p_row=Parent.row;
		int p_col=Parent.col;
		int p_depth=Parent.depth;
		double p_cost=Parent.cost;
		String p_dir=Parent.dir;
		
		if(p_row!=0 && p_row<=Max_Row)
		{
			ArrayList<Integer> p_goals_reached = new ArrayList<Integer>();
			p_goals_reached.addAll(Parent.Message_Obtained);
			String child_dir=p_dir.concat("N");
			Node child=new Node(p_row-1,p_col,p_depth,p_cost+map_cost[p_row-1][p_col],Parent,p_goals_reached,child_dir);
			checkmessage(child);
			open.add(child);
			
		}
		if(p_col!=Max_Col && p_col>=0)
		{
			ArrayList<Integer> p_goals_reached = new ArrayList<Integer>();
			p_goals_reached.addAll(Parent.Message_Obtained);
			String child_dir=p_dir.concat("E");
			Node child=new Node(p_row,p_col+1,p_depth,p_cost+map_cost[p_row][p_col+1],Parent,p_goals_reached,child_dir);
			checkmessage(child);
			open.add(child);}
		if(p_row!=Max_Row && p_row>=0)
		{
			ArrayList<Integer> p_goals_reached = new ArrayList<Integer>();
			p_goals_reached.addAll(Parent.Message_Obtained);
			String child_dir=p_dir.concat("S");
			Node child=new Node(p_row+1,p_col,p_depth,p_cost+map_cost[p_row+1][p_col],Parent,p_goals_reached,child_dir);
			checkmessage(child);
			open.add(child);
		}
		if(p_col!=0 && p_col<=Max_Col)
		{
			ArrayList<Integer> p_goals_reached = new ArrayList<Integer>();
			p_goals_reached.addAll(Parent.Message_Obtained);
			String child_dir=p_dir.concat("W");
			Node child=new Node(p_row,p_col-1,p_depth,p_cost+map_cost[p_row][p_col-1],Parent,p_goals_reached,child_dir);
			checkmessage(child);
			open.add(child);
		}
	}
		
}
	
