package work.my.portfolio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.zip.CRC32;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.Mock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import work.my.portfolio.entity.Shortner;
import work.my.portfolio.service.ShortnerService;

@QuarkusTest
@TestHTTPEndpoint(UrlShortnerResources.class)
class UrlShortnerResourcesTest {

	private static final String LONG_URL = "https://myurlshortnertest.co.jp";

	private static final String SHORT_URL_PATH = "testmyurl";

	@Mock
	@RequestScoped
	private static class MockedShortnerService implements ShortnerService {

		@Override
		public void save(Shortner entity) {
			// no op
			return;
		}

		@Override
		public Optional<Shortner> findByShortUrl(String shortUrl) {
			return mockShortner(shortUrl, LONG_URL);
		}

		@Override
		public Optional<Shortner> findByLongUrl(String longUrl) {
			return mockShortner("anyShortUrl", longUrl);
		}

	}

	private MockedShortnerService getMockedService() {
		MockedShortnerService mock = mock(MockedShortnerService.class);
		QuarkusMock.installMockForType(mock, MockedShortnerService.class);
		return mock;
	}

	@Test
	void getShortnerJSONByLongUrl() {
		Optional<Shortner> mockShortner = mockShortner("anyShortUrl", LONG_URL);
		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(mockShortner.get());

		RestAssured.given()//
				.when()//
				.body(LONG_URL)
				.post("/urlShortnerGetter")
				.then()
				.statusCode(200)
				.body(CoreMatchers.is(json));
	}

	@Test
	void getNewShortnerJSONByLongUrl() {
		MockedShortnerService mockedService = getMockedService();
		when(mockedService.findByLongUrl(Mockito.anyString()))
				.thenReturn(Optional.empty());

		var checksum = new CRC32();
		checksum.update(LONG_URL.getBytes());
		var newEntity = new Shortner(String.format("%x", checksum.getValue()), LONG_URL);

		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(newEntity);

		RestAssured.given()//
				.when()//
				.body(LONG_URL)//
				.post("/urlShortnerGetter")//
				.then()//
				.statusCode(200)//
				.body(CoreMatchers.is(json));
	}

	@Test
	void getShortnerJSONByShortUrlPath() {
		Optional<Shortner> mockShortner = mockShortner(SHORT_URL_PATH, LONG_URL);
		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(mockShortner.get());

		RestAssured.given()//
				.when()//
				.get("/{shortUrlPath}", SHORT_URL_PATH)
				.then()//
				.statusCode(200)//
				.body(CoreMatchers.is(json));
	}

	@Test
	void getNullShortnerByUnknownShortUrlPath() {
		MockedShortnerService mockedService = getMockedService();
		when(mockedService.findByShortUrl("unknownPath"))
				.thenReturn(Optional.empty());

		Optional<Shortner> mockShortner = mockShortner(null, null);
		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(mockShortner.get());

		RestAssured.given()//
				.when()//
				.get("/{shortUrlPath}", "unknownPath")
				.then()//
				.statusCode(200)//
				.body(CoreMatchers.is(json));
	}

	private static Optional<Shortner> mockShortner(String shortUrl, String longUrl) {
		var shortner = new Shortner(shortUrl, longUrl);
		return Optional.of(shortner);
	}

}
