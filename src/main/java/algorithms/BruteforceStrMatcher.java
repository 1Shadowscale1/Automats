package algorithms;

import java.util.ArrayList;
import java.util.List;

public class BruteforceStrMatcher {
    private BruteforceStrMatcher() {}

    public static List<String> Bruteforce(String pattern, String text){
        List<String> result = new ArrayList<>();
        int textLen = text.length();
        int patternLen = pattern.length();
        StringBuilder patternBuilder = new StringBuilder(pattern);
        StringBuilder textBuilder = new StringBuilder(text);

        for (int i = 0; i < textLen; i++) {
            int j = 0;
            for (; j < patternLen; j++) {
                if (j != 0) {
                    result.add(patternBuilder.substring(0, j) + "     " + textBuilder.substring(0, i) +
                        "|" + textBuilder.substring(i, i + j) + "|" + textBuilder.substring(i + j, textLen) + " ");
                }
                if (i + j == textLen || textBuilder.charAt(i + j) != patternBuilder.charAt(j)) {
                    break;
                }
            }
            if (j == patternLen) {
                result.add(patternBuilder.substring(0, j) + "     " + textBuilder.substring(0, i) +
                        "|" + textBuilder.substring(i, i + j) + "|" + textBuilder.substring(i + j, textLen) + " ");
                result.add("Подстрока найдена по индексу " + i);
            }
        }
        return result;
    }
}
