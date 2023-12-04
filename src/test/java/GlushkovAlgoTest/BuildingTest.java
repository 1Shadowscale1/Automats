package GlushkovAlgoTest;

import algorithms.GlushkovAlgo;
import com.google.common.collect.HashBasedTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Disabled
class BuildingNKATest {
    static final String wikiExample = "(a(ab)*)*+(ba)*";
    static final String simpleReg = "abc + abc";
    static final String complexReg = "a + bc* + ((d+e)f)*";
    @Test
    void testWikiExample(){
        HashBasedTable<String, String, ArrayList<String>> expected = HashBasedTable.create();

        expected.put("-1", "a", new ArrayList<>(List.of("1")));
        expected.put("-1", "b", new ArrayList<>(List.of("11")));

        expected.put("1", "a", new ArrayList<>(List.of("1", "3")));

        expected.put("3", "b", new ArrayList<>(List.of("4")));

        expected.put("4", "a", new ArrayList<>(List.of("3", "1")));

        expected.put("11", "a", new ArrayList<>(List.of("12")));

        expected.put("12", "b", new ArrayList<>(List.of("11")));

        HashBasedTable<String, String, ArrayList<String>> real = GlushkovAlgo.buildNKAFromReg(wikiExample);

        Assertions.assertEquals(real, expected);
    }

    @Test
    void TestSimpleReg(){
        HashBasedTable<String, String, ArrayList<String>> expected = HashBasedTable.create();

        expected.put("-1", "a", new ArrayList<>(List.of("0", "6")));
        expected.put("0", "b", new ArrayList<>(List.of("1")));
        expected.put("1", "c", new ArrayList<>(List.of("2")));
        expected.put("6", "b", new ArrayList<>(List.of("7")));
        expected.put("7", "c", new ArrayList<>(List.of("8")));

        HashBasedTable<String, String, ArrayList<String>> real = GlushkovAlgo.buildNKAFromReg(simpleReg);
        Assertions.assertEquals(real, expected);
    }

    @Test
    void TestComplexReg(){
        HashBasedTable<String, String, ArrayList<String>> expected = HashBasedTable.create();

        expected.put("-1", "a", new ArrayList<>(List.of("0")));
        expected.put("-1", "b", new ArrayList<>(List.of("4")));
        expected.put("-1", "d", new ArrayList<>(List.of("12")));
        expected.put("-1", "e", new ArrayList<>(List.of("14")));

        expected.put("4", "c", new ArrayList<>(List.of("5")));

        expected.put("5", "c", new ArrayList<>(List.of("5")));

        expected.put("12", "f", new ArrayList<>(List.of("16")));

        expected.put("14", "f", new ArrayList<>(List.of("16")));

        expected.put("16", "d", new ArrayList<>(List.of("12")));
        expected.put("16", "e", new ArrayList<>(List.of("14")));

        HashBasedTable<String, String, ArrayList<String>> real =
                GlushkovAlgo.buildNKAFromReg(complexReg);

        Assertions.assertEquals(expected, real);
    }
}

class BuildingDKATest{
    static final String wikiExample = "(a(ab)*)*+(ba)*";
    static final String simpleReg = "abc + abc";
    static final String complexReg = "a + bc* + ((d+e)f)*";
    static final String superSimple = "a + b";

    @Test
    void simpleRegTest(){
        try {
            GlushkovAlgo.doGlushkovAlgo(simpleReg);
        }catch (Exception e){e.printStackTrace();}

    }

    @Test
    void wikiRegTest(){
        try{
            GlushkovAlgo.doGlushkovAlgo(wikiExample);
        }catch (Exception e){e.printStackTrace();}

    }

    @Test
    void superSimpleTest(){
        try{
            GlushkovAlgo.doGlushkovAlgo(superSimple);
        }catch (Exception e){e.printStackTrace();}

    }
}
