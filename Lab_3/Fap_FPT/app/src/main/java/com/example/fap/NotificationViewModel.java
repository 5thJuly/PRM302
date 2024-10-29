package com.example.fap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<List<NotificationItem>> notificationList;

    public LiveData<List<NotificationItem>> getNotifications() {
        if (notificationList == null) {
            notificationList = new MutableLiveData<>();
            loadNotifications();
        }
        return notificationList;
    }

    private void loadNotifications() {
        List<NotificationItem> notifications = new ArrayList<>();
        notifications.add(new NotificationItem("Exam Schedule", "Midterms are coming up next week."));
        notifications.add(new NotificationItem("Holiday Notice", "The university will be closed for Winter holidays."));
        notifications.add(new NotificationItem("New Coursera is Now Available", "Enroll in the new course in this semester."));
        notificationList.setValue(notifications);
    }
}
