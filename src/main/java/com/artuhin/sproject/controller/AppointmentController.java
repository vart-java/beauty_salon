package com.artuhin.sproject.controller;

import com.artuhin.sproject.exception.MasterCanNotPerformProcedureException;
import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcedureService procedureService;

    @Secured("ROLE_CLIENT")
    @PostMapping("/add")
    public String addNewAppointment(@ModelAttribute("masterIn") long masterIn, @ModelAttribute("skillIn") String skillIn, @ModelAttribute("dateIn") String dateIn, @ModelAttribute("timeIn") String timeIn, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        appointmentService.createAppointment(CreateAppointmentPostDto.builder().masterId(masterIn).clientId(user.getId()).procedureName(skillIn).startDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateIn), LocalTime.parse(timeIn)))).build());
        model.addAttribute("userAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getClientAppointments(2L)));
        return "clientProcedures";
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("create/master/{id}")
    public String createAppointmentId(@PathVariable("id") long id, @ModelAttribute("dateAppointment") String date, @ModelAttribute("pickedProcedure") Long procedureId, Model model) {
        model.addAttribute("masterAppointments", appointmentMapper.toBookMasterDailyScheduleTableDtos(appointmentService.getMasterAppointments(id).stream().filter(a -> a.getStartTime().toLocalDateTime().getDayOfYear() == LocalDate.parse(date).getDayOfYear()).collect(Collectors.toList())));
        model.addAttribute("dateAppointment", LocalDate.parse(date));
        model.addAttribute("thisDay", LocalDate.now());
        model.addAttribute("allMasters", userMapper.toFullUserDtoList(userService.getMasters()));
        model.addAttribute("masterName", userMapper.toCommonUserDto(userService.getUserById(id)).getLogin());
        return "bookAndMasterSchedule";
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("create/master")
    public String createAppointment(Model model) {
        model.addAttribute("dateAppointment", LocalDateTime.now().toLocalDate());
        model.addAttribute("thisDay", LocalDate.now());
        model.addAttribute("allMasters", userMapper.toFullUserDtoList(userService.getMasters()));
        model.addAttribute("masterName", userMapper.toCommonUserDto(userService.getMasters().stream().findAny().orElseThrow(()->new MasterCanNotPerformProcedureException("No master"))).getLogin());
        return "bookAndMasterSchedule";
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("get/client")
    public String getClientAppointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("userAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getClientAppointments(user.getId())));
        return "clientProcedures";
    }

    @Secured("ROLE_MASTER")
    @GetMapping("get/master")
    public String getMasterAppointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("masterAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getMasterAppointments(user.getId())));
        model.addAttribute("masterTodayAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getMasterAppointments(user.getId()).stream().filter(a->a.getStartTime().toLocalDateTime().getDayOfYear()==LocalDateTime.now().getDayOfYear()).collect(Collectors.toList())));
        return "masterProcedures";
    }

    @Secured("ROLE_MASTER")
    @PostMapping("get/master/conduct")
    public String getMasterAppointments(@ModelAttribute("confirm") long id, Model model) {
        appointmentService.switchStatus(AppointmentSwitchDto.builder().id(id).build());
        model.addAttribute("masterAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getMasterAppointments(3L)));
        return "redirect:/api/appointments/get/master";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("get/admin")
    public String getAdminAppointments(Model model) {
        model.addAttribute("allAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getAll()));
        return "adminProcedures";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("admin/{status}")
    public String getMasterAppointments(@PathVariable("status") String status, @ModelAttribute("confirm") long id, @ModelAttribute("newTime") String time, Model model) {
        if (status.equals("delete")) {
            appointmentService.delete(AppointmentSwitchDto.builder().id(id).build());
        }
        if (status.equals("settime")) {
            appointmentService.updateTimeAppointment(id, time);
        }
        else {
            appointmentService.switchStatus(AppointmentSwitchDto.builder().id(id).build());
        }
        model.addAttribute("allAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getAll()));
        return "redirect:/api/appointments/get/admin";
    }
}
