package org.medibloc.insurance_java_ko.entities;

public class Bill {
    String billNo;
    Receipt[] receipts;
    FeeDetail[] feeDetails;
    Diagnosis[] diagnoses;

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
        ReceiptItem[] receiptItems;
        ReceiptEtcItem[] receiptEtcItems;
    }

    public class ReceiptItem {
        String receiptItemCode;
        String receiptItemName;
        String coveredPatientFee;
        String coveredInsuranceFee;
        String coveredPatientAllFee;
        String uncoveredChosenFee;
        String uncoveredUnchosenFee;
    }

    public class ReceiptEtcItem {
        String receiptEtcItemCode;
        String receiptEtcItemName;
        String receiptEtcItemContent;
    }

    public class FeeDetail {
        String patientNo;
        String patientName;
        String treatmentStartDate;
        String treatmentEndDate;
        String room;
        String patientType;
        String memo;
        String amount;
        String adjustAmount;
        String totalAmount;
        String requester;
        String requesterRelationType;
        String hospitalName;
        String companyRegistrationNo;
        FeeItem[] feeItems;
    }

    public class FeeItem {
        String feeItemCode;
        String feeItemName;
        String treatmentDate;
        String medicalChargeCode;
        String medicalChargeName;
        String treatmentName;
        String ediCode;
        String ediName;
        String unitCost;
        String quantity;
        String repeatNumber;
        String dateNumber;
        String feeTotal;
        String coveredPatientFee;
        String coveredInsuranceFee;
        String coveredPatientAllFee;
        String uncoveredChosenFee;
        String uncoveredUnchosenFee;
        String uncoveredFee;
    }

    public class Diagnosis {
        String diagnosisCodeVersion;
        String diagnosisCodeType;
        String diagnosisCode;
    }
}
