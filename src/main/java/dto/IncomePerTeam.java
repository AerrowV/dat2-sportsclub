package dto;

public class IncomePerTeam {

    private String team_id;
    private int price;

    public IncomePerTeam(String team_id, int price) {
        this.team_id = team_id;
        this.price = price;

    }
    public String getTeam_id() {
        return team_id;
    }

    public int getPrice() {
        return price;

    }

    @Override
    public String toString() {
        return "Team " + team_id + ": Income " + price;
    }
}
