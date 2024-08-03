package com.infina.corso.shared;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Bu sınıf, messages.properties dosyasından mesaj almak için kullanılır
 */
public class Messages {
    public static String getMessageForLocale(String key, Locale locale) {
        return ResourceBundle.getBundle("messages", locale).getString(key);
    }

    public static String getMessageForLocale(String messageKey, Locale locale, Object... arguments) {
        String message = getMessageForLocale(messageKey, locale);
        return MessageFormat.format(message, arguments);
    }
}
