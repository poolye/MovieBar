package com.tec666.moviebar.controller.page.computer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author longge93
 */
@Controller
public class IndexComController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "computer/computerIndex";
    }

}
