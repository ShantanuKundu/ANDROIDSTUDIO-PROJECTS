class Task {
  final int? id;
  final String title;
  final String description;
  final String category; // Academic, Personal, Work
  final int priority;    // 1: Low, 2: Medium, 3: High
  final DateTime dueDate;
  final bool isCompleted;

  Task({
    this.id,
    required this.title,
    required this.description,
    required this.category,
    required this.priority,
    required this.dueDate,
    this.isCompleted = false,
  });

  Task copyWith({
    int? id,
    String? title,
    String? description,
    String? category,
    int? priority,
    DateTime? dueDate,
    bool? isCompleted,
  }) {
    return Task(
      id: id ?? this.id,
      title: title ?? this.title,
      description: description ?? this.description,
      category: category ?? this.category,
      priority: priority ?? this.priority,
      dueDate: dueDate ?? this.dueDate,
      isCompleted: isCompleted ?? this.isCompleted,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'category': category,
      'priority': priority,
      'dueDate': dueDate.toIso8601String(),
      'isCompleted': isCompleted ? 1 : 0,
    };
  }

  factory Task.fromMap(Map<String, dynamic> map) {
    return Task(
      id: map['id'] as int?,
      title: map['title'] as String,
      description: map['description'] as String,
      category: map['category'] as String,
      priority: map['priority'] as int,
      dueDate: DateTime.parse(map['dueDate'] as String),
      isCompleted: (map['isCompleted'] as int) == 1,
    );
  }
}