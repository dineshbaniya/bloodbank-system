package com.bloodbank.dto;

public class HospitalSignupRequest {
    private String hospitalName;
    private String hospitalAddress;
    private String staffFullName;
    private String email;
    private String password;

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public String getStaffFullName() { return staffFullName; }
    public void setStaffFullName(String staffFullName) { this.staffFullName = staffFullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}