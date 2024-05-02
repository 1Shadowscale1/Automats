package algorithms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.HashBasedTable;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import automaton.Automaton;
import automaton.NFAutomaton;

public class ClosureAutomatonsGenerator {
    public int automatonsNumber;
    public int verticesNumber;
    public int lettersNumber;
    public List<Automaton> generatedAutsList;
    public List<Automaton> prefixAutsList;
    public List<Automaton> minimizedPrefixAutsList;
    public List<NFAutomaton> suffixNfAutsList;
    public List<NFAutomaton> subwordNfAutsList;
    public List<Automaton> suffixAutsList;
    public List<Automaton> subwordAutsList;
    public List<Automaton> minimizedSuffixAutsList;
    public List<Automaton> minimizedSubwordAutsList;

    public ClosureAutomatonsGenerator(int automatonsNumber, int verticesNumber, int lettersNumber) {
        this.automatonsNumber = automatonsNumber;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
        this.generatedAutsList = new ArrayList<>();
        this.prefixAutsList = new ArrayList<>();
        this.minimizedPrefixAutsList = new ArrayList<>();
        this.suffixNfAutsList = new ArrayList<>();
        this.subwordNfAutsList = new ArrayList<>();
        this.suffixAutsList = new ArrayList<>();
        this.subwordAutsList = new ArrayList<>();
        this.minimizedSuffixAutsList = new ArrayList<>();
        this.minimizedSubwordAutsList = new ArrayList<>();
    }

    public void generateClosureAutomatons() {
        List<String> letters = new ArrayList<>(List.of("a", "b", "c", "d"));
        Random randomInt = new Random(System.currentTimeMillis());

        int counter = 0;
        int easyTaskCounter = 0;
        while (counter != automatonsNumber) {
            HashBasedTable<String, String, String> currentTable = HashBasedTable.create();

            for (int i = 1; i <= verticesNumber; i++)
                for (int j = 0; j < lettersNumber; j++)
                    currentTable.put(String.valueOf(i), letters.get(j),
                            String.valueOf(1 + randomInt.nextInt(verticesNumber)));

            String startVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));

