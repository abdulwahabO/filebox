package io.github.abdulwahabo.filebox.web;

import io.github.abdulwahabo.filebox.exceptions.AuthenticationException;
import io.github.abdulwahabo.filebox.exceptions.UserCreateException;
import io.github.abdulwahabo.filebox.exceptions.UserNotFoundException;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.UserService;
import io.github.abdulwahabo.filebox.services.dto.GithubAccessTokenDto;
import io.github.abdulwahabo.filebox.services.dto.GithubUserDto;
import io.github.abdulwahabo.filebox.util.CacheHelper;
import io.github.abdulwahabo.filebox.services.GithubAuthService;
import io.github.abdulwahabo.filebox.util.Constants;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private GithubAuthService githubAuthService;
    private CacheHelper cacheHelper;
    private UserService userService;

    @Autowired
    public AuthController(GithubAuthService githubAuthService, CacheHelper cacheHelper, UserService userService) {
        this.githubAuthService = githubAuthService;
        this.cacheHelper = cacheHelper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String githubRedirect() {
        return "redirect:" + githubAuthService.redirectUrl();
    }

    @GetMapping(Constants.OAUTH_CALLBACK_URL)
    public ModelAndView githubCallback(
            @RequestParam Map<String, String> params,
            ModelMap modelMap,
            HttpServletResponse response) throws AuthenticationException, UserCreateException {

        String state = params.get("state");
        String code = params.get("code");
        Optional<Object> optional = cacheHelper.get(Constants.OAUTH_STATE_CACHE, state);

        if (optional.isEmpty()) {
            logger.warn("Could not find oauth2 state");
            return new ModelAndView("github_login");
        }

        GithubAccessTokenDto accessToken = githubAuthService.accesstoken(code);
        GithubUserDto githubUserDto = githubAuthService.getGithubUser(accessToken.getToken());

        try {
            User user = userService.get(githubUserDto.getEmail());
            logger.info("New sigin for user " + user.getEmail());
        } catch (UserNotFoundException e) {
            User user = new User();
            user.setEmail(githubUserDto.getEmail());
            user.setName(githubUserDto.getName());
            userService.save(user);
            logger.info("New signup. email: " + githubUserDto.getEmail());
        }

        String token = randomToken();
        Cookie cookie = new Cookie(Constants.COOKIE_NAME, token);
        cookie.setMaxAge(604800); // 7 days
        cookie.setPath("/");
        response.addCookie(cookie);
        cacheHelper.put(Constants.COOKIE_CACHE, token, githubUserDto.getEmail());
        return new ModelAndView("redirect:/", modelMap);
    }

    private String randomToken() {
        Random random = new Random();
        int rnd = random.nextInt(323233);
        String date = LocalDateTime.now().toString();
        return String.format("%s+%s", rnd, date);
    }
}
