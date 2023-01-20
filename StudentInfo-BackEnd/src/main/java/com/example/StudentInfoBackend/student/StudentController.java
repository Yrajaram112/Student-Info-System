package com.example.StudentInfoBackend.student;


import java.io.*;


import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.zip.Deflater;


@RestController
@RequestMapping(path="/student")
public class StudentController {
    private final StudentService studentService;

    private static Logger log = LoggerFactory.getLogger(StudentController.class);
    public static String uploadDirectory = System.getProperty("user.dir") + "/downloads/images";


    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> students= studentService.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id){
        Student student= studentService.findStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }
    @PostMapping(value="/add",consumes={
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> registerNewStudent(@RequestPart("student") Student student,
                                                @RequestPart("imageFile") MultipartFile[] file) throws IOException {

        try{
            Set<ImageModel> image=uploadImage(file);
            student.setStudentImages(image);
            studentService.addNewStudent(student);
            return new ResponseEntity<>(student,HttpStatus.OK);
        }
        catch (Exception e){
        System.out.println(e.getMessage());
        return null;
        }
    }
    public Set<ImageModel> uploadImage(MultipartFile[] mfile) throws IOException{
        Set<ImageModel> imageModel=new HashSet<>();
        for (MultipartFile file:mfile){
            ImageModel img =new ImageModel(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        imageModel.add(img);
    }
    return imageModel;
    }
    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student){
        Student updateStudent= studentService.updateStudent(student);
        return new ResponseEntity<>(updateStudent, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") Long id){
        studentService.deleteStudent(id);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }


}