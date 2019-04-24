package org.medibloc.insurance_java_ko;

public class UserEntity {
    private String userNo;
    private String userName;
    private String ci; // 주민등록번호
    private String blockchainAddress;

    public UserEntity(String userNo, String userName, String ci) {
        this.userNo = userNo;
        this.userName = userName;
        this.ci = ci;
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
}
