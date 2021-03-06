package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    String billId;
    List<Receipt> receipts;
    FeeDetail feeDetail;
    List<Diagnosis> diagnoses;
}
