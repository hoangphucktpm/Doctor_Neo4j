/*
 * @(#) Department.java       1.0  27/03/2024
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
@AllArgsConstructor

public class Department {
    // id	name	location

    private String id;
    private String name;
    private String location;
}
