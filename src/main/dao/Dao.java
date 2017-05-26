package dao;

import javafx.scene.control.Tab;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import util.DBConn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/25.
 */
public class Dao {

    public void testDbUtilSelect() {
        Connection conn = DBConn.getConn();
        QueryRunner queryRunner = new QueryRunner();

        String sql = "select * from tbdemo limit 2";
        /**
         * rsh参数：这是一个ResultSetHandler类型，一般都是创建一个该类型的子类对象然后传进去，
         * 该类型有五个常用子类，而传递的子类对象会决定query这个方法的返回值。五个常用子类对象分别是：
         * BeanHandler、BeanListHandler、MapHandler、MapListHandler、ScalarHandler。传递这五个子类对
         * 象后query的返回值分别是：一个JavaBean对象、一个装有多个JavaBean对象的List集合对象、一个装有一
         * 行结果集的Map对象（也就是一个Map，Map装着的是一行结果集）、一个装有多个一行结果集的Map的List集合对象
         * （也就是List里有多个Map，每个Map都是一行结果集）、一个Object类型（这种一般运用在查询结果只有一行一列的情况）
         */
        try {
            System.out.println("使用map处理单行记录。");
            Map<String, Object> map = queryRunner.query(conn, sql, new MapHandler(), (Object[]) null);
            //MapHandler()用于获取结果集中的第一行数据，并将其封装到一个Map中，Map 中 key 是数据的列别名（as label）,
            // 如果没有就是列的实际名称，Map 中 value 就是列的值，注意代表列的 key 不区分大小写。
            for (Iterator<Map.Entry<String, Object>> i = map.entrySet().iterator(); i.hasNext(); ) {
                //keySet()方法返回值是Map中key值的集合；entrySet()的返回值也是返回一个Set集合，此集合的类型为Map.Entry。
                //Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry<K,V>。
                // 它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。
                Map.Entry<String, Object> e = i.next();
                System.out.println(e.getKey() + "=" + e.getValue());
            }

            System.out.println("使用map处理多行记录！");
            String sql2 = "select * from tbdemo limit 3";
            List<Map<String, Object>> list = queryRunner.query(conn, sql2, new MapListHandler(), (Object[]) null);
            //MapListHandler 类  （实现ResultSetHandler 接口）把从数据库中查询出的记录 都 放到List  集合当中，
            // List集合中每一个对象都是Map类型，可以根据这条记录的字段名读出相对应的值.

            System.out.println("-------------------------");
            for (Iterator<Map<String, Object>> li = list.iterator(); li.hasNext(); ) {
                //hasNext()， 则是判断当前元素是否存在，并指向下一个元素（即所谓的索引）
                Map<String, Object> m = li.next();
                //next(),  是返回当前元素， 并指向下一个元素。
                for (Iterator<Map.Entry<String, Object>> mi = m.entrySet().iterator(); mi.hasNext(); ) {
                    Map.Entry<String, Object> e = mi.next();
                    System.out.println(e.getKey() + "=" + e.getValue());
                }
            }

            System.out.println("使用bean处理单行记录！");
            String sql3 = "select * from tbdemo limit 2";
            Tab tab = queryRunner.query(conn, sql3, new BeanHandler<Tab>(Tab.class));
            //BeanListHandler 类  （实现ResultSetHandler 接口）把从数据库中的记录 放到List集合中 ，
            // List集合中每一个对象都是一个JavaBean类型的对象，可以根据get 方法得到值
            //BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
            //ArrayHandler：把结果集中的第一行数据转成对象数组。
            //KeyedHandler(name)：将结果集中的每一行数据都封装到一个Map里，再把这些map再存到一个map里，其key为指定的key。
            //MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
            System.out.println("tname=" + tab.getId());

            System.out.println("-------------------------");
            System.out.println("使用array处理单行记录！");
            String sql4 = "select * from tbdemo limit 1";
            Object[] array = queryRunner.query(conn, sql4, new ArrayListHandler()).toArray();

            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);
            }

            System.out.println("----------------------------------------------------");
            System.out.println("使用array处理多行记录！");
            String sql5 = "select * from tbdemo limit 3";
            List<Object[]> arrayList = queryRunner.query(conn, sql5, new ArrayListHandler());
            for (Iterator<Object[]> ite = arrayList.iterator(); ite.hasNext(); ) {
                Object[] a = ite.next();
                for (int i = 0; i < a.length; i++) {
                    System.out.println(a[i]);
                }
            }

            System.out.println("----------------------------------------------------------------");
            System.out.println("使用ColumnListHandler处理单行记录，返回其中指定的一列！");
            String sql6 = "select * from tbdemo limit 3";
            List<Object> colList = (List<Object>) queryRunner.query(conn, sql, new ColumnListHandler("username"));
            for (Iterator<Object> itr = colList.iterator(); itr.hasNext(); ) {
                System.out.println(itr.next());
            }

            System.out.println("-----------------------------------------------------------------------");
            System.out.println("使用ScalarHandler处理单行记录，只返回结果集第一行中的指定字段，如未指定字段，则返回第一个字段！");
            String sql7 = "select * from tbdemo";
            Object scalar1 = queryRunner.query(conn, sql7, new ScalarHandler("username"));
            System.out.println(scalar1);
            Object scalar2 = queryRunner.query(conn, "select * from tbdemo limit 2", new ScalarHandler("password"));
            System.out.println(scalar2);

            // 使用自定义的行处理器
            // Map中的KEY可按输入顺序输出
            System.out.println("------------------------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testDBUtilInsertDeleteUpdateSelect() {
        Connection conn = DBConn.getConn();
        QueryRunner queryRunner = new QueryRunner(true);

        //插数据
        try {
            //删除数据表中的所有数据，一般不用
            queryRunner.update(conn, "delete from tbdemo");
            queryRunner.update(conn, "truncate table tbdemo");
            //truncate table命令将快速删除数据表中的所有记录，但保留数据表结构。
            // 这种快速删除与delete from 数据表的删除全部数据表记录不一样，
            // delete命令删除的数据将存储在系统回滚段中，需要的时候，数据可以回滚恢复，而truncate命令删除的数据是不可以恢复的
            for (int i = 0; i < 20; i++) {
                queryRunner.update(conn, "insert into tbdemo(username,password) values(?,?)",  "张三","aabb");
            }

            System.out.println("-----------------------------------------------");
            System.out.println("插多条记录！");
            queryRunner.batch(conn, "insert into tbdemo(username,password) values(?,?)", new Object[][]{{"李四", "ccdd"}, {"王五", "ccdd"}});

            //删除数据
            System.out.println("删除数据信息！");
            queryRunner.update(conn, "delete from tbdemo where id = 10");
            queryRunner.update(conn, "delete from tbdemo where id = ?", 11);
            queryRunner.batch(conn, "delete from tbdemo where id = ?", new Object[][]{{1}, {2}});

            System.out.println("修改数据！");
            queryRunner.update(conn, "update tbdemo set username = ? where id = ?", "修改后的新值",5);
            System.out.println("最终的显示结果!");
            List<Map<String, Object>> li = queryRunner.query(conn, "select id,username from tbdemo", new MapListHandler(), (Object[]) null);
            for (Iterator<Map<String,Object>> iterator = li.iterator();iterator.hasNext();) {
                Map<String, Object> mi = iterator.next();
                for (Iterator<Map.Entry<String,Object>> m = mi.entrySet().iterator();m.hasNext();) {
                    Map.Entry<String, Object> e = m.next();
                    System.out.print(e.getKey()+"="+e.getValue());
                    System.out.print(",");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
