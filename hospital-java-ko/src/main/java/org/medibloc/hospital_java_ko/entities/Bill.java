package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Bill {
    String billNo;
    List<Receipt> receipts;
    FeeDetail feeDetail;
    List<Diagnosis> diagnoses;
}
