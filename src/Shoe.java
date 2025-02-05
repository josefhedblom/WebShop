public class Shoe {
    private int id;
    private String brand;
    private String model;
    private String color;
    private String category;
    private String gender;
    private int size;
    private double price;
    private int stock;

    public Shoe(int id, String brand, String model, String color, String category, String gender, int size, double price, int stock) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.category = category;
        this.gender = gender;
        this.size = size;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getCategory() {
        return category;
    }

    public String getGender() {
        return gender;
    }

    public int getSize() {
        return size;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Shoe{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", category='" + category + '\'' +
                ", gender='" + gender + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
