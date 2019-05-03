package org.medibloc.insurance_java_ko;

import org.medibloc.insurance_java_ko.entities.ClaimResponse;
import org.medibloc.insurance_java_ko.entities.InsuranceEntity;
import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1.Certification;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("insurance-java sample Main 함수를 실행합니다.");
        MediBloc mediBloc = new MediBloc();

        // 사용자 본인인증 수행
        User user = new User();
        Certification.Builder certificationBuilder = user.certify();
        System.out.println("사용자 - 본인인증을 수행 하였습니다.");

        // 본인인증 결과를 블록체인에 기록
        Certificate certificate = mediBloc.generateCertificate(user.getAddress(), certificationBuilder);
        String certificateTxHash = mediBloc.sendCertificate(certificate);
        System.out.println("MediBloc - 사용자의 본인인증 결과를 블록체인에 기록 하였습니다.");
        System.out.println("           transaction 조회: https://stg-testnet-node.medibloc.org/v1/transaction?hash=" + certificateTxHash);

        // MediBloc 이 사용자에게 인증서, tx hash 반환
        user.setCertificate(certificate);
        user.setCertificateTxHash(certificateTxHash);

        Thread.sleep(5000);

        // 보험사 객체 생성. 생성자 내부적으로 mockup data 와 블록체인 계정을 생성 합니다.
        Insurer insurer = new Insurer();

        // 보험사에 사용자의 blockchain 주소 등록
        insurer.signUp(user.getCertificate(), user.getCertificateTxHash());
        System.out.println("보험사 - 사용자 id 와 사용자의 블록체인 account 를 연계 하였습니다.");

        // 계약 조회
        String encryptedAccidentDate = user.getEncryptedAccidentDate(insurer.getAddress());
        List<InsuranceEntity> insuranceEntityList = insurer.getInsuranceList(user.getAddress(), encryptedAccidentDate);
        user.setInsuranceEntityList(insuranceEntityList);
        System.out.println("보험사 - 보험 계약상품 정보를 사용자에게 반환 하였습니다.");

        // 청구 접수
        ClaimResponse claimResponse = insurer.sendClaim(user.getAddress(), user.getEncryptedClaimRequest(insurer.getAddress()));
        System.out.println("보험사 - 보험 청구를 접수 하였습니다.");
        System.out.println("       접수번호: " + claimResponse.getClaimNumber() + "  접수일: " + claimResponse.getClaimDate());

        // 접수내역 업데이트 조회
        // TODO
    }
}
