package com.javarush.test.level21.lesson08.task01;

import java.io.*;
import java.lang.CloneNotSupportedException;
import java.lang.Cloneable;
import java.lang.Object;
import java.lang.Override;
import java.util.LinkedHashMap;
import java.util.Map;

/* Глубокое клонирование карты
Клонируйтие объект класса Solution используя глубокое клонирование.
Данные в карте users также должны клонироваться.
Метод main изменять нельзя.
*/
public class Solution {

    @Override
    private Solution clone() throws CloneNotSupportedException, IOException, ClassNotFoundException {

        ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(writeBuffer);
        outputStream.writeObject();
        outputStream.close();

        byte[] buffer = writeBuffer.toByteArray();
        ByteArrayInputStream readBuffer = new ByteArrayInputStream(buffer);
        ObjectInputStream inputStream = new ObjectInputStream(readBuffer);
        Solution objectCopy = (Solution)inputStream.readObject();
        return objectCopy;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Solution solution = new Solution();
        solution.users.put("Hubert", new User(172, "Hubert"));
        solution.users.put("Zapp", new User(41, "Zapp"));
        Solution clone = null;
        try {
            clone = solution.clone();

            System.out.println(solution);
            System.out.println(clone);

            System.out.println(solution.users);
            System.out.println(clone.users);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace(System.err);
        }
    }


    protected Map<String, User> users = new LinkedHashMap();

    public static class User {
        int age;
        String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }
}
