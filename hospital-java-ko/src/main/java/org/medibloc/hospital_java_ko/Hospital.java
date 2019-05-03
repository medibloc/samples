package org.medibloc.hospital_java_ko;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.protobuf.ByteString;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.core.HttpService;
import org.medibloc.panacea.core.Panacea;
import org.medibloc.panacea.core.protobuf.BlockChain;
import org.medibloc.panacea.core.protobuf.Rpc;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.crypto.SecureRandomUtils;
import org.medibloc.panacea.crypto.Sign;
import org.medibloc.panacea.tx.Transaction;
import org.medibloc.panacea.utils.Numeric;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1Utils;
import org.medibloc.phr.ClaimDataV1.*;
import org.medibloc.phr.ClaimDataV1Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private static final String JWT_SECRET_KEY = "medibloc_hospital_secret_key";
    private static final String JWT_ISSUER = "hospital";

    private static final String BLOCKCHAIN_URL = "https://stg-testnet-node.medibloc.org";
    private static final String ACCOUNT_REQUEST_TYPE_TAIL = "tail";

    private static final String MNEMONIC = "canyon roast street knock library amount enter popular sea kidney pupil furnace";
    private static final BigInteger PRIVATE_KEY = new BigInteger("eede9347908b2ac3801828cc3293da19109c0730c47314a694c9acacbb95d3da", 16);
    // address: 02718101c8a565a58bf416c8d30b335e6bb9701d1532e76b38298ef7e252c321cd
    private static final BigInteger PUBLIC_KEY = new BigInteger("718101c8a565a58bf416c8d30b335e6bb9701d1532e76b38298ef7e252c321cd3077389f0517d40faac6d8db45aa81ad86914f995abcbdbdc6e9605a1e46c844", 16);
    private static final String PASSWORD = "hospitalPassWord123!";

    private static final String ACCOUNT_FILE_PATH = "sample_accounts";

    private List<Patient> patientList;
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Hospital 생성자. 병원의 환자 list 와 블록체인 account 를 생성 합니다.
     * 블록체인 account 를 생성, 저장, 불러오는 예제를 기술 합니다.
     */
    public Hospital() throws Exception {
        patientList = new ArrayList<Patient>();
        patientList.add(new Patient("00000000", "ㅇㅇㅇ", "000000-0000000"));
        patientList.add(new Patient("11111111", "ㅁㅁㅁ", "111111-1111111"));
        patientList.add(new Patient("12345678", "홍길동", "750101-1234567"));


        /** 블록체인 account 생성, 저장, 불러오기 **/

        // 새로운 account 를 생성합니다.
        // 옵션이 주어지지 않으면 기본 옵션 값이 설정 됩니다.
        ECKeyPair keyPair = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY); // 기 생성 된 keyPair 이용
        Account newAccount = AccountUtils.createAccount(PASSWORD, keyPair, null);
        // Account newAccount = AccountUtils.createAccount(PASSWORD, null); // 새로운 keyPair 를 생성 하는 경우

        // 생성한 account 를 주어진 경로에 저장합니다.
        // 저장되는 파일명은 "UTC--시간--account주소.json" 형식입니다.
        File savedFile = AccountUtils.saveAccount(newAccount, ACCOUNT_FILE_PATH);
        String savedFilePath = savedFile.getAbsolutePath();

        System.out.println("병원 - 새로운 account 를 생성하여 파일에 저장 하였습니다. 파일명 : " + savedFilePath);

        // 저장된 account 파일로부터 account 정보를 읽고, 읽은 account 를 병원의 account 로 설정 합니다.
        setAccount(AccountUtils.loadAccount(savedFilePath));

        System.out.println("병원 - 초기화를 완료 하였습니다. Blockchain address: " + this.account.getAddress());
    }

    /**
     * 주민등록번호로 환자 ID 를 조회하여, 해당 환자 정보에 블록체인 account 를 등록 합니다.
     * 실제 구현 시, 아래 제시된 예외처리가 모두 포함 되어야 합니다.
     */
    public void mapAccountOntoPatientId(String blockchainAddress, Certificate certificate, String certificateTxHash, String residentRegistrationNumber) {
        // 인증서 블록체인 주소 일치 여부 확인
        if (blockchainAddress.equals(certificate.getBlockchainAddress()) != true) {
            throw new RuntimeException("주어진 블록체인 주소가 인증서의 블록체인 주소와 일치하지 않습니다.");
        }

        // tx 의 인증서 hash 와 일치 여부 확인
        if (isUploadedOnBlockchain(certificate, certificateTxHash) != true) {
            throw new RuntimeException("주어진 인증서가 해당 transaction 에 기록 되어 있지 않습니다.");
        }

        // CI 유효성 확인
        String ci = certificate.getCertification().getPersonCi();
        if (isValidCI(ci, residentRegistrationNumber) != true) {
            throw new RuntimeException("주어진 CI 는 해당 주민등록번호의 CI 가 아닙니다.");
        }

        // 환자 ID 와 블록체인 account 연계
        Patient patient = findPatientWithRRN(residentRegistrationNumber);
        if (patient != null) {
            patient.setBlockchainAddress(blockchainAddress);
        } else {
            throw new RuntimeException("주민등록번호가 " + residentRegistrationNumber + " 인 환자 정보를 찾을 수 없습니다.");
        }
    }

    /**
     * 로그인 시 환자가 사용할 nonce 값을 생성하여 반환 합니다.
     * 이후 환자가 nonce 값에 서명한 결과를 검증 하기 위해, 환자에게 전달한 nonce 값을 저장 합니다.
     */
    public String getSignInNonce(String patientBlockchainAddress) {
        Patient patient = findPatientWithBlockchainAddress(patientBlockchainAddress);

        String nonce = Numeric.byteArrayToHex(SecureRandomUtils.generateRandomBytes(32));
        patient.setNonce(nonce);

        return nonce;
    }

    /**
     * 환자가 nonce 값에 서명한 결과를 검증 하고, 검증에 성공했다면 JWT(Json Web Token) 를 반환 합니다.
     * 환자는 이 함수를 호출하여 받은 JWT 를 이후 병원과 통신 시 사용 합니다.
     */
    public String getSignInToken(String patientBlockchainAddress, String signature) {
        Patient patient = findPatientWithBlockchainAddress(patientBlockchainAddress);

        boolean isValidSig = Sign.verifySignature(patientBlockchainAddress, patient.getNonce(), signature);

        if (isValidSig) {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            String jwt = JWT.create().withIssuer(JWT_ISSUER).sign(algorithm);
            return jwt;
        } else {
            throw new IllegalArgumentException(patientBlockchainAddress + "의 nonce 값에 대한 signature 가 올바르지 않습니다.");
        }
    }

    /**
     * 주어진 블록체인 address 를 갖는 환자의 진료 청구서를 생성하여 반환 합니다.
     */
    public Claim getClaim(String patientBlockchainAddress, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm).withIssuer(JWT_ISSUER).build().verify(token);
        } catch (JWTVerificationException ex) {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
        }

        Patient patient = findPatientWithBlockchainAddress(patientBlockchainAddress);

        if (patient != null) {
            /*** Claim ***/
            Claim.Builder claimBuilder = Claim.newBuilder();
            claimBuilder.setClaimNo("20181204-S1284");

            /*** Claim.Receipts ***/
            Receipt.Builder receiptBuilder = Receipt.newBuilder();
            receiptBuilder.setReceiptNo("20181204-S1284");
            receiptBuilder.setReceiptType("I");
            receiptBuilder.setPatientNo(patient.getPatientNo());
            receiptBuilder.setPatientName(patient.getPatientName());
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
        } else {
            throw new RuntimeException(patientBlockchainAddress + " 주소를 가진 환자 정보를 찾을 수 없습니다.");
        }
    }

    /**
     * 청구서를 병원의 개인키로 sign 하고, 블록체인에 기록 할 수 있는 transaction 형태로 반환 합니다.
     */
    public Rpc.SendTransactionRequest getSignedTransaction(Claim claim) throws Exception {
        // Blockchain 에 접근하기 위한 panacea client 를 생성 합니다.
        // panacea client 를 이용하여 Blockchain 과 통신 할 때에는 Rpc(Remote Procedure Call) 패키지 내의 클래스가 사용 됩니다.
        Panacea panacea = Panacea.create(new HttpService(BLOCKCHAIN_URL));

        // Blockchain 에 업로드 할 claim hash 값을 구합니다.
        byte[] claimHash = ClaimDataV1Utils.hash(claim);

        // Blockchain 에서 병원 account 의 현재 정보를 조회 합니다.
        Rpc.GetAccountRequest accountRequest = Rpc.GetAccountRequest.newBuilder()
                .setAddress(getAccount().getAddress())
                .setType(ACCOUNT_REQUEST_TYPE_TAIL)
                .build();
        Rpc.Account accountBCInfo = panacea.getAccount(accountRequest).send();
        long nextNonce = accountBCInfo.getNonce() + 1;

        // Blockchain 의 chainId 를 조회 합니다. 또는, 환경 설정 파일에 저장한 chainId 를 이용 할 수도 있습니다.
        Rpc.MedState medState = panacea.getMedState().send();
        int chainId = medState.getChainId();

        // Blockchain 에 기록 할 transaction 을 생성 합니다. 생성된 transaction 은 hash 및 sign 의 대상이 됩니다.
        BlockChain.TransactionHashTarget transactionHashTarget
                = Transaction.getAddRecordTransactionHashTarget(claimHash, getAccount().getAddress(), nextNonce, chainId);

        // transactionHashTarget 을 hash 하고, 개인키로 sign 합니다. 주어진 account 와 비밀번호는 개인키를 복호화 하는 데 사용 됩니다.
        Rpc.SendTransactionRequest transactionRequest = Transaction.getSignedTransactionRequest(transactionHashTarget, getAccount(), PASSWORD);

        // 생성한 transaction request 를 반환 합니다.
        // 사용자는 이 transaction 을 블록체인에 등록 하여 이후 진본증명 시 이용 합니다.
        return transactionRequest;
    }

    /**
     * 병원의 개인키로 sign 한 청구서를 블록체인에 기록 합니다.
     */
    public String sendClaim(Rpc.SendTransactionRequest transactionRequest) throws Exception {
        Panacea panacea = Panacea.create(new HttpService(BLOCKCHAIN_URL));
        Rpc.TransactionHash resultHash = panacea.sendTransaction(transactionRequest).send();
        return resultHash.getHash();
    }

    /**
     * certificateTxHash 로 블록체인에서 transaction 을 조회 하고,
     * 조회 한 transaction 에 기록 된 hash 값과 주어진 인증서의 hash 값이 일치 하는 지 여부를 반환 합니다.
     */
    private boolean isUploadedOnBlockchain(Certificate certificate, String certificateTxHash) {
        // 주어진 인증서의 hash 깂
        BlockChain.AddRecordPayload certificateHashPayload = BlockChain.AddRecordPayload.newBuilder()
                .setHash(ByteString.copyFrom(CertificateDataV1Utils.hash(certificate)))
                .build();
        String certificateHash = Numeric.toHexStringNoPrefix(certificateHashPayload.toByteArray());

        try {
            Panacea panacea = Panacea.create(new HttpService(BLOCKCHAIN_URL));
            Rpc.Transaction transaction = panacea.getTransaction(certificateTxHash).send();

            // 블록체인에 기록 된 인증서 hash 값
            String certificateHashOnBlockchain = transaction.getPayload();

            if (certificateHashOnBlockchain == null || certificateHashOnBlockchain.isEmpty()) {
                throw new RuntimeException("Transaction payload is empty.");
            }

            return certificateHash.equals(certificateHashOnBlockchain);
        } catch (IOException ex) {
            throw new RuntimeException("Can not find the transaction " + certificateTxHash, ex);
        }
    }

    /**
     * 인증기관과 통신하여 주어진 ci 가 주어진 주민등록번호의 ci 가 맞는 지 확인 합니다.
     */
    private boolean isValidCI(String ci, String residentRegistrationNumber) {
        // TODO - sample 사용 기관에서 구현
        return true;
    }

    private Patient findPatientWithRRN(String rrn) {
        for (Patient p: this.patientList) {
            if (rrn.equals(p.getRRN())) {
                return p;
            }
        }
        return null;
    }

    private Patient findPatientWithBlockchainAddress(String address) {
        for (Patient p: this.patientList) {
            if (address.equals(p.getBlockchainAddress())) {
                return p;
            }
        }
        return null;
    }
}
