import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import '../models/task_model.dart';

class DatabaseHelper {
  static final DatabaseHelper instance = DatabaseHelper._init();
  static Database? _database;

  DatabaseHelper._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('tasks_crud.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, filePath);

    return await openDatabase(
      path,
      version: 1,
      onCreate: _createDB,
    );
  }

  Future<void> _createDB(Database db, int version) async {
    await db.execute('''
      CREATE TABLE tasks (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        description TEXT NOT NULL,
        category TEXT NOT NULL,
        priority INTEGER NOT NULL,
        dueDate TEXT NOT NULL,
        isCompleted INTEGER NOT NULL
      )
    ''');
  }

  // CREATE
  Future<Task> insertTask(Task task) async {
    final db = await instance.database;
    final id = await db.insert('tasks', task.toMap());
    return task.copyWith(id: id);
  }

  // READ
  Future<List<Task>> fetchAllTasks() async {
    final db = await instance.database;
    final result = await db.query('tasks', orderBy: 'isCompleted ASC, priority DESC, dueDate ASC');
    return result.map((json) => Task.fromMap(json)).toList();
  }

  // UPDATE
  Future<int> updateTask(Task task) async {
    final db = await instance.database;
    return db.update(
      'tasks',
      task.toMap(),
      where: 'id = ?',
      whereArgs: [task.id],
    );
  }

  // DELETE (Single)
  Future<int> deleteTask(int id) async {
    final db = await instance.database;
    return await db.delete(
      'tasks',
      where: 'id = ?',
      whereArgs: [id],
    );
  }

  // DELETE (All Completed)
  Future<int> deleteCompletedTasks() async {
    final db = await instance.database;
    return await db.delete(
      'tasks',
      where: 'isCompleted = ?',
      whereArgs: [1],
    );
  }
}