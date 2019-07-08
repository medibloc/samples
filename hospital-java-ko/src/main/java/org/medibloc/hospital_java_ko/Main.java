package org.medibloc.hospital_java_ko;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.medibloc.hospital_java_ko.entities.Bill;
import org.medibloc.hospital_java_ko.entities.Certification;
import org.medibloc.panacea.core.protobuf.Rpc;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("hospital-java sample Main 함수를 실행합니다.");
        MediBloc mediBloc = new MediBloc();

        // 사용자 본인인증 수행
        User user = new User();
        Certification incompleteCertification = user.certify();
        System.out.println("사용자 - 본인인증을 수행 하였습니다.");

        // 본인인증 결과를 블록체인에 기록
        Certification certification = mediBloc.generateCertificate(user.getAddress(), incompleteCertification);
        String certificateTxHash = mediBloc.sendCertificate(certification);
        System.out.println("MediBloc - 사용자의 본인인증 결과를 블록체인에 기록 하였습니다.");
        System.out.println("           transaction 조회: https://stg-testnet-node.medibloc.org/v1/transaction?hash=" + certificateTxHash);

        // MediBloc 이 사용자에게 인증서, tx hash 반환
        user.setCertificate(certification);
        user.setCertificateTxHash(certificateTxHash);

        Thread.sleep(5000);

        // 병원 객체 생성. 생성자 내부적으로 mockup data 와 블록체인 계정을 생성 합니다.
        Hospital hospital = new Hospital();

        // 병원의 환자 id 와 블록체인 account 연계
        hospital.mapAccountOntoPatientId(user.getAddress(), user.getCertificate(), user.getCertificateTxHash(), user.getResidentRegistrationNumber());
        System.out.println("병원 - 환자 id 와 사용자의 블록체인 account 를 연계 하였습니다.");

        // 병원이 청구서, signed tx 생성하여 사용자(환자)에게 전달
        Bill bill = hospital.getBill(user.getAddress());
        System.out.println("병원 - 영수증을 생성 하였습니다. jsonBill: " + new ObjectMapper().writeValueAsString(bill));

        Rpc.SendTransactionRequest claimTransactionRequest = hospital.getSignedTransaction(bill);
        String txHash = hospital.sendClaim(claimTransactionRequest);
        System.out.println("병원 - 환자의 진료 청구서에 sign 하였습니다. txHash: " + txHash);

        user.setBill(bill);
        user.setClaimTxRequest(claimTransactionRequest);
    }
}
