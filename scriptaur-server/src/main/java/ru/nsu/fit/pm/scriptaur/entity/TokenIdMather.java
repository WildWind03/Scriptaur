package ru.nsu.fit.pm.scriptaur.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenIdMather {
    @Id
    private String token;
    private int id;
}
