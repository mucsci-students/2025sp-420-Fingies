package org.fingies;

import org.jline.reader.*;
import java.util.List;

public class TabCompletion {

    public Completer getCompleter() {
        return (reader, line, candidates) -> {
            String buffer = line.line();
            String[] words = buffer.trim().split("\\s+");

            int wordCount = words.length;
            boolean endsWithSpace = buffer.endsWith(" ");

            if (wordCount == 0 || (wordCount == 1 && !endsWithSpace)) {
                // Still typing the first word, offer command completions
                String current = buffer.trim();
                for (String command : Command.COMMANDS) {
                    if (command.startsWith(current) && !command.equals(current)) {
                        candidates.add(new Candidate(command));
                    }
                }
                for (String shorthand : Command.COMMANDS_SHORTHAND) {
                    if (shorthand.startsWith(current) && !shorthand.equals(current)) {
                        candidates.add(new Candidate(shorthand));
                    }
                }
            } else if (wordCount == 1 && endsWithSpace) {
                // Completed first word with a space, now suggesting second word completions
                String first = words[0];

                // Find the command index using the first word
                int commandIndex = Command.indexOfCommand(first);

                if (commandIndex != -1) {
                    // Get the expected arguments for the found command
                    String commandArgs = Command.COMMAND_ARGS[commandIndex];
                    String[] possibleArgs = commandArgs.split("\\s+");

                    // Suggest possible arguments for the second word (after the space)
                    String current = ""; // No second word input yet

                    // Suggesting arguments that start with the current user input
                    for (String arg : possibleArgs) {
                        if (arg.startsWith(current) && !arg.equals(current)) {
                            candidates.add(new Candidate(arg));
                        }
                    }
                }
            }
        };
    }
}