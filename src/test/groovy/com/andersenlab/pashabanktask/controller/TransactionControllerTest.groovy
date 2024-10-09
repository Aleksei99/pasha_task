package com.andersenlab.pashabanktask.controller

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto
import com.andersenlab.pashabanktask.rabbitmq.RabbitMqMessageProducer
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Subject

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest(TransactionController)
class TransactionControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockBean
    @Subject
    RabbitMqMessageProducer rabbitMqMessageProducer

    def "should create transaction and return Ok"() {
        given:
        def transactionRequestDto = new TransactionRequestDto("test",BigDecimal.ONE)
        def objectMapper = new ObjectMapper()
        def jsonContent = objectMapper.writeValueAsString(transactionRequestDto)

        when:
        def result = mockMvc.perform(post("/api/v1/transactions")
                .contentType("application/json")
                .content(jsonContent))

        then:
        1 * rabbitMqMessageProducer.publish(_, _, _)
        result.andExpect(status().isOk())
                .andExpect(content().string("Ok"))
    }
}

