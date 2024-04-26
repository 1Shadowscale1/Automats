package automaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;

public class NFAutomaton extends Automaton {
    public HashBasedTable<String, String, List<String>> jumpTable;
    public List<String> startVertices;

    public NFAutomaton(Boolean isFinalized, HashBasedTable<String, String, List<String>> jumpTable,
            List<String> startVertices, List<String> finalVertices) {
        super(isFinalized, null, finalVertices);

        this.jumpTable = jumpTable;
        this.vertices = Lists.newArrayList(jumpTable.rowKeySet());
        this.letters = Lists.newArrayList(jumpTable.columnKeySet());
        this.startVertices = startVertices;
    }

    public Automaton transformNFA2DFA() {
        Set<Set<String>> dfaVertices = new HashSet<>();
        Map<Set<String>, Map<String, Set<String>>> dfaTransitions = new HashMap<>();
        Set<String> dfaStartVertex = epsilonClosure(new HashSet<>(startVertices));
        List<String> dfaFinalVertices = new ArrayList<>();

        Queue<Set<String>> queue = new LinkedList<>();
        queue.add(dfaStartVertex);
        dfaVertices.add(dfaStartVertex);

        while (!queue.isEmpty()) {
            Set<String> currentVertex = queue.poll();

            for (String letter : letters) {
                Set<String> nextVertex = new HashSet<>();

                for (String vertex : currentVertex) {
                    List<String> transitions = jumpTable.get(vertex, letter);
                    if (transitions != null)
                        nextVertex.addAll(transitions);
                }

                Set<String> nextVertexClosure = epsilonClosure(nextVertex);
                if (!nextVertexClosure.isEmpty() && !dfaVertices.contains(nextVertexClosure)) {
                    dfaVertices.add(nextVertexClosure);
                    queue.add(nextVertexClosure);
                }

                if (!dfaTransitions.containsKey(currentVertex))
                    dfaTransitions.put(currentVertex, new HashMap<>());
                dfaTransitions.get(currentVertex).put(letter, nextVertexClosure);
            }
        }

        HashBasedTable<String, String, String> dfaJumpTable = HashBasedTable.create();
        for (Map.Entry<Set<String>, Map<String, Set<String>>> vertexEntry : dfaTransitions.entrySet()) {
            HashBasedTable<String, String, String> vertexRow = HashBasedTable.create();
            String vertex = getVertexName(vertexEntry.getKey());

            for (Map.Entry<String, Set<String>> letterEntry : vertexEntry.getValue().entrySet())
                vertexRow.put(vertex, letterEntry.getKey(), getVertexName(letterEntry.getValue()));

            dfaJumpTable.putAll(vertexRow);
        }

        for (Set<String> vertex : dfaVertices) {
            for (String finalVertex : finalVertices) {
                if (vertex.contains(finalVertex)) {
                    dfaFinalVertices.add(getVertexName(vertex));
                    break;
                }
            }
        }

        return new Automaton(false, dfaJumpTable, getVertexName(dfaStartVertex), dfaFinalVertices);
    }

    @Override
    public NFAutomaton clone() throws CloneNotSupportedException {
        NFAutomaton nfAutomaton = (NFAutomaton) super.clone();

        nfAutomaton.jumpTable = HashBasedTable.create(jumpTable);
        nfAutomaton.vertices = new ArrayList<>(vertices);
        nfAutomaton.letters = new ArrayList<>(letters);
        nfAutomaton.startVertices = new ArrayList<>(startVertices);
        nfAutomaton.finalVertices = new ArrayList<>(finalVertices);

        return nfAutomaton;
    }

    private Set<String> epsilonClosure(Set<String> vertices) {
        Set<String> epsilonClosure = new HashSet<>(vertices);
        Queue<String> queue = new LinkedList<>(vertices);

        while (!queue.isEmpty()) {
            String currentVertex = queue.poll();
            List<String> epsilonTransitions = jumpTable.get(currentVertex, "");

            if (epsilonTransitions != null) {
                for (String nextVertex : epsilonTransitions) {
                    if (!epsilonClosure.contains(nextVertex)) {
                        epsilonClosure.add(nextVertex);
                        queue.add(nextVertex);
                    }
                }
            }
        }

        return epsilonClosure;
    }

    private String getVertexName(Set<String> vertex) {
        List<String> vertexName = new ArrayList<>(vertex);
        Collections.sort(vertexName);

        return String.join(", ", vertexName);
    }
}
