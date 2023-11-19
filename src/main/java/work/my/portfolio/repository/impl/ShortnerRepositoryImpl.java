package work.my.portfolio.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import work.my.portfolio.entity.Shortner;
import work.my.portfolio.repository.ShortnerRepository;

/**
 * 短縮機repositoryImpl
 * 
 * @author kinoshita daiki
 * @since 2023/11/14
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = { @Inject })
@ApplicationScoped
class ShortnerRepositoryImpl implements ShortnerRepository {

	private final DynamoDbClient dynamoDB;

	@Override
	public void save(Shortner entity) {
		PutItemRequest req = createLongUrlPutRequest(entity);
		dynamoDB.putItem(req);
	}

	private PutItemRequest createLongUrlPutRequest(Shortner shortner) {
		Map<String, AttributeValue> item = new HashMap<>();
		item.put(Shortner.CONSTANT_SHORT_URL, createAttributeValue(shortner.shortUrl()));
		item.put(Shortner.CONSTANT_LONG_URL, createAttributeValue(shortner.longUrl()));
		return PutItemRequest.builder()//
				.tableName(Shortner.class.getSimpleName())//
				.item(item)//
				.build();
	}

	private GetItemRequest createLongUrlGetRequest(String shortUrlValue) {
		return GetItemRequest.builder()//
				.tableName(Shortner.class.getSimpleName())//
				.key(Map.of(Shortner.CONSTANT_SHORT_URL, createAttributeValue(shortUrlValue)))//
				.projectionExpression(Shortner.CONSTANT_LONG_URL)//
				.build();
	}

	private AttributeValue createAttributeValue(String value) {
		return AttributeValue.builder().s(value).build();
	}

	@Override
	public Optional<Shortner> findByLongUrl(String longUrl) {
		QueryRequest queryRequest = QueryRequest.builder()//
				.tableName(Shortner.class.getSimpleName())//
				.indexName(Shortner.CONSTANT_LONG_INDEX)//
				.expressionAttributeValues(Map.of(":val", createAttributeValue(longUrl)))//
				.keyConditionExpression(Shortner.CONSTANT_LONG_URL + " = :val")//
				.build();
		QueryResponse response = dynamoDB.query(queryRequest);
		if (response.items().isEmpty()) {
			return Optional.empty();
		}
		return Optional.ofNullable(Shortner.from(response.items().get(0)));
	}

	@Override
	public Optional<Shortner> find(String shortUrl) {
		GetItemRequest request = createLongUrlGetRequest(shortUrl);
		return Optional.ofNullable(Shortner.from(dynamoDB.getItem(request).item()));
	}

}
