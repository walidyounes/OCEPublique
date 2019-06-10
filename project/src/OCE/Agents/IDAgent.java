/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Agents;

import java.util.Comparator;
import java.util.UUID;

public class IDAgent implements Comparable{
    private final UUID internalReference;

    public IDAgent() {
        internalReference = UUID.randomUUID();
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
        return "OCE.InfraAgent{" +
                "ID=" + internalReference;
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
