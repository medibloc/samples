package org.medibloc.simple_android_java_ko;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.protobuf.util.JsonFormat;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.core.HttpService;
import org.medibloc.panacea.core.Panacea;
import org.medibloc.panacea.core.protobuf.BlockChain;
import org.medibloc.panacea.core.protobuf.Rpc;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.data.Data;
import org.medibloc.panacea.tx.Transaction;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TESTNET_URL = "https://stg-testnet-node.medibloc.org";
    private static final String ACCOUNT_REQUEST_TYPE_TAIL = "tail";

    private static final BigInteger PRIVATE_KEY = new BigInteger("4da8bc28a095870433d8a7d57ca140d6132e722f177c9a94f70a6963b4b8f708", 16);
    // address: 02e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c296
    private static final BigInteger PUBLIC_KEY = new BigInteger("e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c2960da413ededc3216df47f27ba6d46babe0ba54ca35d682182d26a6c6aa63f7930", 16);
    private static final String PASSWORD = "myPassWord123!";
    private static final String UPLOAD_DATA = "MyHealthDataForHashingAndUploading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            System.out.println("Panacea sample 프로그램을 실행합니다.");

            /*** 1. account 생성, 저장 및 불러오기 ***/

            // 새로운 account 를 생성합니다.
            // 옵션이 주어지지 않으면 기본 옵션 값이 설정 됩니다.
            ECKeyPair keyPair = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY); // 기 생성 된 keyPair 이용
            Account account = AccountUtils.createAccount(PASSWORD, keyPair, null);
            // Account newAccount = AccountUtils.createAccount(PASSWORD, null); // 새로운 keyPair 를 생성 하는 경우

            /*** 2. data hash 업로드 ***/

            // Blockchain 에 접근하기 위한 panacea client 를 생성 합니다.
            // panacea client 를 이용하여 Blockchain 과 통신 할 때에는 Rpc(Remote Procedure Call) 패키지 내의 클래스가 사용 됩니다.
            Panacea panacea = Panacea.create(new HttpService(TESTNET_URL));

            // Blockchain 에 업로드 할 data hash 값을 구합니다.
            byte[] dataHash = Data.hashRecord(UPLOAD_DATA);

            // Blockchain 에서 account 의 현재 정보를 조회 합니다.
            Rpc.GetAccountRequest accountRequest = Rpc.GetAccountRequest.newBuilder()
                    .setAddress(account.getAddress())
                    .setType(ACCOUNT_REQUEST_TYPE_TAIL)
                    .build();
            Rpc.Account accountBCInfo = panacea.getAccount(accountRequest).sendAsync().get();
            long nextNonce = accountBCInfo.getNonce() + 1;

            // Blockchain 의 chainId 를 조회 합니다. 또는, 환경 설정 파일에 저장한 chainId 를 이용 할 수도 있습니다.
            Rpc.MedState medState = panacea.getMedState().sendAsync().get();
            int chainId = medState.getChainId();

            // Blockchain 에 등록 할 transaction 을 생성 합니다. 생성된 transaction 은 hash 및 sign 의 대상이 됩니다.
            BlockChain.TransactionHashTarget transactionHashTarget
                    = Transaction.getAddRecordTransactionHashTarget(dataHash, account.getAddress(), nextNonce, chainId);

            // transactionHashTarget 을 hash 하고, 개인키로 sign 합니다. 주어진 account 와 비밀번호는 개인키를 복호화 하는 데 사용 됩니다.
            Rpc.SendTransactionRequest transactionRequest = Transaction.getSignedTransactionRequest(transactionHashTarget, account, PASSWORD);

            System.out.println("블록체인에 새로운 transaction 을 업로드 합니다.\ntransaction : " + JsonFormat.printer().print(transactionRequest));

            // Blockchain 에 transaction 을 업로드 하고 결과를 반환 받습니다.
            Rpc.TransactionHash resultHash = panacea.sendTransaction(transactionRequest).sendAsync().get();

            if (transactionRequest.getHash().equals(resultHash.getHash())) {
                System.out.println("요청한 transaction 이 Blockchain transaction pool 에 등록 되었습니다.");
                System.out.println("transaction 조회: " + TESTNET_URL + "/v1/transaction?hash=" + transactionRequest.getHash());
            } else {
                throw new Exception("transaction 업로드 중 오류가 발생 하였습니다.");
            }
        } catch (Exception ex) {
            String exMsg = ex.getMessage() != null ? ex.getMessage() : "";
            Log.e("simple-android-java-ko", "!!! ERROR !!!", ex);
        }
    }
}
