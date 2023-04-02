package com.demo.api.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author avinash
 */
@RestController
public class EndpointApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EndpointApplication.class);
	/**
	 * Sample config to get battery health
	 */
	private static final  String EXAMPLE_CONFIG = "{\n" +
			"\t\t\t\t\"schedule\": {\n" +
			"\t\t\t\"battery_proc\": {\n" +
			"\t\t\t\t\"query\": \"select health,percent_remaining from battery;\",\n" +
			"\t\t\t\t\t\t\"interval\": 60\n" +
			"\t\t\t}\n" +
			"\t\t},\n" +
			"\t\t\"node_invalid\": false\n" +
			"}";


	/**
	 * enroll endpoint to enroll a device. Only allow if the secret matches
	 * @param request
	 * @return
	 * @throws IOException
	 */
    @PostMapping("/enroll")
    public String enroll(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonNode jsonNode = mapper.readValue(body, JsonNode.class);
        String HTTP_SERVER_ENROLL_SECRET = "this_is_an_enrollment_secret";
        //authenticate enrollment
        if (jsonNode.has("enroll_secret") &&
                !HTTP_SERVER_ENROLL_SECRET.equals(jsonNode.get("enroll_secret").asText())) {
            return "{\"node_invalid\": true}";
        }
        return "{\"node_key\": \"this_is_a_node_secret\"}";
    }


	/**
	 * Api endpoint to pass sql configuration to osquery deamon
	 * @param request
	 * @return
	 * @throws IOException
	 */
    @PostMapping("/config")
    public String config(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonNode jsonNode = mapper.readValue(body, JsonNode.class);
        List<String> nodeKeys = Arrays.asList("this_is_a_node_secret");
        //validate the secret
        if (!jsonNode.has("node_key") || !nodeKeys.contains(jsonNode.get("node_key").asText())) {
            return "{\"node_invalid\": true}";
        }
        return EXAMPLE_CONFIG;
    }


	/**
	 * logger endpoint to push the logs from queries executed by osquery deamon
	 * @param request
	 * @throws IOException
	 */
    @PostMapping("/logger")
    public void logger(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (body.contains("\"log_type\":\"result\"")) {
            LOG.info("data : {}", body);
        }
    }
}
