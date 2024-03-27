/*
 * @(#) Patient.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.entity;

import lombok.*;
/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Patient extends Person {
    // gender	dateOfBirth	address	personID	name	phone
// Trong đó  personID	name	phone thuộc lớp Person và Patient kế thừa từ Person
    // Gender: gom 3 gia trị là m, f, o

    private String dateOfBirth;
    private String address;
    private Gender gender;

    @ToString.Exclude
    private Person person;

    public Patient(String personID, String name, String phone, String dateOfBirth , String address, Gender gender) {
        super(personID, name, phone);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.gender = gender;
    }



}
