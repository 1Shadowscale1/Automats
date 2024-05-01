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

public class ReverseAutomatonsGenerator {
    public int automatonsNumber;
    public int verticesNumber;
    public int lettersNumber;
    public List<Automaton> generatedAutsList;
    public List<NFAutomaton> reverseNfAutsList;
    public List<Automaton> reverseAutsList;
    public List<Automaton> minimizedAutsList;

    public ReverseAutomatonsGenerator(int automatonsNumber, int verticesNumber, int lettersNumber) {
        this.automatonsNumber = automatonsNumber;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
        this.generatedAutsList = new ArrayList<>();
        this.reverseNfAutsList = new ArrayList<>();
        this.reverseAutsList = new ArrayList<>();
        this.minimizedAutsList = new ArrayList<>();
    }

    public void generateReverseAutomatons() {
        List<String> letters = new ArrayList<>(List.of("a", "b", "c", "d"));
        Random randomInt = new Random(System.currentTimeMillis());

        int counter = 0;
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

            if (generatedAut.isAutomatonFullAndWithoutStockVertices()) {
                NFAutomaton reverseNfAut = Reverse.reverseAutomaton(generatedAut);
                Automaton transformedAut = reverseNfAut.transformNFA2DFA();

                generatedAutsList.add(generatedAut);
                reverseNfAutsList.add(reverseNfAut);
                reverseAutsList.add(transformedAut);

                try {
                    Automaton minimizedAut = Adduction.buildAdductedAutomatFromNotFullDFA(transformedAut);
                    minimizedAutsList.add(minimizedAut);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                counter += 1;
            }
        }
    }

    public void createTasksPdfFile() throws IOException {
        Document document = new Document();

        File directory = new File("tasks");
        if (!directory.exists()) {
            directory.mkdir();
        }

        PdfWriter.getInstance(document, new FileOutputStream(new File("tasks/reverse_task.pdf")));

        document.open();

        Font mainFont = FontFactory.getFont(FontFactory.HELVETICA);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        Chunk titleChunk = new Chunk(
                "Task: find the reverse of DFA. If NFA found, then transform it to DFA and find minimized DFA\n\n",
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

        PdfWriter.getInstance(document, new FileOutputStream(new File("tasks/reverse_answer.pdf")));

        document.open();

        Font mainFont = FontFactory.getFont(FontFactory.HELVETICA);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        Chunk titleChunk = new Chunk("Reverse task answers\n\n", boldFont);

        Paragraph paragraph = new Paragraph();
        paragraph.add(titleChunk);

        for (int counter = 1; counter <= automatonsNumber; counter++) {
            Chunk optionChunk = new Chunk("Option " + counter + "\n\n", boldFont);
            paragraph.add(optionChunk);

            Chunk generatedDFAChunk = new Chunk("Initial DFA\n", mainFont);
            paragraph.add(generatedDFAChunk);

            Automaton generatedAut = generatedAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, generatedAut);

            Chunk reverseNFAChunk = new Chunk("Reversed DFA\n", mainFont);
            paragraph.add(reverseNFAChunk);

            NFAutomaton reverseNfAut = reverseNfAutsList.get(counter - 1);
            writeNfAutTable(mainFont, paragraph, reverseNfAut);

            Chunk reverseDFAChunk = new Chunk("Transformed NFA to DFA\n", mainFont);
            paragraph.add(reverseDFAChunk);

            Automaton reverseAut = reverseAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, reverseAut);

            Chunk minimizedDFAChunk = new Chunk("Minimized DFA\n", mainFont);
            paragraph.add(minimizedDFAChunk);

            Automaton minimizedAut = minimizedAutsList.get(counter - 1);
            writeAutTable(mainFont, paragraph, minimizedAut);
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
