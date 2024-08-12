package com.memorykeeper.memory_keeper.controller;

import com.memorykeeper.memory_keeper.Service.DementiaCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dementia-centers")
public class DementiaCenterController {

    @Autowired
    private DementiaCenterService dementiaCenterService;

    @GetMapping("/fetch-and-save")
    public ResponseEntity<String> fetchAndSaveDementiaCenters() {
        dementiaCenterService.fetchAndSaveDementiaCentersFromAPI();
        dementiaCenterService.saveNearbyCentersForUsers();
        return ResponseEntity.ok("Dementia centers data fetched and saved successfully.");
    }
}



