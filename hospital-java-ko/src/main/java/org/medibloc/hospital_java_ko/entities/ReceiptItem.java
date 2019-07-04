package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceiptItem {
    String receiptItemCode;
    String receiptItemName;
    String coveredPatientFee;
    String coveredInsuranceFee;
    String coveredPatientAllFee;
    String uncoveredChosenFee;
    String uncoveredUnchosenFee;
}
