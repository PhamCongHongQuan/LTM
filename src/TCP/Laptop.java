package TCP;

import java.io.Serializable;

public class Laptop implements Serializable {
    private static final long serialVersionUID = 20150711L;
    private int id;
    private String code;
    private String name;
    private int quantity;

    public Laptop (int id, String code, String name, int quantity) {
        this.id=id;
        this.code=code;
        this.name=name;
        this.quantity=quantity;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }



}
