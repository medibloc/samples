package org.medibloc.insurance_java_ko.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequest {
    String insuranceCode;   // 보험 계약 코드
    String accidentType;    // 사고 유형(질병/상해/교통사고)
    String accidentDate;    // 사고일
    String accidentDetail;  // 사고 내용
    String accountBankCode; // 보험금 수령 은행 코드
    String accountBankName; // 보험금 수령 은행 명
    String accountNumber;   // 보험금 수령 계좌 번호
    String accountHolder;   // 보험금 수령 계좌주 명
    String informType;      // 보험청구 진행 알림 방법
    boolean isMedicalCareRecipient; // 의료급여 수급권자 여부
    int medicalCareRecipientType;   // 의료급여 수급권자 구분
    String billTxHash;     // 청구 데이터 transaction hash
    Bill bill;              // 청구 데이터
}
