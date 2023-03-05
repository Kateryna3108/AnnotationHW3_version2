import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        Student student = new Student("Katia", 40, "Java");
        File savedFile = serialization(student);
        Student savedStudent = deserialization(savedFile, Student.class);
        System.out.println("Student before: " + student);
        System.out.println("Student after: " + savedStudent);
    }

    public static File serialization (Object obj) throws IllegalAccessException {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        File file = new File("/Users/katerynahavrylova/IdeaProjects/AnnotationHW3/save.csv");
        String fieldsInFile = "";
        for (Field field: fields) {
            if(field.isAnnotationPresent(Save.class)){
                field.setAccessible(true);
                fieldsInFile = fieldsInFile
                        + field.getName() + ";"
                        + field.getType() + ";"
                        + field.get(obj) + ";"
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

    public static <T> T deserialization (File savedFile, Class<T> cls) throws IOException, IllegalAccessException, InstantiationException {
        Object savedStudent = cls.newInstance();
        System.out.println(savedStudent);
        Field[] fields = cls.getDeclaredFields();
        System.out.println(Arrays.toString(fields));
        Scanner sc = new Scanner(savedFile);
        for (; sc.hasNextLine();) {
            String[] fieldsFromFile = sc.nextLine().split(";");
            for (Field field: fields) {
                if(field.getName().equals(fieldsFromFile[0])){
                    field.setAccessible(true);
                    if(field.getType().toString().equals(fieldsFromFile[1])){
                        if(field.getType() == int.class) {
                            field.set(savedStudent, Integer.parseInt(fieldsFromFile[2]));
                        } else if(field.getType().equals(String.class)) {
                            field.set
                                    (savedStudent, fieldsFromFile[2]);
                        }
                    }
                }
            }
        }
        return (T) savedStudent;
    }
}