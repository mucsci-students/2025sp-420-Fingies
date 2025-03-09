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

### Run:
Clone the project and use ``java`` to run ``Main``.
1. Call ``git clone https://github.com/mucsci-students/2025sp-420-Fingies.git``.
2. Navigate to the ``2025sp-420-Fingies/src/`` directory.
3. Call ``mvn package`` to compile all of the files in ``src/``. This will build the package, test the program, and start it.
4. Call ``mvn test`` to only run tests.
 

## Known issues:
- Invalid/altered JSON files aren't detected immediately by the program.
- New files can be saved to invalid filepaths & replace already existing ones.
- Entering a command with too many arguments (>200,000) can stall the program endlessly.

## Team Fingies
- Nick Hayes
- William Wickenheiser
- Tristan Rush
- Kevin Dichter
- Lincoln Craddock
