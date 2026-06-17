package com.example.demo.stock.Service;

import com.example.demo.stock.Entity.CorpCodeMapping;
import com.example.demo.stock.Repository.CorpCodeMappingRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final CorpCodeMappingRepository corpCodeMappingRepository;

    // KRX 종목코드(6자리) -> DART 고유번호(8자리)
    private final Map<String, String> stockCodeToCorpCode = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            if (corpCodeMappingRepository.count() > 0) {
                loadCorpCodeMappingFromDb();
                log.info(">>> DART corp_code 매핑을 DB에서 로드했습니다. 총 {}건", stockCodeToCorpCode.size());
            } else {
                loadCorpCodeMappingFromDart();
                log.info(">>> DART corp_code 매핑을 외부 API에서 가져와 DB에 저장했습니다. 총 {}건", stockCodeToCorpCode.size());
            }
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

    private void loadCorpCodeMappingFromDb() {
        corpCodeMappingRepository.findAll()
                .forEach(mapping -> stockCodeToCorpCode.put(mapping.getStockCode(), mapping.getCorpCode()));
    }

    private void loadCorpCodeMappingFromDart() throws Exception {
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

        List<CorpCodeMapping> mappings = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".xml")) {
                    parseCorpCodeXml(zis, mappings);
                    break;
                }
            }
        }

        corpCodeMappingRepository.saveAll(mappings);
        for (CorpCodeMapping mapping : mappings) {
            stockCodeToCorpCode.put(mapping.getStockCode(), mapping.getCorpCode());
        }
    }

    private void parseCorpCodeXml(InputStream xmlStream, List<CorpCodeMapping> mappings) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(xmlStream);

        String corpCode = null;
        String corpName = null;
        String stockCode = null;
        String currentTag = null;

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    currentTag = reader.getLocalName();
                    if ("list".equals(currentTag)) {
                        corpCode = null;
                        corpName = null;
                        stockCode = null;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (currentTag != null) {
                        String text = reader.getText().trim();
                        if (!text.isEmpty()) {
                            if ("corp_code".equals(currentTag)) {
                                corpCode = text;
                            } else if ("corp_name".equals(currentTag)) {
                                corpName = text;
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
                            mappings.add(CorpCodeMapping.builder()
                                    .stockCode(stockCode)
                                    .corpCode(corpCode)
                                    .corpName(corpName)
                                    .build());
                        }
                    }
                    currentTag = null;
                    break;
            }
        }
        reader.close();
    }
}
