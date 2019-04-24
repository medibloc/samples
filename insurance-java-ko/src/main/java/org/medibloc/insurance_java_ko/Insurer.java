package org.medibloc.insurance_java_ko;

import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.crypto.ECKeyPair;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Insurer {
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
}
