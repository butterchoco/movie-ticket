import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {
    public static void main(String[] args) {

        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());
            //setting font family, color
            Font font = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.RED);
            doc.open();
            Paragraph para = new Paragraph("FASILKOM THEATRE", font);
            doc.add(para);
            doc.close();
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}