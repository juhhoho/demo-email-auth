package _2024.winter.demoemailauth.service;

import _2024.winter.demoemailauth.config.RedisConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisConfig redisConfig;
    private int authNumber;

    @Value("${spring.mail.username}")
    private String serviceName;

    /* 인증 이메일 전송 */
    public ResponseEntity<String> sendAuthEmail(String customerMail) {
        makeRandomNum();

        // 이메일 제목, 내용
        String title = "[demo-email-auth] 회원 가입 이메일 테스트";
        String content =
                "이메일을 인증하기 위한 절차입니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + " 입니다." +
                        "<br>" +
                        "회원 가입 폼에 해당 번호를 입력해주세요.";
        // 이메일 전송
        mailSend(serviceName, customerMail, title, content);

        // 인증 번호 반환
        return ResponseEntity.ok().body(Integer.toString(authNumber));
    }

    // 이메일로 전송된 인증 번호 확인
    public ResponseEntity<String> checkAuthEmail(String email, String authNum) {
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        String code = valOperations.get(email);
        if (Objects.equals(code, authNum)) {
            return ResponseEntity.ok().body("인증 성공");
        }
        throw new RuntimeException("인증 실패");
    }

    // --------------------------------------------------------------------------------------------------

    /* 랜덤 인증번호 생성 */
    public void makeRandomNum() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content,true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 출력
        }
        // redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        valOperations.set(toMail, Integer.toString(authNumber), 3, TimeUnit.MINUTES);
    }

}