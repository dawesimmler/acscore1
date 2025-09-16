#!/usr/bin/env python3
"""
Test suite for acscore module
"""

import unittest
from acscore import ACScore, calculate_weighted_score, normalize_score


class TestACScore(unittest.TestCase):
    """Test cases for the ACScore class."""
    
    def setUp(self):
        """Set up test fixtures before each test method."""
        self.scorer = ACScore()
    
    def test_init(self):
        """Test ACScore initialization."""
        self.assertEqual(self.scorer.get_count(), 0)
        self.assertEqual(self.scorer.get_total(), 0.0)
        self.assertEqual(self.scorer.get_average(), 0.0)
    
    def test_add_score(self):
        """Test adding scores."""
        self.scorer.add_score(85.5)
        self.assertEqual(self.scorer.get_count(), 1)
        self.assertEqual(self.scorer.get_total(), 85.5)
        
        self.scorer.add_score(92.0, "test")
        self.assertEqual(self.scorer.get_count(), 2)
        self.assertEqual(self.scorer.get_score_by_name("test"), 92.0)
    
    def test_add_score_invalid(self):
        """Test adding invalid scores."""
        with self.assertRaises(ValueError):
            self.scorer.add_score("invalid")
        
        with self.assertRaises(ValueError):
            self.scorer.add_score(None)
    
    def test_statistics(self):
        """Test statistical calculations."""
        scores = [85.5, 92.0, 78.5, 90.0, 87.5]
        for score in scores:
            self.scorer.add_score(score)
        
        self.assertEqual(self.scorer.get_count(), 5)
        self.assertEqual(self.scorer.get_total(), sum(scores))
        self.assertAlmostEqual(self.scorer.get_average(), sum(scores) / len(scores))
        self.assertEqual(self.scorer.get_max(), max(scores))
        self.assertEqual(self.scorer.get_min(), min(scores))
        self.assertEqual(self.scorer.get_median(), 87.5)
    
    def test_empty_statistics(self):
        """Test statistics with no scores."""
        self.assertEqual(self.scorer.get_count(), 0)
        self.assertEqual(self.scorer.get_total(), 0.0)
        self.assertEqual(self.scorer.get_average(), 0.0)
        self.assertEqual(self.scorer.get_median(), 0.0)
        self.assertEqual(self.scorer.get_max(), 0.0)
        self.assertEqual(self.scorer.get_min(), 0.0)
    
    def test_named_scores(self):
        """Test named score functionality."""
        self.scorer.add_score(85.5, "math")
        self.scorer.add_score(92.0, "science")
        
        self.assertEqual(self.scorer.get_score_by_name("math"), 85.5)
        self.assertEqual(self.scorer.get_score_by_name("science"), 92.0)
        self.assertIsNone(self.scorer.get_score_by_name("nonexistent"))
    
    def test_clear(self):
        """Test clearing scores."""
        self.scorer.add_score(85.5, "test")
        self.scorer.add_score(92.0)
        
        self.assertEqual(self.scorer.get_count(), 2)
        
        self.scorer.clear()
        self.assertEqual(self.scorer.get_count(), 0)
        self.assertIsNone(self.scorer.get_score_by_name("test"))
    
    def test_get_statistics(self):
        """Test the comprehensive statistics method."""
        scores = [85.5, 92.0, 78.5]
        for score in scores:
            self.scorer.add_score(score)
        
        stats = self.scorer.get_statistics()
        
        self.assertEqual(stats['count'], 3)
        self.assertEqual(stats['total'], sum(scores))
        self.assertAlmostEqual(stats['average'], sum(scores) / len(scores))
        self.assertEqual(stats['max'], max(scores))
        self.assertEqual(stats['min'], min(scores))
        self.assertEqual(stats['median'], 85.5)


class TestWeightedScore(unittest.TestCase):
    """Test cases for weighted scoring functions."""
    
    def test_calculate_weighted_score(self):
        """Test weighted score calculation."""
        scores = [85.0, 90.0, 80.0]
        weights = [0.3, 0.5, 0.2]
        
        expected = (85.0 * 0.3 + 90.0 * 0.5 + 80.0 * 0.2) / (0.3 + 0.5 + 0.2)
        result = calculate_weighted_score(scores, weights)
        
        self.assertAlmostEqual(result, expected, places=2)
    
    def test_weighted_score_empty(self):
        """Test weighted score with empty lists."""
        result = calculate_weighted_score([], [])
        self.assertEqual(result, 0.0)
    
    def test_weighted_score_mismatched_lengths(self):
        """Test weighted score with mismatched list lengths."""
        with self.assertRaises(ValueError):
            calculate_weighted_score([85.0, 90.0], [0.3])
    
    def test_weighted_score_zero_weights(self):
        """Test weighted score with zero total weight."""
        scores = [85.0, 90.0]
        weights = [0.0, 0.0]
        
        result = calculate_weighted_score(scores, weights)
        self.assertEqual(result, 0.0)


class TestNormalizeScore(unittest.TestCase):
    """Test cases for score normalization."""
    
    def test_normalize_score_within_range(self):
        """Test normalizing a score already within range."""
        result = normalize_score(75.0, 0.0, 100.0)
        self.assertEqual(result, 75.0)
    
    def test_normalize_score_above_max(self):
        """Test normalizing a score above maximum."""
        result = normalize_score(150.0, 0.0, 100.0)
        self.assertEqual(result, 100.0)
    
    def test_normalize_score_below_min(self):
        """Test normalizing a score below minimum."""
        result = normalize_score(-50.0, 0.0, 100.0)
        self.assertEqual(result, 0.0)
    
    def test_normalize_score_invalid_range(self):
        """Test normalizing with invalid range."""
        with self.assertRaises(ValueError):
            normalize_score(50.0, 100.0, 0.0)  # max < min
        
        with self.assertRaises(ValueError):
            normalize_score(50.0, 50.0, 50.0)  # max == min
    
    def test_normalize_score_custom_range(self):
        """Test normalizing with custom range."""
        result = normalize_score(75.0, 50.0, 80.0)
        self.assertEqual(result, 75.0)
        
        result = normalize_score(100.0, 50.0, 80.0)
        self.assertEqual(result, 80.0)
        
        result = normalize_score(25.0, 50.0, 80.0)
        self.assertEqual(result, 50.0)


if __name__ == "__main__":
    unittest.main()