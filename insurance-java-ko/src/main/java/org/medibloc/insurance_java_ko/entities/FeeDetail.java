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
    List<FeeItem> feeItems;
}
