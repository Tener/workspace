package Breadth_First_search;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FileReader {
	private static int row,col,goal_length;
	private static int Goal_pos[][]=new int[40][2];
	private static char Graph[][]=new char[40][20];
	private static ArrayList<Integer> Message_Obtained=new ArrayList<Integer>();
	private static double cost_map[][]=new double[20][20];
	static public void filehandler(){
	try{
		int i=0;
		File file=new File("map1.txt");//SPECIFY THE FILE NAME HERE
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream bis=new BufferedInputStream(fis);
		BufferedReader d= new BufferedReader(new InputStreamReader(bis));
		String line=d.readLine();
		line=line.trim();
		String temp[]=line.split(" ");
		row=Integer.parseInt(temp[0]);
		BFS.setMax_Row(row-1);//SETTING THE MAX_ROW AS ROW-1
		col=Integer.parseInt(temp[1]);
		BFS.setMax_Col(col-1);//SETTING THE MAX_COL AS COL-1
		line=d.readLine();
		line=line.trim();
		temp=line.split(" ");
		int startx=Integer.parseInt(temp[0]);
		BFS.setStartx(startx);//SETTING THE START POSITION OF THE SEARCH ON X AXIS
		int starty=Integer.parseInt(temp[1]);
		BFS.setStarty(starty);//SETTING THE START Y POSITION OF THE SEARCH
		int j=i=0;
		while(i<row)//STORING THE GRAPH FROM THE FILE INTO A VARIABLE
		{
			line=d.readLine();
			char ch[]=line.toCharArray();
			for(int k=0;k<col;k++)
			{
				Graph[i][k]=ch[k];
				if(ch[k]!='.')
				{
					Message_Obtained.add(j);
					j++;
				}
			}
			i++;
		}
		BFS.setGraph(Graph);
		BFS.setGoals(Message_Obtained);
		getpos();
		i=0;
		while(i<row) //Reading the cost map from the file and saving it
		{
			line=d.readLine();
			line=line.trim();
			temp=line.split(" +");
			for(j=0;j<col;j++)
			{
				cost_map[i][j]=Double.parseDouble(temp[j]);
			}
			i++;
		}
		BFS.setMap_cost(cost_map);
	}catch(IOException e)
	{
		
	}
	}

	private static void getpos() { // Gets the Message position and stores it in a 2D array
		int k=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++){
				int change_row=0;
			if(Graph[i][j]!='.')
			{
				Goal_pos[k][change_row]=i;
				Goal_pos[k][change_row+1]=j;
				k++;
			}
			}
		}
		BFS.setGoal_pos(Goal_pos);
	}
}
