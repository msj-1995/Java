package java;

public class Student {
    @Override
    public String toString() {
        return "hello";
    }

    public static void main(String[] args) {
        Student student = new Student();
        System.out.println(student.toString());
    }
}