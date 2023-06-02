package StrMatcherTests;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class KnuthMorrisPratt {
    @Test
    public void checkKMPTest1() {
        String text = "yxyxyxxyxyxyxyyxyxyxxyxyxyxyyxyxyxxyxyxyxyyxyxyxxyxyxyxxy";
        String pattern = "yxyxyxxyxyxyxx";
        List<String> output = algorithms.KnuthMorrisPratt.KMP(pattern, text);
        System.out.println(output);
    }
    @Test
    public void checkKMPTest2() {
        String text = "mbacj";
        String pattern = "acj";
        List<String> output = algorithms.KnuthMorrisPratt.KMP(pattern, text);
        System.out.println(output);
    }
}