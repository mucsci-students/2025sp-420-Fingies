package org.fingies;

import org.fingies.Controller.Action;
import org.fingies.Controller.Command;
import org.fingies.Model.UMLClass;
import org.fingies.Model.UMLClassHandler;
import org.jline.reader.*;

import java.lang.reflect.Type;
import java.util.*;

public class TabCompletion {

    private static final Map<String, Set<String>> commandSubcommands = new HashMap<>();
    private static final Set<String> shorthandCommands = new HashSet<>();


    static {
        // Build commandSubcommands from full commands
        for (String command : Command.COMMANDS) {
            String[] parts = command.split(" ", 2);
            String verb = parts[0];
            String rest = parts.length > 1 ? parts[1] : "";

            commandSubcommands
                .computeIfAbsent(verb, k -> new HashSet<>());

            if (!rest.isEmpty()) {
                commandSubcommands.get(verb).add(rest);
            }
        }

        // Add all shorthand commands
        shorthandCommands.addAll(Arrays.asList(Command.COMMANDS_SHORTHAND));
    }

    public Action getActionOutOfWords(List<String> words) {
        int maxLength = Math.min(3, words.size());

        for (int i = 1; i <= maxLength; ++i) {
            String command = words.get(0);
            for (int j = 1; j < i; ++j) {
                command += " " + words.get(j);
            }
            
            Action a = Command.getPerfectActionOutOfString(command);
            if (a != null) {
                return a;
            }
        }
        return null;
    }

    public Completer getCompleter() {
        return new Completer() {
            @Override
            public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
                List<String> words = line.words();
                int wordIndex = line.wordIndex();

                if (words.isEmpty()) return;

                if (wordIndex == 0) {
                    // Suggest full command verbs
                    for (String verb : commandSubcommands.keySet()) {
                        if (verb.startsWith(line.word())) {
                            candidates.add(new Candidate(verb));
                        }
                    }
                    // Suggest shorthand commands
                    for (String shorthand : shorthandCommands) {
                        if (shorthand.startsWith(line.word())) {
                            candidates.add(new Candidate(shorthand));
                        }
                    }
                } else if (wordIndex == 1) {
                    String firstWord = words.get(0);
                    String current = line.word();

                    Set<String> subcommands = commandSubcommands.get(firstWord);
                    if (subcommands != null) {
                        for (String sub : subcommands) {
                            if (sub.startsWith(current)) {
                                candidates.add(new Candidate(sub));
                            }
                        }
                    }
                }
                Action action = getActionOutOfWords(words);
                if (action == null) {
                    return;
                }
                else if (action != null) {
                    switch (action) {
                        case REMOVE_CLASS:
                            if (wordIndex == 2) {
                                for (UMLClass validClass : UMLClassHandler.getAllClasses()) {
                                    candidates.add(new Candidate(validClass.getName()));
                                }
                            }
                            break;
                        case RENAME_CLASS:
                            if (wordIndex == 2) {
                                for (UMLClass validClass : UMLClassHandler.getAllClasses()) {
                                    candidates.add(new Candidate(validClass.getName()));
                                }
                            }
                            break;
                        case ADD_RELATIONSHIP:
                            if (wordIndex == 2 || wordIndex == 3) {
                                for (UMLClass validClass : UMLClassHandler.getAllClasses()) {
                                    candidates.add(new Candidate(validClass.getName()));
                                }
                            }
                            break;
                    }
                }
                // else if (a != null) {
                //     case(a == add relationship) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         // GET RELATIONSHIP TYPE
                //     }
                //     case(a == remove relationship) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //     }
                //     case(a == save) {
                //         //cannot complete save
                //     }
                //     case(a == load) {
                //         //cannot complete load
                //     }
                //     case(a == list class) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //     }
                //     case(a == help) {
                //         //cannot complete help
                //     }



                //     ADD METHOD
                //     REMOVE METHOD
                //     RENAME METHOD



                //     case(a == add field) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             cannot complete new field name
                //         }
                //         if (wordIndex == 4) {
                //             // GET LIST OF TYPES
                //         }
                //     }
                //     case(a == remove field) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getClass(line.get(2).getallfields()));
                //         }
                //     }
                //     case(a == rename field) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getClass(line.get(2).getallfields()));
                //         }
                //         if (wordIndex == 4) {
                //            // cannot complete new field name
                //         }
                //     }



                //     ADD PARAMETER
                //     REMOVE PARAMETER
                //     RENAME PARATMETER



                //     case(a == change relationship type) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         // GET RELATIONSHIP TYPE
                //     } 


                //     CHANGE PARAMETER TYPE



                //     case(a == change field type) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 3) {
                //             candidates.add(UMLClassHandler.getClass(line.get(2).getallfields()));
                //         }
                //         if (wordIndex == 4) {
                //            // GET FIELD TYPE
                //         }
                //     }



                //     CHANGE METHOD TYPE



                //     case(a == move) {
                //         if (wordIndex == 1) {
                //             candidates.add(UMLClassHandler.getAllClasses());
                //         }
                //         if (wordIndex == 2) {
                //             cannot complete new x
                //         }
                //         if (wordIndex == 3) {
                //             cannot complete new y
                //         }
                //     } 
                // }
            }
        };
    }
}