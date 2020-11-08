package com.example.covid.controller;

import com.example.covid.model.Date;
import com.example.covid.service.CovidService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CovidController
{
    private final CovidService covidService;

    @GetMapping("/date/{year}/{month}/{day}")
    public String getQuarantineTime(@PathVariable int year,
                                    @PathVariable int month,
                                    @PathVariable int day)
    {
        return covidService.getQuarantineTime(year, month, day);
    }

    @PostMapping("/date")
    public String getQuarantineTimeWithPost(@RequestBody Date date)
    {
        return covidService.getQuarantineTime(date);
    }
}