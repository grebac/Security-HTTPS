package hepl.grebac.hepl_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@SpringBootApplication
public class HeplSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeplSecurityApplication.class, args);
    }



}
