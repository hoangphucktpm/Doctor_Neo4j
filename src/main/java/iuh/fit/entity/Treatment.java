/*
 * @(#) Treatment.java       1.0  27/03/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package iuh.fit.entity;

import lombok.*;
import java.time.LocalDate;
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

public class Treatment {
    // startOfDate	endDate	diagnosis	doctor	patient

    private LocalDate startOfDate;
    private LocalDate endDate;
    private String diagnosis;
    @ToString.Exclude
    private Doctor doctor;
    @ToString.Exclude
    private Patient patient;

    public Treatment(LocalDate startOfDate, LocalDate endDate, java.lang.String diagnosis) {
        this.startOfDate = startOfDate;
        this.endDate = endDate;
        this.diagnosis = diagnosis;
    }
}
