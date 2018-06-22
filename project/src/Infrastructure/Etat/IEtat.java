/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Etat;

/**
 * Interface representing an anbstract state of the life cyrcle of an agent
 * @author Walid YOUNES
 * @version 1.0
 */
public interface IEtat {

    /**
     * This functin allows to run the action associated to the current state
     * @param c the life cyrcle of the agent
     */
    void executer(LifeCyrcle c);
}