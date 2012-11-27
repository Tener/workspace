import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * 
 * @author Tanmay
 *
 */

public class Knn {
	
	static final int k = 5; //Set the value of k here
	static int correct = 0;
	static int wrong = 0;
	
	ArrayList<Integer> distance = new ArrayList<Integer>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList<Attributes> instances = new ArrayList<Attributes>();
		instances = FileReader.readFile();
		/*for(Attributes attr1 : instances)
		System.out.println(attr1);*/
		cross_validation(instances);
		//double Percent = (correct/(correct + wrong))*100;
		double Percent = 0.0;
		Percent = (correct/345.0)*100;
		System.out.println("Percentage of correct predictions " + Percent + "%");
		System.out.println("Percentage of wrong predictions " + (100 - Percent) + "%");
		
	}
	
	
	/**
	 * 
	 * @param instances
	 */
	private static void cross_validation(ArrayList<Attributes> instances) 
	{
		for(Attributes atr : instances)
		{
			calculate_distance(instances,atr);
		}
	}

	/**
	 * 
	 * @param atr 
	 * @param instances
	 */

	private static void calculate_distance(ArrayList<Attributes> instances,Attributes atr)
	{
		
		int count_1=0,count_2=0;
		ArrayList<Distances> distances = new ArrayList<Distances>();
		for(Attributes attr : instances)
		{
			double distance=0.0;
			if(!equals(attr,atr))
			{
				distance = Math.sqrt(Math.pow((attr.mcv - atr.mcv),2) + Math.pow((attr.alkphos - atr.alkphos),2) + Math.pow((attr.sgpt - atr.sgpt),2) + Math.pow((attr.sgot - atr.sgot),2) + Math.pow((attr.gammagt - atr.gammagt),2) + Math.pow((attr.selector - atr.selector),2) + Math.pow((attr.drinks - atr.drinks),2));
				Distances distance_attr = new Distances(attr.mcv,attr.alkphos,attr.sgpt,attr.sgot,attr.gammagt,attr.drinks,attr.selector,distance);
				distances.add(distance_attr);
				//System.out.println(i++ + "Distance is " + distance);
			}
			else 
				continue;
		}
		Collections.sort(distances, new Comparator<Distances>(){
		    public int compare(Distances d1, Distances d2)
		    {
		        return Double.valueOf(d1.distance).compareTo(d2.distance);
		    } });
		
			//System.out.println("Distance" + dis.distance + " " + dis.mcv + " " + dis.selector);
		//System.out.println("Prediction based on 1 nearest neighbour " + distances.get(0).selector + " Original " + atr.selector);
		for(int i=0;i<k;i++)
		{
			if(distances.get(i).selector == 1.0)
				count_1++;
			else if(distances.get(i).selector == 2.0)
				count_2++;
		}
		if(count_1 > count_2)
			{
				System.out.println("Prediction based on " + k + " nearest neighbour "+ "1.0" + " Original " + atr.selector);
				if(atr.selector == 1.0)
					correct++;
				else
					wrong++;
			}
		else
		{
			System.out.println("Prediction based on " + k + " nearest neighbour " + "2.0" + " Original " + atr.selector);
			if(atr.selector == 2.0)
				correct++;
			else
				wrong++;
		}
			
	}

	/**
	 * 
	 * @param attr
	 * @param atr
	 * @return
	 */
	private static boolean equals(Attributes attr, Attributes atr) {
		if(attr.mcv == atr.mcv && attr.alkphos == atr.alkphos && attr.drinks == atr.drinks && attr.gammagt == atr.gammagt && attr.sgpt == atr.sgpt && attr.sgot == atr.sgot && attr.selector == atr.selector)
			return true;
		else
			return false;
	}
	
	
}
