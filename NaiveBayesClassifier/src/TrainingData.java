
public class TrainingData {

	protected String Parone;
	protected String Partwo;
	protected String Parthree;
	protected String Parfour;
	protected int Classi;
	
	TrainingData(String attr, String attr2, String attr3, int attr4)
	{
		this.Parone = attr;
		this.Partwo = attr2;
		this.Parthree = attr3;
		this.Classi = attr4;
	}
	
	TrainingData(String attr,String attr2, String attr3, String attr4, int attr5)
	{
		this.Parone = attr;
		this.Partwo = attr2;
		this.Parthree = attr3;
		this.Parfour = attr4;
		this.Classi = attr5;
	}

	@Override
	public String toString() {
		return "TrainingData [Parone=" + Parone + ", Partwo=" + Partwo
				+ ", Parthree=" + Parthree + ", Parfour=" + Parfour
				+ ", Classi=" + Classi + "]";
	}
	
	
	
}