            List<String> finalVertices = new ArrayList<>();
            int finalVerticesCount = 1 + randomInt.nextInt(verticesNumber - 1);
            Set<String> alreadySeen = new HashSet<>();
            for (int i = 0; i < finalVerticesCount; i++) {
                String finalVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));

                while (alreadySeen.contains(finalVertex) || finalVertex.equals(startVertex)) {
                    finalVertex = String.valueOf(1 + randomInt.nextInt(verticesNumber));
                }

                alreadySeen.add(finalVertex);
                finalVertices.add(finalVertex);
            }

            Automaton generatedAut = new Automaton(false, currentTable, startVertex, finalVertices);

            Boolean isEasyTask = false;
            if (generatedAut.isAutomatonWithoutUnreachableVertices()) {
                try {
                    if (randomInt.nextInt(automatonsNumber) == counter &&
                            counter > randomInt.nextInt(automatonsNumber) &&
                            easyTaskCounter < 2) {
                        isEasyTask = true;
                        easyTaskCounter += 1;
                    }

                    Automaton prefixAut = Closures.prefixClosure(generatedAut);
                    Automaton minimizedPrefixAut = Adduction.buildPrettyAdductedAutomat(prefixAut);

                    if (!isEasyTask && minimizedPrefixAut.vertices.size() == 1)
                        continue;

                    NFAutomaton suffixAut = Closures.suffixClosure(generatedAut);
                    Automaton transformedSuffixAut = suffixAut.transformNFA2DFA();
                    Automaton minimizedSuffixAut = Adduction.buildPrettyAdductedAutomat(transformedSuffixAut);

                    if (!isEasyTask && minimizedSuffixAut.vertices.size() == 1)
                        continue;

                    NFAutomaton subwordAut = Closures.subwordClosure(generatedAut);
                    Automaton transformedSubwordAut = subwordAut.transformNFA2DFA();
                    Automaton minimizedSubwordAut = Adduction.buildPrettyAdductedAutomat(transformedSubwordAut);

                    if (!isEasyTask && minimizedPrefixAut.vertices.size() == 1)
                        continue;

                    generatedAutsList.add(generatedAut);

                    prefixAutsList.add(prefixAut);
                    minimizedPrefixAutsList.add(minimizedPrefixAut);

                    suffixNfAutsList.add(suffixAut);
                    suffixAutsList.add(transformedSuffixAut);
                    minimizedSuffixAutsList.add(minimizedSuffixAut);

                    subwordNfAutsList.add(subwordAut);
                    subwordAutsList.add(transformedSubwordAut);
                    minimizedSubwordAutsList.add(minimizedSubwordAut);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                counter += 1;
                isEasyTask = false;
            }
        }
    }

    public void createTasksPdfFile() throws IOException {
        Document document = new Document();

        File directory = new File("tasks");
        if (!directory.exists()) {
            directory.mkdir();
        }

        PdfWriter.getInstance(document, new FileOutputStream(new File("tasks/closure_task.pdf")));

        document.open();

        Font mainFont = FontFactory.getFont(FontFactory.HELVETICA);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        Chunk titleChunk = new Chunk(
                "Task: find prefix, suffix and subword closure of DFA. If NFA found, then transform it to DFA and find minimized DFA\n\n",
                boldFont);

        Paragraph paragraph = new Paragraph();
        paragraph.add(titleChunk);

        for (int counter = 1; counter <= automatonsNumber; counter++) {
            Chunk optionChunk = new Chunk("Option " + counter + "\n", boldFont);
            paragraph.add(optionChunk);

            Automaton generatedAut = generatedAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, generatedAut);
        }

        document.add(paragraph);
        document.close();
    }

    public void createAnswersPdfFile() throws IOException {
        Document document = new Document();

        File directory = new File("tasks");
        if (!directory.exists()) {
            directory.mkdir();
        }

        PdfWriter.getInstance(document, new FileOutputStream(new File("tasks/closure_answer.pdf")));

        document.open();

        Font mainFont = FontFactory.getFont(FontFactory.HELVETICA);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        Chunk titleChunk = new Chunk("Closure task answers\n\n", boldFont);

        Paragraph paragraph = new Paragraph();
        paragraph.add(titleChunk);

        for (int counter = 1; counter <= automatonsNumber; counter++) {
            Chunk optionChunk = new Chunk("Option " + counter + "\n\n", boldFont);
            paragraph.add(optionChunk);

            Chunk generatedDFAChunk = new Chunk("Initial DFA\n", mainFont);
            paragraph.add(generatedDFAChunk);

            Automaton generatedAut = generatedAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, generatedAut);

            Chunk prefixDFAChunk = new Chunk("Prefix closure of DFA\n", mainFont);
            paragraph.add(prefixDFAChunk);

            Automaton prefixAut = prefixAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, prefixAut);

            Chunk minimizedPrefixDFAChunk = new Chunk("Minimized prefix closure of DFA\n", mainFont);
            paragraph.add(minimizedPrefixDFAChunk);

            Automaton minimizedPrefixAut = minimizedPrefixAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, minimizedPrefixAut);

            Chunk suffixNFAChunk = new Chunk("Suffix closure of DFA\n", mainFont);
            paragraph.add(suffixNFAChunk);

            NFAutomaton suffixNfAut = suffixNfAutsList.get(counter - 1);
            writeNfAutTable(mainFont, paragraph, suffixNfAut);

            Chunk suffixDFAChunk = new Chunk("Transformed suffix closure of DFA\n", mainFont);
            paragraph.add(suffixDFAChunk);

            Automaton suffixAut = suffixAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, suffixAut);

            Chunk minimizedSuffixDFAChunk = new Chunk("Minimized suffix closure of DFA\n", mainFont);
            paragraph.add(minimizedSuffixDFAChunk);

            Automaton minimizedSuffixAut = minimizedSuffixAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, minimizedSuffixAut);

            Chunk subwordNFAChunk = new Chunk("Subword closure of DFA\n", mainFont);
            paragraph.add(subwordNFAChunk);

            NFAutomaton subwordNfAut = subwordNfAutsList.get(counter - 1);
            writeNfAutTable(mainFont, paragraph, subwordNfAut);

            Chunk subwordDFAChunk = new Chunk("Transformed subword closure of DFA\n", mainFont);
            paragraph.add(subwordDFAChunk);

            Automaton subwordAut = subwordAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, subwordAut);

            Chunk minimizedSubwordDFAChunk = new Chunk("Minimized subword closure of DFA\n", mainFont);
            paragraph.add(minimizedSubwordDFAChunk);

            Automaton minimizedSubwordAut = minimizedSubwordAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, minimizedSubwordAut);
        }

        document.add(paragraph);
        document.close();
    }

    private void writeAutTable(Font mainFont, Paragraph paragraph, Automaton automaton) {
        PdfPTable pdfPTable = new PdfPTable(lettersNumber + 3);
        pdfPTable.addCell("State \\ Letter");
        for (String letter : automaton.letters)
            pdfPTable.addCell(letter);
        pdfPTable.addCell("S");
        pdfPTable.addCell("F");

        for (String vertex : automaton.vertices) {
            pdfPTable.addCell(vertex);

            for (String letter : automaton.letters)
                pdfPTable.addCell(automaton.getJumpByVertexAndLetter(vertex, letter));

            if (vertex.equals(automaton.startVertex))
                pdfPTable.addCell("1");
            else
                pdfPTable.addCell("0");

            if (automaton.finalVertices.contains(vertex))
                pdfPTable.addCell("1");
            else
                pdfPTable.addCell("0");
        }

        paragraph.add(pdfPTable);
        paragraph.add(new Chunk("\n", mainFont));
    }

    private void writeNfAutTable(Font mainFont, Paragraph paragraph, NFAutomaton nfAutomaton) {
        PdfPTable pdfPTable = new PdfPTable(lettersNumber + 3);
        pdfPTable.addCell("State \\ Letter");
        for (String letter : nfAutomaton.letters)
            pdfPTable.addCell(letter);
        pdfPTable.addCell("S");
        pdfPTable.addCell("F");

        for (String vertex : nfAutomaton.vertices) {
            pdfPTable.addCell(vertex);

            for (String letter : nfAutomaton.letters) {
                List<String> nextVertices = nfAutomaton.jumpTable.get(vertex, letter);

                if (nextVertices == null)
                    pdfPTable.addCell("");
                else
                    pdfPTable.addCell(String.join(", ", nextVertices));
            }

            if (nfAutomaton.startVertices.contains(vertex))
                pdfPTable.addCell("1");
            else
                pdfPTable.addCell("0");

            if (nfAutomaton.finalVertices.contains(vertex))
                pdfPTable.addCell("1");
            else
                pdfPTable.addCell("0");
        }

        paragraph.add(pdfPTable);
        paragraph.add(new Chunk("\n", mainFont));
    }
}
