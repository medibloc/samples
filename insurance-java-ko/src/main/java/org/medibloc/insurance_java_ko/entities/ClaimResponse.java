package org.medibloc.insurance_java_ko.entities;

public class ClaimResponse {
    /** 성공 여부 */
    private boolean success;
    /** 성공 메시지 */
    private String message;
    /** 청구번호 */
    private String claimNumber;
    /** 청구일 */
    private String claimDate;
    /** 청구 상태 */
    private String claimStatus;
    /** 보험 계약 코드 */
    private String insuranceCode;
    /** 보험금 수령 은행 코드 */
    private String accountBankCode;
    /** 보험금 수령 은행 명 */
    private String accountBankName;
    /** 보험금 수령 계좌 번호 */
    private String accountNumber;
    /** 보험금 수령 계좌주 명 */
    private String accountHolder;

    /** 성공 여부 */
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /** 성공 메시지 */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /** 청구번호 */
    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    /** 청구일 */
    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    /** 청구 상태 */
    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    /** 보험 계약 코드 */
    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
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
}
