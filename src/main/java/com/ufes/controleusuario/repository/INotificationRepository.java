package com.ufes.controleusuario.repository;
import com.ufes.controleusuario.model.Notification;
import java.util.List;
public interface INotificationRepository {
    void save(Notification notification);
    void update(Notification notification);
    void delete(Notification notification);
    List<Notification> findByUserId(int userId);
    int countUnreadByUserId(int userId);
    int countByUserId(int userId);
    void markAsRead(int notificationId);
    void markAllAsReadByUserId(int userId);
}
