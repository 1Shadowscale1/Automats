package pdf.ReverseTask;

import algorithms.ReverseAutomatonsGenerator;
import automaton.Automaton;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import pdf.PDFCreator;
import pdf.PDFGenerator;
import pdf.PDFVersion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFReverseGenerator implements PDFGenerator {
    private final int options;
    private final int verticesNumber;
    private final int lettersNumber;

    public PDFReverseGenerator(int options, int verticesNumber, int lettersNumber) {
        this.options = options;
        this.verticesNumber = verticesNumber;
        this.lettersNumber = lettersNumber;
    }

    private PdfPTable generateReversePDFTable(Automaton opt) {
        int columnsAmount = opt.letters.size() + 1;
        PdfPTable table = new PdfPTable(columnsAmount);
        table.setWidthPercentage(70);
        table.addCell("");
        for (String letter: opt.letters) {
            table.addCell(letter);
        }
        for (String vertice: opt.vertices) {
            table.addCell(vertice);
            for (String letter: opt.letters) {
                table.addCell(opt.getJumpByVertexAndLetter(vertice, letter));
            }
        }

        return table;
    }

    public void generateFile(PDFVersion version) throws IOException {
        Document document = new Document();
        String absolutePath = new File("reverseTask.pdf").getAbsolutePath();
        PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
        document.open();
        ReverseAutomatonsGenerator generator = new ReverseAutomatonsGenerator(options, verticesNumber, lettersNumber);
        generator.generateReverseAutomatons();
        document.add(generateReversePDFTable(generator.generatedAutsList.get(0)));
        if (version == PDFVersion.TEACHER) {
            document.add(generateReversePDFTable(generator.generatedAutsList.get(1)));
            document.add(generateReversePDFTable(generator.generatedAutsList.get(2)));
        }
        document.close();

    }
}
