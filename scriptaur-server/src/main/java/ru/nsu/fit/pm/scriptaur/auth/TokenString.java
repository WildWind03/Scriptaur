package ru.nsu.fit.pm.scriptaur.auth;

import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value
class TokenString {
    private final String token;
}
