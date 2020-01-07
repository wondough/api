package wondough;

public class WondoughUser {
    private int id;
    private String username;
    private String password;
    private String salt;
    private int iterations;
    private int keySize;

    public WondoughUser(int id, String username) {
        this.username = username;
    }

    public int getID() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHashedPassword() {
        return this.password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public int getIterations() {
        return this.iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getKeySize() {
        return this.keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }
}
