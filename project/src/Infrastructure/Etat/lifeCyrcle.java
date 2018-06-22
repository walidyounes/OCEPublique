/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Infrastructure.Etat;


import Infrastructure.Agent.ReferenceAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implement a life cycle of an agent, it consist in
 * currentState : the current state of the agent
 * sharedMemorie : an shared area to store data
 * @author Walid YOUNES
 * @version 1.0
 */
public class lifeCyrcle {
    private IEtat currentState;
    private ReferenceAgent refAgent; // La référence de l'agent possédant le cycle
    private Map<String, ArrayList> sharedMemorie;

    /**
     * Constructor of the class
     * @param initialState : the first state of the cycle
     * @param refAgent : the reference of the
     */
    public lifeCyrcle(IEtat initialState, ReferenceAgent refAgent) {
        this.refAgent = refAgent;
        this.currentState = initialState;
        this.sharedMemorie = new HashMap<>();
    }

    /**
     * Change the current state of the agent
     * @param newState : the new state in the life cycle of the agent
     */
    public void setCurrentState(IEtat newState) {
        this.currentState = newState;
    }

    /***
     * Get the current state
     * @return the current state of the agent
     */
    public IEtat getCurrentState() {
        return currentState;
    }

    /**
     * This function allow to share data in a labelized area
     * @param variableName : the name of the are, its used to distinguish the different areas
     * @param dataToshare  : the data to share between the states
     */
    public void shareVariable(String variableName, ArrayList dataToshare) {
        if (this.sharedMemorie.containsKey(variableName)) {
            //Si l'état existe déjà on remplace juste la variable
            this.sharedMemorie.replace(variableName, dataToshare);
        } else {
            this.sharedMemorie.put(variableName, dataToshare);
        }
    }

    /**
     *  get the shared data in the area named "variableName"
     * @param variableName : The name of the area
     * @return the data stored in that area
     */
    public ArrayList getSharedData(String variableName) {
        return this.sharedMemorie.get(variableName);
    }

    /**
     * This function runs the current state of an agent
     */
    public void run() {
        this.currentState.executer(this);
    }
}
