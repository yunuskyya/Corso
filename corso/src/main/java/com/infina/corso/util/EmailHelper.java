package com.infina.corso.util;

import com.infina.corso.model.User;
import com.infina.corso.service.MailService;
import org.springframework.stereotype.Component;

@Component
public class EmailHelper {

    private final MailService mailService;

    public EmailHelper(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendRegistrationEmail(User user, String rawPassword) {
        String subject = "Kayıt Onayı";
        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Sistemimize başarıyla kayıt oldunuz.\n\n" +
                        "Kullanıcı Adınız: %s\n" +
                        "Geçici Şifreniz: %s\n\n" +
                        "Güvenliğiniz için lütfen şifrenizi değiştirmek için sisteme giriş yapın veya aşağıdaki bağlantıyı kullanın:\n" +
                        "http://localhost:8080/swagger-ui/index.html#/User%%20Management/changePassword\n\n" +
                        "Teşekkürler,\n" +
                        "Infina Corso Ekibi",
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                rawPassword
        );
        mailService.sendSimpleMessage(user.getEmail(), subject, text);
    }

    public void sendActivationEmail(User user) {
        String subject = "Hesap Aktifleştirildi";
        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Hesabınız başarıyla aktifleştirildi.\n\n" +
                        "Teşekkürler,\n" +
                        "Infina Corso Ekibi",
                user.getFirstName(),
                user.getLastName()
        );
        mailService.sendSimpleMessage(user.getEmail(), subject, text);
    }
}
