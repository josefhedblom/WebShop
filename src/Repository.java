import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Repository {
    Properties p = new Properties();

    public Repository() {
        try(FileInputStream fis = new FileInputStream("src/config.properties")){
            p.load(fis);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String email, String password) {
        try (Connection connection = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));

             PreparedStatement statement = connection.prepareStatement("SELECT customer_id FROM Customer WHERE email = ? AND password_hash = ?;")
        ) {

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCustomer(){
        try (Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"))) {
            String sql = "{CALL getCustomer(?)}";
            try (CallableStatement stmt = con.prepareCall(sql)) {
                stmt.setInt(1, 1);
                stmt.execute();
                System.out.println("-------------------------------------------------------------------------------------------");
                System.out.printf("%-15s %-15s %-30s %-15s %-15s%n",
                        "Förnamn", "Efternamn", "Epost", "Stad", "Personnummer");
                System.out.println("-------------------------------------------------------------------------------------------");
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String city = rs.getString("city");
                    String personnummer = rs.getString("personnummer");

                    System.out.printf("%-15s %-15s %-30s %-15s %-15s%n",
                            firstName, lastName, email, city, personnummer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCustomerOrderHistory(int id){
        try (Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"))) {
            String sql = "{CALL GetCustomerOrderHistory(?)}";
            try (CallableStatement stmt = con.prepareCall(sql)) {
                stmt.setInt(1, id);
                stmt.execute();
                System.out.println("Lista över kunders beställningar:");
                System.out.println("----------------------------------------------------------------------------------------");
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                        "Märke", "Färg", "Kategori", "Sko typ", "Storlek","Pris");
                System.out.println("----------------------------------------------------------------------------------------");
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    String brand = rs.getString("Märke");
                    String color = rs.getString("Färg");
                    String category = rs.getString("Kategori");
                    String gender_category = rs.getString("Sko typ");
                    int size = rs.getInt("Storlek");
                    int price = rs.getInt("Pris");

                    System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15d%n",
                            brand, color, category,gender_category, size, price);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getShoes(String brand, String color, String category, String size){
        try (Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"))) {
            String sql = "{CALL GetShoes(?,?,?,?)}";
            try (CallableStatement stmt = con.prepareCall(sql)) {
                stmt.setString(1, brand);
                stmt.setString(2, color);
                stmt.setString(3, category);
                stmt.setString(4, size);
                stmt.execute();
                System.out.println("--------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n",
                        "Märke", "Modell", "Färg", "Kategori", "Sko typ", "Storlek", "Pris", "Antal");
                System.out.println("--------------------------------------------------------------------------------------------------------------------------");
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    String brand_name = rs.getString("Märke");
                    String model_name = rs.getString("Modell");
                    String color_name = rs.getString("Färg");
                    String category_name = rs.getString("Kategori");
                    String gender_category_type = rs.getString( "Sko typ");
                    size = rs.getString("Storlek");
                    int price = rs.getInt("Pris");
                    int stock = rs.getInt("Antal");
                    System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15d %-15d%n",
                            brand_name ,model_name, color_name, category_name, gender_category_type, size, price, stock);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
