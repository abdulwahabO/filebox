package io.github.abdulwahabo.filebox.web;

import io.github.abdulwahabo.filebox.exceptions.FileDeleteException;
import io.github.abdulwahabo.filebox.exceptions.FileDownloadException;
import io.github.abdulwahabo.filebox.exceptions.FileUploadException;
import io.github.abdulwahabo.filebox.exceptions.UserNotFoundException;
import io.github.abdulwahabo.filebox.model.File;
import io.github.abdulwahabo.filebox.model.User;
import io.github.abdulwahabo.filebox.services.FileStorageService;
import io.github.abdulwahabo.filebox.util.CacheHelper;
import io.github.abdulwahabo.filebox.services.UserService;
import io.github.abdulwahabo.filebox.util.Constants;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private UserService userService;
    private CacheHelper cacheHelper;
    private FileStorageService fileStorageService;

    @Autowired
    public HomeController(UserService userService, CacheHelper cacheHelper, FileStorageService fileStorageService) {
        this.userService = userService;
        this.cacheHelper = cacheHelper;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public ModelAndView home(HttpServletRequest request, ModelMap model) throws UserNotFoundException {
        Optional<String> emailOpt = checkForUser(request.getCookies());
        if (emailOpt.isPresent()) {
            User user = userService.get(emailOpt.get());
            model.addAttribute("user", user.getEmail());
            model.addAttribute("files", user.getFiles());
            return new ModelAndView("home", model);
        } else {
            model.addAttribute("message", "You need to login first");
            return new ModelAndView("github_login", model);
        }
    }

    @PostMapping(value = "/upload")
    public ModelAndView upload(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile multipartFile,
            ModelMap model) throws FileUploadException {

        Optional<String> emailOpt = checkForUser(request.getCookies());
        if (emailOpt.isPresent()) {
            User user = userService.addFile(multipartFile, emailOpt.get());
            model.addAttribute("user", user.getEmail());
            model.addAttribute("files", user.getFiles());
            return new ModelAndView("redirect:/", model);
        } else {
            model.addAttribute("message", "You need to login first");
            return new ModelAndView("github_login", model);
        }
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download(
            HttpServletRequest request,
            @RequestParam("storage_id") String storageID) throws FileDownloadException, UserNotFoundException {

        Optional<String> emailOpt = checkForUser(request.getCookies());
        String email = emailOpt.orElseThrow(() -> new FileDownloadException("User is not authenticated"));

        User user = userService.get(email);
        Optional<File> fileOpt = user.getFiles()
                                     .stream()
                                     .filter(f -> f.getStorageID().equalsIgnoreCase(storageID))
                                     .findFirst();

        File file = fileOpt.orElseThrow(() -> new FileDownloadException("No file matches the given storage ID"));
        byte[] bytes = fileStorageService.download(file.getStorageID());
        Resource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                             .body(resource);
    }

    @GetMapping("/delete")
    public ModelAndView delete(
            HttpServletRequest request,
            @RequestParam("storage_id") String storageID, ModelMap model) throws UserNotFoundException,
            FileDeleteException {

        Optional<String> emailOpt = checkForUser(request.getCookies());
        String email = emailOpt.orElseThrow(() -> new FileDeleteException("User is not authenticated"));

        User user = userService.get(email);
        Optional<File> fileOpt = user.getFiles()
                                     .stream()
                                     .filter(f -> f.getStorageID().equalsIgnoreCase(storageID))
                                     .findFirst();

        File file = fileOpt.orElseThrow(() -> new FileDeleteException("No file matches the given storage ID"));

        boolean isDeleted = fileStorageService.delete(file.getStorageID());
        if (isDeleted) {
            model.addAttribute("message", "File deleted successfully!");
        } else {
            model.addAttribute("message", "Failed to delete file");
        }
        model.addAttribute("user", user.getEmail());
        model.addAttribute("files", user.getFiles());
        return new ModelAndView("home", model);
    }

    @GetMapping("/logout")
    public ModelAndView signout(HttpServletRequest request, HttpServletResponse response) {
        removeCookie(request.getCookies(), response);
        return new ModelAndView("github_login");
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

    private void removeCookie(Cookie[] cookies, HttpServletResponse response) {
        for (int i = 0; i < cookies.length - 1; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equalsIgnoreCase(Constants.COOKIE_NAME)) {
                String token = cookie.getValue();
                cacheHelper.remove(Constants.COOKIE_CACHE, token);
                Cookie expiredCookie = new Cookie(Constants.COOKIE_NAME, token);
                cookie.setMaxAge(0);
                response.addCookie(expiredCookie);
                return;
            }
        }
    }

    // TODO: write a global exception handler, return an error page.. log exception. ??
    // TODO: Write 500 and 404 pages... map errors there.
}
