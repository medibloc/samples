package org.medibloc.insurance_java_ko;

public class InsuranceEntity {
    /** 보험상품 코드 */
    private String insuranceCode;
    /** 보험상품 이름 */
    private String insuranceName;
    /** 피보험자 이름 */
    private String insuredName;
    /** 수익자 이름 */
    private String beneficiaryName;

    /** 보험상품 코드 */
    public String getInsuranceCode() {
        return insuranceCode;
    }

    /** 보험상품 이름 */
    public String getInsuranceName() {
        return insuranceName;
    }

    /** 피보험자 이름 */
    public String getInsuredName() {
        return insuredName;
    }

    /** 수익자 이름 */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public InsuranceEntity(String insuranceCode, String insuranceName, String insuredName, String beneficiaryName) {
        this.insuranceCode = insuranceCode;
        this.insuranceName = insuranceName;
        this.insuredName = insuredName;
        this.beneficiaryName = beneficiaryName;
    }
}
