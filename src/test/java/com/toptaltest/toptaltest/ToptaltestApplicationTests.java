package com.toptaltest.toptaltest;

import com.toptaltest.toptaltest.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Slf4j
class ToptaltestApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() throws IOException {

        Path csvPath = Paths.get("src/main/resources/employees.csv");
        long expectedCount = 0;
        try(Stream<String> lines = Files.lines(csvPath)) {
            expectedCount = lines.skip(1).filter(line -> {
                return !line.isBlank() && line.split(",").length == 4;
            }).toList().size();
        }
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/toptal_test/read_csv",
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {

            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<Employee> employees = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<>() {}
            );
            assertNotNull(employees);
            assertFalse(employees.isEmpty());
            assertEquals(expectedCount, employees.size());
            assertFalse(employees.isEmpty());
        } else {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            String message = String.format("API failed: status=%s, body=%s",
                    response.getStatusCode(), response.getBody());
            log.error("{}", AnsiOutput.toString(AnsiColor.RED, message));
        }


    }

}
