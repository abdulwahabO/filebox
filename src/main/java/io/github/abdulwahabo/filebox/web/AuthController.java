package io.github.abdulwahabo.filebox.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private CacheManager cacheManager;

    @Autowired
    public AuthController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/auth/callback")
    public ModelAndView githubCallback(@RequestParam Map<String, String> params) {

        String state = params.get("state"); //
        String code = params.get("code");



        return null;
    }

}
