package de.hfu.cnc.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class HasherController {
    private static final Logger LOG = LoggerFactory.getLogger(HasherController.class);

    MessageDigest md;
    Counter requestsCounter;

    public HasherController(MeterRegistry meterRegistry) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-256");
        this.requestsCounter = meterRegistry.counter("hasher_requests");
    }


    // Increment specific counter (requestsCounter)
    @PostMapping("/")
    public String hash(@RequestBody byte[] data) {
        this.requestsCounter.increment();
        byte[] encodedHash = md.digest(data);
        String hash = bytesToHex(encodedHash);
        LOG.info("Hash [" + hash + "] was created");
        return hash;
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
