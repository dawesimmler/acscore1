# acscore1

A simple and flexible scoring system module for Python.

## Overview

`acscore` provides a comprehensive scoring system that can:
- Store and manage individual scores
- Calculate statistical measures (mean, median, min, max)
- Perform weighted score calculations
- Normalize scores to specific ranges
- Provide both programmatic API and command-line interface

## Features

- **Score Management**: Add, store, and retrieve scores with optional names
- **Statistics**: Calculate comprehensive statistics including mean, median, min, max, total, and count
- **Weighted Scoring**: Calculate weighted averages with custom weights
- **Score Normalization**: Clamp scores to specified ranges
- **CLI Interface**: Command-line tools for interactive scoring operations
- **Type Safety**: Full type hints for better development experience

## Installation

Clone the repository and install:

```bash
git clone https://github.com/dawesimmler/acscore1.git
cd acscore1
pip install -e .
```

## Quick Start

### Python API

```python
from acscore import ACScore, calculate_weighted_score, normalize_score

# Create a scorer instance
scorer = ACScore()

# Add scores
scorer.add_score(85.5, "math")
scorer.add_score(92.0, "science")
scorer.add_score(78.5, "english")

# Get statistics
stats = scorer.get_statistics()
print(f"Average: {stats['average']:.2f}")
print(f"Total: {stats['total']}")

# Calculate weighted score
scores = [85.5, 92.0, 78.5]
weights = [0.3, 0.4, 0.3]
weighted_avg = calculate_weighted_score(scores, weights)
print(f"Weighted Average: {weighted_avg:.2f}")

# Normalize a score
normalized = normalize_score(120.0, 0.0, 100.0)  # Clamps 120 to 100
print(f"Normalized: {normalized}")
```

### Command Line Interface

```bash
# Add scores
acscore add 85.5 92.0 78.5

# Add a named score
acscore add 95.0 --name "final_exam"

# Show statistics
acscore stats

# Calculate weighted average
acscore weighted 85.5,92.0,78.5 0.3,0.4,0.3

# Normalize a score
acscore normalize 120 --min 0 --max 100
```

## API Reference

### ACScore Class

#### Methods

- `add_score(score: float, name: Optional[str] = None)` - Add a score
- `get_total() -> float` - Sum of all scores
- `get_average() -> float` - Average of all scores
- `get_median() -> float` - Median of all scores
- `get_max() -> float` - Maximum score
- `get_min() -> float` - Minimum score
- `get_count() -> int` - Number of scores
- `get_score_by_name(name: str) -> Optional[float]` - Get named score
- `get_statistics() -> Dict[str, Union[float, int]]` - Comprehensive statistics
- `clear()` - Clear all scores

### Utility Functions

- `calculate_weighted_score(scores: List[float], weights: List[float]) -> float`
- `normalize_score(score: float, min_val: float = 0.0, max_val: float = 100.0) -> float`

## Testing

Run the test suite:

```bash
python -m pytest test_acscore.py -v
```

Or using unittest:

```bash
python test_acscore.py
```

## Examples

### Grade Book Example

```python
from acscore import ACScore

# Create a grade book
gradebook = ACScore()

# Add student scores
gradebook.add_score(88.5, "Alice")
gradebook.add_score(92.0, "Bob")
gradebook.add_score(79.5, "Charlie")
gradebook.add_score(95.0, "Diana")

# Get class statistics
stats = gradebook.get_statistics()
print(f"Class Average: {stats['average']:.1f}")
print(f"Highest Score: {stats['max']}")
print(f"Lowest Score: {stats['min']}")
print(f"Number of Students: {stats['count']}")
```

### Weighted Assessment Example

```python
from acscore import calculate_weighted_score

# Calculate final grade with weighted components
homework_scores = [85, 90, 88]
quiz_scores = [92, 88, 95]
exam_score = 87

# Calculate component averages
homework_avg = sum(homework_scores) / len(homework_scores)
quiz_avg = sum(quiz_scores) / len(quiz_scores)

# Weight: 30% homework, 30% quizzes, 40% exam
final_grade = calculate_weighted_score(
    [homework_avg, quiz_avg, exam_score],
    [0.3, 0.3, 0.4]
)

print(f"Final Grade: {final_grade:.1f}")
```

## License

MIT License - see LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request
