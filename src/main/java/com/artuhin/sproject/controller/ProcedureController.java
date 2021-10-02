package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.MastersTableDto;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/procedures")
public class ProcedureController {
    private static final String MASTERS_ROW_DTO = "mastersRowDto";
    private static final String MASTERS_TABLE = "mastersTable";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/table")
    public String getTable (Model model) {
        model.addAttribute(MASTERS_ROW_DTO, userMapper.toMastersRowDtoList(userService.getMasters()));
        return MASTERS_TABLE;
    }

    @GetMapping("/table/sort={by}")
    public String getTableSort (@PathVariable("by") String by, Model model) {
        List<MastersTableDto> list = userMapper.toMastersRowDtoList(userService.getMasters());
        new UserMapper().sortMastersRowDtoList(by, list);
        model.addAttribute(MASTERS_ROW_DTO, list);
        return MASTERS_TABLE;
    }

    @GetMapping("/table/{filter}={by}")
    public String getTableSort (@PathVariable("filter") String filter, @PathVariable("by") String by, Model model) {
        List<MastersTableDto> list = userMapper.toMastersRowDtoList(userService.getMasters());
        model.addAttribute(MASTERS_ROW_DTO, new UserMapper().filterMasterRowDtoList(filter, by, list));
        return MASTERS_TABLE;
    }

}
