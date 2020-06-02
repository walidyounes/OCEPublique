/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo;


import Demo.Components.Impl.ColorPicker;
import Demo.Components.Impl.IntDisplay;
import Demo.Components.Impl.IntToIntAndItsNegative;
import Demo.Components.Impl.RandomIntGenerator;


import java.util.HashMap;
import java.util.Map;


public class Demo {

    private static final Class<?>[] AVAILABLE_CLASSES = {
            IntDisplay.class,
            RandomIntGenerator.class,
            IntToIntAndItsNegative.class,
            ColorPicker.class
    };

    public static final Map<String,Class<?>> AVAILABLE_CLASSES_MAP = buildClassesMap();

    private static Map<String,Class<?>> buildClassesMap(){
        Map<String,Class<?>> map = new HashMap<>();
        for(Class c : AVAILABLE_CLASSES){
            map.put(c.getSimpleName(),c);
        }
        return map;
    }
}
