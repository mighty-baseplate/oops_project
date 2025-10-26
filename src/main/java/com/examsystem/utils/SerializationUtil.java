package com.examsystem.utils;

import com.examsystem.entity.Exam;
import com.examsystem.entity.Student;
import java.io.*;

/**
 * Utility class for Object Serialization/Deserialization.
 * Demonstrates Java I/O and Serialization concepts.
 */
public class SerializationUtil {
    
    /**
     * Serializes an Exam object to file.
     * Demonstrates ObjectOutputStream usage.
     * 
     * @param exam Exam object to serialize
     * @param filePath Output file path
     * @throws IOException if I/O error occurs
     */
    public static void serializeExam(Exam exam, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(exam);
            System.out.println("[SERIALIZATION] Exam saved to: " + filePath);
        }
    }
    
    /**
     * Deserializes an Exam object from file.
     * Demonstrates ObjectInputStream usage.
     * 
     * @param filePath Input file path
     * @return Deserialized Exam object
     * @throws IOException if I/O error occurs
     * @throws ClassNotFoundException if class not found
     */
    public static Exam deserializeExam(String filePath) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            Exam exam = (Exam) ois.readObject();
            System.out.println("[DESERIALIZATION] Exam loaded from: " + filePath);
            return exam;
        }
    }
    
    /**
     * Serializes a Student object to file.
     */
    public static void serializeStudent(Student student, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(student);
            System.out.println("[SERIALIZATION] Student saved to: " + filePath);
        }
    }
    
    /**
     * Deserializes a Student object from file.
     */
    public static Student deserializeStudent(String filePath) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            Student student = (Student) ois.readObject();
            System.out.println("[DESERIALIZATION] Student loaded from: " + filePath);
            return student;
        }
    }
}
