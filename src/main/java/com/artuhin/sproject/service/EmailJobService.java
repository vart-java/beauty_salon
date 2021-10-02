package com.artuhin.sproject.service;

import com.artuhin.sproject.model.dto.EmailDto;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@EnableScheduling
@Service
public class EmailJobService {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    JavaMailSender javaMailSender;

    @Scheduled(fixedDelay = 86400000)
    public void executeEmailJob() {
            LocalDate localDate = LocalDate.now();
            List<EmailDto> mailData = appointmentMapper.toEmailDtoList(appointmentService.getDataForEmail(localDate));
            mailData.forEach(this::sendEmail);
    }

    private void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailDto.getClientLogin());
        message.setSubject("Assessment of the procedure performed by the master");
        message.setText("Hello, " + emailDto.getClientLogin().substring(0, emailDto.getClientLogin().indexOf('@')) + "! You underwent the procedure " +
                emailDto.getProcedureName() + " in our salon on " + emailDto.getStartDate().toString() + " by the master " + emailDto.getMasterName() +
                ". Please rate our master for the performed procedure by clicking on the link below" + System.lineSeparator() +
                "http://localhost:8080/api/appointments/get/rate/" + emailDto.getAppointmentId());
        javaMailSender.send(message);
    }
}

