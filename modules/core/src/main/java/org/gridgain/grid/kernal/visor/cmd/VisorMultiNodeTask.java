/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.cmd;

import org.gridgain.grid.*;
import org.gridgain.grid.compute.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.util.*;
import org.jetbrains.annotations.*;

import static org.gridgain.grid.kernal.visor.cmd.VisorTaskUtils.*;

import java.util.*;

/**
 * Base class for Visor tasks intended to query data from a multiple node.
 *
 * @param <A> Task argument type.
 * @param <R> Task result type.
 */
public abstract class VisorMultiNodeTask<A, R, J> implements GridComputeTask<GridBiTuple<Set<UUID>, A>, R> {
    @GridInstanceResource
    protected GridEx g;

    /** Task argument. */
    protected A taskArg;

    protected long start;

    /**
     * @param arg Task arg.
     * @return New job.
     */
    protected abstract VisorJob<A, J> job(A arg);

    /** {@inheritDoc} */
    @Nullable @Override public Map<? extends GridComputeJob, GridNode> map(List<GridNode> subgrid,
        @Nullable GridBiTuple<Set<UUID>, A> arg) throws GridException {
        assert arg != null;
        assert arg.get1() != null;

        start = logStartTask(g.log(), getClass());

        Set<UUID> nodeIds = arg.get1();
        taskArg = arg.get2();

        Map<GridComputeJob, GridNode> map = new GridLeanMap<>(nodeIds.size());

        for (GridNode node : subgrid)
            if (nodeIds.contains(node.id()))
                map.put(job(taskArg), node);

        logTaskMapped(g.log(), getClass(), map.values());

        return map;
    }

    /** {@inheritDoc} */
    @Override public GridComputeJobResultPolicy result(GridComputeJobResult res,
        List<GridComputeJobResult> rcvd) throws GridException {
        // All Visor tasks should handle exceptions in reduce method.
        return GridComputeJobResultPolicy.WAIT;
    }

    /** {@inheritDoc} */
    @Nullable public abstract R reduce0(List<GridComputeJobResult> results) throws GridException;

    /** {@inheritDoc} */
    @Nullable @Override public final R reduce(List<GridComputeJobResult> results) throws GridException {
        logStartReduceTask(g.log(), getClass(), start);

        R result = reduce0(results);

        logFinishTask(g.log(), getClass(), start);

        return result;
    }
}
