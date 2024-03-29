package com.example.CatDog.service;

import com.example.CatDog.repository.DogRepository;
import com.example.CatDog.entity.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DogService {
    @Autowired
    private DogRepository dogRepository;

    public ResponseEntity<Dog> addDog(Dog inputDog){
        Dog dog = addNewDog(inputDog);
        try {
            dogRepository.save(dog);
            return new ResponseEntity<>(dog, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(dog, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Dog>> getAllDogs(){
        return new ResponseEntity<>((List<Dog>) dogRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Dog> getDogById(int dogId){
        boolean dogExists = dogRepository.findById(dogId).isPresent();
        if (dogExists){
            return new ResponseEntity<>(dogRepository.findById(dogId).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Dog>> searchForDogs(String age, String name, String color, String gender, String breed, String weight){
        try {
            Stream<Dog> dogStream = dogRepository.findDogsMatchingCriteria(age, name, color, gender, breed, weight);
            List<Dog> dogs = new ArrayList<Dog>();
            dogStream.forEach((d) -> {
                dogs.add(d);
            });
            return new ResponseEntity<>(dogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((List<Dog>) null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Dog> updateDog(int dogId, Dog inputDog) {
        boolean dogExists = dogRepository.findById(dogId).isPresent();
        if (dogExists) {
            Dog dogToSave = this.setPropertiesOnNewDog(inputDog, dogRepository.findById(dogId).get());
            try {
                dogRepository.save(dogToSave);
                return new ResponseEntity<>(dogToSave, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteDogById(int dogId){
        if (dogRepository.findById(dogId).isPresent()){
            dogRepository.deleteById(dogId);
            return new ResponseEntity<>("Dog has been deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Dog does not exist", HttpStatus.NOT_FOUND);
    }

    private Dog setPropertiesOnNewDog(Dog inputDog, Dog currentDog) {
        if (inputDog.getAge() != 0) {
            currentDog.setAge(inputDog.getAge());
        }
        if (inputDog.getName() != null){
            currentDog.setName(inputDog.getName());
        }
        if (inputDog.getColor() != null){
            currentDog.setColor(inputDog.getColor());
        }
        if (inputDog.getGender() != null){
            currentDog.setGender(inputDog.getGender());
        }
        if (inputDog.getBreed() != null){
            currentDog.setBreed(inputDog.getBreed());
        }
        if (inputDog.getWeight() != 0){
            currentDog.setWeight(inputDog.getWeight());
        }
        return currentDog;
    }

    private Dog addNewDog(Dog inputDog){
        Dog dog = new Dog();
        if (inputDog.getAge() != 0) {
            dog.setAge(inputDog.getAge());
        }
        if (inputDog.getName() != null){
            dog.setName(inputDog.getName());
        }
        if (inputDog.getColor() != null){
            dog.setColor(inputDog.getColor());
        }
        if (inputDog.getGender() != null){
            dog.setGender(inputDog.getGender());
        }
        if (inputDog.getBreed() != null){
            dog.setBreed(inputDog.getBreed());
        }
        if (inputDog.getWeight() != 0){
            dog.setWeight(inputDog.getWeight());
        }
        return dog;
    }
}
