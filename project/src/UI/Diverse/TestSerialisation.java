/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.OCPlateforme.OCService;

import java.io.*;
import java.util.ArrayList;

public class TestSerialisation {

    public static void main(String args[]){

        MockupContainer container = new MockupContainer();

        //Construction of the component "A"
        ArrayList<OCService> providedByA = new ArrayList<>();
        ArrayList<OCService> requiredByA = new ArrayList<>();
        MockupService JdeA = new SingleLinkMockupService("J", "J","A", Way.REQUIRED);
        requiredByA.add(JdeA);
        MockupService KdeA = new SingleLinkMockupService("K", "K","A", Way.REQUIRED);
        requiredByA.add(KdeA);
        MockupService IdeA = new SingleLinkMockupService("I", "I","A", Way.PROVIDED);
        providedByA.add(IdeA);
        MockupComponent A = new MockupComponent("A", providedByA, requiredByA);
        container.addComponent(A);

        //Construction of the component "C"
        ArrayList<OCService> providedByC = new ArrayList<>();
        ArrayList<OCService> requiredByC = new ArrayList<>();

        MockupService JdeC = new SingleLinkMockupService("J", "J","C", Way.PROVIDED);
        providedByC.add(JdeC);
        MockupService KdeC = new SingleLinkMockupService("K", "K", "C", Way.PROVIDED);
        providedByC.add(KdeC);

        MockupComponent C = new MockupComponent("C", providedByC, requiredByC);
        container.addComponent(C);


        // ************************************************************************************************
        // ************************************************************************************************
        // ************************************************************************************************


        String filename = "TestSerialisation.txt";
        // Saving of object in a file
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(container);

            out.close();
            file.close();

        } catch (IOException  e) {
            e.printStackTrace();
        }


        System.out.println("Object has been serialized\n"
                + "Data before Deserialization.");

        container = null;

        // Deserialization
        try {

            // Reading the object from a file
            FileInputStream fileIn = new FileInputStream (filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            // Method for deserialization of object
            container = (MockupContainer) in.readObject();

            in.close();
            fileIn.close();
            System.out.println("Object has been deserialized\n"
                    + "Data after Deserialization.");

            System.out.println(container.getComponents());

            // System.out.println("z = " + object1.z);
        }

        catch (IOException ex) {
            System.out.println("IOException is caught");
        }

        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException" +
                    " is caught");
        }

    }
}
