/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import java.util.UUID;

public class IDAgent implements Comparable{
    private  UUID internalReference;


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
    @Override
    public boolean equals(Object athat) {
        if (this == athat)
            return true;
        if (athat == null || getClass() != athat.getClass())
            return false;

        IDAgent that = (IDAgent) athat;

        return internalReference.equals(that.internalReference);
    }

    @Override
    public int hashCode() {
        return internalReference.hashCode();
    }

    @Override
    public String toString() {
        return "OCE.IDAgent{" + internalReference + "}";
    }

    @Override
    public int compareTo(Object athat) {
        IDAgent that = (IDAgent) athat;
        int result = this.internalReference.compareTo(that.internalReference);
        if (result == 0) {
            assert this.equals(that) :
                    this.getClass().getSimpleName() + ": compareTo inconsistent with equals.";
        }
        return result;
    }
}
