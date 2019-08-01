package by.kolenchik.core.schedule;

import by.kolenchik.core.service.user.UserService;
import by.kolenchik.core.service.user.UserServiceImpl;
import by.kolenchik.core.utils.ClassNameUtils;
import ch.qos.logback.classic.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class ContactBirthdayScheduler {

    static {
        start();
    }

    private static final Logger log =
            Logger.getLogger(ClassNameUtils.getCurrentClassName());

    private static void start() {

        EmailSchedule emailSchedule = new EmailSchedule();

        UserService userService = new UserServiceImpl();
        List<Long> allUserIdsByDate = userService.getAllUserIdsByDate(new Date());

        String email = "notanord@gmail.com";
        String subject = "List of contacts who have birthday today";
        String text;

        StringBuffer stringBuffer = new StringBuffer("");

        if (allUserIdsByDate.size() == 0 || allUserIdsByDate == null) {

            stringBuffer.append("Today nobody celebrates birthday!");

            text = stringBuffer.toString();

        } else {

            stringBuffer.append("Contact id list who celebrates birthday today : ");

            for (int i = 0; i < allUserIdsByDate.size(); i++) {
                stringBuffer.append(allUserIdsByDate.get(i)).append(", ");
            }

            text = stringBuffer.substring(0, stringBuffer.length() - 3) + ".";

        }

        emailSchedule.sendEmail(email, subject, text);

        try {
            org.slf4j.Logger logger =
                    LoggerFactory.getLogger(Class.forName("org.quartz.core.QuartzSchedulerThread"));
            if (logger != null && logger instanceof ch.qos.logback.classic.Logger) {
                //the slf4j Logger interface doesn't expose any configuration API's, but
                //we can cast to a class that does; so cast it and disable the logger
                ((ch.qos.logback.classic.Logger)logger).setLevel(
                        Level.ALL);
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }
}
