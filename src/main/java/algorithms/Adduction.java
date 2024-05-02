package algorithms;

import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Класс алгоритмов приведения ДКА с методами исключение недостижимых вершин,
 * разбиение на классы эквивалентных состояний, построение приведенного автомата.
 */
public class Adduction {
    private Adduction() {}

    public static Automaton excludeUnreachableVertexes(Automaton automaton) throws CloneNotSupportedException {
        Automaton clone = automaton.clone();
        Set<String> reachedVertexes = Sets.newHashSet();
        reachedVertexes.add(clone.startVertex);
        boolean running = true;

        while(running) {
            Set<String> iteratedReachedVertexes = Sets.newHashSet();
            reachedVertexes.forEach(x -> iteratedReachedVertexes.addAll(clone.getAllJumpsByVertex(x)));
            iteratedReachedVertexes.addAll(reachedVertexes);
            if (iteratedReachedVertexes.equals(reachedVertexes)) running = false;
            reachedVertexes = iteratedReachedVertexes;
        }

        Set<String> notReachedVertexes = Sets.difference(new HashSet<>(clone.vertices), reachedVertexes);
        notReachedVertexes.forEach(clone::removeVertex);
        return clone;
    }

    public static List<List<String>> buildMaxCongruence(Automaton automaton) {
        List<List<String>> eqClasses = Lists.newArrayList();
        eqClasses.add(automaton.finalVertices);
        eqClasses.add(Lists.newArrayList(Sets.difference(new HashSet<>(automaton.vertices), new HashSet<>(automaton.finalVertices))));
        int notSplitCounter = 0;
        do {
            List<String> pickedClass = eqClasses.get(0);
            List<List<String>> dividedClass = Lists.newArrayList();
            List<String> hashes = Lists.newArrayList();

            for (String vertex : pickedClass) {
                StringBuilder hash = new StringBuilder();
                for (String letter : automaton.letters)
                    for (int i = 0; i < eqClasses.size(); i++)
                        if ((eqClasses.get(i)).contains(automaton.getJumpByVertexAndLetter(vertex, letter))) hash.append(i);

                hashes.add(hash.toString());
            }

            List<String> uniqueHashes = Lists.newArrayList();
            for (int i = 0; i < hashes.size(); i++) {
                String hash = hashes.get(i);
                if (!uniqueHashes.contains(hash)) {
                    uniqueHashes.add(hash);
                    dividedClass.add(Lists.newArrayList(pickedClass.get(i)));
                }
                else dividedClass.get(uniqueHashes.indexOf(hash)).add(pickedClass.get(i));
            }

            if (dividedClass.size() == 1) {
                eqClasses.add(eqClasses.remove(0));
                notSplitCounter++;
            } else {
                eqClasses.remove(0);
                eqClasses.addAll(dividedClass);
                notSplitCounter = 0;
            }
        } while(notSplitCounter != eqClasses.size());

        return eqClasses;
    }

    public static Automaton buildAdductedAutomat(Automaton automaton) throws CloneNotSupportedException {
        Automaton clone = automaton.clone();
        Automaton modifiedAutomaton = excludeUnreachableVertexes(clone);
        List<List<String>> maxCongruence = buildMaxCongruence(modifiedAutomaton);

        List<String> maxCongruenceJoined = Lists.newArrayList();
        maxCongruence.forEach(x -> maxCongruenceJoined.add(String.join(", ", x)));

        HashBasedTable<String, String, String> newJumpTable = HashBasedTable.create();
        Set<String> newFinalVertexes = Sets.newHashSet();

        for (int i = 0; i < maxCongruence.size(); i++) {
            List<String> eqClass = maxCongruence.get(i);
            if (eqClass.contains(clone.startVertex)) clone.startVertex = maxCongruenceJoined.get(i);
            for (String finalVertex : clone.finalVertices) {
                if (eqClass.contains(finalVertex)) newFinalVertexes.add(maxCongruenceJoined.get(i));
            }
            for (String letter : clone.letters) {
                int index = 0;
                String jump = clone.getJumpByVertexAndLetter(eqClass.get(0), letter);
                for (int j = 0; j < maxCongruence.size(); j++) {
                    List<String> eq = maxCongruence.get(j);
                    if (eq.contains(jump)) index = j;
                }
                newJumpTable.put(String.join(", ", eqClass), letter, maxCongruenceJoined.get(index));
            }
        }

        clone.isFinalized = true;
        clone.jumpTable = newJumpTable;
        clone.vertices = Lists.newArrayList(newJumpTable.rowKeySet());
        clone.finalVertices = Lists.newArrayList(newFinalVertexes);

        return clone;
    }

    public static Automaton buildPrettyAdductedAutomat(Automaton automaton) throws CloneNotSupportedException {
        return buildAdductedAutomat(prettifyAdductedAutomatOutput(automaton));
    }

    private static Automaton prettifyAdductedAutomatOutput(Automaton automaton) {
        String startVertex = "{" + automaton.startVertex + "}";

        List<String> finalVertices = new ArrayList<>();
        for (String finalVertex : automaton.finalVertices)
            finalVertices.add("{" + finalVertex + "}");
        
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        for (String vertex : automaton.vertices) {
            HashBasedTable<String, String, String> vertexRow = HashBasedTable.create();

            for (String letter : automaton.letters) {
                String currentVertex = automaton.getJumpByVertexAndLetter(vertex, letter);

                vertexRow.put("{" + vertex + "}", letter, "{" + currentVertex + "}");
            }

            jumpTable.putAll(vertexRow);
        }
        
        return new Automaton(false, jumpTable, startVertex, finalVertices);
    }
}
