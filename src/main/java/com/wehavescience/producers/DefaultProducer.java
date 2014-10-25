package com.wehavescience.producers;

import com.wehavescience.qualifiers.MySQL;
import com.wehavescience.qualifiers.Startup;
import com.wehavescience.repositories.PersonRepository;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

/**
 * @author Gabriel Francisco  <gabfssilva@gmail.com>
 */
public class DefaultProducer {

    @Produces @ApplicationScoped @Startup
    public SqlSessionFactory sqlSessionFactory(@MySQL DataSource dataSource, TransactionFactory transactionFactory) {
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMappers("com.wehavescience.repositories");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    @Produces @RequestScoped
    public TransactionFactory transactionFactory() {
        return new JdbcTransactionFactory();
    }

    @Produces
    @MySQL
    @ApplicationScoped
    public DataSource mySqlDataSource() {
        PooledDataSource dataSource = new PooledDataSource();

        dataSource.setDriver("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:mysql://localhost/wehavescience");
        dataSource.setPoolMaximumActiveConnections(10);
        dataSource.setPoolMaximumIdleConnections(5);
        dataSource.setPoolPingQuery("SELECT 1");

        return dataSource;
    }

    @Produces @RequestScoped
    public SqlSession sqlSession(@Any SqlSessionFactory sqlSessionFactory){
        return sqlSessionFactory.openSession(true);
    }

    public void sqlSession(@Any @Disposes SqlSession sqlSession){
        sqlSession.close();
    }

    @Produces @RequestScoped
    public PersonRepository personRepository(SqlSession sqlSession){
        return sqlSession.getMapper(PersonRepository.class);
    }
}
