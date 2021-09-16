package com.artuhin.sproject.model.entity;

import com.artuhin.sproject.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(schema = "bs", name = "appointments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private ProcedureEntity procedure;
    @ManyToOne
    @JoinColumn(name = "master_id")
    private UserEntity master;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserEntity client;
    private Timestamp startTime;
    private Timestamp endTime;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;
}
