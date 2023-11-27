package com.example.textanalyzer.rest;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("сервис должен")
public class CountOccurrenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("успешно завершаться когда получает валидный запрос")
    public void completedSuccessfullyWhenReceivedValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/process")
                        .content("{\"text\": \"bob\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                        {
                            "data": [
                                {
                                    "character": "b",
                                    "counter": 2
                                },
                                {
                                    "character": "o",
                                    "counter": 1
                                }
                            ],
                            "direction": "DESC"
                        }
                        """));
    }

    @Test
    @DisplayName("возвращать ошибку кода получает невалидный запрос")
    public void returnBadRequestWhenReceivedNotValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/process")
                        .content("{\"text\": null}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}