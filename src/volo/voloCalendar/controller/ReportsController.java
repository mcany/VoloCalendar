package volo.voloCalendar.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import volo.voloCalendar.service.ReportLogic;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Emin Guliyev on 19/01/2015.
 */
@Controller
@RequestMapping("/admin/reporting")
@Secured({"ROLE_ADMIN"})
public class ReportsController {
    @Autowired
    public ReportLogic reportLogic;
    @RequestMapping(value = "/monthly/{year}/{month}", method = RequestMethod.GET)
    public void monthlyReport(@PathVariable int year,@PathVariable int month, Locale locale
            ,HttpServletResponse response) throws IOException {
        String path = reportLogic.monthlyReport(year, month);
        returnFile(response, path, "monthlyReport.pdf");
    }

    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    public void overviewReport(Locale locale
            ,HttpServletResponse response) throws IOException {
        String path = reportLogic.overviewReport();
        returnFile(response, path, "overviewReport.pdf");
    }

    private void returnFile(HttpServletResponse response, String path, String fileName) throws IOException {
        File file = new File(path);
        final ServletOutputStream outputStream = response.getOutputStream();
        if (file.exists()){
            try {
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
                response.setContentLength((int)file.length());
                final FileInputStream inputStream = new FileInputStream(file);
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } finally {
                FileUtils.forceDelete(file);
            }

        }else{
            throw new RuntimeException();
        }
    }
}
