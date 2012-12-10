import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReadFile {

	protected String filename;
	
	/**
	 * 
	 * @param filename
	 */
	protected static ArrayList<TrainingData> readFile(String filename)
	{
		String line = null;
		BufferedReader reader = null;
		FileInputStream fis = null;
		ArrayList<TrainingData> arr_list = new ArrayList<TrainingData>();
		File f = new File(filename);
		
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		reader = new BufferedReader(new InputStreamReader(fis));
		try {
			while((line = reader.readLine()) != null)
			{
				if(!filename.equalsIgnoreCase("data2.txt"))
				{
					String attr[] = line.split(" ");
					int Classi=Integer.parseInt(attr[3]);
					TrainingData atr=new TrainingData(attr[0],attr[1],attr[2], Classi);
					arr_list.add(atr);
				}
				else
				{
					String attr[] = line.split(" ");
					int Classi=Integer.parseInt(attr[4]);
					TrainingData atr=new TrainingData(attr[0],attr[1],attr[2], attr[3], Classi);
					arr_list.add(atr);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arr_list;
	}
	
}
