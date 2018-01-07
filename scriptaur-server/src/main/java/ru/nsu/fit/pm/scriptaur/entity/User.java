package ru.nsu.fit.pm.scriptaur.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {


    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;

    @Column(name = "trust_factor")
    private float trustFactor;

    @Column(name = "trust_factor_updated", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trustFactorUpdated;

    @Column(name = "password_hash")
    private String hash;

    private String salt;


    public Date getTrustFactorUpdated() {
        return trustFactorUpdated;
    }

    public void setTrustFactorUpdated(Date trustFactorUpdated) {
        this.trustFactorUpdated = trustFactorUpdated;
    }

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
        return "SignUpData{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", trustFactor=" + trustFactor +
                ", hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                ", trustFactorUpdated='" + trustFactorUpdated + '\'' +
                '}';
    }
}
