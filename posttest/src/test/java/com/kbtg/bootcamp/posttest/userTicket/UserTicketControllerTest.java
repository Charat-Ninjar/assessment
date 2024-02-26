package com.kbtg.bootcamp.posttest.userTicket;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class UserTicketControllerTest {

    MockMvc mockMvc;

    @Mock
    private UserTicketService userTicketService;

    @BeforeEach
    void setUp() {
        UserTicketController userTicketController = new UserTicketController(userTicketService);
        mockMvc = MockMvcBuilders.standaloneSetup(userTicketController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("When perform GET: /users/{userId}/lotteries should return list of lotteries")
    void showExistListOfLottery() throws Exception {
        User user = new User("test");
        Long userId = 111111L;
        user.setUser_id(userId);

        Lottery lottery = new Lottery("123456", 1, 1);

        when(userTicketService.showUserLotteriesList(String.valueOf(user.getUser_id())))
                .thenReturn(new UserTicketResponse(List.of(String.valueOf(lottery))));

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/lotteries", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket[0]").value(containsString("123456")));
    }


    @Test
    @DisplayName("When perform Delete: /users/{userId}/lotteries/{ticketId} should return deleted lottery_id")
    void shouldReturnLotteryIdAfterSellBack() throws Exception {
        when(userTicketService.deleteLotteryLotteryById(anyString(), anyString()))
                .thenReturn(new UserTicketResponse(Collections.singletonList("222222")));

        mockMvc.perform(delete("/users/1234567890/lotteries/222222"))
                .andExpect(jsonPath("$.ticket[0]", containsString("222222")))
                .andExpect(status().isOk());

        verify(userTicketService).deleteLotteryLotteryById("1234567890", "222222");


    }

    @Test
    void shouldReturnLotteryIdAfterBuy() throws Exception {
        when(userTicketService.buyLottery(anyString(), anyString())).thenReturn(new UserTicketResponse(Collections.singletonList("123456")));

        // Performing the HTTP POST request to buy a lottery
        mockMvc.perform(post("/users/1234567890/lotteries/123456"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"ticket\": [\"123456\"]}"));
    }
}