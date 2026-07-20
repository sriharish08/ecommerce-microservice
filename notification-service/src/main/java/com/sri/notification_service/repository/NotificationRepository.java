package com.sri.notification_service.repository;

import com.sri.notification_service.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationHistory, Long> {
}
