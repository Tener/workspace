import java.util.ArrayList;
import java.util.Iterator;
/**
 * 
 * @author Tanmay
 *
 */

public class NaiveBayes {

	
	protected static ArrayList<String> poss_values = new ArrayList<String>();
	protected static ArrayList<String> poss_values1 = new ArrayList<String>();
	protected static ArrayList<String> poss_values2 = new ArrayList<String>();
	protected static ArrayList<String> poss_values3 = new ArrayList<String>();
	protected static ArrayList<Prob_AttrMatching> parprob = new ArrayList<Prob_AttrMatching>();
	protected static double prob_1 = 0.0, prob1_Part1=0.0, prob1_Part2=0.0, prob1_Part3 = 0.0;
	protected static double prob_2 = 0.0, prob_3 = 0.0 ,prob2_Part1=0.0, prob2_Part2=0.0, prob2_Part3 = 0.0;
	protected static int count_4prob_1[],count_4prob_2[];// even is classified as zero and add as ones
	protected static int count_0=0,count_1=0, count_2=0;
	protected static String filename = "data2.txt";
	protected static String filename_check = "data2.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		ArrayList<TrainingData> tr_data = ReadFile.readFile(filename);
		
		System.out.println(tr_data);
		System.out.println(tr_data.size());
		if(!(filename.equals(filename_check))) 
		{	
			populate_attr(tr_data);
			calculate_classi_prob(tr_data);
			calculate_attributes_prob(poss_values,tr_data);
			calculate_attributes_prob(poss_values1,tr_data);
			calculate_attributes_prob(poss_values2,tr_data);
			
		}
			
