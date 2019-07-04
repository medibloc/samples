package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Certificate {
    int version;
    String blockchainAddress;
    String expiryDate;
    String certificationResult;
    String personName;
    String personBirthdate;
    String personGender;
    String personNation;
    String personCi;
    String personMobileCompany;
    String personMobileNumber;
}
