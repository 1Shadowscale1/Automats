package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.HashBasedTable;

import automaton.Automaton;
import automaton.NFAutomaton;

public class ReverseAutomatonsGenerator {
    public int automatonsNumber;
    public int verticesNumber;
    public int lettersNumber;
    public List<Automaton> generatedAutsList;
    public List<NFAutomaton> reverseNfAutsList;
    public List<Automaton> reverseAutsList;

    public ReverseAutomatonsGenerator(int automatonsNumber, int verticesNumber, int lettersNumber) {
        this.automatonsNumber = automatonsNumber;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
        this.generatedAutsList = new ArrayList<>();
        this.reverseNfAutsList = new ArrayList<>();
        this.reverseAutsList = new ArrayList<>();
    }

    public void generateReverseAutomatons() {
        List<String> letters = new ArrayList<>(List.of("a", "b", "c", "d"));
        Random randomInt = new Random(System.currentTimeMillis());

        for (int k = 0; k < automatonsNumber; k++) {
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
            NFAutomaton reverseNfAut = Reverse.reverseAutomaton(generatedAut);

            generatedAutsList.add(generatedAut);
            reverseNfAutsList.add(reverseNfAut);
            reverseAutsList.add(reverseNfAut.transformNFA2DFA());
        }
    }
}
