import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/task_model.dart';
import '../providers/task_provider.dart';
import '../widgets/task_form_sheet.dart';
import '../widgets/task_tile.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<String> _filters = ['All', 'Academic', 'Work', 'Personal'];

  @override
  void initState() {
    super.initState();
    Future.microtask(() => context.read<TaskProvider>().loadTasks());
  }

  void _openTaskSheet([Task? task]) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) => TaskFormSheet(existingTask: task),
    );
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<TaskProvider>();
    final tasks = provider.filteredTasks;
    final progress = provider.totalCount == 0
        ? 0.0
        : provider.completedCount / provider.totalCount;

    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(8),
              child: Image.asset(
                'assets/icon/app_logo.png',
                width: 32,
                height: 32,
                errorBuilder: (_, __, ___) => const Icon(Icons.check_circle, size: 30),
              ),
            ),
            const SizedBox(width: 10),
            const Text('TaskFlow', style: TextStyle(fontWeight: FontWeight.bold)),
          ],
        ),
        actions: [
          if (provider.completedCount > 0)
            IconButton(
              tooltip: 'Clear Completed',
              icon: const Icon(Icons.delete_sweep_outlined),
              onPressed: () => provider.clearCompleted(),
            ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16),
        child: Column(
          children: [
            // Progress Card
            Card(
              color: Theme.of(context).colorScheme.primaryContainer,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Task Progress',
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                        ),
                        Text(
                          '${provider.completedCount} / ${provider.totalCount} Done',
                          style: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    ClipRRect(
                      borderRadius: BorderRadius.circular(8),
                      child: LinearProgressIndicator(
                        value: progress,
                        minHeight: 8,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Search Bar
            TextField(
              onChanged: provider.setSearchQuery,
              decoration: InputDecoration(
                hintText: 'Search tasks...',
                prefixIcon: const Icon(Icons.search),
                isDense: true,
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
              ),
            ),
            const SizedBox(height: 10),

            // Category Filter Chips
            SizedBox(
              height: 40,
              child: ListView.separated(
                scrollDirection: Axis.horizontal,
                itemCount: _filters.length,
                separatorBuilder: (_, __) => const SizedBox(width: 8),
                itemBuilder: (context, index) {
                  final category = _filters[index];
                  final isSelected = provider.selectedCategory == category;
                  return ChoiceChip(
                    label: Text(category),
                    selected: isSelected,
                    onSelected: (_) => provider.setCategoryFilter(category),
                  );
                },
              ),
            ),
            const SizedBox(height: 8),

            // Task List
            Expanded(
              child: tasks.isEmpty
                  ? const Center(
                child: Text(
                  'No tasks found.\nTap + to add your first task!',
                  textAlign: TextAlign.center,
                  style: TextStyle(color: Colors.grey, fontSize: 15),
                ),
              )
                  : ListView.builder(
                itemCount: tasks.length,
                itemBuilder: (context, index) {
                  final task = tasks[index];
                  return TaskTile(
                    task: task,
                    onToggle: () => provider.toggleTaskStatus(task),
                    onTap: () => _openTaskSheet(task),
                    onDelete: () {
                      final deletedTask = task;
                      provider.deleteTask(task.id!);
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text('Deleted "${deletedTask.title}"'),
                          action: SnackBarAction(
                            label: 'UNDO',
                            onPressed: () => provider.addTask(deletedTask),
                          ),
                        ),
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _openTaskSheet(),
        icon: const Icon(Icons.add),
        label: const Text('Add Task'),
      ),
    );
  }
}