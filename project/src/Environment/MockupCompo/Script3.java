/*
 * Copyright (c) 2018.  Younes Walid, IRIT, University of Toulouse
 */

package Environment.MockupCompo;


public class Script3 {

    // Component "A" provides service "I" requires service "J" & service "K"
    // Component "B" provides service "K" requires service "I"
    // Component "C" provides service "J" & service "K"
    // Component "B" provides service "K"

    public static void main(String[] args) {
        MockupContainer container = new MockupContainer();

        //Construction du composant "A"
        ArrayList<OCService> providedByA = new ArrayList<OCService>();
        ArrayList<OCService> requiredByA = new ArrayList<OCService>();
        MockupService JdeA = new SingleLinkMockupService("J", "A", Way.REQUIRED);
        requiredByA.add(JdeA);
        MockupService KdeA = new SingleLinkMockupService("K", "A", Way.REQUIRED);
        requiredByA.add(KdeA);
        MockupService IdeA = new SingleLinkMockupService("I", "A", Way.PROVIDED);
        providedByA.add(IdeA);
        MockupComponent A = new MockupComponent("A", providedByA, requiredByA);
        container.addComponent(A);

        System.out.println(container.getComponents());


        //Construction du composant "C"
        ArrayList<OCService> providedByC = new ArrayList<OCService>();
        ArrayList<OCService> requiredByC = new ArrayList<OCService>();

        MockupService JdeC = new SingleLinkMockupService("J", "C", Way.PROVIDED);
        providedByC.add(JdeC);
        MockupService KdeC = new SingleLinkMockupService("K", "C", Way.PROVIDED);
        providedByC.add(KdeC);

        MockupComponent C = new MockupComponent("C", providedByC, requiredByC);
        container.addComponent(C);

        System.out.println(container.getComponents());
        container.bind(JdeA, JdeC);

        //Construction du composant "D"
        ArrayList<OCService> providedByD = new ArrayList<OCService>();
        ArrayList<OCService> requiredByD = new ArrayList<OCService>();

        MockupService KdeD = new MultiLinkMockupService("K", "D", Way.PROVIDED);
        providedByD.add(KdeD);
        MockupComponent D = new MockupComponent("D", providedByD, requiredByD);
        container.addComponent(D);

        System.out.println(container.getComponents());
        container.bind(KdeA, KdeD);

        //Construction du composant "B"
        ArrayList<OCService> providedByB = new ArrayList<OCService>();
        ArrayList<OCService> requiredByB = new ArrayList<OCService>();

        MockupService KdeB = new SingleLinkMockupService("K", "B", Way.PROVIDED);
        providedByB.add(KdeB);
        MockupService IdeB = new SingleLinkMockupService("I", "B", Way.REQUIRED);
        requiredByB.add(KdeA);
        MockupComponent B = new MockupComponent("B", providedByB, requiredByB);
        container.addComponent(B);

        System.out.println(container.getComponents());
        container.bind(IdeA, IdeB);
        container.unbind(KdeA, KdeD);
        container.bind(KdeA, KdeB);
    }

}
