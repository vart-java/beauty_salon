package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.FullProcedureDto;
import com.artuhin.sproject.model.dto.MastersRowDto;
import com.artuhin.sproject.model.dto.generic.ApiResponseWrapper;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.AppointmentMapper;
import com.artuhin.sproject.service.mapper.ProcedureMapper;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Controller
@RequestMapping("/api/procedures")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;
    @Autowired
    private ProcedureMapper procedureMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<FullProcedureDto>>> getProcedures() {
        List<ProcedureEntity> procedures = procedureService.getAll();
        return ResponseEntity.ok(new ApiResponseWrapper<>(procedureMapper.toFullProcedureDtoList(procedures)));
    }

    @GetMapping("/table")
    public String getTable (Model model) {
        model.addAttribute("mastersRowDto", userMapper.toMastersRowDtoList(userService.getMasters()));
        return "master_table";
    }

    @GetMapping("/table/sort={by}")
    public String getTableSort (@PathVariable("by") String by, Model model) {
        List<MastersRowDto> list = userMapper.toMastersRowDtoList(userService.getMasters());
        new UserMapper().sortMastersRowDtoList(by, list);
        model.addAttribute("mastersRowDto", list);
        return "master_table";
    }

}
