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
}
