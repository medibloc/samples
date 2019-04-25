package org.medibloc.insurance_java_ko.entities;

import java.util.List;

public class UserEntity {
    private String userNo;
    private String userName;
    private String ci; // 주민등록번호
    private String blockchainAddress;
    private List<InsuranceEntity> insuranceEntityList;

    public UserEntity(String userNo, String userName, String ci) {
        this.userNo = userNo;
        this.userName = userName;
        this.ci = ci;
    }

    public UserEntity(String userNo, String userName, String ci, List<InsuranceEntity> insuranceEntityList) {
        this(userNo, userName, ci);
        this.insuranceEntityList = insuranceEntityList;
    }

    public String getUserNo() {
        return userNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getCi() {
        return ci;
    }

    public String getBlockchainAddress() {
        return blockchainAddress;
    }

    public void setBlockchainAddress(String blockchainAddress) {
        this.blockchainAddress = blockchainAddress;
    }

    public List<InsuranceEntity> getInsuranceEntityList() {
        return insuranceEntityList;
    }
}
