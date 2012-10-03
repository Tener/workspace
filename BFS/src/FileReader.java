import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FileReader {
	static int row,col,goal_length;
	static int Goal_pos[][]=new int[40][20];
	static char Graph[][]=new char[40][20];
	static ArrayList<Integer> Message_Obtained=new ArrayList<Integer>();
	static double cost_map[][]=new double[20][20];
	static public void filehandler(){
	try{
		int i=0;
	File file=new File("Map1.txt");
	FileInputStream fis=new FileInputStream(file);
	BufferedInputStream bis=new BufferedInputStream(fis);
    BufferedReader d= new BufferedReader(new InputStreamReader(bis));
		String line=d.readLine();
		line=line.trim();
		String temp[]=line.split(" ");
		row=Integer.parseInt(temp[0]);
		Search.setMax_Row(row);
		col=Integer.parseInt(temp[1]);
		Search.setMax_Col(col);
		System.out.println(col + row);
		line=d.readLine();
		line=line.trim();
		temp=line.split(" ");
		int startx=Integer.parseInt(temp[0]);
		Search.setStartx(startx);
		int starty=Integer.parseInt(temp[1]);
		Search.setStarty(starty);
		System.out.println(startx+starty);
		int j=i=0;
		while(i<row)
		{
			line=d.readLine();
			char ch[]=line.toCharArray();
			for(int k=0;k<col;k++){
				System.out.println(ch[k]);
				Graph[i][k]=ch[k];
			if(ch[k]!='.'){
				Message_Obtained.add(j);
				//System.out.println(Goal[j]);
				j++;
			}
		}
			i++;
		}
		goal_length=Message_Obtained.size();
		//Search.setLength(goal_length);
		Search.setGraph(Graph);
		Search.setGoals(Message_Obtained);
		getpos();
		i=0;
		while(i<row)
		{
			line=d.readLine();
			line=line.trim();
			temp=line.split(" ");
			for(j=0;j<col;j++){
				cost_map[i][j]=Double.parseDouble(temp[j]);
				System.out.println("Cost:"+ cost_map[i][j]);}
			i++;
		}
		Search.setMap_cost(cost_map);
	}catch(IOException e)
	{
		
	}
	}

	public static void getpos() {
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
		Search.setGoal_pos(Goal_pos);
	}
//	public FileReader(int r,int col,int startx,int starty,int cost_map[][])
//	{
//	}
}
