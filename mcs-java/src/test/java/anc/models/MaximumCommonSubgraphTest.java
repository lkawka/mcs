package anc.models;

import static anc.TestData.CASE4;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class MaximumCommonSubgraphTest {
    
    @Test
    public void shouldReturnVerticesFromG1NotInMcs() {
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(newArrayList(new Pair(0, 1), new Pair(1, 2), new Pair(2, 4), new Pair(4, 0)), CASE4.g1, CASE4.g2);

        List<Integer> actual = mcs.verticesFromG1NotInMcs();

        assertThat(actual).containsOnly(3);
    }

    @Test
    public void shouldReturnVerticesFromG2NotInMcs() {
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(newArrayList(new Pair(0, 1), new Pair(1, 2), new Pair(2, 4), new Pair(4, 0)), CASE4.g1, CASE4.g2);

        List<Integer> actual = mcs.verticesFromG2NotInMcs();

        assertThat(actual).containsOnly(3, 5);
    }
}
