import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<OrderDetail> getCart(int costumerId) {
        try (Connection connection = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));

             PreparedStatement statement = connection.prepareStatement("SELECT \n" +
                     "    Shoe.shoe_id,\n" +
                     "    Brand.brand_name,\n" +
                     "    Model.model_name,\n" +
                     "    Color.color_name,\n" +
                     "    Category.category_name,\n" +
                     "    GenderCategory.gender_category_type,\n" +
                     "    Shoe.size,\n" +
                     "    Shoe.price,\n" +
                     "    Shoe.stock,\n" +
                     "    `Order`.order_status,\n" +
                     "    OrderDetails.quantity,\n" +
                     "    `Order`.order_id\n" +
                     "FROM OrderDetails\n" +
                     "    INNER JOIN Shoe ON OrderDetails.shoe_id = Shoe.shoe_id\n" +
                     "    INNER JOIN Brand ON Shoe.brand_id = Brand.brand_id\n" +
                     "    INNER JOIN Model ON Shoe.model_id = Model.model_id\n" +
                     "    INNER JOIN Color ON Shoe.color_id = Color.color_id\n" +
                     "    INNER JOIN Category ON Shoe.category_id = Category.category_id\n" +
                     "    INNER JOIN GenderCategory ON Shoe.gender_category_id = GenderCategory.gender_category_id\n" +
                     "    INNER JOIN `Order` ON OrderDetails.order_id = `Order`.order_id\n" +
                     "    INNER JOIN Customer ON `Order`.customer_id = Customer.customer_id\n" +
                     "WHERE Customer.customer_id = ? \n" +
                     "AND `Order`.order_status = 'AKTIV'\n");
        ) {

            statement.setInt(1, costumerId);

            ResultSet rs = statement.executeQuery();

            List<OrderDetail> orderDetails = new ArrayList<>();
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail( rs.getInt("Order.order_id"),
                        new Shoe(rs.getInt("Shoe.shoe_id"),
                            rs.getString("Brand.brand_name"),
                            rs.getString("Model.model_name"),
                            rs.getString("Color.color_name"),
                            rs.getString("Category.category_name"),
                            rs.getString("GenderCategory.gender_category_type"),
                            rs.getInt("Shoe.size"),
                            rs.getDouble("Shoe.price"),
                            rs.getInt("Shoe.stock")),
                        rs.getInt("OrderDetails.quantity"));

                orderDetails.add(orderDetail);
            }

            return orderDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCart(int costumerId, int shoeId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));

             CallableStatement statement = connection.prepareCall("{CALL addToCart(?,?,?,?)}")
        ) {

            statement.setInt(1, costumerId);
            statement.setInt(2, shoeId);
            statement.setNull(3, java.sql.Types.NULL);
            statement.registerOutParameter(4, java.sql.Types.INTEGER);

            statement.executeQuery();
        }
    }

    public List<Shoe> getShoes() {
        try (Connection connection = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));

             PreparedStatement statement = connection.prepareStatement("SELECT \n" +
                     "        shoe_id AS 'id',\n" +
                     "        Brand.brand_name AS 'brand',\n" +
                     "        Model.model_name AS 'model',\n" +
                     "        Color.color_name AS 'color',\n" +
                     "        Category.category_name AS 'category',\n" +
                     "        GenderCategory.gender_category_type AS 'gender',\n" +
                     "        size AS 'size',\n" +
                     "        price AS 'price',\n" +
                     "        stock AS 'stock'\n" +
                     "    FROM Shoe\n" +
                     "    INNER JOIN Brand ON Shoe.brand_id = Brand.brand_id\n" +
                     "    INNER JOIN Model ON Shoe.model_id = Model.model_id\n" +
                     "    INNER JOIN Color ON Shoe.color_id = Color.color_id\n" +
                     "    INNER JOIN Category ON Shoe.category_id = Category.category_id\n" +
                     "    INNER JOIN GenderCategory ON Shoe.gender_category_id = GenderCategory.gender_category_id")
        ) {
            ResultSet rs = statement.executeQuery();

            List<Shoe> shoes = new ArrayList<>();
            while (rs.next()) {
                Shoe shoe = new Shoe(rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("color"),
                        rs.getString("category"),
                        rs.getString("gender"),
                        rs.getInt("size"),
                        rs.getDouble("price"),
                        rs.getInt("stock"));

                shoes.add(shoe);
            }

            return shoes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Customer> getCustomer(String email, String password) {
        try (Connection connection = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password"));

             PreparedStatement statement = connection.prepareStatement("select \n" +
                     "    customer_id,\n" +
                     "    first_name,\n" +
                     "    last_name,\n" +
                     "    email,\n" +
                     "    city,\n" +
                     "    personnummer\n" +
                     "    from Customer\n" +
                     "    where Customer.email = ?" +
                     "    and Customer.password_hash = ?")
        ) {

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(rs.getInt("customer_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("city"), rs.getString("personnummer"));
                return Optional.of(customer);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



//    public void getCustomer(){
//        try (Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"))) {
//            String sql = "{CALL getCustomer(?)}";
//            try (CallableStatement stmt = con.prepareCall(sql)) {
//                stmt.setInt(1, 1);
//                stmt.execute();
//                System.out.println("-------------------------------------------------------------------------------------------");
//                System.out.printf("%-15s %-15s %-30s %-15s %-15s%n",
//                        "Förnamn", "Efternamn", "Epost", "Stad", "Personnummer");
//                System.out.println("-------------------------------------------------------------------------------------------");
//                ResultSet rs = stmt.getResultSet();
//                while (rs.next()) {
//                    String firstName = rs.getString("first_name");
//                    String lastName = rs.getString("last_name");
//                    String email = rs.getString("email");
//                    String city = rs.getString("city");
//                    String personnummer = rs.getString("personnummer");
//
//                    System.out.printf("%-15s %-15s %-30s %-15s %-15s%n",
//                            firstName, lastName, email, city, personnummer);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
