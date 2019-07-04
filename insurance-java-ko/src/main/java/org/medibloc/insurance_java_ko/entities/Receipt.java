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
    String receiptIssueDate;
    String patientNo;
    String patientName;
    String treatmentStartDate;
    String treatmentEndDate;
    String treatmentTimeType;
    String treatmentDepartmentCode;
    String treatmentDepartmentName;
    String dgrNo;
    String room;
    String patientTypeCode;
    String patientType;
    String coveredPatientFeeTotal;
    String coveredInsuranceFeeTotal;
    String coveredPatientAllFeeTotal;
    String coveredFeeTotal;
    String uncoveredChosenFeeTotal;
    String uncoveredUnchosenFeeTotal;
    String uncoveredFeeTotal;
    String upperLimitExcess;
    String payTotal;
    String patientPayTotal;
    String advancePayAmount;
    String payAmount;
    String cardPaidAmount;
    String cashReceiptPaidAmount;
    String cashPaidAmount;
    String paidAmountTotal;
    String uncollectedPayAmount;
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