package com.example.MobileSchool.Model;

/**
 * User: yuhwan
 * Date: 13. 10. 19
 * Time: 오후 4:41
 */
public class PartnerInfo {
    // Model Class which represents Partner
    private String unique_id;
    private String name;
    private String phoneNumber;
    private int age;
    private String gender;
    private String type;

    public PartnerInfo(String unique_id, String name, String phoneNumber, int age, int gender, String type) {
        this.unique_id = unique_id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        if(gender == 1)
            this.gender = "male";
        else
            this.gender = "female";
        this.type = type;
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