		else
		{
			populate_attr(tr_data,filename);
			calculate_classi_prob(tr_data);
			calculate_attributes_prob(poss_values,tr_data);
			calculate_attributes_prob(poss_values1,tr_data);
			calculate_attributes_prob(poss_values2,tr_data);
			calculate_attributes_prob(poss_values3,tr_data);
		}
		classifier(tr_data);
	}
	
	
	/**
	 * 
	 * @param tr_data
	 */
	
	protected static void populate_attr(ArrayList<TrainingData> tr_data)
	{
		for(int i=1;i<=3;i++)
		{
		Iterator<TrainingData> it = tr_data.iterator();
		while(it.hasNext())
		{
			TrainingData td = it.next();
			if((i==1 && poss_values.isEmpty()) || (i==2 && poss_values1.isEmpty()) || (i==3 && poss_values2.isEmpty()))
				{
					if(i==1)poss_values.add(td.Parone);
					else if(i==2)poss_values1.add(td.Partwo);
					else if(i==3)poss_values2.add(td.Parthree);
				}
			else
			{
					if( i==1 && !poss_values.contains(td.Parone)) poss_values.add(td.Parone);
					if( i==2 && !poss_values1.contains(td.Partwo)) poss_values1.add(td.Partwo);
					if( i==3 && !poss_values2.contains(td.Parthree)) poss_values2.add(td.Parthree);
						
			}
		}
		}
		System.out.println(poss_values);
		System.out.println(poss_values1);
		System.out.println(poss_values2);
	}
	
	protected static void populate_attr(ArrayList<TrainingData> tr_data, String filename)
	{
		for(int i=1;i<=4;i++)
		{
		Iterator<TrainingData> it = tr_data.iterator();
		while(it.hasNext())
		{
			TrainingData td = it.next();
			if((i==1 && poss_values.isEmpty()) || (i==2 && poss_values1.isEmpty()) || (i==3 && poss_values2.isEmpty()) || (i==4 && poss_values3.isEmpty()))
				{
					if(i==1)poss_values.add(td.Parone);
					else if(i==2)poss_values1.add(td.Partwo);
					else if(i==3)poss_values2.add(td.Parthree);
					else if(i==4)poss_values3.add(td.Parfour);
				}
			else
			{
					if( i==1 && !poss_values.contains(td.Parone)) poss_values.add(td.Parone);
					if( i==2 && !poss_values1.contains(td.Partwo)) poss_values1.add(td.Partwo);
					if( i==3 && !poss_values2.contains(td.Parthree)) poss_values2.add(td.Parthree);
					if( i==4 && !poss_values3.contains(td.Parfour)) poss_values3.add(td.Parfour);	
			}
		}
		}
		System.out.println(poss_values);
		System.out.println(poss_values1);
		System.out.println(poss_values2);
		System.out.println(poss_values3);
	}
	
	/**
	 * 
	 * @param poss_values
	 * @param tr
	 */
	
	protected static void calculate_attributes_prob(ArrayList<String> poss_values,ArrayList<TrainingData> tr)
	{
		for(int i=0; i< poss_values.size() ; i++)
		{
			int count=0,count_one=0,count_two=0;
			int count4par = 0;
			Iterator<TrainingData> it = tr.iterator();
			while(it.hasNext())
			{
				
				TrainingData tr_par = it.next();
				if(((poss_values.get(i).equals(tr_par.Parone))||(poss_values.get(i).equals(tr_par.Partwo))||(poss_values.get(i).equals(tr_par.Parthree)) || (poss_values.get(i).equals(tr_par.Parfour))) && tr_par.Classi == 0)
					count++;
				else if(((poss_values.get(i).equals(tr_par.Parone))||(poss_values.get(i).equals(tr_par.Partwo))||(poss_values.get(i).equals(tr_par.Parthree)) || (poss_values.get(i).equals(tr_par.Parfour))) && tr_par.Classi == 1)
					count_one++;
				else if(((poss_values.get(i).equals(tr_par.Parone))||(poss_values.get(i).equals(tr_par.Partwo))||(poss_values.get(i).equals(tr_par.Parthree)) || (poss_values.get(i).equals(tr_par.Parfour))) && tr_par.Classi == 2)
					count_two++;
			}
				prob1_Part1 = (double) count/count_0; 
				prob2_Part1 = (double) count_one/count_1;
				//System.out.println(count + " " + count_one + " " + count_two);
				Prob_AttrMatching pr = new Prob_AttrMatching(poss_values.get(i),prob1_Part1, prob2_Part1,(double)count_two/count_2);
				parprob.add(pr);
				System.out.println(pr);
		}
	}
	
	/**
	 * 
	 * @param tr_data
	 */
	
	protected static void calculate_classi_prob(ArrayList<TrainingData> tr_data)
	{
		Iterator<TrainingData> it = tr_data.iterator();
		while(it.hasNext())
		{
			TrainingData element = it.next();
			if(element.Classi == 0)
				count_0 ++;
			else if(element.Classi == 1)
				count_1 ++;
			else if(element.Classi == 2)
				count_2++;
		}
		prob_1 = (double)count_0/tr_data.size();
		prob_2 = (double)count_1/tr_data.size();
		prob_3 = (double)count_2/tr_data.size();
		System.out.println("Count of 0 " + count_0 + " " + " Count of 1 " + count_1 + " Count of 2 " +  count_2);
		System.out.println("Probability of 0 " + prob_1 + " " + "Probability of 1 " + prob_2 + " Probability of 2 " + prob_3);
	}
	
	protected static void classifier(ArrayList<TrainingData> tr_data)
	{
		
		
		int correct_pred = 0;
		
		for(int z=0; z< tr_data.size() ; z++)
		{
			TrainingData tr_one = tr_data.get(z);
			String one = tr_one.Parone;
			String two = tr_one.Partwo;
			String three = tr_one.Parthree;
			String four = tr_one.Parfour;
			int Classifier = tr_one.Classi;
			Iterator<Prob_AttrMatching> it1 = parprob.iterator();
			while(it1.hasNext())
			{
				Prob_AttrMatching pam = it1.next();
				if(pam.Parameter.equals(one))
				{
					Iterator<Prob_AttrMatching> it2 = parprob.iterator();
					while(it2.hasNext())
					{
						Prob_AttrMatching pam1 = it2.next();
						if(pam1.Parameter.equals(two))
						{
							Iterator<Prob_AttrMatching> it3 = parprob.iterator();
							while(it3.hasNext())
							{
								Prob_AttrMatching pam2 = it3.next();
								if(pam2.Parameter.equals(three))
								{
									if(!poss_values3.isEmpty())
									{
									Iterator<Prob_AttrMatching> it4 = parprob.iterator();
									while(it4.hasNext())
									{
										Prob_AttrMatching pam3 = it4.next();
										if(pam3.Parameter.equals(four))
										{
											if(pam3.prob_zero == 0 && pam3.prob_one == 0 && pam3.prob_two == 0)
												pam3.prob_two = pam3.prob_one = pam3.prob_zero = 1.0;
											double prob_zero_pred = prob_1*pam.prob_zero*pam1.prob_zero*pam2.prob_zero*pam3.prob_zero;
											double prob_one_pred = prob_2*pam.prob_one*pam1.prob_one*pam2.prob_one*pam3.prob_one;
											double prob_two_pred = prob_3*pam.prob_two*pam1.prob_two*pam2.prob_two*pam3.prob_two;
											double prob_max_inter = Math.max(prob_zero_pred,prob_one_pred);
											double prob_max = Math.max(prob_max_inter, prob_two_pred);
											if(prob_max == prob_zero_pred)
											{
												System.out.println(" Prediction for " + tr_one + " is " + " 0 " + Classifier);
												if(Classifier == 0)
													correct_pred++;
											}
											else if(prob_max == prob_one_pred)
											{
												System.out.println(" Prediction for " + tr_one + " is " + " 1 " +  Classifier);
												if(Classifier == 1)
													correct_pred++;
											}
											else if(prob_max == prob_two_pred)
											{
												System.out.println(" Prediction for " + tr_one + " is " + " 2 " +  Classifier);
												if(Classifier == 2)
													correct_pred++;
											}
											break;
										}
									}
								}
									else if(poss_values3.isEmpty())
									{
											double prob_zero_pred = prob_1*pam.prob_zero*pam1.prob_zero*pam2.prob_zero;
											double prob_one_pred = prob_2*pam.prob_one*pam1.prob_one*pam2.prob_one;
											double prob_max = Math.max(prob_zero_pred,prob_one_pred);
											if(prob_max == prob_zero_pred)
											{
												System.out.println(" Prediction for " + tr_one + " is " + " 0 " + Classifier);
												if(Classifier == 0)
													correct_pred++;
											}
											else if(prob_max == prob_one_pred)
											{
												System.out.println(" Prediction for " + tr_one + " is " + " 1 " +  Classifier);
												if(Classifier == 1)
													correct_pred++;
											}
											
									}
								}
							}
						}
					}
				}
			}
			
			/*while(it.hasNext())
			{
				TrainingData tr_two = it1.next();
				if(tr_one.Parone.equals(anObject) )
			}*/
		}
		System.out.println(" Percentage of correct predictions " + (float)((correct_pred)/tr_data.size()));
	}

}
