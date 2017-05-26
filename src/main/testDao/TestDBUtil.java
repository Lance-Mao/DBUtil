package testDao;

import dao.Dao;

/**
 * Created by admin on 2017/5/26.
 */
public class TestDBUtil {
    public static void main(String[] args) {
        Dao dao = new Dao();
        //dao.testDbUtilSelect();
        dao.testDBUtilInsertDeleteUpdateSelect();
    }
}
