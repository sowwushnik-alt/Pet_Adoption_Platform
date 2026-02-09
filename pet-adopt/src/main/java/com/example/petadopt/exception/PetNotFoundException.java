package com.example.petadopt.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(Long id) {
        super("Питомец с ID " + id + " не найден в базе данных.");
    }
}