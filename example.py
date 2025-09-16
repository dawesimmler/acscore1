#!/usr/bin/env python3
"""
Example usage of acscore module
"""

from acscore import ACScore, calculate_weighted_score, normalize_score


def demo_basic_scoring():
    """Demonstrate basic scoring functionality."""
    print("=== Basic Scoring Demo ===")
    
    # Create a scorer
    scorer = ACScore()
    
    # Add some test scores
    test_scores = [
        (85.5, "Math Test 1"),
        (92.0, "Science Quiz"),
        (78.5, "English Essay"),
        (90.0, "Math Test 2"),
        (87.5, "History Project")
    ]
    
    print("Adding scores:")
    for score, name in test_scores:
        scorer.add_score(score, name)
        print(f"  {name}: {score}")
    
    # Show statistics
    print(f"\nStatistics:")
    stats = scorer.get_statistics()
    for key, value in stats.items():
        if isinstance(value, float):
            print(f"  {key.capitalize()}: {value:.2f}")
        else:
            print(f"  {key.capitalize()}: {value}")
    
    print()


def demo_weighted_scoring():
    """Demonstrate weighted scoring."""
    print("=== Weighted Scoring Demo ===")
    
    # Simulate a course with different weighted components
    homework_avg = 88.0
    quiz_avg = 85.0
    midterm = 82.0
    final_exam = 90.0
    
    scores = [homework_avg, quiz_avg, midterm, final_exam]
    weights = [0.2, 0.2, 0.3, 0.3]  # 20%, 20%, 30%, 30%
    
    final_grade = calculate_weighted_score(scores, weights)
    
    print("Course Grade Calculation:")
    components = ["Homework", "Quizzes", "Midterm", "Final Exam"]
    for component, score, weight in zip(components, scores, weights):
        print(f"  {component}: {score} (weight: {weight*100}%)")
    
    print(f"\nFinal Grade: {final_grade:.2f}")
    print()


def demo_normalization():
    """Demonstrate score normalization."""
    print("=== Score Normalization Demo ===")
    
    raw_scores = [120, -10, 75, 200, 50]
    
    print("Normalizing scores to 0-100 range:")
    for raw_score in raw_scores:
        normalized = normalize_score(raw_score, 0, 100)
        print(f"  {raw_score} -> {normalized}")
    
    print("\nNormalizing scores to 50-90 range:")
    for raw_score in raw_scores:
        if 0 <= raw_score <= 100:  # Only normalize valid percentages
            normalized = normalize_score(raw_score, 50, 90)
            print(f"  {raw_score} -> {normalized}")
    
    print()


def demo_gradebook_example():
    """Demonstrate a practical gradebook example."""
    print("=== Gradebook Example ===")
    
    # Create a class gradebook
    gradebook = ACScore()
    
    # Student scores
    students = {
        "Alice": [88, 92, 85, 90],
        "Bob": [79, 85, 88, 82],
        "Charlie": [95, 98, 92, 96],
        "Diana": [82, 78, 85, 88]
    }
    
    print("Student Final Scores:")
    for student, scores in students.items():
        # Calculate student's average
        avg_score = sum(scores) / len(scores)
        gradebook.add_score(avg_score, student)
        print(f"  {student}: {avg_score:.1f}")
    
    # Class statistics
    print(f"\nClass Statistics:")
    stats = gradebook.get_statistics()
    print(f"  Class Average: {stats['average']:.1f}")
    print(f"  Highest Score: {stats['max']:.1f}")
    print(f"  Lowest Score: {stats['min']:.1f}")
    print(f"  Class Size: {stats['count']}")
    
    # Grade distribution
    print(f"\nGrade Distribution:")
    for student in students.keys():
        score = gradebook.get_score_by_name(student)
        if score >= 90:
            grade = "A"
        elif score >= 80:
            grade = "B"
        elif score >= 70:
            grade = "C"
        elif score >= 60:
            grade = "D"
        else:
            grade = "F"
        print(f"  {student}: {score:.1f} ({grade})")


if __name__ == "__main__":
    demo_basic_scoring()
    demo_weighted_scoring()
    demo_normalization()
    demo_gradebook_example()