package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptEtcItem {
    String receiptEtcItemCode;
    String receiptEtcItemName;
    String receiptEtcItemContent;
}
