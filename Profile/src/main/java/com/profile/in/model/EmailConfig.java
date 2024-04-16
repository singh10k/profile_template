package com.profile.in.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "emailConfig")
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String mailConfigId;
    @Column(nullable = false)
    private String mailSmtpAuth;
    @Column(nullable = false)
    private String mailSmtpStarttlsEnable;
    @Column(nullable = false)
    private String mailSmtpHost;
    @Column(nullable = false)
    private String mailSmtpPort;
    @Column(nullable = false)
    private String fromMail;
    @Column(nullable = false)
    private String Password;
}
