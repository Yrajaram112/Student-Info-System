import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { map } from 'rxjs/operators';
import { FileHandle } from './file-handle.model';
import { Student } from './student';
import { StudentService } from './student.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  public students: Student[]=[];
  public addStudent:Student={
    id:0,
    name:"",
    regNo:"",
    email:"",
    phoneNumber:"",
    dob:new Date(),
    attendance:"",
    studentImages:[]
   };
  public editStudent:Student={
    id:0,
    name:"",
    regNo:"",
    email:"",
    phoneNumber:"",
    dob:new Date(),
    attendance:"",
    studentImages:[]
   };
  public deleteStudent: Student;

  constructor(private studentService: StudentService,
    private sanitizer:DomSanitizer){
  }

  onFileSelected(event:any) {
    if(event.target.files){
      const file=event.target.files[0];

      const fileHandle:FileHandle={
        file:file,
        url: this.sanitizer.bypassSecurityTrustUrl(
          window.URL.createObjectURL(file)
        )
      }
      console.log(fileHandle)
      
        this.addStudent.studentImages.push(fileHandle);

    }
  }


  ngOnInit(){
    this.getStudents();
  }

  public getStudents(): void{
    this.studentService.getStudents().pipe(
      map((x:Student[])=>x.map((student:Student)=>this.createImage(student)))
    ).subscribe({
        next: (response: Student[])=>{
          console.log(response);          
          this.students=response;
        },
        error: (error:HttpErrorResponse)=>{
          alert(error.message);
        }
  });
  }
data:any[]
  showImage(student:Student){
    data:{
      images:student.studentImages
    }
  }
  public onAddStudent(addForm: NgForm): void{
    var studentFormData=new FormData();
    studentFormData = this.prepareFormData(this.addStudent);     
    document.getElementById("add-Student-form")?.click();
    this.studentService.addStudent(studentFormData).subscribe({
      next: (response: Student)=>{
        console.log(response);
        this.getStudents();
        addForm.reset();
      },
      error: (error:HttpErrorResponse)=>{
        alert(error.message);
        addForm.reset();
      }
});
}
prepareFormData(studente:Student):FormData{
  const formData=new FormData();

  formData.append(
    'student',
    new Blob([JSON.stringify(studente)],{type:'application/json'})
  );
  formData.append(
    'imageFile',
    studente.studentImages[0].file,
    studente.studentImages[0].file.name
  );
  return formData;
}

createImage(student:Student){
  const studentImg:any[]=student.studentImages;
  const studentImagetoFileHandle:FileHandle[]=[];

  for(let i=0;i<studentImg.length;i++){
    const imageFileData=studentImg[i];
    const imageBlob = this.dataURItoBlob(imageFileData.picByte,imageFileData.type);
    const imageFile = new File([imageBlob],imageFileData.name,{type:imageFileData.type});
    const finalFileHandle:FileHandle={
      file:imageFile,
      url:this.sanitizer.bypassSecurityTrustUrl(
        window.URL.createObjectURL(imageFile))
    };
    studentImagetoFileHandle.push(finalFileHandle);
  }
  student.studentImages=studentImagetoFileHandle;
  return student;
}

dataURItoBlob(picBytes: string,imageType: any){
  const byteString=window.atob(picBytes);
  const arrayBuffer = new ArrayBuffer(byteString.length);
  const int8Array = new Uint8Array(arrayBuffer);

  for(let i=0;i<byteString.length;i++){
    int8Array[i]=byteString.charCodeAt(i);
  }
  const blob=new Blob([int8Array],{type:imageType});
  return blob;
}

  public onUpdateStudent(student: Student):void{
    this.studentService.updateStudent(student).subscribe({
      next: (response: Student)=>{
        console.log(response);
        this.getStudents();
      },
      error: (error:HttpErrorResponse)=>{
        alert(error.message);
      }
});

  }

  public onDeleteStudent(studentId: number):void{
    this.studentService.deleteStudent(studentId).subscribe({
      next: (response: void)=>{
        console.log(response);
        this.getStudents();
        document.getElementById('main-container');
      },
      error: (error:HttpErrorResponse)=>{
        alert(error.message);
      }
});
  }
  public searchStudent(key: String){
    const results: Student[] = [];
    for (const student of this.students){
      if(student.name.toLowerCase().indexOf(key.toLowerCase())!==-1
      || student.email.toLowerCase().indexOf(key.toLowerCase())!==-1
      || student.phoneNumber.toLowerCase().indexOf(key.toLowerCase())!==-1
      || student.regNo.toLowerCase().indexOf(key.toLowerCase())!==-1)
      results.push(student);
    }
    this.students=results;

    if(results.length===0 ||!key)
    this.getStudents();
  }        

  public onOpenModal(student: Student,mode:string): void{
    const container = document.getElementById('main-container');
    const button= document.createElement('button');
    button.type = 'button';
   
    button.setAttribute('data-toggle','modal');
    if(mode=== 'add'){
      button.setAttribute('data-target','#addStudentModel');
    }
    if(mode=== 'edit'){
      this.editStudent=student;      
      button.setAttribute('data-target','#updateStudentMod');
    }
    if(mode=== 'delete'){
      this.deleteStudent=student;      
      button.setAttribute('data-target','#deleteStudentModel');
      
    }
    container!.appendChild(button);
    button.click();
  }

}