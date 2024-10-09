package com.andersenlab.pashabanktask.controller

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto
import com.andersenlab.pashabanktask.rabbitmq.RabbitMqMessageProducer
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
class TransactionControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockBean
    RabbitMqMessageProducer rabbitMqMessageProducer

    def "should create transaction and return Ok"() {
        given:
        def transactionRequestDto = new TransactionRequestDto("test",BigDecimal.ONE)
        def objectMapper = new ObjectMapper()
        def jsonContent = objectMapper.writeValueAsString(transactionRequestDto)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                .contentType("application/json")
                .content(jsonContent))

        then:
        1 * rabbitMqMessageProducer.publish(transactionRequestDto, "CBAR_FOREIGN_TRANSFER_Exchange", "CBAR_FOREIGN_TRANSFER_Key")
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Ok"))
    }
}

