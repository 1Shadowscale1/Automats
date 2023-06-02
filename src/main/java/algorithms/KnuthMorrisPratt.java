package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnuthMorrisPratt {
    private KnuthMorrisPratt () {}

    public static List<String> KMP(String pattern, String text) {
        List<String> result = new ArrayList<>();
        int patternLen = pattern.length();
        int textLen = text.length();
        StringBuilder patternBuilder = new StringBuilder(pattern);
        StringBuilder textBuilder = new StringBuilder(text);
        int[] lps = new int[patternLen];
        String[] prefix = new String[patternLen];
        int j = 0;
        LPS(patternBuilder, patternLen, lps, prefix);
        //result.add("Префикс-функция (длины) " + Arrays.toString(lps));
        result.add("Prefix-function (length) " + Arrays.toString(lps));

        //result.add("Префикс-функция (префиксы) " + Arrays.toString(prefix));
        result.add("Prefix-function (prefix) " + Arrays.toString(prefix));

        int i = 0;
        while (i < textLen) {
            if (patternBuilder.charAt(j) == textBuilder.charAt(i)) {
                j++;
                i++;
                result.add(patternBuilder.substring(0, j) + "     " + textBuilder.substring(0, i-j) +
                        "|" + textBuilder.substring(i-j, i) + "|" + textBuilder.substring(i, textLen) + " ");
            }
            if (j == patternLen) {
                //result.add("Подстрока найдена по индексу " + (i - j));
                result.add("Substring found by index " + (i - j));
                j = lps[j - 1];
            }
            else if (i < textLen && patternBuilder.charAt(j) != textBuilder.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return result;
    }

    static void LPS(StringBuilder patternBuilder, int patternLen, int[] lps, String[] prefix)
    {
        int len = 0;
        int ind = 1;
        lps[0] = 0;
        prefix[0] = "";
        while (ind < patternLen) {
            if (patternBuilder.charAt(ind) == patternBuilder.charAt(len)) {
                len++;
                lps[ind] = len;
                prefix[ind] = patternBuilder.substring(0, len);
                ind++;
            }
            else
            {
                if (len != 0)
                    len = lps[len - 1];
                else
                {
                    lps[ind] = len;
                    prefix[ind] = "";
                    ind++;
                }
            }
        }
    }
}
