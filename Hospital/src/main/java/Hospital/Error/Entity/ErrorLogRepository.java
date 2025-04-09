package Hospital.Error.Entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    // 가장 많이 발생한 예외 유형 TOP 5
    @Query("SELECT e.exceptionClass, COUNT(e) FROM ErrorLog e GROUP BY e.exceptionClass "
    		+ "ORDER BY COUNT(e) DESC")
    List<Object[]> findTopExceptions();

    // 에러가 가장 많이 발생한 클래스 TOP 5
    @Query("SELECT e.className, COUNT(e) FROM ErrorLog e GROUP BY e.className "
    		+ "ORDER BY COUNT(e) DESC")
    List<Object[]> findTopErrorClasses();
}