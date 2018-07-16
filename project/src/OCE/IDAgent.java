/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE;

import java.util.UUID;

public class IDAgent {
    private final UUID internalReference;

    public IDAgent() {
        internalReference = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IDAgent that = (IDAgent) o;

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
}
