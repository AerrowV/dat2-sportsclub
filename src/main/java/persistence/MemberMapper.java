package persistence;

import dto.Gender;
import dto.IncomePerTeam;
import dto.Participant;
import entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberMapper {

    private Database database;

    public MemberMapper(Database database) {
        this.database = database;
    }

    public List<Member> getAllMembers() {

        List<Member> memberList = new ArrayList<>();

        String sql = "select member_id, name, address, m.zip, gender, city, year " +
                "from member m inner join zip using(zip) " +
                "order by member_id";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int memberId = rs.getInt("member_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zip = rs.getInt("zip");
                    String city = rs.getString("city");
                    String gender = rs.getString("gender");
                    int year = rs.getInt("year");
                    memberList.add(new Member(memberId, name, address, zip, city, gender, year));
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return memberList;
    }

    public Member getMemberById(int memberId) {
        Member member = null;

        String sql = "select member_id, name, address, m.zip, gender, city, year " +
                "from member m inner join zip using(zip) " +
                "where member_id = ?";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, memberId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    memberId = rs.getInt("member_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zip = rs.getInt("zip");
                    String city = rs.getString("city");
                    String gender = rs.getString("gender");
                    int year = rs.getInt("year");
                    member = new Member(memberId, name, address, zip, city, gender, year);
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int a = 1;
        return member;
    }

    public boolean deleteMember(int member_id) {
        boolean result = false;
        String sql = "delete from member where member_id = ?";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, member_id);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            // TODO: Make own throwable exception and let it bubble upwards
            throwables.printStackTrace();
        }
        return result;
    }

    public Member insertMember(Member member) {
        boolean result = false;
        int newId = 0;
        String sql = "insert into member (name, address, zip, gender, year) values (?,?,?,?,?)";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, member.getName());
                ps.setString(2, member.getAddress());
                ps.setInt(3, member.getZip());
                ps.setString(4, member.getGender());
                ps.setInt(5, member.getYear());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
                ResultSet idResultset = ps.getGeneratedKeys();
                if (idResultset.next()) {
                    newId = idResultset.getInt(1);
                    member.setMemberId(newId);
                } else {
                    member = null;
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            // TODO: Make own throwable exception and let it bubble upwards
            throwables.printStackTrace();
        }
        return member;
    }

    public boolean updateMember(Member member) {
        boolean result = false;
        String sql = "update member " +
                "set name = ?, address = ?, zip = ?, gender = ?, year = ? " +
                "where member_id = ?";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, member.getName());
                ps.setString(2, member.getAddress());
                ps.setInt(3, member.getZip());
                ps.setString(4, member.getGender());
                ps.setInt(5, member.getYear());
                ps.setInt(6, member.getMemberId());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
            } catch (SQLException throwables) {
                // TODO: Make own throwable exception and let it bubble upwards
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            // TODO: Make own throwable exception and let it bubble upwards
            throwables.printStackTrace();
        }
        return result;
    }

    public List<Participant> participantCountPerTeam() {
        List<Participant> participants = new ArrayList<>();
        String sql = "select team.team_id, count(registration.member_id) as participant_count " +
                "from team " +
                "join registration " +
                "on team.team_id = registration.team_id " +
                "group by team.team_id ";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String teamId = rs.getString("team_id");
                    int participant = rs.getInt("participant_count");
                    participants.add(new Participant(teamId, participant));

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return participants;
    }

    public List<Participant> participantCountPerSport() {
        List<Participant> participantsPerSport = new ArrayList<>();
        String sql = "select sport.sport, count(registration.member_id) as participant_sport " +
                "from sport " +
                "join team on sport.sport_id = team.sport_id " +
                "join registration on team.team_id = registration.team_id " +
                "group by sport.sport";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String sportName = rs.getString("sport");
                    int participantPerSport = rs.getInt("participant_sport");
                    participantsPerSport.add(new Participant(sportName, participantPerSport));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return participantsPerSport;
    }

    public List<Gender> genders() {
        List<Gender> genderAmount = new ArrayList<>();
        String sql = "select gender, count(member_id) as gendercount " +
                "from member " +
                "group by gender";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String gender = rs.getString("gender");
                    int amountOfGenders = rs.getInt("gendercount");
                    genderAmount.add(new Gender(gender, amountOfGenders));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genderAmount;
    }

    public int totalIncomeForAllTeams() {
        int totalIncome = 0;
        String sql = "select sum(registration.price) as total_income " +
                "from registration ";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalIncome = rs.getInt("total_income");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalIncome;
    }

    public List<IncomePerTeam> totalIncomePerTeam() {
        List<IncomePerTeam> teamIncomeList = new ArrayList<>();
        String sql = "select registration.team_id, sum(registration.price) as total_teamIncome " +
                "from registration " +
                "group by team_id";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String team_id = rs.getString("team_id");
                    int totalIncome = rs.getInt("total_teamIncome");
                    teamIncomeList.add(new IncomePerTeam(team_id, totalIncome));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teamIncomeList;
    }

    public List<IncomePerTeam> averagePricePerTeam() {
        List<IncomePerTeam> teamPriceList = new ArrayList<>();
        String sql = "select registration.team_id, avg(registration.price) as avg_teamPrice " +
                "from registration " +
                "group by team_id";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String team_id = rs.getString("team_id");
                    int avgIncome = rs.getInt("avg_teamPrice");
                    teamPriceList.add(new IncomePerTeam(team_id, avgIncome));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teamPriceList;
    }
}