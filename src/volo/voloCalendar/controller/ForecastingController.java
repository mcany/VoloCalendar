package volo.voloCalendar.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import volo.voloCalendar.model.ManualForecasting;
import volo.voloCalendar.service.Backend;


/**
 * Created by Emin Guliyev on 15/12/2014.
 */
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin/forecasting")
public class ForecastingController {
    @RequestMapping(value = "/manualForecasting", method = RequestMethod.GET, produces = "application/json")
    public ManualForecasting manualForecasting() {
        return Backend.getManualForecasting();
    }

    @RequestMapping(value = "/manualForecasting", method = RequestMethod.POST, produces = "application/json")
    public void createUser(@RequestBody ManualForecasting manualForecasting) {
        Backend.setManualForecasting(manualForecasting);
    }
}
