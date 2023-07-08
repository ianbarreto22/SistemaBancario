package br.ufrn.SistemaBancario.utils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.SistemaBancario.model.exceptions.RestRequestException;

public class HttpUtils {

	public HttpUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String httpPostRequest(String uri, Map<String, String> headerParams,
			String body, int expectStatus) throws RestRequestException {

		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(uri);

			if (headerParams != null) {

				for (String header : headerParams.keySet()) {
					request.addHeader(header, headerParams.get(header));
				}
			}

			if (body != null) {
				StringEntity bodyEntity = new StringEntity(body, "UTF-8");
				request.setEntity(bodyEntity);
			}
			CloseableHttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != expectStatus) {
				System.out.println(response.getStatusLine().getStatusCode());
				throw new RestRequestException(response.getStatusLine().getReasonPhrase());
			}

			org.apache.http.HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			return responseString;

		} catch (IOException e) {
			throw new RestRequestException(e.getMessage());
		}
	}

	public static String httpPutRequest(String uri, Map<String, String> headerParams,
			String body, int expectStatus) throws RestRequestException {

		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPut request = new HttpPut(uri);

			if (headerParams != null) {
				for (String header : headerParams.keySet()) {
					request.addHeader(header, headerParams.get(header));
				}
			}

			if (body != null) {
				StringEntity bodyEntity = new StringEntity(body, "UTF-8");
				request.setEntity(bodyEntity);
			}

			CloseableHttpResponse response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != expectStatus) {
				throw new RestRequestException(response.getStatusLine().getReasonPhrase());
			}

			org.apache.http.HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			return responseString;

		} catch (IOException e) {
			throw new RestRequestException(e.getMessage());
		}
	}

	public static String httpGetRequest(String uri, Map<String, String> headerParams) throws RestRequestException {

		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(uri);

			if (headerParams != null) {

				for (String header : headerParams.keySet()) {
					request.addHeader(header, headerParams.get(header));
				}
			}

			CloseableHttpResponse response = httpClient.execute(request);

			if (((org.apache.http.HttpResponse) response).getStatusLine().getStatusCode() != 200) {
				throw new RestRequestException(((org.apache.http.HttpResponse) response).getStatusLine().getReasonPhrase());
			}

			org.apache.http.HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			return responseString;

		} catch (IOException e) {
			throw new RestRequestException(e.getMessage());
		}
	}

	public static String httpPostRequest(String uri, Map<String, String> headerParams, JsonNode body,
			int expectStatus) {
		// TODO Auto-generated method stub
		return null;
	}

}
