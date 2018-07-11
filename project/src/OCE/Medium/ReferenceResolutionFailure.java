/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package OCE.Medium;

public class ReferenceResolutionFailure extends Exception {

    public ReferenceResolutionFailure(String message) {
        super("ReferenceResolution failure : "+ message);
    }
}
