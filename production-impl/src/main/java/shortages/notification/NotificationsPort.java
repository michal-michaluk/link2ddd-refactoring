package shortages.notification;

import shortages.Shortages;

public interface NotificationsPort {
    void alertPlanner(Shortages shortages);

    void softNotifyPlanner(Shortages shortages);

    void markOnPlan(Shortages shortages);
}
