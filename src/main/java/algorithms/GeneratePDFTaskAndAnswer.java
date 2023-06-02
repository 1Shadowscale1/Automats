package algorithms;
import java.io.IOException;
import java.util.*;

import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;


public class GeneratePDFTaskAndAnswer {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 5;
    private static final double ver2 = 0.5; //вероятность того, что генератор выдаст подстроку из строки
    private static int wordCount; //колличество слов в генерируемом предложении
    private static int optionCount; //колличество вариантов
    static Map<String, String> stringAndSubstring = new HashMap<>();
    public GeneratePDFTaskAndAnswer(int wordCount, int optionCount) {
        this.wordCount = wordCount;
        this.optionCount = optionCount;
    }

    public void generateStringAndPattern() {
        for (int i = 0; i < optionCount; i++) {
            Random random = new Random();
            double randomNumber2 = random.nextDouble();
            String w = generateWords();
            String[] ww = w.split(" ");
            if (randomNumber2 <= ver2){
                int randomW = random.nextInt(ww.length);
                String wSub = ww[randomW];
                Random r = new Random();
                int startIndex = r.nextInt(wSub.length() - 2); // начальный индекс
                int endIndex = startIndex + r.nextInt(2) + 2; // конечный индекс (2 или 3 символа)
                String subStr = wSub.substring(startIndex, endIndex);
                stringAndSubstring.put(w, subStr);

            }
            else{
                String a = generateSubstring();
                while (!IsStrContainsSubstring(ww, a)){
                    a= generateWords();
                }
                stringAndSubstring.put(w, a);
            }
        }
    }
    public static Boolean IsStrContainsSubstring(String[] str, String substr) {
        for (String e : str) {
            if (e.contains(substr)) {
                return false;
            }
        }
        return true;
    }
    public static List<List<String>> createTable(List<String> result) {
        List<List<String>> items = new ArrayList<>();

        for (String s : result) {
            String[] currentResult = s.trim().split("\\s{5}");
            if (currentResult.length > 1) {
                items.add(new ArrayList<>());
                int lastIndex = items.size() - 1;
                items.get(lastIndex).add(currentResult[0]);
                items.get(lastIndex).add(currentResult[1]);
                items.get(lastIndex).add("");
            } else {
                int lastIndex = items.size() - 1;
                items.get(lastIndex).remove(2);
                items.get(lastIndex).add(currentResult[0]);
            }
        }

        return items;
    }

