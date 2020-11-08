package com.qianke.util;

import java.security.KeyStore;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * @author 
 */
@Component
public class HttpClientUtil {
	/**
	 * POST json withHead
	 *
	 * @param formData
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Mono<Map> postJSONWithHead(WebClient webClient, Map<String, Object> formData, String url,
			Map<String, String> header) {
		Consumer<HttpHeaders> headerConsumer = (h) -> {
			header.forEach(h::add);
		};
		Mono<Map> mono = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(formData), Map.class).headers(headerConsumer)
				.exchange()
				.doOnSuccess(clientResponse -> System.out
						.println("clientResponse.statusCode() = " + clientResponse.statusCode()))
				.flatMap(clientResponse -> clientResponse.bodyToMono(Map.class));

		return mono;
	}

	

	
	/**
	 * POST json 
	 *
	 * @param formData
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Mono<Map> postJSON(WebClient webClient, Map<String, Object> formData, String url) {
		Mono<Map> mono = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(formData), Map.class).exchange()
				// .doOnSuccess(clientResponse -> System.out.println("clientResponse.headers() =
				// " + clientResponse.headers()))
				.doOnSuccess(clientResponse -> System.out
						.println("clientResponse.statusCode() = " + clientResponse.statusCode()))
				.flatMap(clientResponse -> clientResponse.bodyToMono(Map.class));
		return mono;
	}

	/**
	 * form表单无表头提交
	 *
	 * @param formData
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Mono<Map> postForm(WebClient webClient, MultiValueMap<String, String> formData, String url) {
		Mono<Map> mono = webClient.post().uri(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData)).retrieve().bodyToMono(Map.class);
		return mono;
	}
	
	/**
	 * form表单无表头提交返回String
	 *
	 * @param formData
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Mono<String> postFormStr(WebClient webClient, MultiValueMap<String, String> formData, String url) {
		Mono<String> mono = webClient.post().uri(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData)).retrieve().bodyToMono(String.class);
		return mono;
	}
	
	
	

	/**
	 * ssl双向加密
	 *
	 * @return
	 * @throws Exception
	 */
	public static WebClient getSSLWebClient(String baseUrl, String selfcertPath, String trustcaPath, String selfcertPwd,
			String trustcaPwd) {
		try {
			KeyStore selfCert = null;

			selfCert = KeyStore.getInstance("pkcs12");
			selfCert.load(new ClassPathResource(selfcertPath).getInputStream(), selfcertPwd.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(selfCert, selfcertPwd.toCharArray());

			// 2 Import the CA certificate of the server,
			KeyStore caCert = KeyStore.getInstance("jks");
			caCert.load(new ClassPathResource(trustcaPath).getInputStream(), trustcaPwd.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(caCert);

			SslContext sslContext = SslContextBuilder.forClient().trustManager(tmf).keyManager(kmf).build();

			HttpClient secure = HttpClient.create()
					.secure(t -> t.sslContext(sslContext).handlerConfigurator(handler -> {
						SSLParameters params = new SSLParameters();
						List<SNIMatcher> matchers = new LinkedList<>();
						SNIMatcher matcher = new SNIMatcher(0) {
							@Override
							public boolean matches(SNIServerName serverName) {
								return true;
							}
						};
						matchers.add(matcher);
						params.setSNIMatchers(matchers);
						SSLEngine engine = handler.engine();

						engine.setSSLParameters(params);
					}));
			return WebClient.builder().clientConnector(new ReactorClientHttpConnector(secure)).baseUrl(baseUrl).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
