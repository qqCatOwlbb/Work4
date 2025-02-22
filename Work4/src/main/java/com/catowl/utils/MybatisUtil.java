package com.catowl.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory = null;

    //    获取sqlSessionFactory
    static {
        try {
            String resource = "mybatis/Mybatis-config.xml";
            InputStream resourceAsStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    sqlSession封装
    public static SqlSession sqlSession() {
        SqlSession sqlSession = null;
        if (sqlSessionFactory != null) {
            sqlSession = sqlSessionFactory.openSession();
        } else {
            try {
                System.out.println("程序错误!sqlSessionFactory数据无效");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sqlSession;
    }
}
