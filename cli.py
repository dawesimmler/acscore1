#!/usr/bin/env python3
"""
CLI interface for acscore module
"""

import argparse
import sys
from acscore import ACScore, calculate_weighted_score, normalize_score


def main():
    """Main CLI function for acscore."""
    parser = argparse.ArgumentParser(
        description="acscore - A simple scoring system",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  %(prog)s add 85.5 92.0 78.5          # Add multiple scores
  %(prog)s add 85.5 --name math        # Add a named score
  %(prog)s stats                       # Show statistics
  %(prog)s weighted 85,92,78 0.3,0.4,0.3  # Calculate weighted average
        """
    )
    
    subparsers = parser.add_subparsers(dest='command', help='Available commands')
    
    # Add command
    add_parser = subparsers.add_parser('add', help='Add scores')
    add_parser.add_argument('scores', nargs='+', type=float, help='Scores to add')
    add_parser.add_argument('--name', help='Name for the score (only works with single score)')
    
    # Stats command
    stats_parser = subparsers.add_parser('stats', help='Show statistics')
    
    # Weighted command
    weighted_parser = subparsers.add_parser('weighted', help='Calculate weighted average')
    weighted_parser.add_argument('scores', help='Comma-separated list of scores')
    weighted_parser.add_argument('weights', help='Comma-separated list of weights')
    
    # Normalize command
    normalize_parser = subparsers.add_parser('normalize', help='Normalize a score')
    normalize_parser.add_argument('score', type=float, help='Score to normalize')
    normalize_parser.add_argument('--min', type=float, default=0.0, help='Minimum value (default: 0.0)')
    normalize_parser.add_argument('--max', type=float, default=100.0, help='Maximum value (default: 100.0)')
    
    args = parser.parse_args()
    
    if not args.command:
        parser.print_help()
        return 1
    
    scorer = ACScore()
    
    if args.command == 'add':
        for score in args.scores:
            name = args.name if len(args.scores) == 1 else None
            scorer.add_score(score, name)
            if name:
                print(f"Added score {score} with name '{name}'")
            else:
                print(f"Added score {score}")
        
        # Show basic stats after adding
        print(f"\nTotal scores: {scorer.get_count()}")
        print(f"Average: {scorer.get_average():.2f}")
    
    elif args.command == 'stats':
        if scorer.get_count() == 0:
            print("No scores available. Add some scores first.")
            return 1
        
        stats = scorer.get_statistics()
        print("Score Statistics:")
        for key, value in stats.items():
            if isinstance(value, float):
                print(f"  {key.capitalize()}: {value:.2f}")
            else:
                print(f"  {key.capitalize()}: {value}")
    
    elif args.command == 'weighted':
        try:
            scores = [float(x.strip()) for x in args.scores.split(',')]
            weights = [float(x.strip()) for x in args.weights.split(',')]
            
            result = calculate_weighted_score(scores, weights)
            print(f"Weighted average: {result:.2f}")
            
            # Show the calculation details
            print("\nCalculation details:")
            for i, (score, weight) in enumerate(zip(scores, weights)):
                print(f"  Score {i+1}: {score} × {weight} = {score * weight}")
            print(f"  Total weighted sum: {sum(s * w for s, w in zip(scores, weights))}")
            print(f"  Total weights: {sum(weights)}")
            
        except ValueError as e:
            print(f"Error: {e}")
            return 1
    
    elif args.command == 'normalize':
        try:
            result = normalize_score(args.score, args.min, args.max)
            print(f"Normalized score: {result}")
            if result != args.score:
                print(f"  (Original: {args.score}, Range: {args.min}-{args.max})")
        except ValueError as e:
            print(f"Error: {e}")
            return 1
    
    return 0


if __name__ == "__main__":
    sys.exit(main())