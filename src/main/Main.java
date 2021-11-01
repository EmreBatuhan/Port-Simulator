
//DO_NOT_EDIT_ANYTHING_ABOVE_THIS_LINE

package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.util.Scanner;

import java.util.ArrayList;

import ports.Port;
import ships.Ship;
import containers.Container;
import containers.BasicContainer;
import containers.HeavyContainer;
import containers.LiquidContainer;
import containers.RefrigeratedContainer;





/** input is read with command. When a new object has been created it is added to "all___" .
 * after reading, printing is done with printContainers methods from Ship.java and Port.java .
 * 
 * @author batuhanGoc
 */
public class Main {
	/**
	 * 
	 * @param args 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		
		int N = in.nextInt();
		
		// allContainer,allShips,allPorts store all of the containers,ships,ports respectively. indexes of the elements are also their IDs.
		ArrayList<Container> allContainers = new ArrayList<Container>() ;
		ArrayList<Ship> allShips = new ArrayList<Ship>() ;
		ArrayList<Port> allPorts = new ArrayList<Port>() ;
		
		
		for (int i = 0 ; i < N;i++) {
			
			//command determines what interaction will be done next
			int command = in.nextInt();
			
			if (command == 1) {
			    int portID = in.nextInt();
			    int weight = in.nextInt();
			    String lineEnd = in.nextLine();
			    if (lineEnd.strip().equals("R") || lineEnd.strip().equals("L")) {
			    	// checking if the end of the line is R or L
			    	if (lineEnd.strip().equals("R")) {			    		
			    		RefrigeratedContainer newC = new RefrigeratedContainer(allContainers.size(),weight);
			    	    allPorts.get(portID).addContainer(newC);
			    	    allContainers.add(newC);
			    	    
			    	}
			    	else {
			    		LiquidContainer newC = new LiquidContainer(allContainers.size(),weight);
			    		allPorts.get(portID).addContainer(newC);
			    	    allContainers.add(newC);
			    	}	
			    }
			    else if(lineEnd.strip() == ""){
			    	// if the end of the line is empty
			    	
			    	if (weight <= 3000) {
			    		BasicContainer newC = new BasicContainer(allContainers.size(),weight);
			    		allPorts.get(portID).addContainer(newC);
			    	    allContainers.add(newC);
			    	}
			    	else {
			    		HeavyContainer newC = new HeavyContainer(allContainers.size(),weight);
			    		allPorts.get(portID).addContainer(newC);
			    	    allContainers.add(newC);
			    	}
			    }
			}
			
			else if (command == 2) {
				int portID = in.nextInt();
				int maxWeight = in.nextInt();
				int maxNumContainer = in.nextInt();
				int maxNumHContainer = in.nextInt();
				int maxNumRContainer = in.nextInt();
				int maxNumLContainer = in.nextInt();
				double consumptionPerKm = in.nextDouble();
				
				Ship newS = new Ship(allShips.size(),allPorts.get(portID),maxWeight,maxNumContainer,maxNumHContainer,
						maxNumRContainer,maxNumLContainer , consumptionPerKm);
				allShips.add(newS);
			
			}
			
			else if (command == 3) {
				double X = in.nextDouble();
				double Y = in.nextDouble();
				
				Port newP = new Port(allPorts.size(),X,Y);
			    allPorts.add(newP);
				
			}
			
			else if (command == 4) {
				int shipID = in.nextInt();
				int containerID = in.nextInt();
				
				allShips.get(shipID).load(allContainers.get(containerID));
			}
			
			else if (command == 5) {
				int shipID = in.nextInt();
				int containerID = in.nextInt();
		    
			    allShips.get(shipID).unLoad(allContainers.get(containerID));
			}
	
			else if (command == 6) {
				int shipID = in.nextInt();
				int portID = in.nextInt();
				allShips.get(shipID).sailTo(allPorts.get(portID));
						
			}
			
			else if (command == 7) {
				int shipID = in.nextInt();
				double fuelAmount = in.nextDouble();
				
				allShips.get(shipID).reFuel(fuelAmount);
			}
		}
		
		for (int i = 0; i < allPorts.size() ; i++) {
			
			//Printing port with coordinates
			out.print("Port " + i + ": ("  );
			out.print(String.format("%.2f",allPorts.get(i).getX()));
			out.println(String.format(", %.2f)",allPorts.get(i).getY()));
			
			allPorts.get(i).printContainers(out);
			for(int j = 0; j < allPorts.get(i).getCurrentShips().size();j++) {
				out.print("  Ship " + allPorts.get(i).getCurrentShips().get(j).getID());
				out.println(String.format(": %.2f",allPorts.get(i).getCurrentShips().get(j).getFuel()));
				allPorts.get(i).getCurrentShips().get(j).printContainers(out);
			}
		}
		
		
		in.close();
		out.close();
	}
}



//DO_NOT_EDIT_ANYTHING_BELOW_THIS_LINE

