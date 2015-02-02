package volo.voloCalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;
import volo.voloCalendar.service.ForecastingLogic;


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
}
