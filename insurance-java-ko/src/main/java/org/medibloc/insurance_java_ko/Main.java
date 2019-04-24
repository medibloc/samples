package org.medibloc.insurance_java_ko;

import org.medibloc.phr.CertificateDataV1.Certificate;
import org.medibloc.phr.CertificateDataV1.Certification;

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
    }
}
