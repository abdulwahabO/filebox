package io.github.abdulwahabo.filebox.web;

import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.FileStorageService;
import io.github.abdulwahabo.filebox.services.UserService;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private CacheManager cacheManager;
    private UserService userService;
    private FileStorageService fileStorageService;

    private final String COOKIE_NAME = "filebox-session";
    private final String COOKIE_CACHE = "cookie_cache";

    @Autowired
    public UserController(CacheManager cacheManager, UserService userService, FileStorageService fileStorageService) {
        this.cacheManager = cacheManager;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    public ModelAndView home() {

        return null;
    }

    @PostMapping("/upload")
    public ModelAndView upload(MultipartFile file) {
        // todo: Google
        return null;
    }

    @GetMapping("/activity")
    public ModelAndView activity(HttpServletRequest request, ModelMap model) {
        Optional<String> userOpt = checkForUser(request.getCookies());
        if (userOpt.isPresent()) {
            User user = userService.get(userOpt.get());
            model.addAttribute("user", user.getEmail());
            model.addAttribute("events", user.getEvents());
            return new ModelAndView("activity_log", model);
        } else {
            model.addAttribute("message", "You need to login first");
            return new ModelAndView("redirect:/login", model);
        }
    }

    private Optional<String> checkForUser(Cookie[] cookies) {
        for (int i = 0; i < cookies.length - 1; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equalsIgnoreCase(COOKIE_NAME)) {
                String token = cookie.getValue();
                Cache cache = cacheManager.getCache(COOKIE_CACHE);
                Cache.ValueWrapper wrapper = cache.get(token);
                if (wrapper != null) {
                    String email = (String) wrapper.get();
                    return Optional.of(email);
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

}
