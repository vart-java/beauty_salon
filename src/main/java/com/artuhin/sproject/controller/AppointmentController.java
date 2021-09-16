package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.AppointmentGetDto;
import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.model.dto.generic.ApiResponseWrapper;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProcedureService procedureService;

    @GetMapping("/client/{id}")
    public ResponseEntity<ApiResponseWrapper<List<AppointmentGetDto>>> getAppointmentsByClientId(@PathVariable Long id) {
        List<AppointmentEntity> clientAppointments = appointmentService.getClientAppointments(id);
        return ResponseEntity.ok(new ApiResponseWrapper<>(appointmentMapper.toAppointmentGetDtoList(clientAppointments)));
    }

    @PostMapping("/add")
    public String addNewAppointment(@ModelAttribute("masterIn") long masterIn, @ModelAttribute("skillIn") String skillIn, @ModelAttribute("dateIn") String dateIn, @ModelAttribute("timeIn") String timeIn, Model model) {
        appointmentService.createAppointment(CreateAppointmentPostDto.builder().masterId(masterIn).clientId(2L).procedureName(skillIn).startDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateIn), LocalTime.parse(timeIn)))).build());
        model.addAttribute("userAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getClientAppointments(2L)));
        return "client_procedures";
    }

    @GetMapping("/create/appointment/master/{id}")
    public String сreateAppointment(@PathVariable("id") long id, @ModelAttribute("dateAppointment") String date, @ModelAttribute("pickedProcedure") String procedureName, @ModelAttribute("startTime") String time, Model model) {
//        appointmentService.createAppointment(CreateAppointmentPostDto.builder().clientId(7L).masterId(id).procedureId(procedureService.).build())
        return "procedures";
    }

    // get dto -> map to entity -> pass to service -> get entity as result -> map to dto -> set to model -> respond
    @PostMapping("create/master/{id}")
    public String createAppointmentId(@PathVariable("id") long id, @ModelAttribute("dateAppointment") String date, @ModelAttribute("pickedProcedure") Long procedureId, Model model) {
        model.addAttribute("masterAppointments", appointmentService.getMasterAppointments(id).stream().filter(a -> a.getStartTime().toLocalDateTime().getDayOfYear() == LocalDate.parse(date).getDayOfYear()).collect(Collectors.toList()));
        model.addAttribute("dateAppointment", LocalDate.parse(date));
        model.addAttribute("procedureName", procedureService.getProcedureById(procedureId).getName());
        model.addAttribute("appDto", appointmentMapper.toCreateAppGetDto(appointmentService.getModel()));
        model.addAttribute("localDate", LocalDate.now());
        String name = userService.getUserById(id).getLogin();
        model.addAttribute("masterName", name);
        return "master_schedule";
    }

    @GetMapping("create/master")
    public String createAppointment(Model model) {
        model.addAttribute("dateAppointment", LocalDateTime.now().toLocalDate());
        model.addAttribute("appDto", appointmentMapper.toCreateAppGetDto(appointmentService.getModel()));
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("masterName", appointmentMapper.toCreateAppGetDto(appointmentService.getModel()).getMasters().stream().findAny().get().getLogin());
        return "master_schedule";
    }

    @GetMapping("get/client")
    public String getClientAppointments(Model model) {
        model.addAttribute("userAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getClientAppointments(2L)));
        return "client_procedures";
    }

    @Secured("CLIENT")
    @GetMapping("get/master")
    public String getMasterAppointments(Model model) {
        model.addAttribute("masterAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getMasterAppointments(3L)));
        return "master_procedures";
    }

    @PostMapping("get/master/conduct")
    public String getMasterAppointments(@ModelAttribute("confirm") long id, Model model) {
        appointmentService.switchStatus(AppointmentSwitchDto.builder().id(id).build());
        model.addAttribute("masterAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getMasterAppointments(3L)));
        return "redirect:/api/appointments/get/master";
    }

    @GetMapping("get/admin")
    public String getAdminAppointments(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!authentication.getAuthorities().stream().findAny().get().getAuthority().equals("ADMIN")) {
//            return "redirect:"+"http://localhost:8080/main";
//        }
        model.addAttribute("allAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getAll()));
        return "admin_procedures";
    }

    @PostMapping("admin/{status}")
    public String getMasterAppointments(@PathVariable("status") String status, @ModelAttribute("confirm") long id, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!authentication.getAuthorities().stream().findAny().get().getAuthority().equals("ADMIN")) {
//            return "redirect:"+"http://localhost:8080/main";
//        }
        if (status.equals("delete")) {
            appointmentService.delete(AppointmentSwitchDto.builder().id(id).build());
        } else {
            appointmentService.switchStatus(AppointmentSwitchDto.builder().id(id).build());
        }
        model.addAttribute("allAppointments", appointmentMapper.toAppointmentGetDtoList(appointmentService.getAll()));
        return "redirect:/api/appointments/get/admin";
    }
}
