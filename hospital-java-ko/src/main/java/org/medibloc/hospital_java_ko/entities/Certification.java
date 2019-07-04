package org.medibloc.hospital_java_ko.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Certification {
    String bcAddress;
    long expireTime;
    String name;
    String birth;
    int gender;
    String ci;
    int mobileProvider;
    String mobileNumber;
}
