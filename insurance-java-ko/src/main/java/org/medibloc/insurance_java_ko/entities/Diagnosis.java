package org.medibloc.insurance_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Diagnosis {
    String diagnosisCodeVersion;
    String diagnosisCodeType;
    String diagnosisCode;
}
