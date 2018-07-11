/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.FacadeAdapter;

public class UnbindingFailure extends Exception {

    private static final long serialVersionUID = 127821716320841040L;

    public UnbindingFailure(Exception e) {
        super("Unbinding Failure: " + e);
    }
}
