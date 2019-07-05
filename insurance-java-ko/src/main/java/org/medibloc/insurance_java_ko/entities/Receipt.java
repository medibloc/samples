package org.medibloc.insurance_java_ko.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Receipt {
    String receiptNo;
    String receiptType;
    long receiptIssueDate;
    String patientNo;
    String patientName;
    long treatmentStartDate;
    long treatmentEndDate;
    String treatmentTimeType;
    String treatmentDepartmentCode;
    String treatmentDepartmentName;
    String dgrNo;
    String room;
    String patientTypeCode;
    String patientType;
    int coveredPatientFeeTotal;
    int coveredInsuranceFeeTotal;
    int coveredPatientAllFeeTotal;
    int coveredFeeTotal;
    int uncoveredChosenFeeTotal;
    int uncoveredUnchosenFeeTotal;
    int uncoveredFeeTotal;
    int upperLimitExcess;
    int payTotal;
    int patientPayTotal;
    int advancePayAmount;
    int payAmount;
    int cardPaidAmount;
    int cashReceiptPaidAmount;
    int cashPaidAmount;
    int paidAmountTotal;
    int uncollectedPayAmount;
    String cashReceiptInfo;
    String cashReceiptApprovalNo;
    String idConfirmNo;
    String hospitalType;
    String companyRegistrationNo;
    String hospitalName;
    String hospitalPhoneNo;
    String hospitalAddress;
    String hospitalRepresentative;
    List<ReceiptItem> receiptItems;
    List<ReceiptEtcItem> receiptEtcItems;
}
