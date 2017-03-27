package org.randomheroes.bean;

/**
 * Created by jixu_m on 2017/1/17.
 */
public class User {
    private String user_id;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike)) return false;

        User user = (User) o;

        if (!user_id.equals(user.user_id)) return false;

        return true;
    }
}
