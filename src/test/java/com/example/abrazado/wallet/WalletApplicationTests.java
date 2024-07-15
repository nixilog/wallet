package com.example.abrazado.wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.abrazado.wallet.entity.Account;
import com.example.abrazado.wallet.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
class WalletApplicationTests {

	@Autowired
	private MockMvc mvc;
	
	private static ObjectMapper mapper;
	private static ObjectWriter writer;

	@BeforeAll
	public static void setup() {
		mapper = JsonMapper.builder().findAndAddModules().build();
		writer = mapper.writer();
	}

	@Test
	public void getAccountById() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().string("{\"id\":1,\"accountName\":\"nikko\",\"balance\":50000.00,\"version\":0}"));
	}

	@Test
	public void getAccountBalanceById() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/1/balance").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().string("50000.00"));
	}

	@Test
	public void createAccount() throws Exception {
		String json = writer.writeValueAsString(createNewAccount());

        mvc.perform(MockMvcRequestBuilders.post("/account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
	}

	@Test
	public void getAllTransactionsByAccountId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/1/transactions").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].sourceId").value(1L))
				.andExpect(jsonPath("$[0].source").value("nikko"))
				.andExpect(jsonPath("$[0].destinationId").value(2L))
				.andExpect(jsonPath("$[0].destination").value("client715"))
				.andExpect(jsonPath("$[0].amount").value(4000))
				.andExpect(jsonPath("$[0].timestamp").value("2020-01-19T03:14:07"))
				.andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].sourceId").value(2L))
				.andExpect(jsonPath("$[1].source").value("client715"))
				.andExpect(jsonPath("$[1].destinationId").value(1L))
				.andExpect(jsonPath("$[1].destination").value("nikko"))
				.andExpect(jsonPath("$[1].amount").value(20000))
				.andExpect(jsonPath("$[1].timestamp").value("2022-09-07T09:48:29"));
	}

	@Test
	public void getOutgoingTransactionsByAccountId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/1/transactions/out").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].sourceId").value(1L))
				.andExpect(jsonPath("$[0].source").value("nikko"))
				.andExpect(jsonPath("$[0].destinationId").value(2L))
				.andExpect(jsonPath("$[0].destination").value("client715"))
				.andExpect(jsonPath("$[0].amount").value(4000))
				.andExpect(jsonPath("$[0].timestamp").value("2020-01-19T03:14:07"));
	}

	@Test
	public void getIncomingTransactionsByAccountId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/1/transactions/in").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(2L))
				.andExpect(jsonPath("$[0].sourceId").value(2L))
				.andExpect(jsonPath("$[0].source").value("client715"))
				.andExpect(jsonPath("$[0].destinationId").value(1L))
				.andExpect(jsonPath("$[0].destination").value("nikko"))
				.andExpect(jsonPath("$[0].amount").value(20000))
				.andExpect(jsonPath("$[0].timestamp").value("2022-09-07T09:48:29"));
	}

	@Test
	public void createTransaction() throws Exception {
		String json = writer.writeValueAsString(createNewTransaction());

        mvc.perform(MockMvcRequestBuilders.post("/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
	}

	@Test
	public void createTransactionNonExistentSource() throws Exception {
		String json = writer.writeValueAsString(createNewTransactionNonExistentSource());

        mvc.perform(MockMvcRequestBuilders.post("/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
	}

	@Test
	public void createTransactionNonExistentDestination() throws Exception {
		String json = writer.writeValueAsString(createNewTransactionNonExistentDestination());

        mvc.perform(MockMvcRequestBuilders.post("/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
	}

	@Test
	public void createTransactionNonExistentSourceAndDestination() throws Exception {
		String json = writer.writeValueAsString(createNewTransactionNonExistentSourceAndDestination());

        mvc.perform(MockMvcRequestBuilders.post("/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
	}

	private Account createNewAccount() {
		return new Account("new_client_" + LocalDateTime.now().toString(), BigDecimal.valueOf(500000));
	}

	private Transaction createNewTransaction() {
		return new Transaction(3L, "client964", 4L, "client1082", BigDecimal.valueOf(1000), LocalDateTime.now());
	}

	private Transaction createNewTransactionNonExistentSource() {
		return new Transaction(null,
			"non-existent_source_" + LocalDateTime.now().toString(),
			5L,
			"client2867",
			BigDecimal.valueOf(10000),
			LocalDateTime.now());
	}

	private Transaction createNewTransactionNonExistentDestination() {
		return new Transaction(6L,
			"client3319",
			null,
			"non-existent_destination_" + LocalDateTime.now().toString(),
			BigDecimal.valueOf(10000),
			LocalDateTime.now());
	}

	private Transaction createNewTransactionNonExistentSourceAndDestination() {
		return new Transaction(null,
			"non-existent_source_" + LocalDateTime.now().toString(),
			null,
			"non-existent_destination_" + LocalDateTime.now().toString(),
			BigDecimal.valueOf(10000),
			LocalDateTime.now());
	}

}
