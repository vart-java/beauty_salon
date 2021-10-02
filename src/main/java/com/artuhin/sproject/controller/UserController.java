package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.*;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ProcedureService procedureService;

    @PostMapping("/jquery/getdata")
    @ResponseBody
    public List<MasterScheduleTableDto> getUserById(@RequestBody AjaxMasterSkillsDto ajaxMasterSkillsDto) {
        return appointmentMapper.toGetMasterScheduleTableDto(appointmentService.getMasterAppointments(ajaxMasterSkillsDto.getId()).stream().filter(a->a.getStartTime().toLocalDateTime().getDayOfYear()==LocalDate.parse(ajaxMasterSkillsDto.getDate()).getDayOfYear()).collect(Collectors.toList()));
    }

    @Secured("ROLE_CLIENT")
    @GetMapping("/ratings")
    public String getMastersSpecializationRatings(Model model){
        Map<CommonProcedureDto, List<MasterDto>> ratingMap = userMapper.getMastersRatingsAndBookModel(procedureService.getAll(), userService.getMasters());
        model.addAttribute("masterSpecializationRatings", ratingMap);
        model.addAttribute("localDate", LocalDate.now());
        return "mastersRatingsAndBook";
    }
}
