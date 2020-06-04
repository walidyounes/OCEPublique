/*
 * Copyright (c) 2020.  Younes Walid, IRIT, University of Toulouse
 */

package Demo.Components;

import AmbientEnvironment.MockupCompo.MockupComponent;
import AmbientEnvironment.MockupCompo.SingleLinkMockupService;
import AmbientEnvironment.MockupCompo.Way;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        providedServiceNameEscaping(at.getType().getTypeName()),
                        serviceMatchingIdEscaping(at.getType().getTypeName()),
                        componentName,
                        implementationInstance
                ));
            }
        }

        ArrayList<OCService> requiredServices = new ArrayList<>();
        for(Method m : implementationClass.getMethods()) {
            if(m.getAnnotation(Required.class) != null) {
                requiredServices.add(new RequiredService(
                        requiredServiceNameEscaping(m.getName()),
                        serviceMatchingIdEscaping(m.getGenericParameterTypes()[0].getTypeName()),
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

    /**
     * makes an example component from a class. For display purposes.
     *
     * @param implementationClass
     * @return An example component (not running)
     */
    public static MockupComponent makeExampleComponent(Class<?> implementationClass) {
        ArrayList<OCService> providedServices = new ArrayList<>();
        for(AnnotatedType at : implementationClass.getAnnotatedInterfaces()){
            if(at.getAnnotation(Provided.class) != null){
                providedServices.add(new SingleLinkMockupService(
                        providedServiceNameEscaping(at.getType().getTypeName()),
                        serviceMatchingIdEscaping(at.getType().getTypeName()),
                        implementationClass.getSimpleName(),
                        Way.PROVIDED
                ));
            }
        }

        ArrayList<OCService> requiredServices = new ArrayList<>();
        for(Method m : implementationClass.getMethods()) {
            if(m.getAnnotation(Required.class) != null) {
                requiredServices.add(new SingleLinkMockupService(
                        requiredServiceNameEscaping(m.getName()),
                        serviceMatchingIdEscaping(m.getGenericParameterTypes()[0].getTypeName()),
                        implementationClass.getSimpleName(),
                        Way.REQUIRED
                ));
            }
        }

        return new MockupComponent(
                implementationClass.getSimpleName(),
                providedServices,
                requiredServices
        );
    }
    private static String serviceMatchingIdEscaping(String rawMatchingId){
        return rawMatchingId.replace('<','_').replace('>','_').replace('.','_');
    }

    private static String requiredServiceNameEscaping(String rawName){
        return rawName.replaceFirst("set","");
    }

    private static String providedServiceNameEscaping(String rawName){
        Matcher matcher = Pattern.compile("(.*)<(.*)>").matcher(rawName);

        if(matcher.find()){
            String[] firstSplit = matcher.group(1).split("\\.");
            String[] secondSplit = matcher.group(2).split("\\.");
            return secondSplit[secondSplit.length - 1] + firstSplit[firstSplit.length - 1];
        }

        String[] split = rawName.split("\\.");
        return split[split.length - 1];
    }
}
