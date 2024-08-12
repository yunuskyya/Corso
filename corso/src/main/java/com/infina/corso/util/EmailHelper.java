package com.infina.corso.util;

import com.infina.corso.model.User;
import com.infina.corso.service.MailService;
import com.infina.corso.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailHelper {

    private final MailService mailService;
    private final TokenService tokenService;

    private final String resetPasswordUrl = "http://localhost:5173/set-password";

    public void sendTokenEmail(String to, String token) {
        String subject = "CORSO Account Activation";
        String activationUrl = resetPasswordUrl + "?token=" + token;
        String text = "To reset your password, please use the following link:" + activationUrl;

        mailService.sendSimpleMessage(to, subject, text);
    }

    public void sendRegistrationEmail(User user) {
        String subject = "Complete Your Registration";
        String text = String.format("To complete your registration, please set your password using the following link: %s?token=%s",
                resetPasswordUrl, user.getResetPasswordToken());
        mailService.sendSimpleMessage(user.getEmail(), subject, text);
    }
    public void sendAccountLockedEmail(User user) {
        String to = user.getEmail();
        String subject = "Hesabınız bloke edildi";
        String text = "Merhaba " + user.getFirstName() + ",\n\n" +
                "Hesabınız 5 başarısız giriş denemesi nedeniyle bloke edilmiştir. Lütfen daha sonra tekrar deneyiniz veya destek ekibimizle iletişime geçiniz.\n\n" +
                "Saygılarımızla,\n" +
                "Infina Corso Ekibi";
        mailService.sendSimpleMessage(to, subject, text);
    }
    public void sendPasswordChangeNotification(User user) {
        String to = user.getEmail();
        String subject = "Şifre Değiştirme Bilgilendirmesi";
        String text = "Merhaba " + user.getFirstName() + ",\n\n" +
                "Şifreniz başarıyla değiştirilmiştir. Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.\n\n" +
                "Saygılarımızla,\n" +
                "Infina Corso Ekibi";
        mailService.sendSimpleMessage(to, subject, text);
    }
}
