import java.util.*;
public class BondEnergies{
    private class Bond{
	public String name;
	public double BE;
	public Bond(String s, double o, double w, double c, double n){
	    name = s;
	    if(name.equals("CO")){//C0 double bond in CO2
		BE=c/2;
	    }else if(name.equals("NN")){//half of a NN bond in N2
		BE=n/2;//Need to find value
	    }else if(name.equals("HO")){//HO bond in H2O
		BE=w/2+22.0080000;
	    }else if(name.equals("OO")){//HALF of a bond in O2
		BE=o/2;
	    }else{
		throw new IllegalArgumentException();
	    }
	}
    }


    public BondEnergies(int[] data, double o, double w, double c, double n){
	if(data.length!=4){
	    throw new IllegalArgumentException();
	}else{
	    CHON = new int[4];
	    for(int i = 0; i < data.length; i++){
		CHON[i]=data[i];
	    }
	    oxygen = o;
	    water = w;
	    carbondioxide = c;
	    nitrogen = n;
	}
    }
    //Instance Variables
    private int[] CHON;
    private double oxygen;
    private double water;
    private double carbondioxide;
    private double nitrogen;
    private void doubleCoefficients(){//To deal with odd balancing situations and ensure H is always even
	for(int i = 0; i < CHON.length; i++){
	    CHON[i]*=2;
	}
    }
    private void halfCoefficients(){
	for(int i = 0; i < CHON.length; i++){
	    CHON[i]/=2;
	}
    }
    public double eval(double fuel){
	boolean doubled = false;
	ArrayList<Bond> products = new ArrayList<Bond>();
	ArrayList<Bond> reactants = new ArrayList<Bond>();
	if(CHON[1]%2!=0){
	    doubleCoefficients();
	    doubled = true;
	}
	for(int c = 0; c<CHON[0]; c++){
	    products.add(new Bond("CO",oxygen,water,carbondioxide,nitrogen));
	    products.add(new Bond("CO",oxygen,water,carbondioxide,nitrogen));
	}
	for(int n = 0; n<CHON[3];n++){
	    products.add(new Bond("NN",oxygen,water,carbondioxide,nitrogen));
	}
	for(int h = 0; h<CHON[1]; h++){
	    products.add(new Bond("HO",oxygen,water,carbondioxide,nitrogen));
	}
	int oxygens = -CHON[2];
	oxygens+=2*CHON[0];
	oxygens+=CHON[1]/2;
	for(int o = 0; o<oxygens; o++){
	    reactants.add(new Bond("OO",oxygen,water,carbondioxide,nitrogen));
	}
	double retValue = -fuel;
	//System.out.println("Reactants");
	for(Bond m : reactants){
	    retValue-=m.BE;
	    //System.out.println(m.name);
	    //System.out.println(m.BE);
	    //System.out.println(retValue);
	}
	//System.out.println("Products");
	for(Bond m : products){
	    retValue+=m.BE;
	    //System.out.println(m.name);
	    //System.out.println(m.BE);
	    //System.out.println(retValue);
	}
	if(doubled){
	    retValue/=2;//To take into account doubling coefficients
	}
	System.out.println(getReaction(products, reactants, doubled));
	return retValue;
    }
    private String getReaction(ArrayList<Bond> products, ArrayList<Bond> reactants, boolean doubled){
	double molecules[] = new double[4];
	for(Bond b : products){
	    if(b.name.equals("CO")){
		molecules[1]+=.5;
	    }
	    else if(b.name.equals("NN")){
		molecules[3]+=.5;
	    }else if(b.name.equals("HO")){
		molecules[2]+=.5;
	    }
	}
	for(Bond b : reactants){
	    if(b.name.equals("OO")){
		molecules[0]+=.5;
	    }
	}//Fuel
	String fuel = "";
	if(doubled){
	    fuel+="2 ";
	    halfCoefficients();
	}
	String[] letters = {"C","H","O","N"};
	for(int i = 0; i < CHON.length; i++){
	    if(CHON[i]>0){
		fuel+=letters[i];
		if(CHON[i]>1){
		    fuel+=CHON[i];
		}
	    }
	}

	String[] names = {"O2","CO2","H2O","N2"};
	if(molecules[0]>0){
	    fuel+=" + ";
	    if(molecules[0]>1){
		if(molecules[0]==(int)molecules[0]){
		    fuel+=(int)molecules[0];
		}else{
		    fuel+=molecules[0];
		}
		fuel+=" ";
	    }
	    fuel+=names[0];
	}
	//fuel+=
	String Products = "";
	for(int m = 1; m<molecules.length; m++){
	    if(molecules[m]>0){
		Products+=" + ";
		if(molecules[m]>1){
		    if(molecules[m]==(int)molecules[m]){
			Products+=(int)molecules[m];
		    }else{
			Products+=molecules[m];
		    }
		    Products+=" ";
		}
		Products+=names[m];
	    }
	}
	if(Products.length()>3){
	    Products=Products.substring(3);
	}
	return fuel + " --> " + Products;
    }
    public static void main(String[] args){
	int[]inputs = new int[4];
	Scanner s1 = new Scanner(System.in);
	System.out.println("Enter the number of Carbons");
	inputs[0]=Integer.parseInt(s1.next());
	System.out.println("Enter the number of Hydrogens");
	inputs[1]=Integer.parseInt(s1.next());
	System.out.println("Enter the number of Oxygens");
	inputs[2]=Integer.parseInt(s1.next());
	System.out.println("Enter the number of Nitrogens");
	inputs[3]=Integer.parseInt(s1.next());
	double[] chon = new double[5];
	System.out.println("Enter the heat of formation of your fuel");
	chon[0]=Double.parseDouble(s1.next());
	System.out.println("Enter the heat of formation of oxygen");
	chon[1]=Double.parseDouble(s1.next());
	System.out.println("Enter the heat of formation of water");
	chon[2]=Double.parseDouble(s1.next());
	System.out.println("Enter the heat of formation of carbon dioxide");
	chon[3]=Double.parseDouble(s1.next());
	System.out.println("Enter the heat of formation of nitrogen");
	chon[4]=Double.parseDouble(s1.next());
	//for(int i = 0; i<inputs.length;i++){
	//    inputs[i]=Integer.parseInt(args[i]);
	//}
	BondEnergies b = new BondEnergies(inputs,chon[1],chon[2],chon[3],chon[4]);
	System.out.println(b.eval(chon[0]));
    }

}