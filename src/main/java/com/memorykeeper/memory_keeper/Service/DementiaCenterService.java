package com.memorykeeper.memory_keeper.Service;

import com.memorykeeper.memory_keeper.model.DementiaCenter;
import com.memorykeeper.memory_keeper.model.User;
import com.memorykeeper.memory_keeper.model.UserDementiaCenterMapping;
import com.memorykeeper.memory_keeper.repository.DementiaCenterRepository;
import com.memorykeeper.memory_keeper.repository.UserDementiaCenterMappingRepository;
import com.memorykeeper.memory_keeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DementiaCenterService {

    private static final Logger logger = LoggerFactory.getLogger(DementiaCenterService.class);

    @Autowired
    private DementiaCenterRepository dementiaCenterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDementiaCenterMappingRepository userDementiaCenterMappingRepository;

    @Value("${openapi.serviceKey}")
    private String serviceKey;

    public void fetchAndSaveDementiaCentersFromAPI() {
        try {
            String apiUrl = "http://api.data.go.kr/openapi/tn_pubr_public_imbclty_cnter_api?serviceKey=" + serviceKey + "&pageNo=1&numOfRows=312&type=xml";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            List<DementiaCenter> parsedCenters = new ArrayList<>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String cnterNm = getTagValue("cnterNm", eElement);
                    String rdnmadr = getTagValue("rdnmadr", eElement);
                    String lnmadr = getTagValue("lnmadr", eElement);

                    // 중복 데이터 확인
                    boolean exists = dementiaCenterRepository.existsByCnterNmAndRdnmadrAndLnmadr(cnterNm, rdnmadr, lnmadr);
                    if (exists) {
                        continue; // 이미 존재하는 데이터는 추가하지 않음
                    }

                    DementiaCenter center = new DementiaCenter();
                    center.setCnterNm(cnterNm);
                    center.setRdnmadr(rdnmadr);
                    center.setLnmadr(lnmadr);

                    // 위도와 경도를 파싱하며 '+' 이후의 모든 문자를 제거
                    String latitude = getTagValue("latitude", eElement);
                    String longitude = getTagValue("longitude", eElement);
                    if (latitude != null && longitude != null) {
                        latitude = latitude.split("\\+")[0];
                        longitude = longitude.split("\\+")[0];
                    }
                    center.setLatitude(Double.parseDouble(Objects.requireNonNull(latitude)));
                    center.setLongitude(Double.parseDouble(Objects.requireNonNull(longitude)));

                    center.setOperPhoneNumber(getTagValue("operPhoneNumber", eElement));
                    center.setImbcltyIntrcn(getTagValue("imbcltyIntrcn", eElement));

                    parsedCenters.add(center);
                }
            }

            dementiaCenterRepository.saveAll(parsedCenters);

        } catch (Exception e) {
            logger.error("Error fetching and saving dementia centers", e);
        }
    }


    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if (node != null && node.getFirstChild() != null) {
            return node.getFirstChild().getNodeValue();
        }
        return null;
    }

    public void saveNearbyCentersForUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<DementiaCenter> allCenters = dementiaCenterRepository.findAll();

            for (User user : users) {
                List<DementiaCenter> nearbyCenters = new ArrayList<>();
                for (DementiaCenter center : allCenters) {
                    // Rdnmadr와 Lnmadr가 null인 경우, 빈 문자열로 처리하여 오류 방지
                    String rdnmadr = center.getRdnmadr() != null ? center.getRdnmadr() : "";
                    String lnmadr = center.getLnmadr() != null ? center.getLnmadr() : "";

                    if (rdnmadr.contains(user.getRegion()) || lnmadr.contains(user.getRegion())) {
                        nearbyCenters.add(center);
                    }
                }
                saveCentersForUser(user, nearbyCenters);
            }
        } catch (Exception e) {
            logger.error("Error saving nearby centers for users", e);
        }
    }

    private void saveCentersForUser(User user, List<DementiaCenter> centers) {
        for (DementiaCenter center : centers) {
            UserDementiaCenterMapping mapping = new UserDementiaCenterMapping();
            mapping.setUser(user);
            mapping.setDementiaCenter(center);
            userDementiaCenterMappingRepository.save(mapping);
        }
    }
}