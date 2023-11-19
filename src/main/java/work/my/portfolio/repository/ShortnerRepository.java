package work.my.portfolio.repository;

import java.util.Optional;

import work.my.portfolio.entity.Shortner;

/**
 * 短縮機repository
 * 
 * @author kinoshita daiki
 * @since 2023/11/14
 */
public interface ShortnerRepository {
	/**
	 * 短縮機を保存
	 * 
	 * @param entity 短縮機
	 */
	void save(Shortner entity);

	/**
	 * 指定した属性を持つ短縮機を取得する
	 * 
	 * @param longUrl 長いURL
	 * @return 短縮機
	 */
	Optional<Shortner> findByLongUrl(String longUrl);

	/**
	 * 短縮機を取得する
	 * 
	 * @param shortUrl 短いURL
	 * @return 短縮機
	 */
	Optional<Shortner> find(String shortUrl);

}
