package automaton;

import com.google.common.collect.HashBasedTable;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedAutomaton extends Automaton {
    public String syncWord;
    public String shortestSyncWord;
    public List<String> twoElementVertices;
    public List<String> combinedElementVertices;
    public final SynchronizedAutomaton originalSyncAutomaton;

    public SynchronizedAutomaton(HashBasedTable<String, String, String> jumpTable,
                                 List<String> twoElementVertices, List<String> combinedElementVertices,
                                 String syncWord, String shortestSyncWord) throws CloneNotSupportedException {

        super(false, jumpTable, null, new ArrayList<>());

        this.twoElementVertices = twoElementVertices;
        this.combinedElementVertices = combinedElementVertices;
        this.syncWord = syncWord;
        this.shortestSyncWord = shortestSyncWord;
        this.originalSyncAutomaton = this.clone();
    }

    @Override
    public SynchronizedAutomaton clone() throws CloneNotSupportedException {
        SynchronizedAutomaton synchronizedAutomaton = (SynchronizedAutomaton) super.clone();

        synchronizedAutomaton.jumpTable = HashBasedTable.create(jumpTable);
        synchronizedAutomaton.twoElementVertices = new ArrayList<>(twoElementVertices);
        synchronizedAutomaton.combinedElementVertices = new ArrayList<>(combinedElementVertices);

        return synchronizedAutomaton;
    }
}
