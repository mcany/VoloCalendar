package volo.voloCalendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Emin Guliyev on 29/09/2014.
 */
@Controller
public class MainController {
    @RequestMapping(value = {"/{symbolicName:^(?!static).+}/**"}, method = RequestMethod.GET, produces = {"text/html","application/xhtml+xml","application/xml"})
    public String main(@PathVariable String symbolicName){
        return "main";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET, produces = {"text/html","application/xhtml+xml","application/xml"})
    public String index(){
        return "main";
    }
}
