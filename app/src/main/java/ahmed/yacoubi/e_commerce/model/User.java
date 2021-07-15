package ahmed.yacoubi.e_commerce.model;

public class User {
    private String id;
    private String name;
    private double money;
    private String password;
    private String email;
    private String phone;
    private double spentMoney;

    public User() {
    }

    public User(String id, String name, double money, String password, String email, String phone, double spentMoney) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.spentMoney = spentMoney;
    }

    public double getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(double spentMoney) {
        this.spentMoney = spentMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
