package com.lvh.spring_boot_project.controller;

import com.lvh.spring_boot_project.dto.ScheduleEmailRequest;
import com.lvh.spring_boot_project.dto.ScheduleEmailResponse;
import com.lvh.spring_boot_project.quartz.job.EmailJob;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailSchedulerController {

    private final Scheduler scheduler;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schedule/email")
    public ResponseEntity<ScheduleEmailResponse> scheduleEmail(@Valid @RequestBody ScheduleEmailRequest scheduleEmailRequest){
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.now().plusMinutes(1), ZoneId.systemDefault());
            if(dateTime.isBefore(ZonedDateTime.now())){
                ScheduleEmailResponse scheduleEmailResponse
                        = new ScheduleEmailResponse();
                scheduleEmailResponse.setSuccess(false);
                scheduleEmailResponse.setMessage("dateTime must be after current time");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(scheduleEmailResponse);
            }
            JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);
            Trigger trigger = buildTrigger(jobDetail,dateTime);
            scheduler.scheduleJob(jobDetail,trigger);

            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(
                    true,
                    jobDetail.getKey().getName(),
                    jobDetail.getKey().getGroup(),
                    "Email Scheduled Successfully!"
            );
            return ResponseEntity.ok(scheduleEmailResponse);


        }catch (SchedulerException schedulerException){
            log.error("Error while scheduling email: ", schedulerException);
            ScheduleEmailResponse scheduleEmailResponse
                    = new ScheduleEmailResponse();
            scheduleEmailResponse.setSuccess(false);
            scheduleEmailResponse.setMessage("dateTime must be after current time");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(scheduleEmailResponse);
        }
    }

    private JobDetail buildJobDetail(ScheduleEmailRequest scheduleEmailRequest){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("subject",scheduleEmailRequest.getSubject());
        jobDataMap.put("body",scheduleEmailRequest.getBody());
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(),
                        "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

}
