package algorithms;

import automaton.Automaton;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateReversePDF {
    private final int options;
    private final int verticesNumber;
    private final int lettersNumber;

    public GenerateReversePDF(int options, int verticesNumber, int lettersNumber) {
        this.options = options;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
    }

    public Document createPDF(String fileName) throws FileNotFoundException {
        try(Document document = new Document()) {
            String absolutePath = new File(fileName + ".pdf").getAbsolutePath();
            PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
            return document;
        }
    }

    public PdfPTable fillAutomatonPDFTable(Automaton opt) {
        opt.vertices.sort(String::compareTo);
        opt.letters.sort(String::compareTo);
        int columnsAmount = opt.letters.size() + 1;
        PdfPTable table = new PdfPTable(columnsAmount);
        table.setWidthPercentage(70);
        table.addCell(" ");
        for (int i = 0; i < columnsAmount - 1; i++)
            table.addCell(opt.letters.get(i));

        for (int j = 0; j < opt.letters.size(); j++) {
            String vertex_j = opt.vertices.get(j);
            table.addCell(vertex_j);
            for(int k = 0; k < columnsAmount; k++ ){
                table.addCell(opt.getJumpByVertexAndLetter(vertex_j, opt.letters.get(k)));
            }
        }
        return table;
    }

    public void generateReverseTaskPDF() throws IOException {
        int i = 0;
        Document document = createPDF("reverseTask");
        ReverseAutomatonsGenerator generator = new ReverseAutomatonsGenerator(options, verticesNumber, lettersNumber);
        generator.generateReverseAutomatons();
        document.open();
        document.add(fillAutomatonPDFTable(generator.generatedAutsList.get(0)));
        document.close();

    }

    public void generateReverseAnswerPDF() throws IOException {
        int i = 0;
        Document document = createPDF("reverseAnswer");
        document.open();

    }
}
