package Project;
import java.sql.*;
import java.util.Scanner;

public class Flight_Ticket_Booking {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/flight_reservation";
    static final String USER = "root";
    static final String PASS = "shree_r@16";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Create tables if not exists
            String createFlightsTableSQL = "CREATE TABLE IF NOT EXISTS flights (" +
                    "flight_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "flight_name VARCHAR(255)," +
                    "source VARCHAR(255)," +
                    "destination VARCHAR(255)," +
                    "date DATE," +
                    "seats_available INT)";
            stmt.executeUpdate(createFlightsTableSQL);

            String createTicketsTableSQL = "CREATE TABLE IF NOT EXISTS tickets (" +
                    "ticket_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "passenger_name VARCHAR(255)," +
                    "flight_name VARCHAR(255)," +
                    "source VARCHAR(255)," +
                    "destination VARCHAR(255)," +
                    "mobile_number VARCHAR(20)," +
                    "flight_id INT," +
                    "FOREIGN KEY (flight_id) REFERENCES flights(flight_id))";
            stmt.executeUpdate(createTicketsTableSQL);

            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("Main Menu:");
                System.out.println("1. Book a ticket");
                System.out.println("2. Check available flights");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        bookTicket(conn, scanner);
                        break;
                    case 2:
                        checkAvailableFlights(conn, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting program...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } while (choice != 3);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static void bookTicket(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter passenger name:");
        String passengerName = scanner.nextLine();

        System.out.println("Enter flight name:");
        String flightName = scanner.nextLine();

        System.out.println("Enter source:");
        String source = scanner.nextLine();

        System.out.println("Enter destination:");
        String destination = scanner.nextLine();

        System.out.println("Enter mobile number:");
        String mobileNumber = scanner.nextLine();

        System.out.println("Enter date (yyyy-mm-dd):");
        String date = scanner.nextLine();

        // Check available flights
        String availableFlightsQuery = "SELECT * FROM flights WHERE flight_name = ? AND source = ? AND destination = ? AND date = ?";
        PreparedStatement availableFlightsStmt = conn.prepareStatement(availableFlightsQuery);
        availableFlightsStmt.setString(1, flightName);
        availableFlightsStmt.setString(2, source);
        availableFlightsStmt.setString(3, destination);
        availableFlightsStmt.setString(4, date);
        ResultSet availableFlightsResult = availableFlightsStmt.executeQuery();

        if (availableFlightsResult.next()) {
            int flightId = availableFlightsResult.getInt("flight_id");
            int seatsAvailable = availableFlightsResult.getInt("seats_available");

            if (seatsAvailable > 0) {
                System.out.println("Flight available. Booking ticket...");

                // Decrement available seats count
                String updateSeatsQuery = "UPDATE flights SET seats_available = ? WHERE flight_id = ?";
                PreparedStatement updateSeatsStmt = conn.prepareStatement(updateSeatsQuery);
                updateSeatsStmt.setInt(1, seatsAvailable - 1);
                updateSeatsStmt.setInt(2, flightId);
                updateSeatsStmt.executeUpdate();

                // Inserting user input into the database
                String insertSQL = "INSERT INTO tickets (passenger_name, flight_name, source, destination, mobile_number, flight_id) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
                preparedStatement.setString(1, passengerName);
                preparedStatement.setString(2, flightName);
                preparedStatement.setString(3, source);
                preparedStatement.setString(4, destination);
                preparedStatement.setString(5, mobileNumber);
                preparedStatement.setInt(6, flightId);
                preparedStatement.executeUpdate();

                System.out.println("Ticket booked successfully!");
            } else {
                System.out.println("No seats available on this flight.");
            }
        } else {
            System.out.println("Flight not available.");
        }
    }

    private static void checkAvailableFlights(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter flight name:");
        String flightName = scanner.nextLine();

        System.out.println("Enter source:");
        String source = scanner.nextLine();

        System.out.println("Enter destination:");
        String destination = scanner.nextLine();

        System.out.println("Enter date (yyyy-mm-dd):");
        String date = scanner.nextLine();

        // Check available flights
        String availableFlightsQuery = "SELECT * FROM flights WHERE flight_name = ? AND source = ? AND destination = ? AND date = ?";
        PreparedStatement availableFlightsStmt = conn.prepareStatement(availableFlightsQuery);
        availableFlightsStmt.setString(1, flightName);
        availableFlightsStmt.setString(2, source);
        availableFlightsStmt.setString(3, destination);
        availableFlightsStmt.setString(4, date);
        ResultSet availableFlightsResult = availableFlightsStmt.executeQuery();

        if (availableFlightsResult.next()) {
            System.out.println("Flights available:");
            do {
                System.out.println("Flight ID: " + availableFlightsResult.getInt("flight_id") +
                        ", Flight Name: " + availableFlightsResult.getString("flight_name") +
                        ", Source: " + availableFlightsResult.getString("source") +
                        ", Destination: " + availableFlightsResult.getString("destination") +
                        ", Date: " + availableFlightsResult.getString("date") +
                        ", Seats Available: " + availableFlightsResult.getInt("seats_available"));
            } while (availableFlightsResult.next());
        } else {
            System.out.println("Flights not available.");
        }
    }
}
