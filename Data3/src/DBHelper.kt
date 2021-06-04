import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.io.*

class DBHelper(
    val dbName: String,
    val host: String = "localhost",
    val port: Int = 3306,
    val username: String = "root",
    val password: String = "root")
{

    var conn:  Connection

    init {

        conn = DriverManager.getConnection(
            "jdbc:mysql://$host:$port/?serverTimezone=UTC",
            username, password
        )

        if (conn.createStatement().execute("show databases like '$dbName' ") != null){
            conn.createStatement()
                .execute(
                    "CREATE SCHEMA IF NOT EXISTS $dbName"
                )

            conn.createStatement()
                .execute(
                    "USE `$dbName` "
                )
        }
        else{
            conn.createStatement()
                .execute(
                    "USE `$dbName` "
                )
        }
        createTableStudents()
        createTableSpecializations()
        createTablePerformance()
        createTableGroups()
        createtableDisciplines_plans()
        createTableDisciplines()
        createTableAcademic_plans()
    }

    fun createTableStudents() {
        conn.createStatement()
            .execute(
                "CREATE TABLE IF NOT EXISTS students(id integer not null, " +
                        "first_name varchar(40)," +
                        "last_name varchar(40)," +
                        "mid_name varchar(40)," +
                        "group_id varchar(6)," +
                        "gender set('М', 'Ж')," +
                        "birthday date, " +
                        "primary key(id))"
            )
    }

    fun createTableSpecializations(){
        conn.createStatement()
            .execute(
                "CREATE TABLE IF NOT EXISTS specializations(id varchar(8), " +
                        "name varchar(40)," +
                        "primary  key(id))"
            )
    }

    fun createTablePerformance(){
        conn.createStatement()
            .execute(
                "CREATE TABLE IF NOT EXISTS performance(student_id integer not null, " +
                        "disciplines_plan_id integer not null," +
                        "score integer not null," +
                        "attempt integer not null," +
                        "primary key(student_id))"
            )
    }

    fun createTableGroups(){
        conn.createStatement()
            .execute(
                "CREATE TABLE  IF NOT EXISTS grops(id varchar(6), " +
                        "academic_plan_id integer not null," +
                        "qualification enum('master', 'bachelor', 'specialist')," +
                        "primary key(id))"
            )
    }

    fun createtableDisciplines_plans(){
        conn.createStatement()
            .execute(
                "CREATE TABLE  IF NOT EXISTS disciplines_plans(id integer not null, " +
                        "academic_plan_id integer not null," +
                        "discipline_id varchar(40)," +
                        "semester_number integer not null," +
                        "hours integer not null," +
                        "reporting_form varchar(10)," +
                        "primary key(id))"
            )
    }

    fun createTableDisciplines(){
        conn.createStatement()
            .execute(
                "CREATE TABLE  IF NOT EXISTS disciplines(id varchar(40), " +
                        "name varchar(80)," +
                        "primary key(id))"
            )
    }

    fun createTableAcademic_plans(){
        conn.createStatement()
            .execute(
                "CREATE TABLE  IF NOT EXISTS academic_plans(id integer not null , " +
                        "year year," +
                        "specialization_id varchar(8)," +
                        "primary key(id))"
            )
    }

    public fun setData(table: String, data: ArrayList<ArrayList<String>>, ){
        var name = ""
        var values = ""
        for (i in data[0]){
            name = name   + i  + " , "
        }
        name = name.substring(0, name.length - 2)
        for (i in data){
            if (i!=data[0]){
                for (j in i){
                    values = values + "'" + j + "'" +  " , "
                }
                values = values.substring(0, values.length - 2)
                var res = "INSERT INTO $table ($name) VALUES ($values)"
                conn.createStatement()
                    .execute(
                        "INSERT INTO $table ($name) VALUES ($values)"
                    )
                values = ""
            }
        }
    }

    public fun getStip(){
        conn.createStatement()
            .execute(
                "SELECT\n" +
                        "id,\n" +
                        "full_name,\n" +
                        "gr_id,\n" +
                        "CASE\n" +
                        " WHEN stipend = 3 THEN 'нет'\n" +
                        " WHEN stipend = 4 THEN 'обычная'\n" +
                        " WHEN stipend = 5 THEN 'повышенная'\n" +
                        "END AS stipend\n" +
                        "\n" +
                        "FROM (SELECT \n" +
                        "id, \n" +
                        "concat(first_name,' ',mid_name,' ',last_name) AS full_name,\n" +
                        "gr AS gr_id,\n" +
                        " MIN(IF(est = 5 , 5, if(est = 4 , 4,3))) AS stipend \n" +
                        "FROM\n" +
                        "(SELECT DISTINCT id,first_name,mid_name,last_name , gr,   \n" +
                        "CASE \n" +
                        "WHEN performance.score>85 AND TRIM(report) = 'экзамен' AND performance.attempt = 1 THEN 5\n" +
                        "WHEN performance.score>70 AND performance.score<86 AND TRIM(report) = 'экзамен' AND performance.attempt = 1 THEN 4\n" +
                        "WHEN performance.score>55 AND TRIM(report) = 'зачёт' AND performance.attempt=1 THEN 5\n" +
                        "ELSE 3\n" +
                        "END AS 'est'\n" +
                        "From \n" +
                        "(SELECT students.first_name as 'first_name',students.mid_name as 'mid_name',students.last_name as 'last_name' ,grops.id as 'gr', academic_plans.year as 'old', disciplines_plans.semester_number as 'sem',disciplines_plans.reporting_form as 'report',disciplines_plans.discipline_id as 'disp_p_disp_id', students.id as 'id'\n" +
                        "FROM `grops` LEFT JOIN academic_plans ON grops.academic_plan_id = academic_plans.id \n" +
                        " LEFT JOIN disciplines_plans ON disciplines_plans.academic_plan_id = academic_plans.id \n" +
                        " AND 2*(YEAR(NOW()) - year) - (CASE WHEN MONTH(NOW()) < 6 AND MONTH(NOW()) > 1 THEN 1 WHEN MONTH(NOW()) = 1 THEN 2 ELSE 0 END) = disciplines_plans.semester_number\n" +
                        " RIGHT JOIN students ON grops.id = students.group_id\n" +
                        ") as tb1 \n" +
                        " LEFT JOIN performance\n" +
                        "ON performance.student_id = id) AS tb_5\n" +
                        "GROUP BY id, full_name, gr_id \n" +
                        "ORDER BY gr, first_name) AS tab"
            )
    }


}

/*
INSERT INTO students (id,first_name,last_name,mid_name,group_id,gender,birthday )
VALUES (  1  ,   'Имя1.1'   ,  'Имя2.1'   ,   'Имя3.1' , '05-001','Ж',  '0000-00-00')
 */

/*
INSERT INTO disciplines_plans (id , academic_plane_id , discipline_id , semester_number , hours , reporting_form )
VALUES ('1.0' , '1.0' , 'Б1' , '3.0' , '72.0' , 'экзамен ' )
 */