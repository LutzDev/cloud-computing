package de.hfu.cnc.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;

@RestController
public class RandomizerController {
    Counter requestsCounter;
    private static final Logger LOG = LoggerFactory.getLogger(RandomizerController.class);

    public RandomizerController(MeterRegistry meterRegistry){
        this.requestsCounter = meterRegistry.counter("randomizer_requests");
    }

    // Increment specific counter (requestsCounter)
    @GetMapping("/{howManyBytes}")
    public byte[] index(@PathVariable int howManyBytes) throws InterruptedException {
        this.requestsCounter.increment();
        Thread.sleep(100);
        byte[] b = new byte[howManyBytes];
        new Random().nextBytes(b);
        LOG.info("Bytes ["+  Arrays.toString(b) +"] was created");
        return b;
    }
}
