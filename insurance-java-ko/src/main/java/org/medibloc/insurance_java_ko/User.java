package org.medibloc.insurance_java_ko;

import org.medibloc.insurance_java_ko.entities.ClaimRequest;
import org.medibloc.insurance_java_ko.entities.InsuranceEntity;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.crypto.AES256CTR;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.crypto.Keys;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1.Certification;
import org.medibloc.phr.ClaimDataV1.*;
import org.medibloc.phr.ClaimDataV1Utils;

import java.math.BigInteger;
import java.util.List;

public class User {
    private static final String MNEMONIC = "rate knife faculty sting still festival village between base disease violin device";
    private static final BigInteger PRIVATE_KEY = new BigInteger("6957772055e3f3587db5cbb5802dc67d8aa4bef5335ab4ee61ff7f5601fc89a7", 16);
    // address: 03107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507
    private static final BigInteger PUBLIC_KEY = new BigInteger("107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507933890e0c8f931351a9a37d7a151d1e8d9620b55adbe7a5e8663a4cea843f887", 16);
    private static final String PASSWORD = "userPassWord123!";

    private ECKeyPair ecKeyPair;
    private Account account;

    private Certificate certificate;
    private String certificateTxHash;

    private List<InsuranceEntity> insuranceEntityList;

    private String getPrivateKey() {
        return PRIVATE_KEY.toString(16);
    }

    public String getAddress() {
        return this.account.getAddress();
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Certificate getCertificate() {
        return this.certificate;
    }

    public void setCertificateTxHash(String certificateTxHash) {
        this.certificateTxHash = certificateTxHash;
    }

    public String getCertificateTxHash() {
        return this.certificateTxHash;
    }

    public List<InsuranceEntity> getInsuranceEntityList() {
        return insuranceEntityList;
    }

    /** 사고일 당시 계약상태였던 보험 목록을 설정 합니다. */
    public void setInsuranceEntityList(List<InsuranceEntity> insuranceEntityList) {
        this.insuranceEntityList = insuranceEntityList;
    }

    public User() throws Exception {
        ecKeyPair = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);
        this.account = AccountUtils.createAccount(PASSWORD, ecKeyPair, null);

        System.out.println("사용자 - 초기화를 완료 하였습니다. Blockchain address: " + this.account.getAddress());
    }

    public Certification.Builder certify() {
        return Certification.newBuilder()
                .setCertificationResult("success")
                .setPersonName("홍길동")
                .setPersonBirthdate("19750101")
                .setPersonGender("1")
                .setPersonNation("0")
                .setPersonCi("136a78e6v7awe8arw71ver89es17vr8a9ws612vr78es1vr7a8691v7res74164sa7ver68asv6sb87r9h6tg9a2")
                .setPersonMobileCompany("ABC")
                .setPersonMobileNumber("01012345678");
    }

    public String getEncryptedAccidentDate(String insurerBlockchainAddress) throws Exception {
        // claim data 에 해당하는 사고일(청구데이터 중 최초진료일)
        String accidentDate = "99999999";
        for (Receipt receipt : getClaim().getReceiptsList()) {
            if (receipt.getTreatmentStartDate().compareTo(accidentDate) < 0) {
                accidentDate = receipt.getTreatmentStartDate();
            }
        }

        String sharedSecretKey = Keys.getSharedSecretKey(getPrivateKey(), insurerBlockchainAddress);
        return AES256CTR.encryptData(sharedSecretKey, accidentDate);
    }

    public String getEncryptedClaimRequest(String insurerBlockchainAddress) throws Exception {
        ClaimRequest request = new ClaimRequest();
        request.setInsuranceCode(getInsuranceEntityList().get(0).getInsuranceCode());
        request.setAccidentType("disease");
        request.setAccidentDate("20181206");
        request.setAccidentDetail("결장염");
        request.setAccountBankCode("0023");
        request.setAccountBankName("국민은행");
        request.setAccountNumber("1234567890");
        request.setAccountHolder("홍길동");
        request.setInformType("sms");
        request.setIsMedicalCareRecipient(true);
        request.setMedicalCareRecipientType(1);
        request.setClaimTxHash("84d64213b4f27a915f29957b996d92972bae95973cb6d4ba64d32ab6cb9bcb93");
        request.setClaim(getClaim());

        String jsonRequest = request.toString(); // TODO
        String sharedSecretKey = Keys.getSharedSecretKey(getPrivateKey(), insurerBlockchainAddress);
        return AES256CTR.encryptData(sharedSecretKey, jsonRequest);
    }

