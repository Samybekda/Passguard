package fr.miage.passguard;

import fr.miage.passguard.dto.PasswordAnalysisRequest;
import fr.miage.passguard.model.Password;
import fr.miage.passguard.model.PasswordResult;
import fr.miage.passguard.service.PasswordAnalyzerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PassGuardApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordAnalyzerService analyzerService;

    @Test
    void contextLoads() {
        assertNotNull(analyzerService);
    }

    @Test
    void testServiceWithStrongPassword() {
        Password pwd = new Password("S3cur!ty_2026");
        PasswordResult result = analyzerService.analyze(pwd);

        assertEquals("Fort", result.getSecurityLevel());
        assertEquals("Strong", result.getSecurityLevelEn());
        assertEquals(5, result.getScore());
        assertTrue(result.isHasMinLength());
        assertTrue(result.isHasLowercase());
        assertTrue(result.isHasUppercase());
        assertTrue(result.isHasDigit());
        assertTrue(result.isHasSpecialChar());
        assertTrue(result.getRecommendations().isEmpty());
    }

    @Test
    void testServiceWithWeakPassword() {
        Password pwd = new Password("123");
        PasswordResult result = analyzerService.analyze(pwd);

        assertEquals("Faible", result.getSecurityLevel());
        assertEquals(1, result.getScore());
        assertFalse(result.isHasMinLength());
        assertFalse(result.getRecommendations().isEmpty());
    }

    @Test
    void testApiAnalyzeEndpoint() throws Exception {
        mockMvc.perform(post("/api/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"Mi@ge_2026_St@ge!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.securityLevel").value("Fort"))
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.percentage").value(100))
                .andExpect(jsonPath("$.criteria.minLength").value(true));
    }

    @Test
    void testApiHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
