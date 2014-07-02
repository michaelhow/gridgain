/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.portable.os;

import org.gridgain.client.marshaller.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.processors.*;
import org.gridgain.grid.kernal.processors.portable.*;
import org.gridgain.portable.*;
import org.jetbrains.annotations.*;

/**
 * No-op implementation of {@link GridPortableProcessor}.
 */
public class GridOsPortableProcessor extends GridProcessorAdapter implements GridPortableProcessor {
    /**
     * @param ctx Kernal context.
     */
    public GridOsPortableProcessor(GridKernalContext ctx) {
        super(ctx);
    }

    /** {@inheritDoc} */
    @Override public boolean isPortableEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    @Override public void configureClientMarshaller(GridClientMarshaller marsh) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public boolean isPortable(@Nullable Object obj) {
        return false;
    }

    /** {@inheritDoc} */
    @Override public String typeName(int typeId) {
        return null;
    }

    /** {@inheritDoc} */
    @Nullable @Override public <T> GridPortableObject<T> marshal(@Nullable T obj) throws GridPortableException {
        return null;
    }

    /** {@inheritDoc} */
    @Nullable @Override public <T> T unmarshal(byte[] arr) throws GridPortableException {
        return null;
    }
}