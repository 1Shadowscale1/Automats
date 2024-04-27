package pdf;

import java.io.IOException;

public interface PDFGenerator {
    void generateFile(PDFVersion version) throws IOException;
}
