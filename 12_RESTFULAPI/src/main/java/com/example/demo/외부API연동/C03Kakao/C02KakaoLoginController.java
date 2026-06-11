package com.example.demo.외부API연동.C03Kakao;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequestMapping("/Kakao")
public class C02KakaoLoginController {

    private String CLIENT_ID="내 토큰";
    private String REDIRECT_URI="http://192.168.5.13:8080/Kakao/callback";
    private String LOGOUT_REDIRECT_URI="http://192.168.5.13:8080/Kakao/login";

    private KakaoTokenResponse kakaoTokenResponse;
    private KakaoProfileResponse kakaoProfileResponse;
    private KakaoFriendsResponse kakaoFriendsResponse;

    @GetMapping("/login")
    public String login(){
        log.info("GET /login...");
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code";
    }
    @GetMapping("/callback")
    public String callback(String code){
        log.info("GET /Kakao/callback..." + code);

        //요청파라미터 정리
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID);
        params.add("redirect_uri",REDIRECT_URI);

        params.add("code",code);

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoTokenResponse> response =
        restTemplate.exchange(url, HttpMethod.POST,entity,KakaoTokenResponse.class);

//        System.out.println(response.getBody());
        this.kakaoTokenResponse = response.getBody();
        return "redirect:/Kakao";
    }

    //Endpoint : /kakao
    @GetMapping
    public String main(Model model){
        log.info("GET /Kakao..." + this.kakaoTokenResponse);

        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v2/user/me";

//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
//        params.add("","");
//        params.add("","");
//        params.add("","");
//        params.add("","");

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer " +kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity entity = new HttpEntity(header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoProfileResponse> response = restTemplate.exchange(url, HttpMethod.GET,entity,KakaoProfileResponse.class);
        System.out.println(response.getBody());
        this.kakaoProfileResponse = response.getBody();


        model.addAttribute("kakaoProfileResponse",this.kakaoProfileResponse);
        model.addAttribute("profile_images",kakaoProfileResponse.getProperties().getProfile_image());
        return "Kakao/main";
    }


    @GetMapping("/logout1")
    public String logout1(){
        //DOCS : https://developers.kakao.com/docs/ko/kakaologin/rest-api#logout
        log.info("GET /logout1");

        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v1/user/logout";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("","");

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);

        return "redirect:/Kakao/login";
    }
    @GetMapping("/logout2")
    public String logout2(){
        log.info("GET /logout2");
        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v1/user/unlink";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("","");

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        return "redirect:/Kakao/login";
    }
    @GetMapping("/logout3")
    public String logout3(){
        log.info("GET /logout3");

        return "redirect:https://kauth.kakao.com/oauth/logout?client_id="+CLIENT_ID+"&logout_redirect_uri="+LOGOUT_REDIRECT_URI;
    }
    @GetMapping("/getMessageCode")
    public String getMessageCode(){
        log.info("GET /Kakao/getMessageCode...");
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code&scope=talk_message,friends";
    }

    @GetMapping("/message/me/{message}")
    @ResponseBody
    public void message_me(@PathVariable String message){
        log.info("GET /Kakao/message/me...",message);
        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        JSONObject template_object = new JSONObject();
        template_object.put("object_type","text");
        template_object.put("text",message);
        template_object.put("link",new JSONObject());
        template_object.put("button_title",".");

        params.add("template_object",template_object.toJSONString());


        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
    }

    @GetMapping("/friends")
    @ResponseBody
    public void getFriends(){
        log.info("GET /Kakao/friends");

        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v1/api/talk/friends";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();


        //요청 헤더z
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoFriendsResponse> response = restTemplate.exchange(url, HttpMethod.GET,entity,KakaoFriendsResponse.class);
        System.out.println(response.getBody());
        this.kakaoFriendsResponse = response.getBody();

    }

    @GetMapping("/message/friends/{message}")
    @ResponseBody
    public void message_friends(@PathVariable String message){
        log.info("GET /Kakao/message/friends...",message);
        //요청파라미터 정리
        String url = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";

        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();

        String[] uuids = kakaoFriendsResponse.getElements()
                                                .stream()
                                                .map((el ->{return el.getUuid();} ))
                                                .collect(Collectors.toList())
                                                .toArray(String[]::new);
        JSONArray receiver_uuids = new JSONArray();
        for(String uuid : uuids){
            receiver_uuids.add(uuid);
        }
        params.add("receiver_uuids",receiver_uuids.toString());

        JSONObject template_object = new JSONObject();
        template_object.put("object_type","text");
        template_object.put("text",message);
        template_object.put("link",new JSONObject());
        template_object.put("button_title",".");

        params.add("template_object",template_object.toJSONString());


        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 엔티티(헤더 + 바디(params))
        HttpEntity<MultiValueMap<String,Object>> entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
    }
    //-------------------------------------------
    //Kakao ACCESS TOKEN CLASS
    //-------------------------------------------
    @Data
    private static class KakaoTokenResponse{
        public String access_token;
        public String token_type;
        public String refresh_token;
        public int expires_in;
        public String scope;
        public int refresh_token_expires_in;
    }
    //-------------------------------------------
    //Kakao PROFLIE CLASS
    //-------------------------------------------
    @Data
    private static class KakaoAccount{
        public boolean profile_nickname_needs_agreement;
        public boolean profile_image_needs_agreement;
        public Profile profile;
        public boolean has_email;
        public boolean email_needs_agreement;
        public boolean is_email_valid;
        public boolean is_email_verified;
        public String email;
    }
    @Data
    private static class Profile{
        public String nickname;
        public String thumbnail_image_url;
        public String profile_image_url;
        public boolean is_default_image;
        public boolean is_default_nickname;
    }
    @Data
    private static class Properties{
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }
    @Data
    private static class KakaoProfileResponse{
        public long id;
        public Date connected_at;
        public Properties properties;
        public KakaoAccount kakao_account;
    }

    //-------------------------------------------
    //Kakao FRIENDS CLASS
    //-------------------------------------------
    @Data
    private static class Element{
        public String profile_nickname;
        public String profile_thumbnail_image;
        public boolean allowed_msg;
        public long id;
        public String uuid;
        public boolean favorite;
    }

    @Data
    private static class KakaoFriendsResponse{
        public ArrayList<Element> elements;
        public int total_count;
        public Object after_url;
        public int favorite_count;
    }

}
