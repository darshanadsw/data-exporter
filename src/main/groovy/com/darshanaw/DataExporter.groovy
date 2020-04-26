package com.darshanaw

import groovy.sql.Sql

def sql = Sql.newInstance(url:'jdbc:hsqldb:mem:testDB', user:'sa', password:'', driver:'org.hsqldb.jdbc.JDBCDriver')
println 'connected...'

sql.execute "DROP TABLE IF EXISTS Employee"

sql.execute """
            CREATE TABLE Employee (
            ID int NOT NULL,
            LastName varchar(255) NOT NULL,
            FirstName varchar(255),
            Age int,
            PRIMARY KEY (ID)
            )
        """

List employees = [
        [id:1,firstName:'Darshana', lastName: 'Welikala', age: 38],
        [id:2, firstName:'Clovis', lastName: 'Nugeria', age: 45],
        [id:3, firstName:'Rafael', lastName: 'String', age: 40],
        [id:4, firstName:'Iury', lastName: 'Gagarin', age: 42],
        [id:5, firstName:'Rustam', lastName: 'Sirinovi', age: 38]
]

employees.each {emp ->
    sql.execute """
        INSERT INTO Employee (id,FirstName,LastName,Age)
        VALUES (${emp.id},${emp.firstName},${emp.lastName},${emp.age});
"""
}

def path = System.getProperty("user.dir").substring(0,System.getProperty("user.dir").indexOf('/src')) + '/data'

if(!new File(path).exists()){ new File(path).mkdir()}
def data = new File("${path}/employees.csv")
data.write("First Name,Last Name,Age \n")

sql.eachRow("SELECT * FROM Employee"){ row ->
    println "[${row.id} \t: ${row.FirstName} \t: ${row.LastName} :\t ${row.age}]"
    data.append("${row.FirstName},${row.LastName},${row.age}\n")
}

println "Employee data written to ${path}/employee.csv file successfully..."

sql.close();
println "Done..."