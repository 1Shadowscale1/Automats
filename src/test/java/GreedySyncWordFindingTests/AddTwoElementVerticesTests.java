package GreedySyncWordFindingTests;

import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import algorithms.GreedySyncWordFinding;
import automaton.SynchronizedAutomaton;

public class AddTwoElementVerticesTests {
    @Test
    public void SimpleTest() {
        HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

        jumpTable.put("0", "a", "1");
        jumpTable.put("1", "a", "2");
        jumpTable.put("2", "a", "2");

        jumpTable.put("0", "b", "0");
        jumpTable.put("1", "b", "0");
        jumpTable.put("2", "b", "2");

        SynchronizedAutomaton syncAut = null;
        try {
            syncAut = new SynchronizedAutomaton
                (jumpTable, new ArrayList<>(), new ArrayList<>(), null, null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        GreedySyncWordFinding.addTwoElementVertices(syncAut);

        Assertions.assertEquals(List.of("01", "02", "12"), syncAut.twoElementVertices);
    }
}
