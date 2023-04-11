package com.example.findapotty.profile;

public class ReadWriteUserDetails
{
    public String fullName, email, dob, gender, mobile;

    public ReadWriteUserDetails(String textFullName, String textEmail, String textDob, String textGender, String textMobile)
    {
        this.fullName = textFullName;
        this.email = textEmail;
        this.dob = textDob;
        this.gender = textGender;
        this.mobile = textMobile;
    }

    public ReadWriteUserDetails() {}
}
