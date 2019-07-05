package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceiptItem {
    String receiptItemCode;
    String receiptItemName;
    int coveredPatientFee;
    int coveredInsuranceFee;
    int coveredPatientAllFee;
    int uncoveredChosenFee;
    int uncoveredUnchosenFee;
}
