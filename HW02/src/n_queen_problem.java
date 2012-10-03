import java.util.Arrays;




public class n_queen_problem {
	static final int N_queen=8;//Set the number of queens here
	static int min_counter,max_size,count_heuristic;
	static boolean first_choice_execution,random_selection_execution,row_wise;
	static int conflict[][],initial_queen_pos[],change_queen_pos[];
	static char board[][];
	static int minimum_values[],column_first_choice;
	static char solution_board[][];
	static int min_of_min[];
	static int min_old=0;
	static char changed_board[][];
	static int heuristic[][];
	static int minimum,minimum_row_col[][];
	static int min_row,min_col;
	static int initial_heuristic;
	n_queen_problem()//Constructor To Initialise Values
	{
		conflict=new int[N_queen][N_queen]; 
		initial_queen_pos=new int[N_queen];
		min_of_min=new int[N_queen*N_queen];
		change_queen_pos=new int[N_queen];
		board=new char[N_queen][N_queen];
		minimum_row_col=new int[N_queen*N_queen][2];
		minimum_values=new int[N_queen*N_queen];
		solution_board=new char[N_queen][N_queen];
		changed_board=new char[N_queen][N_queen];
		heuristic=new int[N_queen][N_queen];
		column_first_choice=0;
		max_size=100;
		count_heuristic=0;
		random_selection_execution=row_wise=first_choice_execution=false;
		column_first_choice=0;
		minimum=100;
		min_row=0;min_col=0;
		min_counter=0;
	}
	public static void main(String args[])
	{
		new n_queen_problem();
		long startTime = System.currentTimeMillis();
		System.out.println("Starting with Steepest Ascent\n\n");
		place_queen();
		steepest_ascent();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total time::"+ totalTime);
		new n_queen_problem();
		startTime = System.currentTimeMillis();
		System.out.println("Starting with Steepest Ascent row_wise\n\n");
		place_queen();
		row_wise=true;
		steepest_ascent();
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time::"+ totalTime);
		new n_queen_problem();
		startTime=System.currentTimeMillis();
		System.out.println("Starting with First Choice\n\n");
		place_queen();
		first_choice();
		endTime=System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time::"+ totalTime);
		startTime=System.currentTimeMillis();
		new n_queen_problem();
		System.out.println("Starting with Random Choice\n\n");
		place_queen();
		random_selection();
		endTime=System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time::"+ totalTime);
	}
	
	
	static void first_choice()//Method for First Choice Hill climbing
	{
			first_choice_execution=true;
			int curr_row=0,counter=0;
			display_board(solution_board);
			System.out.print("\n\n");
			while(counter<100)
			{
				min_counter=0;
				for(int queen_selected=0;queen_selected<N_queen;queen_selected++)
				{
					if(queen_selected!=0 && count_heuristic<minimum)break;
					count_heuristic=0;
					clone_array(solution_board);
					curr_row=change_queen_pos[queen_selected];
					//System.out.println("Board"+queen_selected+"\n");
					call_conflict(0,curr_row);
					if(queen_selected!=0 && count_heuristic<minimum) break;
					set_heuristic(queen_selected);
					display_heuristics();
					move_queens(queen_selected,curr_row);
				}
				set_heuristic(column_first_choice);
				display_heuristics();
				//System.out.println(min_row+" "+ min_col);
				int temp_row=initial_queen_pos[min_col];
				solution_board[temp_row][min_col]=0;
				initial_queen_pos[min_col]=min_row;
				//display_heuristics();
				print_solution();
				if(minimum==0) {System.out.println("Solution Found - First Choice"); break;}
				counter++;
			}
			display_board(solution_board);
			System.out.println("The total number of iterations::"+counter);
	}
	
	static void random_selection()//Method for Random Selection Hill Climbing
	{
		random_selection_execution=true;
		int c=0;
		do{
		int curr_row=0;
		Arrays.fill(minimum_values,0);
		min_counter=0;
		clone_array(solution_board);
		System.out.println("\nNew Board\n\n");
		display_board(solution_board);
		for(int queen_selected=0;queen_selected<N_queen;queen_selected++)
		{
			count_heuristic=0;
			clone_array(solution_board);
			curr_row=change_queen_pos[queen_selected];
			call_conflict(0,curr_row);
			if(queen_selected==0) initial_heuristic=count_heuristic;
			set_heuristic(queen_selected);
			//display_heuristics();
			move_queens(queen_selected,curr_row);
		}
		display_heuristics();
		System.out.println();
		min_counter=(int)Math.ceil((Math.random()*10)%(min_counter-1));
		min_col=minimum_row_col[min_counter][1];
		min_row=minimum_row_col[min_counter][0];
		minimum=minimum_values[min_counter];
		int temp_row=initial_queen_pos[min_col];
		solution_board[temp_row][min_col]=0;
		initial_queen_pos[min_col]=min_row;
		//display_heuristics();
		print_solution();
		if(minimum==0) {System.out.println("Random Selection- End-Solution Found");break;}
		c++;
		}while(c<100);
		System.out.println("Total number of iterations::"+c);
	}
	
	static void row_movement()//Method for Row movement
	{
		int j=0,k=0;
		for(int i=0;i<min_counter;i++)
		{
			if(minimum==minimum_values[i])
			{
				min_of_min[j]=i;
				j++;
			}
		}
		k=(int)Math.floor((Math.random()*10)%(j-1));
		min_row=minimum_row_col[min_of_min[k]][0];
		min_col=minimum_row_col[min_of_min[k]][1];	
	}
	
