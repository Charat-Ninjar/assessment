package com.kbtg.bootcamp.posttest.userTicket;

import com.kbtg.bootcamp.posttest.exeption.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(MockitoExtension.class)
class UserTicketServiceTest {

	MockMvc mockMvc;

	@Mock
	private UserTicketRepository userTicketRepository;
	@Mock
	private LotteryRepository lotteryRepository;
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	UserTicketService userTicketService;

	@BeforeEach
	void setUp() {
		UserTicketService userTicketService = new UserTicketService(userTicketRepository, lotteryRepository, userRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(userTicketService)
				.alwaysDo(print())
				.build();
	}

	@Test
	@DisplayName("when call buyLottery method with invalid user_id should throw Exception")
	void buyLotteryShouldReturnNotFoundException() {

		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			userTicketService.buyLottery("123", "456");
		});

		String expectedMessage = "User not found with ID: 123";
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	@DisplayName("when call buyLottery method with invalid lottery_id should throw Exception")
	void buyLotteryShouldReturnExceptionWhenLotteryIdNotFound() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

		when(lotteryRepository.findById(anyLong())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			userTicketService.buyLottery("123456", "456");
		});

		String expectedMessage = "Lottery not found with ID: 456";
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}


	@Test
	@DisplayName("Should return UserTicketResponse with correct ticket IDs")
	void showUserLotteriesListShouldReturnCorrectTicketIds() {
		// Mocking userTicketRepository.findByUserId() to return a list of UserTickets
		List<UserTicket> userTickets = Arrays.asList(
				new UserTicket(1L, 101L),
				new UserTicket(1L, 102L),
				new UserTicket(1L, 103L)
		);
		when(userTicketRepository.findByUserId(anyLong())).thenReturn(userTickets);

		UserTicketResponse response = userTicketService.showUserLotteriesList("1");

		List<String> expectedTicketIds = Arrays.asList("101", "102", "103");
		assertEquals(expectedTicketIds, response.getTicket(), "Returned ticket IDs do not match expected");
	}
}