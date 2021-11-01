
//DO_NOT_EDIT_ANYTHING_ABOVE_THIS_LINE

package containers;


/**
 * Parent class of all Containers. 
 * @author batuhanGoc
 */
public abstract class Container {
	/**
	 * ID the number that is assigned to a specific container. Also container's index in allContaniers in Main.java is same with it's ID.
	 */
	private int ID;
	/**
	 * weight is a major factor in determining fuel consumption.
	 */
	private int weight;
	
	
	/**
	 * The constructor that is told in project description for Containers
	 * @param ID the number that assigned to a specific container.
	 * @param weight is used in consumption method and weightCapaticy limit. 
	 */
	public Container(int ID,int weight) {
		this.ID= ID;
		this.weight = weight;
	}
	
	/** Checks the class and gives values according to class. 
	 * @return the consumption rate per km for this container
	 */
	public double consumption() {
		if (this.getClass().equals(BasicContainer.class)) {
			return 2.5*weight;
		}
		else if (this.getClass().equals(HeavyContainer.class)) {
			return 3.0*weight;
		}
		else if (this.getClass().equals(RefrigeratedContainer.class)) {
			return 5.0*weight;
		}
		else {
			return 4.0*weight;
		}
		
	}
	/**Compares two given Containers to see if they have the same attributes
	 * @param other second container that is used to compare
	 * @return true if ID,weight and class matches.
	 */
	public boolean equals(Container other) {
		return (other.ID == ID) && (other.weight == weight) && (this.getClass().equals(other.getClass()));
	}
	
	/**
	 * get method of Container ID
	 * @return ID of Container
	 */
	public int getID() {
		return ID;	
	}
	/**
	 * get method of Container weight
	 * @return weight of Container
	 */
	public int getWeight(){
		return weight;
	}
}



//DO_NOT_EDIT_ANYTHING_BELOW_THIS_LINE

