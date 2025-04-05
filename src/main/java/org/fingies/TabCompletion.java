package org.fingies;

import org.jline.reader.*;
import java.util.List;

public class TabCompletion {

    public Completer getCompleter() {
        return new Completer() {
            @Override
            public void complete(LineReader lineReader, ParsedLine line, List<Candidate> candidates) {
                String buffer = line.line(); // Get the current input line
                // Add command completions (full commands and shorthand)
                for (String command : CLIView.COMMANDS) {
                    if (command.startsWith(buffer)) {
                        candidates.add(new Candidate(command));
                    }
                }
                for (String shorthand : CLIView.COMMANDS_SHORTHAND) {
                    if (shorthand.startsWith(buffer)) {
                        candidates.add(new Candidate(shorthand));
                    }
                }
            }
        };
    }
}