    private Claim getClaim() {
        /*** Claim ***/
        Claim.Builder claimBuilder = Claim.newBuilder();
        claimBuilder.setClaimNo("20181204-S1284");

        /*** Claim.Receipts ***/
        Receipt.Builder receiptBuilder = Receipt.newBuilder();
        receiptBuilder.setReceiptNo("20181204-S1284");
        receiptBuilder.setReceiptType("I");
        receiptBuilder.setPatientNo("12345678");
        receiptBuilder.setPatientName("홍길동");
        receiptBuilder.setCompanyRegistrationNo("11100999");
        receiptBuilder.setTreatmentStartDate("2018-12-06");
        receiptBuilder.setTreatmentEndDate("2018-12-06");
        receiptBuilder.setTreatmentDepartment("피부과");
        receiptBuilder.setTreatmentDepartmentCode("DER");
        receiptBuilder.setTreatmentType("");
        receiptBuilder.setTreatmentTypeCode("");
        receiptBuilder.setCoveredFee("11000");
        receiptBuilder.setUncoveredFee("20000");
        receiptBuilder.setUpperLimitExcess("0");
        receiptBuilder.setPayTotal("31000");
        receiptBuilder.setPatientPayTotal("21000");
        receiptBuilder.setDeductAmount("0");
        receiptBuilder.setAdvancePayAmount("0");
        receiptBuilder.setPayAmount("21000");
        receiptBuilder.setUncollectedPayAmount("0");
        receiptBuilder.setReceiptAmount("21000");
        receiptBuilder.setSurtaxAmount("0");
        receiptBuilder.setCashPayAmount("0");
        receiptBuilder.setCardPayAmount("21000");

        /*** Claim.Receipt.FeeItems ***/
        receiptBuilder.addFeeItems(FeeItem.newBuilder()
                .setFeeItemName("초진 진찰료")
                .setFeeItemCode("")
                .setTreatmentDate("2018-12-06")
                .setCoveredType("")
                .setMedicalChargeCode("AA157")
                .setPrice("11000")
                .setQuantity("1")
                .setRepeatNumber("1")
                .setFeeTotal("11000")
                .setCoveredPatientFee("1000")
                .setCoveredInsuranceFee("10000")
                .setCoveredPatientAllFee("0")
                .setUncoveredChosenFee("0")
                .setUncoveredUnchosenFee("0"));
        receiptBuilder.addFeeItems(FeeItem.newBuilder()
                .setFeeItemName("검사료")
                .setFeeItemCode("")
                .setTreatmentDate("2018-12-06")
                .setCoveredType("")
                .setMedicalChargeCode("BB157")
                .setPrice("20000")
                .setQuantity("1")
                .setRepeatNumber("1")
                .setFeeTotal("20000")
                .setCoveredPatientFee("0")
                .setCoveredInsuranceFee("0")
                .setCoveredPatientAllFee("0")
                .setUncoveredChosenFee("20000")
                .setUncoveredUnchosenFee("0"));

        /*** Claim.Diagnoses ***/
        Diagnosis.Builder diagnosisBuilder1 = Diagnosis.newBuilder();
        diagnosisBuilder1.setDiagnosisCodeVersion("ICD-10-2016");
        diagnosisBuilder1.setDiagnosisCodeType(10); // 주상병
        diagnosisBuilder1.setDiagnosisCode("J00");

        Diagnosis.Builder diagnosisBuilder2 = Diagnosis.newBuilder();
        diagnosisBuilder2.setDiagnosisCodeVersion("KCD-7");
        diagnosisBuilder2.setDiagnosisCodeType(20); // 부상병
        diagnosisBuilder2.setDiagnosisCode("J30.3");

        Claim.Builder partialClaim = claimBuilder
                .addReceipts(receiptBuilder)
                .addDiagnoses(diagnosisBuilder1)
                .addDiagnoses(diagnosisBuilder2);

        return ClaimDataV1Utils.fillClaim(partialClaim);
    }
}
