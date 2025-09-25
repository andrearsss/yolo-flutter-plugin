// Ultralytics 🚀 AGPL-3.0 License - https://ultralytics.com/license


enum ExerciseType {
  squat('Squat'),

  deadlift('Deadlift'),

  pullup('Pull-up');

  final String exerciseName;

  const ExerciseType(this.exerciseName);
}