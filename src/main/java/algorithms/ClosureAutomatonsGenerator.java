package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.HashBasedTable;

import automaton.Automaton;
import automaton.NFAutomaton;

public class ClosureAutomatonsGenerator {
    public int automatonsNumber;
    public int verticesNumber;
    public int lettersNumber;
    public List<Automaton> generatedAutsList;
    public List<Automaton> prefixAutsList;
    public List<NFAutomaton> suffixNfAutsList;
    public List<NFAutomaton> subwordNfAutsList;
    public List<Automaton> suffixAutsList;
    public List<Automaton> subwordAutsList;

    public ClosureAutomatonsGenerator(int automatonsNumber, int verticesNumber, int lettersNumber) {
        this.automatonsNumber = automatonsNumber;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
        this.generatedAutsList = new ArrayList<>();
        this.prefixAutsList = new ArrayList<>();
        this.suffixNfAutsList = new ArrayList<>();
        this.subwordNfAutsList = new ArrayList<>();
        this.suffixAutsList = new ArrayList<>();
        this.subwordAutsList = new ArrayList<>();
    }

    public void generateClosureAutomatons() {
        List<String> letters = new ArrayList<>(List.of("a", "b", "c", "d"));
        Random randomInt = new Random(System.currentTimeMillis());

        int counter = 0;
        while (counter != automatonsNumber) {
            HashBasedTable<String, String, String> currentTable = HashBasedTable.create();

            for (int i = 1; i <= verticesNumber; i++)
                for (int j = 0; j < lettersNumber; j++)
                    currentTable.put(String.valueOf(i), letters.get(j),
                            String.valueOf(1 + randomInt.nextInt(verticesNumber)));

            String startVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));

            List<String> finalVertices = new ArrayList<>();
            int finalVerticesCount = 1 + randomInt.nextInt(verticesNumber);
            Set<String> alreadySeen = new HashSet<>();
            for (int i = 0; i < finalVerticesCount; i++) {
                String finalVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));

                while (alreadySeen.contains(finalVertex)) {
                    finalVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));
                }

                alreadySeen.add(finalVertex);
                finalVertices.add(finalVertex);
            }

            Automaton generatedAut = new Automaton(false, currentTable, startVertex, finalVertices);

            if (generatedAut.isAutomatonFullAndWithoutStockVertices()) {
                generatedAutsList.add(generatedAut);

                try {
                    Automaton prefixAut = Closures.prefixClosure(generatedAut);
                    NFAutomaton suffixAut = Closures.suffixClosure(generatedAut);
                    NFAutomaton subwordAut = Closures.subwordClosure(generatedAut);

                    prefixAutsList.add(prefixAut);
                    suffixNfAutsList.add(suffixAut);
                    suffixAutsList.add(suffixAut.transformNFA2DFA());
                    subwordNfAutsList.add(subwordAut);
                    subwordAutsList.add(subwordAut.transformNFA2DFA());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                counter += 1;
            }
        }
    }
}
