/*
 * @(#) DoctorDao.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.dao;

import iuh.fit.entity.Doctor;
import iuh.fit.entity.Person;
import iuh.fit.utils.AppUtils;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.types.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */
public class DoctorDao {
    private Driver driver;
    private SessionConfig sessionConfig;

    public DoctorDao(Driver driver, String dbName){
        this.driver = driver;
        sessionConfig = SessionConfig.builder().withDatabase(dbName).build();
    }

    // a. Them 1 bác sĩ mới: addDoctor(doctor: Doctor): boolean
    public boolean addDoctor(Doctor doctor){
        // {
        //  "identity": 5,
        //  "labels": [
        //    "Doctor"
        //  ],
        //  "properties": {
        //    "speciality": "Cardiology",
        //    "phone": "654987321",
        //    "name": " Emily White",
        //    "personID": "6"
        //  },
        //  "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:5"
        //}
        // java.lang.NullPointerException: Cannot invoke "iuh.fit.entity.Person.getPhone()" because the return value of "iuh.fit.entity.Doctor.getPerson()" is null
        String query = "CREATE (d:Doctor {personID: $personID, name: $name, phone: $phone, speciality: $speciality})";
        Map<String, Object> params = Map.of(
                "personID", doctor.getPerson().getPersonID(),
                "name", doctor.getPerson().getName(),
                "phone", doctor.getPerson().getPhone(),
                "speciality", doctor.getSpeciality()
        );
        try (Session session = driver.session(sessionConfig)){
            session.executeWrite(tx ->
                tx.run(query, params).consume());
            return true;
        }
    }

    // b. Thống kê số lượng bác sĩ theo tung chuyen khoa(speciality) của mot khoa(department) nào khi biet tên khoa: getNoOfDoctorsBySpeciality(deptName: String): Map<String, Long>
    public Map<String, Long> getNoOfDoctorsBySpeciality(String deptName){
        // {
        //  "start": {
        //    "identity": 3,
        //    "labels": [
        //      "Doctor"
        //    ],
        //    "properties": {
        //      "speciality": "Cardiology",
        //      "phone": "654987321",
        //      "name": " Emily White",
        //      "personID": "6"
        //    },
        //    "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:3"
        //  },
        //  "end": {
        //    "identity": 16,
        //    "labels": [
        //      "Department"
        //    ],
        //    "properties": {
        //      "departmentID": "1",
        //      "name": "City General",
        //      "location": "123 Main Street"
        //    },
        //    "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:16"
        //  },
        //  "segments": [
        //    {
        //      "start": {
        //        "identity": 3,
        //        "labels": [
        //          "Doctor"
        //        ],
        //        "properties": {
        //          "speciality": "Cardiology",
        //          "phone": "654987321",
        //          "name": " Emily White",
        //          "personID": "6"
        //        },
        //        "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:3"
        //      },
        //      "relationship": {
        //        "identity": 0,
        //        "start": 3,
        //        "end": 16,
        //        "type": "BELONG_TO",
        //        "properties": {
        //
        //        },
        //        "elementId": "5:ad547bbd-bc5d-4b09-bc2c-3e802c017913:0",
        //        "startNodeElementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:3",
        //        "endNodeElementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:16"
        //      },
        //      "end": {
        //        "identity": 16,
        //        "labels": [
        //          "Department"
        //        ],
        //        "properties": {
        //          "departmentID": "1",
        //          "name": "City General",
        //          "location": "123 Main Street"
        //        },
        //        "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:16"
        //      }
        //    }
        //  ],
        //  "length": 1.0
        //}
        String query = "MATCH (d:Doctor)-[:BELONG_TO]->(dept:Department {name: $deptName}) RETURN d.speciality, count(d) as count";
        Map<String, Object> params = Map.of("deptName", deptName);
        try (Session session = driver.session(sessionConfig)){
            return session.executeRead(tx -> {
                Result result = tx.run(query, params);
                return result.stream().collect(
                        Collectors.toMap(
                                record -> record.get("d.speciality").asString(),
                                record -> record.get("count").asLong(),
                                (a, b) -> a
)
                );
            });
        }
    }

    // c. Dùng full-text search để tìm kiếm các bac sĩ theo chuyen khoa: listDoctorsBySpeciality(keyword: String): List<Doctor>
    // CREATE FULLTEXT INDEX doctor_speciality FOR (d:Doctor) ON EACH [d.speciality]

    public List<Doctor> listDoctorsBySpeciality(String keyword) {
        String query = "CALL db.index.fulltext.queryNodes('doctor_speciality', $keyword) YIELD node RETURN node";
        Map<String, Object> params = Map.of("keyword", keyword);
        try (Session session = driver.session(sessionConfig)) {
            return session.executeRead(tx -> {
                Result result = tx.run(query, params);
                if (!result.hasNext()) {
                    return null;
                }
                return result.stream().map(record -> {
                    Node node = record.get("node").asNode();
                    Person person = new Person(node.get("personID").asString(), node.get("name").asString(), node.get("phone").asString());
                    return new Doctor(person, node.get("speciality").asString());
                }).toList();
            });
        }
    }

    // Cập nhật lại chuan đoán(diagnosis) cho 1 luot dieu tri khi biet ma so bac si va ma so benh nhan. luu y, chi được phep cap nhat khi luot dieu tri nay van con dang dieu tri
    // (tuc ngay ket thuc dieu tri la null):

    // {
    //  "identity": 7,
    //  "labels": [
    //    "Patient"
    //  ],
    //  "properties": {
    //    "startOfDate": "2022-07-16T00:00:00Z",
    //    "address": "New Yorks",
    //    "gender": "m",
    //    "phone": "12345678",
    //    "endDate": "2022-07-17T00:00:00Z",
    //    "name": "Robert Aderson",
    //    "diagnosis": "Fever",
    //    "Patient": "1",
    //    "dateOfBirth": "5/15/1990",
    //    "personID": "1",
    //    "Doctor": "6"
    //  },
    //  "elementId": "4:ad547bbd-bc5d-4b09-bc2c-3e802c017913:7"
    //}
    // sua lai ngay endDate thanh null: lenh neo4j: MATCH (p:Patient {personID: '1'}) SET p.endDate = null

            public boolean updateDiagnosis(String doctorID, String patientID, String diagnosis){
                // cap nhat lai khi biet ma bac si va ma benh nhan
                String query = "MATCH (p:Patient {personID: $patientID}) WHERE p.endDate IS NULL AND p.Doctor = $doctorID SET p.diagnosis = $diagnosis";
                Map<String, Object> params = Map.of("patientID", patientID, "doctorID", doctorID, "diagnosis", diagnosis);
                try (Session session = driver.session(sessionConfig)){
                    session.executeWrite(tx ->
                            tx.run(query, params).consume());
                    return true;
                }

            }


    public void close(){
        driver.close();
    }
}
