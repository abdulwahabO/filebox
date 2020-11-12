package io.github.abdulwahabo.filebox.web;

import io.github.abdulwahabo.filebox.exceptions.AuthenticationException;
import io.github.abdulwahabo.filebox.exceptions.GithubUserNotFoundException;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private GithubAuthService githubAuthService;
    private CacheHelper cacheHelper;

    @Autowired
    public AuthController(GithubAuthService githubAuthService, CacheHelper cacheHelper) {
        this.githubAuthService = githubAuthService;
        this.cacheHelper = cacheHelper;
    }

    @GetMapping(Constants.OAUTH_CALLBACK_URL)
    public ModelAndView githubCallback(
            @RequestParam Map<String, String> params,
            ModelMap modelMap,
            HttpServletResponse response) throws AuthenticationException, GithubUserNotFoundException {

        String state = params.get("state");
        String code = params.get("code");
        Optional<Object> optional = cacheHelper.get(Constants.OAUTH_STATE_CACHE, state);

        if (optional.isEmpty()) {
            return new ModelAndView("redirect:/login", modelMap);
        }

        GithubAccessTokenDto accessToken = githubAuthService.accesstoken(code);
        GithubUserDto githubUserDto = githubAuthService.getGithubUser(accessToken.getToken());

        // TODO: this cookie might not yet be in browser during redirect to home.
        // TODO: alternative: return a login success page first, so cookies get to browser ???

        // TODO: to handle logout, write another cookie with age=0; Any need to handle logout?

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
