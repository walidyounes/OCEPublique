/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import java.util.UUID;

public class IDAgent implements Comparable{
    private  UUID internalReference; // Agent's reference
    private String visualizingName; // Agent's visualizing name, used when printing agent information

    /**
     * Constructor of the IDAgent with a random ID value
     */
    public IDAgent() {
        this.internalReference = UUID.randomUUID();
    }

    /**
     * Constructor of the IdAgent with a custom ID value
     * @param uuid : the value of the ID
     */
    public IDAgent(String uuid){
        this.internalReference = UUID.fromString(uuid);
    }

    /**
     * Get the value of the ID
     * @return the random generated ID
     */
    public UUID getInternalReference() {
        return internalReference;
    }

    /**
     * Set the value of the ID
     * @param internalReference : The new value of the ID;
     */
    public void setInternalReference(UUID internalReference) {
        this.internalReference = internalReference;
    }

    /**
     * Get the value of the visualizing name of the agent
     * @return the visualizing name
     */
    public String getVisualizingName() {
        return visualizingName;
    }

    /**
     * Update the value of the visualizing name for the agent
     * @param visualizingName : the new value
     */
    public void setVisualizingName(String visualizingName) {
        this.visualizingName = visualizingName;
    }

    @Override
    public boolean equals(Object athat) {
        if (athat == null || getClass() != athat.getClass())
            return false;
        if (this == athat)
            return true;
        IDAgent that = (IDAgent) athat;

        return internalReference.equals(that.internalReference);
    }

    @Override
    public int hashCode() {
        return internalReference.hashCode();
    }

//    @Override
//    public String toString() {
//        return "OCE.IDAgent{" + this.visualizingName + "}";
//    }

    @Override
    public String toString() {
        return "{" + this.visualizingName + "}";
    }

    @Override
    public int compareTo(Object athat) {
        if(athat == null || getClass() != athat.getClass()) return -1;
        if(this == athat) return 0;
        IDAgent that = (IDAgent) athat;
        int result = this.internalReference.compareTo(that.internalReference);
        if (result == 0) {
            assert this.equals(that) :
                    this.getClass().getSimpleName() + ": compareTo inconsistent with equals.";
        }
        return result;
    }
}
