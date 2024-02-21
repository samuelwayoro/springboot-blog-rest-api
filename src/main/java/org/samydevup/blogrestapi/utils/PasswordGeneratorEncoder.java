package org.samydevup.blogrestapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGeneratorEncoder {

    public static void main(String[] args){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("samuel"));
        System.out.println(passwordEncoder.encode("admin"));
    }
}
