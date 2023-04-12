package com.wbtkj.wbt.Exception;

public class MyException extends RuntimeException{
    public MyException(){

    }
    public MyException(String s){
        super(s);
    }
}