/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.OCPlateforme.OCService;
import Demo.Components.Annotations.Provided;
import Demo.Components.Annotations.Required;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DemoComponentFactory {

    private static Map<String, Integer> componentCount = new HashMap<>();

    private DemoComponentFactory(){

    }

    public static MockupComponent makeDemoComponent(Class<?> implementationClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(!componentCount.containsKey(implementationClass.getSimpleName())){
            componentCount.put(implementationClass.getSimpleName(), 0);
        }
        String componentName = implementationClass.getSimpleName() + "_" + componentCount.get(implementationClass.getSimpleName());
        componentCount.put(implementationClass.getSimpleName(), componentCount.get(implementationClass.getSimpleName()) + 1);

        Object implementationInstance = implementationClass.getConstructor().newInstance();

        ArrayList<OCService> providedServices = new ArrayList<>();
        for(AnnotatedType at : implementationClass.getAnnotatedInterfaces()){
            if(at.getAnnotation(Provided.class) != null){
                providedServices.add(new ProvidedService(
                        at.getType().getTypeName().replace('<','_').replace('>','_').replace('.','_'),
                        at.getType().getTypeName().replace('<','_').replace('>','_').replace('.','_'),
                        componentName,
                        implementationInstance
                ));
            }
        }

        ArrayList<OCService> requiredServices = new ArrayList<>();
        for(Method m : implementationClass.getMethods()) {
            if(m.getAnnotation(Required.class) != null) {
                requiredServices.add(new RequiredService(
                        m.getName(),
                        m.getGenericParameterTypes()[0].getTypeName().replace('<','_').replace('>','_').replace('.','_'),
                        componentName,
                        implementationInstance,
                        m
                ));
            }
        }

        return new MockupComponent(
                componentName,
                providedServices,
                requiredServices
        );
    }
}
