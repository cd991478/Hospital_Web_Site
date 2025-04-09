package Hospital.SpotNews.Entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotNewsRepository extends JpaRepository<SpotNews, Integer> {
	List<SpotNews> findTop5ByOrderByPublishedAtDesc();// 최신 순 5개 정렬
}
