/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package MASInfrastructure.Agent;

import java.util.UUID;

public class InfraAgentReference {
    private final UUID referenceInterne;

    public InfraAgentReference() {
        referenceInterne = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InfraAgentReference that = (InfraAgentReference) o;

        return referenceInterne.equals(that.referenceInterne);
    }

    /**
     * Get the value of the reference of the agent
     * @return the UUID value of the reference of the agent
     */
    public UUID getReferenceInterne() {
        return referenceInterne;
    }

    @Override
    public int hashCode() {
        return referenceInterne.hashCode();
    }

    @Override
    public String toString() {
        return "INFRA.IDAgent{" + this.referenceInterne + '}';
    }
}
