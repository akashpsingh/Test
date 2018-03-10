package in.wynk.expenseapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash on 10/03/18.
 */

public class ExpenseStore {

    private static final ExpenseStore sInstance = new ExpenseStore();

    private User me;

    private List<User> friends = new ArrayList<>();

    public ExpenseStore() {
        me = new User("Akash (You)");
    }

    public static ExpenseStore getInstance() {
        return sInstance;
    }

    public void updateUserBalance(User user) {
        if (user.equals(me)) {
            updateMyBalance(user.getCurrentBalance());
            return;
        }

        if (friends.contains(user)) {
            User storeUser = friends.get(friends.indexOf(user));
            storeUser.setCurrentBalance(storeUser.getCurrentBalance() + user.getCurrentBalance());
        } else {
            friends.add(user);
        }

    }

    private void updateMyBalance(double balance) {
        me.setCurrentBalance(me.getCurrentBalance() + balance);
    }

    public List<User> getAllUsersToShow() {
        List<User> allUsers = new ArrayList<>(friends);
        allUsers.add(0, me);
        return allUsers;
    }

    public List<User> getFriends() {
        return friends;
    }

    public User getMe() {
        return me;
    }
}
