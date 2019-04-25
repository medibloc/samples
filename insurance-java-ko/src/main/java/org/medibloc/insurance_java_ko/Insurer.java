package org.medibloc.insurance_java_ko;

import com.google.protobuf.ByteString;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.core.HttpService;
import org.medibloc.panacea.core.Panacea;
import org.medibloc.panacea.core.protobuf.BlockChain;
import org.medibloc.panacea.core.protobuf.Rpc;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.utils.Numeric;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Insurer {
    // MediBloc relay 서버 사용 시 BLOCKCHAIN_URL 을 medibloc relay 서버로 설정합니다.
    private static final String BLOCKCHAIN_URL = "https://stg-testnet-node.medibloc.org";
    private static final String ACCOUNT_REQUEST_TYPE_TAIL = "tail";

    private static final String MNEMONIC = "parade letter awesome much spread popular shed release holiday blind nation sunny";
    private static final BigInteger PRIVATE_KEY = new BigInteger("cf4c72d797e87dab70e63f34a266e5b085b01abc17a3c43299b1f78442dde316", 16);
    // address: 028f684cb16d7f53a58c9f2d24e9dd0315e13c33e446c8bb3a50369d6395014a1e
    private static final BigInteger PUBLIC_KEY = new BigInteger("8f684cb16d7f53a58c9f2d24e9dd0315e13c33e446c8bb3a50369d6395014a1e49d4a3bb91b38c9ec33569e097ebdb8e8bea4ff44a8d9d8ca179c0df768857d0", 16);
    private static final String PASSWORD = "insurerPassWord123!";

    private static final String ACCOUNT_FILE_PATH = "sample_accounts";

    private List<UserEntity> userList;

    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Insurer() throws Exception {
        userList = new ArrayList<UserEntity>();
        userList.add(new UserEntity("00000000", "ㅇㅇㅇ", "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
        userList.add(new UserEntity("11111111", "ㅁㅁㅁ", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"));
        userList.add(new UserEntity("12345678", "홍길동", "136a78e6v7awe8arw71ver89es17vr8a9ws612vr78es1vr7a8691v7res74164sa7ver68asv6sb87r9h6tg9a2"));

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

        System.out.println("보험사 - 새로운 account 를 생성하여 파일에 저장 하였습니다. 파일명 : " + savedFilePath);

        // 저장된 account 파일로부터 account 정보를 읽고, 읽은 account 를 보험사의 account 로 설정 합니다.
        setAccount(AccountUtils.loadAccount(savedFilePath));

        System.out.println("보험사 - 초기화를 완료 하였습니다. Blockchain address: " + this.account.getAddress());
    }

    /**
     * CI 로 사용자 ID 를 조회하여, 해당 사용자 정보에 블록체인 account 를 등록 합니다.
     */
    public void signUp(Certificate certificate, String certificateTxHash) {
        // tx 의 인증서 hash 와 일치 여부 확인
        if (isUploadedOnBlockchain(certificate, certificateTxHash) != true) {
            throw new RuntimeException("주어진 인증서가 해당 transaction 에 기록 되어 있지 않습니다.");
        }

        // 사용자 ID 와 블록체인 account 연계
        UserEntity user = findUserWithCi(certificate.getCertification().getPersonCi());
        if (user != null) {
            user.setBlockchainAddress(certificate.getBlockchainAddress());
        } else {
            throw new RuntimeException("CI 가" + certificate.getCertification().getPersonCi() + " 인 사용자 정보를 찾을 수 없습니다.");
        }
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

    private UserEntity findUserWithCi(String ci) {
        for (UserEntity user: this.userList) {
            if (ci.equals(user.getCi())) {
                return user;
            }
        }
        return null;
    }

    private UserEntity findUserWithBlockchainAddress(String address) {
        for (UserEntity user: this.userList) {
            if (address.equals(user.getBlockchainAddress())) {
                return user;
            }
        }
        return null;
    }
}
