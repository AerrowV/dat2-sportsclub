package dto;

public class Participant {

    private String team_id;
    private int member_id;


    public Participant(String team_id, int member_id) {
        this.team_id = team_id;
        this.member_id = member_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public int getMember_id() {
        return member_id;
    }

    @Override
    public String toString() {
        return "| Team " + team_id + " : Team Size " + member_id + " |";
    }
}
