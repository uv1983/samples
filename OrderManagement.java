import java.io.*;
import java.util.*;

interface IOrder {
    void setName(String name);
    String getName();
    void setPrice(int price);
    int getPrice();
}

interface IOrderSystem {
    void addToCart(IOrder order);
    void removeFromCart(IOrder order);
    int calculateTotalAmount();
    Map<String, Integer> categoryDiscounts();
    Map<String, Integer> cartItems();
}

class Order implements IOrder {
    private String name;
    private int price;
	
    @Override
	public void setName(String name) {
        this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public int getPrice() {
		return price;
	}

}

class OrderSystem implements IOrderSystem {
    private List<IOrder> items = new ArrayList<>();
    private Map<String, Integer> itemMap = new TreeMap<>();;
    private Map<String, Integer> categoryDiscounts = new LinkedHashMap<>();



	@Override
	public void addToCart(IOrder order) {
        itemMap.put(order.getName(), itemMap.getOrDefault(order.getName(), 0) + 1);
        items.add(order);
	}

	@Override
	public void removeFromCart(IOrder order) {
        if(itemMap.containsKey(order.getName()) && itemMap.get(order.getName()) > 1) {
            itemMap.put(order.getName(), itemMap.get(order.getName()) - 1);
        } else {
            itemMap.remove(order.getName());
        }
        for(IOrder orderFromList: items) {
            if(orderFromList.getName().equals(order.getName())) {
                items.remove(orderFromList);
            }
        }
	}

	@Override
	public int calculateTotalAmount() {
        int totalAmount = 0;
        for(IOrder order: this.items) {
            // determine category
            String category = determineCategory(order.getPrice());
            // get discount
            int discount = (int) getDiscount(category, order);
            
            totalAmount =  totalAmount + (order.getPrice() - discount);
        }
		return totalAmount;
	}

	private String determineCategory(int price) {
        
        if(price <=10) 
            return "Cheap";
        if(price > 10 && price <= 20)
            return "Moderate";
		return "Expensive";
	}

	private double getDiscount(String category, IOrder order) {
        
        if(category.equals("Cheap")) 
            return order.getPrice()*0.1;
        if(category.equals("Moderate")) 
            return order.getPrice()*0.2;
        if(category.equals("Expensive")) 
            return order.getPrice()*0.3;
		return 0;
	}

	@Override
	public Map<String, Integer> categoryDiscounts() {
        for(IOrder order: this.items) {
            // determine category
            String category = determineCategory(order.getPrice());
            // get discount
            int discount = (int) getDiscount(category, order);
            // populate the map
            categoryDiscounts.put(category, categoryDiscounts.getOrDefault(category,
             0) + discount);
        }
		return categoryDiscounts;
	}

	@Override
	public Map<String, Integer> cartItems() {
		return this.itemMap;
	}
    
    
}
public class Solution {
    public static void main(String[] args) throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter textWriter = new PrintWriter(System.out);

        IOrderSystem orderSystem = new OrderSystem();
        int oCount = Integer.parseInt(br.readLine().trim());
        for (int i = 1; i <= oCount; i++) {
            String[] a = br.readLine().trim().split(" ");
            IOrder e = new Order();
            e.setName(a[0]);
            e.setPrice(Integer.parseInt(a[1]));
            orderSystem.addToCart(e);
        }
        int totalAmount = orderSystem.calculateTotalAmount();
        textWriter.println("Total Amount: " + totalAmount);

        Map<String, Integer> categoryDiscounts = orderSystem.categoryDiscounts();
        for (Map.Entry<String, Integer> entry : categoryDiscounts.entrySet()) {
            if(entry.getValue() > 0) {
                textWriter.println(entry.getKey() + " Category Discount: " + entry.getValue());
            }
        }

        Map<String, Integer> cartItems = orderSystem.cartItems();
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            textWriter.println(entry.getKey() + " (" + entry.getValue() + " items)");
        }

        textWriter.flush();
        textWriter.close();
    }
}
