package com.lvh.spring_boot_project.quartz.job;

import com.lvh.spring_boot_project.entity.User;
import com.lvh.spring_boot_project.repository.UserRepository;
import com.lvh.spring_boot_project.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailJob extends QuartzJobBean {
    private final EmailService emailService;
    private final UserRepository userRepository;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");

        List<User> users = userRepository.findAllUser();

        for(User user: users){
            try {
                emailService.sendSchedulerEmail("lvh@gmail.com",
                        user.getEmail(),subject,user.getFullName(),
                        body);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
