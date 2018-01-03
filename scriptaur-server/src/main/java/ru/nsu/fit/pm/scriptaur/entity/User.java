package ru.nsu.fit.pm.scriptaur.entity;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;



    private String username;

    @Column(name = "trust_factor")
    private float trustFactor;

    @Column(name = "password_hash")
    private String hash;

    private String salt;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getTrustFactor() {
        return trustFactor;
    }

    public void setTrustFactor(float trustFactor) {
        this.trustFactor = trustFactor;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    public User(String username, float trustFactor, String hash, String salt) {
        this.username = username;
        this.trustFactor = trustFactor;
        this.hash = hash;
        this.salt = salt;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", trustFactor=" + trustFactor +
                ", hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
