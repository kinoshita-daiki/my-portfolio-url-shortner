package work.my.portfolio.service.impl;

import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.utils.StringUtils;
import work.my.portfolio.entity.Shortner;
import work.my.portfolio.repository.ShortnerRepository;
import work.my.portfolio.service.ShortnerService;

/**
 * 短縮機serviceImpl
 * 
 * @author kinoshita daiki
 * @since 2023/11/07
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor_ = { @Inject })
@RequestScoped
class ShortnerServiceImpl implements ShortnerService {

	private final ShortnerRepository repository;

	@Override
	public void save(Shortner entity) {
		repository.save(entity);
	}

	@Override
	public Optional<Shortner> findByShortUrl(String shortUrl) {
		if (StringUtils.isEmpty(shortUrl)) {
			// 不正なURL
			return Optional.empty();
		}
		return repository.find(shortUrl);
	}

	@Override
	public Optional<Shortner> findByLongUrl(String longUrl) {
		return repository.findByLongUrl(longUrl);
	}

}
