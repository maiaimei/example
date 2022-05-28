package cn.maiaimei.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class I18nController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/i18n")
    public String i18n(@RequestParam(required = false, defaultValue = "zh_CN") String l) {
        Locale locale;
        switch (l) {
            case "zh_CN": {
                locale = Locale.CHINA;
                break;
            }
            case "en": {
                locale = Locale.ENGLISH;
                break;
            }
            default: {
                locale = Locale.getDefault();
                break;
            }
        }
        String username = messageSource.getMessage("login.username", null, locale);
        String password = messageSource.getMessage("login.password", null, locale);
        String button = messageSource.getMessage("login.button", null, locale);
        log.info("username={}, password={}, button={}", username, password, button);
        return String.format("username=%s, password=%s, button=%s", username, password, button);
    }
}
