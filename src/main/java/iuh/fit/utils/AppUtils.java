/*
 * @(#) AppUtils.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.types.Node;

import java.net.URI;
import java.util.Map;

/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */
public class AppUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Driver initDriver(){
        URI uri = URI.create("neo4j://localhost:7687");
        String user = "neo4j";
        String password = "12345678";
        return GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public static <T> T convert(Node node, Class<T> clazz) {
        Map<String, Object> properties = node.asMap();
        try {
            String json = objectMapper.writeValueAsString(properties);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


// Viết lệnh import dũ liệu file csv vào
// id	name	location
// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Department.csv' AS row
// MERGE (d:Department {departmentID: row.id})
// SET d.name = row.name, d.location = row.location
// RETURN d

//speciality	personID	name	phone	deptID
// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Doctor.csv' AS row
// MATCH (d:Department {departmentID: row.deptID})
// MERGE (p:Doctor {personID: row.personID})
// SET p.speciality = row.speciality, p.personID = row.personID, p.name = row.name, p.phone = row.phone
// RETURN p

//startOfDate	endDate	diagnosis	doctor	patient
// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Treatment.csv' AS row
// MERGE (p:Patient {personID: row.patient})
// SET p.startOfDate = datetime(row.startOfDate), p.endDate = datetime(row.endDate), p.diagnosis = row.diagnosis, p.Doctor = row.doctor, p.Patient = row.patient

// gender	dateOfBirth	address	personID	name	phone
// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Patient.csv' AS row
// MERGE (p:Patient {personID: row.personID})
// SET p.gender = row.gender, p.dateOfBirth = row.dateOfBirth, p.address = row.address, p.name = row.name, p.phone = row.phone
// RETURN p

// Tạo unique index cho cac id của các node
// CREATE CONSTRAINT unique_doctor_id FOR (d:Doctor) REQUIRE d.personID IS UNIQUE
// CREATE CONSTRAINT unique_patient_id FOR (p:Patient) REQUIRE p.personID IS UNIQUE
// CREATE CONSTRAINT unique_department_id FOR (d:Department) REQUIRE d.departmentID IS UNIQUE

// Thiết lập relationship giữa các node
// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Doctor.csv' AS row
// MATCH (d:Doctor {personID: row.personID})
// MATCH (dept:Department {departmentID: row.deptID})
// MERGE (d)-[:BELONG_TO]->(dept)

// LOAD CSV WITH HEADERS FROM 'file:///Doctor/Treatment.csv' AS row
// MATCH (p:Patient {personID: row.patient})
// MATCH (d:Doctor {personID: row.doctor})
// MERGE (p)-[:BE_TREATED]->(d)

// Xóa bảng
// MATCH (Person) DETACH DELETE Person