import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.*
import java.io.File
import java.io.FileInputStream

class Exel(file: String) {
    val fis = FileInputStream(File(file))
    var wb: XSSFWorkbook? = XSSFWorkbook(fis)
    var students: ArrayList<ArrayList<String>> ?= null
    var specializations:  ArrayList<ArrayList<String>> ?= null
    var performance:  ArrayList<ArrayList<String>> ?= null
    var groups:  ArrayList<ArrayList<String>> ?= null
    var disciplines_plans:  ArrayList<ArrayList<String>> ?= null
    var disciplines:  ArrayList<ArrayList<String>> ?= null
    var academic_plans:  ArrayList<ArrayList<String>> ?= null

    val sheet_stud = wb?.getSheet("students")
    val sheet_spec = wb?.getSheet("specializations")
    val sheet_perf = wb?.getSheet("performance")
    val sheet_groups = wb?.getSheet("groups")
    val sheet_disc_plans = wb?.getSheet("disciplines_plans")
    val sheet_disp = wb?.getSheet("disciplines")
    val sheet_ac_plans = wb?.getSheet("academic_plans")

    init {
        if (sheet_stud != null) students = GetData(sheet_stud)
        if (sheet_spec != null) specializations = GetData(sheet_spec)
        if (sheet_perf != null) performance = GetData(sheet_perf)
        if (sheet_groups != null) groups = GetData(sheet_groups)
        if (sheet_disc_plans != null) disciplines_plans = GetData(sheet_disc_plans)
        if (sheet_disp != null) disciplines = GetData(sheet_disp)
        if (sheet_ac_plans != null) academic_plans = GetData(sheet_ac_plans)
    }
}
fun GetData(sheet: Sheet):  ArrayList<ArrayList<String>>? {
    val rows = sheet.iterator()
    var value: ArrayList<ArrayList<String>> = ArrayList()
    while(rows.hasNext()){
        val row = rows.next()
        val cells = row.iterator()
        var v: ArrayList<String> = ArrayList()
        while (cells.hasNext()) {
            val cell = cells.next()
            v.add( getCell(cell))
        }
        value.add(v)
    }
    return value
}

fun  getCell(cell: Cell): String {
    var v = "null"
    when (cell.getCellType()) {
        CellType.STRING -> v = cell.getRichStringCellValue().getString()

        CellType.NUMERIC ->
            if (DateUtil.isCellDateFormatted(cell)) {
               v = cell.getDateCellValue().toString()
            } else {
               v = cell.getNumericCellValue().toString()
            }

        CellType.BOOLEAN -> v = cell.getBooleanCellValue().toString()

        CellType.FORMULA -> v = cell.getCellFormula().toString()
    }
    return v
}

/*
val fis = FileInputStream(File(file))
    var wb: XSSFWorkbook? = XSSFWorkbook(fis)
    var students: HashMap<String, ArrayList<String>> ?= null
    var specializations: HashMap<String, ArrayList<String>> ?= null
    var performance: HashMap<String, ArrayList<String>> ?= null
    var groups: HashMap<String, ArrayList<String>> ?= null
    var disciplines_plans: HashMap<String, ArrayList<String>> ?= null
    var disciplines: HashMap<String, ArrayList<String>> ?= null
    var academic_plans: HashMap<String, ArrayList<String>> ?= null

    val sheet_stud = wb?.getSheet("students")
    val sheet_spec = wb?.getSheet("specializations")
    val sheet_perf = wb?.getSheet("performance")
    val sheet_groups = wb?.getSheet("groups")
    val sheet_disc_plans = wb?.getSheet("disciplines_plans")
    val sheet_disp = wb?.getSheet("disciplines")
    val sheet_ac_plans = wb?.getSheet("academic_plans")
 */



/*
fun GetData(sheet: Sheet): HashMap<String, ArrayList<String>>? {
    val rows = sheet.iterator()
    var k = 0
    var keys: MutableList<String> = ArrayList()
    var value: MutableList<MutableList<String>> = ArrayList()
    var value_end: MutableList<ArrayList<String>> = ArrayList()
    while(rows.hasNext()){
        val row = rows.next()
        val cells = row.iterator()
        var v: MutableList<String> = ArrayList()
        while (cells.hasNext()) {
            val cell = cells.next()
            if (k ==0) keys.add( cell.getStringCellValue() )
            if(k==1) v.add( getCell(cell))
        }
        if (k==1) value.add(v)
        k = 1
    }
    for(i in 0..(value[0].count() -1)){
        var v: ArrayList<String> = ArrayList()
        for(j in 0..(value.count() - 1)){
            v.add(value[j][i])
        }
        value_end.add(v)
    }
    var table: HashMap<String, ArrayList<String>> ?= HashMap()
    for (i in keys){
        for (j in 0..keys.count()-1){
            var s = sheet.getRow(0).getCell(j).toString()
            if (i == s) table?.put(i, value_end[j])
        }
    }
    return table
}
 */



