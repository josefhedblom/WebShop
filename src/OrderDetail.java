public class OrderDetail {
    private int orderId;
    private Shoe shoe;
    private int quantity;

    public OrderDetail(int orderId, Shoe shoe, int quantity) {
        this.orderId = orderId;
        this.shoe = shoe;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public int getQuantity() {
        return quantity;
    }
}
