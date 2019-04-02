package org.medibloc.hospital_java_ko;

import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.core.protobuf.Rpc;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1.Certification;
import org.medibloc.phr.ClaimDataV1.Claim;

import java.math.BigInteger;

public class User {
    private static final String MNEMONIC = "rate knife faculty sting still festival village between base disease violin device";
    private static final BigInteger PRIVATE_KEY = new BigInteger("6957772055e3f3587db5cbb5802dc67d8aa4bef5335ab4ee61ff7f5601fc89a7", 16);
    // address: 03107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507
    private static final BigInteger PUBLIC_KEY = new BigInteger("107c5eae25e0443be09496162362fee885402379ee4c0fca30af8dbaa340e507933890e0c8f931351a9a37d7a151d1e8d9620b55adbe7a5e8663a4cea843f887", 16);
    private static final String PASSWORD = "userPassWord123!";

    private Account account;
    private String residentRegistrationNumber = "750101-1234567";

    private Certificate certificate;
    private String certificateTxHash;

    private Claim claim;
    private Rpc.SendTransactionRequest claimTxRequest;

    public User() throws Exception {
        ECKeyPair keyPair = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);
        this.account = AccountUtils.createAccount(PASSWORD, keyPair, null);

        System.out.println("사용자 - 초기화를 완료 하였습니다. Blockchain address: " + this.account.getAddress());
    }

    public String getAddress() {
        return this.account.getAddress();
    }

    public String getResidentRegistrationNumber() {
        return this.residentRegistrationNumber;
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

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public void setClaimTxRequest(Rpc.SendTransactionRequest claimTxRequest) {
        this.claimTxRequest = claimTxRequest;
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
}
