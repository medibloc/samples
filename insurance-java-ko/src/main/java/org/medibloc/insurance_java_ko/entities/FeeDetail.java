package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FeeDetail {
    String patientNo;
    String patientName;
    long treatmentStartDate;
    long treatmentEndDate;
    String room;
    String patientTypeCode;
    String patientType;
    String memo;
    int amount;
    int adjustAmount;
    int totalAmount;
    String requester;
    String requesterRelationType;
    String companyRegistrationNo;
    String hospitalName;
    List<FeeItem> feeItems;
}
