package com.artuhin.sproject.model.pages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MastersTableModel {
    private String masterName;
    private String procedureName;
    private double rating;
}
