package by.kolenchik.core.service.utils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {

    private static Logger log = Logger.getLogger(ServiceUtils.class);

    public List<Long> findUserIdMatches(List<List<Long>> list) {
        if (list.size() == 0) {
            return new ArrayList<>();
        } else if (list.size() == 1){
            return list.get(0);
        } else {
            List<Long> comparedList = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                boolean isRetainedAll = comparedList.retainAll(list.get(i));
                for (int j = 0; j < comparedList.size(); j++) {
                    log.trace("List id = " + comparedList.get(j));
                }
                log.trace("isRetainedAll = " + isRetainedAll);
            }
            return comparedList;
        }
    }
}
