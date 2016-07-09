import java.util.ArrayList;
public class BondEnergies{
    private class Bond{
	public String name;
	public double BE;
	public Bond(String s){
	    name = s;
	    if(name.equals("CO")){//C0 double bond in CO2
		BE=799;
	    }else if(name.equals("NO")){//N0 bond in NO2
		BE=406;//Need to find value
	    }else if(name.equals("HO")){//HO bond in H2O
		BE=464.0+22.0080000;
	    }else if(name.equals("OO")){//HALF of a bond in O2
		BE=498/2;
	    }else{
		throw new IllegalArgumentException();
	    }
	}
    }
    public BondEnergies(int[] data){
	if(data.length!=4){
	    throw new IllegalArgumentException();
	}else{
	    CHON = new int[4];
	    for(int i = 0; i < data.length; i++){
		CHON[i]=data[i];
	    }
	}
    }
    private int[] CHON;
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
    public double eval(){
	boolean doubled = false;
	ArrayList<Bond> products = new ArrayList<Bond>();
	ArrayList<Bond> reactants = new ArrayList<Bond>();
	if(CHON[1]%2!=0){
	    doubleCoefficients();
	    doubled = true;
	}
	for(int c = 0; c<CHON[0]; c++){
	    products.add(new Bond("CO"));
	    products.add(new Bond("CO"));
	}
	for(int n = 0; n<CHON[3];n++){
	    products.add(new Bond("NO"));
	    products.add(new Bond("NO"));
	}
	for(int h = 0; h<CHON[1]; h++){
	    products.add(new Bond("HO"));
	}
	int oxygens = -CHON[2];
	oxygens+=2*CHON[0];
	oxygens+=CHON[1]/2;
	oxygens+=2*CHON[3];
	for(int o = 0; o<oxygens; o++){
	    reactants.add(new Bond("OO"));
	}
	double retValue = 0;
	//System.out.println("Reactants");
	for(Bond m : reactants){
	    retValue+=m.BE;
	    System.out.println(m.name);
	    System.out.println(m.BE);
	    System.out.println(retValue);
	}
	//System.out.println("Products");
	for(Bond m : products){
	    retValue-=m.BE;
	    System.out.println(m.name);
	    System.out.println(m.BE);
	    System.out.println(retValue);
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
	    else if(b.name.equals("NO")){
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

	String[] names = {"O2","CO2","H2O","NO2"};
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
	for(int i = 0; i<inputs.length;i++){
	    inputs[i]=Integer.parseInt(args[i]);
	}
	BondEnergies b = new BondEnergies(inputs);
	System.out.println(b.eval());
    }

}