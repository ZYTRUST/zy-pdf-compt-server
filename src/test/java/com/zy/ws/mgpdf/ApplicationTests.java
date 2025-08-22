package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected Utilitario utilitario;

	protected String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3VDb3JyZW8iOiJDQ0VSVkFOVEVTQFpZVFJVU1QuQ09NIiwidXN1U3VjSWQiOjEzMiwidXN1RW1wc0lkIjo0NywidXN1QXBsSWQiOjEsInVzZXJfbmFtZSI6IlpZLU5PVEFSSUFIRVJSRVJBIiwicGVyZklkcyI6WzIwNV0sInVzdUNvSW50ZXJubyI6IlpZLU5PVEFSSUFIRVJSRVJBIiwiYXV0aG9yaXRpZXMiOlsieU0wd3VRbXZRbkdDRTIrVmgrVFBPTlIwVE5VPSIsImVqMUJxNEJqaDhMU2NoNTlMbDkrbHVsQ1BFOD0iLCJOcXVsOFZSM2JiSGtONzI3NjRscUhPNjJjOG89Iiwick0rWXBERWRyN0FraHN0SUxaR0hxc09aV1FRPSIsIjBtVXB5LzVkWFhhcXNlRHRuNE1QWGUwRFo3az0iLCJPMU1EZ0FrRUhEblB0cytwVy8vSllQK0F3VnM9IiwiODE2NW5VbDROY3ZVMzZ3UGo0YU9Ic1EzYWFNPSIsInNBQmJCVXUwZmdWQmNWOEc1R3VGOEcxRlVITT0iLCJlT2dUdEZpQ1U3SXo2YUFtN0JGWHFncmFpSFk9IiwiVkd6OUYzT2ZxVHhhUFlMRnJTZ0FQZWJsTVI4PSIsIkcrbnFYVmVWL0VMdXRVWXNMdTF2ejgreURCST0iLCJwWGNLRVBIQUxkRGJ0d2UwbkpUS0owcE4xUDA9Il0sImNsaWVudF9pZCI6IjEwMDkzODk1NzY4IiwidXN1SWQiOjU1MSwidXN1UGVySWQiOjgxNjUsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sInRpRG9jVXN1YXJpbyI6MSwibnVEb2NVc3VhcmlvIjoiMDAwMDAzMDEiLCJzdWN1RGVzY3JpcGNpb24iOiJOT1RBUklBIEhFUlJFUkEgQ0FSUkVSQSIsImV4cCI6MjYxNTQwNzg3NSwic3VjdUNvZGlnbyI6IjAxIiwianRpIjoiOWYyYjI3ODYtYmNlNC00ZTlmLWJkMTgtODE0YzI4YjJlNGU3In0.I0ehqo5-crGRKF1973Ik5F3jEOk9qkdI4iRwWBpnEEpKVtMASe91eUqP4IlXn1ZMoauil3a4_8LlDAt0B9_3uPtm6af7Pq01NPL2l1oS0NCf_AQJ0tjlVDidonOG7d3BTBryZQd4wmz6NB0XCLkg50X_2CGz7jlhKhlGO1XhIaep514HwIqZGSr0Z4ckST0YsJFAzhhV5UG1LTYBUXtdBuslG3s0ZtbuSgP3gTTdFR6m9Q_yDT_EkIxWPGWh1Z9u-Ic_OsMv7be4Yk8k6_DtIgzzMZKlfVWrFDaGgZLAHO-WpJLIeOwrjjPZxh0odBeXNoW9lqaSH4qQIDcgYS_E3w";

	@Test
	void test_generar_pdf() throws Exception {
		CreditoGrupalBean obj = new CreditoGrupalBean();

		byte[] json = utilitario.convertObjectToJsonBytes(obj);

		MvcResult res = mockMvc.perform(post("/api/solicitud/v1/nueva")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn();
	}

}
