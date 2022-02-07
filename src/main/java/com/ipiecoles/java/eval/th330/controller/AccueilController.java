package com.ipiecoles.java.eval.th330.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccueilController {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/"
    )
    public ModelAndView getArtistsNumber(final ModelMap modelMap){
        ModelAndView modelAndView = new ModelAndView("accueil");

        return modelAndView;
    }
}
