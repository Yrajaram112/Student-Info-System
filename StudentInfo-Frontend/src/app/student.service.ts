import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student } from './student';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiServer=environment.apiBaseUrl; 

  constructor(private http: HttpClient) { }

  public getStudents(): Observable<Student []>{
    return this.http.get<Student []>(`${this.apiServer}/student/all`);
  }
  
  public addStudent(student: FormData): Observable<Student>{
    return this.http.post<Student>(`${this.apiServer}/student/add`,student);
  }

  public updateStudent(student: Student): Observable<Student>{
    return this.http.put<Student>(`${this.apiServer}/student/update`,student);
  }

  public deleteStudent(studentId: number): Observable<void>{
    return this.http.delete<void>(`${this.apiServer}/student/delete/${studentId}`);
  }


}
