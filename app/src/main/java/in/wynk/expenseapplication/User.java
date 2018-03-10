package in.wynk.expenseapplication;

/**
 * Created by Akash on 10/03/18.
 */

public class User {

    private String name;

    private double currentBalance = 0;

    public User(String name) {
        this.name = name;
    }

    public User(String name, double currentBalance) {
        this.name = name;
        this.currentBalance = currentBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
