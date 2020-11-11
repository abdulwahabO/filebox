package io.github.abdulwahabo.filebox.web;

import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.util.CacheHelper;
import io.github.abdulwahabo.filebox.services.UserService;

import io.github.abdulwahabo.filebox.util.Constants;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private UserService userService;
    private CacheHelper cacheHelper;


    @Autowired
    public UserController(UserService userService, CacheHelper cacheHelper) {
        this.userService = userService;
        this.cacheHelper = cacheHelper;
    }

    @GetMapping("/")
    public ModelAndView home(HttpServletRequest request, ModelMap model) {
        Optional<String> userOpt = checkForUser(request.getCookies());
        if (userOpt.isPresent()) {
            User user = userService.get(userOpt.get());
            model.addAttribute("user", user.getEmail());
            model.addAttribute("files", user.getFiles());
            return new ModelAndView("home", model);
        } else {
            model.addAttribute("message", "You need to login first");
            return new ModelAndView("redirect:/login", model);
        }
    }

    @PostMapping("/upload")
    public ModelAndView upload(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile multipartFile,
            ModelMap model) throws FileUploadException {

        Optional<String> userOpt = checkForUser(request.getCookies());
        if (userOpt.isPresent()) {
            User user = userService.addFile(multipartFile, userOpt.get());
            model.addAttribute("user", user.getEmail());
            model.addAttribute("files", user.getFiles());
            return new ModelAndView("redirect:/", model);
        } else {
            model.addAttribute("message", "You need to login first");
            return new ModelAndView("redirect:/login", model);
        }
    }

    private Optional<String> checkForUser(Cookie[] cookies) {
        for (int i = 0; i < cookies.length - 1; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equalsIgnoreCase(Constants.COOKIE_NAME)) {
                String token = cookie.getValue();
                Optional<Object> objectOptional = cacheHelper.get(Constants.COOKIE_CACHE, token);
                if (objectOptional.isPresent()) {
                    String email = (String) objectOptional.get();
                    return Optional.of(email);
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
