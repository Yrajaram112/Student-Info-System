package com.example.StudentInfoBackend.student;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Student_Info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @SequenceGenerator(name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1)

    @GeneratedValue(strategy =GenerationType.SEQUENCE,
            generator = "student_sequence")
    private Long id;
    @Column(name="Name")
    private String name;
    @Column(name="Reg_No")
    private String regNo;
    @Column(name="Email")
    private String email;
    @Column(name="Phone_No")
    private String phoneNumber;
    @Column(name="Birth_Date")
    private LocalDate dob;
    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="student_images",
    joinColumns = {
            @JoinColumn(name="student_id")
    },
    inverseJoinColumns = {
            @JoinColumn(name="image_id")
    })
    private Set<ImageModel> studentImages;
    @Transient
    private Integer age;
    @Column(name="Attendance_Status")
    private String attendance;


     public Integer getAge() {
        return Period.between(this.dob,LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", regNo='" + regNo + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                ", attendance=" + attendance +
                '}';
    }

}

