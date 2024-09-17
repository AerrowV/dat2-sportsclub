import dto.Gender;
import dto.IncomePerTeam;
import dto.Participant;
import entities.Member;
import entities.Registration;
import persistence.Database;
import persistence.MemberMapper;
import persistence.RegistrationMapper;

import java.util.List;

public class Main {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/sportsclub";

    public static void main(String[] args) {

        Database db = new Database(USER, PASSWORD, URL);
        MemberMapper memberMapper = new MemberMapper(db);
        RegistrationMapper registrationMapper = new RegistrationMapper(db);
        List<Member> members = memberMapper.getAllMembers();
        List<Participant> participants = memberMapper.participantCountPerTeam();
        List<Participant> participantPerSport = memberMapper.participantCountPerSport();
        List<Gender> numberOfFemaleAndMalesInTheClub = memberMapper.genders();
        List<IncomePerTeam> income = memberMapper.totalIncomePerTeam();
        List<IncomePerTeam> avgPrice = memberMapper.averagePricePerTeam();
        List<Registration> registrations = registrationMapper.getAllRegistrations();

        Registration newRegistration = new Registration(10, "ten01", 100);
        Registration result = registrationMapper.addToTeam(newRegistration);

        showMembers(members);
        showMemberById(memberMapper, 8);
        showParticipantsPerTeam(participants);
        showParticipantsPerSport(participantPerSport);
        showAmountOfGenders(numberOfFemaleAndMalesInTheClub);
        totalIncomeForAllTeams(memberMapper);
        totalIncomePerTeam(income);
        averageIncomePerTeam(avgPrice);
        registrationList(registrations);

        /*  
            int newMemberId = insertMember(memberMapper);
            deleteMember(newMemberId, memberMapper);
            showMembers(members);
            updateMember(13, memberMapper);
        */
    }

    private static void deleteMember(int memberId, MemberMapper memberMapper) {
        if (memberMapper.deleteMember(memberId)) {
            System.out.println("Member with id = " + memberId + " is removed from DB");
        }
    }

    private static int insertMember(MemberMapper memberMapper) {
        Member m1 = new Member("Ole Olsen", "Banegade 2", 3700, "Rønne", "m", 1967);
        Member m2 = memberMapper.insertMember(m1);
        showMemberById(memberMapper, m2.getMemberId());
        return m2.getMemberId();
    }

    private static void updateMember(int memberId, MemberMapper memberMapper) {
        Member m1 = memberMapper.getMemberById(memberId);
        m1.setYear(1970);
        if (memberMapper.updateMember(m1)) {
            showMemberById(memberMapper, memberId);
        }
    }

    private static void showMemberById(MemberMapper memberMapper, int memberId) {
        System.out.println("\n***** Medlem nr. " + memberId + " *******");
        System.out.println(memberMapper.getMemberById(memberId).toString());
    }

    private static void showMembers(List<Member> members) {
        System.out.println("\n***** Alle medlemmer *******");
        for (Member member : members) {
            System.out.println(member.toString());
        }
    }

    private static void showParticipantsPerTeam(List<Participant> participants) {
        System.out.println("\n***** Antallet af medlemmer pr. hold *******");
        for (Participant list : participants) {
            System.out.println(list.toString());
        }
    }

    private static void showParticipantsPerSport(List<Participant> participantPerSport) {
        System.out.println("\n***** Antallet af medlemmer pr. sport *******");
        for (Participant sport : participantPerSport) {
            System.out.println(sport.toString());
        }
    }

    private static void showAmountOfGenders(List<Gender> genderAmount) {
        System.out.println("\n***** Antallet af mænd og kvinder *****");
        for (Gender gender : genderAmount) {
            System.out.println(gender.toString());
        }
    }

    private static void totalIncomeForAllTeams(MemberMapper memberMapper) {
        int totalIncome = memberMapper.totalIncomeForAllTeams();
        System.out.println("\n***** Total indkomst *****\n" + totalIncome + " kr,-");
    }

    private static void totalIncomePerTeam(List<IncomePerTeam> incomePerTeams) {
        System.out.println("\n***** Indkomst pr. hold *****");
        for (IncomePerTeam income : incomePerTeams) {
            System.out.println(income.toString());
        }
    }

    private static void averageIncomePerTeam(List<IncomePerTeam> averagePricePerTeams) {
        System.out.println("\n***** Gennemsnitlig pris pr. hold *****");
        for (IncomePerTeam price : averagePricePerTeams) {
            System.out.println(price.toString());
        }
    }

    private static void registrationList(List<Registration> registrationList) {
        System.out.println("\n***** Registration liste *****");
        for (Registration registration : registrationList) {
            System.out.println(registration.toString());
        }
    }
}