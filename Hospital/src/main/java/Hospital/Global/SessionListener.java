package Hospital.Global;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        System.out.println("세션 생성됨: " + sessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        System.out.println("세션 종료됨: " + sessionEvent.getSession().getId());
    }
}
