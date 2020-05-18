# OCE

## Opportunistic Composition Engine

This project contains the implementation of the Opportunistic Composition Engine **OCE** 

### IDE & language

OCE is implemented in **Java** 
```JAVA
JDK 9.0
```

Using the **IntelliJ IDEA** 

https://www.jetbrains.com/fr-fr/idea/


### Content of the project

```JAVA
To be done
```

### Configuration in IntelliJ IDEA

Before starting: make sure that you have **JDK 9.0** and **IntelliJ IDEA**  installed on your computer

1. Clone the project from the [GitHub] repository (In the project homepage https://github.com/SylvieTrouilhet/OCE click on **Clone or download**  and copy the link)
1. Open **IntelliJ IDEA** 
    1. Click on *New project*
    1. Choose *Project from version control*
    1. In the new window, for version control choose *Git*
    1. Paste the link of the project (the previous copied link)
    1. Click on *clone*
    1. A new window will appear to log in into your Github account with Intellij (if you did it already, ignore this step)
    1. Intellij should start downlaodng the project
1. Wait until the *configuration* and *indexing* of the project is complete
1. If the *JDK* is not automatically configured, add it manually to the project (it should appear in **External librairies**)
```JAVA
To be completed
```

### Configuration in Eclipse IDEA

```JAVA
To be done
```

### Configuration in VSCode

```JAVA
To be completed by walid with Maxime's help :)
```

### Integrating OCE and ICE (OCE configuration)

To integrate OCE and ICE you must change in OCE's code to update ICE's files path. 

1. Open package *MOICE*
1. Open *ICEXMLFormatter* class
    - In line **22** edit the exiting line code : 
    ```JAVA
    final String ICEFilePath = "C:\\Users\\wyounes\\runtime-Editor\\org.eclipse.ice.editor\\ICE.ice_editor";
    ``` 
    update the file path to 
    ```JAVA
    final String ICEFilePath  = " the path of the file runtime-Editor\\org.eclipse.ice.editor\\ICE.ice_editor in your laptop";
    ``` 
 1. Open *UI*
 1. Open *UIMockupController* class
    -In line **771** edit the existing line code : 
    ```JAVA
    String command = "C:\\Users\\wyounes\\Documents\\GEMOC\\GemocStudio";
    ``` 
    update the gemocStudio app path :
    ```JAVA
    String command = "Path of the GemocStudio application in your laptop";
    ``` 
### Executing OCE

To execute OCE run the main class *UIMockup* wich can be found in the package *UI*
