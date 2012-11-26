
public class Attributes {

	protected double mcv,alkphos,sgpt,sgot,gammagt,drinks,selector;

	/**
	 * @param mcv,alkphos,sgpt,sgot,gammagt,drinks,selector
	 */
	Attributes(double mcv,double alkphos,double sgpt,double sgot,double gammagt,double drinks,double selector)
	{
		this.mcv=mcv;
		this.alkphos=alkphos;
		this.sgpt=sgpt;
		this.sgot=sgot;
		this.gammagt=gammagt;
		this.drinks=drinks;
		this.selector=selector;
	}
	@Override
	public String toString()
	{
		return("  MCV  " + mcv + "  alkphos  " + alkphos +"  sgpt  " + sgpt + "  sgot  " + sgot + "  gammagt  " + gammagt + "  drinks  " + drinks + "  selector  " + selector);
	}
	
}
