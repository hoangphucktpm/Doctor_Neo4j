/*
 * @(#) Person.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
/*
 * @description:
 * @author: Hoang Phuc
 * @date:   27/03/2024
 * @version:    1.0
 */
public class Person {
    // personID	name	phone
    private String personID;
    private String name;
    private String phone;

    public Person(String personID, String name, String phone) {
        this.personID = personID;
        this.name = name;
        this.phone = phone;
    }
}
