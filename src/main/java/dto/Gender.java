package dto;

public class Gender {

    private String gender;
    private int member_id;

    public Gender(String gender, int member_id) {
        this.gender = gender;
        this.member_id = member_id;
    }

    public String getGender() {
        return gender;
    }

    public int getMember_id() {
        return member_id;
    }

    @Override
    public String toString() {
        return "| Gender " + gender + " : Team Size " + member_id + " |";

    }
}
