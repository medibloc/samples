package org.medibloc.insurance_java_ko.entities;

import org.medibloc.phr.ClaimDataV1.Claim;

public class ClaimRequest {
    /** 보험 계약 코드 */
    private String insuranceCode;
    /** 사고 유형(질병/상해/교통사고) */
    private String accidentType;
    /** 사고일 */
    private String accidentDate;
    /** 사고 내용 */
    private String accidentDetail;
    /** 보험금 수령 은행 코드 */
    private String accountBankCode;
    /** 보험금 수령 은행 명 */
    private String accountBankName;
    /** 보험금 수령 계좌 번호 */
    private String accountNumber;
    /** 보험금 수령 계좌주 명 */
    private String accountHolder;
    /** 보험청구 진행 알림 방법 */
    private String informType;
    /** 의료급여 수급권자 여부 */
    private String isMedicalCareRecipient;
    /** 의료급여 수급권자 구분 */
    private int medicalCareRecipientType;
    /** 청구 데이터 transaction hash */
    private String claimTxHash;
    /** 청구 데이터 */
    private Claim claim;

    /** 보험 계약 코드 */
    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    /** 사고 유형(질병/상해/교통사고) */
    public String getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(String accidentType) {
        this.accidentType = accidentType;
    }

    /** 사고일 */
    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    /** 사고 내용 */
    public String getAccidentDetail() {
        return accidentDetail;
    }

    public void setAccidentDetail(String accidentDetail) {
        this.accidentDetail = accidentDetail;
    }

    /** 보험금 수령 은행 코드 */
    public String getAccountBankCode() {
        return accountBankCode;
    }

    public void setAccountBankCode(String accountBankCode) {
        this.accountBankCode = accountBankCode;
    }

    /** 보험금 수령 은행 명 */
    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    /** 보험금 수령 계좌 번호 */
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /** 보험금 수령 계좌주 명 */
    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    /** 보험청구 진행 알림 방법 */
    public String getInformType() {
        return informType;
    }

    public void setInformType(String informType) {
        this.informType = informType;
    }

    /** 의료급여 수급권자 여부 */
    public String getIsMedicalCareRecipient() {
        return isMedicalCareRecipient;
    }

    public void setIsMedicalCareRecipient(String isMedicalCareRecipient) {
        this.isMedicalCareRecipient = isMedicalCareRecipient;
    }

    /** 의료급여 수급권자 구분 */
    public int getMedicalCareRecipientType() {
        return medicalCareRecipientType;
    }

    public void setMedicalCareRecipientType(int medicalCareRecipientType) {
        this.medicalCareRecipientType = medicalCareRecipientType;
    }

    /** 청구 데이터 transaction hash */
    public String getClaimTxHash() {
        return claimTxHash;
    }

    public void setClaimTxHash(String claimTxHash) {
        this.claimTxHash = claimTxHash;
    }

    /** 청구 데이터 */
    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}
