/*
 * @(#) DoctorDaoTest.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package daotest;

import iuh.fit.dao.DoctorDao;
import iuh.fit.entity.Department;
import iuh.fit.entity.Doctor;
import iuh.fit.entity.Person;
import iuh.fit.utils.AppUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.print.Doc;

/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoctorDaoTest {
    public static String DB_NAME = "Phuc21036541";
    private DoctorDao doctorDao;

    @BeforeAll
    public void setUp() {
        doctorDao = new DoctorDao(AppUtils.initDriver(), DB_NAME);
    }
    // Thêm mới một bác sĩ
    @Test
    public void testGetAllDoctors() {
        Person newPerson = new Person("personID123", "Dr. Smith", "123-456-7890");
        Doctor newDoctor = new Doctor(newPerson, "Cardiology");
        boolean isAdded = doctorDao.addDoctor(newDoctor);
        if (isAdded) {
            System.out.println("Thêm thành cong");
        } else {
            System.out.println("Thêm thất bại");
        }
    }

    // getNoOfDoctorsBySpeciality
    @Test
    public void testGetNoOfDoctorsBySpeciality() {
        System.out.println(doctorDao.getNoOfDoctorsBySpeciality("City General"));
    }

    // listDoctorsBySpeciality
    @Test
    public void testListDoctorsBySpeciality() {
        doctorDao.listDoctorsBySpeciality("Cardiology").forEach(doctor -> {
            System.out.println(doctor);
        });
    }



// updateDiagnosis
    @Test
    public void testUpdateDiagnosis() {
        doctorDao.updateDiagnosis("6", "1", "Hellofffff");
        System.out.println("Cập nhật thành công");
    }



    @AfterAll
    public void tearDown() {
        doctorDao.close();
    }
}
