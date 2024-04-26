package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.HashBasedTable;

import automaton.Automaton;
import automaton.NFAutomaton;

public class Reverse {
    public static NFAutomaton reverseAutomaton(Automaton automaton) {
        HashBasedTable<String, String, List<String>> nfaJumpTable = HashBasedTable.create();
        List<String> startVertices = new ArrayList<>(automaton.finalVertices);
        List<String> finalVertices = new ArrayList<>(List.of(automaton.startVertex));

        Map<String, Map<String, Set<String>>> nfaTransitions = new HashMap<>();
        for (String vertex : automaton.vertices) {
            for (String letter : automaton.letters) {
                String nextVertex = automaton.getJumpByVertexAndLetter(vertex, letter);

                if (!nfaTransitions.containsKey(nextVertex))
                    nfaTransitions.put(nextVertex, new HashMap<>(Map.of(letter, new HashSet<>(Set.of(vertex)))));
                else {
                    if (!nfaTransitions.get(nextVertex).containsKey(letter))
                        nfaTransitions.get(nextVertex).put(letter, new HashSet<>(Set.of(vertex)));
                    else
                        nfaTransitions.get(nextVertex).get(letter).add(vertex);
                }
            }
        }

        for (Map.Entry<String, Map<String, Set<String>>> vertexEntry : nfaTransitions.entrySet()) {
            HashBasedTable<String, String, List<String>> vertexRow = HashBasedTable.create();
            String vertex = vertexEntry.getKey();

            for (Map.Entry<String, Set<String>> letterEntry : vertexEntry.getValue().entrySet())
                vertexRow.put(vertex, letterEntry.getKey(), new ArrayList<>(new HashSet<>(letterEntry.getValue())));

            nfaJumpTable.putAll(vertexRow);
        }
        NFAutomaton nfAutomaton = new NFAutomaton(false, nfaJumpTable, startVertices, finalVertices);

        return nfAutomaton;
    }
}
