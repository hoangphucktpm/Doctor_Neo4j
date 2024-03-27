/*
 * @(#) Doctor.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */

public class Doctor extends Person {
    private String speciality;
    @ToString.Exclude
    private Department deptID;
    @ToString.Exclude
    private Person person;

    public Doctor(Person person, String speciality) {
        super(person.getPersonID(), person.getName(), person.getPhone());
        this.speciality = speciality;
        this.person = person;
    }

    public Doctor(String personID, String name, String phone, String speciality) {
        super(personID, name, phone);
        this.speciality = speciality;
    }

    public Doctor(String personID, String name, String phone, String speciality, Department deptID) {
        super(personID, name, phone);
        this.speciality = speciality;
        this.deptID = deptID;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Department getDeptID() {
        return deptID;
    }

    public void setDeptID(Department deptID) {
        this.deptID = deptID;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
