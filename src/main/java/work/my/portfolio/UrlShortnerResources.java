package work.my.portfolio;

import java.util.zip.CRC32;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import work.my.portfolio.entity.Shortner;
import work.my.portfolio.service.ShortnerService;

/**
 * URL短縮機Resources
 * 
 * @author kinoshita daiki
 * @since 2023/11/07
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, onConstructor_ = { @Inject })
@Path("/")
public class UrlShortnerResources {

	private final ShortnerService service;

	/**
	 * 短縮機を取得する
	 * 
	 * @param longUrl 長いURL
	 * @return 短縮機
	 */
	@Path("/urlShortnerGetter")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Shortner getShortnerByLongUrl(
			@NotBlank(message = "URL may not be blank") String longUrl) {
		return service.findByLongUrl(longUrl)//
				.orElseGet(() -> {//
					var checksum = new CRC32();
					checksum.update(longUrl.getBytes());
					var newEntity = new Shortner(String.format("%x", checksum.getValue()), longUrl);
					service.save(newEntity);
					return newEntity;
				});
	}

	/**
	 * 短縮機を取得する
	 * 
	 * @param shortUrlPath 短いURLのパス部分
	 * @return 短縮機
	 */
	@Path("{shortUrlPath}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Shortner getShortnerByShortUrlPath(@PathParam("shortUrlPath") String shortUrlPath) {
		return service.findByShortUrl(shortUrlPath).orElseGet(() -> new Shortner(null, null));
	}
}
