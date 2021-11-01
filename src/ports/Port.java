
//DO_NOT_EDIT_ANYTHING_ABOVE_THIS_LINE

package ports;

import java.util.ArrayList;
import ships.Ship;
import containers.BasicContainer;
import containers.Container;
import containers.HeavyContainer;
import containers.RefrigeratedContainer;
import interfaces.IPort;

import java.io.PrintStream;


/**
 * Represents the ports which ships visit to take or give cargo
 * @author batuhanGoc
 */
public class Port implements IPort{
	/**The number that is assigned to a specific port. Also port's index in allPorts in Main.java is same with it's ID.
	 */
	private int ID;
	/**Represents the X coordinate of Port's position 
	 */
	private double X;
	/**Represents the Y coordinate of Port's position 
	 */
	private double Y;
	/**it holds the containers that are on the port 
	 */
	private ArrayList<Container> containers = new ArrayList<Container>();
	/**it is an ArrayList which keeps track of the ships that visited this port but left
	 */
	private ArrayList<Ship> history = new ArrayList<Ship>();
	/**it is an ArrayList which keeps track of the ships that are in port currently
	 */
	private ArrayList<Ship> current = new ArrayList<Ship>();
    
	
    /** The constructor that is requested. Creates a port .
     * @param ID the number that assigned to a specific port.
     * @param X Represents the X coordinate of Port's position
     * @param Y Represents the Y coordinate of Port's position
     */
	public Port(int ID, double X,double Y) {
    	this.ID = ID;
    	this.X = X;
    	this.Y = Y;	
	}
	
	/**calculates distance between two ports
	 * @param other another port from Port class 
	 * @return the distance between the two given ports 
	 */
    public double getDistance(Port other) {
    	return Math.sqrt((X - other.X)*(X - other.X)+(Y - other.Y)*(Y - other.Y));
    }
    /** get method of X coordinate
     * @return X coordinate
     */
    public double getX() {
    	return X;
    }
    /**get method of Y coordinate
     * @return Y coordinate
     */
    public double getY() {
    	return Y;
    }

    //IPort methods
    /** adds the new ship to the port's current field if not already in
     */
    public void incomingShip(Ship s) {
    	boolean shipInCurrent = false;
    	for (int i = 0 ; i < current.size();i++) {
    	    if (s.getID() == current.get(i).getID()) {
    	    	shipInCurrent = true;
    	    	break;
    	    }
    	}	
    	if(shipInCurrent == false) {
    	    current.add(s);
    	}
    }
    
    /** removes the leaving ship from "current" and if the ship is not  in history ship is added to history
     */
    public void outgoingShip(Ship s) {
    	current.remove(s);
    	//Checking if the ship is in history or not
    	boolean shipInHistory = false;
    	for (int i = 0 ; i < history.size();i++) {
    	    if (s.getID() == history.get(i).getID()) {
    	    	shipInHistory = true;
    	    	break;
    	    }
    	}
    	if (shipInHistory == false){
            history.add(s);
    	}    		  
    }
    
    /**Adds the new container to the port
     * @param c the new container on port
     */
    public void addContainer(Container c) {
    	containers.add(c);
    }
    /**Removes the container from port.
     * Since load function already checks if the container is on port or not no checks here
     * @param c the container on port which is going to be removed
     */
    public void removeContainer(Container c) {
    	containers.remove(c);
    }
    /** Get method of containers
     * @return containers in port
     */
    public ArrayList<Container> getContainers(){
    	return containers;
    }
    /** Get method of current
     * @return sorted ships that are currently in port
     */
    public ArrayList<Ship> getCurrentShips(){
    	for(int j = 1;j<current.size();j++) {    
		    for (int i = 1;i<current.size() ;i++) {
			    if (current.get(i-1).getID() > current.get(i).getID()) {
				    Ship switchVariable = current.get(i-1);
				    current.set( i-1 , current.get(i) );
				    current.set( i , switchVariable );
			    }
		    }
    	}
		return current;
    }
    /** printContainers method's main purpose is to print the containers on port properly.
	 *  Firstly containers is sorted. Container ID's should be sorted when printing so sorting them is done now.
	 *  Secondly _Container objects are created. Their purpose is to hold separated containers according to their class
	 *  And lastly printing is done for each class separately.
	 * @param out PrintStream variable which is used for printing
	 */
    public void printContainers(PrintStream out) {
    	//Sorting
    	for(int j = 1;j<containers.size();j++) {    
		    for (int i = 1;i<containers.size() ;i++) {
			    if (containers.get(i-1).getID() > containers.get(i).getID()) {
				    Container switchVariable = containers.get(i-1);
				    containers.set( i-1 , containers.get(i) );
				    containers.set( i , switchVariable );
			    }
		    }
    	}
		// _Container are created 
    	ArrayList<Container> BContainer = new ArrayList<Container>();
    	ArrayList<Container> HContainer = new ArrayList<Container>();
    	ArrayList<Container> RContainer = new ArrayList<Container>();
    	ArrayList<Container> LContainer = new ArrayList<Container>();
    	
    	
    	for (int i = 0;i<containers.size() ;i++) {
    		if (containers.get(i).getClass().equals(BasicContainer.class)) {
    			BContainer.add(containers.get(i));
    		}
    		else if (containers.get(i).getClass().equals(HeavyContainer.class)) {
    			HContainer.add(containers.get(i));
    		}
    		else if (containers.get(i).getClass().equals(RefrigeratedContainer.class)) {
    			RContainer.add(containers.get(i));
    		}
    		else {
    			LContainer.add(containers.get(i));
    		}
    	}
    	// Lastly printing
    	if(BContainer.size()>0) {    
    	    out.print("  BasicContainer:");
    	    for (int j =0 ; j<BContainer.size();j++) {
    		    out.print(" " + BContainer.get(j).getID());   
    	    }
    	    out.println();
    	}
    	
    	if(HContainer.size()>0) {
    	    out.print("  HeavyContainer:");
    	    for (int j =0 ; j<HContainer.size();j++) {
    		    out.print(" " + HContainer.get(j).getID());
    	    }
            out.println();
    	}
    	
    	if(RContainer.size()>0) {
    	    out.print("  RefrigeratedContainer:");
    	    for (int j =0 ; j<RContainer.size();j++) {
    		    out.print(" " + RContainer.get(j).getID());
    	    }
            out.println();
    	}
    	
    	if(LContainer.size()>0) {
    	    out.print("  LiquidContainer:");
    	    for (int j =0 ; j < LContainer.size();j++) {
    		    out.print(" " + LContainer.get(j).getID());
    	    }
            out.println();
    	}
    	
    }
   
}
    
    




//DO_NOT_EDIT_ANYTHING_BELOW_THIS_LINE

