package cn.maiaimei.example.builder;

/**
 * lombok 的 @Builder
 */
public class User {
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User() {

    }

    public User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
    }

    static class UserBuilder {
        private String username;
        private String password;

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
