package de.hfu.cnc.controller;

import de.hfu.cnc.service.FrontendService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FrontendController {
    private static final Logger LOG = LoggerFactory.getLogger(FrontendController.class);
    private final FrontendService frontendService;
    Counter requestCounter;
    Counter work1Counter;
    Counter work100Counter;
    Counter work1000Counter;
    Counter statsCounter;
    Counter deleteCounter;

    /*
     * Constructor Injection:
     * WebClients
     * Metrics
     */
    public FrontendController(FrontendService frontendService, MeterRegistry meterRegistry) {
        this.frontendService = frontendService;
        this.requestCounter = meterRegistry.counter("frontend_requests");

        this.work1Counter = meterRegistry.counter("frontend_work1_requests");
        this.work100Counter = meterRegistry.counter("frontend_work100_requests");
        this.work1000Counter = meterRegistry.counter("frontend_work1000_requests");
        this.statsCounter = meterRegistry.counter("frontend_stats_requests");
        this.deleteCounter = meterRegistry.counter("frontend_delete_requests");
    }


    /*
     * Created five endpoints:
     * /work1 -> Sending 1 request [just for testing]
     * /work100 -> Sending 100 requests
     * /work1000 -> Sending 1000 requests
     * /stats -> Getting total amount of generated coins and the last generated coin
     * /delete -> Delete all generated coins
     */

    /*
     * https://kube.informatik.hs-furtwangen.de/weigoldl-frontend/work1
     * Increment specific counter (work1Counter, requestCounter)
     */
    @GetMapping("/work1")
    public void work1() {
        this.work1Counter.increment();
        this.requestCounter.increment();

        this.frontendService.sendRequest(1);
    }

    /*
     * https://kube.informatik.hs-furtwangen.de/weigoldl-frontend/work100
     * Increment specific counter (work100Counter, requestCounter)
     */
    @GetMapping("/work100")
    public void work100() {
        this.work100Counter.increment();
        this.requestCounter.increment();

        this.frontendService.sendRequest(100);
    }

    /*
     * https://kube.informatik.hs-furtwangen.de/weigoldl-frontend/work1000
     * Increment specific counter (work1000Counter, requestCounter)
     */
    @GetMapping("/work1000")
    public void work1000() {
        this.work1000Counter.increment();
        this.requestCounter.increment();

        this.frontendService.sendRequest(1000);
    }


    /*
     * https://kube.informatik.hs-furtwangen.de/weigoldl-frontend/stats
     * Increment specific counter (statsCounter, requestCounter)
     * Some basic inline-css styling for better readability
     */
    @GetMapping("/stats")
    public String stats() {
        LOG.info("Stats path was called");
        this.statsCounter.increment();
        this.requestCounter.increment();
        return "<h2 style=\"color:#1d3046;\">Stats:</h2><p style=\"color:#1d3046;\">The total amount of generated coins is: <b style=\"color:#4785c2;\">" + this.frontendService.getTotalAmountOfCoins() + "</b>;</br>" + "The last generated coin is: <b style=\"color:#4785c2;\">" + this.frontendService.getLastCoin() + "</b>;";
    }

    /*
     * https://kube.informatik.hs-furtwangen.de/weigoldl-frontend/delete
     * Increment specific counter (deleteCounter, requestCounter)
     */
    @RequestMapping("/delete")
    public String delete() {
        LOG.info("Delete path was called");
        this.deleteCounter.increment();
        this.requestCounter.increment();
        String response = "<b style=\"color:#4785c2;\">" + this.frontendService.getTotalAmountOfCoins() + "</b> Coins were deleted";
        this.frontendService.deleteCoins();
        return response;
    }

}
