using System;
using System.Collections.Generic;


namespace EmpiricalTesting
{

    class Program
    {
        static void Main(string[] args)
        {
            EvaluatePattern1();
            EvaluatePattern2();
            EvaluatePattern3();

            Console.Write("Press any key to close the window...");
            Console.ReadKey();
        }

        private static void EvaluatePattern1()
        {
            List<int> sizesForBoth = new List<int>() { 2, 5, 10, 15, 20, 21 };
            List<int> sizesForApproximation = new List<int> { 25, 50, 75, 100 };

            Console.Write("Pattern1:");
            Dictionary<int, Result> results = Tester.Evaluate(sizesForBoth, sizesForApproximation, Reader.ReadPattern1);

            Writer.WriteExecutionTimesToFile("execution_times_pattern1_", results);
        }

        private static void EvaluatePattern2()
        {
            List<int> sizesForBoth = new List<int>() { 5, 10, 15, 20, 25, 27 };
            List<int> sizesForApproximation = new List<int> { 50, 75, 100, 150, 200 };

            Console.Write("Pattern2:");
            Dictionary<int, Result> results = Tester.Evaluate(sizesForBoth, sizesForApproximation, Reader.ReadPattern2);

            Writer.WriteExecutionTimesToFile("execution_times_pattern2_", results);
        }

        private static void EvaluatePattern3()
        {
            List<int> sizesForBoth = new List<int>() { 4, 10, 14, 18 };
            List<int> sizesForApproximation = new List<int> { 50, 74, 100, 150, 200 };

            Console.Write("Pattern3:");
            Dictionary<int, Result> results = Tester.Evaluate(sizesForBoth, sizesForApproximation, Reader.ReadPattern3);

            Writer.WriteExecutionTimesToFile("execution_times_pattern3_", results);
        }
    }
}

