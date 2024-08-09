package com.memorykeeper.memory_keeper.dto;

public class DementiaCenterDTO {
    private String cnterNm;
    private String rdnmadr;
    private String lnmadr;
    private double latitude;
    private double longitude;
    private String operPhoneNumber;
    private String imbcltyIntrcn;

    // Getters and Setters
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
}

