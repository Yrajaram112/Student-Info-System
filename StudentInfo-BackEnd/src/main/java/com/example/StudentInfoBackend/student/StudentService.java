package com.example.StudentInfoBackend.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional1=studentRepository
                .findStudentByEmail(student.getEmail());
        Optional<Student> studentOptional2=studentRepository
                .findStudentByEmail(student.getPhoneNumber());
        if(studentOptional1.isPresent()&&studentOptional2.isPresent()){
            throw new IllegalStateException("Email and Phone taken");
        } else if (studentOptional1.isPresent()) {
            throw new IllegalStateException("Email Taken");

        }
        else if (studentOptional2.isPresent()) {
            throw new IllegalStateException("PhoneNumber Taken");

        }
       return studentRepository.save(student);
    }

    public Student findStudentById(Long id){
        return studentRepository.findStudentById(id).orElseThrow(()-> new UserNotFoundException("Student with Id "+id+" does not exists"));
    }
    public void deleteStudent(Long studentId) {
        boolean exists=studentRepository.existsById(studentId);

        if(!exists){
            throw new IllegalStateException(
                    "Student with Id "+studentId+" does not exists");
        }

        studentRepository.deleteById(studentId);
    }
    @Transactional
    public Student updateStudent(Student toUpdate) {
        Long studentId= toUpdate.getId();
        String name=toUpdate.getName();
        String email=toUpdate.getEmail();
        String phoneNumber= toUpdate.getPhoneNumber();
        String attendance=toUpdate.getAttendance();

        Student student=studentRepository.findById(studentId).
                orElseThrow(()->new IllegalStateException(
                        "Student with Id "+studentId+" does not exists"));

        if(name!=null && name.length()>0
        && !Objects.equals(student.getName(),name)){
            student.setName(name);
        }

        if(email!=null && email.length()>0
                && !Objects.equals(student.getEmail(),email)){
            Optional<Student> studentOptional=studentRepository
                    .findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("Email taken");
            }
            student.setEmail(email);
        }

        if(phoneNumber!=null && phoneNumber.length()>0
                && !Objects.equals(student.getPhoneNumber(),phoneNumber)){
            Optional<Student> studentOptional=studentRepository
                    .findStudentByPhoneNumber(phoneNumber);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("Phone Number taken");
            }
            student.setPhoneNumber(phoneNumber);
        }
        student.setAttendance(attendance);
        student.setRegNo(toUpdate.getRegNo());
        return student;
    }
}