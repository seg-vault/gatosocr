/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seg-vault.ocr.service;
import com.seg-vault.ocr.service.OcrService;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
public class OcrController {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private OcrService ocrService;
    
    /*
    @GetMapping("/run-job")
    public String runJob(
            @RequestParam(value = "name", defaultValue = "Hello World") String name) {

        jobScheduler.enqueue(() -> ocrService.execute(name));
        return "Job is enqueued.";

    }*/
    @GetMapping("/run-job")
    public String runJob() {
        jobScheduler.enqueue(() -> ocrService.execute());
        return "Job is scheduled.";

    }
    
    /*
    @GetMapping("/schedule-job")
    public String scheduleJob(
            @RequestParam(value = "name", defaultValue = "Hello World") String name,
            @RequestParam(value = "when", defaultValue = "PT3H") String when) {

        jobScheduler.schedule(
                Instant.now().plus(Duration.parse(when)),
                () -> ocrService.execute(name)
        );

        return "Job is scheduled.";
    }*/

}