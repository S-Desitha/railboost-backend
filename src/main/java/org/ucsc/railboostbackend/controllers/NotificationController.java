package org.ucsc.railboostbackend.controllers;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.repositories.NotificationRepo;
import org.ucsc.railboostbackend.repositories.NotificationRepoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        import java.time.LocalDateTime;
        import java.util.List;


public class NotificationController extends HttpServlet {
    private final NotificationRepo notificationRepository = new NotificationRepoImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Notification> notifications = notificationRepository.findAll();
        String jsonNotifications = objectMapper.writeValueAsString(notifications);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonNotifications);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Read request body and parse JSON into Notification object
        Notification notification = objectMapper.readValue(request.getReader(), Notification.class);
        notification.setTimestamp(LocalDateTime.now());

        // Save the notification
        notificationRepository.save(notification);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}

