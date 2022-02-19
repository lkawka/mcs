using Mcs.Models;
using Mcs.Readers;

namespace EmpiricalTesting
{
    abstract class Reader
    {
        private static readonly string PATH = @"..\..\..\..\..\inputs\";

        public static Input ReadPattern1(int n)
        {
            string size = n < 5 ? "tiny" : (n < 15 ? "small" : (n < 25 ? "medium" : "big"));
            string fileName = $"smaller_graph_isomorphic_to_subgraph__pattern1__{size}_{n}x{2 * n - 1}.txt";
            return InputReader.Read(PATH + fileName);
        }

        public static Input ReadPattern2(int n)
        {
            string size = n < 5 ? "tiny" : (n < 15 ? "small" : (n < 30 ? "medium" : "big"));
            string fileName = $"isomorphic_graphs__pattern2__{size}_{n}x{n}.txt";
            return InputReader.Read(PATH + fileName);
        }

        public static Input ReadPattern3(int n)
        {
            string size = n < 4 ? "tiny" : (n < 14 ? "small" : (n < 24 ? "medium" : "big"));
            string fileName = $"nonisomorphic_graphs__pattern3__{size}_{n}x{n + 1}.txt";
            return InputReader.Read(PATH + fileName);
        }
    }
}
