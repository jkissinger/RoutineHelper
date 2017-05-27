package net.peachmonkey.rest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class RoutineResponseErrorHandler extends DefaultResponseErrorHandler {

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		LOGGER.warn("REST Response Error: StatusCode[{}], ResponseBody[{}].", response.getStatusCode(), getResponseBody(response));
	}

	private String getResponseBody(ClientHttpResponse response) {
		try {
			InputStream responseBody = response.getBody();
			if (responseBody != null) {
				return new String(FileCopyUtils.copyToByteArray(responseBody));
			}
		} catch (IOException ex) {
			// ignore
		}
		return "";
	}
}
