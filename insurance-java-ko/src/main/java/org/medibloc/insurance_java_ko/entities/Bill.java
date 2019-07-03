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
        String companyRegistrationNo;
        String treatmentStartDate;
        String treatmentEndDate;
        String treatmentDepartment;
        String treatmentDepartmentCode;
        String coveredFee;
        String uncoveredFee;
        String upperLimitExcess;
        String payTotal;
        String patientPayTotal;
        String deductAmount;
        String advancePayAmount;
        String payAmount;
        String uncollectedPayAmount;
        String receiptAmount;
        String surtaxAmount;
        String cashPayAmount;
        String cardPayAmount;
        String insuranceType;
        String insuranceName;
        ReceiptItem[] receiptItems;
    }

    public class ReceiptItem {
        String receiptItemName;
        String receiptItemCode;
        String coveredPatientFee;
        String coveredInsuranceFee;
        String coveredPatientAllFee;
        String uncoveredChosenFee;
        String uncoveredUnchosenFee;
    }

    public class FeeDetail {
        String patientNo;
        String patientName;
        String treatmentStartDate;
        String treatmentEndDate;
        String room;
        String patientType;
        String amount;
        String adjustAmount;
        String totalAmount;
        String companyName;
        String companyRegistrationNo;
        FeeItem[] feeItems;
    }

    public class FeeItem {
        String feeItemName;
        String feeItemCode;
        String treatmentDate;
        String claimCode;
        String claimName;
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
