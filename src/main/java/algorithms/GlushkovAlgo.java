package algorithms;

import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import regexp.GlushkovSets;
import regexp.LinearisedSymbol;
import javafx.util.Pair;
import regexp.RegexpException;

import java.util.*;

public class GlushkovAlgo {
    private static String regular;
    private static GlushkovSets glushkovSets;
    private static HashMap<String, String> dictionary1 = new HashMap<>();
    private static HashMap<String, String> dictionary2 = new HashMap<>();

    public static Automaton doGlushkovAlgo (String regexp) throws RegexpException {
        GlushkovSets sets = GlushkovSetsBuild.makeGlushkovSets(regexp);

        regular = regexp;
        glushkovSets = sets;
        HashSet<String> alphabet = findAlphabet(regexp);
        return transformNKA2DKA(buildNKA(sets), alphabet);
    }

    public static HashBasedTable<String, String, ArrayList<String>> buildNKAFromReg (String regexp){
        GlushkovSets sets = new GlushkovSets(null, null,null);
        try {
            sets = GlushkovSetsBuild.makeGlushkovSets(regexp);
        } catch (RegexpException e) {
            e.printStackTrace();
        }
        return buildNKA(sets);
    }


    private static Automaton transformNKA2DKA
            (HashBasedTable<String, String, ArrayList<String>> nka, HashSet<String> alphabet) throws RegexpException {
        boolean hasStock = false;
        List<String> terminals = makeSetOfTerminal(glushkovSets, regular);
        HashSet<String> renamedTerminals = new HashSet<>();


        HashBasedTable<String, String, String> res = HashBasedTable.create();
        Deque<ArrayList<String>> pool = new ArrayDeque<>();
        HashSet<ArrayList<String>> visited = new HashSet<>();
        pool.push(new ArrayList<>(List.of("-1")));
        visited.add(new ArrayList<>(List.of("-1")));

        while (!pool.isEmpty()){
            ArrayList<String> current = pool.pop();
            ArrayList<String> vertexTo = new ArrayList<>();
            for (String letter: alphabet){
                for (String vertex: current){
                    try{
                        vertexTo.addAll(nka.get(vertex, letter));
                    }catch (Exception e){
                        if (!hasStock){
                            for (String ltr: alphabet) {
                                res.put("s", ltr, "s");
                            }
                            hasStock = true;
                        }
                    }
                }

                for (String finalVertex: terminals){
                    if (vertexTo.contains(finalVertex)){
                        Collections.sort(vertexTo);
                        renamedTerminals.add(getName(vertexTo));

                    }
                    if (current.contains(finalVertex)){
                        Collections.sort(current);
                        renamedTerminals.add(getName(current));
                    }
                }
                if (vertexTo.isEmpty()){
                    res.put(getName(current), letter, "s");
                }else {
                    Collections.sort(vertexTo);
                    res.put(getName(current), letter, getName(vertexTo));
                    if (!visited.contains(vertexTo)){
                        pool.push(new ArrayList<>(vertexTo));
                        visited.add(new ArrayList<>(vertexTo));
                    }
                }
                vertexTo.clear();
            }
        }

        HashBasedTable<String,String,String> prepared = renameVertexes(res, -2, false);
        HashBasedTable<String,String,String> jumpTable = renameVertexes(prepared, 0, true);
        List<String> preparedTerminals = new ArrayList<>();
        List<String> finalTerminals = new ArrayList<>();

        for (String old: renamedTerminals){
            preparedTerminals.add(dictionary1.get(old));
        }
        for (String old: preparedTerminals){
            finalTerminals.add(dictionary2.get(old));
        }
        String newStart = dictionary2.get(dictionary1.get("-1"));
        return new Automaton(false, jumpTable, newStart, finalTerminals);
    }

    public static HashBasedTable<String, String, String>
            renameVertexes(HashBasedTable<String, String, String> raw, int startName, boolean up){
        HashBasedTable<String, String, String> renamed = HashBasedTable.create();
        HashMap<String, String> dictionary = new HashMap<>();
        int newName = startName;
        for (String rowKey: raw.rowKeySet()){
            for (String columnKey: raw.columnKeySet()){
                renamed.put(Integer.toString(newName), columnKey, Objects.requireNonNull(raw.get(rowKey, columnKey)));
            }
            dictionary.put(rowKey, Integer.toString(newName));
            if (up){newName++;} else {newName--;}
        }

        if (startName == -2){
            dictionary1 = dictionary;
        }
        if (startName == 0){
            dictionary2 = dictionary;
        }

        for (String rowKey: renamed.rowKeySet()){
            for (String columnKey: renamed.columnKeySet()){
                String value = Objects.requireNonNull(renamed.get(rowKey, columnKey));
                if (dictionary.containsKey(value)){
                    renamed.put(rowKey, columnKey, dictionary.get(value));
                }
            }
        }
        return renamed;
    }

    private static String getName(ArrayList<String> rawName){
        StringBuilder res = new StringBuilder();
        for (String part: rawName){
            res.append(part);
            res.append(", ");
        }
        res.delete(res.length()-2, res.length());
        return res.toString();
    }

    private static HashSet<String> findAlphabet (String regexp){
        HashSet<String> alphabet = new HashSet<>();
        for (int i = 0; i < regexp.length(); i++){
            String letter = regexp.substring(i, i+1);
            if (letter.matches("[a-z0-1A-Z]")){
                alphabet.add(letter);
            }
        }
        return alphabet;
    }

    private static void putInTable (String from, String by, String to,
                                         HashBasedTable<String, String, ArrayList<String>> where){
        ArrayList<String> vertexesTo = where.get(from, by);
        if (vertexesTo == null) {
            vertexesTo = new ArrayList<>();
        }
        vertexesTo.add(to);

        where.put(from, by, vertexesTo);
    }

    private static HashBasedTable<String, String, ArrayList<String>> buildNKA (GlushkovSets sets){
        String firstVertex = "-1";
        HashBasedTable<String, String, ArrayList<String>> automaton = HashBasedTable.create();
        Deque<LinearisedSymbol> pool = new ArrayDeque<>();
        HashSet<LinearisedSymbol> visited = new HashSet<>();

        for (LinearisedSymbol symbol: sets.getSetOfStartSymbol()){
            putInTable(firstVertex, symbol.getSymbol().toString(), symbol.getNumber().toString(), automaton);
            pool.add(symbol);
        }

        while (!pool.isEmpty()){
            LinearisedSymbol current = pool.pop();
            if (visited.contains(current)){continue;}
            for (Pair<LinearisedSymbol, LinearisedSymbol> pair: sets.getSetOfPairs()){
                LinearisedSymbol first = pair.getKey();
                LinearisedSymbol second = pair.getValue();
                if (first.equals(current)){
                    putInTable(first.getNumber().toString(),
                            second.getSymbol().toString(),
                            second.getNumber().toString(), automaton);
                    if (!visited.contains(second) && !second.equals(first)) {
                        pool.push(second);
                    }
                }
            }
            visited.add(current);
        }
        return automaton;
    }

    private static List<String> makeSetOfTerminal(GlushkovSets sets, String regexp) throws RegexpException {
        List<String> terminalVertexes = new ArrayList<>();
        for (LinearisedSymbol terminal: sets.getSetOfEndSymbol()){
            terminalVertexes.add(terminal.getNumber().toString());
        }
        if (checkFirst(regexp)){
            terminalVertexes.add("-1");
        }
        return terminalVertexes;
    }

    private static boolean checkFirst(String regexp) throws RegexpException {
        return RegExprBuild.allowEmptyWord(regexp);
    }

    private GlushkovAlgo(){}
}
