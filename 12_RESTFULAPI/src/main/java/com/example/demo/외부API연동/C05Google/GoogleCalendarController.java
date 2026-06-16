package com.example.demo.외부API연동.C05Google;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * ======================================================================
 *  GoogleCalendarController - 구글 캘린더 (RestTemplate + Bearer 토큰 패턴)
 *
 *  ★ 인증을 따로 하지 않는다. GoogleLoginController 의 static 토큰
 *    (GoogleLoginController.googleTokenResponse) 을 그대로 재사용한다.
 *    → 사용자는 /google/login 한 번만 하면 캘린더까지 사용 가능.
 *    (단, 로그인 scope 에 calendar 권한이 포함되어 있어야 한다 - GoogleLoginController 참고)
 * ======================================================================
 *
 *  [기술 문서 DOC LINK]
 *  - OAuth 2.0 웹서버 인증 흐름   : https://developers.google.com/identity/protocols/oauth2/web-server
 *  - 캘린더 API scope            : https://developers.google.com/identity/protocols/oauth2/scopes#calendar
 *  - 이벤트 추가(events.insert)   : https://developers.google.com/calendar/api/v3/reference/events/insert
 *  - 이벤트 삭제(events.delete)   : https://developers.google.com/calendar/api/v3/reference/events/delete
 *  - 이벤트 목록(events.list)     : https://developers.google.com/calendar/api/v3/reference/events/list
 *  - Event 리소스(필드 설명)      : https://developers.google.com/calendar/api/v3/reference/events
 *
 *  ⚠️ Google Cloud Console 에서 "Google Calendar API" 를 사용 설정(Enable)해야 한다.
 */

@Controller
@Slf4j
@RequestMapping("/google/cal")
public class GoogleCalendarController {

    private String CALENDAR_ID="=";

    //POST
    /**
     * 이벤트 추가 (cal.html 모달 폼 POST: date / title / desc)
     *    DOC: https://developers.google.com/calendar/api/v3/reference/events/insert
     *    ENDPOINT: POST https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events
     *    ※ 종일(all-day) 일정은 end.date 가 "배타적(exclusive)" → 시작일 +1 일.
     */
    @PostMapping
    public void post(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @RequestParam("summary") String summary,
            @RequestParam("description") String description
    ){
        log.info("POST /google/cal....{},{},{},{}",start,end,summary,description);

        //요청파라미터 정리
        String url = "https://www.googleapis.com/calendar/v3/calendars/"+CALENDAR_ID+"/events";

//        //요청 헤더 (https://developers.google.com/identity/protocols/oauth2/web-server?hl=ko#callinganapi)
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+GoogleLoginController.googleTokenResponse.getAccess_token());
        header.add("Content-Type","application/json");


        JSONObject startJSON = new JSONObject();
        startJSON.put("date",start.toLocalDate().toString());
        JSONObject endJSON = new JSONObject();
        endJSON.put("date",end.toLocalDate().toString());

        JSONObject events = new JSONObject();
        events.put("summary",summary);
        events.put("description",description);
        events.put("start",startJSON);
        events.put("end",endJSON);

        System.out.println(events);

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< JSONObject > entity = new HttpEntity<>(events,header);
//
//        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println("RESPONSE : " + response.getBody());


    }

    //REMOVE
    /**
     * 이벤트 삭제
     *    DOC: https://developers.google.com/calendar/api/v3/reference/events/delete
     *    ENDPOINT: DELETE https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events/{eventId}
     */

    //LIST
    /**
     * 이벤트 목록 조회 (JSON 그대로 반환 - 확인용)
     *    DOC: https://developers.google.com/calendar/api/v3/reference/events/list
     *    ENDPOINT: GET https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events
     */

}
