package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceiptEtcItem {
    String receiptEtcItemCode;
    String receiptEtcItemName;
    String receiptEtcItemContent;
}
