package com.kbtg.bootcamp.posttest.admin;

import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

	MockMvc mockMvc;

	@Mock
	private LotteryService lotteryService;

	@BeforeEach
	void setUp() {
		AdminController adminController = new AdminController(lotteryService);
		mockMvc = MockMvcBuilders.standaloneSetup(adminController)
				.alwaysDo(print())
				.build();
	}

	@Test
	void shouldAddLottery() throws Exception {
		String requestBody = "{\"ticket\": \"123456\", \"price\": 10, \"amount\": 1}";

		mockMvc.perform(MockMvcRequestBuilders.post("/admin/lotteries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk());
	}

//    @Test
//    void shouldNotAddLotteryIfNotAuthenticated() throws Exception {
//        String requestBody = "{\"ticket\": \"123456\", \"price\": 10, \"amount\": 1}";
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/admin/lotteries")
//                        .with(httpBasic("user","pass"))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isUnauthorized());
//    }
}