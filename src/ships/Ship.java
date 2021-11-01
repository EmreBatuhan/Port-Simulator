
//DO_NOT_EDIT_ANYTHING_ABOVE_THIS_LINE

package ships;

import java.io.PrintStream;
import java.util.ArrayList;
import ports.Port;
import containers.Container;
import interfaces.IShip;

import containers.BasicContainer;
import containers.HeavyContainer;
import containers.LiquidContainer;
import containers.RefrigeratedContainer;

/**Ships can load containers and carry those containers to other ports
 * @author batuhanGoc
 */
public class Ship implements IShip{
	/** The number that is assigned to a specific ship. Also ship's index in allShips in Main.java is same with it's ID.
	 */
	private int ID;
	/**	The fuel counter of the ship. toSail method requires fuel to operate. fuel can be added with reFuel method. 
	 */
	private double fuel;
	/** The port which ship is currently in.
	 */
	private Port currentPort;
	/** Not requested but used in constructor.It's purpose is to keep track of weight limit in ship.
	 */
	
	private int totalWeightCapacity;
	/**Not requested but used in constructor.It's purpose is to keep track of container quantity limit(all) in ship.
	 */
	private int maxNumberOfAllContainers;
	/**Not requested but used in constructor.It's purpose is to keep track of container quantity limit(heavy ones) in ship.
	 */
	private int maxNumberOfHeavyContainers;
	/**Not requested but used in constructor.It's purpose is to keep track of container quantity limit(refrigerated) in ship.
	 */
	private int maxNumberOfRefrigeratedContainers;
	/**Not requested but used in constructor.It's purpose is to keep track of container quantity limit(liquid) in ship.
	 */
	private int maxNumberOfLiquidContainers;
	
    /**Ship's own rate of fuel consumption per km.It is added to the containers' consumption per km in sailTo method.
     */
	private double fuelConsumptionPerKm;
	
	/**Keeps track of the containers that are on ship. Not necessarily sorted so it will be sorted when needed.
	 */
	ArrayList<Container> currentContainers;
	
	/** Counting the number of all containers in the ship . It is used in load method to control if limit is exceeded or not.
	 */
	private int currentNumberOfAllContainers;
	/** Counting the number of heavy containers in the ship . It is used in load method to control if limit is exceeded or not.
	 */
	private int currentNumberOfHeavyContainers;
	/** Counting the number of refrigerated containers in the ship . It is used in load method to control if limit is exceeded or not.
	 */
	private int currentNumberOfRefrigeratedContainers;
	/** Counting the number of liquid containers in the ship . It is used in load method to control if limit is exceeded or not.
	 */
	private int currentNumberOfLiquidContainers;
	
	
	
	/**
	 * The constructor that is requested. Some parameters are not required field. In order to store them some extra fields were created.
	 * @param ID the number that assigned to a specific ship.
	 * @param p is the port which ship currently in.
	 * @param totalWeightCapacity keep track of weight limit in ship.
	 * @param maxNumberOfAllContainers keeps track of container quantity limit(all) in ship.
	 * @param maxNumberOfHeavyContainers keeps track of container quantity limit(heavy) in ship.
	 * @param maxNumberOfRefrigeratedContainers keeps track of container quantity limit(refrigerated) in ship.
	 * @param maxNumberOfLiquidContainers keeps track of container quantity limit(liquid) in ship.
	 * @param fuelConsumptionPerKm Ship's own rate of fuel consumption per km
	 */
	public Ship(int ID,Port p,int totalWeightCapacity,int maxNumberOfAllContainers,int maxNumberOfHeavyContainers, 
	int maxNumberOfRefrigeratedContainers, int maxNumberOfLiquidContainers , double fuelConsumptionPerKm) {
		this.ID = ID;
		this.currentPort = p;
		this.totalWeightCapacity = totalWeightCapacity;
		this.fuel = 0;
		
		this.maxNumberOfAllContainers = maxNumberOfAllContainers ;
		this.maxNumberOfHeavyContainers = maxNumberOfHeavyContainers ;
		this.maxNumberOfRefrigeratedContainers = maxNumberOfRefrigeratedContainers ;
		this.maxNumberOfLiquidContainers = maxNumberOfLiquidContainers ;
		this.fuelConsumptionPerKm = fuelConsumptionPerKm ;
		this.currentContainers = new ArrayList<Container>();
		this.currentNumberOfAllContainers = 0;
		this.currentNumberOfHeavyContainers = 0;
		this.currentNumberOfRefrigeratedContainers = 0;
		this.currentNumberOfLiquidContainers = 0;
		
		p.incomingShip(this);
		
	}
	/**
	 * currentContainers is sorted and then returned
	 * @return sorted currentContainers
	 */
	public ArrayList<Container> getCurrentContainers(){
		for(int j = 1;j<currentContainers.size();j++) {    
		    for (int i = 1;i<currentContainers.size() ;i++) {
			    if (currentContainers.get(i-1).getID() > currentContainers.get(i).getID()) {
				    Container switchVariable = currentContainers.get(i-1);
				    currentContainers.set( i-1 , currentContainers.get(i) );
				    currentContainers.set( i , switchVariable );
			    }
		    }
		}return currentContainers;
	}


