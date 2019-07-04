package org.medibloc.insurance_java_ko;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.medibloc.insurance_java_ko.entities.*;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.crypto.AES256CTR;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.crypto.Keys;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String MNEMONIC = "rate knife faculty sting still festival village between base disease violin device";
    private static final BigInteger PRIVATE_KEY = new BigInteger("6957772055e3f3587db5cbb5802dc67d8aa4bef5335ab4ee61ff7f5601fc89a7", 16);
    // address: 03107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507
    private static final BigInteger PUBLIC_KEY = new BigInteger("107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507933890e0c8f931351a9a37d7a151d1e8d9620b55adbe7a5e8663a4cea843f887", 16);
    private static final String PASSWORD = "userPassWord123!";

    private ECKeyPair ecKeyPair;
    private Account account;

    private Certification certification;
    private String certificateTxHash;

    private List<InsuranceEntity> insuranceEntityList;

    private String getPrivateKey() {
        return PRIVATE_KEY.toString(16);
    }

    public String getAddress() {
        return this.account.getAddress();
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public Certification getCertification() {
        return this.certification;
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

    public Certification certify() {
        return new Certification(
                null
                , -1
                , "홍길동"
                , "19750101"
                , 1
                , "136a78e6v7awe8arw71ver89es17vr8a9ws612vr78es1vr7a8691v7res74164sa7ver68asv6sb87r9h6tg9a2"
                , 1 // SKT
                , "01012345678");
    }

    public String getEncryptedAccidentDate(String insurerBlockchainAddress) throws Exception {
        // claim data 에 해당하는 사고일(청구데이터 중 최초진료일)
        String accidentDate = "99999999";
        for (Receipt receipt : getBill().getReceipts()) {
            if (receipt.getTreatmentStartDate().compareTo(accidentDate) < 0) {
                accidentDate = receipt.getTreatmentStartDate();
            }
        }

        String sharedSecretKey = Keys.getSharedSecretKey(getPrivateKey(), insurerBlockchainAddress);
        return AES256CTR.encryptData(sharedSecretKey, accidentDate);
    }

    /* '사고내용 + 청구서' 를 암호화 하여 반환 합니다. */
    public String getEncryptedClaimRequest(String insurerBlockchainAddress) throws Exception {
        ClaimRequest request = new ClaimRequest(
                getInsuranceEntityList().get(0).getInsuranceCode()
                , "disease"
                , "20181206"
                , "결장염"
                , "0023"
                , "국민은행"
                , "1234567890"
                , "홍길동"
                , "sms"
                , true
                , 1
                , "8a1af4deb9ebd96874523476f9cdba1f3da8ef4f4e5796a1f47dbcd0a0323070"
                , getBill()
        );

        String jsonRequest = new ObjectMapper().writeValueAsString(request);
        String sharedSecretKey = Keys.getSharedSecretKey(getPrivateKey(), insurerBlockchainAddress);
        return AES256CTR.encryptData(sharedSecretKey, jsonRequest);
    }

    private Bill getBill() {
        /* Bill.Receipts.ReceiptItems */
        List<ReceiptItem> receiptItems = new ArrayList<ReceiptItem>();
        receiptItems.add(new ReceiptItem(
                null
                , null
                , null
                , null
                , null
                , null
                , null
        ));

        /* Bill.Receipts.ReceiptEtcItems */
        List<ReceiptEtcItem> receiptEtcItems = new ArrayList<ReceiptEtcItem>();
        receiptEtcItems.add(new ReceiptEtcItem(
           null
                , null
                , null
        ));

        /* Bill.Receipts */
        List<Receipt> receipts = new ArrayList<Receipt>();
        receipts.add(new Receipt(
                null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , receiptItems
                , receiptEtcItems
        ));

        /* Bill.FeeDetail.FeeItems */
        List<FeeItem> feeItems = new ArrayList<FeeItem>();
        feeItems.add(new FeeItem(
                null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
        ));

        /* Bill.FeeDetail */
        FeeDetail feeDetail = new FeeDetail(
                null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
                , null
        );

        /* Bill.diagnoses */
        List<Diagnosis> diagnoses = new ArrayList<Diagnosis>();
        diagnoses.add(new Diagnosis(
                "ICD-10-2016"
                , "10" // 주상병
                , "J00"
        ));
        diagnoses.add(new Diagnosis(
                "KCD-7"
                , "20" // 부상병
                , "J30.3"
        ));

        /*** Bill ***/
        return new Bill("20181204-S1284", receipts, feeDetail, diagnoses);
    }
}
