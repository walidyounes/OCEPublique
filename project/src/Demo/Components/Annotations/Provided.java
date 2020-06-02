/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Provided {
}
