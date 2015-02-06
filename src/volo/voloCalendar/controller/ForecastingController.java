package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;
import volo.voloCalendar.service.ForecastingLogic;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Locale;


/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin/forecasting")
public class ForecastingController {
    @Autowired
    public ForecastingLogic forecastingLogic;
    @RequestMapping(value = "/manualForecasting", method = RequestMethod.GET, produces = "application/json")
    public ManualForecasting manualForecasting() {
        return forecastingLogic.getManualForecasting();
    }

    @RequestMapping(value = "/manualForecasting", method = RequestMethod.POST, produces = "application/json")
    public void manualForecasting(@RequestBody ManualForecasting manualForecasting) {
        forecastingLogic.setManualForecasting(manualForecasting);
    }

    @RequestMapping(value = "/updateDatabase/{year}/{month}", method = RequestMethod.GET, produces = "application/json")
    public boolean updateDatabase(@PathVariable int year,@PathVariable int month) {
        java.util.Date date = new java.util.Date(year, month, 1);
        return forecastingLogic.updateDatabase(date);
    }

    @RequestMapping(value = "/deleteOutliers/{year}/{month}", method = RequestMethod.POST)
    public int deleteOutliers(@PathVariable int year,@PathVariable int month, @RequestBody double sigma) throws IOException {
        Date date = new Date(year - 1900, month, 1);
        return forecastingLogic.deleteOutliers(sigma, date);
    }

    @RequestMapping(value = "/calculateForecasting/{year}/{month}", method = RequestMethod.GET, produces = "application/json")
    public boolean calculateForecasting(@PathVariable int year,@PathVariable int month) {
        Date date = new Date(year - 1900, month, 1);
        forecastingLogic.calculateForecasting(date);
        return true;
    }
}
