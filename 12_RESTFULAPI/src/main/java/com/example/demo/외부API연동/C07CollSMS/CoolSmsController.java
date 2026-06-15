package com.example.demo.외부API연동.C07CollSMS;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/*
 * 솔라피(CoolSMS) 문자(SMS) 전송 실습
 * - 개인 회원가입 + 본인 휴대폰으로 발신번호 등록 시 사업자 없이 테스트 가능(가입 시 무료 충전금 지급)
 *
 * [공식 문서]
 * - 콘솔(키 발급)   : https://console.solapi.com/credentials
 * - 인증(HMAC)      : https://developers.solapi.com/references/authentication/
 * - 단건 발송 API   : https://developers.solapi.com/references/messages/sendManyDetail
 *
 * [확인 방법]
 *   API_KEY / API_SECRET / FROM(등록된 발신번호) 만 채운 뒤
 *   http://localhost:8099/coolsms/send?to=01012345678&text=hello
 */
@RestController
@Slf4j
@RequestMapping("/coolsms")
public class CoolSmsController {

    private String API_KEY = "";       // 솔라피 API Key
    private String API_SECRET = "";    // 솔라피 API Secret
    private String FROM = "01054339484";          // 사전 등록된 발신번호 (예: 01012345678)

    private final String SEND_URL = "https://api.solapi.com/messages/v4/send";

    // 문자 1건 전송
    // 문서: https://developers.solapi.com/references/messages/sendManyDetail
    @GetMapping("/send")
    public ResponseEntity<String> send(
            @RequestParam("to") String to,
            @RequestParam("text") String text
    ) throws Exception {
        log.info("GET /coolsms/send...to : " + to + ", text : " + text);

        RestTemplate restTemplate = new RestTemplate();

        // 1) HMAC-SHA256 인증 헤더 구성 (date + salt 를 secret 으로 서명)
        // 문서: https://developers.solapi.com/references/authentication/
        String date = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        String salt = UUID.randomUUID().toString().replace("-", "");
        String signature = hmacSha256(date + salt, API_SECRET);

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        header.add("Authorization",
                "HMAC-SHA256 apiKey=" + API_KEY
                        + ", date=" + date
                        + ", salt=" + salt
                        + ", signature=" + signature);

        // 2) 요청 바디 { "message": { to, from, text } }
        JSONObject message = new JSONObject();
        message.put("to", to);
        message.put("from", FROM);
        message.put("text", text);

        JSONObject params = new JSONObject();
        params.put("message", message);

        HttpEntity<String> entity = new HttpEntity<>(params.toJSONString(), header);

        // 3) 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(SEND_URL, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());

        return response;
    }

    // HMAC-SHA256 서명(hex) 생성 유틸
    private String hmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}