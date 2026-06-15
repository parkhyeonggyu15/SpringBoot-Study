package com.example.demo.stock.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartCorpCodeService {

    @Value("${OpenDartKey}")
    private String openDartKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // KRX 종목코드(6자리) -> DART 고유번호(8자리)
    private final Map<String, String> stockCodeToCorpCode = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            loadCorpCodeMapping();
            log.info(">>> DART 고유번호(corp_code) 매핑 로드 완료. 총 {}건", stockCodeToCorpCode.size());
        } catch (Exception e) {
            log.error(">>> DART 고유번호 매핑 로드 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * KRX 종목코드(예: 005930)에 대응하는 DART corp_code(예: 00126380)를 반환합니다.
     * 매핑이 없으면 null을 반환합니다.
     */
    public String getCorpCode(String stockCode) {
        return stockCodeToCorpCode.get(stockCode);
    }

    private void loadCorpCodeMapping() throws Exception {
        URI uri = UriComponentsBuilder.fromUriString("https://opendart.fss.or.kr/api/corpCode.xml")
                .queryParam("crtfc_key", openDartKey)
                .build()
                .toUri();

        ResponseEntity<byte[]> response =
                restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, byte[].class);

        byte[] zipBytes = response.getBody();
        if (zipBytes == null || zipBytes.length == 0) {
            throw new IllegalStateException("corpCode.xml 응답이 비어있습니다. (crtfc_key 확인 필요)");
        }

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".xml")) {
                    parseCorpCodeXml(zis);
                    break;
                }
            }
        }
    }

    private void parseCorpCodeXml(InputStream xmlStream) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(xmlStream);

        String corpCode = null;
        String stockCode = null;
        String currentTag = null;

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    currentTag = reader.getLocalName();
                    if ("list".equals(currentTag)) {
                        corpCode = null;
                        stockCode = null;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (currentTag != null) {
                        String text = reader.getText().trim();
                        if (!text.isEmpty()) {
                            if ("corp_code".equals(currentTag)) {
                                corpCode = text;
                            } else if ("stock_code".equals(currentTag)) {
                                stockCode = text;
                            }
                        }
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if ("list".equals(reader.getLocalName())) {
                        // stock_code가 빈 값인 비상장사는 제외
                        if (stockCode != null && !stockCode.isBlank() && corpCode != null) {
                            stockCodeToCorpCode.put(stockCode, corpCode);
                        }
                    }
                    currentTag = null;
                    break;
            }
        }
        reader.close();
    }
}