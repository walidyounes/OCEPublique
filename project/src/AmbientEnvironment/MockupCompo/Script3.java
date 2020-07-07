/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package AmbientEnvironment.MockupCompo;


import AmbientEnvironment.OCPlateforme.OCService;

import java.util.ArrayList;

public class Script3 {

    // Component "A" provides service "I" requires service "J" & service "K"
    // Component "B" provides service "K" requires service "I"
    // Component "C" provides service "J" & service "K"
    // Component "B" provides service "K"

    public static void main(String[] args) {
        MockupContainer container = new MockupContainer();

        //Construction du composant "A"
        ArrayList<OCService> providedByA = new ArrayList<>();
        ArrayList<OCService> requiredByA = new ArrayList<>();
        MockupService JdeA = new SingleLinkMockupService("J", "J","A", Way.REQUIRED, "Low");
        requiredByA.add(JdeA);
        MockupService KdeA = new SingleLinkMockupService("K", "K","A", Way.REQUIRED, "Low");
        requiredByA.add(KdeA);
        MockupService IdeA = new SingleLinkMockupService("I", "I","A", Way.PROVIDED, "Low");
        providedByA.add(IdeA);
        MockupComponent A = new MockupComponent("A", providedByA, requiredByA);
        container.addComponent(A);

        System.out.println(container.getComponents());


        //Construction du composant "C"
        ArrayList<OCService> providedByC = new ArrayList<>();
        ArrayList<OCService> requiredByC = new ArrayList<>();

        MockupService JdeC = new SingleLinkMockupService("J", "J","C", Way.PROVIDED, "Low");
        providedByC.add(JdeC);
        MockupService KdeC = new SingleLinkMockupService("K", "K", "C", Way.PROVIDED, "Low");
        providedByC.add(KdeC);

        MockupComponent C = new MockupComponent("C", providedByC, requiredByC);
        container.addComponent(C);

        System.out.println(container.getComponents());
        container.bind(JdeA, JdeC);

        //Construction du composant "D"
        ArrayList<OCService> providedByD = new ArrayList<>();
        ArrayList<OCService> requiredByD = new ArrayList<>();

        MockupService KdeD = new MultiLinkMockupService("K", "K","D", Way.PROVIDED);
        providedByD.add(KdeD);
        MockupComponent D = new MockupComponent("D", providedByD, requiredByD);
        container.addComponent(D);

        System.out.println(container.getComponents());
        container.bind(KdeA, KdeD);

        //Construction du composant "B"
        ArrayList<OCService> providedByB = new ArrayList<>();
        ArrayList<OCService> requiredByB = new ArrayList<>();

        MockupService KdeB = new SingleLinkMockupService("K", "K","B", Way.PROVIDED, "Low");
        providedByB.add(KdeB);
        MockupService IdeB = new SingleLinkMockupService("I", "I","B", Way.REQUIRED, "Low");
        requiredByB.add(KdeA);
        MockupComponent B = new MockupComponent("B", providedByB, requiredByB);
        container.addComponent(B);

        System.out.println(container.getComponents());
        container.bind(IdeA, IdeB);
        container.unbind(KdeA, KdeD);
        container.bind(KdeA, KdeB);
    }

}
