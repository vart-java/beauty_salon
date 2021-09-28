package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.*;
import com.artuhin.sproject.model.dto.generic.ApiResponseWrapper;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<MasterScheduleTableDto> getUserById(@RequestBody AjaxDto ajaxDto) {
        return appointmentMapper.toGetMasterScheduleTableDto(appointmentService.getMasterAppointments(ajaxDto.getId()).stream().filter(a->a.getStartTime().toLocalDateTime().getDayOfYear()==LocalDate.parse(ajaxDto.getDate()).getDayOfYear()).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<FullUserDto>> updateUser(@PathVariable Long id, @RequestBody @Valid FullUserDto updateFullUserDto) {
        UserEntity updatedUser = userService.updateUser(id, userMapper.fromFullUserDto(updateFullUserDto));
        return ResponseEntity.ok(new ApiResponseWrapper<>(userMapper.toFullUserDto(updatedUser)));
    }

    @GetMapping("/masters")
    public ResponseEntity<ApiResponseWrapper<List<FullUserDto>>> getMasters() {
        List<UserEntity> masters = userService.getMasters();
        return ResponseEntity.ok(new ApiResponseWrapper<>(userMapper.toFullUserDtoList(masters)));
    }

    @GetMapping("/ratings")
    public String getMastersSpecializationRatings(Model model){
        Map<CommonProcedureDto, List<MasterDto>> ratingMap = userMapper.getMastersRatingsAndBookModel(procedureService.getAll(), userService.getMasters());
        model.addAttribute("masterSpecializationRatings", ratingMap);
        model.addAttribute("localDate", LocalDate.now());
        return "mastersRatingsAndBook";
    }
}
