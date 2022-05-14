package com.estock.gateway.controller;

import com.estock.gateway.configuration.JWTutil;
import com.estock.gateway.entity.AuthenticationRequest;
import com.estock.gateway.entity.AuthenticationResponse;
import com.estock.gateway.service.EStockUserdetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1.0/market")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTutil jwtTokenUtil;

    private final EStockUserdetails userDetailsService;



    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JWTutil jwtTokenUtil,
                                    EStockUserdetails userDetailsService) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;

    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authrequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authrequest.getUserEmail(), authrequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authrequest.getUserEmail());
        AuthenticationResponse authresponse = new AuthenticationResponse(jwtTokenUtil.generateToken(userDetails));

        return ResponseEntity.ok(authresponse);
    }

    /*@PostMapping(value = "/addusers")
    public ResponseEntity<?> authenticate(@RequestBody UserEn) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authrequest.getUserEmail(), authrequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authrequest.getUserEmail());
        AuthenticationResponse authresponse = new AuthenticationResponse(jwtTokenUtil.generateToken(userDetails));

        return ResponseEntity.ok(authresponse);
    }*/
}
