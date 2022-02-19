using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmpiricalTesting
{
    public class Writer
    {
        private static readonly string PATH = @"..\..\..\Results\";

        public static void WriteExecutionTimesToFile(string fileNamePrefix, Dictionary<int, Result> results)
        {
            string filename = $"{PATH}{fileNamePrefix}{new DateTimeOffset(DateTime.UtcNow).ToUnixTimeSeconds()}.csv";
            List<int> sizes = results.Keys.OrderBy(n => n).ToList();
            string[] lines = new string[3] { "", "", "" };

            // header
            lines[0] += "algorithm";
            for (int i = 0; i < sizes.Count; i++)
            {
                lines[0] += $",{sizes[i]}";
            }
            // exact 
            lines[1] += "Exact";
            for (int i = 0; i < sizes.Count; i++)
            {
                if (results[sizes[i]].Exact != null)
                {
                    lines[1] += $",{results[sizes[i]].Exact.ExecutionTime}";
                }
                else
                {
                    lines[1] += ",";
                }
            }
            // approximation
            lines[2] += "Approximation";
            for (int i = 0; i < sizes.Count; i++)
            {
                lines[2] += $",{results[sizes[i]].Approximation.ExecutionTime}";
            }

            using (StreamWriter sw = File.CreateText(filename))
            {
                for (int i = 0; i < 3; i++)
                {
                    sw.WriteLine(lines[i]);
                }
            }
            Console.WriteLine($"Results written to: {filename}");
        }
    }
}
