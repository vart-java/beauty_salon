package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.FullProcedureDto;
import com.artuhin.sproject.model.pages.MastersTableModel;
import com.artuhin.sproject.model.dto.generic.ApiResponseWrapper;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.ProcedureMapper;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        return "mastersTable";
    }

    @GetMapping("/table/sort={by}")
    public String getTableSort (@PathVariable("by") String by, Model model) {
        List<MastersTableModel> list = userMapper.toMastersRowDtoList(userService.getMasters());
        new UserMapper().sortMastersRowDtoList(by, list);
        model.addAttribute("mastersRowDto", list);
        return "mastersTable";
    }

    @GetMapping("/table/{filter}={by}")
    public String getTableSort (@PathVariable("filter") String filter, @PathVariable("by") String by, Model model) {
        List<MastersTableModel> list = userMapper.toMastersRowDtoList(userService.getMasters());
        model.addAttribute("mastersRowDto", new UserMapper().filterMasterRowDtoList(filter, by, list));
        return "mastersTable";
    }

}
