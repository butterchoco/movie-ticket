package ticket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PdfController {

    @RequestMapping(value = "/generate/pdf.htm", method = RequestMethod.GET)
    ModelAndView generatePdf(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        System.out.println("Calling generatePdf()...");

//        Employee employee = new Employee();
//        employee.setFirstName("Yashwant");
//        employee.setLastName("Chavan");

        ModelAndView modelAndView = new ModelAndView("pdfView", "command",employee);

        return modelAndView;
    }

}
