package org.fingies;

import org.jline.reader.Completer;
import org.jline.reader.impl.completer.TreeCompleter;
import java.util.*;

public class TabCompletion {
    public static Completer getCompleter() {
        Map<String, List<String>> commandTree = new HashMap<>();

        // Build command hierarchy
        for (String command : Command.COMMANDS) {
            String[] parts = command.split(" ");
            if (parts.length > 1) {
                commandTree.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
            } else {
                commandTree.put(parts[0], Collections.emptyList());
            }
        }

        List<TreeCompleter.Node> nodes = new ArrayList<>();

        // Construct TreeCompleter nodes
        for (Map.Entry<String, List<String>> entry : commandTree.entrySet()) {
            String rootCommand = entry.getKey();
            List<String> subCommands = entry.getValue();
            
            if (subCommands.isEmpty()) {
                nodes.add(TreeCompleter.node(rootCommand));
            } else {
                List<TreeCompleter.Node> subNodes = new ArrayList<>();
                for (String subCommand : subCommands) {
                    subNodes.add(TreeCompleter.node(subCommand));
                }
                nodes.add(TreeCompleter.node(rootCommand, subNodes.toArray(new TreeCompleter.Node[0])));
            }
        }

        // Add shorthand commands as standalone nodes
        for (String shorthand : Command.COMMANDS_SHORTHAND) {
            nodes.add(TreeCompleter.node(shorthand));
        }

        return new TreeCompleter(nodes.toArray(new TreeCompleter.Node[0]));
    }
}