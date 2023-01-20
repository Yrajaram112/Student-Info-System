import { FileHandle } from "./file-handle.model";


export interface Student{
    id:number;
    name:string;
    regNo: string;
    email:string;
    phoneNumber:string;
    dob:Date;
    age?:number;
    attendance:string;
    studentImages:FileHandle[];
   
}