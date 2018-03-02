数据库访问要点
注意不同版本的mysql的url的要求不一样
1、使用JDBC方式访问需要以下几点
（1）使用Class.forname()注册驱动和数据库信息
（2）使用DriverManager.getConnection(url,uername,password)获取Connection
（3）通过Connection获取PreparedStatment,
（4）通过PreparedStatment的executeUpdate(sql)或executeQuery(sql)，执行sql，并获取Resultset
（5）返回结果

2. 连接池的形式主要在于获取Connection的方式不同，因此需要定义一个getConnection()的方法