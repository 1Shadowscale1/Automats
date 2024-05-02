package algorithms;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashBasedTable;

import automaton.Automaton;
import automaton.NFAutomaton;

public class Closures {
    public static Automaton prefixClosure(Automaton automaton) throws CloneNotSupportedException {
        Automaton clonedAutomaton = automaton.clone();

        List<String> finalVertices = new ArrayList<>();
        for (String vertex : clonedAutomaton.vertices) {
            if (!clonedAutomaton.isVertexStock(vertex))
                finalVertices.add(vertex);
        }

        clonedAutomaton.finalVertices = finalVertices;

        return clonedAutomaton;
    }

    public static NFAutomaton suffixClosure(Automaton automaton) throws CloneNotSupportedException {
        HashBasedTable<String, String, List<String>> nfaJumpTable = HashBasedTable.create();
        List<String> startVertices = new ArrayList<>(automaton.vertices);
        List<String> finalVertices = new ArrayList<>();

        for (String vertex : automaton.vertices) {
            if (!automaton.isVertexStock(vertex))
                finalVertices.add(vertex);
        }

        for (String vertex : automaton.vertices) {
            HashBasedTable<String, String, List<String>> vertexRow = HashBasedTable.create();

            for (String letter : automaton.letters)
                vertexRow.put(vertex, letter,
                        new ArrayList<>(List.of(automaton.getJumpByVertexAndLetter(vertex, letter))));

            nfaJumpTable.putAll(vertexRow);
        }
        NFAutomaton nfAutomaton = new NFAutomaton(false, nfaJumpTable, startVertices, finalVertices);

        return nfAutomaton;
    }

    public static NFAutomaton subwordClosure(Automaton automaton) throws CloneNotSupportedException {
        HashBasedTable<String, String, List<String>> nfaJumpTable = HashBasedTable.create();
        List<String> startVertices = new ArrayList<>(automaton.vertices);
        List<String> finalVertices = new ArrayList<>();

        for (String vertex : automaton.vertices) {
            if (!automaton.isVertexStock(vertex))
                finalVertices.add(vertex);
        }

        for (String vertex : automaton.vertices) {
            HashBasedTable<String, String, List<String>> vertexRow = HashBasedTable.create();

            for (String letter : automaton.letters)
                vertexRow.put(vertex, letter,
                        new ArrayList<>(List.of(automaton.getJumpByVertexAndLetter(vertex, letter))));

            nfaJumpTable.putAll(vertexRow);
        }
        NFAutomaton nfAutomaton = new NFAutomaton(false, nfaJumpTable, startVertices, finalVertices);

        return nfAutomaton;
    }
}