    public static List<List<String>> createPrefixTable(List<String> result) {
        List<List<String>> items = new ArrayList<>();

        String[] firstPrefixData = result.get(0).split("\\) ")[1].split("\\D+");
        String[] rawSecondPrefixData = result.get(1).split("\\) ")[1].split("[^A-Za-z]+");
        String[] secondPrefixData = new String[firstPrefixData.length];
        secondPrefixData[0] = "";

        int counter = 1;
        for (int i = 1; i < firstPrefixData.length; i++) {
            if (Objects.equals(firstPrefixData[i], "0")) {
                secondPrefixData[i] = "";
            } else {
                secondPrefixData[i] = rawSecondPrefixData[counter];
                counter++;
            }
        }

        for (int i = 1; i < firstPrefixData.length; i++) {
            items.add(new ArrayList<>());
            items.get(i - 1).add(String.valueOf(i - 1));
            items.get(i - 1).add(firstPrefixData[i]);
            items.get(i - 1).add(secondPrefixData[i]);
        }

        return items;
    }
    public void generatePdfAnswer() throws IOException {
        int i = 0;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/answer.pdf"));
        document.open();
        for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {

            List<String> output = algorithms.KnuthMorrisPratt.KMP(entry.getValue(), entry.getKey());

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font mainFont = new Font(Font.HELVETICA, 14);
            Chunk optionChunk = new Chunk("Option " + i + "\n", mainFont);
            Chunk isAChunk = new Chunk("Is a ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk valueChunk = new Chunk(entry.getValue(), boldFont);
            Chunk substringChunk = new Chunk(" substring of a string ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk keyChunk = new Chunk(entry.getKey(), boldFont);
            Chunk questionMarkChunk = new Chunk(" ?\n" + "\n",FontFactory.getFont(FontFactory.HELVETICA));
            Paragraph paragraph = new Paragraph();
            paragraph.add(optionChunk);
            paragraph.add(isAChunk);
            paragraph.add(valueChunk);
            paragraph.add(substringChunk);
            paragraph.add(keyChunk);
            paragraph.add(questionMarkChunk);
            document.add(paragraph);

            List<List<String>> out1 = createPrefixTable(output);
            List<String> out2 = new ArrayList<>(output);
            out2.remove(0);
            out2.remove(0);

            List<List<String>> out22 = createTable(out2);
            System.out.println(out1); //
            System.out.println(out22); //

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(70);
            table.addCell("Step");
            table.addCell("Prefix - function");
            table.addCell("Prefix = Suffix");

            for (int j = 0; j < out1.size(); j++) {
                for(int k = 0; k <out1.get(0).size(); k++ ){
                    String a = out1.get(j).get(k);
                    table.addCell(a);
                }
            }
            PdfPTable table2 = new PdfPTable(3);
            table2.setWidthPercentage(70);
            table2.addCell("Substring");
            table2.addCell("Text");
            table2.addCell("State");

            for (int q = 0; q < out22.size(); q++) {
                for(int w = 0; w <out22.get(0).size(); w++ ){
                    String aa = out22.get(q).get(w);
                    table2.addCell(aa);
                }
            }
            Chunk pf = new Chunk(" Prefix - function ", FontFactory.getFont(FontFactory.HELVETICA));
            document.add(pf);
            document.add(table);
            Chunk pr = new Chunk(" Protocol ", FontFactory.getFont(FontFactory.HELVETICA));
            document.add(pr);
            document.add(table2);
            Chunk sp = new Chunk("\n");
            document.add(sp);
            i++;
        }
        document.close();

    }

    public void generatePdfTask() throws IOException {
        int i = 0;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/task.pdf"));
        document.open();
        for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {
            //String f = "Option " + i + " \n" + "Is a "  + entry.getValue() + " substring of a string " + entry.getKey() + " ?";
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font mainFont = new Font(Font.HELVETICA, 14);
            Chunk optionChunk = new Chunk("Option " + i + "\n", mainFont);
            Chunk isAChunk = new Chunk("Is a ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk valueChunk = new Chunk(entry.getValue(), boldFont);
            Chunk substringChunk = new Chunk(" substring of a string ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk keyChunk = new Chunk(entry.getKey(), boldFont);
            Chunk questionMarkChunk = new Chunk(" ?\n" + "\n",FontFactory.getFont(FontFactory.HELVETICA));
            Paragraph paragraph = new Paragraph();
            paragraph.add(optionChunk);
            paragraph.add(isAChunk);
            paragraph.add(valueChunk);
            paragraph.add(substringChunk);
            paragraph.add(keyChunk);
            paragraph.add(questionMarkChunk);
            document.add(paragraph);
            i++;

        }
        document.close();
    }

    public static String generateWords(){
        int n = wordCount; // Количество слов в предложении
        Random rand = new Random();
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            // Генерируем слово случайной длины от 4 до 5 символов
            int len = rand.nextInt((MAX_LENGTH - MIN_LENGTH) + 1) + MIN_LENGTH;
            for (int j = 0; j < len; j++) {
                char ch = CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())); // Выбираем случайный символ из допустимых

                wordBuilder.append(ch);
            }
            if (i != n-1){
                wordBuilder.append(" ");
            }
            else{
                continue;
            }
        }
        return wordBuilder.toString();
    }
    public static String generateSubstring() {
        Random random = new Random();
        int length = random.nextInt(2) + 2; // генерируем случайную длину в диапазоне от 2 до 3
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
