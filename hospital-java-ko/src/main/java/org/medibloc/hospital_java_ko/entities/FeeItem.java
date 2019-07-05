package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeItem {
    String feeItemCode;
    String feeItemName;
    long treatmentDate;
    String medicalChargeCode;
    String medicalChargeName;
    String treatmentName;
    String ediCode;
    String ediName;
    int unitCost;
    int quantity;
    int repeatNumber;
    int dateNumber;
    int feeTotal;
    int coveredPatientFee;
    int coveredInsuranceFee;
    int coveredPatientAllFee;
    int uncoveredChosenFee;
    int uncoveredUnchosenFee;
    int uncoveredFee;
}
