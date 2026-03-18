package com.pao.laboratory03.service;

import com.pao.laboratory03.exceptions.StudentNotFoundException;
import com.pao.laboratory03.model.Student;
import com.pao.laboratory03.model.Subject;

import java.util.*;

public class StudentService {
    private static StudentService instance;

    private List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Un student cu numele '" + name + "' există deja!");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți înregistrați.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți înregistrați.");
            return;
        }
        List<Student> sortedStudents = new ArrayList<>(students);

        sortedStudents.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        System.out.println("--- Top Studenți ---");
        for (Student s : sortedStudents) {
            System.out.printf("%s - Medie: %.2f%n", s.getName(), s.getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                Double grade = entry.getValue();

                sums.put(subject, sums.getOrDefault(subject, 0.0) + grade);
                counts.put(subject, counts.getOrDefault(subject, 0) + 1);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Subject subject : sums.keySet()) {
            averages.put(subject, sums.get(subject) / counts.get(subject));
        }

        return averages;
    }
}