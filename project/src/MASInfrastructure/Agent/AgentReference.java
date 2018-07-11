/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Agent;

import java.util.UUID;

public class AgentReference {
    private final UUID referenceInterne;

    public AgentReference() {
        referenceInterne = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AgentReference that = (AgentReference) o;

        return referenceInterne.equals(that.referenceInterne);
    }

    @Override
    public int hashCode() {
        return referenceInterne.hashCode();
    }

    @Override
    public String toString() {
        return "OCE.Agent{" +
                "reference=" + referenceInterne;
    }
}
