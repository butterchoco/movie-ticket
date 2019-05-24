package com.adpro.purchase.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/payment")
    public String resume(@RequestParam(name = "visitor", required = false)
                                 String name, Model model) {
        model.addAttribute("visitor", name);
        if (!name.equals("")) {
            String pageTitle = name + ", I hope you interested to hire me";
            model.addAttribute("pageTitle", pageTitle);
        } else {
            model.addAttribute("pageTitle", "This is my CV");
        }

        return "payment";
    }
}
