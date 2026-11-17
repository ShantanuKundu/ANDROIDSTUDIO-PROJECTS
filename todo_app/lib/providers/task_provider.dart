import 'package:flutter/material.dart';
import '../models/task_model.dart';
import '../services/database_helper.dart';

class TaskProvider extends ChangeNotifier {
  List<Task> _tasks = [];
  String _searchQuery = '';
  String _selectedCategory = 'All';

  String get selectedCategory => _selectedCategory;

  List<Task> get filteredTasks {
    return _tasks.where((task) {
      final matchesCategory =
          _selectedCategory == 'All' || task.category == _selectedCategory;
      final matchesSearch =
          task.title.toLowerCase().contains(_searchQuery.toLowerCase()) ||
              task.description.toLowerCase().contains(_searchQuery.toLowerCase());
      return matchesCategory && matchesSearch;
    }).toList();
  }

  int get totalCount => _tasks.length;
  int get completedCount => _tasks.where((t) => t.isCompleted).length;

  Future<void> loadTasks() async {
    _tasks = await DatabaseHelper.instance.fetchAllTasks();
    notifyListeners();
  }

  void setSearchQuery(String query) {
    _searchQuery = query;
    notifyListeners();
  }

  void setCategoryFilter(String category) {
    _selectedCategory = category;
    notifyListeners();
  }

  Future<void> addTask(Task task) async {
    await DatabaseHelper.instance.insertTask(task);
    await loadTasks();
  }

  Future<void> updateTask(Task task) async {
    await DatabaseHelper.instance.updateTask(task);
    await loadTasks();
  }

  Future<void> toggleTaskStatus(Task task) async {
    final updated = task.copyWith(isCompleted: !task.isCompleted);
    await DatabaseHelper.instance.updateTask(updated);
    await loadTasks();
  }

  Future<void> deleteTask(int id) async {
    await DatabaseHelper.instance.deleteTask(id);
    await loadTasks();
  }

  Future<void> clearCompleted() async {
    await DatabaseHelper.instance.deleteCompletedTasks();
    await loadTasks();
  }
}