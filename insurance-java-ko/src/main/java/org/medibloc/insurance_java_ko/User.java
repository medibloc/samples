package org.medibloc.insurance_java_ko;

import org.medibloc.insurance_java_ko.entities.InsuranceEntity;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.crypto.AES256CTR;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.crypto.Keys;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1.Certification;

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
        // claim data 에 해당하는 사고일.
        String accidentDate = "20190101";

        String sharedSecretKey = Keys.getSharedSecretKey(getPrivateKey(), insurerBlockchainAddress);
        return AES256CTR.encryptData(sharedSecretKey, accidentDate);
    }
}
