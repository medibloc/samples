package org.medibloc.insurance_java_ko;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.medibloc.insurance_java_ko.entities.Certification;
import org.medibloc.panacea.account.Account;
import org.medibloc.panacea.account.AccountUtils;
import org.medibloc.panacea.core.HttpService;
import org.medibloc.panacea.core.Panacea;
import org.medibloc.panacea.core.protobuf.BlockChain;
import org.medibloc.panacea.core.protobuf.Rpc;
import org.medibloc.panacea.crypto.ECKeyPair;
import org.medibloc.panacea.crypto.Hash;
import org.medibloc.panacea.tx.Transaction;

import java.math.BigInteger;

public class MediBloc {
    private static final String BLOCKCHAIN_URL = "https://stg-testnet-node.medibloc.org";
    private static final String ACCOUNT_REQUEST_TYPE_TAIL = "tail";

    private static final String MNEMONIC = "slam wool bulk fine reduce honey capital wheat evoke enjoy treat flip";
    private static final BigInteger PRIVATE_KEY = new BigInteger("4da8bc28a095870433d8a7d57ca140d6132e722f177c9a94f70a6963b4b8f708", 16);
    // address: 02e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c296
    private static final BigInteger PUBLIC_KEY = new BigInteger("e34caca7b7653eb6cbb64cdd9e7c691545cbbe002a5ef9ed86e71577d9c7c2960da413ededc3216df47f27ba6d46babe0ba54ca35d682182d26a6c6aa63f7930", 16);
    private static final String PASSWORD = "MediBlocPassWord123!";

    private Account account;

    public MediBloc() throws Exception {
        ECKeyPair keyPair = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);
        this.account = AccountUtils.createAccount(PASSWORD, keyPair, null);

        System.out.println("MediBloc - 초기화를 완료 하였습니다. Blockchain address: " + this.account.getAddress());
    }

    public Certification generateCertificate(String address, Certification certification) {
        return new Certification(address      // user's blockchain address
                , System.currentTimeMillis()  // expiry date
                , certification.getName()
                , certification.getBirth()
                , certification.getGender()
                , certification.getCi()
                , certification.getMobileProvider()
                , certification.getMobileNumber());
    }

    public String sendCertificate(Certification certification) throws Exception {
        Panacea panacea = Panacea.create(new HttpService(BLOCKCHAIN_URL));

        String jsonCertification = new ObjectMapper().writeValueAsString(certification);
        System.out.println("MediBloc - 사용자의 본인인증 결과 json: " + jsonCertification);

        byte[] certificateHash = Hash.sha3256(jsonCertification.getBytes());

        Rpc.GetAccountRequest accountRequest = Rpc.GetAccountRequest.newBuilder()
                .setAddress(this.account.getAddress())
                .setType(ACCOUNT_REQUEST_TYPE_TAIL)
                .build();
        Rpc.Account accountBCInfo = panacea.getAccount(accountRequest).send();
        long nextNonce = accountBCInfo.getNonce() + 1;

        Rpc.MedState medState = panacea.getMedState().send();
        int chainId = medState.getChainId();

        BlockChain.TransactionHashTarget transactionHashTarget
                = Transaction.getAddRecordTransactionHashTarget(certificateHash, this.account.getAddress(), nextNonce, chainId);

        Rpc.SendTransactionRequest transactionRequest = Transaction.getSignedTransactionRequest(transactionHashTarget, this.account, PASSWORD);
        Rpc.TransactionHash resultHash = panacea.sendTransaction(transactionRequest).send();

        return resultHash.getHash();
    }
}
