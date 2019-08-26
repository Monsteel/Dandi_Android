package org.techtown.schooler.Model;

public class SchoolInformations {

    private String school_name;
    private String school_locate;
    private String office_code;
    private String school_code;
    private String school_kind;

    public SchoolInformations(String school_name, String school_locate, String office_code,String school_code,String school_kind) {
        this.school_name = school_name;
        this.school_locate = school_locate;
        this.office_code = office_code;
        this.school_code = school_code;
        this.school_kind = school_kind;
    }

    public String getOffice_code() {
        return office_code;
    }

    public String getSchool_code() {
        return school_code;
    }

    public String getSchool_kind() {
        return school_kind;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getSchool_locate() {
        return school_locate;
    }

    public void setOffice_code(String office_code) {
        this.office_code = office_code;
    }

    public void setSchool_code(String school_code) {
        this.school_code = school_code;
    }

    public void setSchool_locate(String school_locate) {
        this.school_locate = school_locate;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public void setSchool_kind(String school_kind) {
        this.school_kind = school_kind;
    }
}