/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import AmbientEnvironment.MockupCompo.*;
import AmbientEnvironment.OCPlateforme.OCComponent;
import AmbientEnvironment.OCPlateforme.OCService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestPlantUMLJavaFX extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        MockupContainer container = new MockupContainer();

        //Construction du composant "A"
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

        //Construction du composant "C"
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
        String stringRepresentationEnvironment = componentsToPlantUMlRepresentation(container.getComponents()) ;

        SourceStringReader reader = new SourceStringReader(stringRepresentationEnvironment);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        try {
            String desc = reader.generateImage(os);
            byte [] data = os.toByteArray();

            InputStream in = new ByteArrayInputStream(data);

            AnchorPane main = new AnchorPane();
            Image img  = new Image(in);
            ImageView image = new ImageView(img);
            main.getChildren().add(image);
            final Scene scene = new Scene(main, 600, 600, Color.WHITE);
            primaryStage.setTitle("JFX Plant UML ");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String componentsToPlantUMlRepresentation(List<OCComponent> listComponents){
        String representation ="@startuml \n" + "skinparam componentStyle uml2 \n";

        List<MockupComponent> listMockupComponents = listComponents.stream().map(c-> (MockupComponent) c).collect(Collectors.toList());

        for (MockupComponent component: listMockupComponents) {
            representation += "[" + component.getName() +"] as " +  component.getName() + "\n";
            //Get the list of  required services of this component
            List<MockupService> requiredServices = component.getRequiredServices().stream().map(s -> (MockupService) s).collect(Collectors.toList());

            for (MockupService requiredService: requiredServices) {
                String interfaceID = ""+ requiredService.getName()+requiredService.getType()+requiredService.getWay()+requiredService.getOwner();
                representation += "interface " + " \" " + requiredService.getName()+ "." + requiredService.getType() + " \" " + " as " + interfaceID + "\n" ;
                representation += "[" + component.getName() +"] -right-( " +  interfaceID + "\n";
            }
            //Get the list of  provided services of this component
            List<MockupService> providedServices = component.getProvidedServices().stream().map(s -> (MockupService) s).collect(Collectors.toList());
            for (MockupService providedService: providedServices) {
                String interfaceID = ""+ providedService.getName()+providedService.getType()+providedService.getWay()+providedService.getOwner();
                representation += "interface " + " \" " + providedService.getName()+ "." + providedService.getType() + " \" " + " as " + interfaceID + "\n" ;
                representation +=  interfaceID + " - [" + component.getName() +"] \n";
            }
        }
        representation +="@enduml";
        return  representation;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
