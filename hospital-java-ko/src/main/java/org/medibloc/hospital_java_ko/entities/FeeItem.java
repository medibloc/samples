package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
