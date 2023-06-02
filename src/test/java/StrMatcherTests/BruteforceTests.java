package StrMatcherTests;

import algorithms.BruteforceStrMatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BruteforceTests {
    @Test
    public void BruteforceTest1(){
        String text = "yxyxyxxyxyxyxyyxyxyxxyxyxyxyyxyxyxxyxyxyxyyxyxyxxyxyxyxxy";
        String pattern = "yxyxyxxyxyxyxx";
        List<String> output = algorithms.BruteforceStrMatcher.Bruteforce(pattern, text);
        System.out.println(output);
    }
    @Test
    public void BruteforceTest2(){
        String text = "aaaaa";
        String pattern = "aa";
        List<String> output = algorithms.BruteforceStrMatcher.Bruteforce(pattern, text);
        System.out.println(output);
    }
}
