import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException {

        Student student = new Student("Katia", 40, "Java");
        File savedFile = serialization(student);
        Student savedStudent = deserialization(savedFile);
        System.out.println(savedStudent);
    }

    public static File serialization (Student student) throws IllegalAccessException {
        Class<?> cls = student.getClass();
        Field[] fields = cls.getDeclaredFields();
        File file = new File("/Users/katerynahavrylova/IdeaProjects/AnnotationHW3/save.csv");
        String fieldsInFile = "";
        for (Field field: fields) {
            if(field.isAnnotationPresent(Save.class)){
                field.setAccessible(true);
                fieldsInFile = fieldsInFile
                        + field.getName() + ";"
                        + field.getType() + ";"
                        + field.get(student) + ";"
                        + System.lineSeparator();
            }
        }
        try(PrintWriter pw = new PrintWriter(file)) {
            pw.println(fieldsInFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Student deserialization (File savedFile) throws IOException, IllegalAccessException {
        Student savedStudent = new Student();
        Class<?> cls = Student.class;
        Field[] fields = cls.getDeclaredFields();
        Scanner sc = new Scanner(savedFile);
        for (; sc.hasNextLine();) {
            String[] fieldsFromFile = sc.nextLine().split(";");
            for (Field field: fields) {
                if(field.getName().equals(fieldsFromFile[0])){
                    field.setAccessible(true);
                    if(field.getType().toString().equals(fieldsFromFile[1])){
                        if(field.getType() == int.class) {
                            field.set(savedStudent, Integer.parseInt(fieldsFromFile[2]));
                        } else
                        field.set(savedStudent, fieldsFromFile[2]);
                    }
                }
            }
        }
        return savedStudent;
    }
}