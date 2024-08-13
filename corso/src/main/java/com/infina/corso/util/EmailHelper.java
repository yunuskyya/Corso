package com.infina.corso.util;

import com.infina.corso.dto.request.RegisterManagerRequest;
import com.infina.corso.model.User;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.MailService;
import com.infina.corso.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailHelper {

    private final MailService mailService;
    private final TokenService tokenService;

    private final String changePasswordUrl = "http://localhost:5173/login";
    private final String resetPasswordUrl = "http://localhost:5173/set-password";
    private final UserRepository userRepository;

    public void sendTokenEmail(String to, String token) {
        String subject = "CORSO Hesap Aktivasyonu";
        String activationUrl = resetPasswordUrl + "?token=" + token;
        String text = String.format(
                "Merhaba,\n\n" +
                        "Şifrenizi sıfırlamak için lütfen aşağıdaki bağlantıyı kullanın:\n" +
                        "%s\n\n" +
                        "Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.\n\n" +
                        "Saygılarımızla,\n" +
                        "Corso",
                activationUrl
        );
        mailService.sendSimpleMessage(to, subject, text);
    }

    public void sendRegistrationEmail(RegisterManagerRequest request) {
        String subject = "Hesabınız Başarıyla Oluşturuldu";
        String changePasswordUrl = "http://localhost:5173/login";

        String text = String.format(
                "Merhaba %s %s,\n\n" +
                        "Hesabınız başarıyla oluşturulmuştur. Aşağıda hesabınız için giriş bilgilerinizi bulabilirsiniz:\n\n" +
                        "Kullanıcı Adı: %s\n" +
                        "Şifre: %s\n\n" +
                        "Hesabınıza giriş yaparak şifenizi değiştirmenizi öneririz." +
                        "Giriş için aşağıdaki bağlantıyı kullanabilirsiniz:\n" +
                        "%s\n\n" +
                        "Saygılarımızla,\n" +
                        "Corso",
                request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), changePasswordUrl
        );

        mailService.sendSimpleMessage(request.getEmail(), subject, text);
    }

    public void sendAccountLockedEmail(User user) {
        String to = user.getEmail();
        String subject = "Hesabınız Kilitlenmiştir";
        String text = "Merhaba " + user.getFirstName() + ",\n\n" +
                "Hesabınız, 5 başarısız giriş denemesi nedeniyle geçici olarak kilitlenmiştir. Hesabınızı tekrar kullanabilmek için lütfen daha sonra tekrar deneyin veya destek ekibimizle iletişime geçin.\n\n" +
                "Saygılarımızla,\n" +
                "Corso";
        mailService.sendSimpleMessage(to, subject, text);
    }
    public void sendPasswordChangeNotification(User user) {
        String to = user.getEmail();
        String subject = "Şifre Değiştirme Bilgilendirmesi";
        String text = "Merhaba " + user.getFirstName() + ",\n\n" +
                "Şifreniz başarıyla değiştirilmiştir. Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.\n\n" +
                "Saygılarımızla,\n" +
                "Corso";
        mailService.sendSimpleMessage(to, subject, text);
    }
}
