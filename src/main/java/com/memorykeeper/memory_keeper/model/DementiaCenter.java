package com.memorykeeper.memory_keeper.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class DementiaCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnterNm; // 치매센터명
    private String rdnmadr; // 소재지 도로명 주소
    private String lnmadr;  // 소재지 지번 주소
    private double latitude; // 위도
    private double longitude; // 경도
    private String operPhoneNumber; // 운영기관 전화번호
    private String imbcltyIntrcn; // 주요 치매관리프로그램 소개
    @OneToMany(mappedBy = "dementiaCenter", cascade = CascadeType.ALL)
    private Set<UserDementiaCenterMapping> users;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnterNm() {
        return cnterNm;
    }

    public void setCnterNm(String cnterNm) {
        this.cnterNm = cnterNm;
    }

    public String getRdnmadr() {
        return rdnmadr;
    }

    public void setRdnmadr(String rdnmadr) {
        this.rdnmadr = rdnmadr;
    }

    public String getLnmadr() {
        return lnmadr;
    }

    public void setLnmadr(String lnmadr) {
        this.lnmadr = lnmadr;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOperPhoneNumber() {
        return operPhoneNumber;
    }

    public void setOperPhoneNumber(String operPhoneNumber) {
        this.operPhoneNumber = operPhoneNumber;
    }

    public String getImbcltyIntrcn() {
        return imbcltyIntrcn;
    }

    public void setImbcltyIntrcn(String imbcltyIntrcn) {
        this.imbcltyIntrcn = imbcltyIntrcn;
    }

    public Set<UserDementiaCenterMapping> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDementiaCenterMapping> users) {
        this.users = users;
    }
}
