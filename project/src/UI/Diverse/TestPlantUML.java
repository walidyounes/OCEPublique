/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI.Diverse;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;

public class TestPlantUML {



    public static void main(String[] args){
        String firstComponent =  " @startuml \n";
        firstComponent+= "skinparam componentStyle uml2 \n";
        firstComponent+= "interface \"Data Access\" as DA \n";
        firstComponent+= "DA - [First Component] \n";
        firstComponent+= "[First Component] ..> HTTP : use \n";
        firstComponent+= "@enduml";

        SourceStringReader reader = new SourceStringReader(firstComponent);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        try {
            String desc = reader.generateImage(os);
            //os.close();
            // The XML is stored into svg
            //final String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
            byte [] data = os.toByteArray();

            InputStream in = new ByteArrayInputStream(data);
            BufferedImage convImg = ImageIO.read(in);

            ImageIO.write(convImg, "png", new File("C:\\Users\\wyounes\\Desktop\\imagePlantUML.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
