package ru.deathman.rpgportal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ru.deathman.rpgportal.core.service.api.MessagesService;

import java.util.Locale;

/**
 * @author Виктор
 */
@Service
public class MessagesServiceImpl implements MessagesService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
