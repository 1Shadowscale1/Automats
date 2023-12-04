package algorithms;

import automaton.Automaton;
import automaton.IsomorphismResult;
import com.google.common.collect.Lists;

import java.util.*;

public class Isomorphism {
    private Isomorphism() {}

    static HashMap<String, String> associations = new HashMap<>();
    static String startOfCycle;

    public static void clear(){
        associations.clear();
        startOfCycle = null;
    }

    public static IsomorphismResult automatsAreIsomorphic(Automaton aut1, Automaton aut2) throws CloneNotSupportedException {
        aut1 = Adduction.buildAdductedAutomat(aut1);
        aut2 = Adduction.buildAdductedAutomat(aut2);
        clear();
        var wordIn2ThatNotIn1 = Lists.reverse(
                findWordIn1ThatNotIn2(aut2, aut1, aut2.startVertex, aut1.startVertex, ""));
        clear();
        var wordIn1ThatNotIn2 = Lists.reverse(
                findWordIn1ThatNotIn2(aut1, aut2, aut1.startVertex, aut2.startVertex, ""));

        return new IsomorphismResult(
                (wordIn1ThatNotIn2.size() != 0 ? String.join("", wordIn1ThatNotIn2) : null),
                (wordIn2ThatNotIn1.size() != 0 ? String.join("", wordIn2ThatNotIn1) : null),
                (wordIn1ThatNotIn2.size() == 0 && wordIn2ThatNotIn1.size() == 0 ? associations : null));
    }

    public static List<String> getEndOfWord(String currentVertex, String prevLetter, Automaton aut){
        return getEndOfWord(currentVertex, prevLetter, aut, new HashSet<>());
    }

    public static List<String> getEndOfWord(String currentVertex, String prevLetter,
                                            Automaton aut, HashSet<String> visited){
        visited.add(currentVertex);
        if (aut.finalVertices.contains(currentVertex)){
            var result = new ArrayList<String>();
            result.add(prevLetter);
            return result;
        }
        for (var letter : aut.letters) {
            var q = aut.getJumpByVertexAndLetter(currentVertex, letter);
            if (visited.contains(q) || aut.isVertexStock(q)){
                continue;
            }
            visited.add(q);
            var result = getEndOfWord(q, letter, aut, visited);
            if (result != null){
                result.add(prevLetter);
                return result;
            }
        }
        return null;
    }

    public static List<String> findWordIn1ThatNotIn2(
            Automaton aut1, Automaton aut2, String u, String v, String prevLetter){
        var wordWithCycle = findWordIn1ThatNotIn2(aut1, aut2, u, v, prevLetter, new HashMap<>());
        if (wordWithCycle == null)
            return new ArrayList<>();
        var currentWord = new ArrayList<>(wordWithCycle.end);
        currentWord.addAll(wordWithCycle.cycle);
        currentWord.addAll(wordWithCycle.start);

        var cyclesCount = 1;
        while (automatonTakeWord(aut2, currentWord)){
            if (cyclesCount > 20)
                return new ArrayList<>();
            cyclesCount++;
            currentWord = new ArrayList<>(wordWithCycle.end);
            for (var i = 0; i < cyclesCount; i++)
                currentWord.addAll(wordWithCycle.cycle);
            currentWord.addAll(wordWithCycle.start);
        }
        return currentWord;
    }

    private static Boolean automatonTakeWord(Automaton aut, List<String> word){
        var currentVertex = aut.startVertex;
        for (var i = word.size()-1; i >= 0; i--){
            var letter = word.get(i);
            if (!aut.letters.contains(letter))
                return false;
            currentVertex = aut.getJumpByVertexAndLetter(currentVertex, letter);
        }
        return aut.finalVertices.contains(currentVertex);
    }

    public static WordWithCycle findWordIn1ThatNotIn2(
            Automaton aut1, Automaton aut2, String u, String v, String prevLetter, HashMap<String, Boolean> prevVisited){
        HashMap<String, Boolean> visited = new HashMap<>();
        if (prevVisited.clone() instanceof HashMap<?, ?>) {
            visited = (HashMap<String, Boolean>)prevVisited;
        }

        visited.put(u, true);

        if (aut1.finalVertices.contains(u) != aut2.finalVertices.contains(v)){
            if (aut1.finalVertices.contains(u)){
                var result = new ArrayList<String>();
                result.add(prevLetter);
                return new WordWithCycle(result);
            }
        }
        if (!associations.containsKey(u))
            associations.put(u, v);

        for (var letter : aut1.letters) {
            var q1 = aut1.getJumpByVertexAndLetter(u, letter);
            if (!aut2.letters.contains(letter) && !aut1.isVertexStock(q1)) {
                var result = getEndOfWord(q1, letter, aut1);
                result.add(prevLetter); // null не вылетит если q1 не сток, а мы это проверили
                return new WordWithCycle(result);
            }
            else if (!aut2.letters.contains(letter)){
                continue;
            }
            var q2 = aut2.getJumpByVertexAndLetter(v, letter);
            if (!aut1.isVertexStock(q1)){
                if (aut2.isVertexStock(q2)){
                    var result = getEndOfWord(q1, letter, aut1);
                    result.add(prevLetter); // null не вылетит если q1 не сток, а мы это проверили
                    return new WordWithCycle(result);
                }
                if (visited.containsKey(q1)){
                    if (!Objects.equals(q2, associations.get(q1))){
                        startOfCycle = q1;
                        var end = getEndOfWord(q1, letter, aut1);
                        end = end.subList(0, end.size()-1);
                        if (prevLetter.isEmpty()){
                            var cycle = new ArrayList<String>();
                            cycle.add(letter);
                            return new WordWithCycle(new ArrayList<>(), cycle, end);
                        }
                        else{
                            var start = new ArrayList<String>();
                            start.add(letter);
                            start.add(prevLetter);
                            return new WordWithCycle(start, new ArrayList<>(), end);
                        }
                    }
                }
                else{
                    var word = findWordIn1ThatNotIn2(aut1, aut2, q1, q2, letter, visited);
                    if (word != null){
                        if (Objects.equals(q1, startOfCycle)){
                            word.cycle = buildCycle(word.start, aut1, q1);

                            word.start = new ArrayList<>();
                            word.start.add(letter);
                            if (!prevLetter.isEmpty())
                                word.start.add(prevLetter);

                            return word;
                        }
                        else if (prevLetter.isEmpty()){
                            if (Objects.equals(u, startOfCycle)){
                                word.start.add(prevLetter);
                                word.cycle = buildCycle(word.start, aut1, u);

                                word.start = new ArrayList<>();

                                return word;
                            }
                        }
                        if (!prevLetter.isEmpty())
                            word.start.add(prevLetter);
                        return word;
                    }
                }
            }
            else{
                if (!associations.containsKey(q1))
                    associations.put(q1, q2);
            }
        }
        return null;
    }

    private static List<String> buildCycle(List<String> result, Automaton aut, String startOfCycle){
        var cycle = new ArrayList<String>();
        if (result.size() == 1){
            cycle.add(result.get(0));
        }
        else{
            var i = result.size()-2;
            var currentState = aut.getJumpByVertexAndLetter(startOfCycle, result.get(i));
            cycle.add(result.get(i));
            while (!Objects.equals(currentState, startOfCycle)){
                i--;
                var l = result.get(i);
                currentState = aut.getJumpByVertexAndLetter(currentState, l);
                cycle.add(l);
            }
        }
        Collections.reverse(cycle);
        return cycle;
    }
}
