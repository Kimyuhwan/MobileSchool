package com.example.MobileSchool.Model;

/**
 * User: yuhwan
 * Date: 13. 10. 19
 * Time: 오후 4:40
 */
public class MyInfo {
    // Model Class which represents myself.
    private String account_id;
    private String unique_id;
    private String name;
    private String phoneNumber;
    private int age;
    private String gender;
    private String type;

    public MyInfo(String account_id, String unique_id, String name, String phoneNumber, int age, String gender, String type) {
        this.account_id = account_id;
        this.unique_id = unique_id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.type = type;
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }
}
