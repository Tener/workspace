import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class FileReader {

	/**
	 * @param
	 */
	protected static ArrayList<Attributes> readFile()
	{
		String line = null;
		BufferedReader reader = null;
		FileInputStream fis = null;
		ArrayList<Attributes> arr_list = new ArrayList<Attributes>();
		File f = new File("bupa.data");
		
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		reader = new BufferedReader(new InputStreamReader(fis));
		try {
			while((line = reader.readLine()) != null)
			{
				String attr[] = line.split("\\,");
				double a=Double.parseDouble(attr[0]);
				double b=Double.parseDouble(attr[1]);
				double c=Double.parseDouble(attr[2]);
				double d=Double.parseDouble(attr[3]);
				double e=Double.parseDouble(attr[4]);
				double f1=Double.parseDouble(attr[5]);
				double g=Double.parseDouble(attr[6]);
				Attributes atr=new Attributes(a,b,c,d,e,f1,g);
				arr_list.add(atr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arr_list;
	}
}
