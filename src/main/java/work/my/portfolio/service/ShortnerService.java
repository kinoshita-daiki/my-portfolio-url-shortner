package work.my.portfolio.service;

import java.util.Optional;

import work.my.portfolio.entity.Shortner;

/**
 * 短縮機service
 * 
 * @author kinoshita daiki
 * @since 2023/11/06
 */
public interface ShortnerService {

	/**
	 * 短縮機を追加する
	 * 
	 * @param entity 短縮機
	 */
	void save(Shortner entity);

	/**
	 * 指定した短いURLに紐づいた短縮機を取得する
	 * 
	 * @param shortUrl 短いURL
	 * @return 短縮機
	 */
	Optional<Shortner> findByShortUrl(String shortUrl);

	/**
	 * 指定した長いURLに紐づいた短縮機を取得する
	 * 
	 * @param longUrl 長いURL
	 * @return 短縮機
	 */
	Optional<Shortner> findByLongUrl(String longUrl);
}
