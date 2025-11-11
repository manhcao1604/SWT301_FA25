package com.bookstore.selenium.utils;

import com.github.javafaker.Faker;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String generateBookTitle() {
        return faker.book().title();
    }

    public static String generateAuthorName() {
        return faker.book().author();
    }

    public static String generateIsbn() {
        return faker.code().isbn13();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generateAddress() {
        return faker.address().fullAddress();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    public static double generatePrice() {
        return faker.number().randomDouble(2, 10, 100);
    }

    public static int generateQuantity() {
        return faker.number().numberBetween(1, 10);
    }
}