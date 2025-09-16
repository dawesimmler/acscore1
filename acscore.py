#!/usr/bin/env python3
"""
acscore - A simple scoring system module

This module provides basic scoring functionality including:
- Individual score calculation
- Score aggregation
- Score statistics
"""

from typing import List, Dict, Optional, Union
import statistics


class ACScore:
    """
    A scoring system that can calculate, store, and analyze scores.
    """
    
    def __init__(self):
        """Initialize the scoring system."""
        self.scores: List[float] = []
        self.named_scores: Dict[str, float] = {}
    
    def add_score(self, score: float, name: Optional[str] = None) -> None:
        """
        Add a score to the system.
        
        Args:
            score: The numerical score to add
            name: Optional name for the score
        """
        if not isinstance(score, (int, float)):
            raise ValueError("Score must be a number")
        
        self.scores.append(float(score))
        if name:
            self.named_scores[name] = float(score)
    
    def get_total(self) -> float:
        """Get the sum of all scores."""
        return sum(self.scores)
    
    def get_average(self) -> float:
        """Get the average of all scores."""
        if not self.scores:
            return 0.0
        return statistics.mean(self.scores)
    
    def get_median(self) -> float:
        """Get the median of all scores."""
        if not self.scores:
            return 0.0
        return statistics.median(self.scores)
    
    def get_max(self) -> float:
        """Get the maximum score."""
        if not self.scores:
            return 0.0
        return max(self.scores)
    
    def get_min(self) -> float:
        """Get the minimum score."""
        if not self.scores:
            return 0.0
        return min(self.scores)
    
    def get_count(self) -> int:
        """Get the number of scores."""
        return len(self.scores)
    
    def get_score_by_name(self, name: str) -> Optional[float]:
        """Get a score by its name."""
        return self.named_scores.get(name)
    
    def get_statistics(self) -> Dict[str, Union[float, int]]:
        """Get comprehensive statistics about all scores."""
        return {
            'count': self.get_count(),
            'total': self.get_total(),
            'average': self.get_average(),
            'median': self.get_median(),
            'min': self.get_min(),
            'max': self.get_max(),
        }
    
    def clear(self) -> None:
        """Clear all scores."""
        self.scores.clear()
        self.named_scores.clear()


def calculate_weighted_score(scores: List[float], weights: List[float]) -> float:
    """
    Calculate a weighted score from a list of scores and weights.
    
    Args:
        scores: List of numerical scores
        weights: List of weights corresponding to each score
        
    Returns:
        The weighted average score
        
    Raises:
        ValueError: If scores and weights lists have different lengths
    """
    if len(scores) != len(weights):
        raise ValueError("Scores and weights lists must have the same length")
    
    if not scores:
        return 0.0
    
    weighted_sum = sum(score * weight for score, weight in zip(scores, weights))
    total_weight = sum(weights)
    
    if total_weight == 0:
        return 0.0
    
    return weighted_sum / total_weight


def normalize_score(score: float, min_val: float = 0.0, max_val: float = 100.0) -> float:
    """
    Normalize a score to a specified range.
    
    Args:
        score: The score to normalize
        min_val: Minimum value of the target range
        max_val: Maximum value of the target range
        
    Returns:
        The normalized score
    """
    if max_val <= min_val:
        raise ValueError("max_val must be greater than min_val")
    
    # Clamp the score to the range
    normalized = max(min_val, min(max_val, score))
    return normalized


if __name__ == "__main__":
    # Example usage
    scorer = ACScore()
    
    # Add some scores
    scorer.add_score(85.5, "math")
    scorer.add_score(92.0, "science")
    scorer.add_score(78.5, "english")
    
    # Print statistics
    stats = scorer.get_statistics()
    print("Score Statistics:")
    for key, value in stats.items():
        print(f"  {key.capitalize()}: {value}")
    
    # Test weighted scoring
    scores = [85.5, 92.0, 78.5]
    weights = [0.3, 0.4, 0.3]
    weighted_avg = calculate_weighted_score(scores, weights)
    print(f"\nWeighted Average: {weighted_avg:.2f}")