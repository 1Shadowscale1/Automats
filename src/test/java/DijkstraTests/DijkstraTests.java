package DijkstraTests;

import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algorithms.Dijkstra;
import algorithms.GreedySyncWordFinding;
import automaton.SynchronizedAutomaton;

public class DijkstraTests {
    @Test
    public void SimpleTest() {
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();
        Map<String, List<String>> dijkstraResult = new HashMap<>();

        jumpTable.put("0", "a", "1");
        jumpTable.put("1", "a", "2");
        jumpTable.put("2", "a", "2");

        jumpTable.put("0", "b", "0");
        jumpTable.put("1", "b", "0");
        jumpTable.put("2", "b", "2");

        SynchronizedAutomaton syncAut = null;
        try {
            syncAut = new SynchronizedAutomaton(jumpTable, new ArrayList<>(), new ArrayList<>(), null, null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        if (syncAut != null) {
            GreedySyncWordFinding.addTwoElementVertices(syncAut);
            for (String vertex : syncAut.twoElementVertices)
                dijkstraResult.put(vertex,
                        Dijkstra.dijkstraAlg(syncAut, vertex).entrySet().iterator().next().getValue());

            Assertions.assertEquals(List.of("b"), dijkstraResult.get("01"));
            Assertions.assertEquals(List.of("a", "a"), dijkstraResult.get("02"));
            Assertions.assertEquals(List.of("a"), dijkstraResult.get("12"));
        }
    }
}
