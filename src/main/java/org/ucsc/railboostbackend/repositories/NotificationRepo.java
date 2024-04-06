package org.ucsc.railboostbackend.repositories;
import org.ucsc.railboostbackend.models.Notification;
import java.util.List;

public interface NotificationRepo {
    void save(Notification notification);
    List<Notification> findAll();
}
