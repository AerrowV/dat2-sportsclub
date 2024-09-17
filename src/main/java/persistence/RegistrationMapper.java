package persistence;

import entities.Member;
import entities.Registration;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper {

    Database database;

    public RegistrationMapper(Database database) {
        this.database = database;
    }

    public Registration addToTeam(Registration registration) {
        String sql = "insert into registration (member_id, team_id, price) values (?,?,?)";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, registration.getMember_id());
                ps.setString(2, registration.getTeam_id());
                ps.setInt(3, registration.getPrice());

                int rowsAffected = ps.executeUpdate();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return registration;
    }

    public List<Registration> getAllRegistrations() {

        List<Registration> registerList = new ArrayList<>();

        String sql = "select team_id, member_id, price " +
                "from registration";

        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int memberId = rs.getInt("member_id");
                    String team_id = rs.getString("team_id");
                    int price = rs.getInt("price");

                    registerList.add(new Registration(memberId, team_id, price));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return registerList;
    }
}
