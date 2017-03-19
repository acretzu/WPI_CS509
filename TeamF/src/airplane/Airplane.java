package airplane;

public class Airplane {

	private String planeManufacturer;             
	private String planeModel;             
	private int planeFirstClass;          
	private int planeCoachClass;        

	public Airplane () {
		planeManufacturer = "";
		planeModel = "";
		planeFirstClass = 0;
		planeCoachClass = 0;
	}

	public Airplane (String manufac, String mdl, int first, int coach) {
		planeManufacturer = manufac;
		planeModel = mdl;
		planeFirstClass = first;
		planeCoachClass = coach;
	}
	
	public void manufacturer (String manufac) {
		planeManufacturer = manufac;
	}
	
	public String manufacturer () {
		return planeManufacturer;
	}
	
	public void model (String mdl) {
		planeModel = mdl;
	}
	
	public String model () {
		return planeModel;
	}
	
	public void firstClass (int first) {
		planeFirstClass = first;
	}
	
	public int firstClass () {
		return planeFirstClass;
	}
	
	public void coachClass (int coach) {
		planeCoachClass = coach;
	}
	
	public int coachClass () {
		return planeCoachClass;
	}
}