    //IShip methods
	
	/** Firstly necessary fuel amount is calculated. if fuel is enough ship sails to destination port
	 * @param p destination port
	 */
	public boolean sailTo(Port p) {
		
		double totalConsumptionRate = fuelConsumptionPerKm;
		for (int j=0; j < currentContainers.size();j++) {
			totalConsumptionRate += currentContainers.get(j).consumption();
		}
	    
		if (fuel >= p.getDistance(currentPort)*totalConsumptionRate) {
			fuel -= p.getDistance(currentPort)*totalConsumptionRate;
			currentPort.outgoingShip(this);
			p.incomingShip(this);
			currentPort = p;
			
			
			return true;
		}
		return false;
			
	}
	/** Adds more fuel to ship
	 */
	public void reFuel(double newFuel) {
		fuel += newFuel;
	}
	
	
	/** load method checks if a container can be loaded and if it can ,load method does the necessary changes to ship.
	 */
	public boolean load(Container cont) {
		//Container is checked whether it is on the port which the ship is.
		boolean containerOnPort = false;
		for (int i=0 ; i < currentPort.getContainers().size();i++) {
			if (cont.equals(currentPort.getContainers().get(i))){
				containerOnPort = true;
			}
		}
		//Calculating total weight.
		int currentWeight = 0;
		for (int i = 0 ; i < currentContainers.size() ; i++) {
			currentWeight += currentContainers.get(i).getWeight();
		}
		
		//Weight and container limit(all) is checked
		if (containerOnPort && currentWeight + cont.getWeight() <= totalWeightCapacity && currentNumberOfAllContainers < maxNumberOfAllContainers) {
			if (cont.getClass().equals(BasicContainer.class)) {
			    currentContainers.add(cont);
			    currentNumberOfAllContainers++;
			    currentPort.removeContainer(cont);
			    return true;
		    }
			//if cont is not BasicContainer then container limit(heavy) checked now since this limit affects all containers except BasicContainers.
			if (currentNumberOfHeavyContainers < maxNumberOfHeavyContainers) {
			    
				if (cont.getClass().equals(HeavyContainer.class)){
			    	currentContainers.add(cont);
				    currentNumberOfAllContainers++;
				    currentNumberOfHeavyContainers++;
				    currentPort.removeContainer(cont);
				    return true;
			    }
			    if (cont.getClass().equals(RefrigeratedContainer.class) && currentNumberOfRefrigeratedContainers < maxNumberOfRefrigeratedContainers){
			    	currentContainers.add(cont);
				    currentNumberOfAllContainers++;
				    currentNumberOfHeavyContainers++;
				    currentNumberOfRefrigeratedContainers++;
				    currentPort.removeContainer(cont);
				    return true;
			    }
			    if (cont.getClass().equals(LiquidContainer.class) && currentNumberOfLiquidContainers < maxNumberOfLiquidContainers) {
			    	currentContainers.add(cont);
				    currentNumberOfAllContainers++;
				    currentNumberOfHeavyContainers++;
				    currentNumberOfLiquidContainers++;
				    currentPort.removeContainer(cont);
				    return true;
			    }	
		    }    
		}    
		return false;//if a container was not loaded
	}
    /** unLoad method checks if a container can be unloaded and if it can, unLoad method does necessary changes to ship.
     */
	public boolean unLoad(Container cont) {
		
		//Container is checked whether it is on ship.
		boolean containerOnShip = false;
		for (int i=0 ; i < currentContainers.size();i++) {
			if (cont.equals(currentContainers.get(i))){
				containerOnShip = true;
			}
		}
		//Since unloading does not make ship exceed any limits , no checks on limits.
		if (containerOnShip) {
			if (cont.getClass().equals(BasicContainer.class)) {
				currentContainers.remove(cont);
				currentNumberOfAllContainers--;
				currentPort.addContainer(cont);
				return true;
			}
			if (cont.getClass().equals(HeavyContainer.class)){
		    	currentContainers.remove(cont);
			    currentNumberOfAllContainers--;
			    currentNumberOfHeavyContainers--;
			    currentPort.addContainer(cont);
			    return true;
			}
			if (cont.getClass().equals(RefrigeratedContainer.class) ){
				currentContainers.remove(cont);
			    currentNumberOfAllContainers--;
			    currentNumberOfHeavyContainers--;
			    currentNumberOfRefrigeratedContainers--;
			    currentPort.addContainer(cont);
			    return true;
			}
			if (cont.getClass().equals(LiquidContainer.class) ) {
		    	currentContainers.remove(cont);
			    currentNumberOfAllContainers--;
			    currentNumberOfHeavyContainers--;
			    currentNumberOfLiquidContainers--;
			    currentPort.addContainer(cont);
			    return true;
		    }
		}
		return false;//if a container was not unloaded
	}

    
    /** get method of currentPort
     * @return currentPort
     */
	public Port getCurrentPort() {
		return currentPort;
	}
	/** get method of fuelConsumptionPerKm
	 * @return fuel consumption rate of ship per km
	 */
	public double getFuelConsumption() {
		return fuelConsumptionPerKm;
	}
	/** get method of ID
	 * @return ID of ship
	 */
	public int getID() {
		return ID;
	}
	/** get method of fuel
	 * @return remaining fuel
	 */
	public double getFuel() {
		return fuel;
	}
	/** printContainers method's main purpose is to print the containers on ship properly.
	 *  Firstly currentContainers is sorted. Containers ID's should be sorted when printing so sorting them is done now.
	 *  Secondly _Container objects are created. Their purpose is to hold separated containers according to their class
	 *  And lastly printing is done for each class separately.
	 * @param out PrintStream variable which is used for printing
	 */
	public void printContainers(PrintStream out) {
    	//Sorting
		for(int j = 1;j<currentContainers.size();j++) {    
		    for (int i = 1;i<currentContainers.size() ;i++) {
			    if (currentContainers.get(i-1).getID() > currentContainers.get(i).getID()) {
				    Container switchVariable = currentContainers.get(i-1);
				    currentContainers.set( i-1 , currentContainers.get(i) );
				    currentContainers.set( i , switchVariable );
			    }
		    }
    	}
		// _Container are created and getting containers from currentContainers
    	ArrayList<Container> BContainer = new ArrayList<Container>();
    	ArrayList<Container> HContainer = new ArrayList<Container>();
    	ArrayList<Container> RContainer = new ArrayList<Container>();
    	ArrayList<Container> LContainer = new ArrayList<Container>();
    	
    	
    	for (int i = 0;i<currentContainers.size() ;i++) {
    		if (currentContainers.get(i).getClass().equals(BasicContainer.class)) {
    			BContainer.add(currentContainers.get(i));
    		}
    		else if (currentContainers.get(i).getClass().equals(HeavyContainer.class)) {
    			HContainer.add(currentContainers.get(i));
    		}
    		else if (currentContainers.get(i).getClass().equals(RefrigeratedContainer.class)) {
    			RContainer.add(currentContainers.get(i));
    		}
    		else {
    			LContainer.add(currentContainers.get(i));
    		}
    	}
    	// Lastly printing
    	if(BContainer.size()>0) {    
    	    out.print("    BasicContainer:");
    	    for (int j =0 ; j<BContainer.size();j++) {
    		    out.print(" " + BContainer.get(j).getID());   
    	    }
    	    out.println();
    	}
    	
    	if(HContainer.size()>0) {
    	    out.print("    HeavyContainer:");
    	    for (int j =0 ; j<HContainer.size();j++) {
    		    out.print(" " + HContainer.get(j).getID());
    	    }
            out.println();
    	}
    	
    	if(RContainer.size()>0) {
    	    out.print("    RefrigeratedContainer:");
    	    for (int j =0 ; j<RContainer.size();j++) {
    		    out.print(" " + RContainer.get(j).getID());
    	    }
            out.println();
    	}
    	
    	if(LContainer.size()>0) {
    	    out.print("    LiquidContainer:");
    	    for (int j =0 ; j < LContainer.size();j++) {
    		    out.print(" " + LContainer.get(j).getID());
    	    }
            out.println();
    	}

	}

}



//DO_NOT_EDIT_ANYTHING_BELOW_THIS_LINE