	static void steepest_ascent()//Method For Steepest Ascent Hill Climbing
	{
		int c=0;
		do{
			int curr_row=0;
			clone_array(solution_board);
			System.out.println("\nNew Board-\n\n");
			display_board(solution_board);
			for(int queen_selected=0;queen_selected<N_queen;queen_selected++)
			{
				count_heuristic=0;
				clone_array(solution_board);
				curr_row=change_queen_pos[queen_selected];
				//System.out.println("Queen Shifted"+queen_selected+"\n");
				call_conflict(0,curr_row);
				set_heuristic(queen_selected);
				//display_heuristics();
				move_queens(queen_selected,curr_row);
			}
			//System.out.print("\n\n");
			//display_heuristics();
			if(row_wise && c%2==0){min_old=minimum;}
			if(row_wise && c%2!=0 && minimum==min_old) row_movement();
			System.out.println(min_row+" "+ min_col);
			int temp_row=initial_queen_pos[min_col];
			solution_board[temp_row][min_col]=0;
			initial_queen_pos[min_col]=min_row;
			print_solution();
			if(minimum==0) {System.out.println("Solution Found");c++;break;}
			c++;
			min_counter=0;
		}while(c<100);
		System.out.println("Total number of iterations::"+c);
	}
	
	static void move_queens(int queen_selected,int curr_row)//Method to move queens within the same column
	{
		int new_column=0;
		do
		{
			count_heuristic=0;
			curr_row=change_queen_pos[queen_selected];
			changed_board[curr_row][queen_selected]=' ';
			curr_row=(curr_row+1)%N_queen;
			change_queen_pos[queen_selected]=curr_row;
			changed_board[curr_row][queen_selected]='Q';
			call_conflict(0,curr_row);
			if(first_choice_execution==true && count_heuristic<minimum) { column_first_choice=queen_selected; break;}
			set_heuristic(queen_selected);
			//display_board(changed_board);
			//display_heuristics();
			new_column=new_column+1;
			//System.out.print("\n\n");
		}while(new_column<(N_queen-1));
	}

	
	
	static void print_solution()//Print the new board or the solution board
	{
		int row;
		//System.out.println("\n\n");
		for(int i=0;i<N_queen;i++)
		{
			row=initial_queen_pos[i];
			solution_board[row][i]='Q';
		}
		display_board(solution_board);
	}
	static void clone_array(char[][] orig_board)
	{
		for(int i=0;i<N_queen;i++)
		{
			change_queen_pos[i]=initial_queen_pos[i];
			for(int j=0;j<N_queen;j++){
				changed_board[i][j]=' ';
				changed_board[i][j]=orig_board[i][j];}
		}
	}
	static void call_conflict(int i,int curr_row)// i is column, curr_row is the row, this method recursively calls the calculate_conflict method 
	{
			if(i<N_queen){
			int conflicts=0;
			curr_row=change_queen_pos[i];
			conflicts=calculate_conflict(i,curr_row);
			conflict[curr_row][i]=conflicts;
			call_conflict(i+1,curr_row);
			count_heuristic=count_heuristic+conflicts;
			//heuristic[curr_row][i]=count_heuristic;
			//System.out.print(count_heuristic+"\t");
			}
	}
	
	static void set_heuristic(int column)//Sets the cost in an array
	{
		int row;
		row=change_queen_pos[column];
		heuristic[row][column]=count_heuristic;
		if(!random_selection_execution && count_heuristic<=minimum)
		{
			minimum_row_col[min_counter][0]=row;
			minimum_row_col[min_counter][1]=column;
			minimum_values[min_counter]=count_heuristic;
			minimum=count_heuristic;
			min_row=row;
			min_col=column;
			min_counter++;
		}	
		else if(random_selection_execution && count_heuristic<=initial_heuristic)
		{
			minimum_row_col[min_counter][0]=row;
			minimum_row_col[min_counter][1]=column;
			minimum_values[min_counter]=count_heuristic;
			min_counter++;
		}
	}
	
	static void place_queen()//Places the queen randomly on the board at the start of the process
	{
		int col;
		for(int i=0;i<N_queen;i++)
		{
			col=(int)Math.floor((Math.random()*10)%N_queen);
			System.out.println(col);
			board[col][i]='Q';
			changed_board[col][i]='Q';
			solution_board[col][i]='Q';
			initial_queen_pos[i]=col;
			change_queen_pos[i]=col;
		}
	}
	
	
	
	
	static void display_board(char[][] changed_board)//Method for displaying board
	{
		for(int i=0;i<N_queen;i++)
		{
			for(int j=0;j<N_queen;j++)
			{
				if(changed_board[i][j]==0)
					System.out.print("-\t");
				else
					System.out.print(changed_board[i][j]+ "\t");
			}
			System.out.println();
		}
	}
	
	static void display_heuristics()//Display the cost matrix
	{
		//System.out.println("\n\n" + "Heuristic Matrix");
		for(int i=0;i<N_queen;i++)
		{
			for(int j=0;j<N_queen;j++)
			{
				System.out.print(heuristic[i][j]+ "\t");
			}
			System.out.println("\n");
		}
	}
	
	
	static int calculate_conflict(int i,int curr_row)//Calculate the number of conflicts
	{
		int count=0;
		int column=i;
				for(;column<N_queen;column++)
				{
					if((column+1)<N_queen && changed_board[curr_row][column+1]=='Q')
						count++;
					if(column==i)
					{
					for(int k=1;k<N_queen;k++)
					{
						if((curr_row+k)<N_queen && (i+k)<N_queen)
						{
							if(changed_board[curr_row+k][i+k]=='Q')
								count++;
						}
						if((curr_row-k)>=0 && (i+k)<N_queen)
						{
							if(changed_board[curr_row-k][i+k]=='Q')
								count++;
						}
					}
				}
				}
		return count;
	}
	
}
