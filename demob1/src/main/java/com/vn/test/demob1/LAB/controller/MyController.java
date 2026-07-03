package com.vn.test.demob1.LAB.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * LAB 3 - Bài 2: phân quyền truy cập cho các phương thức bằng @PreAuthorize
 */
@Controller
public class MyController {

    @GetMapping({"/", "/poly/url0"})
    public String method0(Model model) {
        model.addAttribute("message", "/poly/url0 => method0()");
        return "page";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/poly/url1")
    public String method1(Model model) {
        model.addAttribute("message", "/poly/url1 => method1()");
        return "page";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/poly/url2")
    public String method2(Model model) {
        model.addAttribute("message", "/poly/url2 => method2()");
        return "page";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/poly/url3")
    public String method3(Model model) {
        model.addAttribute("message", "/poly/url3 => method3()");
        return "page";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/poly/url4")
    public String method4(Model model) {
        model.addAttribute("message", "/poly/url4 => method4()");
        return "page";
    }

    @RequestMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "Bạn không có quyền truy cập tài nguyên này!");
        return "access-denied";
    }
}
