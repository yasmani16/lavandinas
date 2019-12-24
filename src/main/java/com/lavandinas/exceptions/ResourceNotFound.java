package com.lavandinas.exceptions;

public class ResourceNotFound extends Exception {
    public ResourceNotFound(String message){
        super(message);
    }

    public ResourceNotFound(){
        super();
    }
}
