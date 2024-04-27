package pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFCreator {
    public Document createPDF(String fileName) throws FileNotFoundException {
        try(Document document = new Document()) {
            String absolutePath = new File(fileName + ".pdf").getAbsolutePath();
            PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
            return document;
        }
    }
}
