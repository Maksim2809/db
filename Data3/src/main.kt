import java.io.*
import java.util.ArrayList

fun main(){
    val name_file = "C:\\Users\\Max\\Documents\\Data66.xlsx"
    val ex = Exel(name_file)
    val dbh = DBHelper("DataData")
    ex.students?.let { dbh.setData("students", it) }
         //     dbh.createTables()
    ex.specializations?.let { dbh.setData("specializations", it) }
    ex.performance?.let { dbh.setData("performance", it) }
    ex.groups?.let { dbh.setData("grops", it) }
    ex.disciplines_plans?.let { dbh.setData("disciplines_plans", it) }
    ex.disciplines?.let { dbh.setData("disciplines", it) }
    ex.academic_plans?.let { dbh.setData("academic_plans", it) }

    dbh.getStip()
 }