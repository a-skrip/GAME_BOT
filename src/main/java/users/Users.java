package users;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Users {
    private  Map<Long, String> usersMap = new HashMap<>();

    public Users() {
        usersMap.put(5404609032L, "Skriplex");
        usersMap.put(6584692810L, "Egorka");
//        usersMap.put(1653294966L, "Olechka");
        usersMap.put(5388561217L, "Vikusik");
    }

}
