
package com.zafainternational.ntcinterns

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "NTCIntern.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {

        // =====================================
        // ACCOUNTS TABLE
        // =====================================

        db.execSQL(
            """
            CREATE TABLE accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            )
            """.trimIndent()
        )

        // =====================================
        // TASKS TABLE
        // =====================================

        db.execSQL(
            """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                intern_id INTEGER NOT NULL,
                deadline TEXT NOT NULL,
                status TEXT NOT NULL,
                feedback TEXT
            )
            """.trimIndent()
        )

        // =====================================
        // ATTENDANCE TABLE
        // =====================================

        db.execSQL(
            """
            CREATE TABLE attendance (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                intern_id INTEGER NOT NULL,
                date TEXT NOT NULL,
                status TEXT NOT NULL,
                UNIQUE(intern_id, date)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        // For version 4, add attendance table
        if (oldVersion < 4) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS attendance (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    intern_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    status TEXT NOT NULL,
                    UNIQUE(intern_id, date)
                )
                """.trimIndent()
            )

        }
    }

    // =====================================
    // CREATE ACCOUNT
    // =====================================

    fun createAccount(
        name: String,
        email: String,
        password: String,
        role: String
    ): Boolean {

        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT id FROM accounts WHERE email=?",
            arrayOf(email)
        )

        val emailExists = cursor.moveToFirst()

        cursor.close()

        if (emailExists) {
            return false
        }

        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("role", role)
        }

        val result = db.insert(
            "accounts",
            null,
            values
        )

        return result != -1L
    }

    // =====================================
    // LOGIN USER
    // =====================================

    fun loginUser(
        email: String,
        password: String,
        role: String
    ): Int {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id
            FROM accounts
            WHERE email=?
            AND password=?
            AND role=?
            """.trimIndent(),
            arrayOf(
                email,
                password,
                role
            )
        )

        var id = -1

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0)
        }

        cursor.close()

        return id
    }

    // =====================================
    // GET ONLY REGISTERED INTERNS
    // =====================================

    fun getInterns(): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id, name
            FROM accounts
            WHERE role='Intern'
            ORDER BY name ASC
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val name = cursor.getString(1)

            list.add("$id - $name")
        }

        cursor.close()

        return list
    }

    // =====================================
    // ADD TASK
    // =====================================

    fun addTask(
        title: String,
        description: String,
        internId: Int,
        deadline: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {

            put("title", title)
            put("description", description)
            put("intern_id", internId)
            put("deadline", deadline)
            put("status", "Assigned")
            put("feedback", "")
        }

        val result = db.insert(
            "tasks",
            null,
            values
        )

        return result != -1L
    }

    // =====================================
    // GET INTERN TASKS
    // =====================================

    fun getInternTasks(
        internId: Int
    ): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id, title, deadline, status
            FROM tasks
            WHERE intern_id=?
            ORDER BY id DESC
            """.trimIndent(),
            arrayOf(internId.toString())
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val deadline = cursor.getString(2)
            val status = cursor.getString(3)

            list.add(
                "$id|$title|$deadline|$status"
            )
        }

        cursor.close()

        return list
    }

    // =====================================
    // GET TASK DETAILS
    // =====================================

    fun getTask(
        taskId: Int
    ): Array<String>? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                title,
                description,
                deadline,
                status,
                feedback
            FROM tasks
            WHERE id=?
            """.trimIndent(),
            arrayOf(taskId.toString())
        )

        var data: Array<String>? = null

        if (cursor.moveToFirst()) {

            data = arrayOf(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4) ?: ""
            )
        }

        cursor.close()

        return data
    }

    // =====================================
    // START TASK
    // =====================================

    fun startTask(
        taskId: Int
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("status", "In Progress")
        }

        val result = db.update(
            "tasks",
            values,
            "id=?",
            arrayOf(taskId.toString())
        )

        return result > 0
    }

    // =====================================
    // SUBMIT TASK
    // =====================================

    fun submitTask(
        taskId: Int
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("status", "Completed")
        }

        val result = db.update(
            "tasks",
            values,
            "id=?",
            arrayOf(taskId.toString())
        )

        return result > 0
    }

    // =====================================
    // GET ALL TASKS
    // =====================================

    fun getAllTasks(): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                tasks.id,
                tasks.title,
                accounts.name,
                tasks.deadline,
                tasks.status
            FROM tasks
            INNER JOIN accounts
            ON tasks.intern_id = accounts.id
            WHERE accounts.role='Intern'
            ORDER BY tasks.id DESC
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val intern = cursor.getString(2)
            val deadline = cursor.getString(3)
            val status = cursor.getString(4)

            list.add(
                "$id|$title|$intern|$deadline|$status"
            )
        }

        cursor.close()

        return list
    }

    // =====================================
    // APPROVE TASK
    // =====================================

    fun approveTask(
        taskId: Int,
        feedback: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("status", "Approved")
            put("feedback", feedback)
        }

        val result = db.update(
            "tasks",
            values,
            "id=?",
            arrayOf(taskId.toString())
        )

        return result > 0
    }

    // =====================================
    // REJECT TASK
    // =====================================

    fun rejectTask(
        taskId: Int,
        feedback: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("status", "Rejected")
            put("feedback", feedback)
        }

        val result = db.update(
            "tasks",
            values,
            "id=?",
            arrayOf(taskId.toString())
        )

        return result > 0
    }

    // =====================================
    // PROGRESS
    // =====================================

    fun getProgress(): Array<Int> {

        val total =
            getCount(
                "SELECT COUNT(*) FROM tasks"
            )

        val completed =
            getCount(
                "SELECT COUNT(*) FROM tasks WHERE status='Completed'"
            )

        val approved =
            getCount(
                "SELECT COUNT(*) FROM tasks WHERE status='Approved'"
            )

        val rejected =
            getCount(
                "SELECT COUNT(*) FROM tasks WHERE status='Rejected'"
            )

        return arrayOf(
            total,
            completed,
            approved,
            rejected
        )
    }

    // =====================================
    // MARK ATTENDANCE
    // =====================================

    fun markAttendance(
        internId: Int,
        date: String,
        status: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("intern_id", internId)
            put("date", date)
            put("status", status)
        }

        // If attendance already exists for this intern/date,
        // update it instead of creating duplicate record.
        val result = db.insertWithOnConflict(
            "attendance",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        return result != -1L
    }

    // =====================================
    // GET ATTENDANCE FOR INTERN
    // =====================================

    fun getInternAttendance(
        internId: Int
    ): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT date, status
            FROM attendance
            WHERE intern_id=?
            ORDER BY date DESC
            """.trimIndent(),
            arrayOf(
                internId.toString()
            )
        )

        while (cursor.moveToNext()) {

            val date = cursor.getString(0)
            val status = cursor.getString(1)

            list.add(
                "$date|$status"
            )
        }

        cursor.close()

        return list
    }

    // =====================================
    // GET ALL ATTENDANCE
    // =====================================

    fun getAllAttendance(): ArrayList<String> {

        val list = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                attendance.id,
                accounts.name,
                attendance.date,
                attendance.status
            FROM attendance
            INNER JOIN accounts
            ON attendance.intern_id = accounts.id
            WHERE accounts.role='Intern'
            ORDER BY attendance.date DESC
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val date = cursor.getString(2)
            val status = cursor.getString(3)

            list.add(
                "$id|$name|$date|$status"
            )
        }

        cursor.close()

        return list
    }

    // =====================================
    // ATTENDANCE PERCENTAGE
    // =====================================

    fun getAttendancePercentage(
        internId: Int
    ): Int {

        val db = readableDatabase

        val totalCursor = db.rawQuery(
            """
            SELECT COUNT(*)
            FROM attendance
            WHERE intern_id=?
            """.trimIndent(),
            arrayOf(
                internId.toString()
            )
        )

        var total = 0

        if (totalCursor.moveToFirst()) {
            total = totalCursor.getInt(0)
        }

        totalCursor.close()

        if (total == 0) {
            return 0
        }

        val presentCursor = db.rawQuery(
            """
            SELECT COUNT(*)
            FROM attendance
            WHERE intern_id=?
            AND status='Present'
            """.trimIndent(),
            arrayOf(
                internId.toString()
            )
        )

        var present = 0

        if (presentCursor.moveToFirst()) {
            present = presentCursor.getInt(0)
        }

        presentCursor.close()

        return (present * 100) / total
    }

    // =====================================
    // GET TODAY ATTENDANCE
    // =====================================

    fun getTodayAttendance(
        internId: Int,
        date: String
    ): String? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT status
            FROM attendance
            WHERE intern_id=?
            AND date=?
            """.trimIndent(),
            arrayOf(
                internId.toString(),
                date
            )
        )

        var status: String? = null

        if (cursor.moveToFirst()) {
            status = cursor.getString(0)
        }

        cursor.close()

        return status
    }

    // =====================================
    // COUNT
    // =====================================

    private fun getCount(
        query: String
    ): Int {

        val db = readableDatabase

        val cursor = db.rawQuery(
            query,
            null
        )

        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()

        return count
    }
}