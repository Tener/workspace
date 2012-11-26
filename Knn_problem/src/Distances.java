
public class Distances extends Attributes{

	
	protected double distance;
	/**
	 * 
	 * @param mcv
	 * @param alkphos
	 * @param sgpt
	 * @param sgot
	 * @param gammagt
	 * @param drinks
	 * @param selector
	 * @param distance
	 */
	Distances(double mcv, double alkphos, double sgpt, double sgot,
			double gammagt, double drinks, double selector, double distance) {
		super(mcv, alkphos, sgpt, sgot, gammagt, drinks, selector);
		this.distance = distance;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	@Override
	public String toString()
	{
		return("  MCV  " + mcv + "  alkphos  " + alkphos + "  sgpt  " + sgpt + "  sgot  " + sgot + "  gammagt  " + gammagt + "  drinks  " + drinks + "  selector  " + selector + " Distances " + distance);
	}

}
