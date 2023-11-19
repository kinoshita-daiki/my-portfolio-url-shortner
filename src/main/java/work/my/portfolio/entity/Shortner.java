package work.my.portfolio.entity;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * 短縮機
 * 
 * @author kinoshita daiki
 * @since 2023/11/06
 */
@RegisterForReflection
public record Shortner(String shortUrl, String longUrl) {

	public static final String CONSTANT_SHORT_URL = "shortUrl";

	public static final String CONSTANT_LONG_URL = "longUrl";

	public static final String CONSTANT_LONG_INDEX = "longIndex";

	/**
	 * 指定した{@code item}から短縮機を作成する
	 * 
	 * @param item 属性と値のマッパー
	 * @return 短縮機
	 */
	public static Shortner from(Map<String, AttributeValue> item) {
		if (item != null && !item.isEmpty()) {
			String shortUrl = null;
			String longUrl = null;
			if (item.containsKey(CONSTANT_SHORT_URL)) {
				shortUrl = item.get(CONSTANT_SHORT_URL).s();
			}
			if (item.containsKey(CONSTANT_LONG_URL)) {
				longUrl = item.get(CONSTANT_LONG_URL).s();
			}
			return new Shortner(shortUrl, longUrl);
		} else {
			return null;
		}
	}
}