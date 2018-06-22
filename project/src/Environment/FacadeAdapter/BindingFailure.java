/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.FacadeAdapter;

public class BindingFailure extends Exception {

    private static final long serialVersionUID = 7541262372471405085L;

    public BindingFailure(Exception e) {
        super("Binding Failure: " + e);
    }


}
