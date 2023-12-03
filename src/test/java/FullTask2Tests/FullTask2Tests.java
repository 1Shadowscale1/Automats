package FullTask2Tests;

import algorithms.GlushkovAlgo;
import algorithms.Isomorphism;
import algorithms.RegExprBuild;
import automaton.Automaton;
import automaton.IsomorphismResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpException;

import java.util.HashMap;

public class FullTask2Tests {
    public IsomorphismResult automatsAreIsomorphic(Automaton aut1, Automaton aut2){
        try {
            return Isomorphism.automatsAreIsomorphic(aut1, aut2);
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;
    }

    private Automaton doGlushkovAlgo(String regexp){
        try
        {
            return GlushkovAlgo.doGlushkovAlgo(regexp);
        }
        catch (RegexpException e){
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void plusIterationTest(){
        var aut1 = doGlushkovAlgo("(b + c)*");
        var aut2 = doGlushkovAlgo("b + c");
        var expected = new IsomorphismResult("", null ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigRegexp1and2Test(){
        var aut1 = doGlushkovAlgo("c + (dd+d)(d)");
        var aut2 = doGlushkovAlgo("(d+d)(e+e)+(f+f)");
        var expected = new IsomorphismResult("c", "de" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigRegexp3and4Test(){
        var aut1 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d))");
        var aut2 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d)) + g");
        var expected = new IsomorphismResult(null, "g" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigRegexp4and5Test(){
        var aut1 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d)) + g");
        var aut2 = doGlushkovAlgo("((c+((d+d)(e+e)+(f+f))(d)) + g) + h");
        var expected = new IsomorphismResult(null, "h" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void complexProdTest(){
        var aut1 = doGlushkovAlgo("(a + b)(c + de)");
        var aut2 = doGlushkovAlgo("(a + b)de");
        var expected = new IsomorphismResult("ac", null ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void plusWithBracketsAndIteration1Test(){
        var aut1 = doGlushkovAlgo("a + b*");
        var aut2 = doGlushkovAlgo("a + b");
        var expected = new IsomorphismResult("", null ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void plusWithBracketsAndIterationTest(){
        var aut1 = doGlushkovAlgo("a + (b + c)* + d");
        var aut2 = doGlushkovAlgo("a + b + c + d");
        var expected = new IsomorphismResult("", null ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void plusMultiplyIterationTest(){
        var aut1 = doGlushkovAlgo("(a + b + cd)*");
        var aut2 = doGlushkovAlgo("(a + b + c)*");
        var expected = new IsomorphismResult("cd", "c" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void wikiExampleTest(){
        var aut1 = doGlushkovAlgo("(a(ab)*)* + (ba)*");
        var aut2 = doGlushkovAlgo("(a(ab)*)* + (b)*");
        var expected = new IsomorphismResult("ba", "b" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void sumSumSumTest(){
        var aut1 = doGlushkovAlgo("ab + (c+d)");
        var aut2 = doGlushkovAlgo("ab + (c+d+e)");
        var expected = new IsomorphismResult(null, "e" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void letterSumSumTest(){
        var aut1 = doGlushkovAlgo("a + (b+c)");
        var aut2 = doGlushkovAlgo("a + (b+cd)");
        var expected = new IsomorphismResult("c", "cd" ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam1and2Test(){
        var regexp1 = String.format(
                "b(ba)*(b(b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol);
        var regexp2 = String.format(
                "b(ba)*(b(b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol);

        var aut1 = doGlushkovAlgo(regexp1);
        var aut2 = doGlushkovAlgo(regexp2);

        var associations = new HashMap<String, String>();
        associations.put("0", "0");
        associations.put("1", "1");
        associations.put("6, 4", "6, 4");
        associations.put("3", "3");
        associations.put("2, 5", "2, 5");
        var expected = new IsomorphismResult(null, null,associations);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam3and4Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "b*a(b(b + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "ab*a(b + %s) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("a", "",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam5and6Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "(ab + ba)*a(a + %s)",
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "a(aa + %s) + b + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("aa", "",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam7and8Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "(bb + a)b + b(a + %s)",
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "(a + b)(b(a + b + %s) + a) + %s",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("b", "",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam9and10Test(){
        var s = RegExprBuild.emptyWordSymbol;
        var aut1 = doGlushkovAlgo(String.format(
                "a(b(b + %s) + %s) + %s", s, s, s));
        var aut2 = doGlushkovAlgo(String.format(
                "(baa)*(ba(ba* + %s) + %s)", s, s));

        var expected = new IsomorphismResult("a", "ba",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam11and12Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "ab* + b(a + b + %s)",
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "ab*(a + %s) + ba*(b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult(null, "aa",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam13and14Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "a*b(a(a + %s) + b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "(bb + a)(a + %s)",
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("ab", "a",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam15and16Test(){
        var aut1 = doGlushkovAlgo(String.format(
                "(ab + ba)*(a(a + %s) + b + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));
        var aut2 = doGlushkovAlgo(String.format(
                "(a + b)((a + b)(a + %s) + %s)",
                RegExprBuild.emptyWordSymbol,
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("", "aaa",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void cycleTest(){
        var aut1 = doGlushkovAlgo("(hello)*");
        var aut2 = doGlushkovAlgo(String.format(
                "hello + hellohello + hellohellohello + %s",
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("hellohellohellohello", null,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam17and18Test(){
        var s = RegExprBuild.emptyWordSymbol;
        var aut1 = doGlushkovAlgo(String.format("(ba)*(bb(a + b + %s) + %s)", s, s));
        var aut2 = doGlushkovAlgo(String.format("b((a + b)(a + b + %s) + %s) + %s", s, s, s));

        var expected = new IsomorphismResult("baba", "b",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void Exam19and20Test(){
        var aut1 = doGlushkovAlgo(
                "(ab + ba)*(a + bb)");
        var aut2 = doGlushkovAlgo(String.format(
                "(baa)*(b + ab* + %s)",
                RegExprBuild.emptyWordSymbol));

        var expected = new IsomorphismResult("aba", "",null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }
}
