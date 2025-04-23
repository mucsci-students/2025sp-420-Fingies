# 2025sp-420-Finges

# UML Editor
A Java application for creating class diagrams.

## How to run
### Dependencies:
Ensure that you have the following dependencies installed:
- Java (https://www.oracle.com/java/technologies/downloads/#java17)
  - Install the latest version for your operating system.
- Maven (https://maven.apache.org/download.cgi)
  - Install the latest version for your operating system.

### Compile:
Clone the project and use ``java`` to run ``Main``.
1. Call ``git clone https://github.com/mucsci-students/2025sp-420-Fingies.git``.
2. Navigate to the ``2025sp-420-Fingies/`` directory.
3. Call ``mvn package`` to compile the program. This will build the package, test the program, and start it.

### Run:
- Run in CLI mode: ``java -jar target/fingies-1.0-SNAPSHOT.jar --cli``.
- Run in GUI mode: ``java -jar target/fingies-1.0-SNAPSHOT.jar``.
- Run tests: ``mvn test``.

### How to use the CLI:
1. Type a command with a list of space separated aguments and press ENTER.

Type ``help`` for a list of commands and their arguments, or ``help command_name`` for help using a specific command.
 
### How to use the GUI:
1. Select a command you want to execute from the menu bar.
2. After filling the textboxes with arguments, press ENTER to execute the command.
3. If you want to cancel the current command, press ESC while in one of the text boxes.

### Design Patterns:
1. Adapter: we used an adapter in our JModel to convert relationships having their sources and destinations as UMLClasses to instead just be the names of the classes as Strings. This insured the propper JSON format was followed using our GSON dependency. Unlike a traditional singleton, they are not instantiated, so their fields are accessed through static methods instead.
2. Singleton: we used a singleton for both our UMLClassHandler and RelationshipHandler classes. These classes have one main static list of all of the classes/relationships that currently exist and can be accessed by any other class that needs them.
3. Decorator: we used a decorator in our WrappingComboBoxRenderer that served as a wrapper class for the JComboBoxes in the GUIView. This wrapper class was able to properly format the JComboBox to meet the needs of the GUI.
4. Memento: we used a memento for storing the previous states of the UML Diagram using our Change class. The change class stores the previous state and the current state of the program for when either the Undo or Redo actions are taken.
5. Observer: we used an observer for updating the GUIView after a command is executed in the controller. The GUIView has three different update methods: update arrows, update classes, and update attributes. Each of these are called depending on the command that refresh the data in the UMLClasses stored in the GUIView.
6. Mediator: we used a mediator for our Controller in our MVC. The controller deals with user input sent from the different views and determines what actions to take. It serves as the central point of communication between the Model and the View by coordinating interactions between them.

### Code Coverage:
A comprehensive code coverage report can be found in the directory coverage_report.
 - Open index.html in the directory for a web view
 - Jacoco.csv provides a comma-seperated value version of the report

## Team Fingies
- Nick Hayes
- William Wickenheiser
- Tristan Rush
- Kevin Dichter
- Lincoln Craddock
- Tim King
