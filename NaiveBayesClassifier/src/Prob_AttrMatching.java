
public class Prob_AttrMatching {

	protected Object Parameter;
	protected double prob_zero;
	protected double prob_one;
	protected double prob_two;

	public Prob_AttrMatching(String Parameter, double prob_zero, double prob_one, double prob_two) {
		
		this.Parameter = Parameter;
		this.prob_zero = prob_zero;
		this.prob_one = prob_one;
		this.prob_two = prob_two;
	}

	@Override
	public String toString() {
		return "Prob_AttrMatching [Parameter=" + Parameter + ", prob_zero="
				+ prob_zero + ", prob_one=" + prob_one + ", prob_two="
				+ prob_two + "]";
	}
	

}
