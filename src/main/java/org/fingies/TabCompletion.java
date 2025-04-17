package org.fingies;

import org.fingies.Controller.Command;
import org.fingies.Model.UMLClassHandler;
import org.jline.reader.*;

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

    public Completer getCompleter() {
        return new Completer() {
            @Override
            public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
                List<String> words = line.words();
                int wordIndex = line.wordIndex();
                //Action a = Command.LINCOLNSPARSER(words);

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
                // else if (a != null) {
                //     swtich statement:
                //     case(a == ADD_CLASS) {
                //         cannot complete new name
                //     }
                //     case(a == remove class) {
                //         if (wordIndex == 2) {
                //             candidates.add(UMLClassHandler.getAllClasses());
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
                // }
            }
        };
    }
}