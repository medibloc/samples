package org.medibloc.hospital_java_ko;

class Patient {
    private String patientNo;
    private String patientName;
    private String RRN; // 주민등록번호
    private String blockchainAddress;
    private String nonce;

    public Patient(String patientNo, String patientName, String RRN) {
        this.patientNo = patientNo;
        this.patientName = patientName;
        this.RRN = RRN;
    }

    public String getPatientNo() {
        return patientNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getRRN() {
        return RRN;
    }

    public String getBlockchainAddress() {
        return blockchainAddress;
    }

    public void setBlockchainAddress(String blockchainAddress) {
        this.blockchainAddress = blockchainAddress;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
